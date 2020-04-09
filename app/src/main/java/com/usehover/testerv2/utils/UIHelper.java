package com.usehover.testerv2.utils;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class UIHelper {
	public void setTextUnderline(TextView textView, String cs) {
		SpannableString content = new SpannableString(cs);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		textView.setText(content);
	}
}
