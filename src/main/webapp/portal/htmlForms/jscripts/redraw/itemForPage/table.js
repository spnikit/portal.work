/**
 * @include "../main.js"
 * @include "../applyBounds.js"
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
function drawTable(el, parentId, parentXformRef) {
	objStateDrawing.thisNestingDrawLevel++;
	// TODO сделать выборку по &lt;layoutflow>

	// console.log(el);
	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, null);
	info.bounds = bounds;
	info.selectedRow = 1;
	// ------------------------------------------

	// ------------------------------------------
	var tableDiv = document.createElement("div");
	var styleDiv = "position:absolute;";
	
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	UtilsHTMLEl.setInfoElRef(tableDiv, info);
	// tableDiv.setInfoElRef(info);
	tableDiv.id = "" + parentId + "-" + el.getAttribute("sid");
	tableDiv.setAttribute("style", styleDiv);

    if(ieXmlDom != null) {
        var label = ieXpathEvaluate(el, 'label');
        var inputLabel = ieXpathEvaluate(el, 'xforms:input/xforms:label');
        if (label.length > 0)
        {
            tableDiv.innerHTML = ""
                + getNodeValue(label[0]) + "<br />";
        } else if (inputLabel.length > 0) {
            if (inputLabel[0].text != "")
                tableDiv.innerHTML = ""
                    + inputLabel[0].text
                    + "<br />";
        }
    } else {
        if (el.xpath("count(xfdl:label) > 0 ").booleanValue)
            {
            tableDiv.innerHTML = ""
                    + getNodeValue(el.xpath("xfdl:label").iterateNext()) + "<br />";
        } else if (el.xpath("count(xforms:input/xforms:label)>0").booleanValue) {
            if (el.xpath("string(xforms:input/xforms:label)").stringValue != "")
                tableDiv.innerHTML = ""
                        + el.xpath("string(xforms:input/xforms:label)").stringValue
                        + "<br />";
        }
    }
	// ------------------------------------------

	// ------------------------------------------
	// нужен для нахождения границ pane.
	var repeatBounds = new Bounds(null);
	var repeat;
    if(ieXmlDom != null) {
        repeat = ieXpathEvaluate(el, 'xforms:repeat')[0];
    } else {
        repeat = el.xpath("xforms:repeat").iterateNext();
    }
	var repeatDiv = drawXformRepeat(repeat, tableDiv.id, parentXformRef, repeatBounds);
	tableDiv.appendChild(repeatDiv);
	// ------------------------------------------

	// ------------------------------------------
	info.x1 = bounds.x1;
	info.x2 = bounds.x1 + repeatBounds.width;
	info.y1 = bounds.y1;
	info.y2 = bounds.y1 + repeatBounds.height;
	info.width = repeatBounds.width;
	info.height = repeatBounds.height;
	// ------------------------------------------
	
	// ------------------------------------------
	styleDiv = styleDiv + "width:" + info.width + "px;";
	styleDiv = styleDiv + "z-index: 10;";
	// ------------------------------------------
	
	// ------------------------------------------
	info.refElem = el;
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;
	info.xformRef = parentXformRef;
	// info.xformRefForLabel = ?;
	info.refHTMLElem = tableDiv;
	info.refHTMLElemDiv = tableDiv;
	info.typeEl = "table";
	info.value = null;
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);
	// info.refHTMLElem.setInfoElRef(info);
	// ------------------------------------------

	// ------------------------------------------

	objStateDrawing.thisNestingDrawLevel--;
	return info;
}

/**
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordTable(info) {
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, null);
	// ------------------------------------------

	// ------------------------------------------
	// нужен для нахождения границ table.
	if (!isAfterInitiator) {
		var repeat = info.refElem.xpath("xforms:repeat").iterateNext();

		// var repeatBounds = findCoordXformRepeat(repeat, info.fullSidRef,
		// info.xformRef);
		var repeatBounds = findCoordXformRepeat(repeat, info);

		if (bounds.width < repeatBounds.width) {
			bounds.x2 = bounds.x1 + repeatBounds.width;
			bounds.width = repeatBounds.width;
		} else {
			bounds.x2 = bounds.x1 + bounds.width;
			bounds.width = bounds.width;
		}

		if (bounds.height < repeatBounds.height) {
			bounds.y2 = bounds.y1 + repeatBounds.height;
			bounds.height = repeatBounds.height;
		} else {
			bounds.y2 = bounds.y1 + bounds.height;
			bounds.height = bounds.height;
		}
	}
	// ------------------------------------------

	// ------------------------------------------
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