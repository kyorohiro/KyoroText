package info.kyorohiro.helloworld.io.charset;

import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.IOException;

/**
 * memo:
 * http://en.wikipedia.org/wiki/ISO/IEC_2022
 * http://charset.7jp.net/sjis.html
 * http://tools.ietf.org/html/rfc1468
 * http://www.ecma-international.org/publications/files/ECMA-ST/Ecma-035.pdf
 */
public abstract class ISO2022 {
	public abstract byte[] currentG0();
	public abstract byte[] currentG1();
	public abstract byte[] currentG2();
	public abstract byte[] currentG3();
	public abstract byte[] currentGL();
	public abstract byte[] currentGR();
	public abstract byte[] currentEscape();
	public abstract void update(VirtualMemory vm);
	
	public static final byte[][] ISO_2022_KR_DESIGNATED_LOCK = {
			DESIGNATED(ISO2022.DESIGNATED_G1DM4, 'C'),//KS X 1001-1992 single
	};

	public static final byte[][] ISO_2022_KR_INVOKE = {
		ISO2022.INVOKED_LS0, //single
		ISO2022.INVOKED_LS1//single
	};

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
	
	public static byte[] DESIGNATED(byte[] code, char character) {
		byte[] buffer = new byte[code.length];
		buffer[code.length-1] = (byte)character;
		return buffer;
	}

	/**
	 *Name:Locking shift zero
	 *Effect:GL encodes G0 from now on
	 **SI Shift In
	 */
	public static final byte[] INVOKED_LS0 = {0x0F};

	/**
	 *Name:Locking shift one
	 *Effect:GL encodes G1 from now on
	 **SO Shift Out
	 */
	public static final byte[] INVOKED_LS1 = {0x0E};

	/**
	 *Name:Locking shift two
	 *Effect:GL encodes G2 from now on
	 */
	public static final byte[] INVOKED_LS2 = {0x1B, 'n'};
	
	/**
	 *Name:Locking shift three
	 *Effect:GL encodes G3 from now on
	 */
	public static final byte[] INVOKED_LS3 = {0x1B, 'o'};

	/**
	 *Name:Single shift two
	 *Effect:GL encodes G2 for next character only
	 */
	public static final byte[] INVOKED_SS2 = {(byte)0x8E, 0x1B, 0x4E, 'N'};

	/**
	 *Name:Single shift three
	 *Effect:GL encodes G3 for next character only
	 */
	public static final byte[] INVOKED_SS3 = {(byte)0x8F, 0x1B, 0x4E, 'O'};

	/**
	 *Name:Locking shift one right
	 *Effect:GR encodes G1 from now on
	 */
	public static final byte[] INVOKED_LS1R = {0x1B, 0x7E, '~'};

	/**
	 *Name:Locking shift two right
	 *Effect:GR encodes G2 from now on
	 */
	public static final byte[] INVOKED_LS2R = {0x1B, 0x7D, '}'};

	/**
	 *Name:Locking shift three right
	 *Effect:GR encodes G3 from now on
	 */
	public static final byte[] INVOKED_LS3R = {0x1B, 0x7C, '|'};
	
	


	/**
	 * Name:C0-designate
	 * Effect:F selects a C0 control character set to be used.
	 */
	public static final byte[] DESIGNATED_CZD = {0x1B,0x21,0x0F};
	
	/**
	 * Name:C1-designate
	 * Effect:F selects a C1 control character set to be used.
	 */
	public static final byte[] DESIGNATED_C1D = {0x1B,0x22,0x0F};

	/**
	 * Name:Designate other coding system
	 * Effect:F selects an 8-bit code; use ESC % @ to return to ISO/IEC 2022.
	 */
	public static final byte[] DESIGNATED_DOCS_0 = {0x1B,0x25,0x0F};

	/**
	 * Name:Designate other coding system
	 * Effect:F selects an 8-bit code; there is no standard way to return.
	 */
	public static final byte[] DESIGNATED_DOCS_1 = {0x1B,0x25,0x2F, 0x0F};

	/**
	 * Name:G0-designate 94-set
	 * Effect:F selects a 94-character set to be used for G0.
	 */
	public static final byte[] DESIGNATED_GZD4 = {0x1B, 0x28, 0x0F};

	/**
	 * Name:G1-designate 94-set
	 * Effect:F selects a 94-character set to be used for G1.
	 */
	public static final byte[] DESIGNATED_G1D4 = {0x1B, 0x29, 0x0F};

	/**
	 * Name:G1-designate 94-set
	 * Effect:F selects a 94-character set to be used for G2.
	 */
	public static final byte[] DESIGNATED_G2D4 = {0x1B, 0x2A, 0x0F};

	/**
	 * Name:G1-designate 94-set
	 * Effect:F selects a 94-character set to be used for G3.
	 */
	public static final byte[] DESIGNATED_G3D4 = {0x1B, 0x2B, 0x0F};

	
	/**
	 * Name:G1-designate 96-set
	 * Effect:F selects a 96-character set to be used for G1.
	 */
	public static final byte[] DESIGNATED_G1D6 = {0x1B, 0x2D, 0x0F};

	/**
	 * Name:G1-designate 96-set
	 * Effect:F selects a 96-character set to be used for G2.
	 */
	public static final byte[] DESIGNATED_G2D6 = {0x1B, 0x2E, 0x0F};

	/**
	 * Name:G1-designate 96-set
	 * Effect:F selects a 96-character set to be used for G3.
	 */
	public static final byte[] DESIGNATED_G3D6 = {0x1B, 0x2F, 0x0F};

	/**
	 * Name:G0--designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G0.
	 */
	public static final byte[] DESIGNATED_GZDM4_0   = {0x1B, 0x24, 0x28, 0x0F};
	public static final byte[] DESIGNATED_GZDM4_1 = {0x1B, 0x24, 0x0F};

	/**
	 * Name:G1-designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G1.
	 */
	public static final byte[] DESIGNATED_G1DM4 = {0x1B, 0x24, 0x29, 0x0F};

	/**
	 * Name:G1-designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G2.
	 */
	public static final byte[] DESIGNATED_G2DM4 = {0x1B, 0x24, 0x2A, 0x0F};

	/**
	 * Name:G1-designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G3.
	 */
	public static final byte[] DESIGNATED_G3DM4 = {0x1B, 0x24, 0x2B, 0x0F};

	/**
	 * Name:G1-designate multibyte 96-set
	 * Effect:F selects a 96n-character set to be used for G1.
	 */
	public static final byte[] DESIGNATED_G1DM6 = {0x1B, 0x24, 0x2D, 0x0F};

	/**
	 * Name:G1-designate multibyte 96-set
	 * Effect:F selects a 96n-character set to be used for G2.
	 */
	public static final byte[] DESIGNATED_G2DM6 = {0x1B, 0x24, 0x2E, 0x0F};

	/**
	 * Name:G1-designate multibyte 96-set
	 * Effect:F selects a 96n-character set to be used for G3.
	 */
	public static final byte[] DESIGNATED_G3DM6 = {0x1B, 0x24, 0x2F, 0x0F};


}