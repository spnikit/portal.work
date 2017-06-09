
function gup( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return results[1];
}


  var objectID = "obj";
  var appletID = "applet";
    
function deletecookies(){
// Delete_Cookie("docid");
// Delete_Cookie("certserial");

}

function set_cookie ( name, value, exp_y, exp_m, exp_d, path, domain, secure )
{
  var cookie_string = name + "=" + escape ( value );

  if ( exp_y )
  {
    var expires = new Date ( exp_y, exp_m, exp_d );
    cookie_string += "; expires=" + expires.toGMTString();
  }

  if ( path )
        cookie_string += "; path=" + escape ( path );

  if ( domain )
        cookie_string += "; domain=" + escape ( domain );
  
  if ( secure )
        cookie_string += "; secure";
  document.cookie = cookie_string;
  }
 
 function Get_Cookie( name ) {	
var start = document.cookie.indexOf( name + "=" );
var len = start + name.length + 1;
if ( ( !start ) &&
( name != document.cookie.substring( 0, name.length ) ) )
{
return null;
}
if ( start == -1 ) return null;
var end = document.cookie.indexOf( ";", len );
if ( end == -1 ) end = document.cookie.length;
return unescape( document.cookie.substring( len, end ) );
}

function Delete_Cookie( name, path, domain ) {

}
	
 
 


var cp; 
var topp;
function ResizeAppletSmall(){ 

if(!cp) cp = Ext.getCmp('cpanel');
if(!topp) topp = Ext.getCmp('toppanell');
cp.collapse(false);
cp.hide();
topp.collapse(false);
topp.hide();
document.getElementById('viewerid').className='';
document.getElementById('viewerid').height=document.body.clientHeight-2; 
}


// function CreateOpenDoc()
// {
//	
//	
// var docid =document.applet_adapter.getDocid();
// var certserial = document.applet_adapter.getCertSer();
// ETD.setTemp(docid, certserial);
// ResizeAppletSmall();
// var xfdl=document.applet_adapter.getXmldata();
//	
// document.getElementById("tableForViewer").style.zIndex = "2";
// document.getElementById("tableForViewer").focus();
// viewername.CreateOpenViewer(xfdl);
//	
// 
// }


function CreateOpenDoc(){

	var docid =document.applet_adapter.getDocid();
	var certserial = document.applet_adapter.getCertSer();
	ETD.setTemp(docid, certserial);	
	var xfdl=document.applet_adapter.getXmldata();	
	isHV = document.applet_adapter.getIsHV();

	switch (isHV){
	case 0:
		useViewer = "IBM";
		break;
	case 1:
		useViewer = "AISA";
		break;
	}
	

if (useViewer=="IBM"){
	ResizeAppletSmall();	
	document.getElementById("tableForViewer").style.zIndex = "2";
	document.getElementById("tableForViewer").focus();
		viewername.CreateOpenViewer(xfdl);
	
	
// ETD.viewerWindow = new Ext.Window({
// id: 'ViewerWindow',
// resizable:false,
// header: false,
// hideBorders: true,
// plain:true,
// layout: 'fit',
// maximized : true,
// modal:true,
// shadow:false,
// bodyBorder: false,
// border: false,
// closable: false,
// constrainHeader:true,
// html:'<iframe id="viewerid" name="viewername" width="100%" height="100%"
// frameborder="0" src="viewer.html"> '+
// '<h1>IFRAME NOT SUPPORTED</h1> '+
// '</iframe>'
// });
//	
// ETD.viewerWindow.show();
//	
// ResizeAppletSmall();
// var innerWindow;
//
// (function () {
// innerWindow = document.getElementById("viewerid").contentWindow;
// innerWindow.CreateOpenViewer(xfdl);
//	
// }).defer(1000);
}


if (useViewer=="AISA"){
	newFormWindow(xfdl);
}


}


