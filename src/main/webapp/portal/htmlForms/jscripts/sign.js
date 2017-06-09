$(document).ready(function()
{
	$("#sign_div_background").hide();

	$("[sign=true]").each(function()
	{
		$(this).bind('click', function()
		{
			var sid = $(this).attr("sid");
			var deletable = $(this).attr("deletable");
			var signed = $(this).attr("signed");
			if(sid == '')
			{
				alert('ERROR');
				return;
			}

			if(signed == 'true')
			{
				$("#sign_do_sign").attr('disabled', 'disabled');
				$("#sign_label").text("Signed");
			}
			else
			{
				$("#sign_do_sign").removeAttr('disabled');
				$("#sign_label").text("Unsigned");
			}

			if(deletable == 'true' && signed == 'true')
				$("#sign_do_del").removeAttr('disabled');
			else
				$("#sign_do_del").attr('disabled', 'disabled');

			$("#sign_div_background").show();
			$("#sign_div_background").attr('sign_button_sid', sid);
		})
	});

});

function ok()
{
	$("#sign_div_background").hide();
	$("#sign_div_background").removeAttr('sign_button_sid');
};



function addSign()
{
	var buttonSid = $("#sign_div_background").attr('sign_button_sid');
	if(buttonSid == '' || buttonSid == 'undefined')
	{
		alert('ERROR');
		return;
	}
	var button = $("[sid="+buttonSid+"]");
	var sid=button.attr("sid");
	
	var signedXfdl="";// = document.signApplet.createSignerForStream();
    /**
     * @type String
     */
	var ss = "";
	var string="<hellow><a>sss</a></hellow>";
//	var s = new XMLSerializer();  
//    var stream = {  
//      close : function()  
//      {  
//      },  
//      flush : function()  
//      {  
//    	  ss="";
//      },  
//      write : function(dd, count)  
//      {  
//        ss=ss.concat(dd);  
//      }  
//    };  
//    
//	s.serializeToStream(((new DOMParser).parseFromString(string, "application/xml")).documentElement,stream, "UTF-8");  
	
	
	//var xfdlToSign=new XMLSerializer().serializeToString(oDocument)
   // console.log(xfdlToSign);
	
	var dom=new DOMParser().parseFromString(xfdl, "text/xml");
	var xfdlFromDOM=new XMLSerializer().serializeToString(dom)
	
	var xfdlToSign=string;
	var signedXfdl=document.signApplet.createSignerForStreamByString(xfdlFromDOM,'PAGE1.BUTTON1');
	var signedXfdl=document.signApplet.getRandom();
    console.log(signedXfdl);
   

    $(button).removeAttr('signed');
	$(button).attr('signed','true');
	$("#sign_do_sign").attr('disabled', 'disabled');
	$("#sign_label").text("Signed"+signedXfdl);
	if($(button).attr("deletable") == 'true')
		$("#sign_do_del").removeAttr('disabled');

};

function deleteSign()
{
	var buttonSid = $("#sign_div_background").attr('sign_button_sid');
	if(buttonSid == '' || buttonSid == 'undefined')
	{
		alert('ERROR');
		return;
	}
	var button = $("[sid="+buttonSid+"]");
	var sid=button.attr("sid");
	
	
	//delete sign from xfdl
	
	
	$(button).removeAttr('signed');
	$(button).attr('signed','fasle');
	$("#sign_do_sign").removeAttr('disabled');
	$("#sign_label").text("Unsigned");
	$("#sign_do_del").attr('disabled', 'disabled');
};










var xfdl="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<XFDL xmlns:custom=\"http://www.ibm.com/xmlns/prod/XFDL/Custom\" xmlns:designer=\"http://www.ibm.com/xmlns/prod/workplace/forms/designer/2.6\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xml:lang=\"ru-RU\" xmlns:xfdl=\"http://www.ibm.com/xmlns/prod/XFDL/7.5\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns=\"http://www.ibm.com/xmlns/prod/XFDL/7.5\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
"<globalpage sid=\"global\">"+
   "<global sid=\"global\">"+
      "<ufv_settings>"+
           "<menu>"+
            "<save>hidden</save>"+
"               <open>hidden</open>"+
            "<mail>on</mail>"+
"               <preferences>hidden</preferences>"+
            "<spellcheck>hidden</spellcheck>"+
"               <spellcheckall>hidden</spellcheckall>"+
            "<help>hidden</help>"+
"               <viewerhelp>hidden</viewerhelp>"+
            "<zoom>hidden</zoom>"+
"               <about>hidden</about>"+
            "<fontdialog>hidden</fontdialog>"+
"               <paragraphdialog>hidden</paragraphdialog>"+
           "</menu>"+
            "<pagedonewithformaterrors>permit</pagedonewithformaterrors>"+
"               <submitwithformaterrors>permit</submitwithformaterrors>"+
            "<signwithformaterrors>deny</signwithformaterrors> "+
        "</ufv_settings>"+
      "<designer:date>20120116</designer:date>"+
"         <formid>"+
         "<title>Платежное поручение</title>"+
"            <serialnumber>79AEF401233142A6:4F8150DA:134E54A36D0:-8000</serialnumber>"+
         "<version>3.46.1</version>"+
      "</formid>"+
