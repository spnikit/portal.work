/**
 * @include "../../includes.js"
 */

/**
 * 
 * @param {Element}
 *            el
 * @param {String}
 *            parentId
 * @param {Boolean=}
 *            isActivePage - текущая страница является активной(по умолчанию
 *            нет)
 */
function drawGlobal(el, parentId, isActivePage) {
	// console.log(el);
	var info = new InfoElement();
	/**
	 * @type Element
	 */
	var activated = xfdlForm.createElement("activated");
	if (isActivePage) {
		thisGlobalForPage = info;
		activated.setValue("on");
	} else {
		activated.setValue("off");
	}
	info.hiddenTeg['activated'] = activated;

	var focuseditem = xfdlForm.createElement("focuseditem");
	info.hiddenTeg['focuseditem'] = focuseditem;

	var id = "" + parentId + "-" + el.getAttribute("sid");

	info.typeEl = "global";
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;
	info.typeEl = "global";
	info.refElem = el;

	treeInfoEls.addElemInfo(info, info.fullSidRef);

}