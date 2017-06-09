/**
 * @include "../includes.js"
 */

/**
 * @private
 */
OptionsFilter.prototype._addSignerKey = function()
	{
	
	if (this.xpathPageRef)
		{
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		
		tmpXRez = xfdlForm.xpath(this.xpathPageRef);
		tmpNode = tmpXRez.iterateNext();
		while (tmpNode)
			{
			tmpNode.setSignFilterKey('pagerefs', 1);
			
			tmpNode = tmpXRez.iterateNext();
			}
		}
	if (this.xpathItem)
		{
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		
		tmpXRez = xfdlForm.xpath(this.xpathItem);
		tmpNode = tmpXRez.iterateNext();
		var tmpParentNode = null;
		while (tmpNode)
			{
			tmpNode.setSignFilterKey('items', 1);
			
			tmpParentNode = tmpNode.parentNode;
			
			while (tmpParentNode)
				{
				tmpParentNode.setSignFilterKey('items', 2);
				tmpParentNode = tmpParentNode.parentNode;
				
				}
			
			tmpNode = tmpXRez.iterateNext();
			}
		}
	
	if (this.xpathOption)
		{
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		
		tmpXRez = xfdlForm.xpath(this.xpathOption);
		tmpNode = tmpXRez.iterateNext();
		var tmpParentNode = null;
		while (tmpNode)
			{
			tmpNode.setSignFilterKey('options', 1);
			
			tmpParentNode = tmpNode.parentNode;
			
			while (tmpParentNode)
				{
				tmpParentNode.setSignFilterKey('options', 2);
				tmpParentNode = tmpParentNode.parentNode;
				
				}
			
			tmpNode = tmpXRez.iterateNext();
			}
		
		}
	
	if (this.massXpathItemRef)
		{
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		for (var i = 0; i < this.massXpathItemRef.length; i++)
			{
			tmpXRez = xfdlForm.xpath(this.massXpathItemRef[i]);
			tmpNode = tmpXRez.iterateNext();
			var tmpParentNode = null;
			while (tmpNode)
				{
				tmpNode.setSignFilterKey('itemrefs', 1);
				
				tmpParentNode = tmpNode.parentNode || tmpNode.ownerElement;
				
				while (tmpParentNode)
					{
					tmpParentNode.setSignFilterKey('itemrefs', 2);
					tmpParentNode = tmpParentNode.parentNode;
					
					}
				
				tmpNode = tmpXRez.iterateNext();
				}
			}
		}
	
	if (this.massXpathOptionRef)
		{
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		for (var i = 0; i < this.massXpathOptionRef.length; i++)
			{
			tmpXRez = xfdlForm.xpath(this.massXpathOptionRef[i]);
			tmpNode = tmpXRez.iterateNext();
			var tmpParentNode = null;
			while (tmpNode)
				{
				tmpNode.setSignFilterKey('optionrefs', 1);
				
				tmpParentNode = tmpNode.parentNode;
				
				while (tmpParentNode)
					{
					tmpParentNode.setSignFilterKey('optionrefs', 2);
					tmpParentNode = tmpParentNode.parentNode;
					}
				tmpNode = tmpXRez.iterateNext();
				}
			}
		}
	
	if (this.massXpathGroup)
		{
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		for (var i = 0; i < this.massXpathGroup.length; i++)
			{
			tmpXRez = xfdlForm.xpath(this.massXpathGroup[i]);
			tmpNode = tmpXRez.iterateNext();
			var tmpParentNode = null;
			while (tmpNode)
				{
				tmpNode.setSignFilterKey('groups', 1);
				
				tmpParentNode = tmpNode.parentNode;
				
				while (tmpParentNode)
					{
					tmpParentNode.setSignFilterKey('groups', 2);
					tmpParentNode = tmpParentNode.parentNode;
					}
				tmpNode = tmpXRez.iterateNext();
				}
			}
		}
	
	if (this.massXpathDataGroup)
		{
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		for (var i = 0; i < this.massXpathDataGroup.length; i++)
			{
			tmpXRez = xfdlForm.xpath(this.massXpathDataGroup[i]);
			tmpNode = tmpXRez.iterateNext();
			var tmpParentNode = null;
			while (tmpNode)
				{
				tmpNode.setSignFilterKey('datagroups', 1);
				
				tmpParentNode = tmpNode.parentNode;
				
				while (tmpParentNode)
					{
					tmpParentNode.setSignFilterKey('datagroups', 2);
					tmpParentNode = tmpParentNode.parentNode;
					}
				tmpNode = tmpXRez.iterateNext();
				}
			}
		}
	
	if (this.mapXpathInstanceRef)
		{
		
		/**
		 * @type XPathResult
		 */
		var tmpXRez;
		/**
		 * @type Element|Node
		 */
		var tmpNode;
		
		for (var pr in this.mapXpathInstanceRef)
			{
			if (this.mapXpathInstanceRef.hasOwnProperty(pr))
				{
				
				/**
				 * @type String[]
				 */
				var arr = this.mapXpathInstanceRef[pr];
				var request;
				if (!('' + pr).trim())// если дефолтная модель
					{
					
					for (var i = 0; i < arr.length; i++)
						{
						request = replaceXPathInstance(
						    arr[i],
						    "/xfdl:XFDL/xfdl:globalpage/xfdl:global/xfdl:xformsmodels/xforms:model[1]/xforms:instance[@id=");
						
						tmpXRez = xfdlForm.xpath(request);
						tmpNode = tmpXRez.iterateNext();
						var tmpParentNode = null;
						while (tmpNode)
							{
							tmpNode.setSignFilterKey('instance', 1);
							
							tmpParentNode = tmpNode.parentNode;
							
							while (tmpParentNode)
								{
								tmpParentNode.setSignFilterKey('instance', 2);
								tmpParentNode = tmpParentNode.parentNode;
								}
							tmpNode = tmpXRez.iterateNext();
							}
						
						}
					
					}
				else
					// если указан id модели
					{
					for (var i = 0; i < arr.length; i++)
						{
						request = replaceXPathInstance(arr[i],
						    "/xfdl:XFDL/xfdl:globalpage/xfdl:global/xfdl:xformsmodels/xforms:model[@id='"
						        + pr + "']/xforms:instance[@id=");
						
						tmpXRez = xfdlForm.xpath(request);
						tmpNode = tmpXRez.iterateNext();
						var tmpParentNode = null;
						while (tmpNode)
							{
							tmpNode.setSignFilterKey('instance', 1);
							
							tmpParentNode = tmpNode.parentNode;
							
							while (tmpParentNode)
								{
								tmpParentNode.setSignFilterKey('instance', 2);
								tmpParentNode = tmpParentNode.parentNode;
								}
							tmpNode = tmpXRez.iterateNext();
							}
						}
					
					}
				}
			}
		
		}
	
	}

