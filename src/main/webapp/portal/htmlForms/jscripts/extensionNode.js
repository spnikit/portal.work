/**
 * 
 * @include "includes.js"
 * 
 */

if (browser == 'MSIE' && !Node.prototype.compareDocumentPosition)
	{
	/**
	 * 
	 * @param {Node|Attr}
	 *          otherNode
	 */
	Node.prototype.compareDocumentPosition = function(
	    /* Node|Attr|Element */otherNode)
		{
		// alert(1);
		var result = 0;
		if (this == otherNode)
			return result;
		
		var DOCUMENT_POSITION_DISCONNECTED = 1;
		var DOCUMENT_POSITION_PRECEDING = 2;
		var DOCUMENT_POSITION_FOLLOWING = 4;
		var DOCUMENT_POSITION_CONTAINS = 8;
		var DOCUMENT_POSITION_CONTAINED_BY = 16;
		var DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
		
		if (!this.ownerDocument || !otherNode.ownerDocument
		    || (this.ownerDocument !== otherNode.ownerDocument))
			return DOCUMENT_POSITION_DISCONNECTED;
		
		var thisParMass = new Array();
		var otherParMass = new Array();
		var iT = 0;
		var iO = 0;
		
		var tmpNode = otherNode;
		while (tmpNode)
			{
			// console.log(tmpNode);
			// alert(2);
			iO++;
			otherParMass.push(tmpNode);
			tmpNode = tmpNode.parentNode || tmpNode.ownerElement;
			}
		tmpNode = this;
		while (tmpNode)
			{
			// alert(3);
			iT++;
			thisParMass.push(tmpNode);
			tmpNode = tmpNode.parentNode || tmpNode.ownerElement;
			}
		
		var eqEl;
		
		var k = 1;
		while (thisParMass[iT - k] && otherParMass[iO - k]
		    && (thisParMass[iT - k] == otherParMass[iO - k]))
			{
			// alert(4);
			
			if (k == iT)
				return DOCUMENT_POSITION_CONTAINS | DOCUMENT_POSITION_PRECEDING;// 10
			if (k == iO)
				return DOCUMENT_POSITION_CONTAINED_BY | DOCUMENT_POSITION_FOLLOWING;// 20
			k++;
			};
		// console.log('k = ' + k + '\niT =' + iT + '\niO =' + iO);
		
		/**
		 * @type Node
		 */
		var tmpNode2 = thisParMass[iT - k];
		// console.log(tmpNode2);
		/**
		 * @type Node
		 */
		var tmpNode3 = otherParMass[iO - k];
		// console.log(tmpNode3);
		if (tmpNode2.nodeType == Node.ATTRIBUTE_NODE
		    && tmpNode3.nodeType == Node.ATTRIBUTE_NODE)
			{
			result = DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;
			if (tmpNode2.nodeName > tmpNode3.nodeName)
				result |= DOCUMENT_POSITION_FOLLOWING;
			else
				result |= DOCUMENT_POSITION_PRECEDING;
			return result;
			}
		if (tmpNode2.nodeType == Node.ATTRIBUTE_NODE)
			return DOCUMENT_POSITION_FOLLOWING;
		if (tmpNode3.nodeType == Node.ATTRIBUTE_NODE)
			return DOCUMENT_POSITION_PRECEDING;
		
		tmpNode2 = tmpNode2.nextSibling;
		while (tmpNode2)
			{
			// alert(5);
			if (tmpNode2 == tmpNode3)
				return 4;
			tmpNode2 = tmpNode2.nextSibling;
			}
		return 2;
		
		};
	}
/**
 * 
 * добавить ссылку к элементу из xform-модели данных
 * 
 * @param {InfoElement}
 *          el - добавляемая ссылка
 * @param {String}
 *          nameTeg - имя связанного свойства
 * @param {EmulateNode}
 *          emulateNode - ссылка на объект-эмулятор тэга
 */
Node.prototype.addXformLink = function(infoEl, nameTeg, emulateNode)
	{
	
	var link = new DuplexXtoX(infoEl, nameTeg, this, emulateNode);
	
	if (this.xformLinks)
		{
		this.xformLinks.push(link);
		}
	else
		{
		/**
		 * @type {DuplexXtoX[]}
		 */
		this.xformLinks = new Array();
		this.xformLinks.push(link);
		
		}
	
	}

/**
 * возвращает массив ссылок на связвнные узлы или <b>null</b> если таковых не
 * имеется
 * 
 * @return {DuplexXtoX[]}
 */
Node.prototype.getXformLinks = function()
	{
	if (this.xformLinks)
		{
		return this.xformLinks;
		}
	else
		{
		return null;
		}
	};
/**
 * возвращает количество ссылок на связвнные узлы (никогда не вернет <b>null</b>,
 * а только 0)
 */
