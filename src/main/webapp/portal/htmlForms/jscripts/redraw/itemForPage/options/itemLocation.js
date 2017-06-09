

/**
 * Функция поиска границ элемента. Происходит применение всех пунктов
 * содерджащихся в itemlocation.
 * 
 * @param {Element}
 *            el
 * @param {String}
 *            parentId
 * @param {Number}
 *            type
 * @return {Bounds}
 */
function findBounds(el, parentId, type) {
	// ------------------------------------------

	var thisID = parentId + "-" + el.getAttribute("sid");
	/**
	 * 
	 * @type InfoElement
	 */
	var currentInfo = treeInfoEls.getElemInfo(thisID);

	var bounds = new Bounds(type);

	// if earlier we calculate bounds
	if (currentInfo) {
		bounds.x1 = currentInfo.x1;
		bounds.x2 = currentInfo.x2;
		bounds.y1 = currentInfo.y1;
		bounds.y2 = currentInfo.y2;
		bounds.height = currentInfo.height;
		bounds.width = currentInfo.width;

		// if it is initiator then return bound, because this bounds are
		// computed
		if (currentInfo.isInitiator)
			return bounds;
	}

	var lastEl = el.previousSibling;
	// var lastEl = el.xpath("./preceding-sibling::*[last()]").iterateNext();
	while (lastEl) {
		if (lastEl.nodeType == Node.ELEMENT_NODE)
			break;
		lastEl = lastEl.previousSibling;
	}
	var lastElId = lastEl ? lastEl.getAttribute('sid') : null;
	// console.logDraw("lastElId = " + lastElId);
	if (lastElId) {
		var refInfo = treeInfoEls.getElemInfo(parentId + "-" + lastElId);
		if (refInfo) {
			// TODO Временно, доделать для контента содержащегося в checkgroup и
			// radiogroup
            if (el.tagName.replace(new RegExp('.+:','g'), '') == 'table' || el.tagName.replace(new RegExp('.+:','g'), '') == 'pane')// позиционирвание
			// как
			// <layoutflow>block</layoutflow>
			{
				var dy = bounds.y1 - refInfo.y2;
				bounds.y1 -= dy;
				bounds.y2 -= dy;
			} else
			// позиционирвание как <layoutflow>inline&lt;/layoutflow>
			{
				var dx = bounds.x1 - refInfo.x2;
				dx = dx - 4;
				bounds.x1 -= dx;
				bounds.x2 -= dx;
				// var dy = bounds.y1 - refInfo.y1;
				// bounds.y1 -= dy;
				// bounds.y2 -= dy;

			}
		}
	}

	// in all otehr cases recalculate bound
    if(ieXmlDom != null) {
        var itemlocation = ieXpathEvaluate(el, "itemlocation")[0];
    } else {
	    var itemlocation = el.xpath("xfdl:itemlocation").iterateNext();
    }
	// there are cases when itemlocataion not found then return found by 'after'
	// bounds
	if (!itemlocation)
		return bounds;

	var usePadding = !haveRelatedLink(itemlocation);

	var padding = 0;

	// в случае если есть относительные ссылки и элемент находится в &lt;pane>
	// или
	// &lt;table> то выставить отступ
    if(ieXmlDom != null) {
        if(usePadding) {
            var elem = ieXpathEvaluate(itemlocation, "../../..")[0];
            var elemName = "";
            if(elem != null)
                elemName = elem.tagName;
            if(elemName == 'pane' || elemName == 'table') {
//                var pad = ieXpathEvaluate(itemlocation, '../../../padding')[0];
//                if(pad != null && pad.text != '') {
//                    padding = parseInt(pad.text);
//                } else {
                    padding = 3;
//                }
            }
        }
    } else
        if (usePadding
                && itemlocation
                        .xpath("name(../../..) = 'pane' or name(../../..) = 'table'").booleanValue) {
            if (itemlocation.xpath("../../../padding != ''").booleanValue) {
                padding = itemlocation.xpath("../../../padding").numberValue;
            } else {
                padding = 3;
            }
        }

	// if earlie we calculate bounds then subtract padding for add lating
	if (currentInfo) {
		bounds.x1 -= padding;
		bounds.x2 -= padding;
		bounds.y1 -= padding;
		bounds.y2 -= padding;
	}
	var nodeList = itemlocation.childNodes;
	/**
	 * текущий элемент уже имеется в дереве элементов(отрисован)
	 * 
	 * @type Boolean
	 */
	var isInfoCalculated = currentInfo ? true : false;
	// if(currentInfo)
	// isComputed = currentInfo.isComputed;

	// recalculating only relative itemlocation tegs if element info is already
	// calculated
	// console.info("===========");
	for (var i = 0; i < nodeList.length; i++) {
		if (nodeList.item(i).nodeType == Node.ELEMENT_NODE)
			applyBounds(bounds, nodeList.item(i), parentId, isInfoCalculated);
	}

	// add padding
	// console.log('bounds.x1 = ' + bounds.x1);
	bounds.x1 += padding;
	// console.log('bounds.x1+ = ' + bounds.x1);
	bounds.x2 += padding;

	bounds.y1 += padding;
	bounds.y2 += padding;

	return bounds;
};

