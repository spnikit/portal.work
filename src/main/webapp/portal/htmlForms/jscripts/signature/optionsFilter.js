/**
 * @include "../includes.js"
 */
/**
 * 
 * @param {Element} -
 *          el элемент &lt;signature>
 * 
 * @class
 * @constructor
 */
function OptionsFilter(el)
	{
	/**
	 * ссылка на элемент &lt;signature>
	 * 
	 * @private
	 * @type {Element}
	 */
	this._refElem = el;
	
	// console.info(serial.serializeToString(el));
	/**
	 * ссылка по sid'ам на элемент
	 * 
	 * @type String
	 */
	this.fullSidRef = el.parentNode.getAttribute("sid") + "."
	    + el.getAttribute("sid");
	
	/**
	 * карта&lt;ключ - id модели>&lt;массив ссылок на теги в этой модели>
	 * 
	 * @type Object|null
	 */
	this.mapInstanceRef = null;
	/**
	 * карта<ключ - id модели(пустая строка - дефолтная(первая)
	 * &lt;модель>&lt;xpath-запросы на теги в этой модели>
	 * 
	 * @type Object|null
	 */
	this.mapXpathInstanceRef = null; //
	
	/**
	 * @type Boolean
	 */
	this.filterInstanceRef = false;
	
	/**
	 * @type String[]|null
	 */
	this.massPageRef = null;
	/**
	 * xpath-запрос для разрешенных страниц
	 * 
	 * @type String|null
	 */
	this.xpathPageRef = null;
	
	/**
	 * @type Boolean
	 */
	this.filterPageRef = false;
	
	/**
	 * @type String[]|null
	 */
	this.massNSRef = null;
	/**
	 * @type Boolean
	 */
	this.filterNSRef = false;
	
	/**
	 * @type String[]|null
	 */
	this.massItemRef = null;
	/**
	 * массив xpath-запросов для разрешенных item'ов по sid'ам
	 * 
	 * @type String|null
	 */
	this.massXpathItemRef = null;
	/**
	 * @type Boolean
	 */
	this.filterItemRef = false;
	
	/**
	 * @type String[]|null
	 */
	this.massOptionRef = null;
	/**
	 * массив xpath-запросов для разрешенных опций item'ов по sid'ам
	 * 
	 * @type String[]|null
	 */
	this.massXpathOptionRef = null;
	/**
	 * @type Boolean
	 */
	this.filterOptionRef = false;
	
	/**
	 * @type String[]|null
	 */
	this.massOption = null;
	/**
	 * xpath-запрос для разрешенных опций
	 * 
	 * @type String|null
	 */
	this.xpathOption = null;
	/**
	 * @type Boolean
	 */
	this.filterOption = false;
	
	/**
	 * @type String[]|null
	 */
	this.massItem = null;
	/**
	 * xpath-запрос для разрешенных пунктов field, label и т.д.
	 * 
	 * @type String|null
	 */
	this.xpathItem = null;
	/**
	 * @type Boolean
	 */
	this.filterItem = false;
	
	/**
	 * @type String[]|null
	 */
	this.massGroup = null;
	/**
	 * xpath-запрос для разрешенных cell'ов
	 * 
	 * @type String[]
	 */
	this.massXpathGroup = null;
	/**
	 * @type String[]|null
	 */
	this.massGroupForSid = null;
	/**
	 * @type Boolean
	 */
	this.filterGroup = false;
	
	/**
	 * @type String[]|null
	 */
	this.massDataGroup = null;
	
	/**
	 * xpath-запрос для разрешенных data
	 * 
	 * @type String[]
	 */
	this.massXpathDataGroup = null;
	/**
	 * @type String[]|null
	 */
	this.massDataGroupForSid = null;
	/**
	 * @type Boolean
	 */
	this.filterDataGroup = false;
	
	this._parse();

	if (this.massXpathItemRef !== null && this.xpathItem === null)
		{
		this.filterItem = !this.filterItemRef;
		}
	if (this.massXpathOptionRef !== null && this.xpathOption === null)
		{
		this.filterOption = !this.filterOptionRef;
		}
	
	}

