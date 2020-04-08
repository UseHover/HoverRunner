package com.usehover.testerv2;

import android.app.Application;

import com.usehover.testerv2.engine.fonts.FontReplacer;
import com.usehover.testerv2.engine.fonts.Replacer;


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
