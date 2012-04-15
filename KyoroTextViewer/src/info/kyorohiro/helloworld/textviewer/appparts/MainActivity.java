package info.kyorohiro.helloworld.textviewer.appparts;

import java.util.LinkedList;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * KyoroLogcatとか作ってみたところ、画面周りのソースが、特定のクラス肥大化してしまった。
 * 
 * なるたけ、機能を子ひとつにクラスに背負い込まないように、仕掛けを入れておく。
 */
public class MainActivity extends Activity {
	
	private LinkedList<MainActivityMenuAction> mMenuAction = new LinkedList<MainActivityMenuAction>();

	public void setMenuAction(MainActivityMenuAction action) {
		mMenuAction.add(action);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		for(MainActivityMenuAction a : mMenuAction) {
			if(a.onPrepareOptionsMenu(this, menu)){
				break;
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		for(MainActivityMenuAction a : mMenuAction){
			if(a.onMenuItemSelected(this, featureId, item)){
				break;
			}
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
