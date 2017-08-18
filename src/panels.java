/*
    A basic extension of the java.applet.Applet class
 */

import java.awt.*;
import java.applet.*;
import java.net.*;

public class panels extends Applet {
	void button1_Clicked(Event event) {


		//{{CONNECTION
		// Repaint the Canvas
		imageViewer1.repaint();
		//}}

		//{{CONNECTION
		// Show the Applet
		show();
		//}}

		//{{CONNECTION
		// Display an image...
		try {
			MediaTracker tracker = new MediaTracker(this);
			Image image = getImage(getCodeBase(), "file://C:/VCTrial/Bin/Projects/Brain.jpg");
			tracker.addImage(image, 0);
			tracker.waitForID(0);
			if (!tracker.isErrorID(0))
				imageViewer1.drawImage(image,0,0,this);
		} catch(Exception e) {}
		//}}

		//{{CONNECTION
		// Repaint the ImageViewer
		imageViewer1.repaint();
		//}}
	}


	public void init() {
		super.init();

		// Take out this line if you don't use symantec.itools.net.RelativeURL
        symantec.itools.lang.Context.setDocumentBase(getDocumentBase());

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(426,266);
		borderPanel1 = new symantec.itools.awt.BorderPanel();
		borderPanel1.setLayout(null);
		borderPanel1.reshape(36,12,359,239);
		borderPanel1.setBackground(new Color(12632256));
		add(borderPanel1);
		textField1 = new java.awt.TextField();
		textField1.setText("default");
		textField1.reshape(206,24,96,24);
		borderPanel1.add(textField1);
		button1 = new java.awt.Button("aperte");
		button1.reshape(218,60,73,53);
		borderPanel1.add(button1);
		imageViewer1 = new symantec.itools.multimedia.ImageViewer();
		imageViewer1.reshape(26,12,170,180);
		imageViewer1.setBackground(new Color(16711680));
		borderPanel1.add(imageViewer1);
		//}}
	}

	public boolean handleEvent(Event event) {
		if (event.target == button1 && event.id == Event.ACTION_EVENT) {
			button1_Clicked(event);
			return true;
		}
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	symantec.itools.awt.BorderPanel borderPanel1;
	java.awt.TextField textField1;
	java.awt.Button button1;
	symantec.itools.multimedia.ImageViewer imageViewer1;
	//}}
}
