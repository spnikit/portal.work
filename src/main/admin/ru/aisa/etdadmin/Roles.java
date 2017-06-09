package ru.aisa.etdadmin;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import ru.aisa.rgd.ws.utility.TypeConverter;


public class Roles extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger	log	= Logger.getLogger(Roles.class);   

	private static String formtypes = "function(){return new Ext.data.SimpleStore({url:'controllers/formtypes',fields:['id','text'],autoLoad:true})}";
	private static String formtypes_all = "function(){return new Ext.data.SimpleStore({url:'controllers/formtypes',fields:['id','text'],autoLoad:true,baseParams:{all:true}})}";	
	private static String roletypes_oco = "function(){return new Ext.data.SimpleStore({url:'controllers/wrknamesoco',fields:['id','text'],autoLoad:true})}";
	private static String roletypes = "function(){return new Ext.data.SimpleStore({url:'controllers/wrknames',fields:['id','text'],autoLoad:true})}";
	private static String predpris = "function(){return new Ext.data.SimpleStore({url:'controllers/prednames',fields:['id','text'],autoLoad:true})}";
	private static String roadnames = "function(){return new Ext.data.SimpleStore({url:'controllers/dors',fields:['id','text'],autoLoad:true})}";
	private static String issm = "function(){return new Ext.data.SimpleStore({url:'controllers/issm',fields:['id','text'],autoLoad:true})}";
	private static String doctype = "function(){return new Ext.data.SimpleStore({url:'controllers/doctype',fields:['id','text'],autoLoad:true})}";
	private static String docdoctype = "function(){return new Ext.data.SimpleStore({url:'controllers/docdoctype',fields:['id','text'],autoLoad:true})}";
	
	
	
	private static String admins = "{columns:" +
			"[{name:'id',type:'hidden',hidden:true}," +
			"{name:'username',header:'Пользователь',type:'text'}," +
			"{name:'dorid',type:'hidden',hidden:true}," +
			"{name:'dorname',type:'hidden',header:'Дорога',store:"+roadnames+",im:true,nc:true}," +
			"{name:'role',type:'hidden',header:'Роль',store:function()" +
			"{return [['"+Utils.ROLE_USER+"','Администратор дороги'],['"+Utils.ROLE_ADMIN+"','Администратор ГВЦ'],['"+Utils.ROLE_READONLY+"','Роль для просмотра'],['"+Utils.ROLE_POWER_USER+"','Роль для администратора ИВЦ']]},im:true,nc:true}," +
			"{name:'password',header:'Пароль',type:'password',hidden:true}],title:'Администраторы',expand:'username',url:'controllers/admins'}";
	


	private static String forms = "{columns:" +
			"[{name:'docname',header:'Название формы',type:'text'}," +
			"{name:'formfile',type:'file',header:'Файл шаблона',im:true}," +
			"{name:'docid',type:'hidden',hidden:true,header:'Тип'}," +
//			"{name:'ptype',header:'Тип',type:'combo',store:"+doctype+"}," +
			"{name:'docdoctypeid',type:'hidden',hidden:true}," +
			"{name:'docdoctype',header:'Вид документа',type:'combo',store:"+docdoctype+"}],title:'Формы',expand:'docname',url:'controllers/forms',search:{header:'Введите название формы'},fullwidth:'true'}";//{header:'Введите название формы'}search_sample:'ru.aisa.SearchForms'

	private static String docdoctypes = "{columns:" +
			"[{name:'doctypename',header:'Название',type:'text'}," +	
			"{name:'docid',type:'hidden',hidden:true,header:'Название'}],title:'Виды документооборота',expand:'doctypename',url:'controllers/docdoctypes',search:{header:'Введите название'},fullwidth:'true'}";//search:{header:'Введите название'}
			
