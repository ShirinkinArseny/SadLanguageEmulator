package ProcessorModeling.GUI;

import javax.swing.*;

public class ReadIntDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JSpinner spinner1;
    private int value;

    public ReadIntDialog(JFrame parent) {
        super(parent, "Read int", true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        setBounds(parent.getX(), parent.getY(), 160, 100);
        setVisible(true);
    }

    private void onOK() {
        value=Integer.valueOf(String.valueOf(spinner1.getValue()));
        dispose();
    }

    public int getValue() {
        return value;
    }
}