Node.prototype.getNumberXformLinks = function()
	{
	if (this.xformLinks)
		{
		return this.xformLinks.lenght;
		}
	else
		{
		return 0;
		}
	};

/**
 * сохраняет значение в заданном узле(Node).
 * 
 * @this {Node|Element}
 * @param {String}
 *          value - новое значение
 * @param {Function}
 *          afterFunction - функция вызываемая после установки значения в тэг но
 *          до начала работы скриптов
 * 
 * @Note: автоматически сохраняет предыдущее значение
 * @return {Boolean} - было ли изменено значение
 */
Node.prototype.setValue = function(value, afterFunction)
	{
	value = '' + value;
	
	if (value == this.getValue())
		{
		
		return false;
		}
	if (this.isSigner())
		{
		var e = new Error();
		e.isMyErr = true;
		e.name = "Ошибка";
		e.message = "Попытка установить новое значение для подписанного тэга "
		    + this.tagName + ".\nДальнейшая работа с формой будет прекращена.";
		// TODO сделать прекращение работы с формой
		alert(e.message);
		treeInfoPages.hideActivePage();
		throw e;
		}
	
	switch (this.nodeType)
		{
		case 1 : // Если элемент
			/**
			 * if (node.firstChild != null) {
			 * node.replaceChild(xfdlForm.createTextNode('' + value),
			 * node.firstChild); } else { node.appendChild(xfdlForm.createTextNode('' +
			 * value)); }
			 */
			// xformEl.appendChild(xfdlForm.createTextNode('' + value));
			// alert("value = " + value + "\n El = " + xformEl.nodeName +
			// "\n
			// nVal =
			// " + xformEl.firstChild.nodeValue);
			if ('text' in this)
				{
				this._prevValue = this.text;
				this.text = '' + value;
				}
			else
				{
				this._prevValue = this.textContent;
				this.textContent = '' + value;
				}
			// alert(node.textContent);
			break;
		case 2 : // Если атрибут
			this._prevValue = this.value;
			this.value = value;
			
			// alert("node.parentNode.nodeName"+node.parentNode.nodeName);
			// node.parentNode.setAttribute(node.nodeName, value);
			break;
		}
	if (this.getPreviousValue() != value)// установить флаг изменений
		{
		
		// printStackTrace();
		// console.trace();
		if (!(this.nodeType == 1 && this.localName == "dirtyflag" && this.parentNode == null))
			globalInfo.setProperty("dirtyflag", "on");
		}
	
	if (afterFunction)
		afterFunction();
	
	/**
	 * @type {UnitScript[]}
	 */
	var scriptListeners = this.getScriptListeners();
	if (scriptListeners)
		{
		for (var i = 0; i < scriptListeners.length; i++)
			{
			scriptListeners[i].work();
			}
		}
	if (this._xformsModel)
		{
		/**
		 * @type XformsModel
		 */
		var a = this._xformsModel;
		a.runUpdating();
		}
	return true;
	};

/**
 * сохраняет значение в заданном узле(Node).
 * 
 * @this {Attr}
 * @param {String}
 *          value - новое значение 
 * @param {Function}
 *          afterFunction - функция вызываемая после установки значения в тэг но
 *          до начала работы скриптов
 * @Note: автоматически сохраняет предыдущее значение
 * @return {Boolean} - было ли изменено значение
 */
Attr.prototype.setValue = function(value, afterFunction)
	{
	value = '' + value;
	// console.logCall("call Attr.prototype.setValue()");
	
	if (value == this.getValue())
		{
		
		return false;
		}
	else
		{
		if (this.isSigner() || this.ownerElement.isSigner())
			{
			var e = new Error();
			e.isMyErr = true;
			e.name = "Ошибка";
			e.message = "Попытка установить новое значение для подписанного тэга "
			    + this.tagName + ".\nДальнейшая работа с формой будет прекращена.";
			// TODO сделать прекращение работы с формой
			alert(e.message);
			treeInfoPages.hideActivePage();
			throw e;
			}
		}
	this._prevValue = this.value;
	this.value = value;
	// alert("node.parentNode.nodeName"+node.parentNode.nodeName);
	// node.parentNode.setAttribute(node.nodeName, value);
	
	if (this.getPreviousValue() != value)// установить флаг изменений
		{
		
		// printStackTrace();
		// console.trace();
		if (!(this.nodeType == 1 && this.localName == "dirtyflag" && this.parentNode == null))
			globalInfo.setProperty("dirtyflag", "on");
		}
	
	if (afterFunction)
		afterFunction();
	/**
	 * @type {UnitScript[]}
	 */
	var scriptListeners = this.getScriptListeners();
	if (scriptListeners)
		{
		for (var i = 0; i < scriptListeners.length; i++)
			{
			scriptListeners[i].work();
			}
		}
	if (this._xformsModel)
		{
		/**
		 * @type XformsModel
		 */
		var a = this._xformsModel;
		a.runUpdating();
		}
	
	return true;
	};

