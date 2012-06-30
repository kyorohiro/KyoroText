package info.kyorohiro.helloworld.io;

import info.kyorohiro.helloworld.util.CyclingList;

import java.util.LinkedList;

//
// BigLineDataにデータ書き込みの機能を追加する。
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
		public void pushCommit(CharSequence text, int cursor);
		public void setComposing(CharSequence text);
	}

	public static interface C {
		public void add(C text,int num);
	}


}
