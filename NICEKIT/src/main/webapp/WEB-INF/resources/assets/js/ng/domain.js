app.controller('domainController', ['$scope', domainController] );

function domainController($scope) {

    $scope.newFromVendor = {
        input:{
            name:"",
            test_url:"",
            real_url:""
        }
    };

    // 사업장 목록 뷰 표시여부
    $scope.showVendorList = false;

    // 생성 중인 사업장명
    $scope.creatingVendorName = "";

    /**
     * 기존 사업장 복사용 신규 서비스 사업장 생성 확인버튼 클릭
     */
    $scope.newFromVendorInputComplete = function() {
        var input = $scope.newFromVendor.input;
        if(input.name.trim() == "") {
            $scope.myAlert("서비스 사업장명을 입력해주세요.");
            return;
        }
        if(input.test_url.trim() == "") {
            $scope.myAlert("연동이 필요한 테스트 URL을 입력 해주세요.");
            return;
        }
        if(input.real_url.trim() == "") {
            $scope.myAlert("연동이 필요한 상용 URL을 입력 해주세요.");
            return;
        }

        $scope.creatingVendorName = input.name;

        $scope.showVendorList = true;
        // $(".article_list").show();
        $(".menus .btns li").removeClass("active");

        $("#popup_business").bPopup().close();
        // $(".new_vendor_name").text(input.name.trim());
    };



    // alert message
    $scope.alertMsg = "";
    /**
     * show alert
     * @param msg
     */
    $scope.myAlert = function(msg) {
        $scope.alertMsg = msg;
        $("#popup_alert_02").bPopup();
    };
}