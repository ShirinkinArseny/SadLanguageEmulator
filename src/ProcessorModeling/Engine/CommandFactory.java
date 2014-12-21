package ProcessorModeling.Engine;

public class CommandFactory {

    public static final Command mark= r -> {};

    private enum Action {
        writeConst, copy,
        sum, subtract, multiply, divide,
        equals0, and, or, xor, not,
        goTo, goToIf, stop,
        printChar, printInt, readInt, mark, putFrom, putTo}

    public static Command getCommand(Action type, IO io, int param1, int param2) {
        switch (type) {
            case writeConst: return r -> r.put(param1, param2);
            case copy: return r -> r.put(param1, r.get(param2));

            case sum:  return r -> r.put(param1, r.get(param1)+r.get(param2));
            case subtract: return r -> r.put(param1, r.get(param1)-r.get(param2));
            case multiply:  return r -> r.put(param1, r.get(param1)*r.get(param2));
            case divide: return r -> r.put(param1, r.get(param1)/r.get(param2));

            case equals0: return r -> r.put(param1, r.get(param2)==0?1:0);
            case and: return r -> r.put(param1, r.get(param1)==1&&r.get(param2)==1?1:0);
            case or:  return r -> r.put(param1, r.get(param1)==1||r.get(param2)==1?1:0);
            case xor: return r -> r.put(param1, r.get(param1)==1^r.get(param2)==1?1:0);
            case not: return r -> r.put(param1, r.get(param2)==0?1:0);

            case goTo: return r->r.goTo(param1);
            case goToIf: return r->{if (r.get(param1)==1) r.goTo(param2);};
            case stop: return Data::stop;

            case printChar: return r -> io.printChar(r.get(param1));
            case printInt: return r -> io.printInt(r.get(param1));
            case readInt: return r -> r.put(param1, io.readInt());

            case mark: return mark;

            case putFrom: return r -> r.put(param1, r.get(param2));
            case putTo: return r -> r.put(r.get(param1), param2);
        }
        return null;
    }

    private static final String[] actionsNames=new String[]{
            "writeConst",
            "copy",
            "sum",
            "subtract",
            "multiply",
            "divide",
            "equals0",
            "and",
            "or",
            "xor",
            "not",
            "goTo",
            "goToIf",
            "stop",
            "printChar",
            "printInt",
            "readInt",
            "mark",
            "putFrom",
            "putTo"
    };

    private static int getTypeByName(String name) {
        for (int i=0; i<actionsNames.length; i++)
            if (name.equals(actionsNames[i]))
                return i;
        return -1;
    }

    private static final Action[] actions=new Action[]
            {
                    Action.writeConst,   //   0
                    Action.copy,         //   1
                    Action.sum,          //   2
                    Action.subtract,     //   3
                    Action.multiply,     //   4
                    Action.divide,       //   5
                    Action.equals0,      //   6
                    Action.and,          //   7
                    Action.or,           //   8
                    Action.xor,          //   9
                    Action.not,          //   10
                    Action.goTo,         //   11
                    Action.goToIf,       //   12
                    Action.stop,         //   13
                    Action.printChar,    //   14
                    Action.printInt,     //   15
                    Action.readInt,      //   16
                    Action.mark,         //   17
                    Action.putFrom,      //   18
                    Action.putTo,        //   19
            };

    public static Command getCommand(String type, IO io, int param1, int param2) throws Exception {


        int typeI;
        try {
            typeI=Integer.valueOf(type);
        }
        catch (Exception e) {
            typeI = getTypeByName(type.trim());
        }
        if (typeI>=0 && typeI<=actions.length) return getCommand(actions[typeI], io, param1, param2);
        throw new Exception("Unknown command: "+type+" :: param1: "+param1+" :: param2: "+param2);
    }

}
