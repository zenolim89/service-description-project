$(document).ready(function(e) {
	var checkBrowser = function() {
		var agt = navigator.userAgent.toLowerCase();
		if (agt.indexOf("chrome") != -1) return 'Chrome';
		if (agt.indexOf("opera") != -1) return 'Opera';
		if (agt.indexOf("staroffice") != -1) return 'Star Office';
		if (agt.indexOf("webtv") != -1) return 'WebTV';
		if (agt.indexOf("beonex") != -1) return 'Beonex';
		if (agt.indexOf("chimera") != -1) return 'Chimera';
		if (agt.indexOf("netpositive") != -1) return 'NetPositive';
		if (agt.indexOf("phoenix") != -1) return 'Phoenix';
		if (agt.indexOf("firefox") != -1) return 'Firefox';
		if (agt.indexOf("safari") != -1) return 'Safari';
		if (agt.indexOf("skipstone") != -1) return 'SkipStone';
		if (agt.indexOf("msie") != -1) return 'Internet Explorer';
		if (agt.indexOf("netscape") != -1) return 'Netscape';
		if (agt.indexOf("mozilla/5.0") != -1) return 'Mozilla';
	}

	function onCheckDevice() {
		var isMoble = (/(iphone|ipod|ipad|android|blackberry|windows ce|palm|symbian)/i.test(navigator.userAgent)) ? "mobile" : "pc";
		$("body").addClass(isMoble);

		var deviceAgent = navigator.userAgent.toLowerCase();
		var agentIndex = deviceAgent.indexOf('android');

		if (agentIndex != -1) {
			var androidversion = parseFloat(deviceAgent.match(/android\s+([\d\.]+)/)[1]);

			$("body").addClass("android");

			// favicon();

			if (androidversion < 4.1) {
				$("body").addClass("android_old android_ics");
			}
			else if (androidversion < 4.3) {
				$("body").addClass("android_old android_oldjb");
			}
			else if (androidversion < 4.4) {
				$("body").addClass("android_old android_jb");
			}
			else if (androidversion < 5) {
				$("body").addClass("android_old android_kk");
			}

			if(checkBrowser() == "Chrome"
				|| checkBrowser() == 'Firefox'
				|| checkBrowser() == 'Mozilla')
			{
				$("body").removeClass("android_ics android_oldjb android_jb");
			}
		}
		else if(deviceAgent.match(/msie 8/) != null || deviceAgent.match(/msie 7/) != null) {
			$("body").addClass("old_ie");
		}
	}

	onCheckDevice();

	// css check
	window.checkSupported = function(property) {
		return property in document.body.style;
	}


	// dropdown list
	var dropList = (function() {
		function init() {
			var time = 150;

			// dropdown list
			$(document).on("change", ".dropLst .hidradio", function(evt) {
				var groupName = $(this).attr("name");
				var radios = $(".hidradio[name=" + groupName + "]");
				var checked = radios.filter(function() { return $(this).prop("checked") === true; });
				var text = $(checked).parents("label").find(".value").text();
				var list = $(checked).parents(".dlst").eq(0);

				$(list).find("label").removeClass("on");
				$(checked).parents("label").eq(0).addClass("on");

				if($(list).siblings(".txt").find(".val").length > 0) {
					$(list).siblings(".txt").find(".val").text(text);
				}
				else {
					$(list).siblings(".txt").text(text);
				}

				$(list).siblings(".txt").removeClass("on");
				$(checked).parents(".dlst").slideUp(time);
			}).on("click", ".dropLst > a", function(evt) {
				evt.preventDefault();

				var label = $(this);
				var target = $(this).parents(".dropLst").eq(0);
				var list = $(this).siblings(".dlst");
				var openList = $(".dropLst").filter(function() { return $(this).find(".dlst").css("display") != "none" && $(this) != target });

				$(openList).find(".dlst").stop().slideUp(time);
				$(target).find(" > a").removeClass("on").addClass("active");

				$(list).stop().slideToggle(time, function() {
					if($(this).css("display") != "none") $(label).addClass("on");
					else $(label).removeClass("on");

					$(label).removeClass("active");
				});
			}).on("click", ".dropLst li a", function(evt) {
				var value = $(this).text();

				$(this).parents(".dlst").eq(0).stop().slideUp(time, function() {
					if($(this).siblings(".txt").find(".val").length > 0) {
						$(this).siblings(".txt").find(".val").text(value);
					}
					else {
						$(this).siblings(".txt").text(value);
					}

					$(this).siblings(".txt").focus();
				});

				$(".dropLst > a").removeClass("on");

				$(this).parents(".dlst").eq(0).find("li a").removeClass("on");
				$(this).addClass("on");
			}).on("keyup", ".dropLst > a", function(evt) {
				var keyCode = evt.keyCode;

				var target = $(this).parents(".dropLst").eq(0);
				var list = $(this).siblings(".dlst");
				var chkRadio = $(this).siblings(".dlst").find(".hidradio:checked");
				var hoverRadio = $(list).find(".hover");
				var idx = -1;

				if(hoverRadio.length < 1) idx = (chkRadio.parents("li").eq(0).index() > -1) ? chkRadio.parents("li").eq(0).index() : 0;
				else idx = hoverRadio.parents("li").eq(0).index();

				var openList = $(list).filter(function() { return $(this).css("display") != "none" });
				if(openList.length < 1) return false;

				if(keyCode == 13) {
					$(list).find("li").find(".hover").find(".hidradio").prop("checked", true).trigger("change");
					$(list).find("label").removeClass("hover");
				}
				else if(keyCode == 38 || keyCode == 37) {
					$(list).find("label").removeClass("hover");

					if(idx == 0) $(list).find("li").eq($(list).find("li").length - 1).find("label").addClass("hover");
					else $(list).find("li").eq(idx - 1).find("label").addClass("hover");
				}
				else if(keyCode == 40 || keyCode == 39) {
					$(list).find("label").removeClass("hover");

					if(idx == ($(list).find("li").length - 1)) $(list).find("li").eq(0).find("label").addClass("hover");
					else $(list).find("li").eq(idx + 1).find("label").addClass("hover");
				}
			}).on("focus", ".dropLst .dlst label", function(evt) {
				$(this).on("keyup", addEnterKeyEvent);
			}).on("blur", "label", function(evt) {
				$(this).off("keyup", addEnterKeyEvent);
			}).on("click touchstart", function(evt) {
				var evt = evt ? evt : event;
				var target = null;

				if (evt.srcElement) target = evt.srcElement;
				else if (evt.target) target = evt.target;

				var openList = $(".dropLst").filter(function() { return $(this).find(".dlst").css("display") != "none" });
				var activeList = $(".dropLst").filter(function() { return $(this).find(".txt").hasClass("on") === true });
				if($(target).parents(".dropLst").eq(0).length < 1) {
					$(openList).find(".dlst").slideUp(time);
					$(".dropLst > a").removeClass("on").removeClass("active");
				}
				else if(activeList.length > 0) {
					activeList.find(".txt").removeClass("on").removeClass("active");
				}
			});

			function addEnterKeyEvent(evt) {
				var keyCode = evt.keyCode;
				if(keyCode == 13) {
					$(this).children(".hidradio").prop("checked", true).trigger("change");
					$(this).parents(".dropLst").eq(0).find(".txt").focus();
				}
			}

			// init dropdown list value
			$(".dropLst").each(function(i) {
				var groupName = $(this).find(".hidradio").eq(0).attr("name");
				var radios = $(".hidradio[name=" + groupName + "]");
				var checked = $(radios).filter(function() { return $(this).prop("checked") === true; });
				var list = $(this).find(".dlst");
				var text = null;

				if(radios.length > 0 && checked.length > 0) {
					text = (checked.length > 0) ? $(checked).parents("label").find(".value").text() : radios.eq(0).siblings(".value").text();

					$(list).find("label").removeClass("on").attr("tabindex", 0);
					$(list).find("label input").attr("tabindex", -1);
					if (checked.length > 0) {
						$(checked).parents("label").eq(0).addClass("on");
					}
					else {
						radios.eq(0).parents("label").eq(0).addClass("on");
					}
				}
				else {
					text = (list.find(".value.on").length > 0) ? list.find(".value.on").text() : (($(this).find(".txt .val").length > 0) ? $(this).find(".txt .val").text() : $(this).find(".txt").text());
				}

				if($(list).siblings(".txt").find(".val").length > 0) {
					$(list).siblings(".txt").find(".val").text(text);
				}
				else {
					$(list).siblings(".txt").text(text);
				}
			});
		}

		return {
			init : init
		};
	}());

	dropList.init();

	// select
	var selectList = (function() {
		function init() {
			// select
			$(document).on("change", ".selectbox select", function(evt) {
				if($(this).attr("readonly")) {
					return false;
				}

				var text = $(this).find("option:selected").text();
				$(this).siblings(".txt").text(text);
			}).on("keyup", ".selectbox > .txt", function(evt) {
				var keyCode = evt.keyCode;
				var labelText = $(this).text();
				var selectObj = $(this).siblings("select");
				var idx = ($(this).text() == $(selectObj).children("option:selected").text()) ? $(selectObj).children("option:selected").index() : 0;

				if(keyCode == 38 || keyCode == 37) {
					if(idx == 0) $(selectObj).children().eq($(selectObj).children().length - 1).attr("selected", "selected").trigger("change");
					else $(selectObj).children().eq(idx - 1).attr("selected", "selected").trigger("change");
				}
				else if(keyCode == 40 || keyCode == 39) {
					if(idx == ($(selectObj).children().length - 1)) $(selectObj).children().eq(0).attr("selected", "selected").trigger("change");
					else $(selectObj).children().eq(idx + 1).attr("selected", "selected").trigger("change");
				}
			}).on("click", ".selectbox > .txt", function(evt) {
				return false;
			}).on("focus", ".selectbox select", function(evt) {
				$(this).siblings(".txt").addClass("focus");
			}).on("blur", ".selectbox select", function(evt) {
				$(this).siblings(".txt").removeClass("focus");
			}).on("reset", "form", function(evt) {
				var selects = $(this).find(".selectbox select");

				setTimeout(function() {
					$(selects).each(function(i) {
						var text = ($(this).find("option:selected").text().length > 0) ? $(this).find("option:selected").text() : $(this).find("option").eq(0).text();
						$(this).siblings(".txt").text(text);
					});
				}, 30);
			});

			// init select box value
			$(".selectbox select").each(function(i) {
				var text = ($(this).find("option:selected").text().length > 0) ? $(this).find("option:selected").text() : $(this).find("option").eq(0).text();
				$(this).siblings(".txt").text(text);
			});

		}

		return {
			init : init
		};
	}());

	selectList.init();

	// file
	window.upFile = (function() {
		function init() {
			// file event
			$(document).on("change", ".hidFile", function(evt) {
				console.log("change")
				var file = $(this).val().split(/(\\|\/)/g).pop();
				var ext = file.split(".").pop();
				var fileLabel = $(this).siblings(".comText");
				var helpText = fileLabel.attr("title");

				if($(this).attr("readonly")) {
					return false;
				}

				if(file.length > 1) {
					$(this).parents(".box").addClass("active");
					$(this).parents(".box").addClass("editing");
					fileLabel.text(file).removeClass("unselect");
				}
				else {
					$(this).parents(".box").removeClass("active");
					$(this).parents(".box").removeClass("editing");
					fileLabel.text(helpText).addClass("unselect");
				}
			}).on("reset", "form", function(evt) {
				$(this).find(".hidFile").each(function(i) {
					$(this).parents(".box").addClass("active");
					$(this).parents(".box").addClass("editing");
					var helpText = $(this).siblings(".comText").attr("title");
					$(this).siblings(".comText").text(helpText).addClass("unselect");
				});
			});
		}

		return {
			init : init
		}
	}());

	upFile.init();


	// placeholder
	$(".comText[type=text]").placeholder();
	$("textarea").placeholder();
});

