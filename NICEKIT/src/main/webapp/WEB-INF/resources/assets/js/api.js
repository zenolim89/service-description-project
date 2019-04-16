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
function getVendorList(domain, temp, cb) {
    if(temp) {
        $.get(server + "getTemp", {domainName: domain}, function (data) {
            console.log(data);
            if(data.resCode == "200") {
                cb(data.resData.tempList);
            } else {
                alert(data.resMsg);
            }
        });
    } else {
        $.get(server + "getVendor", {domainName: domain}, function (data) {
            console.log(data);
            if(data.resCode == "200") {
                cb(data.resData.vendorList);
            } else {
                alert(data.resMsg);
            }
        });
    }

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
 * 기존사업장으로 사업장 생성
 * @param domain
 * @param newVendorName
 * @param urlPath
 * @param cb
 */
function createVendorFromVendor(domain, newVendorName, comUrl, testUrl, urlPath, cb) {
    var param = {domainName:domain, vendorName:newVendorName, comUrl:comUrl, testUrl:testUrl, urlPath:urlPath};

    $.ajax({
        type: 'POST',
        url: server + "CreateWithVendor",
        data: JSON.stringify(param), // or JSON.stringify ({name: 'jonas'}),
        success: function(data) {
            console.log(data);
            cb(data);
        },
        contentType: "application/json",
        dataType: 'json'
    });

}


/**
 * 템플릿으로 사업장 생성
 * @param domain
 * @param newVendorName
 * @param urlPath
 * @param cb
 */
function createVendorFromTemplate(domain, newVendorName, specName, urlPath, cb) {
    var param = {domainName:domain, vendorName:newVendorName, specName: specName, urlPath:urlPath};
    $.ajax({
        type: 'POST',
        url: server + "CreateWithTemplate",
        data: JSON.stringify(param), // or JSON.stringify ({name: 'jonas'}),
        success: function(data) {
            console.log(data);
            cb(data);
        },
        contentType: "application/json",
        dataType: 'json'
    });
}

/**
 * 템플릿 목록 조회
 * @param domain
 * @param cb
 */
function getTemplateList(domain, cb) {
    $.get(server + "getTemplate", {domainName: domain}, function(data) {
        console.log(data);
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
 * 규격목록 조회
 * @param domain
 */
function getSpecList(domain, cb) {
    $.get(server + "getSpecList", {domainName: domain}, function (data) {
        console.log(data);
        if(data.resCode == "200") {
            cb(data.resData.specList);
        } else {
            alert(data.resMsg);
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
function updatePage(domain, vendor, filename, content, cb) {
    domain = "temp";
    // domain = "resources/template/";
    // vendor = vendor + "/";
    console.log(vendor);
    $.post(server + "api/update/file", {domain:domain, workplace:vendor, filename:filename, content:content}, cb);
}


/**
 * 규격정보조회
 * @param domain
 */
function getSpecInfo(domain, spec,cb) {
    $.get(server + "getSpecInfo", {domainName: domain,specName:spec}, function (data) {
        console.log(data);
        if(data.resCode == "200") {
        	 cb(data.resData.specDesc);
        } else {
        	alert(data.resMsg);
        }
    });
}
