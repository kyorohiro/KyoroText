package info.kyorohiro.helloworld.ext.textviewer.manager;

import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus.CircleMenuItem;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.manager.CircleControllerEvent;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward;
import info.kyorohiro.helloworld.ext.textviewer.manager.task.SearchTask;

public class CircleControllerManager {
	public static int COLOR_CIRCLE_DEFAULT = SimpleGraphicUtil.parseColor("#44FFAA44");
	public static String MENU_SEARCH = "Search";

	public void init() {
		SimpleCircleControllerMenuPlus circleMenu = BufferManager.getManager().getCircleMenu();
		circleMenu.setCircleMenuItem(new CircleMenuItem() {
			@Override
			public boolean selected(int id, String title) {
				return _circleSelected(title);
			}
		});
		clear();
		circleMenu.setEventListener(new CircleControllerEvent());
		circleMenu.setColorWhenDefault(COLOR_CIRCLE_DEFAULT);
	}

	private void clear() {
		SimpleCircleControllerMenuPlus circleMenu = BufferManager.getManager().getCircleMenu();
		circleMenu.clearCircleMenu();
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
	}
	public boolean _circleSelected(CharSequence title) {
//		android.util.Log.v("kiyo","selected="+title);
		SimpleCircleControllerMenuPlus circleMenu = BufferManager.getManager().getCircleMenu();
		TextViewer textviewer = BufferManager.getManager().getFocusingTextViewer();
		TextViewer mInfo = BufferManager.getManager().getInfoBuffer();
		CursorableLineView mLineView = textviewer.getLineView();
		if(title.equals(MiniBuffer.MODE_LINE_BUFFER)) {
//			SimpleCircleControllerMenuPlus circleMenu = BufferManager.getManager().getCircleMenu();
			circleMenu.clearCircleMenu();
			circleMenu.addCircleMenu(0, "Paste");
			mLineView.setMode(MiniBuffer.MODE_LINE_BUFFER);
			textviewer.isGuard(true);
		}
		else if(title.equals(BufferManager.SHELL_BUFFER)) {
			circleMenu.clearCircleMenu();
//			circleMenu.addCircleMenu(0, MENU_SEARCH);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			mLineView.setMode(BufferManager.SHELL_BUFFER);
			textviewer.isGuard(true);
		}
		else if (title.equals(CursorableLineView.MODE_VIEW)
				|| title.equals(CursorableLineView.MODE_EDIT)) {
			{
				if(mInfo !=null && mInfo == textviewer) {
					circleMenu.clearCircleMenu();
					circleMenu.addCircleMenu(0, MENU_SEARCH);
					circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
					mLineView.setMode(CursorableLineView.MODE_VIEW);
				} else {
					clear();
					circleMenu.addCircleMenu(0, MENU_SEARCH);
					if (title.equals(CursorableLineView.MODE_EDIT)) {
						mLineView.setMode(CursorableLineView.MODE_EDIT);
						circleMenu.addCircleMenu(0, "Paste");
						textviewer.isGuard(true);
					} else {
						mLineView.setMode(CursorableLineView.MODE_VIEW);
					}
				}
			}

		} else if (title.equals(CursorableLineView.MODE_SELECT)) {
			if(mInfo == textviewer) {
//				android.util.Log.v("kiyo","----0---");
				circleMenu.clearCircleMenu();
				circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
				circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
				circleMenu.addCircleMenu(0, MENU_SEARCH);
			} else if(BufferManager.getManager().getShellBuffer() == textviewer) {
//				android.util.Log.v("kiyo","----1---");
				circleMenu.clearCircleMenu();
				circleMenu.addCircleMenu(0, BufferManager.SHELL_BUFFER);
			} else {
//				android.util.Log.v("kiyo","----2---");
				clear();
				circleMenu.addCircleMenu(0, MENU_SEARCH);
			}
			if (!CursorableLineView.MODE_SELECT.equals(mLineView.getMode())) {
				mLineView.setMode(CursorableLineView.MODE_SELECT);
			}
			circleMenu.addCircleMenu(0, "Copy");
		} else if (title.equals("Copy")) {
			clear();
//			CopyTask.copyStart();
			if(!title.equals(MiniBuffer.MODE_LINE_BUFFER)) {
				circleMenu.addCircleMenu(0, MENU_SEARCH);
			}
			BufferManager.getManager().copyStart();
			return true;
		} else if(title.equals("Paste")){
			circleMenu.clearCircleMenu();
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			BufferManager.getManager().pastStart();
			return true;
		} else if(title.equals(MENU_SEARCH)){
			if(mInfo !=null && mInfo == textviewer) {
//				clear();
				circleMenu.clearCircleMenu();
				circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
				circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
				circleMenu.addCircleMenu(0, MENU_SEARCH);
			}
			else if(mInfo !=null && BufferManager.getManager().getShellBuffer() == textviewer) {
				circleMenu.clearCircleMenu();
				circleMenu.addCircleMenu(0, BufferManager.SHELL_BUFFER);				
			} 
			else {
				clear();
				circleMenu.addCircleMenu(0, MENU_SEARCH);
			}
			ISearchForward is = new ISearchForward();
			EditableLineView e = BufferManager.getManager().getFocusingTextViewer().getLineView();
			is.act(e, (EditableLineViewBuffer)e.getLineViewBuffer());

			return true;
		}
		return false;

	}
}
