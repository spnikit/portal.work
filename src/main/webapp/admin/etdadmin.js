ru = {};
ru.aisa = {};
ru.aisa.width = 200;
ru.aisa.width1 = 400;
var comborows = '20';
var assertMenuHeight = function (m) {

    var maxHeight = Ext.getBody().getHeight() - 480;

    if (m.el.getHeight() > maxHeight) {

        m.el.setHeight(maxHeight);

        m.el.applyStyles('overflow:auto;');

    }

};

function openJoinAddrWnd() {
    var store = new Ext.data.SimpleStore({
        url: 'controllers/adresses',
        autoLoad: false,
        fields: ['id', 'text']

    });

    store.load({
        callback: function () {

            var someFn = function (resp) {
                var obj = eval('(' + resp.responseText + ')');
                var addr_arr = [];

                for (var i = 0; i < obj.length; i++) {
                    var data = {};
                    data.id = obj[i][0];
                    data.active = obj[i][1];
                    addr_arr.push(data);
                }

                var join_addr_panel = new Ext.form.FormPanel({
                    width: 'auto', border: false, id: 'join_addr_panel',
                    items: [
                        {xtype: 'textfield', name: 'adress', id: 'adress', width: 180, fieldLabel: 'Почтовый адрес'},
                        {
                            xtype: 'multiselect', name: 'addrSel', hiddenName: 'addr', id: 'addrSel',
                            width: 180, triggerAction: 'all', mode: 'local', displayField: 'text',
                            valueField: 'id', fieldLabel: 'Адреса рассылки', store: store, listeners: {
                            render: function () {

                                (function () {
                                    var temp = [];

                                    for (var i = 0; i < addr_arr.length; i++) {
                                        if (addr_arr[i].active == 1) {

                                            temp.push(addr_arr[i].id.toString());
                                        }
                                    }


                                    Ext.getCmp('addrSel').setValues(temp);
                                }).defer(1);

                            }
                        }
                        },
                        {
                            xtype: 'combo',
                            name: 'addrCmb',
                            hiddenName: 'addrr',
                            id: 'addrCmb',
                            fieldLabel: 'Выбранный',
                            width: 180,
                            triggerAction: 'all',
                            mode: 'local',
                            displayField: 'text',
                            valueField: 'id',
                            store: store
                        }

                    ],
                    buttons: [

                        {
                            xtype: 'button', text: 'Добавить', handler: function () {
                            var val = Ext.getCmp('adress').getValue();
                            if (val == '' || !validateEmail(val)) {
                                Ext.getCmp('adress').markInvalid();
                                return;
                            }

                            Ext.getCmp('join_addr_panel').getForm().submit({
                                url: 'controllers/addr',
                                params: {act: 'new'},
                                success: function (from, action) {
                                    joinWnd.destroy();
                                    Ext.Msg.show({title: 'Сообщение', msg: action.result.msg, buttons: Ext.Msg.OK});
                                },
                                failure: function (form, action) {
                                    Ext.Msg.show({title: 'Ошибка', msg: action.result.msg, buttons: Ext.Msg.OK});
                                }
                            });

                            Ext.Msg.wait('Подождите');

                        }
                        }, {
                            xtype: 'button', text: 'Удалить выбранный', handler: function () {

                                var cmb = Ext.getCmp('addrCmb');

                                if (typeof(cmb.getValue()) != 'number') {

                                    //alert(cmb.getValue().length)
                                    cmb.markInvalid();
                                    return;
                                }

                                Ext.getCmp('join_addr_panel').getForm().submit({
                                    url: 'controllers/addr',
                                    params: {act: 'delete'},
                                    success: function (form, action) {
                                        joinWnd.destroy();
                                        Ext.Msg.show({title: 'Сообщение', msg: action.result.msg, buttons: Ext.Msg.OK});
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.show({title: 'Ошибка', msg: action.result.msg, buttons: Ext.Msg.OK});
                                    }
                                });

                                Ext.Msg.wait('Подождите');

                            }
                        }],
                    buttonAlign: 'left'
                });

                var joinWnd = new Ext.Window({
                    closable: false,
                    autoScroll: true,
                    resizable: false,

                    title: 'Почтовые адреса',
                    modal: true,
                    width: 400,
                    id: 'joinWnd',
                    items: [join_addr_panel],
                    border: false,
                    buttonAlign: 'right',
                    buttons: [{
                        xtype: 'button', text: 'Сохранить', handler: function () {
                            join_addr_panel.getForm().submit({
                                url: 'controllers/addr',
                                params: {act: 'edit'},
                                success: function (form, action) {
                                    joinWnd.destroy();
                                    Ext.Msg.show({title: 'Сообщение', msg: action.result.msg, buttons: Ext.Msg.OK});
                                },
                                failure: function (form, action) {
                                    Ext.Msg.show({title: 'Ошибка', msg: action.result.msg, buttons: Ext.Msg.OK});
                                }

                            });
                        }
                    }, {
                        xtype: 'button', text: 'Закрыть', handler: function () {
                            joinWnd.destroy();
                        }
                    }]

                });

                joinWnd.show();

            };

            Ext.Ajax.request({
                url: 'controllers/active_addr',
                success: someFn

            });


        }
    });


}