/**
 * сохраняет значение в заданном узле(Node).
 * 
 * @this {Element}
 * @param {String}
 *          value - новое значение
 * @param {Function}
 *          afterFunction - функция вызываемая после установки значения в тэг но
 *          до начала работы скриптов
 * @Note: автоматически сохраняет предыдущее значение
 * @return {Boolean} - было ли изменено значение
 */
Element.prototype.setValue = function(value, afterFunction)
	{
	// console.logCall("call Element.prototype.setValue()");
	
	value = '' + value;
//	console.log('!!! '+this.isSigner());
//	console.log(this);
//	console.log(value);
	// console.logWorkScript('context = '+this.tagName+"\nvalue = "+value);
	if (value == this.getValue())
		{
		return false;
		}
	else
		{
		if (this.isSigner())
			{
			var e = new Error();
			
			console.trace();
			console.log("value = " + value);
			console.log("oldValue = " + this.getValue());
			
			e.isMyErr = true;
			e.name = "Ошибка";
			e.message = "Попытка установить новое значение для подписанного тэга "
			    + this.tagName + ".\nДальнейшая работа с формой будет прекращена.";
			// TODO сделать прекращение работы с формой
			alert(e.message);
			treeInfoPages.hideActivePage();
			throw e;
			}
		}
	
	/**
	 * if (node.firstChild != null) { node.replaceChild(xfdlForm.createTextNode('' +
	 * value), node.firstChild); } else {
	 * node.appendChild(xfdlForm.createTextNode('' + value)); }
	 */
	// xformEl.appendChild(xfdlForm.createTextNode('' + value));
	// alert("value = " + value + "\n El = " + xformEl.nodeName +
	// "\n
	// nVal =
	// " + xformEl.firstChild.nodeValue);
	if (browser == 'MSIE')
		{
		this._prevValue = this.getValue();
		
		/**
		 * @type NodeList
		 */
		var childNodes = this.childNodes;
		for (var i = 0; i < childNodes.length; i++)
			{
			this.removeChild(childNodes.item(0));
			}
		var txtNode = this.ownerDocument.createTextNode(value);
		this.appendChild(txtNode);
		
		}
	else
		{
		this._prevValue = this.textContent;
		this.textContent = value;
		}
	
	// alert(node.textContent);
	
	if (this.getPreviousValue() != value)// установить флаг изменений
		{
		
		// printStackTrace();
		// console.trace();
		if (!(this.nodeType == 1 && this.localName == "dirtyflag" && this.parentNode == null))
			globalInfo.setProperty("dirtyflag", "on");
		}
	
	if (afterFunction)
		afterFunction();
	/**
	 * @type {UnitScript[]}
	 */
	var scriptListeners = this.getScriptListeners();
	if (scriptListeners)
		{
		for (var i = 0; i < scriptListeners.length; i++)
			{
			
			scriptListeners[i].work();
			}
		}
	if (this._xformsModel)
		{
		// console.info(this._xformsModel);
		/**
		 * @type XformsModel
		 */
		var a = this._xformsModel;
		a.runUpdating();
		}
	
	return true;
	};

/**
 * получает значение узла: элемента или аттрибута
 * 
 * @this {Node|Element|Attr}
 * @return {String|null}
 */
Node.prototype.getValue = function()
	{
	// console.logCall("call Element.prototype.getValue()");
	// console.log("value = " + value);
	var value = null;
	if (this)
		{
		switch (this.nodeType)
			{
			case 1 : // Если элемент
				/**
				 * if (node.firstChild != null) {
				 * node.replaceChild(xfdlForm.createTextNode('' + value),
				 * node.firstChild); } else {
				 * node.appendChild(xfdlForm.createTextNode('' + value)); }
				 */
				// xformEl.appendChild(xfdlForm.createTextNode('' + value));
				// alert("value = " + value + "\n El = " + xformEl.nodeName +
				// "\n
				// nVal =
				// " + xformEl.firstChild.nodeValue);
				if ('text' in this)
					{
					value = this.text;
					}
				else
					{
					value = this.textContent;
					}
				// alert(node.textContent);
				break;
			case 2 : // Если атрибут
				value = this.value;
				// alert("node.parentNode.nodeName"+node.parentNode.nodeName);
				// node.parentNode.setAttribute(node.nodeName, value);
				break;
			}
		}
	return value;
	};
/**
 * получает значение узла: элемента или аттрибута
 * 
 * @this {Element}
 * @return {String|null}
 */
