package ProcessorModeling.GUI;

import ProcessorModeling.Engine.Data;
import ProcessorModeling.Engine.IO;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class ModellingUI extends JFrame {
    private JEditorPane source;
    private JTable table;
    private JPanel mainPane;
    private JButton stepButton;
    private JEditorPane outputPane;
    private JScrollPane sourceScroll;
    private JButton fastButton;
    private JButton slowButton;
    private JPanel Source;
    private JPanel Controls;
    private JPanel Output;
    private JPanel Registers;
    private JScrollPane tablePane;
    private JButton showManualButton;
    private JButton saveButton;
    private JButton loadButton;

    private Data data;
    private MyTabel tableModel = new MyTabel();

    public static void writeLines(File file, String data) throws Exception {
        BufferedWriter bw=new BufferedWriter(new FileWriter(file));
        bw.write(data);
        bw.close();
    }

    public static String readLines(File file) throws Exception {
        String Lines = "";
        BufferedReader input;
        input = new BufferedReader(new FileReader(file));
        String line;
        while ((line = input.readLine()) != null) {
            Lines+=line+"\n";
        }
        input.close();
        return Lines;
    }

    private void tryCompile() {
        outputPane.setText("");
        try {
            data = new Data(source.getText(), new IO() {
                @Override
                public int readInt() {
                    ReadIntDialog r = new ReadIntDialog(ModellingUI.this);
                    return r.getValue();
                }

                @Override
                public void printChar(int a) {
                    outputPane.setText(outputPane.getText() + (char) a);
                }

                @Override
                public void printInt(int a) {
                    outputPane.setText(outputPane.getText() + a);
                }
            });
            table.repaint();
            tablePane.repaint();
            data.start();
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHighline() {
        String text = source.getText();
        if (text != null) {
            Highlighter hilit = new DefaultHighlighter();
            source.setHighlighter(hilit);
            Highlighter.HighlightPainter comments = new DefaultHighlighter.DefaultHighlightPainter(
                    new Color(138, 230, 94));
            Highlighter.HighlightPainter numbers = new DefaultHighlighter.DefaultHighlightPainter(
                    new Color(144, 189, 255));
            Highlighter.HighlightPainter defs = new DefaultHighlighter.DefaultHighlightPainter(
                    new Color(255, 149, 78));
            Highlighter.HighlightPainter mark = new DefaultHighlighter.DefaultHighlightPainter(
                    new Color(255, 0, 237));

            String textCopy=text;
            int offset=0;
            int index=textCopy.indexOf("mark");
            while (index!=-1) {
                try {
                    hilit.addHighlight(offset+index, offset+index+4, mark);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                textCopy=textCopy.substring(index+4);
                offset+=index+4;
                index=textCopy.indexOf("mark");
            }

            boolean commented = false;
            boolean defined = false;
            boolean wasSpace = true;
            int commentStart = 0;
            int defineStart = 0;
            for (int i = 0; i < text.length(); i++) {


                if (text.charAt(i) >= '0' && text.charAt(i) <= '9' && !commented && !defined && wasSpace) {
                    try {
                        hilit.addHighlight(i, i + 1, numbers);
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }

                    if (text.charAt(i) == ';') {
                        commented = true;
                        commentStart = i;
                    }

                if (text.charAt(i) == '#') {
                    defined = true;
                    defineStart = i;
                }

                if (text.charAt(i) == '\n') {

                    if (commented) {
                        commented = false;
                        try {
                            hilit.addHighlight(commentStart, i, comments);
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }

                    if (defined) {
                        defined = false;
                        try {
                            hilit.addHighlight(defineStart, i, defs);
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                wasSpace = text.charAt(i) == ' ' || text.charAt(i) == '\t' || text.charAt(i) == '\n';
            }
            if (commented) {
                try {
                    hilit.addHighlight(commentStart, text.length(), comments);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    public ModellingUI() {

        super("CPU modelling");


        setContentPane(mainPane);

        sourceScroll.setMinimumSize(new Dimension(300, 100));
        table.setModel(tableModel);
        tableModel.setSize(Data.size);



        source.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateHighline();
            }
        });

        fastButton.addActionListener(e -> {
            tryCompile();
            if (data != null) {
                while (!data.getIsDone()) {
                    data.makeStep();
                    for (int i = 0; i < Data.size; i++)
                        tableModel.pushValue(i, data.get(i));
                    table.repaint();
                    tablePane.repaint();
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Not compiled yet", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        String man=
                "SYNTAX:\n"+
                "COMMAND ARG1 ARG2 //COMMENT\n"+
                "\n"+
                "COMMANDS:\n"+
                "0 - writeConst(register, value)\n" +
                "1 - copy(register from, register to)\n" +
                "2 - sum (register, addition register)\n" +
                "3 - subtract (register, subtraction register)\n" +
                "4 - multiply (register, multiplying register)\n" +
                "5 - divide (register, divider register)\n" +
                "6 - equals0 (register for answer, comparing register)\n" +
                "7 - and (register for answer, addition register)\n" +
                "8 - or (register for answer, addition register)\n" +
                "9 - xor (register for answer, addition register)\n" +
                "10 - not (register for answer, inverting register)\n" +
                "11 - goTo (codeline index, empty)\n" +
                "12 - goToIf (register eq/not eq to 1, codeline index)\n" +
                "13 - stop (empty, empty)\n" +
                "14 - printChar (register with data, empty)\n" +
                "15 - printInt (register with data, empty)\n" +
                "16 - readInt (register to read, empty)";

        showManualButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, man, "Info", JOptionPane.INFORMATION_MESSAGE));

        stepButton.addActionListener(e -> {
            tryCompile();
            if (data != null) {
                data.makeStep();
                for (int i = 0; i < Data.size; i++)
                    tableModel.pushValue(i, data.get(i));
                table.repaint();
                tablePane.repaint();
            }
            else {
                JOptionPane.showMessageDialog(this, "Not compiled yet", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(ModellingUI.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    String lines = readLines(file);
                    source.setText(lines);
                    updateHighline();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(ModellingUI.this,
                            "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(ModellingUI.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    writeLines(file, source.getText());
                } catch (Exception e1) {JOptionPane.showMessageDialog(ModellingUI.this,
                        "Cannot write file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        new ModellingUI();
    }

}
