package info.kyorohiro.helloworld.util.shell;

import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.util.shell.CLIAppKicker.CLIAppKickerException;
import info.kyorohiro.helloworld.util.SingleTaskRunner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;

public class CmdSession {
	private VirtualFile mVFile = null;
	private SingleTaskRunner mTaskRunner = new SingleTaskRunner();
	private CmdRunner mCmdTask = new CmdRunner();

	public void log(String message) {
//		android.util.Log.v("kiyo","#CmdSession#"+message);
	}

	public CmdSession(VirtualFile vFile, SingleTaskRunner runner) {
		if(runner == null) {
			mTaskRunner = new SingleTaskRunner();
		} else {
			mTaskRunner = runner;
		}
		mVFile = vFile;
	}

	public VirtualFile getVFile() {
		return mVFile;
	}

	public void start(Runnable nextTask) {
		mTaskRunner.startTask(nextTask);
	}

	public void input(String input) {
		log("input:"+input);
		if (!mTaskRunner.isAlive()) {
			log("input:"+mTaskRunner.isAlive());
			start(mCmdTask = new CmdRunner());
		}
		// 念のためガード
		if(mCmdTask != null) {
			mCmdTask.input(input);
		} else {
			System.out.println("#KyoroCommon unexpected W0120123");
		}
	}

	public void end() {
		mTaskRunner.endTask();
	}

	public class CmdRunner implements Runnable {
		private LinkedList<String> mInput = new LinkedList<String>();

		public synchronized void input(String input) {
			log("_input:"+input);
			mInput.addLast(input);
		}

		private synchronized void write(CLIAppKicker command) throws InterruptedException {
			for(String in : mInput) {
				write(command, in);
				Thread.sleep(100);
			}
			mInput.clear();
		}

		private synchronized void write(CLIAppKicker command, String input) {
			log("_write:"+input);
			try {
				PrintWriter pwriter = new PrintWriter(command.getOutputStream());
				pwriter.println(input);
				pwriter.flush();
			} catch (CLIAppKickerException e1) {
				e1.printStackTrace();
			}
		}

		private void showScreen(InputStream input, byte[] buff) throws IOException, InterruptedException {
			int len = -1;
			while(input.available() > 0){
				len = input.read(buff);
				if(len<=0) {
					break;
				}
				android.util.Log.v("kiyo","----"+new String(buff,0,len));
				mVFile.addChunk(buff, 0, len);
				mVFile.addChunk(("\r\n").getBytes());
				//mVFile.isLoading(false);
				
				Thread.yield();
				Thread.sleep(0);
			}
		}

		public void run() {
			log("---------------D1-----------");
			CLIAppKicker cliAppKicker = new CLIAppKicker();
			String shellPath = CrossCuttingProperty.getInstance().getProperty("bin.sh", "/system/bin/sh");
			cliAppKicker.start(shellPath);

			InputStream input = null;
			InputStream error = null;

			byte[] buff = new byte[512];
			try {
				input = new BufferedInputStream(cliAppKicker.getInputStream());
				error = new BufferedInputStream(cliAppKicker.getErrorStream());
				while (true) {
					if(mVFile.isClose()) {
						break;
					}
					write(cliAppKicker);
					if (input.available() != 0) {
						showScreen(input, buff);
					}
					if (error.available() != 0) {
						showScreen(error, buff);
					}
					if (!cliAppKicker.isAlive() && error.available() == 0 && input.available() == 0) {
						break;
					} else {
						Thread.sleep(100);
					}
				}
			} catch (CLIAppKickerException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				try {
					if (error != null) {
						error.close();
					}
					if (input != null) {
						input.close();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				log("---------------D5-----------");
			}
		}
	}

}