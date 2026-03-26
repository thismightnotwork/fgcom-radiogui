/*
 * LicenseWindow dialog.
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble.gui;

import javax.swing.*;
import java.awt.*;

public class LicenseWindow extends JDialog {

    public LicenseWindow() {
        super((Frame) null, "License", false);
        JTextArea ta = new JTextArea(
            "FGCom-mumble radioGUI is distributed under the\n" +
            "GNU General Public License v3.0.\n\n" +
            "For the full license text see:\n" +
            "https://www.gnu.org/licenses/gpl-3.0.en.html\n\n" +
            "Original copyright (c) 2020 Benedikt Hallinger."
        );
        ta.setEditable(false);
        ta.setMargin(new Insets(8, 10, 8, 10));
        add(new JScrollPane(ta));
        setSize(400, 200);
        setLocationRelativeTo(null);
    }
}
