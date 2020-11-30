import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import syntax.Theme;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Home extends JFrame {
    private JPanel mainPane;
    private JTextArea console;
    private JToolBar infoBar;
    private RSyntaxTextArea editorPane;
    private JScrollPane editorScrollPane;
    private JButton runButton;
    private JLabel runningLabel;
    private JLabel languageLabel;
    private JLabel caretLabel;
    private HomeManager manager;
    private Theme theme;

    public Home(Theme theme) {
        this.theme = theme;
        this.setTitle("Skripter");
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
        runningLabel.setIcon(new ImageIcon(getClass().getResource("icons/loading.gif")));
    }

    public void setGoodLabel() {
        runningLabel.setText("Exit code: 0");
        runningLabel.setIcon(new ImageIcon(getClass().getResource("icons/good.png")));
    }

    public void setBadLabel(int exitCode) {
        runningLabel.setText("Non-zero exit code: " + exitCode);
        runningLabel.setIcon(new ImageIcon(getClass().getResource("icons/bad.png")));
    }

    public void setLanguageLabel(Language language) {
        this.languageLabel.setText(language.getName());
        this.languageLabel.setIcon(new ImageIcon(getClass().getResource(language.getIcon())));
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
        createLabels();
    }

    private void createEditorPane() {
        editorPane = new RSyntaxTextArea();
        this.theme.setTextAreaTheme(editorPane);
        editorScrollPane = new RTextScrollPane(editorPane);
        this.theme.setScrollPaneTheme((RTextScrollPane) editorScrollPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        caretLabel = new JLabel();
        editorPane.addCaretListener(l -> {
            var caretPosition = " | " + editorPane.getCaretLineNumber() + ":" + editorPane.getCaretOffsetFromLineStart();
            caretLabel.setText(caretPosition);
        });
    }

    private void createConsole() {
        console = new JTextArea();
        console.setEditable(false);
    }

    private void createMenuBar() {
        var menuBar = new JMenuBar();
        var file = new JMenu("File");

        var newFile = new JMenuItem("New");
        newFile.addActionListener(e -> manager.newFile());
        file.add(newFile);
        var openFile = new JMenuItem("Open");
        openFile.addActionListener(e -> manager.openFile());
        file.add(openFile);
        var saveFile = new JMenuItem("Save");
        saveFile.addActionListener(e -> manager.saveFile());
        file.add(saveFile);
        var saveAsFile = new JMenuItem("Save as");
        saveAsFile.addActionListener(e -> manager.saveFilesAs());
        file.add(saveAsFile);
        var exit = new JMenuItem("Exit");
        exit.addActionListener(e -> manager.closeFile());
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
        var clearConsole = new JMenuItem("Clear console");
        clearConsole.addActionListener(e -> clearConsole());
        console.add(clearConsole);
        menuBar.add(console);

        var help = new JMenu("Help");

        var what = new JMenuItem("What can I do");
        what.addActionListener(e -> showWhatCanIDo());
        help.add(what);
        var source = new JMenuItem("Source");
        source.addActionListener(e -> showSource());
        help.add(source);
        var about = new JMenuItem("About");
        about.addActionListener(e -> showAbout());
        help.add(about);

        menuBar.add(help);

        menuBar.add(Box.createHorizontalGlue());

        runButton = new JButton(new ImageIcon(getClass().getResource("icons/play.png")));
        runButton.setBorder(null);
        runButton.setFocusPainted(false);
        runButton.addActionListener(e -> manager.runScript());
        menuBar.add(runButton);

        this.setJMenuBar(menuBar);
    }

    private void showWhatCanIDo() {
        var options = new String[]{"Thanks!"};
        var message = "<html>" +
                "<h1>Here is a list of what you can do!</h1>" +
                "<ul>" +
                "<li>" +
                "<h3>Open and edit scripts</h3><br>" +
                "<p>You can use the file menu to open existing files in your computer and<br>" +
                "edit them using the built.in editor. You can open any type of file.</p>" +
                "</li>" +
                "<li>" +
                "<h3>Run a script and see its output</h3><br>" +
                "<p>If you has a supported script, you can run it using the green button at<br>" +
                "the top left corner, and si its output in the console below.<br>" +
                "<h5>Currently only Kotlin, Swift and Python scripts are supported.</h5></p>" +
                "</li>" +
                "</ul>" +
                "</html>";
        var title = "Getting help";
        JOptionPane.showOptionDialog(
                this,
                message,
                title,
                JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
        );
    }

    private void showAbout() {
        var message = "<html>" +
                "<h1>Skripter</h1><br>" +
                "<p>A GUI tool that allows users to enter<br>" +
                "a script, execute it, and see its output side-by-side</p>" +
                "<h5>Written with love in Kotlin and Java</h5>" +
                "</html>";
        var title = "About";
        var icon = new ImageIcon(getClass().getResource("icons/script.png"));
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.OK_OPTION,
                icon
        );
    }

    private void showSource() {
        var options = new String[]{"Okay", "Go to Github"};
        var message = "<html>" +
                "<p>This project is free and open source under the MIT License<br>" +
                "and hosted on Github</p>" +
                "</html>";
        var title = "Source code";
        var icon = new ImageIcon(getClass().getResource("icons/github.png"));
        var result = JOptionPane.showOptionDialog(
                this,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                icon,
                options,
                null
        );
        try {
            if (result == 1)
                Desktop.getDesktop().browse(new URI("https://github.com/salva09/skripter"));
        } catch (URISyntaxException | IOException ignored) {}
    }

    private void createLabels() {
        runningLabel = new JLabel();
        languageLabel = new JLabel();
    }

    private JMenuItem createMenuItem(Action action) {
        JMenuItem item = new JMenuItem(action);
        item.setToolTipText(null);
        return item;
    }
}
