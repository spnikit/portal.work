/**
 * @include "sxfdl.js"
 * @include "../utils.js"
 * @include "../docs.js"
 * @include "../includes.js"
 * @include "../redraw/dataStruct.js"
 */

/**
 * Объекты скриптов
 * 
 * @constructor
 * @param {String}
 *          script - скрипт для выполнения(не оттранслированный для выполнения
 *          скрипт)
 * @param {InfoElement}
 *          infoEl - ссылка на infoElement, в котором назодится скрипт
 * @param {Element|EmulateNode}
 *          contextEl - сам узел типа Element в котором находится аттрибут
 *          xfdl:compute
 * @Note: Все три параметра обязательны
 */
function UnitScript(script, infoEl, contextEl)
	{
	/**
	 * 
	 * скрипт запускатеся первый раз?
	 * 
	 * нужно для того чтобы повесить слушателей на контролируемые узлы
	 * 
	 * @note Фактически сам скрипт не выполняется, а тестово выполняются все
	 *       функции xsGetXfdlRef() чтобы повесить слушателей
	 * @type Boolean
	 * @private
	 */
	this._isFirstStart = true;
	/**
	 * работает ли сейчас данный скрипт(сделано для того, чтобы избежать
	 * циклического выполнения скриптов)
	 * 
	 * @private
	 * @type Boolean
	 */
	this._isWork = false;
	
	/**
	 * скрипт для выполнения(не оттранслированный для выполнения скрипт)
	 * 
	 * @private
	 * @type {String}
	 * 
	 */
	this._script = script;
	/**
	 * ссылка на infoElement, в котором назодится скрипт
	 * 
	 * @private
	 * @type InfoElement
	 */
	this._infoEl = infoEl;
	
	/**
	 * сам узел типа Element в котором находится аттрибут <b>xfdl:compute</b>
	 * 
	 * @private
	 * @type {Element}
	 */
	this._contextEl = contextEl;
	
	/**
	 * имя свойства в котором находится скрипт(пока совпадает с именем тэга),
	 * также включает префикс пространства имен
	 * 
	 * @type String
	 * 
	 * @private
	 * 
	 */
	this._nameProperty = null;
    var name = contextEl.parentNode.tagName.replace(new RegExp('.+:','g'), '');
	if ((name == "itemlocation")
	    && (contextEl.parentNode.namespaceURI == getNameSpaceForXfdlForm("xfdl")))
		{
		this._nameProperty = name + "["
		    + contextEl.tagName.replace(/.+:/g, '') + "]";
		}
	else
		{
		this._nameProperty = contextEl.tagName;
		}
	/**
	 * массив тэгов за которыми следит данный скрипт
	 * 
	 * @type Node[]
	 */
	this.listenerNode = new Array();
	
	/**
	 * корректно ли распознан скрипт
	 * 
	 * @type Boolean
	 * @private
	 */
	this._isCorrect = true;
	
	/**
	 * оттранслированный скрипт
	 * 
	 * @private
	 * @type {String}
	 */
	this._tranScript = null
	try
		{
		// if (this._infoEl.sid == 'BUTTON1_1')
		// console.log(script);
		//		
		// console.log("контекст = " + this._infoEl.fullSidRef + "-"+
		// contextEl.tagName);
		this._tranScript = this._translateXfdlScript(script);
		
		// if (this._infoEl.sid == 'BUTTON1_1')
		// console.log(this._tranScript);
		}
	catch (e)
		{
		if (e.isMyErr)
			{
			this._isCorrect = false;
			console.warnTranslateScript("!Ошибка с контекстом "
			    + this._infoEl.fullSidRef + "-" + this._contextEl.tagName + "("
			    + this._infoEl.refElem + ")" + "\n:" + e.message);
			
			}
		else
			throw e;
		}
	
	};

/**
 * работает ли сейчас данный скрипт(сделано для того, чтобы избежать
 * циклического выполнения скриптов)
 * 
 * @return {Boolean}
 */
UnitScript.prototype.isWork = function()
	{
	return this._isWork;
	};

/**
 * получить контекстный объект-связку(!не узел в котором находится скрипт)
 * 
 * @return {InfoElement} - контекстный объект-связка
 */
UnitScript.prototype.getInfoEl = function()
	{
	
	return this._infoEl;
	};

/**
 * получить узел типа Element в котором находится скрипт
 * 
 * @return {Element} - узел типа Element в котором находится скрипт
 */
UnitScript.prototype.getСontextEl = function()
	{
	return this._contextEl;
	};
/**
 * получить оттранслированный скрипт
 * 
 * @return {String} - оттранслированный скрипт
 */
