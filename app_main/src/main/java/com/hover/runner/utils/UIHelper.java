package com.hover.runner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.MainActivity;
import com.hover.runner.R;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.GlobalNav;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.interfaces.ParserClickListener;

public class UIHelper {
	private static final int INITIAL_ITEMS_FETCH = 30;

	public static void doGlobalNavigation(GlobalNav nav, Activity activity) {
		Intent intent = new Intent(activity, MainActivity.class);
		intent.putExtra(Apis.GLOBAL_NAV, nav);
		activity.startActivity(intent);
		activity.finishAffinity();
	}

	public static LinearLayoutManager setMainLinearManagers(Context context) {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
		linearLayoutManager.setInitialPrefetchItemCount(INITIAL_ITEMS_FETCH);
		linearLayoutManager.setSmoothScrollbarEnabled(true);
		return  linearLayoutManager;
	}

	public static int getActionIconDrawable(StatusEnums enums) {
		if (enums == StatusEnums.PENDING) return R.drawable.ic_warning_yellow_24dp;
		else if (enums == StatusEnums.UNSUCCESSFUL) return R.drawable.ic_error_red_24dp;
		else return R.drawable.ic_check_circle_green_24dp;
	}

	public static void setTextUnderline(TextView textView, String cs) {
		SpannableString content = new SpannableString(cs);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		content.setSpan(android.graphics.Typeface.BOLD, 0, content.length(), 0);
		try{
			textView.setText(content);
		}catch (Exception e) {
			//Avoid error due to threading based on users aggressive clicks.
			//I.e when user types in the search, it waits for 1.5secs to update the textView,
			//During this 1.5sec if user goes away from the screen it can through an error called:
			//CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
			//Therefore, putting this in a try and catch to avoid crashing.
		}

	}

	public static void removeTextUnderline(TextView textView) {
		SpannableString ss= new SpannableString(textView.getText());
		UnderlineSpan[] spans=ss.getSpans(0, textView.getText().length(), UnderlineSpan.class);
		for (UnderlineSpan span : spans) {
			ss.removeSpan(span);
		}
	}

	public static void changeStatusBarColor(final Activity activity, final int color) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
			return;

		final Window window = activity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		window.setStatusBarColor(color);
	}

	public static void makeEachTextLinks(final String text, final TextView tv, ParserClickListener clickListener) {
		if (text == null || tv == null) { return; }
		final SpannableString ss = new SpannableString(text);
		final String[] items = text.split(", ");
		int start = 0, end;
		for ( String item : items) {
			end = start + item.length();
			if (start < end) {
				ss.setSpan(new UnderlineSpan(), start, end, 0);
				ss.setSpan(new MyClickableSpan(item, clickListener), start, end, 0);
				ss.setSpan(new ForegroundColorSpan(Color.WHITE), start,end, 0);
			}
			start += item.length() + 2;//comma and space in the original text ;)
		}
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		tv.setText(ss, TextView.BufferType.SPANNABLE);
	}

	private static class MyClickableSpan extends ClickableSpan {
		private final String mText;
		private final ParserClickListener clickListener;

		private MyClickableSpan(final String text, ParserClickListener clickListener) {
			this.mText = text;
			this.clickListener = clickListener;
		}
		@Override
		public void onClick(final View widget) {
			clickListener.onClickParser(mText.replace(" ", "").trim());
		}
	}



	public static void flashMessage(Context context, @Nullable View view, String message) {
		if (view == null) flashMessage(context, message);
		else Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
	}
	public static void flashMessage(Context context, String message) {
		if(context == null) context = ApplicationInstance.getContext();
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static PackageInfo getPackageInfo(Context c) {
		try {
			return c.getPackageManager().getPackageInfo(getPackage(c), 0);
		} catch (PackageManager.NameNotFoundException ignored) { }
		return null;
	}

	private static String getPackage(Context c) {
		try {
			return c.getApplicationContext().getPackageName();
		} catch (NullPointerException e) {
			return "fail";
		}
	}





}
