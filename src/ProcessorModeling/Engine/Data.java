package ProcessorModeling.Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Data {

    public static final int size=10000;

    private int[] data;

    public void put(int index, int value) {
        data[index] = value;
    }

    private ArrayList<Command> program;

    public int[] getRegisters() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    private int index;
    private int executedCommands;

    void goTo(int mark) {
        this.index = marks.get(mark)-1;//cuz index increments after operation
    }

    private HashMap<Integer, Integer> marks=new HashMap<>();

    public void stop() {
        index = program.size();
    }

    public boolean getIsDone() {
         return index >= program.size();
    }

    public void makeStep() {
        if (!getIsDone()) {
            program.get(index).process(this);
            index++;
            executedCommands++;
        }
    }

    public ArrayList<String> getDefinedValues() {
        return definedValues;
    }

    public ArrayList<String> getDefinedNames() {
        return definedNames;
    }

    private ArrayList<String> definedNames=new ArrayList<>();
    private ArrayList<String> definedValues=new ArrayList<>();

    public String[] preprocess(String code) {

        definedNames=new ArrayList<>();
        definedValues=new ArrayList<>();

        String source=code.replaceAll(";[^\\n]*", "");
        source=source.replaceAll("[ \\n\\t]+", "|");
        while (source.startsWith("|"))
            if (source.length()>1)
                source=source.substring(1);
            else source="";
        String[] commands=source.split("\\|");


        ArrayList<String> resultSource=new ArrayList<>();

        for (int i=0; i<commands.length; i++)
        {
            String s=commands[i];
            if (s.equals("#DEF")) {
                String def1=commands[i+1];
                String def2=commands[i+2];
                definedValues.add(def2);
                definedNames.add(def1);
                i+=2;
            }
            else {
                for (int j=0; j<definedNames.size(); j++)
                    s=s.replaceAll(definedNames.get(j), definedValues.get(j));
                resultSource.add(s);
            }
        }
        String[] result=new String[resultSource.size()];
        for (int i=0; i<resultSource.size(); i++)
            result[i]=resultSource.get(i);
        return result;
    }

    public Data(String source, IO io) throws Exception {
        data = new int[size];
        program = new ArrayList<>();
        String[] commands=preprocess(source);
        System.out.println(Arrays.toString(commands));
        for (int i = 0; i < commands.length; i += 3) {

            Command c=CommandFactory.getCommand(
                    commands[i], io,
                    Integer.valueOf(commands[i + 1]),
                    Integer.valueOf(commands[i + 2])
            );

            if (c==CommandFactory.mark)
                marks.put(Integer.valueOf(commands[i + 1]), i/3);

            program.add(c);
        }
        index = 0;
        executedCommands=0;
    }

    public int getExecutedCommands() {
        return executedCommands;
    }

    public int get(int index) {
        return data[index];
    }
}

