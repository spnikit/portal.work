/**
 * @include "../includes.js"
 */

/**
 * Получить дочерний тэг по имени. пспользуется как прослойка, чтобы не
 * проверять нахождение данного тэга в массиве <b>hiddenTeg</b>, среди дочерних
 * тэгов или <b>xform</b>-модели<br/>
 * 
 * пспользуется для вешания скриптов-слушаетелей на искомый тэг и когда
 * требуются атрибуты реального тэга(к примеру <b>encoding</b>)
 * 
 * 
 * 
 * @param {String}
 *            str - имя тэга
 * 
 * @return {Node|EmulateNode} тэг или <b>null</b> если такого не имеется
 */
InfoElement.prototype._getTeg = function(str) {
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
 *            modeSynhrHTML - требется ли синхронизация с html(по умолчанию нет,
 *            используется при изменении свойст из скриптов).пмеет значения
 *            только если текущее свойство могло придти как из html так и из
 *            скриптов
 * 
 * 
 * @this InfoElement
 */
InfoElement.prototype.setProperty = function(nameProperty, value, modeSynhrHTML) {
	if (this.SignerOptions[nameProperty] && false)// это сделано на уровне
	// Node
	{
		// TODO если делать здесь то надо ввести проверку на совпадение с
		// предыдущим
		// значением
		// console.trace();
		var e = new Error();
		e.isMyErr = true;
		e.name = "Ошибка";
		e.message = "Попытка установить подписанное свойство "
				+ this.fullSidRef + "-" + nameProperty
				+ ".\nДальнейшая работа с формой будет прекращена";
		// TODO сделать прекращение работы с формой
		alert(e.message);
		throw e;
	}
	// console.logWorkScript(this.fullSidRef + "-" + nameProperty);
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
	/**
	 * функция передаваемая вторым параметром в setValue
	 * 
	 * @type Function
	 */
	var afterFunc = null;

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
		if (nameProperty == "value" && isGlobalEdit) {
			// console.logCall("call setproperty value");
			// если есть ссылка на узел в xform-модели
			
			if (this.sid=='BUTTONSGN1'){
				//var win = $(window.parent.document.getElementById('viewerInWinId'));
				//var text = win.find('.apply_butt_div div')[1];
				if (value==0){
					//text.style.color = '#AAAAAA';
					maySign = 0;
				} else {
					//text.style.color = '#40515D';
					maySign = 1;
				}
			}
			
			if (this.xformNode) {
				// TODO сделать обработку ситуации когда значение приходящее из
				// скриптов
				// не совсем соответсвует шаблону, но соответсвует дефолтному
				// шадлону
				// для данного типа

				nodeProperty = this.hiddenTeg['value'];
				if (nodeProperty != null)
					nodeProperty.setValue(value);

				var valueForXformLink;
				var tmpVal;

				if (this.typeEl == 'check')// TODO добавить radio?
				{
					valueForXformLink = formatValueForXform(value, this.typeEl);
					tmpVal = formatValueFromXform(valueForXformLink,
							this.typeEl);
				} else {
					if (this.formatObj
							&& this.formatObj.prSetting.keepformatindata == 'on') {
						// console.log('111');
						valueForXformLink = value;
						tmpVal = value;
					} else {
						// console.log('222');
						// valueForXformLink = formatValueForXform(value,
						// this.formatObj.datatype,
						// this.formatObj.prSetting.pattern);
						//						
						// tmpVal = formatValueFromXform(valueForXformLink,
						// this.formatObj.datatype,
						// this.formatObj.prSetting.pattern);
					}
				}
				// moment(value, fromPattern).format(toPattern);

				// если вдруг обратное преобразование не дало этого же
				// результата,то
				// значит, значение из xform-связи не соответсвует шаблону
				if (tmpVal != value)
					valueForXformLink = value;

				// console.log("valueForXformLink = " + valueForXformLink);
				// console.log("tmpVal = " + tmpVal);
				// console.log("value = " + value);
				this.xformNode.setValue(valueForXformLink);
				/**
				 * 
				 * @type {Array|DuplexXtoX[]}
				 */
				// var massDuplexLinks;// = this.xformNode.getXformLinks();
				sinchrXtoX(this.xformNode, this, 'value');

			} else
			// если нет ссылки то сохраняем значение внутри данного item'а
			{
				
				if (this.typeEl == "popup"){
					if (value.indexOf('pagedone')==0){
						var ref = value.split('.')[1].substring(1);
						var newpage = treeInfoPages.getElemInfo(ref);
						var oldpage = treeInfoPages.getElemInfo(this.parentFullSidRef);
						oldpage.refHTMLElem.style.display = 'none';
						newpage.refHTMLElem.style.display = '';
						currentPage = htmlEl.selectedIndex;
						for (var i=0;i<htmlEl.options.length;i++){
							if (htmlEl.options[i].getAttribute("selected")=='selected'){
								htmlEl.value = htmlEl.options[i].value;
								htmlEl.selectedIndex = i;
								break;
							}
						}
						break;
					}
				}
				
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

			var childEl = el.firstChild;
			while (childEl) {
				if (childEl.nodeType == Node.ELEMENT_NODE
					&& childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")
						&& (childEl.tagName.replace(new RegExp('.+:','g'), '') == "input"
							|| childEl.tagName.replace(new RegExp('.+:','g'), '') == "select1")) {
					var path = childEl.getAttribute("ref");
					if (xFormsScriptMass[path]) {
						var arr = xFormsScriptMass[path];
						for (var i = 0; i < arr.length; i++) {
							if (arr[i]._event == 'xforms-value-changed')
								arr[i].work();
						}
					}
//					if (xFormsScriptMass[el.sid]) {
//						var arr = xFormsScriptMass[path];
//						for (var i = 0; i < arr.length; i++) {
//							if (arr[i]._event == 'xforms-value-changed')
//								arr[i].work();
//						}
//					}
				}
				childEl = childEl.nextSibling;
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
					case "check" :
						// console.log("\n!!value = "+value);
						htmlEl.checked = (value == 'on') ? true : false;
						break;
				}

			}

			this.setBackGroundForInvalid(value);
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

		if (nameProperty == "active" && isGlobalEdit) {
			if (value == "off") {
				if (this.refHTMLElem){
					this.refHTMLElem.setAttribute("disabled", "disabled");
					if (this.typeEl=='combobox' && this.formatObj
						&& this.formatObj.datatype=='date'){
						var calend = $(this.refHTMLElem.previousElementSibling.previousElementSibling);
						calend.datepicker( "destroy" );
					}
				}
				else if (this.typeEl == 'action') {
					afterFunc = function() {
						if (this.idTimer)// останавливаем таймер если он есть
						{
							this.delayType == 'once'
									? clearTimeout(this.idTimer)
									: clearInterval(this.idTimer);
							this.idTimer = null;
						}
					};
				}
				this._getTeg('active').setValue("off", afterFunc);
			}
			if (value == "on") {
				if (this.refHTMLElem){
					this.refHTMLElem.removeAttribute("disabled");
					if (this.typeEl=='combobox' && this.formatObj
						&& this.formatObj.datatype=='date'){
							if (!this.isSigner){
								var calend = $(this.refHTMLElem.previousElementSibling.previousElementSibling);
								addDateWidget(calend, this.refHTMLElem);
							}
					}
				}
				else if (this.typeEl == 'action') {
					var self = this;
					afterFunc = function() {
						if (!this.idTimer)// запускаем таймер
						{
							this.delayType == 'once'
									? this.idTimer = setTimeout(function() {
												self.setProperty('activated',
														'on');
												self.setProperty('activated',
														'off');
											}, this.delayInterval)
									: this.idTimer = setInterval(function() {
												self.setProperty('activated',
														'on');
												self.setProperty('activated',
														'off');
											}, this.delayInterval);

						}

					};
				}
				// console.log(this);
				if (this._getTeg('active'))
					this._getTeg('active').setValue("on", afterFunc);

			}
			break;
		}
		if (nameProperty == "readonly" && isGlobalEdit) {
			// console.info('---1-1-1--1-1-1-');
			if (value == "off") {
				this.refHTMLElem.setAttribute("readonly", "readonly");
				this._getTeg('readonly').setValue("off");
			}
			if (value == "on" && !this.isSigner && isGlobalEdit) {
				this.refHTMLElem.removeAttribute("readonly");
				// console.log(this);
				this._getTeg('readonly').setValue("on");

			}
			break;
		}

		if (nameProperty == "activated") {
			switch (value) {
				case "off" : {
					// this.refHTMLElem.setAttribute("disabled", "disabled");
					this.hiddenTeg['activated'].setValue("off");
					break;
				}
				case "on" : {
					// this.refHTMLElem.removeAttribute("disabled");
					// console.log(this.hiddenTeg['activated']);

					var self = this;
					this.hiddenTeg['activated'].setValue("on", function() {
						if (self.actionFunction) {
							self.actionFunction(self);
						}
					});
					break;
				}
				case "maybe" : {
					// this.refHTMLElem.removeAttribute("disabled");
					this.hiddenTeg['activated'].setValue("maybe");
					break;
				}
			}
			break;
		}

		if (nameProperty == "dirtyflag") {
			this.hiddenTeg['dirtyflag'].setValue(value);
			break;
		}

		if (nameProperty == "mimedata") {
			this._getTeg('mimedata').setValue(value);
			if (this.massRelElsImage) {
				var i;
				for (i = 0; i < this.massRelElsImage.length; i++) {
					this.massRelElsImage[i].updateImage();
				}
			}
			break;
		}
		if (nameProperty == "mimetype") {
			this._getTeg('mimetype').setValue(value);
			break;
		}

		if (nameProperty == "scrollvert" && this.typeEl == 'field') {
			// console.info('scrollvert = '+value);
			switch (value) {
				case 'always' :
					this.refHTMLElem.style.overflowY = 'scroll';
					break;
				case 'wordwrap' :// TODO чем отличается от never?
					this.refHTMLElem.style.overflowY = 'visible';
					break;
				default :// never
					this.refHTMLElem.style.overflowY = 'visible';
					break;
			}
			this.hiddenTeg['scrollvert'].setValue(value);
			break;
		}

		if (nameProperty == "scrollhoriz" && this.typeEl == 'field') {
			switch (value) {
				case 'always' :
					this.refHTMLElem.style.overflowX = 'scroll';
					break;
				case 'wordwrap' :// TODO чем отличается от never?
					this.refHTMLElem.style.overflowX = 'visible';
					break;
				default :// never
					this.refHTMLElem.style.overflowX = 'visible';
					break;
			}
			this.hiddenTeg['scrollhoriz'].setValue(value);

			break;
		}
		if (nameProperty == "signer" && this.typeEl == 'button') {
			this._getTeg('signer').setValue(value);
			break;
		}
		if (nameProperty == "image") {
			var dataEl = treeInfoEls.getElemInfo(this.imageRef, this);
			if (dataEl)
				dataEl.delLinkForData(this);
			this.imageRef = value;
			this._getTeg('image').setValue(value);
			this.updateImage();
			break;
		}
		if (nameProperty == "visible") {
			switch (value) {
				case 'on' :
					this.refHTMLElem.style.visibility = 'visible';
				break;
				case 'off' :
					this.refHTMLElem.style.visibility = 'hidden';
				break;
			}
			this._getTeg('visible').setValue(value);
			break;
		}
		if (nameProperty == "printvisible") {
			switch (value) {
				case 'on' :
					this.refHTMLElem.className = this.refHTMLElem.className.replace('noPrint','');
					this.refHTMLElem.setAttribute("class", "forcePrint");
				break;
				case 'off' :
					this.refHTMLElem.className = this.refHTMLElem.className.replace('forcePrint','');
					this.refHTMLElem.setAttribute("class", "noPrint");
				break;
			}
			//this._getTeg('visible').setValue(value);
			break;
		}
		if (nameProperty == "itemlocation[below]") {
			var bounds = this.bounds;
			var node = xfdlForm.createElement("below");
			node.setValue(value);
			applyBounds(bounds, node, this.parentFullSidRef, true);
			this.updateBounds(bounds);
			
			var div = this.refHTMLElemDiv;
			div.style.top = this.y1 + "px";
			div.style.left = this.x1 + "px";
			div.style.width = this.width + "px";
			div.style.height = this.height + "px";
			break;
		}
		if (nameProperty == "bgcolor") {
			this.refHTMLElem.style.backgroundColor = value;
			break;
		}
		
		console.warnWorkScript("Попытка установить отсутсвующее свойство "
				+ nameProperty + " в " + value + " у элемента "
				+ this.fullSidRef + " с типом " + this.typeEl);
		console.trace();

		break;
	}
};
/**
 * Обновить значение взяв новое из xform-узла. Потребность в вызове такой
 * функции вызвана тем, что на один xform-узел могут опираться несколько
 * xfdl-элементов. Эта функции вызывается только из функциии <b>sinchrXtoX()</b>;
 */
InfoElement.prototype.updateProrertyFromXForm = function(property) {

	// console.logCall("call updateValueFromXForm()");

	// console.logObj(this);

	/**
	 * @type HTMLElement
	 */
	var htmlEl = this.refHTMLElem;
	// console.logObj(htmlEl);
	var value;

	var type = this.typeEl;

	switch (property) {
		case 'value' :
			var valueForXformLink = ieXmlDom != null ? this.xformNode.text : this.xformNode.getValue();
			// console.log("valueForXformLink = " + valueForXformLink);
			// если есть ссылка на узел в xform-модели
			if (this.typeEl == 'check')// TODO добавить radio?
			{
				value = formatValueFromXform(valueForXformLink, this.typeEl);
			} else {
				if (this.formatObj && this.formatObj.prSetting.keepformatindata) {
					value = valueForXformLink;
				} else {
					value = formatValueFromXform(valueForXformLink,
							this.formatObj.datatype,
							this.formatObj.prSetting.pattern);
				}
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
				case "check" :
					console.log("\n!!value = " + value);
					htmlEl.checked = (value == 'on') ? true : false;
					break;
			}
			break;

		default :
			console.warnDraw("Для свойства '" + property
					+ "' не задана xforms->xfdl синхронизация");
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
InfoElement.prototype.getProperty = function(nameProperty) {

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
			if(ieXmlDom != null)
                rezValue = this.hiddenTeg["focuseditem"].getValue();
            else
                rezValue = this.hiddenTeg["focuseditem"].text;
			break;
		}
		if (nameProperty == "height") {
			rezValue = this.height;

			break;
		}
		if (nameProperty == "mimetype") {
            if(ieXmlDom != null) {
                rezValue = this.refElem.getElementsByTagName('mimetype').item(0)
                    .text;
            } else {
			    rezValue = this.refElem.getElementsByTagName('mimetype').item(0)
					.getValue();
            }
			break;
		}
		if (nameProperty == "image") {
			rezValue = ieXmlDom != null ? this._getTeg('image').text : this._getTeg('image').getValue();
			break
		}

		if (nameProperty == "") {
			break;
		}
		var tmpTeg = this._getTeg(nameProperty);
		if (tmpTeg) {
			rezValue = ieXmlDom != null ? tmpTeg.text : tmpTeg.getValue();
			break;
		}

		console.warnWorkScript("Нет реализации для получения свойства "
				+ nameProperty + " у элемента " + this.fullSidRef);

		break;
	}
	return rezValue;
}
