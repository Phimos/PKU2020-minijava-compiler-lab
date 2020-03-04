
class Test11{
	public static void main(String[]a){
		System.out.println(new Start().start());
	}
}

class Start{
	Start star;
	int j;

	public int start(){
		j = star.next(true);
		return 0;
	}

	public int next(boolean i){
		int i; //不允许参数重载
		return 0;
	}
}
