package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;

public class SaveTaskForDiffer implements Runnable {

	private LineViewBufferSpec mTarget;
	private Differ mDiffer;
	private VirtualFile mVFile;
	
	public static String encodeDeleteLine(long beginPointer, long endPointer) {
		String encode = "DEL:b=" + beginPointer + ",e=" + endPointer+";";
		return encode;
	}

	public static String encodeAddLine(long beginPointer, long endPointer, String text) {
		String encode = "DEL:b=" + beginPointer + ",e=" + endPointer+",t="+text+";";
		return encode;
	}

	public SaveTaskForDiffer(LineViewBufferSpec target, Differ differ,
			VirtualFile file) {
		mTarget = target;
		mDiffer = differ;
		mVFile = file;
	}

	@Override
	public void run() {
	}

	public void save() {
		int numOfLine = mDiffer.numOfLine();
		for (int location = 0; location < numOfLine; location++) {
			Line line = mDiffer.getLine(location);
			if (line instanceof AddLine) {

			} else if (line instanceof DeleteLine) {
			}
		}
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
		int start = unpatchedPosition + line.begin();
		int end = start + line.length();
		try {
			for (int location = start; location < end; location++) {
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

	public void save(AddLine line) {

	}

	public static class FaileSaveException extends Exception {
	}
}
