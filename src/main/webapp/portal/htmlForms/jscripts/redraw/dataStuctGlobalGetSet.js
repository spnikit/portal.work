/**
 * @include "../includes.js"
 */

/**
 * Получить дочерний тэг по имени. Используется как прослойка, чтобы не
 * проверять нахождение данного тэга в массиве <b>hiddenTeg</b>, среди дочерних
 * тэгов или <b>xform</b>-модели<br/>
 * 
 * Используется для вешания скриптов-слушаетелей на искомый тэг
 * 
 * 
 * 
 * @param {String}
 *            str - имя тэга
 * 
 * @return {Node|EmulateNode} тэг или <b>null</b> если такого не имеется
 */
InfoGlobalElement.prototype._getTeg = function(str) {
	/**
	 * @type Node
	 */
	var node = null;
	// TODO исправить
	if (str == "itemlocation['height']")
		str = "height";
	node = this.hiddenTeg[str];
	if (!this.xformNode && !node) {
		// console.log("str = " + str);
		if (str.indexOf(':') != -1) {
			node = this.refElem.xpath("./" + str).iterateNext();
		} else {
			node = this.refElem.xpath("./xfdl:" + str).iterateNext();
		}

	}

	return node;

};

/**
 * Устанавливает значение какого либо из свойств как в html так и в xfdl
 * 
 * @param {String}
 *            nameProperty - имя свойства
 * @param {String}
 *            value - значение
 * @param {Boolean=}
 *            modeSynhrHTML - требется ли синхронизация с html(по умолчанию нет)
 * 
 * 
 * @this InfoElement
 */
InfoGlobalElement.prototype.setProperty = function(nameProperty, value,
		modeSynhrHTML) {

	// console.logWorkScript(this.fullSidRef);
	/**
	 * @type HTMLElement
	 */
	var htmlEl = this.refHTMLElem;

	/**
	 * @type String
	 */
	var type = this.typeEl;

	/**
	 * @type Element
	 */
	var el = this.refElem;

	/**
	 * тэг в котором содержиться свойство
	 * 
	 * @type EmulateNode|Node|Element
	 */
	var nodeProperty = null;
	while (true) {

		if (nameProperty.indexOf("custom:") != '-1') {
			nodeProperty = this.hiddenTeg[nameProperty];
			if (!this.xformNode && !nodeProperty) {
				nodeProperty = el.xpath("./" + nameProperty).iterateNext();
				if (!nodeProperty)// если такого узла нет то его можно
									// добавить
				{
					nodeProperty = xfdlForm.createElementNS(
							getNameSpaceForXfdlForm("custom"), nameProperty);
					el.appendChild(nodeProperty);
				}
			}
			if (nodeProperty) {
				nodeProperty.setValue(value);
			} else {
				console.trace();
				console.warnSet("Попытка установить отсутсвующее свойство "
						+ nameProperty + " у элемента " + this.fullSidRef
						+ " с типом " + this.typeEl);

			}

			break;
		}
		if (nameProperty == "value") {
			// console.logCall("call setproperty value");
			if (this.xformNode) {
				nodeProperty = this.hiddenTeg['value'];
				nodeProperty.setValue(value);

				var valueForXformLink;
				// если есть ссылка на узел в xform-модели
				if (this.xDifVal && true) {
					valueForXformLink = formatValueForXform(value,
							this.dataType, this.pattern);

				} else {
					valueForXformLink = value;
					// if (this.sid == "FIELD1")
					// console.log(valueForXformLink);
				}

				this.xformNode.setValue(valueForXformLink);
				/**
				 * 
				 * @type {Array|DuplexXtoX[]}
				 */

				// var massDuplexLinks;// = this.xformNode.getXformLinks();
				sinchrXtoX(this.xformNode, this);

			} else
			// если нет ссылки то сохраняем значение внутри данного item'а
			{
				/**
				 * @type Element
				 */
				var valueEl;
				if (valueEl = (el.getElementsByTagNameNS(
						"http://www.ibm.com/xmlns/prod/XFDL/7.5", "value")
						.item(0) || el.getElementsByTagName("value").item(0))) {

				} else {
					el.appendChild(xfdlForm.createTextNode("   "));
					valueEl = xfdlForm.createElement("value");
					el.appendChild(valueEl);
					el.appendChild(xfdlForm.createTextNode("\n      "));
				}
				if (this.typeEl == "check") {
					if (value == "true" || value == "on")
						value = "on";
					else
						value = "off";
				}
				valueEl.setValue(value);

				// TODO синхронизация

			}
			if (modeSynhrHTML) {
				switch (type) {
					case "field" :
						// console.log("2)value = "+ value);
						htmlEl.value = value;
						break;
					case "button" :
						// console.log("\n!!value = "+value);
						// console.trace();
						// htmlEl.setAttribute("value", value);
						UtilsHTMLEl.setValue(htmlEl, value);
						break;
					case "combobox" :
						// console.log("\n!!value = "+value);
						htmlEl.value = value;
						break;
				}

			}
			break;
		}
		if (nameProperty == "focuseditem")// может содераться только в
		// page->global
		{
			// TODO:синхронизация
			this.hiddenTeg["focuseditem"].setValue(value);
			break;
		}

		if (nameProperty == "height" || nameProperty == "itemlocation[height]") {
			if (this.xformNode == null && false) {
				this.height = value;
				recaculacted();
				this.hiddenTeg['height'].setValue(value);
			}

			this.height = value;

			// if (this.typeEl == "field" || this.typeEl == "textarea")
			// {
			// // console.log("new height")
			// this.refHTMLElemDiv.style.height = value + "px";
			// this.refHTMLElem.style.height = value + "px";
			// }
			// TODO досмотреть

			break;
		}
		if (nameProperty == "active") {
			if (value == "off") {
				this.refHTMLElem.setAttribute("disabled", "disabled");
				this._getTeg('active').setValue("off");
			}
			if (value == "on") {
				this.refHTMLElem.removeAttribute("disabled");
				// console.log(this);
				this._getTeg('active').setValue("on");

			}
			break;
		}
		if (nameProperty == "readonly") {
			if (value == "off") {
				this.refHTMLElem.setAttribute("readonly", "readonly");
				this._getTeg('readonly').setValue("off");
			}
			if (value == "on") {
				this.refHTMLElem.removeAttribute("readonly");
				// console.log(this);
				this._getTeg('readonly').setValue("on");

			}
			break;
		}

		if (nameProperty == "activated") {
			if (value == "off") {
				// this.refHTMLElem.setAttribute("disabled", "disabled");
				this.hiddenTeg['activated'].setValue("off");
			} else {
				// this.refHTMLElem.removeAttribute("disabled");
				this.hiddenTeg['activated'].setValue("on");
			}
			break;
		}

		if (nameProperty == "dirtyflag") {
			this.hiddenTeg['dirtyflag'].setValue(value);
			break;
		}

		console.log("Попытка установить отсутсвующее свойство " + nameProperty
				+ " в " + value + " у элемента " + this.fullSidRef
				+ " с типом " + this.typeEl);

		break;
	}
};
/**
 * Обновить значение взяв новое из xform-узла. Потребность в вызове такой
 * функции вызвана тем, что на один xform-узел могут опираться несколько
 * xfdl-элементов. Эта функции вызывается только из функциии <b>sinchrXtoX()</b>;
 */
