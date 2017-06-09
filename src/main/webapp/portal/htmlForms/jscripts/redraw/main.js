/**
 * @include "../includes.js"
 * 
 */

/**
 * то же что и redrawXFDL, но очищает все ссылки забитые в DOM-узлы, чтобы не
 * чистить их в ручную
 * 
 * @param {Document}
 *          xfdlDocument
 * 
 * @note Временно
 */
function fullRedrawXFDL(xfdlDocument)
	{
	
	xfdlForm = parser.parseFromString(replaceNewLine(serial
	        .serializeToString(xfdlDocument)), "text/xml");
	xfdlForm.normalize();
	
	redrawXFDL(xfdlDocument);
	}

/**
 * Функция для перерисовывания всего документа
 * 
 * @param {Document}
 *          xfdlDocument
 * @return {HTMLBodyElement}
 */
function redrawXFDL(xfdlDocument) {
	console.group("Логи отрисовки");
	
	clearXFDLViewer();
	
	// TODO сначала надо очистить все зависиммости а потом создавать новые массивы
	// <b>massAllScripts</b> и <b>massDuplexXtoX</b>
	massLabelImage = new Array();
	massAllScripts = new Array();
	massDuplexXtoX = new Array();
	stackForActionsAfterRedraw = new Array();
	
	treeInfoEls = new TreeInfoElements();
	treeInfoPages = new TreeInfoPages();
	treeSignatures = new TreeSignatures();
	
	xFormsScriptMass = {};
	
	objStateDrawing.isDraw = true;
	// перед отрисовкой вешаем на тэги признаки подписи, для того чтобы при
	// отрисовке уже знать что подписано, а что нет

    var signatureTegs;
    var tmpSignature;
    if (ieXmlDom != null) {
        signatureTegs = ieXpathEvaluate(ieXmlDom, "/XFDL/page/signature");
        tmpSignature = signatureTegs[0];
    } else {
        signatureTegs = xfdlForm.xpath("/xfdl:XFDL/xfdl:page/xfdl:signature");
        tmpSignature = signatureTegs.iterateNext();
    }

	var infoSignature;
	while (tmpSignature)
		{
		infoSignature = new OptionsFilter(tmpSignature);
		treeSignatures.addElemInfo(infoSignature);
		infoSignature.setSignerStatusForXFDL();
        if(ieXmlDom != null)
            tmpSignature = signatureTegs[0];
        else
		    tmpSignature = signatureTegs.iterateNext();
		}
	
	/**
	 * элементы &lt;page>
	 * 
	 * @type {Element[]}
	 */
	var pages = new Array();
	
	/**
	 * @type {NodeList}
	 */
	var pagesNodeList;
	// console.log(xfdlDocument.toString());
	// console.log(xfdlDocument.toSource());
	// console.log(xfdlDocument);
	// console.log(serial.serializeToString(xfdlDocument));
    if(ieXmlDom != null) {
        pagesNodeList = ieXpathEvaluate(ieXmlDom, "/XFDL")[0].getElementsByTagName("page");
    } else
	    pagesNodeList = xfdlForm.documentElement.getElementsByTagNameNS(
	    "http://www.ibm.com/xmlns/prod/XFDL/7.5", "page");
	// console.log("pagesNodeList.length = " + pagesNodeList.length);
	/**
	 * пункт &lt;global> в &lt;globalpage>
	 * 
	 * @type Element|null
	 */

	var globalPage;
	// console.log("\n1===========\n");

    if(ieXmlDom != null) {
        globalPage = ieXpathEvaluate(ieXmlDom, "/XFDL/globalpage")[0];
    } else {
	    globalPage = xfdlForm.xpath("/xfdl:XFDL/xfdl:globalpage").iterateNext();
    }
	if(globalPage) {
		console.log("parse globalPage");
		parseGlobalPage(globalPage);
    }

	for (var i = 0; i < pagesNodeList.length; i++) {
		console.log("начата отрисовка страницы номер " + i);
		if (i == currentPage)
			{
				parsePage(pagesNodeList.item(i), i, true);
			}
		else
			{
				parsePage(pagesNodeList.item(i), i);
			}
		pages.push(pagesNodeList.item(i));
		console.log("закончена отрисовка страницы номер " + (i+1));
    }
	
	/**
	 * текущий прорисовываемый элемент
	 * 
	 * @type {Element}
	 */
	// var thisDrawEl;
	console.log("call endRedrawXFDL()");
	endRedrawXFDL();
	
	return null;
};

