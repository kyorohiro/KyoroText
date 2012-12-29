package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

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
			mTargetView.iSearchForward();
		}

		@Override
		public void begin() {
			LineViewManager.getManager().changeFocus(LineViewManager.getManager().getModeLineBuffer());
		}

		@Override
		public void end() {
			LineViewManager.getManager().changeFocus((TextViewer)mTargetView.getParent());			
		}
	}
}
