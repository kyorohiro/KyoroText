package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.shortcut.Command;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Command.CommandPart;

public class KeyEventManagerPlus extends KeyEventManager {

	private static Command[] EMACS_SHORTCUT_EXTRA = {
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('o', false, false)}, new OtherWindow()),
	};

	public KeyEventManagerPlus() {
		super();
		setExtraCommand(EMACS_SHORTCUT_EXTRA);	
	}

	public Command[] getEmacsShortExtra() {
		return EMACS_SHORTCUT_EXTRA;
	}

	public void add(Command c) {
		Command[] _tmp = EMACS_SHORTCUT_EXTRA;
		Command[] _new = new Command[_tmp.length+1];
		int i=0;
		for(;i<_tmp.length;i++) {
			_new[i] = _tmp[i];
		}
		_new[i] = c;
	}
}
