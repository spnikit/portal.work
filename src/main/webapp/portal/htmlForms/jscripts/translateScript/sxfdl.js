/**
 * @include "../includes.js"
 * @include "../redraw/dataStruct.js"
 */

/*
 * TODO: сделать:
 * 
 * опции:
 * 
 * global.focuseditem, viewer.visible, global.activated
 * 
 * 
 * 
 * функции:
 * 
 * viewer.measureHeight('pixels','')
 * 
 * 
 * forms.SaveLocal('1')
 * 
 */

/**
 * преобразовать аргумент к числу если это возможно иначе вернуть исходную
 * строку
 * 
 * @param {String}
 *          operand
 * @return {String|Number}
 */
UnitScript.prototype.xsParseOperand = function(operand)
	{
	if (operand == '')
		return '';
	
	var regXNumber = new RegExp("^[0-9]*(,[0-9]+)?$");
	
	if (regXNumber.test(operand))
		return 1 * operand;
	else
		return operand;
	
	}

/**
 * получить узел по ссылке
 * 
 * @param {String}
 *          xfdlRef - ссылка на искомый узел
 * @param {Boolean}
 *          mode - следует ли устанавливать слушателя при получении
 *          ссылки(optional), default - true
 * 
 * @this {UnitScript}
 * 
 * @private
 * 
 * @return {?XsNode} - узла типа XsNode или null если такого не имеется
 * 
 * @Note: в отличии от <b>xsGetXfdlRefVal()</b> получает сам узел(типа XsNode)
 *        а не его значение
 * @Note: если скрипт работает в режиме isFirst то при получении узла на него
 *        вешается слушатель(данный скрипт). Отменяется при помощи <b>mode</b>
 * 
 * 
 * @see xsGetXfdlRefVal()
 */
