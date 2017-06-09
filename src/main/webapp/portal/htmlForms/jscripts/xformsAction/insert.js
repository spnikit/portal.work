/**
 * @include "../includes.js"
 * 
 */

/**
 * 
 * @param {String}
 *            refNodeset - ссылка на множество, последний элемент из которого
 *            будет клонирован и вставлен
 * @param {String|Number}
 *            at - элемент перед/после которого вставляется новый
 * @param {String}
 *            position - позиция вставки: до - before, после - after(по
 *            умолчанию)
 */
function xformsInsert(refNodeset, at, position)
	{
	if (at.indexOf("index(") != -1)
		{
		at = getFocusElForIndexTable(at);
		}
	refNodeset = translateXformsRef(refNodeset);
	/**
	 * @type Node
	 */
	var newNode = xfdlForm.xpath(refNodeset + "[last()]").iterateNext();
	if (!newNode)
		return;

	newNode = newNode.cloneNode(true);
	/**
	 * @type Node
	 */
	var posNode = xfdlForm.xpath(refNodeset + "[position() = " + at + "]")
			.iterateNext();

	if (!posNode)
		{
		posNode = xfdlForm.xpath(refNodeset + "[last()]").iterateNext();
		if (!posNode)
			return;
		position = "after";
		}
	/**
	 * @type {String}
	 */
	var space = createCuteSpace(posNode);
	// console.log("space= '"+space+"'");
	var parentNode = posNode.parentNode;
	if (position == "before")
		{
		if (posNode.previousSibling == null
				|| posNode.previousSibling.nodeType == Node.ELEMENT_NODE)
			{
			parentNode.insertBefore(xfdlForm.createTextNode(space), posNode);
			}
		parentNode.insertBefore(newNode, posNode);
		parentNode.insertBefore(xfdlForm.createTextNode(space), posNode);
		}
	else
		{
		posNode = posNode.nextSibling;
		if (posNode)
			{
			if (posNode.nodeType == Node.ELEMENT_NODE)
				{
				// console.log("element");
				parentNode
						.insertBefore(xfdlForm.createTextNode(space), posNode);
				parentNode.insertBefore(newNode, posNode);
				parentNode
						.insertBefore(xfdlForm.createTextNode(space), posNode);
				}
			else
				{
				// console.log("text");
				parentNode.insertBefore(newNode, posNode);
				parentNode
						.insertBefore(xfdlForm.createTextNode(space), newNode);
				}
			}
		else
			{
			parentNode.appendChild(xfdlForm.createTextNode(space));
			parentNode.appendChild(newNode);
			parentNode.appendChild(xfdlForm
					.createTextNode(createCuteSpace(parentNode)));
			}
		}

	}