"         <designer:version>3.0.0.130</designer:version>"+
      "<xformsmodels>"+
         "<xforms:model>"+
            "<xforms:instance id=\"INSTANCE\" xmlns=\"\">"+
               "<data>"+
                  "<formname>Платежное поручение</formname>"+
"                     <P_1>ПЛАТЕЖНОЕ ПОРУЧЕНИЕ</P_1>"+
                  "<P_2>0401060</P_2>"+
"                     <P_3></P_3>"+
                  "<P_4></P_4>"+
"                     <P_5></P_5>"+
                  "<P_6></P_6>"+
"                     <P_7></P_7>"+
                  "<P_8></P_8>"+
"                     <P_9></P_9>"+
                  "<P_10></P_10>"+
"                     <P_11></P_11>"+
                  "<P_12></P_12>"+
"                     <P_13></P_13>"+
                  "<P_14></P_14>"+
"                     <P_15></P_15>"+
                  "<P_16></P_16>"+
"                     <P_17></P_17>"+
                  "<P_18></P_18>"+
"                     <P_19></P_19>"+
                  "<P_20></P_20>"+
"                     <P_21></P_21>"+
                  "<P_22></P_22>"+
"                     <P_23></P_23>"+
                  "<P_24></P_24>"+
"                     <P_43></P_43>"+
                  "<P_44></P_44>"+
"                     <P_45></P_45>"+
                  "<P_62></P_62>"+
"                     <P_71></P_71>"+
                  "<P_60></P_60>"+
"                     <P_61></P_61>"+
                  "<P_101></P_101>"+
"                     <P_102></P_102>"+
                  "<P_103></P_103>"+
"                     <P_104></P_104>"+
                  "<P_105></P_105>"+
"                     <P_106></P_106>"+
                  "<P_107></P_107>"+
"                     <P_108></P_108>"+
                  "<P_109></P_109>"+
"                     <P_110></P_110>"+
                  "<fio_face1></fio_face1>"+
"                     <fio_face2></fio_face2>"+
               "</data>"+
            "</xforms:instance>"+
         "</xforms:model>"+
      "</xformsmodels>"+
   "</global>"+
"</globalpage>"+
"   <page sid=\"PAGE1\">"+
   "<global sid=\"global\">"+
      "<label>PAGE1</label>"+
   "</global>"+
"      <toolbar sid=\"TOOLBAR\">"+
      "<designer:height>63</designer:height>"+
   "</toolbar>"+
"      <button sid=\"exit1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
"            <x>751</x>"+
         "<y>15</y>"+
"            <width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
"         <value>Отмена / Закрыть</value>"+
      "<type>done</type>"+
"         <url>forms/cancel.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
"         <transmititemrefs>"+
         "<filter>keep</filter>"+
"            <itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
   "</button>"+
"      <line sid=\"LINE1\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>67</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
   "</line>"+
"      <label sid=\"LABEL3\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>67</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <value>Поступ. в банк прат.</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>6</size>"+
      "</fontinfo>"+
"         <justify>center</justify>"+
   "</label>"+
"      <line sid=\"LINE2\">"+
      "<itemlocation>"+
         "<x>246</x>"+
"            <y>67</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
   "</line>"+
"      <label sid=\"LABEL4\">"+
      "<itemlocation>"+
         "<x>246</x>"+
"            <y>67</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <value>Списано со сч. плат.</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>6</size>"+
      "</fontinfo>"+
"         <justify>center</justify>"+
   "</label>"+
"      <label sid=\"LABEL5\">"+
      "<itemlocation>"+
         "<x>226</x>"+
"            <y>105</y>"+
      "</itemlocation>"+
"         <value>N</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
"      <line sid=\"LINE3\">"+
      "<itemlocation>"+
         "<x>306</x>"+
"            <y>127</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
   "</line>"+
"      <label sid=\"LABEL6\">"+
      "<itemlocation>"+
         "<x>306</x>"+
"            <y>127</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <value>Дата</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>6</size>"+
      "</fontinfo>"+
"         <justify>center</justify>"+
   "</label>"+
"      <line sid=\"LINE4\">"+
      "<itemlocation>"+
         "<x>496</x>"+
"            <y>127</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
   "</line>"+
"      <label sid=\"LABEL7\">"+
      "<itemlocation>"+
         "<x>496</x>"+
"            <y>127</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <value>Вид платежа</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>6</size>"+
      "</fontinfo>"+
"         <justify>center</justify>"+
   "</label>"+
"      <line sid=\"LINE5\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>916</x>"+
"            <y>105</y>"+
         "<height>40</height>"+
      "</itemlocation>"+
   "</line>"+
"      <spacer sid=\"vfd_spacer\">"+
      "<itemlocation>"+
         "<x>960</x>"+
"            <y>1260</y>"+
         "<width>1</width>"+
"            <height>1</height>"+
      "</itemlocation>"+
   "</spacer>"+
"      <label sid=\"LABEL1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
"            <x>10</x>"+
         "<y>8</y>"+
      "</itemlocation>"+
