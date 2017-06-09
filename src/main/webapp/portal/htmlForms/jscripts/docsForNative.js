/**
 * НЕ ВЗДУМАЙ ПОДГРУЖАТЬ ЭТОТ ФАЙЛ. ЗДЕСЬ ТОЛЬКО НЕДОСТАЮЩАЯ ДОКУМЕНТАЦИЯ ДЛЯ
 * SPKET IDE
 * 
 */

/**
 * 
 * @param {String}
 *            nameExep - имя исключения
 * @constructor
 */
function Error(nameExep) {
	/**
	 * 
	 * @type String
	 */
	this.message = "";
	/**
	 * 
	 * @type String
	 */
	this.name = "";
	/**
	 * 
	 * @type Boolean
	 */
	this.isMyErr = false;
}

/**
 * 
 * @param {String}
 *            action - действие
 * @param {Function}
 *            func - функция
 * @param {Boolean}
 *            state - состояние(<b>false</b> - фаза всплытия, <b>true</b> -
 *            фаза перехвата)
 */
Element.prototype.addEventListener = function(action, func, state) {

};

/**
 * @type {Element}
 */
Node.prototype.ownerElement = Element;
/**
 * @type {Element}
 */
Node.prototype.ownerElement = Element;

/**
 * @type {Object}
 */
// var console = new Object();
/**
 * функция для вывода в консоль строки
 * 
 * @param {String}
 *            str - строка для вывода в консоль
 */
// console.log = function(str)
{
};
/**
 * функция для показа стека вызовов
 */
// console.trace = function()
{
};

/**
 * @param {String||RegExp}
 *            token
 * @param {String||Function}
 *            newString
 * @return {String}
 * 
 * @type {String}
 */
String.prototype.replace = function(token, newString) {
};

/**
 * фунция для вычисления xpath-выражения
 * 
 * @param {String}
 *            <b>xpathExpression</b> - строка, содержащая выражение xpath,
 *            которое нужно вычислить
 * @param {Node}
 *            <b>contextNode</b> узел документа, по отношению к которому должно
 *            быть вычислено выражение xpath
 * @param {Function}
 *            <b>namespaceResolver</b> - функция, принимающая строку с
 *            префиксом пространства имен из xpathExpression и возвращающая
 *            строку, содержащаю URI, которому соответствует этот префикс. Она
 *            дает возможность проеобразования между префиксами, используемыми в
 *            выражениях xpath и (возможно отличными) префиксами, используемыми
 *            в документе
 * @param {Number}
 *            <b>resultType</b> - числовая константа, указывающая тип
 *            возвращаемого результата
 * @param {XPathResult}
 *            <b>result</b> - существующий XPathResult, используемый для
 *            результатов. Передача null приводит к созданию нового XPathResult
 * 
 * @return {XPathResult}
 */
Document.prototype.evaluate = function(xpathExpression, contextNode,
		namespaceResolver, resultType, result) {
};
/**
 * @constructor
 */
function XPathResult() {
};
/**
 * @return {Element|Attr|Node}
 */
XPathResult.prototype.iterateNext = function() {
};
XPathResult.prototype.snapshotItem = function() {
};
/**
 * @type Boolean
 */
XPathResult.prototype.booleanValue = true;
/**
 * @type {Boolean}
 */
XPathResult.prototype.invalidIteratorState = false;
/**
 * @type Number
 */
XPathResult.prototype.numberValue = 0;
/**
 * @type Number
 */
XPathResult.prototype.resultType = 0;
XPathResult.prototype.singleNodeValue;
XPathResult.prototype.snapshotLength;
/**
 * @type String
 */
XPathResult.prototype.stringValue = "";

/**
 * @type Number
 */
XPathResult.ANY_TYPE = 0;
/**
 * @type Number
 */
XPathResult.NUMBER_TYPE = 1;
/**
 * @type Number
 */
XPathResult.STRING_TYPE = 2;
/**
 * @type Number
 */
XPathResult.BOOLEAN_TYPE = 3;
/**
 * @type Number
 */
XPathResult.UNORDERED_NODE_ITERATOR_TYPE = 4;
/**
 * @type Number
 */
XPathResult.ORDERED_NODE_ITERATOR_TYPE = 5;
/**
 * @type Number
 */
XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE = 6;
/**
 * @type Number
 */
XPathResult.ORDERED_NODE_SNAPSHOT_TYPE = 7;
/**
 * @type Number
 */
XPathResult.ANY_UNORDERED_NODE_TYPE = 8;
/**
 * @type Number
 */
XPathResult.FIRST_ORDERED_NODE_TYPE = 9;

/**
 * @constructor
 */
function XMLSerializer() {
}
/**
 * @param {Node||Element||Document}
 *            el
 * @return {String}
 */
XMLSerializer.prototype.serializeToString = function(el) {
};

/**
 * 
 * @param {String}
 *            eventName - имя группы
 *            событий("HTMLEvents","UIEvents","MouseEvents" и т.д.)
 * @return {Event|UIEvent|MutationEvent} - объект-событие()
 * @see {@link Event.initEvent},{@link Element.dispatchEvent}
 */
document.createEvent = function(eventName) {
};

/**
 * Сэмулировать вызов события на элементе.<br/> Служит для генерации событий
 * при работе javascript'ов и создания собственных событий
 * 
 * @param {Event}
 *            ev - эмулируемое событие
 * 
 * @note событие не имеет реального эффекта, т.е. при эмулировании события
 *       <b>focus</b> объект не получит фокус
 */
Element.prototype.dispatchEvent = function(ev) {
};

/**
 * @type Element|null
 */
Element.prototype.nextElementSibling = null;
/**
 * @type Element|null
 */
Element.prototype.previousElementSibling = null;
/**
 * @type Element|null
 */
Element.prototype.firstElementChild = null;
/**
 * @type Element|null
 */
Element.prototype.lastElementChild = null;
/**
 * @type Number
 */
Element.prototype.childElementCount = null;

/**
 * Сэмулировать вызов события на элементе.<br/> Служит для генерации событий
 * при работе javascript'ов и создания собственных событий
 * 
 * @param {Event}
 *            ev - эмулируемое событие
 * 
 * @note событие не имеет реального эффекта, т.е. при эмулировании события
 *       <b>focus</b> объект не получит фокус
 */
HTMLElement.prototype.dispatchEvent = function(ev) {
};

/**
 * @type CSS2Properties
 */
HTMLElement.prototype.style = CSS2Properties.prototype;

/**
 * 
 * @type Number
 */
HTMLElement.prototype.offsetHeight = null;
