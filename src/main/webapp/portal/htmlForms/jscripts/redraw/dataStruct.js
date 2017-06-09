/**
 * @include "../includes.js"
 */

/**
 * @class
 * @constructor
 */
function InfoGlobalPage() {

	/**
	 * ссылка на элемент в DOM-дереве
	 * 
	 * @type Element
	 */
	this.refElem = null;

	/**
	 * sid элемента
	 * 
	 * @type String
	 * 
	 * @Note: совпадает c <b>fullSidRef</b>
	 */
	this.sid = null;

	/**
	 * полная ссылка по sid'ам
	 * 
	 * @Note: совпадает c <b>sid</b>
	 * @type String
	 */
	this.fullSidRef = null;

	/**
	 * ссылка на пункт global типа InfoGlobalElement
	 * 
	 * @type InfoGlobalElement
	 */
	this.globalEl = null;

	this.typeEl = "globalpage";

}

/**
 * объект содержащий информацию об элементе &lt;global> в &lt;globalpage>
 * 
 * @note такой объет всегда только один, работать с ним можно через переменную
 *       globalInfo или через treeInfoPages.getGlobalPage().globalEl
 * @constructor
 */
function InfoGlobalElement() {
	/**
	 * ссылка на элемент в DOM-дереве
	 * 
	 * @type Element
	 */
	this.refElem = null;

	/**
	 * sid элемента
	 * 
	 * @type String
	 */
	this.sid = null;

	/**
	 * полная ссылка по sid'ам
	 * 
	 * @Note: заполняется через "." а не через "-"
	 * @type String
	 */
	this.fullSidRef = null;

	/**
	 * полная ссылка по сидам на родительский элемент
	 * 
	 * @type String
	 */
	this.parentFullSidRef = null;

	this.typeEl = "global";

	/**
	 * Ассоциативный массив для значений скрытых тегов типа Node. <br/>Хранит
	 * значение по ключу, который является именем тэга.<br/>
	 * 
	 * <br/>&#x9;Для всех элементов в случае, если &lt;value> связан с xform
	 * hiddenTeg['value'] указывает на связанный эмулируемый узел, который не
	 * существует в реальности, т.е. это не узел из xform-модели, однако через
	 * него удобнее работать чем через сам xform-узел
	 * 
	 * <br/>&#x9;Также данный массив хранит ссылки не только вообще не
	 * отображаемые тэги(см. ниже), но и на тэги(они эмулируются) в которых есть
	 * скрипты. Это сделано потому что наличие xform-связи запрещает менять
	 * содержимое реального тэга
	 * 
	 * <br/>&#x9;Для <b>button</b>: <br/>&#x9;&#x9;activated - on , off
	 * 
	 * <br/>&#x9;Для <b>global</b>: <br/>&#x9;&#x9;activated - on , off<br/>&#x9;&#x9;focusitem -
	 * sid пункта на котором находиться фокус, в случае нахождение это пункта
	 * внутри табицы или панели работает не так как во вьювере
	 * 
	 * <br/>&#x9;Для <b>field</b>:
	 * 
	 * <br/>&#x9;Для <b>check</b>:
	 * 
	 * @type Node[]
	 */
	this.hiddenTeg = new Object();

	/**
	 * дополнительная информация
	 * 
	 * @type Object
	 */
	this.addData = new Object();

	/**
	 * узел, в который происходит запись значения
	 * 
	 * @type Node|Element
	 * @note: всегда null, сделано для совместимости с InfoElement
	 */
	this.xformNode = null;

};

/**
 * @class
 * @constructor
 */
