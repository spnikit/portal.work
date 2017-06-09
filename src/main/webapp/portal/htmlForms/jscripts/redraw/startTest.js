/**
*  @include "main.js"
*  
*/

function startTest()
{
//	var dom=new DOMParser().parseFromString(xfdlString, "text/xml");
//	
//	var page = dom.getElementsByTagNameNS("http://www.ibm.com/xmlns/prod/XFDL/7.5", "page").item(0);
	
	var page = xfdlForm.getElementsByTagNameNS("http://www.ibm.com/xmlns/prod/XFDL/7.5", "page").item(0);
	
	parsePage(page);
	

//	var xfdlFromDOM=new XMLSerializer().serializeToString(dom)
	
	
	
//	var xfdlToSign=string;
//	var signedXfdl=document.signApplet.createSignerForStreamByString(xfdlFromDOM,'PAGE1.BUTTON1');
//	var signedXfdl=document.signApplet.getRandom();
 //   console.log(page);

}
var xfdlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<XFDL xmlns=\"http://www.ibm.com/xmlns/prod/XFDL/7.5\" xmlns:custom=\"http://www.ibm.com/xmlns/prod/XFDL/Custom\" xmlns:designer=\"http://www.ibm.com/xmlns/prod/workplace/forms/designer/2.6\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xfdl=\"http://www.ibm.com/xmlns/prod/XFDL/7.5\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"ru-RU\">"+
"<globalpage sid=\"global\">"+
   "<global sid=\"global\">"+
      "<ufv_settings>"+
           "<menu>"+
            "<!-- <save>hidden</save> -->"+
            "<open>hidden</open>"+
            "<mail>hidden</mail>"+
            "<preferences>hidden</preferences>"+
            "<spellcheck>hidden</spellcheck>"+
            "<spellcheckall>hidden</spellcheckall>"+
            "<help>hidden</help>"+
            "<viewerhelp>hidden</viewerhelp>"+
            "<zoom>hidden</zoom>"+
            "<about>hidden</about>"+
            "<fontdialog>hidden</fontdialog>"+
            "<paragraphdialog>hidden</paragraphdialog>"+
           "</menu>"+
            "<pagedonewithformaterrors>permit</pagedonewithformaterrors>"+
            "<submitwithformaterrors>permit</submitwithformaterrors>"+
            "<signwithformaterrors>deny</signwithformaterrors> "+
        "</ufv_settings>"+
      "<designer:date>20120123</designer:date>"+
      "<formid>"+
         "<title>Поручение на конверcию одной иностранной валюты на другую</title>"+
         "<serialnumber>79AEF401233142A6:-556BFB21:1350A921A39:-8000</serialnumber>"+
         "<version>3.46.3</version>"+
      "</formid>"+
      "<designer:version>3.0.0.130</designer:version>"+
      "<xformsmodels>"+
         "<xforms:model>"+
            "<xforms:instance xmlns=\"\" id=\"INSTANCE\">"+
               "<data>"+
                  "<formname>Поручение на конверcию одной иностранной валюты на другую</formname>"+
                  "<table1>"+
                     "<row>"+
                        "<Vid_dok></Vid_dok>"+
                        "<Nom_dok></Nom_dok>"+
                        "<Data_dok></Data_dok>"+
                        "<Prim></Prim>"+
                     "</row>"+
                  "</table1>"+
                  "<Nom1></Nom1>"+
                  "<Ot1></Ot1>"+
                  "<Naim_org></Naim_org>"+
                  "<Inn></Inn>"+
                  "<Fio_lica></Fio_lica>"+
                  "<Cod_OKPO></Cod_OKPO>"+
                  "<Tel></Tel>"+
                  "<Pros_poiz></Pros_poiz>"+
                  "<Ysl_post></Ysl_post>"+
                  "<Pros_sov></Pros_sov>"+
                  "<Sum_prod_val1></Sum_prod_val1>"+
                  "<Sun_prod_val2></Sun_prod_val2>"+
                  "<Por_spis></Por_spis>"+
                  "<Ob_pere></Ob_pere>"+
                  "<Nom2></Nom2><Kyp_val_pros1></Kyp_val_pros1>"+
                  "<Kyp_val_pros2></Kyp_val_pros2>"+
                  "<Na_nash1></Na_nash1>"+
                  "<Na_nash2></Na_nash2>"+
                  "<Vbanke></Vbanke>"+
                  "<Kom_isp1></Kom_isp1>"+
                  "<Kom_isp2></Kom_isp2>"+
                  "<Spis_nash></Spis_nash>"+
                  "<Dop_inf></Dop_inf>"+
                  "<pzd_pred></pzd_pred>"+
                  "<pzd_dor></pzd_dor>"+
                  "<Pril_spra></Pril_spra>"+
                  "<Ot2></Ot2>"+
                  "<Otm_Isp></Otm_Isp>"+
                  "<Val_kyp></Val_kyp>"+
                  "<Val_spis></Val_spis>"+
                  "<Data_val></Data_val>"+
                  "<Kyps_sdel></Kyps_sdel>"+
                  "<Sym_kom></Sym_kom>"+
                  "<V1></V1>"+
                    "<V2></V2>"+
                  "<Ot3></Ot3>"+
                  "<table2>"+
                     "<row>"+
                        "<Kod_val_pok></Kod_val_pok>"+
                        "<Sum_pok></Sum_pok>"+
                        "<Kpos_kyrs></Kpos_kyrs>"+
                        "<Sum-prod></Sum-prod>"+
                        "<Cod_val_prod></Cod_val_prod>"+
                     "</row>"+
"                       "+
                  "</table2>"+
                  "<Pechat></Pechat>"+
                  "<data1></data1>"+
                  "<data2></data2>"+
"                     "+
               "</data>"+
            "</xforms:instance>"+
         "</xforms:model>"+
      "</xformsmodels>"+
   "</global>"+
"</globalpage>"+
"<page sid=\"PAGE1\">"+
   "<global sid=\"global\">"+
      "<label>PAGE1</label>"+
      "<designer:pagesize>1000;1300</designer:pagesize>"+
   "</global>"+
   "<toolbar sid=\"TOOLBAR\">"+
      "<designer:height>79</designer:height>"+
   "</toolbar>"+
   "<button sid=\"save\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>519</x>"+
         "<y>19</y>"+
         "<width>130</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<value>Сохранить</value>"+
      "<type>replace</type>"+
      "<url>javascript:saveandnoexit()</url>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') +. set('global.global.dirtyflag','off') : ''\"></custom:option>"+
   "</button>"+
   "<label sid=\"LABEL3\">"+
      "<itemlocation>"+
         "<x>27</x>"+
         "<y>34</y>"+
         "<width>901</width>"+
      "</itemlocation>"+
      "<value>ОАО \"УРАЛСИБ\" Г МОСКВА</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<justify>center</justify>"+
   "</label>"+
   "<button sid=\"save_exit1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>648</x>"+
         "<y>19</y>"+
         "<width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<type>done</type>"+
      "<value>Сохранить и выйти</value>"+
      "<url>forms/save.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
      "<transmititemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') : ''\"></custom:option>"+
      "<saveformat>application/vnd.xfdl;content-encoding=base64-gzip</saveformat>"+
   "</button>"+
   "<button sid=\"exit1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>800</x>"+
         "<y>19</y>"+
         "<width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<value>Отмена / Закрыть</value>"+
      "<type>done</type>"+
      "<url>forms/cancel.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
      "<transmititemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
   "</button>"+
   "<label sid=\"LABEL1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>10</x>"+
         "<y>8</y>"+
      "</itemlocation>"+
      "<value>Поручение на конверcию одной иностранной "+
"валюты на другую</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<label sid=\"LABEL2\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>10</x>"+
         "<y>51</y>"+
      "</itemlocation>"+
      "<value>Версия  3.46.3</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>6</size>"+
      "</fontinfo>"+
   "</label>"+
   "<label sid=\"LABEL4\">"+
      "<itemlocation>"+
         "<x>27</x>"+
         "<y>74</y>"+
         "<width>901</width>"+
      "</itemlocation>"+
      "<value>ПОРУЧЕНИЕ"+
"НА КОНВЕРСИЮ ОДНОЙ ИНОСТРАННОЙ ВАЛЮТЫ В ДРУГУЮ</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL5\">"+
      "<itemlocation>"+
         "<x>377</x>"+
         "<y>114</y>"+
      "</itemlocation>"+
      "<value>№</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL6\">"+
      "<itemlocation>"+
         "<x>477</x>"+
         "<y>114</y>"+
      "</itemlocation>"+
      "<value>от</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<justify>center</justify>"+
   "</label>"+
   "<field sid=\"FIELD1\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Nom1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>397</x>"+
         "<y>113</y>"+
         "<width>76</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<format>"+
         "<datatype>string</datatype>"+
      "</format>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD2\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Ot1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>497</x>"+
         "<y>113</y>"+
         "<width>101</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<format>"+
         "<datatype>string</datatype>"+
      "</format>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD3\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Naim_org\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>207</x>"+
         "<y>163</y>"+
         "<width>721</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</field>"+
   "<field sid=\"FIELD4\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Inn\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>207</x>"+
         "<y>193</y>"+
         "<width>231</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<format>"+
         "<datatype>string</datatype>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD5\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Fio_lica\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>207</x>"+
         "<y>223</y>"+
         "<width>511</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
   "<field sid=\"FIELD6\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Cod_OKPO\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>807</x>"+
         "<y>193</y>"+
         "<width>121</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</field>"+
   "<field sid=\"FIELD7\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Tel\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>807</x>"+
         "<y>223</y>"+
         "<width>121</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<format>"+
         "<datatype>string</datatype>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<label sid=\"LABEL7\">"+
      "<itemlocation>"+
         "<x>27</x>"+
         "<y>164</y>"+
      "</itemlocation>"+
      "<value>Наименование организации</value>"+
   "</label>"+
   "<label sid=\"LABEL8\">"+
      "<itemlocation>"+
         "<x>27</x>"+
         "<y>194</y>"+
      "</itemlocation>"+
      "<value>ИНН</value>"+
   "</label>"+
   "<label sid=\"LABEL9\">"+
      "<itemlocation>"+
         "<x>27</x>"+
         "<y>224</y>"+
      "</itemlocation>"+
      "<value>ФИО ответственного лица</value>"+
   "</label>"+
   "<label sid=\"LABEL12\">"+
      "<itemlocation>"+
         "<x>27</x>"+
         "<y>257</y>"+
      "</itemlocation>"+
      "<value>Просим произвести конверсию иностранной валюты на следующих условиях:</value>"+
   "</label>"+
   "<label sid=\"LABEL10\">"+
      "<itemlocation>"+
         "<x>727</x>"+
         "<y>194</y>"+
      "</itemlocation>"+
      "<value>Код ОКПО</value>"+
   "</label>"+
   "<label sid=\"LABEL11\">"+
      "<itemlocation>"+
         "<x>727</x>"+
         "<y>224</y>"+
      "</itemlocation>"+
      "<value>Телефон</value>"+
   "</label>"+
   "<line sid=\"LINE27\">"+
      "<itemlocation>"+
         "<x>210</x>"+
         "<y>183</y>"+
         "<width>716</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE28\">"+
      "<itemlocation>"+
         "<x>208</x>"+
         "<y>213</y>"+
         "<width>228</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE29\">"+
      "<itemlocation>"+
         "<x>208</x>"+
         "<y>243</y>"+
         "<width>509</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE30\">"+
      "<itemlocation>"+
         "<x>400</x>"+
         "<y>133</y>"+
         "<width>72</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE31\">"+
      "<itemlocation>"+
         "<x>499</x>"+
         "<y>133</y>"+
         "<width>96</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE32\">"+
      "<itemlocation>"+
         "<x>808</x>"+
         "<y>214</y>"+
         "<width>118</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE34\">"+
      "<itemlocation>"+
         "<x>809</x>"+
         "<y>243</y>"+
         "<width>117</width>"+
      "</itemlocation>"+
   "</line>"+
   "<field sid=\"FIELD34\">"+
      "<itemlocation>"+
         "<x>26</x>"+
         "<y>283</y>"+
         "<width>903</width>"+
         "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '54' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '54' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '54'\">54</height>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<xforms:textarea ref=\"instance('INSTANCE')/Pros_poiz\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
   "</field>"+
   "<line sid=\"LINE42\">"+
      "<itemlocation>"+
         "<x>30</x>"+
         "<y>336</y>"+
         "<width>900</width>"+
         "<below>FIELD34</below>"+
         "<offsetx>2</offsetx>"+
         "<offsety>-6</offsety>"+
      "</itemlocation>"+
   "</line>"+
   "<pane sid=\"PANE1\">"+
      "<xforms:group>"+
         "<xforms:label></xforms:label>"+
         "<field sid=\"FIELD8\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Ysl_post\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>244</x>"+
               "<y>0</y>"+
               "<width>651</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
         "</field>"+
         "<field sid=\"FIELD9\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Pros_sov\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>191</x>"+
               "<y>29</y>"+
               "<width>704</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
         "</field>"+
         "<label sid=\"LABEL13\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>0</y>"+
            "</itemlocation>"+
            "<value>Условия поставки покупаемой валюты:</value>"+
         "</label>"+
         "<label sid=\"LABEL14\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>30</y>"+
            "</itemlocation>"+
            "<value>Просим совершить конверсию:</value>"+
         "</label>"+
         "<line sid=\"LINE33\">"+
            "<itemlocation>"+
               "<x>245</x>"+
               "<y>21</y>"+
               "<width>646</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE35\">"+
            "<itemlocation>"+
               "<x>193</x>"+
               "<y>50</y>"+
               "<width>699</width>"+
            "</itemlocation>"+
         "</line>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<x>27</x>"+
         "<y>344</y>"+
         "<below>FIELD34</below>"+
      "</itemlocation>"+
   "</pane>"+
   "<pane sid=\"PANE2\">"+
      "<xforms:group>"+
         "<xforms:label></xforms:label>"+
         "<label sid=\"LABEL15\">"+
            "<itemlocation>"+
               "<x>11</x>"+
               "<y>19</y>"+
               "<width>71</width>"+
            "</itemlocation>"+
            "<value>Код"+
"валюты"+
"(покупка)</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL16\">"+
            "<itemlocation>"+
               "<x>111</x>"+
               "<y>19</y>"+
               "<width>241</width>"+
            "</itemlocation>"+
            "<value>Сумма покупки иностранной валюты</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL17\">"+
            "<itemlocation>"+
               "<x>378</x>"+
               "<y>19</y>"+
            "</itemlocation>"+
            "<value>Кросс-курс"+
"сделки</value>"+
"               <justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL18\">"+
            "<itemlocation>"+
               "<x>476</x>"+
               "<y>19</y>"+
               "<width>321</width>"+
            "</itemlocation>"+
            "<value>Сумма продажи иностранной валюты</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL19\">"+
            "<itemlocation>"+
               "<x>824</x>"+
               "<y>19</y>"+
               "<width>66</width>"+
            "</itemlocation>"+
            "<value>Код"+
