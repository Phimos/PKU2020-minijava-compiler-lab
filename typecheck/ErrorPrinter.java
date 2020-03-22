package typecheck;

import symbol.*;
import syntaxtree.*;

public class ErrorPrinter{
    protected static int errorCode;
    protected static String errorMsg = "";
    protected static String[] errorDetail;
    protected static int row;
    protected static int col;

    public static void getError(int _errorcode, int _row, int _col, String[] _detail){
        errorCode = _errorcode;
        row = _row;
        col = _col;
        errorDetail = _detail;
        printError();
        System.exit(0);
    }

    public static void getError(int _errorcode, MType _type, String ... _detail){
        getError(_errorcode, _type.getRow(), _type.getCol(), _detail);
    }

    public static void getError(int _errorcode, NodeToken _n, String ... _detail){
        getError(_errorcode, _n.beginLine, _n.beginColumn, _detail);
    }

    public static void printError(){
        switch(errorCode){
            case 0: 
            errorMsg = "Unknown error";
            break;
            case 1:
            errorMsg = String.format("Undeclared %s", errorDetail[0]); 
            break;
            case 2: 
            errorMsg = String.format("Duplicated declaration %s", errorDetail[0]); 
            break;
            case 3: 
            errorMsg = "Type mismatch"; 
            break;
            case 4: 
            errorMsg = "Parameter mismatch"; 
            break;
            case 5: 
            errorMsg = "Operator error"; 
            break;
            case 6: 
            errorMsg = "Class error"; 
            break;
        }
        System.out.format("ERROR %d: %s [%d:%d]\n", errorCode, errorMsg, row, col);
    }


}