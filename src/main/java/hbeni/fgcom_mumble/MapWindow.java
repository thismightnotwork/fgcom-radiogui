package hbeni.fgcom_mumble;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * Simple manual location entry dialog (no map library required).
 */
public class MapWindow extends JDialog {

    public MapWindow(State state, JFrame parent) {
        super(parent, "Set Location", true);

        JTextField latField = new JTextField(Double.toString(state.getLatitutde()), 12);
        JTextField lonField = new JTextField(Double.toString(state.getLongitude()), 12);
        JTextField hgtField = new JTextField(Float.toString(state.getHeight()), 8);

        JPanel fields = new JPanel(new GridLayout(3, 2, 6, 6));
        fields.add(new JLabel("Latitude:",  SwingConstants.RIGHT));
        fields.add(latField);
        fields.add(new JLabel("Longitude:", SwingConstants.RIGHT));
        fields.add(lonField);
        fields.add(new JLabel("Height (ft):", SwingConstants.RIGHT));
        fields.add(hgtField);

        JButton ok     = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> {
            try { state.setLatitude(Double.parseDouble(latField.getText().trim())); } catch (NumberFormatException ignored) {}
            try { state.setLongitude(Double.parseDouble(lonField.getText().trim())); } catch (NumberFormatException ignored) {}
            try { state.setHeight(Float.parseFloat(hgtField.getText().trim())); } catch (NumberFormatException ignored) {}
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel);
        buttons.add(ok);

        setLayout(new BorderLayout(10, 10));
        add(fields,  BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