Element.prototype.getValue = function()
	{
	// console.logCall("call Element.prototype.getValue()");
	// console.log("value = " + value);
	if (browser == 'MSIE')
		{
		var resultText = '';
		/**
		 * @type {Node|Attr|Element}
		 */
		var thisEl = this;
		/**
		 * @type NodeList
		 */
		var childNodes = thisEl.childNodes;
		for (var i = 0; i < childNodes.length; i++)
			{
			// console.log('rr = ' + childNodes.item(i));
			switch (childNodes.item(i).nodeType)
				{
				case Node.ELEMENT_NODE :
					resultText += childNodes.item(i).getValue();
					break;
				case Node.TEXT_NODE :
				case Node.CDATA_SECTION_NODE :
					resultText += childNodes.item(i).data;
					break;
				}
			}
		// console.log('resultText = ' + resultText);
		return resultText;
		}
	else
		{
		var value = null;
		
		if (this)
			{
			/**
			 * if (node.firstChild != null) {
			 * node.replaceChild(xfdlForm.createTextNode('' + value),
			 * node.firstChild); } else { node.appendChild(xfdlForm.createTextNode('' +
			 * value)); }
			 */
			// xformEl.appendChild(xfdlForm.createTextNode('' + value));
			// alert("value = " + value + "\n El = " + xformEl.nodeName +
			// "\n
			// nVal =
			// " + xformEl.firstChild.nodeValue);
			// if (this.getTextContent)
			// {
			// }
			// else
			if ('text' in this)
				value = this.text;
			else
				value = this.textContent;
			// alert(node.textContent);
			
			}
		// console.dir(this);
		// console.log(this.text);
		// console.log(this.textContent);
		return value;
		}
	
	};

/**
 * получает значение узла: элемента или аттрибута
 * 
 * @this {Attr}
 * @return {String|null}
 */
Attr.prototype.getValue = function()
	{
	// console.logCall("call Element.prototype.getValue()");
	// console.log("value = " + value);
	var value = null;
	if (this)
		{
		
		value = this.value;
		// alert("node.parentNode.nodeName"+node.parentNode.nodeName);
		// node.parentNode.setAttribute(node.nodeName, value);
		
		}
	return value;
	};
/*
 * Attr.prototype.toString = Attr.prototype.valueOf = function() { return
 * this.getValue(); }; Element.prototype.toLocaleString =
 * Element.prototype.valueOf = function() { // console.log("tostring:"); //
 * console.log(this); var val = this.getValue(); // if (val == null) // val =
 * ""; return val; };
 */

/*
 * Node.prototype.toLocaleString = Node.prototype.toString =
 * Node.prototype.valueOf = function() {
 * 
 * console.log("tostring:"); console.log(this); var val = this.getValue(); if
 * (val == null) val = ""; return val; }
 */
/*
 * Object.prototype.toString = function() {
 * 
 * return Object.prototype.toString.apply(this); }
 * 
 * function Typeof(x) { alert(4); // Начнем с оператора typeof var t = typeof x; //
 * Если результат является информативным, возвращаем его if (t != "object")
 * return t; // В противном случае x – это объект. Получаем его класс, чтобы
 * попытаться // узнать, что это за объект. var c =
 * Object.prototype.toString.apply(x); // Возвращает "[object class]" c =
 * c.substring(8, c.length - 1); // Отбрасываем "[object" и "]" return c; }
 */

/**
 * @param {String}
 *          request
 * @param {Node}
 *          context
 * @param {Number}
 *          rezType - тип результата(по умолчанию любой, т.е 0)
 * @return {XPathResult}
 * 
 * @deprecated
 */
function xpath(request, context, rezType)
	{
	if (typeof rezType == "undefined" || rezType === null)
		rezType = 0;
	var xresult = null;
	if (false || true)
	// try
		{
		if (xpathEvaluator)
			xresult = xpathEvaluator.evaluate(request, context, xfdlNSResolver,
			    rezType, null);
		else
			xresult = xpathEvaluator.evaluate(request, context, xfdlNSResolver,
			    rezType, null);
		
		}
	// catch (e)
	// {
	// console.error("!Возникло исключение '" + e.name + "' с типом:\n"
	// + e.message + "\nПараметры:\nrequest = " + request);
	// console.trace();
	// }
	else
		if (xpathEvaluator)
			xresult = xpathEvaluator.evaluate(request, context, xfdlNSResolver,
			    rezType, null);
		else
			xresult = xpathEvaluator.evaluate(request, context, xfdlNSResolver,
			    rezType, null);
	
	// console.log("endXpath");
	return xresult;
	}
/**
 * @this {Node}
 * @param {String}
 *          request
 * @param {Number}
 *          rezType - тип результата(по умолчанию любой, т.е 0)
 * @return {XPathResult}
 */
