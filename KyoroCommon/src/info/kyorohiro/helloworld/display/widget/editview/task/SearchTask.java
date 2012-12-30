package info.kyorohiro.helloworld.display.widget.editview.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.MyCursor;
import info.kyorohiro.helloworld.text.KyoroString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchTask implements Runnable {
	private EditableLineView mTargetView = null;
	private String mRegex = "";
	public SearchTask(EditableLineView targetView, String regex) {
		mRegex = regex;
		mTargetView = targetView;
	}

	@Override
	public void run() {
		Pattern pattern = Pattern.compile(mRegex);
		MyCursor cursor = mTargetView.getLeft();
		int index = cursor.getCursorCol();
		int row = cursor.getCursorRow();
		EditableLineViewBuffer buffer = (EditableLineViewBuffer)mTargetView.getLineViewBuffer();
		while(index<buffer.getNumberOfStockedElement()) {
			KyoroString str = buffer.get(index);
			Matcher m = pattern.matcher(str.toString());
			if(m.find()) {
				mTargetView.getLeft().setCursorCol(index);
				mTargetView.getLeft().setCursorRow(m.end());
				mTargetView.recenter();
				break;
			}
			index++;
		}
	}
}
