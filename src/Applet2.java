/*
    A basic extension of the java.applet.Applet class
 */

import java.awt.*;
import java.applet.*;

public class Applet2 extends Applet {

	public void init() {
		super.init();

		// Take out this line if you don't use symantec.itools.net.RelativeURL
        symantec.itools.lang.Context.setDocumentBase(getDocumentBase());

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(430,270);
		//}}
	}

	public boolean handleEvent(Event event) {
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	//}}
}
