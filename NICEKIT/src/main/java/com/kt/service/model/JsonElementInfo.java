package com.kt.service.model;

public class JsonElementInfo {

	private String key;
	private String value;

	private String nodeType;
	private String path;

	private boolean isValueNode;
	private boolean isObject;
	private boolean isArray;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isValueNode() {
		return isValueNode;
	}

	public void setValueNode(boolean isValueNode) {
		this.isValueNode = isValueNode;
	}

	public boolean isObject() {
		return isObject;
	}

	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

}
