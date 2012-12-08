package info.kyorohiro.helloworld.desktop;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
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
		JFrame frame = new JFrame("hello swing");
		TextComponent tc = new TextComponent();
		tc.setVisible(true);
		frame.getContentPane().setLayout(new FlowLayout());
		tc.setPreferredSize(new Dimension(200 , 200));
		frame.getContentPane().add(tc);
		frame.getContentPane().add(new Button("botton"));
		
		frame.setBounds(100,100,100,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		System.out.println("/hello world1");	
	}

	public static class TextComponent extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g){
			System.out.println("paint");	
			g.setColor(new Color(0xFF0000));
			g.drawString("adsfasdf", 10, 10);
			g.fillRect(0, 0, 100, 100);
			System.out.println("/paint");	
		}
	}
	public static class MainThread implements Runnable {
		Window mWindow = null;
		public MainThread(Window window) {
			mWindow = window;
		}

		@Override
		public void run() {
			while(true) {
				InputContext im = mWindow.getInputContext();
			}
		}
	}

}