"         <value>Платежное поручение</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
"      <label sid=\"LABEL2\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
"            <x>10</x>"+
         "<y>35</y>"+
      "</itemlocation>"+
"         <value>Версия  3.46.1</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>6</size>"+
      "</fontinfo>"+
   "</label>"+
"      <button sid=\"save\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
"            <x>470</x>"+
         "<y>15</y>"+
"            <width>130</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
"         <value>Сохранить</value>"+
      "<type>replace</type>"+
"         <url>javascript:saveandnoexit()</url>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') +. set('global.global.dirtyflag','off') : ''\"></custom:option>"+
   "</button>"+
"      <button sid=\"save_exit1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
"            <x>599</x>"+
         "<y>15</y>"+
"            <width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
"         <type>done</type>"+
      "<value>Сохранить и выйти</value>"+
"         <url>forms/save.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
"         <transmititemrefs>"+
         "<filter>keep</filter>"+
"            <itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
"         <custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') : ''\"></custom:option>"+
      "<saveformat>application/vnd.xfdl;content-encoding=base64-gzip</saveformat>"+
"         <custom:option1></custom:option1>"+
      "<!--<custom:option2 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON2.signer>''? set ('PAGE1.LABEL28.value','1'): ''\"></custom:option2>"+
"         <custom:option3 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON4.signer>''? set ('PAGE1.LABEL29.value','1'): ''\"></custom:option3>"+
      "<custom:option4 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON5.signer>''? set ('PAGE1.LABEL30.value','1'): ''\"></custom:option4>"+
"         <custom:option5 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON6.signer>''? set ('PAGE1.LABEL31.value','1'): ''\"></custom:option5>"+
      "<custom:option6 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON7.signer>''? set ('PAGE1.LABEL32.value','1'): ''\"></custom:option6>"+
"         <custom:option7 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON8.signer>''? set ('PAGE1.LABEL33.value','1'): ''\"></custom:option7>"+
      "<custom:option8 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON9.signer>''? set ('PAGE1.LABEL34.value','1'): ''\"></custom:option8>"+
"         <custom:option9 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON10.signer>''? set ('PAGE1.LABEL35.value','1'): ''\"></custom:option9>"+
      "<custom:option10 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON11.signer>''? set ('PAGE1.LABEL36.value','1'): ''\"></custom:option10>"+
"         <custom:option11 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON12.signer>''? set ('PAGE1.LABEL37.value','1'): ''\"></custom:option11>"+
      "<custom:option12 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON13.signer>''? set ('PAGE1.LABEL38.value','1'): ''\"></custom:option12>"+
"         <custom:option13 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON14.signer>''? set ('PAGE1.LABEL39.value','1'): ''\"></custom:option13>"+
      "<custom:option14 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON15.signer>''? set ('PAGE1.LABEL40.value','1'): ''\"></custom:option14>"+
"         <custom:option15 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON16.signer>''? set ('PAGE1.LABEL41.value','1'): ''\"></custom:option15>"+
      "<custom:option16 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON17.signer>''? set ('PAGE1.LABEL42.value','1'): ''\"></custom:option16>"+
"         <custom:option17 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON18.signer>''? set ('PAGE1.LABEL43.value','1'): ''\"></custom:option17>"+
      "<custom:option18 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON19.signer>''? set ('PAGE1.LABEL44.value','1'): ''\"></custom:option18>"+
"         <custom:option19 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON20.signer>''? set ('PAGE1.LABEL45.value','1'): ''\"></custom:option19>"+
      "<custom:option20 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON21.signer>''? set ('PAGE1.LABEL46.value','1'): ''\"></custom:option20>"+
"         <custom:option21 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON22.signer>''? set ('PAGE1.LABEL47.value','1'): ''\"></custom:option21>-->"+
      "<custom:option3></custom:option3>"+
"         <custom:option4></custom:option4>"+
   "</button>"+
"      <line sid=\"LINE6\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>956</x>"+
"            <y>105</y>"+
         "<height>40</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE7\">"+
      "<itemlocation>"+
         "<x>916</x>"+
"            <y>105</y>"+
         "<width>40</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE8\">"+
      "<itemlocation>"+
         "<x>916</x>"+
"            <y>144</y>"+
         "<width>40</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE9\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>956</x>"+
"            <y>45</y>"+
         "<height>31</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE10\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>886</x>"+
"            <y>45</y>"+
         "<height>31</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE11\">"+
      "<itemlocation>"+
         "<x>886</x>"+
"            <y>45</y>"+
         "<width>71</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE12\">"+
      "<itemlocation>"+
         "<x>886</x>"+
"            <y>75</y>"+
         "<width>71</width>"+
      "</itemlocation>"+
   "</line>"+
"      <label sid=\"LABEL8\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>165</y>"+
      "</itemlocation>"+
"         <value>Сумма "+
"прописью</value>"+
   "</label>"+
"      <line sid=\"LINE13\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>240</y>"+
         "<width>900</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE34\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>690</y>"+
         "<width>900</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE35\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>715</y>"+
         "<width>900</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE42\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>865</y>"+
         "<width>900</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE14\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>156</x>"+
"            <y>165</y>"+
         "<height>75</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE32\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>731</x>"+