"валюты"+
"(продажа)</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<line sid=\"LINE6\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>900</x>"+
               "<y>9</y>"+
               "<height>80</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE7\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>9</y>"+
               "<width>900</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE1\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>10</y>"+
               "<height>83</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE2\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>90</x>"+
               "<y>10</y>"+
               "<height>82</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE3\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>365</x>"+
               "<y>10</y>"+
               "<height>83</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE4\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>462</x>"+
               "<y>10</y>"+
               "<height>84</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE5\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>811</x>"+
               "<y>10</y>"+
               "<height>83</height>"+
            "</itemlocation>"+
         "</line>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<x>23</x>"+
         "<y>413</y>"+
         "<below>PANE1</below>"+
         "<offsety>7</offsety>"+
         "<offsetx>-4</offsetx>"+
      "</itemlocation>"+
   "</pane>"+
   "<pane sid=\"TABLE2_PANE\">"+
      "<xforms:group ref=\"instance('INSTANCE')/table2/row\">"+
         "<xforms:label></xforms:label>"+
         "<spacer sid=\"HEADER_SPACER1\">"+
            "<itemlocation>"+
               "<width>83</width>"+
               "<height></height>"+
               "<offsetx>6</offsetx>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER2\">"+
            "<itemlocation>"+
               "<width>271</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL1</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER3\">"+
            "<itemlocation>"+
               "<width>93</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL2</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER4\">"+
            "<itemlocation>"+
               "<width>345</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL3</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER5\">"+
            "<itemlocation>"+
               "<width>85</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL4</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<table sid=\"TABLE2_TABLE\">"+
            "<xforms:repeat id=\"TABLE2\" nodeset=\"instance('INSTANCE')/table2/row\">"+
               "<pane sid=\"ROW_GROUP\">"+
                  "<xforms:group ref=\".\">"+
                     "<xforms:label></xforms:label>"+
                     "<spacer sid=\"setHeight\">"+
                        "<itemlocation>"+
                           "<offsety>2</offsety>"+
                           "<expandheight>2</expandheight>"+
                        "</itemlocation>"+
                     "</spacer>"+
                     "<field sid=\"KOD_VAL_POK\">"+
                        "<itemlocation>"+
                           "<x>0</x>"+
                           "<width>84</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Kod_val_pok\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                     "</field>"+
                     "<field sid=\"SUM_POK\">"+
                        "<itemlocation>"+
                           "<width>272</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                           "<after>KOD_VAL_POK</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Sum_pok\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                     "</field>"+
                     "<field sid=\"Cros_cyrs\">"+
                        "<itemlocation>"+
                           "<x>436</x>"+
                           "<y>11</y>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                           "<after>SUM_POK</after>"+
                           "<width>91</width>"+
                        "</itemlocation>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<visible>on</visible>"+
                        "<printvisible>on</printvisible>"+
                        "<border>off</border>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Kpos_kyrs\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                     "</field>"+
                     "<field sid=\"SUM_PROD\">"+
                        "<itemlocation>"+
                           "<width>345</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                           "<after>Cros_cyrs</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Sum-prod\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                     "</field>"+
                     "<field sid=\"COD_VAL_PROD\">"+
                        "<itemlocation>"+
                           "<width>85</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                           "<after>SUM_PROD</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Cod_val_prod\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                     "</field>"+
                     "<line sid=\"LINE8\">"+
                        "<itemlocation>"+
                           "<width>892</width>"+
                           "<x>0</x>"+
                           "<y>0</y>"+
                        "</itemlocation>"+
                     "</line>"+
                  "</xforms:group>"+
                  "<padding>1</padding>"+
               "</pane>"+
            "</xforms:repeat>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>1</y>"+
            "</itemlocation>"+
            "<rowpadding>-2</rowpadding>"+
         "</table>"+
         "<box sid=\"BORDER\">"+
            "<itemlocation>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<alignl2l>TABLE2_TABLE</alignl2l>"+
               "<expandr2r>TABLE2_TABLE</expandr2r>"+
               "<expandb2b>TABLE2_TABLE</expandb2b>"+
            "</itemlocation>"+
         "</box>"+
         "<line sid=\"COLUMN_DIVIDER1\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER1</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE2_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER2\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER2</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE2_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER3\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER3</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE2_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER4\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER4</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE2_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<button sid=\"ADD\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table2\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:insert at=\"last()\" nodeset=\"row\" position=\"after\"></xforms:insert>"+
                  "<xforms:setvalue ref=\"row[last()]/Kod_val_pok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/Sum_pok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/Kpos_kyrs\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/Sum-prod\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/Cod_val_prod\"></xforms:setvalue>"+
                  "<xforms:setfocus control=\"TABLE2\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>TABLE2_TABLE</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>+</value>"+
            "<printvisible>on</printvisible>"+
            "<visible>on</visible>"+
            "<border>on</border>"+
         "</button>"+
         "<button sid=\"REMOVE\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table2\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Kod_val_pok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Sum_pok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Kpos_kyrs\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Sum-prod\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Cod_val_prod\"></xforms:setvalue>"+
                  "<xforms:delete at=\"index('TABLE2')\" nodeset=\"row[last() > 1]\"></xforms:delete>"+
                  "<xforms:setfocus control=\"TABLE2\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>ADD</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>-</value>"+
            "<printvisible>on</printvisible>"+
            "<visible>on</visible>"+
         "</button>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<x>22</x>"+
         "<y>491</y>"+
         "<below>PANE2</below>"+
         "<offsetx>-1</offsetx>"+
         "<offsety>-23</offsety>"+
      "</itemlocation>"+
   "</pane>"+
   "<pane sid=\"PANE3\">"+
      "<xforms:group>"+
         "<xforms:label></xforms:label>"+
         "<line sid=\"LINE10\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>509</y>"+
               "<width>897</width>"+
            "</itemlocation>"+
         "</line>"+
         "<field sid=\"FIELD17\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Na_nash2\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>243</x>"+
               "<y>148</y>"+
               "<width>259</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<readonly compute=\"PAGE1.FIELD27.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD27.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<field sid=\"FIELD21\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Pril_spra\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>300</x>"+
               "<y>438</y>"+
               "<width>160</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
         "</field>"+
         "<field sid=\"FIELD22\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Ot2\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>492</x>"+
               "<y>438</y>"+
               "<width>160</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<format>"+
               "<datatype>string</datatype>"+
            "</format>"+
         "</field>"+
         "<field sid=\"FIELD19\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Spis_nash\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>194</x>"+
               "<y>228</y>"+
               "<width>226</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<readonly compute=\"PAGE1.FIELD37.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD37.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<field sid=\"FIELD18\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Vbanke\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>569</x>"+
               "<y>148</y>"+
               "<width>334</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<readonly compute=\"PAGE1.FIELD27.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD27.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<field sid=\"FIELD14\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Por_spis\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>383</x>"+
               "<y>28</y>"+
               "<width>186</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
            "<readonly compute=\"PAGE1.FIELD13.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD13.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<field sid=\"FIELD35\">"+
            "<xforms:input ref=\"instance('INSTANCE')/V1\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>587</x>"+
               "<y>28</y>"+
               "<width>313</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
            "<readonly compute=\"PAGE1.FIELD13.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD13.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<line sid=\"LINE43\">"+
            "<itemlocation>"+
               "<x>590</x>"+
               "<y>48</y>"+
               "<width>308</width>"+
            "</itemlocation>"+
         "</line>"+
         "<field sid=\"FIELD16\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Na_nash1\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>242</x>"+
               "<y>118</y>"+
               "<width>319</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
            "<readonly compute=\"PAGE1.FIELD26.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD26.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<field sid=\"FIELD15\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Nom2\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>611</x>"+
               "<y>58</y>"+
               "<width>291</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<readonly compute=\"PAGE1.FIELD12.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD12.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<label sid=\"LABEL21\">"+
            "<xforms:output>"+
               "<xforms:label>Cумму продаваемой валюты:</xforms:label>"+
            "</xforms:output>"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>0</y>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
         "</label>"+
         "<label sid=\"LABEL22\">"+
            "<itemlocation>"+
               "<x>22</x>"+
               "<y>28</y>"+
            "</itemlocation>"+
            "<value>поручаем списать с нашего текущего валютного счёта №</value>"+
         "</label>"+
         "<check sid=\"CHECK2\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>57</y>"+
            "</itemlocation>"+
            "<xforms:input ref=\"instance('INSTANCE')/Sun_prod_val2\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
         "</check>"+
         "<label sid=\"LABEL23\">"+
            "<itemlocation>"+
               "<x>22</x>"+
               "<y>58</y>"+
            "</itemlocation>"+
            "<value>обязуемся перевести поручением № </value>"+
         "</label>"+
         "<label sid=\"LABEL24\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>89</y>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
            "<value>Купленную валюту просим зачислить:</value>"+
         "</label>"+
         "<check sid=\"CHECK3\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Kyp_val_pros1\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>117</y>"+
            "</itemlocation>"+
         "</check>"+
         "<check sid=\"CHECK4\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Kyp_val_pros2\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>147</y>"+
            "</itemlocation>"+
         "</check>"+
         "<label sid=\"LABEL26\">"+
            "<itemlocation>"+
               "<x>21</x>"+
               "<y>147</y>"+
            "</itemlocation>"+
            "<value>на наш текущий валютный счёт №</value>"+
         "</label>"+
         "<label sid=\"LABEL25\">"+
            "<itemlocation>"+
               "<x>22</x>"+
               "<y>117</y>"+
            "</itemlocation>"+
            "<value>на наш текущий валютный счёт №</value>"+
         "</label>"+
         "<label sid=\"LABEL27\">"+
            "<itemlocation>"+
               "<x>509</x>"+
               "<y>148</y>"+
            "</itemlocation>"+
            "<value>в банке</value>"+
         "</label>"+
         "<label sid=\"LABEL28\">"+
            "<itemlocation>"+
               "<x>572</x>"+
               "<y>171</y>"+
               "<width>329</width>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>7</size>"+
            "</fontinfo>"+
            "<justify>center</justify>"+
            "<value>(указать реквизиты для перевода)</value>"+
         "</label>"+
         "<label sid=\"LABEL29\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>199</y>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
            "</fontinfo>"+
            "<value>Комиссию за исполнение данного поручения просим:</value>"+
         "</label>"+
         "<check sid=\"CHECK5\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Kom_isp1\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>228</y>"+
            "</itemlocation>"+
         "</check>"+
         "<label sid=\"LABEL30\">"+
            "<itemlocation>"+
               "<x>21</x>"+
               "<y>227</y>"+
            "</itemlocation>"+
            "<value>списать с нашего счета №</value>"+
         "</label>"+
         "<label sid=\"LABEL31\">"+
            "<itemlocation>"+
               "<x>424</x>"+
               "<y>229</y>"+
            "</itemlocation>"+
            "<value>в.</value>"+
         "</label>"+
         "<check sid=\"CHECK6\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Kom_isp2\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>258</y>"+
            "</itemlocation>"+
         "</check>"+
         "<label sid=\"LABEL32\">"+
            "<itemlocation>"+
               "<x>21</x>"+
               "<y>258</y>"+
            "</itemlocation>"+
            "<value>удержать из суммы сделки</value>"+
         "</label>"+
         "<label sid=\"LABEL33\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>300</y>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
            "</fontinfo>"+
            "<value>Дополнительная информация:</value>"+
         "</label>"+
         "<label sid=\"LABEL34\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>370</y>"+
            "</itemlocation>"+
            "<value>Вид валютной операции (код и наименование) в соответствии с Приложением № 2 к Инструкции ЦБР № 117-И от 15 июня 2004г:</value>"+
         "</label>"+
         "<field sid=\"FIELD20\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Dop_inf\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>328</y>"+
               "<width>903</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
         "</field>"+
         "<label sid=\"LABEL36\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>438</y>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
            "</fontinfo>"+
            "<value>Прилагается </value>"+
         "</label>"+
         "<label sid=\"LABEL37\">"+
            "<itemlocation>"+
               "<x>465</x>"+
               "<y>438</y>"+
            "</itemlocation>"+
            "<value>от</value>"+
         "</label>"+
         "<label sid=\"LABEL38\">"+
            "<itemlocation>"+
               "<x>655</x>"+
               "<y>438</y>"+
            "</itemlocation>"+
            "<value>г.</value>"+
         "</label>"+
         "<label sid=\"LABEL39\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>478</y>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
            "</fontinfo>"+
            "<value>Документы, обосновывающие сделку:</value>"+
         "</label>"+
         "<label sid=\"LABEL40\">"+
            "<itemlocation>"+
               "<x>9</x>"+
               "<y>518</y>"+
               "<width>190</width>"+
            "</itemlocation>"+
            "<justify>center</justify>"+
            "<value>Вид документа</value>"+
         "</label>"+
         "<label sid=\"LABEL41\">"+
            "<itemlocation>"+
               "<x>222</x>"+
               "<y>518</y>"+
               "<width>209</width>"+
            "</itemlocation>"+
            "<justify>center</justify>"+
            "<value>Номер документа</value>"+
         "</label>"+
         "<label sid=\"LABEL42\">"+
            "<itemlocation>"+
               "<x>452</x>"+
               "<y>518</y>"+
               "<width>217</width>"+
            "</itemlocation>"+
            "<justify>center</justify>"+
            "<value>Дата документа</value>"+
         "</label>"+
         "<label sid=\"LABEL43\">"+
            "<itemlocation>"+
               "<x>692</x>"+
               "<y>518</y>"+
               "<width>201</width>"+
            "</itemlocation>"+
            "<justify>center</justify>"+
            "<value>Примечание</value>"+
         "</label>"+
         "<label sid=\"LABEL56\">"+
            "<itemlocation>"+
               "<x>359</x>"+
               "<y>57</y>"+
            "</itemlocation>"+
            "<value>от</value>"+
         "</label>"+
         "<field sid=\"FIELD23\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Ob_pere\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>261</x>"+
               "<y>58</y>"+
               "<width>99</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<readonly compute=\"PAGE1.FIELD12.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD12.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<field sid=\"FIELD36\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Ot3\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>382</x>"+
               "<y>59</y>"+
               "<width>94</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<readonly compute=\"PAGE1.FIELD12.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD12.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<line sid=\"LINE44\">"+
            "<itemlocation>"+
               "<x>383</x>"+
               "<y>79</y>"+
               "<width>91</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE13\">"+
            "<itemlocation>"+
               "<x>384</x>"+
               "<y>48</y>"+
               "<width>181</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE20\">"+
            "<itemlocation>"+
               "<x>614</x>"+
               "<y>78</y>"+
               "<width>286</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE21\">"+
            "<itemlocation>"+
               "<x>262</x>"+
               "<y>78</y>"+
               "<width>97</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE22\">"+
            "<itemlocation>"+
               "<x>244</x>"+
               "<y>138</y>"+
               "<width>315</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE23\">"+
            "<itemlocation>"+
               "<x>197</x>"+
               "<y>248</y>"+
               "<width>220</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE24\">"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>348</y>"+
               "<width>900</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE25\">"+
            "<itemlocation>"+
               "<x>244</x>"+
               "<y>168</y>"+
               "<width>257</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE26\">"+
            "<itemlocation>"+
               "<x>574</x>"+
               "<y>168</y>"+
               "<width>328</width>"+
            "</itemlocation>"+
         "</line>"+
         "<field sid=\"FIELD24\">"+
            "<xforms:input ref=\"instance('INSTANCE')/pzd_pred\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>398</y>"+
               "<width>125</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
         "</field>"+
         "<field sid=\"FIELD25\">"+
            "<xforms:input ref=\"instance('INSTANCE')/pzd_dor\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>128</x>"+
               "<y>398</y>"+
               "<width>775</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
         "</field>"+
         "<line sid=\"LINE36\">"+
            "<itemlocation>"+
               "<x>2</x>"+
               "<y>418</y>"+
               "<width>120</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE37\">"+
            "<itemlocation>"+
               "<x>130</x>"+
               "<y>418</y>"+
               "<width>770</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE38\">"+
            "<itemlocation>"+
               "<x>301</x>"+
               "<y>458</y>"+
               "<width>156</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE39\">"+
            "<itemlocation>"+
               "<x>492</x>"+
               "<y>458</y>"+
               "<width>157</width>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL35\">"+
            "<xforms:output>"+
               "<xforms:label>.</xforms:label>"+
            "</xforms:output>"+
            "<itemlocation>"+
               "<x>899</x>"+
               "<y>61</y>"+
               "<width>14</width>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
            "</fontinfo>"+
         "</label>"+
         "<label sid=\"LABEL61\">"+
            "<xforms:output>"+
               "<xforms:label>.</xforms:label>"+
            "</xforms:output>"+
            "<itemlocation>"+
               "<x>900</x>"+
               "<y>31</y>"+
               "<width>14</width>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
            "</fontinfo>"+
         "</label>"+
         "<label sid=\"LABEL57\">"+
            "<itemlocation>"+
               "<x>569</x>"+
               "<y>28</y>"+
               "<width>14</width>"+
            "</itemlocation>"+
            "<value>в</value>"+
         "</label>"+
         "<label sid=\"LABEL58\">"+
            "<itemlocation>"+
               "<x>477</x>"+
               "<y>58</y>"+
            "</itemlocation>"+
            "<value>на валютный счет №</value>"+
         "</label>"+
         "<field sid=\"FIELD10\">"+
            "<xforms:input ref=\"instance('INSTANCE')/V2\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>584</x>"+
               "<y>118</y>"+
               "<width>316</width>"+
            "</itemlocation>"+
            "<scrollhoriz>wordwrap</scrollhoriz>"+
            "<border>off</border>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
            "<readonly compute=\"PAGE1.FIELD26.value =='true' ? 'off' : 'on'\">on</readonly>"+
            "<custom:option xfdl:compute=\"PAGE1.FIELD26.value != 'true' ? set('value', '') :''\"></custom:option>"+
         "</field>"+
         "<line sid=\"LINE9\">"+
            "<itemlocation>"+
               "<x>587</x>"+
               "<y>138</y>"+
               "<width>311</width>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL60\">"+
            "<itemlocation>"+
               "<x>564</x>"+
               "<y>118</y>"+
               "<width>14</width>"+
            "</itemlocation>"+
            "<value>в</value>"+
         "</label>"+
         "<line sid=\"LINE17\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>678</x>"+
               "<y>509</y>"+
               "<height>55</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE16\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>442</x>"+
               "<y>509</y>"+
               "<height>56</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE15\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>211</x>"+
               "<y>509</y>"+
               "<height>51</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE11\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>509</y>"+
               "<height>49</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE14\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>898</x>"+
               "<y>510</y>"+
               "<height>54</height>"+
            "</itemlocation>"+
         "</line>"+
         "<check sid=\"CHECK1\">"+
            "<xforms:input ref=\"instance('INSTANCE')/Sum_prod_val1\">"+
               "<xforms:label></xforms:label>"+
            "</xforms:input>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>29</y>"+
            "</itemlocation>"+
            "<custom:option></custom:option>"+
         "</check>"+
         "<label sid=\"LABEL20\">"+
            "<xforms:output>"+
               "<xforms:label>Справка</xforms:label>"+
            "</xforms:output>"+
            "<itemlocation>"+
               "<x>79</x>"+
               "<y>438</y>"+
            "</itemlocation>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
         "</label>"+
         "<label sid=\"LABEL62\">"+
            "<xforms:output>"+
               "<xforms:label>о валютных операциях №</xforms:label>"+
            "</xforms:output>"+
            "<itemlocation>"+
               "<x>137</x>"+
               "<y>438</y>"+
            "</itemlocation>"+
         "</label>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<x>23</x>"+
         "<y>883</y>"+
         "<below>TABLE2_PANE</below>"+
         "<offsetx>3</offsetx>"+
         "<offsety>1</offsety>"+
      "</itemlocation>"+
   "</pane>"+
   "<pane sid=\"TABLE1_PANE\">"+
      "<xforms:group ref=\"instance('INSTANCE')/table1/row\">"+
         "<xforms:label></xforms:label>"+
         "<spacer sid=\"HEADER_SPACER1\">"+
            "<itemlocation>"+
               "<width>203</width>"+
               "<height></height>"+
               "<offsetx>6</offsetx>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER2\">"+
            "<itemlocation>"+
               "<width>227</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL1</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER3\">"+
            "<itemlocation>"+
               "<width>232</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL2</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER4\">"+
            "<itemlocation>"+
               "<width>226</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL3</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<table sid=\"TABLE1_TABLE\">"+
            "<xforms:repeat id=\"TABLE1\" nodeset=\"instance('INSTANCE')/table1/row\">"+
               "<pane sid=\"ROW_GROUP\">"+
                  "<xforms:group ref=\".\">"+
                     "<xforms:label></xforms:label>"+
                     "<spacer sid=\"setHeight\">"+
                        "<itemlocation>"+
                           "<offsety>2</offsety>"+
                           "<expandheight>2</expandheight>"+
                        "</itemlocation>"+
                     "</spacer>"+
                     "<field sid=\"VID_DOK\">"+
                        "<itemlocation>"+
                           "<x>0</x>"+
                           "<width>204</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Vid_dok\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                     "</field>"+
                     "<field sid=\"NOM_DOK\">"+
                        "<itemlocation>"+
                           "<width>227</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                           "<after>VID_DOK</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Nom_dok\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                        "</format>"+
                     "</field>"+
                     "<field sid=\"DATA_DOK\">"+
                        "<itemlocation>"+
                           "<width>232</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                           "<after>NOM_DOK</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Data_dok\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                        "</format>"+
                     "</field>"+
                     "<field sid=\"PRIM\">"+
                        "<itemlocation>"+
                           "<width>215</width>"+
                           "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
                           "<after>DATA_DOK</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
                        "<xforms:textarea ref=\"Prim\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                        "</format>"+
                     "</field>"+
                     "<line sid=\"LINE12\">"+
                        "<itemlocation>"+
                           "<x>0</x>"+
                           "<y>0</y>"+
                           "<width>890</width>"+
                        "</itemlocation>"+
                     "</line>"+
                  "</xforms:group>"+
                  "<padding>1</padding>"+
               "</pane>"+
            "</xforms:repeat>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>1</y>"+
            "</itemlocation>"+
            "<rowpadding>-2</rowpadding>"+
         "</table>"+
         "<box sid=\"BORDER\">"+
            "<itemlocation>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<alignl2l>TABLE1_TABLE</alignl2l>"+
               "<expandr2r>TABLE1_TABLE</expandr2r>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
            "</itemlocation>"+
         "</box>"+
         "<line sid=\"COLUMN_DIVIDER1\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER1</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER2\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER2</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER3\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER3</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<button sid=\"ADD\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table1\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:insert at=\"last()\" nodeset=\"row\" position=\"after\"></xforms:insert>"+
                  "<xforms:setvalue ref=\"row[last()]/Vid_dok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/Nom_dok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/Data_dok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/Prim\"></xforms:setvalue>"+
                  "<xforms:setfocus control=\"TABLE1\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>TABLE1_TABLE</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>+</value>"+
            "<border>on</border>"+
            "<visible>on</visible>"+
            "<printvisible>on</printvisible>"+
         "</button>"+
         "<button sid=\"REMOVE\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table1\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Vid_dok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Nom_dok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Data_dok\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/Prim\"></xforms:setvalue>"+
                  "<xforms:delete at=\"index('TABLE1')\" nodeset=\"row[last() > 1]\"></xforms:delete>"+
                  "<xforms:setfocus control=\"TABLE1\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>ADD</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>-</value>"+
            "<printvisible>on</printvisible>"+
            "<visible>on</visible>"+
         "</button>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<x>22</x>"+
         "<y>1109</y>"+
         "<below>PANE3</below>"+
         "<offsety>-29</offsety>"+
      "</itemlocation>"+
   "</pane>"+
   "<label sid=\"LABEL44\">"+
      "<itemlocation>"+
         "<x>28</x>"+
         "<y>1154</y>"+
         "<below>TABLE1_PANE</below>"+
         "<offsetx>5</offsetx>"+
      "</itemlocation>"+
      "<value>С</value>"+
   "</label>"+
   "<button sid=\"BUTTON1\">"+
      "<itemlocation>"+
         "<x>618</x>"+
         "<y>1198</y>"+
         "<width>305</width>"+
         "<height>38</height>"+
         "<below>LABEL44</below>"+
         "<offsetx>591</offsetx>"+
         "<offsety>18</offsety>"+
      "</itemlocation>"+
      "<value compute=\"signer=='' ? 'Добавить подпись' : signer\">Добавить подпись</value>"+
      "<type>signature</type>"+
      "<size>"+
         "<width>40</width>"+
         "<height>1</height>"+
      "</size>"+
      "<signature>BUTTON1_SIGNATURE_226146479</signature>"+
      "<signer></signer>"+
      "<signformat>application/vnd.xfdl;engine=\"CryptoAPI\";csp=\"Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider\";csptype=75;delete=\"off\";layoutoverlaptest=\"none\"</signformat>"+
      "<signitemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
         "<itemref>PAGE1.FIELD2</itemref>"+
         "<itemref>PAGE1.FIELD3</itemref>"+
         "<itemref>PAGE1.FIELD4</itemref>"+
         "<itemref>PAGE1.FIELD5</itemref>"+
         "<itemref>PAGE1.FIELD6</itemref>"+
         "<itemref>PAGE1.FIELD7</itemref>"+
         "<itemref>PAGE1.FIELD34</itemref>"+
         "<itemref>PAGE1.PANE1</itemref>"+
         "<itemref>PAGE1.TABLE2_PANE</itemref>"+
         "<itemref>PAGE1.PANE3</itemref>"+
         "<itemref>PAGE1.TABLE1_PANE</itemref>"+
         "<itemref>PAGE1.FIELD28</itemref>"+
         "<itemref>PAGE1.FIELD31</itemref>"+
         "<itemref>PAGE1.FIELD32</itemref>"+
         "<itemref>PAGE1.FIELD33</itemref>"+
         "<itemref>PAGE1.FIELD29</itemref>"+
         "<itemref>PAGE1.FIELD30</itemref>"+
         "<itemref>PAGE1.FIELD8</itemref>"+
         "<itemref>PAGE1.FIELD9</itemref>"+
         "<itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD21</itemref>"+
         "<itemref>PAGE1.FIELD22</itemref>"+
         "<itemref>PAGE1.FIELD19</itemref>"+
         "<itemref>PAGE1.FIELD18</itemref>"+
         "<itemref>PAGE1.FIELD14</itemref>"+
         "<itemref>PAGE1.FIELD35</itemref>"+
         "<itemref>PAGE1.FIELD16</itemref>"+
         "<itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD20</itemref>"+
         "<itemref>PAGE1.FIELD23</itemref>"+
         "<itemref>PAGE1.FIELD36</itemref>"+
         "<itemref>PAGE1.FIELD24</itemref>"+
         "<itemref>PAGE1.FIELD25</itemref>"+
         "<itemref>PAGE1.FIELD10</itemref>"+
         "<itemref>PAGE1.CHECK1</itemref>"+
         "<itemref>PAGE1.CHECK2</itemref>"+
         "<itemref>PAGE1.CHECK3</itemref>"+
         "<itemref>PAGE1.CHECK4</itemref>"+
         "<itemref>PAGE1.CHECK5</itemref>"+
         "<itemref>PAGE1.CHECK6</itemref>"+
      "</signitemrefs>"+
      "<signinstance>"+
         "<filter>keep</filter>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Nom1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Naim_org</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Inn</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Fio_lica</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Cod_OKPO</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Tel</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pros_poiz</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/table2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/table1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Otm_Isp</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Data_val</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyps_sdel</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sym_kom</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Val_kyp</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Val_spis</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ysl_post</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pros_sov</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Na_nash2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pril_spra</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Spis_nash</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Vbanke</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Por_spis</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/V1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Na_nash1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Nom2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Dop_inf</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ob_pere</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot3</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/pzd_pred</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/pzd_dor</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/V2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sum_prod_val1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sun_prod_val2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyp_val_pros1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyp_val_pros2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kom_isp1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kom_isp2</ref>"+
         "</dataref>"+
      "</signinstance>"+
      "<signoptions>"+
         "<filter>omit</filter>"+
         "<optiontype>triggeritem</optiontype>"+
         "<optiontype>coordinates</optiontype>"+
      "</signoptions>"+
   "</button>"+
   "<label sid=\"LABEL47\">"+
      "<itemlocation>"+
         "<x>475</x>"+
         "<y>1205</y>"+
         "<width>130</width>"+
         "<below>LABEL44</below>"+
         "<offsetx>453</offsetx>"+
         "<offsety>26</offsety>"+
      "</itemlocation>"+
      "<value>Главный бухгалтер:</value>"+
   "</label>"+
   "<button sid=\"BUTTON4\">"+
      "<itemlocation>"+
         "<x>155</x>"+
         "<y>1196</y>"+
         "<width>305</width>"+
         "<height>38</height>"+
         "<below>LABEL44</below>"+
         "<offsetx>129</offsetx>"+
         "<offsety>18</offsety>"+
      "</itemlocation>"+
      "<value compute=\"signer=='' ? 'Добавить подпись' : signer\">Добавить подпись</value>"+
      "<type>signature</type>"+
      "<signature>BUTTON4_SIGNATURE_502827902</signature>"+
      "<signer></signer>"+
      "<signformat>application/vnd.xfdl;engine=\"CryptoAPI\";csp=\"Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider\";csptype=75;delete=\"off\";layoutoverlaptest=\"none\"</signformat>"+
      "<size>"+
         "<width>40</width>"+
         "<height>1</height>"+
      "</size>"+
      "<signitemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
         "<itemref>PAGE1.FIELD2</itemref>"+
         "<itemref>PAGE1.FIELD3</itemref>"+
         "<itemref>PAGE1.FIELD4</itemref>"+
         "<itemref>PAGE1.FIELD5</itemref>"+
         "<itemref>PAGE1.FIELD6</itemref>"+
         "<itemref>PAGE1.FIELD7</itemref>"+
         "<itemref>PAGE1.FIELD34</itemref>"+
         "<itemref>PAGE1.PANE1</itemref>"+
         "<itemref>PAGE1.TABLE2_PANE</itemref>"+
         "<itemref>PAGE1.PANE3</itemref>"+
         "<itemref>PAGE1.TABLE1_PANE</itemref>"+
         "<itemref>PAGE1.FIELD28</itemref>"+
         "<itemref>PAGE1.FIELD31</itemref>"+
         "<itemref>PAGE1.FIELD32</itemref>"+
         "<itemref>PAGE1.FIELD33</itemref>"+
         "<itemref>PAGE1.FIELD29</itemref>"+
         "<itemref>PAGE1.FIELD30</itemref>"+
         "<itemref>PAGE1.FIELD8</itemref>"+
         "<itemref>PAGE1.FIELD9</itemref>"+
         "<itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD21</itemref>"+
         "<itemref>PAGE1.FIELD22</itemref>"+
         "<itemref>PAGE1.FIELD19</itemref>"+
         "<itemref>PAGE1.FIELD18</itemref>"+
         "<itemref>PAGE1.FIELD14</itemref>"+
         "<itemref>PAGE1.FIELD35</itemref>"+
         "<itemref>PAGE1.FIELD16</itemref>"+
         "<itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD20</itemref>"+
         "<itemref>PAGE1.FIELD23</itemref>"+
         "<itemref>PAGE1.FIELD36</itemref>"+
         "<itemref>PAGE1.FIELD24</itemref>"+
         "<itemref>PAGE1.FIELD25</itemref>"+
         "<itemref>PAGE1.FIELD10</itemref>"+
         "<itemref>PAGE1.CHECK1</itemref>"+
         "<itemref>PAGE1.CHECK2</itemref>"+
         "<itemref>PAGE1.CHECK3</itemref>"+
         "<itemref>PAGE1.CHECK4</itemref>"+
         "<itemref>PAGE1.CHECK5</itemref>"+
         "<itemref>PAGE1.CHECK6</itemref>"+
      "</signitemrefs>"+
      "<signinstance>"+
         "<filter>keep</filter>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Nom1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Naim_org</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Inn</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Fio_lica</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Cod_OKPO</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Tel</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pros_poiz</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/table2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/table1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Otm_Isp</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Data_val</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyps_sdel</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sym_kom</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Val_kyp</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Val_spis</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ysl_post</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pros_sov</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Na_nash2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pril_spra</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Spis_nash</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Vbanke</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Por_spis</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/V1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Na_nash1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Nom2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Dop_inf</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ob_pere</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot3</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/pzd_pred</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/pzd_dor</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/V2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sum_prod_val1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sun_prod_val2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyp_val_pros1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyp_val_pros2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kom_isp1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kom_isp2</ref>"+
         "</dataref>"+
      "</signinstance>"+
      "<signoptions>"+
         "<filter>omit</filter>"+
         "<optiontype>triggeritem</optiontype>"+
         "<optiontype>coordinates</optiontype>"+
      "</signoptions>"+
      "<format>"+
         "<datatype>string</datatype>"+
      "</format>"+
   "</button>"+
   "<label sid=\"LABEL46\">"+
      "<itemlocation>"+
         "<x>25</x>"+
         "<y>1205</y>"+
         "<below>LABEL44</below>"+
         "<offsety>26</offsety>"+
      "</itemlocation>"+
      "<value>М.П. Руководитель:</value>"+
   "</label>"+
   "<line sid=\"LINE18\">"+
      "<itemlocation>"+
         "<x>25</x>"+
         "<y>1248</y>"+
         "<width>899</width>"+
         "<below>LABEL46</below>"+
         "<offsety>17</offsety>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL48\">"+
      "<itemlocation>"+
         "<x>26</x>"+
         "<y>1251</y>"+
         "<width>899</width>"+
         "<below>LINE18</below>"+
      "</itemlocation>"+
      "<justify>center</justify>"+
      "<value>Отметки Исполняющего Банка:</value>"+
   "</label>"+
   "<field sid=\"FIELD28\">"+
      "<itemlocation>"+
         "<x>26</x>"+
         "<y>1282</y>"+
         "<width>899</width>"+
         "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '54' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '54' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '54'\">54</height>"+
         "<below>LABEL48</below>"+
         "<offsetx>-2</offsetx>"+
         "<offsety>-1</offsety>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
      "<xforms:textarea ref=\"instance('INSTANCE')/Otm_Isp\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
   "</field>"+
   "<line sid=\"LINE40\">"+
      "<itemlocation>"+
         "<x>26</x>"+
         "<y>1335</y>"+
         "<width>895</width>"+
         "<below>FIELD28</below>"+
         "<offsety>-5</offsety>"+
      "</itemlocation>"+
   "</line>"+
   "<field sid=\"FIELD11\">"+
      "<itemlocation>"+
         "<x>669</x>"+
         "<y>1370</y>"+
         "<height compute=\"toggle(global.focuseditem) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' &#xA;"+
           "or  toggle(global.activated) == '1'   and&#xA;"+
           "viewer.measureHeight('pixels','') > '38' ?   &#xA;"+
           "viewer.measureHeight('pixels','') : '38'\">38</height>"+
         "<below>LINE40</below>"+
         "<offsetx>645</offsetx>"+
         "<offsety>26</offsety>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<scrollvert compute=\"set ('viewer.visible', 'off')\">always</scrollvert>"+
      "<xforms:textarea ref=\"instance('INSTANCE')/Pechat\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<readonly>off</readonly>"+
   "</field>"+
   "<line sid=\"LINE19\">"+
      "<itemlocation>"+
         "<x>25</x>"+
         "<y>1413</y>"+
         "<width>899</width>"+
         "<below>FIELD11</below>"+
         "<offsetx>-646</offsetx>"+
         "<offsety>22</offsety>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL50\">"+
      "<itemlocation>"+
         "<x>667</x>"+
         "<y>1375</y>"+
         "<below>LINE40</below>"+
         "<offsetx>592</offsetx>"+
         "<offsety>31</offsety>"+
      "</itemlocation>"+
      "<value>Печать</value>"+
   "</label>"+
   "<button sid=\"BUTTON2\">"+
      "<itemlocation>"+
         "<x>292</x>"+
         "<y>1365</y>"+
         "<width>305</width>"+
         "<height>38</height>"+
         "<below>LINE40</below>"+
         "<offsetx>266</offsetx>"+
         "<offsety>28</offsety>"+
      "</itemlocation>"+
      "<value compute=\"signer=='' ? 'Добавить подпись' : signer\">Добавить подпись</value>"+
      "<type>signature</type>"+
      "<size>"+
         "<width>40</width>"+
         "<height>1</height>"+
      "</size>"+
      "<signature>BUTTON2_SIGNATURE_376604708</signature>"+
      "<signer></signer>"+
      "<signformat>application/vnd.xfdl;engine=\"CryptoAPI\";csp=\"Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider\";csptype=75;delete=\"off\";layoutoverlaptest=\"none\"</signformat>"+
      "<signitemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
         "<itemref>PAGE1.FIELD2</itemref>"+
         "<itemref>PAGE1.FIELD3</itemref>"+
         "<itemref>PAGE1.FIELD4</itemref>"+
         "<itemref>PAGE1.FIELD5</itemref>"+
         "<itemref>PAGE1.FIELD6</itemref>"+
         "<itemref>PAGE1.FIELD7</itemref>"+
         "<itemref>PAGE1.FIELD34</itemref>"+
         "<itemref>PAGE1.PANE1</itemref>"+
         "<itemref>PAGE1.TABLE2_PANE</itemref>"+
         "<itemref>PAGE1.PANE3</itemref>"+
         "<itemref>PAGE1.TABLE1_PANE</itemref>"+
         "<itemref>PAGE1.FIELD28</itemref>"+
         "<itemref>PAGE1.FIELD31</itemref>"+
         "<itemref>PAGE1.FIELD32</itemref>"+
         "<itemref>PAGE1.FIELD33</itemref>"+
         "<itemref>PAGE1.FIELD29</itemref>"+
         "<itemref>PAGE1.FIELD30</itemref>"+
         "<itemref>PAGE1.FIELD8</itemref>"+
         "<itemref>PAGE1.FIELD9</itemref>"+
         "<itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD21</itemref>"+
         "<itemref>PAGE1.FIELD22</itemref>"+
         "<itemref>PAGE1.FIELD19</itemref>"+
         "<itemref>PAGE1.FIELD18</itemref>"+
         "<itemref>PAGE1.FIELD14</itemref>"+
         "<itemref>PAGE1.FIELD35</itemref>"+
         "<itemref>PAGE1.FIELD16</itemref>"+
         "<itemref>PAGE1.FIELD17</itemref>"+
         "<itemref>PAGE1.FIELD20</itemref>"+
         "<itemref>PAGE1.FIELD23</itemref>"+
         "<itemref>PAGE1.FIELD36</itemref>"+
         "<itemref>PAGE1.FIELD24</itemref>"+
         "<itemref>PAGE1.FIELD25</itemref>"+
         "<itemref>PAGE1.FIELD10</itemref>"+
         "<itemref>PAGE1.CHECK1</itemref>"+
         "<itemref>PAGE1.CHECK2</itemref>"+
         "<itemref>PAGE1.CHECK3</itemref>"+
         "<itemref>PAGE1.CHECK4</itemref>"+
         "<itemref>PAGE1.CHECK5</itemref>"+
         "<itemref>PAGE1.CHECK6</itemref>"+
         "<itemref>PAGE1.FIELD11</itemref>"+
      "</signitemrefs>"+
      "<signinstance>"+
         "<filter>keep</filter>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Nom1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Naim_org</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Inn</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Fio_lica</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Cod_OKPO</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Tel</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pros_poiz</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Otm_Isp</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Data_val</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyps_sdel</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sym_kom</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Val_kyp</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Val_spis</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ysl_post</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pros_sov</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Na_nash2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pril_spra</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Spis_nash</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Vbanke</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Por_spis</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/V1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Na_nash1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Nom2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Dop_inf</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ob_pere</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Ot3</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/pzd_pred</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/pzd_dor</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/V2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sum_prod_val1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Sun_prod_val2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyp_val_pros1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kyp_val_pros2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kom_isp1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Kom_isp2</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/Pechat</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/table1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/table2</ref>"+
         "</dataref>"+
      "</signinstance>"+
      "<signoptions>"+
         "<filter>omit</filter>"+
         "<optiontype>triggeritem</optiontype>"+
         "<optiontype>coordinates</optiontype>"+
      "</signoptions>"+
   "</button>"+
   "<label sid=\"LABEL49\">"+
      "<itemlocation>"+
         "<x>26</x>"+
         "<y>1375</y>"+
         "<below>LINE40</below>"+
         "<offsetx>2</offsetx>"+
         "<offsety>32</offsety>"+
      "</itemlocation>"+
      "<value>Ответственный исполнитель обработчика</value>"+
   "</label>"+
   "<label sid=\"LABEL45\">"+
      "<itemlocation>"+
         "<x>530</x>"+
         "<y>1154</y>"+
         "<below>LABEL44</below>"+
         "<offsetx>501</offsetx>"+
         "<offsety>-27</offsety>"+
      "</itemlocation>"+
      "<value>ознакомлены и согласны.</value>"+
   "</label>"+
   "<label sid=\"LABEL59\">"+
      "<itemlocation>"+
         "<x>48</x>"+
         "<y>1153</y>"+
         "<below>LABEL44</below>"+
         "<offsetx>16</offsetx>"+
         "<offsety>-27</offsety>"+
      "</itemlocation>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<value>\"Положением о конверсионных операциях, ОАО \"УРАЛСИБ\" Г МОСКВА\"</value>"+
   "</label>"+
   "<label sid=\"LABEL51\">"+
      "<itemlocation>"+
         "<x>26</x>"+
         "<y>1424</y>"+
         "<below>LINE19</below>"+
         "<offsety>3</offsety>"+
      "</itemlocation>"+
      "<value>Валюты куплено</value>"+
   "</label>"+
   "<label sid=\"LABEL53\">"+
      "<itemlocation>"+
         "<x>26</x>"+
         "<y>1454</y>"+
         "<below>LABEL51</below>"+
         "<offsety>2</offsety>"+
      "</itemlocation>"+
      "<value>Дата валютирования</value>"+
   "</label>"+
   "<field sid=\"FIELD31\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Data_val\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>159</x>"+
         "<y>1454</y>"+
         "<width>136</width>"+
         "<below>LABEL53</below>"+
         "<offsetx>135</offsetx>"+
         "<offsety>-26</offsety>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
   "<label sid=\"LABEL54\">"+
      "<itemlocation>"+
         "<x>306</x>"+
         "<y>1454</y>"+
         "<below>FIELD31</below>"+
         "<offsetx>140</offsetx>"+
         "<offsety>-29</offsety>"+
      "</itemlocation>"+
      "<value>Курс сделки</value>"+
   "</label>"+
   "<field sid=\"FIELD32\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Kyps_sdel\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>419</x>"+
         "<y>1454</y>"+
         "<width>160</width>"+
         "<below>LABEL54</below>"+
         "<offsetx>117</offsetx>"+
         "<offsety>-25</offsety>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
   "<label sid=\"LABEL55\">"+
      "<itemlocation>"+
         "<x>587</x>"+
         "<y>1454</y>"+
         "<below>FIELD32</below>"+
         "<offsetx>171</offsetx>"+
         "<offsety>-28</offsety>"+
      "</itemlocation>"+
      "<value>Сумма комиссии</value>"+
   "</label>"+
   "<field sid=\"FIELD33\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Sym_kom\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>706</x>"+
         "<y>1454</y>"+
         "<width>219</width>"+
         "<below>LABEL55</below>"+
         "<offsety>-26</offsety>"+
         "<offsetx>113</offsetx>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
   "<field sid=\"FIELD29\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Val_kyp\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>136</x>"+
         "<y>1424</y>"+
         "<width>160</width>"+
         "<below>LABEL51</below>"+
         "<offsetx>114</offsetx>"+
         "<offsety>-25</offsety>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
   "<label sid=\"LABEL52\">"+
      "<itemlocation>"+
         "<x>305</x>"+
         "<y>1424</y>"+
         "<below>FIELD29</below>"+
         "<offsetx>163</offsetx>"+
         "<offsety>-28</offsety>"+
      "</itemlocation>"+
      "<value>Валюты списано</value>"+
   "</label>"+
   "<field sid=\"FIELD30\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Val_spis\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>419</x>"+
         "<y>1424</y>"+
         "<width>160</width>"+
         "<below>LABEL52</below>"+
         "<offsetx>114</offsetx>"+
         "<offsety>-26</offsety>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
   "</field>"+
   "<field sid=\"FIELD13\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Sum_prod_val1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>6</x>"+
         "<y>590</y>"+
         "<width>14</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<visible>off</visible>"+
      "<printvisible>off</printvisible>"+
      "<border>off</border>"+
   "</field>"+
   "<field sid=\"FIELD12\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Sun_prod_val2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>8</x>"+
         "<y>620</y>"+
         "<width>14</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<visible>off</visible>"+
      "<printvisible>off</printvisible>"+
      "<border>off</border>"+
   "</field>"+
   "<field sid=\"FIELD26\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Kyp_val_pros1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>7</x>"+
         "<y>682</y>"+
         "<width>14</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<visible>off</visible>"+
      "<printvisible>off</printvisible>"+
      "<border>off</border>"+
   "</field>"+
   "<field sid=\"FIELD27\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Kyp_val_pros2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>10</x>"+
         "<y>712</y>"+
         "<width>14</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<visible>off</visible>"+
      "<printvisible>off</printvisible>"+
      "<border>off</border>"+
   "</field>"+
   "<field sid=\"FIELD37\">"+
      "<xforms:input ref=\"instance('INSTANCE')/Kom_isp1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<itemlocation>"+
         "<x>9</x>"+
         "<y>783</y>"+
         "<width>14</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<visible>off</visible>"+
      "<printvisible>off</printvisible>"+
      "<border>off</border>"+
   "</field>"+
   "<spacer sid=\"vfd_spacer\">"+
      "<itemlocation>"+
         "<x>1000</x>"+
         "<y>1300</y>"+
         "<width>1</width>"+
         "<height>1</height>"+
      "</itemlocation>"+
   "</spacer>"+
