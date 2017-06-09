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
function drawLine(el, parentId, parentXformRef) {
	// console.log(el);
	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, null);
	info.bounds = bounds;
	bounds = updateLineBounds(el, bounds);
	// ------------------------------------------

	if (el.getAttribute("sid") == "BORDER_HEADER") {
		console.infoDraw(el);
		console.dir(bounds);

		// bounds.y1 =77;
		// bounds.y2 =77;
	}

	// ------------------------------------------
	var line = document.createElement("div");
	line.id = "" + parentId + "-" + el.getAttribute("sid");
	var style = "position:absolute;";
	
	/**
	 * 
	 * @type Node|Element
	 */
	var childEl = null;

	/**
	 * 
	 * @type String
	 */
	var lNameChildEl;
	/**
	 * 
	 * @type String
	 */
	var valChildEl;

	childEl = el.firstChild;
	// альтернативный и более правильный метод перебора элементов
	// чем через xpath
	while (childEl) {
		if (childEl.nodeType == Node.ELEMENT_NODE) {
            lNameChildEl = childEl.tagName.replace(new RegExp('.+:','g'), '');
            if(ieXmlDom != null) {
                valChildEl = childEl.text;
            } else
			    valChildEl = childEl.getValue();
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {

				switch (lNameChildEl) {
					// case "value" :
					// info.hiddenTeg['value'] = childEl;
					// value = childEl.getValue();

					// break;
					case 'visible' :
						if (valChildEl == 'off')
							style = style + "visibility: hidden;";
						else
							style = style + "visibility: visible;";
						break;

					case 'itemlocation' :
						// TODO доделать
						break;
					break;
			}
			var attrScript = childEl.getAttribute('compute');
			if (attrScript) {
				/**
				 * @type EmulateNode|Element
				 */
				var emNode;
				if (info.xformNode) {
					emNode = new EmulateNode(lNameChildEl, valChildEl,
							childEl.parentNode);// так
					// как
					// есть
					// xform-связь
					info.hiddenTeg[lNameChildEl] = emNode;
				} else
					emNode = childEl;
				var scriptCompute = new UnitScript(attrScript, info, emNode);
				massAllScripts.push(scriptCompute);
			}

		}
		if (childEl.namespaceURI == getNameSpaceForXfdlForm("custom")) {
			// console.info(childEl.namespaceURI);
			// console.info(getNameSpaceForXfdlForm("custom"));

			var attrScript = childEl.getAttributeNS(
					getNameSpaceForXfdlForm("xfdl"), 'compute');
			// console.info(attrScript);
			if (attrScript) {

				/**
				 * @type EmulateNode|Element
				 */
				var emNode;
				if (info.xformNode) {
					emNode = new EmulateNode('custom:' + lNameChildEl,
							valChildEl, childEl.parentNode);// так как есть
															// xform-связь
					info.hiddenTeg['custom:' + lNameChildEl] = emNode;
					// console.info("info.hiddenTeg[custom:" + lNameChildEl + "]
					// = " +
					// info.hiddenTeg['custom:' + lNameChildEl]);
				} else
					emNode = childEl;
				var scriptCompute = new UnitScript(attrScript, info, emNode);
				massAllScripts.push(scriptCompute);
			}
		}

	}
	childEl = childEl.nextSibling;
}

style = style + "top:" + bounds.y1 + "px;";
style = style + "left:" + bounds.x1 + "px;";
style = style + "width:" + bounds.width + "px;";
style = style + "height:" + bounds.height + "px;";
style = style + "z-index: 11;";

//style = style + "background: #000000;";
var part = "";
if(bounds.height == 0 || bounds.height == 1) {
    part = "top";
} else {
    part = "left";
}
style = style + "border-" + part + ": 1px solid #000000;";

style = style + "-webkit-print-color-adjust: exact;";
line.setAttribute("style", style);

info.x1 = bounds.x1;
info.x2 = bounds.x2;
info.y1 = bounds.y1;
info.y2 = bounds.y2;
info.width = bounds.width;
info.height = bounds.height;

info.refElem = el;
info.sid = el.getAttribute("sid");
info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
info.parentFullSidRef = "" + parentId;
// info.xformRef = ?;
// info.xformRefForLabel = ?;
info.refHTMLElem = line;
info.refHTMLElemDiv = line;
info.typeEl = "line";
info.value = null;
// ------------------------------------------

// ------------------------------------------
UtilsHTMLEl.setInfoElRef(line, info);
// info.refHTMLElem.setInfoElRef(info);
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
function findCoordLine(info) {
// ------------------------------------------
var bounds = findBounds(info.refElem, info.parentFullSidRef, null);
bounds = updateLineBounds(info.refElem, bounds);
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
function updateLineBounds(el, bounds) {

if (ieXmlDom != null) {
    if (ieXpathEvaluate(el, "./size").length > 0) {
        if (ieXpathEvaluate(el, "./size/height").length > 0
            && ieXpathEvaluate(el, "./size/height")[0].text == '1')
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