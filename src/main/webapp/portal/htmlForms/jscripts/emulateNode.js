/**
 * @include "includes.js"
 */

/**
 * класс-оболочка для типа Node. Используется при работе скриптов
 * 
 * @param {Node|Element|Attr|EmulateNode}
 *            node - узел над которым строится оболочка
 * @constructor
 * 
 */
function XsNode(node) {
	/**
	 * узел над которым строится оболочка
	 * 
	 * @private
	 * @type Element|Attr|Node
	 */
	this._node = node;

}

XsNode.prototype.getDOMNode = function() {
	return this._node;
};

XsNode.prototype.toString = function() {
	// console.logCall("call XsNode.prototype.toString()");

	var rez = this._node.getValue();
	if (rez) {

	} else {
		rez = "";
	}
	return rez;
};

XsNode.prototype.valueOf = function() {
	// console.logCall("call XsNode.prototype.valueOf()");
	var rez = this._node.getValue();

	// if (rez == '' || rez === null)
	// return '';

	// var regXNumber = new RegExp("^[0-9]*,?[0-9]*$");
	//	
	// if (regXNumber.test(rez))
	// return 1 * rez;
	// else
	// return rez;
	// if (rez == "")
	// {
	// rez=null;
	// }
	// else
	// {
	// rez *= 1;
	// }
	if (rez != null) {
		if (rez != "") {
			var regXNumber = new RegExp("^[0-9]*,?[0-9]*$");
			if (regXNumber.test(rez))
				rez = 1 * rez;
		}
	} else {
		rez = "";
	}

	return rez;
};

XsNode.prototype.getValue = function() {
	var rez = this._node.getValue() ? this._node.getValue() : '';

	return rez;
};
XsNode.prototype.getPreviousValue = function() {
	// console.trace();
	return this._node.getPreviousValue();
};

XsNode.prototype.setValue = function(value) {
	return UtilsHTMLEl.setValue(this._node, value);
};

/**
 * класс для эмуляции типов Node
 * 
 * @param {String}
 *            name - имя эмулируемого тэга(возможно с префиксом)
 * @param {String}
 *            value - стартовое значение
 * @param {Element=}
 *            parentNode - родительский элемент(optional). Пока не используется
 * @param {Node}
 *            xformRef - связанный xform-узел
 * 
 * @note: физически тэг не становится потомком элемента <b>parentNode</b>. Этот
 *        параметр задается для корректного пространства имен у создаваемого
 *        тэга.
 * 
 * 
 * @class
 * @constructor
 */
function EmulateNode(tagName, value, parentNode, xformRef) {
	/**
	 * @type String
	 */
	this.tagName = tagName;

	/**
	 * @type String
	 */
	this.textContent = "";

	if (value)
		this.textContent = value;

	/**
	 * @type ?String
	 * @private
	 */
	this._prevValue = null;

	/**
	 * @type Array
	 * @private
	 */
	this._massScriptListener = null;

	/**
	 * @type Number
	 */
	this.nodeType = Node.ELEMENT_NODE;

	/**
	 * @type String
	 */
	this.prefix = null;

	/**
	 * @type String
	 */
	this.namespaceURI = null;
	/**
	 * @type String
	 */
	this.localName = null;

	/**
	 * @type Element
	 */
	this.parentNode = parentNode;

	this.xformRef = null;
	if (xformRef)
		this.xformRef = xformRef;
	/**
	 * @private
	 * @type Boolean
	 */
	this._isSigner = false;

	var pos;
	if ((pos = tagName.indexOf(':')) != -1) {
		this.prefix = tagName.substring(0, pos);
		this.namespaceURI = getNameSpaceForXfdlForm(this.prefix);
		this.localName = tagName.substring(pos + 1);
	} else {
		this.namespaceURI = getNameSpaceForXfdlForm("");
		this.localName = tagName;
	}

}
/**
 * установить xform-связь с заданным узлом
 * 
 * @param {Element}
 *            xformRef - заданный xform-элемент
 * @deprecated используйте конструктор с четвертым параметром
 * 
 */
EmulateNode.prototype.setXformRef = function(xformRef) {
	this.xformRef = xformRef;
}
/**
 * получить связанный xform-узел или <b>null</b> если такого не имеется
 * 
 * @return {Element|null}
 */
