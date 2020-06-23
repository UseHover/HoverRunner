package com.hover.runner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.core.util.Pair;

import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.TransactionModels;
import com.hover.runner.utils.fonts.FontReplacer;
import com.hover.runner.utils.fonts.Replacer;

import java.util.ArrayList;
import java.util.List;


public class ApplicationInstance extends Application {
	@SuppressLint("StaticFieldLeak")
	private static Context context;

	//Skipped state
	private static boolean allowSkippedActionsToRun;
	private static int COLOR_RED, COLOR_YELLOW, COLOR_GREEN;

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

		allowSkippedActionsToRun = false;

		COLOR_RED = getResources().getColor(R.color.colorRed);
		COLOR_YELLOW = getResources().getColor(R.color.colorYellow);
		COLOR_GREEN = getResources().getColor(R.color.colorGreen);
	}

	public static int getColorRed() {
		return COLOR_RED;
	}

	public static int getColorYellow() {
		return COLOR_YELLOW;
	}

	public static int getColorGreen() {
		return COLOR_GREEN;
	}

	public static boolean isAllowSkippedActionsToRun() {
		return allowSkippedActionsToRun;
	}

	public static void setAllowSkippedActionsToRun(boolean allowSkippedActionsToRun) {
		ApplicationInstance.allowSkippedActionsToRun = allowSkippedActionsToRun;
	}

	public static Context getContext() {
		return context;
	}
}
