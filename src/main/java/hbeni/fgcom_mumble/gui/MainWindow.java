/* 
 * MainWindow — FGCom-mumble radioGUI (SkyHigh Network edition).
 * SimConnect menu item replaced with "Slave to SkyHigh" which prompts for
 * a CID and starts SkyHighFetcher to poll the OpenFSD JSON API.
 *
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble.gui;

import hbeni.fgcom_mumble.MapWindow;
import hbeni.fgcom_mumble.Radio;
import hbeni.fgcom_mumble.SkyHighFetcher;
import hbeni.fgcom_mumble.State;
import hbeni.fgcom_mumble.radioGUI;
import java.awt.Color;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main application window.
 */
public class MainWindow extends javax.swing.JFrame {

    private final About_Help    helpWindow;
    private final OptionsWindow optionsWindow;
    private final LicenseWindow licenseWindow;

    protected State state;
    JPanel radioContainer;

    // SkyHigh fetcher fields
    private SkyHighFetcher skyHighFetcher;
    private String         currentCID = "";

    public MainWindow(State st) {
        helpWindow    = new About_Help();
        optionsWindow = new OptionsWindow();
        licenseWindow = new LicenseWindow();
        state         = st;

        initComponents();

        URL iconURL = getClass().getResource("/fgcom_logo.png");
        if (iconURL != null) setIconImage(new ImageIcon(iconURL).getImage());

        radioContainer = new JPanel();
        radioContainer.setLayout(new BoxLayout(radioContainer, BoxLayout.PAGE_AXIS));

        updateFromState();

        state.getRadios().forEach(r -> radioContainer.add(new RadioInstance(r)));
        jScrollPanel_RadioPanel.setViewportView(radioContainer);
    }

    public void updateFromState() {
        jTextField_callsign.setText(state.getCallsign());
        jTextField_LAT.setText(Double.toString(state.getLatitutde()));
        jTextField_LON.setText(Double.toString(state.getLongitude()));
        jTextField_HGT.setText(Float.toString(state.getHeight()));
        jTextField_CID.setText(currentCID);

        for (int i = 0; i < radioContainer.getComponentCount(); i++) {
            ((RadioInstance) radioContainer.getComponent(i)).updateFromState();
        }
    }

    public void setInputElemetsEditable(boolean p) {
        jTextField_callsign.setEnabled(p);
        jTextField_LAT.setEnabled(p);
        jTextField_LON.setEnabled(p);
        jTextField_HGT.setEnabled(p);
        jTextField_CID.setEnabled(p);
        jButton_pickLocation.setEnabled(p);
        for (int i = 0; i < radioContainer.getComponentCount(); i++) {
            ((RadioInstance) radioContainer.getComponent(i)).setInputElemetsEditable(p);
        }
    }

    // ── Generated UI ──────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jToggleButton_Connect    = new javax.swing.JToggleButton();
        jLabel_ConnectionStatus  = new javax.swing.JLabel();
        jSeparator2              = new javax.swing.JSeparator();
        jScrollPanel_RadioPanel  = new javax.swing.JScrollPane();
        jLabel1                  = new javax.swing.JLabel();
        jTextField_callsign      = new javax.swing.JTextField();
        jLabel_CID               = new javax.swing.JLabel();
        jTextField_CID           = new javax.swing.JTextField();
        jLabel2                  = new javax.swing.JLabel();
        jLabel3                  = new javax.swing.JLabel();
        jLabel4                  = new javax.swing.JLabel();
        jTextField_LAT           = new javax.swing.JTextField();
        jTextField_LON           = new javax.swing.JTextField();
        jTextField_HGT           = new javax.swing.JTextField();
        jButton_pickLocation     = new javax.swing.JButton();
        jScrollPane_Statusbar    = new javax.swing.JScrollPane();
        jLabel_Statusbar         = new javax.swing.JTextField();
        MainMenu                 = new javax.swing.JMenuBar();
        jMenu1                   = new javax.swing.JMenu();
        jMenuItem_AddIdentity    = new javax.swing.JMenuItem();
        jMenuItem_rdf            = new javax.swing.JMenuItem();
        jMenuItem_SkyHigh        = new javax.swing.JMenuItem();
        jSeparator1              = new javax.swing.JPopupMenu.Separator();
        jMenuItem_options        = new javax.swing.JMenuItem();
        jMenuItem_quit           = new javax.swing.JMenuItem();
        jMenu_Help               = new javax.swing.JMenu();
        jMenuItem_Help_About     = new javax.swing.JMenuItem();
        jMenuItem_License        = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FGCom-mumble radioGUI – SkyHigh Network");

        jToggleButton_Connect.setText("Connect");
        jToggleButton_Connect.setToolTipText("Toggle UDP sending to mumble plugin");

        jLabel_ConnectionStatus.setBackground(new java.awt.Color(255, 255, 0));
        jLabel_ConnectionStatus.setText(" ");
        jLabel_ConnectionStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel_ConnectionStatus.setOpaque(true);

