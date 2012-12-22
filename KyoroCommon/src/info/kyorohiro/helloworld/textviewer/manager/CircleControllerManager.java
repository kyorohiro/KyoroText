package info.kyorohiro.helloworld.textviewer.manager;

import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus.CircleMenuItem;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

public class CircleControllerManager {
	public static int COLOR_CIRCLE_DEFAULT = SimpleGraphicUtil.parseColor("#44FFAA44");
//	public static String MENU_GUARD_ON = "GUARD_ON";
//	public static String MENU_GUARD_OFF = "GUARD_OFF";

	public void init() {
		SimpleCircleControllerMenuPlus circleMenu = LineViewManager.getManager().getCircleMenu();
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
		SimpleCircleControllerMenuPlus circleMenu = LineViewManager.getManager().getCircleMenu();
		circleMenu.clearCircleMenu();
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
//		circleMenu.addCircleMenu(0, MENU_GUARD_ON);
//		circleMenu.addCircleMenu(0, MENU_GUARD_OFF);
	}
	public boolean _circleSelected(CharSequence title) {
		SimpleCircleControllerMenuPlus circleMenu = LineViewManager.getManager().getCircleMenu();
		TextViewer textviewer = LineViewManager.getManager().getFocusingTextViewer();
		CursorableLineView mLineView = textviewer.getLineView();
		if (title.equals(CursorableLineView.MODE_VIEW)
				|| title.equals(CursorableLineView.MODE_EDIT)) {
			clear();
			if (title.equals(CursorableLineView.MODE_EDIT)) {
				mLineView.setMode(CursorableLineView.MODE_EDIT);
				circleMenu.addCircleMenu(0, "Paste");
				textviewer.isGuard(true);
			} else {
				mLineView.setMode(CursorableLineView.MODE_VIEW);
			}

		} else if (title.equals(CursorableLineView.MODE_SELECT)) {
			clear();
			if (!CursorableLineView.MODE_SELECT.equals(mLineView.getMode())) {
				mLineView.setMode(CursorableLineView.MODE_SELECT);
			}
			circleMenu.addCircleMenu(0, "Copy");
		} else if (title.equals("Copy")) {
			clear();
//			CopyTask.copyStart();
			LineViewManager.getManager().copyStart();
			return true;
		} else if(title.equals("Paste")){
			circleMenu.clearCircleMenu();
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			LineViewManager.getManager().pastStart();
//			PastTask.pasteStart();
//		} else if(title.equals(MENU_GUARD_ON)) {
//			textviewer.isGuard(true);
//		} else if(title.equals(MENU_GUARD_OFF)) {
//			textviewer.isGuard(false);			
		}
		return false;

	}
}
