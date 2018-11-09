package com.kt.B2B.SCVInfo.utilities;

// (jdlee) POI lib 3.15v
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelRead {

	public static void readExcelData(String ExcelfileName) {
		
		try {
			
			FileInputStream excelFile = new FileInputStream(new File(ExcelfileName));
		
			//(jdlee) 
			Workbook workbook = null;
			if(ExcelfileName.toLowerCase().endsWith("xlsx")){
				workbook = new XSSFWorkbook(excelFile);
			}else if(ExcelfileName.toLowerCase().endsWith("xls")){
				workbook = new HSSFWorkbook(excelFile);
			}
		
		    //(jdlee) Get the number of sheets in the xlsx file
			int numberOfSheets = workbook.getNumberOfSheets();
			
			//(jdlee) loop through each of the sheets
			for(int i=0; i < numberOfSheets; i++){
				
				//(jdlee) Get the nth sheet from the workbook
				Sheet sheet = workbook.getSheetAt(i);
				
				//(jdlee) every sheet has rows, iterate over them
		        Iterator<Row> iterator = sheet.iterator();
		
		        while (iterator.hasNext()) {
		
		        	// (jdlee) Row
		            Row currentRow = iterator.next();
		            Iterator<Cell> cellIterator = currentRow.iterator();
		
		            while (cellIterator.hasNext()) {
		
		            	// (jdlee) Cell
		                Cell currentCell = cellIterator.next();
		                
		                if (currentCell.getCellTypeEnum() == CellType.STRING) {
		                	System.out.print(currentCell.getStringCellValue() + " / ");
		                    // add value 
		                } 
		                else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
		                	System.out.print(currentCell.getNumericCellValue() + " / ");
		                    // add value
		                }
		
		            }
		            System.out.println();
		
		        }
			}
		
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ExcelRead() {
		super();

	}
	
}
