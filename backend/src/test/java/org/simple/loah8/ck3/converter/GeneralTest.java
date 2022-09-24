package org.simple.loah8.ck3.converter;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simple.loah8.ck3.converter.service.GeneJsonToObjectConverter;
import org.simple.loah8.ck3.converter.service.GeneOrderService;
import org.simple.loah8.ck3.converter.service.RawFileToJsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@RunWith(SpringRunner.class)
public class GeneralTest {

	protected final Logger LOG = LoggerFactory.getLogger(GeneralTest.class);

	@Autowired
	private RawFileToJsonConverter rawFileToJsonConverter;

	@Autowired
	private GeneJsonToObjectConverter geneJsonToObjectConverter;

	@Autowired
	private GeneOrderService geneOrderService;

	// TODO :  버젼에 맞는 ruler_design 형태를 resources/00_needFile/genes.txt 에 저장 한다.
	/* 아래와 같은 형태
	hair_color={ 246 85 246 85 }
	skin_color={ 67 71 67 71 }
	eye_color={ 208 184 208 184 }
	gene_chin_forward={ "chin_forward_pos" 143 "chin_forward_pos" 143 }
	...
	 */

	// 1. CK3 폴더에서 데이터를 읽어서 jsonResult/*.json 파일로 만듦
	@Test
	public void convertAllJson() {
		rawFileToJsonConverter.convertAllJson();
	}

	// CK version 과, parser 를 만든 날짜를 업데이트한다.
	@Test
	public void createInfoFile() {
		rawFileToJsonConverter.setVersions();
	}

	// 2. TODO : 생성된 json 파일들을 한번씩 들어가서 ctrl + Alt + L 로 정렬해준다
	// 컴마가 두번 찍힌다던가 하는 에러가 있음.

	// 3. FrontEnd 용으로 파일을 생성 한다.
	@Test
	@SneakyThrows
	public void createGeneOrderMap() {
		geneOrderService.createGeneOrderMap();
	}

	@Test
	public void createFEFiles() {
		geneJsonToObjectConverter.createFEMapFiles();
	}


}