UnitScript.prototype.getTranScript = function()
	{
	return this._tranScript;
	};

/**
 * запустить скрипт на выполнение<br/> <br/><b>Note</b>:желательно перед
 * выпонением проверять функцией <b>isWork()</b>
 * 
 */
UnitScript.prototype.work = function()
	{
	
	// console.log(this._infoEl.fullSidRef + '-' + this._nameProperty);
	// console.log(this._tranScript);
	
	/**
	 * @type String
	 */
	var oldVal = this._contextEl.getValue();
	
	if (this._isWork || (!this._isCorrect))
		return;
	this._isWork = true;
	var rezult;
	
	// try
	// {
	with (this)
		{
		// console.log("refSids = " + this._infoEl.fullSidRef);
		
		// console.logWorkScript(this._infoEl.fullSidRef);
		// console.logWorkScript(this._script);
		// console.logWorkScript(this.getTranScript());
		rezult = eval(this._tranScript);
		// console.log(this._tranScript);
		
		}
	// console.log(this._contextEl);
	/**
	 * @type String
	 */
	var newVal = this._contextEl.getValue();
	
//	console.log(this);
//	console.log(rezult);
//	console.log(this._nameProperty);
//	console.log(this._infoEl.xformNode);
	
	if (/* && newVal == oldVal && */true || this._infoEl.xformNode != null)
		{
		// console.logWorkScript(this.getTranScript());
		// console.logWorkScript(this._infoEl.fullSidRef + '-' + this._nameProperty
		// + " = " + rezult);
		
		this._infoEl.setProperty(this._nameProperty, rezult, true);
		}
	// }
	//	
	// catch (/* Error */e)
	// {
	// console.warnWorkScript("При работе скрипта" + "(контекст скрипта = "
	// + this._infoEl.fullSidRef + "-" + this._contextEl.tagName + "):\n"
	// + this._script + "\n\nвозникло исключение с именем " + e.name + ":\n"
	// + e.message);
	// if (e.isMyErr)
	// {
	// console.log("Исключение не нативное");
	// }
	// else
	// // передача обработки ислючения дальше
	// {
	// console.log("Исключение нативное");
	// // throw e;
	// }
	// }
	this._isWork = false;
	this._isFirstStart = false;
	
	};

/**
 * текущий режим работы автомата распознавания
 * 
 * @constructor
 * @class
 */
function Mode()
	{
	/**
	 * пустой режим
	 * 
	 * @type Number
	 * @const
	 */
	this.VOID = 0;
	/**
	 * режим возможного комментария
	 * 
	 * @type Number
	 */
	this.POSSIBLY_COMMENT = 2;
	/**
	 * режим простого комментари, т.е. "//"
	 * 
	 * @const
	 * @type Number
	 */
	this.SIMPLE_COMMENT = 3;
	/**
	 * режим длинного комментария, т.е. "/*"
	 * 
	 * @const
	 * @type Number
	 */
	this.LONG_COMMENT = 4;
	
	/**
	 * режим распознания числа(т.е. это либо функция либо XFDL-ссылка)
	 * 
	 * @const
	 * @type Number
	 */
	this.RECOGNITION_NUMBER = 40;
	/**
	 * режим распознания имени(т.е. это либо функция либо XFDL-ссылка)
	 * 
	 * @const
	 * @type Number
	 */
	this.RECOGNITION_NAME = 50;
	/**
	 * режим когда само имя закончено, но не ясно функция ли это или XFDL-ссылка.<br/>Такая
	 * ситуация возможна если после имени стоят невидимые символы(пробел, новая
	 * строка, табуляция)
	 * 
	 * @const
	 * @type Number
	 */
	this.END_NAME = 51;
	
	/**
	 * режим строки в одинарной кавычке
	 * 
	 * @const
	 * @type Number
	 */
	this.STRING_SINGLE_QUOTE = 100;
	
	/**
	 * режим строки в двойной кавычке
	 * 
	 * @const
	 * @type Number
	 */
	this.STRING_DOUBLE_QUOTE = 101;
	
	};
/**
 * @type Mode
 */
var glMode = new Mode();

/**
 * функция для трансляции скрипта во внутреннее представление с заданным режимом
 * 
 * @param {String}
 *          script - скрипт для трансляции TODO
 * @param {Number}
 *          mode - режим трансляции<b>(</b>0-глобальный(т.е. ссылки
 *          дополнительно траслируются что позволит испонить скрипт не зависимо
 *          от контекста),1-локальный<b>)</b>. По умолчанию 1. Режим 0 пока не
 *          работает
 * @return {String} полученный скрипт для выполнения
 * @private
 */
