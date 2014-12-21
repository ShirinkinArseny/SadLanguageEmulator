package ProcessorModeling.GUI;

import javax.swing.*;
import java.awt.event.*;

public class TooMuchCommands extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField cmdsCount;

    public enum Action {Stop, Continue}

    public Action getWhatToDo() {
        return whatToDo;
    }

    private Action whatToDo=Action.Stop;

    public TooMuchCommands(JFrame parent, int cmds) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Too much commands");
        getRootPane().setDefaultButton(buttonOK);
        setBounds(parent.getX(), parent.getY(), 400, 300);


        cmdsCount.setText(String.valueOf(cmds));

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }

    private void onOK() {
        whatToDo=Action.Stop;
        dispose();
    }

    private void onCancel() {
        whatToDo=Action.Continue;
        dispose();
    }
}
