/**
 * @include "../includes.js"
 */

/**
 * объект, хранящий информацию о всех элементах в виде их ассоативного массива<br/>
 * по полной ссылке sid'ов(&lt;xforms:repeat> учитывается)
 * 
 * @constructor
 * @class
 */
function TreeInfoElements()
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

/**
 * добавить информацию об элемента в структуру
 * 
 * @param {InfoElement}
 *          info - объект хранящий информацию об элементе(item'е)
 * @param {String}
 *          fullSidRef - полная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются оба
 *          варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
 */
TreeInfoElements.prototype.addElemInfo = function(info)
	{
	
	var fullSidRef = info.fullSidRef;

	/**
	 * @type {String}
	 */
	var realRef = fullSidRef.replace(new RegExp("-", 'g'), ".");
	if (this._massEls[realRef])
		{
		this.numberElems++;
		}
	this._massEls[realRef] = info;
	
	},
/**
 * удалить информацию об элементе из структуры
 * 
 * @param {String}
 *          fullSidRef - полная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются оба
 *          варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
 */
TreeInfoElements.prototype.delElemInfo = function(fullSidRef)
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
	};

/**
 * получить информацию об элементе из структуры
 * 
 * @param {String}
 *          fullSidRef - полная или относительная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются
 *          оба варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
 * @param {InfoElement} -
 *          контекстный узел(не обязательный параметр)
 * @return {InfoElement|null} информацию об объекте или <b>null</b> если
 *         таковой отсутсвует
 */
TreeInfoElements.prototype.getElemInfo = function(fullSidRef, contextEl)
	{
	/**
	 * @type {String}
	 */
	var realRef = fullSidRef.replace(new RegExp("-", 'g'), ".");
	
	if (contextEl)
		{
		/**
		 * @type {Array|String[]}
		 */
		var massRef = realRef.split("\.");
		if (massRef.length == 1)
			{
			var refSids = contextEl.fullSidRef.replace(new RegExp('-', 'g'), ".");
			
			realRef = refSids.substring(0, refSids.lastIndexOf(".")) + "."
			    + massRef[0];
			
			}
		}
	if (this._massEls[realRef])
		{
		return this._massEls[realRef];
		}
	
	return null;
	};

/**
 * дерево для хранения информации о страницах
 * 
 * @class
 * @constructor
 */
function TreeInfoPages()
	{
	/**
	 * количество страниц
	 * 
	 * @type Number
	 */
	this.numberElems = 0;
	
	/**
	 * ссылка на активную страницу
	 * 
	 * @type InfoPage
	 */
	this.activePage = null;
	
	this.massPages = {};
	
	/**
	 * 
	 * @type InfoGlobalPage
	 */
	this.globalPage = null;
	
	/**
	 * получить страницу globalpage
	 * 
	 * @return {InfoGlobalPage|null}
	 */
	this.getGlobalPage = function()
		{
		return this.globalPage;
		}
	
	/**
	 * добавить информацию об элемента в структуру
	 * 
	 * @param {InfoPage|InfoGlobalPage}
	 *          info - объект хранящий информацию об элементе(item'е)
	 */
	this.addElemInfo = function(info)
		{
		
		var fullSidRef = info.fullSidRef || info.sid;

		if (this.massPages[fullSidRef] == null)
			{
			this.numberElems++;
			}
		if (info.typeEl == "globalpage")
			this.globalPage = info;
		
		if (info.isActive)
			activePage = info;
		this.massPages[fullSidRef] = info;
		
		};
	/**
	 * удалить информацию об элементе из структуры
	 * 
	 * @param {String}
	 *          fullSidRef - полная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются
	 *          оба варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
	 */
	this.delElemInfo = function(fullSidRef)
		{
		/**
		 * @type {String}
		 */
		var realRef = fullSidRef;
		if (this.massPages[realRef])
			{
			this.numberElems--;
			this.massPages[realRef] = null;
			}
		};
	
	/**
	 * получить информацию об элементе из структуры
	 * 
	 * @param {String}
	 *          fullSidRef - полная ссылка по sid'ам.<br/>&#x9;&#x9;Допускаются
	 *          оба варианта: <br/>&#x9;&#x9;PAGE1.TABLE1.12.LABEL1<br/>&#x9;&#x9;PAGE1-TABLE1-12-LABEL1
	 * 
	 * @return {InfoPage|InfoGlobalPage|null} информацию о странице
	 */
	this.getElemInfo = function(fullSidRef)
		{
		/**
		 * @type {String}
		 */
		var realRef = fullSidRef;
		if (this.massPages[realRef])
			{
			return this.massPages[realRef];
			}
		return null;
		};
	/**
	 * установить страницу текущей(отображемой) по sid'у(если такой страницы нет
	 * то действие будет отменено и функция вернет <b>false</b>, в случае успеха
	 * <b>true</b>)
	 * 
	 * @param {String}
	 *          sidPage - sid страницы
	 * @return {Boolean} -в случае успеха <b>true</b>, в случае неудачи вернет
	 *         <b>false</b>
	 */
	this.setNewActivePage = function(sidPage)
		{
		
		var newPage = this.getElemInfo(sidPage);
		if (!newPage)
			return false;
		if (this.activePage)
			{
			this.activePage.isActive = false;
			this.activePage.refHTMLElem.style.display = 'none';
			}
		this.activePage = newPage;
		this.activePage.isActive = true;
		this.activePage.refHTMLElem.style.display = 'block';
		return true;
		};
	/**
	 * скрыть текущую страницу
	 * 
	 * @return {Boolean} - <b>true</b> - если имелась отображаемая страница,
	 *         <b>false</b> - если не было
	 */
	this.hideActivePage = function()
		{
		if (this.activePage)
			{
			this.activePage.isActive = false;
			this.activePage.refHTMLElem.style.display = 'none';
			this.activePage = null;
			return true;
			}
		else
			return false;
		};
	/**
	 * получить активную(отображаемую) страницу(или <b>null</b> если таковой нет)
	 * 
	 * @return InfoPage
	 */
	this.getActivePage = function()
		{
		return this.activePage;
		};
	}