UnitScript.prototype.xsGetXfdlRef = function(xfdlRef, mode)
	{
	var mySelf = this;
	if (typeof mode == "undefined")
		{
		mode = true;
		}
	/**
	 * @type String[]
	 */
	var massDynamicRef = xfdlRef.split("->");
	// console.log(massDynamicRef.lenght);
	
	/**
	 * @type Number
	 */
	var i;
	
	/**
	 * @type ?String
	 */
	var oldRefValue = null;
	
	/**
	 * @type Node|EmulateNode
	 */
	var targetTeg = null;
	
	/**
	 * @type InfoElement
	 */
	var targetInfoEl;
	for (i = 0; i < massDynamicRef.length; i++)
		{
		/**
		 * type String
		 */
		var thisRef = massDynamicRef[i];
		
		if (oldRefValue)
			thisRef = oldRefValue + '.' + thisPath;
		/**
		 * @type Array|String[]
		 */
		var massRef = thisRef.split("\.");
		
		/**
		 * @type String
		 */
		var nameTargetTeg;
		
		if (massRef.length == 1)
			{
			targetInfoEl = this._infoEl;
			}
		
		else
			{
			var refSids = this._infoEl.fullSidRef.replace(new RegExp('-', 'g'), ".");
			
			if (massRef.length == 2)
				{
				refSids = refSids.substring(0, refSids.lastIndexOf(".")) + "." + massRef[0];
				}
			else
				// massRef.length == 3
				{
				// refSids = refSids.substring(0, refSids.lastIndexOf("."));
				// refSids = refSids.substring(0, refSids.lastIndexOf("."));
				refSids = massRef[0] + "." + massRef[1];
				}
			
//			if (xfdlRef == 'F13.value'){
//				console.log('!!!!!!!!!!!!!');
//				console.log(this._infoEl.fullSidRef);
//				console.log('PAGE1-TABLE12_PANE-TABLE12_TABLE-1-ROW_GROUP-F13');
//				console.log(refSids); //PAGE1.F13
//			}
			targetInfoEl = treeInfoEls.getElemInfo(refSids);
			// console.log(refSids);
			// console.log(targetInfoEl);
			}
		// TODO проверить
			
		if (!targetInfoEl) {
			if (xfdlRef.indexOf('_SIGNATURE_')!=-1){
				var params = xfdlRef.split('_');
				var refSids = mySelf._infoEl.parentFullSidRef + "." + params[0];
				targetInfoEl = treeInfoEls.getElemInfo(refSids);
				if (!targetInfoEl) {
					return null;
				}
			} else return null;
		}
		nameTargetTeg = massRef[massRef.length - 1];
		targetTeg = targetInfoEl._getTeg(nameTargetTeg);
		
		// console.log(serial.serializeToString(targetTeg));
		// console.log("mode = "+mode);
		// console.log("this._isFirstStart = "+this._isFirstStart);
		
		if (mode && targetTeg && this._isFirstStart)
			{
			/**
			 * @type Node[]
			 */
			var listNodes = this.listenerNode;
			
			/**
			 * @type Boolean
			 */
			var isNotListening = true;
			
			for (var i = 0; i < listNodes.length; i++)
				{
				// проверяем слушает ли данный скрипт тэг(чтобы не повесить дважды на
				// один тэг)
				if (targetTeg == listNodes[i])
					{
					// console.logObj(targetInfoEl);
					// console.logObj(listNodes[i]);
					isNotListening = false;
					break;
					}
				}
			if (isNotListening)
				{
				// console.log("Скрипт " + this.getTranScript() + " слушает");
				// console.logObj(targetTeg);
				targetTeg.addScriptListener(this);
				this.listenerNode.push(targetTeg);
				}
			}
		if (!targetTeg)
			{
			console.log("пустой тэг " + targetInfoEl.fullSidRef + '-' + nameTargetTeg
			    + " в контексте исполнения " + this._infoEl.fullSidRef + "-"
			    + this._nameProperty);
			
			var e = new Error();
			e.isMyErr = true;
			e.name = "Пустой тэг";
			e.message = "пустой тэг " + targetInfoEl.fullSidRef + '-' + nameTargetTeg
			    + " в контексте исполнения " + this._infoEl.fullSidRef + "-"
			    + this._nameProperty;
			
			targetTeg = nullTeg;
			}
		oldRefValue = ieXmlDom != null ? targetTeg.text : targetTeg.getValue();
		}
	
	return new XsNode(targetTeg);
	};

/**
 * получить имя свойства и его InfoElement по ссылке в виде объекта с
 * именнованными свойствами
 * 
 * @param {String}
 *          xfdlRef - ссылка на искомый узел
 * @private
 * 
 * @return {{nameTargetNode:String,infoEl:InfoElement}} - объект, содержащий
 *         искомый InfoElement и имя устанавливаемой опции
 * 
 * @Note: используется для того чтобы в дальнейшем запустить синхронизацию с
 *        html, т.е. функциями которые меняют значения узлов в xfdl-DOM дереве
 * 
 * @see xsGetXfdlRef()
 * @see synhronizeXfdlToHtml()
 */
UnitScript.prototype.xsScanXfdlRef = function(xfdlRef, type)
	{
	
	/**
	 * @type String[]
	 */
	
	var massRef = xfdlRef.split("\.");
	/**
	 * @type String
	 */
	var nameTargetTeg;
	/**
	 * @type InfoElement
	 */
	var targetInfoEl;
	
	if (massRef.length == 1)
		{
		targetInfoEl = this._infoEl;
		}
	else
		{
		var refSids = this._infoEl.fullSidRef.replace(new RegExp('-', 'g'), ".");
		
		if (massRef.length == 2)
			{
			refSids = refSids.substring(0, refSids.lastIndexOf(".")) + "."
			    + massRef[0];
			}
		else
			// massRef.length == 3
			{
			// refSids = refSids.substring(0, refSids.lastIndexOf("."));
			// refSids = refSids.substring(0, refSids.lastIndexOf("."));
			refSids = massRef[0] + "." + massRef[1];
			}
		targetInfoEl = treeInfoEls.getElemInfo(refSids);
		// console.log(refSids);
		// console.log(targetInfoEl);
		}
	if (!targetInfoEl)
		return null;
	nameTargetTeg = massRef[massRef.length - 1];
	// targetTeg = targetInfoEl.getTeg(nameTargetTeg);
	
	return {
		nameTargetNode	: nameTargetTeg,
		infoEl		     : targetInfoEl
	};
	
	};

