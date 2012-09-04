package info.kyorohiro.helloworld.textviewer.task;

import java.util.regex.Pattern;

import android.widget.TextView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewerBuffer;

public class SearchTextTask {
	private String mTarget = ".*";
	private TextViewer mViewer;
	private int mCurrentPoint = 0;
	private Pattern pattern = null;

	private static SearchTextTask sTask = null;

	public static NextRunner getSearchTextTask(TextViewer viewer,
			String searchWord) {
		//if (sTask == null && viewer != null) {
			sTask = new SearchTextTask(viewer);
			sTask.setTarget(searchWord);
			sTask.setPoint(viewer.getLineView().getShowingTextStartPosition());
		//}
		return new NextRunner(sTask);
	}

	public Runnable newNextTask() {
		return new NextRunner(this);
	}

	public static class NextRunner implements Runnable {
		private SearchTextTask mTask = null;

		private NextRunner(SearchTextTask task) {
			mTask = task;
		}

		@Override
		public void run() {
			int lineNumber = -1;
			// do {
			lineNumber = mTask.nextLine();
			// } while(lineNumber>=0);
			android.util.Log.v("kiyo", "lineNumber=" + lineNumber);
			android.util.Log.v("kiyo", "numOfChild=" + mTask.mViewer.getTextViewerBuffer().getNumberOfStockedElement());

			mTask.mViewer.getLineView().setPositionY(mTask.mViewer.getTextViewerBuffer().getNumberOfStockedElement()-(lineNumber)-5);
		}
	}

	public SearchTextTask(TextViewer viewer) {
		mViewer = viewer;
		mCurrentPoint = 0;
		pattern = Pattern.compile(mTarget);
	}

	public void setPoint(int point) {
		mCurrentPoint = point;
	}

	public void setTarget(String target) {
		mTarget = target;
		pattern = Pattern.compile(mTarget);
	}

	public int nextLine() {
		return _next();
	}

	public int prevLine() {
		return 0;
	}

	public int _next() {
		TextViewerBuffer buffer = mViewer.getTextViewerBuffer();
		if(buffer == null) {
			return -1;
		}

		int i=mCurrentPoint;
		while(i<buffer.getNumberOfStockedElement()){
			mCurrentPoint = i;
			if(find(buffer, i)){
				return i;
			}
			i++;
		}
		mCurrentPoint = 0;
		return -1;
	}

	private boolean find(TextViewerBuffer buffer, int i) {
		mCurrentPoint = i;
		LineViewData data = buffer.get(i);
		if (pattern.matcher(data).find()) {
			return true;
		} else {
			return false;
		}
	}
}
