package info.kyorohiro.helloworld.textviewer.appparts;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActionWarningMessagePlus extends Dialog{

	public static void showDialog(Activity context, MyTask task, boolean showDialog) {
		if(showDialog) {
			MenuActionWarningMessagePlus i = new MenuActionWarningMessagePlus(context, task);
			i.show();
		} else {
			task.run(context);
		}
	}

	public static interface  MyTask {
		void run(Activity c);
	}
	private MyTask mTask = null;
	private WeakReference<Activity> mActivity = null; 
	public MenuActionWarningMessagePlus(Activity context,  MyTask task) {
		super(context);
		mActivity = new WeakReference<Activity>(context);
		mTask = task;
		init();
	}

	public void init() {
		TextView view = new TextView(getContext());
		Button positiveButton = new Button(getContext());
		positiveButton.setText("OK?");
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		view.setText("This action\n release edit/view data. OK?");
		layout.addView(view);
		layout.addView(positiveButton);
		setContentView(layout);
		this.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dismiss();
			}
		});
		positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTask.run(mActivity.get());
				dismiss();
			}
		});
	}
}