//add ptype
	private static String signdetails_oco = "{columns:" +
			"[{name:'dtid',type:'hidden',hidden:true}," +
			"{name:'documentname',header:'Форма',type:'combo',store:"+formtypes+"}," +
			"{name:'order',header:'Номер',type:'text',reg: /^[0-9]*$/}," +
			"{name:'wrkid',type:'hidden',hidden:true}," +
			"{name:'rolename',header:'Предприятие',type:'combo',store:"+roletypes_oco+"}," +
			"{name:'old_order',type:'hidden',hidden:true}," +
			"{name:'parent',header:'Родительский номер',type:'text',reg: /^[0-9]*$/,allowBlank:true}" +
			",{name:'exp',header:'XPath выражение',type:'text',allowBlank:true}" +
			"],title:'Маршрутизация',expand:'rolename',url:'controllers/signdetailsoco',extbutt_create:'ru.aisa.extCreateButt',app:'false',exbutt:'ru.aisa.megabutton',searchOCO:'ru.aisa.SearchOCO'}";
	
	private static String rights_oco = "{columns:" +
			"[{name:'dtid',type:'hidden',hidden:true}," +
			"{name:'docname',header:'Форма',type:'combo',store:"+formtypes_all+"}," +
			"{name:'wrkid',type:'hidden',hidden:true}," +
			"{name:'wrkname',header:'Должность',type:'combo',store:"+roletypes_oco+"}," +
			"{name:'cview',header:'Просмотр',type:'checkbox',items:[{fieldLabel: 'Просмотр',boxLabel: 'Разрешить',name: 'cview'}]},"+
			"{name:'cedit',header:'Редактирование',type:'checkbox',items:[{fieldLabel: 'Редактирование',boxLabel: 'Разрешить',name: 'cedit'}]},"+
			"{name:'cnew',header:'Создание',type:'checkbox',items:[{fieldLabel: 'Создание',boxLabel: 'Разрешить',name: 'cnew'}]},],title:'Права для форм',expand:'wrkname',url:'controllers/rights',app:'false',exbuttrights:'ru.aisa.megabuttonrights',mysearch:'ru.aisa.SearchRights'}";//search:{header:'Введите название формы'}
	
	private static String preds_admin = "{columns:" +
			"[{name:'FIO',header:'Пользователь',type:'readonly'}," +
			"{name:'certserial',header:'ID сертификата',hidden:true,type:'text',reg:new RegExp('^[0-9]*$')}," +
			"{name:'wrkid',hidden:true,type:'hidden'}," +
			"{name:'rolename',header:'Должность',type:'combo',store:"+roletypes+"}," +			
			"{name:'predid',type:'hidden',hidden:true}," +
			"{name:'predprs',header:'Предприятие',type:'combo',store:"+predpris+"}," +
			"{name:'pid',type:'hidden',hidden:true}],title:'Привязка пользователей',expand:'FIO',url:'controllers/pred',search:{header:'Введите ФИО пользователя'},fullwidth:'true'}";
	
	
	private static String roles = "{columns:" +
			"[{name:'name',type:'text',header:'Должность'}," +
			"{name:'id',type:'hidden',hidden:true}," +
			"{name:'issm',type:'combo',header:'Функциональная роль',store:"+issm+"}," +
			"{name:'issmid',type:'hidden',hidden:true}," +
			"],title:'Должности',expand:'name',url:'controllers/roles',search:{header:'Введите должность'},extbutt_create:'ru.aisa.extCreateButt'}";

	private static String users_admin = "{columns:" +
			"[{name:'fname',header:'Фамилия',type:'text',allowBlank:true}," +
			"{name:'mname',header:'Имя',type:'text',allowBlank:true}," +
			"{name:'lname',header:'Отчество',type:'text',allowBlank:true},"+
			"{name:'title',header:'Должность',type:'text',allowBlank:true},"+
	        "{name:'certserial',header:'ID сертификата',type:'readonly'}," +
			"{name:'certfile',type:'file',im:true,description:'Файл сертификата'}," +
			"{name:'id',type:'hidden',hidden:true}," +
			"{name:'depid',type:'hidden',hidden:true}," +
			"{name:'otdel',header:'Должность',type:'combo',store:"+roletypes+",im:true,nc:true}," +
			"{name:'pred',header:'Предприятие',type:'combo',store:"+predpris+",im:true,nc:true}, " +
			"{name:'auto',header:'Автоподпись',type:'checkbox',width:100,items:[{fieldLabel: 'Автоподпись',boxLabel: 'Разрешить',name: 'auto'}]},"+
			"{name:'sgn',header:'Подписание квитанций',type:'checkbox',width:100,items:[{fieldLabel: 'Подписание квитанций',boxLabel: 'Разрешить',name: 'sgn'}]}"+
			"],title:'Пользователи',expand:'certserial',url:'controllers/users',search:{header:'Введите фамилию пользователя'},fullwidth:'true'}";

	
	private static String new_section = "{columns:"+
			"[{name:'id', header:'ID',type:'hidden',hidden:true}," +
			"{name:'vname',header:'Сокращенное наименование',type:'text',allowBlank:true}," +
			"{name:'name',header:'Наименование',type:'text',allowBlank:true}," +
			"{name:'okpo_kod',header:'код ОКПО',type:'text',allowBlank:true}," +
			"{name:'lname',header:'Полное наименование',type:'text',allowBlank:true}," +
			"{name:'cabinet_id',header:'Идентификатор предприятия ЭСЧФ',type:'readonly'}," +
			"{name:'inn',header:'ИНН',type:'text',allowBlank:true}," +
			"{name:'kpp',header:'КПП',type:'text',allowBlank:true}," +
			"{name:'headid',header:'Идентификатор для связи',type:'text',allowBlank:true}," +
			"{name:'contr_code',header:'Порядковый код контрагента',type:'text',allowBlank:true}," +
			"{name:'priceCheck',header:'Вкл./Выкл. проверку цен',type:'checkbox',width:100,items:[{fieldLabel: 'Вкл./Выкл. проверку цен',boxLabel: 'Разрешить',name: 'priceCheck'}]},"+
			"{name:'pdfCheck',header:'Выгрузка в PDF',type:'checkbox',width:100,items:[{fieldLabel: 'Выгрузка PDF',boxLabel: 'Разрешить',name: 'pdfCheck'}]},"+
			"], title:'Предприятия', url:'controllers/prednew', mysearch:'ru.aisa.advancedSearch' }";
	
	private static String directory_services = "{columns:"+
			"[{name:'id', header:'ID',type:'hidden',hidden:true}," +
			"{name:'name_pred',header:' Наименование предприятия',type:'combo',store:"+predpris+"}, " +
			"{name:'service_code',header:'Код услуги',type:'text',allowBlank:true}," +
			"{name:'service_name',header:'Наименование услуги',type:'text',allowBlank:true}," +
			"{name:'service_price',header:'Цена услуги',type:'text',allowBlank:true}," +
			"], title:'Справочник/услуги',expand:'service_name', url:'controllers/directory_service', search:{header:'Код'}, addArray:'ru.aisa.saveBdByExcel', deleteAllServices:'ru.aisa.deleteAllServices'  }";
	
	private static String directory_goods = "{columns:"+
			"[{name:'id', header:'ID',type:'hidden',hidden:true}," +
			"{name:'name_pred',header:' Наименование предприятия',type:'combo',store:"+predpris+"}, " +
			"{name:'service_name',header:'Наименование товарно-материальных ценностей',type:'text',allowBlank:true, width:'300px'}," +
			"{name:'service_price',header:'Цена',type:'text',allowBlank:true}," +
			"], title:'Справочник/товары',expand:'service_name',url:'controllers/directory_goods', search:{header:'Наименование товарно-материальных ценностей'} }";
	
	private static String pred_headid = "{columns:"+
			"[{name:'etdid',header:'Идентификатор документа',type:'text',allowBlank:true},"+ 
			"{name:'id_pak',header:'Идентификатор пакета',type:'text',allowBlank:true},"+ 
			"{name:'vagnum',header:'Номер вагона',type:'text',allowBlank:true},"+
			"{name:'repdate',header:'Дата ремонта',type:'text',allowBlank:true},"+
			"{name:'name',header:'Наименование документа',type:'text',allowBlank:true},"+
			"{name:'vname',header:'Наименование предприятия',type:'text',allowBlank:true}"+
			"], title:'Выборка',url:'controllers/headid',app:'false',del:'false',search_sample:'ru.aisa.createButtonSample',unloading_data:'ru.aisa.uploadExcel',mysearch:'ru.aisa.advancedSearch',clearbutton:'ru.aisa.clearButton'}";
	
	
	private static String protocol = "{title:'Протокол',url:'controllers/emptyUploadProtocol',app:'false',del:'false',search_sample:'ru.aisa.createProtocol'}";
	
	private static String addHTML_Template = "{columns:" +
			"[{name:'docname',header:'Название формы',type:'text'}," +
			"{name:'formfile',type:'file',header:'Файл шаблона',im:true}," +
			"{name:'docid',type:'hidden',hidden:true,header:'Тип'}],title:'HTML шаблоны',expand:'docname',url:'controllers/HTML_Template', app:'false', del:'false', fullwidth:'true'}";
	
	
	private static String news = "{notgrid:true,title:'Новости',config:{title:'Отправить сообщение',items:[new Ext.form.FormPanel({width:750,id:'news-form',border:false,reader: new Ext.data.JsonReader()," +
	        "items:[{xtype:'hidden',name:'edit',id:'news-edit',value:'false'}," +
	        "{xtype:'hidden',name:'del',id:'news-del',value:'false'}," +
	        "{xtype:'textarea',fieldLabel:'Текст',maxLength :512,width:ru.aisa.width1,name:'news',id:'news-text'}]," +
	        "buttons:[{xtype:'button',text:'Отправить',handler:function(){" +
	        	"var form = Ext.getCmp('news-form');" +
	        	"Ext.getCmp('news-edit').setValue('true');" +
	        	"Ext.getCmp('news-del').setValue('false');" +
	        	"form.getForm().submit({url:'controllers/news',"+
	        			"waitMsg:'Выполняется',failure:function(form,action){"+
	        		"Ext.Msg.alert('Сообщение не отправлено');" +
	        "}});" +
	        "}}" +
	        ",{xtype:'button',text:'Удалить',handler:function(){" +
	        	"var form = Ext.getCmp('news-form');" +
	        	"Ext.getCmp('news-text').setValue('');" +
	        	"Ext.getCmp('news-edit').setValue('false');" +
	        	"Ext.getCmp('news-del').setValue('true');" +
	        	"form.getForm().submit({url:'controllers/news',"+
	        			"waitMsg:'Выполняется',failure:function(form,action){"+
	        		"Ext.Msg.alert('Сообщение не удалено');" +
	        "}});}}" +
	        "]})]}}";
	
	//для readonly
	private static String admins_read = "{columns:" +
	"[{name:'id',type:'hidden',hidden:true}," +
	"{name:'username',header:'Пользователь',type:'text'}," +
	"{name:'dorid',type:'hidden',hidden:true}," +
	"{name:'dorname',type:'combo',header:'Дорога',store:"+roadnames+"}," +
	"{name:'role',type:'combo',header:'Роль',store:function()" +
	"{return [['"+Utils.ROLE_USER+"','Администратор дороги'],['"+Utils.ROLE_ADMIN+"','Администратор ГВЦ'],['"+Utils.ROLE_READONLY+"','Роль для просмотра'],['"+Utils.ROLE_POWER_USER+"','Роль для администратора ИВЦ']]}}," +
	"{name:'password',header:'Пароль',type:'password',hidden:true}],title:'Администраторы',expand:'username',url:'controllers/admins',app:'false',del:'false',readonly:'true'}";

