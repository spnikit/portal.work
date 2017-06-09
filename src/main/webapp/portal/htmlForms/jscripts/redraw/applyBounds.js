/**
 * @include "main.js"
 */

/**
 * @param {Bounds}
 *            bounds
 * @param {Element}
 *            node
 * @param {String}
 *            parentId
 * @param {Boolean}
 *            isInfoCalculated - текущий элемент уже имеется в дереве
 *            элементов(отрисован)
 */
function applyBounds(bounds, node, parentId, isInfoCalculated) {
	if (!node)
		return;
	// console.dir(bounds);
	// console.dir(node.tagName);
	// if info is already calculated recalculate only relative tegs

	if (isInfoCalculated) {
		switch (node.nodeName) {
			// case "x" :
			// case "y" :
			case "width" :
			case "height" :
				// case "offsetx" :
				// case "offsety" :
			case "expandheight" :
			case "expandwidth" :
			case "#text" :
				return;
				break;
		}
	}

	/**
	 * @type String
	 */
    var value;
    if(ieXmlDom != null)
        value = node.text;
    else
	    value = node.getValue();
	/**
	 * @type Number
	 */
	var numberValue = parseInt(value);
	/**
	 * @type Boolean
	 */
	var isNanValue = isNaN(numberValue);
	switch (node.nodeName) {
		// =========== base ===============
		case "x" :
			if (isNanValue)
				break;

			bounds.isInitX = true;
			var dx = bounds.x1 - numberValue;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			// console.info('sid = ' +
			// node.parentNode.parentNode.getAttribute('sid'))
			// console .log('bounds.x1 = ' + bounds.x1 + "\nnumberValue = " +
			// numberValue);
			break;
		case "y" :
			if (isNanValue)
				break;
			bounds.isInitY = true;
			var dy = bounds.y1 - numberValue;
			bounds.y1 -= dy;
			bounds.y2 -= dy;
			break;
		case "width" :
			if (isNanValue)
				break;
			bounds.width = numberValue;
			bounds.x2 = bounds.x1 + bounds.width;
			break;
		case "height" :
			if (isNanValue)
				break;
			bounds.height = numberValue;
			bounds.y2 = bounds.y1 + bounds.height;
			break;
		case "offsetx" :
			if (isNanValue)
				break;
			bounds.x1 += numberValue;
			bounds.x2 += numberValue;
			break;
		case "offsety" :
			if (isNanValue)
				break;
			bounds.y1 += numberValue;
			bounds.y2 += numberValue;
			break;
		// =========== aGroup ===============
		case "after" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x1 - refInfo.x2;
			var n;
			if(refInfo.fullSidRef.indexOf("TABLE") > -1) {
			    n = 4;
			} else {
			    n = 1.4;
			}
			dx = dx - n;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			var dy = bounds.y1 - refInfo.y1;
			bounds.y1 -= dy;
			bounds.y2 -= dy;
			break;
		case "before" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x2 - refInfo.x1;
			dx = dx - 4;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			var dy = bounds.y1 - refInfo.y1;
			bounds.y1 -= dy;
			bounds.y2 -= dy;
			break;
		case "below" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
				
			//console.log(refInfo);
			//console.log(refInfo.y2);
				
			var dx = bounds.x1 - refInfo.x1;

			bounds.x1 -= dx;
			bounds.x2 -= dx;

			var dy = bounds.y1 - refInfo.y2;
			// TODO откуда эа хрень?!!!
			dy = dy - 5;
			bounds.y1 -= dy;
			bounds.y2 -= dy;
			
			//console.log(bounds.y1);
			break;
		case "above" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x1 - refInfo.x1;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			var dy = bounds.y2 - refInfo.y1;
			dy = dy + 5;
			bounds.y1 -= dy;
			bounds.y2 -= dy;
			break;
		case "within" :
			bounds.within = value;

			break;
		// =========== alignGroup ===============
		// ========= button to xxxxx ============
		case "alignb2b" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = bounds.y2 - refInfo.y2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		case "alignb2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = bounds.y2 - (refInfo.y1 + refInfo.y2) / 2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		case "alignb2t" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = bounds.y2 - refInfo.y1;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		// ========= center to xxxxx ============
		case "alignc2b" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = (bounds.y1 + bounds.y2) / 2 - refInfo.y2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		case "alignc2l" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = (bounds.x1 + bounds.x2) / 2 - refInfo.x1;
			bounds.x1 -= delta;
			bounds.x2 -= delta;
			break;
		case "alignc2r" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = (bounds.x1 + bounds.x2) / 2 - refInfo.x2;
			bounds.x1 -= delta;
			bounds.x2 -= delta;
			break;
		case "alignc2t" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = (bounds.y1 + bounds.y2) / 2 - refInfo.y1;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		case "alignhorizc2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = (bounds.x1 + bounds.x2) / 2 - (refInfo.x1 + refInfo.x2)
					/ 2;
			bounds.x1 -= delta;
			bounds.x2 -= delta;
			break;
		case "alignvertc2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = (bounds.y1 + bounds.y2) / 2 - (refInfo.y1 + refInfo.y2)
					/ 2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		// ========== left to xxxxx =============
		case "alignl2l" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x1 - refInfo.x1;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			break;
		case "alignl2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x1 - (refInfo.x1 + refInfo.x2) / 2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			break;
		case "alignl2r" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x1 - refInfo.x2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			break;
		// ========= right to xxxxx =============
		case "alignr2l" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x2 - refInfo.x1;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			break;
		case "alignr2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x2 - (refInfo.x1 + refInfo.x2) / 2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			break;
		case "alignr2r" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var dx = bounds.x2 - refInfo.x2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
			break;
		// =========== top to xxxxx =============
		case "alignt2b" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = bounds.y1 - refInfo.y2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		case "alignt2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = bounds.y1 - (refInfo.y1 + refInfo.y2) / 2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		case "alignt2t" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			var delta = bounds.y1 - refInfo.y1;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
			break;
		// ========== expandGroup ===============
		// ========= button to xx ===============
		case "expandb2b" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.y2 = refInfo.y2;
			bounds.height = bounds.y2 - bounds.y1;
			break;
		case "expandb2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.y2 = (refInfo.y1 + refInfo.y2) / 2;
			bounds.height = bounds.y2 - bounds.y1;
			break;
		case "expandb2t" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.y2 = refInfo.y1;
			bounds.height = bounds.y2 - bounds.y1;
			break;
		// ========== left to xx ================
		case "expandl2l" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.x1 = refInfo.x1;
			bounds.width = bounds.x2 - bounds.x1;
			break;
		case "expandl2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.x1 = (refInfo.x1 + refInfo.x2) / 2;
			bounds.width = bounds.y2 - bounds.y1;
			break;
		case "expandl2r" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.x1 = refInfo.x2;
			bounds.width = bounds.x2 - bounds.x1;
			break;
		// ========= rigth to xx ================
		case "expandr2l" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.x2 = refInfo.x1;
			bounds.width = bounds.x2 - bounds.x1;
			break;
		case "expandr2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.x2 = (refInfo.x1 + refInfo.x2) / 2;
			bounds.width = bounds.y2 - bounds.y1;
			break;
		case "expandr2r" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.x2 = refInfo.x2;
			bounds.width = bounds.x2 - bounds.x1;
			break;
		// ========== top to xx ================
		case "expandt2b" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.y1 = refInfo.y2;
			bounds.height = bounds.y2 - bounds.y1;
			break;
		case "expandt2c" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.y1 = (refInfo.y1 + refInfo.y2) / 2;
			bounds.height = bounds.y2 - bounds.y1;
			break;
		case "expandt2t" :
			var refInfo = getRefInfo(node, parentId);
			if (!refInfo)
				break;
			bounds.y1 = refInfo.y1;
			bounds.height = bounds.y2 - bounds.y1;
			break;
		// ==========================
		case "expandheight" :
			bounds.y2 += parseInt(ieXmlDom != null ? node.text : node.getValue());
			bounds.height = bounds.y2 - bounds.y1;
			break;
		case "expandwidth" :
			bounds.x2 += parseInt(ieXmlDom != null ? node.text : node.getValue());
			bounds.width = bounds.x2 - bounds.x1;
			break;
		// ==========================
	}
	// console.dir(bounds);
}