Element.prototype.xpath = Node.prototype.xpath = function(request, rezType)
	{
	// console.log("stXpath(request = "+request+")");
	// console.trace();
	// console.info("==============\nrequest = " + request);
	
	if (request.indexOf('instance(') != -1)
		request = replaceXPathInstance(request);
	if (request.indexOf('row[index(') != -1)
		request = replaceXPathTableRow(request);
	if (typeof rezType == "undefined" || rezType === null)
		rezType = 0;
	if (browser == "MSIE" && false)
		{
		request = request.replace(new RegExp("/xfdl:", "g"), "/");
		}
	// console.log("stXpath(request = "+request+")");
	// console.trace();
	// console.log("request = " + request);
	// console.dir(xfdlNSResolver.ns);
	var xresult = null;

	if (false || true)
	// try
		{
        if (typeof document.evaluate != 'undefined' && browser != "Firefox")
            xresult = document.evaluate(request, this, xfdlNSResolver, rezType, null);
		else 
			if (xpathEvaluator)
			xresult = xpathEvaluator.evaluate(request, this, xfdlNSResolver, rezType, null);
		else
			xresult = xfdlForm.evaluate(request, this, xfdlNSResolver, rezType, null);
		}
	// catch (e)
	// {
	//			
	// console.error("!Возникло исключение '" + e.name + "' с типом:\n"
	// + e.message + "\nПараметры:\nrequest = " + request);
	// console.trace();
	//			
	// for (var b in e)
	// {
	// // console.log(b + " = " + e[b]);
	// }
	//			
	// }
	else
		if (xpathEvaluator)
			xresult = xpathEvaluator.evaluate(request, this, xfdlNSResolver, rezType,
			    null);
		else
			xresult = xfdlForm.evaluate(request, this, xfdlNSResolver, rezType, null);
	
	// console.log("endXpath");
	return xresult;
	};

/**
 * @this {Node}
 * @param {String}
 *          request
 * @param {Number}
 *          rezType - тип результата(по умолчанию любой, т.е 0)
 * @return {XPathResult}
 */
Document.prototype.xpath = function(request, rezType)
	{
	if (request.indexOf('instance(') != -1)
		request = replaceXPathInstance(request);
	if (request.indexOf('row[index(') != -1)
		request = replaceXPathTableRow(request);
	if (typeof rezType == "undefined" || rezType === null)
		rezType = XPathResult.ANY_TYPE;
	
	if (browser == "MSIE" && false)
		{
		request = request.replace(new RegExp("/xfdl:", "g"), "/");
		}

	var xresult = null;
	if (false || true)
	// try
		{
		// console.info("======");
		// console.info(xfdlNSResolver.lookupNamespaceURI("xfdl"));
		if (typeof document.evaluate != 'undefined' && browser != "Firefox")
            xresult = document.evaluate(request, this, xfdlNSResolver, rezType, null);
		else 
			if (xpathEvaluator)
			xresult = xpathEvaluator.evaluate(request, this, xfdlNSResolver, rezType, null);
		else
			xresult = xfdlForm.evaluate(request, this, xfdlNSResolver, rezType, null);
		}
	// catch (e)
	// {
	// console.error("!Возникло исключение '" + e.name + "' с типом:\n"
	// + e.message + "\nПараметры:\nrequest = " + request);
	// console.trace();
	// }
	
	else
		if (xpathEvaluator)
			xresult = xpathEvaluator.evaluate(request, this, xfdlNSResolver, rezType, null);
		else
			xresult = xfdlForm.evaluate(request, this, xfdlNSResolver, rezType, null);
	
	// console.log("endXpath Document");
//	console.log(xpathEvaluator.evaluate(request, this, xfdlNSResolver, rezType,
//			    null).iterateNext());
	return xresult;
	
	};

/**
 * установить предыдущее значение
 * 
 * @param {String}
 *          str
 * 
 * @Note: только для типов Element и Attr, <br/>&#x9;автоматически
 *        устанавливается внутри методов: <b><br/>&#x9;&#x9;Node.prototype.setValue()<br/>&#x9;&#x9;saveNodeValue()<br/>&#x9;&#x9;
 *        setNodeValue()</b>
 */

Node.prototype.setPreviousValue = function(str)
	{
	if (this.nodeType == Node.ELEMENT_NODE
	    || this.nodeType == Node.ATTRIBUTE_NODE)
		this._prevValue = str;
	};
/**
 * получить предыдущее значение или <b>null</b> если такого не имеется
 * 
 * @Note: только для типов Element и Attr
 */
Node.prototype.getPreviousValue = function()
	{
	if (this._prevValue === undefined)
		return this.getValue();
	else
		return this._prevValue;
	
	};