private static String forms_read = "{columns:" +
	"[{name:'docname',header:'Название формы',type:'text'}," +
	"{name:'formfile',type:'file',header:'Файл шаблона',im:true}," +
	"{name:'docid',type:'hidden',hidden:true,header:'Тип'}," +
	"{name:'docdoctypeid',type:'hidden',hidden:true,header:'Вид документа'}," +
	"{name:'ptype',header:'Тип',type:'combo',store:"+docdoctype+"}," +
	"{name:'docdoctype',header:'Вид документа',type:'combo',store:"+doctype+"}],title:'Формы',expand:'docname',url:'controllers/forms',search:{header:'Введите название формы'},fullwidth:'true',app:'false',del:'false',readonly:'true'}";
//add ptype
private static String signdetails_oco_read = "{columns:" +
	"[{name:'dtid',type:'hidden',hidden:true}," +
	"{name:'documentname',header:'Форма',type:'combo',store:"+formtypes+"}," +
	"{name:'order',header:'Номер',type:'text',reg: /^[0-9]*$/}," +
	"{name:'wrkid',type:'hidden',hidden:true}," +
	"{name:'rolename',header:'Предприятие',type:'combo',store:"+roletypes_oco+"}," +
	"{name:'old_order',type:'hidden',hidden:true}," +
	"{name:'parent',header:'Родительский номер',type:'text',reg: /^[0-9]*$/,allowBlank:true}" +
	//",{name:'exp',header:'XPath выражение',type:'text',allowBlank:true}" +
	"],title:'Маршрутизация',expand:'rolename',url:'controllers/signdetailsoco',searchOCO:'ru.aisa.SearchOCO',app:'false',del:'false',readonly:'true'}";


