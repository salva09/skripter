import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import syntax.Theme;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Home extends JFrame {
    private JPanel mainPane;
    private JTextArea console;
    private JToolBar infoBar;
    private RSyntaxTextArea editorPane;
    private JScrollPane editorScrollPane;
    private JLabel runningLabel;
    private JLabel languageLabel;
    private HomeManager manager;
    private Theme theme;

    public Home(Theme theme) {
        this.theme = theme;
        this.setContentPane(mainPane);
        this.setSize(700, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.closeFile();
            }
        });
    }

    public void setIdleLabel() {
        runningLabel.setText("Idle");
        runningLabel.setIcon(null);
    }

    public void setRunningLabel() {
        runningLabel.setText("Running");
        // runningLabel.setIcon(new ImageIcon(getClass().getResource("/icons/loading.gif")));
    }

    public void setGoodLabel() {
        runningLabel.setText("Exit code: 0");
        // runningLabel.setIcon(new ImageIcon(getClass().getResource("/icons/good.png")));
    }

    public void setBadLabel(int exitCode) {
        runningLabel.setText("Non-zero exit code: " + exitCode);
        // runningLabel.setIcon(new ImageIcon(getClass().getResource("/icons/bad.png")));
    }

    public void clearConsole() {
        manager.cleanConsole();
    }

    private void createUIComponents() {
        this.theme.setFrameLookAndFeel(this);
        createEditorPane();
        createConsole();
        manager = new HomeManager(this, editorPane, console);
        createMenuBar();
    }

    private void createEditorPane() {
        editorPane = new RSyntaxTextArea();
        this.theme.setTextAreaTheme(editorPane);
        editorScrollPane = new RTextScrollPane(editorPane);
        this.theme.setScrollPaneTheme((RTextScrollPane) editorScrollPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void createConsole() {
        console = new JTextArea();
        console.setEditable(false);
    }

    private void createMenuBar() {
        var menuBar = new JMenuBar();
        var file = new JMenu("File");

        var newFile = new JMenuItem("New");
        newFile.addActionListener(e -> {
            manager.newFile();
        });
        file.add(newFile);
        var openFile = new JMenuItem("Open");
        openFile.addActionListener(e -> {
            manager.openFile();
        });
        file.add(openFile);
        var saveFile = new JMenuItem("Save");
        saveFile.addActionListener(e -> {
            manager.saveFile();
        });
        file.add(saveFile);
        var saveAsFile = new JMenuItem("Save as");
        saveAsFile.addActionListener(e -> {
            manager.saveFilesAs();
        });
        file.add(saveAsFile);
        var exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            manager.closeFile();
        });
        file.add(exit);

        menuBar.add(file);

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.UNDO_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.REDO_ACTION)));
        editMenu.addSeparator();
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.CUT_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.COPY_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.PASTE_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.DELETE_ACTION)));
        editMenu.addSeparator();
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION)));
        menuBar.add(editMenu);

        var console = new JMenu("Console");
        console.addActionListener(e -> {
            clearConsole();
        });
        menuBar.add(console);

        menuBar.add(Box.createHorizontalGlue());

        var runButton = new JButton("Run");
        runButton.setBorder(null);
        runButton.setFocusPainted(false);
        runButton.addActionListener(e -> {
            manager.runScript();
        });
        menuBar.add(runButton);

        this.setJMenuBar(menuBar);
    }

    private JMenuItem createMenuItem(Action action) {
        JMenuItem item = new JMenuItem(action);
        item.setToolTipText(null);
        return item;
    }
}
