package com.kt.dataManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.http.protocol.RequestConnControl;
import org.json.simple.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.datastax.driver.core.Row;
import com.kt.commonUtils.Constants;
import com.kt.dataConverter.HtmlConverter;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataDao.SelectDataTo;

public class UITemplateController {

	public String getAttribute() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		String realPath = attr.getRequest().getRealPath("/");

//				.getContextPath();

//		contextPath = contextPath.getClass().getResource("/").getPath();

		System.out.println(realPath);

		return realPath;

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 20. 오후 1:55:40] desc : 아래 리턴 규격 JSON이 아닌 다른
	 *         형태로 전달 방법 고민
	 * @version :
	 * @param :
	 * @return : JSONObject
	 * @throws :
	 * @see :
	 * 
	 * @param venderName
	 * @param urlPath
	 * @return
	 */
	public JSONObject createWithTemplate(String vendorName, String urlPath, String specName) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject obj = new JSONObject();
		String templateName = null;
		String dn = vendorName;
		String sourcePath = this.extractURL(urlPath);

		List<Row> list = selectTo.selectTemplateForPath(sourcePath);

		for (Row row : list)
			templateName = row.getString("templatename");

		File sourceFilePath = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMPLATE
				+ File.separator + templateName);
		File targetFilePath = new File(
				Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMP + File.separator + dn);

		System.out.println((!sourceFilePath.exists()));

		if (!sourceFilePath.exists()) {
			obj.put("code", "400");
			return obj;

		} else if (targetFilePath.exists()) {

			obj.put("code", "409");
			return obj;

		} else {

			targetFilePath.mkdirs();
			this.copyTemplate(sourceFilePath, targetFilePath, specName);
			obj.put("code", "201");
			obj.put("specName", specName);
			obj.put("tempPath", Constants.EXTERNAL_FOLDER_URLPATH_TEMP + "/" + dn);
			return obj;
		}
	}

	public JSONObject createWithVendor(String newVendorName, String urlPath, String comUrl, String testUrl) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject obj = new JSONObject();
		String preVendorName = null;
		String preSpecName = null;
		String sourcePath = this.extractURL(urlPath);

		List<Row> vendorList = selectTo.selectVendorForPath(sourcePath);
		for (Row row : vendorList) {
			preVendorName = row.getString("vendorname");
			preSpecName = row.getString("specname");
		}

		InsertDataTo insertTo = new InsertDataTo();
		insertTo.insertSpecCopyData(preSpecName, newVendorName, comUrl, testUrl);

		File sourceFilePath = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_VENDORS
				+ File.separator + preVendorName);
		File targetFilePath = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMP
				+ File.separator + newVendorName);

		System.out.println((!sourceFilePath.exists()));

		if (!sourceFilePath.exists()) {
			obj.put("code", "400");
			return obj;

		} else if (targetFilePath.exists()) {

			obj.put("code", "409");
			return obj;

		} else {

			targetFilePath.mkdirs();
			this.copyVendor(sourceFilePath, targetFilePath);
			obj.put("code", "201");
			obj.put("tempPath", Constants.EXTERNAL_FOLDER_URLPATH_TEMP + "/" + newVendorName);
			return obj;
		}
	}

	public String extractURL(String urlPath) {

		String extractPath = null;

		try {

			URL aURL = new URL(urlPath);

			extractPath = aURL.getPath();

		} catch (MalformedURLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return extractPath;

	}

	public void copyTemplate(File sourceFile, File targetFile, String SpecName) {

		HtmlConverter htmlConverter = new HtmlConverter();

		File[] fileList = sourceFile.listFiles();

		for (File file : fileList) {

			File temp = new File(targetFile.getAbsolutePath() + File.separator + file.getName());

			if (file.isDirectory()) {

				temp.mkdirs();

				this.copyTemplate(file, temp, SpecName);

			} else {

				// 주석대상 1)
				FileInputStream fileIn = null;
				// InputStream fileIn = null;

				FileOutputStream fileOut = null;

				try {

					// DB처리 이후에 해당 리스트 이곳에 대입
					// 처리 후 주석대상 1), 2), 3) 번 주석처리 필요
					// fileIn = new ByteArrayInputStream(htmlConverter.removeItm(file, "UTF-8",
					// _selectorMap, _itmMap),"UTF-8");

					// 주석대상 2)
					fileIn = new FileInputStream(file);
					fileOut = new FileOutputStream(temp);

					byte[] b = new byte[4096];

					int cnt = 0;

					while ((cnt = fileIn.read(b)) != -1) {

						fileOut.write(b, 0, cnt);
					}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				} finally {

					try {

						// 주석대상 3)
						fileIn.close();
						fileOut.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						System.out.println(e.getMessage());
					}

				}
			}

		}
	}

	public void copyVendor(File sourceFile, File targetFile) {
		File[] fileList = sourceFile.listFiles();
		for (File file : fileList) {
			File temp = new File(targetFile.getAbsolutePath() + File.separator + file.getName());
			if (file.isDirectory()) {
				temp.mkdirs();
				this.copyVendor(file, temp);
			} else {
				FileInputStream fileIn = null;
				FileOutputStream fileOut = null;

				try {
					fileIn = new FileInputStream(file);
					fileOut = new FileOutputStream(temp);
					byte[] b = new byte[4096];
					int cnt = 0;
					while ((cnt = fileIn.read(b)) != -1) {
						fileOut.write(b, 0, cnt);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					try {
						fileIn.close();
						fileOut.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}

}
