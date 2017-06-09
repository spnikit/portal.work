/**
 * @include "includes.js"
 */

/**
 * сохранить значение из html в xfdl
 * 
 * @param {HTMLElement}
 *            htmlEl
 * @param {String}
 *            nameProperty - имя устанавливаемого
 *            параметра('value','focuseditem','activated' и т.д.)
 * @param {String}
 *            value
 */
function updateHTMLtoXFDL(htmlEl, nameProperty, value) {
	// console.logParam("htmlEl.id = " + htmlEl.id + "\nnameProperty = " +
	// nameProperty + "\nvalue = " + value);
	var infoEl = UtilsHTMLEl.getInfoElRef(htmlEl);
	if (!infoEl) {
		console.warnDraw("html-элементу с id = '" + htmlEl.id
				+ "' не уставлен infoElement напрямую");
		infoEl = treeInfoEls.getElemInfo(htmlEl.id);
		if (!infoEl) {
			var e = new Error();
			e.isMyErr = true;
			e.name = "Ошибка дерева InfoElement'ов";
			e.message = "Попытка получить отсутсвующий в дереве InfoElement'ов элемент с полной ссылкой '"
					+ htmlEl.id + "'";
			throw e;
		}

	}
	infoEl.setProperty(nameProperty, value, false);

}

function toolbar_save() {
	// saveFormOnServer();
	if (mapMandatoryItems.getNumberItems()) {
		if (!confirm('Предупреждение. Некоторые поля формы содержат недопустимые значения. Продолжить?'))
			return;

	}

	var contentForm;

	// if (browser == 'MSIE')
	// {
	// contentForm = replaceNewLine(xfdlForm.documentElement.xml);
	// }
	// else
	// {
	// console.log("\n!!\n");
	contentForm = replaceNewLine(serial.serializeToString(xfdlForm));
	// console.log(replaceNewLine(s.serializeToString(xmlDoc)));
	// s.serializeToStream(xfdlForm, stream, "UTF-8");
	// contentForm = encodeURIComponent(rezStr);

	// }
	if (false && window.parent && window.parent.document.applet_adapter
			&& window.parent.document.applet_adapter.saveFormToLocalFile) {
		console.logWorkScript("Сохранение через родительский апплет");
		contentForm = replaceNewLine(serial.serializeToString(xfdlForm));
		rez = window.parent.document.applet_adapter
				.saveFormToLocalFile(contentForm);
	} else {
		if (window.parent.document.applet_adapter && window.parent.document.applet_adapter.saveLocalXFDLForm
				&& false)
			//var rez = document.utilsApplet.saveLocalXFDLForm(contentForm, "");
			var rez = window.parent.document.applet_adapter.saveLocalXFDLForm(contentForm, "");
		else {
			rez = 0;
			saveFormOnServer();
		}
	}
	if (rez == 0) {
		console
				.info("Сохранение формы прошло успешно по адресу 'C:/temp/saveform.xfdl'");
	} else {
		console
				.warn("Сохранение формы по адресу 'C:/temp/saveform.xfdl' не удалось");
	}

}

// TODO доделать
function toolbar_save_as() {

}

// TODO доделать
function toolbar_print() {
	functionForType.print();
}

/**
 * функция для сохранения текущей формы на сервер
 * 
 * @param {Document}
 *            xmlDoc - документ для сохранения(по умолчанию текущий)
 */
function saveFormOnServer(xmlDoc) {

	if (xmlDoc == null)
		xmlDoc = xfdlForm;

	var s = serial;
	/*
	 * var rezStr; rezStr = ""; var stream = { close : function() {
	 * alert("Stream closed"); }, flush : function() { }, write :
	 * function(string, count) { rezStr = rezStr + string; } };
	 */

	if (isViewerDebug) {
		var contentForm;

		// if (browser == 'MSIE')
		// {
		// contentForm =
		// encodeURIComponent(replaceNewLine(xfdlForm.documentElement.xml));
		// }
		// else
		// {
		// console.log("\n!!\n");
		contentForm = encodeURIComponent(replaceNewLine(s
				.serializeToString(xmlDoc)));
		// console.log(replaceNewLine(s.serializeToString(xmlDoc)));
		// s.serializeToStream(xfdlForm, stream, "UTF-8");
		// contentForm = encodeURIComponent(rezStr);

		// }

		$.post(saveURL, {
					form : nameXFDL,
					content : contentForm
				}, function() {

				});
	}

	else {
		// TODO здесь написать сохранение формы
	}
	// XMLHttp.open("POST", saveURL, false);
	// XMLHttp.setRequestHeader('Content-Type',
	// 'application/x-www-form-urlencoded');
	// XMLHttp.onreadystatechange = endGetForm;
	// XMLHttp.send("form=" + encodeURIComponent(nameXFDL) + "&content=" +
	// xfdlForm);
}

