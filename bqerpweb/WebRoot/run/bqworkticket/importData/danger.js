Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var photo = new Ext.form.TextField({
        id : "path", 
        editable:true,
        fieldLabel:'设备目录',
        name : 'path',
        width : 800,
        height : 22 
    });
    var equLevelId = new Ext.form.NumberField({
        id : "equLevelId",
        fieldLabel : '设备目录ID' 
    });
     var equLevelCode = new Ext.form.TextField({
        id : "equLevelCode",
        fieldLabel : '设备目录CODE' 
    });
    var bntConfirm = new Ext.Button({
    	text:'导入',
    	handler:function()
    	{ 
//    		Ext.Msg.wait("正在保存数据，请等待！");
    		form.getForm().submit({
                method : "post", 
                waitMsg:'正在保存数据，请等待！',
                url : 'comm/createOpration.action',
                success : function(form, action) {
                    Ext.Msg.alert("提示","操作成功!");
                },
                faliue : function() {
                    Ext.Msg.alert("错误","操作失败!");
                }
            });
    	}
    });
  var form = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 200,
		title : '导入',
		items : [photo,equLevelId,equLevelCode,bntConfirm]}
		);
  new Ext.Viewport({
  	layout:'fit',
  	items:[form]
  });
});
