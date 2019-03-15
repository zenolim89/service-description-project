package com.kt.testForCassandra;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class testForXlxs {

	public static void main(String arg[]) {

		String excelFile = "/home/ho/workbook.xlsx";
		// 새로운 Excel workbook 생성
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCellStyle LockedCellStyle = wb.createCellStyle();
		LockedCellStyle.setLocked(true);

		LockedCellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
		LockedCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
		LockedCellStyle.setAlignment(HorizontalAlignment.LEFT);
		LockedCellStyle.setWrapText(true);
		LockedCellStyle.setBorderTop(BorderStyle.DASHED);
		LockedCellStyle.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
		LockedCellStyle.setBorderBottom(BorderStyle.DASHED);
		LockedCellStyle.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
		LockedCellStyle.setBorderLeft(BorderStyle.DASHED);
		LockedCellStyle.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
		LockedCellStyle.setBorderRight(BorderStyle.DASHED);
		LockedCellStyle.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());

		HSSFCellStyle UnLockedCellStyle = wb.createCellStyle();
		UnLockedCellStyle.setLocked(false);
		UnLockedCellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		UnLockedCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
		UnLockedCellStyle.setAlignment(HorizontalAlignment.LEFT);
		UnLockedCellStyle.setWrapText(true);
		UnLockedCellStyle.setBorderTop(BorderStyle.DASHED);
		UnLockedCellStyle.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
		UnLockedCellStyle.setBorderBottom(BorderStyle.DASHED);
		UnLockedCellStyle.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
		UnLockedCellStyle.setBorderLeft(BorderStyle.DASHED);
		UnLockedCellStyle.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
		UnLockedCellStyle.setBorderRight(BorderStyle.DASHED);
		UnLockedCellStyle.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());

		// 새로운 Sheet 생성
		HSSFSheet sheet = wb.createSheet("First");
		// 셀 보호
		sheet.protectSheet("password");

		Row selectrow = sheet.createRow(11);
		Cell keycell = selectrow.createCell(0);
		keycell.setCellValue("Parameter");
		keycell.setCellStyle(LockedCellStyle);
		Cell selectcell = selectrow.createCell(1);
		selectcell.setCellValue("선택");
		selectcell.setCellStyle(UnLockedCellStyle);

		Row selectrow2 = sheet.createRow(12);
		Cell keycell2 = selectrow2.createCell(0);
		keycell2.setCellValue("Parameter");
		keycell2.setCellStyle(LockedCellStyle);
		Cell selectcell2 = selectrow2.createCell(1);
		selectcell2.setCellValue("선택");
		selectcell2.setCellStyle(UnLockedCellStyle);

		// 드롭다운 박스 값 지정
		String[] strFormula = new String[] { "100", "200", "300", "400", "500" };
		// 드롭다운 박스 범위 지정
		CellRangeAddressList addressList = new CellRangeAddressList();
		addressList.addCellRangeAddress(11, 1, 12, 1);
		DVConstraint constraing = DVConstraint.createExplicitListConstraint(strFormula);
		HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, constraing);
		// 공백무시 옵션 true : 무시, false: 무시안함
		dataValidation.setEmptyCellAllowed(true);
		// cell 선택시 설명메시지 보이기 옵션 true: 표시, false : 표시안함
		dataValidation.setShowPromptBox(true);
		// cell 선택시 드롭다운박스 list 표시여부 설정 true : 안보이게, false : 보이게
		dataValidation.setSuppressDropDownArrow(false);

		// 오류메시지 생성. 형식에 맞지 않는 데이터 입력시 createErrorBox(String title,String text)
		dataValidation.createErrorBox("텍스트입력불가", "리스트 항목을 선택해 주세요");
		dataValidation.createPromptBox("설명", "규격 Spec을 선택");
		dataValidation.setErrorStyle(HSSFDataValidation.ErrorStyle.STOP);
		sheet.addValidationData(dataValidation);

		// 칼럼 크기 조절
		sheet.setColumnWidth(0, 15 * 256);
		sheet.setColumnWidth(1, 20 * 256);

		// 생성한 Sheet에 Row 생성
		for (int i = 0; i < 5; i++) {
			Row row = sheet.createRow(i);
			// 생성한 Row에 Cell 값 입력
			for (int j = 0; j < 2; j++) {
				if (j == 0) {
					Cell cell = row.createCell(j);
					cell.setCellValue("서비스 명세 항목");
					cell.setCellStyle(LockedCellStyle);
				} else {
					Cell cell = row.createCell(j);
					cell.setCellValue("Value");
					cell.setCellStyle(UnLockedCellStyle);
				}
			}
		}

		for (int i = 6; i < 10; i++) {
			Row row = sheet.createRow(i);
			for (int j = 0; j < 2; j++) {
				if (j == 0) {
					Cell cell = row.createCell(j);
					cell.setCellValue("서비스 명세 항목");
					cell.setCellStyle(LockedCellStyle);
				} else {
					Cell cell = row.createCell(j);
					cell.setCellValue("Value");
					cell.setCellStyle(UnLockedCellStyle);
				}
			}
		}
		try {
			FileOutputStream fileOut = new FileOutputStream(excelFile);
			wb.write(fileOut);
			fileOut.close();
			wb.close();
			System.out.println("=====성공====");
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
