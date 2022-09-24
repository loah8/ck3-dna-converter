package org.simple.loah8.ck3.converter.service;


import org.junit.Test;
import org.simple.loah8.ck3.converter.GeneralTest;
import org.springframework.beans.factory.annotation.Autowired;

public class RawFileToJsonConverterTest extends GeneralTest {

	@Autowired
	private RawFileToJsonConverter rawFileToJsonConverter;

	@Test
	public void TestConvertAllJson() {
		rawFileToJsonConverter.convertAllJson();
	}
}
