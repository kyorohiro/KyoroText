package info.kyorohiro.helloworld.ext.textviewer.manager.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferList;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.util.AutocandidateList;

public class ListBufferTask implements Runnable {
	private TextViewer mInfo = null;
	private AutocandidateList mCandidate = new AutocandidateList(); 
	public ListBufferTask(TextViewer info) {
		mInfo = info;
	}

	public String getCandidate(String c) {
		return mCandidate.candidateText(c);
	}

	@Override
	public void run() {
		try {
			int c=0;
			mCandidate.clear();
			EditableLineView infoBufferEViewer = mInfo.getLineView();
			EditableLineViewBuffer infoBufferEBuffer = (EditableLineViewBuffer)mInfo.getLineView().getLineViewBuffer();
			BufferList bufferList = BufferManager.getManager().getBufferList();
			infoBufferEViewer.setTextSize(BufferManager.getManager().getBaseTextSize());
			infoBufferEBuffer.clear();

			{
				for(int i=0;i<bufferList.size();i++) {
					TextViewer tmp = bufferList.getTextViewer(i);
					if(tmp != null && !tmp.isDispose()) {
						infoBufferEBuffer.getDiffer().asisSetType("list-buffers");
						infoBufferEBuffer.getDiffer().asisSetExtra(""+i+":"+tmp.getCurrentPath());
						infoBufferEBuffer.pushCommit(""+i+":   sorry now creating: "+tmp.getCurrentPath(), 1);
						infoBufferEBuffer.crlf(false, false);	
						infoBufferEBuffer.crlf(false, false);	
						infoBufferEBuffer.crlf();
					}
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
