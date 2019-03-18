"use strict";

var Utils = new function() {

	/**
	 * isEmpty
	 * @param _str
	 * @return boolean
	 */
	this.isEmpty = function(_str) {

		try {
			return !this.isNotEmpty(_str);
		}
		catch (excp) {
			log(ERROR, 'message : ' + excp.message);
			return true;
		}
		finally {
			//
		}

	};

	/**
	 * isNotEmpty
	 * @param _str
	 * @return boolean
	 */
	this.isNotEmpty = function(_str) {
		if (typeof _str == "function" || typeof _str == "boolean" || typeof _str == "number") {
			return true;
		}
		else if (typeof _str == "object") {
			if (_str === null || _str === undefined || _str === "undefined"
						|| typeof _str == "undefined") {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			var obj = String(_str);
			if (jQuery.trim(obj).length <= 0 || obj === null || obj === undefined
						|| obj === "undefined" || typeof obj == "undefined" || obj === "null") {
				return false;
			}
			else {
				return true;
			}
		}

		/*
		 * if(typeof _str == "object"){ if(_str === null || _str === undefined ||
		 * _str === "undefined" || typeof _str == "undefined"){ return false;
		 * }else{ return true; } }else if(typeof _str == "function"){ return
		 * true; }else{ var obj = String(_str); if(obj === null || obj ===
		 * undefined || obj === "undefined" || typeof obj == "undefined"){
		 * return false; }else{ return true; } }
		 */
	};

	/**
	 * isEmpty params
	 * @param _params
	 * @return boolean
	 */
	this.isEmptyParams = function(_params) {

		try {

			return jQuery.isEmptyObject(_params);

		}
		catch (excp) {
			log(ERROR, 'message : ' + excp.message);
			return true;
		}
		finally {
			//
		}

	};

	this.log = function(log, msg, value) {
		if (printLog) {
			if (jQuery.trim(value) != "" && jQuery.trim(value).length > 0) {
				console.log('[' + log + '] ' + msg + ' : ', value);
			}
			else {
				console.log('[' + log + '] ' + msg);
			}
		}
	};
};

//

function popupControl() {
	var target = $('.layer-pop-control');
	$.each(target, function() {
		$(this).on('click', function(e) {
			e.preventDefault();
			$($(this).attr('href')).show();
			$($(this).attr('href')).find('.confirm').on('click', function(e) {
				e.preventDefault();
				$(this).closest($('.popup-fulltype')).hide();
			})
			$($(this).attr('href')).find('.cancel').on('click', function(e) {
				e.preventDefault();
				$(this).closest($('.popup-fulltype')).hide();
			})
		})
	})
}
popupControl();

var checkView = 0;
function initSlide() {
	var check = $('.menu-main .swiper-slide').length, check2 = $('.menu-main .swiper-slide-duplicate').length;
	$('.placeholder').html($('.menu-main .swiper-slide').eq(0).data('slidedesc'));
	checkView = check - check2;
	$('.menu-main .swiper-container').css({
		width : (225 * (checkView)) - 18
	})
	return checkView;
}
initSlide();
function setSlide() {
	$('.menu-main').css({
		opacity : 1
	})
	var menuSwiper = new Swiper('.menu-main .swiper-container', {
			slidesPerView : checkView,
			spaceBetween : 18,
			centeredSlides : true,
			loop : true,
	});
	$('.menu-main .prev').on('click', function() {
		menuSwiper.slidePrev();
	})
	$('.menu-main .next').on('click', function() {
		menuSwiper.slideNext();
	})
	menuSwiper.on('slideChange', function() {
		$('.placeholder').html(
					$('.menu-main .swiper-slide').eq(menuSwiper.realIndex).data('slidedesc'));
	});
	var menuSwiper2 = new Swiper('.genie-voice .swiper-container', {
			slidesPerView : 1,
			spaceBetween : 0,
			direction : 'vertical',
			loop : true,
			height : 69,
			autoplay : {
				delay : 1000,
			}
	});
}
setTimeout(function() {
	setSlide()
}, 10)
