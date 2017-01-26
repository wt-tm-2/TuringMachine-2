/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import karel.application.properties.KarelApplicationProperties;
import karel.controller.KarelController;
import karel.interpreter.InterpreterException;
import karel.world.parser.WorldParserException;

/**
 *
 * @author ladypomaster
 */
public class KarelView extends javax.swing.JFrame {
    
    private karel.view.TextEditorFrame editorPane;

    public static final boolean DEBUG = true; //Set to false to turn off debug statements
    public static final int CODE_TAB_INDEX = 0;
    public static final int WORLD_TAB_INDEX = 1;
    public static final Font DEFAULT_FONT = new Font("Tahoma", Font.PLAIN, 14);
    public static final Font WINDOWS_FONT = new Font("Courier New", Font.BOLD, 14);
    public static final Font LINUX_FONT = new Font("DejaVu Sans Mono", Font.BOLD, 14);
    public static final Font MAC_FONT = new Font("Dialog", Font.BOLD, 14);
    private static final String defaultRobot = "robot 1 1 n 0"; //Default robot location
    
    private final KarelController controller;
    private KarelApplicationProperties applicationProperties; 
    private String workingDirectory = System.getProperty("user.dir");
    private File sourceFile;
    private File worldFile;
    private int selectedTabIndex = CODE_TAB_INDEX;
    
    private ButtonGroup zoomButtonGroup = new ButtonGroup();
    private JFontChooser fontChooser = new JFontChooser();
    private Font selectedFont = DEFAULT_FONT;
    
