package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.shell.CmdSession;
import info.kyorohiro.helloworld.util.shell.CLIAppKicker;
import info.kyorohiro.helloworld.util.shell.CLIAppKicker.CLIAppKickerException;

//
// まずは、課題抽出用に書いた、基本書き直す予定
// 
public class ShellTaskDone implements Task {

	private static CmdSession mCmd = null;

	@Override
	public String getCommandName() {
		return "shell-task-done";
	}

	@Override
	public synchronized void act(EditableLineView view,
			EditableLineViewBuffer buffer) {
		buffer.clearYank();

		//
		// MiniBuffer miniBuffer = BufferManager.getManager().getMiniBuffer();
		// miniBuffer.startTask(new
		// A(BufferManager.getManager().getFocusingTextViewer()));
		//
		// todo
		Thread t = new Thread(new A(BufferManager.getManager()
				.getFocusingTextViewer()));
		t.start();
	}

	public class A implements Runnable {
		private TextViewer mTarget = null;

		public A(TextViewer target) {
			mTarget = target;
		}

		@Override
		public void run() {
			EditableLineViewBuffer buffer = (EditableLineViewBuffer) mTarget
					.getLineView().getLineViewBuffer();
			VirtualFile targetVFile = mTarget.getVFile();
			if (mCmd == null || mCmd.getVFile() != targetVFile) {
				if (mCmd != null) {
					mCmd.end();
				}
				mCmd = new CmdSession(targetVFile, BufferManager.getManager()
						.getMiniBuffer().getSingleTaskRunner());
			}

			// input
			int len = buffer.getNumberOfStockedElement();
			// StringBuilder builder = new StringBuilder();
			// for(int i=1;len-i>=0;i++) {
			// KyoroString line = buffer.get(len-i);
			// if(line.includeLF()) {
			// break;
			// }
			// builder.append(line.toString());
			// }
			EditableLineViewBuffer editor = BufferManager.getManager()
					.getShellBuffer().getLineView().getEditableLineBuffer();
			editor.normalize(editor.getNumberOfStockedElement());
			int index = editor.getCol();
			;// buffer.getCol();
			if (index >= len - 1) {
				index = len - 1;
			}
			KyoroString line = buffer.get(index);
			android.util.Log.v("kiyo",
					"line>" + line.toString().replace("\r\n|\n", ""));
			// builder.append(line.toString());

			//
			try {
				buffer.setCursor(buffer.get(buffer.getCol()).length(),
						buffer.getCol());
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
			}
			buffer.crlf();
			buffer.clear();
			//
			//
			String input = line.toString().replaceAll("\r\n|\n", "");
			if(input.length() == 0) {
				return;
			}
			try {
				targetVFile.addChunk((">>"+input+"\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			mCmd.input(line.toString().replaceAll("\r\n|\n", ""));
			// builder.setLength(0);
			// builder = null;
		}
	}

}
