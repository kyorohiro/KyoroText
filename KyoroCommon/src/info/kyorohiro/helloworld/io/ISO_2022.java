package info.kyorohiro.helloworld.io;

public class ISO_2022 {

	
	/**
	 *Name:Locking shift zero
	 *Effect:GL encodes G0 from now on
	 **SI Shift In
	 */
	final byte[] INVOKED_LS0 = {0x0F};

	/**
	 *Name:Locking shift one
	 *Effect:GL encodes G1 from now on
	 **SO Shift Out
	 */
	final byte[] INVOKED_LS1 = {0x0E};

	/**
	 *Name:Locking shift two
	 *Effect:GL encodes G2 from now on
	 */
	final byte[] INVOKED_LS2 = {0x1B, 0x6E, 'n'};
	
	/**
	 *Name:Locking shift three
	 *Effect:GL encodes G3 from now on
	 */
	final byte[] INVOKED_LS3 = {0x1B, 0x6E, 'o'};

	/**
	 *Name:Single shift two
	 *Effect:GL encodes G2 for next character only
	 */
	final byte[] INVOKED_SS2 = {(byte)0x8E, 0x1B, 0x4E, 'N'};

	/**
	 *Name:Single shift three
	 *Effect:GL encodes G3 for next character only
	 */
	final byte[] INVOKED_SS3 = {(byte)0x8F, 0x1B, 0x4E, 'O'};

	/**
	 *Name:Locking shift one right
	 *Effect:GR encodes G1 from now on
	 */
	final byte[] INVOKED_LS1R = {0x1B, 0x7E, '~'};

	/**
	 *Name:Locking shift two right
	 *Effect:GR encodes G2 from now on
	 */
	final byte[] INVOKED_LS2R = {0x1B, 0x7D, '}'};

	/**
	 *Name:Locking shift three right
	 *Effect:GR encodes G3 from now on
	 */
	final byte[] INVOKED_LS3R = {0x1B, 0x7C, '|'};
	
	


	/**
	 * Name:C0-designate
	 * Effect:F selects a C0 control character set to be used.
	 */
	final byte[] DESIGNATED_CZD = {0x1B,0x21,0x0F};
	
	/**
	 * Name:C1-designate
	 * Effect:F selects a C1 control character set to be used.
	 */
	final byte[] DESIGNATED_C1D = {0x1B,0x22,0x0F};

	/**
	 * Name:Designate other coding system
	 * Effect:F selects an 8-bit code; use ESC % @ to return to ISO/IEC 2022.
	 */
	final byte[] DESIGNATED_DOCS_0 = {0x1B,0x25,0x0F};

	/**
	 * Name:Designate other coding system
	 * Effect:F selects an 8-bit code; there is no standard way to return.
	 */
	final byte[] DESIGNATED_DOCS_1 = {0x1B,0x25,0x2F, 0x0F};

	/**
	 * Name:G0-designate 94-set
	 * Effect:F selects a 94-character set to be used for G0.
	 */
	final byte[] DESIGNATED_GZD4 = {0x1B, 0x28, 0x0F};

	/**
	 * Name:G1-designate 94-set
	 * Effect:F selects a 94-character set to be used for G1.
	 */
	final byte[] DESIGNATED_G1D4 = {0x1B, 0x29, 0x0F};

	/**
	 * Name:G1-designate 94-set
	 * Effect:F selects a 94-character set to be used for G2.
	 */
	final byte[] DESIGNATED_G2D4 = {0x1B, 0x2A, 0x0F};

	/**
	 * Name:G1-designate 94-set
	 * Effect:F selects a 94-character set to be used for G3.
	 */
	final byte[] DESIGNATED_G3D4 = {0x1B, 0x2B, 0x0F};

	
	/**
	 * Name:G1-designate 96-set
	 * Effect:F selects a 96-character set to be used for G1.
	 */
	final byte[] DESIGNATED_G1D6 = {0x1B, 0x2D, 0x0F};

	/**
	 * Name:G1-designate 96-set
	 * Effect:F selects a 96-character set to be used for G2.
	 */
	final byte[] DESIGNATED_G2D6 = {0x1B, 0x2E, 0x0F};

	/**
	 * Name:G1-designate 96-set
	 * Effect:F selects a 96-character set to be used for G3.
	 */
	final byte[] DESIGNATED_G3D6 = {0x1B, 0x2F, 0x0F};

	/**
	 * Name:G0--designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G0.
	 */
	final byte[] DESIGNATED_GZDM4 = {0x1B, 0x24, 0x28, 0x0F};

	/**
	 * Name:G1-designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G1.
	 */
	final byte[] DESIGNATED_G1DM4 = {0x1B, 0x24, 0x29, 0x0F};

	/**
	 * Name:G1-designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G2.
	 */
	final byte[] DESIGNATED_G2DM4 = {0x1B, 0x24, 0x2A, 0x0F};

	/**
	 * Name:G1-designate multibyte 94-set
	 * Effect:F selects a 94n-character set to be used for G3.
	 */
	final byte[] DESIGNATED_G3DM4 = {0x1B, 0x24, 0x2B, 0x0F};

	/**
	 * Name:G1-designate multibyte 96-set
	 * Effect:F selects a 96n-character set to be used for G1.
	 */
	final byte[] DESIGNATED_G1DM6 = {0x1B, 0x24, 0x2D, 0x0F};

	/**
	 * Name:G1-designate multibyte 96-set
	 * Effect:F selects a 96n-character set to be used for G2.
	 */
	final byte[] DESIGNATED_G2DM6 = {0x1B, 0x24, 0x2E, 0x0F};

	/**
	 * Name:G1-designate multibyte 96-set
	 * Effect:F selects a 96n-character set to be used for G3.
	 */
	final byte[] DESIGNATED_G3DM6 = {0x1B, 0x24, 0x2F, 0x0F};


}