private static String rights_oco_read = "{columns:" +
	"[{name:'dtid',type:'hidden',hidden:true}," +
	"{name:'docname',header:'Форма',type:'combo',store:"+formtypes_all+"}," +
	"{name:'wrkid',type:'hidden',hidden:true}," +
	"{name:'wrkname',header:'Предприятие',type:'combo',store:"+roletypes_oco+"}," +
	"{name:'cview',header:'Просмотр',type:'checkbox',items:[{fieldLabel: 'Просмотр',boxLabel: 'Разрешить',name: 'cview'}]},"+
	"{name:'cedit',header:'Редактирование',type:'checkbox',items:[{fieldLabel: 'Редактирование',boxLabel: 'Разрешить',name: 'cedit'}]},"+
	"{name:'cnew',header:'Создание',type:'checkbox',items:[{fieldLabel: 'Создание',boxLabel: 'Разрешить',name: 'cnew'}]},],title:'Права для форм',expand:'wrkname',url:'controllers/rights',search:{header:'Введите название формы'},app:'false',del:'false',readonly:'true'}";

private static String preds_admin_read = "{columns:" +
	"[{name:'FIO',header:'Пользователь',type:'readonly'}," +
	"{name:'certserial',header:'ID сертификата',hidden:true,type:'text',reg:new RegExp('^[0-9]*$')}," +
	"{name:'wrkid',hidden:true,type:'hidden'}," +
	"{name:'rolename',header:'Предприятие',type:'combo',store:"+roletypes+"}," +
	"{name:'pid',type:'hidden',hidden:true}],title:'Привязка к предприятиям',expand:'FIO',url:'controllers/pred',search:{header:'Введите ФИО пользователя'},fullwidth:'true',app:'false',del:'false',readonly:'true'}";