/**
 * @private
 */
OptionsFilter.prototype._parse = function()
	{
	/**
	 * @type Element
	 */
	var tempNode; // рабочая переменная
	/**
	 * @type NodeList
	 */
	var tempNodeList;
	
	for (i = 0; i < this.typeSigner.length; i++)
		{
		tempNodeList = this._refElem.getElementsByTagName(this.typeSigner[i]);// Получение
		// опций
		// фильтрации
		if (tempNodeList.length == 0)
			continue;
		if (tempNodeList.length > 1)
			{
			continue;
			}
		tempNode = tempNodeList.item(0);
		
		switch (i)
			{
			case 0 : // signitemrefs
				this.handleItemRef(tempNode);
				break;
			case 1 : // signinstance
				this.handleInstance(tempNode);
				break;
			case 2 :// signoptions
				this.handleOption(tempNode);
				break;
			case 3 :// signer
				continue;
				// break;
			case 4 :// signformat
				this.handleSignFormat(tempNode);
				break;
			// break;
			case 5 :// signgroups
				this.handleGroup(tempNode);
				break;
			case 6 :// signitems
				this.handleItem(tempNode);
				break;
			case 7 :// signnamespaces
				this.handleNS(tempNode);
				break;
			case 8 :// signoptionrefs
				this.handleOptionRef(tempNode);
				break;
			case 9 :// signpagerefs
				this.handlePageRef(tempNode);
				break;
			case 10 :// signdatagroups
				this.handleDataGroup(tempNode);
				break;
			default :
				break;
			}
		}
	}
/**
 * имена всех возможных фильтров
 * 
 * @note !НЕ ИЗМЕНЯЙ ПОРЯДОК СЛЕДОВАНИЯ ЭЛЕМЕНТОВ В ЭТОМ МАССИВЕ
 * @type String[]
 */
