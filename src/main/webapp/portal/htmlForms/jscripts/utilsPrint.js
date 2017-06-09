
/**
 * @type RegExp
 */
var searchTeg = new RegExp(
		"(?:(<([a-zA-Z][-\\w]*:)?(([a-zA-Z][-\\w]*)))\\s+)(([a-zA-Z][-\\w]*:)?([a-zA-Z][-\\w]*)\\s*=\\s*\"[^\"]*(\\r\\n|\\n|\\r|(&gt;))[^\"]*\"\\s*)+/?>",
		"g");

/**
 * @type RegExp
 */
var searchAttr = new RegExp("\"[^\"]*(\\r\\n|\\n|\\r|(&gt;))[^\"]*\"\\s*", "g");

/**
 * @type RegExp
 */
var searchNewStr = new RegExp("(\\r\\n|\\n|\\r)", "g");

/**
 * @type RegExp
 */
var searchGT = new RegExp("&gt;", "g");
/**
 * @param {String}
 *            e
 */
function replTeg(e)
	{
	return e.replace(searchAttr, rerlAttr);
	}

/**
 * @param {String}
 *            e
 */
function rerlAttr(e)
	{
	//console.log("до  = " + e);
	e = e.replace(searchNewStr, "&#xD;&#xA;");
	e = e.replace(searchGT, ">");
	//console.log("после  = " + e);
	return e;

	}

/**
 * функция для замены новых строк в атрибутах на сущности типа &#xD;&#xA;
 *
 * @param {String}
 *            xmlTest - строка содержащая xml-файл
 * @return {String} результатирующая строка
 *
 */
function replaceNewLine(xmlTest)
	{

	/**
	 * @type String
	 */
	var xmlRezult;
	xmlRezult = "";

	/**
	 * регулярное выражение
	 */
	// (?:(<([a-zA-Z][-\w]*:)?(([a-zA-Z][-\w]*)))\s+)(([a-zA-Z][-\w]*:)?([a-zA-Z][-\w]*)\s*=\s*"[^"]*(\r\n|\n|\r)[^"]*"\s*)+/?>
	/**
	 *
	 * @type String
	 */
	// xmlTest = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
	// + " <el:root a:n1=\"sss\nasas\"/>\n<c></c>\n"
	// + " <b:a attr=\"50.\n\\naa\"></br:a>\n\n\n<r></r>\n"
	// + " <!--<a attr=\"50.\n aa\"></a>-->\n" + "<!--<e></e>-->"
	// + "\n <![CDATA[<b ] ]] ]> >]] attr=\"qqqqq\n\n\naa\"></a>]]>\n\n"
	// + "</root>";
	/**
	 * @type Number
	 */
	var xmlLenght = xmlTest.length;
	
	// console.log(xmlTest);

	/**
	 * @type Number
	 */
	var startCdata = 0;

	/**
	 * @type Number
	 */
	var startComment = 0;

	/**
	 * @type Number
	 */
	var endC = 0;

	/**
	 * @type Number
	 */
	var lastEndC = 0;

	/**
	 * @type Number
	 */
	var startC = 0;

	/**
	 * @type String
	 */
	var tempStr = "";

	/**
	 * @type RegExp || Number
	 */
	var rez;

	while (true)
		{
		lastEndC = endC;
		if (startCdata >= 0)
			{
			startCdata = xmlTest.indexOf("<![CDATA[", endC);
			console.log("\nstartCdata =    " + startCdata);
			}
		if (startComment >= 0)
			{
			startComment = xmlTest.indexOf("<!--", endC);
			// console.log("\nstartComment = " + startComment);
			}
		if (startCdata == -1 && startComment == -1)
			break;
		if (startCdata == -1)
			{
			startC = startComment;
			endC = xmlTest.indexOf("-->", startC);
			// console.log("\nendC = " + endC);
			}
		else
			{
			if (startComment == -1)
				{
				startC = startCdata;
				endC = xmlTest.indexOf("]]>", startC);
				// console.log("\nendC = " + endC);
				}
			else
				{
				if (startComment > startCdata)
					{
					startC = startCdata;
					endC = xmlTest.indexOf("]]>", startC);
					// console.log("\nendC = " + endC);
					}
				else
					{
					startC = startComment;
					endC = xmlTest.indexOf("-->", startC);
					// console.log("\nendC = " + endC);
					}
				}
			}

		if (endC == -1)
			{
			console.log("Ошибка. Незавершенная секция CDATA или комментариев");
			endC = startC;
			break;
			}

		endC += 3;
		tempStr = xmlTest.substring(lastEndC, startC);

		rez = tempStr.replace(searchTeg, replTeg);
		xmlRezult = xmlRezult.concat(rez);
		xmlRezult = xmlRezult.concat(xmlTest.substring(startC, endC));
		// rez = tempStr.match(new
		// RegExp("(?:(<([a-zA-Z][-\\w]*:)?(([a-zA-Z][-\\w]*)))\\s+)(([a-zA-Z][-\\w]*:)?([a-zA-Z][-\\w]*)\s*=\s*\"[^\"]*(\\r\\n|\\n|\\r)[^\"]*\"\s*)+/?>","g"));

		// if (rez)
		// {
		// console.log("!!!");
		// console.log(rez);
		// console.log(tempStr);
		// }

		// console.log(tempStr);
		}
	tempStr = xmlTest.substring(endC, xmlLenght);

	rez = tempStr.replace(searchTeg, replTeg);
	if (startC != -1)
		xmlRezult = xmlRezult.concat(rez);
	// console.log(xmlRezult);
	return xmlRezult;
	}
