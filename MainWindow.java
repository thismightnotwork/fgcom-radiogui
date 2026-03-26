package hbeni.fgcom_mumble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main application window for fgcom-radioGUI (SkyHigh Network edition).
 *
 * Changes from the original:
 *  - SimConnect removed entirely
 *  - CID input box added at the top
 *  - "Connect" button fetches pilot position from the SkyHigh OpenFSD JSON API
 *  - Position (lat/lon/alt) is fed into the UDPclient for fgcom-mumble
 */
public class MainWindow extends JFrame {

    // ── CID panel components ──────────────────────────────────────────────────
    private JTextField  cidField;
    private JButton     connectBtn;
    private JLabel      statusLabel;

    // ── Radio control panels ──────────────────────────────────────────────────
    private RadioInstance radio1;
    private RadioInstance radio2;

    // ── Position state ────────────────────────────────────────────────────────
    private double currentLat = 0.0;
    private double currentLon = 0.0;
    private double currentAlt = 0.0;
    private String currentCallsign = "";

    private SkyHighFetcher fetcher;

    // ── UDP ───────────────────────────────────────────────────────────────────
    private UDPclient udpClient;

    public MainWindow() {
        super("FGCom RadioGUI – SkyHigh Network");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(6, 6));

        add(buildTopPanel(),    BorderLayout.NORTH);
        add(buildRadioPanel(),  BorderLayout.CENTER);
        add(buildStatusBar(),   BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(480, 360));
        setLocationRelativeTo(null);

        // Start UDP client on default fgcom-mumble port
        udpClient = new UDPclient();
    }

    // ── Top panel: CID input ──────────────────────────────────────────────────
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Network Connection"));

        panel.add(new JLabel("CID:"));

        cidField = new JTextField(12);
        cidField.setToolTipText("Your SkyHigh Network CID (numeric)");
        panel.add(cidField);

        connectBtn = new JButton("Connect");
        connectBtn.addActionListener(e -> onConnectClicked());
        panel.add(connectBtn);

        JButton disconnectBtn = new JButton("Disconnect");
        disconnectBtn.addActionListener(e -> onDisconnectClicked());
        panel.add(disconnectBtn);

        return panel;
    }

    // ── Radio panels ──────────────────────────────────────────────────────────
    private JPanel buildRadioPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        radio1 = new RadioInstance("COM1", 1);
        radio2 = new RadioInstance("COM2", 2);

        panel.add(radio1);
        panel.add(radio2);
        return panel;
    }

    // ── Status bar ────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createEtchedBorder());
        statusLabel = new JLabel("  Not connected");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 11f));
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    // ── Button handlers ───────────────────────────────────────────────────────
    private void onConnectClicked() {
        String cid = cidField.getText().trim();
        if (cid.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your CID.", "CID Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Stop any existing fetcher
        if (fetcher != null) fetcher.stop();

        setStatus("Connecting for CID " + cid + "…", Color.ORANGE);
        connectBtn.setEnabled(false);

        fetcher = new SkyHighFetcher(cid, new SkyHighFetcher.PositionListener() {
            @Override
            public void onPosition(double lat, double lon, double alt, String callsign) {
                currentLat      = lat;
                currentLon      = lon;
                currentAlt      = alt;
                currentCallsign = callsign;

                setStatus(String.format(
                    "  %s  |  %.4f° / %.4f°  |  %.0f ft",
                    callsign, lat, lon, alt), new Color(0, 140, 0));

                // Push to fgcom-mumble via UDP
                sendPositionUDP();
            }

            @Override
            public void onNotFound(String cid) {
                setStatus("  CID " + cid + " not found on network", Color.RED);
            }

            @Override
            public void onError(String message) {
                setStatus("  Error: " + message, Color.RED);
                connectBtn.setEnabled(true);
            }
        });

        fetcher.start();
        connectBtn.setEnabled(true);
    }

    private void onDisconnectClicked() {
        if (fetcher != null) {
            fetcher.stop();
            fetcher = null;
        }
        setStatus("  Disconnected", Color.GRAY);
        connectBtn.setEnabled(true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void setStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    /**
     * Sends current position + radio state to the local fgcom-mumble UDP port.
     * The UDPclient builds the fgcom-mumble protocol packet.
     */
    private void sendPositionUDP() {
        if (udpClient == null) return;
        udpClient.sendPosition(
            currentCallsign, currentLat, currentLon, (int) currentAlt,
            radio1.getRadio(), radio2.getRadio());
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
