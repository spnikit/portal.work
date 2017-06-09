/**
 * @include "../includes.js"
 * @include "../utils.js"
 */

/**
 * функция преобразует действия заключенные в &lt;xforms:trigger> в
 * эквивалентную последовательность javascript-функций для выполнения внутри
 * <b>eval()</b>
 * 
 * @param {Element}
 *            trigger - обрабатываемый тэг &lt;xforms:trigger>
 * @param {InfoElement}
 *            infoEl - ссылка на родительский элемент(button'а или action'а) в
 *            котором находится &lt;xforms:trigger> в виде объекта InfoElement
 * @param {String}
 *            parentXformRef - родительская xform-ссылка или <b>null</b> если
 *            её нет
 * 
 * @return {String} преобразованный скрипт
 */
function parseXTrigger(trigger, infoEl, parentXformRef)
	{
	/**
	 * @type String
	 */
	var refNodeSet = trigger.getAttribute("ref");

	/**
	 * @type String
	 */
	var script = "";
	if (refNodeSet){
		refNodeSet = generateRealXRef(refNodeSet, parentXformRef);
	}else{
		refNodeSet = parentXformRef;
	}

	var actions = trigger.xpath("./xforms:action/xforms:*",
			XPathResult.ORDERED_NODE_ITERATOR_TYPE);

	/**
	 * @type Element
	 */
	var act = actions.iterateNext();

	while (act)
		{
		switch (act.tagName.replace(new RegExp('.+:','g'), ''))
			{
			case "insert" :
				/**
				 * @type String
				 */
				var posit = act.getAttribute("position");
				/**
				 * @type String
				 */
				var xpathRef = act.getAttribute("nodeset");
				xpathRef = generateRealXRef(xpathRef, refNodeSet);
				/**
				 * @type String
				 */
				var at = act.getAttribute("at");

				// TODO
				// !доделать
				if (at.indexOf("index(") != -1)
					{
					/**
					 * @type String
					 */
					var idTable = at.substring(at.indexOf("'") + 1, at
									.lastIndexOf("'"));
					/**
					 * @type Element
					 */
					// var controlTable;
					console.log("id = " + idTable);
					console.log(controlTable);
					at = "last()";
					}

				script += "" + xformsInsert.name + "(\"" + xpathRef + "\",\""
						+ at + "\",\"" + posit + "\");";
				break;
			case "delete" :
				/**
				 * @type String
				 */
				var xpathRef = act.getAttribute("nodeset");
				xpathRef = generateRealXRef(xpathRef, refNodeSet);

				/**
				 * @type String
				 */
				var at = act.getAttribute("at");
	

				script += "" + xformsDelete.name + "(\"" + xpathRef + "\",\""
						+ at + "\");";
				break;
			case "setvalue" :
				/**
				 * @type String
				 */
				var xpathRef = act.getAttribute("ref");
				xpathRef = generateRealXRef(xpathRef, refNodeSet);

				/**
				 * @type {String}
				 */
				var valueXpath = act.getAttribute("value");
				if (valueXpath != null && valueXpath.trim() != "")
					{
					valueXpath = generateRealXRef(valueXpath, xpathRef);
					}
				else
					{
					valueXpath = null;
					}
				/**
				 * @type String
				 */
				var valueString = act.getValue();

				script += "" + xformsSetValue.name + "(\"" + xpathRef + "\",\""
						+ valueXpath + "\",\"" + valueString + "\");";

				break;
			case "setfocus" :
				break;
			}
		act = actions.iterateNext();
		}

	return script;
	}
	
	
	
/**
 * получить номер элемента на котором сфокусирован курсор в таблице, ссылка на
 * которую задана, или "last()" если такого не найдется
 * 
 * @param {String}
 *            at - ссылка типа index('ссылка_на_id_таблицы')
 * @return {String} полученный номер в виде строки начиная с 1 или "last()"
 */
function getFocusElForIndexTable(at)
	{

	/**
	 * @type HTMLElement
	 */
	var focusEl = document.previousFocusEl;
	if (focusEl)
		{
		/**
		 * @type String
		 */
		var idTable = at.substring(at.indexOf("'") + 1, at.lastIndexOf("'"));
		/**
		 * @type Element
		 */
		var controlTable = null;
		xrepMass = xfdlForm.getElementsByTagNameNS(
				"http://www.w3.org/2002/xforms", "repeat");
		for (var i = 0; i < xrepMass.length; i++)
			{
			if (xrepMass.item(i).getAttribute("id") == idTable)
				{
				controlTable = xrepMass.item(i).parentNode;
				break;
				}

			}
		// console.log(controlTable);

		if (controlTable)
			{
			/**
			 * @type {String}
			 */
			var sidRefsTable = gener_sidfullref(controlTable);

			console.log(sidRefsTable);
			/**
			 * @type {String} - полная sid-ссылка на элемент фокуса
			 */
			var refToFocusEl = focusEl.getInfoElRef().fullSidRef;
			console.log("sidRefsTable = " + sidRefsTable);
			console.log("refToFocusEl = " + refToFocusEl);
			console.log("at = " + at);
			at = searchPos(sidRefsTable, refToFocusEl);

			}
		else
			at = "last()";
		}
	else
		at = "last()";
	console.log("at = " + at);
	return "" + at;
	}

/**
 * сгенерировать полную sid-ссылку по родителям не учитывая xfprms:repeat и т.д.<br/>
 * К примеру "PAGE1-TABLE1-PANE3-LABEL4"
 * 
 * @param {Element}
 *            item - пункт для которого формируется ссылка
 * @return {String} - получившаяся ссылка
 */

function gener_sidfullref(item)
	{
	var fullSidRef = "";

	while (item && item.localName.toLowerCase() != "xfdl")
		{
		if (item.namespaceURI.toLowerCase() == "http://www.ibm.com/xmlns/prod/xfdl/7.5")
			{
			if (fullSidRef != "")
				{
				fullSidRef = item.getAttribute("sid") + "-" + fullSidRef;
				}
			else
				{
				fullSidRef = item.getAttribute("sid");
				}
			}
		item = item.parentNode;
		}
	return fullSidRef;
	}
/**
 * 
 * @param {String}
 *            srcRef
 * @param {String}
 *            endRef
 * @return {String}
 * 
 * <br/> Пример работы:<br/> scrRef = "PAGE1-TABLE1-PANE2-TABLE4"<br/> endRef =
 * "PAGE1-TABLE1-5-PANE2-TABLE4-4-TABLE1-8-FIELD1"<br/> результат = 4
 */
function searchPos(srcRef, endRef)
	{
	/**
	 * @type String[]
	 */
	var massScrRef = srcRef.split("-");
	console.log(massScrRef);
	/**
	 * @type String[]
	 */
	var massEndRef = endRef.split("-");
	console.log(massEndRef);
	
	var pos = "last()";
	var j = 0;
	for (var i = 0; i < massScrRef.length; i++)
		{
		while (!isNaN(Number(massEndRef[i + j])) && (i + j) < massEndRef.length)
			j++;
		if (massScrRef[i] != massEndRef[i + j])
			{
			break;
			}
		if (i == massScrRef.length - 1)
			{
			pos = isNaN(Number(massEndRef[i + j + 1]))
					? "last()"
					: massEndRef[i + j + 1] ;
			break;
			}
		}
	console.log("pos = " + pos);
	return pos;
	}
