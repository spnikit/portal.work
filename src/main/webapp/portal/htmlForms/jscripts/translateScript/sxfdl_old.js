/**
 * файл для скриптов xfdl
 * 
 * @include "../coreXFDL.js"
 * @include "../docs.js"
 * 
 */
/**
 * @type XFDLscript
 */
var xfdlWorkScript = new XFDLscript();
/**
 * @param {Node||Element}
 *            contextEl
 * @constructor
 */
function ContextNode(contextEl)
	{
	if (contextEl)
		this.thisEl = contextEl;
	}

/**
 * функция для выполнения скрипта с заданным режимом
 * 
 * @param {String}
 *            script - скрипт для выполнения
 * @param {Number}
 *            mode - режим выполнения(0 - уже оттранслирован, 1 - еще не
 *            оттранслирован). По умолчанию 1
 */
ContextNode.prototype.start = function(script, mode)
	{
	if (!mode)
		{
		mode = 1;
		}

	if (mode == 1)
		{

		}

	};

/**
 * @constructor
 */
function XFDLscript()
	{
	/**
	 * @type {InfoElement || Element || Node}
	 */
	this.context = null;
	this.setValue = setValue;
	this.toggle = toggle;
	this.tolower = tolower;
	this.getXFDLRef = getXFDLRef;
	this.getDay = getDay;
	this.getMonth = getMonth;
	this.getYear = getYear;
	this.getMinute = getMinute;
	this.getHour = getHour;

	};
/**
 * функция для получения Element по его sid-ссылке
 * 
 * @param {String}
 *            xfdlRef
 */
function getXFDLRef(xfdlRef)
	{
	}
function setValue()
	{
	}
function toggle()
	{
	}
function tolower()
	{
	}

function getDay(ms)
	{
	if (ms)
		return new Date(ms).getDay();
	else
		return new Date().getDay();

	}
function getMonth(ms)
	{
	if (ms)
		return new Date(ms).getMonth();
	else
		return new Date().getMonth();

	}

function getYear(ms)
	{
	if (ms)
		return new Date(ms).getYear();
	else
		return new Date().getYear();

	}
function getMinute(ms)
	{
	if (ms)
		return new Date(ms).getMinutes();
	else
		return new Date().getMinutes();
	}
function getHour(ms)
	{
	if (ms)
		return new Date(ms).getHours();
	else
		return new Date().getHours();
	}
/**
 * функция
 * 
 * @param {String}
 *            ref
 */
function parseXFDLref(ref)
	{

	}

/**
 * функция заменяет имя xfdl-функции на имя для выполнения в javascript'е
 * 
 * @param {String}
 *            funcName
 * @return {String}
 */
function replaceXFDLFuncName(funcName)
	{
	funcName = funcName.trim();
	switch (funcName)
		{
		case "set" :
			funcName = xfdlWorkScript.setValue.name;
			break;
		case "toggle" :
			funcName = xfdlWorkScript.toggle.name;
			break;
		case "tolower" :
			funcName = xfdlWorkScript.tolower.name;
			break;
		// case "" :
		// funcName = xfdlWorkScript.setValue.name;
		// break;
		}
	return funcName;
	}