/**
 * установить опцию для xform-узла(всё эти опции ставятся из xform:bind)
 * 
 * @param {String}
 *          nameOpt - имя опции
 * @param {Boolean|String}
 *          val - значение
 * @see список опций:<br/><b> {Boolean} constraint </b> - соответствует ли
 *      значение назначенным ограничениям <br/><b> {Boolean} readonly </b>-
 *      только для чтения(переопределяет аналгичную опцию у связанного пункта)
 *      <br/> <b> {Boolean} relevant</b> - скрывает связанный пункт(однако не
 *      может переопределить опции active и visible у связанного пункта)<br/>
 *      <b> {Boolean} requred </b> -требует ли поле ввода(отражается в связанном
 *      пункте) <br/> <b>{String} type </b>- тип значения(дополняет datatype в
 *      связанном пункте, т.е. введенное значение должно соответсвовать обоим
 *      шаблонам)<br/><b>{Boolean} isSigner</b>- - подписан ли данный узел<br/>
 */
Node.prototype.setXformOption = function(nameOpt, val)
	{
	if (!namesXformOpt[nameOpt])
		return;
	if (!this._massXformOpt)
		{
		this._massXformOpt = new Object();
		this._numberMassXformOpt = 0;
		}
	if (this._massXformOpt[nameOpt] !== null)
		this._numberMassXformOpt++;
	this._massXformOpt[nameOpt] = val;
	
	};
/**
 * получить опцию xform-узла(всё эти опции ставятся из xform:bind)
 * 
 * @param {String}
 *          nameOpt - имя опции
 * @return {Boolean|String|null} значение или null если она не определена
 * @see список опций:<br/><b> {Boolean} constraint </b> - соответствует ли
 *      значение назначенным ограничениям <br/><b> {Boolean} readonly </b>-
 *      только для чтения(переопределяет аналгичную опцию у связанного пункта)
 *      <br/> <b> {Boolean} relevant</b> - скрывает связанный пункт(однако не
 *      может переопределить опции active и visible у связанного пункта)<br/>
 *      <b> {Boolean} requred </b> -требует ли поле ввода(отражается в связанном
 *      пункте) <br/> <b>{String} type </b>- тип значения(дополняет datatype в
 *      связанном пункте, т.е. введенное значение должно соответсвовать обоим
 *      шаблонам)<br/><b>{Boolean} isSigner</b>- - подписан ли данный узел<br/>
 */
Node.prototype.getXformOption = function(nameOpt)
	{
	
	if (!this._massXformOpt || !this._massXformOpt[nameOpt])
		return null;
	else
		return this._massXformOpt[nameOpt];
	
	};
/**
 * получить опцию xform-узла(всё эти опции ставятся из xform:bind)
 * 
 * @param {String}
 *          nameOpt - имя опции
 * @return {Boolean|String|null} значение или null если она не определена
 * @see список опций:<br/><b> {Boolean} constraint </b> - соответствует ли
 *      значение назначенным ограничениям <br/><b> {Boolean} readonly </b>-
 *      только для чтения(переопределяет аналгичную опцию у связанного пункта)
 *      <br/> <b> {Boolean} relevant</b> - скрывает связанный пункт(однако не
 *      может переопределить опции active и visible у связанного пункта)<br/>
 *      <b> {Boolean} requred </b> -требует ли поле ввода(отражается в связанном
 *      пункте) <br/> <b>{String} type </b>- тип значения(дополняет datatype в
 *      связанном пункте, т.е. введенное значение должно соответсвовать обоим
 *      шаблонам)<br/><b>{Boolean} isSigner</b>- - подписан ли данный узел<br/>
 */
Node.prototype.delXformOption = function(nameOpt)
	{
	if (!namesXformOpt[nameOpt])
		return false;
	if (!this._massXformOpt || !this._massXformOpt[nameOpt])
		return false;
	else
		{
		this._massXformOpt[nameOpt] = null;
		this._numberMassXformOpt--;
		return true;
		}
	};
/**
 * получить количество xform-опций
 * 
 * @return {Number}
 */
Node.prototype.getNumbetXformOptions = function()
	{
	if (this._numberMassXformOpt)
		return this._numberMassXformOpt;
	else
		return 0;
	};
/**
 * установить текущему узлу ссылку на оболочку <xforms:model>
 * 
 * @param {XformsModel}
 *          xfromsModel
 */
Node.prototype.setParentXformsModel = function(xfromsModel)
	{
	this._xformsModel = xfromsModel;
	}
/**
 * получить ссылку на оболочку <xforms:model> для текущего узла или null если
 * таковая отсутсвует
 * 
 * @return {XformsModel|null}
 * 
 */
Node.prototype.getParentXformsModel = function()
	{
	if (this._xformsModel)
		return this._xformsModel;
	else
		return null;
	}

/**
 * функция для добавления скриптового слушателя к данному узлу
 * 
 * @param {UnitScript}
 *          scriptListener - слушающий данный узел скрипт
 */