function InfoPage() {

	/**
	 * sid элемента
	 * 
	 * @type String
	 * 
	 * @Note: совпадает c <b>fullSidRef</b>
	 */
	this.sid = null;

	/**
	 * полная ссылка по sid'ам
	 * 
	 * @Note: совпадает c <b>sid</b>
	 * @type String
	 */
	this.fullSidRef = null;

	/**
	 * 
	 * @type String
	 */
	this.toolbarSid = null;

	/**
	 * 
	 * @type InfoElement
	 */
	this.toolbarInfo = null;

	/**
	 * ссылка на пункт global типа InfoElement
	 * 
	 * @type InfoElement
	 */
	this.globalEl = null;

	this.isActive = false;

	this.x1 = -1;
	this.x2 = -1;
	this.y1 = -1;
	this.y2 = -1;
	this.width = -1;
	this.height = -1;
	/**
	 * ссылка на элемент в DOM-дереве
	 * 
	 * @type Element
	 */
	this.refElem = null;
	/**
	 * sid элемента
	 * 
	 * @type String
	 */
	this.sid = null;
	/**
	 * полная ссылка по sid'ам
	 * 
	 * @Note: заполняется через "." а не через "-"
	 * @type String
	 */

	this.fullSidRef = null;
	/**
	 * ссылка на элемент в DOM-дереве html
	 * 
	 * @type HTMLElement
	 */
	this.refHTMLElem = null;
	/**
	 * ссылка на контейнер(div) в DOM-дереве html
	 * 
	 * @type HTMLDivElement|HTMLElement
	 */
	this.refHTMLElemDiv = null;
	this.typeEl = "page";

	/**
	 * является ли данная страница активной(отображаемой)
	 * 
	 * @readonly
	 * @type Boolean
	 */
	this.isActive = false;

	/**
	 * 
	 * @type HTMLDivElement
	 */
	this.toolbarDiv = null;
	/**
	 * 
	 * @type HTMLDivElement
	 */
	this.contentDiv = null;
	/**
	 * 
	 * @type HTMLDivElement
	 */
	this.pageDiv = null;
	/**
	 * первый элемент на странице
	 * 
	 * @type InfoElement
	 */
	this.itemFirstInfo = null;

	/**
	 * последний элемент на странице
	 * 
	 * @type InfoElement
	 */
	this.itemLastInfo = null;
}

/**
 * объект содержащий информацию об элементе(далее именуемый объект-связка)
 * 
 * @constructor
 */
