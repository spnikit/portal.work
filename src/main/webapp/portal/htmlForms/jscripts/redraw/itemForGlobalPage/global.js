/**
 * @include "../../includes.js"
 */

/**
 * 
 * @param {Element}
 *            el
 */
function parseGlobalPage(el) {
	var pageInfo = new InfoGlobalPage();
	pageInfo.sid = pageInfo.fullSidRef = el.getAttribute("sid");
	pageInfo.refElem = el;
	treeInfoPages.addElemInfo(pageInfo);

    var globalEl;
    if(ieXmlDom != null) {
        globalEl = ieXpathEvaluate(el, "./global")[0];
    } else
	    globalEl = el.xpath("./xfdl:global").iterateNext();

	if (!globalEl) {
		e = new Error();
		e.message = "Отсутствует элемент <global> в <globalpage>";
		e.name = "Ошибка отрисовки";
		e.isMyErr = true;
		throw e;
	}
	parseGlobalItemInGlobalPage(globalEl, pageInfo.sid);

	drawXfdlMenu();
}

function parseGlobalItemInGlobalPage(el, parentId) {
	/**
	 * @type InfoElement
	 */
	var info = new InfoGlobalElement();
	treeInfoPages.getGlobalPage().globalEl = info;
	globalInfo = info;
	/**
	 * @type String
	 */
	var thisSid;

	/**
	 * @type String
	 */
	var id;

	thisSid = el.getAttribute("sid");

	id = thisSid + '-' + parentId;

	info.refElem = el;
	info.sid = thisSid;
	info.fullSidRef = id;
	treeInfoEls.addElemInfo(info);
	/**
	 * @type Element
	 */
	var dirtyflag = new EmulateNode("dirtyflag", "off", el);
	info.hiddenTeg['dirtyflag'] = dirtyflag;
	info.typeEl = 'global';

	/**
	 * 
	 * @type Node|Element
	 */
	var childEl = null;
	var lNameChildEl;

	childEl = el.firstChild;
	while (childEl) {
		if (childEl.nodeType == Node.ELEMENT_NODE) {
            lNameChildEl = childEl.tagName.replace(new RegExp('.+:', 'g'), '');
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {
				switch (lNameChildEl) {
					case 'xformsmodels' :
						treeXformsModels = new TreeXformsModels(childEl);
						break;
					case 'ufv_settings' :

						parseUfvSettings(childEl);
						break;

					default :
						console.warnDraw("Для тэга <" + childEl.tagName
								+ "> нет распознания");
						break;
				}

			}
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("custom")) {
				info.hiddenTeg['custom:' + lNameChildEl] = childEl;

			}
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("designer")) {
			}
		}
		childEl = childEl.nextSibling;
	}

}