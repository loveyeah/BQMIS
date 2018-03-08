Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
        function Register() {
        // 到货单号
        this.mifNo = null;
        this.supplierCode = null;
        this.arrivalId = null;
        
        // 新增监听器
        this.addRecord = null;
        // 新增监听器
        this.regeRecord = null;


        Register.prototype.edit = function(argMifNo,argSupplierCode,argArrivalId) {
            // 设置到货单号
            this.mifNo = argMifNo;
            this.supplierCode = argSupplierCode;
             this.arrivalId = argArrivalId;
            // 设置第二个页面为活动页面
            this.toTab2();
            if(this.addRecord && typeof this.addRecord == 'function' ){
                this.addRecord.call(this, this.mifNo,this.supplierCode,this.arrivalId);
            }
         }
        Register.prototype.add = function() {
//                this.toTab1();
                 if (this.regeRecord && typeof this.regeRecord == 'function') {
                    // 新增操作票
                    this.regeRecord.apply(this);
                }
        }

        Register.prototype.toTab2 = function() {
                tabPanel.setActiveTab('arriveCangoRegister');
         }

         Register.prototype.toTab1 = function() {
                tabPanel.setActiveTab('tablePanel');
         }
     }
    var register = new Register();
     register.regeRecord = tabInti;
    Ext.QuickTips.init();

       // ↓↓*******************到货登记列表Tab**************************************
       // 到货gridStore
      var arriveCangoStore = new Ext.data.JsonStore({
        url : 'resource/getTableDetails.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : [
            // 到货单号
            {name:'mifNo'},
            // 供应商编号
            {name: 'supplierId'}, 
             // 供应商
            {name: 'supplier'}, 
            // 日期
            {name: 'date'},
            //流水号
            {name: 'arrivalId'}]
    });
    arriveCangoStore.setDefaultSort("mifNo",'DESC');//将升序显示ASC改为降序DESC显示 modify by ywliu 2009/7/2
    arriveCangoStore.load(  {
        params: {
                             start: 0, 
                             limit : Constants.PAGE_SIZE
                          }});
       // 确认按钮
       var btnSub = new Ext.Button({
        text : "修改",
        iconCls : Constants.CLS_OK,
        handler : function() {
            var rec = gridTable.getSelectionModel().getSelections();
            if(rec == null) {
                 Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
            } else {
                submitBtn1();
            }
        }
       })
      // tbar
       var tableTbar = new Ext.Toolbar({
              items : [btnSub]
       })
       var gridTable = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        buttonAlign : 'center',
       style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        // 标题不可以移动
        enableColumnMove : false,
        store : arriveCangoStore,
        // 单选
        sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
        columns : [new Ext.grid.RowNumberer({
                            header : "行号",
                            align : 'right',    
                            width : 35
                        }),
            // 到货单号
            {   header : "到货单号",
                width : 150,
                sortable : true,
                align : 'left',
                defaultSortable : true,
                dataIndex : 'mifNo'
            },
            // 供应商
            {   header : "供应商",
                width : 150,
                align : 'left',
                sortable : true,
                dataIndex : 'supplier'
            },
            // 日期
            {   header : "日期",
                width : 150,
                renderer : fromDateRender,    
                align : 'left',
                sortable : true,
                dataIndex : 'date'
            }
        ],
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        },
        tbar : tableTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : arriveCangoStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
       });
   
    // 双击事件
    gridTable.on('rowdblclick', submitBtn1);
       // 列表Tab
       var tablePanel = new Ext.Panel({
        layout : 'fit',
        id:"tablePanel",
        title : '列表',
          style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        id : 'tablePanel',
        border : false,
        items :[gridTable]
       });

       // ↓↓*******************到货登记布局**************************************
       // tabPanel
       var tabPanel = new Ext.TabPanel({
           renderTo : document.body,
        activeTab : 0,
         id:"tabPanel",
        style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
        tabPosition : 'bottom',
        autoScroll : true,
        items : [{
               id : 'arriveCangoRegister',
            title : '登记',
            html : "<iframe name='arriveCangoRegister' src='run/resource/buy/arriveCango/arriveCangoRegister/RB003_Register.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, tablePanel]
       });

       // 显示区域
       var layout = new Ext.Viewport({
           layout : 'fit',
           margins : '0 0 0 0',
          region : 'center',
          border : true,
           items : [tabPanel]        
       });
         //***************************************
    Ext.getCmp("tabPanel").register = register;

         // 列表Tabgrid双击处理
    function submitBtn1(){
        var sm = gridTable.getSelectionModel();
        var selected = sm.getSelections();
        if (selected.length == 0) {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
        } else{
            var member=selected[0].data;
             // 初始化到货tab
            var mifNo = member["mifNo"];
            var supplierCode = member["supplierId"];
            var arriveId = member["arrivalId"];
            register.edit(mifNo, supplierCode,arriveId);
        }
    }
    function tabInti() {
        arriveCangoStore.reload(  {
        params: {
                             start: 0, 
                             limit : Constants.PAGE_SIZE
                          }});
    }

   /**
     * 渲染日期
     */
    function fromDateRender(value) {
        if (!value)
            return '';
        if (value instanceof Date)
            return renderDate(value);
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate)
            return "";
        return strDate;
    }
});