function InfoElement() {
	/**
	 * @type Number
	 */
	this.x1 = -1;

	/**
	 * @type Number
	 */
	this.y1 = -1;

	/**
	 * @type Number
	 */
	this.x2 = -1;

	/**
	 * @type Number
	 */
	this.y2 = -1;

	/**
	 * @type Number
	 */
	this.width = -1;

	/**
	 * @type Number
	 */
	this.height = -1;

	/**
	 * размер шрифта в пикселях
	 * 
	 * @type Number
	 */
	this.fontSize = 12;

	/**
	 * тип шрифта
	 * 
	 * @type String
	 */
	this.fontName = "Helvetica";

	/**
	 * ссылка на элемент в DOM-дереве
	 * 
	 * @type Element
	 */
	this.refElem = null;

	/**
	 * ссылка на элемент в DOM-дереве html
	 * 
	 * @type HTMLElement
	 */
	this.refHTMLElem = null;

	/**
	 * ссылка на контейнер(div) в DOM-дереве html
	 * 
	 * @type HTMLDivElement|HTMLElement
	 */
	this.refHTMLElemDiv = null;

	/**
	 * sid элемента
	 * 
	 * @type String
	 */
	this.sid = null;

	/**
	 * полная ссылка по sid'ам
	 * 
	 * @Note: заполняется через "." а не через "-"
	 * @type String
	 */
	this.fullSidRef = null;

	/**
	 * полная ссылка по сидам на родительский элемент
	 * 
	 * @type String
	 */
	this.parentFullSidRef = null;

	/**
	 * xpath-ссылка на xform-узел в который происходит запись значения.
	 * 
	 * @type String
	 * 
	 * <br/><br/><b>Note:</b> содержит 'instance()'
	 */
	this.xformRef = null;

	/**
	 * узел, в который происходит запись значения
	 * 
	 * @type Node|Element *
	 * @note: если есть xform-связка то все новые значения не будут записываться
	 *        в xfdl-тэги. К примеру, если скрипт меня значения в тэге
	 *        &lt;height> то сам тэг не измениться однако изменения высоты будут
	 *        отображены. <br/> Кроме того чтобы изменять значения при имении
	 *        xform-связи кроме &lt;value> надо чтобы скрипт находился в самом
	 *        тэге, иначе будет ошибка
	 * 
	 */
	this.xformNode = null;

	/**
	 * xpath-ссылка на xform-узел в котором хранится лейбл.
	 * 
	 * @type String
	 * 
	 * <br/><br/><b>Note:</b> содержит 'instance()'
	 */
	this.xformRefForLabel = null;

	/**
	 * узел, в котором хранится лейбл.
	 * 
	 * @type {Node|Element}
	 */
	this.xformLabel = null;

	/**
	 * различаюттся ли значения для отображения и связанных xform-узлов. К
	 * примеру для типов date и time отображаемое и хранящиеся в xform-модели
	 * значения могут различатся. Так же для пунктов check и popup.
	 * 
	 * @type Boolean
	 * 
	 */
	this.xDifVal = false;

	/**
	 * тип элемента: label, check, field, pane и т.д.
	 * 
	 * @type String
	 */
	this.typeEl = null;

	/**
	 * значение элемента
	 * 
	 * @type String
	 */
	this.value = null;

	/**
	 * слушающие его элементы(item'ы), т.е. те которые поменя.т свои некоторые
	 * значения при изменении данного элемента
	 * 
	 * @type InfoElement[]
	 */
	this.listeningToitems = new Array();

	/**
	 * те элементы(item'ы) за которыми наблюдает данный, т.е. при изменении
	 * которых данный поменяет своё значение
	 * 
	 * @type InfoElement[]
	 */
	this.watchItems = new Array();

	/**
	 * тип данных item'а (date, time, float, string, check и т.д)
	 * 
	 * 
	 * <b>Note:</b> Может не совпадать с format/datatype, т.к. может быть
	 * равным 'check'
	 * 
	 * @type {String}
	 */
	this.dataType = null;

	/**
	 * шаблон для значения
	 * 
	 * @type {String}
	 */
	this.pattern = null;

	/**
	 * Ассоциативный массив для значений скрытых тегов типа Node(EmulateNode).
	 * <br/>Хранит значение по ключу, который является именем тэга.<br/>
	 * 
	 * <br/>&#x9;Для всех элементов в случае, если &lt;value> связан с xform
	 * hiddenTeg['value'] указывает на связанный эмулируемый узел, который не
	 * существует в реальности, т.е. это не узел из xform-модели, однако через
	 * него удобнее работать чем через сам xform-узел
	 * 
	 * <br/>&#x9;Также данный массив хранит ссылки не только вообще не
	 * отображаемые тэги(см. ниже), но и на тэги(они эмулируются) в которых есть
	 * скрипты. Это сделано потому что наличие xform-связи запрещает менять
	 * содержимое реального тэга
	 * 
	 * <br/>&#x9;Для <b>button</b>: <br/>&#x9;&#x9;activated - on , off
	 * 
	 * <br/>&#x9;Для <b>global</b>: <br/>&#x9;&#x9;activated - on , off<br/>&#x9;&#x9;focusitem -
	 * sid пункта на котором находиться фокус, в случае нахождение это пункта
	 * внутри табицы или панели работает не так как во вьювере
	 * 
	 * <br/>&#x9;Для <b>field</b>:
	 * 
	 * <br/>&#x9;Для <b>check</b>:
	 * 
	 * @type Node[]
	 */
	this.hiddenTeg = new Object();

	/**
	 * дополнительная информация
	 * 
	 * @type Object
	 */
	this.addData = new Object();

	/**
	 * true у инициатора пересчета координат. по окончании переасчета
	 * сбрасывается.
	 * 
	 * @type Boolean
	 */
	this.isInitiator = false;

	/**
	 * объект информации о шрифте
	 * 
	 * @type FontInfoObj
	 */
	this.fontInfoObj = null;
	/**
	 * объект информации о формате значения
	 * 
	 * @type FormatObj
	 */
	this.formatObj = null;

	/**
	 * как продить выравнивание текста.<br/> допустимые значения: left, right,
	 * center, lead и trial. trail
	 * 
	 * @type String
	 */
	this.justify = "left";

	/**
	 * подписан ли элемент
	 * 
	 * @type Boolean
	 */
	this.isSigner = false;

	/**
	 * ассоциативный массив имен подписанных опций
	 * 
	 * @type Object
	 */
	this.SignerOptions = {};

	/**
	 * ссылка на тэг &lt;signature> котоый содержит подпись(имеется только для
	 * кнопки подписи)
	 * 
	 * @type Element
	 */
	this.refTegSignature = null;

	/**
	 * состоние валидности(0 - валиден, 1 - требует ввода,2 - не соответсвует
	 * шаблону)
	 * 
	 * @type Number
	 */
	this.invalidState = 0;

	/**
	 * 
	 * @type Bounds
	 */
	this.bounds = null;

	/**
	 * ссылка на следующий отрисованный пункт(аналог <b>&lt;itemnext></b>)
	 * 
	 * @type InfoElement
	 */
	this.itemNextInfo = null;

	/**
	 * ссылка на предыдущий отрисованный пункт(аналог <b>&lt;itemprevios></b>)
	 * 
	 * @type InfoElement
	 */
	this.itemPreviousInfo = null;

	/**
	 * строка-ссылка на элемент в котором содержится контент для картинки
	 * 
	 * @note только для типа label, проверка на <b>null</b> дает возмоность
	 *       узнать о наличии тэга &lt;image> в элементе
	 * @type String
	 */
	this.imageRef = null;

	/**
	 * массив элементов, которые завязаны на данный элемент
	 * 
	 * 
	 * @type InfoElement[]
	 * @note только для типа data
	 */
	this.massRelElsImage = null;

	/**
	 * тип действия(once - один раз, repeat - повторно)
	 * 
	 * @type String
	 * @note только для типа <b>action</b>
	 */
	this.delayType = null;
	/**
	 * временная задержка(если -1 то действие выполняется только один раз до
	 * отображения страницы)
	 * 
	 * @type Number
	 * @note только для типа <b>action</b>
	 */
	this.delayInterval = null;
	/**
	 * id-таймера запущенного для данного action
	 * 
	 * @type Number
	 * @note только для типа <b>action</b>
	 */
	this.idTimer = null;

	/**
	 * функция запускаемая при активации кнопки или действия
	 * 
	 * @type Function
	 * @note только для типа <b>action</b> и <b>button</b>
	 */
	this.actionFunction = null;
	/**
	 * тип действия при активации
	 * 
	 * @type String
	 * @note только для типа <b>action</b> и <b>button</b>. Для <b>cell</b>
	 *       пока нет реализации
	 * 
	 * @addInfo типы действий:<br/> &#x9;cancel <br/> &#x9;display <br/>
	 *          &#x9;done <br/> &#x9;enclose <br/> &#x9;extract <br/> &#x9;link
	 *          <br/> &#x9;pagedone <br/> &#x9;print <br/> &#x9;refresh <br/>
	 *          &#x9;remove <br/> &#x9;replace <br/> &#x9;saveform <br/>
	 *          &#x9;saveas <br/> &#x9;select <br/> &#x9;signature(только для
	 *          button) - кнопка для создания подписи <br/> &#x9;submit <br/>
	 * 
	 */
	this.typeAction = null;

	/**
	 * 
	 * @type String
	 */
	this.url = null;
	this.sidSignature = null;

};