"</page>"+
"</XFDL>"



































































var xfdlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<XFDL xmlns:custom=\"http://www.ibm.com/xmlns/prod/XFDL/Custom\" xmlns:designer=\"http://www.ibm.com/xmlns/prod/workplace/forms/designer/2.6\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xml:lang=\"ru-RU\" xmlns:xfdl=\"http://www.ibm.com/xmlns/prod/XFDL/7.5\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns=\"http://www.ibm.com/xmlns/prod/XFDL/7.5\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
"<globalpage sid=\"global\">"+
   "<global sid=\"global\">"+
      "<ufv_settings>"+
         "<menu>"+
        "<!-- <save>hidden</save>-->"+
        "<!-- <print>hidden</print>-->"+
         "<open>hidden</open>"+
         "<mail>hidden</mail>"+
         "<preferences>hidden</preferences>"+
         "<spellcheck>hidden</spellcheck>"+
         "<spellcheckall>hidden</spellcheckall>"+
         "<help>hidden</help>"+
         "<viewerhelp>hidden</viewerhelp>"+
         "<zoom>hidden</zoom>"+
         "<about>hidden</about>"+
         "<fontdialog>hidden</fontdialog>"+
         "<paragraphdialog>hidden</paragraphdialog>"+
         "</menu>"+
            "<pagedonewithformaterrors>permit</pagedonewithformaterrors>"+
            "<submitwithformaterrors>permit</submitwithformaterrors>"+
            "<signwithformaterrors>permit</signwithformaterrors>"+
         "</ufv_settings>"+
      "<designer:date>20110418</designer:date>"+
      "<formid>"+
         "<title>ТУ-3 ВЦЕ</title>"+
         "<serialnumber>BCF9E0C17C5B518C:-57AA87DC:12F681A5E23:-8000</serialnumber>"+
         "<version>3.46.20</version>"+
      "</formid>"+
      "<designer:version>3.0.0.130</designer:version>"+
      "<xformsmodels>"+
         "<xforms:model>"+
            "<xforms:instance id=\"INSTANCE\" xmlns=\"\">"+
               "<data>"+
                  "<TN1></TN1>"+
                  "<P1></P1>"+
                  "<P2></P2>"+
                  "<P3></P3>"+
                  "<P4></P4>"+
                  "<P5></P5>"+
                  "<P6></P6>"+
                  "<P7></P7>"+
                  "<P8></P8>"+
"                     "+
                  "<P9></P9>"+
                  "<P9_1></P9_1>"+
                  "<P9_2></P9_2>"+
                  "<P9_3></P9_3>"+
"                     "+
                  "<P10></P10>"+
                  "<P10_1></P10_1>"+
                  "<P10_2></P10_2>"+
                  "<P10_3></P10_3>"+
"                     "+
                  "<P11></P11>"+
                  "<P11_1></P11_1>"+
                  "<P11_2></P11_2>"+
                  "<P11_3></P11_3>"+
"                     "+
                  "<P12></P12>"+
                  "<P12_1></P12_1>"+
                  "<P12_2></P12_2>"+
                  "<P12_3></P12_3>"+
"                     "+
"                     "+
                  "<P13></P13>"+
                  "<P14></P14>"+
                  "<P15></P15>"+
                  "<P16></P16>"+
                  "<P17></P17>"+
                  "<P18></P18>"+
                  "<P19></P19>"+
                  "<P20></P20>"+
                  "<P21></P21>"+
                  "<P22></P22>"+
                  "<P22_2></P22_2>"+
                  "<P23></P23>"+
                  "<P24></P24>"+
                  "<P25></P25>"+
                  "<P26></P26>"+
                  "<P27></P27>"+
                  "<P27_2></P27_2>"+
                  "<P28></P28>"+
                  "<P29></P29>"+
                  "<P30></P30>"+
                  "<P31></P31>"+
                  "<P32></P32>"+
                  "<P33></P33>"+
                  "<P34></P34>"+
                  "<P35></P35>"+
                  "<P36></P36>"+
                  "<P37></P37>"+
                  "<P38></P38>"+
                  "<P39></P39>"+
                  "<P40></P40>"+
                  "<P41></P41>"+
                  "<P42></P42>"+
                  "<P43></P43>"+
                  "<P44></P44>"+
                  "<P45></P45>"+
                  "<P46></P46>"+
                  "<P47></P47>"+
                  "<P48></P48>"+
                  "<P49></P49>"+
                  "<P50></P50>"+
                  "<P51></P51>"+
                  "<P52></P52>"+
                  "<P53></P53>"+
                  "<P54></P54>"+
                  "<P55></P55>"+
                  "<P56></P56>"+
                  "<P57></P57>"+
                  "<P58></P58>"+
                  "<P59></P59>"+
                  "<P60></P60>"+
                  "<P61></P61>"+
                  "<P62></P62>"+
                  "<P63></P63>"+
                  "<P64></P64>"+
                  "<P65></P65>"+
                  "<P66></P66>"+
                  "<P67></P67>"+
                  "<table1>"+
                     "<row1>"+
                        "<T1></T1>"+
                        "<T2></T2>"+
                        "<T4></T4>"+
                        "<T5></T5>"+
                        "<T5_1></T5_1>"+
                        "<T6></T6>"+
                        "<T7></T7>"+
                        "<T8></T8>"+
                        "<T9></T9>"+
                        "<T10></T10>"+
                        "<T11></T11>"+
                        "<T12></T12>"+
"                           "+
                        "<T1table_1_13></T1table_1_13>"+
                        "<T1table_2_13></T1table_2_13>"+
                        "<T1table_1_14></T1table_1_14>"+
                        "<T1table_2_14></T1table_2_14>"+
                        "<T1table_1_15></T1table_1_15>"+
                        "<T1table_2_15></T1table_2_15>"+
                        "<T1table_1_16></T1table_1_16>"+
                        "<T1table_2_16></T1table_2_16>"+
                        "<T1table_1_19></T1table_1_19>"+
                        "<T1table_2_19></T1table_2_19>"+
                        "<T1table_1_20></T1table_1_20>"+
                        "<T1table_2_20></T1table_2_20>"+
                        "<T1table_1_21></T1table_1_21>"+
                        "<T1table_2_21></T1table_2_21>"+
                        "<T1table_1_22></T1table_1_22>"+
                        "<T1table_2_22></T1table_2_22>"+
                        "<T1table_1_24></T1table_1_24>"+
                        "<T1table_2_24></T1table_2_24>"+
                        "<T1table_1_26></T1table_1_26>"+
                        "<T1table_2_26></T1table_2_26>"+
                        "<T1table_1_27></T1table_1_27>"+
                        "<T1table_2_27></T1table_2_27>"+
                        "<T1table_1_28></T1table_1_28>"+
                        "<T1table_2_28></T1table_2_28>"+
                        "<T1table_1_29></T1table_1_29>"+
                        "<T1table_2_29></T1table_2_29>"+
                        "<T1table_1_30></T1table_1_30>"+
                        "<T1table_2_30></T1table_2_30>"+
                        "<T1table_1_31></T1table_1_31>"+
                        "<T1table_2_31></T1table_2_31>"+
