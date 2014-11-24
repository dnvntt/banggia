package priceboard.json;


public class JsonResponse {
	private String type;
	private MultipleData data;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public MultipleData getData() {
		return data;
	}
	
	public void setData(MultipleData data) {
		this.data = data;
	}
}