var join_msg_panel = new Ext.form.FormPanel({
    width: 'auto', border: false, id: 'join_msg_panel',
    items: [
        {
            xtype: 'textarea',
            name: 'message',
            id: 'text',
            fieldLabel: 'Информационное сообщение',
            height: 200,
            width: 600,
            allowBlank: false
        }

    ],
    buttons: [
        {
            xtype: 'button', text: 'Сохранить', handler: function () {

            if (!Ext.getCmp('join_msg_panel').getForm().isValid())
                return;
            Ext.getCmp('join_msg_panel').getForm().submit({
                url: 'controllers/join_msg',
                params: {act: 'new'},
                success: function (from, action) {
                    Ext.Msg.show({title: 'Сообщение', msg: action.result.msg, buttons: Ext.Msg.OK});
                },
                failure: function (form, action) {
                    Ext.Msg.show({title: 'Ошибка', msg: action.result.msg, buttons: Ext.Msg.OK});
                }
            });

            Ext.Msg.wait('Подождите');

        }
        },
        {
            xtype: 'button', text: 'Удалить', handler: function () {

            Ext.getCmp('join_msg_panel').getForm().submit({
                url: 'controllers/join_msg',
                params: {act: 'delete'},
                success: function (form, action) {
                    Ext.Msg.show({title: 'Сообщение', msg: action.result.msg, buttons: Ext.Msg.OK});
                },
                failure: function (form, action) {
                    Ext.Msg.show({title: 'Ошибка', msg: action.result.msg, buttons: Ext.Msg.OK});
                }
            });

            Ext.Msg.wait('Подождите');

        }

        },
        {xtype: 'button', text: 'Почтовые адреса', handler: openJoinAddrWnd}

    ],
    buttonAlign: 'left'
});

//function validateEmail(email)  {   
//	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\ ".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA -Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
//		
//		return email.match(re)  
//}