        jSeparator2.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator2.setMaximumSize(new java.awt.Dimension(3500, 1));

        jScrollPanel_RadioPanel.setAutoscrolls(true);
        jScrollPanel_RadioPanel.setMinimumSize(new java.awt.Dimension(24, 200));

        jLabel1.setText("Callsign:");
        jTextField_callsign.setText("ZZZZ");
        jTextField_callsign.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                state.setCallsign(jTextField_callsign.getText());
            }
        });

        // ── CID field ──────────────────────────────────────────────────────
        jLabel_CID.setText("CID:");
        jLabel_CID.setToolTipText("Your SkyHigh Network numeric CID");
        jTextField_CID.setToolTipText("Enter your SkyHigh Network CID then use RadioGUI → Slave to SkyHigh");
        jTextField_CID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                currentCID = jTextField_CID.getText().trim();
            }
        });

        jLabel2.setText("LAT:");
        jLabel3.setText("LON:");
        jLabel4.setText("HGT:");

        jTextField_LAT.setText("0.0");
        jTextField_LAT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try { state.setLatitude(Double.valueOf(jTextField_LAT.getText())); }
                catch (NumberFormatException e) { jTextField_LAT.setText(Double.toString(state.getLatitutde())); }
            }
        });
        jTextField_LON.setText("0.0");
        jTextField_LON.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try { state.setLongitude(Double.valueOf(jTextField_LON.getText())); }
                catch (NumberFormatException e) { jTextField_LON.setText(Double.toString(state.getLongitude())); }
            }
        });
        jTextField_HGT.setText("0");
        jTextField_HGT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try { state.setHeight(Float.valueOf(jTextField_HGT.getText())); }
                catch (NumberFormatException e) { jTextField_HGT.setText(Float.toString(state.getHeight())); }
            }
        });

        jButton_pickLocation.setText("pick location");
        jButton_pickLocation.addActionListener(e -> new MapWindow(state, this));

        jScrollPane_Statusbar.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jLabel_Statusbar.setEditable(false);
        jLabel_Statusbar.setFont(new java.awt.Font("Monospaced", 0, 10));
        jLabel_Statusbar.setText("initializing...");
        jScrollPane_Statusbar.setViewportView(jLabel_Statusbar);

        // ── Menu ───────────────────────────────────────────────────────────
        jMenu1.setText("RadioGUI");

        jMenuItem_AddIdentity.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem_AddIdentity.setText("Add new Radio");
        jMenuItem_AddIdentity.addActionListener(e -> {
            Radio r = new Radio();
            state.getRadios().add(r);
            radioContainer.add(new RadioInstance(r));
            radioContainer.revalidate();
        });
        jMenu1.add(jMenuItem_AddIdentity);

        jMenuItem_rdf.setText("Open RDF window");
        jMenuItem_rdf.addActionListener(e -> {
            // RDF window omitted in this build; show info
            JOptionPane.showMessageDialog(this,
                "RDF window is not included in this build.",
                "RDF", JOptionPane.INFORMATION_MESSAGE);
        });
        jMenu1.add(jMenuItem_rdf);

        jMenuItem_SkyHigh.setText("Slave to SkyHigh");
        jMenuItem_SkyHigh.setToolTipText("Fetch position from SkyHigh Network using your CID");
        jMenuItem_SkyHigh.addActionListener(e -> onSkyHighSlave());
        jMenu1.add(jMenuItem_SkyHigh);
        jMenu1.add(jSeparator1);

        jMenuItem_options.setText("Options");
        jMenuItem_options.addActionListener(e -> optionsWindow.setVisible(true));
        jMenu1.add(jMenuItem_options);

        jMenuItem_quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem_quit.setText("Quit");
        jMenuItem_quit.addActionListener(e -> this.dispose());
        jMenu1.add(jMenuItem_quit);

        MainMenu.add(jMenu1);

        jMenu_Help.setText("Help/About");
        jMenuItem_Help_About.setText("Help/About");
        jMenuItem_Help_About.addActionListener(e -> helpWindow.setVisible(true));
        jMenu_Help.add(jMenuItem_Help_About);
        jMenuItem_License.setText("License");
        jMenuItem_License.addActionListener(e -> licenseWindow.setVisible(true));
        jMenu_Help.add(jMenuItem_License);
        MainMenu.add(jMenu_Help);
        setJMenuBar(MainMenu);

        // ── Layout ─────────────────────────────────────────────────────────
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                // Row 1: Callsign | CID | pick location
                .addComponent(jLabel1)
                .addComponent(jTextField_callsign, 100, 140, 180)
                .addComponent(jLabel_CID)
                .addComponent(jTextField_CID, 80, 110, 150)
                .addComponent(jButton_pickLocation)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                // Row 2: LAT | LON | HGT
                .addComponent(jLabel2)
                .addComponent(jTextField_LAT, 80, 120, 180)
                .addComponent(jLabel3)
                .addComponent(jTextField_LON, 80, 120, 180)
                .addComponent(jLabel4)
                .addComponent(jTextField_HGT, 60, 80, 100)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPanel_RadioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToggleButton_Connect)
                .addComponent(jLabel_ConnectionStatus, 13, 13, 13)
                .addComponent(jScrollPane_Statusbar))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jTextField_callsign)
                .addComponent(jLabel_CID)
                .addComponent(jTextField_CID)
                .addComponent(jButton_pickLocation))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jTextField_LAT)
                .addComponent(jLabel3)
                .addComponent(jTextField_LON)
                .addComponent(jLabel4)
                .addComponent(jTextField_HGT))
            .addComponent(jScrollPanel_RadioPanel, 200, 300, Short.MAX_VALUE)
            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jToggleButton_Connect)
                .addComponent(jLabel_ConnectionStatus, 13, 13, 13)
                .addComponent(jScrollPane_Statusbar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    // ── SkyHigh slaving ───────────────────────────────────────────────────────
    private void onSkyHighSlave() {
        // Use CID already typed in the CID field, or prompt
        String cid = jTextField_CID.getText().trim();
        if (cid.isEmpty()) {
            cid = JOptionPane.showInputDialog(this,
                "Enter your SkyHigh Network CID:",
                "Slave to SkyHigh", JOptionPane.PLAIN_MESSAGE);
            if (cid == null || cid.trim().isEmpty()) return;
            cid = cid.trim();
            jTextField_CID.setText(cid);
        }
        currentCID = cid;

        // Stop any existing fetcher
        if (skyHighFetcher != null) skyHighFetcher.stop();

        radioGUI.enableSkyHigh();
        setInputElemetsEditable(false);
        jMenuItem_SkyHigh.setEnabled(false);
        jMenuItem_AddIdentity.setEnabled(false);

        final String finalCid = cid;
        skyHighFetcher = new SkyHighFetcher(finalCid, new SkyHighFetcher.PositionListener() {
            @Override
            public void onPosition(double lat, double lon, double alt, String callsign) {
                state.setCallsign(callsign.isEmpty() ? finalCid : callsign);
                state.setLocation(lat, lon, (float) alt);
                updateFromState();
            }
            @Override
            public void onNotFound(String cid) {
                setStatusText("[SkyHigh] CID " + cid + " not found on network");
            }
            @Override
            public void onError(String message) {
                setStatusText("[SkyHigh] Error: " + message);
            }
        });
        skyHighFetcher.start();
    }

    // ── Public API ────────────────────────────────────────────────────────────
    public boolean getConnectionActivation() { return jToggleButton_Connect.isSelected(); }
    public void    setConnectionActivation(boolean p) { jToggleButton_Connect.setSelected(p); }

    public void setConnectionState(boolean ok) {
        jLabel_ConnectionStatus.setBackground(ok ? Color.green : Color.red);
    }

    public void setStatusText(String txt) { jLabel_Statusbar.setText(txt); }

    // ── Variable declarations ─────────────────────────────────────────────────
    private javax.swing.JMenuBar                 MainMenu;
    private javax.swing.JButton                  jButton_pickLocation;
    private javax.swing.JLabel                   jLabel1;
    private javax.swing.JLabel                   jLabel2;
    private javax.swing.JLabel                   jLabel3;
    private javax.swing.JLabel                   jLabel4;
    private javax.swing.JLabel                   jLabel_CID;
    private javax.swing.JLabel                   jLabel_ConnectionStatus;
    private javax.swing.JTextField               jLabel_Statusbar;
    private javax.swing.JMenu                    jMenu1;
    private javax.swing.JMenuItem                jMenuItem_AddIdentity;
    private javax.swing.JMenuItem                jMenuItem_Help_About;
    private javax.swing.JMenuItem                jMenuItem_License;
    private javax.swing.JMenuItem                jMenuItem_SkyHigh;
    private javax.swing.JMenuItem                jMenuItem_options;
    private javax.swing.JMenuItem                jMenuItem_quit;
    private javax.swing.JMenuItem                jMenuItem_rdf;
    private javax.swing.JMenu                    jMenu_Help;
    private javax.swing.JScrollPane              jScrollPane_Statusbar;
    private javax.swing.JScrollPane              jScrollPanel_RadioPanel;
    private javax.swing.JPopupMenu.Separator     jSeparator1;
    private javax.swing.JSeparator               jSeparator2;
    private javax.swing.JTextField               jTextField_CID;
    private javax.swing.JTextField               jTextField_HGT;
    private javax.swing.JTextField               jTextField_LAT;
    private javax.swing.JTextField               jTextField_LON;
    private javax.swing.JTextField               jTextField_callsign;
    private javax.swing.JToggleButton            jToggleButton_Connect;
    private javax.swing.JMenuItem                jMenuItem2;
}
