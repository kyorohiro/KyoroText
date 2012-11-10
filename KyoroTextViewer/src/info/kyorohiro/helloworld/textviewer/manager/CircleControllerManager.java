package info.kyorohiro.helloworld.textviewer.manager;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus.CircleMenuItem;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.textviewer.task.CopyTask;

public class CircleControllerManager {
	public static int COLOR_CIRCLE_DEFAULT = Color.parseColor("#44FFAA44");

	public void init() {
		SimpleCircleControllerMenuPlus circleMenu = LineViewManager.getManager().getCircleMenu();
		circleMenu.setCircleMenuItem(new CircleMenuItem() {
			@Override
			public boolean selected(int id, String title) {
				return _circleSelected(title);
			}
		});
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
		circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
		circleMenu.setEventListener(new MyCircleControllerEvent());
		circleMenu.setColorWhenDefault(COLOR_CIRCLE_DEFAULT);
	}

	public boolean _circleSelected(CharSequence title) {
		SimpleCircleControllerMenuPlus circleMenu = LineViewManager.getManager().getCircleMenu();
		CursorableLineView mLineView = LineViewManager.getManager().getFocusingTextViewer().getLineView();
		if (title.equals(CursorableLineView.MODE_VIEW)
				|| title.equals(CursorableLineView.MODE_EDIT)) {
			circleMenu.clearCircleMenu();
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
			if (title.equals(CursorableLineView.MODE_EDIT)) {
				mLineView.setMode(CursorableLineView.MODE_EDIT);
				circleMenu.addCircleMenu(0, "paste");
			} else {
				mLineView.setMode(CursorableLineView.MODE_VIEW);
			}

		} else if (title.equals(CursorableLineView.MODE_SELECT)) {
			circleMenu.clearCircleMenu();
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
			if (!CursorableLineView.MODE_SELECT.equals(mLineView.getMode())) {
				mLineView.setMode(CursorableLineView.MODE_SELECT);
			}
			circleMenu.addCircleMenu(0, "Copy");
		} else if (title.equals("Copy")) {
			circleMenu.clearCircleMenu();
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_EDIT);
			circleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			CopyTask.copyStart();
			return true;
		}
		return false;

	}
}
