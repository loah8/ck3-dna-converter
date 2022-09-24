package org.simple.loah8.ck3.converter.service;

import lombok.SneakyThrows;
import org.junit.Test;
import org.simple.loah8.ck3.converter.GeneralTest;
import org.springframework.beans.factory.annotation.Autowired;

public class GeneOrderServiceTest extends GeneralTest {

	@Autowired
	private GeneOrderService geneOrderService;

	@Test
	@SneakyThrows
	public void createMap() {
		geneOrderService.createGeneOrderMap();;
	}
}