"                           "+
                        "<T32></T32>"+
                        "<T33a></T33a>"+
                        "<T33b></T33b>"+
                        "<T33v></T33v>"+
                        "<Tsh3></Tsh3>"+
                        "<T34></T34>"+
                        "<T35></T35>"+
                     "</row1>"+
                  "</table1>"+
"                     "+
""+
                  ""+
                  "<table2>"+
                     "<row1>"+
                        "<T1></T1>"+
                        "<T2></T2>"+
                        "<T3></T3>"+
                        "<T4></T4>"+
                        "<T5></T5>"+
                        "<T5_1></T5_1>"+
                        "<T5_2></T5_2>"+
                        "<T6></T6>"+
                        "<T6_1></T6_1>"+
                        "<T6_2></T6_2>"+
                        "<T7></T7>"+
                        "<T7_1></T7_1>"+
                        "<T7_2></T7_2>"+
                        "<T8></T8>"+
                        "<T8_1></T8_1>"+
                        "<T8_2></T8_2>"+
                        "<T9></T9>"+
                        "<T10></T10>"+
                        "<T11></T11>"+
                        "<T12></T12>"+
                        "<T13></T13>"+
                        "<T14></T14>"+
                        "<T15></T15>"+
                        "<T16></T16>"+
                        "<T17></T17>"+
                        "<T18></T18>"+
                     "</row1>"+
                  "</table2>"+
"                     "+
                  "<table3>"+
                     "<row1>"+
                        "<P1></P1>"+
                        "<P2></P2>"+
                        "<P3></P3>"+
                        "<P4></P4>"+
                        "<P25>0</P25>"+
                        "<P26>0</P26>"+
                        "<P5></P5>"+
                        "<P6></P6>"+
                        "<P7></P7>"+
                        "<P8></P8>"+
                        "<P9></P9>"+
                        "<P10></P10>"+
                        "<P11></P11>"+
                        "<P12></P12>"+
                        "<P13></P13>"+
                        "<P14></P14>"+
                        "<P15></P15>"+
                        "<P16></P16>"+
                        "<P17></P17>"+
                        "<P18></P18>"+
                        "<P19></P19>"+
                        "<P20></P20>"+
                        "<P21></P21>"+
                        "<P22></P22>"+
                        "<P23></P23>"+
                        "<P24></P24>"+
                     "</row1>"+
                  "</table3>"+
"                     "+
                  "<table6>"+
                     "<row>"+
                        "<t1></t1>"+
                        "<table61>"+
                           "<row1>"+
                              "<t1></t1>"+
                              "<t2></t2>"+
                           "</row1>"+
                        "</table61>"+
                     "</row>"+
                  "</table6>"+
"                     "+
"                     "+
                  "<P9_pop code=\"\" max=\"\" maxkod=\"\" min=\"\" name=\"\"></P9_pop>"+
                  "<T2_pop code=\"\" name=\"\"></T2_pop>"+
                  "<NAIM_SKLAD code=\"\" name=\"\"></NAIM_SKLAD>"+
                  "<zamech cod=\"\" max=\"\" name=\"\"></zamech>"+
"                     "+
"                     "+
                  "<table100></table100>"+
                  "<P49_1></P49_1>"+
                  "<P51_1></P51_1>"+
               "</data>"+
            "</xforms:instance>"+
         "</xforms:model>"+
      "</xformsmodels>"+
   "</global>"+
"</globalpage>"+
"<page sid=\"PAGE1\">"+
   "<global sid=\"global\">"+
      "<label>PAGE1</label>"+
      "<designer:pagesize>2960;1260</designer:pagesize>"+
      "<designer:backgroundimagealpha>0</designer:backgroundimagealpha>"+
   "</global>"+
   "<toolbar sid=\"TOOLBAR\">"+
      "<designer:height>93</designer:height>"+
   "</toolbar>"+
   "<box sid=\"BOX2\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>160</x>"+
         "<y>5</y>"+
         "<width>107</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</box>"+
   "<label sid=\"LABEL1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>182</x>"+
         "<y>5</y>"+
         "<width>64</width>"+
      "</itemlocation>"+
      "<value>0356845</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<button sid=\"BUTTON9\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>375</x>"+
         "<y>26</y>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>Страница 1</value>"+
      "<url>#PAGE1.global</url>"+
      "<type>pagedone</type>"+
      "<border>off</border>"+
      "<bgcolor>transparent</bgcolor>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>10</size>"+
         "<effect>italic</effect>"+
         "<effect>underline</effect>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</button>"+
   "<button sid=\"BUTTON10\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>485</x>"+
         "<y>26</y>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>Страница 2</value>"+
      "<url>#PAGE2.global</url>"+
      "<type>pagedone</type>"+
      "<border>off</border>"+
      "<bgcolor>transparent</bgcolor>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>10</size>"+
         "<effect>italic</effect>"+
         "<effect>underline</effect>"+
      "</fontinfo>"+
   "</button>"+
   "<label sid=\"LABEL2\">"+
      "<itemlocation>"+
         "<x>2190</x>"+
         "<y>9</y>"+
         "<width>132</width>"+
      "</itemlocation>"+
      "<value>Форма ТУ-3 ВЦЕ</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<label sid=\"LABEL3\">"+
      "<itemlocation>"+
         "<x>2198</x>"+
         "<y>30</y>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>Утверждена ОАО «РЖД» в 2004 году</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<line sid=\"LINE1\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>87</y>"+
         "<width>1608</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE2\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>66</y>"+
         "<height>548</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL4\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>87</y>"+
         "<width>47</width>"+
         "<height>66</height>"+
      "</itemlocation>"+
      "<value>Код сооб-"+
"щения</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE3\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>66</x>"+
         "<y>87</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL5\">"+
      "<itemlocation>"+
         "<x>66</x>"+
         "<y>87</y>"+
         "<width>296</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>Депо приписки (компания-собств.)</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL6\">"+
      "<itemlocation>"+
         "<x>66</x>"+
         "<y>123</y>"+
         "<width>154</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>локомотива</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL7\">"+
      "<itemlocation>"+
         "<x>219</x>"+
         "<y>123</y>"+
         "<width>143</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>бригады</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE4\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>219</x>"+
         "<y>124</y>"+
         "<height>138</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE5\">"+
      "<itemlocation>"+
         "<x>66</x>"+
         "<y>123</y>"+
         "<width>588</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE6\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>653</x>"+
         "<y>87</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE7\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>152</y>"+
         "<width>634</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL8\">"+
      "<itemlocation>"+
         "<x>361</x>"+
         "<y>87</y>"+
         "<width>293</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>Номера локомотивов или секций</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL9\">"+
      "<itemlocation>"+
         "<x>361</x>"+
         "<y>123</y>"+
         "<width>74</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>1</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL10\">"+
      "<itemlocation>"+
         "<x>434</x>"+
         "<y>123</y>"+
         "<width>74</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>2</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL11\">"+
      "<itemlocation>"+
         "<x>507</x>"+
         "<y>123</y>"+
         "<width>74</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>3</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL13\">"+
      "<itemlocation>"+
         "<x>580</x>"+
         "<y>123</y>"+
         "<width>74</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>4</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE8\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>434</x>"+
         "<y>124</y>"+
         "<height>138</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE9\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>507</x>"+
         "<y>124</y>"+
         "<height>138</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE10\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>580</x>"+
         "<y>124</y>"+
         "<height>138</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE11\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>361</x>"+
         "<y>87</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE12\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>66</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL14\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>66</y>"+
         "<width>1610</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>Р а з д е л    1.    Сведения о локомотиве и составе локомотивной бригады</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<field sid=\"FIELD1\">"+
      "<itemlocation>"+
         "<x>659</x>"+
         "<y>103</y>"+
         "<width>221</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<xforms:input ref=\"instance('INSTANCE')/P1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<line sid=\"LINE13\">"+
      "<itemlocation>"+
         "<x>659</x>"+
         "<y>123</y>"+
         "<width>221</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL15\">"+
      "<itemlocation>"+
         "<x>879</x>"+
         "<y>102</y>"+
         "<width>35</width>"+
         "<height>23</height>"+
      "</itemlocation>"+
      "<value>ж.д.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL16\">"+
      "<itemlocation>"+
         "<x>659</x>"+
         "<y>151</y>"+
         "<width>98</width>"+
         "<height>54</height>"+
      "</itemlocation>"+
      "<value>МАРШРУТ"+
"МАШИНИСТА</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<label sid=\"LABEL18\">"+
      "<itemlocation>"+
         "<x>760</x>"+
         "<y>151</y>"+
         "<width>35</width>"+
         "<height>23</height>"+
      "</itemlocation>"+
      "<value>№</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<field sid=\"FIELD2\">"+
      "<itemlocation>"+
         "<x>800</x>"+
         "<y>152</y>"+
         "<width>110</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<xforms:input ref=\"instance('INSTANCE')/P2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<line sid=\"LINE14\">"+
      "<itemlocation>"+
         "<x>800</x>"+
         "<y>172</y>"+
         "<width>110</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL19\">"+
      "<itemlocation>"+
         "<x>659</x>"+
         "<y>211</y>"+
         "<width>23</width>"+
         "<height>23</height>"+
      "</itemlocation>"+
      "<value>от</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<field sid=\"FIELD3\">"+
      "<itemlocation>"+
         "<x>683</x>"+
         "<y>212</y>"+
         "<width>68</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<xforms:input ref=\"instance('INSTANCE')/P3\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<line sid=\"LINE15\">"+
      "<itemlocation>"+
         "<x>683</x>"+
         "<y>232</y>"+
         "<width>68</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE16\">"+
      "<itemlocation>"+
         "<x>750</x>"+
         "<y>232</y>"+
         "<width>74</width>"+
      "</itemlocation>"+
   "</line>"+
   "<field sid=\"FIELD94\">"+
      "<itemlocation>"+
         "<x>748</x>"+
         "<y>212</y>"+
         "<width>76</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<xforms:input ref=\"instance('INSTANCE')/P4\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<label sid=\"LABEL20\">"+
      "<itemlocation>"+
         "<x>823</x>"+
         "<y>211</y>"+
         "<width>23</width>"+
         "<height>23</height>"+
      "</itemlocation>"+
      "<value>20</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<field sid=\"FIELD5\">"+
      "<itemlocation>"+
         "<x>845</x>"+
         "<y>212</y>"+
         "<width>43</width>"+
      "</itemlocation>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<xforms:input ref=\"instance('INSTANCE')/P5\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:input>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<line sid=\"LINE17\">"+
      "<itemlocation>"+
         "<x>845</x>"+
         "<y>232</y>"+
         "<width>43</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL21\">"+
      "<itemlocation>"+
         "<x>887</x>"+
         "<y>211</y>"+
         "<width>23</width>"+
         "<height>23</height>"+
      "</itemlocation>"+
      "<value>г.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE18\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>915</x>"+
         "<y>87</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL22\">"+
      "<itemlocation>"+
         "<x>915</x>"+
         "<y>87</y>"+
         "<width>248</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>Ф., и., о. машин.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE19\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1162</x>"+
         "<y>87</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL23\">"+
      "<itemlocation>"+
         "<x>1162</x>"+
         "<y>87</y>"+
         "<width>232</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>Ф., и., о. пом. маш.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE20\">"+
      "<itemlocation>"+
         "<x>915</x>"+
         "<y>123</y>"+
         "<width>714</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL24\">"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>87</y>"+
         "<width>237</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>Ф., и., о. 3-го лица</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE21\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>87</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE22\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1629</x>"+
         "<y>67</y>"+
         "<height>196</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE23\">"+
      "<itemlocation>"+
         "<x>915</x>"+
         "<y>210</y>"+
         "<width>714</width>"+
      "</itemlocation>"+
   "</line>"+
   "<field sid=\"FIELD6\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P13\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>914</x>"+
         "<y>122</y>"+
         "<width>249</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<label sid=\"LABEL25\">"+
      "<itemlocation>"+
         "<x>915</x>"+
         "<y>174</y>"+
         "<width>168</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>табельный номер</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE24\">"+
      "<itemlocation>"+
         "<x>915</x>"+
         "<y>174</y>"+
         "<width>714</width>"+
      "</itemlocation>"+
   "</line>"+
   "<field sid=\"FIELD7\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P14\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1162</x>"+
         "<y>122</y>"+
         "<width>232</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD8\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P15\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>122</y>"+
         "<width>237</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<label sid=\"LABEL26\">"+
      "<itemlocation>"+
         "<x>1082</x>"+
         "<y>174</y>"+
         "<width>81</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>должн. призн.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE25\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1082</x>"+
         "<y>174</y>"+
         "<height>88</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL27\">"+
      "<itemlocation>"+
         "<x>1162</x>"+
         "<y>174</y>"+
         "<width>146</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>табельный номер</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL28\">"+
      "<itemlocation>"+
         "<x>1307</x>"+
         "<y>174</y>"+
         "<width>87</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>должн. призн.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE26\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1307</x>"+
         "<y>174</y>"+
         "<height>88</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL30\">"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>174</y>"+
         "<width>155</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>табельный номер</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL31\">"+
      "<itemlocation>"+
         "<x>1547</x>"+
         "<y>174</y>"+
         "<width>83</width>"+
         "<height>37</height>"+
      "</itemlocation>"+
      "<value>должн. призн.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE27\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1547</x>"+
         "<y>174</y>"+
         "<height>88</height>"+
      "</itemlocation>"+
   "</line>"+
   "<field sid=\"FIELD9\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P6\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>152</y>"+
         "<width>47</width>"+
         "<height>111</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD10\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P7\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>66</x>"+
         "<y>152</y>"+
         "<width>153</width>"+
         "<height>111</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD11\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P8\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>218</x>"+
         "<y>152</y>"+
         "<width>143</width>"+
         "<height>111</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD16\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P16\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>915</x>"+
         "<y>210</y>"+
         "<width>168</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD17\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P18\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1162</x>"+
         "<y>210</y>"+
         "<width>145</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<popup sid=\"POPUP2\">"+
      "<itemlocation>"+
         "<x>436</x>"+
         "<y>158</y>"+
         "<width>69</width>"+
      "</itemlocation>"+
      "<xforms:select1 ref=\"instance('INSTANCE')/P10\">"+
         "<xforms:label></xforms:label>"+
         "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
            "<xforms:label></xforms:label>"+
            "<xforms:value ref=\"@name\"></xforms:value>"+
         "</xforms:itemset>"+
      "</xforms:select1>"+
      "<border>off</border>"+
   "</popup>"+
   "<field sid=\"FIELD14\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P10_1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>436</x>"+
         "<y>185</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<popup sid=\"POPUP3\">"+
      "<itemlocation>"+
         "<x>509</x>"+
         "<y>158</y>"+
         "<width>69</width>"+
      "</itemlocation>"+
      "<border>off</border>"+
      "<xforms:select1 ref=\"instance('INSTANCE')/P11\">"+
         "<xforms:label></xforms:label>"+
         "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
            "<xforms:label></xforms:label>"+
            "<xforms:value ref=\"@name\"></xforms:value>"+
         "</xforms:itemset>"+
      "</xforms:select1>"+
   "</popup>"+
   "<field sid=\"FIELD70\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P11_1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>509</x>"+
         "<y>185</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<popup sid=\"POPUP4\">"+
      "<itemlocation>"+
         "<x>583</x>"+
         "<y>158</y>"+
         "<width>69</width>"+
      "</itemlocation>"+
      "<border>off</border>"+
      "<xforms:select1 ref=\"instance('INSTANCE')/P12\">"+
         "<xforms:label></xforms:label>"+
         "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
            "<xforms:label></xforms:label>"+
            "<xforms:value ref=\"@name\"></xforms:value>"+
         "</xforms:itemset>"+
      "</xforms:select1>"+
   "</popup>"+
   "<field sid=\"FIELD73\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P12_1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>583</x>"+
         "<y>185</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD74\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P12_2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>583</x>"+
         "<y>210</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD71\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P11_2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>509</x>"+
         "<y>210</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD15\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P10_2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>436</x>"+
         "<y>210</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD18\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P20\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>210</y>"+
         "<width>155</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD19\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P17\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1082</x>"+
         "<y>210</y>"+
         "<width>80</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD20\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P19\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1307</x>"+
         "<y>210</y>"+
         "<width>87</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD21\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P21\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1547</x>"+
         "<y>210</y>"+
         "<width>83</width>"+
         "<height>53</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<line sid=\"LINE28\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>262</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<box sid=\"BOX1\">"+
      "<itemlocation>"+
         "<x>2321</x>"+
         "<y>9</y>"+
         "<width>107</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</box>"+
   "<label sid=\"LABEL32\">"+
      "<itemlocation>"+
         "<x>2343</x>"+
         "<y>9</y>"+
         "<width>64</width>"+
      "</itemlocation>"+
      "<value>0356845</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<label sid=\"LABEL33\">"+
      "<itemlocation>"+
         "<x>1629</x>"+
         "<y>66</y>"+
         "<width>812</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Р а з д е л    2.    Следование локомотивной бригады"+
"резервом в вагоне пассажирского поезда</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<line sid=\"LINE29\">"+
      "<itemlocation>"+
         "<x>1628</x>"+
         "<y>103</y>"+
         "<width>813</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL34\">"+
      "<itemlocation>"+
         "<x>1629</x>"+
         "<y>103</y>"+
         "<width>58</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Направ-"+
"ление</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE30\">"+
      "<itemlocation>"+
         "<x>1629</x>"+
         "<y>140</y>"+
         "<width>812</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE31\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1686</x>"+
         "<y>103</y>"+
         "<height>160</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL35\">"+
      "<itemlocation>"+
         "<x>1629</x>"+
         "<y>140</y>"+
         "<width>58</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<value>туда</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL36\">"+
      "<itemlocation>"+
         "<x>1629</x>"+
         "<y>200</y>"+
         "<width>58</width>"+
         "<height>63</height>"+
      "</itemlocation>"+
      "<value>обратно</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE32\">"+
      "<itemlocation>"+
         "<x>1629</x>"+
         "<y>200</y>"+
         "<width>812</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL37\">"+
      "<itemlocation>"+
         "<x>1686</x>"+
         "<y>103</y>"+
         "<width>46</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Строка</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL38\">"+
      "<itemlocation>"+
         "<x>1686</x>"+
         "<y>140</y>"+
         "<width>46</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<value>1</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL39\">"+
      "<itemlocation>"+
         "<x>1686</x>"+
         "<y>200</y>"+
         "<width>46</width>"+
         "<height>63</height>"+
      "</itemlocation>"+
      "<value>2</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL40\">"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>103</y>"+
         "<width>73</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Дата,"+