function CreateOpenReport()
{
if(!cp) cp = Ext.getCmp('cpanel');
	if(!topp) topp = Ext.getCmp('toppanell');
ResizeAppletSmall();
var xfdl=document.applet_adapter.getXmldata();
viewername.CreateOpenViewer(xfdl);
// ETD.viewerWindow = new Ext.Window({
// id: 'ViewerWindow',
// resizable:false,
// header: false,
// hideBorders: true,
// plain:true,
// layout: 'fit',
// maximized : true,
// modal:true,
// shadow:false,
// bodyBorder: false,
// border: false,
// closable: false,
// constrainHeader:true,
// html:'<iframe id="viewerid" name="viewername" width="100%" height="100%"
// frameborder="0" src="viewer.html"> '+
// '<h1>IFRAME NOT SUPPORTED</h1> '+
// '</iframe>'
// });
// ETD.viewerWindow.show();
//	
// ResizeAppletSmall();
// var innerWindow;
// var xfdl=document.applet_adapter.getXmldata();
// (function () {
// innerWindow = document.getElementById("viewerid").contentWindow;
// innerWindow.CreateOpenViewer(xfdl);
//	
// }).defer(1000);
	
	
}


function doRedirect(str)
{
viewername.CreateOpenViewer(str);
}


function Refresh(){
// window.location.href = 'indexscan.html?t=' + (new Date()).getTime();
	window.location.href = '';
}


function resizeLargePartialResp(){
	busy();
	var response = eval('('+document.applet_adapter.PartialResp()+')');
	
	if (response.error == 'false'){
		Refresh();
		}
	else 
		if (response.error)
		{ 
			Ext.MessageBox.alert(response.error);
		}
	resizeLargeAfterCancel();
	ready();
}


var clear_win = function(){
	
	/*
	 * for(var ii=0; ii<filterwin_newall.items.length; ii++) {
	 * filterwin_newall.items.get(ii).setValue(""); }
	 */
	
}

var doaftersave = function(){
	
// ETD.viewerWindow.destroy();
if (ETD.packid>0){
(function(){
Ext.Ajax.request({
url: 'forms/count',
params: {docid:ETD.packid},
callback:function(options, success, response){
var resp = response.responseText;
var arr= Ext.util.JSON.decode(resp);

if (arr[0].success==true&&arr[0].count>0){

	Pack.req = ETD.packid;
	Pack.mkpackwin(ETD.packid);
}
else if (arr[0].success==true&&arr[0].count==0){
ETD.dropPackid();
}
}
});
}).defer(1);
}

if (ETD.packRTKid>0){
	PackRTK.req = ETD.packRTKid;
	PackRTK.mkPackRTKwin(ETD.packRTKid);
}

if (ETD.packCSSid>0) {
	(function(){
	Ext.Ajax.request({
		url: 'forms/count',
		params: {docid:ETD.packCSSid},
		callback:function(options, success, response){
			var resp = response.responseText;
			var arr= Ext.util.JSON.decode(resp);
			if (arr[0].success==true&&arr[0].count>0) {
				PackCSS.req = ETD.packCSSid;
				PackCSS.mkpackwin(ETD.packCSSid);
			} else if (arr[0].success==true&&arr[0].count==0) {
				ETD.dropPackCSSid();
			}
		}
	});
	}).defer(1);
}



	if (document.getElementById('viewerInWinId'+IdForWindow)){
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
		Ext.getCmp('viewerInWinId'+IdForWindow).destroy();
		ETD.ready();
	} else {
		document.getElementById('viewerid').className='frame';
		document.getElementById("tableForViewer").style.zIndex = "-1";
		
		cp.show();
		cp.expand();
		topp.show();
		topp.expand();
// (function(){}).defer(1);
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
// Ext.getCmp('tpanel').getActiveTab().getStore().reload();
		ETD.ready();
// ETD.viewerWindow.destroy();
	}
	
}