"            <y>615</y>"+
         "<height>75</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE33\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>831</x>"+
"            <y>615</y>"+
         "<height>75</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE25\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>306</x>"+
"            <y>240</y>"+
         "<height>36</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE36\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>281</x>"+
"            <y>690</y>"+
         "<height>25</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE37\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>431</x>"+
"            <y>690</y>"+
         "<height>25</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE38\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>481</x>"+
"            <y>690</y>"+
         "<height>25</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE27\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>306</x>"+
"            <y>540</y>"+
         "<height>36</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE15\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>556</x>"+
"            <y>240</y>"+
         "<height>450</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE18\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>631</x>"+
"            <y>240</y>"+
         "<height>450</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE16\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>275</y>"+
         "<width>500</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE26\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>575</y>"+
         "<width>500</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE20\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>389</y>"+
         "<width>575</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE22\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>540</y>"+
         "<width>575</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE23\">"+
      "<itemlocation>"+
         "<x>556</x>"+
"            <y>490</y>"+
         "<width>75</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE28\">"+
      "<itemlocation>"+
         "<x>556</x>"+
"            <y>640</y>"+
         "<width>75</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE30\">"+
      "<itemlocation>"+
         "<x>731</x>"+
"            <y>640</y>"+
         "<width>100</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE31\">"+
      "<itemlocation>"+
         "<x>731</x>"+
"            <y>665</y>"+
         "<width>100</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE29\">"+
      "<itemlocation>"+
         "<x>556</x>"+
"            <y>665</y>"+
         "<width>75</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE21\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>465</y>"+
         "<width>900</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE17\">"+
      "<itemlocation>"+
         "<x>556</x>"+
"            <y>315</y>"+
         "<width>400</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE24\">"+
      "<itemlocation>"+
         "<x>556</x>"+
"            <y>615</y>"+
         "<width>400</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE19\">"+
      "<itemlocation>"+
         "<x>556</x>"+
"            <y>415</y>"+
         "<width>75</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE39\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>606</x>"+
"            <y>690</y>"+
         "<height>25</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE40\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>781</x>"+
"            <y>690</y>"+
         "<height>25</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE41\">"+
      "<size>"+
         "<height>1</height>"+
"            <width>0</width>"+
      "</size>"+
"         <itemlocation>"+
         "<x>906</x>"+
"            <y>690</y>"+
         "<height>25</height>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE43\">"+
      "<itemlocation>"+
         "<x>296</x>"+
"            <y>940</y>"+
         "<width>300</width>"+
      "</itemlocation>"+
   "</line>"+
"      <line sid=\"LINE44\">"+
      "<itemlocation>"+
         "<x>296</x>"+
"            <y>1015</y>"+
         "<width>300</width>"+
      "</itemlocation>"+
   "</line>"+
"      <label sid=\"LABEL9\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>245</y>"+
      "</itemlocation>"+
"         <value>ИНН</value>"+
   "</label>"+
"      <label sid=\"LABEL10\">"+
      "<itemlocation>"+
         "<x>311</x>"+
"            <y>245</y>"+
      "</itemlocation>"+
"         <value>КПП</value>"+
   "</label>"+
"      <label sid=\"LABEL18\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>545</y>"+
      "</itemlocation>"+
"         <value>ИНН</value>"+
   "</label>"+
"      <label sid=\"LABEL19\">"+
      "<itemlocation>"+
         "<x>311</x>"+
"            <y>545</y>"+
      "</itemlocation>"+
"         <value>КПП</value>"+
   "</label>"+
"      <label sid=\"LABEL11\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>245</y>"+
      "</itemlocation>"+
"         <value>Самма</value>"+
   "</label>"+
"      <label sid=\"LABEL20\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>365</y>"+
      "</itemlocation>"+
"         <value>Плательщик</value>"+
   "</label>"+
"      <label sid=\"LABEL21\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>440</y>"+
      "</itemlocation>"+
"         <value>Банк плательщика</value>"+
   "</label>"+
"      <label sid=\"LABEL22\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>515</y>"+
      "</itemlocation>"+
"         <value>Банк получателя</value>"+
   "</label>"+
"      <label sid=\"LABEL23\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>665</y>"+
      "</itemlocation>"+
"         <value>Получатель</value>"+
   "</label>"+
"      <label sid=\"LABEL30\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>840</y>"+
      "</itemlocation>"+
"         <value>Назначение платежа</value>"+
   "</label>"+
"      <label sid=\"LABEL33\">"+
      "<itemlocation>"+
         "<x>166</x>"+
"            <y>955</y>"+
      "</itemlocation>"+
"         <value>М.П.</value>"+
   "</label>"+
"      <label sid=\"LABEL31\">"+
      "<itemlocation>"+
         "<x>296</x>"+
"            <y>870</y>"+
         "<width>301</width>"+
      "</itemlocation>"+
"         <value>Подписи</value>"+
      "<justify>center</justify>"+
   "</label>"+
"      <label sid=\"LABEL32\">"+
      "<itemlocation>"+
         "<x>606</x>"+
"            <y>870</y>"+
         "<width>351</width>"+
      "</itemlocation>"+
