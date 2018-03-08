Ext.onReady(function() {
var arg = window.dialogArguments;
var danger_id=arg.dangerId;//部门录入ID需传入
//根据部门录入ID查询
// grid列表数据源
var Record = new Ext.data.Record.create([{
				name : 'dangerId'
			},{
				name : 'enterpriseCode'
			},{
				name : 'id'
			},{
				name : 'isUse'
			},{
				name : 'lastModifiedBy'
			},{
				name : 'lastModifiedDate'
			},{
				name : 'score1'
			},  {
				name : 'score10'
			},{
				name : 'score2'
			}, {
				name : 'score3'
			}, {
				name : 'score4'
			}, {
				name : 'score5'
			}, {
				name : 'score6'
			}, {
				name : 'score7'
			}, {
				name : 'score8'
			}, {
				name : 'score9'
			},{
				name : 'scoreSort'
				//,mapping:0
			}]);
var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'security/findScorebyDangerId.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, Record);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});			
			
	function cancerChange() {

		store.reload();
		store.rejectChanges();
	}
	

var gridTbar = new Ext.Toolbar({
				items : [{
							id : 'save',
							text : "保存",
							iconCls : 'save',
							handler : saveDangerScore
						}, '-', {
							id : 'cancer',
							text : "取消",
							iconCls : 'cancer',
							handler : cancerChange
						}]
			});	
			
		
	
function saveDangerScore() {
		var alertMsg = "";
		
		dangerTypegrid.stopEditing();
	
		var modifyRec = dangerTypegrid.getStore().getModifiedRecords();
		 if(modifyRec.length>0)
		 {
			
		 	
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i <store.getCount(); i++) {
							updateData.push(store.getAt(i).data);
						}
						Ext.Ajax.request({
									url : 'security/saveDangerDeptValue.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData)
									},
									success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									if (obj && obj.msg) {
										Ext.Msg.alert('提示', obj.msg);
										if (obj.msg.indexOf('已经存在') != -1)
											return;
									}
									store.commitChanges();
									store.rejectChanges();
									store.reload();
								},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
	}
	else {Ext.Msg.alert('提示','没有更改的数据!')}
	}	
	

		
// 页面的Grid主体
var dangerTypegrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		clicksToEdit:1,
		columns : [{
					header : '类别',
					dataIndex : 'scoreSort',
					align : 'left',
					width : 150,
					renderer : function(v) {
						if (v =='1') {
							return 'L值';
						} else {
							return 'B2值';
						}
					}
				}, {
					header : '1分',
					dataIndex : 'score1',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
						allowBlank : true,
						id : 'score1',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							
					})	
				}, {
					header : '2分',
					dataIndex : 'score2',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score2',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '3分',
					dataIndex : 'score3',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score3',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '4分',
					dataIndex : 'score4',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score4',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '5分',
					dataIndex : 'score5',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score5',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '6分',
					dataIndex : 'score6',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score6',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '7分',
					dataIndex : 'score7',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score7',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '8分',
					dataIndex : 'score8',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score8',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '9分',
					dataIndex : 'score9',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score9',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})

				}, {
					header : '10分',
					dataIndex : 'score10',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'score10',
						listeners: {   
           					 'blur': function(v){   
                						   var s = v.getValue().split(",");
                						   var check =/^[0-9]*$/;
 ;
                						   for(i=0;i<s.length;i++){
                						   		if(!check.test(s[i])){
                						   			Ext.Msg.alert('提示','您输入了非法字符，请输入数字！');
                						   			v.setValue('');
                						   			return;
                						   		}
                						   }}
                		}
							})
				}],
			bbar:gridTbar
//		viewConfig : {
//			forceFit : true
//		}
//		,
//		autoWidth:true,
//		autoHeight:true
		
});

	
	

store.baseParams = {
			dangerId :danger_id
		};
		
		store.load();

	
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : true,
				frame : false,
				items : [{
        					region: 'north',
       						html: '<h1 class="x-panel-header">说明：分值里面填写具体项时，请用英文\',\'隔开</h1>',
       						autoHeight: true,
       						border: false,
        					margins: '0 0 5 0'
    						},
					{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [dangerTypegrid]
						}]
			});
			
})