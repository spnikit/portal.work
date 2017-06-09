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

function drawData(el, parentId, parentXformRef) {
	var info = new InfoElement();
	info.refElem = el;
	info.sid = el.getAttribute("sid");
	info.fullSidRef = "" + parentId + "-" + el.getAttribute("sid");
	info.parentFullSidRef = "" + parentId;
	info.typeEl = "data";
	info.massRelElsImage = new Array();
	info.bounds = null;
	/**
	 * 
	 * @type Element
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

	childEl = el.firstChild;

	while (childEl) {
		if (childEl.nodeType == Node.ELEMENT_NODE) {
			lNameChildEl = childEl.tagName.replace(new RegExp('.+:','g'), '');
            valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {

				switch (lNameChildEl) {
					case "datagroup" :
						break;
					case "filename" :
						break;
					case "mimedata" :
						break;
					case "mimetype" :
						break;

				}
				var attrScript = childEl.getAttribute('compute');
				if (attrScript) {
					/**
					 * @type EmulateNode|Element
					 */
					var emNode;
					if (info.xformNode) {
						emNode = new EmulateNode(lNameChildEl, valChildEl,
								childEl.parentNode);// так как есть xform-связь
						info.hiddenTeg[lNameChildEl] = emNode;
					} else
						emNode = childEl;
					var scriptCompute = new UnitScript(attrScript, info, emNode);
					massAllScripts.push(scriptCompute);
				}

			}
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("custom")) {
				// console.info(childEl.namespaceURI);
				// console.info(getNameSpaceForXfdlForm("custom"));

				var attrScript = childEl.getAttributeNS(
						getNameSpaceForXfdlForm("xfdl"), 'compute');
				// console.info(attrScript);
				if (attrScript) {

					/**
					 * @type EmulateNode|Element
					 */
					var emNode;
					if (info.xformNode) {
						emNode = new EmulateNode('custom:' + lNameChildEl,
								valChildEl, childEl.parentNode);// так как есть
																// xform-связь
						info.hiddenTeg['custom:' + lNameChildEl] = emNode;
						// console.info("info.hiddenTeg[custom:" + lNameChildEl
						// + "] = " +
						// info.hiddenTeg['custom:' + lNameChildEl]);
					} else
						emNode = childEl;
					var scriptCompute = new UnitScript(attrScript, info, emNode);
					massAllScripts.push(scriptCompute);
				}
			}

		}
		childEl = childEl.nextSibling;
	}

	treeInfoEls.addElemInfo(info);

	return info;
};
/**
 * 
 * @param {Element}
 *            pageEl
 * @param {Element}
 *            previosEl
 * @param {String}
 *            sid
 * @param {String}
 *            mimetype
 * @param {String}
 *            filename
 * @param {String}
 *            mimedata
 * @param {String}
 *            encMimedata
 */
function insertDataEl(pageEl, beforeEl, sid, mimetype, filename, mimedata,
		encMimedata) {
	var dataEl = xfdlForm.createElementNS(getNSX('xfdl'), 'data')
	dataEl.setAttribute('sid', sid);

	var mimetypeEl = xfdlForm.createElementNS(getNSX('xfdl'), 'mimetype')
	mimetypeEl.setValue(mimetype);

	var filenameEl = xfdlForm.createElementNS(getNSX('xfdl'), 'filename')
	filenameEl.setValue(filename);

	var mimedataEl = xfdlForm.createElementNS(getNSX('xfdl'), 'mimedata')
	mimedataEl.setAttribute("encoding", encMimedata);
	mimedataEl.setValue(mimedata);

	dataEl.appendChild(xfdlForm.createTextNode('\n         '));
	dataEl.appendChild(mimetypeEl);

	dataEl.appendChild(xfdlForm.createTextNode('\n         '));
	dataEl.appendChild(filenameEl);

	dataEl.appendChild(xfdlForm.createTextNode('\n         '));
	dataEl.appendChild(mimedataEl);

	dataEl.appendChild(xfdlForm.createTextNode('\n      '));
	pageEl.insertBefore(dataEl, beforeEl);
	pageEl.insertBefore(xfdlForm.createTextNode("\n      "), beforeEl);
	redrawXFDLElement(dataEl);

};