"         <value>Отметки банка</value>"+
      "<justify>center</justify>"+
   "</label>"+
"      <label sid=\"LABEL12\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>325</y>"+
      "</itemlocation>"+
"         <value>Сч. N</value>"+
   "</label>"+
"      <label sid=\"LABEL13\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>391</y>"+
      "</itemlocation>"+
"         <value>БИК</value>"+
   "</label>"+
"      <label sid=\"LABEL15\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>467</y>"+
      "</itemlocation>"+
"         <value>БИК</value>"+
   "</label>"+
"      <label sid=\"LABEL16\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>497</y>"+
      "</itemlocation>"+
"         <value>Сч. N</value>"+
   "</label>"+
"      <label sid=\"LABEL17\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>545</y>"+
      "</itemlocation>"+
"         <value>Сч. N</value>"+
   "</label>"+
"      <label sid=\"LABEL24\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>617</y>"+
      "</itemlocation>"+
"         <value>Вид оп.</value>"+
   "</label>"+
"      <label sid=\"LABEL27\">"+
      "<itemlocation>"+
         "<x>736</x>"+
"            <y>617</y>"+
      "</itemlocation>"+
"         <value>Срок плат.</value>"+
   "</label>"+
"      <label sid=\"LABEL28\">"+
      "<itemlocation>"+
         "<x>736</x>"+
"            <y>642</y>"+
      "</itemlocation>"+
"         <value>Очер. плат.</value>"+
   "</label>"+
"      <label sid=\"LABEL29\">"+
      "<itemlocation>"+
         "<x>736</x>"+
"            <y>667</y>"+
      "</itemlocation>"+
"         <value>Рез. поле</value>"+
   "</label>"+
"      <label sid=\"LABEL14\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>421</y>"+
      "</itemlocation>"+
"         <value>Сч. N</value>"+
   "</label>"+
"      <label sid=\"LABEL25\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>642</y>"+
      "</itemlocation>"+
"         <value>Наз. пл.</value>"+
   "</label>"+
"      <label sid=\"LABEL26\">"+
      "<itemlocation>"+
         "<x>560</x>"+
"            <y>667</y>"+
      "</itemlocation>"+
"         <value>Код</value>"+
   "</label>"+
"      <field sid=\"FIELD1\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_62\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>56</x>"+
"            <y>45</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<message>Формат данных - ДД.ММ.ГГГГ или число указывается цифрами, месяц - прописью, год - цифрами (полностью). Например: 05.12.2006 или 05 декабря 2006.</message>"+
"               <patterns>"+
               "<pattern>((([0][1-9])|([1-2][0-9])|([3][0-1]))[.](([0][1-9])|([1][0-2]))[.]\d{4})|((([0][1-9])|([1-2][0-9])|([3][0-1]))[ ]((января)|(февраля)|(марта)|(апреля)|(мая)|(июня)|(июля)|(августа)|(сентября)|(октября)|(ноября)|(декабря))[ ]\d{4})</pattern>"+
            "</patterns>"+
         "</constraints>"+
      "</format>"+
   "</field>"+
"      <field sid=\"FIELD2\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_71\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>246</x>"+
"            <y>45</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<message>Формат данных - ДД.ММ.ГГГГ или число указывается цифрами, месяц - прописью, год - цифрами (полностью). Например: 05.12.2006 или 05 декабря 2006.</message>"+
"               <patterns>"+
               "<pattern>((([0][1-9])|([1-2][0-9])|([3][0-1]))[.](([0][1-9])|([1][0-2]))[.]\d{4})|((([0][1-9])|([1-2][0-9])|([3][0-1]))[ ]((января)|(февраля)|(марта)|(апреля)|(мая)|(июня)|(июля)|(августа)|(сентября)|(октября)|(ноября)|(декабря))[ ]\d{4})</pattern>"+
            "</patterns>"+
         "</constraints>"+
      "</format>"+
   "</field>"+
"      <field sid=\"FIELD3\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_3\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>246</x>"+
"            <y>105</y>"+
         "<width>51</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<message>Только численные значения. Максимум 3 знака.</message>"+
"               <patterns>"+
               "<pattern>\d*</pattern>"+
            "</patterns>"+
"               <length>"+
               "<min>0</min>"+
"                  <max>3</max>"+
            "</length>"+
         "</constraints>"+
      "</format>"+
   "</field>"+
"      <field sid=\"FIELD4\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_4\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>306</x>"+
"            <y>105</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<message>Формат данных - ДД.ММ.ГГГГ или число указывается цифрами, месяц - прописью, год - цифрами (полностью). Например: 05.12.2006 или 05 декабря 2006.</message>"+
"               <patterns>"+
               "<pattern>((([0][1-9])|([1-2][0-9])|([3][0-1]))[.](([0][1-9])|([1][0-2]))[.]\d{4})|((([0][1-9])|([1-2][0-9])|([3][0-1]))[ ]((января)|(февраля)|(марта)|(апреля)|(мая)|(июня)|(июля)|(августа)|(сентября)|(октября)|(ноября)|(декабря))[ ]\d{4})</pattern>"+
            "</patterns>"+
         "</constraints>"+
      "</format>"+
   "</field>"+
