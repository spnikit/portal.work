/**
 * @include "includes.js"
 */

if (!('console' in window))
	{
	window.console = new Object();
	
	}

if (!('log' in console))
	{
	window.console.log = function()
		{
		};
	}

if (!('warn' in console))
	{
	window.console.warn = function()
		{
		};
	}

if (!('error ' in console))
	{
	window.console.error = function()
		{
		};
	}
if (!('debug' in console))
	{
	window.console.debug = function()
		{
		};
	}
if (!('info' in console))
	{
	window.console.info = function()
		{
		};
	}

if (!('trace' in console))
	{
	window.console.trace = function()
		{
		};
	}

if (!('dir' in console))
	{
	window.console.dir = function()
		{
		};
	}

window.console.warnSet = window.console.warn;
window.console.warnWorkScript = window.console.warn;
window.console.warnTranslateScript = window.console.warn;

window.console.logCall = window.console.info;
window.console.logCallScript = window.console.info;

window.console.logL1 = window.console.log;

window.console.logL2 = window.console.log;

window.console.logWorkScript = window.console.log;

window.console.logParam = window.console.log;

window.console.logDraw = function()
	{
	};
window.console.logDraw = window.console.log;

window.console.warnDraw = function()
	{
	};
window.console.warnDraw = window.console.warn;

window.console.errDraw = function()
	{
	};
window.console.errDraw = window.console.error;

window.console.logObj = window.console.dir;
