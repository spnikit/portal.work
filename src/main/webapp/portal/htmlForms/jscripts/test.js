/**
 * 
 */
//   (?:(<([a-zA-Z][-\w]*:)?(([a-zA-Z][-\w]*)))\s+)(([a-zA-Z][-\w]*:)?([a-zA-Z][-\w]*)\s*=\s*"[^"]*(\r\n|\n|\r)[^"]*"\s*)+/?>
/**
 * 
 * @type String
 */
var xmlTest = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n	<el:root a:n1=\"sss\nasas\"/>\n\n		<b:a attr=\"50.\\n	aa\"></br:a>\n\n\n<r></r>\n	    <!--<a attr=\"50.\n	  aa\"></a>-->\n        <![CDATA[<b ]  ]] ]> >]]  attr=\"qqqqq\n]>\n\naa\"></a>]]>\n\n</root>";

/**
 * @type Number
 */
var xmlLenght = xmlTest.length;
console.log(xmlTest);
console.log(replaceCodeInAttrValues(xmlTest));




//Происходит проход по введенной строке, простое перекопирвоание в выходную строку.
//Если встречаются <![CDATA[ или <!--, то далее идет простое копирвоание в выходную строку
//до появления ]]> и --> соответственно. Если встречается ", то далее идет простое копирование,
//если встречаются <![CDATA[ или <!--, то далее идет простое копирование в выходную строку
//до появления ]]> и --> соответственно. Если встерчается \n или \r, то вместо них в выходную строку 
//выводятся &#xD и &#xA соответственно.


function replaceCodeInAttrValues(strIn)
{
	var length = strIn.length;
	var strOut = "";
	var index = 0;

	S1();
	return strOut;

	// обход по входной строке
	function S1()
	{
		while(true)
		{
			if(index >= length)
				break;

			if(strIn[index] == '"')
			{
				strOut += strIn[index];
				index++;
				S2();
			}
			else if(strIn[index] == '<' && (index + 9 <= length) && strIn.substring(index, index + 9) == "<![CDATA[")
			{
				strOut += "<![CDATA[";
				index += 9;
				S3();
			}
			else if(strIn[index] == '<' && (index + 4 <= length) && strIn.substring(index, index + 4) == "<!--")
			{
				strOut += "<!--";
				index += 4;
				S4();
			}
			else
			{
				strOut += strIn[index];
				index++;
			}
		}
	}

	//внутри " "
	function S2()
	{
		while(true)
		{
			if(strIn[index] == '"')
			{
				strOut += strIn[index];
				index++;
				break;
			}
			else if(strIn[index] == '<' && (index + 9 <= length) && strIn.substring(index, index + 9) == "<![CDATA[")
			{
				strOut += "<![CDATA[";
				index += 9;
				S3();
			}
			else if(strIn[index] == '<' && (index + 4 <= length) && strIn.substring(index, index + 4) == "<!--")
			{
				strOut += "<!--";
				index += 4;
				S4();
			}
			else if(strIn[index] == '\n')
			{
				strOut += "&#xD;";
				index++;
			}
			else if(strIn[index] == '\r')
			{
				strOut += "&#xA;";
				index++;
			}
			else
			{
				strOut += strIn[index];
				index++;
			}
		}
	}

	//внутри <![CDATA[ ]]>
	function S3()
	{
		while(true)
		{
			if(strIn[index] == ']' && (index + 3 <= length) && strIn.substring(index, index + 3) == "]]>")
			{
				strOut += "]]>";
				index += 3;
				break;
			}
			else
			{
				strOut += strIn[index];
				index++;
			}
		}
	}

	//внутри <!-- -->
	function S4()
	{
		while(true)
		{
			if(strIn[index] == '-' && (index + 3 <= length) && strIn.substring(index, index + 3) == "-->")
			{
				strOut += "-->";
				index += 3;
				break;
			}
			else
			{
				strOut += strIn[index];
				index++;
			}
		}
	}
}

