Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();
    // 参数
    var arg = window.dialogArguments;
    // 模式
    var mode = arg.mode;
    // 劳动合同id
    var contractId = arg.contractId;
    // 附件来源
    var fileOriger = arg.fileOriger;

    // 合同附件store
    var dsMatieria = new Ext.data.JsonStore({
        url : 'hr/getContractAppendFileDatas.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : [
            // 流水号
            'fileId',
            // 文件名
            'fileName',
            // 更新日时
            'lastModifiyDate']
    });
    // load时的参数
    dsMatieria.on('beforeload', function() {
        Ext.apply(this.baseParams, {
            workcontractid : contractId,
            fileOriger : fileOriger
        });
    })
    dsMatieria.load();
    // 返回按钮
    var btnReturn = new Ext.Button({
        text : "返回",
        iconCls : Constants.CLS_BACK,
        handler : function() {
            window.close();
        }
    });
    /**
     * 下载文件
     */
    downloadFile = function(id) {
        document.all.blankFrame.src = "hr/downloadContractAppendFile.action?fileId=" + id;
    }
    // 附件panel
    var appendFilePanel = new AppendFilePanel({
        // 显示工具栏
        // 是否可编辑
        readOnly : mode == "read",
        // store
        store : dsMatieria,
        // 下载方法名字
        downloadFuncName : 'downloadFile',
        // 上传文件url
        uploadUrl : 'hr/uploadContractAppendFile.action',
        // 上传文件时参数
        uploadParams : {
            workcontractid : contractId,
            fileOriger : fileOriger
        },
        // 删除url
        deleteUrl : 'hr/deleteContractAppendFile.action',
        // 工具栏按钮
        returnButton : btnReturn
    });
    var view = new Ext.Viewport({
        enableTabScroll : true,
        autoScroll : true,
        layout : "fit",
        items : [appendFilePanel]
    })
})
