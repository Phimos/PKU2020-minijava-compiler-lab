
class Test7{
	public static void main(String[]a){
		System.out.println(new Start().start());
	}
}

class Start{
	public int start(){
		return 0;
	}
}

class A extends B{

}

class B extends C{

}

class C extends A{

}