EmulateNode.prototype.getXformRef = function() {
	if (this.xformRef)
		return this.xformRef;
	else
		return null;
}

/**
 * является ли узел реальным
 * 
 * @return {Boolean}
 */
EmulateNode.prototype.isEmulate = function() {
	return true;
}

/**
 * сохраняет значение в заданном узле(EmulateNode).
 * 
 * @this {EmulateNode}
 * @param {String}
 *            value - новое значение
 * @param {Function}
 *            afterFunction - функция вызываемая после установки значения в тэг
 *            но до начала работы скриптов
 * @Note: автоматически сохраняет предыдущее значение
 */
EmulateNode.prototype.setValue = function(value, afterFunction) {
	// console.logCall('call EmulateNode.prototype.setValue(' + value + ') ' +
	// this.tagName);
	// console.logCall('afterFunction = ' + afterFunction);
	value = '' + value;
	if (value === this._prevValue)
		return false;
	this._prevValue = this.textContent ? this.textContent : null;
	this.textContent = value;

	if (afterFunction) {
		// console.log('rrrrrrrrrrrr');
		afterFunction();
	}
	/**
	 * @type {UnitScript[]}
	 */
	var scriptListeners = this.getScriptListeners();
	if (scriptListeners) {
		for (var i = 0; i < scriptListeners.length; i++) {
			// console.logWorkScript("value = " + value);
			// console.logWorkScript("scriptListeners[" + i + "]= " +
			// scriptListeners[i].getTranScript());
			scriptListeners[i].work();
		}
	}
	return true;
}

/**
 * получает значение элемента
 * 
 * @this {EmulateNode}
 * @return {?String}
 */
EmulateNode.prototype.getValue = function() {
	if (this.textContent)
		return this.textContent;
	else
		"";
}

/**
 * получает значение элемента
 * 
 * @this {EmulateNode}
 * @return {?String}
 */
EmulateNode.prototype.toString = function() {
	if (this.textContent)
		return this.textContent;
	else
		"";
}

/**
 * установить предыдущее значение
 * 
 * @param {String}
 *            str
 * 
 * @Note: только для типов Element и Attr, <br/>&#x9;автоматически
 *        устанавливается внутри методов: <b><br/>&#x9;&#x9;Node.prototype.setValue()<br/>&#x9;&#x9;saveNodeValue()<br/>&#x9;&#x9;
 *        setNodeValue()</b>
 */
EmulateNode.prototype.setPreviousValue = function(str) {
	this._prevValue = str;
};

/**
 * получить предыдущее значение или <b>null</b> если такого не имеется
 * 
 */
EmulateNode.prototype.getPreviousValue = function() {
	if (this._prevValue)
		return this._prevValue;
	else
		return null;

};

/**
 * функция для добавления скриптового слушателя к данному узлу
 * 
 * @param {UnitScript}
 *            scriptListener - слушающий данный узел скрипт
 */
EmulateNode.prototype.addScriptListener = function(scriptListener) {
	if (!this._massScriptListener) {
		this._massScriptListener = new Array();
		this._massScriptListener.push(scriptListener);
	} else {
		this._massScriptListener.push(scriptListener);
	}

};

/**
 * получить массив скриптов слушателей или <b>null</b> если слушателей нет
 * 
 * @return {UnitScript[]}
 */
EmulateNode.prototype.getScriptListeners = function() {
	if (!this._massScriptListener) {
		return null;
	} else {
		return this._massScriptListener;
	}

};

/**
 * получить количество скриптов слушателей
 * 
 * @return {Number}
 */
EmulateNode.prototype.getNumberScriptListeners = function() {
	if (!this._massScriptListener) {
		return 0;
	} else {
		return this._massScriptListener.length;
	}

};

/**
 * получить статус подписи для текущего узла
 * 
 * @return {Boolean}
 */
EmulateNode.prototype.isSigner = function() {
	if (this._isSigner)
		return true;
	else
		return false;
}
/**
 * устаноить статус подписи для узла
 * 
 * @param {Boolean}
 *            singerStatus
 */
EmulateNode.prototype.setSignerStatus = function(singerStatus) {
	this._isSigner = singerStatus;
}

console.log('load emulateNode.js');