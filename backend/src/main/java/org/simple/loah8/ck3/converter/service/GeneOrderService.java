package org.simple.loah8.ck3.converter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.simple.loah8.ck3.converter.constants.DNAConstants.FeFile.INDEX_MAP;


/**
 * saveGames 에서 .ck3 에서 ruler_design 데이터 에서 gene 만 추출 하여
 * resources/00_needFile/genes.txt  에 집어 넣는다.

 * hair_color={ 246 85 246 85 }
 * pose={ "" 255 "" 0 }
 * cloaks={ "gaelic_cloak" 156 "no_cloak" 0 }
 *
 * 해당 파일을 한줄씩 읽어서
 * {0=hair_color, 1=skin_color, 2=eye_color} 형태의 MAP 으로 컨버팅
 *
 */


@Service
public class GeneOrderService {
	private static final Logger LOG = LoggerFactory.getLogger(GeneOrderService.class);

	private static final String KEY_NAME = "keyName";
	private static final String regex = "(?<" + KEY_NAME + ">.*)=";    // regex : (?<keyName>.*)=
	private static final Pattern pattern = Pattern.compile(regex);
	private static final String FILE_PATH = "src/main/resources/00_needFile/genes.txt";
	private final int MXA_LOOP = 1000;

	private final CustomFileWriter customFileWriter;

	public GeneOrderService(CustomFileWriter customFileWriter) {
		this.customFileWriter = customFileWriter;
	}

	private Map<Integer, String> getOrderedGenes() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));

		int i = 0;
		String currentLine = reader.readLine();

		Map<Integer, String> indexAndKeyMap = new HashMap<>();

		while (StringUtils.hasLength(currentLine) && i < MXA_LOOP) {
			Matcher matcher = pattern.matcher(currentLine);
			matcher.find();
			String keyName = matcher.group(KEY_NAME);

			LOG.debug("index: {}, key: {}", i, keyName);
			indexAndKeyMap.put(i, keyName);

			i++;
			currentLine = reader.readLine();
		}

		LOG.info("result : {}", indexAndKeyMap);
		reader.close();

		return indexAndKeyMap;
	}

	public void createGeneOrderMap() throws IOException {
		customFileWriter.jsonFileWrite(INDEX_MAP.getResourcePath(), this.getOrderedGenes());
		Files.copy(Path.of(INDEX_MAP.getResourcePath()), Path.of(INDEX_MAP.getFEPath()), StandardCopyOption.REPLACE_EXISTING);
	}

}
