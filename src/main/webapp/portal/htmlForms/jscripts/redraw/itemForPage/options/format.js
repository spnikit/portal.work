/**
 * @include "../../../includes.js"
 */

/**
 * 
 * @param {Element}
 *            formatEl
 * 
 * @return {FormatObj}
 */
function parseFormat(formatEl) {
	var formatObj = new FormatObj(false);
	/**
	 * 
	 * @type Element
	 */
	var dopChild = null;
	var dopValChild = null;

	var tmpChild = formatEl.firstChild;
	/**
	 * режим обработки("pr" - presentation,"cr" - constraints,"" - обычный)
	 * 
	 * @type String
	 */

	while (tmpChild) {
		if (tmpChild.nodeType == Node.ELEMENT_NODE) {
            var valEl;
            if(ieXmlDom != null)
                valEl = tmpChild.text;
			else
                valEl = tmpChild.getValue();
			switch (tmpChild.tagName.replace(new RegExp('.+:','g'), '')) {
				case 'datatype' :
					// TODO посмотреть что будет если тип равен void
					if (valEl == "void")
						this.isNoEffect = true;
					formatObj.datatype = valEl;
					break;
				case 'presentation' :
					dopChild = tmpChild.firstChild;

					while (dopChild) {
						if (dopChild.nodeType == Node.ELEMENT_NODE) {
							formatObj.prSetting[dopChild.tagName.replace(/.+:/g, '')] = ieXmlDom != null ? dopChild.text : dopChild.getValue();

						}
						dopChild = dopChild.nextSibling;
					}
					break;
				case 'constraints' :
					// TODO доделать
					dopChild = tmpChild.firstChild;
					while (dopChild) {
						if (dopChild.nodeType == Node.ELEMENT_NODE) {
                            if(ieXmlDom != null)
                                dopValChild = dopChild.text;
							else
                                dopValChild = dopChild.getValue();
                            var name = "";
                            if(dopChild.tagName != null)
                                name = dopChild.tagName.replace(/.+:/g, '');
							switch (name) {
								case 'mandatory' :
									if (dopValChild == 'on')
										formatObj.crSetting['mandatory'] = true;
									break;
								case 'patterns' :

									/**
									 * @type NodeList
									 */
									var allPat = dopChild.getElementsByTagName('pattern');
//									formatObj.crSetting.patterns = new Array(allPat.length);
//									for (var k = 0; k < allPat.length; k++) {
//										formatObj.crSetting.patterns[k] = allPat
//												.item(k).getValue();
//									}
									formatObj.crSetting.patterns = new Array();
									for (var k = 0; k < allPat.length; k++) {
                                        if (ieXmlDom != null &&
                                            allPat.item(k).text!=null && allPat.item(k).text!='') {
                                            formatObj.crSetting.patterns.push(allPat
                                                .item(k).text);
                                        } else if (allPat.item(k).getValue()!=null && allPat.item(k).getValue()!=''){
											formatObj.crSetting.patterns.push(allPat
												.item(k).getValue());
										}
									}
									break;

								case 'lenght' :
									var minEl = dopChild
											.getElementsByTagName('min')
											.item(0);
									if (minEl)
										formatObj.crSetting.minChar = ieXmlDom != null ?
                                            parseInt(minEl.text) * 1 : minEl.getValue() * 1;

									var maxEl = dopChild
											.getElementsByTagName('max')
											.item(0);
									if (maxEl)
										formatObj.crSetting.mxaChar = ieXmlDom != null ?
                                            parseInt(maxEl.text) * 1 :
                                            maxEl.getValue() * 1;
									break;
								case 'message' :
									formatObj.crSetting['message'] = dopValChild;
									break;

							}

						}
						dopChild = dopChild.nextSibling;
					}
					break;
			}
		}
		tmpChild = tmpChild.nextSibling;
	}

	if (!formatObj.prSetting.style) {
		formatObj.prSetting.style = 'short';
	}
	if (!formatObj.prSetting.pattern) {
		formatObj.prSetting.pattern = defXfdlPattern[formatObj.datatype + '-'
				+ formatObj.prSetting.style]
				? defXfdlPattern[formatObj.datatype + '-'
						+ formatObj.prSetting.style]
				: '';
	}

	return formatObj;
}

/**
 * 
 * @param {Boolean}
 *            isNo - создает ли формат ограничения(по умолчанию true)
 * @class
 * @constructor
 * 
 */
function FormatObj(isNo) {
	this.datatype = null;
	/**
	 * создает ли формат ограничения
	 * 
	 * @type Boolean
	 */
	this.isNoEffect = null;

	this.prSetting = {
		pattern : null,
		style : null,
		keepformatindata : 'off'
	}
	this.crSetting = {
		minChar : 0,
		maxChar : 100000,

		// pattern : null,
		mandatory : false,
		message : null,
		/**
		 * 
		 * @type String[]|null
		 */
		patterns : null,
		keepformatindata : false

	}

	if (isNo === undefined)
		this.isNoEffect = true;
	else
		this.isNoEffect = isNo;

}