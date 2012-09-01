package info.kyorohiro.helloworld.io;


import java.util.LinkedList;

// 
// 近々削除する
// 
public class BigLineDataBuilder {
	private LinkedList<A> mList = new LinkedList<A>();

	public void addCyclingList(A item) {
		mList.add(item);
	}

	public static interface A {
		public int startLine();
		public int endLine();
		public CharSequence getData(int position);
	}

	public static interface W {
		public void setCursor(int row, int col);
		public void pushCommit(CharSequence text, int cursor);
	}

	public static interface C {
		public void add(C text,int num);
	}


}
