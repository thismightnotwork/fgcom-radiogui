/*
 * About_Help dialog.
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble.gui;

import javax.swing.*;
import java.awt.*;

public class About_Help extends JDialog {

    public About_Help() {
        super((Frame) null, "Help / About", false);
        JTextArea ta = new JTextArea(
            "FGCom-mumble radioGUI – SkyHigh Network Edition\n\n" +
            "This build replaces SimConnect with the SkyHigh Network\n" +
            "OpenFSD JSON API to fetch pilot position.\n\n" +
            "Usage:\n" +
            "  1. Enter your SkyHigh Network CID in the CID field.\n" +
            "  2. Choose RadioGUI → Slave to SkyHigh.\n" +
            "  3. Press Connect to start sending UDP to fgcom-mumble.\n\n" +
            "Original project: https://github.com/hbeni/fgcom-mumble\n" +
            "SkyHigh API: https://fsd.skyhighnetwork.co.uk/api/v1/data/openfsd-data.json"
        );
        ta.setEditable(false);
        ta.setMargin(new Insets(8, 10, 8, 10));
        add(new JScrollPane(ta));
        setSize(480, 280);
        setLocationRelativeTo(null);
    }
}