/**
 * @type Number
 */
var commondx1 = 2;
/**
 * @type Number
 */
var commondx2 = 2;
/**
 * @type Number
 */
var commondy1 = 2;
/**
 * @type Number
 */
var commondy2 = 2;

/**
 * Применяет к границам общие исправления, необходимо для элементов, у которых
 * во вьювере имеются прозрачные границы(достигается урезанием размеров)
 * 
 * @param {Bounds}
 *            bounds
 * @param {Element}
 *            node
 * @return {Bounds}
 */
function applyCommonUpdates(bounds, node) {
	var newBounds = new Bounds();

    switch (node.tagName.replace(new RegExp('.+:','g'), '')) {
		case "button" :
		case "check" :
		case "combobox" :
		case "field" :
			// case "label" :
		case "list" :
		case "popup" :
		case "radio" :

			newBounds.x1 = bounds.x1 + commondx1;
			newBounds.x2 = bounds.x2 - commondx2;
			newBounds.width = bounds.width - (commondx1 + commondx2);

			newBounds.y1 = bounds.y1 + commondy1;
			newBounds.y2 = bounds.y2 - commondy2;
			newBounds.height = bounds.height - (commondy1 + commondy2);
			break;
		default :
			newBounds.x1 = bounds.x1;
			newBounds.x2 = bounds.x2;
			newBounds.y1 = bounds.y1;
			newBounds.y2 = bounds.y2;
			newBounds.width = bounds.width;
			newBounds.height = bounds.height;
			break;
	}
	return newBounds;
}