/**
 * объект содержащий координаты углов элемента,его ширинцу и высоту. Все
 * параметры в конструкторе инициализируются нулями, НО ,в случае, если type==1,
 * то y2=22,height=22;
 * 
 * @param {Number}
 *            type
 * 
 * 
 * @constructor
 * @class
 */
function Bounds(type) {

	// if(this.height>18.5 && this.height<18.7)

	/**
	 * @type Number
	 */
	this.x1 = 0;

	/**
	 * @type Number
	 */
	this.y1 = 0;

	/**
	 * @type Number
	 */
	this.x2 = 0;

	/**
	 * @type Number
	 */
	this.y2 = 0;

	/**
	 * @type Number
	 */
	this.width = 0;

	/**
	 * @type Number
	 */
	this.height = 0;

	/**
	 * 
	 * @type String
	 */
	this.within = null;

	/**
	 * координата X инициализированна четко(через &lt;x>)
	 * 
	 * @type Boolean
	 */
	this.isInitX = false;
	/**
	 * координата Y инициализированна четко(через &lt;Y>)
	 * 
	 * @type Boolean
	 */
	this.isInitY = false;

	if (type) {
		switch (type) {
			case 1 :
				this.y2 = 22;
				this.height = 22;
				break;
			case 2 :
				this.y2 = 17;
				this.height = 17;
				break;
		}
	}

};

/**
 * Функция поиска наличия позиционной ссылки в &lt;itemlocation>. Если есть тэг
 * ссылка, то вернет <b>true</b>, если нет - <b>false</b>
 * 
 * @param {Element}
 *            itemlocation
 * @return {Boolean} - вернет <b>false</b>, если есть хотя бы один из следующих
 *         узлов:<br/> &#x9;&#x9;<b>&lt;x></b>, <b>&lt;y></b>, <b>&lt;width></b>,
 *         <b>&lt;height></b>, <b>&lt;offsetx></b>, <b>&lt;offsety></b>,
 *         <b>&lt;expandheight></b>, <b>&lt;expandwidth></b><br/> в противном
 *         случае вернет <b>true</b>
 */
function haveRelatedLink(itemlocation) {
	var findLink = false;
	var nodeList = itemlocation.childNodes;

	for (var i = 0; i < nodeList.length && !findLink; i++) {

		if (nodeList.item(i).nodeType != Node.ELEMENT_NODE)
			continue;
		switch (nodeList.item(i).tagName.replace(/.+:/g, ''))
		// если имя первого тэга не одно из следующих то это позиционная ссылка
		{
			case "x" :
			case "y" :
			case "width" :
			case "height" :
			case "offsetx" :
			case "offsety" :
			case "expandheight" :
			case "expandwidth" :
				break;
			default :
				findLink = true;
		}
	}

	return findLink;
};

/**
 * Переменная определяет прошли мы инициатора или нет. Необходим чтобы не
 * пересчитывать элементы до инициатора, такое возникает если он находится
 * внутри, например, таблицы.
 * 
 * @type {Boolean}
 */
var isAfterInitiator = false;// = treeInfoEls.getElemInfo("" + fullSidRef);

