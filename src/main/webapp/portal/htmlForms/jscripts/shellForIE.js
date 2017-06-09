/**
 * @include "includes.js"
 */

function printStackTrace()
	{
	var callstack = [];
	var isCallstackPopulated = false;
	try
		{
		i.dont.exist += 0; // doesn't exist- that's the point
		}
	catch (e)
		{
		if (e.stack)
			{ // Firefox
			var lines = e.stack.split('\n');
			for (var i = 0, len = lines.length; i < len; i++)
				{
				if (lines[i].match(/^\s*[A-Za-z0-9\-_\$]+\(/))
					{
					callstack.push(lines[i]);
					}
				}
			// Remove call to printStackTrace()
			callstack.shift();
			isCallstackPopulated = true;
			}
		else
			if (window.opera && e.message)
				{ // Opera
				var lines = e.message.split('\n');
				for (var i = 0, len = lines.length; i < len; i++)
					{
					if (lines[i].match(/^\s*[A-Za-z0-9\-_\$]+\(/))
						{
						var entry = lines[i];
						// Append next line also since it has the file info
						if (lines[i + 1])
							{
							entry += ' at ' + lines[i + 1];
							i++;
							}
						callstack.push(entry);
						}
					}
				// Remove call to printStackTrace()
				callstack.shift();
				isCallstackPopulated = true;
				}
		}
	if (!isCallstackPopulated)
		{ // IE and Safari
		var currentFunction = arguments.callee.caller;
		while (currentFunction)
			{
			var fn = currentFunction.toString();
			var fname = fn.substring(fn.indexOf('function') + 8, fn.indexOf(''))
			    || 'anonymous';
			callstack.push(fname);
			currentFunction = currentFunction.caller;
			}
		}
	output(callstack);
	}

function output(arr)
	{
	// Optput however you want
	console.log(arr.join('\n\n'));
	}

if (navigator.userAgent.indexOf("MSIE") != -1)
	{
	
	/**
	 * @type Number
	 */
	function myItem(position)
		{
		return this[position];
		}
	
	if (!Element.prototype.getElementsByTagNameNS)
		{
		
		/**
		 * @this Element
		 */
		Element.prototype.getElementsByTagNameNS = function(ns, tagName)
			{
			/**
			 * @type NodeList
			 */
			var tempList;
			console.log("call getElementsByTagNameNS(" + ns + ", " + tagName + ")");
			tempList = this.getElementsByTagName(tagName);
			var result = new Array();
			result.length = 0;
			for (var i = 0; i < tempList.length; i++)
				{
				if (tempList.item(i).namespaceURI == ns)
					result.push(tempList.item(i));
				}
			result.item = myItem;
			}
		}
	
	if (!Document.prototype.getElementsByTagNameNS)
		{
		
		/**
		 * @this Element
		 */
		Document.prototype.getElementsByTagNameNS = function(ns, tagName)
			{
			console.log("call getElementsByTagNameNS(" + ns + ", " + tagName + ")");
			/**
			 * @type NodeList
			 */
			var tempList;
			tempList = this.getElementsByTagName(tagName);
			var result = new Array();
			result.length = 0;
			for (var i = 0; i < tempList.length; i++)
				{
				if (tempList.item(i).namespaceURI == ns)
					result.push(tempList.item(i));
				}
			result.item = myItem;
			}
		}
	
	if (!XPathException)
		{
		/**
		 * @param codeException -
		 *          код исключения(51 или 52)
		 * @param mes -
		 *          сообщение об ошибке(может отсутствовать)
		 * @constructor
		 * @class
		 */
		function XPathException(codeException, mes)
			{
			var e = new Error("XPathException");
			e.name = "XPathException";
			if (typeof mes == "undefined")
				{
				if (codeException == 52)// TYPE_ERR
					{
					e.message = "If the expression cannot be converted to return the specified type(результат не может быть конвертирован в заданный тип).";
					}
				if (codeException == 51)// INVALID_EXPRESSION_ERR
					{
					e.message = "If the expression has a syntax error or otherwise is not a legal expression according to the rules of the specific XPathEvaluator or contains specialized extension functions or variables not supported by this implementation(выражение содержит синтактическую ошибку или нереализованную функцию или переменную).";
					}
				else
					e.message = "Неизвестная природа исключения";
				}
			else
				e.message = mes;
			// throw e;
			}
		}
	
	
	if (!HTMLElement.prototype.addEventListener)
		{
		//console.log("HTMLElement");
		HTMLElement.prototype.addEventListener = function(action, handler, stage)
			{
			this.attachEvent("on" + action, function()
				    {
				    handler.call(this);
				    });
			}
		}
	}
console.log("load shellForIE.js");