/**
 * функция для определения/замены именованного параметра
 * 
 * @param {String}
 *            param - имя параметра
 * @param {Number||Element||String||Object}
 *            value - значение параметра
 * @this {InfoElement}
 */
InfoElement.prototype.addInfo = function(param, value) {
	this[param] = value;
};

/**
 * функция для обновления границ
 * 
 * @param {Bounds}
 *            newBounds - новые значения границ
 */
InfoElement.prototype.updateBounds = function(newBounds) {
	//info.bounds = bounds;
	this.bounds = newBounds;
	this.x1 = newBounds.x1;
	this.y1 = newBounds.y1;
	this.x2 = newBounds.x2;
	this.y2 = newBounds.y2;
	this.width = newBounds.width;
	this.height = newBounds.height;
};


/**
 * проверяет на соответсвие шаблону переданное значение
 * 
 * @param {String} -
 *            проверяемое значение(или текущее если не передан первый параметр)
 * @return {Number} вернет <b>0</b> если значение соответсвует, <b>1</b> если
 *         значение не соответсвует т.к. оно пустое(используется для выявления
 *         mandatory), <b>2</b> если значение не соответсвует т.к. не подходит
 *         под шаблон
 */
InfoElement.prototype.isInvalidValue = function() {
	/**
	 * @type String
	 */
	var value;
	if (arguments[0] !== null || arguments[0] !== undefined)
		value = arguments[0];
	else
		value = this.getProperty('value');
	if (value == "")
		if (this.formatObj.crSetting.mandatory)
			return 1;
		else
			return 0;

	if (this.formatObj.crSetting.patterns) {
		switch (this.formatObj.datatype) {
			case 'string' :
				for (var i = 0; i < this.formatObj.crSetting.patterns.length; i++) {
					var res = (new RegExp(this.formatObj.crSetting.patterns[i]))
							.exec(value);
					if (!res || res[0] != value)
						return 2;
				}
				break;
			case 'date' :
				for (var i = 0; i < this.formatObj.crSetting.patterns.length; i++) {
					var a = formatValueForXform(value,'date',this.formatObj.crSetting.patterns[i]);
					var b = formatValueFromXform(a,'date',this.formatObj.crSetting.patterns[i]);
					if (value!=b){
						return 2;
					}
				}
				break;
		}
	}

	return 0;
}
/**
 * обновить картинку для элемента(вызывается при смене содержимого в тэга
 * &lt;data>, и при отрисовке)
 * 
 * @note атоматически добавляет, но не удаляет, связи между элементами data и
 *       label
 * 
 */