"№ поезда</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE33\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>103</y>"+
         "<height>160</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL41\">"+
      "<itemlocation>"+
         "<x>1803</x>"+
         "<y>103</y>"+
         "<width>202</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Станция отправления</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE34\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1803</x>"+
         "<y>103</y>"+
         "<height>160</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL42\">"+
      "<itemlocation>"+
         "<x>2004</x>"+
         "<y>103</y>"+
         "<width>116</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Время отправления</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE35\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2004</x>"+
         "<y>103</y>"+
         "<height>160</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL43\">"+
      "<itemlocation>"+
         "<x>2119</x>"+
         "<y>103</y>"+
         "<width>207</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Станция назначения</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE36\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2119</x>"+
         "<y>103</y>"+
         "<height>160</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL44\">"+
      "<itemlocation>"+
         "<x>2325</x>"+
         "<y>103</y>"+
         "<width>116</width>"+
         "<height>38</height>"+
      "</itemlocation>"+
      "<value>Время прибытия</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE37\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2325</x>"+
         "<y>103</y>"+
         "<height>160</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE39\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2440</x>"+
         "<y>66</y>"+
         "<height>548</height>"+
      "</itemlocation>"+
   "</line>"+
   "<field sid=\"FIELD22\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P22\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>140</y>"+
         "<width>73</width>"+
         "<height>33</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<field sid=\"FIELD23\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P23\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1803</x>"+
         "<y>140</y>"+
         "<width>202</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD188\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P22_2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>172</y>"+
         "<width>73</width>"+
         "<height>29</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<field sid=\"FIELD24\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P24\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2004</x>"+
         "<y>140</y>"+
         "<width>116</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<field sid=\"FIELD25\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P25\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2119</x>"+
         "<y>140</y>"+
         "<width>207</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD26\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P26\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2325</x>"+
         "<y>140</y>"+
         "<width>116</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<field sid=\"FIELD27\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P27\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>201</y>"+
         "<width>73</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<field sid=\"FIELD28\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P28\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1803</x>"+
         "<y>201</y>"+
         "<width>202</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD303\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P27_2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>230</y>"+
         "<width>73</width>"+
         "<height>32</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<field sid=\"FIELD29\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P29\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2004</x>"+
         "<y>201</y>"+
         "<width>116</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<field sid=\"FIELD30\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P30\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2119</x>"+
         "<y>201</y>"+
         "<width>207</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<readonly>on</readonly>"+
   "</field>"+
   "<field sid=\"FIELD31\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P31\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2325</x>"+
         "<y>201</y>"+
         "<width>116</width>"+
         "<height>61</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
   "</field>"+
   "<label sid=\"LABEL46\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>262</y>"+
         "<width>2421</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>Р а з д е л    3.    Сведения о рабочем времени и отдыхе бригады</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<label sid=\"LABEL49\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>291</y>"+
         "<width>81</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Путь следования</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE40\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>291</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL50\">"+
      "<itemlocation>"+
         "<x>100</x>"+
         "<y>291</y>"+
         "<width>47</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Строка</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE41\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>100</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL51\">"+
      "<itemlocation>"+
         "<x>146</x>"+
         "<y>291</y>"+
         "<width>112</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Время явки на работу</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL52\">"+
      "<itemlocation>"+
         "<x>257</x>"+
         "<y>291</y>"+
         "<width>119</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Время начала приёмки локомотива</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL53\">"+
      "<itemlocation>"+
         "<x>375</x>"+
         "<y>291</y>"+
         "<width>259</width>"+
         "<height>34</height>"+
      "</itemlocation>"+
      "<value>При смене на путях</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL54\">"+
      "<itemlocation>"+
         "<x>375</x>"+
         "<y>324</y>"+
         "<width>132</width>"+
         "<height>91</height>"+
      "</itemlocation>"+
      "<value>время оконч. сдачи локомомтива сдающей бригадой</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL55\">"+
      "<itemlocation>"+
         "<x>506</x>"+
         "<y>324</y>"+
         "<width>128</width>"+
         "<height>91</height>"+
      "</itemlocation>"+
      "<value>номер поезда и станция, откуда прибыл локомотив</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL56\">"+
      "<itemlocation>"+
         "<x>633</x>"+
         "<y>291</y>"+
         "<width>74</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Признак смены бригады на начальной станции</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL57\">"+
      "<itemlocation>"+
         "<x>706</x>"+
         "<y>291</y>"+
         "<width>253</width>"+
         "<height>34</height>"+
      "</itemlocation>"+
      "<value>Время проследования КП</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL58\">"+
      "<itemlocation>"+
         "<x>706</x>"+
         "<y>324</y>"+
         "<width>125</width>"+
         "<height>91</height>"+
      "</itemlocation>"+
      "<value>при выходе из депо</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL59\">"+
      "<itemlocation>"+
         "<x>830</x>"+
         "<y>324</y>"+
         "<width>129</width>"+
         "<height>91</height>"+
      "</itemlocation>"+
      "<value>при заходе в депо</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE42\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>146</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE43\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>257</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE44\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>375</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE45\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>506</x>"+
         "<y>324</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE46\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>633</x>"+
         "<y>291</y>"+
         "<height>124</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE47\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>706</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE48\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>958</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE49\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>830</x>"+
         "<y>324</y>"+
         "<height>175</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE50\">"+
      "<itemlocation>"+
         "<x>375</x>"+
         "<y>324</y>"+
         "<width>259</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE51\">"+
      "<itemlocation>"+
         "<x>706</x>"+
         "<y>324</y>"+
         "<width>253</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE52\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>414</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL60\">"+
      "<itemlocation>"+
         "<x>659</x>"+
         "<y>124</y>"+
         "<width>221</width>"+
         "<height>18</height>"+
      "</itemlocation>"+
      "<value>(приписки локомотива)</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>6</size>"+
      "</fontinfo>"+
   "</label>"+
   "<label sid=\"LABEL61\">"+
      "<itemlocation>"+
         "<x>958</x>"+
         "<y>291</y>"+
         "<width>147</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Время окончания сдачи локомотива и станция, куда следует локомотив</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE53\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1177</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE54\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1104</x>"+
         "<y>291</y>"+
         "<height>124</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL62\">"+
      "<itemlocation>"+
         "<x>1104</x>"+
         "<y>291</y>"+
         "<width>74</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Признак смены бригады на конеч. станции</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL63\">"+
      "<itemlocation>"+
         "<x>1177</x>"+
         "<y>291</y>"+
         "<width>144</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Время окончания работы локомотивной бригады</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL64\">"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>291</y>"+
         "<width>236</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>При заходе в оборотное депо</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL65\">"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>312</y>"+
         "<width>236</width>"+
         "<height>58</height>"+
      "</itemlocation>"+
      "<value>Момент проследования КП предыдущей бригадой, следовавшей с данным локомомтивом</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL66\">"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>369</y>"+
         "<width>74</width>"+
         "<height>46</height>"+
      "</itemlocation>"+
      "<value>дата</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL67\">"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>369</y>"+
         "<width>74</width>"+
         "<height>46</height>"+
      "</itemlocation>"+
      "<value>месяц</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL68\">"+
      "<itemlocation>"+
         "<x>1466</x>"+
         "<y>369</y>"+
         "<width>90</width>"+
         "<height>46</height>"+
      "</itemlocation>"+
      "<value>время</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL69\">"+
      "<itemlocation>"+
         "<x>1555</x>"+
         "<y>291</y>"+
         "<width>147</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>Простой</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL70\">"+
      "<itemlocation>"+
         "<x>1555</x>"+
         "<y>312</y>"+
         "<width>74</width>"+
         "<height>103</height>"+
      "</itemlocation>"+
      "<value>в ремонте</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL71\">"+
      "<itemlocation>"+
         "<x>1628</x>"+
         "<y>312</y>"+
         "<width>74</width>"+
         "<height>103</height>"+
      "</itemlocation>"+
      "<value>в ожидании работы</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL72\">"+
      "<itemlocation>"+
         "<x>1701</x>"+
         "<y>291</y>"+
         "<width>209</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Норма времени установленной продолжительности непрерывной работы локомотивной бригады за поездку</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL73\">"+
      "<itemlocation>"+
         "<x>1909</x>"+
         "<y>291</y>"+
         "<width>133</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Фактическое время начала отдыха</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL74\">"+
      "<itemlocation>"+
         "<x>2041</x>"+
         "<y>291</y>"+
         "<width>400</width>"+
         "<height>124</height>"+
      "</itemlocation>"+
      "<value>Причина непредоставления отдыха</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE55\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE56\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1555</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE57\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1701</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE58\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1909</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE59\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1628</x>"+
         "<y>312</y>"+
         "<height>187</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE60\">"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>312</y>"+
         "<width>382</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE61\">"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>369</y>"+
         "<width>236</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE62\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>369</y>"+
         "<height>130</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE63\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1466</x>"+
         "<y>369</y>"+
         "<height>130</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE64\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2041</x>"+
         "<y>291</y>"+
         "<height>208</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE65\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>456</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE66\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>498</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL75\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>414</y>"+
         "<width>81</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>Туда</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL76\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>456</y>"+
         "<width>81</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>Обратно</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL78\">"+
      "<itemlocation>"+
         "<x>100</x>"+
         "<y>414</y>"+
         "<width>47</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>1</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL79\">"+
      "<itemlocation>"+
         "<x>100</x>"+
         "<y>456</y>"+
         "<width>47</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>2</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<field sid=\"FIELD32\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P32\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>146</x>"+
         "<y>414</y>"+
         "<width>112</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD33\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P33\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>257</x>"+
         "<y>414</y>"+
         "<width>119</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD34\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P34\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>375</x>"+
         "<y>414</y>"+
         "<width>132</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD35\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P35\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>506</x>"+
         "<y>414</y>"+
         "<width>122</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<label sid=\"LABEL80\">"+
      "<itemlocation>"+
         "<x>624</x>"+
         "<y>414</y>"+
         "<width>19</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>/</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>11</size>"+
      "</fontinfo>"+
   "</label>"+
   "<field sid=\"FIELD36\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P36\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>642</x>"+
         "<y>414</y>"+
         "<width>65</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD37\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P37\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>706</x>"+
         "<y>414</y>"+
         "<width>125</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD38\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P38\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>830</x>"+
         "<y>414</y>"+
         "<width>129</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD39\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P39\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>958</x>"+
         "<y>414</y>"+
         "<width>136</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<label sid=\"LABEL81\">"+
      "<itemlocation>"+
         "<x>1093</x>"+
         "<y>414</y>"+
         "<width>19</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>/</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>11</size>"+
      "</fontinfo>"+
   "</label>"+
   "<field sid=\"FIELD40\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P40\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1111</x>"+
         "<y>414</y>"+
         "<width>67</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD41\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P41\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1177</x>"+
         "<y>414</y>"+
         "<width>144</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD42\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P42\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>414</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD43\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P43\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>414</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD44\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P44\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1466</x>"+
         "<y>414</y>"+
         "<width>90</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD45\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P45\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1555</x>"+
         "<y>414</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD46\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P46\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1628</x>"+
         "<y>414</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD47\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P47\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1701</x>"+
         "<y>414</y>"+
         "<width>209</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD48\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P48\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1909</x>"+
         "<y>414</y>"+
         "<width>133</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD49\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P49\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2041</x>"+
         "<y>414</y>"+
         "<width>400</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD50\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P50\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>146</x>"+
         "<y>456</y>"+
         "<width>112</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD51\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P51\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>257</x>"+
         "<y>456</y>"+
         "<width>119</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD52\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P52\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>375</x>"+
         "<y>456</y>"+
         "<width>132</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD53\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P53\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>506</x>"+
         "<y>456</y>"+
         "<width>122</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<label sid=\"LABEL82\">"+
      "<itemlocation>"+
         "<x>624</x>"+
         "<y>456</y>"+
         "<width>19</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>/</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>11</size>"+
      "</fontinfo>"+
   "</label>"+
   "<field sid=\"FIELD54\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P54\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>642</x>"+
         "<y>456</y>"+
         "<width>65</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD55\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P55\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>706</x>"+
         "<y>456</y>"+
         "<width>125</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD56\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P56\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>830</x>"+
         "<y>456</y>"+
         "<width>129</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD57\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P57\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>958</x>"+
         "<y>456</y>"+
         "<width>136</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<label sid=\"LABEL83\">"+
      "<itemlocation>"+
         "<x>1093</x>"+
         "<y>456</y>"+
         "<width>19</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>/</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>11</size>"+
      "</fontinfo>"+
   "</label>"+
   "<field sid=\"FIELD58\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P58\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1111</x>"+
         "<y>456</y>"+
         "<width>67</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD59\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P59\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1177</x>"+
         "<y>456</y>"+
         "<width>144</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD60\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P60\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1320</x>"+
         "<y>456</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD61\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P61\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1393</x>"+
         "<y>456</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD62\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P62\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1466</x>"+
         "<y>456</y>"+
         "<width>90</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD63\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P63\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1555</x>"+
         "<y>456</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD64\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P64\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1628</x>"+
         "<y>456</y>"+
         "<width>74</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD65\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P65\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1701</x>"+
         "<y>456</y>"+
         "<width>209</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD66\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P66\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>1909</x>"+
         "<y>456</y>"+
         "<width>133</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD67\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P67\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>2041</x>"+
         "<y>456</y>"+
         "<width>400</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<label sid=\"LABEL84\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>498</y>"+
         "<width>2420</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>Р а з д е л    4.    Сведения о локомотивах, работающих в подталкивании, двойной тяге и пересылаемых</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<line sid=\"LINE68\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>527</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE69\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>613</y>"+
         "<width>2421</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL86\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>527</y>"+
         "<width>48</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>Строка</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL87\">"+
      "<itemlocation>"+
         "<x>67</x>"+
         "<y>527</y>"+
         "<width>385</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>Вид следования"+
