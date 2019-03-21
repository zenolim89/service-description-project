function template(templatePath, onInitialized) {
    this.templatePath = templatePath;

    $.get(templatePath + "template.json", function(data) {
        // console.log(this);
        // console.log(data);
        this.data = data;

        // 초기화 완료 콜백 호출
        onInitialized();
    }.bind(this));

    /**
     * 프리뷰 페이지 url 조회
     * @returns {*}
     */
    this.getPreviewPagePath = function() {
        return this.templatePath + this.data.preview;
    };

    /**
     * 템플릿 명 조회
     * @returns {*}
     */
    this.getTemplateName = function() {
        return this.data.name;
    };

    /**
     * 전체 페이지 목록 조회
     * @returns {any}
     */
    this.getPages = function() {
        return this.data.pages;
    };

    /**
     * 페이지 정보 찾기
     * @param pageName
     */
    this.getPage = function(pageName) {
        var pages = this.data.pages;
        for(var i = 0; i < pages.length; i++) {
            if(pages[i].name == pageName) {
                return pages[i];
            }
        }
        return null;
    };

    /**
     * path 로 페이지 정보 찾기
     * @param pagePath
     * @returns {*}
     */
    this.getPageByPath = function(pagePath) {
        var pages = this.data.pages;
        for(var i = 0; i < pages.length; i++) {
            if(pages[i].path == pagePath) {
                return pages[i];
            }
        }
        return null;
    };
}


