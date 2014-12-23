package priceboard.reloaddata;

import java.util.List;

public class Category {
	
	private String categoryName;
	
	private List<String> codeList;

	public String getCategoryName() {
		return categoryName;
	}
	
	public String getCateName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}
	
}