"(дв. тяга, подталкивание, пересылка, сплотка)</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL88\">"+
      "<itemlocation>"+
         "<x>451</x>"+
         "<y>527</y>"+
         "<width>106</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>Код вида следования</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL89\">"+
      "<itemlocation>"+
         "<x>556</x>"+
         "<y>527</y>"+
         "<width>92</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>Код депо приписки локомотива</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL90\">"+
      "<itemlocation>"+
         "<x>647</x>"+
         "<y>550</y>"+
         "<width>101</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>1</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL91\">"+
      "<itemlocation>"+
         "<x>647</x>"+
         "<y>527</y>"+
         "<width>400</width>"+
         "<height>24</height>"+
      "</itemlocation>"+
      "<value>Номер локомотивов или секций</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL92\">"+
      "<itemlocation>"+
         "<x>747</x>"+
         "<y>550</y>"+
         "<width>100</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>2</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL93\">"+
      "<itemlocation>"+
         "<x>846</x>"+
         "<y>550</y>"+
         "<width>103</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>3</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL94\">"+
      "<itemlocation>"+
         "<x>948</x>"+
         "<y>550</y>"+
         "<width>99</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>4</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL95\">"+
      "<itemlocation>"+
         "<x>1046</x>"+
         "<y>527</y>"+
         "<width>448</width>"+
         "<height>24</height>"+
      "</itemlocation>"+
      "<value>От станции или км</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL96\">"+
      "<itemlocation>"+
         "<x>1046</x>"+
         "<y>550</y>"+
         "<width>380</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>наименование</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL97\">"+
      "<itemlocation>"+
         "<x>1425</x>"+
         "<y>550</y>"+
         "<width>69</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>код</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL98\">"+
      "<itemlocation>"+
         "<x>1493</x>"+
         "<y>527</y>"+
         "<width>476</width>"+
         "<height>24</height>"+
      "</itemlocation>"+
      "<value>До станции или км</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL99\">"+
      "<itemlocation>"+
         "<x>1493</x>"+
         "<y>550</y>"+
         "<width>410</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>наименование</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL100\">"+
      "<itemlocation>"+
         "<x>1902</x>"+
         "<y>550</y>"+
         "<width>67</width>"+
         "<height>64</height>"+
      "</itemlocation>"+
      "<value>код</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL101\">"+
      "<itemlocation>"+
         "<x>1968</x>"+
         "<y>527</y>"+
         "<width>473</width>"+
         "<height>24</height>"+
      "</itemlocation>"+
      "<value>Для локомотивов, пересыл. без бригад</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL102\">"+
      "<itemlocation>"+
         "<x>1968</x>"+
         "<y>550</y>"+
         "<width>238</width>"+
         "<height>36</height>"+
      "</itemlocation>"+
      "<value>заход в депо или прибытие лок-ва на станцию</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL103\">"+
      "<itemlocation>"+
         "<x>2205</x>"+
         "<y>550</y>"+
         "<width>236</width>"+
         "<height>36</height>"+
      "</itemlocation>"+
      "<value>выход из депо</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL104\">"+
      "<itemlocation>"+
         "<x>1968</x>"+
         "<y>585</y>"+
         "<width>62</width>"+
         "<height>29</height>"+
      "</itemlocation>"+
      "<value>дата</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL105\">"+
      "<itemlocation>"+
         "<x>2029</x>"+
         "<y>585</y>"+
         "<width>81</width>"+
         "<height>29</height>"+
      "</itemlocation>"+
      "<value>месяц</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL106\">"+
      "<itemlocation>"+
         "<x>2109</x>"+
         "<y>585</y>"+
         "<width>97</width>"+
         "<height>29</height>"+
      "</itemlocation>"+
      "<value>время</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL107\">"+
      "<itemlocation>"+
         "<x>2205</x>"+
         "<y>585</y>"+
         "<width>78</width>"+
         "<height>29</height>"+
      "</itemlocation>"+
      "<value>дата</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL108\">"+
      "<itemlocation>"+
         "<x>2282</x>"+
         "<y>585</y>"+
         "<width>73</width>"+
         "<height>29</height>"+
      "</itemlocation>"+
      "<value>месяц</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL109\">"+
      "<itemlocation>"+
         "<x>2354</x>"+
         "<y>585</y>"+
         "<width>87</width>"+
         "<height>29</height>"+
      "</itemlocation>"+
      "<value>время</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE71\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>67</x>"+
         "<y>527</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE72\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>451</x>"+
         "<y>527</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE73\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>556</x>"+
         "<y>527</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE74\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>647</x>"+
         "<y>527</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE75\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1046</x>"+
         "<y>527</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE76\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1493</x>"+
         "<y>527</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE77\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1968</x>"+
         "<y>527</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE81\">"+
      "<itemlocation>"+
         "<x>647</x>"+
         "<y>550</y>"+
         "<width>1793</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE82\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>747</x>"+
         "<y>550</y>"+
         "<height>64</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE83\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>846</x>"+
         "<y>550</y>"+
         "<height>64</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE84\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>948</x>"+
         "<y>550</y>"+
         "<height>64</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE85\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1425</x>"+
         "<y>550</y>"+
         "<height>64</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE86\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1902</x>"+
         "<y>550</y>"+
         "<height>64</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE87\">"+
      "<itemlocation>"+
         "<x>1968</x>"+
         "<y>585</y>"+
         "<width>473</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE88\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2029</x>"+
         "<y>585</y>"+
         "<height>29</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE89\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2109</x>"+
         "<y>585</y>"+
         "<height>29</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE90\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2205</x>"+
         "<y>550</y>"+
         "<height>64</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE91\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2282</x>"+
         "<y>585</y>"+
         "<height>29</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE92\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2354</x>"+
         "<y>585</y>"+
         "<height>29</height>"+
      "</itemlocation>"+
   "</line>"+
   "<pane sid=\"TABLE1_PANE\">"+
      "<xforms:group ref=\"instance('INSTANCE')/table2/row1\">"+
         "<xforms:label></xforms:label>"+
         "<spacer sid=\"HEADER_SPACER1\">"+
            "<itemlocation>"+
               "<width>40</width>"+
               "<height></height>"+
               "<offsetx>6</offsetx>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER2\">"+
            "<itemlocation>"+
               "<width>380</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER1</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER3\">"+
            "<itemlocation>"+
               "<width>101</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER2</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER4\">"+
            "<itemlocation>"+
               "<width>87</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER3</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER5\">"+
            "<itemlocation>"+
               "<width>96</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER4</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER6\">"+
            "<itemlocation>"+
               "<width>95</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER5</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER7\">"+
            "<itemlocation>"+
               "<width>98</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER6</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER8\">"+
            "<itemlocation>"+
               "<width>94</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER7</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER9\">"+
            "<itemlocation>"+
               "<width>375</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER8</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER10\">"+
            "<itemlocation>"+
               "<width>64</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER9</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER11\">"+
            "<itemlocation>"+
               "<width>405</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER10</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER12\">"+
            "<itemlocation>"+
               "<width>62</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER11</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER13\">"+
            "<itemlocation>"+
               "<width>57</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER12</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER14\">"+
            "<itemlocation>"+
               "<width>76</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER13</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER15\">"+
            "<itemlocation>"+
               "<width>92</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER14</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER16\">"+
            "<itemlocation>"+
               "<width>73</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER15</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER17\">"+
            "<itemlocation>"+
               "<width>68</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER16</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER18\">"+
            "<itemlocation>"+
               "<width>81</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER17</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<table sid=\"TABLE1_TABLE\">"+
            "<xforms:repeat id=\"TABLE11\" nodeset=\"instance('INSTANCE')/table2/row1\">"+
               "<pane sid=\"ROW_GROUP\">"+
                  "<xforms:group ref=\".\">"+
                     "<xforms:label></xforms:label>"+
                     "<spacer sid=\"setHeight\">"+
                        "<itemlocation>"+
                           "<offsety>2</offsety>"+
                           "<expandheight>2</expandheight>"+
                        "</itemlocation>"+
                     "</spacer>"+
                     "<field sid=\"T1\">"+
                        "<xforms:input ref=\"T1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<x>0</x>"+
                           "<width>40</width>"+
                           "<height>81</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<line sid=\"LINE70\">"+
                        "<itemlocation>"+
                           "<below>T1</below>"+
                           "<offsety>-5</offsety>"+
                           "<width>2411</width>"+
                        "</itemlocation>"+
                     "</line>"+
                     "<field sid=\"T2\">"+
                        "<xforms:input ref=\"T2\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>359</width>"+
                           "<after>T1</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<popup sid=\"POPUP5\">"+
                        "<xforms:select1 ref=\"T2\">"+
                           "<xforms:label>POPUP1</xforms:label>"+
                           "<xforms:itemset nodeset=\"instance('INSTANCE')/T2_pop\">"+
                              "<xforms:label ref=\"\"></xforms:label>"+
                              "<xforms:value ref=\"@name\"></xforms:value>"+
                           "</xforms:itemset>"+
                        "</xforms:select1>"+
                        "<itemlocation>"+
                           "<height>22</height>"+
                           "<width>18</width>"+
                           "<after>T2</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</popup>"+
                     "<field sid=\"T3\">"+
                        "<xforms:input ref=\"T3\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>101</width>"+
                           "<after>POPUP5</after>"+
                           "<!--<after>T2</after>-->"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T4\">"+
                        "<xforms:input ref=\"T4\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>87</width>"+
                           "<after>T3</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<popup sid=\"T5\">"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<after>T4</after>"+
                        "</itemlocation>"+
                        "<xforms:select1 ref=\"T5\">"+
                           "<xforms:label></xforms:label>"+
                           "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
                              "<xforms:label></xforms:label>"+
                              "<xforms:value ref=\"@name\"></xforms:value>"+
                           "</xforms:itemset>"+
                        "</xforms:select1>"+
                        "<border>off</border>"+
                     "</popup>"+
                     "<field sid=\"FIELD12\">"+
                        "<xforms:textarea ref=\"T5_1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>T5</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<field sid=\"FIELD13\">"+
                        "<xforms:textarea ref=\"T5_2\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>FIELD12</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<popup sid=\"T6\">"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<after>T5</after>"+
                        "</itemlocation>"+
                        "<xforms:select1 ref=\"T6\">"+
                           "<xforms:label></xforms:label>"+
                           "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
                              "<xforms:label></xforms:label>"+
                              "<xforms:value ref=\"@name\"></xforms:value>"+
                           "</xforms:itemset>"+
                        "</xforms:select1>"+
                        "<border>off</border>"+
                     "</popup>"+
                     "<field sid=\"FIELD69\">"+
                        "<xforms:textarea ref=\"T6_1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>T6</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<field sid=\"FIELD72\">"+
                        "<xforms:textarea ref=\"T6_2\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>FIELD69</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<popup sid=\"T7\">"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<after>T6</after>"+
                        "</itemlocation>"+
                        "<xforms:select1 ref=\"T7\">"+
                           "<xforms:label></xforms:label>"+
                           "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
                              "<xforms:label></xforms:label>"+
                              "<xforms:value ref=\"@name\"></xforms:value>"+
                           "</xforms:itemset>"+
                        "</xforms:select1>"+
                        "<border>off</border>"+
                     "</popup>"+
                     "<field sid=\"FIELD75\">"+
                        "<xforms:textarea ref=\"T7_1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>T7</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<field sid=\"FIELD76\">"+
                        "<xforms:textarea ref=\"T7_2\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>FIELD75</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<popup sid=\"T8\">"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<after>T7</after>"+
                        "</itemlocation>"+
                        "<xforms:select1 ref=\"T8\">"+
                           "<xforms:label></xforms:label>"+
                           "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
                              "<xforms:label></xforms:label>"+
                              "<xforms:value ref=\"@name\"></xforms:value>"+
                           "</xforms:itemset>"+
                        "</xforms:select1>"+
                        "<border>off</border>"+
                     "</popup>"+
                     "<field sid=\"FIELD77\">"+
                        "<xforms:textarea ref=\"T8_1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>T8</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<field sid=\"FIELD78\">"+
                        "<xforms:textarea ref=\"T8_2\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<itemlocation>"+
                           "<width>96</width>"+
                           "<height>22</height>"+
                           "<below>FIELD77</below>"+
                        "</itemlocation>"+
                        "<size>"+
                           "<width>30</width>"+
                           "<height>4</height>"+
                        "</size>"+
                        "<scrollvert>never</scrollvert>"+
                        "<scrollhoriz>wordwrap</scrollhoriz>"+
                        "<border>off</border>"+
                        "<justify>center</justify>"+
                        "<format>"+
                           "<datatype>string</datatype>"+
                           "<constraints>"+
                              "<mandatory>on</mandatory>"+
                           "</constraints>"+
                        "</format>"+
                     "</field>"+
                     "<field sid=\"T9\">"+
                        "<xforms:input ref=\"T9\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>375</width>"+
                           "<after>T8</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T10\">"+
                        "<xforms:input ref=\"T10\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>64</width>"+
                           "<after>T9</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T11\">"+
                        "<xforms:input ref=\"T11\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>405</width>"+
                           "<after>T10</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T12\">"+
                        "<xforms:input ref=\"T12\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>62</width>"+
                           "<after>T11</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T13\">"+
                        "<xforms:input ref=\"T13\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>57</width>"+
                           "<after>T12</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T14\">"+
                        "<xforms:input ref=\"T14\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>76</width>"+
                           "<after>T13</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T15\">"+
                        "<xforms:input ref=\"T15\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>92</width>"+
                           "<after>T14</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T16\">"+
                        "<xforms:input ref=\"T16\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>73</width>"+
                           "<after>T15</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T17\">"+
                        "<xforms:input ref=\"T17\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>68</width>"+
                           "<after>T16</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"T18\">"+
                        "<xforms:input ref=\"T18\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>81</width>"+
                           "<after>T17</after>"+
                           "<height>73</height>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                  "</xforms:group>"+
                  "<padding>1</padding>"+
               "</pane>"+
            "</xforms:repeat>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>1</y>"+
            "</itemlocation>"+
            "<rowpadding>-2</rowpadding>"+
         "</table>"+
         "<box sid=\"BORDER\">"+
            "<itemlocation>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<alignl2l>TABLE1_TABLE</alignl2l>"+
               "<expandr2r>TABLE1_TABLE</expandr2r>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
            "</itemlocation>"+
         "</box>"+
         "<line sid=\"COLUMN_DIVIDER1\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER1</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER2\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER2</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER3\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER3</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER4\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER4</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER5\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER5</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER6\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER6</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER7\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER7</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER8\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER8</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER9\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER9</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER10\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER10</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER11\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER11</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER12\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER12</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER13\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER13</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER14\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER14</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER15\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER15</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER16\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER16</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER17\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER17</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<x>16</x>"+
         "<y>610</y>"+
         "<width>2435</width>"+
      "</itemlocation>"+
   "</pane>"+
   "<popup sid=\"POPUP1\">"+
      "<itemlocation>"+
         "<x>363</x>"+
         "<y>158</y>"+
         "<width>69</width>"+
      "</itemlocation>"+
      "<xforms:select1 ref=\"instance('INSTANCE')/P9\">"+
         "<xforms:label></xforms:label>"+
         "<xforms:itemset nodeset=\"instance('INSTANCE')/P9_pop\">"+
            "<xforms:label></xforms:label>"+
            "<xforms:value ref=\"@name\"></xforms:value>"+
         "</xforms:itemset>"+
      "</xforms:select1>"+
      "<border>off</border>"+
   "</popup>"+
   "<field sid=\"FIELD4\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P9_1\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>363</x>"+
         "<y>184</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD68\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P9_2\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>363</x>"+
         "<y>209</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<pane sid=\"PANE2\">"+
      "<xforms:group>"+
         "<xforms:label></xforms:label>"+
         "<line sid=\"LINE97\">"+
            "<itemlocation>"+
               "<x>6</x>"+
               "<y>2</y>"+
               "<width>1920</width>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL138\">"+
            "<itemlocation>"+
               "<x>4</x>"+
               "<y>2</y>"+
               "<width>1922</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>Р а з д е л    5.    Сведения о наборе и расходе топлива или электроэнергии</value>"+
            "<justify>center</justify>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
         "</label>"+
         "<line sid=\"LINE108\">"+
            "<itemlocation>"+
               "<x>4</x>"+
               "<y>116</y>"+
               "<width>1921</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE110\">"+
            "<itemlocation>"+
               "<x>4</x>"+
               "<y>30</y>"+
               "<width>1922</width>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL147\">"+
            "<itemlocation>"+
               "<x>817</x>"+
               "<y>30</y>"+
               "<width>416</width>"+
               "<height>29</height>"+
            "</itemlocation>"+
            "<value>Показания счётчика</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL151\">"+
            "<itemlocation>"+
               "<x>1029</x>"+
               "<y>58</y>"+
               "<width>204</width>"+
               "<height>29</height>"+
            "</itemlocation>"+
            "<value>электроотопл. ваг.</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL152\">"+
            "<itemlocation>"+
               "<x>1029</x>"+
               "<y>86</y>"+
               "<width>98</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>принято</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL153\">"+
            "<itemlocation>"+
               "<x>1127</x>"+
               "<y>86</y>"+
               "<width>107</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>сдано</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL154\">"+
            "<itemlocation>"+
               "<x>1233</x>"+
               "<y>30</y>"+
               "<width>221</width>"+
               "<height>29</height>"+
            "</itemlocation>"+
            "<value>Марка топлива</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL155\">"+
            "<itemlocation>"+
               "<x>1233</x>"+
               "<y>86</y>"+
               "<width>41</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>код</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL156\">"+
            "<itemlocation>"+
               "<x>1273</x>"+
               "<y>86</y>"+
               "<width>71</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>кол-во</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL157\">"+
            "<itemlocation>"+
               "<x>1343</x>"+
               "<y>86</y>"+
               "<width>41</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>код</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL158\">"+
            "<itemlocation>"+
               "<x>1383</x>"+
               "<y>86</y>"+
               "<width>71</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>кол-во</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL159\">"+
            "<itemlocation>"+
               "<x>1453</x>"+
               "<y>86</y>"+
               "<width>41</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>код</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL160\">"+
            "<itemlocation>"+
               "<x>1493</x>"+
               "<y>86</y>"+
               "<width>71</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>кол-во</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL161\">"+
            "<itemlocation>"+
               "<x>1453</x>"+
               "<y>30</y>"+
               "<width>111</width>"+
               "<height>29</height>"+
            "</itemlocation>"+
            "<value>Марка масла</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL162\">"+
            "<itemlocation>"+
               "<x>1563</x>"+
               "<y>30</y>"+
               "<width>62</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Признак заправки</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL163\">"+
            "<itemlocation>"+
               "<x>1623</x>"+
               "<y>30</y>"+
               "<width>62</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Снято топлива по акту</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL164\">"+
            "<itemlocation>"+
               "<x>1685</x>"+
               "<y>30</y>"+
               "<width>139</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Наименование склада</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL165\">"+
            "<itemlocation>"+
               "<x>1823</x>"+
               "<y>30</y>"+
               "<width>88</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>№ суточных требований и дата набора топлива</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL166\">"+
            "<itemlocation>"+
               "<x>1909</x>"+
               "<y>30</y>"+
               "<width>183</width>"+
               "<height>29</height>"+
            "</itemlocation>"+
            "<value>Время (час., мин.)</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL167\">"+
            "<itemlocation>"+
               "<x>1909</x>"+
               "<y>58</y>"+
               "<width>90</width>"+
               "<height>58</height>"+
            "</itemlocation>"+
            "<value>прибытия лок-ва на склад топлива</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL168\">"+
            "<itemlocation>"+
               "<x>1999</x>"+
               "<y>58</y>"+
               "<width>94</width>"+
               "<height>58</height>"+
            "</itemlocation>"+
            "<value>окончания набора топлива</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<line sid=\"LINE117\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1233</x>"+
               "<y>32</y>"+
               "<height>84</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE118\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1453</x>"+
               "<y>32</y>"+
               "<height>84</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE119\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1563</x>"+
               "<y>30</y>"+
               "<height>85</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE120\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1623</x>"+
               "<y>30</y>"+
               "<height>85</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE121\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1685</x>"+
               "<y>32</y>"+
               "<height>84</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE122\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1823</x>"+
               "<y>32</y>"+
               "<height>84</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE123\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1909</x>"+
               "<y>32</y>"+
               "<height>85</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE125\">"+
            "<itemlocation>"+
               "<x>819</x>"+
               "<y>58</y>"+
               "<width>745</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE126\">"+
            "<itemlocation>"+
               "<x>1909</x>"+
               "<y>58</y>"+
               "<width>183</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE130\">"+
            "<itemlocation>"+
               "<x>819</x>"+
               "<y>86</y>"+
               "<width>745</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE132\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1127</x>"+
               "<y>86</y>"+
               "<height>30</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE133\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1273</x>"+
               "<y>86</y>"+
               "<height>30</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE134\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1343</x>"+
               "<y>58</y>"+
               "<height>57</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE135\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1383</x>"+
               "<y>86</y>"+
               "<height>30</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE136\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1493</x>"+
               "<y>86</y>"+
               "<height>30</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE137\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1999</x>"+
               "<y>58</y>"+
               "<height>59</height>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL141\">"+
            "<itemlocation>"+
               "<x>4</x>"+
               "<y>30</y>"+
               "<width>79</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Путь следования</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL142\">"+
            "<itemlocation>"+
               "<x>82</x>"+
               "<y>30</y>"+
               "<width>101</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Номер локомотивов или секций</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL143\">"+
            "<itemlocation>"+
               "<x>483</x>"+
               "<y>30</y>"+
               "<width>238</width>"+
               "<height>29</height>"+
            "</itemlocation>"+
            "<value>Принято</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL144\">"+
            "<itemlocation>"+
               "<x>483</x>"+
               "<y>58</y>"+
               "<width>118</width>"+
               "<height>58</height>"+
            "</itemlocation>"+
            "<value>от машиниста при постановке локомотива в депо</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL145\">"+
            "<itemlocation>"+
               "<x>599</x>"+
               "<y>58</y>"+
               "<width>121</width>"+
               "<height>58</height>"+
            "</itemlocation>"+
            "<value>машинистом от депо или при смене на путях</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL146\">"+
            "<itemlocation>"+
               "<x>719</x>"+
               "<y>30</y>"+
               "<width>99</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Сдано после поездки</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL148\">"+
            "<itemlocation>"+
               "<x>817</x>"+
               "<y>58</y>"+
               "<width>213</width>"+
               "<height>29</height>"+
            "</itemlocation>"+
            "<value>рекуперация</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL149\">"+
            "<itemlocation>"+
               "<x>817</x>"+
               "<y>86</y>"+
               "<width>107</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>принято</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL150\">"+
            "<itemlocation>"+
               "<x>923</x>"+
               "<y>86</y>"+
               "<width>107</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>сдано</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<line sid=\"LINE114\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>82</x>"+
               "<y>30</y>"+
               "<height>86</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE115\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>483</x>"+
               "<y>30</y>"+
               "<height>87</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE94\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>4</x>"+
               "<y>3</y>"+
               "<height>114</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE116\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>719</x>"+
               "<y>32</y>"+
               "<height>85</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE124\">"+
            "<itemlocation>"+
               "<x>483</x>"+
               "<y>58</y>"+
               "<width>238</width>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE127\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>599</x>"+
               "<y>58</y>"+
               "<height>58</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE128\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>817</x>"+
               "<y>30</y>"+
               "<height>86</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE129\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>923</x>"+
               "<y>86</y>"+
               "<height>30</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE131\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1029</x>"+
               "<y>58</y>"+
               "<height>58</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE96\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>2092</x>"+
               "<y>3</y>"+
               "<height>112</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE79\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>181</x>"+
               "<y>31</y>"+
               "<height>87</height>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL45\">"+
            "<itemlocation>"+
               "<x>180</x>"+
               "<y>31</y>"+
               "<width>59</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Код вида тяги</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL47\">"+
            "<itemlocation>"+
               "<x>239</x>"+
               "<y>31</y>"+
               "<width>77</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Код вида следования</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<line sid=\"LINE98\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>239</x>"+
               "<y>30</y>"+
               "<height>87</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE100\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>315</x>"+
               "<y>30</y>"+
               "<height>87</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE101\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>403</x>"+
               "<y>30</y>"+
               "<height>87</height>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL48\">"+
            "<itemlocation>"+
               "<x>315</x>"+
               "<y>30</y>"+
               "<width>88</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Неисправный счётчик</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL77\">"+
            "<itemlocation>"+
               "<x>404</x>"+
               "<y>31</y>"+
               "<width>81</width>"+
               "<height>86</height>"+
            "</itemlocation>"+
            "<value>Не учавствует в тяге</value>"+
            "<justify>center</justify>"+
         "</label>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<below>TABLE1_PANE</below>"+
         "<offsetx>-4</offsetx>"+
         "<offsety>-6</offsety>"+
      "</itemlocation>"+
   "</pane>"+
   "<field sid=\"FIELD95\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P9_3\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>363</x>"+
         "<y>235</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD96\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P10_3\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>436</x>"+
         "<y>235</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD97\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P11_3\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>509</x>"+
         "<y>235</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<field sid=\"FIELD98\">"+
      "<xforms:textarea ref=\"instance('INSTANCE')/P12_3\">"+
         "<xforms:label></xforms:label>"+
      "</xforms:textarea>"+
      "<itemlocation>"+
         "<x>583</x>"+
         "<y>235</y>"+
         "<width>69</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<size>"+
         "<width>30</width>"+
         "<height>4</height>"+
      "</size>"+
      "<scrollvert>never</scrollvert>"+
      "<scrollhoriz>wordwrap</scrollhoriz>"+
      "<border>off</border>"+
      "<justify>center</justify>"+
      "<format>"+
         "<datatype>string</datatype>"+
         "<constraints>"+
            "<mandatory>on</mandatory>"+
         "</constraints>"+
      "</format>"+
      "<value></value>"+
   "</field>"+
   "<pane sid=\"TABLE1_PANE2\">"+
      "<xforms:group ref=\"instance('INSTANCE')/table3/row1\">"+
         "<xforms:label></xforms:label>"+
         "<spacer sid=\"HEADER_SPACER1\">"+
            "<itemlocation>"+
               "<width>74</width>"+
               "<height></height>"+
               "<offsetx>6</offsetx>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER2\">"+
            "<itemlocation>"+
               "<width>95</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER1</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER3\">"+
            "<itemlocation>"+
               "<width>54</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER2</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER4\">"+
            "<itemlocation>"+
               "<width>72</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER3</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER24\">"+
            "<itemlocation>"+
               "<width>83</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER4</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER25\">"+
            "<itemlocation>"+
               "<width>76</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER24</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER5\">"+
            "<itemlocation>"+
               "<width>112</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER25</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER6\">"+
            "<itemlocation>"+
               "<width>117</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER5</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER7\">"+
            "<itemlocation>"+
               "<width>94</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER6</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER8\">"+
            "<itemlocation>"+
               "<width>102</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER7</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER9\">"+
            "<itemlocation>"+
               "<width>102</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER8</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER10\">"+
            "<itemlocation>"+
               "<width>93</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER9</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER11\">"+
            "<itemlocation>"+
               "<width>102</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER10</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER12\">"+
            "<itemlocation>"+
               "<width>36</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER11</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER13\">"+
            "<itemlocation>"+
               "<width>66</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER12</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER14\">"+
            "<itemlocation>"+
               "<width>36</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER13</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER15\">"+
            "<itemlocation>"+
               "<width>66</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER14</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER16\">"+
            "<itemlocation>"+
               "<width>36</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER15</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER17\">"+
            "<itemlocation>"+
               "<width>66</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER16</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER18\">"+
            "<itemlocation>"+
               "<width>57</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER17</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER19\">"+
            "<itemlocation>"+
               "<width>57</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER18</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER20\">"+
            "<itemlocation>"+
               "<width>134</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER19</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER21\">"+
            "<itemlocation>"+
               "<width>83</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER20</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER22\">"+
            "<itemlocation>"+
               "<width>85</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER21</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER23\">"+
            "<itemlocation>"+
               "<width>89</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER22</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<table sid=\"TABLE1_TABLE\">"+
            "<xforms:repeat id=\"TABLE14\" nodeset=\"instance('INSTANCE')/table3/row1\">"+
               "<pane sid=\"ROW_GROUP\">"+
                  "<xforms:group ref=\".\">"+
                     "<xforms:label></xforms:label>"+
                     "<spacer sid=\"setHeight\">"+
                        "<itemlocation>"+
                           "<offsety>2</offsety>"+
                           "<expandheight>2</expandheight>"+
                        "</itemlocation>"+
                     "</spacer>"+
                     "<field sid=\"P1\">"+
                        "<itemlocation>"+
                           "<x>0</x>"+
                           "<width>74</width>"+
                           "<height>52</height>"+
                        "</itemlocation>"+
                        "<xforms:textarea ref=\"P1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:textarea>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<line sid=\"LINE99\">"+
                        "<itemlocation>"+
                           "<below>P1</below>"+
                           "<offsety>-5</offsety>"+
                           "<width>1920</width>"+
                        "</itemlocation>"+
                     "</line>"+
                     "<field sid=\"P2\">"+
                        "<xforms:input ref=\"P2\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>95</width>"+
                           "<height>52</height>"+
                           "<after>P1</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P3\">"+
                        "<xforms:input ref=\"P3\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>54</width>"+
                           "<height>52</height>"+
                           "<after>P2</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P4\">"+
                        "<xforms:input ref=\"P4\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>72</width>"+
                           "<height>52</height>"+
                           "<after>P3</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<check sid=\"CHECK1\">"+
                        "<xforms:input ref=\"P25\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<after>P4</after>"+
                           "<offsetx>35</offsetx>"+
                        "</itemlocation>"+
                     "</check>"+
                     "<check sid=\"CHECK2\">"+
                        "<xforms:input ref=\"P26\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<after>CHECK1</after>"+
                           "<offsetx>65</offsetx>"+
                        "</itemlocation>"+
                     "</check>"+
                     "<field sid=\"P25\">"+
                        "<xforms:input ref=\"P25\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>83</width>"+
                           "<height>22</height>"+
                           "<after>P4</after>"+
                           "<offsety>29</offsety>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<custom:option xfdl:compute=\"value == 'true' ? set('value', '1') : value == '1' ? set('value', '1') : set('value', '0')\"></custom:option>"+
                        "<visible>off</visible>"+
                        "<printvisible>off</printvisible>"+
                     "</field>"+
                     "<field sid=\"P26\">"+
                        "<xforms:input ref=\"P26\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>76</width>"+
                           "<height>22</height>"+
                           "<after>P25</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                        "<custom:option xfdl:compute=\"value == 'true' ? set('value', '1') : value == '1' ? set('value', '1') : set('value', '0')\"></custom:option>"+
                        "<visible>off</visible>"+
                        "<printvisible>off</printvisible>"+
                     "</field>"+
                     "<field sid=\"P5\">"+
                        "<xforms:input ref=\"P5\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>112</width>"+
                           "<height>52</height>"+
                           "<after>P26</after>"+
                           "<offsety>-35</offsety>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P6\">"+
                        "<xforms:input ref=\"P6\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>117</width>"+
                           "<height>52</height>"+
                           "<after>P5</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P7\">"+
                        "<xforms:input ref=\"P7\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>94</width>"+
                           "<height>52</height>"+
                           "<after>P6</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P8\">"+
                        "<xforms:input ref=\"P8\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>102</width>"+
                           "<height>52</height>"+
                           "<after>P7</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P9\">"+
                        "<xforms:input ref=\"P9\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>102</width>"+
                           "<height>52</height>"+
                           "<after>P8</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P10\">"+
                        "<xforms:input ref=\"P10\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>93</width>"+
                           "<height>52</height>"+
                           "<after>P9</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P11\">"+
                        "<xforms:input ref=\"P11\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>102</width>"+
                           "<height>52</height>"+
                           "<after>P10</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P12\">"+
                        "<xforms:input ref=\"P12\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>36</width>"+
                           "<height>52</height>"+
                           "<after>P11</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P13\">"+
                        "<xforms:input ref=\"P13\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>66</width>"+
                           "<height>52</height>"+
                           "<after>P12</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P14\">"+
                        "<xforms:input ref=\"P14\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>36</width>"+
                           "<height>52</height>"+
                           "<after>P13</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P15\">"+
                        "<xforms:input ref=\"P15\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>66</width>"+
                           "<height>52</height>"+
                           "<after>P14</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P16\">"+
                        "<xforms:input ref=\"P16\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>36</width>"+
                           "<height>52</height>"+
                           "<after>P15</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P17\">"+
                        "<xforms:input ref=\"P17\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>66</width>"+
                           "<height>52</height>"+
                           "<after>P16</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P18\">"+
                        "<xforms:input ref=\"P18\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>57</width>"+
                           "<height>52</height>"+
                           "<after>P17</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P19\">"+
                        "<xforms:input ref=\"P19\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>57</width>"+
                           "<height>52</height>"+
                           "<after>P18</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<popup sid=\"P20\">"+
                        "<xforms:input ref=\"P20\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>134</width>"+
                           "<after>P19</after>"+
                        "</itemlocation>"+
                        "<xforms:select1 ref=\"P20\">"+
                           "<xforms:label></xforms:label>"+
                           "<xforms:itemset nodeset=\"instance('INSTANCE')/NAIM_SKLAD\">"+
                              "<xforms:label></xforms:label>"+
                              "<xforms:value ref=\"@name\"></xforms:value>"+
                           "</xforms:itemset>"+
                        "</xforms:select1>"+
                        "<border>off</border>"+
                     "</popup>"+
                     "<field sid=\"P21\">"+
                        "<xforms:input ref=\"P21\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>83</width>"+
                           "<after>P20</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P22\">"+
                        "<xforms:input ref=\"P22\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>83</width>"+
                           "<below>P21</below>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P23\">"+
                        "<xforms:input ref=\"P23\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>85</width>"+
                           "<height>52</height>"+
                           "<after>P21</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                     "<field sid=\"P24\">"+
                        "<xforms:input ref=\"P24\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>89</width>"+
                           "<height>52</height>"+
                           "<after>P23</after>"+
                        "</itemlocation>"+
                        "<border>off</border>"+
                     "</field>"+
                  "</xforms:group>"+
                  "<padding>1</padding>"+
               "</pane>"+
            "</xforms:repeat>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>1</y>"+
            "</itemlocation>"+
            "<rowpadding>-2</rowpadding>"+
         "</table>"+
         "<box sid=\"BORDER\">"+
            "<itemlocation>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<alignl2l>TABLE1_TABLE</alignl2l>"+
               "<expandr2r>TABLE1_TABLE</expandr2r>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
            "</itemlocation>"+
         "</box>"+
         "<line sid=\"COLUMN_DIVIDER1\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER1</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER2\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER2</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER3\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER3</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER4\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER4</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER23\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER24</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER24\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER25</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER5\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER5</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER6\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER6</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER7\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER7</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER8\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER8</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER9\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER9</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER10\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER10</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER11\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER11</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER12\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER12</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER13\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER13</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER14\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER14</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER15\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER15</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER16\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER16</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER17\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER17</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER18\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER18</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER19\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER19</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER20\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER20</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER21\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER21</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER22\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER22</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<below>PANE2</below>"+
         "<width>1944</width>"+
         "<offsety>-6</offsety>"+
      "</itemlocation>"+
   "</pane>"+
   "<pane sid=\"PANE1\">"+
      "<xforms:group>"+
         "<xforms:label></xforms:label>"+
         "<line sid=\"LINE111\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>1</y>"+
               "<width>1702</width>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL85\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>1</y>"+
               "<width>1702</width>"+
               "<height>30</height>"+
            "</itemlocation>"+
            "<value>Р а з д е л    6</value>"+
            "<justify>center</justify>"+
            "<fontinfo>"+
               "<fontname>Arial</fontname>"+
               "<size>8</size>"+
               "<effect>bold</effect>"+
            "</fontinfo>"+
         "</label>"+
         "<line sid=\"LINE93\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>30</y>"+
               "<width>1702</width>"+
            "</itemlocation>"+
         "</line>"+
         "<label sid=\"LABEL111\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>30</y>"+
               "<width>524</width>"+
               "<height>24</height>"+
            "</itemlocation>"+
            "<value>Замечания</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL110\">"+
            "<itemlocation>"+
               "<x>1094</x>"+
               "<y>30</y>"+
               "<width>608</width>"+
               "<height>24</height>"+
            "</itemlocation>"+
            "<value>Значение параметра</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<label sid=\"LABEL112\">"+
            "<itemlocation>"+
               "<x>522</x>"+
               "<y>30</y>"+
               "<width>573</width>"+
               "<height>24</height>"+
            "</itemlocation>"+
            "<value>Наименование параметра</value>"+
            "<justify>center</justify>"+
         "</label>"+
         "<line sid=\"LINE95\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1094</x>"+
               "<y>31</y>"+
               "<height>23</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE38\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>2</y>"+
               "<height>52</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE67\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>1701</x>"+
               "<y>2</y>"+
               "<height>52</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE80\">"+
            "<size>"+
               "<height>1</height>"+
               "<width>0</width>"+
            "</size>"+
            "<itemlocation>"+
               "<x>522</x>"+
               "<y>33</y>"+
               "<height>22</height>"+
            "</itemlocation>"+
         "</line>"+
         "<line sid=\"LINE78\">"+
            "<itemlocation>"+
               "<x>0</x>"+
               "<y>53</y>"+
               "<width>1702</width>"+
            "</itemlocation>"+
         "</line>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<below>TABLE1_PANE2</below>"+
      "</itemlocation>"+
   "</pane>"+
   "<pane sid=\"TABLE1_PANE1\">"+
      "<xforms:group ref=\"instance('INSTANCE')/table6/row\">"+
         "<xforms:label></xforms:label>"+
         "<spacer sid=\"HEADER_SPACER1\">"+
            "<itemlocation>"+
               "<width>518</width>"+
               "<height></height>"+
               "<offsetx>6</offsetx>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER2\">"+
            "<itemlocation>"+
               "<width>100</width>"+
               "<height></height>"+
               "<after>HEADER_LABEL1</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<table sid=\"TABLE1_TABLE\">"+
            "<xforms:repeat id=\"TABLE12\" nodeset=\"instance('INSTANCE')/table6/row\">"+
               "<pane sid=\"ROW_GROUP\">"+
                  "<xforms:group ref=\".\">"+
                     "<xforms:label></xforms:label>"+
                     "<spacer sid=\"setHeight\">"+
                        "<itemlocation>"+
                           "<offsety>2</offsety>"+
                           "<expandheight>2</expandheight>"+
                        "</itemlocation>"+
                     "</spacer>"+
                     "<popup sid=\"T1\">"+
                        "<itemlocation>"+
                           "<x>0</x>"+
                           "<width>518</width>"+
                           "<height>22</height>"+
                        "</itemlocation>"+
                        "<xforms:select1 ref=\"t1\">"+
                           "<xforms:label></xforms:label>"+
                           "<xforms:itemset nodeset=\"instance('INSTANCE')/zamech\">"+
                              "<xforms:label></xforms:label>"+
                              "<xforms:value ref=\"@name\"></xforms:value>"+
                           "</xforms:itemset>"+
                        "</xforms:select1>"+
                     "</popup>"+
                     "<pane sid=\"TABLE1_PANE\">"+
                        "<xforms:group ref=\".\">"+
                           "<xforms:label></xforms:label>"+
                           "<spacer sid=\"HEADER_SPACER1\">"+
                              "<itemlocation>"+
                                 "<width>559</width>"+
                                 "<height></height>"+
                                 "<offsetx>6</offsetx>"+
                              "</itemlocation>"+
                           "</spacer>"+
                           "<spacer sid=\"HEADER_SPACER2\">"+
                              "<itemlocation>"+
                                 "<width>600</width>"+
                                 "<height>0</height>"+
                                 "<after>HEADER_SPACER1</after>"+
                              "</itemlocation>"+
                           "</spacer>"+
                           "<table sid=\"TABLE1_TABLE\">"+
                              "<xforms:repeat id=\"TABLE13\" nodeset=\"table61/row1\">"+
                                 "<pane sid=\"ROW_GROUP\">"+
                                    "<xforms:group ref=\".\">"+
                                       "<xforms:label></xforms:label>"+
                                       "<spacer sid=\"setHeight\">"+
                                          "<itemlocation>"+
                                             "<offsety>2</offsety>"+
                                             "<expandheight>2</expandheight>"+
                                          "</itemlocation>"+
                                       "</spacer>"+
                                       "<field sid=\"T1\">"+
                                          "<xforms:input ref=\"t1\">"+
                                             "<xforms:label></xforms:label>"+
                                          "</xforms:input>"+
                                          "<itemlocation>"+
                                             "<x>0</x>"+
                                             "<width>561</width>"+
                                          "</itemlocation>"+
                                       "</field>"+
                                       "<field sid=\"T2\">"+
                                          "<xforms:input ref=\"t2\">"+
                                             "<xforms:label></xforms:label>"+
                                          "</xforms:input>"+
                                          "<itemlocation>"+
                                             "<width>600</width>"+
                                             "<after>T1</after>"+
                                          "</itemlocation>"+
                                       "</field>"+
                                    "</xforms:group>"+
                                    "<padding>1</padding>"+
                                    "<itemlocation></itemlocation>"+
                                 "</pane>"+
                              "</xforms:repeat>"+
                              "<itemlocation>"+
                                 "<x>1</x>"+
                                 "<y>1</y>"+
                              "</itemlocation>"+
                              "<rowpadding>-2</rowpadding>"+
                           "</table>"+
                           "<box sid=\"BORDER\">"+
                              "<itemlocation>"+
                                 "<alignt2t>HEADER_SPACER1</alignt2t>"+
                                 "<alignl2l>TABLE1_TABLE</alignl2l>"+
                                 "<expandr2r>TABLE1_TABLE</expandr2r>"+
                                 "<expandb2b>TABLE1_TABLE</expandb2b>"+
                              "</itemlocation>"+
                           "</box>"+
                           "<line sid=\"COLUMN_DIVIDER1\">"+
                              "<itemlocation>"+
                                 "<after>HEADER_SPACER1</after>"+
                                 "<alignt2t>HEADER_SPACER1</alignt2t>"+
                                 "<expandb2b>TABLE1_TABLE</expandb2b>"+
                                 "<offsetx>-2</offsetx>"+
                              "</itemlocation>"+
                              "<size>"+
                                 "<width>0</width>"+
                                 "<height>1</height>"+
                              "</size>"+
                           "</line>"+
                        "</xforms:group>"+
                        "<itemlocation>"+
                           "<after>T1</after>"+
                           "<width>1169</width>"+
                        "</itemlocation>"+
                     "</pane>"+
                  "</xforms:group>"+
                  "<padding>1</padding>"+
                  "<itemlocation></itemlocation>"+
               "</pane>"+
            "</xforms:repeat>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>1</y>"+
               "<width>1705</width>"+
            "</itemlocation>"+
            "<rowpadding>-2</rowpadding>"+
         "</table>"+
         "<box sid=\"BORDER\">"+
            "<itemlocation>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<alignl2l>TABLE1_TABLE</alignl2l>"+
               "<expandr2r>TABLE1_TABLE</expandr2r>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
            "</itemlocation>"+
         "</box>"+
         "<line sid=\"COLUMN_DIVIDER1\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER1</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<button sid=\"ADD\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table6\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:insert at=\"last()\" nodeset=\"row\" position=\"after\"></xforms:insert>"+
                  "<xforms:setvalue ref=\"row[last()]/t1\" value=\"\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/table61/row1/t1\" value=\"\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last()]/table61/row1/t2\" value=\"\"></xforms:setvalue>"+
                  "<xforms:delete at=\"2\" nodeset=\"row[last()]/table61/row1[last()>1]\"></xforms:delete>"+
                  "<xforms:delete at=\"2\" nodeset=\"row[last()]/table61/row1[last()>1]\"></xforms:delete>"+
                  "<xforms:delete at=\"2\" nodeset=\"row[last()]/table61/row1[last()>1]\"></xforms:delete>"+
                  "<xforms:setfocus control=\"TABLE12\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>TABLE1_TABLE</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>+</value>"+
         "</button>"+
         "<button sid=\"REMOVE\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table6\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:setvalue ref=\"row[last() = 1]/t1\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row[last() = 1]/table61\"></xforms:setvalue>"+
                  "<xforms:delete at=\"index('TABLE12')\" nodeset=\"row[last() > 1]\"></xforms:delete>"+
                  "<xforms:setfocus control=\"TABLE12\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>ADD</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>-</value>"+
         "</button>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<below>PANE1</below>"+
         "<offsetx>-1</offsetx>"+
      "</itemlocation>"+
   "</pane>"+
   "<button sid=\"BUTTON1\">"+
      "<itemlocation>"+
         "<x>141</x>"+
         "<y>1067</y>"+
         "<width>462</width>"+
         "<height>27</height>"+
         "<below>TABLE1_PANE1</below>"+
         "<offsetx>120</offsetx>"+
         "<offsety>32</offsety>"+
      "</itemlocation>"+
      "<value compute=\"signer=='' ? 'Добавить подпись' : signer\">Добавить подпись</value>"+
      "<type>signature</type>"+
      "<signature>BUTTON1_SIGNATURE_1977412494</signature>"+
      "<signer></signer>"+
      "<signformat>application/vnd.xfdl;engine=\"CryptoAPI\";csp=\"Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider\";csptype=75;layoutoverlaptest=\"none\";delete=\"off\";layoutoverlaptest=\"none\"</signformat>"+
      "<size>"+
         "<width>40</width>"+
         "<height>1</height>"+
      "</size>"+
      "<signinstance>"+
         "<filter>omit</filter>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/table100</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/P41_1</ref>"+
         "</dataref>"+
         "<dataref>"+
            "<model></model>"+
            "<ref>instance('INSTANCE')/P59_1</ref>"+
         "</dataref>"+
      "</signinstance>"+
      "<signoptions>"+
         "<filter>omit</filter>"+
         "<optiontype>triggeritem</optiontype>"+
         "<optiontype>coordinates</optiontype>"+
      "</signoptions>"+
   "</button>"+
   "<label sid=\"LABEL113\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>1067</y>"+
         "<width>103</width>"+
         "<height>27</height>"+
         "<below>BUTTON1</below>"+
         "<offsetx>-115</offsetx>"+
         "<offsety>-32</offsety>"+
      "</itemlocation>"+
      "<value>Машинист:</value>"+
   "</label>"+
   "<line sid=\"LINE102\">"+
      "<itemlocation>"+
         "<y>75</y>"+
         "<x>16</x>"+
         "<width>41</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE103\">"+
      "<itemlocation>"+
         "<x>16</x>"+
         "<y>95</y>"+
         "<width>41</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE104\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>56</x>"+
         "<y>75</y>"+
         "<height>22</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE105\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>36</x>"+
         "<y>75</y>"+
         "<height>22</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE106\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>16</x>"+
         "<y>75</y>"+
         "<height>22</height>"+
      "</itemlocation>"+
   "</line>"+
   "<spacer sid=\"vfd_spacer\">"+
      "<itemlocation>"+
         "<x>2892</x>"+
         "<y>807</y>"+
         "<width>19</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
   "</spacer>"+
   "<button sid=\"exit\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>971</x>"+
         "<y>28</y>"+
         "<width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<type>done</type>"+
      "<value>Отмена / Закрыть</value>"+
      "<url>forms/cancel.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
      "<transmititemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') : ''\"></custom:option>"+
      "<custom:option1 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON1.signer>'' ? set('PAGE1.L1.value','1'):''\"></custom:option1>"+
      "<saveformat>application/vnd.xfdl;content-encoding=base64-gzip</saveformat>"+
   "</button>"+
   "<button sid=\"button_save_exit\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>817</x>"+
         "<y>28</y>"+
         "<width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<type>done</type>"+
      "<value>Сохранить и выйти</value>"+
      "<url>forms/save.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
      "<transmititemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') : ''\"></custom:option>"+
      "<saveformat>application/vnd.xfdl;content-encoding=base64-gzip</saveformat>"+
      "<custom:option1 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON1.signer>'' ? set('PAGE1.L1.value','1'):''\"></custom:option1>"+
      "<custom:option2 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? set('PAGE1.REZ1.value', PAGE1.FIELD12.value) : ''\"></custom:option2>"+
   "</button>"+
   "<button sid=\"button_save\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>688</x>"+
         "<y>28</y>"+
         "<width>130</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<value>Сохранить</value>"+
      "<type>replace</type>"+
      "<url>javascript:saveandnoexit()</url>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') +. set('global.global.dirtyflag','off') : ''\"></custom:option>"+
      "<custom:option2 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? set('PAGE1.REZ1.value', PAGE1.FIELD12.value) : ''\"></custom:option2>"+
   "</button>"+
   "<label sid=\"LABEL12\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>24</x>"+
         "<y>54</y>"+
         "<width>101</width>"+
      "</itemlocation>"+
      "<value>Версия 3.46.20</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>6</size>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<label sid=\"LABEL17\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>24</x>"+
         "<y>30</y>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>Утверждена ОАО «РЖД» в 2004 году</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<label sid=\"LABEL29\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>24</x>"+
         "<y>5</y>"+
         "<width>132</width>"+
      "</itemlocation>"+
      "<value>Форма ТУ-3 ВЦЕ</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
