package com.kt.dataManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.simple.JSONObject;

import com.kt.dataForms.BaseExcelForm;

public class CreateExcelForm {
	String realPath;
	String filePath;
	String fileName;
	HSSFWorkbook workbook;
	ArrayList<BaseExcelForm> svcList;

	public CreateExcelForm(ArrayList<BaseExcelForm> svcList, String realPath, String filePath, String fileName) {
		this.workbook = new HSSFWorkbook();
		this.svcList = svcList;
		this.realPath = realPath;
		this.filePath = filePath;
		this.fileName = fileName;
	}

	public JSONObject createSheet() {
		for (int arrIndex = 0; arrIndex < this.svcList.size(); arrIndex++) {
			HSSFSheet sheet = this.workbook.createSheet(this.svcList.get(arrIndex).getServiceName());
			sheet.protectSheet("password");
			sheet.setColumnWidth(0, 40 * 256);
			sheet.setColumnWidth(1, 40 * 256);
			sheet.setColumnWidth(2, 40 * 256);
			writeServiceDescForm(sheet, svcList.get(arrIndex));
			writeHeaderProtocolForm(sheet, svcList.get(arrIndex));
			writeReqProtocolForm(sheet, svcList.get(arrIndex));
			writeRespProtocolForm(sheet, svcList.get(arrIndex));
		}
		return createExcelFile();
	}

	public void writeServiceDescForm(HSSFSheet sheet, BaseExcelForm excelForm) {
		for (int svcDataRow = 0; svcDataRow < 6; svcDataRow++) {
			if (svcDataRow == 0) {
				Row row = sheet.createRow(svcDataRow);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceName");
				keyCell.setCellStyle(lockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceName());
				valueCell.setCellStyle(lockedValueCellStyle());
			} else if (svcDataRow == 1) {
				Row row = sheet.createRow(svcDataRow);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("invokeType");
				keyCell.setCellStyle(lockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getInvokeType());
				valueCell.setCellStyle(lockedValueCellStyle());
			} else if (svcDataRow == 2) {
				Row row = sheet.createRow(svcDataRow);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceType");
				keyCell.setCellStyle(lockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceType());
				valueCell.setCellStyle(lockedValueCellStyle());
			} else if (svcDataRow == 3) {
				Row row = sheet.createRow(svcDataRow);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceLink");
				keyCell.setCellStyle(lockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceLink());
				valueCell.setCellStyle(lockedValueCellStyle());
			} else if (svcDataRow == 4) {
				Row row = sheet.createRow(svcDataRow);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceDesc");
				keyCell.setCellStyle(lockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceDesc());
				valueCell.setCellStyle(lockedValueCellStyle());
			} else if (svcDataRow == 5) {
				Row row = sheet.createRow(svcDataRow);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("serviceCode");
				keyCell.setCellStyle(lockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getServiceCode());
				valueCell.setCellStyle(lockedValueCellStyle());
			}
			if (excelForm.getIntentInfo() != null) {
				Row row = sheet.createRow(6);
				Cell keyCell = row.createCell(0);
				keyCell.setCellValue("intentInfo");
				keyCell.setCellStyle(lockedKeyCellStyle());
				Cell valueCell = row.createCell(1);
				valueCell.setCellValue(excelForm.getId());
				valueCell.setCellStyle(lockedValueCellStyle());

				Row titleRow = sheet.createRow(8);
				Cell titleCell = titleRow.createCell(0);
				titleCell.setCellValue("dicList");
				titleCell.setCellStyle(lockedKeyCellStyle());

				Row dicListRow = sheet.createRow(9);
				Cell dicNameCell = dicListRow.createCell(0);
				dicNameCell.setCellValue("dicName");
				dicNameCell.setCellStyle(lockedKeyCellStyle());
				Cell dicWordListCell = dicListRow.createCell(1);
				dicWordListCell.setCellValue("wordList");
				dicWordListCell.setCellStyle(lockedKeyCellStyle());

				for (int dicArrRow = 10; dicArrRow < 15; dicArrRow++) {
					Row dicListInputRow = sheet.createRow(dicArrRow);
					Cell dicNameInputCell = dicListInputRow.createCell(0);
					dicNameInputCell.setCellValue("dicName");
					dicNameInputCell.setCellStyle(lockedValueCellStyle());
					Cell dicWordInputCell = dicListInputRow.createCell(1);
					dicWordInputCell.setCellValue("wordList");
					dicWordInputCell.setCellStyle(lockedValueCellStyle());
				}
				int dicArrRow = 10;
				for (int dicInfoRow = 0; dicInfoRow < excelForm.getDicList().size(); dicInfoRow++) {
					JSONObject dicObj = (JSONObject) excelForm.getDicList().get(dicInfoRow);
					Row dicListInputRow = sheet.createRow(dicArrRow);
					Cell dicNameInputCell = dicListInputRow.createCell(0);
					dicNameInputCell.setCellValue(dicObj.get("dicName").toString());
					dicNameInputCell.setCellStyle(lockedValueCellStyle());
					Cell dicWordInputCell = dicListInputRow.createCell(1);
					dicWordInputCell.setCellValue(dicObj.get("wordList").toString().trim());
					dicWordInputCell.setCellStyle(lockedValueCellStyle());
					dicArrRow++;
				}
			}
		}
	}

