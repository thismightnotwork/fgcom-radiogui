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
 * Fetches pilot position data from the SkyHigh Network OpenFSD JSON API.
 * Replaces SimConnect. Call start() to begin polling; stop() to halt.
 */
public class SkyHighFetcher {

    private static final String API_URL =
        "https://fsd.skyhighnetwork.co.uk/api/v1/data/openfsd-data.json";
    private static final int POLL_INTERVAL_MS = 5000; // 5 seconds

    private final String cid;
    private final PositionListener listener;
    private Timer timer;
    private boolean running = false;

    public interface PositionListener {
        /**
         * Called on the Swing EDT (via SwingUtilities.invokeLater) when a
         * matching pilot entry is found in the feed.
         *
         * @param lat      latitude in decimal degrees
         * @param lon      longitude in decimal degrees
         * @param alt      altitude in feet
         * @param callsign pilot callsign
         */
        void onPosition(double lat, double lon, double alt, String callsign);

        /** Called when the CID is not found in the current feed snapshot. */
        void onNotFound(String cid);

        /** Called when an error occurs fetching/parsing the feed. */
        void onError(String message);
    }

    /**
     * @param cid      the numeric CID (VATSIM-style) to look up in the feed
     * @param listener callback that receives position updates
     */
    public SkyHighFetcher(String cid, PositionListener listener) {
        this.cid      = cid.trim();
        this.listener = listener;
    }

    /** Start polling the API on a background thread. */
    public synchronized void start() {
        if (running) return;
        running = true;
        timer = new Timer("SkyHighFetcher", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetch();
            }
        }, 0, POLL_INTERVAL_MS);
    }

    /** Stop polling. */
    public synchronized void stop() {
        running = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void fetch() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                notifyError("HTTP " + responseCode + " from API");
                return;
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
            }

            parseAndNotify(sb.toString());

        } catch (Exception e) {
            notifyError("Fetch error: " + e.getMessage());
        }
    }

    /**
     * Parses the OpenFSD JSON feed and looks for the pilot matching {@code cid}.
     * Expected structure (simplified):
     * <pre>
     * {
     *   "pilots": [
     *     {
     *       "cid": 1234567,
     *       "callsign": "SHN123",
     *       "latitude": 51.1,
     *       "longitude": -0.2,
     *       "altitude": 35000
     *     }, ...
     *   ]
     * }
     * </pre>
     * Also handles a top-level array of pilot objects.
     */
    private void parseAndNotify(String json) {
        try {
            JSONArray pilots = null;

            // Try top-level object with "pilots" key first
            if (json.trim().startsWith("{")) {
                JSONObject root = new JSONObject(json);
                if (root.has("pilots")) {
                    pilots = root.getJSONArray("pilots");
                } else if (root.has("data") && root.getJSONObject("data").has("pilots")) {
                    pilots = root.getJSONObject("data").getJSONArray("pilots");
                }
            } else if (json.trim().startsWith("[")) {
                pilots = new JSONArray(json);
            }

            if (pilots == null) {
                notifyError("Unexpected JSON structure from API");
                return;
            }

            for (int i = 0; i < pilots.length(); i++) {
                JSONObject pilot = pilots.getJSONObject(i);

                // CID can be string or int in the feed
                String pilotCid = pilot.has("cid")
                    ? String.valueOf(pilot.get("cid")).trim()
                    : "";

                if (pilotCid.equals(cid)) {
                    final double lat      = pilot.getDouble("latitude");
                    final double lon      = pilot.getDouble("longitude");
                    final double alt      = pilot.optDouble("altitude", 0.0);
                    final String callsign = pilot.optString("callsign", "");

                    javax.swing.SwingUtilities.invokeLater(() ->
                        listener.onPosition(lat, lon, alt, callsign));
                    return;
                }
            }

            // Not found
            javax.swing.SwingUtilities.invokeLater(() -> listener.onNotFound(cid));

        } catch (Exception e) {
            notifyError("JSON parse error: " + e.getMessage());
        }
    }

    private void notifyError(String msg) {
        javax.swing.SwingUtilities.invokeLater(() -> listener.onError(msg));
    }
}