"      <field sid=\"FIELD5\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_5\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>496</x>"+
"            <y>105</y>"+
         "<width>175</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<patterns>"+
               "<pattern>(почтой)|(телеграфом)|(электронно)</pattern>"+
            "</patterns>"+
         "</constraints>"+
      "</format>"+
   "</field>"+
"      <field sid=\"FIELD6\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_101\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>916</x>"+
"            <y>115</y>"+
         "<width>41</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD7\">"+
      "<itemlocation>"+
         "<x>161</x>"+
"            <y>165</y>"+
         "<width>796</width>"+
"            <height>71</height>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <xforms:textarea ref=\"instance('INSTANCE')/P_6\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
   "</field>"+
"      <field sid=\"FIELD8\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_60\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>96</x>"+
"            <y>245</y>"+
         "<width>210</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<message>Только численные значения. 10 знаков или 12 знаков.</message>"+
"               <patterns>"+
               "<pattern>(\d{10})|(\d{12})</pattern>"+
            "</patterns>"+
         "</constraints>"+
      "</format>"+
   "</field>"+
"      <field sid=\"FIELD9\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_102\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>346</x>"+
"            <y>245</y>"+
         "<width>210</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD10\">"+
      "<itemlocation>"+
         "<x>636</x>"+
"            <y>245</y>"+
         "<width>321</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<message>Только численные значения. Например: \"28-10\" или \"140 =\" </message>"+
"               <patterns>"+
               "<pattern>(\d*[-]\d{2})|(\d*[=])</pattern>"+
            "</patterns>"+
         "</constraints>"+
      "</format>"+
"         <xforms:input ref=\"instance('INSTANCE')/P_7\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
   "</field>"+
"      <field sid=\"FIELD11\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>280</y>"+
         "<width>500</width>"+
"            <height>76</height>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <xforms:textarea ref=\"instance('INSTANCE')/P_8\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
   "</field>"+
"      <field sid=\"FIELD12\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_9\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>325</y>"+
         "<width>321</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD13\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_10\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>56</x>"+
"            <y>395</y>"+
         "<width>500</width>"+
"            <height>41</height>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD14\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_11\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>391</y>"+
         "<width>321</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD15\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_12\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>421</y>"+
         "<width>321</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD16\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_13\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>56</x>"+
"            <y>471</y>"+
         "<width>500</width>"+
"            <height>41</height>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD17\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_14\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>467</y>"+
         "<width>321</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD18\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_15\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>497</y>"+
         "<width>321</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD19\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_61\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>96</x>"+
"            <y>545</y>"+
         "<width>210</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <format>"+
         "<datatype>string</datatype>"+
"            <constraints>"+
            "<message>Только численные значения. 10 знаков или 12 знаков.</message>"+
"               <patterns>"+
               "<pattern>(\d{10})|(\d{12})</pattern>"+
            "</patterns>"+
         "</constraints>"+
      "</format>"+
   "</field>"+
"      <field sid=\"FIELD20\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_103\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>346</x>"+
"            <y>545</y>"+
         "<width>210</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD21\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_17\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>545</y>"+
         "<width>321</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD22\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>585</y>"+
         "<width>500</width>"+
"            <height>76</height>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <xforms:textarea ref=\"instance('INSTANCE')/P_16\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
   "</field>"+
"      <field sid=\"FIELD23\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_18\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>615</y>"+
         "<width>91</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD24\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_20\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>642</y>"+
         "<width>91</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD25\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_22\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>636</x>"+
"            <y>667</y>"+
         "<width>91</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD26\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_19\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>836</x>"+
"            <y>615</y>"+
         "<width>121</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD27\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_21\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>836</x>"+
"            <y>642</y>"+
         "<width>121</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD28\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_23\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>836</x>"+
"            <y>667</y>"+
         "<width>121</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD29\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_104\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>56</x>"+
"            <y>692</y>"+
         "<width>225</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD30\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_105\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>281</x>"+
"            <y>692</y>"+
         "<width>151</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD31\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_106\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>431</x>"+
"            <y>692</y>"+
         "<width>51</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD32\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_107\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>481</x>"+
"            <y>692</y>"+
         "<width>126</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD33\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_108\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>606</x>"+
"            <y>692</y>"+
         "<width>176</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD34\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_109\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>781</x>"+
"            <y>692</y>"+
         "<width>126</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD35\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_110\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>906</x>"+
"            <y>692</y>"+
         "<width>51</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD36\">"+
      "<itemlocation>"+
         "<x>56</x>"+
"            <y>725</y>"+
         "<width>901</width>"+
"            <height>111</height>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <xforms:textarea ref=\"instance('INSTANCE')/P_24\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
   "</field>"+
"      <field sid=\"FIELD39\">"+
      "<itemlocation>"+
         "<x>606</x>"+
"            <y>895</y>"+
         "<width>351</width>"+
"            <height>121</height>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <xforms:textarea ref=\"instance('INSTANCE')/P_45\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
   "</field>"+
"      <field sid=\"FIELD38\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_43\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>96</x>"+
"            <y>980</y>"+
         "<width>191</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
"      <field sid=\"FIELD41\">"+
      "<xforms:input ref=\"instance('INSTANCE')/fio_face1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>216</x>"+
