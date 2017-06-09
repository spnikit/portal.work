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
function drawSpacer(el, parentId, parentXformRef) {
	// console.log(el);
	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, null);
	info.bounds = bounds;
	bounds = updateSpacerBounds(el, bounds);
	// ------------------------------------------

	// ------------------------------------------
	var spacer = document.createElement("div");
	UtilsHTMLEl.setInfoElRef(spacer, info);
	// spacer.setInfoElRef(info);
	spacer.id = "" + parentId + "-" + el.getAttribute("sid");
	var style = "position:absolute;";
	style = style + "top:" + bounds.y1 + "px;";
	style = style + "left:" + bounds.x1 + "px;";
	style = style + "width:" + bounds.width + "px;";
	style = style + "height:" + bounds.height + "px;";
	style = style + "z-index: 10;";
	// style = style + "background: #000000;";
	
	spacer.setAttribute("style", style);
	// ------------------------------------------

	// ------------------------------------------

	// console.warn("spacer = "+spacer.id);
	// console.dir(bounds);
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
	info.refHTMLElem = spacer;
	info.refHTMLElemDiv = spacer;
	info.typeEl = "spacer";
	info.value = null;
	info.bounds = bounds;
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);
	// info.refHTMLElem.setInfoElRef(info);
	// ------------------------------------------

	// ------------------------------------------
	return info;
}

/**
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordSpacer(info) {
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, null);
	bounds = updateSpacerBounds(info.refElem, bounds);
	info.updateBounds(bounds);
	// ------------------------------------------

	// ------------------------------------------
	var div = info.refHTMLElemDiv;
	div.style.top = info.y1 + "px";
	div.style.left = info.x1 + "px";
	div.style.width = info.width + "px";
	div.style.height = info.height + "px";
	// ------------------------------------------

	return bounds;
}

/**
 * Функция применима только к линиям. Необходима для выставления толцины линии в
 * единицу.
 * 
 * @param {Bounds}
 *            bounds - текущие координаты
 * @param {Element}
 *            el - элемент, для перерасчета
 * @return {Bounds} - изменный результат
 */
function updateSpacerBounds(el, bounds) {
    if(ieXmlDom != null) {
        var size = ieXpathEvaluate(el, "./size");
        if(size.length > 0 && size[0].text == "1") {
            bounds.width = 1;
        } else {
            if (!bounds.height)
                bounds.height = 1;
        }
    } else {
        if (el.xpath("count(./xfdl:size)>0").booleanValue) {
            if (el.xpath("count(./xfdl:size/xfdl:height)>0").booleanValue
                    && el.xpath("number(./xfdl:size/xfdl:height)").numberValue == 1)
                bounds.width = 1;
        } else {
            if (!bounds.height)
                bounds.height = 1;
        }
    }
	return bounds;
}