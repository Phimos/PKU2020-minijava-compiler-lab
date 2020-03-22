class Test{
    public static void main(String[]a){
        System.out.println(new Start().start());
    }
}

class B extends Start{
    Start ab;
}

class C extends B{
    public int start(boolean sdf){
        return 0;
    }
}


class Start{
    Start star;
    int j;
    int i;

    public int plusone(int w,int ww){
        w = w + 1;
        return w;
    }

    public int start(boolean asdf){
        star = this.next();
        return 0;
    }

    public int faslkdjf(int asdf){
        asdf = this.plusone(this.plusone(this.plusone(asdf, asdf), asdf), this.plusone(asdf, asdf));
        return asdf;
    }

    public Start next(){
        B bb;
        bb = new B();
        return bb;
    }
}