var loading = {
	show : function(target) {
		var html = '<div class="loadings' + ((target) ? ' inner' : '') + '"><div><i></i></div></div>';

		if(target) {
			if($(target).find("> .loadings").length < 1) $(target).append(html);
		}
		else {
			$("body").append(html);
		}
	},

	hide : function(target) {
		if(target) {
			$(target).find(".loadings").remove();
		}
		else {
			$(".loadings").remove();
		}
	}
}

var noticeSlider = (function() {
	function init() {

		$(".notice_slider").each(function(i) {
			var id = $(this).attr("id");
			var btn = $(this).siblings(".ctr_btn");

			$("#" + id).on("init", function() {
				// tab key
				$(this).on("beforeChange", function(event, slick, currentSlide, nextSlide) {
					$(this).find(".item a").attr("tabindex" , "-1");
				}).on("afterChange", function(event, slick, currentSlide) {
					$(this).find(".slick-active a").attr("tabindex" , 0);
				});
			}).slick({
				infinite : true
				, arrows : true
				, appendArrows : btn
				, dots : false
				, initialSlide : 0
				, autoplay : true
				, autoplaySpeed : 6000
				, speed : 300
				, pauseOnHover : true
				, draggable : false
				, accessibility : false
				, cssEase : "ease-out"
				, touchThreshold : 10
				, vertical : true
			});
		});
	}

	return {
		init : init
	}
}());


