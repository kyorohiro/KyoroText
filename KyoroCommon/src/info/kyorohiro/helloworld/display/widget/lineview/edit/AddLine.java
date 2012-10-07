package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.Line;

import java.util.ArrayList;

public class AddLine  implements Line {
	public int mOrifinalInsertIndex = 0;
	public ArrayList<CharSequence> mLines = new ArrayList<CharSequence>();

	public AddLine(int index, CharSequence line) {
		mOrifinalInsertIndex = index;
		mLines.add(line);
	}

	@Override
	public CharSequence get(int i) {
		return mLines.get(i);
	}

	@Override
	public int begin() {
		return mOrifinalInsertIndex;
	}

	@Override
	public int length() {
		return mLines.size();
	}

	public void setStart(int start) {
		mOrifinalInsertIndex = start;
	}

	@Override
	public void set(int index, CharSequence line) {
		mLines.set(index, line);
	}

	@Override
	public void insert(int index, CharSequence line) {
		mLines.add(index, line);
	}

	@Override
	public void rm(int index) {
		mLines.remove(index);
	}
}