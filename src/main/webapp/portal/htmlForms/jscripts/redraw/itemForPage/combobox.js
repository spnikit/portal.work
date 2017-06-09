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

function drawCombobox(el, parentId, parentXformRef) {
	// ------------------------------------------

	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, 1);
	info.bounds = bounds;
	// ------------------------------------------

	// ------------------------------------------
	bounds = applyCommonUpdates(bounds, el);
	// ------------------------------------------

	// ------------------------------------------
	var elemHTMLDiv = document.createElement("div");
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	styleDiv = styleDiv + "width:" + bounds.width + "px;";
	styleDiv = styleDiv + "height:" + bounds.height + "px;";
	styleDiv = styleDiv + "z-index: 10;";
	var fontcolor = 'black';
	var thisId = "" + parentId + "-" + el.getAttribute("sid");
	var style = "";

	/**
	 * 
	 * @type SelectItem[]
	 */
	var selectItems = null;

	elemHTMLDiv.setAttribute("style", styleDiv);
	// ------------------------------------------

	// ------------------------------------------
	var elemHTML = document.createElement("textarea");
	elemHTML.setAttribute('class', 'defaultComboboxItem noBorder');

	UtilsHTMLEl.setInfoElRef(elemHTML, info);
	var tegActivated = null;
	var value = "";
	var xformValue = "";
	elemHTML.id = "" + parentId + "-" + el.getAttribute("sid");

	if ((bounds.width - 22) > 0)
		style = style + "width:" + (bounds.width - 22) + "px;";
	else
		style = style + "width:" + 0 + "px;";
	style = style + "height:" + bounds.height + "px;";

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
					console.log(tmpArr[0]);
					// TODO смотри определение значения после поиска шаблона
					// info.hiddenTeg['value'] = tmpArr[0];
					xformValue = ieXmlDom != null ? tmpArr[0].text : tmpArr[0].getValue();
					if (info.xformNode.isSigner()) {
						info.SignerOptions["value"] = true;
					}
				} else
					// TODO это плохо
					value = "";
				break;
			}
			if (childEl.tagName.replace(new RegExp('.+:','g'), '') == "select1") {
				// TODO доделать

				if (childEl.getAttribute("selection") != 'open') {
					console
							.errDraw("!Error. Аттрибут 'selection' задан неверно");
				}
				/**
				 * @type {String}
				 */
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

				/**
				 * @type {String}
				 */
				var pathItemSet;
				/**
				 * @type {String}
				 */
				var pathItemSetValue;

				/**
				 * @type {String}
				 */
				var pathItemSetLabel;

                if(ieXmlDom != null) {
                    pathItemSet = ieXpathEvaluate(childEl, "./xforms:itemset/@nodeset")[0].text;
                } else {
                    pathItemSet = childEl.xpath("string(./xforms:itemset/@nodeset)").stringValue;
                }
				console.info(path);
				console.info(pathItemSet);
				pathItemSet = generateRealXRef(pathItemSet, path).trim();
				pathItemSetValue = childEl
						.xpath("string(./xforms:itemset/xforms:value/@ref)").stringValue
						.trim();
				pathItemSetLabel = childEl
						.xpath("string(./xforms:itemset/xforms:label/@ref)").stringValue
						.trim();

				/**
				 * @type {Node[]}
				 */
				var itemSet;

				console.info(pathItemSet);
				itemSet = getNodeArrayForInstanceRef(pathItemSet);
				if (itemSet) {
					selectItems = new Array(itemSet.length);
					for (var i = 0; i < itemSet.length; i++) {
						/**
						 * @type String
						 */
                        var valueItem;
                        if(ieXmlDom != null)
                            valueItem = pathItemSetValue != ""
                                ? ieXpathEvaluate(itemSet[i], pathItemSetValue)[0].text
                                : itemSet[i].text;
                        else
                            valueItem = pathItemSetValue != ""
								? itemSet[i].xpath("string(" + pathItemSetValue
										+ ")").stringValue
								: itemSet[i].getValue();
						/**
						 * @type String
						 */
                        var labelItem;
                        if(ieXmlDom != null)
                            labelItem = pathItemSetLabel != ""
                                ? ieXpathEvaluate(itemSet[i], pathItemSetLabel)[0].text : itemSet[i].text;
                        else
						    labelItem = pathItemSetLabel != ""
								? itemSet[i].xpath("string(" + pathItemSetLabel
										+ ")").stringValue
								: itemSet[i].getValue();

						selectItems[i] = new SelectItem(valueItem, labelItem);
						// console.dir(selectItems[i]);
					}
				} else {
					selectItems = new Array(0);
				}
				break;
			}

		}
		childEl = childEl.nextSibling;
	}
	// теперь переберем остальные-узлы
	childEl = el.firstChild;
	var isBorder = true;
	while (childEl)// альтернативный и более правильный метод перебора
	// элементов
	// чем через xpath
	{
		if (childEl.nodeType == Node.ELEMENT_NODE) {
			lNameChildEl = childEl.tagName.replace(new RegExp('.+:', 'g'), '');
			valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {
				switch (lNameChildEl) {
					// case "value" :
					// info.hiddenTeg['value'] = childEl;
					// value = childEl.getValue();

					// break;
					case 'readonly' :
						if (valChildEl == 'on')
							elemHTML.setAttribute('readonly', 'readonly');
						break;
					case 'border' :
						if (valChildEl == 'off') {
							isBorder = false;
							style = style + "border-style: none;";
						} else
							style = style + "border-style: inset;";
						break
					case 'visible' :
						if (valChildEl == 'off')
							styleDiv = styleDiv + "visibility: hidden;";
						else
							styleDiv = styleDiv + "visibility: visible;";
						break;
					case 'justify' :
						info.justify = valChildEl;
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
					case 'activated' :
						// TODO доделать
						break;
					case 'itemlocation' :
						// TODO доделать
						break;
					case 'group' :
						if (!selectItems) {
							selectItems = new Array();
							var group = ieXmlDom != null ? childEl.text : childEl.getValue();
							var cellGroup = el
									.xpath("../xfdl:cell[./xfdl:group='"
											+ group + "']");
							var cell = cellGroup.iterateNext();
							while (cell) {
								if (cell.nodeType == Node.ELEMENT_NODE) {
									/**
									 * @type String
									 */
									var valueOpt = cell
											.xpath("string(./xfdl:value)").stringValue;
									/**
									 * @type String
									 */
									var labelOpt = cell
											.xpath("string(./xfdl:label)").stringValue;
									selectItems.push(new SelectItem(valueOpt,
											labelOpt));
								}
								var cell = cellGroup.iterateNext();
							}
						}
						console.info('111');
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
						info.hiddenTeg[lNameChildEl] = emNode
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
	if (!isGlobalEdit)
		elemHTML.setAttribute('readonly', 'readonly');
	tegActivated = new EmulateNode('activated', 'off', el);
	info.hiddenTeg['activated'] = tegActivated;

	if (!info.fontInfoObj)// создаем дефолтный объект если не было тэга
							// <fontinfo>
		info.fontInfoObj = new FontInfoObj();

	style += "max-height:"
			+ (1 * objStateDrawing.convertFontSize[info.fontInfoObj.fontSize] + 4)
			+ "px;";
	style += info.fontInfoObj.getCssStyle();

	if (!info.formatObj)// создаем дефолтный объект если не было тэга <format>
		info.formatObj = new FormatObj();

	if (info.xformNode) {
		var xPatternDate = defXFormPattern[info.formatObj.datatype];
		if (xformValue) {
			value = dateFormatS2S(xformValue, xPatternDate,
					info.formatObj.prSetting.pattern);
			var tmpDateVal = dateFormatS2S(value,
					info.formatObj.prSetting.pattern, xPatternDate);
			// если вдруг обратное преобразование не
			// дало этого же результата то значит
			// значение из xform-связи не соответсвует
			// шаблону
			if (tmpDateVal != xformValue)
				value = xformValue;
		}
		var emNode = new EmulateNode('value', value, el, info.xformNode);
		info.hiddenTeg['value'] = emNode;
		info.xformNode.addXformLink(info, 'value', emNode);
	}
	if (info.SignerOptions['value']) {
		info.isSigner = true;

		elemHTML.setAttribute("readonly", false);
		elemHTMLDiv
				.setAttribute("title",
						"У этого элемента есть цифровая подпись, поэтому его нельзя изменять");
	}

	info.selectItems = selectItems;
	if (selectItems || (!selectItems && info.formatObj.datatype != 'date')) {
		console.log(selectItems);
		elemHTMLDiv.appendChild(elemHTML);
		$(elemHTML).autocombobox({
					isBorder : isBorder,
					source : selectItems,
					// change : function(event, ui)
					// {
					// console.log('c1');
					// console.info(this);// .trigger('change');
					// },
					select : function(event, ui) {
						// console.log('s1');
						// console.info(this);// .trigger('select');

						// if (elemHTML.value != valDate)
						// {
						// elemHTML.value = valDate;
						// Теперь эмулируем событие onChange, чтобы результат
						// сохранился
						// в xfdl'нике

						// var emulEvent = document.createEvent("HTMLEvents");
						// emulEvent.initEvent('change', true, true);
						// elemHTML.dispatchEvent(emulEvent);
						//$(elemHTML).trigger('change');
						updateHTMLtoXFDL(elemHTML, 'value', ui.item.value);
						// }

					},
					isAutocomp : false
				});
		// далее старый вариант
		// var butCombobox = document.createElement('input');
		// butCombobox.setAttribute('type', 'image');
		// butCombobox.setAttribute('class', 'datepickerButton');
		// butCombobox.setAttribute('src', addPathResource
		// + 'images/popupSelect.svg.png');
		// butCombobox.setAttribute('style', style + '; padding-left:'
		// + ((bounds.width > 22) ? bounds.width - 22 : 0) + 'px;');
		//		
		// butCombobox.style.width = 'auto';
		// butCombobox.style.paddingLeft = ((bounds.width > 22)
		// ? bounds.width - 22
		// : 0)
		// + 'px';
		// butCombobox.style.zIndex = 8;
		//		
		// // Чтобы удобнее было достучаться
		// elemHTML.setAttribute('datePattern',
		// info.formatObj.prSetting.pattern);
		//		
		// elemHTMLDiv.appendChild(butCombobox);
		// elemHTMLDiv.appendChild(elemHTML);
		//		
		// /**
		// * Реальная высота поля ввода
		// *
		// * @type Number
		// */
		// var realHeight = elemHTML.offsetHeight;
		// var ulMenu = document.createElement("ul");
		// ulMenu.setAttribute("class", "comboboxMenu");
		//		
		// // минус 4 потому что padding = 2
		// ulMenu.setAttribute("style",
		// "display:none;position:absolute; left:0px; min-width: "
		// + (bounds.width - 4) + "px;");
		// if (selectItems)
		// for (var i = 0; i < selectItems.length; i++)
		// {
		// /**
		// * @type {HTMLElement}
		// */
		// var liEl = document.createElement("li");
		// liEl.setAttribute("value", selectItems[i].value);
		// liEl.setAttribute("class", "comboboxItem");
		//				
		// setNodeValue(liEl, selectItems[i].label);
		// liEl.addEventListener("click", function(e)
		// {
		// // console.logCall("call new combobox value");
		// resetVisible(ulMenu);
		// if (tegActivated.getValue() == 'on')
		// {
		// tegActivated.setValue('off');
		// elemHTMLDiv.style.zIndex = "10";
		// }
		// else
		// {
		// tegActivated.setValue('on');
		// elemHTMLDiv.style.zIndex = "18";
		// }
		// if (elemHTML.value != this.getAttribute('value'))
		// {
		// elemHTML.value = this.getAttribute('value');
		// // Теперь эмулируем событие onChange, чтобы результат сохранился
		// // в
		// // xfdl'нике
		//						    
		// var emulEvent = document.createEvent("HTMLEvents");
		// emulEvent.initEvent('change', true, true);
		// elemHTML.dispatchEvent(emulEvent);
		// }
		// /**
		// * @type HTMLElement
		// */
		// // var target = (e.target || e.scrElement);
		// // console.log(this.parentNode.parentNode.parentNode.children[1]);
		// // setValueThisBlock(
		// // this.getAttribute('value'),
		// //
		// this.parentNode.parentNode.parentNode.parentNode.children[0].children[0]);
		// return true;
		// }, false);
		// ulMenu.appendChild(liEl);
		// }
		// // ulMenu.style.width = bounds.width + "px";
		// // ulMenu.style.minHeight = bounds.width;
		// elemHTMLDiv.appendChild(ulMenu);
		// if (!info.isSigner)
		// butCombobox.addEventListener("click", function(e)
		// {
		//				    
		// /**
		// * @type HTMLElement
		// */
		// // var target = (e.target || e.scrElement);
		// // console.log(this.parentNode.parentNode.parentNode.children[1]);
		// ulMenu.style.top = elemHTML.offsetHeight + "px";
		//				    
		// resetVisible(ulMenu);
		// if (tegActivated.getValue() == 'on')
		// {
		// tegActivated.setValue('off');
		// elemHTMLDiv.style.zIndex = "10";
		// }
		// else
		// {
		// tegActivated.setValue('on');
		// elemHTMLDiv.style.zIndex = "18";
		// }
		// return true;
		// }, false);
	} else {
		style += "position:absolute;";
		style += "top:" + 0 + "px;";
		style += "left:" + 0 + "px;";
		if (info.formatObj.datatype == 'date') {

			/**
			 * кнопка вызывающая календарь
			 * 
			 * @type HTMLInputElement
			 */
			var butDatePicker = document.createElement('input');
			butDatePicker.setAttribute('type', 'image');
			butDatePicker.setAttribute('class', 'datepickerButton noPrint');
			butDatePicker.setAttribute('src', addPathResource
							+ 'images/dateCombobox2.png');
			butDatePicker.setAttribute('style', style + '; padding-left:'
							+ ((bounds.width > 22) ? bounds.width - 20 : 0)
							+ 'px; display: none;');

			butDatePicker.style.width = '18px';
			butDatePicker.style.zIndex = 8;
			butDatePicker.style.paddingRight = '2px';

			// Чтобы удобнее было достучаться
			elemHTML.setAttribute('datePattern',
					info.formatObj.prSetting.pattern);

			elemHTMLDiv.appendChild(butDatePicker);
			elemHTMLDiv.appendChild(elemHTML);

			var calend = $(butDatePicker);
			// console.dir(calend);
			if (!info.isSigner) addDateWidget(calend, elemHTML);
				/*calend.datepicker({
							gotoCurrent : true,
							duration : '',
							changeMonth : true,
							changeYear : true,
							defaultDate : null,
							beforeShow : function() {
								var input = arguments[0];
								var inst = arguments[1];

							},
							onClose : function() {
								var dateText = arguments[0];
								var inst = arguments[1];

							},
							onSelect : function() {
								var dateText = arguments[0];
								var inst = arguments[1];
								var pattern = elemHTML
										.getAttribute("datePattern");
								var valDate = dateFormatS2S(dateText,
										'dd.MM.yyyy', pattern);

								if (elemHTML.value != valDate) {
									elemHTML.value = valDate;
									// Теперь эмулируем событие onChange, чтобы
									// результат
									// сохранился в xfdl'нике
									$(elemHTML).trigger('change');
								}
								// console.log('select ' + valDate);
							},
							// Важно понимать что шаблоны разных бибилиотек не
							// совсем
							// одинаковы
							dateFormat : 'dd.mm.yy',
							showButtonPanel : true
						});*/

		}

	}

	style += "resize: none;";
	elemHTML.setAttribute("style", style);
	elemHTML.style.zIndex = 10;
	// ------------------------------------------

	// ------------------------------------------

	// нельзя ставить toUperCase() т.к. MM это месяцы, а mm это минуты

	elemHTML.value = value;
	info.value = value;
	// ------------------------------------------

	// ------------------------------------------
	if (el.xpath("count(xfdl:size/xfdl:height)>0").booleanValue)
		elemHTML.setAttribute("rows",
				el.xpath("number(xfdl:size/xfdl:height)").numberValue);

	if (el.xpath("count(xfdl:size/xfdl:width)>0").booleanValue)
		elemHTML.setAttribute("cols",
				el.xpath("number(xfdl:size/xfdl:width)").numberValue);
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
	info.refHTMLElem = elemHTML;
	info.refHTMLElemDiv = elemHTMLDiv;
	info.typeEl = "combobox";
	info.value = value;
	// ------------------------------------------

	// ------------------------------------------
	if (elemHTML.height < 22) {
		info.height = 22;
		elemHTML.style.height = 22;
	}
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);

	info.setBackGroundForInvalid(value);
	// ------------------------------------------

	return info;
};

/**
 * @param {InfoElement}
 *            info - infoElement созраняемы в дереве результатов
 * @return {Bounds} - полученные координаты
 */
function findCoordCombobox(info) {
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

function addDateWidget(calend, elemHTML){
	calend.datepicker({
		gotoCurrent : true,
		duration : '',
		changeMonth : true,
		changeYear : true,
		defaultDate : 0,
		beforeShow : function() {
			var input = arguments[0];
			var inst = arguments[1];

		},
		onClose : function() {
			var dateText = arguments[0];
			var inst = arguments[1];

		},
		onSelect : function() {
			var dateText = arguments[0];
			var inst = arguments[1];
			var pattern = elemHTML
					.getAttribute("datePattern");
			var valDate = dateFormatS2S(dateText,
					'dd.MM.yyyy', pattern);

			if (elemHTML.value != valDate) {
				elemHTML.value = valDate;
				// Теперь эмулируем событие onChange, чтобы
				// результат
				// сохранился в xfdl'нике
				$(elemHTML).trigger('change');
			}
			// console.log('select ' + valDate);
		},
		// Важно понимать что шаблоны разных бибилиотек не
		// совсем
		// одинаковы
		dateFormat : 'dd.mm.yy',
		showButtonPanel : true,
		showOn: 'button',
		buttonImageOnly: true,
		buttonImage: 'htmlForms/images/dateCombobox2.png'
	});
}