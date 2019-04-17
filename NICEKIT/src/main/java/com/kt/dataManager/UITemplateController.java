package com.kt.dataManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	public JSONObject createWithTemplate(String domainName, String vendorName, String urlPath, String specName) {
		SelectDataTo selectTo = new SelectDataTo();
		ArrayList<String> wordList = new ArrayList<String>();
		JSONObject resObj = new JSONObject();
		String templateName = null;
		String dn = vendorName;
		String sourcePath = this.extractURL(urlPath);
		String serviceName = "객실용품";

		List<Row> list = selectTo.selectTemplateForPath(sourcePath);
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		wordList = serializerTo.resWordInfo(domainName, specName, serviceName);

		for (Row row : list)
			templateName = row.getString("templatename");

		File sourceFilePath = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMPLATE
				+ File.separator + templateName);
		File targetFilePath = new File(
				Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMP + File.separator + dn);

		System.out.println((!sourceFilePath.exists()));

		if (!sourceFilePath.exists()) {
			resObj.put("code", "400");
			return resObj;

		} else if (targetFilePath.exists()) {

			resObj.put("code", "409");
			return resObj;

		} else {

			targetFilePath.mkdirs();
			this.copyTemplate(sourceFilePath, targetFilePath, wordList);
			resObj.put("code", "201");
			resObj.put("specName", specName);
			resObj.put("tempPath", Constants.EXTERNAL_FOLDER_URLPATH_TEMP + "/" + dn);
			return resObj;
		}
	}

	public JSONObject moveToVendor(String vendorName, String urlPath) {
		JSONObject obj = new JSONObject();

		File sourceFilePath = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMP
				+ File.separator + vendorName);
		File targetFilePath = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_VENDORS
				+ File.separator + vendorName);

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
			this.deleteTemp(sourceFilePath);

			obj.put("code", "201");
			obj.put("vendorPath", Constants.EXTERNAL_FOLDER_URLPATH_VENDORS + "/" + vendorName);
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

	public void copyTemplate(File sourceFile, File targetFile, ArrayList<String> wordList) {

		HtmlConverter htmlConverter = new HtmlConverter();

		File[] fileList = sourceFile.listFiles();

		for (File file : fileList) {

			File temp = new File(targetFile.getAbsolutePath() + File.separator + file.getName());

			if (file.isDirectory()) {

				temp.mkdirs();

				this.copyTemplate(file, temp, wordList);

			} else {
				InputStream fileIn = null;
				FileOutputStream fileOut = null;
				try {
					if (file.getName().equals("ame_detail.html")) {
						fileIn = new ByteArrayInputStream(
								(htmlConverter.removeItm(file, "UTF-8", "[name=amenity]", "target-name", wordList))
										.getBytes());
					} else {
						fileIn = new FileInputStream(file);
					}
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

	public void deleteTemp(File tempFolder) {
		try {
			if (tempFolder.exists()) {
				File[] fileList = tempFolder.listFiles(); // 파일리스트 얻어오기
				for (File file : fileList) {
					if (file.isFile())
						file.delete(); // 파일 삭제
					else
						this.deleteTemp(file);
					file.delete();
				}
				tempFolder.delete();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

	}
}
