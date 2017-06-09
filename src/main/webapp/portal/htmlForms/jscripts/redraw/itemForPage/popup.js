/**
 * @include "../../includes.js"
 */

/**
 * @param {Element}
 *            el - отрисовываемый элемент
 * @param {String}
 *            parentId - родительский id или <b>null</b> если его нет
 * @param {String}
 *            parentXformRef - родительская xform-ссылка или <b>null</b> если
 *            её нет
 * 
 * @return {InfoElement} - полученный элемент
 */

function drawPopup(el, parentId, parentXformRef) {
	// console.log(el);
	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, 1);
	info.bounds = bounds;
	bounds = applyCommonUpdates(bounds, el);
	// ------------------------------------------

	// ------------------------------------------
	var popupDiv = document.createElement("div");
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	styleDiv = styleDiv + "width:" + bounds.width + "px;";
	styleDiv = styleDiv + "height:" + bounds.height + "px;";
	styleDiv = styleDiv + "z-index: 10;";

	popupDiv.setAttribute("style", styleDiv);
	// ------------------------------------------

	// ------------------------------------------
	var popup = document.createElement("select");
	UtilsHTMLEl.setInfoElRef(popup, info);

	popup.id = "" + parentId + "-" + el.getAttribute("sid");
	var style = "";

	if (el.xpath("xfdl:readonly = 'on'").booleanValue)
		popup.setAttribute('readonly', 'readonly');

	if (el.xpath("xfdl:border = 'off'").booleanValue)
		style = style + "border-style: none;";
	else
		style = style + "border-style: inset;";

	if (el.xpath("xfdl:visible = 'off'").booleanValue)
		style = style + "visibility: hidden;";
	else
		style = style + "visibility: visible;";

	if (el.xpath("xfdl:active = 'off'").booleanValue)
		popup.setAttribute("disabled", "disabled");

	style = style + "width:" + bounds.width + "px;";
	style = style + "height:" + bounds.height + "px;";

	popup.setAttribute("style", style);
	// ------------------------------------------

	// ------------------------------------------
	var value = "";
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

	while (childEl) { // сначала переберем xforms-узлы

		if (childEl.nodeType == Node.ELEMENT_NODE
				&& childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")) {
			if (childEl.tagName.replace(new RegExp('.+:','g'), '') == "select1") {

				var path = childEl.getAttribute("ref");
				path = generateRealXRef(path, parentXformRef);
				info.xformRef = path;

				var tmpArr = getNodeArrayForInstanceRef(path);
				if (tmpArr) {
					info.xformNode = tmpArr[0];
					// TODO смотри определение значения после поиска шаблона
					// info.hiddenTeg['value'] = tmpArr[0];
					xformValue = ieXmlDom != null ? tmpArr[0].text : tmpArr[0].getValue();
					if (info.xformNode.isSigner()) {
						info.SignerOptions["value"] = true;
					}
				} else
					// TODO это плохо
					value = "";

				var pathItemSet;

				var pathItemSetValue;

				var pathItemSetLabel;

				// TODO начало опорной точки 1

				pathItemSet = childEl
						.xpath("string(./xforms:itemset/@nodeset)").stringValue;
				console.info(path);
				console.info(pathItemSet);
				pathItemSet = generateRealXRef(pathItemSet, path).trim();
				pathItemSetValue = childEl
						.xpath("string(./xforms:itemset/xforms:value/@ref)").stringValue
						.trim();
				pathItemSetLabel = childEl
						.xpath("string(./xforms:itemset/xforms:label/@ref)").stringValue
						.trim();

				// TODO конец опорной точки 1

				var itemSet;

				// TODO начало опорной точки 2
				itemSet = getNodeArrayForInstanceRef(pathItemSet);
				if (itemSet) {
					for (var i = 0; i < itemSet.length; i++) {
                        var valueOpt;
                        if(ieXmlDom != null)
                            valueOpt = pathItemSetValue != ""
                                ? ieXpathEvaluate(itemSet[i], pathItemSetValue)[0].text
                                : itemSet[i].text;
                        else
						    valueOpt = pathItemSetValue != ""
								? itemSet[i].xpath("string(" + pathItemSetValue
										+ ")").stringValue
								: itemSet[i].getValue();

                        var labelOpt;
                        if(ieXmlDom != null)
                            labelOpt = pathItemSetLabel != ""
                                ? ieXpathEvaluate(itemSet[i], pathItemSetLabel)[0].text
                                : itemSet[i].text;
                        else
						    labelOpt = pathItemSetLabel != ""
								? itemSet[i].xpath("string(" + pathItemSetLabel
										+ ")").stringValue
								: itemSet[i].getValue();
						if (labelOpt == "")
							labelOpt = valueOpt;

						var opt = document.createElement("option");
						opt.setAttribute("value", valueOpt);
						if (value && value == valueOpt) {
							opt.setAttribute("select", "select");
						}
						UtilsHTMLEl.setValue(opt, labelOpt);
						popup.appendChild(opt);
					}
				} else {
					// пока неясно
				}
				// TODO конец опорной точки 2
				break;
			}
		}
		childEl = childEl.nextSibling;
	}

	// ------------------------------------------

	// ------------------------------------------
	// теперь переберем остальные-узлы

	childEl = el.firstChild;

	while (childEl)// альтернативный и более правильный метод перебора
	// элементов
	// чем через xpath
	{
		if (childEl.nodeType == Node.ELEMENT_NODE) {
			lNameChildEl = childEl.tagName.replace(new RegExp('.+:', 'g'), '');
			valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {
				switch (lNameChildEl) {
					case 'group' : {
						var popupGroup = ieXmlDom != null ? childEl.text : childEl.getValue();

						var cellSet = el.xpath("../xfdl:cell[./xfdl:group='"
								+ popupGroup + "']");
						var cell = cellSet.iterateNext();

						while (cell) {
							if (cell.nodeType == Node.ELEMENT_NODE) {
								var opt = document.createElement("option");

								var valueOpt = cell.xpath("string(./xfdl:value)").stringValue;

								var labelOpt = cell.xpath("string(./xfdl:label)").stringValue;
										
								var typeOpt = cell.xpath("string(./xfdl:type)").stringValue;
								if (typeOpt=='pagedone'){
									var url = cell.xpath("string(./xfdl:url)").stringValue;
									opt.setAttribute("value", typeOpt+'.'+url);
									if (url=='#'+parentId+'.global'){
										opt.setAttribute("selected", "selected");
									}
								}

								if (labelOpt == "")
									labelOpt = valueOpt;

								opt.setAttribute("label", labelOpt);
								UtilsHTMLEl.setValue(opt, valueOpt);

								if (cell.xpath("xfdl:active = 'off'").booleanValue
										|| !isGlobalEdit)
									opt.setAttribute("disabled", "disabled");

								if (value == valueOpt)
									opt.setAttribute("selected", "selected");
									
								console.log(typeOpt);
								popup.appendChild(opt);
								
								$(popup).on("click", function(e) {});
							}
							cell = cellSet.iterateNext();
						}
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
								valChildEl, childEl.parentNode);// так как
						// есть
						// xform-связь
						info.hiddenTeg['custom:' + lNameChildEl] = emNode;
						// console.info("info.hiddenTeg[custom:" +
						// lNameChildEl
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
	// ------------------------------------------

	// ------------------------------------------
	if (el.xpath("count(xfdl:size/xfdl:height)>0").booleanValue)
		popup.setAttribute("rows",
				el.xpath("number(xfdl:size/xfdl:height)").numberValue);

	if (el.xpath("count(xfdl:size/xfdl:width)>0").booleanValue)
		popup.setAttribute("cols",
				el.xpath("number(xfdl:size/xfdl:width)").numberValue);

	// console.log(serial.serializeToString(popup));
	popupDiv.appendChild(popup);
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
	info.refHTMLElem = popup;
	info.refHTMLElemDiv = popupDiv;
	info.typeEl = "popup";
	info.value = value;
	// ------------------------------------------

	// ------------------------------------------
	if (popup.height < 22) {
		info.height = 22;
		popup.style.height = 22;
	}
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);

	// ------------------------------------------

	// ------------------------------------------
	return info;
};

/**
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordPopup(info) {
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, 1);
	// ------------------------------------------

	// ------------------------------------------
	if (bounds.height < 22)
		bounds.height = 22;
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
	var htmlEl = info.refHTMLElem;
	htmlEl.style.width = info.width + "px";
	htmlEl.style.height = info.height + "px";
	// ------------------------------------------

	// ------------------------------------------
	bounds = cancelCommonUpdates(bounds, info.refElem);
	// ------------------------------------------

	return bounds;
}
