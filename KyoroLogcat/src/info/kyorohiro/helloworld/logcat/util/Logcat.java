package info.kyorohiro.helloworld.logcat.util;

import java.io.InputStream;
import java.io.OutputStream;

public class Logcat extends Object {

	private String mOption = "";
	private static final String sAction = "logcat";
	private Process mLogcatProcess = null;

	public Logcat(){
		super();
	}

	public synchronized void start(String option) {
		stop();
		mOption = option;
		Runtime r = Runtime.getRuntime();
		String command = sAction + " " + mOption;
		try {
			mLogcatProcess = r.exec(command);
		} catch (Exception e) {
			;
		}
	}

	public String getOption() {
		return mOption;
	}

	public boolean isAlive() {
		if (mLogcatProcess == null) {
			return false;
		}
		try {
			mLogcatProcess.exitValue();
			return false;
		} catch(IllegalThreadStateException  e) {
			return true;
		}
	}

	public void terminate() {
		stop();
	}

	private synchronized void stop() {
		if (mLogcatProcess != null) {
			mLogcatProcess.destroy();
			mLogcatProcess = null;
		}
	}

	public InputStream getInputStream() throws LogcatException {
		ActionWithForceErrorCheck action = new ActionWithForceErrorCheck(
				new Action() { public Object run() {
					return mLogcatProcess.getInputStream();
				}}
		);
		return (InputStream)action.getResult();
	}

	public OutputStream getOutputStream() throws LogcatException {
		ActionWithForceErrorCheck action = new ActionWithForceErrorCheck(
				new Action() { public Object run() {
					return mLogcatProcess.getOutputStream();
				}}
		);
		return (OutputStream)action.getResult();
	}

	public InputStream getErrorStream() throws LogcatException {
		ActionWithForceErrorCheck action = new ActionWithForceErrorCheck(
				new Action() { public Object run() {
					return mLogcatProcess.getErrorStream();
				}}
		);
		return (InputStream)action.getResult();
	}

	@SuppressWarnings("serial")
	public static class LogcatException extends Exception {
		public static final String CODING_ERROR_SETTED_NULL_ACTION = "coding error: setted null action";
		public static final String UNEXPECTED_ERROR_AND_RETRUN_NULL_FROM_SYSTEM = "sorry unexpected error. pf return null";
		public static final String NEED_TO_CALL_START_METHOD = "please call Logcar#start()";
		private String mLogcatInfo = "";

		public LogcatException(String logcatInfo) {
			super(logcatInfo);
			mLogcatInfo = logcatInfo;
		}

		public String getLogcatInfo() {
			return mLogcatInfo;
		}
	}

	private class ActionWithForceErrorCheck {
		Action mAction = null;

		public ActionWithForceErrorCheck(Action action){
			mAction = action;
		}

		public Object getResult()throws LogcatException {
			try {
				if (mAction == null) {
					throw new LogcatException(LogcatException.CODING_ERROR_SETTED_NULL_ACTION);					
				}
				if (mLogcatProcess == null) {
					throw new LogcatException(LogcatException.NEED_TO_CALL_START_METHOD);
				}
				Object obj = mAction.run();
				if (obj == null) {
					throw new LogcatException(
							LogcatException.UNEXPECTED_ERROR_AND_RETRUN_NULL_FROM_SYSTEM);
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
