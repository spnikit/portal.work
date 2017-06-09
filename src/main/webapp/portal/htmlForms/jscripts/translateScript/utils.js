/**
 * @include "../includes.js"
 * @include "sxfdl.js"
 */

function StackSinhronize()
	{
	/**
	 * 
	 * @type UnitSynhrTeg[]
	 * @private
	 */
	this._massSinhrTeg = new Array();

	/**
	 * 
	 * @type Number
	 * @private
	 */
	this._sizeMass = 0;

	}

/**
 * 
 * @param {UnitSynhrTeg}
 *            unitSynhr
 */
StackSinhronize.prototype.addListenedTeg = function(unitSynhr)
	{
	this._massSinhrTeg[0] = unitSynhr;
	this._sizeMass++;
	};

/**
 * 
 */
StackSinhronize.prototype.empty = function()
	{
	this._massSinhrTeg = new Array();
	this._sizeMass = 0;
	};

StackSinhronize.prototype.getSize = function()
	{
	return this._sizeMass;
	};

StackSinhronize.prototype.startSynhronize = function()
	{
	/**
	 * @type UnitSynhrTeg
	 */
	var thisUnitSynhr;

	};

/**
 * 
 * 
 * @param {InfoElement}
 *            infoEl
 * @param {String}
 *            nameTeg
 * @param {Node}
 *            teg
 * @class
 * 
 * @constructor
 */
function UnitSynhrTeg(infoEl, nameTeg, teg)
	{
	this._infoEl = infoEl;
	this.nameTeg = nameTeg;
	if (teg)
		this._teg = teg;
	else
		// typeof teg == "undefined"
		{

		}
	}
