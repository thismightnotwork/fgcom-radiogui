/* 
 * This file is part of the FGCom-mumble distribution (https://github.com/hbeni/fgcom-mumble).
 * Copyright (c) 2020 Benedikt Hallinger
 * Modified for SkyHigh Network: SimConnect removed, SkyHighFetcher added.
 *
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble;

import com.formdev.flatlaf.FlatDarkLaf;
import hbeni.fgcom_mumble.UDPclient.SendRes;
import hbeni.fgcom_mumble.gui.MainWindow;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * FGCom-mumble radio GUI client (SkyHigh Network edition).
 * SimConnect has been removed; position is fetched from the SkyHigh OpenFSD
 * JSON API via {@link SkyHighFetcher}.
 */
public class radioGUI {

    public static class Options {
        public static String  udpHost                    = "localhost";
        public static int     udpPort                    = 16661;
        public static float   udpSendRateHz              = 10;
        public static int     debugSignalOverride        = -5;
        public static boolean enableAudioEffecs          = true;
        public static boolean allowHearingNonPluginUsers = false;
        public static boolean alwaysMumblePTT            = false;
    }

    protected static State      state;
    protected static UDPclient  udpClient;
    public    static MainWindow mainWindow;

    public static void main(String[] args) throws InterruptedException {

        FlatDarkLaf.install();
        try {
            javax.swing.UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ignored) {}

        state = new State();
        state.setCallsign("ZZZZ");
        state.setLocation(48.3440238, 11.7650830, 6.6f);
        state.getRadios().add(new Radio("121.000"));
        state.getRadios().add(new Radio("122.540"));

        mainWindow = new MainWindow(state);
        mainWindow.setVisible(true);

        udpClient = new UDPclient(state);

        while (mainWindow.isVisible()) {
            udpClient.setActive(mainWindow.getConnectionActivation());
            udpClient.prepare();
            SendRes result = udpClient.send();

            if (result.res) {
                mainWindow.setConnectionState(true);
            } else {
                mainWindow.setConnectionState(false);
                if (result.msg.equals("")) result.msg = "not connected";
            }

            if (!state.statusmessage.equals(""))
                result.msg = state.statusmessage + " / " + result.msg;

            if (state.isSkyHighSlave())
                result.msg = "[SkyHigh] " + result.msg;

            mainWindow.setStatusText(result.msg);
            Thread.sleep((long) (1000 / Options.udpSendRateHz));
        }
    }

    public static State     getState()     { return state;     }
    public static UDPclient getUDPClient() { return udpClient; }

    /**
     * Remove a radio from the state, broadcasting a deletion packet first.
     */
    public static void deregisterRadio(Radio r) {
        r.setFrequency("<del>");
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        state.radios.remove(r);
        mainWindow.updateFromState();
    }

    /**
     * Prepare GUI and state for SkyHigh slaving (called from MainWindow).
     */
    public static void enableSkyHigh() {
        udpClient.setActive(false);
        try {
            for (Radio r : state.getRadios()) deregisterRadio(r);
        } catch (Exception e) {
            state.statusmessage = "INTERNAL ERROR – restart RadioGUI!";
        }
        state.getRadios().clear();
        state.setCallsign("SkyHigh");
        state.setLocation(0, 0, 0f);
        state.getRadios().add(new Radio("121.000"));
        state.getRadios().add(new Radio("122.540"));
        state.setSkyHighSlaving(true);
        mainWindow.updateFromState();
    }
}
