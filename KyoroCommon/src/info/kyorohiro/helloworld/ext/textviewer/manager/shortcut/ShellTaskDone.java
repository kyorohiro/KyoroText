package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward.ISearchForwardTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.ShellCommand;
import info.kyorohiro.helloworld.util.ShellCommand.CommandException;

public class ShellTaskDone implements Task {

	@Override
	public String getCommandName() {
		return "shell-task-done";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		BufferManager.getManager().getApplication().showMessage("-- "+getCommandName());
		buffer.clearYank();
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
			android.util.Log.v("kiyo", "--1--");
			ShellCommand command = new ShellCommand();
			EditableLineViewBuffer buffer = (EditableLineViewBuffer)mTarget.getLineView().getLineViewBuffer();

			int len = buffer.getNumberOfStockedElement();
			StringBuilder builder = new StringBuilder();
			for(int i=1;len-i>=0;i++) {
				KyoroString line = buffer.get(len-i);
				if(line.includeLF()) {
					break;
				}
				builder.append(line.toString());
			}
			command.start(""+builder.toString());
			buffer.crlf();

			InputStream output = null;
			InputStream error  = null;

			BufferedReader outputReader = null;
			BufferedReader errorReader = null;
			android.util.Log.v("kiyo", "--2--"+builder.toString());
			try {
				output = command.getInputStream();
				error  = command.getErrorStream();
				InputStreamReader outputISR = new InputStreamReader(output);
				outputReader = new BufferedReader(outputISR);
				errorReader = new BufferedReader(new InputStreamReader(error));
				android.util.Log.v("kiyo", "--3--");
				while(true) {

					//if(output.available() != 0){
					{
						String line = null;
						do {
						line = outputReader.readLine();
						if(line != null) {
							buffer.pushCommit(line, 1);
							buffer.crlf();
						}
						Thread.yield();
						} while(line != null);
					}
					{
						String line = null;
						do {
						line = errorReader.readLine();
						if(line != null) {
							buffer.pushCommit(line, 1);
							buffer.crlf();
						}
						Thread.yield();
						} while(line != null);
					}
					Thread.sleep(10);
					if(!command.isAlive()&&error.available()==0&&output.available()==0){
						android.util.Log.v("kiyo", "--1-3-");
						break;
					} 
				}
				android.util.Log.v("kiyo", "--5--");
			} catch (CommandException e) {
				e.printStackTrace();				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			android.util.Log.v("kiyo", "--6--");
		}
	}
	
}
