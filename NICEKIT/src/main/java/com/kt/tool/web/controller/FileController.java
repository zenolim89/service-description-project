package com.kt.tool.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.tool.web.exception.UploadException;
import com.kt.tool.web.model.RequestFileUploadVo;
import com.kt.tool.web.model.ResponseVo;
import com.kt.tool.web.service.FileService;
import com.kt.tool.web.util.Utils;

/**
 * file controller
 * 
 * @author nbware
 *
 */
@Controller
@RestController
@RequestMapping(value = "/api")
public class FileController {
	
	/** print log **/
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	FileService fileService;
	
	@Autowired
	Utils utils;
	
	/**
	 * 파일 업로드
	 * 
	 * @param params
	 * @param bindingResult
	 * @param request
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/upload/file", method = { RequestMethod.POST }, headers = "Content-Type=multipart/form-data")
	public ResponseVo uploadFile(@Valid RequestFileUploadVo params, BindingResult bindingResult, HttpServletRequest request) {
		
		int rst = fileService.uploadFile(request, params.getDomain(), params.getWorkplace(), params.getPath(), params.getFile());
		if (rst == 1);
		else {
			throw new UploadException(HttpStatus.UNAUTHORIZED, "Upload Failed");
		}
		
		ResponseVo vo = new ResponseVo();
		int code = Integer.parseInt(null != HttpStatus.OK ? HttpStatus.OK.toString() : "200");
		vo.setCode(code);
		vo.setMessage(utils.getResultMessage(String.valueOf(code)));
		
		return vo;
	}
	
	/**
	 * 파일 내용 변경
	 * 
	 * @param params
	 * @param bindingResult
	 * @param request
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/update/file", method = { RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseVo updateFile(@Valid RequestFileUploadVo params, BindingResult bindingResult, HttpServletRequest request) {
		
		int rst = fileService.updateFile(request, params.getDomain(), params.getWorkplace(), params.getPath(), params.getFilename(), params.getContent());
		if (rst == 1);
		else {
			throw new UploadException(HttpStatus.UNAUTHORIZED, "File Update Failed");
		}
		
		ResponseVo vo = new ResponseVo();
		int code = Integer.parseInt(null != HttpStatus.OK ? HttpStatus.OK.toString() : "200");
		vo.setCode(code);
		vo.setMessage(utils.getResultMessage(String.valueOf(code)));
		
		return vo;
	}
	
}
