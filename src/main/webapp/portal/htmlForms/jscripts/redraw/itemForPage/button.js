/**
 * @include "../main.js"
 * @include "../../includes.js"
 */

/**
 * @param {Element|Node}
 *            el - отрисовываемый элемент
 * @param {String}
 *            parentId - родительский sid
 * @param {String}
 *            parentXformRef - родительский xformRef
 * @return {InfoElement} - полученный элемент
 */
function drawButton(el, parentId, parentXformRef) {
	// console.log(el);

	// ------------------------------------------
	var info = new InfoElement();
	/*var bounds = emptyCoordEl();
	var size = emptySizeEl();*/
	var bounds = findBounds(el, parentId, 1);
	info.bounds = bounds;
	bounds = applyCommonUpdates(bounds, el);
	var value = "";
	// ------------------------------------------
	
	// ------------------------------------------
	var elemHTMLDiv = document.createElement("div");
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	if (bounds.width != 0)
		styleDiv = styleDiv + "width:" + bounds.width + "px;";
	styleDiv = styleDiv + "height:" + bounds.height + "px;";
	styleDiv = styleDiv + "z-index: 10;";
	styleDiv += "min-height: 22px;";
	
	elemHTMLDiv.setAttribute("style", styleDiv);
	
	var sidSignature = null;
	/**
	 * 
	 * @type String
	 */
	var url = null;
	// ------------------------------------------
	
	// ------------------------------------------
	/**
	 * 
	 */
	var elemHTML = document.createElement("button");
	elemHTML.setAttribute('class', "defaultButtonItem");
	UtilsHTMLEl.setInfoElRef(elemHTML, info);
	// elemHTML.setInfoElRef(info);

	elemHTML.id = "" + parentId + "-" + el.getAttribute("sid");

	info.refElem = el;
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;
	// info.xformRef = ?;
	// info.xformRefForLabel = ?;
	info.refHTMLElem = elemHTML;
	info.refHTMLElemDiv = elemHTMLDiv;
	info.typeEl = "button";

	var style = "cursor:pointer;";
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
			parseXFormsForScript(childEl, null, null, info.sid);
			
			if (childEl.tagName.replace(new RegExp('.+:','g'), '') == "trigger") {
				$(elemHTML).on("click", function() {
					var arr = xFormsScriptMass[info.sid];
					if (arr){
						for (var i = 0; i < arr.length; i++) {
							if (arr[i]._event == 'DOMActivate')
								arr[i].work();
						}
						fullRedrawXFDL(xfdlForm);
					}
				});
			}
			
//			if (childEl.localName == "trigger") {
				/*var script = parseXTrigger(childEl);
				$(elemHTML).on("click", function() {
							// alert(script)

							// console.log(serial.serializeToString(el));
							if (info.isSigner != true) {
								// eval(script);
								// toolbar_save();
								// TODO попробовать найти другой способ без
								// перерисовки
								fullRedrawXFDL(xfdlForm);
							}

							return;
						});*/
//			}
		}
		childEl = childEl.nextSibling;
	}
	{
		// теперь переберем остальные-узлы
		childEl = el.firstChild;
		while (childEl)// альтернативный и более правильный метод перебора
		// элементов
		// чем через xpath
		{

			if (childEl.nodeType == Node.ELEMENT_NODE) {
				lNameChildEl = childEl.tagName.replace(/.+:/g, '');
				valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
				if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {

					switch (lNameChildEl) {
						case 'border' :
							if (valChildEl == 'off')
								style = style + "border-style: none;";
							else
								style = style + "border-style: outset;";
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
							// TODO доделать
							break;
						case 'fontinfo' :
							// TODO доделать
							break;
						case 'itemlocation' :
							// TODO доделать
							break;
						
						case 'type' :
							info.typeAction = valChildEl;
							break;

						case 'url' :
							info.url = valChildEl;
							break;
						case 'signature' :
							info.sidSignature = valChildEl;
							break;
						case 'value' :
							if (!info.xformNode) {
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
									childEl.parentNode);// так как есть
														// xform-связь
							info.hiddenTeg[lNameChildEl] = emNode
						} else
							emNode = childEl;
						var scriptCompute = new UnitScript(attrScript, info,
								emNode);
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
							// lNameChildEl + "] = " +
							// info.hiddenTeg['custom:' + lNameChildEl]);
						} else
							emNode = childEl;
						var scriptCompute = new UnitScript(attrScript, info,
								emNode);
						massAllScripts.push(scriptCompute);
					}
				}

			}
			childEl = childEl.nextSibling;
		}

	}
	if (el.isSigner())// определяем по элементу, т.к. value не имеет значения
		{
		info.isSigner = true;
		elemHTMLDiv.setAttribute("title",
		    "У этого элемента есть цифровая подпись, поэтому его нельзя изменять");
		}
	
	if (!isGlobalEdit)
		{
		elemHTML.setAttribute("disabled", "disabled");
	}
	
	if (!value && el.xpath("count(xforms:trigger/xforms:label)>0").booleanValue)
		value = el.xpath("string(xforms:trigger/xforms:label)").stringValue;
	console.logDraw("value = " + value);
	
	if (bounds.width > 0){
		style = style + "width:" + bounds.width + "px;";
	} else {
		var str = "";
		for (var i=0;i<value.length;i++){
			str += "w";
		}
		dopDivSize.setValue(str);
		bounds.width = 1 * (dopDivSize.offsetWidth + 0.6);
		bounds.x2 = bounds.x1 + bounds.width;
		style = style + "width:" + bounds.width + "px;";
	}
	style = style + "height:" + bounds.height + "px;";
	
	style += "min-height: 22px;";
	elemHTML.setAttribute("style", style);
	elemHTML.setAttribute("type", "button");
	elemHTMLDiv.setAttribute("style", styleDiv);

	elemHTML.setAttribute("value", value);
	UtilsHTMLEl.setValue(elemHTML, value);
	// /elemHTML.setValue(value);
	// console.logDraw(serial.serializeToString(elemHTML));

	elemHTMLDiv.appendChild(elemHTML);
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
	
	info.value = value;
	// ------------------------------------------

	// ------------------------------------------

	/**
	 * @type Node
	 */
	var activated = new EmulateNode('activated', 'off', el);
	info.hiddenTeg["activated"] = activated;
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);
	// info.refHTMLElem.setInfoElRef(info);
	// ------------------------------------------

	// ------------------------------------------
	// console.log(info)
	switch (info.typeAction) {
		case 'signature' :
			info.actionFunction = functionForType.signature;
			break;
		case 'done' :
			info.actionFunction = functionForType.done;
			break;
		case 'pagedone' :
			info.actionFunction = functionForType.pagedone;
			break;
		case 'extract' :
			info.actionFunction = functionForType.extract;
			break;
		case 'select' :
			info.actionFunction = functionForType.select;
			break;

		// TODO это заглушка
		case 'replace' :// отменить ссылку на которую проихойдет переход
			info.actionFunction = function() {
				if (info.url)
					if (info.url.indexOf('javascript:') != -1)
						window.eval(info.url);
			}
			break;
		case '' :
			break;
		case '' :
			break;

	}

	return info;
}

