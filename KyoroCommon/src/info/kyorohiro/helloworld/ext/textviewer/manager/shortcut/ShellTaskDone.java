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
	public synchronized void act(EditableLineView view, EditableLineViewBuffer buffer) {
		buffer.clearYank();

		//
		//MiniBuffer miniBuffer = BufferManager.getManager().getMiniBuffer();
		//miniBuffer.startTask(new A(BufferManager.getManager().getFocusingTextViewer()));
		//
		//todo 
		Thread t = new Thread(new A(BufferManager.getManager().getFocusingTextViewer()));
		t.start();
	}

	public class A implements Runnable {
		private TextViewer mTarget = null;
		public A(TextViewer target) {
			mTarget = target;
		}

		@Override
		public void run() {
			EditableLineViewBuffer buffer = (EditableLineViewBuffer)mTarget.getLineView().getLineViewBuffer();
			VirtualFile targetVFile = mTarget.getVFile();
			if(mCmd == null || mCmd.getVFile() != targetVFile) {
				if(mCmd != null) {
					mCmd.end();
				}
				mCmd = new CmdSession(targetVFile, BufferManager.getManager().getMiniBuffer().getSingleTaskRunner());
			}

			// input
			int len = buffer.getNumberOfStockedElement();
			StringBuilder builder = new StringBuilder();
			for(int i=1;len-i>=0;i++) {
				KyoroString line = buffer.get(len-i);
				if(line.includeLF()) {
					break;
				}
				builder.append(line.toString());
			}
			//
			try {
				buffer.setCursor(buffer.get(buffer.getCol()).length(), buffer.getCol());
			} catch(Throwable e) {
				e.printStackTrace();
			}finally {
			}
			buffer.crlf();
			//
			//
			mCmd.input(builder.toString());
			builder.setLength(0);
			builder = null;
		}
	}

}


/*
{
	String tmp = builder.toString().replace("^[\\s]*|[\\s]*$", "");
	String [] a = tmp.split("\\s");
//	android.util.Log.v("kiyo", "--0000--"+a.length+","+tmp);
	if(a.length >= 2&&"cd".equals(""+a[0])){
		String curDirS = CrossCuttingProperty.getInstance().getProperty("user.dir","/");
		File curDir = new File(curDirS);
		File curAbFil = new File(a[1]);
		File curFil = new File(curDir, a[1]);
//		android.util.Log.v("kiyo", "--0001--"+curDirS);
		if(curAbFil.exists()&& curAbFil.isDirectory()) {
//			android.util.Log.v("kiyo", "--0002--");
			//absolute path
			CrossCuttingProperty.getInstance().setProperty("user.dir",curAbFil.getAbsolutePath());
			return;
		}
		else if(curFil.exists()&&curDir.isDirectory()) {
//			android.util.Log.v("kiyo", "--0003--");
			CrossCuttingProperty.getInstance().setProperty("user.dir",curFil.getAbsolutePath());
			return;						
		} 
		else {
			android.util.Log.v("kiyo", "--0004--");
			// 
		}
	}
	else if(a.length >= 1&&"pwd".equals(""+a[0])) {
		String line = CrossCuttingProperty.getInstance().getProperty("user.dir","/");
		buffer.pushCommit(line, 1);
		buffer.crlf();
		return;
	}
}
*/