package com.kt.dataManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellReference;

public class ExcelCellRef {
	
	public static String getName(Cell cell, int cellIndex) {
		int cellNum = 0;

		if(cell != null) {
			cellNum = cell.getColumnIndex();
		}else {
			cellNum = cellIndex;
		}
		
		return CellReference.convertNumToColString(cellNum);
	}
	
	public static String getValue(Cell cell) {
		String value="";
		
		if(cell == null) {
			value="";
		}else {
			CellType type = cell.getCellType();
			switch (type) {
			case FORMULA:
				value = cell.getCellFormula();
				break;
			case NUMERIC:
				value = cell.getNumericCellValue() + "";
				break;
			case STRING:
				value = cell.getStringCellValue();
				break;
			case BOOLEAN:
				value = cell.getBooleanCellValue() + "";
				break;
			case ERROR:
				value = cell.getErrorCellValue() + "";
				break;
			case BLANK:
				value = "";
				break;

			default:
				value = cell.getStringCellValue();
				break;
			}
		}
		
		return value;
	}

}
