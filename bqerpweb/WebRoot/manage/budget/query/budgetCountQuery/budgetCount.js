Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var itemFCode;
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";

		return s;
	}
	var itemId ;
	var itemCode;
	function treeClick(node, e) {
		e.stopEvent();
		panel.setTitle("当前选择指标:[" + node.text + "]");

			var isItem = node.attributes.description;
			form.addOrUpdate = "update";
			var isItem = (isItem == "Y" ? true : false);
			iyRadio.setValue(isItem);
			inRadio.setValue(!isItem);
			iyRadio.disable();
			inRadio.disable();
			if (isItem) {
//				Ext.Msg.wait("正在加载数据,请等待...");
				Ext.Ajax.request({
							url : 'managebudget/findBudgetCount.action',
							params : {
								id : node.id,
								year:year.getValue()
							},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
								llDetail.setDisabled(false);
								contractDetail.setDisabled(false);
								wwDetail.setDisabled(false);
								zbDetail.setDisabled(false);
								var str = result.responseText;
								var o = eval("("+ str+ ")");
								itemName.setValue(o.itemName);
								itemUnit.setValue(o.unitName);
								budgetMake.setValue(o.wqValue);
								ysgs.setValue(o.ysFormula);
								sjgs.setValue(o.sjFormula);
								//**************************
								
								wqz.setValue(o.wqValue);
								ylz.setValue(o.fValue*o.wqValue);
								czz.setValue(o.cValue*o.wqValue);
								llFee.setValue(o.llFee);
								contractFee.setValue(o.contractFee);
								wwFee.setValue(o.wwFee);
								zbFee.setValue(o.zbFee);
								happenfee = parseFloat(o.llFee)+parseFloat(o.contractFee)+parseFloat(o.wwFee)+parseFloat(o.zbFee);
								happenfee = Math.round(happenfee*100)/100;
								happenFee.setValue(happenfee);
								lastfee = parseFloat(o.wqValue)-parseFloat(happenFee.getValue());
								lastfee = Math.round(lastfee*100)/100;
								lastFee.setValue(lastfee);
								
								
								itemCode = o.itemCode;
								itemId = o.itemId;
								formPositon2.show();
								
							},
							failure : function(result, request) {
								var str = result.responseText;
								var o = eval("("+ str+ ")");
								Ext.MessageBox.alert('错误', o.status);
							}
						});
			} else {
				
			}

		
	};
	
	var year = new Ext.form.TextField({
				fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				//name : 'model.year',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : new Date().getFullYear()
											.toString(),
									alwaysUseStartDate : true,
									dateFmt : "yyyy",		
									onpicked : function(v) {
										root.reload();
										this.blur();
									}

								});
					}
				},
				value : new Date().getFullYear()
			});
	
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
				autoScroll : true,
				root : root,
				layout : 'fit',
				autoHeight : false,
				animate : true,// 是否有动画效果
				enableDD : false,// 是否可以拖放节点
				useArrows : false, // 文件夹前显示的图标改变了不在是+号了
				border : false,
				rootVisible : true,
				containerScroll : true,// 是否含滚动条
				lines : true,
				loader : new Tree.TreeLoader({
							dataUrl : 'managebudget/findBudgetTree.action?pid=000',
							 listeners: {
					                "beforeload": function(treeloader, node) {
					                	var yeartime = year.getValue();
					                    treeloader.baseParams = {
					                    	pid:node.attributes.code,
					                        method: 'POST',
					                        year:yeartime
					                    };
					                }
					            }
						}),
				tbar : ["年度：",year]
			});
			
			
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'managebudget/findBudgetTree.action?pid='
						+ node.attributes.code;
			});
	var root = new Tree.AsyncTreeNode({
				text : '预算指标编码体系',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				code : '000'
			});
	tree.setRootNode(root);
	root.expand();
	tree.on('click', treeClick);

	var itemName = new Ext.form.TextField({
		fieldLabel:'指标名称',
		anchor : '90%',
		readOnly:true
	})
	
	var itemUnit = new Ext.form.TextField({
		fieldLabel:'计量单位',
		anchor : '80%',
		readOnly:true
	})
	
	var budgetMake = new Ext.form.TextField({
		fieldLabel:'预算编制',
		anchor : '80%',
		readOnly:true
	})
	
	var ysgs = new Ext.form.TextArea({
		fieldLabel:'预算公式',
		anchor : '90%',
		readOnly:true
	})
	var sjgs = new Ext.form.TextArea({
		fieldLabel:'实际公式',
		anchor : '90%',
		readOnly:true
	})
	
		// 有无数据
	var iyRadio = new Ext.form.Radio({
		boxLabel : '有',
		readOnly : true,
		inputValue : 'Y',
		checked : true,
		listeners : {
			'check' : function(radio, check) {
				if (check) {
//					Ext.get("systemName").dom.parentNode.parentNode.style.display = 'none';
//					// Ext.get("displayNo").dom.parentNode.parentNode.style.display
//					// = 'none';
//					formPositon2.show();
//					itemName.allowBlank = false;
				}
			}
		}
	});

	var inRadio = new Ext.form.Radio({
		boxLabel : '无',
		readOnly : true,
		inputValue : 'N',
		listeners : {
			'check' : function(radio, check) {
//				if (check) {
//					formPositon2.hide();
//					Ext.get("systemName").dom.parentNode.parentNode.style.display = '';
//					itemName.allowBlank = true;
//					itemName.focus();
//				}
			}
		}
	});

