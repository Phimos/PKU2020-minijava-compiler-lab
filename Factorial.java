class Test{
    public static void main(String[]a){
        System.out.println(new Start().start());
    }
}

class B extends Start{
    Start ab;
}

class Start{
    Start star;
    int j;
    int i;

    public int start(){
        star = this.next();
        return 0;
    }

    public Start next(){
        B bb;
        bb = new B();
        return bb;
    }
}