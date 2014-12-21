package ProcessorModeling.Engine;

import java.util.ArrayList;

public class Preprocessor {

    public static String[] preprocess(String code) {

        ArrayList<String> definedNames=new ArrayList<>();
        ArrayList<String> definedValues=new ArrayList<>();

        String source=code.replaceAll(";[^\\n]*", "");
        source=source.replaceAll("[ \\n\\t]+", "|");
        while (source.startsWith("|"))
            if (source.length()>1)
                source=source.substring(1);
            else source="";
        String[] commands=source.split("\\|");


        ArrayList<String> resultSource=new ArrayList<>();

        /*#DEF val 1
        writeConst 10 val
        printInt 10 1
*/

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

}
