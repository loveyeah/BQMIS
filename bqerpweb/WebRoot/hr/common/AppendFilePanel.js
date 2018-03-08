AppendFilePanel = Ext.extend(Ext.FormPanel, {
    /**
     * 是否显示toolbar
     * 
     * @type Boolean
     */
    showToolbar : true,
    /**
     * 附件是否可以编辑
     * 
     * @type
     */
    readOnly : false,
    /**
     * 附件grid所需的store
     * 
     * @type Ext.data.Store
     */
    store : null,
    /**
     * 上传文件时参数
     * 
     * @type String
     */
    uploadParams : '',
    /**
     * 下载附件的方法名称
     * 
     * @type String
     */
    downloadFuncName : "",
    /**
     * 上传附件的url
     */
    uploadUrl : '',
    /**
     * 删除附件的url
     */
    deleteUrl : '',
    /**
     * 是否显示合同附件label
     * 
     * @type
     */
    showlabel : false,
    /**
     * 返回按钮
     * 
     * @type Ext.Button
     */
    returnButton : null,

    layout : 'absolute',
    height : 500,
    /**
     * private
     */
    initComponent : function() {
        // 创建grid
        this.createGrid();
        AppendFilePanel.superclass.initComponent.call(this);
        // 上传参数
        if (!this.uploadParams) {
            this.uploadParams = {};
        }
        this.addEvents(
        /**
         * 上传开始之前，可以用来传递一些上传时的参数
         * 
         * @event beforeupload
         */
        'beforeupload');
        // 设定grid的高度和宽度
        this.on('afterlayout', function() {
            this.grid_panel.setHeight(this.getInnerHeight());
            this.grid_panel.setWidth(this.getInnerWidth());
        })
    },
    /**
     * 创建附件grid
     * 
     * @access private
     */
    createGrid : function() {
        var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                header : "文件名",
                dataIndex : 'fileName',
                width : 200,
                sortable : true,
                renderer : this.fileNameCellRenderer.createDelegate(this)
            }, {
                header : '删除附件',
                width : 65,
                align : 'center',
                renderer : this.deleCellRenderer.createDelegate(this)
            }]);
        var toolbar = this.createToolbar();
        this.grid_panel = new Ext.grid.GridPanel({
            ds : this.store,
            enableColumnMove : false,
            cm : cm,
            border : false,
            tbar : toolbar
        });
        if (!this.showToolbar) {
            this.grid_panel.getTopToolbar().hide();
        }
        this.items = [this.grid_panel];
    },
    /**
     * 生成grid toolbar
     * 
     * @access private
     */
    createToolbar : function() {
        var buttons = [];
        this.edits = {};

        if (this.showlabel) {
            var label = new Ext.form.Label({
                text : "合同附件:"
            });
            buttons.push(label);
        }

        var select = new Ext.form.TextField({
            name : "fileUpload",
            width : 180,
            height : 20,
            inputType : 'file'
        });
        this.edits.select = select;
        buttons.push(select);
        buttons.push(new Ext.Toolbar.Separator());

        var add = new Ext.Button({
            text : '上传附件',
            iconCls : Constants.CLS_UPLOAD,
            handler : this.uploadAppendFile,
            scope : this
        });
        this.edits.add = add;
        buttons.push(add);

        if (this.returnButton) {
            var back = this.returnButton;
            buttons.push(back);
        }
        this.form = new Ext.FormPanel({
            frame : true,
            fileUpload : true,
            style : "border:0px",
            items : buttons
        })

        this.updateToolbar();
        return (new Ext.Toolbar(this.form))
    },
    /**
     * 设置上传按钮的可用性
     */
    updateToolbar : function() {
        if (this.readOnly) {
            this.edits.select.disable();
            this.edits.add.disable();
        } else {
            this.edits.select.enable();
            this.edits.add.enable();
        }
    },
    /**
     * 设置不可用
     * 
     * @access public
     */
    disable : function() {
        if (this.edits) {
            this.edits.select.disable();
            this.edits.add.disable();
        }
        this.readOnly = true;
        this.grid_panel.getView().refresh();
    },
    /**
     * 设置可用
     * 
     * @access public
     */
    enable : function() {
        if (this.edits) {
            this.edits.select.enable();
            this.edits.add.enable();
        }
        this.readOnly = false;
        this.grid_panel.getView().refresh();
    },
    /**
     * 文件名称link渲染
     * 
     * @access private
     */
    fileNameCellRenderer : function(value, cellmeta, record) {
        if (value != "") {
            var id = record.get("fileId");
            var download = this.downloadFuncName + '("' + id + '");return false;';
            return "<a href='#' onclick='" + download + "'>" + value + "</a>";
        } else {
            return "";
        }
    },
    /**
     * 文件删除渲染
     * 
     * @access private
     */
    deleCellRenderer : function(value) {
        // 文件删除函数
        deleteAppendHandler = this.deleteAppendFile.createDelegate(this);
        if (this.readOnly) {
            return "<img src='comm/ext/tool/dialog_close_btn.gif'>";
        }
        return "<a href='#'  onclick= 'deleteAppendHandler();return false;'>"
        + "<img src='comm/ext/tool/dialog_close_btn.gif'></a>";
    },
    /**
     * 判断是否是正确的文件路径
     */
    checkFilePath : function(filePath) {
        if (!filePath)
            return false;
        return /^(\w:|\\\\)/.test(String.escape(filePath));
    },
    /**
     * 清除附件上传控件原始值
     * 
     * @access public
     */
    clearAppendFileValue : function() {

        // 清除附件内容
        var domAppend = this.edits.select.el.dom;
        var parent = domAppend.parentNode;

        // 保存
        var domForSave = domAppend.cloneNode();
        // 移除附件控件
        parent.removeChild(domAppend);
        // 再追加控件
        parent.appendChild(domForSave);
        // 应用该控件
        this.edits.select.applyToMarkup(domForSave);
    },
    /**
     * 删除附件
     * 
     * @access private
     */
    deleteAppendFile : function() {
        var grid = this.grid_panel;
        var store = this.grid_panel.getStore();
        var record = this.grid_panel.selModel.getSelected();
        var url = this.deleteUrl;
        if (!record)
            return;
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002, function(button) {
            // 是否删除
            if (button == 'yes') {
                // 数据库中删除
                Ext.Ajax.request({
                    url : url,
                    method : Constants.POST,
                    params : {
                        fileId : record.get('fileId'),
                        lastModifiyDate : record.get('lastModifiyDate')
                    },
                    success : function(result, response) {
                        var o = eval("(" + result.responseText + ")");
                        if (o.msg == Constants.SQL_FAILURE) {
                            // db操作失败
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            return;
                        }
                        store.reload();
                        // 删除成功
                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005, function() {
                        });
                    }
                })
            }
        })
    },
    /**
     * 上传附件
     */
    uploadAppendFile : function() {
        this.fireEvent('beforeupload', this);
        var fileName = this.edits.select.getValue();
        this.edits.select.validate();
        if (!fileName) {
            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_003, '上传附件'));
            return;
        }
        if (!this.checkFilePath(fileName)) {
            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_025, fileName));
            return;
        }
        var store = this.store;
        Ext.Ajax.request({
            url : this.uploadUrl,
            method : Constants.POST,
            form : this.form.el,
            isUpload : true,
            params : this.uploadParams,
            success : function(action) {
                if(action.responseText.charAt(0) != "{"){
                    // 文件太大上传不了，没有传到action
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    return;
                }                
                var o = Ext.decode("(" + action.responseText + ")");
                if (o.msg == Constants.FILE_NOT_EXIST) {
                    // 文件不存在
                    Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_025, fileName));
                    return;
                }
                if (o.msg == Constants.IO_FAILURE || o.msg == Constants.SQL_FAILURE) {
                    // 操作失败
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    return;
                }
                store.reload();
                Ext.Msg.alert(Constants.REMIND, Constants.UPLOAD_SUCCESS);
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
            }
        })
    }
});