/**
 * Функция пересчета границ элементов следующих за инициатором. В случае, если
 * инициатор находится в табилце или пейне, то идет перерасчет начиная с
 * прародителя инициатора, который является непосредственно одним из детей page.
 * 
 * Function for recalculation elements bounds which follows after initiator. If
 * initiator is in table or in pane, then recalculation start from ancestor,
 * which is child of page element.
 * 
 * @param {String}
 *            fullSidRef
 * @return {Boolean}
 */
function recalculateBounds(fullSidRef) {
	/**
	 * @type {InfoElement}
	 */
	var elementInfo;// = treeInfoEls.getElemInfo("" + fullSidRef);

	// set initiator flag
	elementInfo.isInitiator = true;

	/**
	 * @type {Element}
	 */
	var el = elementInfo.refElem;

	var itemlocation = el.xpath("xfdl:itemlocation").iterateNext();

	while (el.xpath("name(..) != 'page'").booleanValue)
		el = el.xpath("..").iterateNext();

	var curSid = el.getAttribute("sid");
	var parentSid = el.xpath("string(../@sid)").stringValue;

	var mainBodyBounds = new Bounds(null);
	var foundBounds = null;
	var currentInfo = null;
	// reset flag
	isAfterInitiator = false;
	// ------------------------------------------

	// ------------------------------------------
	while (currentEl) {
		currentInfo = treeInfoEls.getElemInfo("" + parentSid + "-"
				+ currentEl.getAttribute("sid"));
		foundBounds = null;
		if (currentEl.nodeType == Node.ELEMENT_NODE) {
			switch (currentEl.localName) {
				case "action" :
					break;
				case "box" :
					foundBounds = findCoordBox(currentInfo);
					break;
				case "button" :
					foundBounds = findCoordButton(currentInfo);
					break;
				case "cell" :
					break;
				case "check" :
					foundBounds = findCoordCheck(currentInfo);
					break;
				case "combobox" :
					foundBounds = findCoordCombobox(currentInfo);
					break;
				case "data" :
					break;
				case "field" :
					foundBounds = findCoordField(currentInfo);
					break;
				case "help" :
					break;
				case "label" :
					foundBounds = findCoordLabel(currentInfo);
					break;
				case "line" :
					foundBounds = findCoordLine(currentInfo);
					break;
				case "list" :
					// drawList(currentEl);
					break;
				case "pane" :
					foundBounds = findCoordPane(currentInfo);
					break;
				case "popup" :
					foundBounds = findCoordPopup(currentInfo);
					break;
				case "radio" :
					foundBounds = findCoordRadio(currentInfo);
					break;
				case "signature" :
					break;
				case "spacer" :
					foundBounds = findCoordSpacer(currentInfo);
					break;
				case "table" :
					foundBounds = findCoordTable(currentInfo);
					break;
			}

			if (foundBounds) {
				if (mainBodyBounds.x2 < foundBounds.x2) {
					mainBodyBounds.x2 = foundBounds.x2;
				}
				if (mainBodyBounds.y2 < foundBounds.y2) {
					mainBodyBounds.y2 = foundBounds.y2;
				}
			}

			if (!isAfterInitiator)
				isAfterInitiator = true;
		}
		currentEl = currentEl.nextSibling;
	}
	// ------------------------------------------

	// ------------------------------------------
	if (width < mainBodyBounds.x2)
		width = mainBodyBounds.x2;

	if (height < mainBodyBounds.y2)
		height = mainBodyBounds.y2;
	// ------------------------------------------

	// ------------------------------------------
	var pageSid = elementInfo.parentFullSidRef;
	var pageInfo = treeInfoEls.getElemInfo(pageSid);
	pageInfo.width = width;
	pageInfo.height = height;
	pageInfo.x2 = width;
	pageInfo.y2 = pageInfo.y1 + height;
	// ------------------------------------------

	// ------------------------------------------
	var pageDiv = pageInfo.refHTMLElemDiv;
	pageDiv.style.width = pageInfo.width;
	pageDiv.style.height = pageInfo.height;
	// ------------------------------------------

	// ------------------------------------------
	elementInfo.isInitiator = false;
	// ------------------------------------------
};