/**
 * getElementFoFullSidRef - ищет элемента ссылка которого задана
 * 
 * @param {String}
 *            fullSidRef - ссылка на искомый элемент по sid'ам
 * @param {Element}
 *            contextEl - контекстный элемент
 * 
 * @return {Element} искомый элемент
 */
function getElementFoFullSidRef(fullSidRef, contextEl) {

	/**
	 * искомый элемент (сначала корневой)
	 * 
	 * @type Element
	 */
	var searchElement;
	if (contextEl == undefined) {
		searchElement = xfdlForm.getElementsByTagName("XFDL").item(0);
	} else {
		searchElement = contextEl;
	}

	/**
	 * найден ли элемент
	 * 
	 * @type Bollean
	 */
	var isFound = true;

	/**
	 * ссылка в виде массива
	 * 
	 * @type String[]
	 */
	var arraySid;

	arraySid = fullSidRef.split('\.');

	// поиск элемента
	for (var i = 0; i < arraySid.length; i++) {
		// console.log(searchElement.nodeType);
		// console.log(arraySid[i]);
		/**
		 * @type Element;
		 */
		var thisChild = searchElement.firstChild;

		var isExit = false;
		do {
			while (true) {
				if (thisChild == null) {
					// alert(null);
					return null;
				}
				if (thisChild.nodeType == 1) {
					/**
					 * @type Element
					 */
					if (thisChild.namespaceURI) {
						// !доделать (пока идем на потомка если <xforms:...>)
						if ((thisChild.namespaceURI.toLowerCase() == "http://www.w3.org/2002/xforms")) {
							// console.log("thisChild.nodeName = " +
							// thisChild.nodeName);
							if (thisChild.parentNode.nodeName == "pane"
									|| thisChild.parentNode.nodeName == "table") {
								thisChild = thisChild.firstChild;
								continue;
							}
						}
					}

					break;
				}

				thisChild = thisChild.nextSibling;
			}

			// console.log(serial.serializeToString(thisChild));
			var thisSid = thisChild.getAttribute('sid');

			// alert("thisSid = " + thisSid);
			if (thisSid == arraySid[i]) {
				searchElement = thisChild;
				// alert("searchElement = " +
				// searchElement.getAttribute('sid'));
				isExit = true;
			} else {
				thisChild = thisChild.nextSibling;
			}

		} while (!isExit);
	}
	if (isFound) {
		// alert(searchElement.nodeName);
		return searchElement;
	} else {
		// alert(null);
		return null;
	}
}

/**
 * 
 * функция возвращает массив элементов, который соответсвует заданной
 * xpath-ссылке или <b>null</b> если таковых не имеется
 * 
 * <br/><b>Note:</b><br/>ссылка должна содержать функцию instance(), которая
 * не является родной для xpath
 * 
 * @param {String}
 *            ref - ссылка
 * @param {Element}
 *            contextEl - контекстный узел
 * @return {Node[]|Array|null} массив полученных узлов или null если таковых не
 *         имеется
 */
