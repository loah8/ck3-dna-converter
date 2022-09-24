package org.simple.loah8.ck3.converter.service;

import org.junit.Test;
import org.simple.loah8.ck3.converter.GeneralTest;
import org.springframework.beans.factory.annotation.Autowired;

public class GeneJsonToObjectConverterTest extends GeneralTest {

	@Autowired
	private GeneJsonToObjectConverter geneJsonToObjectConverter;

	@Test
	public void TestCreateFEMapFiles() {
		geneJsonToObjectConverter.createFEMapFiles();
	}
}