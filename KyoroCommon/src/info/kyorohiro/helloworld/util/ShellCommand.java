package info.kyorohiro.helloworld.util;


import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class ShellCommand extends Object {

	private Process mCommandProcess = null;

	public ShellCommand(){
		super();
	}

	public synchronized void start(String command) {
		stop();
		Runtime r = Runtime.getRuntime();
		try {
			android.util.Log.v("kiyo","---CMD CMD");
			r.availableProcessors();
			mCommandProcess = r.exec(command//);
			, null, new File(CrossCuttingProperty.getInstance().getProperty("user.dir","/")));
		} catch (Exception e) {
			;
		}
	}


	public boolean isAlive() {
		if (mCommandProcess == null) {
			return false;
		}
		try {
			mCommandProcess.exitValue();
			return false;
		} catch(IllegalThreadStateException  e) {
			return true;
		}
	}

	public void terminate() {
		stop();
	}

	private synchronized void stop() {
		if (mCommandProcess != null) {
			mCommandProcess.destroy();
			mCommandProcess = null;
		}
	}

	public InputStream getInputStream() throws CommandException {
		ActionWithForceErrorCheck action = new ActionWithForceErrorCheck(
				new Action() { public Object run() {
					return mCommandProcess.getInputStream();
				}}
		);
		return (InputStream)action.getResult();
	}

	public OutputStream getOutputStream() throws CommandException {
		ActionWithForceErrorCheck action = new ActionWithForceErrorCheck(
				new Action() { public Object run() {
					return mCommandProcess.getOutputStream();
				}}
		);
		return (OutputStream)action.getResult();
	}

	public InputStream getErrorStream() throws CommandException {
		ActionWithForceErrorCheck action = new ActionWithForceErrorCheck(
				new Action() { public Object run() {
					return mCommandProcess.getErrorStream();
				}}
		);
		return (InputStream)action.getResult();
	}

	@SuppressWarnings("serial")
	public static class CommandException extends Exception {
		public static final String CODING_ERROR_SETTED_NULL_ACTION = "coding error: setted null action";
		public static final String UNEXPECTED_ERROR_AND_RETRUN_NULL_FROM_SYSTEM = "sorry unexpected error. pf return null";
		public static final String NEED_TO_CALL_START_METHOD = "please call ShellCommand#start()";
		private String mCommandInfo = "";

		public CommandException(String logcatInfo) {
			super(logcatInfo);
			mCommandInfo = logcatInfo;
		}

		public String getLogcatInfo() {
			return mCommandInfo;
		}
	}

	private class ActionWithForceErrorCheck {
		Action mAction = null;

		public ActionWithForceErrorCheck(Action action){
			mAction = action;
		}

		public Object getResult()throws CommandException {
			try {
				if (mAction == null) {
					throw new CommandException(CommandException.CODING_ERROR_SETTED_NULL_ACTION);					
				}
				if (mCommandProcess == null) {
					throw new CommandException(CommandException.NEED_TO_CALL_START_METHOD);
				}
				Object obj = mAction.run();
				if (obj == null) {
					throw new CommandException(
							CommandException.UNEXPECTED_ERROR_AND_RETRUN_NULL_FROM_SYSTEM);
				}
				return obj;
			} finally {
				mAction = null;
			}
		}
	}

	private static interface Action {
		public Object run();
	}
}
