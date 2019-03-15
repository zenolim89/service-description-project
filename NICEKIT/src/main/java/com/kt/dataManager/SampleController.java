package com.kt.dataManager;

import javax.xml.bind.ValidationEventHandler;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.XLSBUnsupportedException;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.parser.JSONParser;

public class SampleController {

	JSONParser parser = new JSONParser();
	Workbook sample = new XSSFWorkbook();

	//Sheet 이름 추가하여 앞 함수에서 For 문
	public Workbook createSample (String response) {

		Sheet sheet = sample.createSheet("웹캠서비스"); // 수신 받은 데이터를 기반으로 시트명 동적 생성

		sheet.setColumnWidth(0,  10000);
		sheet.setColumnWidth(9,  10000);
		
		sheet = this.createServiceDesc(sheet); //수신 받은 정보도 넘겨야 함
		sheet = this.createHeaderInformation(sheet); // 수신받은 정보도 넘겨야 함
//메소드








		return sample;

	}

	public CellStyle createCellStype () {

		CellStyle cellStyle = sample.createCellStyle();

		cellStyle.setWrapText(true);

		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());
		cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);

		Row row = null;
		Cell cell = null;

		return cellStyle;

	}
	
	public Sheet createRequestFormatForGET (Sheet sheet) {
		
		Sheet xls = sheet;
		
		Row row = null;
		Cell cell = null;
		
		row = xls.createRow(0);
		
		cell = row.createCell(0);
		cell.setCellValue("데이터 요청 시 (Request");
		cell.setCellStyle(this.createCellStype());
		
		row = xls.createRow(1);
		
		cell = row.createCell(0);
		cell.setCellValue("파라미터 항목");
		cell.setCellStyle(this.createCellStype());
		
		cell = row.createCell(1);
//		cell.setCellValue();
		
	
		
		
		
		return xls;
		
		
	}
	
	public Sheet createRequestFormatForPOST(Sheet sheet) {
		
		Sheet xls = sheet;
		
		return xls;
		
	}
	  
	public Sheet createSelectBoxItem(Sheet sheet ) {
		
		Sheet xls = sheet;
		
		DataValidation dataValidation = null;
		DataValidationConstraint constraint = null;
		DataValidationHelper validationHelper = null;
		
		
//		validationHelper = new XSSFDataValidationHelper(xls);
		
		
		
		
		return xls;
		
		
		
	}
	


	
	public Sheet createHeaderInformation (Sheet sheet) {
		
		Sheet xls = sheet;
		
		Row row = null;
		Cell cell = null;
		
		//첫번째 줄 생성
		
		row = xls.createRow(0);
		
		cell = row.createCell(0);
		cell.setCellValue("URL 정보");
		cell.setCellStyle(this.createCellStype());
		
		row = xls.createRow(1);
		
		cell = row.createCell(0);
		cell.setCellValue("상용서버 URL 정보");
		cell.setCellStyle(this.createCellStype());
		
		cell = row.createCell(1);
		cell.setCellValue("접속 가능한 서버 URL 정보를 입력하세요 EX) http://kt.com"); // 사용자 입력값
		
		row = xls.createRow(2);
		
		cell = row.createCell(0);
		cell.setCellValue("테스트서버 URL 정보");
		cell.setCellStyle(this.createCellStype());
		
		cell = row.createCell(1);
		cell.setCellValue("연동 규격 테스트 시 사용 가능한 서버 URL 정보를 입력하세요 EX) http://kt.com");
		
		row = xls.createRow(3);
		cell.setCellValue("");
		
		return xls;
				
	}
	
	


	public Sheet createServiceDesc (Sheet sheet) {

		Sheet xls = sheet;

		Row row = null;
		Cell cell = null;

		// 첫번째 줄 생성
		row = xls.createRow(0);

		// 첫번째 가로 셀 값 생성
		cell = row.createCell(0);
		cell.setCellValue("서비스 코드");
		cell.setCellStyle(this.createCellStype());

		// 두번째 가로 셀 값 생성
		cell = row.createCell(1);
		cell.setCellValue("수신 받은 서비스 코드 값");

		//두번째 줄 생성
		row = xls.createRow(1);

		//첫번째 가로 셀 값 생성
		cell = row.createCell(0);
		cell.setCellValue("서비스 제공 형태");
		cell.setCellStyle(this.createCellStype());

		//두번째 가로 셀 값 생성
		cell = row.createCell(1);
		cell.setCellValue("수신받은 서비스 제공 형태 값");

		//세번째 줄 생성
		row = xls.createRow(2);

		//첫번째 가로 셀 값 생성
		cell = row.createCell(0);
		cell.setCellValue("서비스 명");
		cell.setCellStyle(this.createCellStype());

		//두번째 가로 셀 값 생성
		cell = row.createCell(1);
		cell.setCellValue("수신받은 서비스 명 값 ");

		//네번째 줄 생성
		row = xls.createRow(3);

		//첫번째 가로 셀 값 생성
		cell = row.createCell(0);
		cell.setCellValue("서비스 개요");
		cell.setCellStyle(this.createCellStype());

		//두번째 가로 셀 값 생성
		cell = row.createCell(1);
		cell.setCellValue("수신받은 서비스 개요 값");

		//서비스 타입에 따라 Intent 행 생성 여부 결정
		if ("서비스 타입" == "시스템") {
			
			row = xls.createRow(4);
			cell.setCellValue("");
			
			return xls;
			
		} else {
			
			row = xls.createRow(4);
			cell.setCellValue("사용 Intent 명");
			cell.setCellStyle(this.createCellStype());
			
			cell.setCellValue("수신 받은 인테트 값");
			
			row = xls.createRow(5);
			cell.setCellValue("");
			
			
			return xls;
		}
			
		

	}


}

