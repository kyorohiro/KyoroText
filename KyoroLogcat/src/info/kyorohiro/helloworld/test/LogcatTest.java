package info.kyorohiro.helloworld.test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.logcat.util.Logcat;
import info.kyorohiro.helloworld.logcat.util.Logcat.LogcatException;
import junit.framework.TestCase;

public class LogcatTest extends TestCase {

	public void testHello() {
		Logcat logcat = new Logcat();
		try {
			logcat.start("");
			Thread.sleep(1000);
			android.util.Log.v("kyoro","hello");
			if(!includeInLog(logcat, ".*kyoro.*hello.*")){
				assertTrue(false);
			}
		} catch (LogcatException e) {
			e.printStackTrace();
			assertTrue(e.toString(), false);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(e.toString(), false);
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertTrue(e.toString(), false);
		} finally {
			logcat.terminate();			
		}
	}

	public void testNotFound() {
		Logcat logcat = new Logcat();
		try {
			logcat.start("");
			Thread.sleep(1000);
			if(includeInLog(logcat, ".*tekitouna mojiretu.*")){
				assertTrue(false);
			}
		} catch (LogcatException e) {
			e.printStackTrace();
			assertTrue(e.toString(), false);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(e.toString(), false);
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertTrue(e.toString(), false);
		} finally {
			logcat.terminate();			
		}

	}


	private boolean includeInLog(Logcat logcat, String regex) throws LogcatException, IOException{
		InputStream stream = logcat.getInputStream();
		DataInputStream ds = new DataInputStream(stream);
		Pattern pattern = Pattern.compile(regex);

		while(true){
			int available = ds.available();
			if(0>=available){
				return false;
			}
			String line = ds.readLine();
			if(line == null){
				return false;
			}
			Matcher m = pattern.matcher(line);
			if(m.matches()){
				break;
			}
		}
		return true;
	}
}
