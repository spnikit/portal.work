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
 * @return {HTMLElement} - полученный элемент
 */

function drawCombobox(el, parentId, parentXformRef) {
	var xPatternDate = "YYYY-MM-DD";
	var xPatternTime = "hh:mm:ss";
	// ------------------------------------------

	// ------------------------------------------
	var info = new InfoElement();
	var bounds = findBounds(el, parentId, 1);
	// ------------------------------------------

	// ------------------------------------------
	bounds = applyCommonUpdates(bounds, el);
	// ------------------------------------------

	// ------------------------------------------
	var comboboxDiv = document.createElement("div");
	var styleDiv = "position:absolute;";
	styleDiv = styleDiv + "top:" + bounds.y1 + "px;";
	styleDiv = styleDiv + "left:" + bounds.x1 + "px;";
	styleDiv = styleDiv + "width:" + bounds.width + "px;";
	styleDiv = styleDiv + "height:" + bounds.height + "px;";
	styleDiv = styleDiv + "z-index: 10;";

	comboboxDiv.setAttribute("style", styleDiv);
	// ------------------------------------------

	// ------------------------------------------
	var combobox = document.createElement("input");
	combobox.setInfoElRef(info);

	var value = "";
	while (true) {
		if (el.xpath("string(./xfdl:format/xfdl:datatype) = 'date'").booleanValue) {

			if (pattern == "")
				pattern = "DD.MM.YYYY";
			else
				info.xDifVal = true;

			info.dataType = "date";
			info.pattern = pattern;

			combobox.setAttribute("class", "comboboxDate");

			/**
			 * путь к xform-узлу в который будет производиться запись значения
			 * 
			 * @type {String}
			 */
			var path = el.xpath("string(./xforms:input/@ref)").stringValue;

			path = generateRealXRef(path, parentXformRef);
			info.xformRef = path;
			var tmpArr = getNodeArrayForInstanceRef(path);
			if (tmpArr) {
				info.xformNode = tmpArr[0];
				info.hiddenTeg['value'] = tmpArr[0];
				value = getNodeValue(tmpArr[0]);
			} else
				value = "";

			var table = document.createElement("table");
			table.style.width = bounds.width + "px;";
			table.style.height = bounds.height + "px;";
			// table.style.cellpadding = 0 + "px";
			// table.style.cellspacing = 0 + "px";
			// table.style.border = 0;
			table.setAttribute("class", "comboTable");
			comboboxDiv.appendChild(table);
			/**
			 * строка для поля ввода и кнопки
			 * 
			 * @type {HTMLElement|HTMLNode}
			 */
			var tr1 = document.createElement("tr");
			// tr1.setAttribute("style","width:"+bounds.width+"px;");
			// tr1.style.width=bounds.width+"px;";
			table.appendChild(tr1);
			/**
			 * столбец для поля ввода
			 * 
			 * @type HTMLElement
			 */
			var tdInput = document.createElement("td");
			tdInput.style.align = "center";
			tdInput.style.width = (bounds.width - 20) + "px;";
			tr1.appendChild(tdInput);
			tdInput.appendChild(combobox);
			// combobox.style.width=""+(bounds.width-15)+"px;";
			combobox.setAttribute("class", "inputCombobox");

			// console.log("value = " + value);
			if (value == "") {
				value = thisDateAndTime(xPatternDate);
			}

			value = dateFormatS2S(value, xPatternDate, pattern);
			// console.log("value = " + value);
			combobox.setAttribute("value", value);

			/**
			 * столбец для кнопки
			 * 
			 * @type HTMLElement
			 */
			var tdBut = document.createElement("td");
			tr1.appendChild(tdBut);
			var butCom = document.createElement("input");
			butCom.setAttribute("height", "20px;");

			// TODO

			// .setAttribute(
			// "onclick",
			// "serial.serializeToString(this.parentNode.parentNode.parentNode.parentNode);"
			// +
			// "resetVisible(this.parentNode.parentNode.parentNode.parentNode.children[1]);");
			butCom.setAttribute("type", "image");
			butCom.setAttribute("src", "images/dateCombobox.png");
			butCom.setAttribute("class", "buttonCombobox");

			tdBut.appendChild(butCom);
			/**
			 * строка для меню
			 * 
			 * @type {HTMLElement|HTMLNode}
			 */
			var tr2 = document.createElement("tr");
			tr2.style.display = "none";
			tr2.style.width = bounds.width + "px;";
			var tdDatapicker = document.createElement("td");
			tdDatapicker.setAttribute("colspan", 2);
			tdDatapicker.style.width = bounds.width + "px;";
			tr2.appendChild(tdDatapicker);

			var currentDate = thisDateAndTime(pattern);
			// !позицию я изменял в библиотеке
			var datapic = $(butCom).DatePicker({
						format : pattern,
						date : $(combobox).val(),
						current : $(combobox).val(),
						starts : 1,
						position : 'left bottom',
						onBeforeShow : function() {
							$(butCom)
									.DatePickerSetDate($(combobox).val(), true);
						},
						onChange : function(formated, dates) {
							$(combobox).val(moment(dates).format(pattern));

							$(butCom).DatePickerHide();

						}
					});
			butCom.addEventListener('click', function(e) {

						return true;
					}, false);
			tdInput.appendChild(combobox);
			break;
		}

		if (el.xpath("string(./xfdl:format/xfdl:datatype) = 'time'").booleanValue) {
			// <hour_1>11:00:00</hour_1>
			// <min_1>00:42:00</min_1>

			if (pattern == "") {
				pattern = "hh:mm:ss";
			}

			combobox.setAttribute("class", "comboboxDate");// TODO : какой тип
			// ставить для времени?

			if (el.xpath("./xforms:select1/@selection !='open'").booleanValue) {
				console.log("!Warring. Аттрибут 'selection' задан неверно");
			}

			/**
			 * путь к xform-узлу в который будет производиться запись значения
			 * 
			 * @type {String}
			 */
			var path = el.xpath("string(./xforms:input/@ref)").stringValue;
			path = generateRealXRef(path, parentXformRef);

			value = getNodeArrayForInstanceRef(path)
					? getNodeValue((getNodeArrayForInstanceRef(path))[0])
					: "";

			var table = document.createElement("table");
			table.style.width = bounds.width + "px;";
			table.style.height = bounds.height + "px;";
			// table.style.cellpadding = 0 + "px;";
			// table.style.cellspacing = 0 + "px;";
			// table.style.border = 0;
			table.setAttribute("class", "comboTable");
			comboboxDiv.appendChild(table);
			/**
			 * строка для поля ввода и кнопки
			 * 
			 * @type {HTMLElement|HTMLNode}
			 */
			var tr1 = document.createElement("tr");
			// tr1.setAttribute("style","width:"+bounds.width+"px;");
			// tr1.style.width=bounds.width+"px;";
			table.appendChild(tr1);
			/**
			 * столбец для поля ввода
			 * 
			 * @type HTMLElement
			 */
			var tdInput = document.createElement("td");
			tdInput.style.align = "center";
			tdInput.style.width = (bounds.width - 20) + "px;";
			tr1.appendChild(tdInput);
			tdInput.appendChild(combobox);
			// combobox.style.width=""+(bounds.width-15)+"px;";
			combobox.setAttribute("class", "inputCombobox");

			console.log("value = " + value);
			if (value == "") {
				value = thisDateAndTime(xPatternDate);
			}

			value = dateFormatS2S(value, xPatternDate, pattern);
			console.log("value = " + value);
			combobox.setAttribute("value", value);

			/**
			 * столбец для кнопки
			 * 
			 * @type HTMLElement
			 */
			var tdBut = document.createElement("td");
			tr1.appendChild(tdBut);
			var butCom = document.createElement("input");
			butCom.setAttribute("height", "20px;");

			// TODO

			// .setAttribute(
			// "onclick",
			// "serial.serializeToString(this.parentNode.parentNode.parentNode.parentNode);"
			// +
			// "resetVisible(this.parentNode.parentNode.parentNode.parentNode.children[1]);");
			butCom.setAttribute("type", "image");
			butCom.setAttribute("src", "images/dateCombobox.png");
			butCom.setAttribute("class", "buttonCombobox");

			tdBut.appendChild(butCom);
			/**
			 * строка для меню
			 * 
			 * @type {HTMLElement|HTMLNode}
			 */
			var tr2 = document.createElement("tr");
			tr2.style.display = "none";
			tr2.style.width = bounds.width + "px;";
			var tdDatapicker = document.createElement("td");
			tdDatapicker.setAttribute("colspan", 2);
			tdDatapicker.style.width = bounds.width + "px;";
			tr2.appendChild(tdDatapicker);

			var currentDate = thisDateAndTime(pattern);
			// !позицию я изменял в библиотеке
			var datapic = $(butCom).DatePicker({
						format : pattern,
						date : $(combobox).val(),
						current : $(combobox).val(),
						starts : 1,
						position : 'left bottom',
						onBeforeShow : function() {
							$(butCom)
									.DatePickerSetDate($(combobox).val(), true);
						},
						onChange : function(formated, dates) {
							$(combobox).val(moment(dates).format(pattern));

							$(butCom).DatePickerHide();

						}
					});
			butCom.addEventListener('click', function(e) {

						return true;
					}, false);
			tdInput.appendChild(combobox);
			break;
		}

		if (el.xpath("count(./xforms:select1) > 0").booleanValue) {

			if (el.xpath("./xforms:select1/@selection !='open'").booleanValue) {
				console.log("!Warring. Аттрибут 'selection' задан неверно");
			}

			/**
			 * путь к xform-узлу в который будет производиться запись значения
			 * 
			 * @type {String}
			 */
			var path = el.xpath("string(./xforms:select1/@ref)").stringValue;
			path = generateRealXRef(path, parentXformRef);
			info.xformRef = path;
			var tmpArr = getNodeArrayForInstanceRef(path);
			if (tmpArr) {
				info.xformNode = tmpArr[0];
				info.hiddenTeg['value'] = tmpArr[0];
				value = getNodeValue(tmpArr[0]);
			} else
				value = "";
			/**
			 * @type HTMLElement
			 */
			var table = document.createElement("table");

			table.cols = 2;
			table.style.position = "absolute";
			table.style.cellpadding = 0;
			table.style.cellspacing = 0;
			table.style.border = 0;
			table.style.width = bounds.width + 'px';

			comboboxDiv.appendChild(table);
			/**
			 * строка для поля ввода и кнопки
			 * 
			 * @type {HTMLElement|HTMLNode}
			 */
			var tr1 = document.createElement("tr");
			// tr1.setAttribute("style","width:"+bounds.width+"px;");
			// tr1.style.width=bounds.width+"px;";

			var widthBut = 20;

			table.appendChild(tr1);
			/**
			 * столбец для поля ввода
			 * 
			 * @type HTMLElement
			 */
			var tdInput = document.createElement("td");
			tr1.appendChild(tdInput);
			tdInput.appendChild(combobox);
			// combobox.style.width=""+(bounds.width-15)+"px;";
			combobox.setAttribute("class", "inputCombobox");
			combobox.setAttribute("value", value);
			if (bounds.width - widthBut > 0) {
				tdInput.style.width = (bounds.width - widthBut) + "px";
				combobox.style.width = (bounds.width - widthBut) + "px";
			} else {
				tdInput.style.width = 0 + "px";
				combobox.style.width = 0 + "px";
			}

			/**
			 * столбец для кнопки
			 * 
			 * @type HTMLElement
			 */
			var tdBut = document.createElement("td");
			tr1.appendChild(tdBut);
			var butCom = document.createElement("input");
			butCom.setAttribute("height", "20px");

			butCom.setAttribute("width", widthBut + "px;");
			// TODO
			butCom.addEventListener("click", function(e) {

				/**
				 * @type HTMLElement
				 */
				// var target = (e.target || e.scrElement);
				// console.log(this.parentNode.parentNode.parentNode.children[1]);
				resetVisible(this.parentNode.parentNode.parentNode.children[1]);

				return true;
			}, false);

			// .setAttribute(
			// "onclick",
			// "serial.serializeToString(this.parentNode.parentNode.parentNode.parentNode);"
			// +
			// "resetVisible(this.parentNode.parentNode.parentNode.parentNode.children[1]);");
			butCom.setAttribute("type", "image");
			butCom.setAttribute("src", "images/popupSelect.svg.png");
			butCom.setAttribute("class", "buttonCombobox");

			tdBut.appendChild(butCom);
			/**
			 * строка для меню
			 * 
			 * @type {HTMLElement|HTMLNode}
			 */
			var tr2 = document.createElement("tr");
			tr2.style.display = "none";
			tr2.style.width = bounds.width + "px";
			var tdMenu = document.createElement("td");
			tdMenu.setAttribute("colspan", 2);
			tdMenu.style.width = bounds.width + "px";
			tr2.appendChild(tdMenu);

			var ulMenu = document.createElement("ul");
			ulMenu.setAttribute("class", "comboboxMenu");

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

			while (true) {
				pathItemSet = el
						.xpath("string(./xforms:select1/xforms:itemset/@nodeset)").stringValue;
				pathItemSet = generateRealXRef(pathItemSet, path,
						parentXformRef).trim();
				pathItemSetValue = el
						.xpath("string(./xforms:select1/xforms:itemset/xforms:value/@ref)").stringValue
						.trim();
				pathItemSetLabel = el
						.xpath("string(./xforms:select1/xforms:itemset/xforms:label/@ref)").stringValue
						.trim();
				break;
			}
			/**
			 * @type {Node[]}
			 */
			var itemSet;

			itemSet = getNodeArrayForInstanceRef(pathItemSet);
			for (var i = 0; i < itemSet.length; i++) {
				/**
				 * @type String
				 */
				var valueLiEl = pathItemSetValue != ""
						? itemSet[i].xpath("string(" + pathItemSetValue + ")").stringValue
						: getNodeValue(itemSet[i]);
				/**
				 * @type String
				 */
				var labelLiEl = pathItemSetLabel != ""
						? itemSet[i].xpath("string(" + pathItemSetLabel + ")").stringValue
						: getNodeValue(itemSet[i]);;
				if (labelLiEl == "")
					labelLiEl = valueLiEl;
				/**
				 * @type {HTMLElement}
				 */
				var liEl = document.createElement("li");
				liEl.setAttribute("value", valueLiEl);
				liEl.setAttribute("class", "liEl");

				setNodeValue(liEl, labelLiEl);
				liEl.addEventListener("click", function(e) {

					/**
					 * @type HTMLElement
					 */
					// var target = (e.target || e.scrElement);
					// console.log(this.parentNode.parentNode.parentNode.children[1]);
					setValueThisBlock(
							this.getAttribute('value'),
							this.parentNode.parentNode.parentNode.parentNode.children[0].children[0]);
					resetVisible(this.parentNode.parentNode.parentNode);
					return true;
				}, false);
				ulMenu.appendChild(liEl);
			}

			// TODO
			ulMenu.style.width = bounds.width + "px";
			tdMenu.appendChild(ulMenu);
			table.appendChild(tr2);

			break;
		}

		if (el.xpath("count(./xfdl:value) > 0").booleanValue) {
			value = el.xpath("string(./xfdl:value)").stringValue;
			comboboxDiv.appendChild(combobox);
			break;
		}
		break;
	}

	combobox.id = "" + parentId + "-" + el.getAttribute("sid");
	var thisId = "" + parentId + "-" + el.getAttribute("sid");
	var style = "";

	if (el.xpath("xfdl:readonly = 'on'").booleanValue)
		combobox.setAttribute('readonly', 'readonly');

	if (el.xpath("xfdl:border = 'off'").booleanValue)
		style = style + "border-style: none;";
	else
		style = style + "border-style: inset;";

	if (el.xpath("xfdl:visible = 'off'").booleanValue)
		style = style + "visibility: hidden;";
	else
		style = style + "visibility: visible;";

	/**
	 * @type Element
	 */
	var tegActive = el.xpath("xfdl:active").iterateNext();
	if (tegActive) {
		if (tegActive.getValue() == "off") {

			if (info.xformRef)
				info.hiddenTeg['active'] = new EmulateNode('active', 'off', el);
			else
				info.hiddenTeg['active'] = tegActive;

			combobox.setAttribute("disabled", "disabled");
		} else {
			if (info.xformRef)
				info.hiddenTeg['active'] = new EmulateNode('active', 'on', el);
			else
				info.hiddenTeg['active'] = tegActive;

		}
	} else {

	}

	if ((bounds.width - 22) > 0)
		style = style + "width:" + (bounds.width - 22) + "px;";
	else
		style = style + "width:" + 0 + "px;";
	style = style + "height:" + bounds.height + "px;";

	combobox.setAttribute("style", style);
	// ------------------------------------------

	// ------------------------------------------

	var pattern = el
			.xpath("string(./xfdl:format/xfdl:presentation/xfdl:pattern)").stringValue;
	// нельзя ставить toUperCase() т.к. MM это месяцы, а mm это минуты

	saveNodeValue(combobox, value);
	// ------------------------------------------

	// ------------------------------------------
	if (el.xpath("count(xfdl:size/xfdl:height)>0").booleanValue)
		combobox.setAttribute("rows",
				el.xpath("number(xfdl:size/xfdl:height)").numberValue);

	if (el.xpath("count(xfdl:size/xfdl:width)>0").booleanValue)
		combobox.setAttribute("cols",
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
	info.refHTMLElem = combobox;
	info.refHTMLElemDiv = comboboxDiv;
	info.typeEl = "combobox";
	info.value = value;
	// ------------------------------------------

	// ------------------------------------------
	if (combobox.height < 22) {
		info.height = 22;
		combobox.style.height = 22;
	}
	// ------------------------------------------

	// ------------------------------------------
	treeInfoEls.addElemInfo(info);
	info.refHTMLElem.setInfoElRef(info);
	// ------------------------------------------

	/**
	 * @type XPathRezult
	 */
	var tegsScriptCompute = el.xpath(".//@xfdl:compute|.//@compute");

	/**
	 * аттрибут в котором находится скрипт
	 * 
	 * @type Attr
	 */
	var attrScript = tegsScriptCompute.iterateNext();
	while (attrScript) {
		/**
		 * @type UnitScript
		 */
		var scriptCompute = new UnitScript(attrScript.getValue(), info,
				attrScript.ownerElement);
		massAllScripts.push(scriptCompute);
		attrScript = tegsScriptCompute.iterateNext();
	}
	return comboboxDiv;
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