InfoElement.prototype.updateImage = function() {
	console.logCall("call InfoElement.prototype.updateImage ");
	if (this.typeEl == 'label') {
		/**
		 * @type InfoElement
		 */
		var dataEl = treeInfoEls.getElemInfo(this.getProperty('image'), this);
		if (dataEl) {
			dataEl.addLinkForData(this);

			var mimetype = dataEl.getProperty('mimetype');
			/**
			 * @type Element
			 */
			var mimedataEl = dataEl._getTeg('mimedata');
			var typeEncoding = mimedataEl.getAttribute('encoding');
			// console.log(mimedataEl.getValue());
			if (typeEncoding.toLowerCase().trim() == "base64") {
				this.refHTMLElem.setAttribute("src", "data:" + mimetype
								+ ";base64," + ieXmlDom != null ? mimedataEl.text : mimedataEl.getValue());
			}
		}
		if (typeEncoding.toLowerCase().trim() == "base64-gzip") {
			var decodeImg = window.parent.document.applet_adapter
					.decompressBase64GZip(ieXmlDom != null ? mimedataEl.text : mimedataEl.getValue());
			this.refHTMLElem.setAttribute("src", "data:" + mimetype
							+ ";base64," + decodeImg);
		} else
			this.refHTMLElem.setAttribute("src", "data:image/jpeg;base64,");

	}

};

/**
 * устанавливает фон поля в зависимости от соотвествия переданного значения
 * шаблону(если ничего не передано, то берется текущее)
 * 
 * @param {String}
 *            value - значение(по умолчанию текущее)
 * @note автоматически удаляет/добавляет поле в карту <b>mapMandatoryItems</b>
 */
InfoElement.prototype.setBackGroundForInvalid = function(value) {
	if (!this.formatObj)
		return;
	if (this.invalidState==2) saveError--;
	this.invalidState = this.isInvalidValue(value);
	// console.log( this.invalidState);
	switch (this.invalidState) {
		case 0 :
			mapMandatoryItems.delItem(this);
			this.refHTMLElem.style.backgroundColor = objStateDrawing.colorBackgroundNormal;
			break;
		case 1 :
			mapMandatoryItems.addItem(this);
			this.refHTMLElem.style.backgroundColor = objStateDrawing.colorBackgroundMandatory;
			break;
		case 2 :
			mapMandatoryItems.addItem(this);
			saveError++;
			this.refHTMLElem.style.backgroundColor = objStateDrawing.colorBackgroundError;
			break;
	}
	
	/*var win = $(window.parent.document.getElementById('viewerInWinId'));
	var text = win.find('.apply_butt_div div')[2];
	if (saveError>0){
		text.style.color = '#AAAAAA';
	} else {
		text.style.color = '#40515D';
	}*/
}
/**
 * добавить ссылку в массив связей для элемента типа <b>data</b>
 * 
 * @note сам массив пока используется только для внутренних функций
 * @param {InfoElement}
 *            linkEl
 */
InfoElement.prototype.addLinkForData = function(linkEl) {
	if (this.typeEl == 'data')
		if (this.massRelElsImage) {
			if ($.inArray(linkEl, this.massRelElsImage) == -1)
				this.massRelElsImage.push(linkEl);
		} else {
			this.massRelElsImage = new Array();
			this.massRelElsImage.push(linkEl);
		}
}
/**
 * далить ссылку в массиве связей для элемента типа <b>data</b>
 * 
 * @note сам массив пока используется только для внутренних функций
 * @param {InfoElement}
 *            linkEl
 */
InfoElement.prototype.delLinkForData = function(linkEl) {
	if (this.typeEl == 'data')
		if (this.massRelElsImage) {
			var tmpIndex;
			if ((tmpIndex = $.inArray(linkEl, this.massRelElsImage)) != -1)
				this.massRelElsImage.splice(tmpIndex, 1);
		}
}
