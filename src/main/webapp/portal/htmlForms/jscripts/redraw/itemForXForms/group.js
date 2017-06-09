/**
 * @include "../main.js"
 * @include "../../includes.js"
 */

/**
 * @param {Element}
 *          el - отрисовываемый элемент
 * @param {String}
 *          parentId - родительский sid
 * @param {String}
 *          parentXformRef - родительский xformRef
 * @param {Bounds}
 *          groupBounds - для определения границ pane
 * @return {HTMLElement} - полученный элемент
 */
function drawXformGroup(el, parentId, parentXformRef, groupBounds) {
	// console.log(el);
	
	// ------------------------------------------
	var thisXformsRef = "";

    if(ieXmlDom != null) {
        var ref = ieXpathEvaluate(el, "./@ref");
        if (parentXformRef == null || parentXformRef == "undefined") {
            if(ref.length > 0)
                thisXformsRef = ref[0].text;
        }
        else {
            if(ref.length > 0 && ref[0].text.indexOf('instance(') != -1) {
                thisXformsRef = ref[0].text;
            } else {
                thisXformsRef = "" + parentXformRef + "/" + ref[0].text;
            }
        }
    } else {
        if (parentXformRef == null || parentXformRef == "undefined") {
            // console.log("xforms:group parentXformRef=null or underfined");
            if (el.xpath("count(./@ref) > 0").booleanValue)
                thisXformsRef = el.xpath("string(./@ref)").stringValue;
        }
        else {
            if (el.xpath("contains(./@ref,'instance(')").booleanValue)
                thisXformsRef = el.xpath("string(./@ref)").stringValue;
            else
                thisXformsRef = "" + parentXformRef + "/"
                    + el.xpath("string(./@ref)").stringValue;
        }
    }

	// ------------------------------------------
	
	// ------------------------------------------
	var info = new InfoElement();
	// нельзя заменять на вызов поиска координат т.к. в первый раз отрисовки для
	// дочерних элементов не созданы инфоэлементы и не добавлены
	var bounds = findBounds(el, parentId, 1);
	
	var groupDiv = document.createElement("div");
	
	var id = "" + parentId;;// + "-group";
	// groupDiv.id = id;
	// ------------------------------------------
	
	// ------------------------------------------
	var currentEl = el.firstChild;
	
	if (currentEl.tagName != null && currentEl.tagName.replace(new RegExp('.+:','g'), '') == "label"
	    && currentEl.namespaceURI == "http://www.w3.org/2002/xforms")
		currentEl = currentEl.nextSibling;
	
	while (currentEl)
		{
			/**
			 * @type InfoElement
			 */
		var thisDrawEl = null;
		if (currentEl.nodeType == Node.ELEMENT_NODE)
			{
			switch (currentEl.tagName.replace(/.+:/g, ''))
				{
				case "action" :
					thisDrawEl = drawAction(currentEl, id, thisXformsRef);
					break;
				case "box" :
					thisDrawEl = drawBox(currentEl, id, thisXformsRef);
					break;
				case "button" :
					thisDrawEl = drawButton(currentEl, id, thisXformsRef);
					break;
				case "cell" :
					break;
				case "check" :
					thisDrawEl = drawCheck(currentEl, id, thisXformsRef);
					break;
				case "combobox" :
					thisDrawEl = drawCombobox(currentEl, id, thisXformsRef);
					break;
				case "data" :
					break;
				case "field" :
					thisDrawEl = drawField(currentEl, id, thisXformsRef);
					break;
				case "help" :
					break;
				case "label" :
					thisDrawEl = drawLabel(currentEl, id, thisXformsRef);
					break;
				case "line" :
					thisDrawEl = drawLine(currentEl, id, thisXformsRef);
					break;
				case "list" :
					// drawList(currentEl);
					break;
				case "pane" :
					thisDrawEl = drawPane(currentEl, id, thisXformsRef);
					break;
				case "popup" :
					thisDrawEl = drawPopup(currentEl, id, thisXformsRef);
					break;
				case "radio" :
					thisDrawEl = drawRadio(currentEl, id, thisXformsRef);
					break;
				case "signature" :
					break;
				case "spacer" :
					thisDrawEl = drawSpacer(currentEl, id, thisXformsRef);
					break;
				case "table" :
					thisDrawEl = drawTable(currentEl, id, thisXformsRef);
					break;
				}
			
			if (thisDrawEl)
				{
				groupDiv.appendChild(thisDrawEl.refHTMLElemDiv);
				var foundInfo = treeInfoEls.getElemInfo("" + id + "-"
				    + currentEl.getAttribute("sid"));
				if (groupBounds.x2 < foundInfo.x2)
					groupBounds.x2 = foundInfo.x2;
				if (groupBounds.y2 < foundInfo.y2)
					groupBounds.y2 = foundInfo.y2;
				
				}
			
			}
		currentEl = currentEl.nextSibling;
		}
	// ------------------------------------------
	
	// ------------------------------------------
	groupBounds.width = groupBounds.x2;
	groupBounds.height = groupBounds.y2;
	// ------------------------------------------
	
	// ------------------------------------------
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:0px;";
	styleDiv = styleDiv + "left:0px;";
	styleDiv = styleDiv + "width:" + groupBounds.width + "px;";
	styleDiv = styleDiv + "height:" + groupBounds.height + "px;";
	groupDiv.setAttribute("style", styleDiv);
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
	info.sid = "group";
	info.fullSidRef = "" + parentId + "-group";
	info.parentFullSidRef = "" + parentId;
	info.xformRef = thisXformsRef;
	// info.xformRefForLabel = ?;
	info.refHTMLElem = groupDiv;
	info.refHTMLElemDiv = groupDiv;
	info.typeEl = "xform:group";
	info.value = null;
	// ------------------------------------------
	
	// ------------------------------------------
    UtilsHTMLEl.setInfoElRef(groupDiv,info);
//	/info.refHTMLElem.setInfoElRef(info);
	treeInfoEls.addElemInfo(info);
	// ------------------------------------------
	
	return groupDiv;
}