/**
 * функция вызываемая при завершении перерисовки
 */
function endRedrawXFDL()
	{
	objStateDrawing.thisDrawPage = null;
	objStateDrawing.isDraw = false;
	
	document.thisFocusEl = null;
	document.previousFocusEl = null;
	console.groupEnd();
	/**
	 * @type Function
	 */
	var tmpFunc = stackForActionsAfterRedraw.pop();
	while (tmpFunc)
		{
		tmpFunc.call(window);
		tmpFunc = stackForActionsAfterRedraw.pop();
		}
	
	console.group("Начата работа скриптов после инициализации");
	console.logWorkScript("massAllScripts.length = " + massAllScripts.length);
	for (var i = 0; i < massAllScripts.length; i++)
		{
		
		/**
		 * @type Srting[]|String
		 */
		var testStartScript = massAllScripts[i].getTranScript();
		if (testStartScript)
			testStartScript = testStartScript.match(new RegExp(
			    "xsGetXfdlRef\\('[^']*'\\)", 'g'));
		if (testStartScript)
			{
			testStartScript = testStartScript.join(';');
			// console.log("testStartScript = " + testStartScript);
			with (massAllScripts[i])
				{
				eval(testStartScript);
				}
			// console.log("end\n \n");
			}
		massAllScripts[i]._isFirstStart = false;
		massAllScripts[i].work();
		}
	
	console.groupEnd();
	
	}

/**
 * очистить вьювер TODO сделать очистку объектов у которых нет
 * класса-конструктора(xfdlInfo,objStateDrawing)
 */
function clearXFDLViewer()
	{
	console.logCall("call  clearXFDLViewer()");
	treeInfoEls = null;
	// TODO сначала надо очистить все зависиммости а потом создавать новые массивы
	// <b>massAllScripts</b> и <b>massDuplexXtoX</b>
	massLabelImage = null;
	massAllScripts = null;
	massDuplexXtoX = null;
	mapMandatoryItems.clear();
	objStateDrawing.reset();
	treeXformsModels = null;
	stackForActionsAfterRedraw = null;
	treeInfoPages = null;
	globalInfo = null;
	thisGlobalForPage = null;
	xformsInstance = null;
	
	xFormsScriptMass = null;
	
	// /**
	// * @type {HTMLBodyElement}
	// */
	// var bodyEl = $("html body");
	// bodyEl.empty();
	
	var tmpNode = XFDLViever.firstChild;
	
	// удаление всех div'ов, сделано так чтобы не удалить aplet
	if (jQuery && false)
		{
		$(XFDLViever).empty();
		}
	else
		{
		var tmpNode2;
		while (tmpNode)
			{
			tmpNode2 = tmpNode.nextSibling;
			XFDLViever.removeChild(tmpNode);
			tmpNode = tmpNode2;
			}
		}
	}
/**
 * функция для обработки страницы
 * 
 * @param {Element}
 *          pageElement - ссылка на страницу в DOM-дереве в xfdl'нике
 * @param {Boolean}
 *          isActivePage - является ли текущая страница активной(default -
 *          false)
 * 
 * @return {HTMLElement} - контейнер содержащий страницу
 */
