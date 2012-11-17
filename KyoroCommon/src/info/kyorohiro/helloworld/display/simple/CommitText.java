package info.kyorohiro.helloworld.display.simple;

public class CommitText {
		private CharSequence mText = null;
		private int mNewCursorPosition = 0;
		//
		// ��ŏC��
		private boolean mIsKeyCode = false;
		private int mKeycode = 0;

		@Deprecated
		public CommitText(int keycode) {
			mIsKeyCode = true;
			mKeycode = keycode;
		}

		public CommitText(CharSequence text, int newCursorPosition) {
			mText = text;
			mNewCursorPosition = newCursorPosition;
		}
		public CharSequence getText() {
			return mText;
		}
		public int getNewCursorPosition() {
			return mNewCursorPosition;
		}
		public boolean isKeyCode() {
			return mIsKeyCode;
		}
		public int getKeyCode() {
			return mKeycode;
		}
	}
