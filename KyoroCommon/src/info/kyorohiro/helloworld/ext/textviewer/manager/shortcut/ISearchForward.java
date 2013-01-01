package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.display.widget.editview.task.SearchTask;
import info.kyorohiro.helloworld.display.widget.lineview.MyCursor;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.ModeLineBuffer;
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
		buffer.clearYank();
	}

	public static String PREV_LINE = "";
//	public static Thread th = null;
//	public void startTask(Runnable task) {
//		if(th !=null && th.isAlive()) {
//			th.interrupt();
//			th = null;
//		}
//		th = new Thread(task);
//		th.start();
//	}
	public class ISearchForwardTask implements ModeLineTask{
		private EditableLineView mTargetView = null;
		public ISearchForwardTask(EditableLineView targetView) {
			mTargetView = targetView;
		}

		@Override
		public void enter(String line){
			PREV_LINE = line;
			if(mTargetView.isDispose()) {
				return;
			}
//			android.util.Log.v("kiyo","#-#ISearchForward-enter"+line);
			ModeLineBuffer modeBuffer = LineViewManager.getManager().getModeLineBuffer();
			modeBuffer.startTask(new SearchTask(mTargetView, line));
		}

		@Override
		public void begin() {
			LineViewManager.getManager().changeFocus(LineViewManager.getManager().getModeLineBuffer());
			ModeLineBuffer modeBuffer = LineViewManager.getManager().getModeLineBuffer();
			EditableLineViewBuffer buffer = (EditableLineViewBuffer)modeBuffer.getLineView().getLineViewBuffer();
			buffer.clear();
			buffer.pushCommit(PREV_LINE, 1);
			modeBuffer.getLineView().getLeft().setCursorCol(0);
			modeBuffer.getLineView().getLeft().setCursorRow(0);
			modeBuffer.getLineView().recenter();
//			LineViewManager.getManager().getModeLineBuffer().getLineView().getLineViewBuffer()
		}

		@Override
		public void end() {
			LineViewManager.getManager().changeFocus((TextViewer)mTargetView.getParent());			
		}

	}

}
