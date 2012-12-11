package info.kyorohiro.helloworld.pfdep.j2se.adapter;

/*
 * Copyright 2002 Sun Microsystems, Inc. All  Rights Reserved.
 *  
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the following 
 * conditions are met:
 * 
 * -Redistributions of source code must retain the above copyright  
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright 
 *  notice, this list of conditions and the following disclaimer in 
 *  the documentation and/or other materials provided with the 
 *  distribution.
 *  
 * Neither the name of Sun Microsystems, Inc. or the names of 
 * contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any 
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND 
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY 
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY 
 * DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF OR 
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR 
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE 
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, 
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER 
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF 
 * THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that Software is not designed, licensed or 
 * intended for use in the design, construction, operation or 
 * maintenance of any nuclear facility. 
 */

import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
    	System.out.println("keyTyped="+keyChar);
    	event.consume();
    	CommitText c = new CommitText(""+event.getKeyChar(),1);
    	mInputConnection.addCommitText(c);
//    	repaint();
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

