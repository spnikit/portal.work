

/**
 * дополнительный <b>&lt;div></b> для измерения высоты/ширины текста
 * 
 * @type HTMLElement
 */

var dopDivSize = null;

/**
 * разрешено ли редактирование
 * 
 * @type Boolean
 */
var isGlobalEdit = true;

dopDivSize = document.createElement("div");

/**
 * Является ли текущий запуск тестовым. Нужно для отладки отрисовки форм. Если
 * равно <b>true</b> то считается что запуск происходит из АСЭТД, в проивном
 * случае запуск считается отладочным
 * 
 * @type Boolean
 */
var isViewerDebug = false;

/**
 * Главное окно АСЭТД. Нужно для обратной связи из iframe
 * 
 * @type Window
 */
var glWindow;

/**
 * путь добавляемый при загрузке ресурсов
 * 
 * @type String
 * 
 * @example
 * при использовании вьюера из АСЭТД будет равным "htmlFroms/", при использоавнии специального отладочного сервера будет равен ""
 */
var addPathResource = "htmlForms/";

/**
 * тэг div содержащий все отрисованные элементы
 * 
 * @type HTMLDivElement
 */
var XFDLViever;

/**
 * @constant
 * @type String
 */
var base64GZipHeader = "application/vnd.xfdl;content-encoding=\"base64-gzip\"";
var base64GZipHeaderOld = "application/vnd.xfdl; content-encoding=\"base64-gzip\"";

/**
 * div для окошка создания подписи
 * 
 * @type HTMLDivElement
 */
var windowForSignManager = null;
/**
 * div для окошка выбора кем подписывать
 * 
 * @type HTMLDivElement
 * @note пока не используется TODO что будет если два токена?
 */
var windowForSelectInfoSign = null;

/**
 * div для окна предупреждения
 * 
 * @type HTMLDivElement
 * @note пока не используется
 */
var windowForWarring = null;

/**
 * div для окна несоответсвия шаблону
 * 
 * @type HTMLDivElement|HTMLElement
 * 
 */
var windowForMandatoryMes = null;

var windowInfo = null;

var windowForAttach = null;
// TODO далее идут переменные используемые
// тольк при отладочном сервере

/**
 * url для сохранения формы при помощи POST, где form='имя_формы', content -
 * собственно сама форма
 * 
 * @type String
 */
var saveURL;

/**
 * 
 * @type String
 */
var nameXFDL;

//new-----------------------------
//количество ошибок, проверяется перед сохранением 0-можно сохранять >0-нельзя
var saveError = 0;
//возможна ли подпись формы 0-нет, 1-да
var maySign = 0;
//переменная плагина
var plug = null;
//new-----------------------------
