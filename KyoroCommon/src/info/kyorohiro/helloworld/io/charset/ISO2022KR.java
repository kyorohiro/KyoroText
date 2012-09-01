package info.kyorohiro.helloworld.io.charset;

import info.kyorohiro.helloworld.io.MarkableFileReader;

import java.io.IOException;


//
// * 一文字だけを対象とした、ESCは記録しない、LockするESCだけ記録する。
// * 各々、バッファごとに記録する。
//
// * LFが現れた時だけ、改行ごとASCIIに戻す仕様とする。
// ※ ISO2022では、改行前にASCIIに戻すためのESCをいれる決まるになっているが、
//   念のため守られていない事も考慮しておく。
public class ISO2022KR extends ISO2022 {

	// ESCが省略された場合は、G1DM4 (C)が設定されたとみなすことにする。Ie FireFox
	private byte[] mCurrentESC = DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'C');
	private byte[] mCurrentInvoke = ISO2022.INVOKED_LS0;
	
	private G1 mG1 = new G1();
	private Invoke mInvoke = new Invoke();

	public byte[] currentG0() {return null;}
	public byte[] currentG1() {return mCurrentESC;}
	public byte[] currentG1Invoke() {return mCurrentInvoke;}
	public byte[] currentG2() {return null;}
	public byte[] currentG3() {return null;}
	public byte[] currentGL() {return null;}
	public byte[] currentGR() {return null;}

	public int currentEscape(byte[] escape) {
		int p = 0;
		int len = 0;
		if(mCurrentESC  == null&&mCurrentInvoke==null){
			return 0;
		}
		if(mCurrentESC  != null){
			for(int i=0;i<mCurrentESC .length;i++) {
				escape[p++] = mCurrentESC[i];
			}
			len += mCurrentESC.length;
		}
		if(mCurrentInvoke != null){
			for(int i=0;i<mCurrentInvoke.length;i++) {
				escape[p++] = mCurrentInvoke[i];
			}
			len += mCurrentInvoke.length;
		}
		return len;
	}

	public void update(MarkableFileReader v) {
		try {
			v.pushMark();
			if (LF(v)) {;
			} else if (findG1Designated(v)){;
			} else if (findG1Invoked(v)){;}
		} finally {
			v.backToMark();
		}
	}

	private boolean findG1Designated(MarkableFileReader v) {
		return mG1.match(v);
	}

	private boolean findG1Invoked(MarkableFileReader v) {
		return mInvoke.match(v);
	}

	public static final byte[][] ISO_2022_KR_DESIGNATED_LOCK = {
		DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'C'),//KS X 1001-1992 single
	};

	public static final byte[][] ISO_2022_KR_INVOKE = {
		ISO2022.INVOKED_LS0,
		ISO2022.INVOKED_LS1
	};


	private class G1 extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public G1() {
			super(ISO_2022_KR_DESIGNATED_LOCK);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentESC = matchedData;
		}
	}

	private class Invoke extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public Invoke() {
			super(ISO_2022_KR_INVOKE);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentInvoke = matchedData;
		}
	}

}