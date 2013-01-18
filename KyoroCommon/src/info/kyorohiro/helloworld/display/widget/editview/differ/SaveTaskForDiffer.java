package info.kyorohiro.helloworld.display.widget.editview.differ;



import java.io.File;
import java.util.LinkedList;

import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.TaskTicket;

public class SaveTaskForDiffer implements CheckAction ,TaskTicket.Task<String>, Runnable {
	private TaskTicket<String> mTicket = null;
	private Differ mDiffer = null;
	private LineViewBufferSpec mTarget = null;
	private VirtualFile mVFile = null;

	public SaveTaskForDiffer(Differ differ, LineViewBufferSpec spec, VirtualFile path) {
		super();
		mTicket = new TaskTicket<String>(this);
		mDiffer = differ;
		mTarget = spec;
		mVFile = path;
	}

	TaskTicket<String> getTicket() {
		return mTicket;
	}

	@Override
	public String get() {
		return "dummy";
	}


	@Override
	public void run() {
		mTicket.run();
	}

	//
	// -------------------------------------------------------
	//
	@Override
	public void doTask() {
		mDiffer.checkAllSortedLine(this);
	}

	@Override
	public void init() {
		mPrevPatchedPosition = 0;
		mPrevUnpatchedPosition = 0;
	}

	private int mPrevPatchedPosition = 0;
	private int mPrevUnpatchedPosition = 0;

	@Override
	public boolean check(Differ owner, int lineLocation, int patchedPosition,
			int unpatchedPosition, int index) {
		Line targetLine = owner.getLine(lineLocation);

		try {
		if(targetLine instanceof DeleteLine) {
			try {
				save(unpatchedPosition, patchedPosition, (DeleteLine)targetLine);
			} catch (FaileSaveException e) {
				//todo
				e.printStackTrace();
			}			
		}
		} finally {
			mPrevPatchedPosition = patchedPosition;
			mPrevUnpatchedPosition = unpatchedPosition;
		}
		return true;
	}

	@Override
	public void end(LinkedList<Line> ll) {
	}

	//
	//
	//
	public static String encodeDeleteLine(long beginPointer, long endPointer) {
		String encode = "DEL:b=" + beginPointer + ",e=" + endPointer+";";
		return encode;
	}

	public static String encodeAddLine(long beginPointer, long endPointer, String text) {
		String encode = "DEL:b=" + beginPointer + ",e=" + endPointer+",t="+text+";";
		return encode;
	}
	public void save(int unpatchedPosition, int patchedPositon, AddLine line) throws FaileSaveException {
		int start = unpatchedPosition + line.begin();
		int end = start + line.length();
		try {
			for (int location = start, lineLocation=0; location < end; location++) {
				KyoroString insertedLine = mTarget.get(location);
				long beginPointer = insertedLine.getBeginPointer();
				long endPointer = insertedLine.getEndPointer();
				String encodedData = encodeAddLine(beginPointer, endPointer, line.get(lineLocation).toString());
				mVFile.addChunk(encodedData.getBytes("utf8"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new FaileSaveException();
		}
	}

	public void save(int unpatchedPosition, int patchedPositon, DeleteLine line) throws FaileSaveException {
		int start = mPrevUnpatchedPosition + line.begin();
		int end = start + line.length();
		try {
			for (int location = start; location < end; location++) {
				android.util.Log.v("kiyo",""+location);
				KyoroString deletedLine = mTarget.get(location);
				long beginPointer = deletedLine.getBeginPointer();
				long endPointer = deletedLine.getEndPointer();
				String encodedData = encodeDeleteLine(beginPointer, endPointer);
				mVFile.addChunk(encodedData.getBytes("utf8"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new FaileSaveException();
		}

	}
	public static class FaileSaveException extends Exception {
	}
}
