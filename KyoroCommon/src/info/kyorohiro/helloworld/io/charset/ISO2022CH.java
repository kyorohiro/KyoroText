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
public class ISO2022CH extends ISO2022 {

	private byte[] mCurrentESC = null;
	private byte[] mCurrentInvoke = null;

	private G1 mG1 = new G1();
	private Invoke mInvoke = new Invoke();

	public byte[] currentG0() {return null;}
	public byte[] currentG1() {return mCurrentESC;}
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

	public void update(VirtualMemory v) {
		try {
			v.pushMark();
			if (LF(v)) {;
			} else if (doG1(v)){;
			} else if (doInvoke(v)){;}
		} finally {
			v.backToMark();
		}
	}

	private boolean doG1(VirtualMemory v) {
		return mG1.match(v);
	}

	private boolean doInvoke(VirtualMemory v) {
		return mInvoke.match(v);
	}

	public static final byte[][] ISO_2022_CH_DESIGNATED_LOCK = {
		DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'A'),//GB 2312-80
		DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'G'),//CNS 11643-1992
		DESIGNATED(ISO2022.DESIGNATED_G2DM4, 'H'),//CNS 11643-1992  // single

		//ext
		DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'E'),//CNS 11643-1992
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'I'),//CNS 11643-1992 3 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'J'),//CNS 11643-1992 4 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'K'),//CNS 11643-1992 5 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'L'),//CNS 11643-1992 6 //single
		DESIGNATED(ISO2022.DESIGNATED_G3DM4, 'M'),//CNS 11643-1992 7 //single
	};

	public static final byte[][] ISO_2022_CH_INVOKE = {
		ISO2022.INVOKED_LS0, //single
		ISO2022.INVOKED_LS1,//single
		ISO2022.INVOKED_LS2
	};


	private class G1 extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public G1() {
			super(ISO_2022_CH_DESIGNATED_LOCK);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentESC = matchedData;
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