/**
 * 
 * @param {XsNode}
 *          xfdlNode
 * @param {String}
 *          start
 * @param {String}
 *          end
 * 
 * @return {Number} 0 или 1
 * 
 * @private
 * 
 */
UnitScript.prototype.xsToggle = function(xfdlNode, start, end)
	{
	
	/**
	 * @type XsNode
	 */
	var rez;
	var watchNode = xfdlNode;// this.xsGetXfdlRef(xfdlNode);
	if (watchNode == null)
		rez = 0;
	else
		{
		if (arguments.length == 3)
			{
			// console.trace();
			// console.logWorkScript("arguments.length == 3");
			// console.logWorkScript("start = " + start);
			// console.logWorkScript("end = " + end);
			
			// console.logWorkScript("watchNode.getPreviousValue() = " +
			// watchNode.getPreviousValue());
			// console.logWorkScript("watchNode.getValue() = " +
			// watchNode.getValue());
			
			if (start != end && watchNode.getPreviousValue() == start
			    && watchNode.getValue() == end)
				rez = 1;
			else
				rez = 0;
			}
		else
			{
			if (watchNode.getPreviousValue() != watchNode.getValue())
				rez = 1;
			else
				rez = 0;
			
			}
		}
	// console.logWorkScript("rez = " + rez);
	return rez;
	};

/**
 * 
 * @param {String}
 *          reference
 * @param {Element|String}
 *          value
 * @param {}
 *          referenceType (optional)
 * @param {}
 *          scheme (optional)
 * @private
 * @return {Number} 1 - в случае успеха, 0 - в случае неудачи
 */
UnitScript.prototype.xsSet = function(reference, value, referenceType, scheme)
	{
	// console.logWorkScript(this.getTranScript());
	// console.logWorkScript(reference);
	// console.logWorkScript("setvalue = " + value);
	var obj = this.xsScanXfdlRef(reference);
	
	if (!obj)
		return 0;
	
	/**
	 * @type InfoElement
	 */
	var infoEl = obj.infoEl;
	
	/**
	 * @type String
	 */
	var nameProperty = obj.nameTargetNode;
	
	if (value)
		value = value.toString();
	if (infoEl.xformNode)
		{
		if (nameProperty == "value" || nameProperty == "xfdl:value")
			infoEl.setProperty('value', value, true);
		else
			{
			var e = new Error();
			e.name = "ошибка скрипта";
			e.message = "Попытка изменить защищенное свойство " + reference
			    + " через функцию set()";
			this._isCorrect = false;
			throw e;
			}
		}
	else
		{
		infoEl.setProperty(nameProperty, value, true);
		}
	
	return 1;
	};
/**
 * возвращает позицию начаная с которой есть вхождение строки str2 в str1 или -1
 * если вхождений нет
 * 
 * @param {String}
 *          str1
 * @param {String}
 *          str2
 * @return {Number}
 * @private
 * @note аналог indexOf в javascript
 */
UnitScript.prototype.xsStrstr = function(str1, str2)
	{
	// console.log(str2);
	// console.log("str1 = "+(""+str1)+"\nstr2 = "+(""+str2));
	// console.log(("" + str1).indexOf(("" + str2)));
	return ("" + str1).indexOf(("" + str2));
	};

/**
 * возвращает подстроку из строки <b>str</b> начиная с позиции <b>start</b> до
 * позиции <b>end</b> включая символ на позиции <b>end</b>
 * 
 * @param {String}
 *          str
 * @param {Number}
 *          start
 * @param {Number}
 *          end
 * @return {String}
 */