Node.prototype.addScriptListener = function(scriptListener)
	{
	if (!this._massScriptListener)
		{
		this._massScriptListener = new Array();
		this._massScriptListener.push(scriptListener);
		}
	else
		{
		this._massScriptListener.push(scriptListener);
		}
	
	};
/**
 * получить массив скриптов слушателей или <b>null</b> если слушателей нет
 * 
 * @return {UnitScript[]}
 */
Node.prototype.getScriptListeners = function()
	{
	if (!this._massScriptListener)
		{
		return null;
		}
	else
		{
		return this._massScriptListener;
		}
	
	};

/**
 * получить количество скриптов слушателей
 * 
 * @return {Number}
 */
Node.prototype.getNumberScriptListeners = function()
	{
	if (!this._massScriptListener)
		{
		return 0;
		}
	else
		{
		return this._massScriptListener.length;
		}
	
	};
Node.prototype.isEmulate = function()
	{
	return false;
	};
/**
 * получить статус подписи для текущего узла
 * 
 * @return {Boolean}
 */
Node.prototype.isSigner = function()
	{
	if (this._isSigner)
		return true;
	else
		return false;
	}
/**
 * устаноить статус подписи для узла
 * 
 * @param {Boolean}
 *          signerStatus - установить статус подписи(если передано false то
 *          статус данного тэга относящийся к подписи refTegSignature будет
 *          удален)
 * @param {String}
 *          refTegSignature - ссылка на тэг подписи
 * 
 */
Node.prototype.setSignerStatus = function(signerStatus, refTegSignature)
	{
	if (signerStatus)
		{
		if (this._massRefsSignature)
			{
			if (!this._massRefsSignature[refTegSignature])
				{
				this._massRefsSignature[refTegSignature] = true;
				this._numberRefsSignature++;
				}
			}
		else
			{
			this._massRefsSignature = {
				refTegSignature	: true
			};
			this._numberRefsSignature = 1;
			}
		this._isSigner = true;
		}
	else
		{
		if (this._massRefsSignature)
			{
			if (this._massRefsSignature[refTegSignature])
				{
				this._massRefsSignature[refTegSignature] = false;
				if ((this._numberRefsSignature--) == 0)
					this._isSigner = false;
				
				}
			}
		}
	//this._isSigner = false;
	}

/**
 * установить статус поиска по фильтрам подписи
 * 
 * @param {String}
 *          key - ключ(имя фильтрующей опции: <b>"itemrefs"</b>, <b>"instance"</b>,
 *          <b>"options"</b>, <b> "groups"</b>, <b>"items"</b>,
 *          <b>"optionrefs"</b>,<b> "pagerefs"</b> или <b> "datagroups"</b> )
 * @param {Number}
 *          val - значение(<b>0</b>-данный тэг не найден данным фильтром, <b>1</b> -
 *          данный тэг найден данным фильтром как конечный, <b>2</b>- данный
 *          тэг найден данным фильтром как родительский для конечного)
 * 
 * @note <b>"namespaces"</b>-фильтр не устанавливается данной опцией
 */
Node.prototype.setSignFilterKey = function(key, val)
	{
	val = 1 * val;
	if (!this._massFilterKey)
		{
		this._massFilterKey = {};
		}
	this._massFilterKey[key] = val;
	
	}
/**
 * получить статус поиска по фильтрам подписи
 * 
 * @param {String}
 *          key - ключ(имя фильтрующей опции: <b>"itemrefs"</b>, <b>"instance"</b>,
 *          <b>"options"</b>, <b> "groups"</b>, <b>"items"</b>,
 *          <b>"namespaces"</b>, <b>"optionrefs"</b>,<b> "pagerefs"</b> или
 *          <b> "datagroups"</b> )
 * @return {Number} значение(<b>0</b>-данный тэг не найден данным фильтром,
 *         <b>1</b> - данный тэг найден данным фильтром как конечный, <b>2</b>-
 *         данный тэг найден данным фильтром как родительский для конечного)
 * 
 * @note <b>"namespaces"</b>-фильтр данной опцией не возвращается
 */
Node.prototype.getSignFilterKey = function(key)
	{
	if (!this._massFilterKey || !this._massFilterKey[key])
		return 0;
	else
		return this._massFilterKey[key];
	
	}
/**
 * удалить все статусы поиска по фильтрам подписи
 */
Node.prototype.delSignFilterKeys = function()
	{
	if (this._massFilterKey)
		delete this._massFilterKey;
	}