	public void writeHeaderProtocolForm(HSSFSheet sheet, BaseExcelForm excelForm) {
		Row methodRow = sheet.createRow(16);
		Cell methodCell = methodRow.createCell(0);
		methodCell.setCellValue("transMethod");
		methodCell.setCellStyle(lockedKeyCellStyle());
		Cell methodInputCell = methodRow.createCell(1);
		methodInputCell.setCellValue("전송방식 선택");
		methodInputCell.setCellStyle(unLockedCellStyle());
		sheet.addValidationData(methodDropDownList(methodRow.getRowNum(), methodInputCell.getColumnIndex(),
				methodRow.getRowNum(), methodInputCell.getColumnIndex()));

		for (int arrUrl = 18; arrUrl < 20; arrUrl++) {
			Row urlRow = sheet.createRow(arrUrl);
			Cell urlCell = urlRow.createCell(0);
			urlCell.setCellValue((arrUrl == 18) ? "comURL" : "testURL");
			urlCell.setCellStyle(lockedKeyCellStyle());
			Cell urlInputCell = urlRow.createCell(1);
			urlInputCell.setCellValue("http://");
			urlInputCell.setCellStyle(unLockedCellStyle());
		}

		Row titleRow = sheet.createRow(21);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Header");
		titleCell.setCellStyle(lockedKeyCellStyle());

		Row headerRow = sheet.createRow(22);
		Cell headerKey = headerRow.createCell(0);
		headerKey.setCellValue("Parameter");
		headerKey.setCellStyle(lockedKeyCellStyle());
		Cell headerValue = headerRow.createCell(1);
		headerValue.setCellValue("Value");
		headerValue.setCellStyle(lockedKeyCellStyle());
		Cell headerNote = headerRow.createCell(2);
		headerNote.setCellValue("Note");
		headerNote.setCellStyle(lockedKeyCellStyle());

		for (int headerDataRow = 23; headerDataRow < 28; headerDataRow++) {
			Row specInputRow = sheet.createRow(headerDataRow);
			Cell keyCell = specInputRow.createCell(0);
			keyCell.setCellValue("Parameter");
			keyCell.setCellStyle(unLockedCellStyle());
			Cell selectCell = specInputRow.createCell(1);
			selectCell.setCellValue("Value");
			selectCell.setCellStyle(unLockedCellStyle());
			Cell noteCell = specInputRow.createCell(2);
			noteCell.setCellValue("Note");
			noteCell.setCellStyle(unLockedCellStyle());
		}
	}