function getNodeArrayForInstanceRef(ref, contextEl) {
	// alert("ref = "+ref);
	/**
	 * 
	 * @type {Node[]|Array}
	 */
	var rez;

	var xpathRez;
	/**
	 * @type Number
	 */
	var tmpIndex = ref.indexOf("instance(");

	/**
	 * @type String
	 */
	var realRef;
	while (tmpIndex < 0) {
		return null;
		break;
	}
	if (false)// устаревший вариант
	{

		/**
		 * @type String
		 */
		var instanceId;
		instanceId = ref.substring(tmpIndex + 10);
		instanceId = instanceId.substring(0, instanceId.indexOf(")") - 1);

		/**
		 * @type
		 */
		var refXformModels;
		refXformModels = ref.substring(ref.indexOf('/', tmpIndex) + 1);

		realRef = "/xfdl:XFDL/xfdl:globalpage/xfdl:global/xfdl:xformsmodels/xforms:model/xforms:instance[@id=\""
				+ instanceId + "\"]/child::*[1]";
		if (refXformModels.length > 0)
			realRef = realRef + "/" + refXformModels;

		// alert("realRef = " + realRef);
		try {
			xpathRez = xfdlForm.evaluate(realRef, xfdlForm,
					getNameSpaceForXfdlForm,
					XPathResult.ORDERED_NODE_ITERATOR_TYPE, xfdlForm);
		} catch (e) {

			console.error("Исключение типа " + e.name + ":\n" + e.message
					+ ":\n\nПараметры:\nref = " + ref + "\nrealRef = "
					+ realRef);
		}

	} else {
		try {
            if(ieXmlDom != null) {
                xpathRez = ieXpathEvaluate(ieXmlDom, ref);
            } else {
                xpathRez = xfdlForm.xpath(ref);
            }
			if (!xpathRez)
				throw new Error("xpathError");
		} catch (e) {

			console.error("Исключение типа " + e.name + ":\n" + e.message
					+ ":\n\nПараметры:\nref = " + ref + "\nrealRef = "
					+ realRef);
			return null;
		}
	}

	/**
	 * 
	 * @type Node
	 */
	var tmpNode;
    if(ieXmlDom != null) {
        var it = 0;
        tmpNode = xpathRez[it++];
    } else {
        tmpNode = xpathRez.iterateNext();
    }
	if (tmpNode)
		rez = new Array();
	else
		rez = null;
	// alert("tmpNode.nodeName = " + tmpNode);
	while (tmpNode) {
		// alert("tmpNode.nodeName = " + tmpNode.nodeName + "\ntmpNode.parrent =
		// " + tmpNode.parentNode);
		rez.push(tmpNode);
        if(ieXmlDom != null) {
            tmpNode = xpathRez[it++];
        } else {
            tmpNode = xpathRez.iterateNext();
        }
	}
	
	/*console.log('!!!!!!!!!!!!!!!!!!');
	console.log(rez);
	if (rez==null){
		if (ref.indexOf('instance(') != -1)
		ref = replaceXPathInstance(ref);
		if (ref.indexOf('row[index(') != -1)
		ref = replaceXPathTableRow(ref);
		console.log(ref);
		
		console.log($(xfdlForm).xpath(ref, xfdlNSResolver));
	}*/

	return rez;

}
/**
 * функция ищет недостающую часть xform-ссылки по родительским элементам
 * 
 * @param {String}
 *            xformRef - текущая ссылка
 * @param {Element}
 *            contextNode - текущий узел
 * @param {Number[]}
 *            repeatMass - массив с положениями(порядковыми номерами) связанных
 *            элементов при использовании <xforms:repeat>
 * @return {String} - результатирующая ссылка или null если таковой не имеется
 */
function searchXformRef(xformRef, contextNode, repeatMass) {
	/**
	 * @type Element
	 */
	var parentEl = contextNode.parentNode;

	/**
	 * @type String
	 */
	var rezultRef = xformRef;
	// console.log("rezultRef = " + rezultRef);

	if (parentEl.nodeName == "page")
		return null;
	while (true) {
		if (parentEl.tagName.replace(/.+:/g, '') == "repeat") {
			if (parentEl.getAttribute("nodeset")) {
				/**
				 * @type Number
				 */
				var numXformRepeat = parseInt(repeatMass.pop());
				rezultRef = parentEl.getAttribute("nodeset") + "[position()="
						+ numXformRepeat + "]/" + rezultRef;
			}
			break;
		}
		if (parentEl.tagName.replace(/.+:/g, '') == "group") {
			if (parentEl.getAttribute("ref"))
				rezultRef = parentEl.getAttribute("ref") + "/" + rezultRef;
			break;
		}
		break;
	}
	if (rezultRef.indexOf("instance(") < 0)
		rezultRef = searchXformRef(rezultRef, parentEl, repeatMass);
	// else
	// return rezultRef;

	if (rezultRef == "")
		rezultRef = null;
	return rezultRef;

}

/**
 * фукнция для преобразования значения в пригодное для сохранение в xform-модели
 * 
 * @param {String}
 *            value - исходное значение
 * @param {String}
 *            datatype - тип данных(string, check, float, time и т.д.).Может не
 *            совпадать с format/datatype, т.к. может быть равным 'check'
 * @param {String}
 *            itemPattern - исходный шаблон
 * @return {String} - полученное значение
 */
function formatValueForXform(value, datatype, itemPattern) {
	// TODO тут еще подумать
	if ((value + '').trim() === "")
		return value;
	/**
	 * шаблон по умолчанию
	 * 
	 * @type String
	 */
	var defParrent = "";
	/**
	 * 
	 * @type String
	 */
	var formatingValue = "";
	switch (datatype) {
		case "date" :
			defParrent = defXFormPattern['date'];
			formatingValue = dateFormatS2S(value, itemPattern, defParrent);
			console.log("value = " + value + "\nformatingValue = "
					+ formatingValue);
			console.log("itemPattern = " + itemPattern + "\ndefParrent = "
					+ defParrent);

			break;
		case "time" :
			defParrent = defXFormPattern['time'];
			formatingValue = dateFormatS2S(value, itemPattern, defParrent);

			break;
		case "check" :

			if (value == 'on' || value == 'true')
				formatingValue = "true";
			else
				formatingValue = "false";
			break;
		case "string" :
			formatingValue = value;
			break;
		case "void" :
			formatingValue = value;
			break;

		default :
			formatingValue = value;
			break;
	}
	return formatingValue;
}
/**
 * фукнция для преобразования значения сохранённое в xform-модели в обычное
 * 
 * @param {String}
 *            value - исходное значение
 * @param {String}
 *            datatype - тип данных(string, check, float, time и т.д.).Может не
 *            совпадать с format/datatype, т.к. может быть равным 'check'
 * @param {String}
 *            itemPattern - шаблон в который надо преобразовать(optional)
 * @return {String} - полученное значение
 */