/**
 * @param {InfoElement}
 *          info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordXformGroup(info)
	{
	var el = info.refElem;
	// ------------------------------------------
	var thisXformsRef = info.xformRef;
	// ------------------------------------------
	
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, 1);
	var id = "" + info.parentFullSidRef;// + "-group";
	// ------------------------------------------
	
	// ------------------------------------------
	var currentEl = el.firstChild;
	
	if (currentEl.tagName.replace(/.+:/g, '') == "label"
	    && currentEl.namespaceURI == "http://www.w3.org/2002/xforms")
		currentEl = currentEl.nextSibling;
	
	var foundBounds;
	while (currentEl)
		{
		foundBounds = null;
		if (currentEl.nodeType == Node.ELEMENT_NODE)
			{
			currentInfo = treeInfoEls.getElemInfo("" + id + "-"
			    + currentEl.getAttribute("sid"));
			switch (currentEl.localName)
				{
				case "action" :
				
					break;
				case "box" :
					foundBounds = findCoordBox(currentInfo);
					break;
				case "button" :
					foundBounds = findCoordButton(currentInfo);
					break;
				case "cell" :
					break;
				case "check" :
					foundBounds = findCoordCheck(currentInfo);
					break;
				case "combobox" :
					foundBounds = findCoordCombobox(currentInfo);
					break;
				case "data" :
					break;
				case "field" :
					foundBounds = findCoordField(currentInfo);
					break;
				case "help" :
					break;
				case "label" :
					foundBounds = findCoordLabel(currentInfo);
					break;
				case "line" :
					foundBounds = findCoordLine(currentInfo);
					break;
				case "list" :
					// drawList(currentEl);
					break;
				case "pane" :
					foundBounds = findCoordPane(currentInfo);
					break;
				case "popup" :
					foundBounds = findCoordPopup(currentInfo);
					break;
				case "radio" :
					foundBounds = findCoordRadio(currentInfo);
					break;
				case "signature" :
					break;
				case "spacer" :
					foundBounds = findCoordSpacer(currentInfo);
					break;
				case "table" :
					foundBounds = findCoordTable(currentInfo);
					break;
				}
			
			if (foundBounds)
				{
				if (bounds.x2 < foundBounds.x2)
					bounds.x2 = foundBounds.x2;
				if (bounds.y2 < foundBounds.y2)
					bounds.y2 = foundBounds.y2;
				}
			
			}
		currentEl = currentEl.nextSibling;
		}
	// ------------------------------------------
	
	// ------------------------------------------
	bounds.width = bounds.x2;
	bounds.height = bounds.y2;
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
