package info.kyorohiro.helloworld.util;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class FileListGetter implements Runnable {
	private File mTargetPath = null;
	private Thread mParentThread = null;
	private String mFilter = null;
	private File[] mList = null;
	//private boolean mIsEndTask = false;

	public FileListGetter(File targetPath, String filter, Thread parentThread) {
		mTargetPath = targetPath;
		mParentThread = parentThread;
		mFilter = filter;
		if(mFilter != null && mFilter.length() == 0) {
			mFilter = null;
		}
	}

	@Override
	public synchronized void run() {
		//try {
		int start = 0;
		int end = 0;
		if(mParentThread != null && mParentThread.isInterrupted()) {
			return;
		}

		File[] list = mTargetPath.listFiles();
		if(mParentThread != null && mParentThread.isInterrupted()) {
			return;
		}
		if(list == null) {
			return;
		}
		end = list.length;
		if(mFilter != null) {
			ArrayList<File> calcList = new ArrayList<File>();
			for(File t:list) {
				if(t.getName().startsWith(mFilter)) {
					calcList.add(t);
				}
			}
			end  = calcList.size();
			list = null;
			list = new File[end];
			calcList.toArray(list);
		}
		if(start == end) {
			list = new File[0];
		} else {
			Arrays.sort(list, start, end, new FileComparator());
		}
		mList = list;
		//} finally {
		//	mIsEndTask = true;
		//	notifyAll();
		//}
	}

	public synchronized File[] getFileList() {
		//try {
		//	if(!mIsEndTask) {
		//		wait();
		//	}
		//} catch (InterruptedException e) {
		//	e.printStackTrace();
		//}
		return mList;
	}

	static class FileComparator implements Comparator<File> {
		@Override
		public int compare(File l, File r) {
			if(l.isDirectory() && !r.isDirectory()) {
				return -1;
			}
			else if(!l.isDirectory() && r.isDirectory()) {
				return 1;
			}
			else {
				int ret = 0;
				ret = l.getName().compareTo(r.getName());
				if(ret == 0) {
					return l.compareTo(r);
				} else {
					return ret;
				}
			}
		}
	}
}