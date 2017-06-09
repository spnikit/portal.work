/**
 * @include "../includes.js"
 * 
 */

/**
 * функция для установки значения какого либо узла
 * 
 * @param {String}
 *            pathEl - xpath-ссылка на элемент, значение которого
 *            устанавливается
 * @param {String}
 *            valueXpath - xpath-ссылка на элемент, значение которого будет
 *            взято для установки.<br/>&#x9;&#x9; Если пустая строка или
 *            <b>null</b> то см. valueString, иначе является приоритетнее
 *            valueString
 * @param {String}
 *            valueString - значение в виде строки, которое будет установлено в
 *            элемент. Не приоритетнее valueXpath
 */
function xformsSetValue(pathEl, valueXpath, valueString)
	{
	pathEl = translateXformsRef(pathEl);
	valueXpath = translateXformsRef(valueXpath);
	/**
	 * узел, значение которого будет устанавливаться
	 * 
	 * @type {Node|Element}
	 */
	var el = xfdlForm.xpath(pathEl).iterateNext();
	if (!el)
		return;

	/**
	 * значение которое будет установлено
	 * 
	 * @type String
	 */
	var value = "";

	if (valueXpath)
		{
		value = xfdlForm.xpath(valueXpath, XPathResult.STRING_TYPE).stringValue;
		}
	else
		{
		value = valueString;
		}
	//console.log(value);
	el.setValue(value);
	return;
	}