UnitScript.prototype.xsSubstr = function(str, start, end)
	{
	// console.log("str = " + str + "\nstart = " + start + "\nend = " + end);
	var rez = ("" + str).substring(start, end + 1);
	// console.log("rez = " + rez);
	return rez;
	}
/**
 * получить длинну строки
 * 
 * @param {String}
 *          str
 * @return {Number} - длинна строки
 */
UnitScript.prototype.xsStrlen = function(str)
	{
	// console.log(end);
	return ("" + str).length;
	}

/**
 * 
 * преобразовать к строчному регистру
 * 
 * @param {String|XsNode}
 *          str
 * 
 * @return {String}
 */
UnitScript.prototype.xsToLower = function(str)
	{
	if (str != null)
		return str.toString().toLowerCase();
	else
		return null;
	}

UnitScript.prototype.xsToUpper = function(str)
	{
	if (str != null)
		return str.toString().toUpperCase();
	else
		return null;
	}

/**
 * 
 * @param {String}
 *          units - единицы измерения (<b>chars</b> или <b>pixels</b>)
 * @param {String=}
 *          refItem - ссылка на пункт(по умолчанию текущий)(potional)
 * @param {}
 *          scrollvert
 * 
 * @return {String} высота пункта
 * 
 * TODO сделать уменьшение
 */
UnitScript.prototype.xsViewer_measureHeight = function(units, refItem,
    scrollvert)
	{
	var result = "";
	/**
	 * @type InfoElement
	 */
	var item;
	if (units == "pixels")
		{
		if (refItem)
			{
			
			// TODO найти пункт
			}
		else
			{
			item = this._infoEl;
			}
		// console.log("item.refHTMLElem.scrollHeight = "+
		// item.refHTMLElem.scrollHeight);
		
		dopDivSize.style.width = item.width + "px";
		// console.log("item.width = " + item.width);
		dopDivSize.style.fontSize = item.fontSize + "px;";
		dopDivSize.style.fontName = item.fontName;
		
		// dopDivSize.style.position = "absolute";
		// dopDivSize.style.top = "1600px";
		// dopDivSize.style.left = "50px";
		// dopDivSize.style.width = "300px";
		
		UtilsHTMLEl.setValue(dopDivSize, item.getProperty("value"));
		
		// console.log(item.getProperty("value"));
		
		result = 1 * dopDivSize.scrollHeight;
		// очистка
		// dopDivSize.setValue("");
		UtilsHTMLEl.setValue(dopDivSize, "");
		}
	
	// console.log("height = " + result);
	return 1 * result;
	
	}

/**
 * 
 * @param {}
 *          message
 * @param {}
 *          caption
 * @param {}
 *          messagetype
 */
UnitScript.prototype.xsViewer_messageBox = function(message, caption,
    messagetype)
	{
	if (messagetype == 1)
		{
		return +confirm(message);
		}
	else
		{
		return alert(message);
		}
	}

/**
 * @return {String} - количество милисекунд начиная с 1970 года (по умолчанию до
 *          текущего момента
 */
UnitScript.prototype.xsNow = function()
	{
	/**
	 * @type String
	 */
	var nowms;
	nowms = new Date().getTime();
	return nowms;
	}
/**
 * 
 * @param {!Number}
 *          ms - количество милисекунд начиная с 1970 года (по умолчанию до
 *          текущего момента)
 * @return {String} - дата в формате yyyymmdd
 */
UnitScript.prototype.xsDate = function(ms)
	{
	/**
	 * @type String
	 */
	var date;
	if (ms == undefined)
		{
		date = thisDateAndTime("YYYYMMdd");
		}
	else
		{
		ms = ms * 1;
		date = moment(new Date(ms)).format("YYYYMMdd");
		}
	return date;
	}
/**
 * 
 * @param {Number}
 *          ms
 * @return {String}
 */
