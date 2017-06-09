/*
 * XPathResult. ANY_TYPE не какой-то конкретизированный тип, какой угодно тип.
 * Метод возвращает тип, который естественным образом вытекает из вычисления
 * выражения. XPathResult.ANY_UNORDERED_NODE_TYPE возвращает набор узлов,
 * состоящий из единственного узла, к которому можно обратиться через свойство
 * singleNodeValue. В случае если требуемый узел не будет найден, возвращается
 * значение null. Возвращаемый узел может быть, а может не быть первым узлом в
 * документе, соответствующим заданному критерию отбора. XPathResult.
 * BOOLEAN_TYPE возвращает логическое значение. XPathResult.
 * FIRST_ORDERED_NODE_TYPE возвращает набор узлов, состоящий из единственного
 * узла, к которому можно обратиться через свойство singleNodeValue класса
 * XPathResult. Возвращаемый узел обязательно будет первым узлом в документе,
 * соответствующим заданному критерию отбора. XPathResult. NUMBER_TYPE
 * возвращает числовое значение. XPathResult. ORDERED_NODE_ITERATOR_TYPE
 * возвращает упорядоченный (в порядке следования в документе) набор узлов,
 * который можно последовательно обойти с помощью метода (итератора) iterateNext
 * (). Этот метод позволяет обратиться к любому узлу в наборе.
 * XPathResult.ORDERED_NODE_SNAPSHOT_TYPE возвращает упорядоченный (в порядке
 * следования в документе) статический набор узлов. Модификации узлов,
 * выполняемые в документе, не оказывают влияния на результат. XPathResult.
 * STRING_TYPE возвращает текстовую строку.
 * XPathResult.UNORDERED_NODE_ITERATOR_TYPE -возвращает неупорядоченный набор
 * узлов, обойти который можно с помощью уже упоминавшегося метода
 * iterateNext(). Однако порядок следования узлов в наборе может совпадать, а
 * может не совпадать с порядком следования узлов в документе.
 * XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE возвращает неупорядоченный
 * статический набор узлов. Модификации узлов, выполняемые в документе, не
 * оказывают влияния на результат. Чаще всего используется тип XPathResult.
 * ORDERED_NODE_ITERATOR_TYPE:
 */

/**
 * слушатели данного узла
 * 
 * @type {Array|UnitScript[]}
 */
Node.prototype._massScriptListener = new Array();

/**
 * предыдущее значение узла
 * 
 * @type String
 * 
 * @Note только для типов Element и Attr
 * 
 */
Node.prototype._prevValue = "";

/**
 * @type InfoElement
 */
HTMLNode.prototype.refInfoEl = HTMLElement.prototype.refInfoEl = null;

/**
 * установить ссылку HTMLElement->InfoElement
 * 
 * @param {InfoElement}
 *            infoEl
 * @this {HTMLElement}
 */
Node.prototype.setInfoElRef = Element.prototype.setInfoElRef = HTMLNode.prototype.setInfoElRef = HTMLElement.prototype.setInfoElRef = function(
		infoEl) {
};
/**
 * получить ссылку HTMLElement->InfoElement
 * 
 * @this {HTMLElement}
 * @return {InfoElement} полученная ссылка или <b>null</b> если её нет
 */
Node.prototype.getInfoElRef = Element.prototype.getInfoElRef = HTMLElement.prototype.getInfoElRef = function() {
};

/**
 * элемент на котором находится фокус
 * 
 * @type HTMLElement
 */
document.thisFocusEl = null;
/**
 * предыдущий элемент с фокусом
 * 
 * @type HTMLElement
 */
document.previousFocusEl = null;
