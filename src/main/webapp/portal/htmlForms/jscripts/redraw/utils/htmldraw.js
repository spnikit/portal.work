
/**
 * Объект, хранящий данные из itemlocation элемента
 */
function coordElHtml(){
	this.x = 0;
	this.y = 0;
	this.width = 0;
	this.height = 0;
	this.offsetx = 0;
	this.offsety = 0;
	this.isInitX = false;
	this.isInitY = false;
	this.position = "relative";
	
	this.setPar = function(par, val){
		switch (par){
			case 'x': this.x = parseInt(val); this.isInitX = true; this.position = "absolute"; break;
			case 'y': this.y = parseInt(val); this.isInitY = true; this.position = "absolute"; break;
			case 'width': this.width = parseInt(val); break;
			case 'height': this.height = parseInt(val); break;
			case 'offsetx': this.offsetx = parseInt(val); break;
			case 'offsety': this.offsety = parseInt(val); break;
			
			case 'after': this.after = val; this.position = "relative"; break;
			case 'before': this.before = val; this.position = "relative"; break;
			case 'below': this.below = val; this.position = "relative"; break;
			case 'above': this.above = val; this.position = "relative"; break;
			case 'within': this.within = val; break;
			// =========== alignGroup ===============
			// ========= bottom to xxxxx ============
			case 'alignb2b': this.alignb2b = val; break;
			case 'alignb2c': this.alignb2c = val; break;
			case 'alignb2t': this.alignb2t = val; break;
			// ========= center to xxxxx ============
			case 'alignc2b': this.alignc2b = val; break;
			case 'alignc2l': this.alignc2l = val; break;
			case 'alignc2r': this.alignc2r = val; break;
			case 'alignc2t': this.alignc2t = val; break;
			case 'alignhorizc2c': this.alignhorizc2c = val; break;
			case 'alignvertc2c': this.alignvertc2c = val; break;
			// ========== left to xxxxx =============
			case 'alignl2l': this.alignl2l = val; break;
			case 'alignl2c': this.alignl2c = val; break;
			case 'alignl2r': this.alignl2r = val; break;
			// ========= right to xxxxx =============
			case 'alignr2l': this.alignr2l = val; break;
			case 'alignr2c': this.alignr2c = val; break;
			case 'alignr2r': this.alignr2r = val; break;
			// =========== top to xxxxx =============
			case 'alignt2b': this.alignt2b = val; break;
			case 'alignt2c': this.alignt2c = val; break;
			case 'alignt2t': this.alignt2t = val; break;
			// ========== expandGroup ===============
			// ========= button to xx ===============
			case 'expandb2b': this.expandb2b = val; break;
			case 'expandb2c': this.expandb2c = val; break;
			case 'expandb2t': this.expandb2t = val; break;
			// ========== left to xx ================
			case 'expandl2l': this.expandl2l = val; break;
			case 'expandl2c': this.expandl2c = val; break;
			case 'expandl2r': this.expandl2r = val; break;
			// ========= rigth to xx ================
			case 'expandr2l': this.expandr2l = val; break;
			case 'expandr2c': this.expandr2c = val; break;
			case 'expandr2r': this.expandr2r = val; break;
			// ========== top to xx ================
			case 'expandt2b': this.expandt2b = val; break;
			case 'expandt2c': this.expandt2c = val; break;
			case 'expandt2t': this.expandt2t = val; break;
			// ==========================
			case 'expandheight': this.expandheight = val; break;
			case 'expandwidth': this.expandwidth = val; break;
			// ==========================
		}
	}
}


function emptyCoordEl(){
	this.x = 0;
	this.y = 0;
	this.width = 0;
	this.height = 0;
	this.offsetx = 0;
	this.offsety = 0;
	this.isInitX = false;
	this.isInitY = false;
	this.position = "relative";
	return this;
}

function emptySizeEl(){
	this.width = 0;
	this.height = 0;
	return this;
}

function getSizeEl(locEl){
	/**
	 * 
	 * @type Node|Element
	 */
	var childEl = locEl.firstChild;
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
	
	this.width = 0;
	this.height = 0;
	
	while (childEl){
		lNameChildEl = childEl.tagName.replace(new RegExp('.+:','g'), '');
		valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
		if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")){
			switch (lNameChildEl){
				case 'width' : this.width = parseInt(valChildEl); break;
				case 'height' : this.height = parseInt(valChildEl); break;
			}
		}
		childEl = childEl.nextSibling;
	}
	return this;
}

