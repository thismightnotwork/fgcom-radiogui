/*
 * OptionsWindow — simple options dialog.
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble.gui;

import hbeni.fgcom_mumble.radioGUI;
import javax.swing.*;
import java.awt.*;

public class OptionsWindow extends JDialog {

    private JTextField tfHost, tfPort, tfRate;
    private JCheckBox  cbAudioFX, cbHearAll, cbAlwaysPTT;

    public OptionsWindow() {
        super((Frame) null, "Options", false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets  = new Insets(4, 6, 4, 6);
        c.anchor  = GridBagConstraints.WEST;

        tfHost = new JTextField(radioGUI.Options.udpHost, 14);
        tfPort = new JTextField(Integer.toString(radioGUI.Options.udpPort), 6);
        tfRate = new JTextField(Float.toString(radioGUI.Options.udpSendRateHz), 6);
        cbAudioFX   = new JCheckBox("Enable audio FX",             radioGUI.Options.enableAudioEffecs);
        cbHearAll   = new JCheckBox("Hear non-plugin users",        radioGUI.Options.allowHearingNonPluginUsers);
        cbAlwaysPTT = new JCheckBox("Always use Mumble PTT",        radioGUI.Options.alwaysMumblePTT);

        c.gridx=0; c.gridy=0; add(new JLabel("UDP Host:"), c);
        c.gridx=1;             add(tfHost, c);
        c.gridx=0; c.gridy=1; add(new JLabel("UDP Port:"), c);
        c.gridx=1;             add(tfPort, c);
        c.gridx=0; c.gridy=2; add(new JLabel("Send rate (Hz):"), c);
        c.gridx=1;             add(tfRate, c);
        c.gridx=0; c.gridy=3; c.gridwidth=2; add(cbAudioFX,   c);
        c.gridy=4;             add(cbHearAll,   c);
        c.gridy=5;             add(cbAlwaysPTT, c);

        JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            radioGUI.Options.udpHost = tfHost.getText().trim();
            try { radioGUI.Options.udpPort = Integer.parseInt(tfPort.getText().trim()); } catch (NumberFormatException ignored) {}
            try { radioGUI.Options.udpSendRateHz = Float.parseFloat(tfRate.getText().trim()); } catch (NumberFormatException ignored) {}
            radioGUI.Options.enableAudioEffecs          = cbAudioFX.isSelected();
            radioGUI.Options.allowHearingNonPluginUsers = cbHearAll.isSelected();
            radioGUI.Options.alwaysMumblePTT            = cbAlwaysPTT.isSelected();
            setVisible(false);
        });
        c.gridx=0; c.gridy=6; c.gridwidth=2; c.anchor=GridBagConstraints.CENTER;
        add(ok, c);

        pack();
        setLocationRelativeTo(null);
    }

    public void prepareSimConnect() { /* no-op: SimConnect removed */ }
}
