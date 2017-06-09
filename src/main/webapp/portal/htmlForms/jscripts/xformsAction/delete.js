/**
 * @include "../includes.js"
 * 
 */

/**
 * функция для удаления узла
 * 
 * @param {String}
 *            refNodeset - ссылка на множество, элемент из которого будет удален
 * @param {String|Number}
 *            at - элемент перед/после которого вставляется новый
 */
function xformsDelete(refNodeset, at)
	{
	refNodeset = translateXformsRef(refNodeset);
	// TODO
	// !доделать
	if (at.indexOf("index(") != -1)
		{
		at = getFocusElForIndexTable(at);
		}
	/**
	 * элемент который будет удален
	 * 
	 * @type {Node|Element}
	 */
	var delNode = xfdlForm.xpath(refNodeset + "[position() = " + at + "]")
			.iterateNext();

	if (!delNode)
		{
		return;
		}

	/**
	 * @type {Element|Node}
	 */
	var parentNode;
	if (delNode.nodeType == Node.ATTRIBUTE_NODE)
		parentNode = delNode.ownerElement;
	else
		parentNode = delNode.parentNode;

	/**
	 * 
	 * @type {Node}
	 */
	var previousText = delNode.previousSibling;

	while (previousText && previousText.nodeType == Node.TEXT_NODE)
		{
		parentNode.removeChild(previousText);
		previousText = delNode.previousSibling;
		}
	parentNode.removeChild(delNode);
	return;
	}