	public void writeReqProtocolForm(HSSFSheet sheet, BaseExcelForm excelForm) {
		Row titleRow = sheet.createRow(29);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Request Param");
		titleCell.setCellStyle(lockedKeyCellStyle());

		Row reqSpecRow = sheet.createRow(30);
		Cell specKey = reqSpecRow.createCell(0);
		specKey.setCellValue("Parameter");
		specKey.setCellStyle(lockedKeyCellStyle());
		Cell specValue = reqSpecRow.createCell(1);
		specValue.setCellValue("Value");
		specValue.setCellStyle(lockedKeyCellStyle());
		Cell specNote = reqSpecRow.createCell(2);
		specNote.setCellValue("Note");
		specNote.setCellStyle(lockedKeyCellStyle());

		for (int specDataRow = 31; specDataRow < 36; specDataRow++) {
			Row specInputRow = sheet.createRow(specDataRow);
			Cell keyCell = specInputRow.createCell(0);
			keyCell.setCellValue("Parameter");
			keyCell.setCellStyle(unLockedCellStyle());
			Cell selectCell = specInputRow.createCell(1);
			selectCell.setCellValue("Spec 선택");
			selectCell.setCellStyle(unLockedCellStyle());
			Cell noteCell = specInputRow.createCell(2);
			noteCell.setCellValue("Note");
			noteCell.setCellStyle(unLockedCellStyle());
		}
		sheet.addValidationData(specDropDownList(31, 1, 36, 1));

		Row exampleTitleRow = sheet.createRow(36);
		Cell exampleTitleCell = exampleTitleRow.createCell(0);
		exampleTitleCell.setCellValue("Request Example");
		exampleTitleCell.setCellStyle(lockedKeyCellStyle());

		Row exampleRow = sheet.createRow(37);
		exampleRow.setHeight((short) 2000);
		Cell reqExampleFirstCell = exampleRow.createCell(0);
		reqExampleFirstCell.setCellValue("Request Example Input");
		reqExampleFirstCell.setCellStyle(unLockedCellStyle());
		Cell reqExampleCell = exampleRow.createCell(1);
		reqExampleCell.setCellStyle(unLockedCellStyle());
		Cell reqExampleLastCell = exampleRow.createCell(2);
		reqExampleLastCell.setCellStyle(unLockedCellStyle());
		sheet.addMergedRegion(new CellRangeAddress(exampleRow.getRowNum(), exampleRow.getRowNum(),
				reqExampleFirstCell.getColumnIndex(), reqExampleLastCell.getColumnIndex()));
	}

	public void writeRespProtocolForm(HSSFSheet sheet, BaseExcelForm excelForm) {
		Row titleRow = sheet.createRow(39);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Response Param");
		titleCell.setCellStyle(lockedKeyCellStyle());

		Row respSpecRow = sheet.createRow(40);
		Cell specKey = respSpecRow.createCell(0);
		specKey.setCellValue("Parameter");
		specKey.setCellStyle(lockedKeyCellStyle());
		Cell specValue = respSpecRow.createCell(1);
		specValue.setCellValue("Value");
		specValue.setCellStyle(lockedKeyCellStyle());
		Cell specNote = respSpecRow.createCell(2);
		specNote.setCellValue("Note");
		specNote.setCellStyle(lockedKeyCellStyle());

		for (int specDataRow = 41; specDataRow < 46; specDataRow++) {
			Row specInputRow = sheet.createRow(specDataRow);
			Cell keyCell = specInputRow.createCell(0);
			keyCell.setCellValue("Parameter");
			keyCell.setCellStyle(unLockedCellStyle());
			Cell selectCell = specInputRow.createCell(1);
			selectCell.setCellValue("Spec 선택");
			selectCell.setCellStyle(unLockedCellStyle());
			Cell noteCell = specInputRow.createCell(2);
			noteCell.setCellValue("Note");
			noteCell.setCellStyle(unLockedCellStyle());
		}
		sheet.addValidationData(specDropDownList(41, 1, 46, 1));

		Row exampleTitleRow = sheet.createRow(46);
		Cell exampleTileCell = exampleTitleRow.createCell(0);
		exampleTileCell.setCellValue("Response Example");
		exampleTileCell.setCellStyle(lockedKeyCellStyle());

		Row exampleRow = sheet.createRow(47);
		exampleRow.setHeight((short) 2000);
		Cell respExampleFirstCell = exampleRow.createCell(0);
		respExampleFirstCell.setCellValue("Response Example Input");
		respExampleFirstCell.setCellStyle(unLockedCellStyle());
		Cell respExampleCell = exampleRow.createCell(1);
		respExampleCell.setCellStyle(unLockedCellStyle());
		Cell respExampleLastCell = exampleRow.createCell(2);
		respExampleLastCell.setCellStyle(unLockedCellStyle());
		sheet.addMergedRegion(new CellRangeAddress(exampleRow.getRowNum(), exampleRow.getRowNum(),
				respExampleFirstCell.getColumnIndex(), respExampleLastCell.getColumnIndex()));
	}