function getCoordEl(locEl){
	/**
	 * 
	 * @type Node|Element
	 */
	var childEl = locEl.firstChild;
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
	/**
	 * 
	 * @type coordElHtml
	 */
	var coordEl = new coordElHtml();
	
	while (childEl){
		lNameChildEl = childEl.tagName.replace(/.+:/g, '');
		valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
		if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")){
			coordEl.setPar(lNameChildEl, valChildEl);
		}
		childEl = childEl.nextSibling;
	}
	return coordEl;
}

/**
 * Функция поиска границ элемента. Происходит применение всех пунктов
 * содерджащихся в itemlocation.
 * 
 * @param {InfoElement}
 *            info
 * @param {coordElHtml}
 *            coordEl
 */
function htmlLocation(info, coordEl, prevInfo){
	var bounds = new Bounds(null);
	
	bounds.x1 = 0;
	bounds.y1 = 0;
	bounds.x2 = 0;
	bounds.y2 = 0;
	bounds.width = 0;
	bounds.height = 0;
	bounds.within = null;
	bounds.position = coordEl.position;
	
//	console.log(coordEl);
	
	//default
	/**
	 * 
	 * @type InfoElement 
	 */
	if (!prevInfo){
		if (lastDrawingItem){
			prevInfo = lastDrawingItem;
			bounds.y1 = prevInfo.y2 + coordEl.offsety + 5;
		} else {
			bounds.y1 += coordEl.offsety;
		}
	} else {
		bounds.y1 = prevInfo.y2 + coordEl.offsety + 5;
	}
	if (info.sid=="HEADER_SPACER1"){
		console.log("!!!!!!!!!!!!!!!!!!!!");
		console.log(prevInfo);
	}
//	console.log('!!!!! '+prevInfo.y2);
//	bounds.y1 = prevInfo.y2 + coordEl.offsety + 5;
//	bounds.y2 = bounds.y1 + bounds.height;
	bounds.x1 += coordEl.offsetx;
//	bounds.x2 = bounds.x1 + bounds.width;
//	if (prevInfo.bounds) bounds.within = prevInfo.bounds.within;
	//----------------
	
	if (coordEl.isInitX && coordEl.position=="absolute") {bounds.x1 = coordEl.x;}
	if (coordEl.isInitY && coordEl.position=="absolute") {bounds.y1 = coordEl.y;}
	if (coordEl.within) {bounds.within = coordEl.within;}
	if (coordEl.width && coordEl.width>0) {bounds.width = coordEl.width; bounds.x2 = bounds.x1 + coordEl.width;}
	if (coordEl.height && coordEl.height>0) {bounds.height = coordEl.height; bounds.y2 = bounds.y1 + coordEl.height;}
	
	if (coordEl.after && coordEl.position=="relative"){
		var refInfo = getRefInfo2(coordEl.after, info.parentFullSidRef);
		if (refInfo) {
//			bounds.x1 = coordEl.x; bounds.y1 = coordEl.y;
//			if (bounds.width){bounds.x2 += bounds.width;}
//			if (bounds.height){bounds.y2 += bounds.height;}
//		} else {
//			var dx = bounds.x1 - refInfo.x2;
//	//		dx = dx - 4;
//			bounds.x1 -= dx;
//			bounds.x2 -= dx;
//			var dy = bounds.y1 - refInfo.y1;
//			bounds.y1 -= dy;
//			bounds.y2 -= dy;
//			
			bounds.x1 = refInfo.x2 + coordEl.offsetx + 5;
			bounds.y1 = refInfo.y1 + coordEl.offsety;
			//if (bounds.width){bounds.x2 += bounds.width;}
			//if (bounds.height){bounds.y2 += bounds.height;}
			if (bounds.width){bounds.x2 = bounds.x1 + bounds.width;}
			if (bounds.height){bounds.y2 = bounds.y1 + bounds.height;}
		}
	}
	if (coordEl.before && coordEl.position=="relative"){
		var refInfo = getRefInfo2(coordEl.before, info.parentFullSidRef);
		if (refInfo) {
//			bounds.x1 = coordEl.x; bounds.y1 = coordEl.y;
//			if (bounds.width){bounds.x2 += bounds.width;}
//			if (bounds.height){bounds.y2 += bounds.height;}
//		} else {
//			var dx = bounds.x2 - refInfo.x1;
//	//		dx = dx - 4;
//			bounds.x1 -= dx;
//			bounds.x2 -= dx;
//			var dy = bounds.y1 - refInfo.y1;
//			bounds.y1 -= dy;
//			bounds.y2 -= dy;
			
			bounds.x2 = refInfo.x1 + coordEl.offsetx - 5;
			bounds.y1 = refInfo.y1 + coordEl.offsety;
//			if (bounds.width){bounds.x1 -= bounds.width;}
//			if (bounds.height){bounds.y2 += bounds.height;}
			if (bounds.width){bounds.x1 = bounds.x2 - bounds.width;}
			if (bounds.height){bounds.y2 = bounds.y1 + bounds.height;}
		}
	}
	if (coordEl.below && coordEl.position=="relative"){
		var refInfo = getRefInfo2(coordEl.below, info.parentFullSidRef);
		if (refInfo) {
//			bounds.x1 = coordEl.x; bounds.y1 = coordEl.y;
//			if (bounds.width){bounds.x2 += bounds.width;}
//			if (bounds.height){bounds.y2 += bounds.height;}
//		} else {
//			var dx = bounds.x1 - refInfo.x1;
//			bounds.x1 -= dx;
//			bounds.x2 -= dx;
//			var dy = bounds.y1 - refInfo.y2;
//	//		dy = dy - 5;
//			bounds.y1 -= dy;
//			bounds.y2 -= dy;
			bounds.x1 = refInfo.x1 + coordEl.offsetx;
			bounds.y1 = refInfo.y2 + coordEl.offsety + 5;
//			if (bounds.width){bounds.x2 += bounds.width;}
//			if (bounds.height){bounds.y2 += bounds.height;}
			if (bounds.width){bounds.x2 = bounds.x1 + bounds.width;}
			if (bounds.height){bounds.y2 = bounds.y1 + bounds.height;}
		}
	}
	if (coordEl.above && coordEl.position=="relative"){
		var refInfo = getRefInfo2(coordEl.above, info.parentFullSidRef);
		if (refInfo) {
//			bounds.x1 = coordEl.x; bounds.y1 = coordEl.y;
//			if (bounds.width){bounds.x2 += bounds.width;}
//			if (bounds.height){bounds.y2 += bounds.height;}
//		} else {
//			var dx = bounds.x1 - refInfo.x1;
//			bounds.x1 -= dx;
//			bounds.x2 -= dx;
//			var dy = bounds.y2 - refInfo.y1;
//	//		dy = dy + 5;
//			bounds.y1 -= dy;
//			bounds.y2 -= dy;
			
			bounds.x1 = refInfo.x1 + coordEl.offsetx;
			bounds.y2 = refInfo.y1 + coordEl.offsety - 5;
//			if (bounds.width){bounds.x2 += bounds.width;}
//			if (bounds.height){bounds.y1 -= bounds.height;}
			if (bounds.width){bounds.x2 = bounds.x1 + bounds.width;}
			if (bounds.height){bounds.y1 = bounds.y2 - bounds.height;}
		}
	}
	// ======================================
	if (!bounds.width){bounds.x2 = bounds.x1}
	if (!bounds.height){bounds.y2 = bounds.y1}
//	bounds.position = "absolute";
	// ========= bottom to xxxxx ============
	if (coordEl.alignb2b){
		var refInfo = getRefInfo2(coordEl.alignb2b, info.parentFullSidRef);
		if (refInfo){
			var delta = bounds.y2 - refInfo.y2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	if (coordEl.alignb2c){
		var refInfo = getRefInfo2(coordEl.alignb2c, info.parentFullSidRef);
		if (refInfo){
			var delta = bounds.y2 - (refInfo.y1 + refInfo.y2) / 2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	if (coordEl.alignb2t){
		var refInfo = getRefInfo2(coordEl.alignb2t, info.parentFullSidRef);
		if (refInfo){
			var delta = bounds.y2 - refInfo.y1;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	// ========= center to xxxxx ============
	if (coordEl.alignc2b){
		var refInfo = getRefInfo2(coordEl.alignc2b, info.parentFullSidRef);
		if (refInfo){
			var delta = (bounds.y1 + bounds.y2) / 2 - refInfo.y2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	if (coordEl.alignc2l){
		var refInfo = getRefInfo2(coordEl.alignc2l, info.parentFullSidRef);
		if (refInfo){
			var delta = (bounds.x1 + bounds.x2) / 2 - refInfo.x1;
			bounds.x1 -= delta;
			bounds.x2 -= delta;
		}
	}
	if (coordEl.alignc2r){
		var refInfo = getRefInfo2(coordEl.alignc2r, info.parentFullSidRef);
		if (refInfo){
			var delta = (bounds.x1 + bounds.x2) / 2 - refInfo.x2;
			bounds.x1 -= delta;
			bounds.x2 -= delta;
		}
	}
	if (coordEl.alignc2t){
		var refInfo = getRefInfo2(coordEl.alignc2t, info.parentFullSidRef);
		if (refInfo){
			var delta = (bounds.y1 + bounds.y2) / 2 - refInfo.y1;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	if (coordEl.alignhorizc2c){
		var refInfo = getRefInfo2(coordEl.alignhorizc2c, info.parentFullSidRef);
		if (refInfo){
			var delta = (bounds.x1 + bounds.x2) / 2 - (refInfo.x1 + refInfo.x2) / 2;
			bounds.x1 -= delta;
			bounds.x2 -= delta;
		}
	}
	if (coordEl.alignvertc2c){
		var refInfo = getRefInfo2(coordEl.alignvertc2c, info.parentFullSidRef);
		if (refInfo){
			var delta = (bounds.y1 + bounds.y2) / 2 - (bounds.y1 + bounds.y2) / 2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	// ========== left to xxxxx =============
	if (coordEl.alignl2l){
		var refInfo = getRefInfo2(coordEl.alignl2l, info.parentFullSidRef);
		if (refInfo){
			var dx = bounds.x1 - refInfo.x1;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
		}
	}
	if (coordEl.alignl2c){
		var refInfo = getRefInfo2(coordEl.alignl2c, info.parentFullSidRef);
		if (refInfo){
			var dx = bounds.x1 - (refInfo.x1 + refInfo.x2) / 2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
		}
	}
	if (coordEl.alignl2r){
		var refInfo = getRefInfo2(coordEl.alignl2r, info.parentFullSidRef);
		if (refInfo){
			var dx = bounds.x1 - refInfo.x2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
		}
	}
	// ========= right to xxxxx =============
	if (coordEl.alignr2l){
		var refInfo = getRefInfo2(coordEl.alignr2l, info.parentFullSidRef);
		if (refInfo){
			var dx = bounds.x2 - refInfo.x1;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
		}
	}
	if (coordEl.alignr2c){
		var refInfo = getRefInfo2(coordEl.alignr2c, info.parentFullSidRef);
		if (refInfo){
			var dx = bounds.x2 - (refInfo.x1 + refInfo.x2) / 2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
		}
	}
	if (coordEl.alignr2r){
		var refInfo = getRefInfo2(coordEl.alignr2r, info.parentFullSidRef);
		if (refInfo){
			var dx = bounds.x2 - refInfo.x2;
			bounds.x1 -= dx;
			bounds.x2 -= dx;
		}
	}
	// =========== top to xxxxx =============
	if (coordEl.alignt2b){
		var refInfo = getRefInfo2(coordEl.alignt2b, info.parentFullSidRef);
		if (refInfo){
			var delta = bounds.y1 - refInfo.y2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	if (coordEl.alignt2c){
		var refInfo = getRefInfo2(coordEl.alignt2c, info.parentFullSidRef);
		if (refInfo){
			var delta = bounds.y1 - (refInfo.y1 + refInfo.y2) / 2;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	if (coordEl.alignt2t){
		var refInfo = getRefInfo2(coordEl.alignt2t, info.parentFullSidRef);
		if (refInfo){
			var delta = bounds.y1 - refInfo.y1;
			bounds.y1 -= delta;
			bounds.y2 -= delta;
		}
	}
	// ========== expandGroup ===============
	// ========= bottom to xx ===============
	if (coordEl.expandb2b){
		var refInfo = getRefInfo2(coordEl.expandb2b, info.parentFullSidRef);
		if (refInfo){
			bounds.y2 = refInfo.y2;
			bounds.height = bounds.y2 - bounds.y1;
		}
	}
	if (coordEl.expandb2c){
		var refInfo = getRefInfo2(coordEl.expandb2c, info.parentFullSidRef);
		if (refInfo){
			bounds.y2 = (refInfo.y1 + refInfo.y2) / 2;
			bounds.height = bounds.y2 - bounds.y1;
		}
	}
	if (coordEl.expandb2t){
		var refInfo = getRefInfo2(coordEl.expandb2t, info.parentFullSidRef);
		if (refInfo){
			bounds.y2 = refInfo.y1;
			bounds.height = bounds.y2 - bounds.y1;
		}
	}
	// ========== left to xx ================
	if (coordEl.expandl2l){
		var refInfo = getRefInfo2(coordEl.expandl2l, info.parentFullSidRef);
		if (refInfo){
			bounds.x1 = refInfo.x1;
			bounds.width = bounds.x2 - bounds.x1;
		}
	}
	if (coordEl.expandl2c){
		var refInfo = getRefInfo2(coordEl.expandl2c, info.parentFullSidRef);
		if (refInfo){
			bounds.x1 = (refInfo.x1 + refInfo.x2) / 2;
			bounds.width = bounds.y2 - bounds.y1;
		}
	}
	if (coordEl.expandl2r){
		var refInfo = getRefInfo2(coordEl.expandl2r, info.parentFullSidRef);
		if (refInfo){
			bounds.x1 = refInfo.x2;
			bounds.width = bounds.x2 - bounds.x1;
		}
	}
	// ========= rigth to xx ================
	if (coordEl.expandr2l){
		var refInfo = getRefInfo2(coordEl.expandr2l, info.parentFullSidRef);
		if (refInfo){
			bounds.x2 = refInfo.x1;
			bounds.width = bounds.x2 - bounds.x1;
		}
	}
	if (coordEl.expandr2c){
		var refInfo = getRefInfo2(coordEl.expandr2c, info.parentFullSidRef);
		if (refInfo){
			bounds.x2 = (refInfo.x1 + refInfo.x2) / 2;
			bounds.width = bounds.y2 - bounds.y1;
		}
	}
	if (coordEl.expandr2r){
		var refInfo = getRefInfo2(coordEl.expandr2r, info.parentFullSidRef);
		if (refInfo){
			bounds.x2 = refInfo.x2;
			bounds.width = bounds.x2 - bounds.x1;
		}
	}
	// ========== top to xx ================
	if (coordEl.expandt2b){
		var refInfo = getRefInfo2(coordEl.expandt2b, info.parentFullSidRef);
		if (refInfo){
			bounds.y1 = refInfo.y2;
			bounds.height = bounds.y2 - bounds.y1;
		}
	}
	if (coordEl.expandt2c){
		var refInfo = getRefInfo2(coordEl.expandt2c, info.parentFullSidRef);
		if (refInfo){
			bounds.y1 = (refInfo.y1 + refInfo.y2) / 2;
			bounds.height = bounds.y2 - bounds.y1;
		}
	}
	if (coordEl.expandt2t){
		var refInfo = getRefInfo2(coordEl.expandt2t, info.parentFullSidRef);
		if (refInfo){
			bounds.y1 = refInfo.y1;
			bounds.height = bounds.y2 - bounds.y1;
		}
	}
	// ==========================
	if (coordEl.expandheight){
		bounds.y2 += parseInt(coordEl.expandheight);
		bounds.height = bounds.y2 - bounds.y1;
	}
	if (coordEl.expandwidth){
		bounds.x2 += parseInt(coordEl.expandwidth);
		bounds.width = bounds.x2 - bounds.x1;
	}
	
	return bounds;
}

			/*
			// ==========================
			case 'expandheight': this.expandheight = val; break;
			case 'expandwidth': this.expandwidth = val; break;*/

/**
 * Находит инфо элемента.
 * 
 * @param {String}
 *            nodeValue
 * @param {String}
 *            parentId
 * @return {InfoElement}
 */
function getRefInfo2(nodeValue, parentId) {
	//var nodeValue = node.getValue();
	if (nodeValue.indexOf(parentId + ".") >= 0)
		return treeInfoEls.getElemInfo(nodeValue);
	else
		return treeInfoEls.getElemInfo(parentId + "-" + nodeValue);
}