var isItem = {
		id : 'isItem',
		layout : 'column',
		isFormField : true,
		disabled:true,
		fieldLabel : '有无数据',
		style : 'cursor:hand',
		labelWidth : 100,
		border : false,
		items : [{
					columnWidth : .1,
					border : false,
					items : [iyRadio]
				}, {
					columnWidth : .1,
					border : false,
					items : [inRadio]
				}]
	};

	var formPositon1 = new Ext.form.FieldSet({
				border : true,
				title : '基本信息',
				autoHeight : true,
				layout : 'column',
						items : [{
							layout : "column",
							border : false,
							items : [{
										columnWidth : 1,
										layout : "form",
										border : false,
										items : [{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 1,
																layout : "form",
																border : false,
																items : [itemName]
															}]
												}, {
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [itemUnit]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [budgetMake]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 1,
																layout : "form",
																border : false,
																items : [isItem]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 1,
																layout : "form",
																border : false,
																items : [ysgs]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 1,
																layout : "form",
																border : false,
																items : [sjgs]
															}]
												}
												 ]
									}]
						}]
			});
			
	var wqz = new Ext.form.TextField({
		fieldLabel:'挖潜值',
		anchor : '80%',
		readOnly:true
	
	})
	
	var ylz = new Ext.form.TextField({
		fieldLabel:'一流值',
		anchor : '80%',
		readOnly:true
	})
	
	var czz = new Ext.form.TextField({
		fieldLabel:'创造值',
		anchor : '80%',
		readOnly:true
	})
	var happenFee = new Ext.form.TextField({
		fieldLabel:'已发生费用',
		anchor : '80%',
		readOnly:true
	})
	var lastFee = new Ext.form.TextField({
			fieldLabel:'剩余费用',
			anchor : '80%',
			readOnly:true
		})
	var llFee = new Ext.form.TextField({
			fieldLabel:'领料费用',
			anchor : '80%',
			readOnly:true
		})
		var contractFee = new Ext.form.TextField({
			fieldLabel:'合同费用',
			anchor : '80%',
			readOnly:true
		})
		var wwFee = new Ext.form.TextField({
			fieldLabel:'外委费用',
			anchor : '80%',
			readOnly:true
		})
		var zbFee = new Ext.form.TextField({
			fieldLabel:'费用直报',
			anchor : '80%',
			readOnly:true
		})
		
		
		var llDetail = new Ext.Button({
			text:'查看明细',
			disabled:true,
			handler:function(){
				var arg = new Object();
					arg.year = year.getValue();
					arg.itemCode = itemCode;
			window.showModalDialog('llDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
			}
		})
		var contractDetail = new Ext.Button({
			text:'查看明细',
			disabled:true,
			handler:function(){
				var arg = new Object();
					arg.itemCode = itemCode;
			window.showModalDialog('contractDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
			}
		})
		var wwDetail = new Ext.Button({
			text:'查看明细',
			disabled:true,
			handler:function(){
				var arg = new Object();
					arg.itemId = itemId;
			window.showModalDialog('wwDetailQuery.jsp', arg,
					'status:no;dialogWidth=900px;dialogHeight=450px');
			}
		})
		var zbDetail = new Ext.Button({
			text:'查看明细',
			disabled:true,
			handler:function(){
				var arg = new Object();
					arg.itemId = itemId;
			window.showModalDialog('zbDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
			
			}
		})
		
		var lb = new Ext.form.Label({
			text:'  其中:'
		})

	var formPositon2 = new Ext.form.FieldSet({
				title : '详细信息',
				border : true,
				height : '100%',
				layout : 'column',
				items : [{
							layout : "column",
							border : false,
							items : [{
										columnWidth : 1,
										layout : "form",
										border : false,
										items : [{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [wqz]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [ylz]
															}]
												},{
													layout : "column",
													border : false,
													items : [ {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [czz]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [happenFee]
															}, {
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [lastFee]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth :1,
																layout : "form",
																border : false,
																items : [lb]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [llFee]
															}, {
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [llDetail]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [contractFee]
															}, {
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [contractDetail]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [wwFee]
															}, {
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [wwDetail]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [zbFee]
															}, {
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [zbDetail]
															}]
												}
												 ]
									}]
						}]
			});

			
			
	
	Ext.form.FormPanel.prototype.addOrUpdate = "add";
	
	


	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 10px 0",
				labelAlign : 'right',
				labelWidth : 70,
				autoHeight : true,
				region : 'center',
				border : false,
				items : [formPositon2]
			});
	var notItemFormPanel = new Ext.FormPanel({
				bodyStyle : "padding:5px 10px 0",
				labelAlign : 'right',
				labelWidth : 70,
				autoHeight : true,
				border : false,
				items : [formPositon1]
			});

	
	var panel = new Ext.Panel({
				border : false,
				collapsible : true,
				title : '预算信息维护',
				items : [notItemFormPanel, form]
			});
	
	//debitCredit.disable();
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : "center",
							layout : 'fit',
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							items : [panel]
						}, {
							title : "预算指标",
							region : 'west',
							margins : '0 0 0 0',
							split : true,
							collapsible : true,
							titleCollapse : true,
							width : 200,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [tree]
						}]
			});
	root.select();

});