UnitScript.prototype.xsSecond = function(ms)
	{
	
	/**
	 * @type String
	 */
	var seconds;
	if (ms == undefined)
		{
		seconds = (new Date()).getSeconds();
		}
	else
		{
		ms = ms * 1;
		seconds = (new Date(ms)).getSeconds();
		}
	if (seconds < 10)
		seconds = '0' + '' + seconds;
	else
		seconds = '' + seconds;
	return seconds;
	}
/**
 * 
 * @param {Number}
 *          ms
 * @return {String}
 */
UnitScript.prototype.xsMinute = function(ms)
	{
	
	/**
	 * @type String
	 */
	var minute;
	if (ms == undefined)
		{
		minute = (new Date()).getMinutes();
		}
	else
		{
		ms = ms * 1;
		minute = (new Date(ms)).getMinutes();
		}
	if (minute < 10)
		minute = '0' + '' + minute;
	else
		minute = '' + minute;
	return minute;
	}
/**
 * 
 * @param {Number}
 *          ms
 * @return {String}
 */
UnitScript.prototype.xsHour = function(ms)
	{
	
	/**
	 * @type String
	 */
	var hour;
	if (ms == undefined)
		{
		hour = (new Date()).getHours();
		}
	else
		{
		ms = ms * 1;
		hour = (new Date(ms)).getHours();
		}
	if (hour < 10)
		hour = '0' + '' + hour;
	else
		hour = '' + hour;
	return hour;
	}

/**
 * 
 * @param {Number}
 *          ms
 * @return {String}
 */
UnitScript.prototype.xsDay = function(ms)
	{
	
	/**
	 * @type String
	 */
	var day;
	if (ms == undefined)
		{
		day = (new Date()).getDate();
		}
	else
		{
		ms = ms * 1;
		day = (new Date(ms)).getDate();
		}
	if (day < 10)
		day = '0' + '' + day;
	else
		day = '' + day;
	return day;
	}

/**
 * 
 * @param {Number}
 *          ms
 * @return {String}
 */
UnitScript.prototype.xsMonth = function(ms)
	{
	
	/**
	 * @type String
	 */
	var month;
	if (ms == undefined)
		{
		month = (new Date()).getMonth();
		}
	else
		{
		ms = ms * 1;
		month = (new Date(ms)).getMonth();
		}
	month += 1;
	if (month < 10)
		month = '0' + '' + month;
	else
		month = '' + month;
	return month;
	}

/**
 * 
 * @param {Number}
 *          ms
 * @return {String}
 */
UnitScript.prototype.xsYear = function(ms)
	{
	
	/**
	 * @type String
	 */
	var year;
	if (ms == undefined)
		{
		year = (new Date()).getFullYear();
		}
	else
		{
		ms = ms * 1;
		year = (new Date(ms)).getFullYear();
		}
	year = '' + year;
	return year;
	}

/**
 * 
 * @param {String}
 *          date
 * @param {String}
 *          time
 * @param {}
 *          reference
 */
UnitScript.prototype.xsDateToSeconds = function(date, time, reference)
	{
	// TODO сделать по нормальному
	/**
	 * @type String
	 */
	var second;
	second = ''
	    + Math.floor(moment('' + date + ':' + time).toDate().getTime() / 1000);
	
	return second;
	
	}
/**
 * 
 * @param {}
 *          param
 * @return {}
 */
