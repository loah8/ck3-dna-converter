package org.simple.loah8.ck3.converter.service;

import org.junit.Test;
import org.simple.loah8.ck3.converter.GeneralTest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest extends GeneralTest {

	private static final String NAME = "key";

	@Test
	public void regexTest() {
//		Pattern pattern = Pattern.compile("(.*)=\\{ (\\d{1,3}|\"\\w*\") (\\d{1,3}|\"\\w*\") (\\d{1,3}|\"\\w*\") (\\d{1,3}|\"\\w*\") }");
		String regex = "(?<" + NAME + ">.*)=";
		Pattern pattern = Pattern.compile(regex);
		List<String> testString = List.of("hair_color={ 246 85 246 85 }", "pose={ \"\" 255 \"\" 0 }", "cloaks={ \"gaelic_cloak\" 156 \"no_cloak\" 0 }");

		testString.forEach(string -> {
			Matcher matcher = pattern.matcher(string);
			while (matcher.find()) {
				LOG.debug("string: {}, key : {}", string, matcher.group(NAME) );
			}
		});
	}
}
