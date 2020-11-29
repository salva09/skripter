import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Home extends JFrame {
    private JPanel mainPane;
    private JTextArea console;
    private JToolBar infoBar;
    private JScrollPane editorScrollPane;
    private JLabel runningLabel;
    private JLabel languageLabel;

    public Home() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPane);
        this.setSize(700, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        FlatLightLaf.install();

        RSyntaxTextArea editorPane = new RSyntaxTextArea();
        editorScrollPane = new RTextScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        createMenuBar();
    }

    private void createMenuBar() {
        var menuBar = new JMenuBar();
        var file = new JMenu("File");

        var newFile = new JMenuItem("New");
        file.add(newFile);
        var openFile = new JMenuItem("Open");
        file.add(openFile);
        var saveFile = new JMenuItem("Save");
        file.add(saveFile);
        var saveAsFile = new JMenuItem("Save as");
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
