package com.kt.B2B.SCVInfo.utilities;

public class ExcelMain {

	private static final String FILE_PATH = "C:/test.xlsx";
	
	public static void main(String[] args) {
	
		ExcelRead readdata = new ExcelRead();
		readdata.readExcelData(FILE_PATH);
	
	}
}