var trackpadScroll = {
	init : function(scrollId) {
		if($("#" + scrollId).hasClass("active")) {
			$("#" + scrollId).TrackpadScrollEmulator("recalculate");
		}
		else {
			$("#" + scrollId).addClass("active").TrackpadScrollEmulator({ autoHide: false });
		}
	},

	refresh : function(scrollId) {
		$("#" + scrollId).TrackpadScrollEmulator("recalculate");
	},

	destory : function(scrollId) {
		$("#" + scrollId).TrackpadScrollEmulator("destroy");
	},

	scrollTop : function(scrollId, top) {
		if(!top || top < 0) top = 0;
		$("#" + scrollId + " .tse-scroll-content").stop().animate({scrollTop:top}, 100);
	},

	scrollEnd : function(scrollId) {
		var top = $("#" + scrollId + " .tse-scroll-content").outerHeight();
		$("#" + scrollId + " .tse-scroll-content").stop().animate({scrollTop:top}, 100);
	}
}

var gnb = (function() {
	function init() {
		$(document).ready(function() {
			var atime = 400;
			var ease = "easeOutCirc";

			$(".header").append('<div class="gnb_modal"></div>');

			// bg click
			$(".gnb_modal").on("click", onHideSiteMap);

			// sitemap
			$(".tnav.btn_search").on("click", function(evt) {
				evt.preventDefault();

				if($(".gnb_nav .search").hasClass("active")) {
					onHideSiteMap();
				}
				else {
					onShowSiteMap();
				};
			});
			$(".gnb_nav .search .btn_close").on("click", function(evt) {
				evt.preventDefault();
				onHideSiteMap();
			});

		});
	}

	function onShowSiteMap() {
		var obj = $(".gnb_nav .search");
		var btn = $(".tnav.btn_search");

		$("html, body").stop().animate({scrollTop: 0}, 200);
		checkActiveX.show();
		$(".gnb_modal").addClass("on");
		btn.addClass("active");
		obj.addClass("active");
	}

	function onHideSiteMap() {
		var obj = $(".gnb_nav .search");
		var btn = $(".tnav.btn_search");

		$(".gnb_modal").removeClass("on");
		$(".header .search .search_results").removeClass("active");
		$("#searchWord").val('');
		obj.removeClass("active");
		btn.removeClass("active");
	}

	function setHeight() {
		var header = $(".header").outerHeight();
		var wh = window.innerHeight ? window.innerHeight : $(window).height();
		$(".gnb_nav .search").css("height", wh - header);
	}

	return {
		init : init,
		siteMap : onShowSiteMap,
	}
}());

