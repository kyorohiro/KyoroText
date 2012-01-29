package info.kyorohiro.helloworld.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;




import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SimpleFileExplorer extends Dialog {

	private Activity mOwnerActivity =  null; 
	private ListView mCurrentFileList = null;
	private SelectedFileAction mAction = null;
	private EditText mEdit = null;
	private LinearLayout mLayout = null;
	private File mDir = null;
	
	public static SimpleFileExplorer createDialog(Activity owner, File dir) {
		return new SimpleFileExplorer(owner, owner,dir);		
	}

	public SimpleFileExplorer(Context context, Activity owner, File dir) {
		super(context);
		mLayout =new LinearLayout(context);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mOwnerActivity = owner;
		mCurrentFileList = new ListView(context);
		mEdit = new EditText(context);
		mDir = dir;
		init();
		startUpdateTask(dir);
	}

	public void setOnSelectedFileAction(SelectedFileAction action){
		mAction = action;
	}

	private void init() {
		mEdit.setSelected(false);
		mEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		mEdit.setHint("search file : regex(find)");
		mEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// All IME Application take that actionId become imeoption's value.
				String find = v.getText().toString();
				if(find == null || find.equals("")){
					startUpdateTask(mDir);
				}
				else {
					try {
						startUpdateTask(new UpdateListFromSearchTask(mDir, Pattern.compile(find)));
					} catch(Exception e){
						
					}
				}
				return false;
			}
		});
		ViewGroup.LayoutParams params = 
			new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		ArrayAdapter<ListItemWithFile> adapter = 
		 new ArrayAdapter<ListItemWithFile>(
				getContext(),
				android.R.layout.simple_list_item_1);

		mLayout.addView(mEdit, params);
		mLayout.addView(mCurrentFileList, params);
		addContentView(mLayout, params);
		mCurrentFileList.setAdapter(adapter);
		mCurrentFileList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				ListItemWithFile item = ((ArrayAdapter<ListItemWithFile>)mCurrentFileList.getAdapter()).getItem(pos);
				File f = item.getFile();
				if(f.exists() && f.isDirectory()){
					mDir = f;
				}
				if(mAction == null || mAction != null) {
					if(mAction.onSelectedFile(f)) {
						try {
							SimpleFileExplorer.this.dismiss();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						startUpdateTask(f);
					}
				}
			}
		});
	}

	private Thread mTh = null;
	private synchronized void startUpdateTask(File f) {
		Thread th = mTh;
		mTh = null;
		if(th != null && th.isAlive() &&th.isInterrupted()) {
			th.interrupt();
		}
		Thread.yield();
		mTh = new Thread(new UpdateListFromDirTask(f));
		mTh.start();
	}

	private synchronized void startUpdateTask(Runnable task) {
		mTh = new Thread(task);
		mTh.start();
	}

	private synchronized void add(ArrayAdapter adapter){
		add(adapter,Thread.currentThread());
	}

	private synchronized void add(ArrayAdapter adapter, Thread owner){
		if(mTh == owner){
			mCurrentFileList.setAdapter(adapter);
		}
	}
	

	private synchronized boolean checkEnding() {
		if(mTh == Thread.currentThread()){
			return false;
		}
		else {
			return true;
		}
	}


	public class UpdateListFromSearchTask implements Runnable {
		private File mDir = null;
		private Pattern mPattern = null;

		public UpdateListFromSearchTask(File dir, Pattern pattern) {
			mDir = dir;
			mPattern = pattern;
		}

		@Override
		public void run() {
			ArrayList<ListItemWithFile> mOutput = new ArrayList<ListItemWithFile>();
			ArrayList<File> mTmp = new ArrayList<File>();
			
			try {
				mTmp.add(mDir);
				File t = mTmp.get(mTmp.size()-1);
				mTmp.remove(t);
				for(File f : t.listFiles()){
					if(!f.exists()){
						continue;
					}
					if(f.isDirectory()){
						mTmp.add(f);
					}
					if(f.isFile()){
						if(mPattern.matcher(f.getPath()).find()) {
							mOutput.add(new ListItemWithFile(f));
						}
					}
				}
				
			} catch(Throwable t){
				
			} finally {
				ArrayAdapter<ListItemWithFile> adapter =
					new ArrayAdapter<ListItemWithFile>(
						SimpleFileExplorer.this.getContext(),
						android.R.layout.simple_list_item_1,
						mOutput);				
				SimpleFileExplorer.this.mOwnerActivity.runOnUiThread(new UpdateListTask( adapter,Thread.currentThread()));
			}
		}
		
	}

	public class UpdateListFromDirTask implements Runnable {
		private File mDir = null;

		public UpdateListFromDirTask(File dir) {
			mDir = dir;
		}

		@Override
		public void run() {
			try {
				ArrayAdapter<ListItemWithFile> adapter = new ArrayAdapter<ListItemWithFile>(
						SimpleFileExplorer.this.getContext(),
						android.R.layout.simple_list_item_1);
				if(!mDir.exists()|| !mDir.isDirectory()){
					return;
				}

				if(mDir.getParent() != null && mDir.exists() && !mDir.isFile()) {
					adapter.add(new ListItemWithFile(mDir.getParentFile(), "../"));
				}

				File[] list = mDir.listFiles();
				if(list == null){
					return;
				}
				for( File f : list) {
					if(checkEnding()){
						return;
					}
					adapter.add(new ListItemWithFile(f));
				}
				SimpleFileExplorer.this.mOwnerActivity.runOnUiThread(new UpdateListTask( adapter,Thread.currentThread()));
			} catch(Throwable t) {
				t.printStackTrace();
			} finally {
			}
		}
	}

	public class UpdateListTask implements Runnable {
		private ArrayAdapter<ListItemWithFile> mAdapter;
		private Thread mOwner;
		
		public UpdateListTask(ArrayAdapter<ListItemWithFile> adapter, Thread owner) {
			mAdapter = adapter;
			mOwner = owner;
		}
		@Override
		public void run() {
			add(mAdapter, mOwner);
		}
	}

	public static class ListItemWithFile {
		private File mPath = null;
		private String mOutput = "";

		public ListItemWithFile(File path, String output){
			mPath = path;
			mOutput = ""+output;
		}
		public ListItemWithFile(File path) {
			mPath = path;
			mOutput = path.getName();
			if(path.isDirectory()){
				mOutput = mOutput+"/";
			}
		}
		
		@Override
		public String toString() {
			return mOutput;
		}
		
		public File getFile() {
			return mPath;
		}	
	}
	
	public static interface SelectedFileAction {
		/**
		 * @param file is user selected file
		 * @return if end dialog return true, else return false;  
		 */
		public boolean onSelectedFile(File file);
	}
}
