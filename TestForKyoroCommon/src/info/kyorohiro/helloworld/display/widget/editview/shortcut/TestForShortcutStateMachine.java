package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Command.CommandPart;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import junit.framework.TestCase;

public class TestForShortcutStateMachine extends TestCase {
	public void testHello() {
		assertTrue(true);
	}

	public void testFirst() {
		ShortcutStateMachine ssm = new ShortcutStateMachine(TEST_COMMAND);
		TestTask.clear();
		ssm.update('g', true, false, null, null);
		assertEquals(1, TestTask.sNumOfAct);
		assertEquals("g", TestTask.sLastAct);

		TestTask.clear();
		ssm.update('g', false, false, null, null);
		assertEquals(0, TestTask.sNumOfAct);
		assertEquals(TEST_COMMAND.length, ssm.getWorkList());

		TestTask.clear();
		ssm.update('g', false, true, null, null);
		assertEquals(0, TestTask.sNumOfAct);
		assertEquals(TEST_COMMAND.length, ssm.getWorkList());

		TestTask.clear();
		ssm.update('a', true, false, null, null);
		assertEquals(0, TestTask.sNumOfAct);
		assertEquals(4, ssm.getWorkList());
		assertEquals("aa", ssm.getWork(0).getTask().getCommandName());
		assertEquals("abc", ssm.getWork(1).getTask().getCommandName());
		assertEquals("abd", ssm.getWork(2).getTask().getCommandName());
		assertEquals("ad", ssm.getWork(3).getTask().getCommandName());
	}

	public static Command[] TEST_COMMAND = {
		new Command(new CommandPart[]{new CommandPart('g', true, false)}, new TestTask("g")),
		new Command(new CommandPart[]{new CommandPart('a', true, false), new CommandPart('a', true, false)}, new TestTask("aa")),
		new Command(new CommandPart[]{new CommandPart('a', true, false), new CommandPart('b', true, false), new CommandPart('c', true, false)}, new TestTask("abc")),
		new Command(new CommandPart[]{new CommandPart('a', true, false), new CommandPart('b', true, false), new CommandPart('d', true, false)}, new TestTask("abd")),
		new Command(new CommandPart[]{new CommandPart('a', true, false), new CommandPart('d', true, false)}, new TestTask("ad")),
		new Command(new CommandPart[]{new CommandPart('c', true, false)}, new TestTask("c")),
		new Command(new CommandPart[]{new CommandPart('d', false, true)}, new TestTask("d1")),
		new Command(new CommandPart[]{new CommandPart('d', true, true)}, new TestTask("d2")),
	};

	public static class TestTask implements Task {
		public static int sNumOfAct = 0;
		public static String sLastAct = "";

		public static void clear() {
			sNumOfAct = 0;
			sLastAct = "";			
		}

		private String mId = "";
		public TestTask(String id) {
			mId = id;
		}

		@Override
		public String getCommandName() {
			return mId;
		}

		@Override
		public void act(EditableLineView view, EditableLineViewBuffer buffer) {
			sNumOfAct++;
			sLastAct = mId;
		}
	}
}