InfoElement.prototype.updateValueFromXForm = function() {

	// console.logCall("call updateValueFromXForm()");

	// console.logObj(this);

	/**
	 * @type HTMLElement
	 */
	var htmlEl = this.refHTMLElem;
	// console.logObj(htmlEl);
	var value;

	var type = this.typeEl;

	var valueForXformLink = ieXmlDom != null ? this.xformNode.text : this.xformNode.getValue();
	console.log("valueForXformLink = " + valueForXformLink);
	// если есть ссылка на узел в xform-модели
	if (this.xDifVal && true) {
		value = formatValueFromXform(valueForXformLink, this.dataType,
				this.pattern);

	} else {
		value = valueForXformLink;
		// if (this.sid == "FIELD1")
		// console.log(valueForXformLink);
	}
	var nodeProperty = this.hiddenTeg['value'];
	nodeProperty.setValue(value);

	switch (type) {
		case "field" :
			console.log("2)value = " + value);
			htmlEl.value = value;
			break;
		case "button" :
			// console.log("\n!!value = "+value);
			htmlEl.setAttribute("value", value);
			break;
		case "combobox" :
			console.log("\n!!value = " + value);
			htmlEl.value = value;
			break;
	}

}

/**
 * получить значение свойства по его имени
 * 
 * @param {String}
 *            nameProperty
 * @return {String|Number} - искомое значение или null если его нет
 */
InfoGlobalElement.prototype.getProperty = function(nameProperty) {

	/**
	 * @type String
	 */
	var type = this.typeEl;
	/**
	 * @type Element
	 */
	var el = this.refElem;

	/**
	 * @type {String|Number}
	 */
	var rezValue = null;

	while (true) {
		if (nameProperty == "value") {
			if (this.xformNode) {
				var valueForXformLink = ieXmlDom != null ? this.xformNode.text : this.xformNode.getValue();

				// если есть ссылка на узел в xform-модели
				if (this.xDifVal)
					rezValue = formatValueFromXform(valueForXformLink,
							this.dataType, this.pattern);
				else
					rezValue = valueForXformLink;

			} else
			// если нет ссылки то берем значение внутри данного item'а
			{
                if(ieXmlDom != null) {
                    rezValue = (el.getElementsByTagNameNS(
                            "http://www.ibm.com/xmlns/prod/XFDL/7.5", "value")
                        .item(0) || el.getElementsByTagName("value").item(0))
                        .text;
                } else {
				    rezValue = (el.getElementsByTagNameNS(
						"http://www.ibm.com/xmlns/prod/XFDL/7.5", "value")
						.item(0) || el.getElementsByTagName("value").item(0))
						.getValue();
                }
			}

			break;
		}
		if (nameProperty == "focuseditem")// может содераться только в
		// page->global
		{
			// TODO:синхронизация
			rezValue = ieXmlDom != null ? this.hiddenTeg["focuseditem"].text : this.hiddenTeg["focuseditem"].getValue();
			break;
		}
		if (nameProperty == "height") {
			rezValue = this.height;

			break;
		}
		if (nameProperty == "") {
			break;
		}
		if (nameProperty == "") {
			break;
		}
		break;
	}
	return rezValue;
}