"            <y>895</y>"+
         "<width>41</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <visible>off</visible>"+
      "<printvisible>off</printvisible>"+
   "</field>"+
"      <field sid=\"FIELD42\">"+
      "<xforms:input ref=\"instance('INSTANCE')/fio_face2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>216</x>"+
"            <y>925</y>"+
         "<width>41</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <visible>off</visible>"+
      "<printvisible>off</printvisible>"+
   "</field>"+
"      <field sid=\"FIELD37\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>886</x>"+
"            <y>50</y>"+
         "<width>70</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
"         <readonly>on</readonly>"+
   "</field>"+
"      <field sid=\"FIELD40\">"+
      "<xforms:input ref=\"instance('INSTANCE')/P_1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
"         <itemlocation>"+
         "<x>56</x>"+
"            <y>105</y>"+
         "<width>171</width>"+
      "</itemlocation>"+
"         <scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
"         <justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
"            <size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
"         <readonly>on</readonly>"+
   "</field>"+
"      <button sid=\"BUTTON1\">"+
      "<itemlocation>"+
         "<x>296</x>"+
"            <y>895</y>"+
         "<width>301</width>"+
"            <height>41</height>"+
      "</itemlocation>"+
"         <value compute=\"signer=='' ? 'Добавить подпись' : signer\">Добавить подпись</value>"+
      "<type>signature</type>"+
"         <signer></signer>"+
      "<signformat>application/vnd.xfdl;engine=\"CryptoAPI\";csp=\"Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider\";csptype=75;delete=\"off\";layoutoverlaptest=\"none\"</signformat>"+
"         <custom:option xfdl:compute=\"toggle(signer) == '1' &#xD;&#xA;"+
"? signer == '' &#xD;&#xA;"+
    "? set('signer', '') &#xD;&#xA;"+
"       : tolower(signer) != 'invalid'&#xD;&#xA;"+
        "? set('signer', substr(signer, '0', strstr(signer, ',')-'1'))&#xD;&#xA;"+
"           &#xD;&#xA;"+
        ": ''&#xD;&#xA;"+
": ''\"></custom:option>"+
      "<custom:option1></custom:option1>"+
"         <signature>BUTTON1_SIGNATURE_1664820190</signature>"+
      "<size>"+
         "<width>40</width>"+
"            <height>1</height>"+
      "</size>"+
"         <signitemrefs>"+
         "<filter>keep</filter>"+
"            <itemref>PAGE1.FIELD1</itemref>"+
         "<itemref>PAGE1.FIELD2</itemref>"+
"            <itemref>PAGE1.FIELD3</itemref>"+
         "<itemref>PAGE1.FIELD4</itemref>"+
"            <itemref>PAGE1.FIELD5</itemref>"+
         "<itemref>PAGE1.FIELD6</itemref>"+
"            <itemref>PAGE1.FIELD7</itemref>"+
         "<itemref>PAGE1.FIELD8</itemref>"+
"            <itemref>PAGE1.FIELD9</itemref>"+
         "<itemref>PAGE1.FIELD10</itemref>"+
"            <itemref>PAGE1.FIELD11</itemref>"+
         "<itemref>PAGE1.FIELD12</itemref>"+
"            <itemref>PAGE1.FIELD13</itemref>"+
         "<itemref>PAGE1.FIELD14</itemref>"+
"            <itemref>PAGE1.FIELD15</itemref>"+
         "<itemref>PAGE1.FIELD16</itemref>"+
"            <itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD18</itemref>"+
"            <itemref>PAGE1.FIELD19</itemref>"+
         "<itemref>PAGE1.FIELD20</itemref>"+
"            <itemref>PAGE1.FIELD21</itemref>"+
         "<itemref>PAGE1.FIELD22</itemref>"+
"            <itemref>PAGE1.FIELD23</itemref>"+
         "<itemref>PAGE1.FIELD24</itemref>"+
"            <itemref>PAGE1.FIELD25</itemref>"+
         "<itemref>PAGE1.FIELD26</itemref>"+
"            <itemref>PAGE1.FIELD27</itemref>"+
         "<itemref>PAGE1.FIELD28</itemref>"+
"            <itemref>PAGE1.FIELD29</itemref>"+
         "<itemref>PAGE1.FIELD30</itemref>"+
"            <itemref>PAGE1.FIELD31</itemref>"+
         "<itemref>PAGE1.FIELD32</itemref>"+
"            <itemref>PAGE1.FIELD33</itemref>"+
         "<itemref>PAGE1.FIELD34</itemref>"+
"            <itemref>PAGE1.FIELD35</itemref>"+
         "<itemref>PAGE1.FIELD36</itemref>"+
"            <itemref>PAGE1.FIELD39</itemref>"+
         "<itemref>PAGE1.FIELD38</itemref>"+
      "</signitemrefs>"+
"         <signinstance>"+
         "<filter>keep</filter>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_62</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_71</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_3</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_4</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_5</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_101</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_6</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_60</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_102</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_7</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_8</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_9</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_10</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_11</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_12</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_13</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_14</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_15</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_61</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_103</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_17</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_16</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_18</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_20</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_22</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_19</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_21</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_23</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_104</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_105</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_106</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_107</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_108</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_109</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_110</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_24</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_45</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_43</ref>"+
         "</dataref>"+
      "</signinstance>"+
