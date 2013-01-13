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

	public void save(int position, DeleteLine line) throws FaileSaveException {
		int start = position + line.begin();
		int end = start + line.length();
		try {
			for (int location = start; location < end; location++) {
				KyoroString deletedLine = mTarget.get(location);
				long beginPointer = deletedLine.getBeginPointer();
				long endPointer = deletedLine.getEndPointer();
				String delete = "DEL:b=" + beginPointer + ",e=" + endPointer+";";
				mVFile.addChunk(delete.getBytes("utf8"));
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
