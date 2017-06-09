/**
 * 
 * @include "includes.js"
 */

/**
 * объект через который происходит обращение к аплету
 * 
 * @type
 */
// var applet_adapter;
/**
 * iframe для xfdl-вьвера
 * 
 * @type HTMLIFrameElement
 */
var viewerIFrame;

/**
 * 
 * @type String
 */
var browser;

var isMyDegug = false;

/**
 * какой вьювер использовать
 * 
 * @type String<br/> допустимые значения:<br/> <b>IBM</b> - Lotus<br/>
 *       <b>AISA</b> - наш<br/>
 * @default IBM
 * 
 */
var useViewer = "IBM";

/**
 * производится ли в данный момент работа с какой либо формой
 * 
 * @type Boolean
 */
var isFormView;

/**
 * можно ли открывать полученную форму html-вьювером<br/> 0- нет<br/> 1 -
 * можно в рабочем режиме <br/> 2 - можно в тестовом режиме
 * 
 * @type Number
 */
var isHV;

/**
 * является ли пользователь тестером(позволяет просматривать некоторые формы в
 * html-вьювере, недоступные для такого просмотра обычным пользовательям)
 * 
 * @type Boolean
 */
var isUserTester;


console.log("load js/globalVar.js");