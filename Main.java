import java.io.*;
import syntaxtree.*;
import symbol.*;
import visitor.*;
import java.util.regex.*;
// import typecheck.*;
import java.util.Scanner;

public class Main{
    public static void main(String args[]){
        lab4(args);
    }
    public static void lab1(String args[]){
        try{
            InputStream in = new FileInputStream(args[0]);
            Node root = new MiniJavaParser(in).Goal();
            MType allClassList = new MClassList();
            root.accept(new BuildSymbolTableVisitor(), allClassList);
            root.accept(new TypeCheckVisitor((MClassList)allClassList), allClassList);
            System.out.println("Program type checked successfully");
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        catch(TokenMgrError e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void lab2(String args[]){
        try{
            InputStream in = new FileInputStream(args[0]);
            Node root = new MiniJavaParser(in).Goal();
            MType allClassList = new MClassList();
            root.accept(new BuildSymbolTableVisitor(), allClassList);
            root.accept(new TypeCheckVisitor((MClassList)allClassList), allClassList);
            // ---piglet---
            allClassList.classComplete();
            allClassList.allocTemp(25);
            // avoid overlap
            //allClassList.printAll();
            PrintStream out = new PrintStream(args[0].replace(".java", ".pg"));
            MPiglet result = root.accept(new MiniJavaToPigletVistor((MClassList)allClassList), allClassList);
            out.println(result.getCode().toString());
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        catch(TokenMgrError e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void lab3(String args[]){
        try{
            InputStream in = new FileInputStream(args[0]);
            Node root = new PigletParser(in).Goal();
            String pigletCode =""; 
            in = new FileInputStream(args[0]);
            int size = in.available();
            for (int i = 0; i < size; i++) {
                pigletCode += ((char)in.read());
            }
		
            String patternString = "\\s*(TEMP)\\s*(\\d*)";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(pigletCode);
            
            int mx = 0;
            while(matcher.find()){
                int num = Integer.valueOf(matcher.group(2).toString());
                // group(2) - > num
                if(num > mx)
                    mx = num;
            }
            //System.out.println(mx);
            PigletToSpigletVisitor lab3 = new PigletToSpigletVisitor(mx+10);
            // ensure temp from bigger num
            MSpiglet result = root.accept(lab3);
            PrintStream out = new PrintStream(args[0].replace(".pg", ".spg"));
            out.println(result.getCode().toString());
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        catch(TokenMgrError e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void lab4(String args[]){
        try{
            InputStream in = new FileInputStream(args[0]);
            PrintStream out = new PrintStream(args[0].replace(".spg", "_my.kg"));

            Scanner sc = new Scanner(in);
            String spgCode = "";
            while(sc.hasNext()){
                spgCode +=sc.nextLine()+"\n";
            }
            sc.close();
            in = new ByteArrayInputStream(spgCode.getBytes());

            MSpgProgram program = new MSpgProgram();

            Node root = new SpigletParser(in).Goal();

            root.accept(new BuildSpiglitTable(), program);
            program.analyzeAll();
            program.alloc();
            root.accept(new SpiglitToKanga(), program);
            //System.out.print(program.code);
            //MSpgProcedure del =  program.getProcedure("List_Delete");
            //for(MSpgStmt stmt: del.stmts){
            //    for(int var: stmt.active){
            //        System.out.print(var+" ");
            //    }
            //    System.out.println();
            //}
            out.print(program.code);
        }
        catch (TokenMgrError e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}