/**
 * 
 * @param {InfoElement}
 *            butInfo
 */
function showSignDialog(butInfo) {
	console.logCall("call showSignDialog");
//    $(windowInfo).dialog('open');
//    $('#windowInfo').dialog('widget').find(".ui-dialog-titlebar").hide();
//    return;

	var allDiv = windowForSignManager.getElementsByTagName('div');
	var divIsSign = $(allDiv.item(0));
	var divInfoSign = $(allDiv.item(1));
	var divAllButtons = allDiv.item(2);

	var allBut = allDiv.item(2).getElementsByTagName('button');

	var butClose = $(allBut.item(0));
	var butCreateSign = $(allBut.item(1));
	var butDel = $(allBut.item(2));
	
	if (butInfo.refTegSignature) {
		divIsSign.text('Цифровая подпись имеется');
		var signInfo = butInfo.refTegSignature.xpath('string(./xfdl:fullname)').stringValue;
		divInfoSign.text('Информация о подписавшем пользователе:\n' + signInfo);
		butCreateSign.prop('disabled', true);
	} else {
		if (mapMandatoryItems.getNumberItems()) {
			if (!confirm('Предупреждение. Некоторые поля формы содержат недопустимые значения. Продолжить?'))
				return;

		}
		divIsSign.text('Цифровая подпись отсутсвует');
		divInfoSign.text('');

		//if (document.utilsApplet && document.utilsApplet.createSign) {
		//if (window.parent.document.applet_adapter && window.parent.document.applet_adapter.createSign) {
		if (plug != null){
			
			butCreateSign.prop('disabled', false);
			butCreateSign.click(function(e) {
				/**
				 * @type String
				 */
				/*var allInfo = (window.parent.document.applet_adapter.getReadersInfoAppet() + '')
						.split('\n\n')[0];
				if (allInfo.indexOf(':') != -1) {*/
					//var readerName = allInfo.substring(0, allInfo.indexOf(':'));

					/**
					 * @type String[]
					 */
					/*var massUserInfo = allInfo.substring(allInfo.indexOf(':\n')
							+ 2).split('\n');

					// console.log(massUserInfo);
					var userInfo = {};
					for (var j = 0; j < massUserInfo.length; j++) {
						var tmpInfo = massUserInfo[j].split(' = ');
						userInfo[tmpInfo[0]] = tmpInfo[1];
					}

					var tmpStr = '';
					var tmpFlag = false;
					if (userInfo['Subject: CN']) {
						tmpStr += userInfo['Subject: CN'];
						tmpFlag = true;
					}
					if (userInfo['Subject: E']) {
						if (tmpFlag)
							tmpStr += ', ';
						tmpStr += userInfo['Subject: E'];
					}*/

					var signTeg = butInfo.refElem.xpath('xfdl:signer').iterateNext();
					signTeg.setValue(plug.GetSubject());

					var xfdlStr = null;

					/*xfdlStr = window.parent.document.applet_adapter.createSign(
							replaceNewLine(serial.serializeToString(xfdlForm)),
							butInfo.fullSidRef.replace("-", "."), readerName);*/
							
					xfdlStr = plug.SignForm(replaceNewLine(SerializeXFDL()),
						butInfo.fullSidRef.replace("-", "."));

					// console.log("кнопка = " +
					// info.fullSidRef.replace("-","."));
					// console.log("исходник:\n " +
					// replaceNewLine(serial.serializeToString(xfdlForm)));
					// console.log("xfdStr:\n" + xfdlStr);

					if (xfdlStr) {
						xfdlForm = parser.parseFromString(xfdlStr, "text/xml");
						xfdlForm.normalize();

						//redrawXFDL(xfdlForm);// fullRedrawXFDL() здесь
												// необязателен
						$(windowForSignManager).dialog("close");
//						showSignDialog(treeInfoEls
//								.getElemInfo(butInfo.fullSidRef));
						alert("Форма была подписана");
					} else {

						signTeg.setValue('');
						// принудительно устанавливаем предыдущее значение как
						// будто бы
						// переключения не было
						signTeg._prevValue = '';

						alert("Неудалось создать подпись");
					}
					// xfdlForm = copyXfdl;

				/*} else {
					alert("Неудалось создать подпись, т.к. невозможно обнаружить подключенные ридеры");
				}*/

			}

			);

		} else {
			butCreateSign.prop('disabled', true);
		}
	}

	$(windowForSignManager).dialog("open");

}

/**
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordButton(info) {
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
	var htmlEl = info.refHTMLElem;
	htmlEl.style.width = info.width + "px";
	htmlEl.style.height = info.height + "px";
	// ------------------------------------------

	// ------------------------------------------
	bounds = cancelCommonUpdates(bounds, info.refElem);
	// ------------------------------------------

	return bounds;
}
