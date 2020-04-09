package com.usehover.testerv2;

import com.usehover.testerv2.utils.UIHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(JUnit4.class)
public class UtilsTest {

	private UIHelper uiHelper;

	@Before
	public void setup() {
		uiHelper = new UIHelper();
	}
@Test
public void addition_isCorrect() {
	assertEquals(4, 2 + 2);
}

@Test
public void test_validateEmail() {
	String testEmail = "djaljdfhadjfadljfa@dafda";
	assertFalse(uiHelper.validateEmail(testEmail));

	String testEmail2 = "akinpeluocom@gmail.com";
	assertTrue(uiHelper.validateEmail(testEmail2));

}

@Test
public void test_validatePassword(){
	String testPassword = "asdfdwhthwy~@#4234=-+|>,?&*(./sdfadfadf";
	assertTrue(uiHelper.validatePassword(testPassword));

	String testPassword3 = "djfa dadf saf ";
	assertFalse(uiHelper.validatePassword(testPassword3));

	String testPassword2 = "dadfdasdfdsasdfdsadsfdasdfdsdfasdfdsadfdasdfdsdfasdfdsadfdsadfdasd";
	assertFalse(uiHelper.validatePassword(testPassword2));

	String testPassword4 = "1234";
	assertFalse(uiHelper.validatePassword(testPassword4));
}
}