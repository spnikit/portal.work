/**
 * @include "docs.js"
 * 
 * 
 * @include "includes.js"
 */

/**
 * @param {String}
 *          id - id который надо распарсить
 * @return {} объект, параметр "realRef" которого равен sid-ссылке и параметр
 *         "massRepeatRef" которого равен массиву поряковых номер для repeat
 */
function parseId(id)
	{
	
	/**
	 * @type String
	 */
	var sidRef = "";
	
	sidRef = id.replace(new RegExp("-\\d+[\\w]*", 'g'), "");
	// alert("id sidRef = " + sidRef);
	/**
	 * @type String[];
	 */
	var numberRepeatRef = id.match(new RegExp("(?:-)\\d+(?:[\\w]*)", 'g'));
	if (numberRepeatRef)
		{
		for (var index = 0; index < numberRepeatRef.length; index++)
			{
			numberRepeatRef[index] = numberRepeatRef[index].replace(new RegExp("-",
			        'g'), "");
			}
		}
	// alert("id numberRepeatRef = " + numberRepeatRef);
	return {
		realRef		    : sidRef,
		massRepeatRef	: numberRepeatRef
	};
	
	}

if (!String.prototype.trim)
	{
	/**
	 * функция для обрезания невидимых символов с концов строки
	 * 
	 * @this {String}
	 * @return {String} полученная строка
	 */
	String.prototype.trim = function()
		{
		
		var s;
		s = this.replace(/^\s+/, '');
		for (var i = s.length - 1; i >= 0; i--)
			{
			if (/\S/.test(s.charAt(i)))
				{
				s = s.substring(0, i + 1);
				break;
				}
			}
		return s;
		};
	}

/**
 * формирует конечную ссылку из многих ссылок, которые содержат 'insctance('. Те
 * что идут первее имеют больший приоритет
 * 
 * @param {String...}
 *          pathXRef - текущая ссылка (приоритетнее)
 */
function generateRealXRef(pathXRef)

	{
	//console.trace();
	/**
	 * @type String
	 */
	var realRef = arguments[0];
	// console.logDraw("arguments.length = " + arguments.length);
	// console.dir(arguments);
	for (var i = 1; i < arguments.length
	    && realRef.search(/^\s*instance\(/) == -1; i++)
	// for (var i = 1; i < arguments.length &&
	// realRef.indexOf("^\\s*instance\\(","") != -1; i++)
		{
		if ((arguments[i]+"").trim())// если строка не пустая
			realRef = "" + arguments[i] + "/" + realRef;
		}
	
	return realRef;
	}

/**
 * фунцкция транслирует xform-ссылку содержащую 'instance(' в ссылку пригодную
 * для xpath-запроса
 * 
 * @param {String}
 *          ref
 * @return {?String} - сгенерированную ссылку или <b>null</b> если она не
 *         содержит 'instance()'
 */
function translateXformsRef(ref)
	{
	
	/**
	 * @type Number
	 */
	var tmpIndex = ref.indexOf("instance(");
	
	while (tmpIndex < 0)
		{
		return null;
		break;
		}
	
	/**
	 * @type String
	 */
	var instanceId;
	instanceId = ref.substring(tmpIndex + 10);
	instanceId = instanceId.substring(0, instanceId.indexOf(")") - 1);
	
	/**
	 * @type
	 */
	var refXformModels;
	refXformModels = ref.substring(ref.indexOf('/', tmpIndex) + 1);
	
	/**
	 * @type String
	 */
	var realRef;
	
	realRef = "/xfdl:XFDL/xfdl:globalpage/xfdl:global/xfdl:xformsmodels/xforms:model/xforms:instance[@id=\""
	    + instanceId + "\"]/child::*[1]";
	if (refXformModels.length > 0)
		realRef = realRef + "/" + refXformModels;
	return realRef;
	}

/**
 * создать пространство пробелов по три на каждую вложенность элемента внутри
 * xforms:model
 * 
 * @param {Element}
 *          el - элемент для которого создается пространство
 * @return {String}
 */
function createCuteSpace(el)
	{
	var space = "\n";
	
	el = el.parentNode;
	while (!el.namespaceURI
	    || (el.namespaceURI.toLowerCase() != "http://www.w3.org/2002/xforms"))
		{
		space += "   ";
		el = el.parentNode;
		}
	
	return space;
	}

/**
 * создать пространство пробелов по три на каждую вложенность элемента<br/>
 * используется для "красивого" добавление элементов в переданный
 * 
 * 
 * @param {Element}
 *          el - элемент для которого создается пространство
 * @return {String}
 */
function createCuteSpaceXFDL(el)
	{
	var space = "\n";
	
	el = el.parentNode;
	while (el)
		{
		space += "   ";
		el = el.parentNode;
		}
	
	return space;
	}

/**
 * 
 * @param {String}
 *          date - исходное значение
 * @param {String}
 *          fromPattern - исходный шаблон
 * @param {String}
 *          toPattern - конечный шаблон
 * @return {String}
 */
function dateFormatS2S(date, fromPattern, toPattern)
	{
	if (!toPattern)
		toPattern = "yyyy-MM-dd";
	return moment(date, fromPattern).format(toPattern);
	}

/**
 * @param {String}
 *          toPattern
 * @return {String}
 */
function thisDateAndTime(toPattern)
	{
	if (!toPattern)
		toPattern = "yyyy-MM-dd";
	return moment(new Date()).format(toPattern);
	}

/**
 * 
 * @param {String}
 *          time
 * @param {String}
 *          fromPattern
 * @param {String}
 *          toPattern
 * @return {String}
 */
function timeFormatS2S(time, fromPattern, toPattern)
	{
	if (!toPattern)
		toPattern = "hh:mm:ss";
	return moment(time, fromPattern).format(toPattern);
	}

/**
 * запустить скрипты которые слушаю данный тэг
 * 
 * @param {Element|EmulateNode}
 *          teg
 */
function workScriptForTeg(teg)
	{
	/**
	 * @type {UnitScript[]}
	 */
	var scriptListeners = teg.getScriptListeners();
	if (scriptListeners)
		{
		for (var i = 0; i < scriptListeners.length; i++)
			{
			scriptListeners[i].work();
			}
		}
	
	}
