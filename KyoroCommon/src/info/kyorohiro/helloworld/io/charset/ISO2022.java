package info.kyorohiro.helloworld.io.charset;

import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.IOException;

/**
 * memo: http://en.wikipedia.org/wiki/ISO/IEC_2022
 * http://charset.7jp.net/sjis.html http://tools.ietf.org/html/rfc1468
 * http://www.ecma-international.org/publications/files/ECMA-ST/Ecma-035.pdf
 */
public abstract class ISO2022 {
	public abstract byte[] currentG0();

	public abstract byte[] currentG1();

	public abstract byte[] currentG2();

	public abstract byte[] currentG3();

	public abstract byte[] currentGL();

	public abstract byte[] currentGR();

	public abstract int currentEscape(byte[] escape);

	public abstract void update(VirtualMemory vm);


	public static byte[] DESIGNATED(byte[] code, char character) {
		byte[] buffer = new byte[code.length];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = code[i];
		}
		buffer[code.length - 1] = (byte) character;
		return buffer;
	}

	public static final byte ESC = 0x1B;

	/**
	 * Name:Locking shift zero Effect:GL encodes G0 from now on SI Shift In
	 * Code: 0F
	 */
	public static final byte[] INVOKED_LS0 = { 0x0F };

	/**
	 * Name:Locking shift one Effect:GL encodes G1 from now on SO Shift Out
	 * Code: 0E
	 */
	public static final byte[] INVOKED_LS1 = { 0x0E };

	/**
	 * Name:Locking shift two Effect:GL encodes G2 from now on
	 * Code: ESC 0x6E'n'
	 */
	public static final byte[] INVOKED_LS2 = {ESC, 'n' };

	/**
	 * Name:Locking shift three Effect:GL encodes G3 from now on
	 * Code: ESC 0x6F'o'
	 */
	public static final byte[] INVOKED_LS3 = {ESC, 'o' };

	/**
	 * Name:Single shift two Effect:GL encodes G2 for next character only
	 * Code: 0x8E | ESC 4E(N)(
	 */
	public static final byte[] INVOKED_SS2_1 = {ESC, 'N' };
	public static final byte[] INVOKED_SS2_2 = {(byte) 0x8E };

	/**
	 * Name:Single shift three Effect:GL encodes G3 for next character only
	 * Code: 0x8F | ESC 4F(O)(
	 */
	public static final byte[] INVOKED_SS3 = {ESC, 'O' };
	public static final byte[] INVOKED_SS3_2 = {(byte) 0x8F };

	/**
	 * Name:Locking shift one right Effect:GR encodes G1 from now on
	 * Code: ESC 7F(~)(
	 */
	public static final byte[] INVOKED_LS1R = {ESC, '~'};

	/**
	 * Name:Locking shift two right Effect:GR encodes G2 from now on
	 * Code: ESC 0x7D(})(
	 */
	public static final byte[] INVOKED_LS2R = {ESC, '}' };

	/**
	 * Name:Locking shift three right Effect:GR encodes G3 from now on
	 * Code: ESC 0x7C(|)(
	 */
	public static final byte[] INVOKED_LS3R = { 0x1B, 0x7C, '|' };

	/**
	 * Name:C0-designate Effect:F selects a C0 control character set to be used.
	 * Code: ESC ! F HEx: 1B 21 F
	 */
	public static final byte[] DESIGNATED_CZD = { ESC, '!', 0x00 };

	/**
	 * Name:C1-designate Effect:F selects a C1 control character set to be used.
	 * Code:ESC " F HEX:1B 22 F
	 */
	public static final byte[] DESIGNATED_C1D = { ESC, '\"', 0x00 };

	/**
	 * Name:Designate other coding system Effect:F selects an 8-bit code; use
	 * ESC % @ to return to ISO/IEC 2022. Code: ESC % F HEX: 1B 25 F
	 */
	public static final byte[] DESIGNATED_DOCS_0 = { ESC, '%', 0x00 };

	/**
	 * Name:Designate other coding system Effect:F selects an 8-bit code; there
	 * is no standard way to return. Code: ESC % / F Hex: 1B 25 2F F
	 */
	public static final byte[] DESIGNATED_DOCS_1 = { ESC, '%', '/', 0x00 };

	/**
	 * Name:G0-designate 94-set Effect:F selects a 94-character set to be used
	 * for G0. Code: ESC ( F Hex: 1B 28 F
	 */
	public static final byte[] DESIGNATED_GZD4 = { ESC, '(', 0x00 };

	/**
	 * Name:G1-designate 94-set Effect:F selects a 94-character set to be used
	 * for G1. Code: ESC ) F Hex: 1B 29 F
	 */
	public static final byte[] DESIGNATED_G1D4 = { ESC, ')', 0x00 };

	/**
	 * Name:G1-designate 94-set Effect:F selects a 94-character set to be used
	 * for G2. Code: ESC * F Hex: 1B 2A F
	 */
	public static final byte[] DESIGNATED_G2D4 = { ESC, '*', 0x00 };

	/**
	 * Name:G2-designate 94-set Effect:F selects a 94-character set to be used
	 * for G3. Code: ESC + F Hex: 1B 2B F
	 */
	public static final byte[] DESIGNATED_G3D4 = { ESC, '+', 0x00 };

	/**
	 * Name:G1-designate 96-set Effect:F selects a 96-character set to be used
	 * for G1. Code: ESC - F Hex: 1B 2D F
	 */
	public static final byte[] DESIGNATED_G1D6 = { ESC, '-', 0x00 };

	/**
	 * Name:G2-designate 96-set Effect:F selects a 96-character set to be used
	 * for G2. Code: ESC . F Hex: 1B 2E F
	 */
	public static final byte[] DESIGNATED_G2D6 = { ESC, '.', 0x00 };

	/**
	 * Name:G3-designate 96-set Effect:F selects a 96-character set to be used
	 * for G3. Code: ESC / F Hex: 1B 2F F
	 */
	public static final byte[] DESIGNATED_G3D6 = { ESC, '/', 0x00 };

	/**
	 * Name:G4--designate multibyte 94-set Effect:F selects a 94n-character set
	 * to be used for G0.Code: ESC $ ( F Hex: 1B 24 28 F
	 */
	public static final byte[] DESIGNATED_GZDM4_0 = { ESC, '$', '(', 0x00 };
	public static final byte[] DESIGNATED_GZDM4_1 = { ESC, '$', 0x00 };


	/**
	 * Name:G1-designate multibyte 94-set Effect:F selects a 94n-character set
	 * to be used for G1.Code: ESC $ ) F Hex: 1B 24 29 F
	 */
	public static final byte[] DESIGNATED_G1DM4 = { ESC, '$', ')', 0x00 };

	/**
	 * Name:G2-designate multibyte 94-set Effect:F selects a 94n-character set
	 * to be used for G2.Code: ESC $ * F Hex: 1B 24 2A F
	 */
	public static final byte[] DESIGNATED_G2DM4 = { ESC, '$', '*', 0x00 };

	/**
	 * Name:G3-designate multibyte 94-set Effect:F selects a 94n-character set
	 * to be used for G3.Code: ESC $ + F Hex: 1B 24 2B F
	 */
	public static final byte[] DESIGNATED_G3DM4 = {ESC, '$', '+', 0x00 };

	/**
	 * Name:G1-designate multibyte 96-set Effect:F selects a 96n-character set
	 * to be used for G1.Code: ESC $ - F Hex: 1B 24 2D F
	 */
	public static final byte[] DESIGNATED_G1DM6 = {ESC, '$', '-', 0x00 };

	/**
	 * Name:G2-designate multibyte 96-set Effect:F selects a 96n-character set
	 * to be used for G2.Code: ESC $ . F Hex: 1B 24 2E F
	 */
	public static final byte[] DESIGNATED_G2DM6 = {ESC, '$', '.', 0x00 };

	/**
	 * Name:G3-designate multibyte 96-set Effect:F selects a 96n-character set
	 * to be used for G3.Code: ESC $ / F Hex: 1B 24 2F F
	 */
	public static final byte[] DESIGNATED_G3DM6 = {ESC, '$', '/', 0x00 };


	/**
	 * This function is used at many points. become optimization target.
	 */
	public static class ActionForMatechingEscape {
		private byte[][] mPattern;
		private ObserverForMatechingEscape mObserver;

		public ActionForMatechingEscape(final byte[][] pattern) {
			mPattern = pattern;
		}

		public void setObserver(final ObserverForMatechingEscape observer) {
			mObserver = observer;
		}

		boolean match(VirtualMemory v) {
			int len = mPattern.length;
			X: for (int i = 0; i < len; i++) {
				byte[] tmp = mPattern[i];
				try {
					v.pushMark();
					for (int j = 0; j < tmp.length; j++) {
						int r = v.read();
						if (tmp[j] != r) {
							continue X;
						}
					}
					mObserver.matched(tmp);
					return true;
				} catch (IOException e) {
				} finally {
					v.backToMark();
					v.popMark();
				}
			}
			return false;
		}
	}

	public static interface ObserverForMatechingEscape {
		public void matched(byte[] matchedData);
	}

	protected boolean LF(VirtualMemory v) {
		try {
			v.pushMark();
			if (0x0a == v.read()) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			v.backToMark();
			v.popMark();
		}
		return false;
	}
}