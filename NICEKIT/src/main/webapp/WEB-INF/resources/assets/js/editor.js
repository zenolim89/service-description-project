var tmp;
var domain;
var vendor;
var curPageInfo;
var curPageHtml;
var curPageObj;

// 페이지 편집됨 상태를 저장
var pageEdited = false;

$(document).ready(function () {

});

/**
 * 초기화
 */
function init(_domain, _vendor, path) {
    domain = _domain;
    vendor = _vendor;
    tmp = new template(path, _init);
}
function _init() {
    // 페이지명
    $("#vendor_name").text(tmp.getTemplateName());

    // 프리뷰 로드 이벤트
    $("#preview").load(function() {
        var as = getElementInsidePreview("a");
        as.click(function() {
            var link = $(this).attr("href");
            if(link.substr(0,1) == "#") {
                return true;
            }
            var linkPage = tmp.getPageByPath(link);
            if(linkPage != null) {
                link = linkPage.name;
            }
            alert("'" + link + "' 페이지로 이동됩니다.");
            return false;
        });
    });

    // 페이지 목록 조기화
    var pages = tmp.getPages();
    for(var i = 0; i < pages.length; i++) {
        addPage(pages[i]);
    }

    // 탭 크기 적용
    var num = $(".tab_menu li").length;
    var wli = 100 / num +"%"
    $(".tab_menu li").css("width", wli);

    // $(".cbtns .btn_used").on("click", function(e){
    //     e.preventDefault();
    //     if($(this).hasClass("active")){
    //         $(this).removeClass("active")
    //     }
    //     else
    //     {
    //         $(this).addClass("active")
    //     }
    // });

    // 페이지 클릭 이벤트

    // 첫펀째 페이지 열기
    openPageIndex(0);
}

/**
 * 페이지 목록에 페이지 추가
 * @param page
 */
function addPage(page) {
    if(page.editable === false) { // 편집 불가능한 페이지는 건너띔
        return;
    }

    var item = $("<li><button type=\"menu\"><em></em></button></li>");
    var a = item.find("button");
    a.find("em").text(page.name);
    a.attr("data-name", page.name);
    a.click(function() {
        var pageName = $(this).attr("data-name");
        openPage(pageName);
    });
    $("#pages").append(item);
}

/**
 * 템플릿 페이지 열기
 * @param pageName
 */
function openPage(pageName) {
    if(pageEdited) { // 저장 확인
        if(! confirm("편집중인 내용이 있습니다.\n저장하지않고 넘어 가시겠습니까?")) {
            return;
        }
    }

    // 컴포넌트 섹션들 숨기기
    $(".w_menus, .w_menus2, .s_menus, .s2_menus, .service_area").css("display", "none");

    // 하단 서비스 설정 항목 내용 삭제
    // $(".w_menus2").html("");

    // 기존 컴포넌트들 삭제
    $(".editor").html("");

    // 페이지 정보 조회
    var pageInfo = tmp.getPage(pageName);
    curPageInfo = pageInfo;
    var templatePath = tmp.templatePath;

    // 프리뷰 불러오기
    var pagePath = templatePath + pageInfo.path;
    // preview 에서 a 태그 클릭 이벤트 발생시 어디로 가는지 알려주고 이동은 시키지 않음
    $("#preview").attr("src", pagePath);
    console.log("preview : " + pagePath);

    // 페이지 html 조회
    $.get(pagePath, function(pageHtml) {
        curPageHtml = pageHtml;
        var pageObj = $(curPageHtml);
        curPageObj = $("<div/>").append(pageObj).find("#" + curPageInfo.root);
        // pageObj = pageObj.find("#" + pageInfo.main_element);
        // curPageObj = pageObj;
        // var mainElementHtml = pageObj.find("#" + pageInfo.main_element)[0].outerHTML;
        // console.log(mainElementHtml);

        // var body = curPageHtml.substr(curPageHtml.indexOf("<body"));
        // var body = body.substr(0, body.indexOf("</body>") + "</body>".length);
        // console.log(body);
        // curPageObj = $(body);

        // 편집용 컴포넌트 추가
        var components = pageInfo.components;
        for(var i = 0; i < components.length; i++) {
            addComponents(components[i]);
        }
    });

    $("#pages button").removeClass("active");
    $("#pages button[data-name='" + pageName + "']").addClass("active");

    // 사용안함 버튼 체크 처리
    var pageName = curPageInfo.name;
    var btn = $("#pages button[data-name='" + pageName + "']");
    if(btn.hasClass("disabled")) {
        $(".cbtns .btn_ten").addClass("active");
    } else {
        $(".cbtns .btn_ten").removeClass("active");
    }

    // 페이지 편집 안된 상태로 적용
    pageEdited = false;
}

