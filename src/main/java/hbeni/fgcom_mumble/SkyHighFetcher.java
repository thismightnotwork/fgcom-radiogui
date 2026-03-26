/* 
 * SkyHighFetcher — replaces SimConnect for FGCom-mumble radioGUI.
 * Polls the SkyHigh Network OpenFSD JSON API to obtain pilot position.
 *
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Polls https://fsd.skyhighnetwork.co.uk/api/v1/data/openfsd-data.json every
 * {@value #POLL_INTERVAL_MS} ms and fires callbacks on the Swing EDT.
 *
 * JSON structure observed:
 * <pre>
 * {
 *   "general": { ... },
 *   "pilots": [
 *     { "cid": 1234567, "callsign": "SHN123",
 *       "latitude": 51.1, "longitude": -0.2, "altitude": 35000 }
 *   ],
 *   "controllers": [ ... ]
 * }
 * </pre>
 */
public class SkyHighFetcher {

    public static final String API_URL =
        "https://fsd.skyhighnetwork.co.uk/api/v1/data/openfsd-data.json";
    private static final int POLL_INTERVAL_MS = 5000;

    private final String          cid;
    private final PositionListener listener;
    private Timer   timer;
    private boolean running = false;

    // ── Callback interface ────────────────────────────────────────────────────
    public interface PositionListener {
        /** Pilot found; values delivered on Swing EDT. */
        void onPosition(double lat, double lon, double alt, String callsign);
        /** CID not present in current snapshot. */
        void onNotFound(String cid);
        /** Network / parse error. */
        void onError(String message);
    }

    public SkyHighFetcher(String cid, PositionListener listener) {
        this.cid      = cid.trim();
        this.listener = listener;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        timer   = new Timer("SkyHighFetcher", /*daemon=*/true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() { fetch(); }
        }, 0, POLL_INTERVAL_MS);
    }

    public synchronized void stop() {
        running = false;
        if (timer != null) { timer.cancel(); timer = null; }
    }

    // ── Internal ──────────────────────────────────────────────────────────────
    private void fetch() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                notifyError("HTTP " + conn.getResponseCode());
                return;
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }
            parseAndNotify(sb.toString());

        } catch (Exception e) {
            notifyError("Fetch error: " + e.getMessage());
        }
    }

    private void parseAndNotify(String json) {
        try {
            JSONArray pilots = null;
            String trimmed = json.trim();
            if (trimmed.startsWith("{")) {
                JSONObject root = new JSONObject(trimmed);
                if (root.has("pilots")) {
                    pilots = root.getJSONArray("pilots");
                } else if (root.has("data")) {
                    JSONObject data = root.getJSONObject("data");
                    if (data.has("pilots")) pilots = data.getJSONArray("pilots");
                }
            } else if (trimmed.startsWith("[")) {
                pilots = new JSONArray(trimmed);
            }

            if (pilots == null) { notifyError("Unexpected JSON structure"); return; }

            for (int i = 0; i < pilots.length(); i++) {
                JSONObject p = pilots.getJSONObject(i);
                String pilotCid = p.has("cid") ? String.valueOf(p.get("cid")).trim() : "";
                if (pilotCid.equals(cid)) {
                    final double lat      = p.getDouble("latitude");
                    final double lon      = p.getDouble("longitude");
                    final double alt      = p.optDouble("altitude", 0.0);
                    final String callsign = p.optString("callsign", "");
                    javax.swing.SwingUtilities.invokeLater(() ->
                        listener.onPosition(lat, lon, alt, callsign));
                    return;
                }
            }
            javax.swing.SwingUtilities.invokeLater(() -> listener.onNotFound(cid));

        } catch (Exception e) {
            notifyError("JSON parse error: " + e.getMessage());
        }
    }

    private void notifyError(String msg) {
        javax.swing.SwingUtilities.invokeLater(() -> listener.onError(msg));
    }
}