function formatValueFromXform(value, datatype, itemPattern) {
	// TODO тут еще подумать
	if ((value + '').trim() === "")
		return value;
	// console.logCall("call formatValueFromXform(" + value + ")");
	/**
	 * шаблон по умолчанию
	 * 
	 * @type String
	 */
	var defParrent = "";
	/**
	 * 
	 * @type String
	 */
	var formatingValue = "";
	switch (datatype) {
		case "date" :
			defParrent = defXFormPattern['date'];
			formatingValue = dateFormatS2S(value, defParrent, itemPattern);
			break;
		case "time" :
			defParrent = defXFormPattern['time'];
			formatingValue = dateFormatS2S(value, defParrent, itemPattern);
			break;
		case "check" :
			if (value == 'true' || value == '1')
				formatingValue = "on";
			else
				formatingValue = "off";
			break;
		case "string" :
			formatingValue = value;
			break;
		// case "" :
		// break;

		default :
			formatingValue = value;
			break;
	}
	return formatingValue;
}
/**
 * заменить вызовы нестандартной для xpath-функции <b>instance(id)</b> на <b>
 * "/xfdl:XFDL/xfdl:globalpage/xfdl:global/xfdl:xformsmodels/xforms:model/xforms:instance[@id=<i>id</i>]/child::*[1]"</b>
 * или другую строку, которая указана в replStr(т.е. по умолчанию replStr =
 * "/xfdl:XFDL/xfdl:globalpage/xfdl:global/xfdl:xformsmodels/xforms:model/xforms:instance[@id=")
 * 
 * @param {String}
 *            srcXPath
 * 
 * @param {String}
 *            replStr
 * @return {String}
 */
function replaceXPathInstance(srcXPath, replStr) {
	if (!replStr)
		replStr = "/xfdl:XFDL/xfdl:globalpage/xfdl:global/xfdl:xformsmodels/xforms:model/xforms:instance[@id=";
	// console.logCall("call replaceXPathInstance()");

	/**
	 * 
	 * @type String
	 */
	var newXPath = null;
	var tmpStr = srcXPath;
	// здесь выделяем не строковые константы
	{
		var indSing;
		var indDoub;

	}

	newXPath = srcXPath
			.replace(
					/('[^']*instance\([^']*')|("[^"]*instance\([^"]*")|(instance\([^)]*\))/g,
					function(str) {
						if (str.charAt(0) == '"' || str.charAt(0) == '\'')
							return str;
						str = str.replace(/^instance\(/, replStr);
						str = str.replace(/\)$/, "]/child::*[1]");
						return str;
					});
	return newXPath;

};

/**
 * заменить вызовы нестандартной для xpath-функции <b>index(table.id)</b> на индекс
 * выделенной строки в данной таблице
 * 
 * @param {String}
 *            srcXPath
 * 
 * @return {String}
 */
function replaceXPathTableRow(srcXPath) {
	/**
	 * 
	 * @type String
	 */
	var newXPath = null;
	var tmpStr = srcXPath;
	var regexp = /index\('(\w*)'\)/g;
	var res = regexp.exec(tmpStr);
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	while(res){
		var p = res[1];
		res = regexp.exec(tmpStr);
		for (var val in treeInfoEls._massEls){
			if (val.indexOf(p)!=-1){
				if (treeInfoEls._massEls[val].typeEl == "table"){
					var index = treeInfoEls._massEls[val].selectedRow;
					tmpStr = tmpStr.replace(regexp, index);
				}
			}
		}
	}
	return tmpStr;
};


/**
 * ищет имя пространства имен, которому соответсвует заданный префикс
 * 
 * @type Function|XPathNSResolver
 * 
 * @param {String}
 *            nsPrefix - префикс пространства имен
 * @return {String} URL пространства имен
 */
var getNameSpaceForXfdlForm;

/**
 * сокращение для getNameSpaceForXfdlForm
 * 
 * @type Function
 */
var getNS;
/**
 * сокращение для getNameSpaceForXfdlForm
 * 
 * @type Function
 */
var getNSX;