var doafterdrop  = function(){

// ETD.viewerWindow.destroy();
if (ETD.packid>0){

	Pack.req = ETD.packid;
	Pack.mkpackwin(ETD.packid);

}
if (ETD.packCSSid>0) {
	PackCSS.req = ETD.packCSSid;
	PackCSS.mkpackwin(ETD.packCSSid);
}

	if (document.getElementById('viewerInWinId'+IdForWindow)){
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
		Ext.getCmp('viewerInWinId'+IdForWindow).destroy();
		ETD.ready();
	} else {
		document.getElementById('viewerid').className='frame';
		document.getElementById("tableForViewer").style.zIndex = "-1";
		
		cp.show();
		cp.expand();
		topp.show();
		topp.expand();
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
		ETD.ready();
// ETD.viewerWindow.destroy();
	}
	

}


function resizeLarge_(saveparam){
// ETD.viewerWindow.hide();
if (saveparam.length>2 ){
}
doaftersave();
}

function getPare(){
  busy();
  document.applet_adapter.getparedoc();   
  ResizeAppletSmall();
  setTimeout('CreateOpenDoc()',50);
  ready();
}



function Save(whereOCO){
	if (oco_or_snt_index==1) 
		document.applet_adapter.saveDocSNT();
	else 
		document.applet_adapter.saveDocOCO(whereOCO);
	
}




function Update_pbar(inp){
	update_pbar(inp);
}
function resizeaftersay(){
doaftersave();
}



