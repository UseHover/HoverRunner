package com.usehover.testerv2;

import android.app.Application;
import android.content.Context;

import com.usehover.testerv2.utils.fonts.FontReplacer;
import com.usehover.testerv2.utils.fonts.Replacer;


public class ApplicationInstance extends Application {
	static Context context;
	@Override
	public void onCreate() {
		super.onCreate();

		Replacer replacer = FontReplacer.Build(getApplicationContext());
		replacer.setDefaultFont("Gibson-Regular.otf");
		replacer.setBoldFont("Gibson-Bold.otf");
		replacer.setItalicFont("Gibson-SemiBoldItalic.otf");
		replacer.setThinFont("Gibson-Light.otf");
		replacer.applyFont();
		context = this;
	}

	public static Context getContext() {
		return context;
	}
}