OptionsFilter.prototype.typeSigner = ["signitemrefs", "signinstance",
    "signoptions", "signer", "signformat", "signgroups", "signitems",
    "signnamespaces", "signoptionrefs", "signpagerefs", "signdatagroups"];

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleItemRef = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = ieXmlDom ? el.getElementsByTagName("filter").item(0).text : el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit' ? this.filterItemRef = true : this.filterItemRef = false;;
	
	var nodeOptions = el.getElementsByTagName("itemref");
	var size = nodeOptions.length;
	
	/**
	 * @type String
	 */
	var thisPageSid = this._refElem.parentNode.getAttribute("sid");
	this.massItemRef = new Array(size);
	this.massXpathItemRef = new Array(size);
	var i;
	/**
	 * @type String
	 */
	var sidItem;
	/**
	 * @type String
	 */
	var sidPage;
	for (i = 0; i < size; i++)
		{
		/**
		 * @type String
		 */
		var tmpVal = nodeOptions.item(i).getValue();
		if (tmpVal.indexOf(".") == -1)
			{
			sidItem = tmpVal;
			sidPage = thisPageSid;
			tmpVal = thisPageSid + '.' + tmpVal;
			}
		else
			{
			var arr = tmpVal.split(/\./);
			sidPage = arr[0];
			sidItem = arr[1];
			}
		this.massItemRef[i] = tmpVal;
		this.massXpathItemRef[i] = "(/xfdl:XFDL/xfdl:page|/xfdl:XFDL/xfdl:globalpage)[@sid='"
		    + sidPage + "']/child::*[@sid='" + sidItem + "']";
		}
	
	return;
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleInstance = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit'
	    ? this.filterInstanceRef = true
	    : this.filterInstanceRef = false;;
	
	var xformsModels = el.getElementsByTagName("dataref");
	this.mapInstanceRef = {};
	this.mapXpathInstanceRef = {};
	var i;
	for (i = 0; i < xformsModels.length; i++)
		{
		var idModel = xformsModels.item(i).getElementsByTagName("model").item(0)
		    .getValue();
		idModel = '' + idModel;
		var ref = xformsModels.item(i).getElementsByTagName("ref").item(0)
		    .getValue();
		if (!(idModel in this.mapInstanceRef))
			{
			this.mapInstanceRef[idModel] = new Array();
			this.mapXpathInstanceRef[idModel] = new Array();
			}
		this.mapInstanceRef[idModel].push(ref);
		this.mapXpathInstanceRef[idModel].push(ref);
		}
	return;
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleOption = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit' ? this.filterOption = true : this.filterOption = false;;
	
	var nodeOptions = el.getElementsByTagName("optiontype");
	var size = nodeOptions.length;
	this.massOption = new Array(size);
	
	var i;
	for (i = 0; i < size; i++)
		{
		/**
		 * @type String
		 */
		var text = nodeOptions.item(i).getValue();
		this.massOption[i] = text;
		
		if (text.indexOf(':') == -1)
			text = 'xfdl:' + text;
		
		if (this.xpathOption)
			{
			this.xpathOption += "|(/xfdl:XFDL/xfdl:page|/xfdl:XFDL/xfdl:globalpage)/*/"
			    + text;
			}
		else
			{
			this.xpathOption = "(/xfdl:XFDL/xfdl:page|/xfdl:XFDL/xfdl:globalpage)/*/"
			    + text;
			}
		}
	return;
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleSignFormat = function(el)
	{
	
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleGroup = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit' ? this.filterGroup = true : this.filterGroup = false;;
	
	var nodeOptions = el.getElementsByTagName("groupref");
	var size = nodeOptions.length;
	
	/**
	 * @type String
	 */
	var thisPageSid = this._refElem.parentNode.getAttribute("sid");
	this.massGroup = new Array(size);
	this.massXpathGroup = new Array(size);
	
	/**
	 * @type String
	 */
	var sidItem;
	/**
	 * @type String
	 */
	var sidPage;
	
	var i;
	for (i = 0; i < size; i++)
		{
		/**
		 * @type String
		 */
		var tmpVal = nodeOptions.item(i).getValue();
		if (tmpVal.indexOf(".") == -1)
			{
			sidItem = tmpVal;
			sidPage = thisPageSid;
			tmpVal = thisPageSid + '.' + tmpVal;
			}
		else
			{
			var arr = tmpVal.split(/\./);
			sidPage = arr[0];
			sidItem = arr[1];
			}
		this.massGroup[i] = tmpVal;
		this.massXpathGroup[i] = "/xfdl:XFDL/xfdl:page[@sid='" + sidPage
		    + "']/xfdl:cell[./xfdl:group='" + sidItem + "']";
		}
	return;
	}

/**
 * @param {Element}
 *          el
 * @this {OptionFilter}
 * 
 */