UnitScript.prototype.xsForms_SaveLocal = function(param)
	{
	console.info("call UnitScript.xsForms_SaveLocal()");
	
	if (funcAddingSave.length>0){
		eval(funcAddingSave);
	}
	
	// alert(1);
	// console.logCallScript("call UnitScript.xsForms_SaveLocal()");
	
	/*if (window.parent.document.getElementById('viewerInWinId')){
		window.parent.document.getElementById('vieweridWin').nextSibling.style.visibility = 'visible';
	}
	
	var rez = "";
	var contentForm;
	if (window.parent && window.parent.document.applet_adapter
	    && window.parent.document.applet_adapter.saveFormToLocalFile)
		{
		console.logWorkScript("Сохранение через родительский апплет");
		contentForm = replaceNewLine(serial.serializeToString(xfdlForm));
		rez = window.parent.document.applet_adapter
		    .saveFormToLocalFile(contentForm);
		}
	else
		{
		if (window.parent.document.applet_adapter)
			{
			console.logWorkScript("Сохранение через апплет утилит вьювера");
			contentForm = replaceNewLine(serial.serializeToString(xfdlForm));
			
			//console.log('!!!!!');
			//console.log(attachFileMass);
			
			var rez = 1;
			
			if (attachDeleteMass.length>0){
				$.ajax({
					url: 'forms/attach.remove',
					data: {fname: attachDeleteMass, act: '1'},
					type: 'POST',
					async : false,
					success: function (resp, textStatus, jqXHR){
						if (resp=='1'){
							rez = window.parent.document.applet_adapter.saveLocalXFDLForm(contentForm, "");
						}
					},
					error: function (jqXHR, textStatus, errorThrown){}
				});
			} else {
				rez = window.parent.document.applet_adapter.saveLocalXFDLForm(contentForm, "");
			}
			
			if (rez == 0)
				{
				console
				    .info("Сохранение формы прошло успешно по адресу 'C:/aisa/form_temp.xfdl'");
				rez = "";
				}
			else
				{
				if (window.parent.document.getElementById('viewerInWinId')){
					window.parent.document.getElementById('vieweridWin').nextSibling.style.visibility = 'hidden';
				}
				console
				    .warn("Сохранение формы прошло по адресу 'C:/aisa/form_temp.xfdl' не удалось");
				}
			}
		else
			console.warnWorkScript("Невозможно сохранить форму");
		}
	return rez;*/
	
	}


UnitScript.prototype.xsForms_AttachDialog = function(param)
		{
//			if (document.getElementById('attachWinId')){
//				var win = document.getElementById('attachWinId');
//				win.style.visibility = 'visible';
//			} else {
//				createAttachWin(true);
//			}
			createAttachWin(0);
		}
		

UnitScript.prototype.ZpssFormOpen = function(action, src, path, type){
//	console.log('!!!!!!!');
	//console.log(action);
	//console.log(path);
	//console.log(type);
	var blob = xfdlForm.xpath(src).iterateNext().getValue();
	if (type=='xfdl') {
		if (blob.substring(0, base64GZipHeader.length + 30).indexOf(base64GZipHeader) != -1) {
			if (window.parent && window.parent.document.applet_adapter && window.parent.document.applet_adapter.decodeFormBase64GZip) {
				blob = window.parent.document.applet_adapter.decodeFormBase64GZip(blob.replace(base64GZipHeader,''));
				window.parent.newFormWindow(blob);
			}
		}
	}
	if (type=='html') {
		blob = window.parent.document.applet_adapter.decodeFormBase64GZip(blob);
		myWin = open("", "html", "scrollbars=yes,menubar=yes");
		// открыть объект document для последующей печати 
		myWin.document.open();
		// генерировать новый документ 
		myWin.document.write(blob);
		// закрыть документ - (но не окно!)
		myWin.document.close();
	}
		
}


/**
 * Возвращает ближайшее большее(!) целое или <b>null</b> если параметр не число
 * 
 * @example: -19,6 => -19<br/> &#x9;-19 => -19<br/> &#x9;19,6 => 20<br/>
 *           &#x9;0 => 0<br/>
 * 
 * @param {Number}
 *          num
 * @return {Number|null}
 */
UnitScript.prototype.xsCeiling = function(num)
	{
	var rez = null;
	try
		{
		rez = Math.ceil(num);
		if (num > 0 && num > rez)
			rez += 1;
		}
	catch (e)// если выражение не распознано как число то вернуть ноль
		{
		console
		    .warnWorkScript("Предупреждение:\nВ функция ceiling() в качестве параметра было передано не число '"
		        + num
		        + "'\nконтекст = "
		        + this._infoEl.fullSidRef
		        + "-"
		        + this._contextEl.tagName);
		rez = 0;
		}
	return rez;
	
	}