function resizeLarge_(saveparam){
// ETD.viewerWindow.hide();
	if (saveparam.length>2 ){
	}
	doaftersave();
	}

	function resizeLarge(){
// ETD.viewerWindow.hide();
		if (document.getElementById('viewerInWinId'+IdForWindow)){
			document.getElementById('vieweridWin'+IdForWindow).style.visibility = 'hidden';
			document.getElementById('vieweridWin'+IdForWindow).nextSibling.style.visibility = 'visible';
		}
			
		document.applet_adapter.saveDoc("1");
// deletecookies();
		doaftersave();
	}


	
	function resizeLargeDrop(){
// ETD.viewerWindow.hide();
		if (document.getElementById('viewerInWinId'+IdForWindow)){
			document.getElementById('vieweridWin'+IdForWindow).style.visibility = 'hidden';
			document.getElementById('vieweridWin'+IdForWindow).nextSibling.style.visibility = 'visible';
		}
		var text = 'Документ забракован собственником';
		var tab =0;
		var docid = ETD.tempdocid;
		document.applet_adapter.dropdoc(text, tab, docid);

		ETD.unlockDoc(ETD.tempdocid, ETD.tempcert);
// deletecookies();
		ETD.dropTemp();
		doafterdrop();
	}
	
	
	function torg12drop(){
// ETD.viewerWindow.hide();
		var tab =0;
		ETD.droptorgid = ETD.tempdocid;
		droptorg();
		ETD.unlockDoc(ETD.tempdocid, ETD.tempcert);
		ETD.dropTemp();
		doafterdrop();
	}
	
	function fpu26ppsdrop(){
		var tab =0;
		ETD.fpu26ppsdropid = ETD.tempdocid;
		dropfpu26pps();
		ETD.unlockDoc(ETD.tempdocid, ETD.tempcert);
		ETD.dropTemp();
		doafterdrop();
	}
	
	
	function resizeLargeafterSogl(){
		
	var docid = ETD.tempdocid;
	(function(){
Ext.Ajax.request({
	url : 'forms/accdoc',
params : {
docid : docid
},
callback : function(options, success, response) {
var resp = response.responseText;
var arr = Ext.util.JSON.decode(resp);

if (arr == 'true') {
	
} else {
	Ext.MessageBox
	.alert('Ошибка',
			'Ошибка при согласовании документа');
}


}
});
}).defer(1);
ETD.unlockDoc(ETD.tempdocid,ETD.tempcert);
doaftersave();
	
		
	}
		
		
	
	function resizeLargeAfterCancel(){
		
// ETD.viewerWindow.hide();
// ETD.viewerWindow.destroy();
		if (ETD.tempdocid>-1){

			ETD.unlockDoc(ETD.tempdocid,ETD.tempcert);
		}
		// ETD.dropTemp();
// deletecookies();
		// cp.updatetable();
		if (ETD.packid>0){
			Pack.req = ETD.packid;
			Pack.mkpackwin(ETD.packid);

		}
		
		else if (ETD.packRTKid>0){
			PackRTK.req = ETD.packRTKid;
			PackRTK.mkPackRTKwin(ETD.packRTKid);
		}
		
		else if (ETD.packCSSid>0){
			PackCSS.req = ETD.packCSSid;
			PackCSS.mkpackwin(ETD.packCSSid);
		}
		
		
		if (document.getElementById('viewerInWinId'+IdForWindow)){
			Ext.getCmp('viewerInWinId'+IdForWindow).destroy();
			ETD.ready();
		} else {
			document.getElementById('viewerid').className='frame';
			document.getElementById("tableForViewer").style.zIndex = "-1";
			
			cp.show();
			cp.expand();
			topp.show();
			topp.expand();
			ETD.ready();
// ETD.viewerWindow.destroy();
		}

	}
	
	
	var IdForWindow=0;
	function newFormWindow(blob){

		var winframe;
		IdForWindow++;
		new Ext.Window({
			id: 'viewerInWinId'+IdForWindow,
			width: 800,
// height: window.innerHeight-50,
			height: 500,
			modal: false,
			resizable: true,
			maximizable: true,
			shadow: false,
			html: '<div style="background-image: url('+"'pics/2.jpg'"+')">'+
			'<iframe id="vieweridWin'+IdForWindow+'" height="100%" width="100%" frameborder="0" name="viewernameWin'+IdForWindow+'" src="aisaViewer.html">'+
			'</iframe>'+
			'<div style="position:absolute;text-align:center;top:50%;visibility:hidden;width:100%;">'+
				'<h2>Пожалуйста, подождите.</h2></br><h2>Документ сохраняется.</h2>'+
			'</div>'+
			'</div>',
			listeners: {
				show: function(win){
					
					winframe = Ext.query('iframe',win.body.dom)[0];
					if (winframe.attachEvent) winframe.attachEvent('onload', function(){
						try {
							winframe.contentWindow.CreateOpenViewer(blob, IdForWindow);
						} catch(e){	}
					});
					else winframe.addEventListener('load', function(){
						try {
							winframe.contentWindow.CreateOpenViewer(blob, IdForWindow);
						} catch(e){}
					}, false);
				},
				beforeclose: function(win){
					winframe.contentWindow.location.assign('forms/cancel.form');
					return false;
				},
				destroy: function(win){
					IdForWindow--;
				}
			}
		}).show();

	}
	
	var VRKMassSign = new Object();
	VRKMassSign.createwin = function(){
		
// var textData = new Ext.form.Label({
// html : '<b>Подписание пакета</b>'
// });
		
		var progressBar = new Ext.ProgressBar({
			id : 'pbMS',
			cls : 'left-align',
			style : 'margin-top: 5px;'
		});
		
		var win = new Ext.Window({
			title : '<b>Подписание пакета</b>',
			layout: 'form',
			width : 400,
// border : false,
// hideBorders : true,
			style : 'text-align:center;',
			resizable : false,
			buttonAlign : 'center',
			closable : true,
			modal : true,
			items : [progressBar/* ,textData */]
// ,buttons : [ {
// text : 'OK',
// handler : on_ok
// } ]
		,listeners:{
            'close':function(win){
            	 Ext.getCmp('tpanel').getActiveTab().getStore().reload();
        }
		}
		});
		
		progressBar.setVisible(true);
		console.log('createwin');
// textData.setVisible(false);
		
		return [win/* ,textData */,progressBar];
	};