OptionsFilter.prototype.handleItem = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit' ? this.filterItem = true : this.filterItem = false;;
	
	var nodeOptions = el.getElementsByTagName("itemtype");
	var size = nodeOptions.length;
	this.massItem = new Array(size);
	var i;
	for (i = 0; i < size; i++)
		{
		/**
		 * @type String
		 */
		var text = nodeOptions.item(i).getValue();
		this.massItem[i] = text;
		
		if (text.indexOf(':') == -1)
			text = "xfdl:" + text;
		
		if (this.xpathItem)
			{
			this.xpathItem += "|(/xfdl:XFDL/xfdl:page|/xfdl:XFDL/xfdl:globalpage)/"
			    + text;
			}
		else
			{
			this.xpathItem = "(/xfdl:XFDL/xfdl:page|/xfdl:XFDL/xfdl:globalpage)/"
			    + text;
			}
		
		}
	return;
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleNS = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit' ? this.filterNSRef = true : this.filterNSRef = false;;
	
	var nodeOptions = el.getElementsByTagName("uri");
	var size = nodeOptions.length;
	this.massNSRef = new Array(size);
	var i;
	for (i = 0; i < size; i++)
		{
		this.massNSRef[i] = nodeOptions.item(i).getValue;
		}
	return;
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleOptionRef = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit'
	    ? this.filterOptionRef = true
	    : this.filterOptionRef = false;;
	
	var nodeOptions = el.getElementsByTagName("optionref");
	var size = nodeOptions.length;
	
	/**
	 * @type String
	 */
	var thisPageSid = this._refElem.parentNode.getAttribute("sid");
	this.massOptionRef = new Array(size);
	this.massXpathOptionRef = new Array(size);
	var i;
	for (i = 0; i < size; i++)
		{
		/**
		 * @type String
		 */
		var tmpVal = nodeOptions.item(i).getValue();
		/**
		 * @type String
		 */
		var sidPage;
		/**
		 * @type String
		 */
		var sidItem;
		/**
		 * @type String
		 */
		var nameOpt;
		
		var arr = tmpVal.split(/\./);
		if (arr.lenght == 2)
			{
			tmpVal = thisPageSid + '.' + tmpVal;
			
			sidPage = thisPageSid;
			sidItem = arr[0];
			nameOpt = arr[1];
			
			}
		else
			{
			sidPage = arr[0];
			sidItem = arr[1];
			nameOpt = arr[2];
			}
		
		if (nameOpt.indexOf(':') == -1)
			{
			nameOpt = "xfdl:" + nameOpt
			}
		this.massOptionRef[i] = tmpVal;
		this.massXpathOptionRef[i] = "(/xfdl:XFDL/xfdl:page|/xfdl:XFDL/xfdl:globalpage)[@sid='"
		    + sidPage + "']/child::*[@sid='" + sidItem + "']/" + nameOpt;
		
		}
	return;
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handlePageRef = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit' ? this.filterPageRef = true : this.filterPageRef = false;;
	
	var nodeOptions = el.getElementsByTagName("pageref");
	var size = nodeOptions.length;
	this.massPageRef = new Array(size);
	var i;
	for (i = 0; i < size; i++)
		{
		/**
		 * @type String
		 */
		var text = nodeOptions.item(i).getValue();
		this.massPageRef[i] = text;
		if (this.xpathPageRef)
			{
			this.xpathPageRef += "' or @sid='" + text;
			}
		else
			{
			this.xpathPageRef = "(/xfdl:XFDL/xfdl:page|/xfdl:XFDL/xfdl:globalpage)[@sid='"
			    + text;
			}
		this.xpathPageRef += "']";
		}
	
	return;
	}

/**
 * 
 * @param {Element}
 *          el
 */
OptionsFilter.prototype.handleDataGroup = function(el)
	{
	/**
	 * @type String
	 */
	var typeFilter;
	typeFilter = el.getElementsByTagName("filter").item(0).getValue();
	typeFilter == 'omit'
	    ? this.filterDataGroup = true
	    : this.filterDataGroup = false;;
	
	var nodeOptions = el.getElementsByTagName("datagroupref");
	var size = nodeOptions.length;
	
	/**
	 * @type String
	 */
	var thisPageSid = this._refElem.parentNode.getAttribute("sid");
	this.massDataGroup = new Array(size);
	this.massXpathDataGroup = new Array(size);
	
	/**
	 * @type String
	 */
	var sidItem;
	/**
	 * @type String
	 */
	var sidPage;
	
	var i;
	
	for (i = 0; i < size; i++)
		{
		/**
		 * @type String
		 */
		var tmpVal = nodeOptions.item(i).getValue();
		if (tmpVal.indexOf(".") == -1)
			{
			sidItem = tmpVal;
			sidPage = thisPageSid;
			tmpVal = thisPageSid + '.' + tmpVal;
			}
		else
			{
			var arr = tmpVal.split(/\./);
			sidPage = arr[0];
			sidItem = arr[1];
			}
		this.massDataGroup[i] = tmpVal;
		
		this.massXpathGroup[i] = "/xfdl:XFDL/xfdl:page[@sid='" + sidPage
		    + "']/xfdl:data[./xfdl:datagroup/xfdl:datagroupref='" + sidItem + "']";
		
		}
	
	}
