package com.kt.dataManager;

import java.util.List;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataForms.ExcelUploadForm;

public class ExcelService {

	public List<ExcelUploadForm> excelUpload(String filePath, String domainName, String domainId, String specName) {

		ExcelReadOption option = new ExcelReadOption();

		/* 파일경로 추가 */
		option.setFilePath(filePath);

		/* 엑셀 데이터 파싱 */
		List<ExcelUploadForm> list = ExcelRead.read(option, domainName, domainId, specName);

		InsertDataTo insertTo = new InsertDataTo();
		insertTo.insertExcelData(list, specName);

//		JSONArray jsonList = new JSONArray();
//		jsonList.add(list);
//		System.out.println(jsonList.toString());

		return list;

	}

}