//
// /**
// * @this {Node}
// * @param {String}
// * request
// * @param {Number}
// * rezType - тип результата(по умолчанию любой, т.е 0)
// * @return {XPathResult}
// */
// HTMLElement.prototype.xpath = function(request, rezType)
// {
// // console.trace();
// if (typeof rezType == "undefined" || rezType === null)
// rezType = 0;
// if (browser == "MSIE")
// {
// request = request.replace(new RegExp("/xfdl:", "g"), "/");
// }
// // console.trace();
// // console.log("request = " + request);
// return this.ownerDocument.evaluate(request, this, getNameSpaceForHTMLForm,
// rezType, null);
// };
//
// /**
// * @this {Node}
// * @param {String}
// * request
// * @param {Number}
// * rezType - тип результата(по умолчанию любой, т.е 0)
// * @return {XPathResult}
// */
// // HTMLDocument.prototype.xpath =
// document.xpath = function(request, rezType)
// {
// if (typeof rezType == "undefined" || rezType === null)
// rezType = 0;
// if (browser == "MSIE")
// {
// request = request.replace(new RegExp("/xfdl:", "g"), "/");
// }
// return this.evaluate(request, this, getNameSpaceForHTMLForm, rezType, null);
// };
//

var UtilsHTMLEl = {
	/**
	 * установить ссылку HTMLElement->InfoElement
	 * 
	 * @param {InfoElement}
	 *          infoEl
	 * @this {HTMLElement}
	 */
	setInfoElRef	: function(htmlEl, infoEl)
		{
		$(htmlEl).data('_islinkXFDL', true);
		$(htmlEl).data('refInfoEl', infoEl);
		},
	/**
	 * получить ссылку HTMLElement->InfoElement
	 * 
	 * @this {HTMLElement}
	 * @return {InfoElement|null} полученная ссылка или <b>null</b> если её нет
	 */
	getInfoElRef	: function(htmlEl)
		{
		if ($(htmlEl).data('refInfoEl'))
			return $(htmlEl).data('refInfoEl')
		else
			return null;
		
		},
	/**
	 * есть ли у данного html-элемента связанный InfoElement в дереве
	 * TreeInfoElements
	 * 
	 * @return {Boolean}
	 */
	isLinkXFDL	 : function(htmlEl)
		{
		if ($(htmlEl).data('_islinkXFDL'))
			return true;
		else
			return false;
		},
	/**
	 * Установить, что данному html-элементу найдется связанный InfoElement в
	 * дереве TreeInfoElements
	 * 
	 * @deprecated Автоматически устанавливается в {@link #setInfoElRef()}
	 */
	yesLinkXFDL	 : function(htmlEl)
		{
		$(htmlEl).data('_islinkXFDL', true);
		},
	/**
	 * сохраняет значение в заданном узле(Node).
	 * 
	 * @this {Node|Element}
	 * @param {String}
	 *          value - новое значение
	 * 
	 * @Note: автоматически сохраняет предыдущее значение
	 */
	setValue	   : function(htmlEl, value)
		{
		
		$(htmlEl).data('_prevValue', $(htmlEl).text());
		$(htmlEl).text(value);
		//		
		// if ('text' in this)
		// {
		// htmlEl._prevValue = htmlEl.text;
		// htmlEl.text = value;
		// }
		// else
		// {
		// htmlEl._prevValue = htmlEl.textContent;
		// htmlEl.textContent = value;
		// }
		}
	
};
// /**
// * установить ссылку HTMLElement->InfoElement
// *
// * @param {InfoElement}
// * infoEl
// * @this {HTMLElement}
// */
// HTMLElement.prototype.setInfoElRef = function(infoEl)
// {
// this._islinkXFDL = true;
// this.refInfoEl = infoEl;
// };
// /**
// * получить ссылку HTMLElement->InfoElement
// *
// * @this {HTMLElement}
// * @return {InfoElement|null} полученная ссылка или <b>null</b> если её нет
// */
// HTMLElement.prototype.getInfoElRef = function()
// {
// if (this.refInfoEl)
// return this.refInfoEl;
// else
// return null;
// };

// /**
// * есть ли у данного html-элемента связанный InfoElement в дереве
// * TreeInfoElements
// *
// * @return {Boolean}
// */
// HTMLElement.prototype.isLinkXFDL = function()
// {
// if (this._islinkXFDL)
// return true;
// else
// return false;
// };
//
// /**
// * Установить, что данному html-элементу найдется связанный InfoElement в
// дереве
// * TreeInfoElements
// *
// * @deprecated Автоматически устанавливается в {@link #setInfoElRef()}
// */
// HTMLElement.prototype.yesLinkXFDL = function()
// {
// this._islinkXFDL = true;
// };

// /**
// * сохраняет значение в заданном узле(Node).
// *
// * @this {Node|Element}
// * @param {String}
// * value - новое значение
// *
// * @Note: автоматически сохраняет предыдущее значение
// */
// HTMLElement.prototype.setValue = Node.prototype.setValue;
// console.log('load extensionNode.js');
