package info.kyorohiro.helloworld.desktop;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.sample.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.pfdep.j2se.adapter.SimpleFontForJ2SE;
import info.kyorohiro.helloworld.pfdep.j2se.adapter.SimpleGraphicsForJ2SE;
import info.kyorohiro.helloworld.pfdep.j2se.adapter.SimpleStageForJ2SE;
import info.kyorohiro.helloworld.text.KyoroString;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.im.InputContext;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodContext;
import java.awt.im.spi.InputMethodDescriptor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.Character.Subset;
import java.text.AttributedCharacterIterator;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	public class Stage extends SimpleStageForJ2SE {// extends SimpleStageForJ2SE
													// {
		private boolean mInit = false;

		@Override
		protected void paintComponent(Graphics g) {
//		public void paint(Graphics g) {
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	                RenderingHints.VALUE_ANTIALIAS_ON);

			if (!mInit) {
				mInit = true;
				initItem(g);
			}
			AttributedCharacterIterator t;
		//	super.paint(g);
			super.paintComponent(g);
		}
	}

	public static void main(String[] args) {
//		System.out.println("hello world1");
		Main main = new Main();
		main.init();
//		System.out.println("/hello world1");
	}

	private SimpleStage mStage = null;

	private void init() {
		JFrame frame = new JFrame("hello swing");
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		initKyoroCommon(frame.getContentPane());
		mStage.start();
	}

	private void initKyoroCommon(Container appRoot) {
		SimpleStageForJ2SE stage = new Stage();// SimpleStageForJ2SE();
		stage.setVisible(true);
		stage.setPreferredSize(new Dimension(600, 400));
		appRoot.add(stage);
		mStage = stage;
	}

	private void initItem(Graphics g) {
		if (false) {
			SimpleDisplayObject child = new TestDisplayObject();
			child.setRect(100, 100);
			mStage.getRoot().addChild(child);
		}
		if (false) {
			EmptyLineViewBufferSpecImpl e = new EmptyLineViewBufferSpecImpl(100);
			e.append(new KyoroString("--1--\n"));
			e.append(new KyoroString("--2--\n"));
			e.append(new KyoroString("--3--\n"));
			e.append(new KyoroString("--4--\n"));
			EditableLineView v = new EditableLineView(e, 22, 256);
			v.setRect(200, 200);
			mStage.getRoot().addChild(v);
		}
		if (true) {
			EmptyLineViewBufferSpecImpl e = new EmptyLineViewBufferSpecImpl(100);
			e.append(new KyoroString("--1--\n"));
			e.append(new KyoroString("--2--\n"));
			e.append(new KyoroString("--3--\n"));
			e.append(new KyoroString("--4--\n"));

			g.setFont(g.getFont().deriveFont(11));
			TextViewer viewer = new TextViewer(e, 11, 400, 10,
					new SimpleFontForJ2SE(null, g.getFontMetrics()), "utf8");
			readStartupMessage(viewer);
			viewer.setRect(800, 800);
			mStage.getRoot().addChild(viewer);
//			viewer.getLineView().setMode(EditableLineView.MODE_SELECT);
			viewer.getLineView().setMode(EditableLineView.MODE_EDIT);
		}
	}

	public void readStartupMessage(TextViewer v) {
		try {
			File dir = new File("./");
			File filePathOfStartMessage = new File(dir, "startup_message.txt");
			createStartupMessageIfNonExist(filePathOfStartMessage);
			v.readFile(filePathOfStartMessage, false);
			// readFile(filePathOfStartMessage, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createStartupMessageIfNonExist(File f) throws IOException {
		if (f.exists()) {
			return;
		}
		f.createNewFile();
		FileOutputStream output = new FileOutputStream(f);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				output));
		for (String s : message) {
			output.write(s.getBytes("utf8"));
			// writer.write(s);
		}
		writer.close();
		output.close();
	}

	String[] message = { "Sorry, this application is pre-alpha version\n",
			"Testing and Developing.. now\n",
			"Please mail kyorohiro.android@gmail.com, \n",
			"If you have particular questions or comments, \n",
			"please don't hesitate to contact me. Thank you. \n" };

	private static class TestDisplayObject extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
//			System.out.println("begin paint");
			SimpleGraphicsForJ2SE graj2se = (SimpleGraphicsForJ2SE) graphics;
			graj2se.drawBackGround(0xAAFFFF00);
			graphics.setColor(0xFFFF0000);
			// graphics.drawLine(0, 0, 100, 100);
			graphics.drawCircle(0, 0, 100);
			// graphics.setColor(0xFFFF00);
			// graj2se.setTextSize(20);
			// graj2se.drawText("text", 50, 50);
			float[] widths = { 10, 10, 10, 10, 10, 10 };
			graj2se.drawPosText("abcd".toCharArray(), widths, (float) 1.5, 0,
					3, 50, 50);
//			System.out.println("end paint");
		}
	}

}
