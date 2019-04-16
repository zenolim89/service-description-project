package com.kt.dataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import com.kt.dataForms.ExcelUploadForm;
import com.kt.dataForms.HttpParam;
import com.kt.dataForms.DicParam;

public class ExcelRead {

	public static List<ExcelUploadForm> read(ExcelReadOption excelReadOption, String domainName, String domainId,
			String specName) {

		/* 엑셀 파일 읽기 */
		Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());

		/* 유효한 시트 수 */
		int sheetCnt = wb.getNumberOfSheets();
		System.out.println("sheetCnt: " + sheetCnt);

		/* 리턴 값 */
		List<ExcelUploadForm> data = new ArrayList<ExcelUploadForm>();

		/* 객체 바인딩 */
		for (int i = 0; i < sheetCnt; i++) {

			Sheet sheet = wb.getSheetAt(i);
			System.out.println("Sheet 이름: " + wb.getSheetName(i));

			// System.out.println(String.format("LastRowNum: %d, PhysicalNumberOfRow: %d",
			// sheet.getLastRowNum(), sheet.getPhysicalNumberOfRows()));

			int endRows = sheet.getLastRowNum();
			System.out.println("END ROWS: " + endRows);

			Map<String, CellReference> format = new HashMap<String, CellReference>();

			Row row = null;
			Cell cell = null;

			for (int j = 0; j < endRows + 5; j++) {

				row = sheet.getRow(j);

				if (row == null)
					continue;

				cell = row.getCell(0);
				String key = ExcelCellRef.getValue(cell);

				switch (key) {
				case "serviceName":
				case "invokeType":
				case "serviceType":
				case "serviceLink":
				case "serviceDesc":
				case "serviceCode":
				case "intentInfo":
				case "transMethod":
				case "comURL":
				case "testURL":
					format.put(key, new CellReference(j, 1));
					break;
				case "Request Example":
				case "Response Example":
					format.put(key, new CellReference(j + 1, 0));
					break;
				case "dicList":
				case "Header":
				case "Request Param":
				case "Response Param":
					format.put(key, new CellReference(j + 2, 0));
					break;
				default:
					break;
				}
			}

//			 for(String key : format.keySet()) { 
//				 System.out.println(String.format("키 : %s, 값 : %s", key, format.get(key)) );
//				 }

			ExcelUploadForm form = new ExcelUploadForm();

			// 기본 정보
			form.setServiceName(cellReader(sheet, format.get("serviceName")));
			form.setInvokeType(cellReader(sheet, format.get("invokeType")));
			form.setServiceType(cellReader(sheet, format.get("serviceType")));
			form.setServiceLink(cellReader(sheet, format.get("serviceLink")));
			form.setServiceDesc(cellReader(sheet, format.get("serviceDesc")));
			form.setServiceCode(cellReader(sheet, format.get("serviceCode")));
			form.setTransMethod(cellReader(sheet, format.get("transMethod")));
			form.setCommonURL(cellReader(sheet, format.get("comURL")));
			form.setTestURL(cellReader(sheet, format.get("testURL")));

			// Intent 정보
			if (format.get("intentInfo") != null)
				form.setIntentInfo(cellReader(sheet, format.get("intentInfo")));
			if (format.get("dicList") != null)
				form.setDicList(bindDicParam(sheet, format.get("dicList")));

			// 도메인 및 스펙 네임
			form.setDomainId(domainId);
			form.setDomainName(domainName);
			form.setSpecName(specName);

			// TODO SERA 임시 고정값
			form.setDataType("JSON");

			// Header 파라메터 파싱
			form.setHeader(bindHttpParam(sheet, format.get("Header"), format.get("Request Param")));

			// REQ 파라메터 파싱
			form.setReqParam(bindHttpParam(sheet, format.get("Request Param"), format.get("Request Example")));
			form.setReqEx(cellReader(sheet, format.get("Request Example")));

			// RES 파라메터 파싱
			form.setResParam(bindHttpParam(sheet, format.get("Response Param"), format.get("Response Example")));
			form.setResEx(cellReader(sheet, format.get("Response Example")));

			data.add(form);

		}

		return data;
	}

	public static String cellReader(Sheet sheet, CellReference ref) {

		if (ref == null)
			return "";

		Row row = sheet.getRow(ref.getRow());

		if (row == null)
			return "";

		Cell cell = row.getCell(ref.getCol());

		return ExcelCellRef.getValue(cell);
	}

	public static List<HttpParam> bindHttpParam(Sheet sheet, CellReference start, CellReference end) {

		List<HttpParam> reqParams = new ArrayList<HttpParam>();

		for (int j = 0; j < 5; j++) {

			Row row = sheet.getRow(start.getRow() + j);
			if (row == null)
				continue;

			if (!ExcelCellRef.getValue(row.getCell(0)).equals("Parameter")) {
				HttpParam param = new HttpParam();
				param.setParameter(ExcelCellRef.getValue(row.getCell(0)));
				param.setValue(ExcelCellRef.getValue(row.getCell(1)));
				param.setNote(ExcelCellRef.getValue(row.getCell(2)));
				reqParams.add(param);
			}
		}
		return reqParams;
	}

	public static List<DicParam> bindDicParam(Sheet sheet, CellReference start) {

		List<DicParam> reqParams = new ArrayList<DicParam>();

		for (int j = 0; j < 5; j++) {

			Row row = sheet.getRow(start.getRow() + j);
			if (row == null)
				continue;

			if (!ExcelCellRef.getValue(row.getCell(0)).equals("dicName")) {
				DicParam param = new DicParam();
				param.setDicName(ExcelCellRef.getValue(row.getCell(0)));
				param.setWordList(ExcelCellRef.getValue(row.getCell(1)));
				reqParams.add(param);
			}
		}
		return reqParams;
	}
}
