"use strict";

var Utils =  new function(){
	
	/**
	 * isEmpty
	 * @param _str
	 * @return boolean
	 */
	this.isEmpty = function(_str){
		
		try{
			return !this.isNotEmpty(_str);
		} catch(excp) {
			log(ERROR,'message : '+excp.message);
			return true;
		} finally {
			//
		}
		
	};
	
	
	/**
	 * isNotEmpty
	 * @param _str
	 * @return boolean
	 */
	this.isNotEmpty = function(_str){
		if(typeof _str == "function" || typeof _str == "boolean" || typeof _str == "number"){
			return true;
		}else if(typeof _str == "object"){
			if(_str === null || _str === undefined || _str === "undefined" || typeof _str == "undefined"){
				return false;
			}else{
				return true;
			}
		}else{
			var obj = String(_str);
			if(jQuery.trim(obj).length <= 0 || obj === null || obj === undefined || obj === "undefined" || typeof obj == "undefined" || obj === "null"){
				return false;
			}else{
				return true;
			}
		}
		
		/*
		if(typeof _str == "object"){
			if(_str === null || _str === undefined || _str === "undefined" || typeof _str == "undefined"){
				return false;
			}else{
				return true;
			}
		}else if(typeof _str == "function"){
			return true;		
		}else{
			var obj = String(_str);
			if(obj === null || obj === undefined || obj === "undefined" || typeof obj == "undefined"){
				return false;
			}else{
				return true;
			}
		}
		*/	
	};
	
	
	/**
	 * isEmpty params
	 * @param _params
	 * @return boolean
	 */
	this.isEmptyParams = function(_params){
		
		try{
			
			return jQuery.isEmptyObject(_params);
			
		} catch(excp) {
			log(ERROR,'message : '+excp.message);
			return true;
		} finally {
			//
		}
		
	};
	
	this.log = function(log, msg, value) {
		if (printLog) {
			console.log('['+ log + '] '+ msg + ' : ', value); 
		}
		/*	if(jQuery.trim(value) != "" && jQuery.trim(value).length > 0){
				console.log('['+ log + '] '+ msg + ' : ', value); 
			} else {
				console.log('['+ log + '] '+ msg);
			}
		}*/
	};
};