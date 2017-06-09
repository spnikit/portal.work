/**
 * @include "../main.js"
 * @include "../itemForXForms/group.js"
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
function drawPane(el, parentId, parentXformRef) {
	objStateDrawing.thisNestingDrawLevel++;
	// TODO сделать выборку по &lt;layoutflow>
	// console.log(el);
	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, null);
	info.bounds = bounds;
	// ------------------------------------------

	// ------------------------------------------
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	
	var paneDiv = document.createElement("div");
	UtilsHTMLEl.setInfoElRef(paneDiv, info);
	// paneDiv.setInfoElRef(info);
	paneDiv.id = "" + parentId + "-" + el.getAttribute("sid");

    if(ieXmlDom != null) {
        if (ieXpathEvaluate(el, "label").length > 0) {
            paneDiv.innerHTML = "" + getNodeValue(ieXpathEvaluate(el, "label")[0]) + "<br />";
        } else if (ieXpathEvaluate(el, "input/label").length > 0) {
            var text = ieXpathEvaluate(el, "input/label")[0].text;
            if (text != "")
                paneDiv.innerHTML = ""
                    + text
                    + "<br />";
        }
    } else {
        if (el.xpath("count(xfdl:label) > 0 ").booleanValue) {
            paneDiv.innerHTML = ""
                    + getNodeValue(el.xpath("xfdl:label").iterateNext()) + "<br />";
        } else if (el.xpath("count(xforms:input/xforms:label)>0").booleanValue) {
            if (el.xpath("string(xforms:input/xforms:label)").stringValue != "")
                paneDiv.innerHTML = ""
                        + el.xpath("string(xforms:input/xforms:label)").stringValue
                        + "<br />";
        }
    }

	// нужен для нахождения границ pane.
	var groupBounds = new Bounds(null);
    var group;

    if(ieXmlDom != null){
        group = ieXpathEvaluate(el, "xforms:group")[0];
    } else {
        group = el.xpath("xforms:group").iterateNext();
    }

	var groupDiv = drawXformGroup(group, paneDiv.id, parentXformRef, groupBounds);
	paneDiv.appendChild(groupDiv);
	// ------------------------------------------

	var childEl = el.firstChild;
	while (childEl){
		if (childEl.nodeType == Node.ELEMENT_NODE){
            lNameChildEl = childEl.tagName.replace(new RegExp('.+:','g'), '');
            if(ieXmlDom != null) {
                valChildEl = childEl.text;
            } else {
			    valChildEl = childEl.getValue();
            }
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")){
				switch (lNameChildEl){
					case 'itemlocation' :
						var sub = childEl.firstChild;
						while (sub){
							if (sub.nodeType == Node.ELEMENT_NODE){
								if (sub.namespaceURI == getNameSpaceForXfdlForm("xfdl")){
									findCompute(sub, info);
									//console.log(info);
								}
							}
							sub = sub.nextSibling;
						}
						break;
					break;
				}
				findCompute(childEl, info);
			}
		}
		childEl = childEl.nextSibling;
	}
	
	// ------------------------------------------
	var padding = 3;

    /*if (ieXmlDom != null) {
        if (ieXpathEvaluate(el, "padding")[0] != null && ieXpathEvaluate(el, "padding")[0].text != "")
            padding = parseInt(ieXpathEvaluate(el, "padding")[0].text);
    } else {
        if (el.xpath("padding != ''").booleanValue)
            padding = el.xpath("number(padding)").numberValue;
    }*/

	info.x1 = bounds.x1;
	if (bounds.width < groupBounds.width) {
		info.x2 = bounds.x1 + groupBounds.width + padding;
		info.width = groupBounds.width + padding;
	} else {
		info.x2 = bounds.x1 + bounds.width + padding;
		info.width = bounds.width + padding;
	}

	info.y1 = bounds.y1;

	if (bounds.height < groupBounds.height) {
		info.y2 = bounds.y1 + groupBounds.height + padding;
		info.height = groupBounds.height + padding;
	} else {
		info.y2 = bounds.y1 + bounds.height + padding;
		info.height = bounds.height + padding;
		// styleDiv = styleDiv + "height:" + bounds.height + "px;";
		}
	
	console.infoDraw("pane bounds.height = " + bounds.height);
	console.infoDraw("pane groupBounds.height = " + groupBounds.height);
	console.infoDraw("pane info.height = " + info.height);
	console.infoDraw("pane info.y1 = " + info.y1);
	console.infoDraw("pane info.y2= " + info.y2);

	// ------------------------------------------
	
	// ------------------------------------------
	styleDiv = styleDiv + "width:" + info.width + "px;";
	styleDiv = styleDiv + "z-index: 10;";
	paneDiv.setAttribute("style", styleDiv);
	// ------------------------------------------
	
	// ------------------------------------------
	info.refElem = el;
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;
	// info.xformRef = ?;
	// info.xformRefForLabel = ?;
	info.refHTMLElem = paneDiv;
	info.refHTMLElemDiv = paneDiv;
	info.typeEl = "pane";
	info.value = null;
	// ------------------------------------------
	
	// ------------------------------------------
	treeInfoEls.addElemInfo(info);

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
function findCoordPane(info) {
	var el = info.refElem;
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, null);
	// ------------------------------------------
	info.bounds = bounds;
	// ------------------------------------------
	// нужен для нахождения границ pane.
	if (!isAfterInitiator) {
		var group = el.xpath("xforms:group").iterateNext();
		var groupId = info.fullSidRef + "-group";
		var groupInfo = treeInfoEls.getElemInfo(groupId);

		var groupBounds = findCoordXformGroup(groupInfo);

		var padding = 3;
		if (el.xpath("padding != ''").booleanValue)
			padding = el.xpath("number(padding)").numberValue;

		if (bounds.width < groupBounds.width) {
			bounds.x2 = bounds.x1 + groupBounds.width + padding;
			bounds.width = groupBounds.width + padding;
		} else {
			bounds.x2 = bounds.x1 + bounds.width + padding;
			bounds.width = bounds.width + padding;
		}

		if (bounds.height < groupBounds.height) {
			bounds.y2 = bounds.y1 + groupBounds.height + padding;
			bounds.height = groupBounds.height + padding;
		} else {
			bounds.y2 = bounds.y1 + bounds.height + padding;
			bounds.height = bounds.height + padding;
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
