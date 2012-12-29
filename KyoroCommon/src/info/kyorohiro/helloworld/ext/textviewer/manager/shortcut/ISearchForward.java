package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.display.widget.lineview.MyCursor;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;

public class ISearchForward implements Task {

	@Override
	public String getCommandName() {
		return "isearch-forward";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		LineViewManager.getManager().getModeLineBuffer().startModeLineTask(new ISearchForwardTask(view));
	}

	public class ISearchForwardTask implements ModeLineTask{
		private EditableLineView mTargetView = null;
		public ISearchForwardTask(EditableLineView targetView) {
			mTargetView = targetView;
		}

		@Override
		public void enter(String line){
			android.util.Log.v("kiyo","#-#ISearchForward-enter"+line);
			Thread t = new Thread(new Search(line));
			t.start();
		}

		@Override
		public void begin() {
			LineViewManager.getManager().changeFocus(LineViewManager.getManager().getModeLineBuffer());
		}

		@Override
		public void end() {
			LineViewManager.getManager().changeFocus((TextViewer)mTargetView.getParent());			
		}

		public class Search implements Runnable {
			private String mRegex = "";
			public Search(String regex) {
				mRegex = regex;
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
	}

}
