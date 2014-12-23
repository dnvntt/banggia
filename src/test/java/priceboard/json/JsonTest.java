package priceboard.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonTest {
	
	private JsonParser jsonParser;
	
	@Before
	public void setUp() {
		jsonParser = new JsonParser();
	}
	
	@Test
	public void testJsonParseType() throws JsonProcessingException, IOException {
		JsonNode node = jsonParser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":0,\"params\":{\"name\":\"STOCK\",\"codes\":[\"\",\"VND\",\"FPT\",\"FLC\"]}}}");
		String type = jsonParser.parseType(node);
		Assert.assertEquals("registConsumer", type);
	}
	
	@Test
	public void testJsonParseTypeWithNoFieldDefinitionShouldReturnEmpty() throws JsonProcessingException, IOException {
		JsonNode node = jsonParser.parse("{\"typ\":\"registConsumer\",\"data\":{\"sequence\":0,\"params\":{\"name\":\"STOCK\",\"codes\":[\"\",\"VND\",\"FPT\",\"FLC\"]}}}");
		String type = jsonParser.parseType(node);
		Assert.assertEquals("", type);
	}
	
	@Test
	public void testJsonParseDataName() throws JsonProcessingException, IOException {
		JsonNode node = jsonParser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":0,\"params\":{\"name\":\"STOCK\",\"codes\":[\"\",\"VND\",\"FPT\",\"FLC\"]}}}");
		String name = jsonParser.parseDataName(node);
		Assert.assertEquals("STOCK", name);
	}
	
	@Test
	public void testJsonParseDataCodes() throws JsonProcessingException, IOException {
		JsonNode node = jsonParser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":0,\"params\":{\"name\":\"STOCK\",\"codes\":[\"\",\"VND\",\"FPT\",\"FLC\"]}}}");
		List<String> codes = jsonParser.parseDataCodes(node);
		Assert.assertTrue(codes.contains("VND"));
		Assert.assertTrue(codes.contains("FLC"));
		Assert.assertTrue(codes.contains("FPT"));
		Assert.assertEquals(3, codes.size());
	}
	
	@Test
	public void testJsonParseData() throws JsonProcessingException, IOException {
		JsonNode node = jsonParser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":0,\"params\":{\"name\":\"STOCK\",\"codes\":[\"\",\"VND\",\"FPT\",\"FLC\"]}}}");
		String data = jsonParser.parseData(node);
		Assert.assertEquals("{\"sequence\":0,\"params\":{\"name\":\"STOCK\",\"codes\":[\"\",\"VND\",\"FPT\",\"FLC\"]}}", data);
	}
	
	
	@Test
	public void testJsonParseBuildStockReturnData() throws JsonProcessingException, IOException {
		String actualReturn = jsonParser.buildReturnJsonStockAsString("this is data for VND");
		String expectedReturn = "{\"type\":\"STOCK\",\"data\":\"this is data for VND\"}";
		Assert.assertEquals(expectedReturn, actualReturn);
	}
	
	@Test
	public void testJsonParseBuildStockReturnDataWithMap() throws JsonProcessingException, IOException {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("VND", "this is data for VND");
		dataMap.put("HAG", "this is data for HAG");
		String actualReturn = jsonParser.buildReturnJsonStockAsString(dataMap);
		String expectedReturn = "{\"type\":\"returnData\",\"data\":{\"name\":\"STOCK\",\"data\":{\"VND\":\"this is data for VND\",\"HAG\":\"this is data for HAG\"}}}";
		Assert.assertEquals(expectedReturn, actualReturn);
	}

	
	@Test
	public void testConvertArrayToString() {
		String[] stockCompression = new String[] {"a|b|c", "d|e|f"};
		String expectedJson = "[\"a|b|c\",\"d|e|f\"]";
		String jsonAsString = jsonParser.objectToString(stockCompression);
		Assert.assertEquals(expectedJson, jsonAsString);
	}
	
}