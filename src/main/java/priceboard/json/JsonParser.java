package priceboard.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonParser {
	private static final Logger logger = Logger.getLogger(JsonParser.class);
	
	private static final String TYPE_PATH = "/type";
	
	private static final String DATA_PATH = "/data";
	
	private static final String DATA_NAME_PATH = "/data/params/name";
	
	private static final String DATA_REGIST_PATH = "/data/isIntervalRegist";
	
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
			logger.error(e.getMessage(), e);
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
	
	public Boolean parseDataRegistInterval(JsonNode node) {
		JsonNode registNode = node.at(DATA_REGIST_PATH);
		if (registNode != null) {
			return registNode.asBoolean();
		}
		return false;
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

	public String buildReturnJsonStockAsString(String type, Object data) {
		Data dataObject = new Data();
		dataObject.setType(type);
		dataObject.setData(data == null ? "" : data);
		try {
			return objectMapper.writeValueAsString(dataObject);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	public String objectToString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
	public <T> T stringToObject(String data, Class<T> clazz) {
		try {
			return objectMapper.readValue(data, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}

 