
class Test11{
	public static void main(String[]a){
		System.out.println(new Start().start());
	}
}

class Start{
	Start star;
	int j;
	int b;

	public int start(){
		//j = star.next(true);
		int mm;
		star = new Start();
		b = 1;
		j = 2;
		mm = b+j;
		return mm;
	}

	public int woqu(){
		//int i; //不允许参数重载
		b = 11;
		j = 2;
		return 11;
	}

	public int next(boolean i){
		//int i; //不允许参数重载
		return 11;
	}
}
