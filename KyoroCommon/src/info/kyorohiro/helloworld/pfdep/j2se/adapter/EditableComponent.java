package info.kyorohiro.helloworld.pfdep.j2se.adapter;

/*
*/

import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleKeyEvent;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;


public class EditableComponent extends JLabel implements KeyListener, FocusListener {

    private boolean mIsFocus;
    private InputConnectionPlus mInputConnection = new InputConnectionPlus();

    public EditableComponent(String name, boolean enableInputMethods) {
    	super();
    	setForeground(Color.black);
    	setBackground(Color.white);
    	setVisible(true);
    	setEnabled(true);
    	addKeyListener(this);
    	addFocusListener(this);
    	addMouseListener(new MyMouseFocusListener());
    	enableInputMethods(enableInputMethods);
    }

	public MyInputConnection getCurrentInputConnection() {
		return mInputConnection;
	}

	@Override
    public void keyTyped(KeyEvent event) {
        char keyChar = event.getKeyChar();

    	System.out.println("keyTyped="+keyChar+",keyChar="+event.getKeyChar()+":"+(int)event.getKeyChar()+","+event.getKeyCode());
    	System.out.println("keyTyped ext="+event.getExtendedKeyCode()+","+event.getModifiers());
    	if(event.getKeyChar() == KeyEvent.VK_CONTROL){
    		CommitText c = new CommitText(SimpleKeyEvent.KEYCODE_DEL);
    		//c.isKeyCode();
        	mInputConnection.addCommitText(c);
    	} else 
    	if(event.getKeyChar() == KeyEvent.VK_BACK_SPACE){
    		CommitText c = new CommitText(SimpleKeyEvent.KEYCODE_DEL);
    		//c.isKeyCode();
        	mInputConnection.addCommitText(c);
    	} else if(event.getKeyChar() == KeyEvent.VK_ENTER){
    		CommitText c = new CommitText(SimpleKeyEvent.KEYCODE_ENTER);
    		//c.isKeyCode();
        	mInputConnection.addCommitText(c);
    	} else {

    		CommitText c = null;
    		if((InputEvent.CTRL_DOWN_MASK&event.getModifiersEx())==InputEvent.CTRL_DOWN_MASK) {
    			System.out.println("---ctl--"+(""+(char)('a'+event.getKeyChar()-1)));
        		c = new CommitText(""+(char)('a'+event.getKeyChar()-1),0);
    			c.pushingCtrl(true);  
            	mInputConnection.addCommitText(c);
    			
    		} else {
        		c = new CommitText(""+event.getKeyChar(),1);
    			c.pushingCtrl(false);  
            	mInputConnection.addCommitText(c);
    		}
    	}
       	event.consume();
	}

    @Override
    public void keyPressed(KeyEvent event) {
    	
    }

    @Override
    public void keyReleased(KeyEvent event) {
    	
    }

    @Override
    public void focusGained(FocusEvent event) {
    	mIsFocus = true;
    	repaint();
    }

    @Override
    public void focusLost(FocusEvent event) {
    	mIsFocus = false;
    	repaint();
    }    

    class MyMouseFocusListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
        	EditableComponent.this.requestFocus();
        } 
    }
}

