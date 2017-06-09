package ru.aisa.rgd.etd.extend;

public class ShortContentDescriptor {
	String tagName;
	String prefix;
	String postfix;
	// -1 = last; null = first
	Boolean last;
	Boolean all;
	String allPostfix;
	private String notEmptyTagPostfix;
	
	int limit;
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public ShortContentDescriptor() {
		super();
		this.tagName = "";
		this.prefix = "";
		this.limit = -1;
		this.postfix = " ";
		this.last = false;
		this.all = false;
		this.allPostfix = "";
		this.notEmptyTagPostfix = "";
	}
	public ShortContentDescriptor(String tagName, String prefix, String postfix, int limit, Boolean last) {
		super();
		this.tagName = tagName;
		this.prefix = prefix;
		this.limit = limit;
		this.postfix = postfix;
		this.last = false;
		this.all = false;
		this.allPostfix = "";
		this.notEmptyTagPostfix = "";
	}
	public String getPostfix() {
		return postfix;
	}
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	public Boolean getLast() {
		return last;
	}
	public void setLast(Boolean last) {
		this.last = last;
	}
	public void setAll(Boolean all) {
		this.all = all;
	}
	public Boolean getAll() {
		return all;
	}
	public void setAllPostfix(String allPostfix) {
		this.allPostfix = allPostfix;
	}
	public String getAllPostfix() {
		return allPostfix;
	}
	public void setNotEmptyTagPostfix(String notEmptyTagPostfix) {
		this.notEmptyTagPostfix = notEmptyTagPostfix;
	}
	public String getNotEmptyTagPostfix() {
		return notEmptyTagPostfix;
	}

	

}