	public HSSFCellStyle lockedKeyCellStyle() {
		HSSFCellStyle lockedKeyCellStyle = workbook.createCellStyle();
		HSSFFont fontStyle = workbook.createFont();
		fontStyle.setColor(IndexedColors.GOLD.getIndex());
		fontStyle.setBold(true);
		lockedKeyCellStyle.setFont(fontStyle);
		lockedKeyCellStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
		lockedKeyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		lockedKeyCellStyle.setAlignment(HorizontalAlignment.LEFT);
		lockedKeyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		lockedKeyCellStyle.setBorderTop(BorderStyle.DASHED);
		lockedKeyCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		lockedKeyCellStyle.setBorderBottom(BorderStyle.DASHED);
		lockedKeyCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		lockedKeyCellStyle.setBorderLeft(BorderStyle.DASHED);
		lockedKeyCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		lockedKeyCellStyle.setBorderRight(BorderStyle.DASHED);
		lockedKeyCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		return lockedKeyCellStyle;
	}

	public HSSFCellStyle lockedValueCellStyle() {
		HSSFCellStyle lockedValueCellStyle = workbook.createCellStyle();
		HSSFFont fontStyle = workbook.createFont();
		fontStyle.setColor(IndexedColors.BLACK.getIndex());
		lockedValueCellStyle.setFont(fontStyle);
		lockedValueCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		lockedValueCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		lockedValueCellStyle.setAlignment(HorizontalAlignment.LEFT);
		lockedValueCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		lockedValueCellStyle.setBorderTop(BorderStyle.DASHED);
		lockedValueCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		lockedValueCellStyle.setBorderBottom(BorderStyle.DASHED);
		lockedValueCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		lockedValueCellStyle.setBorderLeft(BorderStyle.DASHED);
		lockedValueCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		lockedValueCellStyle.setBorderRight(BorderStyle.DASHED);
		lockedValueCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		return lockedValueCellStyle;
	}

	public HSSFCellStyle unLockedCellStyle() {
		HSSFCellStyle unLockedCellStyle = workbook.createCellStyle();
		unLockedCellStyle.setLocked(false);
		HSSFFont fontStyle = workbook.createFont();
		fontStyle.setColor(IndexedColors.BLACK.getIndex());
		fontStyle.setItalic(true);
		unLockedCellStyle.setFont(fontStyle);
		unLockedCellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
		unLockedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		unLockedCellStyle.setAlignment(HorizontalAlignment.LEFT);
		unLockedCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		unLockedCellStyle.setBorderTop(BorderStyle.DASHED);
		unLockedCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		unLockedCellStyle.setBorderBottom(BorderStyle.DASHED);
		unLockedCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		unLockedCellStyle.setBorderLeft(BorderStyle.DASHED);
		unLockedCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		unLockedCellStyle.setBorderRight(BorderStyle.DASHED);
		unLockedCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		return unLockedCellStyle;
	}

	public HSSFDataValidation specDropDownList(int firstRow, int firstCol, int lastRow, int lastCol) {
		// 드롭다운 박스 값 지정
		String[] strFormula = new String[] { "고정 값", "설정 값", "발화 어휘" };
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
		dataValidation.createPromptBox("Descrioption", "변수 타입을 선택");
		dataValidation.setErrorStyle(HSSFDataValidation.ErrorStyle.STOP);
		return dataValidation;
	}

	public HSSFDataValidation methodDropDownList(int firstRow, int firstCol, int lastRow, int lastCol) {
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

	public JSONObject createExcelFile() {
		JSONObject res = new JSONObject();
		HashMap<String, String> resData = new HashMap<String, String>();
		try {
			FileOutputStream fileOut = new FileOutputStream(this.realPath + this.fileName + ".xls");
			this.workbook.write(fileOut);
			fileOut.close();
			this.workbook.close();
			resData.put("urlPath", this.filePath + this.fileName + ".xls");
			res.put("resCode", "2001");
			res.put("resMsg", "성공");
			res.put("resData", resData);
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			res.put("resCode", "5000");
			res.put("resMsg", e.toString());
			res.put("resData", "");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			res.put("resCode", "5000");
			res.put("resMsg", e.toString());
			res.put("resData", "");
		}
		return res;
	}

}
