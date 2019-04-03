//var server = "http://222.107.124.9:8080/NICEKIT/";
var server = "/NICEKIT/";

/**
 * 도메인 목록 조회
 * @param cb
 */
function getDomainList(cb) {
    $.get(server + "getDomain", function(data){
        console.log(data);
        if(data.resCode == "200") {
            var domains = data.resData.domainList;
            cb(domains);
        } else {
            alert(data.resMsg);
        }
    });
}

/**
 * 사업장 목록 조회
 * @param domain
 * @param cb
 */
function getVendorList(domain, cb) {
    $.get(server + "getVendor", {domainName: domain}, function (data) {
        console.log(data);
        if(data.resCode == "200") {
            cb(data.resData.vendorList);
        } else {
            alert(data.resMsg);
        }
    });
}

/**
 * 사업장 템플릿 경로 조회
 * @param vendor
 * @param cb
 */
function getVendorDetail(domain, vendor, cb) {
    $.get(server + "getVendorPage", {domainName: domain, vendorName: vendor}, function(data) {
        console.log(data);
        if(data.resCode == "200") {
            cb(data.resData);
        } else {
            alert(data.regMsg);
        }
    });
}

/**
 * 사업장 생성
 * @param domain 도메인
 * @param newVendorName 사업장명
 * @param fromTemplatePath 복사할 기존 템플릿 경로
 * @param cb
 */
function createVendor(domain, newVendorName, fromTemplatePath, cb) {
    var reqParam = {domainName:domain, vendorName:newVendorName, urlPath:fromTemplatePath};
	$.post(server + "VendorGen", JSON.stringify(reqParam), function(data) {
        console.log(JSON.stringify(data));
        cb(data);
    });
}

/**
 * 템플릿 목록 조회
 * @param domain
 * @param cb
 */
function getTemplateList(domain, cb) {
    $.get(server + "getTemplate", {domainName: domain}, function(data) {
        console.log(JSON.stringify(data));
        if(data.resCode == "200") {
            cb(data.resData.templateList);
        } else {
            alert(data.regMsg);
        }
    });
}

/**
 * 템플릿 경로 조회
 * @param domain
 * @param template
 * @param cb
 */
function getTemplateDetail(domain, template, cb) {
    $.get(server + "getTemplatePage", {domainName: domain, templateName: template}, function(data) {
        console.log(data);
        if(data.resCode == "200") {
            cb(data.resData);
        } else {
            alert(data.regMsg);
        }
    });
}

/**
 * 파일 컨텐츠 업데이트
 * @param domain
 * @param vendor
 * @param filename
 * @param content
 */
function updatePage(domain, vendor, filename, content) {
    domain = "template";
    // domain = "resources/template/";
    // vendor = vendor + "/";
    console.log(vendor);
    $.post(server + "api/update/file", {domain:domain, workplace:vendor, filename:filename, content:content}, function (data) {
    // $.post(server + "api/update/file", {workplace:vendor, filename:filename, content:content}, function (data) {
        console.log(data);
    });
}