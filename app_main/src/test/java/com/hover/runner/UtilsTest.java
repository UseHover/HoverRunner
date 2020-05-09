package com.hover.runner;

import com.hover.runner.api.AlgorithmsPlay;
import com.hover.runner.utils.Utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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

		String testPassword5 = "AsdfDwhthwy~@#4234=-+|>,?&*(./sdfadfadf";
		assertTrue(Utils.validatePassword(testPassword5));

		String testPassword3 = "djfa dadf saf ";
		assertFalse(Utils.validatePassword(testPassword3));

		String testPassword2 = "dadfdasdfdsasdfdsadsfdasdfdsdfasdfdsadfdasdfdsdfasdfdsadfdsadfdasd";
		assertFalse(Utils.validatePassword(testPassword2));

		String testPassword4 = "1234";
		assertFalse(Utils.validatePassword(testPassword4));
	}

	@Test
	public void test_formatDate() {
		String expectedFormat = "21:35:50 (WAT) Apr 20, 2020";
		Assert.assertEquals(expectedFormat, Utils.formatDate(Long.valueOf("1587414950910")));
	}

	@Test
	public void test_findSingleAppearance() {
		int[] sampleList = new int[] {1,3,5,5,3,1,3,6,5,1,7,2,9,9,2,7,2,7,9};
		Assert.assertEquals(6, AlgorithmsPlay.findSingleAppearanceV2(sampleList));
	}

}