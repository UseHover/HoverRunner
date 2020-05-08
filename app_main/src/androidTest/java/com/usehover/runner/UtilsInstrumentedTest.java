package com.usehover.runner;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.usehover.runner.models.StreamlinedStepsModel;
import com.usehover.runner.utils.Dummy;
import com.usehover.runner.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {
@Test
public void useAppContext() {
	// Context of the app under test.
	Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

	assertEquals("com.usehover.testerv2", appContext.getPackageName());
}

	@Test
	public void test_streamlineStepsFromRaw() throws JSONException {
		String rootCode = "*737#";
		String rawArray =  Dummy.getStringTwo();
		JSONArray jsonArray = new JSONArray(rawArray);
		List<String> labels = Arrays.asList("Amount", "IUC", "TestLabel", "pin");
		List<String> desc = Arrays.asList("Amount of subcription",
				"Enter your IUC number",
				"To see how it looks like",
				"Enter your pin");

		String fullUSSD = "*737*8*Amount*IUC*TestLabel*pin#";
		StreamlinedStepsModel s1 = new StreamlinedStepsModel(fullUSSD, labels, desc);
		StreamlinedStepsModel s2 = Utils.getStreamlinedStepsStepsFromRaw(rootCode, jsonArray);

		Assert.assertEquals(s1.getStepsVariableDesc(), s2.getStepsVariableDesc());
		Assert.assertEquals(s1.getStepVariableLabel(), s2.getStepVariableLabel());
		Assert.assertEquals(s1.getFullUSSDCodeStep(), s2.getFullUSSDCodeStep());
	}
	@Test
	public void test_USSDJSONArrayToStringArray() throws JSONException {
		JSONArray ussdMessages = new JSONArray();
		ussdMessages.put("tobi");
		ussdMessages.put("jerry");
		ussdMessages.put("yomi");

		String[] stringList = Utils.convertNormalJSONArrayToStringArray(ussdMessages);
		Assert.assertEquals(stringList[0], ussdMessages.get(0));
		Assert.assertEquals(stringList[1], ussdMessages.get(1));
		Assert.assertEquals(stringList[2], ussdMessages.get(2));
	}
}