/**
 * 템플릿 페이지 열기
 * @param pageIndex
 */
function openPageIndex(pageIndex) {
    var pageName = $("#pages li:nth-child(" + (Number(pageIndex)+1) + ") button").attr("data-name");
    openPage(pageName);
}

/**
 * 편집용 템플릿 컴포넌트 생성
 * @param info
 * @returns {MediaStream | Response | MediaStreamTrack | Request | *}
 * @private
 */
function _getComponentTemplate(info) {
    var com = $("#components .comp." + info.type);
    com = $(com.html());
    return com;
}

/**
 * 컴포넌트 하나 추가
 * @param info
 */
function addComponents(info) {
    switch (info.type) {
        case "textarea":
            // 편집 컴포넌트
            var com = _getComponentTemplate(info);
            // 이름
            com.find(".name").text(info.name);
            // 기존 텍스트
            var oldText = curPageObj.find("#" + info.id).html();
            com.find(".textarea").text(br2nl(oldText));

            // 텍스트 편집 실시간 반영
            com.find(".textarea").keyup(function() {
                var text = com.find(".textarea").val();
                text = nl2br(text);
                applyTextToPreview(info.id, null, text);

                // 편집됨
                pageEdited = true;
            });

            // 편집 컴포넌트 추가
            _addComponent(info.section, com);
            break;
        case "image":
            // 편집 컴포넌트
            var com = _getComponentTemplate(info);
            // 이름
            com.find(".name").text(info.name);

            // 파일 연결
            _connectInputFile(info.id, com.find(".file"), com.find("img"));

            // 편집 컴포넌트 추가
            _addComponent(info.section, com);
            break;
        case "background":
            // 편집 컴포넌트
            var com = _getComponentTemplate(info);
            // 이름
            com.find(".name").text(info.name);

            // 파일 연결
            _connectInputFileBackground(info.id, com.find(".file"), com.find(".img"));

            // 편집 컴포넌트 추가
            _addComponent(info.section, com);
            break;
        case "link_text":
            // 편집 컴포넌트
            var com = _getComponentTemplate(info);
            // 이름
            com.find(".name").text(info.name);

            // 기존 텍스트
            _connectInputText(info.id, com.find(".text"));

            // 링크 연결
            _connectInputLink(info.id, com.find(".link"));

            // 편집 컴포넌트 추가
            _addComponent(info.section, com);
            break;
        case "link_text2":
            // 편집 컴포넌트
            var com = _getComponentTemplate(info);
            // 이름
            com.find(".name").text(info.name);

            // 링크 연결
            _connectInputLink(info.id, com.find(".link"));

            // 서비스명
            // com.find(".text").val(info.service_name);

            // com.find(".link").val(info.service_name);

            // 기존 텍스트
            // _connectInputText(info.id, com.find(".text"));

            // 링크 연결
            // _connectInputLink(info.id, com.find(".link"));

            // 편집 컴포넌트 추가
            _addComponent(info.section, com);
            break;
        case "hover_image_text":
            // 편집 컴포넌트
            var com = _getComponentTemplate(info);
            // 이름
            com.find(".name").text(info.name);

            // 파일 연결
            _connectInputFile(info.id_normal, com.find(".file_normal"), com.find(".img_normal"));
            _connectInputFile(info.id_hover, com.find(".file_hover"), com.find(".img_hover"));
            // 텍스트 인풋 연결
            _connectInputText(info.id_text, com.find(".text"));

            // 기능변경 클릭
            ////////////////////////////////////////////////
            // com.find(".btn_plus.btn_spec").click(function() {
            //     $(".w_menus2 .editor").html("");
            //     var name = com.find(".text").val();
            //     addComponents({"name": info.name,
            //         "type": "link_text2",
            //         "service_name": name,
            //         "section": "w_menus2",
            //         "id": "nolink"});
            //     return false;
            // });
            ////////////////////////////////////////////////

            // 하위 편집 컴포넌트 추가
            addComponents({"name": info.name,
                "type": "link_text2",
                "service_name": com.find(".text").val(),
                "section": "w_menus2",
                "id": "nolink"});


            // 편집 컴포넌트 추가
            _addComponent(info.section, com);

            // TODO 기능변경 버튼 추가, 하나만 추가되어야 해서, 없으면 추가하고, 있으면 추가 안하게

            break;
        case "image_text_link":
        case "image_text_popup":
            // 편집 컴포넌트
            var com = _getComponentTemplate(info);
            // 이름들
            com.find(".title_text").text(info.title_text);
            com.find(".title_link").text(info.title_link);
            com.find(".title_image").text(info.title_image);

            // 기존 텍스트
            _connectInputText(info.id_text, com.find(".text"));

            // 링크 연결
            _connectInputLink(info.id_link, com.find(".link"));

            // 파일 연결
            _connectInputFile(info.id_img, com.find(".file"), null);

            // 편집 컴포넌트 추가
            _addComponent(info.section, com);
            break;
    }
}

