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
public class ISO2022JP extends ISO2022 {

	private byte[] mCurrentGL = null;
	private byte[] mCurrentGR = null;
	private byte[] mCurrentG0 = null;
	private byte[] mCurrentG2 = null;
	private G0 mG0 = new G0();
	private G2 mG2 = new G2();

	public byte[] currentG0() {
		return mCurrentG0;
	}
	public byte[] currentG1() {return null;}
	public byte[] currentG2() {
		return mCurrentG2;
	}
	public byte[] currentG3() {return null;}
	public byte[] currentGL() {
		return mCurrentGL;
	}
	public byte[] currentGR() {
		return mCurrentGR;
	}

	public int currentEscape(byte[] escape) {
		int p = 0;
		int len = 0;
		if(mCurrentG0 == null&&mCurrentG2==null){
			return 0;
		}
		if(mCurrentG0 != null){
			for(int i=0;i<mCurrentG0.length;i++) {
				escape[p++] = mCurrentG0[i];
			}
			len += mCurrentG0.length;
		}
		if(mCurrentG2 != null){
			for(int i=0;i<mCurrentG2.length;i++) {
				escape[p++] = mCurrentG2[i];
			}
			len += mCurrentG2.length;
		}
		return len;
	}

	public void update(VirtualMemory v) {
		try {
			v.pushMark();
			if (LF(v)) {;
			} else if (G0(v)){;
			} else if (G2(v)){;}
		} finally {
			v.backToMark();
		}
	}

	private boolean G0(VirtualMemory v) {
		return mG0.match(v);
	}

	private boolean G2(VirtualMemory v) {
		return mG0.match(v);
	}

	public static final byte[][] ISO_2022_JP_DESIGNATED_G0 = {
			// ISO-2022-JP
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZD4, 'B'),// ascii
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZD4, 'J'),// JIS X 0201-1976
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_1, '@'),// JIS X
																// 0208-1978
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_1, 'B'),// JIS X
																// 0208-1983
			// ISO-2022-JP-1
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'D'),// JIS X
																// 0212-1990
			// ISO-2022-JP-2
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_1, 'A'),// GB 2312-1980
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'C'),// KS X
																// 1001-1992
			// ISO-2022-JP-3
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'O'),// JIS X
																// 0213:2000
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'P'),// JIS X
																// 0213:2000
			// ISO-2022-JP-2004
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'Q'),// JIS X
																// 0213:2004
	};

	public static final byte[][] ISO_2022_JP_DESIGNATED_G2 = {
			// ISO-2022-JP-2
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_G2D6, 'A'),// ISO/IEC 8859-1 single lock
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_G2D6, 'F'),// ISO/IEC 8859-7 single lock
	};

	public static final byte[][] ISO_2022_JP_INVOKE = {
	// ISO-2022-JP-2
	ISO2022.INVOKED_LS2 
	};

	private class G0 extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public G0() {
			super(ISO_2022_JP_DESIGNATED_G0);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentG0 = mCurrentGL = mCurrentGR =matchedData;
		}
	}
	private class G2 extends ActionForMatechingEscape implements ObserverForMatechingEscape {
		public G2() {
			super(ISO_2022_JP_DESIGNATED_G2);
			setObserver(this);
		}
		@Override
		public void matched(byte[] matchedData) {
			mCurrentG2 = matchedData;
		}
	}

}