/**
 * @include "includes.js"
 */


if (typeof(console)=='undefined'){
	console = new Object();
}

if (!console) {
	console = new Object();

}

if (!console.log) {
	console.log = function() {
	};
}
if (!console.assert) {
	console.assert = function() {
	};
}

if (!console.group) {
	console.group = function() {
	};
}
if (!console.groupEnd) {
	console.groupEnd = function() {
	};
}

if (!console.warn) {
	console.warn = function() {
	};
}

if (!console.error) {
	console.error = function() {
	};
}
if (!console.debug) {
	console.debug = function() {
	};
}
if (!console.info) {
	console.info = function() {
	};
}

if (!console.trace) {
	console.trace = function() {
	};
}

if (!console.dir) {
	console.dir = function() {
	};
}

console.warnSet = console.warn;
console.warnWorkScript = console.warn;
console.warnTranslateScript = console.warn;

console.logCall = console.info;
console.logCallScript = console.info;

console.logL1 = console.log;

console.logL2 = console.log;

console.logWorkScript = console.log;

console.logParam = console.log;

console.logDraw = function() {
};
console.logDraw = console.log;

console.infoDraw = function() {
};
console.infoDraw = console.info;

console.warnDraw = function() {
};
console.warnDraw = console.warn;

console.errDraw = function() {
};
console.errDraw = console.error;

console.logObj = console.dir;
