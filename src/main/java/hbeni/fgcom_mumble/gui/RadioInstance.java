/* 
 * RadioInstance — panel for one COM radio.
 * Pulled from hbeni/fgcom-mumble and retained as-is.
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble.gui;

import hbeni.fgcom_mumble.Radio;
import hbeni.fgcom_mumble.radioGUI;
import javax.swing.*;
import java.awt.*;

/**
 * A Swing panel representing a single radio (COM1 / COM2 etc.).
 * Provides frequency, PTT, volume, power, squelch controls.
 */
public class RadioInstance extends javax.swing.JPanel {

    private Radio radio;
    private boolean closable = true;

    // UI components
    private JLabel         jLabel_freq;
    private JTextField     jTextField_freq;
    private JToggleButton  jToggleButton_ptt;
    private JSlider        jSlider_vol;
    private JCheckBox      jCheckBox_pwr;
    private JButton        jButton_close;
    private JLabel         jLabel_vol;

    public RadioInstance(Radio r) {
        this.radio = r;
        initComponents();
        updateFromState();
    }

    private void initComponents() {
        jLabel_freq       = new JLabel("Freq:");
        jTextField_freq   = new JTextField(9);
        jToggleButton_ptt = new JToggleButton("PTT");
        jLabel_vol        = new JLabel("Vol:");
        jSlider_vol       = new JSlider(0, 100, 100);
        jCheckBox_pwr     = new JCheckBox("PWR", true);
        jButton_close     = new JButton("✕");

        jTextField_freq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                radio.setFrequency(jTextField_freq.getText());
            }
        });
        jToggleButton_ptt.addChangeListener(e ->
            radio.setPTT(jToggleButton_ptt.isSelected()));
        jSlider_vol.addChangeListener(e ->
            radio.setVolume(jSlider_vol.getValue() / 100.0f));
        jCheckBox_pwr.addChangeListener(e ->
            radio.setPwrBtn(jCheckBox_pwr.isSelected()));
        jButton_close.addActionListener(e -> {
            if (closable) radioGUI.deregisterRadio(radio);
        });

        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 2));
        setBorder(BorderFactory.createEtchedBorder());
        add(jLabel_freq);
        add(jTextField_freq);
        add(jToggleButton_ptt);
        add(jLabel_vol);
        add(jSlider_vol);
        add(jCheckBox_pwr);
        add(jButton_close);
    }

    public void updateFromState() {
        jTextField_freq.setText(radio.getFrequency());
        jToggleButton_ptt.setSelected(radio.getPTT());
        jSlider_vol.setValue((int)(radio.getVolume() * 100));
        jCheckBox_pwr.setSelected(radio.getPwrBtn());
    }

    public void setInputElemetsEditable(boolean p) {
        jTextField_freq.setEnabled(p);
        jSlider_vol.setEnabled(p);
        jCheckBox_pwr.setEnabled(p);
    }

    public void setClosable(boolean c) {
        this.closable = c;
        jButton_close.setEnabled(c);
    }

    public void dispose() { setVisible(false); }

    public Radio getRadio() { return radio; }
}