OptionsFilter.prototype.setSignerStatusForXFDL = function()
	{
	// TODO не забыть удалить служебные метки
	this._addSignerKey();
	
	/**
	 * @type String
	 */
	var sidThisItem = null;
	
	/**
	 * @type String
	 */
	var sidThisPage = null;
	
	var level;
	
	/**
	 * @type Element|Node
	 */
	var docEl;
	/**
	 * @type Element|Node
	 */
	var tmpEl;
	
	/**
	 * @type Element|Node
	 */
	var childEl;
	/**
	 * @type Element|Node
	 */
	var nextEl;
	/**
	 * @type Element|Node
	 */
	var parentEl;
	
	/**
	 * @type Boolean
	 */
	var printOrNotPrint;
	
	/**
	 * @type String
	 */
	var sidTeg = null;
	
	/**
	 * @type String
	 */
	var sidPage = null;
	
	level = 1;
	
	/**
	 * @type Element
	 */
	docEl = xfdlForm.documentElement;
	/**
	 * @type Element
	 */
	tmpEl = docEl.firstElementChild;
	
	var processingMode = 0;
	/**
	 * @type Boolean[]
	 */
	var stacPrintOrNotPrint = new Array();
	
	stacPrintOrNotPrint.push(true);
	
	/**
	 * 
	 * @type Boolean
	 */
	var flagNS = false;
	
	// флаги для распознавания разрешена ли страница
	/**
	 * разрешена ли текущая страница signNameSpaces'ом
	 * 
	 * @type Boolean
	 */
	var flagPageNS;
	/**
	 * разрешена ли текущая страница signPageRef'ом
	 * 
	 * @type Boolean
	 */
	var flagPage;
	/**
	 * найдена ли текущая страница в signItemRef
	 * 
	 * @type Boolean
	 */
	var hasPageItemRef;
	/**
	 * найдена ли текущая страница в signOptonRef
	 * 
	 * @type Boolean
	 */
	var hasPageOptionRef;
	/**
	 * найдена ли текущая страница в signGroup
	 * 
	 * @type Boolean
	 */
	var hasPageGroup;
	/**
	 * найдена ли текущая страница в signDataGroup
	 * 
	 * @type Boolean
	 */
	var hasPageDataGroup;
	
	// флаги для распознавания разрешен ли пункт
	/**
	 * разрешен ли текущий пункт signNameSpaces'ом
	 * 
	 * @type Boolean
	 */
	var flagItemNS;
	/**
	 * разрешен ли текущий пункт signItem'ом
	 * 
	 * @type Boolean
	 */
	var flagItem;
	/**
	 * найден ли текущий пункт в signItemRef
	 * 
	 * @type Boolean
	 */
	var hasItemRef;
	/**
	 * найден ли текущий пункт в signOptonRef
	 * 
	 * @type Boolean
	 */
	var hasItemOptionRef;
	/**
	 * найден ли текущий пункт в signGroup
	 * 
	 * @type Boolean
	 */
	var hasItemGroup;
	/**
	 * найден ли текущий пункт в signDataGroup
	 * 
	 * @type Boolean
	 */
	var hasItemDataGroup;
	
	/**
	 * разрешен ли текущий пункт signGroup'ом
	 * 
	 * @type Boolean
	 */
	var groupByItem;
	/**
	 * разрешен ли текущий пункт signDataGroup'ом
	 * 
	 * @type Boolean
	 */
	var dataGroupByItem;
	
	// флаги для распознавания разрешена ли опция
	/**
	 * разрешена ли текущая опция signNameSpaces'ом
	 * 
	 * @type Boolean
	 */
	var flagOptionNS;
	/**
	 * разрешена ли текущая опция signOption'ом
	 * 
	 * @type Boolean
	 */
	var flagOption;
	/**
	 * найдена ли текущая опция в signOptionRef
	 * 
	 * @type Boolean
	 */
	var hasOptionRef;
	
	// флаги нахождения внутри пунктов или опций
	/**
	 * флаг того что находимя внутри допущенного к печати <cell>
	 * 
	 * @type Boolean
	 */
	var itemCell;
	/**
	 * флаг того что находимя внутри допущенного к печати <data>
	 * 
	 * @type Boolean
	 */
	var itemData;
	
	/**
	 * @type Boolean
	 */
	
	/**
	 * @type Boolean
	 */
	var keepOrOmit;
	
	/**
	 * последний ли элемент найден в цепочке ссылок по signInstance
	 * 
	 * @type Boolean
	 */
	var flagPoslEl;
	
	/**
	 * включен ли элемент в фильтр signInstance
	 * 
	 * @type Boolean
	 */
	var flagInstance;
	
	/**
	 * флаг того что находимся в первой xforms:model
	 * 
	 * @type Boolean
	 */
	var flagFirstXformModel;
	
	/**
	 * флаг находимся ли мы внутри тэга <xformsmodels>
	 * 
	 * @type Boolean
	 */
	var flagXformsmodels;
	
	/**
	 * равен истине если находимся внутри xformsmodels и этот тэг допущен только
	 * благодаря signInstanceRefs, в противном случае равен ложь<br>
	 * <br>
	 * устанавливается особоый режим обработки символов и комментариев внутри
	 * xformsmodels и после xformsmodels добавляется пустая строка
	 * 
	 * @type Boolean
	 */
	var flagOnlyXformsmodels;
	
	/**
	 * @type Boolean
	 */
	var flagTekNaborModel;
	
	/**
	 * уровень последнего элемента найденного в цепочке ссылок по signInstance
	 * 
	 * @type Number
	 */
	var levelPoslEl;
		
		{// изначальная установка флагов
		flagFirstXformModel = true;
		printOrNotPrint = true;
		flagPageNS = false;
		flagPage = false;
		hasPageItemRef = false;
		hasPageOptionRef = false;
		hasPageGroup = false;
		hasPageDataGroup = false;
		flagItemNS = false;
		flagItem = false;
		hasItemRef = false;
		hasItemOptionRef = false;
		hasItemGroup = false;
		hasItemDataGroup = false;
		
		groupByItem = false;
		dataGroupByItem = false;
		
		flagOptionNS = false;
		flagOption = false;
		hasOptionRef = false;
		
		flagNS = false;
		
		itemCell = false;
		itemData = false;
		flagPoslEl = false;
		flagInstance = false;
		flagXformsmodels = false;
		
		filterNSRef = false;
		filterPageRef = false;
		filterItem = false;
		filterOption = false;
		}
	
	while (tmpEl && tmpEl != docEl)
		{
		
		nextEl = tmpEl.nextElementSibling;
		parentEl = tmpEl.parentNode;
		childEl = tmpEl.firstElementChild;
		
		if (tmpEl._massFilterKey && false)
			{
			////console.info(tmpEl.tagName);
			////console.logObj(tmpEl._massFilterKey);
			}
		
		switch (level)
			// изначальная установка флагов
			{
			case 0 :
				printOrNotPrint = true;
				break;
			case 1 : // если верно то следущий либо <globalpage> либо <page>
				while (true)
					{
					if (tmpEl.tagName.replace(new RegExp('.+:','g'), '') == "globalpage")
						{
						processingMode = 1; // установка режима обработки как <globalpage>
						break;
						}
					if (tmpEl.tagName.replace(new RegExp('.+:','g'), '') == "page")
						{
						processingMode = 2; // установка режима обработки как <page>
						break;
						}
					break;
					
					}
				break;
			case 2 :

				break;
			}
		if (stacPrintOrNotPrint[level - 1])
			{
			
			if (this.massNSRef)// первый проход(разрешен ли данный тэг фильтром
			// signNameSpaces)
				{
				
				var j;
				for (j = 0; j < this.massNSRef.length; j++)
					if (this.massNSRef[j] == tmpEl.namespaceURI)
						break;
				if (j == this.massNSRef.length)
					flagNS = this.filterNSRef;
				else
					flagNS = !this.filterNSRef;
				
				printOrNotPrint = flagNS;
				}
			else
				{
				flagNS = true;// по умолчанию всё разрешено
				switch (level)
					// первый проход(по signPageRef,signItem и signOption)
					{
					case 0 :
						break;
					case 1 : // если <page>
					
						if (this.xpathPageRef)
							{// проверка разрешена ли страница signPafeRef'ом
							if (tmpEl.getSignFilterKey("pagerefs") == 1)
								{
								flagPage = !this.filterPageRef;
								}
							else
								// tmpEl.getSignFilterKey("pagerefs")==0
								{
								flagPage = this.filterPageRef;
								}
							}
						else
							// по умолчанию если не определено
							flagPage = true;
						
						printOrNotPrint = flagPage; // определение флага печати тэга
						break;
					case 2 : // если пункт
					
						if (flagPage) // если текущая страница разрешена signPage'ом
							{
							// проверка разрешен ли пункт signItem'ом
							if (this.massItem == null)
								flagItem = !this.filterItem;
							else
								{
								if (tmpEl.getSignFilterKey('items') == 1)
									flagItem = !this.filterItem;
								else
									flagItem = this.filterItem;
								}
							
							}
						else
							flagItem = false; // если текущая страница запрещена signPage'ом
							
						printOrNotPrint = flagItem; // определение флага печати тэга
						break;
					case 3 : // если опция пункта
					
						if (flagItem
						    || itemCell
						    || itemData
						    || ((this.massItemRef != null) && (hasItemRef
						        ? !this.filterItemRef
						        : this.filterItemRef)))
						// если опция внутри пункта разрешенного signItem'ом или
						// signItemRef'ом или signGroups или signDataGroups
							{
							/*
							 * (*logBuffer)<<"\noption"<<" flagItem = "<<flagItem<<"
							 * itemCell = " << itemCell<<" itemData = " << itemData ;
							 */
							if (!this.xpathOption)
								flagOption = !this.filterOption;
							else
								{
								if (tmpEl.getSignFilterKey('options') == 1)
									flagOption = !this.filterOption;
								else
									flagOption = this.filterOption;
								}
							}
						else
							flagOption = false;
						printOrNotPrint = flagOption; // определение флага печати тэга
						break;
					default : // если внутри опции пункта
						printOrNotPrint = flagOption; // разрешает печать дочерних тэгов
						// опций так разрешен родительский тэг
						break;
					}
				}
			if (printOrNotPrint)
				{
				//console.info("1)тэг <" + tmpEl.tagName + "> подписан");
				}
			switch (level)
				// второй проход (уточняет по signGroups и signDataGroups)
				{
				case 0 :

					break;
				case 1 : // если <page>
					if (this.massXpathGroup != null)
						{
						
						hasPageGroup = tmpEl.getSignFilterKey('groups') ? true : false;
						
						if (hasPageGroup)
							{
							printOrNotPrint = printOrNotPrint
							    || (hasPageGroup ? !this.filterGroup : this.filterGroup); // разрешить
							// если была
							// запрещена
							// первым
							// проходом
							}
						}
					if (this.massXpathDataGroup != null)
						{
						hasPageDataGroup = tmpEl.getSignFilterKey('datagroups')
						    ? true
						    : false;
						if (hasPageDataGroup)
							{
							printOrNotPrint = printOrNotPrint
							    || (hasPageDataGroup
							        ? !this.filterDataGroup
							        : this.filterDataGroup); // разрешить
							// если была запрещена первым проходом
							}
						
						}
					break;
				
				case 2 : // если пункт
					if (this.massXpathGroup != null && tmpEl.tagName.replace(/.+:/g, '') == "cell") // если
					// есть
					// опция
					// signGroups
						{
						
						hasItemGroup = tmpEl.getSignFilterKey('groups') ? true : false; // есть
						// ли
						// данный
						// пункт
						// в
						// singGroup
						if (this.xpathItem == null)
							{
							printOrNotPrint = this.filterGroup;
							}
						if (hasItemGroup)
							{
							if (this.filterGroup) // если singGroup определен как omit то
								// запретить текущий пункт независимо от
								// предыдущих разрешений
								printOrNotPrint = false;
							else
								// если singItemRef определен как keep то разрешить текущий
								// пункт если он был запрещен
								printOrNotPrint = true;
							}
						
						itemCell = printOrNotPrint;
						}
					if (this.massXpathDataGroup != null && tmpEl.localName == "data") // если
					// есть
					// опция
					// signDataGroups
						{
						if (this.xpathItem == null)
							{
							printOrNotPrint = this.filterDataGroup;
							}
						hasItemDataGroup = tmpEl.getSignFilterKey('datagroups')
						    ? true
						    : false;; // есть ли данный пункт в
						// singDataGroup
						if (hasItemDataGroup)
							{
							
							if (this.filterDataGroup)
								printOrNotPrint = false;
							else
								printOrNotPrint = true;
							}
						itemData = printOrNotPrint;
						}
					break;
				
				default :
					break;
				}
			if (printOrNotPrint)
				{
				//console.info("2)тэг <" + tmpEl.tagName + "> подписан");
				}
			switch (level)
				// третий проход, уточняющий(по signItemRef и signOprionRef)
				{
				case 0 :
					break;
				case 1 : // если <page>
				
					if (this.massXpathItemRef != null) // если есть опция signItemRef
						{
						hasPageItemRef = tmpEl.getSignFilterKey("itemrefs") ? true : false;
						// если страница была запрещена первым
						// проходом то разрешить вторым если в ней
						// имеются пункты разрешенные
						// signItemRef'om
						printOrNotPrint = printOrNotPrint
						    || (hasPageItemRef ? this.filterItemRef : this.filterItemRef);
						
						}
					if (this.massXpathOptionRef != null) // если есть опция signOptionRef
						{
						hasPageOptionRef = tmpEl.getSignFilterKey("optionrefs")
						    ? true
						    : false;
						// если страница была запрещена первым проходом то разрешить вторым
						// если в ней имеются пункты разрешенные signOptionRef'om
						printOrNotPrint = printOrNotPrint
						    || (hasPageOptionRef
						        ? !this.filterOptionRef
						        : this.filterOptionRef);
						
						}
					
					break;
				case 2 : // если пункт
				
					if (this.massXpathItemRef != null)
						{
						if (this.xpathItem == null) // сброс фильтра item в противоположный
						// itemRef'у если он не определен
							{
							printOrNotPrint = this.filterItemRef;
							}
						// есть ли данный пункт в singItemRef
						hasItemRef = tmpEl.getSignFilterKey("itemrefs") ? true : false;
						if (hasItemRef)
							{
							if (this.filterItemRef) // если singItemRef определен как omit то
								// запретить текущий пункт независимо от
								// предыдущих разрешений
								printOrNotPrint = false;
							else
								// если singItemRef определен как keep то разрешить текущий
								// пункт если он был запрещен
								printOrNotPrint = true;
							}
						
						}
					if (this.massXpathOptionRef != null) // если есть опция signOptionRef
						{
						// есть ли опция в // signOptionRef
						hasItemOptionRef = tmpEl.getSignFilterKey("optionrefs")
						    ? true
						    : false;
						if (hasItemOptionRef)
							// если опция была запрещена первым проходом то разрешить вторым
							// если в ней имеются пункты разрешенные signOptionRef'om
							printOrNotPrint = printOrNotPrint
							    || (hasItemOptionRef
							        ? !this.filterOptionRef
							        : this.filterOptionRef);
						
						}
					
					break;
				case 3 : // если опция пункта
					if (this.massXpathOptionRef != null && hasItemOptionRef)
						{
						if (this.xpathOption == null) // сброс фильтра option в
						// противоположный
						// optionRef'у если он не определен
							{
							printOrNotPrint = this.filterOptionRef;
							
							}
						// есть ли данная опция в singOptionRef
						hasOptionRef = tmpEl.getSignFilterKey("optionrefs") ? true : false;
						if (hasOptionRef) // если опция найдена в signOptionRef то
						// переопределить флаг печати
							{
							printOrNotPrint = !this.filterOptionRef;
							}
						
						}
					break;
				default : // если внутри опции пункта
				
					if (this.massXpathOptionRef != null && hasOptionRef)
						{
						printOrNotPrint = !this.filterOptionRef;
						
						}
					break;
				}
			if (printOrNotPrint)
				{
				//console.info("3)тэг <" + tmpEl.tagName + "> подписан");
				}
			
			if (processingMode == 1) // четвертый проход для того чтобы оставить тэг
			// <designer:version> и отфильтровать по
			// signInstanceRefs
				{
				switch (level)
					{
					case 0 :
						break;
					case 1 :
						if (tmpEl.localName == "globalpage")
							printOrNotPrint = true;
						break;
					case 2 :
						if (tmpEl.localName == "global")
							printOrNotPrint = true;
						break;
					case 3 :
						if (tmpEl.tagName == "designer:version")
							{
							printOrNotPrint = true;
							}
						break;
					default :
						break;
					}
				
				if (this.mapXpathInstanceRef != null) // пятый проход по
					// signInstanceRefs
					switch (level)
						{
						case 0 :
							break;
						case 1 :
							break;
						case 2 :
							break;
						case 3 :

							if (tmpEl.localName == "xformsmodels")
								{
								printOrNotPrint = printOrNotPrint || !this.filterInstanceRef; // разрешить
								// если
								// было
								// запрещено и фильтр
								// signInstanceRef = keep
								flagXformsmodels = true; // установка флага нахождения внутри
								// <xformsmodels>
								}
							break;
						
						case 4 :

							if (flagXformsmodels)
							// если находимся внутри допущенного к печати <xformsmodels>
								{
								if (tmpEl.tagName == "xforms:model")
									{
									flagTekNaborModel = tmpEl.getSignFilterKey("instance");
									}
								else
									{
									flagTekNaborModel
									}
								
								if (flagTekNaborModel)
								// если модель найдена то в ней применяется фильтр, в противном
								// случае остается всё как было
									{
									
									flagInstance = true;
									printOrNotPrint = printOrNotPrint || !this.filterInstanceRef;
									// разрешить если было запрещено и фильтр signInstanceRef =
									// keep
									}
								if (flagTekNaborModel == 1)
									{
									// System.out.println("\nПоследний элемент");
									levelPoslEl = level;
									flagPoslEl = true;
									}
								}
							break;
						
						default :
							console.log("+===============");
								console.info("tmpEl = "+tmpEl.tagName);
							// (*logBuffer)<<"\nОткрыт тэг "<<XMLString::transcode(fullName);
								console.info("step0-0");
							if (flagXformsmodels)
								{
										console.info("step0-1");
								//console.info("4)тэг <" + tmpEl.tagName + "> " + printOrNotPrint);
								if (flagPoslEl)
									{
										console.info("step0-2");
									printOrNotPrint = stacPrintOrNotPrint[level - 1];
									// System.out.print("\nВнутри последнего");
									break;
									}
							
								if (flagTekNaborModel)
									{
										console.info("step0-3");
										
									// проверка, разрешен ли текущий тэг
									var tmNum = tmpEl.getSignFilterKey("instance");
									
									flagInstance = tmNum ? true : false;
									flagPoslEl = tmNum == 1 ? true : false;
									
								
									console.info("tmNum = "+tmNum);
									console.info("flagInstance = "+flagInstance);
									console.info("flagPoslEl = "+flagPoslEl);
									
									
									//console.log("\nflagInstance = " + flagInstance);
									//console.log("\nflagPoslEl = " + flagPoslEl);
									// (*logBuffer)<<"\nflagPoslEl = "<<flagPoslEl;
									if (flagInstance)
										{
										if (flagPoslEl) // если тэг последний и найден то следущее
										// можно сказать однозначно
											{
											printOrNotPrint = !this.filterInstanceRef;
											// (*logBuffer)<<"\nпоследний";
											// System.out.print("\nПоследний");
											levelPoslEl = level;
											console.info("step1-1");
											}
										else
											{
											// разрешить если было запрещено и фильтр signInstanceRef
											// = keep
											printOrNotPrint = printOrNotPrint
											    || !this.filterInstanceRef;
											// (*logBuffer)<<"\nне последний";
											    console.info("step1-2");
											}
										}
										//todo: временно залепили дыру, надо разобрать как сделать правильно
										else
										{
										printOrNotPrint = this.filterInstanceRef;
										}
									}
								
								// (*logBuffer)<<"\nprintOrNotPrint = "<<printOrNotPrint;
								
								}
							// (*logBuffer)<<"Закрыт тэг
							// "<<XMLString::transcode(fullName)<<"\n";
							break;
						
						}
				
				}
			
			}
		else
			{
			printOrNotPrint = false
			}
		
		if (printOrNotPrint)
			{
			tmpEl.setSignerStatus(true, this.fullSidRef);
			console.info("тэг <" + tmpEl.tagName + "> подписан");
			}
		// if (childEl && printOrNotPrint)
		if (childEl) // как выше делать нельзя т.к. надо удалить все
		// вспомогательные аттрибуты с тэгов
			{
			level++;
			stacPrintOrNotPrint.push(printOrNotPrint);
			tmpEl = childEl;
			continue;
			}
		if (nextEl)
			{
			if (level == levelPoslEl)
				{
				levelPoslEl = 0;
				flagPoslEl = false;
				console.info("clearFlagPostEl");
				}
			if (printOrNotPrint)
				{
				switch (level)
					// сброс флагов
					{
					case 0 :
						break;
					case 1 :// если <page>
						flagPage = false;
						hasPageItemRef = false;
						hasPageOptionRef = false;
						flagSignaturePage = false;
						break;
					case 2 :// если пункт
						flagItem = false;
						hasItemRef = false;
						hasItemOptionRef = false;
						flagItemVoid = false;
						itemCell = false;
						itemData = false;
						break;
					case 3 :// если опция пункта
						flagOption = false;
						hasOptionRef = false;
						hasItemGroup = false;
						hasItemDataGroup = false;
						
						if (tmpEl.localName == "xformsmodels")
							{
							flagXformsmodels = false;
							}
						break;
					case 4 :
						if (tmpEl.tagName == "xforms:model")// закончен перебор одной
						// модели
							{
							flagFirstXformModel = false;
							flagTekNaborModel = 0;
							}
						break;
					default :// если внутри опции пункта
						break;
					}
				
				switch (level)
					// Проверка на выход из <page> или <globalpage>
					{
					case 0 :
						printOrNotPrint = false;
						break;
					case 1 :// если верно то выход либо из <globalpage> либо из <page>
					
						processingMode = 0;
						flagPageVoid = false;
						break;
					case 2 :

						break;
					}
				}
			
			tmpEl.delSignFilterKeys();
			tmpEl = nextEl;
			continue;
			}
		console.info("parentEl "+parentEl);
		while (parentEl && parentEl != docEl)
			{
			console.info("levelPoslEl "+levelPoslEl)
			console.info("level "+level)
			if (level == levelPoslEl)
				{
				levelPoslEl = 0;
				flagPoslEl = false;
				console.info("clearFlagPostEl");
				}
			
			level--;
			stacPrintOrNotPrint.pop();
			
			
			if (printOrNotPrint)
				{
				switch (level)
					// сброс флагов
					{
					case 0 :
						break;
					case 1 :// если <page>
						flagPage = false;
						hasPageItemRef = false;
						hasPageOptionRef = false;
						flagSignaturePage = false;
						break;
					case 2 :// если пункт
						flagItem = false;
						hasItemRef = false;
						hasItemOptionRef = false;
						flagItemVoid = false;
						itemCell = false;
						itemData = false;
						break;
					case 3 :// если опция пункта
						flagOption = false;
						hasOptionRef = false;
						hasItemGroup = false;
						hasItemDataGroup = false;
						
						if (parentEl.localName == "xformsmodels")
							{
							flagXformsmodels = false;
							}
						break;
					case 4 :
						if (parentEl.tagName == "xforms:model") // закончен перебор одной
						// модели
							{
							flagFirstXformModel = false;
							flagTekNaborModel = 0;
							}
						break;
					default :// если внутри опции пункта
						break;
					}
				
				switch (level)
					// Проверка на выход из <page> или <globalpage>
					{
					case 0 :
						printOrNotPrint = false;
						break;
					case 1 :// если верно то выход либо из <globalpage> либо из <page>
					
						processingMode = 0;
						flagPageVoid = false;
						break;
					case 2 :

						break;
					}
				}
			if (tmpEl)
				tmpEl.delSignFilterKeys();
			parentEl.delSignFilterKeys();
			tmpEl = parentEl.nextElementSibling;
			if (tmpEl)
				{
				break;
				}
			parentEl = parentEl.parentNode;
			}
		}
	
	}