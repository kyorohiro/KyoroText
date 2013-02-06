package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.KyoroFile;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.TaskTicket;

public class DifferSaveAction implements CheckAction, TaskTicket.Task<String>,
		Runnable {
	private TaskTicket<String> mTicket = null;
	private Differ mDiffer = null;
	private LineViewBufferSpec mTarget = null;
	private VirtualFile mVFile = null;

	public DifferSaveAction(Differ differ, LineViewBufferSpec spec, VirtualFile path) {
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

//		android.util.Log.v("kiyo","##check#A#"+owner.length()+","+lineLocation);
		try {
			if (targetLine instanceof DeleteLine) {
				try {
					save(unpatchedPosition, patchedPosition, (DeleteLine) targetLine);
				} catch (FaileSaveException e) {
					e.printStackTrace();
				}
			}
			else if(targetLine instanceof AddLine) {
				try {
					save(unpatchedPosition, patchedPosition, (AddLine) targetLine);
				} catch (FaileSaveException e) {
					e.printStackTrace();
				}				
			}
//			android.util.Log.v("kiyo","##check#B#"+owner.length()+","+lineLocation);
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
		String encode = "DEL:b=" + beginPointer + ",e=" + endPointer + ";\n";
		try {
			return "TAG"+encode.getBytes("utf8").length+":"+encode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static String encodeAddLine(long beginPointer, long endPointer, String text) {
		String encode = "ADD:b=" + beginPointer + ",e=" + endPointer + ",l="+text.length()+",t="
				+ text + ";\n";
		try {
			return "TAG"+encode.getBytes("utf8").length+":"+encode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void save(int unpatchedPosition, int patchedPositon, AddLine line)
			throws FaileSaveException {
//		android.util.Log.v("kiyo","save add #A#"+unpatchedPosition +","+ patchedPositon +"," + line);
		int start = mPrevUnpatchedPosition + line.begin();
		//unpatchedPosition + line.begin();
		//int end = start + line.length();
		try {
			long beginPointer = 0;
			long endPointer = 0;
			if(start < mTarget.getNumberOfStockedElement()) {
				KyoroString insertedLine = mTarget.get(start);
				beginPointer = insertedLine.getBeginPointer();
				endPointer = insertedLine.getEndPointer();
			}
			else {
				KyoroString insertedLine = mTarget.get(mTarget.getNumberOfStockedElement()-1);
				beginPointer = insertedLine.getEndPointer();
				endPointer = insertedLine.getEndPointer();				
			}
//			android.util.Log.v("kiyo","save add #B-1#");
//			for (int lineLocation = 0; lineLocation < end; lineLocation++) {
			for (int lineLocation = 0; lineLocation < line.length(); lineLocation++) {
				String encodedData = 
					encodeAddLine(beginPointer, endPointer, line.get(lineLocation).toString());
				mVFile.addChunk(encodedData.getBytes("utf8"));
			}
//			android.util.Log.v("kiyo","save add #B-2#");
		} catch (Exception e) {
			e.printStackTrace();
			throw new FaileSaveException();
		}
//		android.util.Log.v("kiyo","save add #C#");
	}

	public void save(int unpatchedPosition, int patchedPositon, DeleteLine line)
			throws FaileSaveException {
		int start = mPrevUnpatchedPosition + line.begin();
		int end = start + line.length();
		try {
			for (int location = start; location < end; location++) {
				KyoroString deletedLine = mTarget.get(location);
				long beginPointer = deletedLine.getBeginPointer();
				long endPointer = deletedLine.getEndPointer();
//				android.util.Log.v("kiyo","location:"+location+","
//						+beginPointer+","+endPointer+","+deletedLine);

				String encodedData = encodeDeleteLine(beginPointer, endPointer);
				mVFile.addChunk(encodedData.getBytes("utf8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new FaileSaveException();
		}

	}

	public static class FaileSaveException extends Exception {
	}

	public static String readTag(KyoroFile vFile) throws IOException {
		byte[] header = new byte[128];
		long pointer = vFile.getFilePointer();
		vFile.read(header);
		int sep = 0;
		for(int i=0;i<header.length;i++) {
			if(':' == header[i]) {
				sep = i;
				break;
			}
		}
		String sizeAsS = new String(header,3,sep-3);
		//android.util.Log.v("kiyo","##NNNN#"+new String(header,0,header.length));
		long size = Long.parseLong(sizeAsS);
		vFile.seek(pointer+sep+1);
		byte[] body = new byte[(int)size];
		vFile.read(body);
		return new String(body,"utf8");
	}
	//
	// call close in this method
	public static void restore(KyoroFile index, KyoroFile input, KyoroFile output) throws IOException {
//		android.util.Log.v("kiyo","#restore()--1--");

		//BufferedReader isrIndex = new BufferedReader(new InputStreamReader(input));
		long inputPosition = 0;
		try {
//			android.util.Log.v("kiyo","#restore()--2-"+input.getFilePointer()+"<"+input.length());
//			android.util.Log.v("kiyo","#restore()--2-"+index.getFilePointer()+"<"+index.length());
			while(index.getFilePointer()<index.length()) {
//				android.util.Log.v("kiyo","#restore()--3-"+index.getFilePointer()+"<"+index.length());
//				String line = VirtualFile.readLine(index, "utf8");
				String line = readTag(index);

				//
				// "DEL:b=" + beginPointer + ",e=" + endPointer + ";/n";
				//
				if(line.startsWith("DEL")) {
//					android.util.Log.v("kiyo","#restore()--4--");
					String tmp = line.substring(6,line.length());
					String[] sp = tmp.split(",e=|;");
					long begin = Long.parseLong(sp[0]);
					long end = Long.parseLong(sp[1]);
//					android.util.Log.v("kiyo","#be="+begin+","+end);
					//
					// write begin
//					android.util.Log.v("kiyo","#1fp"+input.getFilePointer()+","+output.getFilePointer());
					saveF(input, output, 0, begin-inputPosition);
//					android.util.Log.v("kiyo","#2fp"+input.getFilePointer()+","+output.getFilePointer());
					inputPosition += begin-inputPosition;
//					android.util.Log.v("kiyo","#3fp"+input.getFilePointer()+","+output.getFilePointer());
					// jump 
					input.seek(input.getFilePointer()+(end-begin));
//					android.util.Log.v("kiyo","#4fp"+input.getFilePointer()+","+output.getFilePointer());
					inputPosition +=(end-begin);
//					android.util.Log.v("kiyo","#5fp"+input.getFilePointer()+","+output.getFilePointer());
					
				} else if(line.startsWith("ADD")) {
					//
					// "ADD:b=" + beginPointer + ",e=" + endPointer +",l="+",t="+ text + ";\n";
					//
//					android.util.Log.v("kiyo","#restore()--5--");
					String tmp = line.substring(6,line.length());
					String[] sp = tmp.split(",e=|,t=|;|,l=");
					long begin = Long.parseLong(sp[0]);
					long end = Long.parseLong(sp[1]);
					long length = Long.parseLong(sp[2]);
					String text = tmp.substring((int)(tmp.length()-length-2), tmp.length()-2);
					//
					// write begin
//					android.util.Log.v("kiyo","#a1fp"+input.getFilePointer()+","+output.getFilePointer());
					saveF(input, output, 0, begin-inputPosition);
//					android.util.Log.v("kiyo","#a2fp"+input.getFilePointer()+","+output.getFilePointer());
					inputPosition += begin-inputPosition;
//					android.util.Log.v("kiyo","#a3fp"+input.getFilePointer()+","+output.getFilePointer());
					// write text
					byte[] buf = text.getBytes();
					output.addChunk(buf, 0, buf.length);
				} else {
//					android.util.Log.v("kiyo","#restore()--6--"+line);
				}
			}
			long av = input.length()-input.getFilePointer();
			if(av>=0){
			saveF(input, output, 0, av);
			inputPosition += av;
			}
		} finally {
			
		}
	}

	public static void saveF(KyoroFile input, KyoroFile output, long begin, long end) throws IOException {
		byte[] buffer = new byte[1024];
		long len = 0;
		int writed =0;
		do {
			len = end-begin-writed;
			if(len >=1024){
				len =1024;
			}
			if(len<=0){
				break;
			}
			len = input.read(buffer, (int)len);
			if(len<=0) {
				break;
			}
			writed += len;
			output.addChunk(buffer,0,(int)len);
			if(writed>=end-begin){
				break;
			}
		}while(true);
	}
}