function parsePage(pageElement, pageIndex, isShowPage) {
	var pageInfo = new InfoPage();
	pageInfo.pageIndex = pageIndex;
	pageInfo.pageDiv = document.createElement("div");
	pageInfo.contentDiv = document.createElement('div');
	pageInfo.pageDiv.appendChild(pageInfo.contentDiv);
	
	objStateDrawing.thisDrawPage = pageInfo;
	
	objStateDrawing.stackLayoutFlow.push("inline");
	
	/**
	 * @type {String}
	 */
	var pageSid = pageElement.getAttribute("sid");
	// console.log("1");
	
	// xpath("/html/body").iterateNext();
	// console.log("2");
	var menuButtonHeight = 0;
	if (objStateDrawing.isShowMenu)
		menuButtonHeight = 24;
	
	/**
	 * текущий прорисовываемый элемент
	 * 
	 * @type{Element}
	 */
	var currentEl;
	
	/**
	 * текущий прорисовываемый элемент
	 * 
	 * @type {InfoElement}
	 */
	var thisDrawEl;
	console.log(pageElement)
	currentEl = pageElement.firstChild;
	var mainBodyBounds = new Bounds(null);
	pageInfo.itemFirstInfo = null;
	pageInfo.itemLastInfo = null;
	/**
	 * есть ли видовая проекция у элемента
	 * 
	 * @type {Boolean}
	 */
	var isHasHTMLEl;
	while (currentEl) {
		thisDrawEl = null;
		isHasHTMLEl = true;
		if (currentEl.nodeType == Node.ELEMENT_NODE) {
			console.logDraw("отрисовка элемента " + currentEl.getAttribute("sid"));
			switch (currentEl.tagName.replace(new RegExp('.+:','g'), '')) {
				case "action" :
					thisDrawEl = drawAction(currentEl, pageSid, null);
					isHasHTMLEl = false;
					break;
				case "box" :
					thisDrawEl = drawBox(currentEl, pageSid, null);
					break;
				case "button" :
					thisDrawEl = drawButton(currentEl, pageSid, null);
					break;
				case "data" :
					thisDrawEl = drawData(currentEl, pageSid, null);
					isHasHTMLEl = false;
					break;
				case "cell" :
					break;
				case "check" :
					thisDrawEl = drawCheck(currentEl, pageSid, null);
					break;
				case "combobox" :
					thisDrawEl = drawCombobox(currentEl, pageSid, null);
					break;
				case "field" :
					thisDrawEl = drawField(currentEl, pageSid, null);
					break;
				case "help" :
					break;
				case "label" :
					thisDrawEl = drawLabel(currentEl, pageSid, null);
					break;
				case "line" :
					thisDrawEl = drawLine(currentEl, pageSid, null);
					break;
				case "list" :
					thisDrawEl = drawField(currentEl, pageSid, null);
					break;
				case "pane" :
					thisDrawEl = drawPane(currentEl, pageSid, null);
					break;
				case "popup" :
					thisDrawEl = drawPopup(currentEl, pageSid, null);
					break;
				case "radio" :
					thisDrawEl = drawRadio(currentEl, pageSid, null);
					break;
				case "signature" :
					break;
				case "spacer" :
					thisDrawEl = drawSpacer(currentEl, pageSid, null);
					break;
				case "table" :
					thisDrawEl = drawTable(currentEl, pageSid, null);
					break;
				case "toolbar" :
					thisDrawEl = drawToolbar(currentEl, pageSid, null)
					break;
				case "global" :
					drawGlobal(currentEl, pageSid, isShowPage);
					
					break;
				default :
					break;
            }
			if (thisDrawEl)
				{
				
				if (thisDrawEl.typeEl == 'toolbar')
					{
					pageInfo.toolbarInfo = thisDrawEl;
					pageInfo.toolbarSid = thisDrawEl.sid;
					pageInfo.toolbarDiv = pageInfo.pageDiv
					    .appendChild(thisDrawEl.refHTMLElemDiv);
					}
				else
					{
					if (isHasHTMLEl)
						{
						if (thisDrawEl.bounds.within
						    && thisDrawEl.bounds.within == pageInfo.toolbarSid)
							pageInfo.toolbarDiv.appendChild(thisDrawEl.refHTMLElemDiv)
						else
							{
							pageInfo.contentDiv.appendChild(thisDrawEl.refHTMLElemDiv);
							
							if (mainBodyBounds.x2 < thisDrawEl.x2)
								{
								mainBodyBounds.x2 = thisDrawEl.x2;
								}
							if (mainBodyBounds.y2 < thisDrawEl.y2)
								{
								mainBodyBounds.y2 = thisDrawEl.y2;
								}
							}
						}
					}
				
				if (pageInfo.itemFirstInfo)
					{
					objStateDrawing.itemPreviousInfo.itemNextInfo = thisDrawEl;
					thisDrawEl.itemPreviousInfo = objStateDrawing.itemPreviousInfo
					}
				else
					{
					pageInfo.itemFirstInfo = thisDrawEl;
					}
				objStateDrawing.itemPreviousInfo = thisDrawEl;
				lastDrawingItem = thisDrawEl;
				pageInfo.itemLastInfo = thisDrawEl;
				}
			
			}
		currentEl = currentEl.nextSibling;
    }
	
	pageInfo.itemFirstInfo.itemPreviousInfo = pageInfo.itemLastInfo;
	pageInfo.itemLastInfo.itemNextInfo = pageInfo.itemFirstInfo;
	
	var pageDivBounds = new Bounds(null);
	pageDivBounds.y1 += menuButtonHeight;
	var width = 0;
	var height = 0;

    if(ieXmlDom != null) {
        var childs = pageElement.childNodes;
        var i;
        for(i=0; i<childs.length; i++) {
            if(childs[i].getAttribute("sid") == "vfd_spacer")
                break;
        }
        if(i != childs.length) {
            var x = childs[i].selectNodes("itemlocation/x");
            if(x != null && x[0].text != "") {
                width = parseInt(x[0].text);
            }
            var y = childs[i].selectNodes("itemlocation/y");
            if(y != null && y[0].text != "") {
                height = parseInt(y[0].text);
            }
        }
    } else {
        if (pageElement.xpath("count(child::*[@sid='vfd_spacer']/itemlocation/x) > 0").booleanValue) {
            width = pageElement.xpath("number(child::*[@sid='vfd_spacer']/itemlocation/x)").numberValue;
        }

        if (pageElement.xpath("count(child::*[@sid='vfd_spacer']/itemlocation/y) > 0").booleanValue) {
            height = pageElement.xpath("number(child::*[@sid='vfd_spacer']/itemlocation/y)").numberValue;
        }
    }

	if (width < mainBodyBounds.x2)
		{
		width = mainBodyBounds.x2;
		}
	
	if (height < mainBodyBounds.y2)
		{
		height = mainBodyBounds.y2;
		}
	// ------------------------------------------
	
	// ------------------------------------------
	var bounds = "position:absolute;";
	bounds = bounds + "top:" + pageDivBounds.y1 + "px;";
	bounds = bounds + "left:0px;";
	bounds = bounds + "width:" + width + "px;";
	bounds = bounds + "height:" + height + "px;";
	
	bounds = bounds + "background: #FFFFFF;";
	if (isShowPage == null)
		{
		bounds += "display:none;";
		}
	pageInfo.pageDiv.setAttribute("style", bounds);
	pageInfo.pageDiv.setAttribute("class", "pageStyle");
	
	pageInfo.x1 = 0;
	pageInfo.x2 = width;
	pageInfo.y1 = pageDivBounds.y1;
	pageInfo.y2 = pageDivBounds.y1 + height;
	pageInfo.width = width;
	pageInfo.height = height;
	// ------------------------------------------
	
	// ------------------------------------------
	pageInfo.refElem = pageElement;
	pageInfo.sid = pageSid;
	pageInfo.fullSidRef = pageSid;
	pageInfo.refHTMLElem = pageInfo.pageDiv;
	pageInfo.refHTMLElemDiv = pageInfo.pageDiv;
	pageInfo.typeEl = "page";
	pageInfo.value = null;
	
	var styleContent = "position:absolute;";
	
	pageInfo.contentDiv.setAttribute('style', styleContent);
	// ------------------------------------------
	
	// ------------------------------------------
	UtilsHTMLEl.setInfoElRef(pageInfo.pageDiv, pageInfo);
	// pageInfo.refHTMLElem.setInfoElRef(pageInfo);
	treeInfoPages.addElemInfo(pageInfo);
	// ------------------------------------------
	
	// ------------------------------------------
	XFDLViever.appendChild(pageInfo.pageDiv);
	
	if (pageInfo.toolbarDiv)
		{
		var toolbarDelimiter = document.createElement("div");
		var tdStyle = "position:absolute;";
		// tdStyle = tdStyle + "top:"+ ((tmp1 ? (+tmp1.getValue()) : 0) +
		// menuButtonHeight) + "px;";
		tdStyle = tdStyle + "left:0px;";
		tdStyle = tdStyle
		    + "top:"
		    + (pageInfo.toolbarInfo.height
		        ? pageInfo.toolbarInfo.height - 2
		        : pageInfo.toolbarDiv.offsetHeight - 2) + "px;";
		styleContent += "top:"
		    + (pageInfo.toolbarInfo.height
		        ? pageInfo.toolbarInfo.height
		        : pageInfo.toolbarDiv.offsetHeight) + "px;";;
		pageInfo.contentDiv.setAttribute('style', styleContent);
		
		tdStyle = tdStyle + "width:100%;";
		tdStyle = tdStyle + "height:2px;";
		tdStyle = tdStyle + "z-index: 999;";
		tdStyle = tdStyle + "background: #646464;";
		toolbarDelimiter.setAttribute("style", tdStyle);
		pageInfo.toolbarDiv.appendChild(toolbarDelimiter);
		
		}
	objStateDrawing.stackLayoutFlow.pop();
}

