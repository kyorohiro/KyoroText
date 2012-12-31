package info.kyorohiro.helloworld.display.widget.editview.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.MyCursor;
import info.kyorohiro.helloworld.text.KyoroString;

import java.util.ArrayList;
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
		MyCursor cursor = mTargetView.getLeft();
		int index = cursor.getCursorCol();
		int row = cursor.getCursorRow();
		int baseIndex = index;
		int baseRow = row;
		EditableLineViewBuffer buffer = (EditableLineViewBuffer) mTargetView
				.getLineViewBuffer();
		try {
			//mTargetView.isLockScreen(true);
			buffer.isSync(true);
			Line l = new Line(mRegex);
			boolean f = true;
			int end = buffer.getNumberOfStockedElement();
			int indexa = index;
			if (indexa > 0) {
				end = index;
			}
			if (0 == buffer.getNumberOfStockedElement()) {
				return;
			}
			//android.util.Log.v("kiyo", "=end=" + end);
			boolean lastoneline = false;
			boolean find = false;
			while (index != end || f || !lastoneline) {
				Thread.sleep(0);
				if (index == end && !f) {
					lastoneline = true;
				}
				//android.util.Log.v("kiyo", "#=i==111===" + index + "," + end
				//		+ ",bl=" + buffer.getNumberOfStockedElement());
				if (index >= buffer.getNumberOfStockedElement()) {
					index = 0;
				}
				KyoroString str = buffer.get(index);
				//
				end = buffer.getNumberOfStockedElement();
				if (indexa > 0) {
					end = indexa;
				}
				//
				//android.util.Log.v("kiyo", "#=i=====" + index + "," + end
				//		+ ",bl=" + buffer.getNumberOfStockedElement() + ","
				//		+ str);
				if (f) {
					l.add(str, row, str.length());
					f = false;
				} else {
					l.add(str, 0, str.length());
				}
				if (str.includeLF()
						|| index + 1 == buffer.getNumberOfStockedElement()
						|| index + 1 == end) {
					if (l.find()) {
					//	android.util.Log.v("kiyo", "=0=i-" + index + "," + row);
					//	android.util.Log.v("kiyo", "=0=-"
					//			+ mTargetView.getLeft().getCursorCol() + ","
					//			+ l.length());
						mTargetView.getLeft().setCursorCol(
								l.getY() + index - (l.length()) + 1);
						mTargetView.getLeft().setCursorRow(
								l.getX() + (l.getY() == 0 ? row : 0));
					//	android.util.Log.v("kiyo", "=1=-"
					//			+ mTargetView.getLeft().getCursorCol());
						mTargetView.recenter();
						find = true;
					//	android.util.Log.v("kiyo", "=2=-"
					//			+ mTargetView.getLeft().getCursorCol());
						break;
					} else {
						mTargetView.getLeft().setCursorCol(
								l.getY() + index - (l.length()) + 1);
						mTargetView.getLeft().setCursorRow(
								l.getX() + (l.getY() == 0 ? row : 0));
						mTargetView.recenter();
					}
					l.clear();
					row = 0;
				}
				index++;
			}
			if(!find) {
				mTargetView.getLeft().setCursorCol(baseIndex);
				mTargetView.getLeft().setCursorRow(baseRow);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			buffer.isSync(false);
			mTargetView.isLockScreen(false);
			mTargetView.recenter();
		}
	}

	//
	// KyoroText's Managed Text data dont't divide per CRLF
	// This class create group per CRLF. and find
	public static class Line {
		private StringBuffer mBuffer = new StringBuffer();
		private ArrayList<Integer> mLength = new ArrayList<Integer>(10);
		private Pattern mPattern = null;
		private int mX = 0;
		private int mY = 0;

		public int length() {
			return mLength.size();
		}

		public Line(String regex) {
			mPattern = Pattern.compile(regex);
		}

		public void add(CharSequence str, int begin, int end) {
			mBuffer.append(str.subSequence(begin, end));
			mLength.add(end - begin);
		}

		public void clear() {
			mLength.clear();
			mBuffer = new StringBuffer();
			mX = 0;
			mY = 0;
		}

		public boolean find() {

			Matcher mMatcher = mPattern.matcher(mBuffer.toString());
			boolean ret = mMatcher.find();
//			android.util.Log.v("kiyo",
//					"----ret=" + ret + "," + mBuffer.toString());
			if (ret) {
				int start = mMatcher.start();
				int end = mMatcher.end();
				int len = 0;
				int prev = 0;
//				android.util.Log.v("kiyo", "get=-----");
				for (int i = 0; i < mLength.size(); i++) {
					len += mLength.get(i);
//					android.util.Log.v("kiyo", "get=" + len + "," + end + "-"
//							+ prev);
					if (end <= len) {
						mY = i;
						mX = end - prev;
						break;
					}
					prev = len;
				}
//				android.util.Log.v("kiyo", "get=-----");
			}
			return ret;
		}

		public int getX() {
//			android.util.Log.v("kiyo", "getX=-" + mX);
			return mX;
		}

		public int getY() {
//			android.util.Log.v("kiyo", "getY=-" + mY);
			return mY;
		}
	}
}
