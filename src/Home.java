import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Home extends JFrame {
    private JPanel mainPane;
    private JTextArea console;
    private JToolBar infoBar;
    private RSyntaxTextArea editorPane;
    private JScrollPane editorScrollPane;
    private JLabel runningLabel;
    private JLabel languageLabel;
    private HomeManager manager;

    public Home() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPane);
        this.setSize(700, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void setIdleLabel() {
        runningLabel.setText("Idle");
        runningLabel.setIcon(null);
    }

    public void setRunningLabel() {
        runningLabel.setText("Running");
        runningLabel.setIcon(new ImageIcon(getClass().getResource("/icons/loading.gif")));
    }

    public void setGoodLabel() {
        runningLabel.setText("Exit code: 0");
        runningLabel.setIcon(new ImageIcon(getClass().getResource("/icons/good.png")));
    }

    public void setBadLabel(int exitCode) {
        runningLabel.setText("Non-zero exit code: " + exitCode);
        runningLabel.setIcon(new ImageIcon(getClass().getResource("/icons/bad.png")));
    }

    public void clearConsole() {
        manager.cleanConsole();
    }

    private void createUIComponents() {
        FlatLightLaf.install();
        createEditorPane();
        createConsole();
        manager = new HomeManager(this, editorPane, console);
        createMenuBar();
    }

    private void createEditorPane() {
        editorPane = new RSyntaxTextArea();
        editorScrollPane = new RTextScrollPane(editorPane);
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
            System.exit(0);
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

        this.setJMenuBar(menuBar);
    }

    private JMenuItem createMenuItem(Action action) {
        JMenuItem item = new JMenuItem(action);
        item.setToolTipText(null);
        return item;
    }
}