//private static String admins = "{columns:[{name:'id',type:'hidden',hidden:true},{name:'username',header:'Пользователь',type:'text'},{name:'password',header:'Пароль',type:'password',hidden:true}],title:'Администраторы',expand:'username',url:'controllers/admins'}";
private static String roles_read = "{columns:" +
	"[{name:'name',type:'text',header:'Предприятие'}," +
	"{name:'id',type:'hidden',hidden:true}," +
	"{name:'issm',type:'combo',header:'Функциональная роль',store:"+issm+"}," +
	"{name:'issmid',type:'hidden',hidden:true}," +
	"{name:'dor',type:'combo',store:"+roadnames+",header:'Дорога'}," +
	"{name:'dorid',type:'hidden',hidden:true}," +
	/*"{name: 'logo',type:'file', im:true, description: 'Логотип'},"+*/
	"{name:'bal',type:'text',header:'Балансовая единица',reg: /^[0-9]*$/}" +
	"],title:'Предприятия',expand:'name',url:'controllers/roles',search:{header:'Введите название предприятия'},app:'false',del:'false',readonly:'true'}";

private static String users_admin_read = "{columns:" +
"[{name:'fname',header:'Фамилия',type:'text',reg:/^[А-Я]+.*/}," +
"{name:'mname',header:'Имя',type:'text',reg:/^[А-Я]+.*/}," +
"{name:'lname',header:'Отчество',type:'text',reg:/^[А-Я]+.*/}," +
"{name:'certserial',header:'ID сертификата',type:'readonly'}," +
"{name:'certfile',type:'file',im:true,description:'Файл сертификата'}," +
"{name:'id',type:'hidden',hidden:true},{name:'depid',type:'hidden',hidden:true}," +
"{name:'otdel',header:'Предприятие',type:'combo',store:"+roletypes+",im:true,nc:true}],title:'Пользователи',expand:'certserial',url:'controllers/users',search:{header:'Введите фамилию пользователя'},fullwidth:'true',app:'false',del:'false',readonly:'true'}";


	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try {
		  doWork(request,response);
		  }
	  catch (Exception e) {
		 log.error(TypeConverter.exceptionToString(e));
		}
	  }
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		try {
			doWork(request, response);
		}
		catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	}
	private void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		

			Cookie ck = new Cookie("JUSERNAME",Utils.getUserName()) ;
			response.addCookie(ck);	
			if(Utils.getAuth().equals(Utils.ROLE_ADMIN))
				out.print("{roles:["+forms+","+docdoctypes+","+signdetails_oco+","+roles+","+rights_oco+","
						+admins+","+users_admin+","+preds_admin+","+news+","+new_section+","+pred_headid+","
						+protocol+","+directory_services+","+directory_goods+","+addHTML_Template+"]}");
			
		else if(Utils.getAuth().equals(Utils.ROLE_USER))
				out.print("{roles:[" +preds_admin+","+users_admin+","+rights_oco_read+","+signdetails_oco_read+","+docdoctypes+"]}");
			
		else if(Utils.getAuth().equals(Utils.ROLE_READONLY))
			out.print("{roles:["+forms_read+" ,"+preds_admin_read+","+admins_read+","+roles_read+","
		+rights_oco_read+","+signdetails_oco_read+","+users_admin_read+","+docdoctypes+"]}");
			
		else if(Utils.getAuth().equals(Utils.ROLE_POWER_USER))
			out.print("{roles:["+forms_read+" ,"+preds_admin+","+admins_read+","+roles_read+","+rights_oco_read+","+signdetails_oco_read+","+users_admin+","+docdoctypes+"]}");
	}
	
	public Roles() {
	}
	
}
