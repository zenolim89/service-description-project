package com.kt.dataManager;

import java.util.ArrayList;
import java.util.List;

public class ExcelReadOption {

	private String filePath;
	private List<String> outputColums;
	private int startRow;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<String> getOutputColums() {
		List<String> temp = new ArrayList<String>();
		temp.addAll(outputColums);
		return temp;
	}

	public void setOutputColums(List<String> outputColums) {
		List<String> temp = new ArrayList<String>();
		temp.addAll(outputColums);

		this.outputColums = temp;
	}

	public void setOutputColums(String... outputColums) {

		if (this.outputColums == null) {
			this.outputColums = new ArrayList<String>();
		}

		for (String outputColum : outputColums) {
			this.outputColums.add(outputColum);
		}
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

}
