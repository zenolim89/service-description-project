package com.kt.dataConverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlConverter {

	/*
	 * 실행 메소드
	 */
	public String removeItm(File _file, String _charterSet, String _selector, String _selectorAttribute, ArrayList<String> _itmList) throws IOException {
		
		Document document = getDocument(_file, _charterSet);
		
		getRemoveItmDocument(document, _selector, _selectorAttribute, _itmList);
		
		return document.html();
	}
	
	/*
	 * 실행 메소드
	 */
	public String removeItm(File _file, String _charterSet, HashMap<String, String> _selectorMap,
			HashMap<String, Object> _itmMap) throws IOException {

		Document document = getDocument(_file, _charterSet);

		getRemoveItmDocument(document, _selectorMap, _itmMap);
		
		return document.html();
	}
	
	/*
	 * 실행 메소드
	 */
	public String replaceItm(File _file, String _charterSet, HashMap<String, String> _selectorMap,
			HashMap<String, Object> _itmMap) throws IOException {

		Document document = getDocument(_file, _charterSet);

		getReplaceDocument(document, _selectorMap, _itmMap);
		
		return document.html();
	}
	
	
	/*
	 * 실행 메소드
	 */
	public String removeItm(String _filePath, String _charterSet, String _selector, String _selectorAttribute, ArrayList<String> _itmList) throws IOException {
		
		Document document = getDocument(_filePath, _charterSet);
		
		getRemoveItmDocument(document, _selector, _selectorAttribute, _itmList);
		
		return document.html();
	}
	
	/*
	 * 실행 메소드
	 */
	public String removeItm(String _filePath, String _charterSet, HashMap<String, String> _selectorMap,
			HashMap<String, Object> _itmMap) throws IOException {

		Document document = getDocument(_filePath, _charterSet);

		getRemoveItmDocument(document, _selectorMap, _itmMap);
		
		return document.html();
	}
	
	/*
	 * 실행 메소드
	 */
	public String replaceItm(String _filePath, String _charterSet, HashMap<String, String> _selectorMap,
			HashMap<String, Object> _itmMap) throws IOException {

		Document document = getDocument(_filePath, _charterSet);

		getReplaceDocument(document, _selectorMap, _itmMap);
		
		return document.html();
	}
	
	/*
	 * File기반으로 Document 리턴하는 내부 메소드
	 */
	private Document getDocument(String _filePath, String _charterSet) throws IOException {
		
		File sourceFile = new File(_filePath);
		
		return Jsoup.parse(sourceFile, _charterSet);
	}
	
	/*
	 * File기반으로 Document 리턴하는 내부 메소드
	 */
	private Document getDocument(String _filePath) throws IOException {
		
		File sourceFile = new File(_filePath);
		
		return Jsoup.parse(sourceFile, "UTF-8");
	}
	
	/*
	 * File기반으로 Document 리턴하는 내부 메소드
	 */
	private Document getDocument(File _file, String _charterSet) throws IOException {
		
		return Jsoup.parse(_file, _charterSet);
	}
	
	/*
	 * File기반으로 Document 리턴하는 내부 메소드
	 */
	private Document getDocument(File _file) throws IOException {
		
		return Jsoup.parse(_file, "UTF-8");
	}
	
	/*
	 * 특정 Document에 대해 Element 제거
	 * selector와 대상의 attribute가 단일개일때 수행
	 */
	private Document getRemoveItmDocument(Document _document, String _selector, String _selectorAttribute, ArrayList<String> _itmList) {
		
		Elements elements = _document.select(_selector);
		
		for( Element element : elements) {
			
			int i = 0 ;
			
			for(; i< _itmList.size() ; i++ ) {
				
				if( element.attr(_selectorAttribute).equals(_itmList.get(i))){
					break;
				}
				
			}
			
			if( i==_itmList.size()) {
				element.remove();
			}
			
		}
		
		return _document;
	}
	
	/*
	 * 특정 Document에 대해 Element 제거
	 * selector와 대상의 attribute가 다수일때 수행
	 */
	private Document getRemoveItmDocument (Document _document, HashMap<String, String> _selectorMap,
			HashMap<String, Object> _itmMap) throws IOException{
		
		for (String selector : _selectorMap.keySet()) {

			String attribute = _selectorMap.get(selector);
			Elements elements = _document.select(selector);
			Object itmList = _itmMap.get(selector);

			if( itmList instanceof ArrayList) {
				
				removeElements(attribute, elements, (ArrayList)itmList);
			}
			else if( itmList instanceof HashMap<?, ?> ) {
				removeElements(attribute, elements, (HashMap<String, Object>)itmList);
			}

		}
		
		return _document;
	}
	
	/*
	 * 특정 Document에 대해 대해 Element 속성 변경
	 * selector와 대상의 attribute가 다수일때 수행
	 */
	private Document getReplaceDocument (Document _document, HashMap<String, String> _selectorMap,
			HashMap<String, Object> _itmMap) throws IOException{
		
		for (String selector : _selectorMap.keySet()) {

			String attribute = _selectorMap.get(selector);
			Elements elements = _document.select(selector);
			Object itmList = _itmMap.get(selector);

			replaeElement(attribute, elements, (HashMap<String, Object>)itmList);

		}
		
		return _document;
	}

	/*
	 * Elements제거 상세 타입이 HashMap일때
	 */
	private void removeElements(String _atrribute, Elements _elements, HashMap<String, Object> _itmDetailMap) {
		
		for (Element element : _elements) {

			if( _itmDetailMap.get(element.attr(_atrribute)) == null){
				element.remove();
			}
		}
		
	}
	
	/*
	 * Elements제거 상세 타입이 ArrayList일때
	 */
	private void removeElements(String _atrribute, Elements _elements, ArrayList<String> _itmList) {
		
		for (Element element : _elements) {

			int i = 0;

			for (; i < _itmList.size(); i++) {
				if (element.attr(_atrribute).equals(_itmList.get(i))) {
					break;
				}
			}
			if (i == _itmList.size()) {
				element.remove();
			}
		}
	}
	
	/*
	 * Elements변경 상세 타입이 HashMap일때
	 */
	private void replaeElement(String _atrribute, Elements _elements, HashMap<String, Object> _itmDetailMap) {
		
		for (Element element : _elements) {

			if( _itmDetailMap.get(element.attr(_atrribute)) != null){
				element.attr(_atrribute, (String) _itmDetailMap.get(element.attr(_atrribute)));
			}
		}
	}
}
