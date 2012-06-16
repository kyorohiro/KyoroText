package info.kyorohiro.helloworld.io.charset;

import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.IOException;


//
// * 一文字だけを対象とした、ESCは記録しない、LockするESCだけ記録する。
// * 各々、バッファごとに記録する。
//
// * LFが現れた時だけ、改行ごとASCIIに戻す仕様とする。
// ※ ISO2022では、改行前にASCIIに戻すためのESCをいれる決まるになっているが、
//   念のため守られていない事も考慮しておく。
public class ISO2022CN extends ISO2022 {

	private byte[] mCurrentESC_G1 = null;
	private byte[] mCurrentESC_G2 = null;
	private byte[] mCurrentESC_G3 = null;
	private byte[] mCurrentInvoke = null;

	private G1 mG1 = new G1();
	private G2 mG2 = new G2();
	private G3 mG3 = new G3();
	private Invoke mInvoke = new Invoke();

	public byte[] currentG1() {return mCurrentESC_G1;}
	public byte[] currentG2() {return mCurrentESC_G2;}
	public byte[] currentG3() {return mCurrentESC_G3;}
	public byte[] currentG1Invoke() {return mCurrentInvoke;}

	public int currentEscape(byte[] escape) {
		return 0;
	}

	public void update(VirtualMemory v) {
		try {
			v.pushMark();
			if (LF(v)) {;
			} else if (doG1(v)){;
			} else if (doG2(v)){;
			} else if (doG3(v)){;
			} else if (doInvoke(v)){;}
		} finally {
			v.backToMark();
		}
	}

	private boolean doG1(VirtualMemory v) {
		return mG1.match(v);
	}
	private boolean doG2(VirtualMemory v) {
		return mG2.match(v);
	}
	private boolean doG3(VirtualMemory v) {
		return mG3.match(v);
	}

	private boolean doInvoke(VirtualMemory v) {
		return mInvoke.match(v);
	}

	public static final byte[][] ISO_2022_CN_DESIGNATED_G1 = {
		DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'A'),//GB 2312-80
		DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'G'),//CNS 11643-1992
		//ext
		DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'E'),//CNS 11643-1992
	};

	public static final byte[][] ISO_2022_CH_DESIGNATED_G2 = {
		DESIGNATED(ISO2022.DESIGNATED_G2DM4, 'H'),//CNS 11643-1992  // single
	};

	public static final byte[][] ISO_2022_CN_DESIGNATED_G3 = {
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'I'),//CNS 11643-1992 3 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'J'),//CNS 11643-1992 4 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'K'),//CNS 11643-1992 5 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'L'),//CNS 11643-1992 6 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'M'),//CNS 11643-1992 7 //single
	};

	public static final byte[][] ISO_2022_CH_INVOKE = {
		ISO2022.INVOKED_LS0,
		ISO2022.INVOKED_LS1,
//		ISO2022.INVOKED_LS2 // single
//		ISO2022.INVOKED_SS3 // single
	};


	private class G1 extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public G1() {
			super(ISO_2022_CN_DESIGNATED_G1);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentESC_G1 = matchedData;
		}
	}
	private class G2 extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public G2() {
			super(ISO_2022_CH_DESIGNATED_G2);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentESC_G2 = matchedData;
		}
	}

	private class G3 extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public G3() {
			super(ISO_2022_CN_DESIGNATED_G3);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentESC_G3 = matchedData;
		}
	}

	private class Invoke extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public Invoke() {
			super(ISO_2022_CH_INVOKE);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentInvoke = matchedData;
		}
	}

}