package org.simple.loah8.ck3.converter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import org.simple.loah8.ck3.converter.constants.DNAConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.simple.loah8.ck3.converter.constants.DNAConstants.CK_VERSION_FILE;
import static org.simple.loah8.ck3.converter.constants.DNAConstants.FeFile.INFO;


@Service
public class RawFileToJsonConverter {

	private static Logger LOG = LoggerFactory.getLogger(RawFileToJsonConverter.class);

	private static final String openObject = "{";
	private static final String closeObject = "}";
	private static final String parseToken = "=";

	private final ObjectMapper mapper;
	private final CustomFileWriter customFileWriter;

	public RawFileToJsonConverter(ObjectMapper mapper, CustomFileWriter customFileWriter) {
		this.mapper = mapper;
		this.customFileWriter = customFileWriter;
	}

	public void convertAllJson() {
		Arrays.stream(DNAConstants.JsonFileInfo.values()).distinct().forEach(this::convertJson);
	}

	public void setVersions() {
		this.setVersion();
	}

	@SneakyThrows
	private void setVersion() {
		String rawVersion = "rawVersion";
		String lastUpdate = "lastUpdate";

		LinkedHashMap<String, Object> versionMap = mapper.readValue(new File(CK_VERSION_FILE), LinkedHashMap.class);
		String version = (String) versionMap.get(rawVersion);
		ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

		Map<String, String> resultMap = Map.of(
				rawVersion, version,
				lastUpdate, now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
		);

		customFileWriter.jsonFileWrite(INFO.getResourcePath(), resultMap);
		Files.copy(Path.of(INFO.getResourcePath()), Path.of(INFO.getFEPath()), StandardCopyOption.REPLACE_EXISTING);
	}

	@SneakyThrows
	private void convertJson(DNAConstants.JsonFileInfo jsonFileInfo) {
		File resultFile = new File(jsonFileInfo.getAbsoluteJsonPath());
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFileInfo.getFullTxtPath())));
		BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile.getAbsolutePath(), false));

		String line;    // line 단위로 읽어온다.
		writer.write(openObject);
		while ((line = reader.readLine()) != null) {
			String trimmedLine = StringUtils.trimAllWhitespace(line);
			if (!StringUtils.hasLength(trimmedLine) || isCommentedString(trimmedLine) || isOneLineData(trimmedLine) || isWhiteButNotChecked(trimmedLine)) {
				continue;
			}
			writer.write(this.resultLine(trimmedLine));
			writer.flush();
		}
		writer.write(closeObject);
		reader.close();
		writer.close();
	}

	private String resultLine(final String line) {
		String thisString = parsedCommentString(line);

		if (this.isCloseLine(thisString)) {
			return "},";
		}
		if (!thisString.contains(parseToken)) {
			return null;
		}
		String[] data = thisString.split(parseToken);
		StringBuffer sb = new StringBuffer();
		sb.append('"');
		sb.append(data[0]);
		sb.append('"');
		sb.append(':');
		if (data[1].contains("{")) {
			sb.append(data[1]);
			return sb.toString();
		}
		if (data[1].contains("\"")) {
			sb.append(data[1]);
			sb.append(",");
			return sb.toString();
		}
		sb.append('"');
		sb.append(data[1]);
		sb.append('"');
		sb.append(",");
		return sb.toString();
	}

	// 주석인지 체크
	private boolean isCommentedString(String line) {
		return line.startsWith("@") || line.startsWith("#") || line.startsWith("\uFEFF");
	}

	// 한줄짜리 데이터는 필요없음
	private boolean isOneLineData(String line) {
		return line.contains(openObject) && line.contains(closeObject);
	}

	// 객체가 닫히는 라인인지 체크
	private boolean isCloseLine(String line) {
		return line.contains(closeObject);
	}

	// 눈에 안 보이는 공백인지 체크
	private boolean isWhiteButNotChecked(String line) {
		if (line.length() > 1) {
			return false;
		}
		return (line.getBytes()[0] == -17);
	}

	// 주석이랑 혼용된 경우, 주석 이전만 리턴한다.
	private String parsedCommentString(String line) {
		if (!line.contains("#")) {
			return line;
		}
		String[] data = line.split("#");
		return data[0];
	}

}