/**
 * функция для перерисовывания элемента
 * 
 * 
 * @param {Element}
 *          xfdlElement
 * @note данную функцию можно использовать только если в DOM добавлен только
 *       один элемент
 */
function redrawXFDLElement(xfdlElement)
	{
	/**
	 * 
	 * @type InfoElement
	 */
	var thisDrawEl;
	var pageEl = xfdlElement.parentNode;
	
	if (pageEl.tagName.replace(new RegExp('.+:','g'), '') != 'page')
		{
		var e = new Error();
		e.isMyErr = true;
		e.message = "Не доступна отрисовка элементов находящихся в "
		    + pageEl.tagName.replace(new RegExp('.+:','g'), '');
		e.name = "ErrorDraw";
		throw e;
		}
	var pageSid = pageEl.getAttribute('sid');
	var thisSid = xfdlElement.getAttribute('sid');
	if (treeInfoEls.getElemInfo(pageSid + "-" + thisSid))
		{
		var e = new Error();
		e.isMyErr = true;
		e.message = "Элемент " + pageSid + "-" + thisSid + " уже имется";
		e.name = "ErrorDraw";
		throw e;
		}
	switch (xfdlElement.tagName.replace(new RegExp('.+:','g'), ''))
		{
		case "data" :
			thisDrawEl = drawData(xfdlElement, pageSid, null);
			stackForActionsAfterRedraw.push(function()
				    {
				    for (var i = 0; i < massLabelImage.length; i++)
					    {
					    massLabelImage[i].updateImage();
					    }
				    
				    })
			break;
		default :
			var e = new Error();
			e.isMyErr = true;
			e.message = "Доступна только отрисовка элементов типа <data>";
			e.name = "ErrorDraw";
			throw e;
			break;
		}
	
	if (thisDrawEl)
		{
		/**
		 * @type Element
		 */
		var nextEl, previosEl;
		var nextSid, previosSid;
		/**
		 * @type InfoElement
		 */
		var nextInfo = null;
		var previosInfo = null;
		
		/**
		 * @type InfoPage
		 */
		var pageInfo = null;
		pageInfo = treeInfoPages.getElemInfo(pageSid);
		nextEl = xfdlElement.nextElementSibling;
		previosEl = xfdlElement.previousElementSibling;
		
		if (nextEl)
			{
			nextSid = nextEl.getAttribute('sid');
			nextInfo = treeInfoEls.getElemInfo(pageSid + "-" + nextSid);
			nextInfo.itemPreviousInfo = thisDrawEl;
			thisDrawEl.itemNextInfo = nextInfo;
			}
		else
			{
			thisDrawEl.itemNextInfo = pageInfo.itemFirstInfo;
			pageInfo.itemFirstInfo.itemPreviousInfo = thisDrawEl;
			}
		
		if (previosEl)
			{
			previosSid = previosEl.getAttribute('sid');
			previosInfo = treeInfoEls.getElemInfo(pageSid + "-" + previosSid);
			previosInfo.itemNextInfo = thisDrawEl;
			thisDrawEl.itemPreviousInfo = previosInfo;
			}
		else
			{
			thisDrawEl.itemPreviousInfo = pageInfo.itemLastInfo;
			pageInfo.itemLastInfo.itemNextInfo = thisDrawEl;
			}
		}
	
	/**
	 * @type Function
	 */
	var tmpFunc = stackForActionsAfterRedraw.pop();
	while (tmpFunc)
		{
		tmpFunc.call(window);
		tmpFunc = stackForActionsAfterRedraw.pop();
		}
	
	return null;
	};

console.log('load redraw/main.js');
