package info.kyorohiro.helloworld.logcat.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.Intent;

import info.kyorohiro.helloworld.logcat.KyoroApplication;
import info.kyorohiro.helloworld.logcat.util.Logcat;
import info.kyorohiro.helloworld.util.CyclingList;

public class SendCurrentLogTask extends Thread {

	private final Logcat mLogcat = new Logcat();
	private String mOption = "-d -v time";
	private Context mContext = null;

	public SendCurrentLogTask(Context context){
		mContext = context;
	}

	public void terminate() {
		if(mLogcat != null){
			mLogcat.terminate();
		}
		interrupt();
	}

	public void run() {
		mLogcat.start(mOption);
		CyclingList<String> temp = new CyclingList<String>(400);
		try {
			showMessage("start to collect log.");
			InputStream stream = mLogcat.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			while(true) {
				if(!reader.ready()){
					//				if(0<input.available()){
					if(!mLogcat.isAlive()){
						break;
					}
					else {
						Thread.sleep(100);
					}
				} else {
					temp.add(reader.readLine()+"\r\n");
					Thread.yield();
				}
			}
			showMessage("successed to collect log. and start to ticket for sending mail.");
			StringBuilder builder = new StringBuilder();
			int len = temp.getNumberOfStockedElement();
			for(int i=0;i<len;i++) {
				builder.append(temp.get(i));
			}
			sendMail("KyoroLogcat log(logcat -d)", "xxx@example.com", builder);
		} catch (InterruptedException e) {
			// expected exception
		} catch (Throwable e) {
			e.printStackTrace();
			showMessage("failed to collect log.");
		}
		finally {
			mLogcat.terminate();
		}
	}

	public void showMessage(String message) {
		try {
			KyoroApplication.showMessage(message);
		} catch(Throwable e){
			e.printStackTrace();
		}
	}

	public void sendMail(String subject, String address, StringBuilder body){
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent .setType("plain/text");
		emailIntent .putExtra(android.content.Intent.EXTRA_EMAIL,  new String[]{address});
		emailIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent .putExtra(android.content.Intent.EXTRA_TEXT, body.toString());
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(emailIntent);
	}
}