ru.aisa.panel = Ext.extend(Ext.grid.GridPanel, {
    constructor: function (config)// {return new Ext.Panel({title:config.title,items:[{xtype:'button',text:'123'}]})}
    {
        var width = ru.aisa.width;
        if (config.fullwidth == 'true') width = ru.aisa.width1;
        var label = function (column) {
            if (column.description) return column.description
            else return column.header;
        }
        var grid = this;
        //var adduser = new Ext.Button({xtype:'button',text:'Создать'});
        var createbutton = new Ext.Button({xtype: 'button', text: 'Создать'});
        var editbutton = new Ext.Button({xtype: 'button', text: 'Редактировать'});
        //handler writes data from grid to excel file

        //my code
        var savebutton = new Ext.Button({
            xtype: 'button', text: 'Выгрузка', handler: function () {


            }
        });

        //end

        var deletebutton = new Ext.Button({
            xtype: 'button', text: 'Удалить', handler: function () {
                var items = [];
                items.push(new Ext.form.Hidden({value: 'delete', name: 'act'}));
                var selected = grid.getSelectionModel().getSelected();
                for (i = 0; config.columns[i]; i++) {
                    if (!config.columns[i].im) {
                        //items.push( new Ext.form.Hidden({value:selected.get(config.columns[i].name),name:config.columns[i].name}));
                        items.push(new Ext.form.TextField({
                            name: config.columns[i].name,
                            value: selected.get(config.columns[i].name)
                        }));
                    }
                    /*
                     if( config.columns[i].type == 'text' )
                     items.push(new Ext.form.TextField({fieldLabel:label(config.columns[i]),width:200,name:config.columns[i].name,labelStyle:'width:120px;'}));
                     if( config.columns[i].type == 'combo' )
                     items.push(new Ext.form.ComboBox({fieldLabel:label(config.columns[i]),editable:false,triggerAction:'all',mode:'local',store:config.columns[i].store(),displayField:'text',width:200,name:config.columns[i].name,labelStyle:'width:120px;'}));
                     if( config.columns[i].type == 'file' ){
                     items.push(new Ext.form.FileUploadField({fieldLabel:label(config.columns[i]),width:200,buttonCfg: {text: 'Выбрать'},name:config.columns[i].name,labelStyle:'width:120px;'}));file=true}
                     */
                }
                var el = document.createElement('div');
                document.getElementsByTagName('body')[0].appendChild(el);
                var form = new Ext.form.FormPanel({
//				id:'form',
//				hidden:'true',
                    applyTo: el,
                    items: items
                });
                //form.render();
                form.getForm().submit({
                    url: config.url,
                    waitMsg: 'Подождите',
                    success: function () {
                        grid.getStore().reload();
                        form.destroy();
                    },
                    failure: function (form, action) {
                        Ext.Msg.alert('Ошибка', action.result.description);
                        form.destroy();
                    }
                });
            }
        })

        createbutton.on('click', function () {
            var items = [];
            var file = false;
            items.push(new Ext.form.Hidden({value: 'new', name: 'act'}));
            var form;
            var win = Ext.id('win');

            var file_selected_handler = function (field, v) {

                if (Ext.getCmp('treepanel').getSelectionModel().getSelectedNode().text == 'Пользователи') {
                    var ffield = form.findByType('textfield')[0];
                    var mfield = form.findByType('textfield')[1];
                    var lfield = form.findByType('textfield')[2];
                    var title = form.findByType('textfield')[3];
                    ffield.setValue('Заполнить');
                    mfield.setValue('Заполнить');
                    lfield.setValue('Заполнить');
                    title.setValue('Заполнить');
                    if (form.getForm().isValid()) {
                        form.getForm().submit({
                            url: config.url,
                            waitMsg: 'Подождите',
                            success: function (form, action) {
                                ffield.setValue(action.result.name0);
                                mfield.setValue(action.result.name1);
                                lfield.setValue(action.result.name2);
                                title.setValue(action.result.title);
                            },
                            failure: function (form, action) {
                                Ext.Msg.alert('Ошибка', action.result.description);
                                createwin.destroy();
                            }
                        })
                    }
                }
            }

            for (i = 0; config.columns[i]; i++) {
                if (config.columns[i].type == 'custom')
                    items.push(config.columns[i].func('new', win));
                if (config.columns[i].type == 'password')
                    items.push(new Ext.form.TextField({
                        fieldLabel: label(config.columns[i]),
                        width: width,
                        name: config.columns[i].name,
                        inputType: 'password',
                        regex: config.columns[i].reg,
                        allowBlank: false
                    }));
                if (config.columns[i].type == 'text')
                    items.push(new Ext.form.TextField({
                        fieldLabel: label(config.columns[i]),
                        width: width,
                        name: config.columns[i].name,
                        regex: config.columns[i].reg,
                        allowBlank: config.columns[i].allowBlank
                    }));
                if (config.columns[i].type == 'combo')
                    items.push(new Ext.form.ComboBox({
                        fieldLabel: label(config.columns[i]),
                        editable: true,
                        forceSelection: false,
                        triggerAction: 'all',
                        mode: 'local',
                        store: config.columns[i].store(),
                        displayField: 'text',
                        valueField: 'id',
                        width: width,
                        hiddenName: config.columns[i].name,
                        allowBlank: true
                    }));
                if (config.columns[i].type == 'file') {
                    items.push(new Ext.form.FileUploadField({
                        fieldLabel: label(config.columns[i]),
                        width: width,
                        buttonCfg: {text: 'Выбрать'},
                        name: config.columns[i].name,
                        allowBlank: false,
                        listeners: {
                            fileselected: file_selected_handler
                        }
                    }));
                    file = true
                }
                if (config.columns[i].type == 'radio')
                    items.push(new Ext.form.FieldSet({
                        border: false,
                        allowBlank: false,
                        autoHeight: true,
                        items: config.columns[i].items,
                        defaultType: 'radio'
                    }));
                if (config.columns[i].type == 'checkbox')
                    items.push(new Ext.form.FieldSet({
                        style: 'margin-bottom:0px;padding:5px;',
                        border: false,
                        autoHeight: true,
                        items: config.columns[i].items,
                        defaultType: 'checkbox'
                    }));
            }
            file = (config.url == 'controllers/roles') ? true : file;
            var form = new Ext.form.FormPanel({
                id: 'form',
                border: false,
                fileUpload: file,
                autoScroll: false,
                autoHeight: true,
                style: 'margin-left: 5px;',
                //labelStyle:'margin-left: 5px;',
                //bodyStyle:'text-align:center;',
                //floating:true,

//				width:220,
                labelWidth: 120,
                //monitorResize:true,
                buttons: [{
                    xtype: 'button', text: 'Применить', handler: function () {
                        if (form.getForm().isValid()) {
                            form.getForm().submit({
                                url: config.url,
                                waitMsg: 'Подождите', success: function () {
                                    createwin.destroy();
                                    grid.getStore().reload()
                                }, failure: function (form, action) {
                                    Ext.Msg.alert('Ошибка', action.result.description);
                                    createwin.destroy();
                                }
                            })
                        }
                    }
                },
                    {
                        xtype: 'button', text: 'Отмена', handler: function () {
                        createwin.destroy()
                    }
                    }],
                items: items
            });
            ;

            var createwin = new Ext.Window({
                closable: false,
                width: width + 165,
                id: win,
                autoScroll: true,
//				autoHeight:true,
                resizable: false,
                title: 'Новое значение',
                items: [form],
                modal: true
            });
            createwin.show();
            items[1].focus(false, 300);
            //form.on('move',function(a,x,y){alert(x);});
            //document.getElementById('window').onmousemove = function(){Ext.getCmp('addbutt').setText('Добавить роль');};
        });
        var gridconfig = {
            loadMask: true,
            autoExpandColumn: config.expand,
            title: config.title,
            region: 'center',
            columns: [],
            enableHdMenu: false,
            enableColumnHide: false,
            sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
            margins: '0 0 0 0 '
        };
        var columns = [];
        for (i = 0; config.columns[i]; i++) {
            if (!config.columns[i].im) {
                columns.push({name: config.columns[i].name});
                gridconfig.columns.push({
                    id: config.columns[i].name,
                    header: config.columns[i].header || config.columns[i].name,
                    sortable: true,
                    dataIndex: config.columns[i].name,
                    hidden: config.columns[i].hidden,
                    width: config.columns[i].width || 200
                })
            }
        }
        gridconfig.store = new Ext.data.JsonStore({
//			autoLoad:true,
            totalProperty: 'count',
            root: 'data',
            fields: columns,
            remoteSort: true,
            proxy: new Ext.data.HttpProxy({url: config.url})
        });

        var buttons = [];

        if (config.exbuttrights) {
            var tmp = eval(config.exbuttrights);
            buttons.push(new tmp(config, this));
        }
        if (config.app != 'false') buttons.push(createbutton);

        if (config.extbutt_create) {
            var tmp = eval(config.extbutt_create);
            buttons.push(new tmp(config, this));
        }

        if (config.del != 'false') buttons.push(deletebutton);
        //buttons.push(new Ext.ux.Exporter.Button({xtype:'exportbutton', text:'Экспорт'}));
        //linkButton.getEl().child('a', true).href = 'data:application/vnd.ms-excel;base64,' +  Base64.encode(grid.getExcelXml());

        if (config.searchOCO) {
            var tmp = eval(config.searchOCO);
            buttons.push(new tmp(config, this));
        }
        if (config.searchSNT) {
            var tmp = eval(config.searchSNT);
            buttons.push(new tmp(config, this));
        }

        if (config.exbutt) {
            var tmp = eval(config.exbutt);
            buttons.push(new tmp(config, this));
        }
        //task
        if (config.search_sample) {
            var tmp = eval(config.search_sample);
            buttons.push(new tmp(config, this));
        }
        //+++++++++

        if (config.unloading_data) {
            var tmp = eval(config.unloading_data);
            buttons.push(new tmp(config, this));
        }

        if (config.addArray) {
            var tmp = eval(config.addArray);
            buttons.push(new tmp(config, this));
        }

        if (config.deleteAllServices) {
            var tmp = eval(config.deleteAllServices);
            buttons.push(new tmp(config, this));
        }

        //mysearch
        if (config.mysearch) {
            var tmp = eval(config.mysearch);
            buttons.push(new tmp(config, this));
        }
        if (config.clearbutton) {
            var tmp = eval(config.clearbutton);
            buttons.push(new tmp(config, this));
        }
        //+++++++++++


        if (config.search instanceof Array) {
            for (i = 0; config.search[i]; i++) {

                var searchbutton = new Ext.Button({xtype: 'button', text: config.search[i].text});
                searchbutton.field = config.search[i].field;
                searchbutton.reg = config.search[i].reg;
                searchbutton.on('click', function (button) {
                    var value;
                    if (grid.getStore().baseParams.searchField == button.field)
                        value = grid.getStore().baseParams.search;
                    else value = '';
                    var search = new Ext.form.TextField({
                        fieldLabel: 'Строка для поиска по первому полю',
                        name: 'pattern',
                        width: ru.aisa.width,
                        value: value,
                        regex: button.reg
                    });
                    Ext.QuickTips.getQuickTip().register({
                        target: search, text: 'Введите критерии поиска, допустимые символы подстановки: _,%'//,title:'zxc'//,width: 100

                        //		dismissDelay: 200
                    });


                    var form = new Ext.form.FormPanel({
                        //				id:'form',
                        labelWidth: 120,
                        border: false,
                        style: 'margin-left: 5px;margin-top:5px;margin-right: 5px',

                        //reader:new Ext.data.JsonReader(),
                        buttons: [{
                            xtype: 'button', text: 'Применить', handler: function () {
                                createwin.destroy();
                                if (search.getValue().length < 1)
                                    grid.getStore().baseParams.search = null;
                                else {
                                    grid.getStore().baseParams.search = search.getValue().toUpperCase();
                                    grid.getStore().baseParams.searchField = button.field;
                                }
                                grid.getStore().load();
                            }
                        },
                            {
                                xtype: 'button', text: 'Отмена', handler: function () {
                                createwin.destroy();
                            }
                            },
                            {
                                xtype: 'button', text: 'Очистить', handler: function () {
                                createwin.destroy();
                                grid.getStore().baseParams.search = null;

                                grid.getStore().baseParams.searchField = null;
                                grid.getStore().load();
                            }
                            }],
                        items: [search]
                    });


                    var createwin = new Ext.Window({
                        closable: false,
                        width: ru.aisa.width + 155,
                        title: config.search.header,
                        items: [form],
                        modal: true
                    });
                    createwin.show();
                    search.focus(false, 300);

                });
                buttons.push(searchbutton);
            }
        }


        else {
            var searchbutton = new Ext.Button({xtype: 'button', text: 'Поиск'});
            searchbutton.on('click', function () {
                var search = new Ext.form.TextField({
                    fieldLabel: 'Строка для поиска',
                    name: 'pattern',
                    width: ru.aisa.width,
                    value: grid.getStore().baseParams.search
                });
                Ext.QuickTips.getQuickTip().register({
                    target: search, text: 'Введите критерии поиска, допустимые символы подстановки: _,%'

                });
                var form = new Ext.form.FormPanel({
                    labelWidth: 120,
                    border: false,
                    style: 'margin-left: 5px;margin-top:5px;margin-right: 5px',
                    buttons: [{
                        xtype: 'button', text: 'Применить', handler: function () {
                            createwin.destroy();
                            if (search.getValue().length < 1) grid.getStore().baseParams.search = null;
                            else grid.getStore().baseParams.search = search.getValue().toUpperCase();
                            grid.getStore().load();
                        }
                    },
                        {
                            xtype: 'button', text: 'Отмена', handler: function () {
                            createwin.destroy();
                        }
                        },
                        {
                            xtype: 'button', text: 'Очистить', handler: function () {
                            createwin.destroy();
                            grid.getStore().baseParams.search = null;
                            grid.getStore().load();
                        }
                        }],
                    items: [search]
                });
                var createwin = new Ext.Window({
                    closable: false,
                    width: ru.aisa.width + 155,
                    title: config.search.header,
                    items: [form],
                    modal: true
                });
                createwin.show();
                search.focus(false, 300);
            });
            if (config.search) buttons.push(searchbutton);

        }


        gridconfig.tbar = new Ext.PagingToolbar({
            pageSize: 20,
            buttons: buttons,
            store: gridconfig.store,
            displayInfo: true
        });


        buttons.push('->');

        buttons.push(
            new Ext.form.ComboBox({
                store: new Ext.data.SimpleStore({
                    fields: ['rows'],
                    data: [['20'], ['50'], ['100']]
                }),
                width: 60,
                maxHeight: 100,
                value: comborows,
                displayField: 'rows',
                hidden: false,
                selectOnFocus: true,
                editable: false,
                resizable: false,
                mode: 'local',
                triggerAction: 'all',
                listeners: {
                    select: function (como, record, numberIndex) {
                        grid.getStore().baseParams.limit = record.data.rows - 0;
                        gridconfig.tbar.pageSize = record.data.rows - 0;
                        gridconfig.tbar.changePage(1);
                    }
                }
            })
        );


        ru.aisa.panel.superclass.constructor.apply(this, [gridconfig]);
        var edit = function () {
            var items = [];
            var selected = this.getSelectionModel().getSelected();
            var file = false;
            items.push(new Ext.form.Hidden({value: 'edit', name: 'act'}));
            var field;

            var file_selected_handler_edit = function (field, v) {

                if (Ext.getCmp('treepanel').getSelectionModel().getSelectedNode().text == 'Пользователи') {

                    var ffield = form.findByType('textfield')[0];
                    var mfield = form.findByType('textfield')[1];
                    var lfield = form.findByType('textfield')[2];
                    var title = form.findByType('textfield')[3];
                    ffield.setValue('Заполнить');
                    mfield.setValue('Заполнить');
                    lfield.setValue('Заполнить');
                    title.setValue('Заполнить');
                    if (form.getForm().isValid()) {
                        form.getForm().submit({
                            url: config.url,
                            waitMsg: 'Подождите',
                            success: function (form, action) {
                                ffield.setValue(action.result.name0);
                                mfield.setValue(action.result.name1);
                                lfield.setValue(action.result.name2);
                                title.setValue(action.result.title);
                            },
                            failure: function (form, action) {
                                Ext.Msg.alert('Ошибка', action.result.description);
                                createwin.destroy();
                            }
                        })
                    }
                }
            }


            for (i = 0; config.columns[i]; i++) {
                if (!config.columns[i].nc) {
                    if (config.columns[i].type == 'custom')
                        items.push(config.columns[i].func('edit', selected));
                    if (config.columns[i].type == 'password') {
                        items.push(new Ext.form.TextField({
                            fieldLabel: label(config.columns[i]),
                            width: width,
                            name: config.columns[i].name,
                            inputType: 'password',
                            regex: config.columns[i].reg
                        }));
                        if (field == null) field = items[items.length - 1];
                    }
                    if (config.columns[i].type == 'text') {
                        items.push(new Ext.form.TextField({
                            fieldLabel: label(config.columns[i]),
                            width: width,
                            name: config.columns[i].name,
                            value: selected.get(config.columns[i].name),
                            regex: config.columns[i].reg,
                            allowBlank: config.columns[i].allowBlank
                        }));
                        if (field == null) field = items[items.length - 1];
                    }
                    if (config.columns[i].type == 'combo') {
                        items.push(new Ext.form.ComboBox({
                            fieldLabel: label(config.columns[i]),
                            editable: true,
                            forceSelection: true,
                            triggerAction: 'all',
                            mode: 'local',
                            store: config.columns[i].store(),
                            displayField: 'text',
                            valueField: 'id',
                            width: width,
                            hiddenName: config.columns[i].name,
                            value: selected.get(config.columns[i].name),
                            regex: config.columns[i].reg
                        }));
                        if (field == null) field = items[items.length - 1];
                    }


                    if (config.columns[i].type == 'file') {
                        items.push(new Ext.form.FileUploadField({
                            fieldLabel: label(config.columns[i]),
                            width: width,
                            buttonCfg: {text: 'Выбрать'},
                            name: config.columns[i].name,
                            regex: config.columns[i].reg,
                            listeners: {
                                fileselected: file_selected_handler_edit
                            }
                        }));
                        file = true
                    }


                    if (config.columns[i].type == 'hidden')
                        items.push(new Ext.form.Hidden({
                            value: selected.get(config.columns[i].name),
                            name: config.columns[i].name
                        }));
                    if (config.columns[i].type == 'readonly')
                        items.push(new Ext.form.TextField({
                            fieldLabel: label(config.columns[i]),
                            width: width,
                            name: config.columns[i].name,
                            readOnly: true,
                            value: selected.get(config.columns[i].name)
                        }));
                    if (config.columns[i].type == 'radio') {
                        for (j = 0; config.columns[i].items[j]; j++)
                            config.columns[i].items[j].checked = (config.columns[i].items[j].inputValue == selected.get(config.columns[i].name))
                        items.push(new Ext.form.FieldSet({
                            border: false,
                            allowBlank: false,
                            autoHeight: true,
                            items: citems,
                            defaultType: 'radio'
                        }));
                        for (j = 0; config.columns[i].items[j]; j++)
                            config.columns[i].items[j].checked = false;
                        if (field == null) field = items[items.length - 1];
                    }
                    if (config.columns[i].type == 'checkbox') {
                        config.columns[i].items[0].checked = (selected.get(config.columns[i].name) == '1')
                        items.push(new Ext.form.FieldSet({
                            style: 'margin-bottom:0px;padding:5px;',
                            border: false,
                            autoHeight: true,
                            items: config.columns[i].items,
                            defaultType: 'checkbox'
                        }));
                        config.columns[i].items[0].checked = false;
                        if (field == null) field = items[items.length - 1];
                    }
                }
            }
            file = (config.url == 'controllers/roles') ? true : file;
            var form = new Ext.form.FormPanel({
                id: 'form',
                fileUpload: file,
                labelWidth: 120,
                border: false,
                style: 'margin-left: 5px;',
                reader: new Ext.data.JsonReader(),
                buttons: [{
                    xtype: 'button', text: 'Применить', handler: function () {
                        if (form.getForm().isValid()) {
                            form.getForm().submit({
                                url: config.url,
                                waitMsg: 'Подождите', success: function () {
                                    createwin.destroy();
                                    grid.getStore().reload()
                                }, failure: function (form, action) {
                                    Ext.Msg.alert('Ошибка', action.result.description);
                                    createwin.destroy();
                                }
                            })
                        }
                    }
                },
                    {
                        xtype: 'button', text: 'Отмена', handler: function () {
                        createwin.destroy()
                    }
                    }],
                items: items
            });

            var createwin = new Ext.Window({
                closable: false,
                width: width + 160,
                autoScroll: true,
                autoHeight: true,
                resizable: false,
                title: 'Изменить значение',
                items: [form],
                modal: true
            });
            createwin.show();
            field.focus(false, 300);
        };
        this.getStore().on('loadexception', function () {
            window.location.href = 'index.html?t=' + (new Date()).getTime();
        });
        if (!config.readonly) {
            this.on('celldblclick', edit);
            editbutton.on('click', edit);
        }
        ;

    }
});
Ext.onReady(function () {
    Ext.QuickTips.init();

    /*	Ext.Msg.prompt('Number', 'Please enter your number:', function(btn, text){
     if (btn == 'ok'){
     main(text);
     }

     });
     */
    main();
});
function main() {
    var store = new Ext.data.JsonStore({
        url: 'Roles',
        root: 'roles',
        autoLoad: false,
        fields: ['columns', 'title', 'expand', 'url', 'app', 'del', 'edit', 'search', 'notgrid', 'config', 'readonly', 'fullwidth', 'searchOCO', 'searchSNT', 'exbutt', 'exbuttrights', 'extbutt_create', 'search_sample', 'mysearch', 'unloading_data', 'clearbutton', 'addArray', 'deleteAllServices']
    })
    /*	function hide_all(){
     var arr = Ext.getCmp('center').findByType(ru.aisa.panel);
     for(i=0;arr[i];i++){
     arr[i].setVisible(false);
     }
     };
     */
    store.on('loadexception', function () {
        window.location.href = 'index.html?t=' + (new Date()).getTime();
    });
    store.load({
        callback: function () {
            var rootN = new Ext.tree.TreeNode({
                expanded: true,
                leaf: false,
                text: 'Выберите действие',
                draggable: false,
                id: '-1'
            });
            var grids = [];
            store.each(function (r) {
                var nNode = new Ext.tree.TreeNode({
                    expanded: true,
                    leaf: true,
                    text: r.data.title,
                    id: '' + grids.length
                });
                rootN.appendChild(nNode);
                if (r.data.notgrid) {
                    grids.push(new Ext.Panel(r.data.config));
                }
                else {
                    grids.push(new ru.aisa.panel(r.data));
                }
                return true;
            })
            var menu = new Ext.tree.TreePanel({
                region: 'west',
                id: 'treepanel',
                //floatable:false,
                minSize: 75,
                maxSize: 250,
                split: true,
                collapsible: true,
                title: 'Разделы',
                width: 200,
                rootVisible: false,
                useArrows: true,
                autoScroll: true,
                animate: true,
                containerScroll: true,
                root: rootN,
                listeners: {
                    'render': function (tp) {
                        tp.getSelectionModel().on('selectionchange', function (tree, node) {
                            pan.layout.setActiveItem(Number(node.id));
                            if (typeof grids[Number(node.id)].getStore == 'function')
                                grids[Number(node.id)].getStore().load();
                        })
                    }
                }
            });

            var pan = new Ext.Panel({
                region: 'center',
                layout: 'card',
                border: false,

                //style:'owerflow:auto;',
                //autoHeight:true,
                //containerScroll:true,
                //title:'<span style="float: left; margin-left: 20px;">ЭТД Консоль</span><span style="float: right; margin-right: 20px;"><a href="j_spring_security_logout">Выйти</a></span>',
                //activeItem:0,
                items: grids
            });
            var head = new Ext.Panel({
                region: "north",
                border: false,
                html: "<table border='0' width='100%' height='100%'><tr><td width='20%'>&nbsp;</td><td align='center'><img src='logo.png' id='small-logo' height='55' width='155' /></td><td width='20%' style='padding-right: 20px;' align='top' id='userinfo'></td></tr></table>",
                height: 100
            });
            var userinfo = new Ext.Panel({
                title: "Личная информация",
//		region: "north",
                border: false,
                frame: true,
                html: '<div><center>Пользователь: ' + Get_Cookie('JUSERNAME') + '</center><a href="javascript:location.href = \'j_spring_security_logout\'">Выход</a></div>',
                height: 65
            });


            view = new Ext.Viewport({
                layout: 'border',

                items: [menu, pan, head]
            });
            menu.getSelectionModel().select(rootN.item(0));
            userinfo.render('userinfo');
            /*		for(var i=0;grids[i];i++){
             if(typeof grids[i].getStore != 'function'){
             var el=grids[i].findByType('fileuploadfield');
             for(var j=0;el[j];j++)
             el[j].setSize(200,0);
             //el[j].autoSize();
             //alert(el[j].getSize().height);
             //alert(el[j].getSize().width);
             }
             }*/
            if (Ext.isIE6) {
                document.getElementById('small-logo').style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='logo.png', sizingMethod='scale')";
                document.getElementById('small-logo').src = Ext.BLANK_IMAGE_URL;
            }
            Delete_Cookie('JUSERNAME');
        }
    });
}
function set_cookie(name, value, exp_y, exp_m, exp_d, path, domain, secure) {
    var cookie_string = name + "=" + escape(value);

    if (exp_y) {
        var expires = new Date(exp_y, exp_m, exp_d);
        cookie_string += "; expires=" + expires.toGMTString();
    }

    if (path)
        cookie_string += "; path=" + escape(path);

    if (domain)
        cookie_string += "; domain=" + escape(domain);

    if (secure)
        cookie_string += "; secure";


    document.cookie = cookie_string;
}

function Get_Cookie(name) {
    var start = document.cookie.indexOf(name + "=");
    var len = start + name.length + 1;
    if (( !start ) &&
        ( name != document.cookie.substring(0, name.length) )) {
        return null;
    }
    if (start == -1) return null;
    var end = document.cookie.indexOf(";", len);
    if (end == -1) end = document.cookie.length;
    return unescape(document.cookie.substring(len, end));
}

function Delete_Cookie(name, path, domain) {
    if (Get_Cookie(name)) document.cookie = name + "=" +
        ( ( path ) ? ";path=" + path : "") +
        ( ( domain ) ? ";domain=" + domain : "" ) +
        ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
}
