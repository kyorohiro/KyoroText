package info.kyorohiro.helloworld.io.charset;

import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.IOException;

public class ISO2022JP extends ISO2022 {

	byte[] currentGL = null;
	byte[] currentG0 = null;
	byte[] currentG2 = null;

	public byte[] currentG0() {return currentG0;}
	public byte[] currentG2(){return currentG2;}
	public byte[] currentGL(){return currentGL;}
	public byte[] currentG3(){return null;}
	public byte[] currentG1() {return null;}
	public byte[] currentGR(){return null;}
	public byte[] currentEscape(){
		return null;
	}

	public void update(VirtualMemory vm){
		try {
			_update(vm);
		}catch(IOException e) {
		}
	}

	public void _update(VirtualMemory v) throws IOException {
		try {
			v.pushMark();
			if(LF(v)){;}
			else if(G0(v)){;}
			else if(G2(v)){;}
			else {;}
		} finally {
			v.backToMark();
		}
	}

	public boolean LF(VirtualMemory v) {
		try {
			v.pushMark();
			if(0x0a == v.read()){
				return true;
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			v.backToMark();
			v.popMark();
		}
		return false;
	}

	public boolean G0(VirtualMemory v) {
		X:for(int i=0;i<ISO_2022_JP_DESIGNATED_G0.length;i++) {
			byte[] tmp = ISO_2022_JP_DESIGNATED_G0[i];
			try {
				v.pushMark();
				for(int j=0;j<tmp.length;i++){
					if(tmp[j] != v.read()){
						continue X;
					}
				}
				currentG0 = currentGL = tmp;
				return true;
			} catch(IOException e) {

			} finally {
				v.backToMark();
			}
		}
		return false;
	}

	public boolean G2(VirtualMemory v) {
		X:for(int i=0;i<ISO_2022_JP_DESIGNATED_G2.length;i++) {
			byte[] tmp = ISO_2022_JP_DESIGNATED_G2[i];
			try {
				v.pushMark();
				for(int j=0;j<tmp.length;i++){
					if(tmp[j] != v.read()){
						continue X;
					}
				}
				currentG2 = tmp;
				return true;
			} catch(IOException e) {
			} finally {
				v.backToMark();
			}
		}
		return false;
	}

	final byte[][] ISO_2022_JP_DESIGNATED_G0 = {
			//ISO-2022-JP
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZD4, 'B'),//ascii
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZD4, 'J'),//JIS X 0201-1976
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_1, '@'),//JIS X 0208-1978
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_1, 'B'),//JIS X 0208-1983 
			//ISO-2022-JP-1
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'D'),//JIS X 0212-1990 
			//ISO-2022-JP-2
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_1, 'A'),//GB 2312-1980
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'C'),//KS X 1001-1992
			//ISO-2022-JP-3
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'O'),//JIS X 0213:2000 
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'P'),//JIS X 0213:2000		
			//ISO-2022-JP-2004
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_0, 'Q'),//JIS X 0213:2004
	};

	final byte[][] ISO_2022_JP_DESIGNATED_G2 = {
			//ISO-2022-JP-2
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_G2D6, 'A'),//ISO/IEC 8859-1 single lock
			ISO2022.DESIGNATED(ISO2022.DESIGNATED_G2D6, 'F'),// ISO/IEC 8859-7 single lock
	};

	final byte[][] ISO_2022_JP_INVOKE = {
			//ISO-2022-JP-2
			ISO2022.INVOKED_LS2
	};

}