UnitScript.prototype._translateXfdlScript = function(script, mode)
	{
	/**
	 * последний значащий символ(т.е. не пробельный)
	 * 
	 * @type String
	 * 
	 * используется при составных операторах типа '+.','->' и т.д.
	 */
	var lastMeaningSym = "";
	var endScript = "";
	var buf = "";
	var size = script.length;
	var i;
	var r;
	
	/**
	 * type Number;
	 */
	var mode = glMode.VOID;
	
	for (i = 0; i < size; i++)
		{
		r = script.charAt(i);
		if (r >= "a" && r <= "z" || r >= "A" && r <= "Z" || r == "_") // если буква
			{
			if (mode == glMode.VOID || mode == glMode.RECOGNITION_NAME)
				{
				buf += r;
				mode = glMode.RECOGNITION_NAME;
				}
			lastMeaningSym = r;
			continue;
			}
		if (r >= "0" && r <= "9") // если цифра
			{
			if (mode == glMode.RECOGNITION_NAME)
				{
				buf += r;
				}
			lastMeaningSym = r;
			continue;
			}
		if (r == " " || r == "\t" || r == "\n" || r == "\r" || r == "\0")
			{
			
			if (mode == glMode.RECOGNITION_NAME)
				{
				mode = glMode.END_NAME;
				var keyBuf;
				keyBuf = isKeyWord(buf);
				// console.log("keyBuf = " + keyBuf);
				if (keyBuf)
					{
					endScript += keyBuf;
					buf = "";
					mode = glMode.VOID;
					}
				}
			endScript += r;
			continue;
			}
		
		switch (r)
			{
			case "," :
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case '.' :// точка
				if (mode == glMode.VOID)
					{
					if (script.charAt(i - 1) == '+')// составной оператор конкатенции
						{
						endScript += "''+";
						break;
						}
					}
				if (mode == glMode.RECOGNITION_NAME)
					{
					buf += r;
					}
				
				break;
			case '(' :// открывающая скобка
				if (mode == glMode.VOID)
					{
					endScript += "(";
					break;
					}
				if (mode == glMode.RECOGNITION_NAME || glMode.END_NAME)
					{
					endScript += replaceXFDLFuncName(buf);
					// endScript += buf;
					endScript += "(";
					buf = "";
					mode = glMode.VOID;
					}
				break;
			case ')' :// закрывающая скобка
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case '\'' :// одинарная кавычка
				if (mode == glMode.VOID)
					{
					/**
					 * @type Number
					 */
					var newI = i;
					do
						{
						if (script.indexOf('\'', newI + 1) != -1)
							newI = script.indexOf('\'', newI + 1);
						}
					while (script[newI - 1] == '\\');
					// TODO имена функций
					if (lastMeaningSym != '.')
						endScript += "xsParseOperand(";
					endScript += script.substring(i, newI + 1);
					if (lastMeaningSym != '.')
						endScript += ")";
					// console.log("i = " + i + " newI = " + newI);
					i = newI;
					}
				break;
			case '"' ://
				if (mode == glMode.VOID)
					{
					/**
					 * @type Number
					 */
					var newI = i;
					do
						{
						if (script.indexOf('\"', newI + 1) != -1)
							newI = script.indexOf('\"', newI + 1);
						}
					while (script[newI - 1] == '\\');
					endScript += script.substring(i, newI + 1);
					// console.log("i = " + i + " newI = " + newI);
					i = newI;
					}
				break;
			case ':' ://
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case '?' ://
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case '/' ://
				if (mode == glMode.POSSIBLY_COMMENT)
					{
					if (script.indexOf("\n", i) != -1)
						{
						i = script.indexOf("\n", i) - 1;
						}
					else
						// если комментарием заканчивается скрипт
						{
						i = size;
						}
					mode = glMode.VOID;
					break;
					}
				if (mode != glMode.STRING_DOUBLE_QUOTE
				    && mode != glMode.STRING_SINGLE_QUOTE)
					{
					
					mode = glMode.POSSIBLY_COMMENT;
					}
				break;
			case '*' ://
			
				if (mode == glMode.POSSIBLY_COMMENT)
					{
					if (script.indexOf("*/", i + 1) != -1)
						{
						i = script.indexOf("*/", i + 1) + 1;
						}
					mode = glMode.VOID;
					break;
					}
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					// TODO: name function
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case "=" :// знак равно
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					// TODO: name function
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				if (script.charAt(i - 1) == '=')// если составной оператор сравнения то
				// возможна ситуация когда оба операнда
				// ссылки и тогда явно применяем
				// преобразование к строке правого
					{
					endScript += "''+";
					break;
					}
				mode = glMode.VOID;
				break;
			case ">" :// знак равно
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					// TODO: name function
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case "<" :// знак равно
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					// TODO: name function
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case "+" :// плюс
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					// TODO: name function
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case "-" :// минус
				if (script.charAt(i + 1) == '>')
					{
					i++;
					buf += "->";
					}
				else
					{
					if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
						{
						// TODO: name function
						endScript += "xsGetXfdlRef" + "('" + buf + "')";
						buf = "";
						}
					endScript += r;
					mode = glMode.VOID;
					}
				break;
			case "!" :// восклицательный знак
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					// TODO: name function
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case '&' ://
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			
			case '|' ://
				if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
					{
					endScript += "xsGetXfdlRef" + "('" + buf + "')";
					buf = "";
					}
				endScript += r;
				mode = glMode.VOID;
				break;
			case "[" :
				if (mode == glMode.RECOGNITION_NAME)
					{
					buf += r;
					}
				break;
			case "]" :
				if (mode == glMode.RECOGNITION_NAME)
					{
					buf += r;
					}
				break;
			default :
				break;
			}
		lastMeaningSym = r;
		}
	if (mode == glMode.RECOGNITION_NAME || mode == glMode.END_NAME)
		{
		endScript += "xsGetXfdlRef" + "('" + buf + "')";
		buf = "";
		mode = glMode.VOID;
		}
	// console.log("mode = " + mode);
	return endScript;
	};

