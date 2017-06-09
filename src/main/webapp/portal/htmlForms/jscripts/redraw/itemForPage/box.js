/**
 * @include "../../includes.js"
 */

/**
 * @param {Element}
 *            el - отрисовываемый элемент
 * @param {String}
 *            parentId - родительский sid
 * @param {String}
 *            parentXformRef - родительский xformRef
 * @return {InfoElement} - полученный элемент
 */
function drawBox(el, parentId, parentXformRef) {
	// console.log(el);
	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, null);
	info.bounds = bounds;
	// ------------------------------------------

	// ------------------------------------------
	var box = document.createElement("div");
	box.id = "" + parentId + "-" + el.getAttribute("sid");
	var style = "position:absolute;";
	style = style + "top:" + bounds.y1 + "px;";
	style = style + "left:" + bounds.x1 + "px;";
	style = style + "width:" + bounds.width + "px;";
	style = style + "height:" + (bounds.height - 1) + "px;";
	style = style + "z-index: 5;";

	style = style + "border: solid 1px black;";
	if (el.getElementsByTagName("bgcolor").length != 0) {
		style = style + "background:"
				+ el.getElementsByTagName("bgcolor").item(0).textContent + ";";
	} else {
		style = style + "background: #FFFFFF;";
	}

	box.setAttribute("style", style);
	// ------------------------------------------

	// ------------------------------------------
	info.x1 = bounds.x1;
	info.x2 = bounds.x2;
	info.y1 = bounds.y1;
	info.y2 = bounds.y2;
	info.width = bounds.width;
	info.height = bounds.height;
	// ------------------------------------------

	// ------------------------------------------
	info.refElem = el;
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;
	// info.xformRef = ?;
	// info.xformRefForLabel = ?;
	info.refHTMLElem = box;
	info.refHTMLElemDiv = box;
	info.typeEl = "box";
	info.value = null;
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);
	UtilsHTMLEl.setInfoElRef(box,info);
	
	// ------------------------------------------

	return info;
}

/**
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordBox(info) {
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, null);
	info.updateBounds(bounds);
	// ------------------------------------------

	// ------------------------------------------
	var div = info.refHTMLElemDiv;
	div.style.top = info.y1 + "px";
	div.style.left = info.x1 + "px";
	div.style.width = info.width + "px";
	div.style.height = (info.height - 1) + "px";
	// ------------------------------------------

	// ------------------------------------------
	return bounds;
}
