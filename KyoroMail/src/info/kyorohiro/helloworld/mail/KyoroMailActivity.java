package info.kyorohiro.helloworld.mail;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class KyoroMailActivity extends Activity {
    /** Called when the activity is first created. */
	
	private LinearLayout mLayout = null;
	private Button mButton = null;
	private LinearLayout.LayoutParams mButtonLayout = 
		new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = new LinearLayout(this);
        mButton = new Button(this);

        mLayout.addView(mButton, mButtonLayout);
        mButton.setText("test");
        setContentView(mLayout);
        
        mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Thread th = new Thread(new GetListTask());
				th.start();
			}
		});
    }
    
    
    private String mHost = "imap.gmail.com";
    private int mPort = 993;
    private String mUser = "kyorohiro@gmail.com";
    private String mPassword = "miraigan";

    public class GetListTask implements Runnable {
		@Override
		public void run() {
			Properties props = System.getProperties();
			Session session = Session.getDefaultInstance(props, null); 
			try {
				Store imapStore = session.getStore("imaps");
				imapStore.connect(mHost, mUser, mPassword);
				Folder inbox = imapStore.getFolder("INBOX");
				if (inbox.exists()) {
					for(Folder f : inbox.list()) {
						android.util.Log.v("kiyohiro",""+f.getName());
					}
					inbox.open(Folder.READ_ONLY);
					
					for(Message m : inbox.getMessages()) {
						android.util.Log.v("kiyohiro",""+m.getSubject());						
					}
				}
				inbox.close(false);
				imapStore.close();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}  	
    }
}