/**
 * функция для сканирования имени, т.е. проверки его на ключевое слово
 * 
 * @param {String}
 *          str - сканируемая строка
 * @return {?String | Boolean} аналог этого слова из javascript если это
 *         ключевое слово, <b>false</b> в проивном случае
 */
function isKeyWord(str)
	{
	/**
	 * @type Array
	 */
	var keyWords = ["break", "if", "else", "while", "do", "return"];
	str = str.trim().toLowerCase();
	
	switch (str)
		{
		case "and" :
			return '&'
			break;
		case "or" :
			return '|'
			break;
		
		}
	for (var i = 0; i < keyWords.length; i++)
		{
		if (str == keyWords[i])
			return keyWords[i];
		}
	
	return false;
	};

/**
 * функция заменяет имя xfdl-функции на имя для выполнения в javascript'е
 * 
 * @param {String}
 *          funcName
 * @return {String}
 */
function replaceXFDLFuncName(funcName)
	{
	funcName = funcName.trim();
	// TODO именв функций
	switch (funcName)
		{
		case "set" :
			funcName = '' + 'xsSet';
			break;
		case "toggle" :
			funcName = '' + 'xsToggle';
			break;
		case "strstr" :
			funcName = '' + 'xsStrstr';
			break;
		case "substr" :
			funcName = '' + 'xsSubstr';
			break;
		case "strlen" :
			funcName = '' + 'xsStrlen';
			break;
		// TODO доделать
		// case "viewer.measureHeight" :
		// funcName = 'xsViewer_measureHeight';
		// break;
		case "tolower" :
			funcName = "xsToLower";
			break;
		case "toupper" :
			funcName = "xsToUpper";
			break;
		case "date" :
			funcName = "xsDate";
			break;
		case "day" :
			funcName = "xsDay";
			break;
		case "month" :
			funcName = "xsMonth";
			break;
		case "year" :
			funcName = "xsYear";
			break;
		case "hour" :
			funcName = "xsHour";
			break;
		case "minute" :
			funcName = "xsMinute";
			break;
		case "second" :
			funcName = "xsSecond";
			break;
		case "ceiling" :
			funcName = "xsCeiling";
			break;
		
		case "dateToSeconds" :
			funcName = "xsDateToSeconds";
			break;
		// case "viewer.messageBox" :
		// funcName = "xsViewer_messageBox";
		// break;
		
		case "print_nal.print_nal" :
			funcName = "xsPrint_nalPrint_nal";
			break;
		
		case "forms.SaveLocal" :
			if (isGlobalEdit)// отключаем кнопку сохранить
				{
				funcName = "xsForms_SaveLocal";
				break;
				}
			
		case "portprint_nal.portprint_nal" :
			funcName = "xsForms_AttachDialog";
			break;
			
		case "Zpss.FormOpen" :
			funcName = "ZpssFormOpen";
			break;
			
		case "now" :
			funcName = "xsNow";
			break;
			
		default :
			var exeption;
			exeption = new Error();
			exeption.isMyErr = true;
			exeption.message = "Для функции xfdl-скрипта '" + funcName
			    + "()' нет реализации";
			throw exeption;
			break;
		// case "" :
		// funcName = xfdlWorkScript.setValue.name;
		// break;
		}
	return funcName;
	}