/**
 * @include "../main.js"
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
function drawRadio(el, parentId, parentXformRef) {
	// console.log(el);
	// ------------------------------------------

	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, 1);
	info.bounds = bounds;
	bounds = applyCommonUpdates(bounds, el);
	// ------------------------------------------

	// ------------------------------------------
	var checkDiv = document.createElement("div");
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	styleDiv = styleDiv + "width:" + bounds.width + "px;";
	styleDiv = styleDiv + "height:" + bounds.height + "px;";
	styleDiv = styleDiv + "z-index: 10;";

	checkDiv.setAttribute("style", styleDiv);
	// ------------------------------------------

	// ------------------------------------------
	var check = document.createElement("input");
	UtilsHTMLEl.setInfoElRef(check, info);

	check.id = "" + parentId + "-" + el.getAttribute("sid");
	var style = "";

	if (el.getAttribute("visible") == "off") {
		style = style + "visibility: hidden;";
	} else if (el.getAttribute("visible") == "on") {
		style = style + "visibility: visible;";
	} else {
		style = style + "visibility: visible;";
	}

	check.setAttribute("style", style);
	check.setAttribute("type", "radio");

	if (el.getAttribute("active") == "off" || !isGlobalEdit)
		check.setAttribute("disabled", "disabled");

	if (el.getElementsByTagName("label").length != 0) {
		checkDiv.innerHTML = ""
				+ getNodeValue(el.getElementsByTagName("label").item(0))
				+ "<br />";
		bounds.x2 += 20;
		bounds.width += 20;
		bounds.y2 += 20;
		bounds.height += 20;
	} else if (el.xpath("count(xforms:input/xforms:label)>0").booleanValue) {
		if (el.xpath("string(xforms:input/xforms:label)").stringValue != "") {
			checkDiv.innerHTML = ""
					+ el.xpath("string(xforms:input/xforms:label)").stringValue
					+ "<br />";
			bounds.x2 += 20;
			bounds.width += 20;
			bounds.y2 += 20;
			bounds.height += 20;
		}
	}

	checkDiv.appendChild(check);
	// ------------------------------------------

	// ------------------------------------------
	bounds = cancelCommonUpdates(bounds, el);
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
	info.refHTMLElem = check;
	info.refHTMLElemDiv = checkDiv;
	info.typeEl = "check";
	info.value = null;
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);

	// ------------------------------------------

	// ------------------------------------------
	return info;
}

/**
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordRadio(info) {
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, 1);
	// ------------------------------------------

	// ------------------------------------------
	if (info.refElem.getElementsByTagName("label").length != 0) {
		bounds.x2 += 20;
		bounds.width += 20;
		bounds.y2 += 20;
		bounds.height += 20;
	} else if (info.refElem.xpath("count(xforms:input/xforms:label)>0").booleanValue) {
		if (info.refElem.xpath("string(xforms:input/xforms:label)").stringValue != "") {
			bounds.x2 += 20;
			bounds.width += 20;
			bounds.y2 += 20;
			bounds.height += 20;
		}
	}
	// ------------------------------------------

	// ------------------------------------------
	info.updateBounds(bounds);
	// ------------------------------------------

	// ------------------------------------------
	bounds = applyCommonUpdates(bounds, info.refElem);
	// ------------------------------------------

	// ------------------------------------------
	var div = info.refHTMLElemDiv;
	div.style.top = info.y1 + "px";
	div.style.left = info.x1 + "px";
	div.style.width = info.width + "px";
	div.style.height = info.height + "px";
	// ------------------------------------------

	// ------------------------------------------
	bounds = cancelCommonUpdates(bounds, info.refElem);
	// ------------------------------------------

	return bounds;
}
