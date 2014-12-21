package ProcessorModeling.Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static ProcessorModeling.Engine.Preprocessor.preprocess;

public class Data {

    public static final int size=1488;

    private int[] data;

    public int get(int index) {
        return data[index];
    }

    public void put(int index, int value) {
        data[index] = value;
    }

    private ArrayList<Command> program;
    private int index;

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
        }
    }

    public void start() {
        index = 0;
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
    }

}

