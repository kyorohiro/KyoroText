package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.ShellCommand;
import info.kyorohiro.helloworld.util.ShellCommand.CommandException;

//
// まずは、課題抽出用に書いた、基本書き直す予定
// 
public class ShellTaskDone implements Task {

	@Override
	public String getCommandName() {
		return "shell-task-done";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		buffer.clearYank();

		//
		// share MiniBuffer thread
		MiniBuffer miniBuffer = BufferManager.getManager().getMiniBuffer();
		miniBuffer.startTask(new A(BufferManager.getManager().getFocusingTextViewer()));
	}

	public class A implements Runnable {
		private TextViewer mTarget = null;
		public A(TextViewer target) {
			mTarget = target;
		}

		@Override
		public void run() {
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
			//
			try {
				buffer.setCursor(buffer.get(buffer.getCol()).length(), buffer.getCol());
			} catch(Throwable e) {
				e.printStackTrace();
			}finally {
			}
			buffer.crlf();
			//

			{
				String tmp = builder.toString().replace("^[\\s]*|[\\s]*$", "");
				String [] a = tmp.split("\\s");
//				android.util.Log.v("kiyo", "--0000--"+a.length+","+tmp);
				if(a.length >= 2&&"cd".equals(""+a[0])){
					String curDirS = CrossCuttingProperty.getInstance().getProperty("user.dir","/");
					File curDir = new File(curDirS);
					File curAbFil = new File(a[1]);
					File curFil = new File(curDir, a[1]);
//					android.util.Log.v("kiyo", "--0001--"+curDirS);
					if(curAbFil.exists()&& curAbFil.isDirectory()) {
//						android.util.Log.v("kiyo", "--0002--");
						//absolute path
						CrossCuttingProperty.getInstance().setProperty("user.dir",curAbFil.getAbsolutePath());
						return;
					}
					else if(curFil.exists()&&curDir.isDirectory()) {
//						android.util.Log.v("kiyo", "--0003--");
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
			command.start(""+builder.toString());

			InputStream output = null;
			InputStream error  = null;

			BufferedReader outputReader = null;
			BufferedReader errorReader = null;
			SimpleStage stage = BufferManager.getManager().getStage(BufferManager.getManager());

			android.util.Log.v("kiyo", "--2--"+builder.toString());
			char[] text = new char[127];
			byte[] buff = new byte[512];
			try {
				output = command.getInputStream();
				error  = command.getErrorStream();
				InputStreamReader outputISR = new InputStreamReader(output);
				outputReader = new BufferedReader(outputISR);
				errorReader = new BufferedReader(new InputStreamReader(error));
				android.util.Log.v("kiyo", "--3--");
				while(true) {

					if(output.available() != 0){
						String line = null;
						do {
							int l = output.read(buff);
							mTarget.getVFile().addChunk(buff,0, l);
							Thread.yield();
							Thread.sleep(0);
						} while(line != null);
					}
					if(error.available() != 0){
						String line = null;
						do {
							int l = error.read(buff);
							mTarget.getVFile().addChunk(buff,0, l);
							Thread.yield();
							Thread.sleep(0);
						} while(line != null);
					}
					if(!command.isAlive()&&error.available()==0&&output.available()==0){
						break;
					} else {
						Thread.sleep(100);						
					}
				}
				android.util.Log.v("kiyo", "--5--");
			} catch (CommandException e) {
				e.printStackTrace();				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();				
			} finally {
				try {
					if(errorReader != null) {
						errorReader.close();
					}
					if(error != null) {
						error.close();
					}
					if(output != null) {
						output.close();
					}
					if(outputReader != null) {
						outputReader.close();
					}
				} catch (Throwable e) {
					e.printStackTrace();				
				}
			}
			android.util.Log.v("kiyo", "--6--");
		}
	}

}
