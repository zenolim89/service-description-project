package com.kt.tool.web.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Stack;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * zip 제어 유틸
 * 
 * @author nbware
 *
 */
@Component
public class CompressionUtil {
	
	/** The Constant log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Utils utils;
	
	public void unzip(File zippedFile) throws IOException {
		unzip(zippedFile, Charset.defaultCharset().name());
	}
	
	public void unzip(File zippedFile, String charsetName ) throws IOException {
		unzip(zippedFile, zippedFile.getParentFile(), charsetName);
	}
	public void unzip(File zippedFile, File destDir) throws IOException {
		unzip(new FileInputStream(zippedFile), destDir, Charset.defaultCharset().name());
	}
	
	public void unzip(File zippedFile, File destDir, String charsetName)
	throws IOException {
		unzip(new FileInputStream(zippedFile), destDir, charsetName);
	}
	public void unzip(InputStream is, File destDir) throws IOException{
		unzip(is, destDir, Charset.defaultCharset().name());
	}
	
	/**
	 * zip 압축해제
	 * 
	 * @param is
	 * @param destDir
	 * @param charsetName
	 * @throws IOException
	 */
	public void unzip( InputStream is, File destDir, String charsetName)
	throws IOException {
		ZipArchiveInputStream zis ;
		ZipArchiveEntry entry ;
		String name ;
		File target ;
		int nWritten = 0;
		BufferedOutputStream bos ;
		byte [] buf = new byte[1024 * 8];
		
		zis = new ZipArchiveInputStream(is, charsetName, false);
		while ( (entry = zis.getNextZipEntry()) != null ){
			name = entry.getName();
			target = new File (destDir, name);
			if ( entry.isDirectory() ){
				target.mkdirs();
				log.debug("dir : {}", name);
			} else {
				target.createNewFile();
				bos = new BufferedOutputStream(new FileOutputStream(target));
				while ((nWritten = zis.read(buf)) >= 0 ){
					bos.write(buf, 0, nWritten);
				}
				bos.close();
				log.debug("file : {}", name);
			}
		}
		zis.close();
	}
		
	public void zip(File src) throws IOException{
		zip(src, Charset.defaultCharset().name(), true);
	}
	
	public void zip(File src, boolean includeSrc) throws IOException{
		zip(src, Charset.defaultCharset().name(), includeSrc);
	}
	
	public void zip(File src, String charSetName, boolean includeSrc) throws IOException {
		zip( src, src.getParentFile(), charSetName, includeSrc);
	}
	
	public void zip(File src, OutputStream os) throws IOException {
		zip(src, os, Charset.defaultCharset().name(), true);
	}
	
	public void zip(File src, File destDir, String charSetName, boolean includeSrc) throws IOException {
		String fileName = src.getName();
		if ( !src.isDirectory() ){
			int pos = fileName.lastIndexOf(".");
			if ( pos >  0){
				fileName = fileName.substring(0, pos);
			}
		}
		fileName += ".zip";

		File zippedFile = new File ( destDir, fileName);
		if ( !zippedFile.exists() ) zippedFile.createNewFile();
		zip(src, new FileOutputStream(zippedFile), charSetName, includeSrc);
	}
	
	public void zip(File src, OutputStream os, String charsetName, boolean includeSrc)
	throws IOException {
		ZipArchiveOutputStream zos = new ZipArchiveOutputStream(os);
		zos.setEncoding(charsetName);
		FileInputStream fis ;
		
		int length ;
		ZipArchiveEntry ze ;
		byte [] buf = new byte[8 * 1024];
		String name ;
		
		Stack<File> stack = new Stack<File>();
		File root ;
		if ( src.isDirectory() ) {
			if( includeSrc ){
				stack.push(src);
				root = src.getParentFile();
			}
			else {
				File [] fs = src.listFiles();
				if (!utils.isEmpty(fs)) {
					for (int i = 0; i < fs.length; i++) {
						stack.push(fs[i]);
					}
				}
				root = src;
			}
		} else {
			stack.push(src);
			root = src.getParentFile();
		}
		
		while ( !stack.isEmpty() ){
			File f = stack.pop();
			name = toPath(root, f);
			if ( f.isDirectory()){
				log.debug("dir : {}", name);
				File [] fs = f.listFiles();
				
				if (!utils.isEmpty(fs)) {
					for (int i = 0; i < fs.length; i++) {
						if ( fs[i].isDirectory() ) stack.push(fs[i]);
						else stack.add(0, fs[i]);
					}
				}
			} else {
				log.debug("file : {}", name);
				ze = new ZipArchiveEntry(name);
				zos.putArchiveEntry(ze);
				fis = new FileInputStream(f);
				while ( (length = fis.read(buf, 0, buf.length)) >= 0 ){
					zos.write(buf, 0, length);
				}
				fis.close();
				zos.closeArchiveEntry();
			}
		}
		zos.close();
	}
	
	private String toPath(File root, File dir){
		String path = dir.getAbsolutePath();
		path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');
		if ( path.startsWith("/")) path = path.substring(1);
		if ( dir.isDirectory() && !path.endsWith("/")) path += "/" ;
		return path ;
	}

}
