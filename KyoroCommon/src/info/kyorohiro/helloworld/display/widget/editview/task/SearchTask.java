package info.kyorohiro.helloworld.display.widget.editview.task;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
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

	public void log(String log) {
		//android.util.Log.v("kiyo", "SearchTask="+log);
	}

	@Override
	public void run() {
//		android.util.Log.v("kiyo","ST: start start");
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
			log("=end=" + end);
			boolean lastoneline = false;
			boolean find = false;
			while (index != end || f || !lastoneline) {
				Thread.sleep(0);
				if (index == end && !f) {
					lastoneline = true;
				}
				log("#=check1===i=" + index + ",r="+row+",e=" + end + ",f="+f+",bl=" + buffer.getNumberOfStockedElement());
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
				log("#=i=====" + index + "," + end + ",bl=" + buffer.getNumberOfStockedElement() + ","
				+ str);
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
					log("=0=i-" + index + "," + row);
					log("=0=-"
								+ mTargetView.getLeft().getCursorCol() + ","
								+ l.length());
						mTargetView.getLeft().setCursorCol(
								l.getY() + index - (l.length()) + 1);
						mTargetView.getLeft().setCursorRow(
								l.getX() + (l.getY() == 0 ? row : 0));
					log("=1=-"
								+ mTargetView.getLeft().getCursorCol());
						mTargetView.recenter();
						find = true;
					log("=2=-"
							+ mTargetView.getLeft().getCursorCol());
						break;
					} else {
						mTargetView.getLeft().setCursorCol(
								l.getY() + index - (l.length()) + 1);
						mTargetView.getLeft().setCursorRow(
								l.getX() + (l.getY() == 0 ? row : 0));
						mTargetView.recenter();
						SimpleStage stage = SimpleDisplayObject.getStage(mTargetView);
						if(stage != null) {
							stage.resetTimer();
						}
					}
					l.clear();
					row = 0;
				}
				index++;
				log("rof=i:"+index +"!=e:"+ end +"||f:"+ f +"|| !l:"+lastoneline);
			}
			if(!find) {
				mTargetView.getLeft().setCursorCol(baseIndex);
				mTargetView.getLeft().setCursorRow(baseRow);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Throwable e) {
			// todo 面倒なので...
			e.printStackTrace();
		} finally {
//			android.util.Log.v("kiyo","ST: start end--------------------------------");
			if(mTargetView !=null && !mTargetView.isDispose()) {
				buffer.isSync(false);
				mTargetView.isLockScreen(false);
				mTargetView.recenter();
			}
//			android.util.Log.v("kiyo","ST: end end--------------------------------");
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