/**
 * Отменяет принятое общее исправление.
 * 
 * @param {Bounds}
 *            bounds
 * @param {Element}
 *            node
 * @return {Bounds}
 */
function cancelCommonUpdates(bounds, node) {
	var newBounds = new Bounds();
	switch (node.tagName.replace(new RegExp('.+:','g'), '')) {
		case "button" :
		case "check" :
		case "combobox" :
		case "field" :
			// case "label" :
		case "list" :
		case "popup" :
		case "radio" :
			newBounds.x1 = bounds.x1 - commondx1;
			newBounds.x2 = bounds.x2 + commondx2;
			newBounds.y1 = bounds.y1 - commondy1;
			newBounds.y2 = bounds.y2 + commondy2;
			newBounds.width = bounds.width + (commondx1 + commondx2);
			newBounds.height = bounds.height + (commondy1 + commondy2);
			break;
		default :
			newBounds.x1 = bounds.x1;
			newBounds.x2 = bounds.x2;
			newBounds.y1 = bounds.y1;
			newBounds.y2 = bounds.y2;
			newBounds.width = bounds.width;
			newBounds.height = bounds.height;
			break;
	}
	return newBounds;
}

/**
 * Находит инфо элемента.
 * 
 * @param {Element}
 *            node
 * @param {String}
 *            parentId
 * @return {InfoElement}
 */
function getRefInfo(node, parentId) {
	// console.log(node);

    var nodeValue;
    if(ieXmlDom != null)
        nodeValue = node.text;
	else
        nodeValue = node.getValue();
	// /console.log(nodeValue);
	if (nodeValue.indexOf(parentId + ".") >= 0)
		return treeInfoEls.getElemInfo(nodeValue);
	else
		return treeInfoEls.getElemInfo(parentId + "-" + nodeValue);
}
