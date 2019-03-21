var fixsticky = (function() {
	var sticky = null;
	var bottomButtom = null;
	var minTop = 0;

	function init() {
		$(document).ready(function() {
			if($(".sticky_link").length < 1) return;

			sticky = $(".sticky_link");

			if($(".bottom_article").length > 0) {
				bottomButtom = $(".bottom_article .buttons");
			}

			// go top
			if(sticky.find(".goTop").length > 0) {
				minTop = sticky.find(".btn_top").height();

				sticky.find(".btn_top").on("click", function(evt) {
					evt.preventDefault();
					gotoTop();
				});
			}			

			// sns
			if(sticky.find(".sns").length > 0) {
				sticky.after('<div class="sns_bg"></div>');

				$(".sns_bg").on("click", function(evt) {
					if(sticky.find(".sns").is(".active")) {
						onHideSns();
					}
					else {
						onShowSns();
					}
				});

				sticky.find(".btn").on("click", function(evt) {
					evt.preventDefault();

					if($(this).parents(".sns").eq(0).is(".active")) {
						onHideSns();
					}
					else {
						onShowSns();
					}
				});

				sticky.fadeIn(100);
			}

			$(window).resize(onScroll).load(onScroll).scroll(onScroll);

			onScroll();
		});
	}

	function onScroll() {
		var wh = window.innerHeight;
		var dh = $(document).height();
		var scrollTop = $(window).scrollTop();

		if(bottomButtom) {
			var btnTop = bottomButtom.offset().top;

			if(btnTop - scrollTop < wh) {
				sticky.css({"margin-bottom":wh - (btnTop - scrollTop)});
			}
			else {
				sticky.css({"margin-bottom":0});
			}
		}

		if(sticky.find(".goTop").length > 0) {
			if(scrollTop > minTop) {
				sticky.fadeIn(100);
			}
			else {
				sticky.fadeOut(100);
			}
		}
	}

	function onRefresh() {
		if($(".sticky_link").length < 1) return;

		onScroll();
	}

	function onShowSns() {
		$(".sns_bg").stop(true, true).fadeIn(300);
		sticky.find(".sns").addClass("active");
	}

	function onHideSns() {
		$(".sns_bg").stop(true, true).fadeOut(300);
		sticky.find(".sns").removeClass("active");
	}

	function gotoTop() {
		$("html, body").stop().animate({scrollTop:0}, 300, "easeOutQuint");
	}

	return {
		init : init,
		refresh : onRefresh,
		onShow : onShowSns,
		onHide : onHideSns,
		goTop : gotoTop
	}
}());

fixsticky.init();