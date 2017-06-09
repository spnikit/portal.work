





/**
 * объект, хранящий информацию о всех подписях в виде их ассоативного массива<br/>
 * по полной ссылке sid'ов
 * 
 * @constructor
 * @class
 */
function TreeSignatures()
	{
	/**
	 * количество элементов
	 * 
	 * @type Number
	 */
	this.numberElems = 0;
	
	/**
	 * 
	 * @type
	 * @private
	 */
	this._massEls = {};
	
	};
TreeSignatures.prototype = {
	/**
	 * добавить информацию об подписи в структуру
	 * 
	 * @param {OptionsFilter}
	 *          info - объект хранящий информацию об элементе(item'е)
	 * @param {String}
	 *          fullSidRef - полная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются
	 *          оба варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
	 */
	addElemInfo	: function(info)
		{
		
		var fullSidRef = info.fullSidRef;

		/**
		 * @type {String}
		 */
		var realRef = fullSidRef.replace(new RegExp("-", 'g'), ".");
		if (!this._massEls[realRef])
			{
			this.numberElems++;
			}
		this._massEls[realRef] = info;
		
		},
	/**
	 * удалить информацию об элементе из структуры
	 * 
	 * @param {String}
	 *          fullSidRef - полная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются
	 *          оба варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
	 */
	delElemInfo	: function(fullSidRef)
		{
		/**
		 * @type {String}
		 */
		var realRef = fullSidRef.replace(new RegExp("-", 'g'), ".");
		if (this._massEls[realRef])
			{
			this.numberElems--;
			this._massEls[realRef] = null;
			}
		},
	
	/**
	 * получить информацию об элементе из структуры
	 * 
	 * @param {String}
	 *          fullSidRef - полная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются
	 *          оба варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
	 * 
	 * @return {OptionsFilter|null} информацию об объекте или <b>null</b> если
	 *         таковой отсутсвует
	 */
	getElemInfo	: function(fullSidRef)
		{
		/**
		 * @type {String}
		 */
		var realRef = fullSidRef.replace(new RegExp("-", 'g'), ".");
		if (this._massEls[realRef])
			{
			return this._massEls[realRef];
			}
		return null;
		}
	
};

TreeSignatures.prototype.constructor = TreeSignatures;