var checkActiveX = {
	show : function() {
		var iframe = null;
		var vodPlayer = $(".vod_player");

		$(".hideFrame").remove();

		if(vodPlayer.length > 0) {
			iframe = "<iframe class='hideFrame' frameborder='0' scrolling='no' style='filter:alpha(opacity=0); opacity:0; position:absolute; z-index:1;"
					+ "left: " + vodPlayer.offset().left + "px;"
					+ "top: " + vodPlayer.offset().top + "px;"
					+ "width: " + vodPlayer.outerWidth() + "px;"
					+ "height: " + vodPlayer.outerHeight() + "px;'></iframe>";

			vodPlayer.css("zIndex", 0);
			$(".vod_panel").append(iframe);
		}
	},

	hide : function() {
		$(".hideFrame").remove();
	}
}


var jqDatePicker = (function() {
	function init() {
		var dateOption = {
			changeYear: false,
			changeMonth: false,
			autoSize:true,
			showMonthAfterYear:true,
			dateFormat:"yy-mm-dd",
			minDate: "-150y",
			maxDate: "+1y",
			yearRange: "-100:+1",
			showButtonPanel: false,
			closeText: "닫기",
			currentText: 'Today',
			dayNames: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
			dayNamesMin:["일", "월", "화", "수", "목", "금", "토"],
			monthNames:[". 01",". 02",". 03",". 04",". 05",". 06",". 07",". 08",". 09",". 10",". 11",". 12"],
			monthNamesShort: [ "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ],
			navigationAsDateFormat: true,
			prevText: '이전달',
			nextText: '다음달',
			beforeShow: function(input, picker) {
				// showPrevNextYearButton($(this));

				if($(this).parents(".frm_date").length > 0) {
					picker.dpDiv.css({marginTop:"-1px", marginLeft: (-1 * (picker.dpDiv.outerWidth() - input.offsetWidth)) + 'px'});
				}
			},
			onChangeMonthYear: function(input, picker) {
				// showPrevNextYearButton($(this));
			}
		};

		$.datepicker.setDefaults(dateOption);

		$(document).ready(function() {
			$(".txtDate").each(function(i) {
				$(this).datepicker(dateOption);
			});

			$(window).resize(function() {
				$(".txtDate").each(function(i) {
					$(this).datepicker("hide");
				});
			});
		});
	}

	// 달력
	function showPrevNextYearButton($input) {
		setTimeout(function() {
			var header = $input.datepicker('widget').find('.ui-datepicker-header');

			if($input.datepicker('widget').find('.ui-datepicker-header').find(".ui-datepicker-prev").is(".ui-state-disabled") == false) {
				var $prevButton = $('<a title="이전년도" class="ui-datepicker-prevYear ui-corner-all"><span>이전년도</span></a>');
				header.find('a.ui-datepicker-prev').before($prevButton);
				$prevButton.unbind("click").bind("click", function() {
					$.datepicker._adjustDate($input, -1, 'Y');
				});
			}

			// ui-state-disabled
			if($input.datepicker('widget').find('.ui-datepicker-header').find(".ui-datepicker-next").is(".ui-state-disabled") == false) {
				var $nextButton = $('<a title="다음년도" class="ui-datepicker-nextYear ui-corner-all"><span>다음년도</span></a>');
				header.find('a.ui-datepicker-next').after($nextButton);
				$nextButton.unbind("click").bind("click", function() {
					$.datepicker._adjustDate($input, +1, 'Y');
				});
			}
		}, 1);
	};


	// mobile check
	function isMobile() {
		return /(iphone|ipod|ipad|android|blackberry|windows ce|palm|symbian)/i.test(navigator.userAgent);
	}

	return {
		init : init
	};
}());

jqDatePicker.init();


var checkPopup = {
	show : function() {
		var iframe = null;
		var vodPlayer = $(".vod_player");

		$(".hideFrame").remove();

		if(vodPlayer.length > 0) {
			var openPopup = $(".popup").filter(function() { return $(this).css("display") == "block" });

			if(openPopup.length == 1) {
				iframe = "<iframe class='hideFrame' frameborder='0' scrolling='no' style='filter:alpha(opacity=0); opacity:0; position:absolute; z-index:1;"
						+ "left: " + openPopup.css("left") + ";"
						+ "top: " + openPopup.css("top") + ";"
						+ "width: " + openPopup.outerWidth() + "px;"
						+ "height: " + openPopup.outerHeight() + "px;'></iframe>";
			}
			else {
				iframe = "<iframe class='hideFrame' frameborder='0' scrolling='no' style='filter:alpha(opacity=0); opacity:0; position:absolute; z-index:1;"
						+ "left: " + vodPlayer.offset().left + "px;"
						+ "top: " + vodPlayer.offset().top + "px;"
						+ "width: " + vodPlayer.outerWidth() + "px;"
						+ "height: " + vodPlayer.outerHeight() + "px;'></iframe>";
			}

			vodPlayer.css("zIndex", 0);
			$(".vod_panel").append(iframe);
		}
	},

	hide : function() {
		$(".hideFrame").remove();
	}
}


function buttonPosition(popupId) {
	var uagent = navigator.userAgent.toLocaleLowerCase();
    var isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/));
    var isIE11 = !!(navigator.userAgent.match(/Trident/) && navigator.userAgent.match(/rv 11/));
    var isChrome = window.chrome && !navigator.userAgent.match(/Opera|OPR\//);
    var isEdge = navigator.appVersion.indexOf("Edge/") != -1 ? true : false;
    var isFirefox = navigator.userAgent.toLowerCase().indexOf("firefox") > -1;
    var isOpera = navigator.userAgent.toLowerCase().indexOf("opr/") != -1 ? true : false;


    var bMove = !isChrome && !isEdge && !isFirefox && !isOpera && !isAtLeastIE11;
    var btnPostion = $(".btn_popup").filter(function() { return $(this).attr("href") == popupId });

    if(btnPostion.length > 0 && bMove) {
		$("html, body").stop().animate({ scrollTop : btnPostion.offset().top - 20}, 0, function(evt) {

		});
    }
}