UnitScript.prototype.xsPrint_nalPrint_nal = function(typeFunc,
    numberPageToScan, afterPageInsert)
	{
	switch (typeFunc)
		{
		case 'scan' :
			var imageContent = document.applet_scan.ScanOnePageBase64();
			insertDataEl(this._contextEl.parentNode.parentNode, this._contextEl.parentNode, 'a342',
			    'image/jpeg', '235.JPG', imageContent, 'base64');
			break;
		default :
			console
			    .warnWorkScript("Для скрипта xsPrint_nalPrint_nal не задано реализация где первый параметр равен  '"
			        + typeFunc + "'");
		}
	}

/**
 * Создает копию указанного(ой) элемента, страницы или опции и помещает его(её)
 * в указанную позицию, присваивая новый sid.
 * 
 * @note: следите за тем чтобы sid создаваемого элемента был уникальным
 * 
 * @param {String}
 *          reference - ссылка на копируемый узел
 * @param {String}
 *          type - тип копируемого узла(<b>page</b>,<b>item</b> или
 *          <b>option</b> )
 * @param {String}
 *          refPositon- ссылка на узел, в который либо после/до которого будет
 *          вставляться дублированный узел
 * @param {String}
 *          typeRefPositon - тип узла, в который либо после/до которого будет
 *          вставляться дублированный узел
 * @param {String}
 *          location - куда будет вставляться новый узел(<b>append_child</b>,
 *          <b>after_sibling</b> или <b>before_sibling</b>)
 * @param {String}
 *          newSid - sid нового элемента
 */
UnitScript.prototype.xsDuplicate = function(reference, type, refPositon,
    typeRefPositon, location, newSid)
	{
	/**
	 * копируемый узел
	 * 
	 * @type Element
	 */
	var copyEl;
	
	/**
	 * новый узел
	 * 
	 * @type Element
	 */
	var newEl;
	
	/**
	 * элемент в который,до которого или после которого будет вставляться новый
	 * 
	 * @type Element
	 */
	var posEl;
	
	while (true)
		{
		if (type == "item")
			{
			/**
			 * @type InfoElement
			 */
			var infoEl;
			// TODO временно
			infoEl = this.xsScanXfdlRef(reference + '.asdas').infoEl;
			
			if (treeInfoEls.getElemInfo(infoEl.parentFullSidRef + '.' + newSid))
				{
				// TODO уточнить поведение
				console.log("Попытка скопировать существующий узел "
				    + infoEl.parentFullSidRef + '.' + newSid)
				return null;
				}
			newEl = copyEl.cloneNode(true);
			newEl.setAttribute('sid', newSid);
			break;
			}
		return null;
		break
		}
	while (true)
		{
		if (typeRefPositon == "item")
			{
			// TODO временно
			posEl = this.xsScanXfdlRef(refPositon + '.asdas').infoEl.refElem;
			break;
			}
		if (typeRefPositon == "page")
			{
			posEl = xfdlForm.xpath("/xfdl:XFDL/xfdl:page[sid='" + refPositon + "'")
			    .iterateNext();
			break;
			}
		return null;
		break
		}
	if (!posEl)
		return null;
	switch (location)
		{
		// TODO 'красивая вставка'
		case 'append_child' :
			posEl.appendChild(newEl);
			break
		case 'after_sibling' :
			if (posEl.nextSibling)
				posEl.parentNode.insertBefore(newEl, posEl.nextSibling);
			else
				posEl.parentNode.appendChild(newEl, posEl.nextSibling);
			break
		case 'before_sibling' :
			posEl.parentNode.insertBefore(newEl, posEl);
			break
		default :
			return null;
			break
		}
	// TODO отрисовка дублированного элемента
	}
