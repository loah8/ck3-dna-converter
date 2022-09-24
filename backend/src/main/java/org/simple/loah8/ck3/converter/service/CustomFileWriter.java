package org.simple.loah8.ck3.converter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@Service
public class CustomFileWriter {

	private static final Logger LOG = LoggerFactory.getLogger(CustomFileWriter.class);

	private final ObjectMapper objectMapper;

	public CustomFileWriter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@SneakyThrows
	public void jsonFileWrite(String filePath, Map<?, ?> content) {
		File file = new File(filePath);
		file.getParentFile().mkdirs();  // 부모 path 없으면 생성
		file.createNewFile();   // 파일 없으면 만듦

		LOG.info("File {} is writing", file.getName());

		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		String indexMapJson =  objectMapper.writeValueAsString(content);
		writer.write(indexMapJson);
		writer.flush();
		writer.close();
	}
}
