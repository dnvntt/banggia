package priceboard.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonParser {
	
	private static final Logger log = LoggerFactory.getLogger(JsonParser.class);
	
	private static final String TYPE_PATH = "/type";
	
	private static final String DATA_PATH = "/data";
	
	private static final String DATA_NAME_PATH = "/data/params/name";
	
	private static final String DATA_CODES_PATH = "/data/params/codes";
	
	private static final String STOCK = "STOCK";
	
	private static final String RETURN_DATA = "returnData";

	private ObjectMapper objectMapper;

	public JsonParser() {
		objectMapper = new ObjectMapper();
	}
	
	public JsonNode parse(String json) {
		JsonNode node;
		try {
			node = objectMapper.readTree(json);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return node;
	}

	public String parseType(JsonNode node) {
		return node.at(TYPE_PATH).asText();
	}

	public String parseDataName(JsonNode node) {
		return node.at(DATA_NAME_PATH).asText();
	}

	public List<String> parseDataCodes(JsonNode node) {
		ArrayNode nodes = (ArrayNode) node.at(DATA_CODES_PATH);
		Iterator<JsonNode> iterator = nodes.iterator();
		List<String> codes = new ArrayList<String>();
		while(iterator.hasNext()) {
			String code = iterator.next().asText();
			if (code != null && code.trim().length() > 0)
				codes.add(code);
		}
		return codes;
	}

	public String parseData(JsonNode node) {
		return node.at(DATA_PATH).toString();
	}

	public String buildReturnJsonStockAsString(String data) {
		Data dataObject = new Data();
		dataObject.setType("STOCK");
		dataObject.setData(data == null ? "" : data);
		try {
			return objectMapper.writeValueAsString(dataObject);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}


	public String buildReturnJsonStockAsString(Map<String, String> dataMap) {
		JsonResponse response = new JsonResponse();
		Map<String, String> properties = new HashMap<String, String>();
		properties.putAll(dataMap);
		MultipleData dataObject = new MultipleData();
		response.setType(RETURN_DATA);
		dataObject.setName(STOCK);
		dataObject.setData(properties);
		response.setData(dataObject);
		try {
			return objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
	
}

 