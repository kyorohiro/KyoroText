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
		public void pushCommit(CharSequence text);
		public void setComposing(CharSequence text);
	}

	public static interface C {
		public void add(C text,int num);
	}

	public static class SampleText implements W {
		private int cursorRow = 0;//line
		private int cursorCol = 0;//point
		private LinkedList<CharSequence> lines = new LinkedList<CharSequence>();
		private BreakText mBreakText = null;
		private MyBuilder builder = new MyBuilder();

		public SampleText(BreakText breaktext) {
			mBreakText = breaktext;
		}

		@Override
		public void pushCommit(CharSequence text) {
			builder.clear();
			pushCommit(text, cursorRow, cursorCol);
		}

		public void pushCommit(CharSequence text, int row, int col) {
			CharSequence current = lines.get(row);

			int p = 0;
			builder.clear();
			for(int i=0;i<col;i++) {
				builder.append(current.charAt(i));
				p++;
			}
			for(int i=0;i<text.length();i++){
				builder.append(text.charAt(i));
			}
			for(int i=col;i<current.length();i++){
				builder.append(current.charAt(i));
			}
			// mod
			int breakPoint = mBreakText.breakText(builder);
			// 再度計算する。
			builder.clearFirst(breakPoint);
			pushCommit(null,row+1,0);
		}

		@Override
		public void setComposing(CharSequence text) {
		}
		
		public void setCursor(int row, int col) {
			cursorRow = row;
			cursorCol = col;
		}
	}
}
