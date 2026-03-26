/* 
 * Placeholder MapWindow — retained so existing imports compile.
 * The full map-picker implementation requires the jmapviewer dependency.
 * Install jmapviewer locally before building.
 *
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Simple location picker dialog (map functionality requires jmapviewer).
 */
public class MapWindow extends JDialog {

    public MapWindow(State state, javax.swing.JFrame parent) {
        super(parent, "Pick Location", false);
        add(new JLabel("Map picker requires jmapviewer library.", SwingConstants.CENTER));
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
