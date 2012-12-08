package info.kyorohiro.helloworld.display.simple;

public interface MetaState {
	public void clear();
	public boolean pushingCtl();
	public boolean pushingAlt();
	public boolean pushingEsc();
	public boolean pushingShift();
}
