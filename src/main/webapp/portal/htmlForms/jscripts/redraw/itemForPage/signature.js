/**
 * @include "../../includes.js"
 */

/**
 * @param {Element}
 *            el - отрисовываемый элемент
 * @param {String}
 *            parentId - родительский id или <b>null</b> если его нет
 * @param {String}
 *            parentXformRef - родительская xform-ссылка или <b>null</b> если
 *            её нет
 * 
 * @return {InfoElement} - полученный элемент
 */

function drawSignature(el, parentId, parentXformRef) {
	var info = new InfoElement();
	info.typeEl = "signature";
	info.refElem = el;
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;

	/**
	 * 
	 * @type Node|Element
	 */
	var childEl = null;

	/**
	 * 
	 * @type String
	 */
	var lNameChildEl;
	/**
	 * 
	 * @type String
	 */
	var valChildEl;

	// теперь переберем остальные-узлы
	childEl = el.firstChild;
	while (childEl)// альтернативный и более правильный метод перебора
	// элементов
	// чем через xpath
	{
		if (childEl.nodeType == Node.ELEMENT_NODE
				&& childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {
			lNameChildEl = childEl.tagName.replace(new RegExp('.+:','g'), '');
			valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
			switch (lNameChildEl) {
				case '' :

					break;
			}
		}
		childEl = childEl.nextSibling;
	}

	// --------------------------------------------------------------------
	// --------------------------------------------------------------------

	// style = style + "z-index: 10";

	// --------------------------------------------------------------------

	// --------------------------------------------------------------------
	treeInfoEls.addElemInfo(info);
	return null;
};
