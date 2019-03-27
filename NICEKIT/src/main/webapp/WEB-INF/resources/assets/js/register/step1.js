/**
 * 도메인 목록에 클릭이벤트 연결
 */
function initDomain() {
    // 도메인 클릭 이벤트 연결
    $(".domain_tab li button").on("click", function(e){
        e.preventDefault();
        $(".domain_tab li button").removeClass("active");
        $(".service_list li button").removeClass("active");
        $(this).addClass("active");


        // 해당 도메인 선택 작업
        var domainName = $(this).attr("data-domain");
        console.log(domainName);
        
       	switch(domainName){
       		case "resort":
       		 	//selectDomain(domainName);
       			break;
       		case "hotel":
       			alert('서비스 준비 중 입니다.');
       			$("button.resort").click();
       			break;
       		case "shop":
       			alert('서비스 준비 중 입니다.');
       			$("button.resort").click();
       			break;
			default:
				alert("[ERR] Not Found Domain Name");
       	}
    });
}