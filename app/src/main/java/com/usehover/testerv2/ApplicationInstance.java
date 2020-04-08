package com.usehover.testerv2;

import android.app.Application;

import com.sylversky.fontreplacer.FontReplacer;
import com.sylversky.fontreplacer.Replacer;

public class ApplicationInstance extends Application {
@Override
public void onCreate() {
	super.onCreate();

	Replacer replacer = FontReplacer.Build(getApplicationContext());
	replacer.setDefaultFont("Gibson-Regular.ttf");
	replacer.setBoldFont("Gibson-semibold.ttf");
	replacer.setItalicFont("Gibson-RegularItalic.ttf");
	replacer.applyFont();

}
}
