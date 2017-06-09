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
function drawCheck(el, parentId, parentXformRef) {
	// console.log(el);

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
	// check.setInfoElRef(info);

	check.id = "" + parentId + "-" + el.getAttribute("sid");
	var style = "";

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

	// альтернативный и более правильный метод перебора элементов чем через
	// xpath
	childEl = el.firstChild;
	while (childEl) // сначала переберем xforms-узлы
	{
		if (childEl.nodeType == Node.ELEMENT_NODE
				&& childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")) {
			if (childEl.tagName.replace(new RegExp('.+:','g'), '') == "input") {
				/**
				 * @type {String}
				 */
				var path = childEl.getAttribute("ref");

				path = generateRealXRef(path, parentXformRef);
				info.xformRef = path;
				var tmpArr = getNodeArrayForInstanceRef(path);
				if (tmpArr) {
					info.xformNode = tmpArr[0];

					if (info.xformNode.isSigner()) {
						info.SignerOptions["value"] = true;
					}

					// TODO смотри определение значения после поиска шаблона
					// info.hiddenTeg['value'] = tmpArr[0];
					xformValue = ieXmlDom != null ? tmpArr[0].text : tmpArr[0].getValue();
				} else
					// TODO это плохо
					value = "";
				break;
			}

		}
		childEl = childEl.nextSibling;
	}

	while (childEl)// альтернативный и более правильный метод перебора
	// элементов
	// чем через xpath
	{
		if (childEl.nodeType == Node.ELEMENT_NODE) {
			lNameChildEl = childEl.tagName.replace(/.+:/g, '');
			valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {

				switch (lNameChildEl) {
					// case "value" :
					// info.hiddenTeg['value'] = childEl;
					// value = childEl.getValue();

					// break;
					case 'readonly' :
						if (valChildEl == 'on') {
							elemHTML.setAttribute('readonly', 'readonly');
						}
						break;
					case 'border' :
						if (valChildEl == 'off')
							style = style + "border-style: none;";
						else
							style = style + "border-style: inset;";
						break
					case 'visible' :
						if (valChildEl == 'off')
							style = style + "visibility: hidden;";
						else
							style = style + "visibility: visible;";
						break;
					case 'active' :
						if (valChildEl == 'off') {
							elemHTML.setAttribute("disabled", "disabled");
						}
						break;
					case 'format' :
						info.formatObj = parseFormat(childEl);
						break;
					case 'fontinfo' :
						info.fontInfoObj = parseFontInfo(childEl);
						break;
					case 'fontcolor' :
						fontColor = ieXmlDom != null ? childEl.text : childEl.getValue();
						break;
					case 'itemlocation' :

						// TODO доделать
						break;
					case 'value' :
						if (!info.xformNode) {
							if (childEl.isSigner()) {
								info.SignerOptions["value"] = true;
							}
							value = ieXmlDom != null ? childEl.text : childEl.getValue();
							break;
						}

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
								childEl.parentNode);// так как есть xform-связь
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
						// console.info("info.hiddenTeg[custom:" + lNameChildEl
						// + "] = " +
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
	// if (!isGlobalEdit)
	// elemHTML.setAttribute("disabled", "disabled");
	// if (!info.formatObj)// создаем дефолтный объект если не было тэга
	// <format>
	// info.formatObj = new FormatObj();
	
	check.setAttribute("style", style);
	check.setAttribute("type", "checkbox");
	
	if (el.getAttribute("active") == "off")
		check.setAttribute("disabled", "disabled");

	if (info.xformNode) {
		value = formatValueFromXform(xformValue, 'check');
		var emNode = new EmulateNode('value', value, el, info.xformNode);
		info.hiddenTeg['value'] = emNode;
		info.xformNode.addXformLink(info, 'value', emNode);
	} else {

	}

	if (value == "on") {
		check.setAttribute("checked", "checked");
	}
	if (el.getElementsByTagName("label").length != 0) {
		checkDiv.innerHTML = ""
				+ getNodeValue(el.getElementsByTagName("label").item(0))
				+ "<br />";
	} else {
        if (ieXmlDom != null) {
            var nodes = ieXpathEvaluate(el, "xforms:input/xforms:label");
            if (nodes.length > 0) {
                if(nodes[0].text != "") {
                    checkDiv.innerHTML = ""
                        + nodes[0].text
                        + "<br />";
                }
            }
        } else {
            if (el.xpath("count(xforms:input/xforms:label)>0").booleanValue) {
                if (el.xpath("string(xforms:input/xforms:label)").stringValue != "") {
                    checkDiv.innerHTML = ""
                        + el.xpath("string(xforms:input/xforms:label)").stringValue
                        + "<br />";
                }
            }
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
	info.value = value;
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
function findCoordCheck(info) {
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
