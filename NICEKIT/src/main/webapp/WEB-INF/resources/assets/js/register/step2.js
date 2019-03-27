/**
 * 
 */
function popup(url, w, h, name, option) {
	var pozX, pozY;
	var sw = screen.availWidth;
	var sh = screen.availHeight;
	var scroll = 0;
	if (option == 'scroll') {
		scroll = 1;
	}
	pozX = (sw - w) / 2;
	pozY = (sh - h) / 2;
	window.open(url, name, "toolbar=no,menubar=no,location=no,status=0,scrollbars=" + scroll
				+ ",resizable=1,width=" + w + ",height=" + h + ",left=" + pozX + ",top=" + pozY);
}