/**
 * input 파일 연결 작업
 * @param page
 * @param id
 * @param compElem
 * @private
 */
function _connectInputFile(id, compElem, imgElem) {
    // 변경 대상이 되는 원본 파일명 기록
    var image = curPageObj.find("#" + id);
    var oldSrc = image.attr("src");
    compElem.attr("data-orig-filename", oldSrc);
    if(imgElem != null) {
        imgElem.attr("src", tmp.templatePath + oldSrc);
    }

    // 이미지 선택시 preview 에 바로 반영
    compElem.change(function() {
        applyImageToPreview(id, this, imgElem);
        // 편집됨
        pageEdited = true;
    });
}

function _connectInputFileBackground(id, compElem, imgElem) {
    // 변경 대상이 되는 원본 파일명 기록
    var image = curPageObj.find("#" + id);
    if(image.length <= 0 && curPageObj.attr("id") == id) {
        image = curPageObj;
    }
    var oldSrc = image.css("background-image");
    compElem.attr("data-orig-filename", oldSrc);
    if(oldSrc.indexOf("url(\"data:") == 0) {
        imgElem.css("background-image", oldSrc);
    } else {
        var bgPath = oldSrc.substr(0, "url(\"".length) + tmp.templatePath + oldSrc.substr("url(\"".length);
        imgElem.css("background-image", bgPath);
    }

    // 이미지 선택시 preview 에 바로 반영
    compElem.change(function() {
        applyBackgroundImageToPreview(id, this, imgElem);
        // 편집됨
        pageEdited = true;
    });
}

/**
 * input 태그 연결 작업
 * @param page
 * @param id
 * @param compElem
 * @private
 */
function _connectInputText(id, compElem) {
    // 기존 텍스트
    var oldText = curPageObj.find("#" + id).text();
    compElem.val(oldText);

    // 텍스트 편집 실시간 반영
    compElem.keyup(function() {
        var text = compElem.val();
        applyTextToPreview(id, text);
        // 편집됨
        pageEdited = true;
    });
}

/**
 * 링크 연결
 * @param id
 * @param compElem
 * @private
 */
function _connectInputLink(id, compElem) {
    if(compElem.prop("tagName") == "SELECT") {
        compElem.html("");
        // 페이지 항목 추가
        var pages = tmp.getPages();
        for(var i = 0; i < pages.length; i++) {
            var el = $("<option></option>");
            el.text(pages[i].name);
            el.attr("value", pages[i].path);
            compElem.append(el);
        }

        compElem.change(function() {
            var text = compElem.val();
            applyAttributeToPreview(id, "href", text);
            // 편집됨
            pageEdited = true;
        });
    } else {
        // 텍스트 편집 실시간 반영
        compElem.keyup(function() {
            var text = compElem.val();
            applyAttributeToPreview(id, "href", text);
            // 편집됨
            pageEdited = true;
        });
    }

    // 기존 텍스트
    var oldLink = curPageObj.find("#" + id).attr("href");
    compElem.val(oldLink);
}

/**
 * 생성된 컴포넌트 추가
 * @param com
 * @private
 */
function _addComponent(section, com) {
    $("." + section).css("display", "block");
    $("." + section + " .editor").append(com);
}

/**
 * attribute 에 값 반영
 * @param id
 * @param attrName
 * @param value
 */
function applyAttributeToPreview(id, attrName, value) {
    // var item = getElementInsidePreview("#" + id);
    var item = getElementInsidePreview("*[id='" + id + "']");
    item.attr(attrName, value);

    // 페이지 obj 에 반영
    // item = curPageObj.find("#" + id);
    item = curPageObj.find("*[id='" + id + "']");
    item.attr(attrName, value);
}

/**
 * 프리뷰에 변경사항 적용
 * @param id
 * @param text
 * @param html
 */
