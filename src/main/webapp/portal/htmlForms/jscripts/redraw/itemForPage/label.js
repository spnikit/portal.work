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
function drawLabel(el, parentId, parentXformRef) {
	// console.log(el);
	console.traceLabel = console.trace;
	/**
	 * @type InfoElement
	 */
	var info = new InfoElement();
	
	info.sid = el.getAttribute("sid");
	/**
	 * @type String
	 */
	// var stylePre = "";
	var bounds = findBounds(el, parentId, null);
	info.bounds = bounds;
	bounds = applyCommonUpdates(bounds, el);
	// console.log('bounds.x1 = ' + bounds.x1);
	
	/**
	 * информации о используемом шрифте
	 * 
	 * @type FontInfoObj
	 */
	var fontInfo;

	// ------------------------------------------

	// ------------------------------------------
	/**
	 * @type HTMLElement
	 */
	var elemHTMLDiv = document.createElement("div");
	/**
	 * @type HTMLElement|HTMLDivElement|HTMLImageElement
	 */
	var elemHTML = null;
	/**
	 * @type String
	 */
	var style = "white-space:pre-wrap;line-height:1.6;";// для пробелов и новых
	// строк

	var styleDiv = "position:absolute;";
	styleDiv += "top:" + bounds.y1 + "px;";
	styleDiv += "left:" + bounds.x1 + "px;";
	styleDiv += "z-index: 10;";

	// ------------------------------------------
	var disabled = false;
	var fontColor = "black";
	
//	styleDiv = styleDiv + "display: table; @maedia print { white-space:pre;}";
	
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
	while (childEl) // сначала переберем xforms-узлы
	{
		if (childEl.nodeType == Node.ELEMENT_NODE
				&& childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")) {
			if (childEl.tagName.replace(new RegExp('.+:','g'), '') == "output") {
				/**
				 * @type {String}
				 */
				var path = childEl.getAttribute("ref");

				if (path) {
					path = generateRealXRef(path, parentXformRef);
					info.xformRef = path;
					var tmpArr = getNodeArrayForInstanceRef(path);
					if (tmpArr) {
						info.xformNode = tmpArr[0];
						// TODO смотри определение значения после поиска шаблона
						// info.hiddenTeg['value'] = tmpArr[0];
						if(ieXmlDom != null)
                            value = tmpArr[0].text;
                        else
                            value = tmpArr[0].getValue();
					}
				} else {
                    if(ieXmlDom != null) {
                        value = ieXpathEvaluate(childEl, './xforms:label')[0].text;
                    } else
					    value = childEl.xpath("string(./xforms:label)").stringValue;
                }
				break;
			}

		}
		childEl = childEl.nextSibling;
	}
	// теперь переберем остальные-узлы

	childEl = el.firstChild;

	while (childEl)// альтернативный и более правильный метод перебора
	// элементов
	// чем через xpath
	{
		if (childEl.nodeType == Node.ELEMENT_NODE) {
            lNameChildEl = childEl.tagName.replace(new RegExp('.+:','g'), '');
            if(ieXmlDom != null)
			    valChildEl = childEl.text;
            else
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

					case 'active' :
						if (valChildEl == 'off') {
							disabled = true;
						}
						break;
					case 'format' :
						info.formatObj = parseFormat(childEl);
						// TODO доделать
						break;
					case 'fontinfo' :
						info.fontInfoObj = parseFontInfo(childEl);
						break;
					case 'fontcolor' :
						fontColor = ieXmlDom != null ? childEl.text : childEl.getValue();
						break;
					case 'justify' :
						info.justify = valChildEl;
						break;
					case 'image' :
						info.imageRef = valChildEl;
						break;

					case 'itemlocation' :
						// TODO доделать
						break;
					case 'value' :// в лэйблах xform-значение не перекравывает встроенное
						{
						if (!info.xformNode) {
                            value = ieXmlDom != null ? childEl.text : childEl.getValue();
                        }
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
	if (!info.fontInfoObj)// создаем дефолтный объект если не было тэга
							// <fontinfo>
		info.fontInfoObj = new FontInfoObj();
	if (!info.formatObj)// создаем дефолтный объект если не было тэга <format>
		info.formatObj = new FormatObj();

	style += info.fontInfoObj.getCssStyle();

	style += "color:" + fontColor + ";";

	if (info.imageRef == null) {
		elemHTML = document.createElement("div");
		elemHTML.setAttribute("class", "defaulLabelItem");
		elemHTML.id = "" + parentId + "-" + el.getAttribute("sid");

		if (disabled) elemHTML.setAttribute("disabled", "disabled");

		if (bounds.width == 0) {
			dopDivSize.setAttribute('style', info.fontInfoObj.getCssStyle());

            // делаем value=" ", иначе Google Chrome и IE приравнивают offsetHeight к нулю, из-за чего следующий элемент может наложиться на текущий
            if(value == null || value == "")
                value = " ";
			UtilsHTMLEl.setValue(dopDivSize, value);
			// dopDivSize.setValue(value);
			bounds.width = 1 * (dopDivSize.offsetWidth + 10.6);// на всякий
																// случай
			// добавляем 0.6, так
			// как обнаружить
			// дробную часть
			// невозможно
			bounds.x2 = bounds.x1 + bounds.width;
			dopDivSize.removeAttribute('style');
		}
		if (bounds.height == 0) {
			dopDivSize.setAttribute('style', info.fontInfoObj.getCssStyle()
							+ ";width:" + bounds.width + "px;"
							+ "white-space:pre-wrap;");

            if(value == null || value == "")
                value = " ";
			UtilsHTMLEl.setValue(dopDivSize, value);
			// dopDivSize.setValue(value);

			bounds.height = 1 * (dopDivSize.offsetHeight + 0.6);// на всякий
																// случай
			// добавляем 0.6, так
			// как обнаружить
			// дробную часть
			// невозможно

			bounds.y2 = bounds.y1 + bounds.height;
			dopDivSize.removeAttribute('style');
		}
	} else {
		elemHTML = document.createElement("img");
		if (disabled) elemHTML.setAttribute("disabled", "disabled");
		massLabelImage.push(info);
		stackForActionsAfterRedraw.push(function() {
					info.updateImage();
				});

	}
	
	if (bounds.height < 22 && info.imageRef == null){
		bounds.height = 22;
	}
	
	styleDiv += "height:" + bounds.height + "px;";
	styleDiv += "width:" + bounds.width + "px;";
	
	elemHTMLDiv.setAttribute("style", styleDiv);

	switch (info.justify) {
		case "center" :
			style += "display: table-cell;";
			style += "vertical-align: middle;";
            style += "width: " + bounds.width + "px;";
			break;
		case "left" :
			style += "text-align: left;";

			break;
		case "right" :
			break;
		case "lead" :
			break;
		case "trial" :
			break;
		default :// "left"
	}
	
	elemHTML.setAttribute("style", style);
	if (!info.imageRef)
		UtilsHTMLEl.setValue(elemHTML, value);

	// elemHTML.setValue(value);
	// console.infoDraw("value = " + value);
	elemHTMLDiv.appendChild(elemHTML);

	bounds = cancelCommonUpdates(bounds, el);
	
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
	info.refHTMLElem = elemHTML;
	info.refHTMLElemDiv = elemHTMLDiv;
	info.typeEl = "label";
	info.value = value;
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);
	UtilsHTMLEl.setInfoElRef(elemHTML, info);
	// /info.refHTMLElem.setInfoElRef(info);
	// ------------------------------------------

	console.traceLabel = null;

	return info;
}

/**
 * @param {String}
 *            e
 */
function replSym(e) {
	var retExp = "";
	switch (e) {
		case "\r\n" :
			retExp = "<br/>";
			break;
		case "\n" :
			retExp = "<br/>";
			break;
		case "\r" :
			retExp = "<br/>";
			break;
		case "\t" :
			retExp = "&#x9;";
			break;
		case " " :
			retExp = "&#x20;";
			break;
		default :
			// retExp = "<br/>";
	}
	return retExp;
}

/**
 * <<<<<<< .mine
 * 
 * @param {String}
 *            input - входная строка
 * @return {String[]} - полученный массив строк =======
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты >>>>>>> .r8772
 */
function findCoordLabel(info) {
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, 1);
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
