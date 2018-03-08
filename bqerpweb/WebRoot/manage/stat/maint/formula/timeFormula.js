Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
    var args = window.dialogArguments;
    var itemCode = args ? args.itemCode : 'lsyy1';
    var deriveDataType = args ? args.deriveDataType : '3';
    
    var itemType;
    var dataTimeType;
    if (deriveDataType == 2) {
        itemType = 2;
    }
    //时段
    if (deriveDataType == 5) {
        dataTimeType = 1;
    }
    
    function hideColumn(deriveDataType){
        if (deriveDataType == '2') {
            grid.getColumnModel().setHidden(3, true);
            
        }
        
    }
    
    var MyRecord = Ext.data.Record.create([{
        name: 'runFormulaInfo.id.itemCode'
    }, {
        name: 'runFormulaInfo.id.runDataCode'
    }, {
        name: 'runFormulaInfo.operatorCode'
    }, {
        name: 'runFormulaInfo.deriveDataType'
    }, {
        name: 'runFormulaInfo.sdType'
    }, {
        name: 'runFormulaInfo.enterpriseCode'
    }, {
        name: 'runFormulaInfo.displayNo'
    }, {
        name: 'itemCodeName'
    }, {
        name: 'runDataCodeName'
    }]);
    
    var dataProxy = new Ext.data.HttpProxy({
        url: 'manager/getRunFormulaList.action '
    });
    
    var theReader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "list"
    }, MyRecord);
    
    var store = new Ext.data.Store({
        proxy: dataProxy,
        reader: theReader
    });
    store.load({
        params: {
            deriveDataType: deriveDataType,
            itemCode: itemCode
        }
    });
    store.on('update', function(store){
        var i;
        var strTemp = "";
        
        for (i = 0; i < store.getCount(); i++) {
        
            if (deriveDataType == '2') {
            
                strTemp += store.getAt(i).get("runFormulaInfo.operatorCode") +
                store.getAt(i).get("runDataCodeName");
            }
            else 
                if (deriveDataType == '5') {
                
                    strTemp += store.getAt(i).get("runFormulaInfo.operatorCode") +
                    store.getAt(i).get("runDataCodeName") +
                    "(" +
                    (store.getAt(i).get("runFormulaInfo.sdType") == '1' ? '高峰' : (store.getAt(i).get("runFormulaInfo.sdType") == '2' ? '腰荷' : (store.getAt(i).get

("runFormulaInfo.sdType") == '3' ? '低谷' : (store.getAt(i).get("runFormulaInfo.sdType") == '6' ? '一值' : (store.getAt(i).get("runFormulaInfo.sdType") == '7' ? '二值' : 

(store.getAt(i).get("runFormulaInfo.sdType") == '8' ? '三值' : (store.getAt(i).get("runFormulaInfo.sdType") == '9' ? '四值' : '五值'))))))) +
                    ")";
                    
                }
        }
        
        Ext.get("winspace").dom.value = strTemp.substring(1);
        
    })
    store.on('load', function(store){
        var i;
        var strTemp = "";
        if (deriveDataType == '2') {
            for (i = 0; i < store.getCount(); i++) {
                strTemp += store.getAt(i).get("runFormulaInfo.operatorCode") +
                store.getAt(i).get("runDataCodeName");
            }
        }
        else 
            if (deriveDataType == '5') {
                for (i = 0; i < store.getCount(); i++) {
                    strTemp += store.getAt(i).get("runFormulaInfo.operatorCode") +
                    store.getAt(i).get("runDataCodeName") +
                    "(" +
                    (store.getAt(i).get("runFormulaInfo.sdType") == '1' ? '高峰' : (store.getAt(i).get("runFormulaInfo.sdType") == '2' ? '腰荷' : (store.getAt(i).get

("runFormulaInfo.sdType") == '3' ? '低谷' : (store.getAt(i).get("runFormulaInfo.sdType") == '6' ? '一值' : (store.getAt(i).get("runFormulaInfo.sdType") == '7' ? '二值' : 

(store.getAt(i).get("runFormulaInfo.sdType") == '8' ? '三值' : (store.getAt(i).get("runFormulaInfo.sdType") == '9' ? '四值' : '五值'))))))) +
                    ")";
                }
            }
        Ext.get("winspace").dom.value = strTemp.substring(1);
    });
    
    // function checkFormula() {
    // try {
    // var strTemp = "";
    // for (var i = 0; i < store.getCount(); i++) {
    // var rec = store.getAt(i);
    // if (rec.get("fornulaType") ==
    // itemBaseAttr.formulaContentType.item)
    // strTemp += " " + i + " ";
    // else
    // strTemp += " " + rec.get("formulaContent") + " ";
    // }
    // Ext.util.JSON.decode(strTemp);
    // return true;
    // } catch (err) {
    // Ext.Msg.alert("错误", "公式维护错误!");
    // return false;
    // }
    // }
    
    var sm = new Ext.grid.CheckboxSelectionModel({
        singleSelect: false
    });
    
    number = new Ext.grid.RowNumberer({
        header: "",
        align: 'left'
    })
    // 重置排序号
    function resetLine(){
        for (var j = 0; j < store.getCount(); j++) {
            var temp = store.getAt(j);
            temp.set("displayNo", j + 1);
            temp.set("runFormulaInfo.displayNo", temp.get("displayNo"));
            
        }
    }
    
    // 增加
    function addRecord(){
    	if(deriveDataType == '5' ){
    		if(grid.store.getCount() == 1){
    			Ext.Msg.alert("提示","班组指标只能累计一个时指标!");
    			return;
    		}
    	}
        var currentRecord = grid.getSelectionModel().getSelected();
        var rowNo = store.indexOf(currentRecord);
        var count = store.getCount();
        var currentIndex = currentRecord ? rowNo + 1 : count;
        // var currentIndex = count;
        
        var o = new MyRecord({
        
            'runFormulaInfo.operatorCode': '+',
            'runFormulaInfo.deriveDataType': deriveDataType,
            'runFormulaInfo.sdType': '1',
            'runDataCodeName': ''
        });
        grid.stopEditing();
        store.insert(currentIndex, o);
        sm.selectRow(currentIndex);
        grid.startEditing(currentIndex, 3);
        resetLine();
    }
    
    // 删除记录
    
    var isDeletes = false;
    function deleteRecords(){
        var sm = grid.getSelectionModel();
        var selections = sm.getSelections();
        if (selections.length == 0) {
            Ext.Msg.alert("提示", "请选择要删除的记录！");
        }
        else {
            for (var i = 0; i < selections.length; i += 1) {
                var member = selections[i];
                grid.getStore().remove(member);
                grid.getStore().getModifiedRecords().remove(member);
                isDeletes = true;
            }
            resetLine();
        }
    }
    
    // 保存
    function saveModifies(){
        grid.stopEditing();
        // 验证公式正确性
        // if (!checkFormula())
        // return;
        
        var modifyRec = grid.getStore().getModifiedRecords();
        if (modifyRec.length > 0 || isDeletes) {
            var updateData = new Array();
            for (var i = 0; i < grid.getStore().getCount(); i++) {
                var obj = store.getAt(i).data;
                
                // 非空限制，待加
                // if (rec.formulaContent == null || rec.formulaContent
                // == '') {
                // Ext.Msg.alert('提示', '第' + (i + 1) + '行公式内容不能为空');
                // return;
                // }
                ;
                updateData.push(obj);
            }
            Ext.Ajax.request({
                url: 'manager/saveRunFormulaList.action',
                method: 'post',
                params: {
                    itemCode: itemCode,
                    isUpdate: Ext.util.JSON.encode(updateData)
                },
                success: function(result, request){
                    var o = eval('(' + result.responseText + ')');
                    Ext.MessageBox.alert('提示信息', o.msg);
                    store.rejectChanges();
                    isDeletes = false;
                    store.reload();
                },
                failure: function(result, request){
                    Ext.MessageBox.alert('提示信息', '未知错误！')
                }
            })
        }
        else {
            Ext.MessageBox.alert('提示信息', '没有做任何修改！')
        }
    }
    // 取消
    function cancel(){
        var modifyRec = store.getModifiedRecords();
        if (modifyRec.length > 0 || isDeletes) {
            if (!confirm("确定要放弃修改吗")) 
                return;
            store.reload();
            store.rejectChanges();
            isDeletes = false;
        }
        else {
            store.reload();
            store.rejectChanges();
            isDeletes = false;
        }
    }
    // 关闭
    function close(){
        var object = Ext.get("winspace").dom.value;
        
        // alert(record.get('standardInfo.professionCode'));
        window.returnValue = object;
        window.close();
    }
    // // 时间类型
    // var Operate = Ext.data.Record.create([{
    // name : 'value'
    // }, {
    // name : 'key'
    // }]);
    //
    // var operateStore = new Ext.data.JsonStore({
    // url : '',
    // fields : Operate
    // });
    // operateStore.loadData(itemBaseAttr.operates);
    
    var formulaContentTypeBox = new Ext.form.ComboBox({
        store: new Ext.data.SimpleStore({
            fields: ["retrunValue", "displayText"],
            data: [['1', '高峰'], ['2', '腰荷'], ['3', '低谷'], ['6', '一值'], ['7', '二值'], ['8', '三值'], ['9', '四值'], ['10', '五值']]
        }),
        valueField: "retrunValue",
        displayField: "displayText",
        mode: 'local',
        forceSelection: true,
        blankText: '',
        emptyText: '',
        editable: false,
        triggerAction: 'all',
        selectOnFocus: true,
        allowBlank: false,
        anchor: '99%'
    
    });
    // 时间类型
    var Operate = Ext.data.Record.create([{
        name: 'value'
    }, {
        name: 'key'
    }]);
    
    var operateStore = new Ext.data.JsonStore({
        url: '',
        fields: Operate
    });
    operateStore.loadData(itemBaseAttr.operates);
    var operatorEditor = new Ext.form.ComboBox({
        store: operateStore,
        id: 'operateBox',
        valueField: "key",
        displayField: "value",
        mode: 'local',
        typeAhead: true,
        forceSelection: true,
        editable: false,
        triggerAction: 'all',
        selectOnFocus: true,
        allowBlank: true,
        emptyText: '请选择',
        anchor: '85%'
    })
    
    // 定义grid
    
    var grid = new Ext.grid.EditorGridPanel({
        autoScroll: true,
        store: store,
        layout: 'fit',
        columns: [sm, // 选择框
 number, {
            header: "运行数据名称",
            sortable: false,
            dataIndex: 'runDataCodeName'
            // ,
            // editor :
            // new Ext.form.NumberField({
            // listeners : {
            // change : function() {
            // var rec = grid.getSelectionModel()
            // .getSelected();
            // alert(this.value);
            // rec.set("rowItemCode", this.value);
            // }
            // }
            // })
        }, {
            header: "时段类型",
            align: "center",
            sortable: true,
            dataIndex: 'runFormulaInfo.sdType',
            renderer: function changeIt(val){
                if (val == "1") {
                    return "高峰";
                }
                else 
                    if (val == "2") {
                        return "腰荷";
                    }
                    else 
                        if (val == "3") {
                            return "低谷";
                        }
                        else 
                            if (val == "6") {
                                return "一值";
                            }
                            else 
                                if (val == "7") {
                                    return "二值";
                                }
                                else 
                                    if (val == "8") {
                                        return "三值";
                                    }
                                    else 
                                        if (val == "9") {
                                            return "四值";
                                        }
                                        else 
                                            if (val == "10") {
                                                return "五值";
                                            }
                                            else {
                                                return "内容类别异常";
                                            }
            },
            editor: formulaContentTypeBox
        }, {
            header: "计算符号",
            align: "center",
            sortable: true,
            dataIndex: 'runFormulaInfo.operatorCode',
            
            editor: operatorEditor
            // }, {
            // header : "顺序",
            // align : "center",
            // sortable : true,
            // dataIndex : 'runFormulaInfo.displayNo',
            //
            // editor : operatorEditor
        }],
        tbar: [{
            text: "新增",
            iconCls: 'add',
            handler: addRecord
        }, {
            text: "删除",
            iconCls: 'delete',
            handler: deleteRecords
        }, {
            text: "保存",
            iconCls: 'save',
            handler: saveModifies
        }, {
            text: "取消",
            iconCls: 'reflesh',
            handler: cancel
        }, {
            text: "关闭",
            iconCls: 'cancer',
            handler: close
        }],
        sm: sm,
        clicksToEdit: 1
        // ,
        // listeners : {
        // 'beforeedit' : function(e) {
        // if (e.field == "formulaContent") {
        // var type = e.record.get('fornulaType');
        // if (type == itemBaseAttr.formulaContentType.item) {
        // Ext.Msg.alert("提示", "请从右边面板中选择指标!");
        // return false;
        // } else if (type ==
        // itemBaseAttr.formulaContentType.operate) {
        // grid.getColumnModel().setEditor(2,
        // new Ext.grid.GridEditor(new Ext.form.ComboBox({
        // store : operateStore,
        // id : 'operateBox',
        // valueField : "key",
        // displayField : "value",
        // mode : 'local',
        // typeAhead : true,
        // forceSelection : true,
        // editable : false,
        // triggerAction : 'all',
        // selectOnFocus : true,
        // allowBlank : true,
        // emptyText : '请选择',
        // anchor : '85%',
        // listeners : {
        // 'select' : function(combo,
        // record, index) {
        // var rec = grid
        // .getSelectionModel()
        // .getSelected();
        // rec.set("rowItemCode",
        // record.data.key);
        // }
        // }
        // })));
        // } else if (type == itemBaseAttr.formulaContentType.data)
        // {
        // grid
        // .getColumnModel()
        // .setEditor(
        // 2,
        // new Ext.grid.GridEditor(new Ext.form.NumberField()));
        // }
        // }
        // }
        // }
    });
    
    /* 模糊查询的grid */
    
    var query = new Ext.form.TextField({
        id: 'argFuzzy',
        
        fieldLabel: "模糊查询",
        hideLabel: true,
        emptyText: '指标编码/名称模糊查询',
        name: 'argFuzzy',
        value: '',
        width: 250
    });
    function fuzzyQuery(){
        con_ds.reload({
            params: {
                start: 0,
                limit: 18,
                argFuzzy: query.getValue(),
                dataTimeType: dataTimeType,
                itemType: itemType
            }
        });
    };
    
    // 签字时的Enter
    document.onkeydown = function(){
        if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
            fuzzyQuery();
            // document.getElementById('btnSign').click();
        }
    }
    
    var con_item = Ext.data.Record.create([{
        name: 'itemCode'
    }, {
        name: 'itemName'
    }, {
        name: 'unitCode'
    }, {
        name: 'itemType'
    }]);
    var con_sm = new Ext.grid.CheckboxSelectionModel({
        singleSelect: true
    
    });
    var con_ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'manager/getAllStatItemaList.action'
        }),
        reader: new Ext.data.JsonReader({
            totalProperty: "totalCount",
            root: "list"
        }, con_item)
    });
    con_ds.load({
        params: {
            start: 0,
            limit: 18,
            argFuzzy: query.getValue(),
            dataTimeType: dataTimeType,
            itemType: itemType
        }
    })
    con_ds.on('beforeload', function(){
        Ext.apply(this.baseParams, {
            itemType: itemType,
            dataTimeType: dataTimeType,
            argFuzzy: query.getValue()
        });
    });
    var con_item_cm = new Ext.grid.ColumnModel([{
        header: '指标编码',
        dataIndex: 'itemCode',
        align: 'center'
    
    }, {
        header: '指标名称',
        dataIndex: 'itemName',
        align: 'center'
    }]);
    con_item_cm.defaultSortable = true;
    
    var gridbbar = new Ext.PagingToolbar({
        pageSize: 18,
        store: con_ds,
        displayInfo: true,
        displayMsg: "显示第{0}条到{1}条，共{2}条",
        beforePageText: '',
        afterPageText: '',
        emptyMsg: "没有记录"
    });
    var Grid = new Ext.grid.GridPanel({
    
        ds: con_ds,
        cm: con_item_cm,
        height: 300,
        split: true,
        autoScroll: true,
        bbar: gridbbar,
        border: true
    });
    
    Grid.on('rowdblclick', modifyBtn);
    function modifyBtn(){
        var recR = Grid.getSelectionModel().getSelected();
        if (recR.get('itemCode') == itemCode) {
            Ext.Msg.alert("提示", "指标公式不能包含该指标本身！");
            return;
        }
        
        var recL = grid.getSelectionModel().getSelected();
        if (recL) {
            recL.set("runFormulaInfo.id.runDataCode", recR.get('itemCode'));
            recL.set("runDataCodeName", recR.get('itemName'));
        }
        else {
            Ext.Msg.alert("提示", "请选择左边的一条记录！")
        }
    }
    
    // 指标体系树
    var rootNode = {
        id: '0',
        text: '指标编码体系',
        attributes: {
            "isItem": "N"
        }
    };
    var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
    var deptTree = new Ext.tree.TreePanel({
    
        loader: new Ext.tree.TreeLoader({
            dataUrl: 'manager/findStatTree.action'
        }),
        root: rootNodeObj,
        autoWidth: true,
        layout: 'fit',
        autoScroll: true,
        animate: true,
        enableDD: false,
        border: false,
        rootVisible: true,
        containerScroll: true
    });
    deptTree.on('beforeload', function(node){
        deptTree.loader.dataUrl = 'manager/findStatTree.action?pid=' +
        node.id;
    });
    
    deptTree.on('dblclick', function(node, e){
        if (node.id == itemCode) {
            Ext.Msg.alert("提示", "指标公式不能包含该指标本身！");
            return;
        }
        
        // 防止指标重复（否则主键重复）
        if (node.attributes.description == 'Y') {
        
            if (node.attributes.cls == '2' && deriveDataType == '2') {
                var recL = grid.getSelectionModel().getSelected();
                if (recL) {
                    recL.set("runFormulaInfo.id.runDataCode", node.id);
                    recL.set("runDataCodeName", node.text);
                }
                else {
                    Ext.Msg.alert("提示", "请从左边选择一条[内容类别]为[指标]的记录！")
                }
            }
            else {
                var recL = grid.getSelectionModel().getSelected();
                if (recL) {
                    recL.set("runFormulaInfo.id.runDataCode", node.id);
                    recL.set("runDataCodeName", node.text);
                }
                else {
                    Ext.Msg.alert("提示", "请从左边选择一条[内容类别]为[指标]的记录！")
                }
            }
        }
        else {
            Ext.Msg.alert("提示", "该指标无数据！")
        }
    });
    
    rootNodeObj.expand();
    
    var winspace = new Ext.form.TextArea({
        id: 'winspace',
        readOnly: true,
        hideLabel: true
    });
    
    var innerPanel2 = new Ext.Panel({
        hidden: true,
        layout: "fit",
        items: [{
        
            layout: 'fit',
            border: false,
            frame: false,
            // region : "center",
            
            items: [winspace]
        }]
    
    });
    
    var innerPanel1 = new Ext.Panel({
    
        layout: "fit",
        hidden: true,
        border: false,
        tbar: [query, new Ext.Button({
            iconCls: 'query',
            text: '查询',
            handler: function(){
                con_ds.reload();
            }
        })],
        items: [{
            border: false,
            frame: false,
            layout: 'fit',
            items: [Grid]
        }]
    
    });
    
    var rightPanel = new Ext.Panel({
    
        layout: "fit",
        tbar: [{
            text: "项目选择",
            id: "prjChose",
            handler: prjChose
        }, {
            text: "模糊查询",
            id: "Query",
            handler: Query
        }, {
            text: "公式生成",
            id: "formulaMade",
            handler: formulaMade
        
        }],
        items: [{
            autoScroll: true,
            layout: 'fit',
            border: false,
            frame: false,
            items: [innerPanel2, deptTree, innerPanel1]
        }]
    })
    
    new Ext.Viewport({
        enableTabScroll: true,
        layout: "border",
        border: false,
        frame: false,
        items: [{
            // bodyStyle : "padding: 20,30,20,20",
            layout: 'fit',
            border: false,
            frame: false,
            region: "west",
            width: '50%',
            
            items: [grid]
        }, {
            // bodyStyle : "padding: 20,20,20,20",
            region: "center",
            border: false,
            frame: false,
            layout: 'fit',
            // width : '50%',
            items: [rightPanel]
        }]
    });
    
    // deptTree.hide();
    // innerPanel1.hide();
    function prjChose(){
        innerPanel1.hide();
        innerPanel2.hide();
        deptTree.show();
        
    }
    function Query(){
    
        innerPanel2.hide();
        deptTree.hide();
        innerPanel1.show();
        
    }
    function formulaMade(){
        innerPanel1.hide();
        deptTree.hide();
        innerPanel2.show();
        
    };
    window.onbeforeunload = onbeforeunload_handler;
    
    function onbeforeunload_handler(){
        var object = Ext.get("winspace").dom.value;
        window.returnValue = object;
        
    }
    hideColumn(deriveDataType);
    
})
