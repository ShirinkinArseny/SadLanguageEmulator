package ProcessorModeling.Engine;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        new Data(


                "0 5 1 " +//  "SET 5 1" +    0
                "16 0 0 "+//  "REI 0 0" +   1
                "3 0 5 " +//  "SUB 0 5" +   1
                "3 0 5 " +//  "SUB 0 5" +   1
                "0 1 1 " +//  "SET 1 1" +    25
                "0 2 1 " +//  "SET 2 1" +    3
                "0 4 0 " +//  "SET 4 0" +    4
                "2 4 1 " +//  "SUM 4 1" +    5
                "2 4 2 " +//  "SUM 4 2" +    6
                "1 1 2 " +//  "COP 1 2" +    7
                "1 2 4 " +//  "COP 2 4" +    8
                "3 0 5 " +//  "SUB 0 5" +    9
                "6 7 0 " +//  "EQO 7 0" +    10
                "10 7 7 "+//  "NOT 7 7" +   11
                "12 7 6 "+//  "GTI 7 6" +   12
                "15 2 2"  //  "PRI 2 2"        13


                , new IO() {
            @Override
            public int readInt() {
                try {
                    return Integer.valueOf((char)System.in.read()+"");
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            public void printChar(int a) {
                System.out.print((char)a);
            }

            @Override
            public void printInt(int a) {
                System.out.println(a);
            }
        });
    }

}
