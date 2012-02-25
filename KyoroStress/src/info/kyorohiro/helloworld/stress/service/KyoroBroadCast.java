package info.kyorohiro.helloworld.stress.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class KyoroBroadCast extends BroadcastReceiver {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    Bundle bundle = intent.getExtras();
	    String message = bundle.getString("message");
	    Toast.makeText(
	      context, 
	      "onReceive! " + message, 
	      Toast.LENGTH_LONG).show();
	  }
 
		public static KyoroBroadCast 
		registReceiver(Context context, String action){
			KyoroBroadCast receiver = new KyoroBroadCast();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("action");
			context.registerReceiver(receiver, intentFilter);   
			return receiver;
		}

}