function applyTextToPreview(id, text, html) {
    // preview 에 반영
    // var item = getElementInsidePreview("#" + id);
    var item = getElementInsidePreview("*[id='" + id + "']");
    if(text != null && typeof text !== 'undefined') {
        item.text(text);
    } else if(html != null && typeof html !== 'undefined') {
        item.html(html);
    } else {
    }

    // 페이지 obj 에 반영
    // item = curPageObj.find("#" + id);
    item = curPageObj.find("*[id='" + id + "']");
    if(text != null && typeof text !== 'undefined') {
        item.text(text);
    } else if(html != null && typeof html !== 'undefined') {
        item.html(html);
    } else {
    }
}

/**
 * 이미지를 프리뷰에 반영
 * @param id
 * @param input
 */
function applyImageToPreview(id, input, imgElem) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            // var item = getElementInsidePreview("#" + id);
            var item = getElementInsidePreview("*[id='" + id + "']");
            item.attr('src', e.target.result);

            if(imgElem != null) {
                imgElem.attr("src", e.target.result);
            }

            // 페이지 obj 에 반영
            // item = curPageObj.find("#" + id);
            item = curPageObj.find("*[id='" + id + "']");
            item.attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

/**
 * 이미지를 프리뷰에 반영
 * @param id
 * @param input
 */
function applyBackgroundImageToPreview(id, input, imgElem) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            // var item = getElementInsidePreview("#" + id);
            var item = getElementInsidePreview("*[id='" + id + "']");
            item.css('background-image', "url(" + e.target.result + ")");

            imgElem.css("background-image", "url(" + e.target.result + ")");

            // 페이지 obj 에 반영
            // item = curPageObj.find("#" + id);
            item = curPageObj.find("*[id='" + id + "']");
            if(item.length <= 0 && curPageObj.attr("id") == id) {
                item = curPageObj;
            }
            item.css('background-image', "url(" + e.target.result + ")");
        };

        reader.readAsDataURL(input.files[0]);
    }
}

/**
 * preview 내 id 로 element 조회
 * @param id
 * @returns {*|*|jQuery|HTMLElement}
 */
function getElementInsidePreview(el) {
    var frame = $("#preview");
    return $(el, frame.contents());
}

function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

/**
 * br to new line
 * @param text
 * @returns {void | string}
 */
function br2nl(text) {
    var regex = /<br\s*[\/]?>/gi;
    return text.replace(regex, "\n");
}

function nl2br (str, is_xhtml) {
    var breakTag = (is_xhtml || typeof is_xhtml === 'undefined') ? '<br />' : '<br>';
    return (str + '').replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1'+ breakTag +'$2');
}

/**
 * 편집완료된 html 가져오기
 * @returns {string}
 */
function getGeneratedHtml() {
    var bodyHtml = curPageObj.get(0).outerHTML;
    // console.log(bodyHtml);
    var beforeBody = curPageHtml.substr(0, curPageHtml.indexOf("<body"));
    var afterBody = curPageHtml.substr(curPageHtml.indexOf("</body>") + "</body>".length);

    // console.log(beforeBody);
    // console.log(afterBody);

    var generatedHtml = beforeBody + "<body>" + bodyHtml + "</body>" + afterBody;
    return generatedHtml;
}

/**
 * 저장하기
 */
function save() {
    // 현재 iframe 의 url
    // $("#preview").contents().get(0).location.href


    var previewLocation = $("#preview").contents().get(0).location.href;
    if(endsWith(previewLocation, curPageInfo.path)) {
        var generatedHtml = getGeneratedHtml();
        var htmlFilename = curPageInfo.path;
        console.log(generatedHtml);
        console.log(htmlFilename);

        // 서버에 내용 업데이트
        updatePage(domain, vendor, htmlFilename, generatedHtml, function(data) {
            if(data.code == 200) { // 저장 성공
                pageEdited = false;
                alert(data.message);
            } else {
                alert(data.code + ": " + data.message);
            }
        });

        // 편집 안된 상태로 변경
        pageEdited = false;
    } else {
        alert("저장할 수 없습니다.");
    }
}


/**
 * 페이지 사용안함 처리
 */
function disablePage() {
    var pageName = curPageInfo.name;
    var btn = $("#pages button[data-name='" + pageName + "']");
    if(btn.hasClass("disabled")) {
        btn.removeClass("disabled");
        $(".cbtns .btn_ten").removeClass("active");
    } else {
        btn.addClass("disabled");
        $(".cbtns .btn_ten").addClass("active");
    }
    // console.log(curPageInfo);
}