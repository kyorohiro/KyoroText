package info.kyorohiro.helloworld.util.shell;

import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.util.ShellCommand;
import info.kyorohiro.helloworld.util.ShellCommand.CommandException;
import info.kyorohiro.helloworld.util.SingleTaskRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Cmd {
	private VirtualFile mVFile = null;
	private SingleTaskRunner mRunner = new SingleTaskRunner();
	private CmdRunner mCmdTask = new CmdRunner();

	public Cmd(VirtualFile vFile, SingleTaskRunner runner) {
		if(runner == null) {
			mRunner = new SingleTaskRunner();
		} else {
			mRunner = runner;
		}
		mVFile = vFile;
	}

	public VirtualFile getVFile() {
		return mVFile;
	}

	public void start(Runnable nextTask) {
		mRunner.startTask(nextTask);
	}

	public void input(String input) {
		if (mRunner.getCurrentTask()!= this||!mRunner.isAlive()) {
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
		mRunner.endTask();
	}

	public class CmdRunner implements Runnable {
		private LinkedList<String> mInput = new LinkedList<String>();

		public synchronized void input(String input) {
			android.util.Log.v("kiyo","-input-"+input);
			mInput.addLast(input);
		}

		private synchronized void write(ShellCommand command) throws InterruptedException {
			if(0<mInput.size()) {
				android.util.Log.v("kiyo","-write-");
			}
			for(String in : mInput) {
				write(command, in);
				Thread.sleep(100);
			}
			mInput.clear();
		}

		private synchronized void write(ShellCommand command, String input) {
			android.util.Log.v("kiyo","-write-"+input);
			try {
				PrintWriter pwriter = new PrintWriter(command.getOutputStream());
				pwriter.println(input);
				pwriter.flush();
			} catch (CommandException e1) {
				e1.printStackTrace();
			}
		}

		public void run() {
			android.util.Log.v("kiyo","---------------D1-----------");
			ShellCommand command = new ShellCommand();
			String shellPath = CrossCuttingProperty.getInstance().getProperty("bin.sh", "/system/bin/sh");
			command.start(shellPath);

			InputStream output = null;
			InputStream error = null;

			BufferedReader outputReader = null;
			BufferedReader errorReader = null;

			char[] text = new char[127];
			byte[] buff = new byte[512];
			try {
				output = command.getInputStream();
				error = command.getErrorStream();
				InputStreamReader outputISR = new InputStreamReader(output);
				outputReader = new BufferedReader(outputISR);
				errorReader = new BufferedReader(new InputStreamReader(error));
				while (true) {
					if(mVFile.isClose()) {
						break;
					}
					write(command);
					if (output.available() != 0) {
						String line = null;
						do {
							int l = output.read(buff);
							mVFile.addChunk(buff, 0, l);
							Thread.yield();
							Thread.sleep(0);
						} while (line != null);
					}
					if (error.available() != 0) {
						String line = null;
						do {
							int l = error.read(buff);
							mVFile.addChunk(buff, 0, l);
							Thread.yield();
							Thread.sleep(0);
						} while (line != null);
					}
					if (!command.isAlive() && error.available() == 0
							&& output.available() == 0) {
						break;
					} else {
						Thread.sleep(100);
					}
				}
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
					if (errorReader != null) {
						errorReader.close();
					}
					if (error != null) {
						error.close();
					}
					if (output != null) {
						output.close();
					}
					if (outputReader != null) {
						outputReader.close();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				android.util.Log.v("kiyo","---------------D5-----------");
			}
		}
	}

}