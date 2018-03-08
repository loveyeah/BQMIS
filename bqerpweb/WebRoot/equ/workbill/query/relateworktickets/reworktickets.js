Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
			function getParameter(psName) {
				var url = self.location;
				var result = "";
				var str = self.location.search.substring(0);
				if (str.indexOf(psName) != -1
						&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
								.substr(str.indexOf(psName) - 1, 1) == "&")) {
					if (str.substring(str.indexOf(psName), str.length)
							.indexOf("&") != -1) {
						var Test = str.substring(str.indexOf(psName),
								str.length);
						result = Test.substr(Test.indexOf(psName), Test
								.indexOf("&")
								- Test.indexOf(psName));
						result = result.substring(result.indexOf("=") + 1,
								result.length);
					} else {
						result = str.substring(str.indexOf(psName), str.length);
						result = result.substring(result.indexOf("=") + 1,
								result.length);
					}
				}
				return result;
			};

			//var woCode1 = getParameter("woCode");
			var workorderStatus = getParameter("workorderStatus");
			// grid中的数据
			var runGridList = new Ext.data.Record.create([ {
				name : 'model.workticketNo'
			}, {
				name : 'model.workticketStausId'
			}, {
				name : 'statusName'
			}, {
				name : 'workticketContentAndLocationName'
			}, {
				name : 'model.sourceId'
			}, {
				name : 'sourceName'
			}, {
				name : 'model.chargeBy'
			}, {
				name : 'chargeByName'
			}, {
				name : 'planStartDate'
			}, {
				name : 'planEndDate'
			}, {
				name : 'model.members'
			}, {
				name : 'model.chargeDept'
			}, {
				name : 'deptName'
			}, {
				name : 'model.workFlowNo'
			}])

			// grid中的store
			var ds = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : 'workbill/relateWorkticketAllList.action'
				}),
				reader : new Ext.data.JsonReader( {
					root : 'root',
					totalProperty : 'totalCount'
				}, runGridList)
			})
			ds.load({
				params : {
					woCode : getParameter("woCode")
					//woCode : 11
				}
			});
			// 生成序列号
			var rn = new Ext.grid.RowNumberer( {
				header : "行号",
				selectMode : 'true',
				width : 35
			});
			// 选择列
			var smCSM = new Ext.grid.CheckboxSelectionModel( {
				header : "选择",
				id : "check",
				width : 35,
				singleSelect : true//,
				//listeners : {
			    //rowselect : function(sm, row, rec) {
					//if (rec.get("messageStatus") == 1) {
						//Ext.get("sendmessage").dom.disabled = true
						//Ext.get("delmessage").dom.disabled = true
					//} else {
						//Ext.get("sendmessage").dom.disabled = false
						//Ext.get("delmessage").dom.disabled = false
					//}
			 //}
		    // }
			});
			var cm = new Ext.grid.ColumnModel([smCSM,rn, {
				header : '工作票编号',
				width : 60,
				align : 'center',
				sortable : true,
				dataIndex : 'model.workticketNo'
			}, {
				header : "",
				hidden : true,
				align : 'center',
				dataIndex : 'model.workticketStausId'
			}, {
				header : "状态",
				width : 40,
				region : "center",
				sortable : true,
				align : 'center',
				dataIndex : 'statusName'
			}, {
				header : "工作内容及地点",
				width : 150,
				sortable : true,
				align : 'center',
				dataIndex : 'workticketContentAndLocationName'
			}, {
				header : "",
				hidden : true,
				dataIndex : 'model.sourceId'
			}, {
				header : '来源',
				width : 50,
				sortable : true,
				align : 'center',
				dataIndex : 'sourceName'
			}, {
				header : "",
				hidden : true,
				align : 'center',
				dataIndex : 'model.chargeBy'
			}, {
				header : '工作负责人',
				width : 40,
				sortable : true,
				align : 'center',
				dataIndex : 'chargeByName'
			}, {
				header : '计划开始时间',
				width : 60,
				sortable : true,
				align : 'center',
				dataIndex : 'planStartDate'
			}, {
				header : '计划结束时间',
				width : 60,
				sortable : true,
				align : 'center',
				dataIndex : 'planEndDate'
			}, {
				header : '工作人员',
				width : 70,
				sortable : true,
				align : 'center',
				dataIndex : 'model.members'
			}, {
				header : "",
				hidden : true,
				align : 'center',
				dataIndex : 'model.chargeDept'
			}, {
				header : '所属班组',
				width : 70,
				sortable : true,
				align : 'center',
				dataIndex : 'deptName'
			}, {
				header : "",
				hidden : true,
				dataIndex : 'model.workFlowNo'
			}]);
			var etbar = new Ext.Toolbar( {
				items : [ {
							id : 'btnPriview',
							iconCls : 'preview',
							text : "预览", 
							width : 80,
							handler : btnPreview
						},'-',{
							// 刷新按钮
							text : '刷新',
							iconCls : 'reflesh',
							handler : function() {
								ds.reload();
							}
						},"<font color='red'>双击某行可实现预览该工作票基本信息</font>"]
			});
			
			var storeRepairSpecail = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'workticket/getDetailRepairSpecialityType.action'
								}),
						reader : new Ext.data.JsonReader({
									root : 'list'
								}, [{
											name : 'specialityCode',
											mapping : 'specialityCode'
										}, {
											name : 'specialityName',
											mapping : 'specialityName'
										}])
			});
			storeRepairSpecail.load();
			var cbxRepairSpecail = new Ext.form.ComboBox({
						id : 'repairSpecailCode',
						fieldLabel : "检修专业<font color='red'>*</font>",
						store : storeRepairSpecail,
						displayField : "specialityName",
						valueField : "specialityCode",
						hiddenName : 'workticketBaseInfo.repairSpecailCode',
						mode : 'local',
						triggerAction : 'all',
						value : '',
						readOnly : true,
						anchor : "85%"
			});
			// 工作票种类
			var storeWorkticketType = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'workticket/getDetailWorkticketTypeName.action'
								}),
						reader : new Ext.data.JsonReader({
									root : 'list'
								}, [{
											name : 'workticketTypeCode',
											mapping : 'workticketTypeCode'
										}, {
											name : 'workticketTypeName',
											mapping : 'workticketTypeName'
										}])
					});
			storeWorkticketType.load();
			var cbxWorkticketType = new Ext.form.ComboBox({
						id : 'workticketTypeCode',
						fieldLabel : "工作票种类<font color='red'>*</font>",
						store : storeWorkticketType,
						displayField : "workticketTypeName",
						valueField : "workticketTypeCode",
						hiddenName : 'workticketBaseInfo.workticketTypeCode',
						mode : 'local',
						triggerAction : 'all',
						value : '',
						readOnly : true,
						anchor : "85%"
					});
			//工作票来源
		    var storeWorkticketSource = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailWorkticketSource.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'sourceId',
									mapping : 'sourceId'
								}, {
									name : 'sourceName',
									mapping : 'sourceName'
								}])
			});
			storeWorkticketSource.load();
			var cbxWorkticketSource = new Ext.form.ComboBox({
						id : 'sourceId',
						fieldLabel : "工作票来源<font color='red'>*</font>",
						store : storeWorkticketSource,
						displayField : "sourceName",
						valueField : "sourceId",
						hiddenName : 'workticketBaseInfo.sourceId',
						mode : 'local',
						triggerAction : 'all',
						value : '',
						readOnly : true,
						anchor : "85%"
				
			})
			// 工作负责人
			var triggerChargeBy = new Ext.form.TriggerField({
						id : 'chargebyname',
						fieldLabel : "工作负责人<font color='red'>*</font>",
						onTriggerClick : chargeBySelect,
						value : '',
						readOnly : true,
						anchor : "85%"
		    })
			// 工作负责人选择
			function chargeBySelect() {
				if (cbxWorkticketType.value == "") {
					Ext.Msg.alert("提示", "请先选择工作票种类");
				} else {
					triggerChargeBy.blur();
					var args = new Object();
					// 工作票类型名称
					args.workticketTypeName = cbxWorkticketType.lastSelectionText;
					var object = window
							.showModalDialog(
									'../../../run/workticket/business/register/baseInfo/workticketChargeBySelect.jsp',
									args,
									'dialogWidth=550px;dialogHeight=350px;center=yes;help=no;resizable=no;status=no;');
					if (typeof(object) == 'object') {
						Ext.get("workticketBaseInfo.chargeBy").dom.value = object.empCode;
						Ext.get("chargebyname").dom.value = object.chsName
								? object.chsName
								: '';
		
						Ext.get('workticketBaseInfo.chargeDept').dom.value = object.deptCode;
						Ext.get('deptname').dom.value = object.deptName
								? object.deptName
								: '';
					}
				}
			}
			// 所属部门
			var txtChargeDept = new Ext.form.TextField({
						id : 'deptname',
						fieldLabel : "所属部门",
						value : '',
						readOnly : true,
						disabled : true,
						anchor : "85%"
			})
			// 接收专业
			var storeReceiveSpecail = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
					url : 'workticket/getDetailReceiveSpecialityType.action'
				}),
				reader : new Ext.data.JsonReader({
					id : "unit"
				}, [{
					name : 'specialityCode'
				}, {
					name : 'specialityName'
				}])
					});
			storeReceiveSpecail.load();
			var cbxReceiveSpecail = new Ext.form.ComboBox({
						id : 'permissionDept',
						fieldLabel : "接收专业<font color='red'>*</font>",
						store : storeReceiveSpecail,
						displayField : "specialityName",
						valueField : "specialityCode",
						hiddenName : 'workticketBaseInfo.permissionDept',
						mode : 'local',
						triggerAction : 'all',
						value : '',
						readOnly : true,
						anchor : "85%"
					})
			// 所属机组或系统
			var storeChargeBySystem = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'workticket/getDetailEquList.action'
								}),
						reader : new Ext.data.JsonReader({
									root : 'list'
								}, [{
											name : 'blockCode',
											mapping : 'blockCode'
										}, {
											name : 'blockName',
											mapping : 'blockName'
										}])
					});
			storeChargeBySystem.load();
			var cbxChargeBySystem = new Ext.form.ComboBox({
						id : 'equAttributeCode',
						fieldLabel : "所属机组或系统<font color='red'>*</font>",
						store : storeChargeBySystem,
						displayField : "blockName",
						valueField : "blockCode",
						hiddenName : 'workticketBaseInfo.equAttributeCode',
						mode : 'local',
						triggerAction : 'all',
						value : '',
						readOnly : true,
						anchor : "85%",
						listeners : {
							select : function() {
								locationName.setValue(" ");
							}
						}
					})
			// 工作位置/地点
			var locationName = new Ext.form.TriggerField({
						fieldLabel : '工作地点<font color="red">*</font>',
						id : "locationName",
						name : "workticketBaseInfo.locationName",
						allowBlank : false,
						onTriggerClick : locationSelect,
						anchor : "85%"
					});
		
			/** 区域选择处理 */
			function locationSelect() {
				if (cbxChargeBySystem.getValue() == "") {
					Ext.Msg.alert("提示", "请选择所属机组或系统");
					return;
				}
				var url = "../../../run/workticket/business/register/baseInfo/selectLocation.jsp?op=many&blockCode="
						+ cbxChargeBySystem.getValue()+"&workticketTypeCode="+Ext.get("workticketBaseInfo.workticketTypeCode").dom.value;
				var location = window.showModalDialog(url, window,
						'dialogWidth=400px;dialogHeight=300px;status=no');
				if (typeof(location) != "undefined") {
					// 设置设备名
					locationName.setValue(location.name);
				}
			} 
			//form表单
			var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				height : 400,
				items:[cbxRepairSpecail,cbxWorkticketType,cbxWorkticketSource,triggerChargeBy,{
											id : 'chargeBy',
											xtype : 'hidden',
											id:'workticketBaseInfo.chargeBy',
											name : 'workticketBaseInfo.chargeBy'
										},{
											id : 'chargeDept',
											xtype : 'hidden',
											name : 'workticketBaseInfo.chargeDept'
										},txtChargeDept,cbxReceiveSpecail,cbxChargeBySystem,locationName]
			});
			//窗口
			var win = new Ext.Window({
				width : 400,
				height : 285,
				title : "工作票填写",
				buttonAlign : "center",
				//resizable : false,
				modal:true,
				items : [myaddpanel],
				buttons : [{
					text : "保存",
					iconCls: "save",
					handler : function() {
						createWorkticket();
					}
					}, {
					text : "取消",
		            iconCls:"cancer",
					handler : function() {
						win.hide();
					}
					}],
				layout : 'fit',
				closeAction : 'hide'
			});
			//保存生成的一条工作票记录
			function createWorkticket(){
				if(checkInput()){
					var url = "workbill/createWorkticket.action?workticketTypeCode="
					           +cbxWorkticketType.getValue()+"&repairSpecailCode="+cbxRepairSpecail.getValue()
					           +"&equAttributeCode="+cbxChargeBySystem.getValue()+"&sourceId="+cbxWorkticketSource.getValue()
					           +"&chargeBy="+Ext.get("workticketBaseInfo.chargeBy").dom.value+"&chargeDept="
					           +Ext.get('workticketBaseInfo.chargeDept').dom.value+"&permissionDept="+cbxReceiveSpecail.getValue()
					           +"&locationName="+Ext.get('locationName').dom.value;
		            var conn = Ext.lib.Ajax.getConnectionObject().conn;
		            conn.open("POST", url, false);
		            conn.send(null);
		            result = eval('(' + conn.responseText + ')');
		            var workticketsNo = result.workticketNo;
		            var ifCreateRelation = result.success;
		            if(ifCreateRelation){
		            	Ext.Ajax.request({
										url : 'workbill/addWorktickets.action',
										method : 'post',
										params : {
											       workticketsNo : workticketsNo,
											       woCode : getParameter("woCode")
											      },
										success :function(result, request){ 
											     ds.reload();
											     Ext.Msg.alert("提示","添加成功!");
											     win.hide();
												
									   },
										failure : function(result,request){
											Ext.Msg.alert("提示","操作失败");
										}
						}); 
		            }
		            
				}
			}
			//批量删除工作票时判断每条工作票状态是否可删
			function ifdele(){
				var sme = runGrid.getSelectionModel().getSelections();
				//alert(sme[0].get("model.workticketStausId"));
				for(var i=0; i<sme.length; i++){
					if(sme[i].get("model.workticketStausId") != 1){
					     return true;
					  }
				}
					return false;
			}
			//工作票填写函数
			function addRecord(){
				// 重置表单
				myaddpanel.getForm().reset();
				// 显示增加窗口
		        win.show();
		        // 自动生成字段
				//Ext.get("workticketNo").dom.value=Constants.AUTO_CREATE;
			}
			// 运行执行的Grid主体
			var runGrid = new Ext.grid.GridPanel( {
				ds : ds,
				sm : smCSM,
				region : 'center',
				layout : 'fit',
				viewConfig : {
					forceFit : true
				},
				cm : cm,
				tbar : etbar
			});
			//判断工作票填写窗口选项是否输入
			function checkInput() {
				// 工作票种类
				var ticketType = Ext.get("workticketTypeCode").dom.value;
				// 工作票来源
		        var ticketSource = Ext.get("sourceId").dom.value;
				// 检修专业
				var repairSpecail = Ext.get("repairSpecailCode").dom.value;
				// 工作负责人
		        var chargeBy = Ext.get("chargebyname").dom.value;
		        // 接收专业
		        var permissionDept = Ext.get("permissionDept").dom.value;
		        // 所属机组或系统
		        var equAttribute = Ext.get("equAttributeCode").dom.value; 
				// 弹出信息
				var strMsg = "你还需要填入下列项目,然后才能保存:";
				var msg = "";
				if (ticketType == '') {
					msg += "{工作票种类},";
				}
				if (repairSpecail == '') {
					msg += "{检修专业},";
				}
				if (ticketSource == '') {
			        msg += "{工作票来源},";
		        }
		        if (chargeBy == '') {
			        msg += "{工作负责人},";
		        }
		        if (permissionDept == '') {
			        msg += "{接收专业},";
		        }
		        if (locationName.getValue() == "") {
			        msg += "{工作地点},";
		        }
		        if (equAttribute == '') {
			        msg += "{所属机组或系统}";
		        }
				if (msg != '') {
					Ext.Msg.alert("提示", strMsg + msg);
					return false;
				}
				return true;
		   }
			//设置双击事件
			runGrid.on("dblclick", function() {
		        Ext.get("btnPriview").dom.click();
	        });
			//预览
	       function btnPreview(){
			    var sm = runGrid.getSelectionModel();
		        var selected = sm.getSelections();
		        if(selected.length == 0 || selected.length<0){
		        	Ext.Msg.alert("提示", "请选择工作票！");
		        }else{
		        var workticketNo = selected[0].get("model.workticketNo");
		        var url = "workticketBaseInfo.jsp?workticketNo=" + workticketNo+"&woCode="+getParameter("woCode");
					var fo = window.showModalDialog(url, null,
							'dialogWidth=800px;dialogHeight=700px;status=no');
	       }}
			// runGridStore.load();
			// 设定布局器及面板
			new Ext.Viewport( {
				enableTabScroll : true,
				layout : "fit",
				items : [runGrid]
			})
			// 设置按钮
			if(workorderStatus != 0){
				Ext.get("btnWrite").dom.disabled=true;
				Ext. get("btnChoose").dom.disabled=true;
				Ext.get("cencerBtn").dom.disabled=true;
				Ext.get("btnDelete").dom.disabled=true;
         }
});
