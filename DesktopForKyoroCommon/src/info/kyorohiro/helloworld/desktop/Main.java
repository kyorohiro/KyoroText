package info.kyorohiro.helloworld.desktop;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.j2se.adapter.SimpleStageForJ2SE;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.im.InputContext;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodContext;
import java.awt.im.spi.InputMethodDescriptor;
import java.lang.Character.Subset;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	public static void main(String[] args) {
		System.out.println("hello world1");
		Main main = new Main();
		main.init();
		System.out.println("/hello world1");	
	}

	private SimpleStage mStage = null;

	private void init() {
		JFrame frame = new JFrame("hello swing");
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setBounds(100,100,100,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
		initKyoroCommon(frame.getContentPane());
		testItem();
		mStage.start();
	}

	private void initKyoroCommon(Container appRoot) {
		SimpleStageForJ2SE stage = new SimpleStageForJ2SE();
		stage.setVisible(true);
		stage.setPreferredSize(new Dimension(200, 200));
		appRoot.add(stage);
		mStage = stage;
	}

	private void testItem() {
		SimpleDisplayObject child = new TestDisplayObject();
		mStage.getRoot().addChild(child);
	}

	private static class TestDisplayObject extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			System.out.println("begin paint");
			graphics.setColor(0xFF0000);
			graphics.drawLine(0, 0, 100, 100);
			graphics.drawCircle(0, 0, 100);
			System.out.println("end paint");
		}		
	}
	
	
}
