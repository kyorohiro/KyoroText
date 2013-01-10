package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.simple.SimpleKeyEvent;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.BackwardWord;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.BeginningOfBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.BeginningOfLine;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Command;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.CrlfTask;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.DeleteBackwardChar;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.DeleteChar;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.EndOfBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.EndOfLine;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.FowardWord;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KillLine;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.NextLine;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.PreviousLine;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Recenter;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.SingleByteSpaceTask;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.SingleByteTabTask;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Yank;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Command.CommandPart;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;

public class KeyEventManagerPlus extends KeyEventManager {

	public KeyEventManagerPlus() {
		super();
	}

	// must to call
	@Override
	public void onUpdate(CharSequence mode) {
//		android.util.Log.v("kiyo","onUpdate:"+mode);
		if(CursorableLineView.MODE_EDIT.equals(mode)) {
			getManager().updateCommnad(EMACS_SHORTCUT_EDIT);			
		}
		else if(MiniBuffer.MODE_LINE_BUFFER.equals(mode)) {
			getManager().updateCommnad(EMACS_SHORTCUT_MODELINE);			
		}
		else {
			getManager().updateCommnad(EMACS_SHORTCUT_VIEW);
		}
	}

	public static Command[] EMACS_SHORTCUT_VIEW = {
		
		new Command(new CommandPart[]{new CommandPart('a', true, false)}, new BeginningOfLine()),
		new Command(new CommandPart[]{new CommandPart('e', true, false)}, new EndOfLine()),
		new Command(new CommandPart[]{new CommandPart('n', true, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart('b', true, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart('p', true, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart('f', true, false)}, new FowardWord()),
		new Command(new CommandPart[]{new CommandPart('l', true, false)}, new Recenter()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('<', false, true)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('>', false, true)}, new EndOfBuffer()),

		//
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('o', false, false)}, new OtherWindow()),
		new Command(new CommandPart[]{new CommandPart('g', true, false)}, new KeyboadQuit()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('s', true, false)}, new SaveBuffer()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('0', false, false)}, new DeleteWindow()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('1', false, false)}, new DeleteOtherWindows()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('2', false, false)}, new SplitWindowVertically()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('3', false, false)}, new SplitWindowHorizontally()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('b', true, false)}, new ListBuffers()),

		//
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('f', true, false)}, new FindFile()),

		//*/
		///*
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_DOWN, false, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_UP, false, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_LEFT, false, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_RIGHT, false, false)}, new FowardWord()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_ENTER, false, false)}, new MessageDispatcherTask()),

		//*/
		new Command(new CommandPart[]{new CommandPart('s', true, false)}, new ISearchForward()),
	};

	public static Command[] EMACS_SHORTCUT_EDIT = {
		new Command(new CommandPart[]{new CommandPart('a', true, false)}, new BeginningOfLine()),
		new Command(new CommandPart[]{new CommandPart('e', true, false)}, new EndOfLine()),
		new Command(new CommandPart[]{new CommandPart('n', true, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart('b', true, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart('p', true, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart('f', true, false)}, new FowardWord()),
		new Command(new CommandPart[]{new CommandPart('l', true, false)}, new Recenter()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('<', false, true)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('>', false, true)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('h', true, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart('d', true, false)}, new DeleteBackwardChar()),
		new Command(new CommandPart[]{new CommandPart('l', true, false)}, new Recenter()),
		new Command(new CommandPart[]{new CommandPart('k', true, false)}, new KillLine()),
		new Command(new CommandPart[]{new CommandPart('y', true, false)}, new Yank()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('<', false, true)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('>', false, true)}, new EndOfBuffer()),
		//
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('o', false, false)}, new OtherWindow()),
		new Command(new CommandPart[]{new CommandPart('g', true, false)}, new KeyboadQuit()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('s', true, false)}, new SaveBuffer()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('0', false, false)}, new DeleteWindow()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('1', false, false)}, new DeleteOtherWindows()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('2', false, false)}, new SplitWindowVertically()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('3', false, false)}, new SplitWindowHorizontally()),
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('b', true, false)}, new ListBuffers()),

		//
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('f', true, false)}, new FindFile()),

		//
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_DOWN, false, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_UP, false, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_LEFT, false, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_RIGHT, false, false)}, new FowardWord()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DEL, false, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_ENTER, false, false)}, new CrlfTask()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_SPACE, false, false)}, new SingleByteSpaceTask()),	
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_BACK, false, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_TAB, false, false)}, new SingleByteTabTask()),	

		new Command(new CommandPart[]{new CommandPart('s', true, false)}, new ISearchForward()),

	};

	public static Command[] EMACS_SHORTCUT_MODELINE = {
		new Command(new CommandPart[]{new CommandPart('a', true, false)}, new BeginningOfLine()),
		new Command(new CommandPart[]{new CommandPart('e', true, false)}, new EndOfLine()),
		new Command(new CommandPart[]{new CommandPart('n', true, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart('p', true, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart('f', true, false)}, new FowardWord()),
		new Command(new CommandPart[]{new CommandPart('b', true, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart('l', true, false)}, new Recenter()),
		new Command(new CommandPart[]{new CommandPart('k', true, false)}, new KillLine()),
		new Command(new CommandPart[]{new CommandPart('y', true, false)}, new Yank()),
		new Command(new CommandPart[]{new CommandPart('h', true, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart('d', true, false)}, new DeleteBackwardChar()),

		//
		new Command(new CommandPart[]{new CommandPart('x', true, false), new CommandPart('o', false, false)}, new OtherWindow()),
		new Command(new CommandPart[]{new CommandPart('g', true, false)}, new KeyboadQuit()),

		//
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_DOWN, false, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_UP, false, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_LEFT, false, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_RIGHT, false, false)}, new FowardWord()),
		//
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DEL, false, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_BACK, false, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_SPACE, false, false)}, new SingleByteSpaceTask()),		
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_ENTER, false, false)}, new MiniBufferTaskDone()),	
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_TAB, false, false)}, new MiniBufferTaskNext()),	
	};


}
