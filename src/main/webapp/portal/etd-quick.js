var applet_adapter;
Ext.BLANK_IMAGE_URL='pics/2.jpg';
function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\\\s)"+searchClass+"(\\\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

var busy_win = new Ext.Window({
	resizable:false,
	headerAsText:false,
	plain:true,
	layout: 'fit',
	width:1,
	height:1,
	modal:true,
	shadow:false,
	bodyBorder: false,
	border: false,
	hideBorders: true,
	closable: false
		});


var store,store_arch,combostore,combocrstore,combo,gridpanel,gridpanel_arch,tpanel
var cpanel,opendoc,dropdoc,toppanel,toolbarpanel,btnwidth,datewin

Ext.onReady(function(){
	globalInit();
	document.applet_adapter =new NpapiPlugin('aisa');
//	Ext.MessageBox.alert('Error','java applet  found');
	if (document.applet_adapter == null ) 
	{
		Ext.MessageBox.alert('Error','java applet not found');
	}
	
	else 
	(function(){
		var updated=eval('('+document.applet_adapter.Updated()+')');
		if(updated.updated)
		{
		login();
		} else{	ready(); Ext.MessageBox.alert('Внимание','Необходимо обновить программное обеспечение, версия <a href="'+updated.url+'">'+updated.version+'</a>');}
	}).defer(1)
	if(Ext.isIE6){
		document.getElementById('loading').style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='pics/misc/loading_panel_filled.png', sizingMethod='scale')";
		document.getElementById('loading').src=Ext.BLANK_IMAGE_URL;
	}


});
function login()
{
//console.logCall("call etd-quick/login()");
if (eval('(' + document.applet_adapter.login(' ') + ')').value)
	{
	ETD.busy();
	select_depo();
	}
else
	{
	ETD.ready();
	Ext.MessageBox.alert('Ошибка', 'необходима авторизация', function(btn)
		    {
		    Refresh();
		    });
	
	};
ETD.ready();
};

var select_depo = function(){
	var json = eval('('+document.applet_adapter.getWorkPosition()+')');
	 Dev.logger.log("Select depo : return ", json);
	if(json.error){
		 ETD.ready();
		Ext.MessageBox.alert("Ошибка!",json.error,function(btn){Refresh();});
	}else{
		
		if(json.name && json.rolename){
			predname=json.predname
			dorname=json.dorname;
			name=json.name;
			depname = json.rolename;
			rolename=json.rolename;
			ETD.fr = json.fr;
			ETD.predid = json.predid;
			ETD.predname = json.predname;
			ETD.wrkid = json.wrkid;
			mainwin();
			
			Ext.get(document.getElementsByTagName('html')[0]).addClass('x-viewport');
			Ext.get(document.getElementsByTagName('body')[0]).addClass('x-border-layout-ct');
						
		}
		else{
			// Auto size
			var Size=333;
			var MaxLen=0;
			var WinSize=Ext.getBody().getWidth();
			
			for (i=0;i<json.values.length;i++)
			{
				
			if (json.values[i].length>MaxLen)	MaxLen=json.values[i].length;
			}
			
			if (MaxLen*6>WinSize) MaxLen=(WinSize-200)/6;
			// Auto size
			var combopred = new Ext.form.ComboBox({
				store: json.values,
				width: MaxLen*6-20,
				editable: false,
				hideLabel:true,
				value: json.values[0],
				mode: 'local',
				triggerAction: 'all',
				selectOnFocus: true
			});
			on_ok = function(){
				
				ETD.busy();
				win.destroy();				
				(function(){
					json = eval('('+document.applet_adapter.setWorkPosition(combopred.getValue())+')');
					name = json.name;
					rolename=json.rolename;
					ETD.fr = json.fr;
					dorname=json.dorname;
					predname=json.predname;
					depname = json.rolename;
					ETD.predid = json.predid;
					ETD.predname = json.predname;
					ETD.wrkid = json.wrkid;
					mainwin();
					if(!predname){
						ETD.busy();
							if(!dorname){
		        		Ext.getCmp('combdor').enable();
			        	}
			        var predprs = eval('('+document.applet_adapter.getPredprs()+')');
						if(predprs.data)
						{
						}
	        		 		ETD.ready();
			        }
					Ext.get(document.getElementsByTagName('html')[0]).addClass('x-viewport');
					Ext.get(document.getElementsByTagName('body')[0]).addClass('x-border-layout-ct');
					}).defer(1);
			}
			var win = new Ext.Window({
				title: 'Выберите предприятие:',
				layout:'fit',
				width: 400,
				border:false,
				maximizable:true,
				hideBorders:true,
				resizable: true,
				buttonAlign:'center',
				closable: false,
				items:combopred,
				buttons: [{text: 'OK',	handler:on_ok}]
			});
			combopred
			ETD.ready();
			win.show();
			combopred.on('specialkey',function(field,e){
				if(e.getKey()==13) on_ok();
			})
		}
	}
};

var mainwin=function()
{	
	var initjson=eval('('+document.applet_adapter.getinitvalues()+')');
	if(initjson.error) {
		ETD.ready();
		Ext.get(document.getElementsByTagName('html')[0]).removeClass('x-viewport');
		Ext.get(document.getElementsByTagName('body')[0]).removeClass('x-border-layout-ct');
		Ext.MessageBox.alert('Ошибка!',initjson.error);
		return;
	}
	var docId = document.applet_adapter.getDocid();
//	alert(docId);
	var initjson=eval('('+document.applet_adapter.openDocument(docId,"ФПУ-26", 0,"ID_DOCUMENT_GRID")+')');
//	Ext.get(document.getElementsByTagName('body')[0]).removeClass('white-body');
//	document.body.bgColor="#96a2ad";
//	if(initjson.error) {
//		ETD.ready();
////		Ext.MessageBox.alert('Ошибка!',initjson.error);
//document.getElementById('ret').appendChild(document.createTextNode(initjson.error));
//	document.getElementById('ret').style.top=document.body.clientHeight/2-15+ 'px';
//		return;
//	}
	ETD.ready();
	OpenDoc();
}


function saveandnoexit()
{
console.logCall("call quickutils/saveandnoexit()");
var saveresp = document.applet_adapter.saveDoc('0');
console.info("saveresp = " + saveresp);
if (saveresp != 'Документ сохранен успешно')
	alert(saveresp);
return saveresp;
}

