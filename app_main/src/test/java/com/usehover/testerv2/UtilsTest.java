package com.usehover.testerv2;

import com.google.gson.JsonArray;
import com.usehover.testerv2.models.StreamlinedStepsModel;
import com.usehover.testerv2.utils.Utils;
import com.usehover.testerv2.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(JUnit4.class)
public class UtilsTest {

	@Test
	public void addition_isCorrect() {
		assertEquals(4, 2 + 2);
	}

	@Test
	public void test_validateEmail() {
		String testEmail = "djaljdfhadjfadljfa@dafda";
		assertFalse(Utils.validateEmail(testEmail));

		String testEmail2 = "akinpeluocom@gmail.com";
		assertTrue(Utils.validateEmail(testEmail2));

	}

	@Test
	public void test_validatePassword(){
		String testPassword = "asdfdwhthwy~@#4234=-+|>,?&*(./sdfadfadf";
		assertTrue(Utils.validatePassword(testPassword));

		String testPassword3 = "djfa dadf saf ";
		assertFalse(Utils.validatePassword(testPassword3));

		String testPassword2 = "dadfdasdfdsasdfdsadsfdasdfdsdfasdfdsadfdasdfdsdfasdfdsadfdsadfdasd";
		assertFalse(Utils.validatePassword(testPassword2));

		String testPassword4 = "1234";
		assertFalse(Utils.validatePassword(testPassword4));
	}


}