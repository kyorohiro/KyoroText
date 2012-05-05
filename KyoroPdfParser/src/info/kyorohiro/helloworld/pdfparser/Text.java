package info.kyorohiro.helloworld.pdfparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//
// if this library use bic size text. 
// library could not load all text to memory.
// this class load a part text to memory. other save external storage. 
//
public class Text {

	private CharSequence mText;
	private int mCurrentPosition = 0;
	public static Pattern  sSpace = Pattern.compile("\\s");

	public Text(String text) {
		mText = text;
	}

	public void next() {
		mCurrentPosition++;
	}

	public CharSequence next(String pattern){
		Pattern regex = Pattern.compile(pattern);
		CharSequence target = mText.subSequence(mCurrentPosition, mText.length());
		Matcher m = regex.matcher(
				mText.subSequence(mCurrentPosition, 
				mText.length()));
		if(m.find()) {
			mCurrentPosition += m.end();
			m.start();
			return target.subSequence(0, m.end());
		} else {
			//todo throw exception
			return "error";
		}
	}

	public void reset(int position){
		mCurrentPosition = position;
	}

	public Character getCharacter() {
		// todo now no guard 
		try {
			return mText.charAt(mCurrentPosition);
		} catch (Throwable t) {
			return '\0';
		}
	}

	public boolean muchHead(String pattern) {
		Pattern regex = Pattern.compile(pattern);
		Matcher m = regex.matcher(
				mText.subSequence(mCurrentPosition, 
				mText.length()));
		return m.find();
	}
}
