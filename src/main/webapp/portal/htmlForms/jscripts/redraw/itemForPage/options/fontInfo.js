/**
 * @include "../../../includes.js"
 */

/**
 * 
 * @param {Element}
 *            fontInfoEl
 * 
 * @return {FontInfoObj}
 */
function parseFontInfo(fontInfoEl) {
	var fontInfoObj = new FontInfoObj();

	var tmpChild = fontInfoEl.firstChild;
	while (tmpChild) {
		if (tmpChild.nodeType == Node.ELEMENT_NODE) {
            var valEl;
            if(ieXmlDom != null)
                valEl = tmpChild.text;
			else
                valEl = tmpChild.getValue();
			switch (tmpChild.tagName.replace(new RegExp('.+:','g'), '')) {
				case 'fontname' :
					fontInfoObj.fontName = valEl;
					break;
				case 'size' :
					fontInfoObj.fontSize = valEl;
					break;
				case 'effect' :
					if (fontInfoObj.hasOwnProperty(valEl)) {
						fontInfoObj[valEl] = true;
					}
					break;
			}
		}
		tmpChild = tmpChild.nextSibling;
	}

	return fontInfoObj;
}

/**
 * информация о шрифте
 * 
 * @constructor
 * @class
 */
function FontInfoObj() {
	this.fontName = "Helvetica";
	/**
	 * !xfdl-ный размер, а не размер в пикселях
	 * 
	 * @type Number
	 */
	this.fontSize = 8;
	this.plain = false;
	this.bold = false;
	this.underline = false;
	this.italic = false;
	/**
	 * @private
	 * @type String
	 */
	this._cssStyle = null;
}
/**
 * сгенерировать css стиль из данного объекта
 * 
 * @return {String}
 */
FontInfoObj.prototype.genCssStyle = function() {
	this._cssStyle = "";

	this._cssStyle += "font-size: "
			+ objStateDrawing.convertFontSize[this.fontSize] + "px;";

	this._cssStyle += "font-family: " + this.fontName + ";";
	// TODO сделать определение по plain
	if (this.bold)
		this._cssStyle += "font-weight:bold;";
	if (this.underline)
		this._cssStyle += "text-decoration:underline;";
	if (this.italic)
		this._cssStyle += "font-style:italic;";
	return this._cssStyle;
};
/**
 * получить сгенерированный css-стиль.
 * 
 * @param {Boolean}
 *            reGenerStyle - принудительно перегенировать стиль
 * @return {String} - css-стиль
 * @note НЕ СЛЕДИТ ЗА ИЗМЕНЕНИЕМ СТИЛЯ ПОСЛЕ ПЕРВОЙ ИНИЦИАЛИЗАЦИИ(используйте
 *       reGenerStyle для принудительной перегенерации)
 */
FontInfoObj.prototype.getCssStyle = function(reGenerStyle) {
	if (this._cssStyle === null || reGenerStyle) {
		this.genCssStyle();
	}
	return this._cssStyle;
};