"</page>"+
"<page sid=\"PAGE2\">"+
   "<global sid=\"global\">"+
      "<label>PAGE2</label>"+
      "<designer:pagesize>2300;1260</designer:pagesize>"+
   "</global>"+
   "<toolbar sid=\"TOOLBAR\">"+
      "<designer:height>82</designer:height>"+
   "</toolbar>"+
   "<label sid=\"LABEL29\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>24</x>"+
         "<y>5</y>"+
         "<width>132</width>"+
      "</itemlocation>"+
      "<value>Форма ТУ-3 ВЦЕ</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<button sid=\"BUTTON2\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>375</x>"+
         "<y>26</y>"+
         "<height>26</height>"+
      "</itemlocation>"+
      "<value>Страница 1</value>"+
      "<url>#PAGE1.global</url>"+
      "<type>pagedone</type>"+
      "<border>off</border>"+
      "<bgcolor>transparent</bgcolor>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>10</size>"+
         "<effect>italic</effect>"+
         "<effect>underline</effect>"+
      "</fontinfo>"+
   "</button>"+
   "<button sid=\"BUTTON3\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>485</x>"+
         "<y>27</y>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<value>Страница 2</value>"+
      "<url>#PAGE2.global</url>"+
      "<type>pagedone</type>"+
      "<border>off</border>"+
      "<bgcolor>transparent</bgcolor>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>10</size>"+
         "<effect>italic</effect>"+
         "<effect>underline</effect>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</button>"+
   "<label sid=\"LABEL14\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>24</y>"+
         "<width>2475</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>Р а з д е л    7.    Сведения о ходе, весе и составе поезда</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
   "</label>"+
   "<line sid=\"LINE1\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>24</y>"+
         "<width>2822</width>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL45\">"+
      "<itemlocation>"+
         "<x>2494</x>"+
         "<y>24</y>"+
         "<width>348</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>ТУ-3 ВЦЕ</value>"+
      "<justify>center</justify>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
      "</fontinfo>"+
   "</label>"+
   "<line sid=\"LINE2\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>24</y>"+
         "<height>150</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE3\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>45</y>"+
         "<width>2822</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE6\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>54</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL4\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>45</y>"+
         "<width>35</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value></value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE7\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>312</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE4\">"+
      "<itemlocation>"+
         "<x>20</x>"+
         "<y>173</y>"+
         "<width>2822</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE8\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>365</x>"+
         "<y>74</y>"+
         "<height>100</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL2\">"+
      "<itemlocation>"+
         "<x>54</x>"+
         "<y>45</y>"+
         "<width>259</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Наименование станций, остановочных пунктов или км, где остановился поезд</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE9\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>421</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL3\">"+
      "<itemlocation>"+
         "<x>312</x>"+
         "<y>45</y>"+
         "<width>110</width>"+
         "<height>30</height>"+
      "</itemlocation>"+
      "<value>Время</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE10\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>468</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL5\">"+
      "<itemlocation>"+
         "<x>312</x>"+
         "<y>74</y>"+
         "<width>54</width>"+
         "<height>100</height>"+
      "</itemlocation>"+
      "<value></value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE11\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>592</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL6\">"+
      "<itemlocation>"+
         "<x>365</x>"+
         "<y>74</y>"+
         "<width>57</width>"+
         "<height>100</height>"+
      "</itemlocation>"+
      "<value></value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE13\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>669</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL7\">"+
      "<itemlocation>"+
         "<x>421</x>"+
         "<y>45</y>"+
         "<width>48</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value></value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE14\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>728</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<spacer sid=\"vfd_spacer\">"+
      "<itemlocation>"+
         "<x>2300</x>"+
         "<y>1260</y>"+
         "<width>1</width>"+
         "<height>1</height>"+
      "</itemlocation>"+
   "</spacer>"+
   "<box sid=\"BOX2\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>160</x>"+
         "<y>5</y>"+
         "<width>107</width>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</box>"+
   "<label sid=\"LABEL1\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>182</x>"+
         "<y>5</y>"+
         "<width>64</width>"+
      "</itemlocation>"+
      "<value>0356845</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<button sid=\"exit\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>971</x>"+
         "<y>28</y>"+
         "<width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<type>done</type>"+
      "<value>Отмена / Закрыть</value>"+
      "<url>forms/cancel.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
      "<transmititemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') : ''\"></custom:option>"+
      "<custom:option1 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON1.signer>'' ? set('PAGE1.L1.value','1'):''\"></custom:option1>"+
      "<saveformat>application/vnd.xfdl;content-encoding=base64-gzip</saveformat>"+
   "</button>"+
   "<button sid=\"button_save_exit\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>817</x>"+
         "<y>28</y>"+
         "<width>154</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<type>done</type>"+
      "<value>Сохранить и выйти</value>"+
      "<url>forms/save.form</url>"+
      "<datagroup>"+
         "<datagroupref></datagroupref>"+
      "</datagroup>"+
      "<transmititemrefs>"+
         "<filter>keep</filter>"+
         "<itemref>PAGE1.FIELD1</itemref>"+
      "</transmititemrefs>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') : ''\"></custom:option>"+
      "<saveformat>application/vnd.xfdl;content-encoding=base64-gzip</saveformat>"+
      "<custom:option1 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' and PAGE1.BUTTON1.signer>'' ? set('PAGE1.L1.value','1'):''\"></custom:option1>"+
      "<custom:option2 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? set('PAGE1.REZ1.value', PAGE1.FIELD12.value) : ''\"></custom:option2>"+
   "</button>"+
   "<button sid=\"button_save\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>688</x>"+
         "<y>28</y>"+
         "<width>130</width>"+
         "<height>25</height>"+
      "</itemlocation>"+
      "<value>Сохранить</value>"+
      "<type>replace</type>"+
      "<url>javascript:saveandnoexit()</url>"+
      "<custom:option xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? forms.SaveLocal('1') +. set('global.global.dirtyflag','off') : ''\"></custom:option>"+
      "<custom:option2 xfdl:compute=\"toggle(activated, 'off', 'on') == '1' ? set('PAGE1.REZ1.value', PAGE1.FIELD12.value) : ''\"></custom:option2>"+
   "</button>"+
   "<label sid=\"LABEL17\">"+
      "<itemlocation>"+
         "<within>TOOLBAR</within>"+
         "<x>24</x>"+
         "<y>30</y>"+
         "<height>22</height>"+
      "</itemlocation>"+
      "<value>Утверждена ОАО «РЖД» в 2004 году</value>"+
      "<fontinfo>"+
         "<fontname>Arial</fontname>"+
         "<size>8</size>"+
         "<effect>bold</effect>"+
      "</fontinfo>"+
      "<printvisible>on</printvisible>"+
      "<visible>on</visible>"+
   "</label>"+
   "<line sid=\"LINE15\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>787</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE16\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>846</x>"+
         "<y>87</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE17\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>903</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE18\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>971</x>"+
         "<y>87</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE22\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1033</x>"+
         "<y>87</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE21\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1095</x>"+
         "<y>87</y>"+
         "<height>87</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE23\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1153</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE25\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1206</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE27\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1281</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE28\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1356</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE29\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1431</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE30\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1506</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE31\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1562</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE32\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1646</x>"+
         "<y>112</y>"+
         "<height>62</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE33\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>112</y>"+
         "<height>62</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE35\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1799</x>"+
         "<y>112</y>"+
         "<height>62</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE36\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1875</x>"+
         "<y>112</y>"+
         "<height>62</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE37\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>1966</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE38\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2083</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE39\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2174</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE40\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2270</x>"+
         "<y>80</y>"+
         "<height>94</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE42\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2377</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE43\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2432</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE44\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2494</x>"+
         "<y>45</y>"+
         "<height>129</height>"+
      "</itemlocation>"+
   "</line>"+
   "<label sid=\"LABEL8\">"+
      "<itemlocation>"+
         "<x>468</x>"+
         "<y>45</y>"+
         "<width>125</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Прололжительность простоя у запрещающих сигналов в час. и мин., причина остановки</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL9\">"+
      "<itemlocation>"+
         "<x>592</x>"+
         "<y>45</y>"+
         "<width>78</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Отметка о нагоне или опоздании</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL10\">"+
      "<itemlocation>"+
         "<x>669</x>"+
         "<y>45</y>"+
         "<width>60</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Номер поезда</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL11\">"+
      "<itemlocation>"+
         "<x>728</x>"+
         "<y>45</y>"+
         "<width>60</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Код рода работы</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL12\">"+
      "<itemlocation>"+
         "<x>787</x>"+
         "<y>45</y>"+
         "<width>117</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>Масса поезда в тоннах</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL13\">"+
      "<itemlocation>"+
         "<x>787</x>"+
         "<y>87</y>"+
         "<width>60</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>нетто</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL15\">"+
      "<itemlocation>"+
         "<x>846</x>"+
         "<y>87</y>"+
         "<width>58</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>брутто</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL16\">"+
      "<itemlocation>"+
         "<x>903</x>"+
         "<y>45</y>"+
         "<width>251</width>"+
         "<height>43</height>"+
      "</itemlocation>"+
      "<value>Пассажирские вагоны рабочего парка в физ. ед.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL18\">"+
      "<itemlocation>"+
         "<x>903</x>"+
         "<y>87</y>"+
         "<width>69</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>пассажир-"+
