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
import java.util.ArrayList;

public class ModellingUI extends JFrame {
    private JEditorPane source;
    private JTable table;
    private JPanel mainPane;
    private JButton stepButton;
    private JTextField outputPane;
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
    private JPanel ProgramStatistic;
    private JTextField statField;
    private JTable definesTable;
    private JScrollPane defsPane;
    private JPanel Defines;
    private JButton dropData;
    private Data data;
    private RegistersTable tableModel;
    private DefinesTable defsModel;
    private boolean compiled=false;
    private static final String man=
            "SYNTAX:\n"+
                    "COMMAND (or its index) ARG1 ARG2 ;COMMENT\n"+
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
                    "11 - goTo (mark index, empty)\n" +
                    "12 - goToIf (register eq/not eq to 1, mark index)\n" +
                    "13 - stop (empty, empty)\n" +
                    "14 - printChar (register with data, empty)\n" +
                    "15 - printInt (register with data, empty)\n" +
                    "16 - readInt (register to read, empty)\n" +
                    "17 - mark (mark index, empty)\n" +
                    "18 - putFrom (register, register, which contains index of source register)\n" +
                    "19 - putTo (register which contains index of result register, register)\n" +
                    "\n" +
                    "DEFINES:\n" +
                    "#DEF WORD_WHICH_WILL_BE_REPLACED WORD_WHICH_WILL_REPLACE\n" +
                    "\n" +
                    "";



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

    private void repaintTables() {
        if (data!=null) {
            tableModel.pushValue(data.getRegisters());
            defsModel.pushValues(data.getDefinedNames(), data.getDefinedValues());
        }
        else {
            tableModel.pushValue(new int[]{});
            defsModel.pushValues(new ArrayList<>(), new ArrayList<>());
        }
        table.updateUI();
        definesTable.updateUI();
    }

    private void processCompilationException(String excptText) {
        JOptionPane.showMessageDialog(this, "Failed to compile! " + excptText, "Error", JOptionPane.ERROR_MESSAGE);
        compiled=false;
        data=null;
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
            compiled=true;
        }
        catch (ArrayIndexOutOfBoundsException e1) {
            e1.printStackTrace();
            processCompilationException("Wrong number of parameters");
        }
        catch (NumberFormatException e1) {
            e1.printStackTrace();
            processCompilationException("Excepted integer number, found something else");
        }
        catch (Exception e1) {
            e1.printStackTrace();
            processCompilationException("Unknown error");
        }
        finally {
            repaintTables();
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
            compiled=false;
        }
    }

    private void processRuntimeException(String excptText) {
        JOptionPane.showMessageDialog(this, "Exception in your code! "+excptText, "Error", JOptionPane.ERROR_MESSAGE);
        statField.setText("Executed " + data.getExecutedCommands() + " commands" + ", last command: " + data.getIndex() + ", stopped with exception");
        compiled=false;
    }

    private void doOneStep() {
        try {
            data.makeStep();
            tableModel.pushValue(data.getRegisters());
            statField.setText("Executed " + data.getExecutedCommands() + " commands" + ", last command: " + data.getIndex());
            repaintTables();
        }
        catch (ArithmeticException e) {
            e.printStackTrace();
            processRuntimeException("Divide by zero");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            processRuntimeException("Not correct register index");
        }
        catch (Exception e) {
            e.printStackTrace();
            processRuntimeException("Unknown error");
        }
    }

    public ModellingUI() {

        super("CPU modelling");


        setContentPane(mainPane);

        sourceScroll.setMinimumSize(new Dimension(300, 100));

        table.setModel(tableModel = new RegistersTable());
        definesTable.setModel(defsModel=new DefinesTable());



        source.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                updateHighline();
            }
        });

        showManualButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, man, "Info", JOptionPane.INFORMATION_MESSAGE));

        dropData.addActionListener(e -> {
            data = null;
            repaintTables();
        });

        fastButton.addActionListener(e -> {
            tryCompile();
            if (compiled) {
                while (!data.getIsDone() && compiled) {
                    doOneStep();
                    if (data.getExecutedCommands()%10000==0 && data.getExecutedCommands()>0) {
                        TooMuchCommands t=new TooMuchCommands(this, data.getExecutedCommands());
                        if (t.getWhatToDo()== TooMuchCommands.Action.Stop) {
                            break;
                        }
                    }
                }
            }
        });

        stepButton.addActionListener(e -> {
            if (!compiled) tryCompile();
            if (compiled) doOneStep();
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