    /**
     * Creates new form KarelView
     * @param controller
     */
    public KarelView(final KarelController controller) {
        
        this.controller = controller;
    
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                controller.disposeKarelView();
                KarelView.this.joinEditor();
            }
        };
        
        this.addWindowListener(exitListener);

        
        initComponents();
        
        startButton.setEnabled(false);
        stepButton.setEnabled(false);
        stopButton.setEnabled(false);
        resetButton.setEnabled(false);
     
        codeEditor.setTabbedPane(rightTabbedPane);
        worldEditor.setTabbedPane(rightTabbedPane);
        
        codeEditor.setTabIndex(CODE_TAB_INDEX);
        worldEditor.setTabIndex(WORLD_TAB_INDEX);
        
        codeEditor.setSaveMenuItem(saveMenuItem);
        worldEditor.setSaveMenuItem(saveMenuItem);
        
        worldEditor.setText(defaultRobot); //Implemented so the GUI world builder wouldn't throw errors when initially 
        //implementing walls
        
        createZoomButtonGroup();
        addLanguageReference();
        addWorldReference();
        
        undoMenuItem.setAction(codeEditor.getUndoAction());
        redoMenuItem.setAction(codeEditor.getRedoAction());
        
        applicationProperties = new KarelApplicationProperties();
        saveMenuItem.setEnabled(true);
        
        this.setSize(new Dimension(1000, 750));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void createZoomButtonGroup() {
        
        zoomButtonGroup.add(zoom100Button);
        zoomButtonGroup.add(zoom80Button);
        zoomButtonGroup.add(zoom60Button);
        
        ActionListener zoomListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == zoom100Button) {
                    world.setCellSize(50);
                }
                else if (e.getSource() == zoom80Button){
                    world.setCellSize(40);
                }
                else {
                    world.setCellSize(30);
                }
            }  
        };
        
        zoom100Button.addActionListener(zoomListener);
        zoom80Button.addActionListener(zoomListener);
        zoom60Button.addActionListener(zoomListener);
    }
    
    private void addLanguageReference() {
        addHtmlReference(languageReferenceScrollPane, languageReferencePane, "/html/languageReference.html");
    }
    
    private void addWorldReference() {
        addHtmlReference(worldReferenceScrollPane, worldReferencePane, "/html/worldReference.html");
    }
    
    private void addHtmlReference(JScrollPane editorScrollPane, JEditorPane referencePane, String htmlPath) {
        referencePane.setContentType("text/html");
        InputStream inputStream =  KarelView.class.getResourceAsStream(htmlPath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.contains("<!-- image=")) {
                    int imageNameStart = line.indexOf('\"') + 1;
                    int imageNameEnd = line.indexOf('\"', imageNameStart);
                    String imageName = line.substring(imageNameStart, imageNameEnd);
                    URL imagePath = getClass().getClassLoader().getResource("karel/images/" + imageName);
                    line = "<p><img src=\"" + imagePath + "\"/></p>";
                }
                sb.append(line);
            }
            referencePane.setText(sb.toString());
        } catch (IOException ex) {
            referencePane.setText("Could not load language reference.");
        }
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(KarelView.class.getName()).log(Level.SEVERE, null, ex);
        }
        URL test = getClass().getClassLoader().getResource("karel/images/beepers.gif");
        referencePane.setEditable(false);
    }
   
    private void setWorkingDirectoryFromLastInvocation() {
        workingDirectory = applicationProperties.getLastUsedDirectory();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftToRightSplitPane = new javax.swing.JSplitPane();
        leftTabbedPane = new javax.swing.JTabbedPane();
        worldPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        controlPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        stepButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        speedLabel = new javax.swing.JLabel();
        speedList = new javax.swing.JComboBox();
        loadButton = new javax.swing.JButton();
        world = new karel.world.World();
        SplitButton = new javax.swing.JToggleButton();
        languageReferenceScrollPane = new javax.swing.JScrollPane();
        languageReferencePane = new javax.swing.JEditorPane();
        worldReferenceScrollPane = new javax.swing.JScrollPane();
        worldReferencePane = new javax.swing.JEditorPane();
        rightTabbedPane = new javax.swing.JTabbedPane();
        codeEditor = new karel.view.TextEditor();
        worldEditor = new karel.view.TextEditor();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newSourceMenuItem = new javax.swing.JMenuItem();
        newWorldMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        openSourceFileMenuItem = new javax.swing.JMenuItem();
        openWorldFileMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        formatMenu = new javax.swing.JMenu();
        zoomMenu = new javax.swing.JMenu();
        zoom100Button = new javax.swing.JRadioButtonMenuItem();
        zoom80Button = new javax.swing.JRadioButtonMenuItem();
        zoom60Button = new javax.swing.JRadioButtonMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Karel the Robot");

        leftToRightSplitPane.setDividerLocation(650);
        leftToRightSplitPane.setResizeWeight(0.6);

        leftTabbedPane.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        worldPanel.setPreferredSize(new java.awt.Dimension(682, 792));

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        statusLabel.setText("Waiting for world input...");

        controlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Control Panel", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 12))); // NOI18N
        controlPanel.setMinimumSize(new java.awt.Dimension(500, 50));
        controlPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        startButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        controlPanel.add(startButton);

        stepButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        stepButton.setText("Step");
        stepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepButtonActionPerformed(evt);
            }
        });
        controlPanel.add(stepButton);

        stopButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        controlPanel.add(stopButton);

        resetButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        controlPanel.add(resetButton);

        speedLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        speedLabel.setText("Speed:");
        controlPanel.add(speedLabel);

        speedList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        speedList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Instant", "1000%", "400%", "200%", "100%", "50%", "25%" }));
        speedList.setSelectedIndex(4);
        speedList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedListActionPerformed(evt);
            }
        });
        controlPanel.add(speedList);

        loadButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        loadButton.setText("Load World");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        controlPanel.add(loadButton);

        world.setMinimumSize(new java.awt.Dimension(0, 0));
        world.setPreferredSize(new java.awt.Dimension(600, 600));
        world.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                worldMousePressed(evt);
            }
        });

        SplitButton.setText("Split");
        SplitButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SplitButtonItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout worldPanelLayout = new javax.swing.GroupLayout(worldPanel);
        worldPanel.setLayout(worldPanelLayout);
        worldPanelLayout.setHorizontalGroup(
            worldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(worldPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(worldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(worldPanelLayout.createSequentialGroup()
                        .addComponent(statusLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(worldPanelLayout.createSequentialGroup()
                        .addGroup(worldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(world, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(worldPanelLayout.createSequentialGroup()
                                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(SplitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)))
                        .addContainerGap())))
        );
        worldPanelLayout.setVerticalGroup(
            worldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, worldPanelLayout.createSequentialGroup()
                .addGroup(worldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(worldPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(worldPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(SplitButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(world, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                .addContainerGap())
        );

        leftTabbedPane.addTab("The World", worldPanel);

        languageReferenceScrollPane.setViewportView(languageReferencePane);

        leftTabbedPane.addTab("Language Reference", languageReferenceScrollPane);

        worldReferenceScrollPane.setViewportView(worldReferencePane);

        leftTabbedPane.addTab("World Reference", worldReferenceScrollPane);

        leftToRightSplitPane.setLeftComponent(leftTabbedPane);

        rightTabbedPane.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rightTabbedPane.setPreferredSize(new java.awt.Dimension(200, 792));
        rightTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rightTabbedPaneStateChanged(evt);
            }
        });

        codeEditor.setPreferredSize(new java.awt.Dimension(300, 700));
        rightTabbedPane.addTab("*Code editor", codeEditor);
        rightTabbedPane.addTab("*World editor", worldEditor);

        leftToRightSplitPane.setRightComponent(rightTabbedPane);

        getContentPane().add(leftToRightSplitPane, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        newSourceMenuItem.setText("New Source File...");
        newSourceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSourceMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newSourceMenuItem);

        newWorldMenuItem.setText("New World File...");
        newWorldMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWorldMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newWorldMenuItem);
        fileMenu.add(jSeparator1);

        openSourceFileMenuItem.setText("Open Source File...");
        openSourceFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSourceFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openSourceFileMenuItem);

        openWorldFileMenuItem.setText("Open World File...");
        openWorldFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openWorldFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openWorldFileMenuItem);
        fileMenu.add(jSeparator2);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Save ");
        saveMenuItem.setEnabled(false);
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save as...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        undoMenuItem.setText("Undo...");
        editMenu.add(undoMenuItem);

        redoMenuItem.setText("Redo...");
        editMenu.add(redoMenuItem);

        menuBar.add(editMenu);

        formatMenu.setLabel("Font Chooser");
        formatMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                formatMenuMenuSelected(evt);
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });
        menuBar.add(formatMenu);

        zoomMenu.setText("Zoom");

        zoom100Button.setSelected(true);
        zoom100Button.setText("100%");
        zoomMenu.add(zoom100Button);

        zoom80Button.setSelected(true);
        zoom80Button.setText("80%");
        zoomMenu.add(zoom80Button);

        zoom60Button.setSelected(true);
        zoom60Button.setText("60%");
        zoomMenu.add(zoom60Button);

        menuBar.add(zoomMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if (startButton.getText().equals("Start")) {

            try {
                controller.startSimulation(codeEditor.getText());
            } catch (InterpreterException ex) {
                String message = ex.getMessage();
                codeEditor.highlightLine(ex.getLineNum(), TextEditor.ERROR_COLOR);
                setStatusMessage(message);
                showDialog(message);
                rightTabbedPane.setEnabled(true);
                codeEditor.setEditable(true);
            }
        }
        else {
            controller.runSimulation();
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
         try {
            controller.stepSimulation(codeEditor.getText());
        } catch (InterpreterException ex) {
            String message = ex.getMessage();
            codeEditor.highlightLine(ex.getLineNum(), TextEditor.ERROR_COLOR);
            setStatusMessage(message);
            showDialog(message);
            rightTabbedPane.setEnabled(true);
            codeEditor.setEditable(true);
        }
    }//GEN-LAST:event_stepButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        controller.stopSimulation();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        controller.resetSimulation();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void speedListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedListActionPerformed
        controller.setSimulationSpeed(getSpeed());
    }//GEN-LAST:event_speedListActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        JFileChooser fc = new JFileChooser(workingDirectory);
        int returnVal;
        String content;
        String tabTitle = rightTabbedPane.getTitleAt(selectedTabIndex);
        
        if (selectedTabIndex == CODE_TAB_INDEX) {
            content = codeEditor.getText();
            if (sourceFile != null) {
                controller.saveFile(sourceFile, content);
                rightTabbedPane.setTitleAt(selectedTabIndex, tabTitle.replace("*", ""));
                saveMenuItem.setEnabled(false);
            }
            else {
                FileFilter karelFilter = new FileNameExtensionFilter("Karel source (.karel)","karel");
                fc.addChoosableFileFilter(karelFilter);
                fc.setFileFilter(karelFilter);
                fc.setSelectedFile(new File("untitled.karel"));
                returnVal = fc.showSaveDialog(this);
                
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    setNewWorkingDirectory(fc.getCurrentDirectory());
                    sourceFile = fc.getSelectedFile();
                    controller.saveFile(sourceFile, content);
                    rightTabbedPane.setTitleAt(selectedTabIndex, tabTitle.replace("*", ""));
                    saveMenuItem.setEnabled(false);
                }
            }
        }
        else {
            content = worldEditor.getText();
            if (worldFile != null) {
                controller.saveFile(worldFile, content);
                rightTabbedPane.setTitleAt(selectedTabIndex, tabTitle.replace("*", ""));
                saveMenuItem.setEnabled(false);
            }
            else {
                FileFilter worldFilter = new FileNameExtensionFilter("Karel world (.world)","world");
                fc.addChoosableFileFilter(worldFilter);
                fc.setFileFilter(worldFilter);
                fc.setSelectedFile(new File("untitled.world"));
                returnVal = fc.showSaveDialog(this);
                
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (!(fc.getSelectedFile().getName().contains(".")) || !fc.getSelectedFile().getName().substring(fc.getSelectedFile().getName().lastIndexOf(".")).equals(".world")){
                        String oldName = fc.getSelectedFile().getAbsolutePath() + ".world";
                        fc.setSelectedFile(new File(oldName));
                    }
                    setNewWorkingDirectory(fc.getCurrentDirectory());
                    worldFile = fc.getSelectedFile();
                    controller.saveFile(worldFile, content);
                    rightTabbedPane.setTitleAt(selectedTabIndex, tabTitle.replace("*", ""));
                    saveMenuItem.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void openSourceFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSourceFileMenuItemActionPerformed
        JFileChooser fc = new JFileChooser(workingDirectory);
        FileFilter karelFilter = new FileNameExtensionFilter("Karel source (.karel)","karel");
        fc.addChoosableFileFilter(karelFilter);
        fc.setFileFilter(karelFilter);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!(fc.getSelectedFile().getName().contains(".")) || !fc.getSelectedFile().getName().substring(fc.getSelectedFile().getName().lastIndexOf(".")).equals(".karel")){
                    String oldName = fc.getSelectedFile().getAbsolutePath() + ".karel";
                    fc.setSelectedFile(new File(oldName));
            }
            setNewWorkingDirectory(fc.getCurrentDirectory());
            sourceFile = fc.getSelectedFile();
            selectedTabIndex = CODE_TAB_INDEX;
            TextEditor textEditor = (TextEditor)rightTabbedPane.getComponent(selectedTabIndex);
            textEditor.setText(controller.openFile(sourceFile));
            rightTabbedPane.setSelectedIndex(selectedTabIndex);
        }
    }//GEN-LAST:event_openSourceFileMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        
        JFileChooser fc = new JFileChooser(workingDirectory);
        selectedTabIndex = rightTabbedPane.getSelectedIndex();
        int returnVal;
        
         if (selectedTabIndex == CODE_TAB_INDEX) {
             
            FileFilter karelFilter = new FileNameExtensionFilter("Karel source (.karel)","karel");
            fc.addChoosableFileFilter(karelFilter);
            fc.setFileFilter(karelFilter);
            fc.setSelectedFile(new File("untitled.karel"));
            returnVal = fc.showSaveDialog(this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (!(fc.getSelectedFile().getName().contains(".")) || !fc.getSelectedFile().getName().substring(fc.getSelectedFile().getName().lastIndexOf(".")).equals(".karel")){
                    String oldName = fc.getSelectedFile().getAbsolutePath() + ".karel";
                    fc.setSelectedFile(new File(oldName));
                }
                setNewWorkingDirectory(fc.getCurrentDirectory());
                sourceFile = fc.getSelectedFile();
                controller.saveFile(sourceFile, codeEditor.getText());
                saveMenuItem.setEnabled(false);
                String tabTitle = rightTabbedPane.getTitleAt(selectedTabIndex);
                rightTabbedPane.setTitleAt(selectedTabIndex, tabTitle.replace("*", ""));
            }
        }
        else {
            FileFilter worldFilter = new FileNameExtensionFilter("Karel world (.world)","world");
            fc.addChoosableFileFilter(worldFilter);
            fc.setFileFilter(worldFilter);
            fc.setSelectedFile(new File("untitled.world"));
            returnVal = fc.showSaveDialog(this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (!(fc.getSelectedFile().getName().contains(".")) || !fc.getSelectedFile().getName().substring(fc.getSelectedFile().getName().lastIndexOf(".")).equals(".world")){
                    String oldName = fc.getSelectedFile().getAbsolutePath() + ".world";
                    fc.setSelectedFile(new File(oldName));
                }
                setNewWorkingDirectory(fc.getCurrentDirectory());
                worldFile = fc.getSelectedFile();
                controller.saveFile(worldFile, worldEditor.getText());
                saveMenuItem.setEnabled(false);
                String tabTitle = rightTabbedPane.getTitleAt(selectedTabIndex);
                rightTabbedPane.setTitleAt(selectedTabIndex, tabTitle.replace("*", ""));
            }
        }
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void openWorldFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openWorldFileMenuItemActionPerformed
        JFileChooser fc = new JFileChooser(workingDirectory);
        FileFilter worldFilter = new FileNameExtensionFilter("Karel world (.world)","world");
        fc.addChoosableFileFilter(worldFilter);
        fc.setFileFilter(worldFilter);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            setNewWorkingDirectory(fc.getCurrentDirectory());
            worldFile = fc.getSelectedFile();
            selectedTabIndex = WORLD_TAB_INDEX;
            TextEditor textEditor = (TextEditor)rightTabbedPane.getComponent(selectedTabIndex);
            textEditor.setText(controller.openFile(worldFile));
            rightTabbedPane.setSelectedIndex(selectedTabIndex);
            loadWorld();
        }
    }//GEN-LAST:event_openWorldFileMenuItemActionPerformed
    
    private void setNewWorkingDirectory(File currentDirectory) {
        workingDirectory = currentDirectory.getAbsolutePath();
        applicationProperties.setLastUsedDirectory(workingDirectory);
    }
    
    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        loadWorld();
    }//GEN-LAST:event_loadButtonActionPerformed

    /** Switches the save and undo/redo context to the selected editor.
     * @param evt 
     */
    private void rightTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rightTabbedPaneStateChanged

        selectedTabIndex = rightTabbedPane.getSelectedIndex();
        String tabTitle = rightTabbedPane.getTitleAt(selectedTabIndex);
        if (tabTitle.startsWith("*")) {
            saveMenuItem.setEnabled(true);
        }
        else {
            saveMenuItem.setEnabled(false);
        }
        if (selectedTabIndex == CODE_TAB_INDEX) {
            undoMenuItem.setAction(codeEditor.getUndoAction());
            redoMenuItem.setAction(codeEditor.getRedoAction());
        }
        else {
            undoMenuItem.setAction(worldEditor.getUndoAction());
            redoMenuItem.setAction(worldEditor.getRedoAction());
        }
        
    }//GEN-LAST:event_rightTabbedPaneStateChanged

    private void newSourceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSourceMenuItemActionPerformed
        int option = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to create a new source file?");
        
        if (option == JOptionPane.YES_OPTION) {
            rightTabbedPane.setSelectedIndex(CODE_TAB_INDEX);
            sourceFile = null;
            codeEditor.setText("");
            rightTabbedPane.setTitleAt(CODE_TAB_INDEX, "*Code editor");
            saveMenuItem.setEnabled(true);
        }
    }//GEN-LAST:event_newSourceMenuItemActionPerformed

    private void newWorldMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWorldMenuItemActionPerformed
        int option = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to create a new world file?");
        
        if (option == JOptionPane.YES_OPTION) {
            rightTabbedPane.setSelectedIndex(WORLD_TAB_INDEX);
            worldFile = null;
            worldEditor.setText("");
            rightTabbedPane.setTitleAt(WORLD_TAB_INDEX, "*World editor");
            saveMenuItem.setEnabled(true);
        }
    }//GEN-LAST:event_newWorldMenuItemActionPerformed

    private void worldMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_worldMousePressed
    if (world.gridWasClicked()) {
        loadWorld();
        world.setGridClicked(false);
        }
    }//GEN-LAST:event_worldMousePressed
    /** Splits the editor into a TextEditorFrame and also functions as the join.
     * @param evt 
     */
    
    private void SplitButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SplitButtonItemStateChanged
       if (SplitButton.isSelected()){
           menuBar.setVisible(false);
           this.editorPane =  new karel.view.TextEditorFrame(rightTabbedPane,menuBar, this);
           this.editorPane.validate();
           this.editorPane.setVisible(true);
           menuBar.setVisible(true);
           SplitButton.setText("Join");
        }
        else {
           leftTabbedPane.setPreferredSize(new java.awt.Dimension(690,829));
           rightTabbedPane.setPreferredSize(new java.awt.Dimension(300,700));
           menuBar.setVisible(false);
           leftToRightSplitPane.setRightComponent(rightTabbedPane);
           setJMenuBar(menuBar);
           menuBar.setVisible(true);
           this.editorPane.dispose();
           SplitButton.setText("Split");
        }
    }//GEN-LAST:event_SplitButtonItemStateChanged

    protected void joinEditor() {
           leftTabbedPane.setPreferredSize(new java.awt.Dimension(690,829));
           rightTabbedPane.setPreferredSize(new java.awt.Dimension(300,700));
           menuBar.setVisible(false);
           leftToRightSplitPane.setRightComponent(rightTabbedPane);
           setJMenuBar(menuBar);
           menuBar.setVisible(true);
           if (this.editorPane != null) {
                this.editorPane.dispose();
           }
           SplitButton.setText("Split");
           SplitButton.setSelected(false);
    }
    
    private void formatMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_formatMenuMenuSelected
        fontChooser.setSelectedFont(selectedFont);
        int value = fontChooser.showDialog(this);
       
        if (value == JFontChooser.OK_OPTION) {
            selectedFont = fontChooser.getSelectedFont();
            worldEditor.setTextPaneFont(selectedFont);
            codeEditor.setTextPaneFont(selectedFont);
        }
    }//GEN-LAST:event_formatMenuMenuSelected

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        final String aboutMessage = "New Karel the Robot features added by team Karel-2\n\n" +
                "The Karel-2 Team:\n\n" + "\tH. Paul Haiduk - Project Director\n" +
                "\tAnthony Thornton - Team Member\n" + "\tZachary Gutierrez - Team Member\n" + 
                "\tMichael Johnson - Team Member\n";
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                    JOptionPane.showMessageDialog(KarelView.this, aboutMessage, 
                            "About Karel the Robot", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton SplitButton;
    private javax.swing.JMenuItem aboutMenuItem;
    private karel.view.TextEditor codeEditor;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu formatMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JEditorPane languageReferencePane;
    private javax.swing.JScrollPane languageReferenceScrollPane;
    private javax.swing.JTabbedPane leftTabbedPane;
    private javax.swing.JSplitPane leftToRightSplitPane;
    private javax.swing.JButton loadButton;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newSourceMenuItem;
    private javax.swing.JMenuItem newWorldMenuItem;
    private javax.swing.JMenuItem openSourceFileMenuItem;
    private javax.swing.JMenuItem openWorldFileMenuItem;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JButton resetButton;
    private javax.swing.JTabbedPane rightTabbedPane;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JComboBox speedList;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton stepButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JMenuItem undoMenuItem;
    private karel.world.World world;
    public static karel.view.TextEditor worldEditor;
    private javax.swing.JPanel worldPanel;
    private javax.swing.JEditorPane worldReferencePane;
    private javax.swing.JScrollPane worldReferenceScrollPane;
    private javax.swing.JRadioButtonMenuItem zoom100Button;
    private javax.swing.JRadioButtonMenuItem zoom60Button;
    private javax.swing.JRadioButtonMenuItem zoom80Button;
    private javax.swing.JMenu zoomMenu;
    // End of variables declaration//GEN-END:variables
    public void setStatusMessage(String msg) {
        statusLabel.setText("<html>" + msg.replaceAll("\n", "<br>") + "</html>");
    }

    public void disableStopButton() {
        stopButton.setEnabled(false);
    }

    public void disableStepButton() {
        stepButton.setEnabled(false);
    }

    public void disableStartButton() {
        startButton.setEnabled(false);
    }

    public void showDialog(final String msg) {

        // create JOptionPane on Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                    JOptionPane.showMessageDialog(KarelView.this, msg);
            }
        });
    }
    
    public void highlightLine(int tabIndex, int lineNumber, Color color) {
        if (tabIndex == CODE_TAB_INDEX) {
            codeEditor.highlightLine(lineNumber, color);
        }
        else {
            worldEditor.highlightLine(lineNumber, color);
        }
    }
    
    public void setStartState() {
        startButton.setText("Run");
        
        setRunState();
    }
    
    public void setRunState() {
        rightTabbedPane.setEnabled(false);
        codeEditor.setEditable(false);
        startButton.setEnabled(false);
        stepButton.setEnabled(false);
        stopButton.setEnabled(true);
        resetButton.setEnabled(true);
        loadButton.setEnabled(false);
        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        
        rightTabbedPane.setSelectedComponent(codeEditor);
        selectedTabIndex = CODE_TAB_INDEX;
    }
    
    public void setStepState() {
        rightTabbedPane.setEnabled(false);
        codeEditor.setEditable(false);
        startButton.setText("Run");
        stopButton.setEnabled(false);
        resetButton.setEnabled(true);
        loadButton.setEnabled(false);
        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        
        rightTabbedPane.setSelectedComponent(codeEditor);
        selectedTabIndex = CODE_TAB_INDEX;
    }
    
    public void setStoppedState() {
        startButton.setEnabled(true);
        stepButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    /** Resets all controls to their starting properties. */
    public void reset() {
            
        loadWorld();
        
        rightTabbedPane.setEnabled(true);
        codeEditor.setEditable(true);
        
        codeEditor.resetHighlighter();
        worldEditor.resetHighlighter();

        startButton.setText("Start");
        startButton.setEnabled(true);
        stepButton.setEnabled(true);
        stopButton.setEnabled(false);
        resetButton.setEnabled(false);
        loadButton.setEnabled(true);
        fileMenu.setEnabled(true);
        editMenu.setEnabled(true);
    }

    public int getSpeed() {

        switch((String) speedList.getSelectedItem()) {
        case "Instant":
                return 0;
        case "1000%":
                return 15;
        case "400%":
                return 37;
        case "200%":
                return 75;
        case "100%":
                return 150;
        case "50%":
                return 300;
        case "25%":
                return 600;
        default:
                return 150;
        }
    }
    
    public karel.world.World getWorld() {
        return world;
    }
    
    private void loadWorld() {
        try {
            worldEditor.lowerCase();
            controller.loadWorld(world, worldEditor.getText());
            startButton.setEnabled(true);
            stepButton.setEnabled(true);
            setStatusMessage(world.getRobotStatus() + "\nInstruction count: 0"
                    + "\nConditional count: 0");
        } catch (WorldParserException ex) {
            String message = ex.getMessage();
            if (ex.getLineNum() != -1) {
                worldEditor.highlightLine(ex.getLineNum(), TextEditor.ERROR_COLOR);
            }
            setStatusMessage(message);
            showDialog(message);
            rightTabbedPane.setSelectedIndex(WORLD_TAB_INDEX);
            startButton.setEnabled(false);
            stepButton.setEnabled(false);
        }
    }
    
    public static void guiAddWall(String wall) {
        if (worldEditor.contains(wall)) {
            worldEditor.delete("\n" + wall);
        } else {
            worldEditor.setText(worldEditor.getText() + "\n" + wall);
        }
    }
    
    public static void guiAddBeeper(String beeper) {
        if (worldEditor.contains(beeper)) {
            worldEditor.delete("\n" + beeper);
        } else {
            worldEditor.setText(worldEditor.getText() + "\n" + beeper);
        }
    }
}
