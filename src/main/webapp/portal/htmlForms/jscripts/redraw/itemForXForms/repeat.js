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
 *          repeatBounds - для определения границ pane
 * @return {HTMLElement} - полученный элемент
 */
function drawXformRepeat(el, parentId, parentXformRef, repeatBounds) {
	// console.log(el);
	
	// ------------------------------------------
	var thisXformsRef = "";

    if(ieXmlDom != null) {
        if (parentXformRef == null || parentXformRef == "undefined") {
            console.log("xforms:group parentXformRef=null or underfined");
            var node = ieXpathEvaluate(el, "./@nodeset");
            if (node.length > 0)
                thisXformsRef = node[0].text;
        } else {
            var node = ieXpathEvaluate(el, "./@nodeset");
            if(node[0].text.indexOf("instance(") != -1) {
                thisXformsRef = node[0].text;
            } else {
                thisXformsRef = "" + parentXformRef + "/"
                    + node[0].text;
            }
        }
    } else {
        if (parentXformRef == null || parentXformRef == "undefined")
        {
            console.log("xforms:group parentXformRef=null or underfined");
            if (el.xpath("count(./@nodeset) > 0").booleanValue)
                thisXformsRef = el.xpath("string(./@nodeset)").stringValue;
        }
        else
        {
            if (el.xpath("contains(./@nodeset,'instance(')").booleanValue)
                thisXformsRef = el.xpath("string(./@nodeset)").stringValue;
            else
                thisXformsRef = "" + parentXformRef + "/"
                    + el.xpath("string(./@nodeset)").stringValue;
        }
    }
	// ------------------------------------------
	
	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, 1);
	
	var repeatDiv = document.createElement("div");
	
	var id = "" + parentId;// + "-repeat";
	// repeatDiv.id = id;
	// ------------------------------------------
	
	// ------------------------------------------
	/**
	 * @type {Node[]}
	 */
	var rowsData = getNodeArrayForInstanceRef(thisXformsRef, null);
	// ------------------------------------------
	
	// ------------------------------------------
	var rowPadding = -1;
    if(ieXmlDom != null) {
        if(ieXpathEvaluate(el, "../rowpadding")[0].text != '') {
            if(parseInt(ieXpathEvaluate(el, "../rowpadding")[0].text) > -2) {
                rowPadding = -2;
            } else {
                rowPadding = parseInt(ieXpathEvaluate(el, "../rowpadding")[0].text);
            }
        }
    } else {
        if (el.xpath("../rowpadding !=''").booleanValue) {
            if (el.xpath("number(../rowpadding) &lt; number(-2)").booleanValue) {
                rowPadding = -2;
            } else {
                rowPadding = el.xpath("number(../rowpadding)").numberValue;
            }
        }
    }
	// ------------------------------------------
	
	// ------------------------------------------
	var currentNumb = 1;// номер первой строки
	/**
	 * @type {Element}
	 */
	var row = null;
	
	/**
	 * 
	 * @type {Bounds}
	 */
	var lastRowBounds = new Bounds(null);
	console.logDraw("rowsData.length  =  " + rowsData.length);
	for (var j = 0; j < rowsData.length; j++) {
		row = rowsData[j];
		var rowInfo = new InfoElement();
		var rowBounds = new Bounds(null);
		var rowDiv = document.createElement("div");
		var idForCurEl = id + "-" + currentNumb;
		var xformsRefForCurEl = thisXformsRef + "[position()='" + currentNumb
		    + "']";
		// ------------------------------------------
		var currentEl = el.firstChild;
		
		while (currentEl && currentEl.tagName && currentEl.tagName.replace(new RegExp('.+:','g'), '') == "label"
		    && currentEl.namespaceURI == "http://www.w3.org/2002/xforms")
			currentEl = currentEl.nextSibling;
		
		while (currentEl)
			{
			
			var thisDrawEl = null;
			if (currentEl.nodeType == Node.ELEMENT_NODE)
				{
//				console.log(currentEl);
//				console.log(idForCurEl);
//				console.log(xformsRefForCurEl);
				switch (currentEl.tagName.replace(/.+:/g, ''))
					{
					case "action" :
						thisDrawEl = drawAction(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "box" :
						thisDrawEl = drawBox(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "button" :
						thisDrawEl = drawButton(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "cell" :
						break;
					case "check" :
						thisDrawEl = drawCheck(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "combobox" :
						thisDrawEl = drawCombobox(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "data" :
						break;
					case "field" :
						thisDrawEl = drawField(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "help" :
						break;
					case "label" :
						thisDrawEl = drawLabel(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "line" :
						thisDrawEl = drawLine(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "list" :
						// drawList(currentEl);
						break;
					case "pane" :
						thisDrawEl = drawPane(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "popup" :
						thisDrawEl = drawPopup(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "radio" :
						thisDrawEl = drawRadio(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "signature" :
						break;
					case "spacer" :
						thisDrawEl = drawSpacer(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					case "table" :
						thisDrawEl = drawTable(currentEl, idForCurEl, xformsRefForCurEl);
						break;
					}
				
				if (thisDrawEl)
					{
					rowDiv.appendChild(thisDrawEl.refHTMLElemDiv);
					var foundInfo = treeInfoEls.getElemInfo("" + idForCurEl + "-"
					    + currentEl.getAttribute("sid"));
					if (rowBounds.x2 < foundInfo.x2)
						rowBounds.x2 = foundInfo.x2;
					if (rowBounds.y2 < foundInfo.y2)
						rowBounds.y2 = foundInfo.y2;
					}
				}
			currentEl = currentEl.nextSibling;
			}
		// ------------------------------------------
		
		// ------------------------------------------
		rowBounds.width = rowBounds.x2;
		rowBounds.height = rowBounds.y2;
		
		if (lastRowBounds.y2 != 0)
			rowBounds.y1 = lastRowBounds.y2 + rowPadding * 2 + 1;
		else
			rowBounds.y1 = 0;
		
		rowBounds.y2 = rowBounds.y1 + rowBounds.height;
		lastRowBounds = rowBounds;
		// ------------------------------------------
		
		// ------------------------------------------
		var styleDiv = "position:absolute;";
		styleDiv = styleDiv + "top:" + rowBounds.y1 + "px;";
		styleDiv = styleDiv + "width:" + rowBounds.width + "px;";
		styleDiv = styleDiv + "height:" + rowBounds.height + "px;";
		styleDiv = styleDiv + "z-index: 10;";
		rowDiv.setAttribute("style", styleDiv);
		rowDiv.id = idForCurEl;
		// ------------------------------------------
		
		// ------------------------------------------
		rowInfo.x1 = 0;
		rowInfo.x2 = rowBounds.x2;
		rowInfo.y1 = rowBounds.y1;
		rowInfo.y2 = rowBounds.y2;
		rowInfo.width = rowBounds.width;
		rowInfo.height = rowBounds.height;
		// ------------------------------------------
		
		// ------------------------------------------
		rowInfo.refElem = el;
		rowInfo.sid = "" + currentEl;
		rowInfo.fullSidRef = "" + idForCurEl;
		rowInfo.parentFullSidRef = "" + id;
		rowInfo.xformRef = row;
		// rowInfo.xformRefForLabel = ?;
		rowInfo.refHTMLElem = rowDiv;
		rowInfo.refHTMLElemDiv = rowDiv;
		rowInfo.typeEl = "row";
		rowInfo.value = null;
		// ------------------------------------------
		
		// ------------------------------------------
		UtilsHTMLEl.setInfoElRef(rowDiv, info);
		// rowInfo.refHTMLElem.setInfoElRef(rowInfo);
		treeInfoEls.addElemInfo(rowInfo);
		// ------------------------------------------
		
		// ------------------------------------------
		if (repeatBounds.x2 < rowBounds.x2)
			repeatBounds.x2 = rowBounds.x2;
		if (repeatBounds.y2 < rowBounds.y2)
			repeatBounds.y2 = rowBounds.y2;
		// ------------------------------------------
		repeatDiv.appendChild(rowDiv);
		currentNumb++;
    }
	// ------------------------------------------
	
	// ------------------------------------------
	repeatBounds.width = repeatBounds.x2;
	repeatBounds.height = repeatBounds.y2;
	if (bounds.width < repeatBounds.width)
		bounds.width = repeatBounds.width;
	if (bounds.height < repeatBounds.height)
		bounds.height = repeatBounds.height;
	bounds.x2 = bounds.x1 + bounds.width;
	bounds.y2 = bounds.y1 + bounds.height;
	// ------------------------------------------
	
	// ------------------------------------------
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:0px;";
	styleDiv = styleDiv + "left:0px;";
	styleDiv = styleDiv + "width:" + bounds.width + "px;";
	styleDiv = styleDiv + "height:" + bounds.height + "px;";
	repeatDiv.setAttribute("style", styleDiv);
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
	info.sid = "repeat";
	info.fullSidRef = "" + id;
	info.parentFullSidRef = "" + parentId;
	info.xformRef = thisXformsRef;
	// info.xformRefForLabel = ?;
	info.refHTMLElem = repeatDiv;
	info.refHTMLElemDiv = repeatDiv;
	info.typeEl = "xform:repeat";
	info.value = null;
	// ------------------------------------------
	
	// ------------------------------------------
	UtilsHTMLEl.setInfoElRef(repeatDiv, info);
	// info.refHTMLElem.setInfoElRef(info);
	treeInfoEls.addElemInfo(info);
	// ------------------------------------------
	
	return repeatDiv;
}

/**
 * @param {InfoElement}
 *          info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordXformRepeat(info)
	{
	
	// ------------------------------------------
	var thisXformsRef = info.xformRef;
	// ------------------------------------------
	
	// ------------------------------------------
	var bounds = findBounds(el, parentId, 1);
	var id = "" + parentId;// + "-group";
	// ------------------------------------------
	
	// ------------------------------------------
	/**
	 * @type {Node[]}
	 */
	var rowsData = getNodeArrayForInstanceRef(thisXformsRef, null);
	// ------------------------------------------
	
	// ------------------------------------------
	var rowPadding = -1;
	if (el.xpath("../rowpadding !=''").booleanValue)
		{
		if (el.xpath("number(../rowpadding) &lt; number(-2)").booleanValue)
			{
			rowPadding = -2;
			}
		else
			{
			rowPadding = el.xpath("number(../rowpadding)").numberValue;
			
			}
		}
	// ------------------------------------------
	
	// ------------------------------------------
	var currentNumb = 1;// номер первой строки
	/**
	 * @type {Element}
	 */
	var row = null;
	
	/**
	 * 
	 * @type {Bounds}
	 */
	var lastRowBounds = new Bounds(null);
	for (row in rowsData)
		{
		var rowBounds = new Bounds(null);
		var idForCurEl = id + "-" + currentNumb;
		var rowInfo = treeInfoEls.getElemInfo("" + idForCurEl);
		var xformsRefForCurEl = thisXformsRef + "[position()='" + currentNumb
		    + "']";
		// ------------------------------------------
		var currentEl = el.firstChild;
		
		if (currentEl.tagName.replace(/.+:/g, '') == "label"
		    && currentEl.namespaceURI == "http://www.w3.org/2002/xforms")
			currentEl = currentEl.nextSibling;
		var currentInfo = null;
		var foundBounds = null;
		while (currentEl)
			{
			foundBounds = null;
			currentInfo = treeInfoEls.getElemInfo("" + idForCurEl + "-"
			    + currentEl.getAttribute("sid"));
			if (currentEl.nodeType == Node.ELEMENT_NODE)
				{
				switch (currentEl.tagName.replace(/.+:/g, ''))
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
					if (rowBounds.x2 < foundBounds.x2)
						rowBounds.x2 = foundBounds.x2;
					if (rowBounds.y2 < foundBounds.y2)
						rowBounds.y2 = foundBounds.y2;
					}
				
				}
			currentEl = currentEl.nextSibling;
			}
		// ------------------------------------------
		
		// ------------------------------------------
		rowBounds.width = rowBounds.x2;
		rowBounds.height = rowBounds.y2;
		if (lastRowBounds.y2 != 0)
			rowBounds.y1 = lastRowBounds.y2 + rowPadding * 2 + 1;
		else
			rowBounds.y1 = 0;
		
		rowBounds.y2 = rowBounds.y1 + rowBounds.height;
		lastRowBounds = rowBounds;
		// ------------------------------------------
		
		// ------------------------------------------
		rowInfo.updateBounds(rowBounds)
		// ------------------------------------------
		
		// ------------------------------------------
		var rowDiv = rowInfo.refHTMLElemDiv;
		rowDiv.style.top = rowInfo.y1;
		rowDiv.style.left = rowInfo.x1;
		rowDiv.style.width = rowInfo.width;
		rowDiv.style.height = rowInfo.height;
		// ------------------------------------------
		
		// ------------------------------------------
		if (repeatBounds.x2 < rowBounds.x2)
			repeatBounds.x2 = rowBounds.x2;
		if (repeatBounds.y2 < rowBounds.y2)
			repeatBounds.y2 = rowBounds.y2;
		// ------------------------------------------
		currentNumb++;
		}
	// ------------------------------------------
	
	// ------------------------------------------
	repeatBounds.width = repeatBounds.x2;
	repeatBounds.height = repeatBounds.y2;
	if (bounds.width < repeatBounds.width)
		bounds.width = repeatBounds.width;
	if (bounds.height < repeatBounds.height)
		bounds.height = repeatBounds.height;
	bounds.x2 = bounds.x1 + bounds.width;
	bounds.y2 = bounds.y1 + bounds.height;
	// ------------------------------------------
	
	// ------------------------------------------
	info.updateBounds(bounds);
	// ------------------------------------------
	
	// ------------------------------------------
	var div = info.refHTMLElemDiv;
	div.style.top = info.y1;
	div.style.left = info.x1;
	div.style.width = info.width;
	div.style.height = info.height;
	// ------------------------------------------
	
	return bounds;
	}