"         <signoptions>"+
         "<filter>omit</filter>"+
"            <optiontype>triggeritem</optiontype>"+
         "<optiontype>coordinates</optiontype>"+
      "</signoptions>"+
   "</button>"+
"      <button sid=\"BUTTON2\">"+
      "<itemlocation>"+
         "<x>296</x>"+
"            <y>970</y>"+
         "<width>301</width>"+
"            <height>41</height>"+
      "</itemlocation>"+
"         <value compute=\"signer=='' ? 'Добавить подпись' : signer\">Добавить подпись</value>"+
      "<type>signature</type>"+
"         <signer></signer>"+
      "<signformat>application/vnd.xfdl;engine=\"CryptoAPI\";csp=\"Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider\";csptype=75;delete=\"off\";layoutoverlaptest=\"none\"</signformat>"+
"         <custom:option xfdl:compute=\"toggle(signer) == '1' &#xD;&#xA;"+
"? signer == '' &#xD;&#xA;"+
    "? set('signer', '') &#xD;&#xA;"+
"       : tolower(signer) != 'invalid'&#xD;&#xA;"+
        "? set('signer', substr(signer, '0', strstr(signer, ',')-'1'))&#xD;&#xA;"+
"           &#xD;&#xA;"+
        ": ''&#xD;&#xA;"+
": ''\"></custom:option>"+
      "<custom:option1></custom:option1>"+
"         <size>"+
         "<width>40</width>"+
"            <height>1</height>"+
      "</size>"+
"         <signature>BUTTON2_SIGNATURE_1045176175</signature>"+
      "<signitemrefs>"+
         "<filter>keep</filter>"+
"            <itemref>PAGE1.FIELD1</itemref>"+
         "<itemref>PAGE1.FIELD2</itemref>"+
"            <itemref>PAGE1.FIELD3</itemref>"+
         "<itemref>PAGE1.FIELD4</itemref>"+
"            <itemref>PAGE1.FIELD5</itemref>"+
         "<itemref>PAGE1.FIELD6</itemref>"+
"            <itemref>PAGE1.FIELD7</itemref>"+
         "<itemref>PAGE1.FIELD8</itemref>"+
"            <itemref>PAGE1.FIELD9</itemref>"+
         "<itemref>PAGE1.FIELD10</itemref>"+
"            <itemref>PAGE1.FIELD11</itemref>"+
         "<itemref>PAGE1.FIELD12</itemref>"+
"            <itemref>PAGE1.FIELD13</itemref>"+
         "<itemref>PAGE1.FIELD14</itemref>"+
"            <itemref>PAGE1.FIELD15</itemref>"+
         "<itemref>PAGE1.FIELD16</itemref>"+
"            <itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD18</itemref>"+
"            <itemref>PAGE1.FIELD19</itemref>"+
         "<itemref>PAGE1.FIELD20</itemref>"+
"            <itemref>PAGE1.FIELD21</itemref>"+
         "<itemref>PAGE1.FIELD22</itemref>"+
"            <itemref>PAGE1.FIELD23</itemref>"+
         "<itemref>PAGE1.FIELD24</itemref>"+
"            <itemref>PAGE1.FIELD25</itemref>"+
         "<itemref>PAGE1.FIELD26</itemref>"+
"            <itemref>PAGE1.FIELD27</itemref>"+
         "<itemref>PAGE1.FIELD28</itemref>"+
"            <itemref>PAGE1.FIELD29</itemref>"+
         "<itemref>PAGE1.FIELD30</itemref>"+
"            <itemref>PAGE1.FIELD31</itemref>"+
         "<itemref>PAGE1.FIELD32</itemref>"+
"            <itemref>PAGE1.FIELD33</itemref>"+
         "<itemref>PAGE1.FIELD34</itemref>"+
"            <itemref>PAGE1.FIELD35</itemref>"+
         "<itemref>PAGE1.FIELD36</itemref>"+
"            <itemref>PAGE1.FIELD39</itemref>"+
         "<itemref>PAGE1.FIELD38</itemref>"+
      "</signitemrefs>"+
"         <signinstance>"+
         "<filter>keep</filter>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_62</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_71</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_3</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_4</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_5</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_101</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_6</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_60</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_102</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_7</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_8</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_9</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_10</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_11</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_12</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_13</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_14</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_15</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_61</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_103</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_17</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_16</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_18</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_20</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_22</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_19</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_21</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_23</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_104</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_105</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_106</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_107</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_108</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_109</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_110</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_24</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_45</ref>"+
         "</dataref>"+
"            <dataref>"+
            "<model></model>"+
"               <ref>instance('INSTANCE')/P_43</ref>"+
         "</dataref>"+
      "</signinstance>"+
"         <signoptions>"+
         "<filter>omit</filter>"+
"            <optiontype>triggeritem</optiontype>"+
         "<optiontype>coordinates</optiontype>"+
      "</signoptions>"+
   "</button>"+
"</page>"+
"</XFDL>"


