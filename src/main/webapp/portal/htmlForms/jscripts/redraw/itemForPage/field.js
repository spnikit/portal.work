/**
 * @include "../../includes.js"
 */

/**
 * @param {Element}
 *          el - отрисовываемый элемент
 * @param {String}
 *          parentId - родительский id или <b>null</b> если его нет
 * @param {String}
 *          parentXformRef - родительская xform-ссылка или <b>null</b> если её
 *          нет
 * 
 * @return {InfoElement} - полученный элемент
 */

function drawField(el, parentId, parentXformRef)
	{
	var info = new InfoElement();
	info.refElem = el;
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;
	info.typeEl = "field";
	/**
	 * @type {HTMLElement}
	 */
	var elemHTML = document.createElement("textarea");
	elemHTML.setAttribute("class", "defaultFieldItem noBorder");
	var pattern = '';
	var value = "";
	var xformValue = "";
	
	var fontColor = "black";
	
	var bounds = findBounds(el, parentId, 2);
	info.bounds = bounds;
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	bounds = applyCommonUpdates(bounds, el);
	
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	var elemHTMLDiv = document.createElement("div");
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	
	styleDiv = styleDiv + "height:" + bounds.height + "px;";
	styleDiv = styleDiv + "min-height:" + "17" + "px;";
	styleDiv = styleDiv + "z-index: 10;";
	
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	
	elemHTML.id = info.fullSidRef;
	// console.log("sid = " + field.id);
	var style = "";
	var isBorder = true;
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
	
	// альтернативный и более правильный метод перебора элементов чем через xpath
	childEl = el.firstChild;
	while (childEl) // сначала переберем xforms-узлы
		{
		if (childEl.nodeType == Node.ELEMENT_NODE
		    && childEl.namespaceURI == getNameSpaceForXfdlForm("xforms"))
			{
			parseXFormsForScript(childEl, null, null, info.sid);
            var name = childEl.tagName.replace(new RegExp('.+:','g'), '');
			if (name == "input" || name == "textarea")
				{
				/**
				 * @type {String}
				 */
				var path = childEl.getAttribute("ref");
				path = generateRealXRef(path, parentXformRef);
				info.xformRef = path;
				var tmpArr = getNodeArrayForInstanceRef(path);
				if (tmpArr)
					{
					info.xformNode = tmpArr[0];

                    // TODO сделать поддержку в IE
					/*if (info.xformNode.isSigner())
						{
						info.SignerOptions["value"] = true;
						}*/
					
					// TODO смотри определение значения после поиска шаблона
					// info.hiddenTeg['value'] = tmpArr[0];
					xformValue = ieXmlDom != null ? tmpArr[0].text : tmpArr[0].getValue();
					}
				else
					// TODO это плохо
					value = "";
				if (childEl.tagName.replace(/.+:/g, '') == "textarea")
					{
					// TODO максимальная высота доделать
					// field.style.maxHeight = fontSize + 10;
					}
					
//				if (childEl.childElementCount>0){
//					var sub = childEl.firstChild;
//					while (sub){
//						if (sub.nodeType == Node.ELEMENT_NODE
//		    				&& sub.namespaceURI == getNameSpaceForXfdlForm("xforms"))
//		    				{
//		    					if (sub.localName == "setvalue" || sub.localName == "send"){
//		    						var xfscript = new xFormsScript(sub.attributes, sub.localName);
//		    						addElemToXFormsScriptMass(childEl.getAttribute("ref"),xfscript);
//		    						if (childEl.getAttribute("ref")!=info.sid) {
//		    							addElemToXFormsScriptMass(info.sid,xfscript);
//		    						}
//		    					}
//		    				}
//						sub = sub.nextSibling;
//					}
//				}
				
				break;
				}
			
			}
		childEl = childEl.nextSibling;
		}
	// теперь переберем остальные-узлы
	/**
	 * информации о используемом шрифте
	 * 
	 * @type FontInfoObj
	 */
	var fontInfo;
	childEl = el.firstChild;
	
	while (childEl)// альтернативный и более правильный метод перебора
	// элементов
	// чем через xpath
		{
		if (childEl.nodeType == Node.ELEMENT_NODE) {
            if(childEl.tagName) {
                lNameChildEl = childEl.tagName.replace(/.+:/g, '');
            } else {
                lNameChildEl = null;
            }
            valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl"))
				{

				switch (lNameChildEl)
					{
					// case "value" :
					// info.hiddenTeg['value'] = childEl;
					// value = childEl.getValue();

					// break;
					case 'readonly' :
						if (valChildEl == 'on'){
							elemHTML.setAttribute('readonly', 'readonly');
						}
						break;
					case 'border' :
						if (valChildEl == 'off'){
							style = style + "border-style: none;";
							isBorder = false;
						} else
							style = style + "border-style: inset;";
						break
					case 'visible' :
						if (valChildEl == 'off')
							style = style + "visibility: hidden;";
						else
							style = style + "visibility: visible;";
						break;
					case 'printvisible' :
						if (valChildEl == 'off')
							elemHTMLDiv.setAttribute("class", "noPrint");
						else
							elemHTMLDiv.setAttribute("class", "forcePrint");
						break;
					case 'justify' :
						info.justify = valChildEl;
						break;
					case 'active' :
						if (valChildEl == 'off'){
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
					// TODO доделать
					case 'scrollvert' :// это может быть только в field

						break;
					case 'scrollvert' :// это может быть только в field

						break;

					case 'itemlocation' :

						// TODO доделать
						break;
					case 'value' :
						if (!info.xformNode)
							{
							if (childEl.isSigner())
								{
								info.SignerOptions["value"] = true;
								}
							value = ieXmlDom != null ? childEl.text : childEl.getValue();
							break;
							}

						break;
					}
				var attrScript = childEl.getAttribute('compute');
				if (attrScript)
					{
					/**
					 * @type EmulateNode|Element
					 */
					var emNode;
					if (info.xformNode)
						{
						emNode = new EmulateNode(lNameChildEl, valChildEl,
						    childEl.parentNode);// так как есть xform-связь
						info.hiddenTeg[lNameChildEl] = emNode;
						}
					else
						emNode = childEl;
					var scriptCompute = new UnitScript(attrScript, info, emNode);
					massAllScripts.push(scriptCompute);
					}

				}
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("custom"))
				{
				// console.info(childEl.namespaceURI);
				// console.info(getNameSpaceForXfdlForm("custom"));

                var attrScript;
                if(ieXmlDom != null) {
                    var attr = null;
                    var i = childEl.attributes.length;
                    while (i--) {
                        var node = childEl.attributes[i];
                        if (node.localName == 'compute'
                            && node.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {
                            attr = node;
                            break;
                        }
                    }
                    attrScript = attr && attr.value || '';
                } else {
                    attrScript = childEl.getAttributeNS(getNameSpaceForXfdlForm("xfdl"), 'compute');
                }


				// console.info(attrScript);
				if (attrScript)
					{

					/**
					 * @type EmulateNode|Element
					 */
					var emNode;
					if (info.xformNode)
						{
						emNode = new EmulateNode('custom:' + lNameChildEl, valChildEl,
						    childEl.parentNode);// так как есть xform-связь
						info.hiddenTeg['custom:' + lNameChildEl] = emNode;
						// console.info("info.hiddenTeg[custom:" + lNameChildEl + "] = " +
						// info.hiddenTeg['custom:' + lNameChildEl]);
						}
					else
						emNode = childEl;
					var scriptCompute = new UnitScript(attrScript, info, emNode);
					massAllScripts.push(scriptCompute);
					}
				}

			}
		childEl = childEl.nextSibling;
		}
	
	if (!isGlobalEdit)
		elemHTML.setAttribute('readonly', 'readonly');
	
	if (!info.fontInfoObj)// создаем дефолтный объект если не было тэга <fontinfo>
		info.fontInfoObj = new FontInfoObj();
	if (!info.formatObj)// создаем дефолтный объект если не было тэга <format>
		info.formatObj = new FormatObj();
	
	// --------------------------------------------------------------------
	// --------------------------------------------------------------------
	
	// style = style + "z-index: 10";
	
	style += info.fontInfoObj.getCssStyle();
	style = style + "color:" + fontColor + ";";
	style += "resize:none;";
	style = style + "min-height:"
	    + (objStateDrawing.convertFontSize[info.fontInfoObj.fontSize] + 5)
	    + "px;";
	
	// TODO сделать без
	// el.xpath("./xfdl:itemlocation/xfdl:width").iterateNext()==null
	var widthElem;
    if (ieXmlDom != null) {
        widthElem = ieXpathEvaluate(el, "./itemlocation/width")[0];
    } else {
        widthElem = el.xpath("./xfdl:itemlocation/xfdl:width").iterateNext();
    }

    if (bounds.width == -4
	    && widthElem == null)// если
	// ширина
	// не
	// определена
	// то
	// ставим
	// дефолтную
	// в 30
	// символов
		{
		dopDivSize.setAttribute('style', style);
		
		// как показала практика непонятно какой символ брать за эквивалент, поэтому
		// берем 29 символово 'w'
		//dopDivSize.setValue("wwwwwwwwwwwwwwwwwwwwwwwwwwwww");
		dopDivSize.innerHTML = "wwwwwwwwwwwwwwwwwwwwwwwwwwwww";
		
		// на всякий случай добавляем 0.6, так как обнаружить дробную часть
		// невозможно
		bounds.width = 1 * (dopDivSize.offsetWidth + 0.6);
		bounds.x2 = bounds.x1 + bounds.width;
		dopDivSize.removeAttribute('style');
		}
	style = style + "height:" + bounds.height + "px;";
	style += "width:" + (isBorder ? bounds.width - 2 : bounds.width) + "px;";
	
	switch (info.justify)
		{
		case "center" :
			style += "text-align: center;";
			break;
		case "left" :
			style += "text-align: left;";
			break;
		case "right" :
			style += "text-align: right;";
			break;
		case "lead" :// работает только в CSS3
			style += "text-align: start;";
			break;
		case "trial" :// работает только в CSS3
			style += "text-align: end;";
			break;
		default :// "left"
		}
	
	elemHTML.setAttribute("style", style);
	styleDiv = styleDiv + "width:" + bounds.width + "px;";
	elemHTMLDiv.setAttribute("style", styleDiv);
	// console.log('1)value = '+value);
	if (info.xformNode)
		{
		value = formatValueFromXform(xformValue, info.formatObj.datatype,
		    info.formatObj.prSetting.pattern);
		var emNode = new EmulateNode('value', value, el, info.xformNode);
		info.hiddenTeg['value'] = emNode;
        if(ieXmlDom != null) {
            //info.xformNode.addXformLink(info, 'value', emNode);
        } else {
            info.xformNode.addXformLink(info, 'value', emNode);
        }
		}
	else
		{
		
		}
	// console.log('2)value = '+value);
	elemHTML.value = value;
	info.value = value;
	
	if (info.SignerOptions['value'])
		{
		info.isSigner = true;
		elemHTML.setAttribute("readonly", false);
		elemHTMLDiv.setAttribute("title",
		    "У этого элемента есть цифровая подпись, поэтому его нельзя изменять");
		}
	
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
    if(ieXmlDom != null) {
        var itemLocHeight = ieXpathEvaluate(el, "itemlocation/height");
        var sizeHeight = ieXpathEvaluate(el, "size/height");
        var value = null;
        if(itemLocHeight.length > 0) {
            value = parseInt(itemLocHeight[0].text);
        } else if(sizeHeight.length > 0) {
            value = parseInt(sizeHeight[0].text);
        }
        if(value != null) {
            elemHTML.setAttribute("height", value);
        }
        var width = ieXpathEvaluate(el, "size/width");
        if(width.length > 0)
            elemHTML.setAttribute("cols", parseInt(width[0].text));
    } else {
        if (el
            .xpath("count(xfdl:itemlocation/xfdl:height  | xfdl:size/xfdl:height)>0").booleanValue)
            elemHTML
                .setAttribute(
                    "height",
                    el
                        .xpath("number(xfdl:itemlocation/xfdl:height  | xfdl:size/xfdl:height)").numberValue);

        if (el.xpath("count(xfdl:size/xfdl:width)>0").booleanValue)
            elemHTML.setAttribute("cols",
                el.xpath("number(xfdl:size/xfdl:width)").numberValue);
    }
	
	/*
	 * if (el.xpath("count(xfdl:size/xfdl:height)>0").booleanValue)
	 * field.setAttribute("rows",
	 * el.xpath("number(xfdl:size/xfdl:height)").numberValue);
	 * 
	 * if (el.xpath("count(xfdl:size/xfdl:width)>0").booleanValue)
	 * field.setAttribute("cols",
	 * el.xpath("number(xfdl:size/xfdl:width)").numberValue);
	 */

	// --------------------------------------------------------------------
	// --------------------------------------------------------------------
	elemHTMLDiv.appendChild(elemHTML);
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	bounds = cancelCommonUpdates(bounds, el);
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	info.x1 = bounds.x1;
	info.x2 = bounds.x2;
	info.y1 = bounds.y1;
	info.y2 = bounds.y2;
	info.width = bounds.width;
	info.height = bounds.height;
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	// info.xformRef = ?;
	// info.xformRefForLabel = ?;
	info.refHTMLElem = elemHTML;
	info.refHTMLElemDiv = elemHTMLDiv;
	
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	if (elemHTML.height < 17)
		{
		info.height = 17;
		field.style.height = 17;
		}
	// --------------------------------------------------------------------
	
	// --------------------------------------------------------------------
	treeInfoEls.addElemInfo(info);
	UtilsHTMLEl.setInfoElRef(elemHTML, info);
	// info.refHTMLElem.setInfoElRef(info);
	// --------------------------------------------------------------------
	
	var checkD;
	var messageD;
	var mandatoryD;
	var lengthD;
	var patternsD;
	var rangeD;
	var templateD;
	// ------------------------------------------
	info.typeEl = "field";
	
	info.setBackGroundForInvalid(value);
	
	return info;
	};
/**
 * @param {InfoElement}
 *          info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordButton(info)
	{
	
	// ------------------------------------------
	var bounds = findBounds(info.refElem, info.parentFullSidRef, 1);
	info.updateBounds(bounds);
	// ------------------------------------------
	
	// ------------------------------------------
	bounds = applyCommonUpdates(bounds, info.refElem);
	// ------------------------------------------
	
	// ------------------------------------------
	var div = info.refHTMLElemDiv;
	div.style.top = info.y1;
	div.style.left = info.x1;
	div.style.width = info.width;
	div.style.height = info.height;
	// ------------------------------------------
	
	// ------------------------------------------
	var htmlEl = info.refHTMLElem;
	htmlEl.style.width = info.width;
	htmlEl.style.height = info.height;
	// ------------------------------------------
	
	// ------------------------------------------
	bounds = cancelCommonUpdates(bounds, info.refElem);
	// ------------------------------------------
	
	return bounds;
	}
