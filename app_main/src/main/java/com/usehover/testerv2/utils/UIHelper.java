package com.usehover.testerv2.utils;

import android.app.Activity;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.usehover.testerv2.interfaces.ParserClickListener;

import java.util.Arrays;
import java.util.List;

public class UIHelper {
	public static void setTextUnderline(TextView textView, String cs) {
		SpannableString content = new SpannableString(cs);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		textView.setText(content);
	}

	public static void changeStatusBarColor(final Activity activity, final int color) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
			return;

		final Window window = activity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		window.setStatusBarColor(color);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void makeEachTextLinks(final String text, final TextView tv, ParserClickListener clickListener) {
				if (text == null || tv == null) {
						return;
					}
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

	public static void showHoverToast(Context context, @Nullable View view, String message) {
		if(view == null) showHoverToastV2(context, message);
		else Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
	}
	public static void showHoverToastV2(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static boolean validateEmail(String string) {
		if(string == null) return  false;
		return string.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
	}
	public static boolean validatePassword (String string) {
		if(string == null) return  false;
		return string.length() < 40 && string.length() >4 && !string.contains(" ");
	}



}