"ские</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL19\">"+
      "<itemlocation>"+
         "<x>971</x>"+
         "<y>87</y>"+
         "<width>63</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>почтовые</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL20\">"+
      "<itemlocation>"+
         "<x>1033</x>"+
         "<y>87</y>"+
         "<width>63</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>багажные</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL21\">"+
      "<itemlocation>"+
         "<x>1095</x>"+
         "<y>87</y>"+
         "<width>59</width>"+
         "<height>87</height>"+
      "</itemlocation>"+
      "<value>прочие</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL22\">"+
      "<itemlocation>"+
         "<x>1153</x>"+
         "<y>45</y>"+
         "<width>931</width>"+
         "<height>36</height>"+
      "</itemlocation>"+
      "<value>Грузовые вагоны рабочего парка в физических единицах</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL24\">"+
      "<itemlocation>"+
         "<x>1153</x>"+
         "<y>80</y>"+
         "<width>54</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>крытые</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL25\">"+
      "<itemlocation>"+
         "<x>1206</x>"+
         "<y>80</y>"+
         "<width>76</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>платформы</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL26\">"+
      "<itemlocation>"+
         "<x>1281</x>"+
         "<y>80</y>"+
         "<width>76</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>полувагоны</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL27\">"+
      "<itemlocation>"+
         "<x>1356</x>"+
         "<y>80</y>"+
         "<width>76</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>цистерны</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL28\">"+
      "<itemlocation>"+
         "<x>1431</x>"+
         "<y>80</y>"+
         "<width>76</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>рефриже-"+
"раторные</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL30\">"+
      "<itemlocation>"+
         "<x>1506</x>"+
         "<y>80</y>"+
         "<width>57</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>прочие</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL37\">"+
      "<itemlocation>"+
         "<x>1966</x>"+
         "<y>80</y>"+
         "<width>118</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>итого груз. вагонов рабочего парка"+
"(груж. + порожн.)</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL32\">"+
      "<itemlocation>"+
         "<x>1562</x>"+
         "<y>112</y>"+
         "<width>85</width>"+
         "<height>62</height>"+
      "</itemlocation>"+
      "<value>цементовозы</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL33\">"+
      "<itemlocation>"+
         "<x>1646</x>"+
         "<y>112</y>"+
         "<width>86</width>"+
         "<height>62</height>"+
      "</itemlocation>"+
      "<value>окатышевозы</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL34\">"+
      "<itemlocation>"+
         "<x>1731</x>"+
         "<y>112</y>"+
         "<width>69</width>"+
         "<height>62</height>"+
      "</itemlocation>"+
      "<value>зерновозы</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL35\">"+
      "<itemlocation>"+
         "<x>1799</x>"+
         "<y>112</y>"+
         "<width>77</width>"+
         "<height>62</height>"+
      "</itemlocation>"+
      "<value>фитинговые</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL31\">"+
      "<itemlocation>"+
         "<x>1562</x>"+
         "<y>80</y>"+
         "<width>405</width>"+
         "<height>33</height>"+
      "</itemlocation>"+
      "<value>В том числе</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL36\">"+
      "<itemlocation>"+
         "<x>1875</x>"+
         "<y>112</y>"+
         "<width>92</width>"+
         "<height>62</height>"+
      "</itemlocation>"+
      "<value>минераловозы</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL39\">"+
      "<itemlocation>"+
         "<x>2083</x>"+
         "<y>80</y>"+
         "<width>92</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>Грузовые перевозки</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL40\">"+
      "<itemlocation>"+
         "<x>2174</x>"+
         "<y>80</y>"+
         "<width>97</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>Пассажирские перевозки</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL41\">"+
      "<itemlocation>"+
         "<x>2270</x>"+
         "<y>80</y>"+
         "<width>108</width>"+
         "<height>94</height>"+
      "</itemlocation>"+
      "<value>Недействующие локомотивые,механизмы и др.</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL42\">"+
      "<itemlocation>"+
         "<x>2377</x>"+
         "<y>45</y>"+
         "<width>56</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Состав поезда в осях</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL43\">"+
      "<itemlocation>"+
         "<x>2432</x>"+
         "<y>45</y>"+
         "<width>63</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Условная длина поезда</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL44\">"+
      "<itemlocation>"+
         "<x>2494</x>"+
         "<y>45</y>"+
         "<width>348</width>"+
         "<height>129</height>"+
      "</itemlocation>"+
      "<value>Подпись ДСП или машиниста</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<label sid=\"LABEL38\">"+
      "<itemlocation>"+
         "<x>2083</x>"+
         "<y>45</y>"+
         "<width>295</width>"+
         "<height>36</height>"+
      "</itemlocation>"+
      "<value>Нерабочий парк, физических единиц</value>"+
      "<justify>center</justify>"+
   "</label>"+
   "<line sid=\"LINE5\">"+
      "<size>"+
         "<height>1</height>"+
         "<width>0</width>"+
      "</size>"+
      "<itemlocation>"+
         "<x>2841</x>"+
         "<y>24</y>"+
         "<height>150</height>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE12\">"+
      "<itemlocation>"+
         "<x>312</x>"+
         "<y>74</y>"+
         "<width>110</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE19\">"+
      "<itemlocation>"+
         "<x>1155</x>"+
         "<y>80</y>"+
         "<width>1223</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE20\">"+
      "<itemlocation>"+
         "<x>787</x>"+
         "<y>87</y>"+
         "<width>367</width>"+
      "</itemlocation>"+
   "</line>"+
   "<line sid=\"LINE26\">"+
      "<itemlocation>"+
         "<x>1562</x>"+
         "<y>112</y>"+
         "<width>405</width>"+
      "</itemlocation>"+
   "</line>"+
   "<pane sid=\"TABLE1_PANE\">"+
      "<xforms:group ref=\"instance('INSTANCE')/table1/row1\">"+
         "<xforms:label></xforms:label>"+
         "<spacer sid=\"HEADER_SPACER1\">"+
            "<itemlocation>"+
               "<width>28</width>"+
               "<height></height>"+
               "<offsetx>6</offsetx>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER2\">"+
            "<itemlocation>"+
               "<width>253</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER1</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER3\">"+
            "<itemlocation>"+
               "<width>50</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER2</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER4\">"+
            "<itemlocation>"+
               "<width>98</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER3</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER6\">"+
            "<itemlocation>"+
               "<width>120</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER4</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER7\">"+
            "<itemlocation>"+
               "<width>73</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER6</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER8\">"+
            "<itemlocation>"+
               "<width>55</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER7</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER9\">"+
            "<itemlocation>"+
               "<width>55</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER8</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER10\">"+
            "<itemlocation>"+
               "<width>55</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER9</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER11\">"+
            "<itemlocation>"+
               "<width>53</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER10</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER12\">"+
            "<itemlocation>"+
               "<width>64</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER11</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER13\">"+
            "<itemlocation>"+
               "<width>58</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER12</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER14\">"+
            "<itemlocation>"+
               "<width>58</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER13</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER15\">"+
            "<itemlocation>"+
               "<width>54</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER14</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER16\">"+
            "<itemlocation>"+
               "<width>54</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER15</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER25\">"+
            "<itemlocation>"+
               "<width>70</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER16</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER26\">"+
            "<itemlocation>"+
               "<width>70</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER25</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER27\">"+
            "<itemlocation>"+
               "<width>70</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER26</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER28\">"+
            "<itemlocation>"+
               "<width>70</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER27</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER29\">"+
            "<itemlocation>"+
               "<width>51</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER28</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER30\">"+
            "<itemlocation>"+
               "<width>82</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER29</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER31\">"+
            "<itemlocation>"+
               "<width>82</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER30</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER32\">"+
            "<itemlocation>"+
               "<width>64</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER31</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER33\">"+
            "<itemlocation>"+
               "<width>72</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER32</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER34\">"+
            "<itemlocation>"+
               "<width>87</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER33</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER18\">"+
            "<itemlocation>"+
               "<width>113</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER34</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER19\">"+
            "<itemlocation>"+
               "<width>87</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER18</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER20\">"+
            "<itemlocation>"+
               "<width>92</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER19</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER21\">"+
            "<itemlocation>"+
               "<width>103</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER20</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER22\">"+
            "<itemlocation>"+
               "<width>51</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER21</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER23\">"+
            "<itemlocation>"+
               "<width>58</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER22</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<spacer sid=\"HEADER_SPACER24\">"+
            "<itemlocation>"+
               "<width>343</width>"+
               "<height></height>"+
               "<after>HEADER_SPACER23</after>"+
            "</itemlocation>"+
         "</spacer>"+
         "<table sid=\"TABLE1_TABLE\">"+
            "<xforms:repeat id=\"TABLE1\" nodeset=\"instance('INSTANCE')/table1/row1\">"+
               "<pane sid=\"ROW_GROUP\">"+
                  "<xforms:group ref=\".\">"+
                     "<xforms:label></xforms:label>"+
                     "<spacer sid=\"setHeight\">"+
                        "<itemlocation>"+
                           "<offsety>2</offsety>"+
                           "<expandheight>2</expandheight>"+
                        "</itemlocation>"+
                     "</spacer>"+
                     "<field sid=\"T1\">"+
                        "<xforms:input ref=\"T1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<x>0</x>"+
                           "<width>30</width>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<line sid=\"LINE46\">"+
                        "<itemlocation>"+
                           "<width>2816</width>"+
                           "<below>T1</below>"+
                           "<offsety>-5</offsety>"+
                           "<offsetx>-7</offsetx>"+
                        "</itemlocation>"+
                     "</line>"+
                     "<field sid=\"T2\">"+
                        "<xforms:input ref=\"T2\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>253</width>"+
                           "<after>T1</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T4\">"+
                        "<xforms:input ref=\"T4\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>50</width>"+
                           "<after>T2</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T5\">"+
                        "<xforms:input ref=\"T5\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>42</width>"+
                           "<after>T4</after>"+
                           "<height>80</height>"+
                           "<offsetx>-1</offsetx>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<label sid=\"L5\">"+
                        "<itemlocation>"+
                           "<after>T5</after>"+
                           "<width>16</width>"+
                           "<height>77</height>"+
                           "<offsetx>-3</offsetx>"+
                        "</itemlocation>"+
                        "<value>/</value>"+
                        "<justify>center</justify>"+
                        "<fontinfo>"+
                           "<fontname>Arial</fontname>"+
                           "<size>11</size>"+
                        "</fontinfo>"+
                     "</label>"+
                     "<field sid=\"T5a\">"+
                        "<xforms:input ref=\"T5_1\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>39</width>"+
                           "<after>L5</after>"+
                           "<height>80</height>"+
                           "<offsetx>-3</offsetx>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T7\">"+
                        "<xforms:input ref=\"T7\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>120</width>"+
                           "<after>T5a</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T8\">"+
                        "<xforms:input ref=\"T8\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>73</width>"+
                           "<after>T7</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T9\">"+
                        "<xforms:input ref=\"T9\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>55</width>"+
                           "<after>T8</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T10\">"+
                        "<xforms:input ref=\"T10\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>55</width>"+
                           "<after>T9</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T11\">"+
                        "<xforms:input ref=\"T11\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>55</width>"+
                           "<after>T10</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T12\">"+
                        "<xforms:input ref=\"T12\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>53</width>"+
                           "<after>T11</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T13\">"+
                        "<xforms:input ref=\"T1table_1_13\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>64</width>"+
                           "<after>T12</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T13_1\">"+
                        "<xforms:input ref=\"T1table_2_13\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>64</width>"+
                           "<after>T12</after>"+
                           "<height>35</height>"+
                           "<below>T13</below>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T14\">"+
                        "<xforms:input ref=\"T1table_1_14\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>58</width>"+
                           "<after>T13</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T14_1\">"+
                        "<xforms:input ref=\"T1table_2_14\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>58</width>"+
                           "<after>T13</after>"+
                           "<height>35</height>"+
                           "<below>T14</below>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T15\">"+
                        "<xforms:input ref=\"T1table_1_15\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>58</width>"+
                           "<after>T14</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T15_1\">"+
                        "<xforms:input ref=\"T1table_2_15\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>58</width>"+
                           "<after>T14</after>"+
                           "<height>35</height>"+
                           "<below>T15</below>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T16\">"+
                        "<xforms:input ref=\"T1table_1_16\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>54</width>"+
                           "<after>T15</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T16_1\">"+
                        "<xforms:input ref=\"T1table_2_16\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>54</width>"+
                           "<after>T15</after>"+
                           "<height>35</height>"+
                           "<below>T16</below>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<line sid=\"LINE34\">"+
                        "<itemlocation>"+
                           "<width>1122</width>"+
                           "<below>T13</below>"+
                           "<offsety>-3</offsety>"+
                           "<offsetx>-4</offsetx>"+
                        "</itemlocation>"+
                     "</line>"+
                     "<field sid=\"T1TABLE1\">"+
                        "<xforms:input ref=\"T1table_1_19\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>54</width>"+
                           "<after>T16</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE2\">"+
                        "<xforms:input ref=\"T1table_1_20\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE1</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE3\">"+
                        "<xforms:input ref=\"T1table_1_21\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE2</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE4\">"+
                        "<xforms:input ref=\"T1table_1_22\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE3</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE5\">"+
                        "<xforms:input ref=\"T1table_1_24\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE4</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE6\">"+
                        "<xforms:input ref=\"T1table_1_26\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>51</width>"+
                           "<after>T1TABLE5</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE7\">"+
                        "<xforms:input ref=\"T1table_1_27\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>82</width>"+
                           "<after>T1TABLE6</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE8\">"+
                        "<xforms:input ref=\"T1table_1_28\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>82</width>"+
                           "<after>T1TABLE7</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE9\">"+
                        "<xforms:input ref=\"T1table_1_29\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>64</width>"+
                           "<after>T1TABLE8</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE10\">"+
                        "<xforms:input ref=\"T1table_1_30\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>72</width>"+
                           "<after>T1TABLE9</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE11\">"+
                        "<xforms:input ref=\"T1table_1_31\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>87</width>"+
                           "<after>T1TABLE10</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE13\">"+
                        "<xforms:input ref=\"T1table_2_19\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>54</width>"+
                           "<height>35</height>"+
                           "<below>T1TABLE1</below>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE14\">"+
                        "<xforms:input ref=\"T1table_2_20\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE13</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE15\">"+
                        "<xforms:input ref=\"T1table_2_21\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE14</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE16\">"+
                        "<xforms:input ref=\"T1table_2_22\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE15</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE17\">"+
                        "<xforms:input ref=\"T1table_2_24\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>70</width>"+
                           "<after>T1TABLE16</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE18\">"+
                        "<xforms:input ref=\"T1table_2_26\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>51</width>"+
                           "<after>T1TABLE17</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE19\">"+
                        "<xforms:input ref=\"T1table_2_27\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>82</width>"+
                           "<after>T1TABLE18</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE20\">"+
                        "<xforms:input ref=\"T1table_2_28\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>82</width>"+
                           "<after>T1TABLE19</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE21\">"+
                        "<xforms:input ref=\"T1table_2_29\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>64</width>"+
                           "<after>T1TABLE20</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE22\">"+
                        "<xforms:input ref=\"T1table_2_30\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>72</width>"+
                           "<after>T1TABLE21</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T1TABLE23\">"+
                        "<xforms:input ref=\"T1table_2_31\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>87</width>"+
                           "<after>T1TABLE22</after>"+
                           "<height>35</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T32\">"+
                        "<xforms:input ref=\"T32\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>113</width>"+
                           "<after>T1TABLE11</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T33A\">"+
                        "<xforms:input ref=\"T33a\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>87</width>"+
                           "<after>T32</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T33B\">"+
                        "<xforms:input ref=\"T33b\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>92</width>"+
                           "<after>T33A</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T33V\">"+
                        "<xforms:input ref=\"T33v\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>103</width>"+
                           "<after>T33B</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"TSH3\">"+
                        "<xforms:input ref=\"Tsh3\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>51</width>"+
                           "<after>T33V</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T34\">"+
                        "<xforms:input ref=\"T34\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>58</width>"+
                           "<after>TSH3</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                     "<field sid=\"T35\">"+
                        "<xforms:input ref=\"T35\">"+
                           "<xforms:label></xforms:label>"+
                        "</xforms:input>"+
                        "<itemlocation>"+
                           "<width>343</width>"+
                           "<after>T34</after>"+
                           "<height>80</height>"+
                        "</itemlocation>"+
                     "</field>"+
                  "</xforms:group>"+
                  "<padding>1</padding>"+
               "</pane>"+
            "</xforms:repeat>"+
            "<itemlocation>"+
               "<x>1</x>"+
               "<y>1</y>"+
            "</itemlocation>"+
            "<rowpadding>-2</rowpadding>"+
         "</table>"+
         "<box sid=\"BORDER\">"+
            "<itemlocation>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<alignl2l>TABLE1_TABLE</alignl2l>"+
               "<expandr2r>TABLE1_TABLE</expandr2r>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
            "</itemlocation>"+
         "</box>"+
         "<line sid=\"COLUMN_DIVIDER1\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER1</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER2\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER2</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER3\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER3</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER5\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER4</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER6\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER6</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER7\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER7</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER8\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER8</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER9\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER9</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER10\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER10</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER11\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER11</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER12\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER12</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER13\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER13</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER14\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER14</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER16\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER15</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER15\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER16</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER24\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER25</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER25\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER26</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER26\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER27</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER27\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER28</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER28\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER29</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER29\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER30</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER30\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER31</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER31\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER32</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER32\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER33</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER17\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER34</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER18\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER18</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER19\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER19</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER20\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER20</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER21\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER21</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER22\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER22</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<line sid=\"COLUMN_DIVIDER23\">"+
            "<itemlocation>"+
               "<after>HEADER_SPACER23</after>"+
               "<alignt2t>HEADER_SPACER1</alignt2t>"+
               "<expandb2b>TABLE1_TABLE</expandb2b>"+
               "<offsetx>-2</offsetx>"+
            "</itemlocation>"+
            "<size>"+
               "<width>0</width>"+
               "<height>1</height>"+
            "</size>"+
         "</line>"+
         "<button sid=\"ADD\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table1\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:insert at=\"last()\" nodeset=\"row1\" position=\"after\"></xforms:insert>"+
                  "<xforms:setvalue ref=\"row1[last()]/T1\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T2\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T4\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T5\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T6\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T7\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T8\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T9\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T10\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T11\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T12\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T13\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T14\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T15\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T16\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T17\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T1table\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T32\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T33a\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T33b\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T33v\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/Tsh3\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T34\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last()]/T35\"></xforms:setvalue>"+
                  "<xforms:setfocus control=\"TABLE1\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>TABLE1_TABLE</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>+</value>"+
         "</button>"+
         "<button sid=\"REMOVE\">"+
            "<xforms:trigger ref=\"instance('INSTANCE')/table1\">"+
               "<xforms:label></xforms:label>"+
               "<xforms:action ev:event=\"DOMActivate\">"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T1\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T2\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T4\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T5\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T6\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T7\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T8\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T9\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T10\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T11\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T12\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T13\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T14\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T15\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T16\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T17\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T1table\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T32\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T33a\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T33b\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T33v\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/Tsh3\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T34\"></xforms:setvalue>"+
                  "<xforms:setvalue ref=\"row1[last() = 1]/T35\"></xforms:setvalue>"+
                  "<xforms:delete at=\"index('TABLE1')\" nodeset=\"row1[last() > 1]\"></xforms:delete>"+
                  "<xforms:setfocus control=\"TABLE1\"></xforms:setfocus>"+
               "</xforms:action>"+
            "</xforms:trigger>"+
            "<itemlocation>"+
               "<after>ADD</after>"+
               "<width>22</width>"+
            "</itemlocation>"+
            "<value>-</value>"+
         "</button>"+
      "</xforms:group>"+
      "<itemlocation>"+
         "<x>16</x>"+
         "<y>176</y>"+
         "<width>2986</width>"+
      "</itemlocation>"+
   "</pane>"+
"</page>"+
"</XFDL>"
;
	
