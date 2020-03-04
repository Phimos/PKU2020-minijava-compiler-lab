package typecheck;

import java.util.ArrayList;

public class ErrorPrinter{
    protected static ArrayList<String> errorMsg = new ArrayList<String>();

    public static void addError(String err){
        errorMsg.add(err);
        suicide();
    }

    public static void printAll(){
        System.out.println(errorMsg.get(0));
    }

    public static int getsize(){
        return errorMsg.size();
    }

    public static void suicide(){
        printAll();
        System.exit(0);
    }
}