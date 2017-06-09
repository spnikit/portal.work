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
 * @return {HTMLElement} - полученный элемент
 */

function drawAction(el, parentId, parentXformRef) {
	var info = new InfoElement();
	info.typeEl = "action";
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

	// альтернативный и более правильный метод перебора элементов чем через
	// xpath
	childEl = el.firstChild;
	while (childEl) // сначала переберем xforms-узлы
	{
		if (childEl.nodeType == Node.ELEMENT_NODE
				&& childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")) {
			if (childEl.tagName.replace(new RegExp('.+:','g'), '') == "submit" || childEl.tagName.replace(new RegExp('.+:','g'), '') == "trigger") {

				break;
			}

		}
		childEl = childEl.nextSibling;
	}
	// сделано для того чтобы распознаватьнаходение внутри pane или table, т.к.
	// у
	// action не предусмотрен value
	if (el.parentNode.tagName.replace(new RegExp('.+:','g'), '') != "page")
		info.xformNode = true;

	// теперь переберем остальные-узлы
	childEl = el.firstChild;
	while (childEl)// альтернативный и более правильный метод перебора
	// элементов
	// чем через xpath
	{
		if (childEl.nodeType == Node.ELEMENT_NODE) {
			lNameChildEl = childEl.tagName.replace(new RegExp('.+:','g'), '');
			valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
			if (childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {

				switch (lNameChildEl) {
					// case "value" :
					// info.hiddenTeg['value'] = childEl;
					// value = childEl.getValue();

					// break;

					case 'delay' :
						if (valChildEl == 'off') {
							info.delayType = ieXmlDom != null ? childEl
                                .getElementByTagName('type').item(0)
                                .text : childEl
									.getElementByTagName('type').item(0)
									.getValue();
							if (info.delayType != 'repeat')
								info.delayType = 'once';
							try {
                                if(ieXmlDom != null) {
                                    info.delayInterval = parseInt(childEl
                                        .getElementByTagName('interval')
                                        .item(0).text);
                                } else {
								    info.delayInterval = parseInt(childEl
										.getElementByTagName('interval')
										.item(0).getValue());
                                }
								if (info.delayInterval < -1)
									info.delayInterval = 0;
							} catch (e) {
								info.delayInterval = 0;
							}
							if (info.delayInterval == 0)// чтобы не получить
														// бесконечно
							// повторяющихся действий
							{
								info.delayType = 'once';
							}
						}
						break;

					case 'active' :
						if (valChildEl == 'on') {
							if (info.delayInterval != -1)
								stackForActionsAfterRedraw.push(function() {
											info.delayType == 'once'
													? info.idTimer = setTimeout(
															info.actionFunction,
															info.delayInterval)
													: info.idTimer = setInterval(
															info.actionFunction,
															info.delayInterval);

										});
							else
								stackForActionsAfterRedraw.push(function() {
											info.actionFunction();
										});
						}
						break;
					case 'type' :
						info.typeAction = valChildEl;
						break;
					case 'url' :
						info.url = valChildEl;
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
								childEl.parentNode);// так
						// как
						// есть
						// xform-связь
						info.hiddenTeg[lNameChildEl] = emNode
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
	info.actionFunction = function() {

	};
	
	/**
	 * @type Node
	 */
	var activated = new EmulateNode('activated', 'off', el);
	info.hiddenTeg["activated"] = activated;
	
	treeInfoEls.addElemInfo(info);
	
	switch (info.typeAction) {
		case 'done' :
			info.actionFunction = function() {
				if (info.url) {
					treeInfoPages.hideActivePage();
					if (window.parent.resetFormView)
						with (window.parent) {
							resetFormView();
						}
					window.location.assign(info.url);
				}
			}
			break;
		// TODO это заглушка
		case 'replace' :// отменить ссылку на которую произойдет переход
			info.actionFunction = function() {
				if (info.url)
					if (info.url.indexOf('javascript:') != -1)
						window.eval(info.url);
			}
			break;
		case 'pagedone' :
			break;
		case 'select' :
			break;

	}

	return null;
};
