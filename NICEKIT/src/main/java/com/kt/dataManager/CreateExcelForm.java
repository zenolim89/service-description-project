package com.kt.dataManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import com.kt.dataForms.BaseExcelForm;

public class CreateExcelForm {
	String FilePath;
	public HSSFWorkbook workbook = new HSSFWorkbook();
	ArrayList<BaseExcelForm> svcList;

	public CreateExcelForm() {

	}

	public CreateExcelForm(ArrayList<BaseExcelForm> _svcList, String _FilePath) {
		this.svcList = _svcList;
		this.FilePath = _FilePath + "/workbook.xlsx";
	}

	public void createSheet() {
		for (int i = 0; i < this.svcList.size(); i++) {
			HSSFSheet sheet = this.workbook.createSheet(this.svcList.get(i).getServiceName());
			sheet.protectSheet("password");
			sheet.setColumnWidth(0, 40 * 256);
			sheet.setColumnWidth(1, 40 * 256);
			sheet.setColumnWidth(2, 40 * 256);
			createServiceDesc(sheet, svcList.get(i));
			ReqProtocolForm(sheet, svcList.get(i));
			RespProtocolForm(sheet, svcList.get(i));
		}
	}

	public void createServiceDesc(HSSFSheet sheet, BaseExcelForm excelForm) {
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				Row row = sheet.createRow(i);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceName");
				keyCell.setCellStyle(LockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceName());
				valueCell.setCellStyle(LockedValueCellStyle());
			} else if (i == 1) {
				Row row = sheet.createRow(i);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("invokeType");
				keyCell.setCellStyle(LockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getInvokeType());
				valueCell.setCellStyle(LockedValueCellStyle());
			} else if (i == 2) {
				Row row = sheet.createRow(i);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceType");
				keyCell.setCellStyle(LockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceType());
				valueCell.setCellStyle(LockedValueCellStyle());
			} else if (i == 3) {
				Row row = sheet.createRow(i);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceLink");
				keyCell.setCellStyle(LockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceLink());
				valueCell.setCellStyle(LockedValueCellStyle());
			} else if (i == 4) {
				Row row = sheet.createRow(i);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceDesc");
				keyCell.setCellStyle(LockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceDesc());
				valueCell.setCellStyle(LockedValueCellStyle());
			} else if (i == 5) {
				Row row = sheet.createRow(i);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceCode");
				keyCell.setCellStyle(LockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceCode());
				valueCell.setCellStyle(LockedValueCellStyle());
			}
			if (excelForm.getIntentInfo() != null) {
				Row row = sheet.createRow(6);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("intentInfo");
				keyCell.setCellStyle(LockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getDicInfo());
				valueCell.setCellStyle(LockedValueCellStyle());
			}
			Row row = sheet.createRow(7);
			Cell keyCell = row.createCell(0);
			keyCell.setCellValue("전송방식");
			keyCell.setCellStyle(LockedKeyCellStyle());
			Cell valueCell = row.createCell(1);
			valueCell.setCellValue("전송방식 선택");
			valueCell.setCellStyle(UnLockedCellStyle());
			sheet.addValidationData(transDropDownList(7, 1, 7, 1));
		}
	}

	public void ReqProtocolForm(HSSFSheet sheet, BaseExcelForm excelForm) {
		Row headRow = sheet.createRow(9);
		Cell titleCell = headRow.createCell(0);
		titleCell.setCellValue("Request Param");
		titleCell.setCellStyle(LockedKeyCellStyle());

		Row specRow = sheet.createRow(10);
		Cell specKey = specRow.createCell(0);
		specKey.setCellValue("Parameter");
		specKey.setCellStyle(LockedKeyCellStyle());
		Cell specValue = specRow.createCell(1);
		specValue.setCellValue("Value");
		specValue.setCellStyle(LockedKeyCellStyle());
		Cell specNote = specRow.createCell(2);
		specNote.setCellValue("Note");
		specNote.setCellStyle(LockedKeyCellStyle());

		for (int i = 11; i < 16; i++) {
			Row selectrow = sheet.createRow(i);
			Cell keycell = selectrow.createCell(0);
			keycell.setCellValue("Parameter");
			keycell.setCellStyle(UnLockedTempCellStyle());
			Cell selectcell = selectrow.createCell(1);
			selectcell.setCellValue("Spec 선택");
			selectcell.setCellStyle(UnLockedTempCellStyle());
			Cell notecell = selectrow.createCell(2);
			notecell.setCellValue("Note");
			notecell.setCellStyle(UnLockedTempCellStyle());
		}
		sheet.addValidationData(specDropDownList(11, 1, 16, 1));

		Row reqTitleRow = sheet.createRow(16);
		Cell reqCell = reqTitleRow.createCell(0);
		reqCell.setCellValue("Request Example");
		reqCell.setCellStyle(LockedKeyCellStyle());

		Row exampleRow = sheet.createRow(17);
		exampleRow.setHeight((short) 2000);
		Cell reqExCell = exampleRow.createCell(0);
		reqExCell.setCellValue("Request Example");
		sheet.addMergedRegion(new CellRangeAddress(exampleRow.getRowNum(), exampleRow.getRowNum(), 0, 2));
		reqExCell.setCellStyle(UnLockedTempCellStyle());
	}

	public void RespProtocolForm(HSSFSheet sheet, BaseExcelForm excelForm) {
		Row headRow = sheet.createRow(19);
		Cell titleCell = headRow.createCell(0);
		titleCell.setCellValue("Response Param");
		titleCell.setCellStyle(LockedKeyCellStyle());

		Row specRow = sheet.createRow(20);
		Cell specKey = specRow.createCell(0);
		specKey.setCellValue("Parameter");
		specKey.setCellStyle(LockedKeyCellStyle());
		Cell specValue = specRow.createCell(1);
		specValue.setCellValue("Value");
		specValue.setCellStyle(LockedKeyCellStyle());
		Cell specNote = specRow.createCell(2);
		specNote.setCellValue("Note");
		specNote.setCellStyle(LockedKeyCellStyle());

		for (int i = 21; i < 26; i++) {
			Row selectrow = sheet.createRow(i);
			Cell keycell = selectrow.createCell(0);
			keycell.setCellValue("Parameter");
			keycell.setCellStyle(UnLockedTempCellStyle());
			Cell selectcell = selectrow.createCell(1);
			selectcell.setCellValue("Spec 선택");
			selectcell.setCellStyle(UnLockedTempCellStyle());
			Cell notecell = selectrow.createCell(2);
			notecell.setCellValue("Note");
			notecell.setCellStyle(UnLockedTempCellStyle());
		}
		sheet.addValidationData(specDropDownList(21, 1, 26, 1));

		Row reqTitleRow = sheet.createRow(26);
		Cell reqCell = reqTitleRow.createCell(0);
		reqCell.setCellValue("Response Example");
		reqCell.setCellStyle(LockedKeyCellStyle());

		Row exampleRow = sheet.createRow(27);
		exampleRow.setHeight((short) 2000);
		Cell reqExCell = exampleRow.createCell(0);
		reqExCell.setCellValue("Response Example");
		sheet.addMergedRegion(new CellRangeAddress(exampleRow.getRowNum(), exampleRow.getRowNum(), 0, 2));
		reqExCell.setCellStyle(UnLockedTempCellStyle());
	}

	public HSSFCellStyle LockedKeyCellStyle() {
		HSSFCellStyle LockedKeyCellStyle = workbook.createCellStyle();
		HSSFFont fontStyle = workbook.createFont();
		fontStyle.setColor(IndexedColors.LIGHT_YELLOW.getIndex());
		fontStyle.setBold(true);
		LockedKeyCellStyle.setFont(fontStyle);
		LockedKeyCellStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
		LockedKeyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		LockedKeyCellStyle.setAlignment(HorizontalAlignment.LEFT);
		LockedKeyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		LockedKeyCellStyle.setBorderTop(BorderStyle.DASHED);
		LockedKeyCellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		LockedKeyCellStyle.setBorderBottom(BorderStyle.DASHED);
		LockedKeyCellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		LockedKeyCellStyle.setBorderLeft(BorderStyle.DASHED);
		LockedKeyCellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		LockedKeyCellStyle.setBorderRight(BorderStyle.DASHED);
		LockedKeyCellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		return LockedKeyCellStyle;
	}

	public HSSFCellStyle LockedValueCellStyle() {
		HSSFCellStyle LockedValueCellStyle = workbook.createCellStyle();
		LockedValueCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		LockedValueCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		LockedValueCellStyle.setAlignment(HorizontalAlignment.LEFT);
		LockedValueCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		LockedValueCellStyle.setBorderTop(BorderStyle.DASHED);
		LockedValueCellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		LockedValueCellStyle.setBorderBottom(BorderStyle.DASHED);
		LockedValueCellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		LockedValueCellStyle.setBorderLeft(BorderStyle.DASHED);
		LockedValueCellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		LockedValueCellStyle.setBorderRight(BorderStyle.DASHED);
		LockedValueCellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		return LockedValueCellStyle;
	}

	public HSSFCellStyle UnLockedCellStyle() {
		HSSFCellStyle UnLockedCellStyle = workbook.createCellStyle();
		UnLockedCellStyle.setLocked(false);
		UnLockedCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		UnLockedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		UnLockedCellStyle.setAlignment(HorizontalAlignment.LEFT);
		UnLockedCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		UnLockedCellStyle.setBorderTop(BorderStyle.DASHED);
		UnLockedCellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		UnLockedCellStyle.setBorderBottom(BorderStyle.DASHED);
		UnLockedCellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		UnLockedCellStyle.setBorderLeft(BorderStyle.DASHED);
		UnLockedCellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		UnLockedCellStyle.setBorderRight(BorderStyle.DASHED);
		UnLockedCellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		return UnLockedCellStyle;
	}

	public HSSFCellStyle UnLockedTempCellStyle() {
		HSSFCellStyle UnLockedTempCellStyle = workbook.createCellStyle();
		UnLockedTempCellStyle.setLocked(false);
		HSSFFont fontStyle = workbook.createFont();
		fontStyle.setColor(IndexedColors.GREY_40_PERCENT.getIndex());
		fontStyle.setItalic(true);
		UnLockedTempCellStyle.setFont(fontStyle);
		UnLockedTempCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		UnLockedTempCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		UnLockedTempCellStyle.setAlignment(HorizontalAlignment.LEFT);
		UnLockedTempCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		UnLockedTempCellStyle.setBorderTop(BorderStyle.DASHED);
		UnLockedTempCellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		UnLockedTempCellStyle.setBorderBottom(BorderStyle.DASHED);
		UnLockedTempCellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		UnLockedTempCellStyle.setBorderLeft(BorderStyle.DASHED);
		UnLockedTempCellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		UnLockedTempCellStyle.setBorderRight(BorderStyle.DASHED);
		UnLockedTempCellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		return UnLockedTempCellStyle;
	}

	public HSSFDataValidation specDropDownList(int firstRow, int firstCol, int lastRow, int lastCol) {
		// 드롭다운 박스 값 지정
		String[] strFormula = new String[] { "고정값", "설정값", "발화 어휘" };
		// 드롭다운 박스 범위 지정
		CellRangeAddressList addressList = new CellRangeAddressList();
		addressList.addCellRangeAddress(firstRow, firstCol, lastRow, lastCol);
		DVConstraint constraing = DVConstraint.createExplicitListConstraint(strFormula);
		HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, constraing);
		// 공백무시 옵션 true : 무시, false: 무시안함
		dataValidation.setEmptyCellAllowed(true);
		// cell 선택시 설명메시지 보이기 옵션 true: 표시, false : 표시안함
		dataValidation.setShowPromptBox(true);
		// cell 선택시 드롭다운박스 list 표시여부 설정 true : 안보이게, false : 보이게
		dataValidation.setSuppressDropDownArrow(false);
		// 오류메시지 생성. 형식에 맞지 않는 데이터 입력시 createErrorBox(String title,String text)
		dataValidation.createErrorBox("No Input, Select Only", "리스트 항목을 선택해 주세요");
		dataValidation.createPromptBox("Descrioption", "규격 Spec을 선택");
		dataValidation.setErrorStyle(HSSFDataValidation.ErrorStyle.STOP);
		return dataValidation;
	}
	
	public HSSFDataValidation transDropDownList(int firstRow, int firstCol, int lastRow, int lastCol) {
		// 드롭다운 박스 값 지정
		String[] strFormula = new String[] { "GET", "POST" };
		// 드롭다운 박스 범위 지정
		CellRangeAddressList addressList = new CellRangeAddressList();
		addressList.addCellRangeAddress(firstRow, firstCol, lastRow, lastCol);
		DVConstraint constraing = DVConstraint.createExplicitListConstraint(strFormula);
		HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, constraing);
		// 공백무시 옵션 true : 무시, false: 무시안함
		dataValidation.setEmptyCellAllowed(true);
		// cell 선택시 설명메시지 보이기 옵션 true: 표시, false : 표시안함
		dataValidation.setShowPromptBox(true);
		// cell 선택시 드롭다운박스 list 표시여부 설정 true : 안보이게, false : 보이게
		dataValidation.setSuppressDropDownArrow(false);
		// 오류메시지 생성. 형식에 맞지 않는 데이터 입력시 createErrorBox(String title,String text)
		dataValidation.createErrorBox("No Input, Select Only", "리스트 항목을 선택해 주세요");
		dataValidation.createPromptBox("Descrioption", "전송 방식을 선택");
		dataValidation.setErrorStyle(HSSFDataValidation.ErrorStyle.STOP);
		return dataValidation;
	}

	public void createExcelFile() {
		System.out.println(FilePath);
		try {
			FileOutputStream fileOut = new FileOutputStream(FilePath);
			this.workbook.write(fileOut);
			fileOut.close();
			this.workbook.close();
			System.out.println("===== 파일 생성 성공 ====");
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
