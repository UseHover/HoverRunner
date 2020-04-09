package com.usehover.testerv2;

import android.app.Application;

import com.usehover.testerv2.utils.fonts.FontReplacer;
import com.usehover.testerv2.utils.fonts.Replacer;


public class ApplicationInstance extends Application {
@Override
public void onCreate() {
	super.onCreate();

	Replacer replacer = FontReplacer.Build(getApplicationContext());
	replacer.setDefaultFont("Gibson-Regular.otf");
	replacer.setBoldFont("Gibson-Semibold.otf");
	replacer.setItalicFont("Gibson-SemiBoldItalic.otf");
	replacer.setThinFont("Gibson-Light.otf");
	replacer.applyFont();

}
}
