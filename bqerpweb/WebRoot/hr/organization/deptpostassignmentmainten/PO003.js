Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';



Ext.override(Ext.grid.GridView, {
    initUI : function(grid){
//      Buffer processing of rows so that multiple changes to the GridView
//      only trigger this expensive operation when they are all done.
        this.on("processrows", this.doProcessRows, this, {buffer: 100});

        grid.on("headerclick", this.onHeaderClick, this);

        if(grid.trackMouseOver){
            grid.on("mouseover", this.onRowOver, this);
          grid.on("mouseout", this.onRowOut, this);
      }
    },

    processRows : function(startRow, skipStripe){
        this.fireEvent('processrows', startRow, skipStripe);
    },

//  private
    doProcessRows: function(startRow, skipStripe) {

//      Ensure the focus element doesn't get stranded outside the occupied area.
        var h = this.mainBody.dom.offsetHeight;
        if (parseInt(this.focusEl.dom.style.top) > h) {
            this.focusEl.dom.style.top = h + 'px';
        }

        if(this.ds.getCount() < 1){
            return;
        }
        skipStripe = skipStripe || !this.grid.stripeRows;
        startRow = startRow || 0;
        var rows = this.getRows();
        var cls = ' x-grid3-row-alt ';
        for(var i = startRow, len = rows.length; i < len; i++){
            var row = rows[i];
            row.rowIndex = i;
            if(!skipStripe){
                var isAlt = ((i+1) % 2 == 0);
                var hasAlt = (' '+row.className + ' ').indexOf(cls) != -1;
                if(isAlt == hasAlt){
                    continue;
                }
                if(isAlt){
                    row.className += " x-grid3-row-alt";
                }else{
                    row.className = row.className.replace("x-grid3-row-alt", "");
                }
            }
        }
    }
});



// ↓↓********************grid插件，用来显示一行checkbox***********************
Ext.grid.CheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
}; 

Ext.grid.CheckColumn.prototype ={
    init : function(grid){
        this.grid = grid;
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

    onMouseDown : function(e, t){
        if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1){
            e.stopEvent();
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);
            if(record.data.flag==='0'){
            	record.set("flag",'2');
            }
            if(record.data[this.dataIndex] == Constants.CHECKED_VALUE){
                record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
            }else{
                record.set(this.dataIndex, Constants.CHECKED_VALUE);
            }            
        }
    },

    renderer : function(v, p, record){
        p.css += ' x-grid3-check-col-td'; 
        return '<div class="x-grid3-check-col'+(v=='Y'?'-on':'')+' x-grid3-cc-'+this.id+'">&#160;</div>';
    }
};

 
Ext.onReady(function() {
	Ext.QuickTips.init();
	var oldRender = Ext.form.NumberField.prototype.onRender;
	Ext.override(Ext.form.NumberField,{
    onRender : function(ct, position){
        oldRender.call(this, ct, position);
        this.el.dom.maxLength = 16;
    }
})
	var ZERO="0";
	var ONE = "1";
	var TWO = '2';
	var THREE = "3";
	//定义树结构
	var deptId;
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'comm/getDeptsByPid.action'
	})
	// 部门树
	var deptTree = new Ext.tree.TreePanel({
		region : 'center',
		animate : true,
		height:700,
		allowDomMove:false,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		border : false,
		rootVisible : false,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		containerScroll : true,
		loader :treeLoader
	});  
	// 定义根节点
	var root = new Ext.tree.AsyncTreeNode({
		text : Constants.POWER_NAME,
		isRoot : true,
		id : '0'
		
	});
	deptTree.on("click", treeClick);
	deptTree.setRootNode(root);
	root.select();
	
	/**
	 * 点击树时处理
	 */
	function treeClick(node, e) {
		e.stopEvent();
//		if (node.isLeaf()) {
//			e.stopEvent();
//		}
			// 判断数据是否被修改
			if (judgeDataISChanged()) {
				Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_004,
						function(buttonobj) {
							if (buttonobj == "yes") {
								// 保存部门号
								deptId = node.id;
								// 导入未分配岗位数据
								UnAssignMentPostStore.load({
											params : {
												deptId : node.id
											}
										});
								// 导入已分配的岗位数据
								AssignMentPostStore.load({
											params : {
												deptId : node.id
											}
										});

							}
						});
			} else {
				deptId = node.id;
				// 导入未分配岗位数据
				UnAssignMentPostStore.load({
							params : {
								deptId : node.id
							}
						});
				// 导入已分配的岗位数据
				AssignMentPostStore.load({
							params : {
								deptId : node.id
							}
						});
			}
		currentID = node.id;
		currentName = node.text;
		node.toggle();
	}
	var UnAssignMentPostData = Ext.data.Record.create([
			// 岗位ID
			{
				name : 'stationId'
			},
			// 待分配的岗位
			{
				name : 'stationName'
			},{
				name : 'depstationcorrespondid'
			}]);
	// 待分配的岗位store
	var UnAssignMentPostStore = new Ext.data.JsonStore({
				url : 'hr/getUnAssignMentPostList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : UnAssignMentPostData
			});
	// 待分配的岗位store
	var UnAssignMentPostCopyStore = new Ext.data.JsonStore({
				url : 'hr/getUnAssignMentPostList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : UnAssignMentPostData
			});
		// 待分配的岗位store load完后的处理	
		UnAssignMentPostStore.on("load",function(){
			// 分配按钮的有效化判断
			if(UnAssignMentPostStore.getCount()>0){
				assignBtn.setDisabled(false);
			}else{
				assignBtn.setDisabled(true);
			}
			UnAssignMentPostCopyStore.removeAll();
			// 备份待分配的岗位store数据用于数据是否修改判定
			for (var i = 0; i < UnAssignMentPostStore.getCount(); i++) {
					UnAssignMentPostCopyStore
							.add(UnAssignPostStoreCopy(UnAssignMentPostStore
									.getAt(i)))
				}
			
		});	
	// add by liuyi 091231
	var fuzzyName = new Ext.form.TextField({
		id : 'fuzzy'
	})
	var tbar = new Ext.Toolbar({
		items : ['岗位名称：',fuzzyName,
			{text : '查询',
			iconCls : 'query',
			id : 'btuQuery',
			handler : function(){
				if(deptId)
				{
							UnAssignMentPostStore.filter('stationName',fuzzyName.getValue(),true)
				}
			}}]
	})
	// 待分配的岗位grid
	var UnAssignMentPostGrid = new Ext.grid.EditorGridPanel({
				height:262,
				width :605,
				tbar : tbar,
				title :"待分配岗位",
				sm : new Ext.grid.RowSelectionModel({singleSelect : false}),
				enableColumnMove : false,
				store : UnAssignMentPostStore,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}),
						// 岗位名称
						{
							header : "岗位名称",
							width : 100,
							sortable : true,
							dataIndex : 'stationName'
						}],
				 onMouseDown : function(e, t) {
					if (e.button === 0) {													
						e.stopEvent();
						var row = e.getTarget('.x-grid3-row');
						if (row) {
							var rowIndex = row.rowIndex;
							var isSelected = this.selModel.isSelected(rowIndex);
                            if (isSelected) {
                                this.selModel.deselectRow(rowIndex);
                            } else if (!isSelected || this.getCount() > 1) {
                                this.selModel.selectRow(rowIndex, true);
                                var view = this.selModel.grid.getView();
                                view.focusRow(rowIndex);
                            }
						}
					}
		},		
				autoSizeColumns : true

			});
	// 分配按钮
	var assignBtn = new Ext.Button({
		text : "分配",
		iconCls : Constants.CLS_ASSIGN,
		disabled:true,
		handler : function() {
			AssignStation();
						}
	});
	// 撤销按钮
	var cancelBtn = new Ext.Button({
		text : "撤销",
		iconCls : Constants.CLS_DESTROY,
		disabled:true,
		handler : function() {
			CancelStation();
						}
	});
	// 保存按钮
	var saveBtn = new Ext.Button({
		text : Constants.BTN_SAVE,
		iconCls : Constants.CLS_SAVE,
		disabled:true,
		handler : function() {
					SaveStation();
				}
	});
	var centerFieldSet = new Ext.form.FieldSet({
				layout :"column",
				style : "padding-left:2px;padding-top:12px;margin-bottom:3px",
				height:43,
				border : false,
				labelAlign : 'right',
				anchor : '100%',
				items : [{
					border : false,
					width:62,
					layout : 'form',
					items : [assignBtn]
				}, {
					width:62,
					border : false,
					layout : 'form',
					items : [cancelBtn]
				},{
					border : false,
					width :62,
					layout : 'form',
					items : [saveBtn]
				}]

			});
		
			
	var AssignMentPostData = Ext.data.Record.create([
			// 部门岗位ID
			{
				name : 'depstationcorrespondid'
			},
			// 部门ID
			{
				name : 'deptId'
			},
			// 标准人数
			{
				name : 'standardPersonNum'
			},
			// 上次修改日期
			{
				name : 'lastModifiedDate'
			},
			// 是否领导岗位
			{
				name : 'isLead'
			},
			// 用于判断这条记录是否删除、新规、修改
			{
				name : 'flag'
			},
			// 岗位ID
			{
				name : 'stationId'
			},
			// 岗位名称
			{
				name : 'stationName'
			},
			// 岗位编码
			{
				name : 'stationCode'
			}]);
	// 已分配的岗位store
	var AssignMentPostStore = new Ext.data.JsonStore({
				url : 'hr/getAssignMentPostList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : AssignMentPostData
			});
		// 已分配的岗位store
	var AssignMentPostCopyStore = new Ext.data.JsonStore({
				url : 'hr/getAssignMentPostList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : AssignMentPostData
			});		
	// load完后的处理		
	AssignMentPostStore.on("load",function(){
			// 取消按钮的有效化判定
			if(AssignMentPostStore.getCount()>0){
				cancelBtn.setDisabled(false);
			}else{
				cancelBtn.setDisabled(true);
			}
			saveBtn.setDisabled(false);
			AssignMentPostCopyStore.removeAll();
			// 已分配岗位的数据备份
			for (var i = 0; i < AssignMentPostStore.getCount(); i++) {
					AssignMentPostCopyStore
							.add(AssignPostStoreCopy(AssignMentPostStore
									.getAt(i)))
				}
	});	
	 
	 var isLead = new Ext.grid.CheckColumn({
            header : "是否领导岗位",
            dataIndex:"isLead",
            width: 150
        });	 

	// 已分配的岗位grid
	var AssignMentPostGrid = new Ext.grid.EditorGridPanel({
				autoScroll : true,
				height:250,
				width :605,
				title :"已分配岗位",
				enableColumnMove : false,
				clicksToEdit :1,
				sm : new Ext.grid.RowSelectionModel({singleSelect : false}),
				store : AssignMentPostStore,
				plugins:isLead,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}),

						{
							header : "岗位名称",
							width : 100,
							sortable : true,
							dataIndex : 'stationName'
						},{
							header : '标准人数<font color="red">*</font>',
							width : 75,
							sortable : true,
							align :'right',
							renderer:renderNumber,
							editor : new Ext.form.NumberField({
								allowNegative:false,
								maxLength :10,
								allowDecimals:false
							}),
							dataIndex : 'standardPersonNum'
						},isLead],
				 onMouseDown : function(e, t) {
					if (e.button === 0) {													
						e.stopEvent();
						var row = e.getTarget('.x-grid3-row');
						if (row) {
							var rowIndex = row.rowIndex;
							var isSelected = this.selModel.isSelected(rowIndex);
                            if (isSelected) {
                                this.selModel.deselectRow(rowIndex);
                            } else if (!isSelected || this.getCount() > 1) {
                                this.selModel.selectRow(rowIndex, true);
                                var view = this.selModel.grid.getView();
                                view.focusRow(rowIndex);
                            }
						}
					}
		},		
				autoSizeColumns : true
			}); 
	AssignMentPostGrid.on('afteredit',function(obj){
		var record = obj.record;
		// 将未修改的记录的标记位设成"2"，表示这条记录要更新
		if(record.data.flag===ZERO){
			record.set("flag",TWO);
		}
		AssignMentPostGrid.getView().refresh();
	});

	var leftPanel = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				width : '20%',
				autoScroll : true,
				border:false,
				containerScroll : true,
				collapsible : true,
				split : false,
				items : [deptTree]
			});
	// 右边的panel
	var rightPanel = new Ext.Panel({
				layout : 'form',
//				width :580,
//				height:543,
//				autoScroll:true,
				border:false,
				items : [UnAssignMentPostGrid,centerFieldSet,AssignMentPostGrid]
			});
	// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll:true,
				layout : "border",
				items : [leftPanel, {
					region :'center',
					layout : 'form',
					width :580,
					height:650,
					autoScroll:true,
					items : [rightPanel]
				}]
			})
	/**
	 * 分配岗位处理
	 */		
	function AssignStation(){
		// 是否选定一条记录
		if (UnAssignMentPostGrid.selModel.hasSelection()) {
			var records = UnAssignMentPostGrid.selModel.getSelections();
			var record;
			var recordArray = [];
			var isExist;
			for(var i =0;i<records.length;i++){
				AssignPostStoreAdd(records[i])
			}
			AssignMentPostGrid.getView().refresh();
			for(var i =0;i<records.length;i++){
				record = records[i];
				UnAssignMentPostStore.remove(record);
				UnAssignMentPostGrid.getView().refresh();
			}
			// 初始化按钮的状态
			IntialButtonStatus();
		} else {
			Ext.Msg.alert(Constants.REMIND, Constants.PO003_I_001);
		}
	}
	/**
	 * 撤销岗位处理
	 */
	function CancelStation(){
		// 是否有记录被选定
		if (AssignMentPostGrid.selModel.hasSelection()) {
			var records = AssignMentPostGrid.selModel.getSelections();
			var record;
			for(var i =0;i<records.length;i++){
				record = records[i];
				UnAssignPostStoreAdd(record);
			}
			UnAssignMentPostGrid.getView().refresh();
			for(var i =0;i<records.length;i++){
				AssignMentPostStore.remove(records[i]);
			}
			AssignMentPostGrid.getView().refresh();	
			IntialButtonStatus();
		} else {
			Ext.Msg.alert(Constants.REMIND, Constants.PO003_I_002);
		}
		
	}
	/**
	 * 已分配岗位增加一条记录
	 */
	function AssignPostStoreAdd(record){
			// 新规一条记录，设定默认值
			var newRecord = new AssignMentPostData({
					depstationcorrespondid : record.data.depstationcorrespondid,
					deptId : deptId,
					standardPersonNum : "",
					lastModifiedDate : "",
					isLead : "N",
					flag : ONE,
					stationId : record.data.stationId,
					stationName : record.data.stationName,
					stationCode : ""
				});
			AssignMentPostStore.insert(AssignMentPostStore.getCount(),newRecord);	
	}
	/**
	 * 复制已分配岗位的Store
	 */
	function AssignPostStoreCopy(record){
			// 复制一条记录，备份用于判断数据是否被修改
			var newRecord = new AssignMentPostData({
					depstationcorrespondid : record.data.depstationcorrespondid,
					deptId : record.data.deptId,
					standardPersonNum : record.data.standardPersonNum,
					lastModifiedDate : record.data.lastModifiedDate,
					isLead : record.data.isLead,
					flag : record.data.flag,
					stationId : record.data.stationId,
					stationName : record.data.stationName,
					stationCode : record.data.stationCode
				});
			return newRecord;
	}
	
	/**
	 * 未分配岗位增加一条记录
	 */
	function UnAssignPostStoreAdd(record){
			// 新规一条记录，设定默认值
			var newRecord = new UnAssignMentPostData({
					depstationcorrespondid:record.data.depstationcorrespondid,
					stationId : record.data.stationId,
					stationName : record.data.stationName
				});
			UnAssignMentPostStore.add(newRecord);	
	}
	
	/**
	 * 复制未分配岗位的Store
	 */
	function UnAssignPostStoreCopy(record){
			// 复制一条记录，备份用于判断数据是否被修改
			var newRecord = new UnAssignMentPostData({
					depstationcorrespondid:record.data.depstationcorrespondid,
					stationId : record.data.stationId,
					stationName : record.data.stationName
				});
			return newRecord;	
	}
	/**
	 * 初始化button使用状态
	 */
	function IntialButtonStatus(){
		if(UnAssignMentPostStore.getCount()>0){
				assignBtn.setDisabled(false);
			}else{
				assignBtn.setDisabled(true);
			}
		if(AssignMentPostStore.getCount()>0){
				cancelBtn.setDisabled(false);
			}else{
				cancelBtn.setDisabled(true);
			}
		saveBtn.setDisabled(false);	
	}
	/**
	 * 判断数据是否被修改
	 */
	function judgeDataISChanged(){
		var unAssignPostStoreCount = UnAssignMentPostStore.getCount();
		var unAssignPostCopyStoreCount = UnAssignMentPostCopyStore.getCount();
		var assignPostStoreCount = AssignMentPostStore.getCount();
		var assignPostStoreCopyCount =AssignMentPostCopyStore.getCount();
		// 初始化状态的判断
		if(unAssignPostStoreCount==0&&assignPostStoreCount==0){
			return false;
		}
		// 未分配的岗位个数是否修改判断
		if(unAssignPostCopyStoreCount!= unAssignPostCopyStoreCount){
			return true;
		}else{
			var record;
			var copyRecord;
			var isExist;
			// 未分配的岗位个数相等时，判断这条记录是否存在在备份的数据中 
			for(var i = 0;i<unAssignPostStoreCount;i++){
				record = UnAssignMentPostStore.getAt(i);
				isExist = false;
				for(var j =0;j<unAssignPostCopyStoreCount;j++){
					copyRecord = UnAssignMentPostCopyStore.getAt(j);
					if(record.data.stationId==copyRecord.data.stationId){
						isExist = true;
						break;
					}
				}
				if(!isExist){
					return true;
				}
			}
		}
		// 已分配的岗位个数是否修改判断
		if(assignPostStoreCount!=assignPostStoreCopyCount){
			return true;
		}else{
			var record;
			var copyRecord;
			var isExist;
			// 已分配的岗位个数相等时，判断这条记录是否存在在备份的数据中
			for(var i = 0;i<assignPostStoreCount;i++){
				record = AssignMentPostStore.getAt(i);
				isExist = false;
				for(var j =0;j<assignPostStoreCopyCount;j++){
					copyRecord = AssignMentPostCopyStore.getAt(j);
					if(record.data.depstationcorrespondid==copyRecord.data.depstationcorrespondid){
						// 这条记录存在的话，判断各个字段值是否修改
						if(record.data.standardPersonNum!=copyRecord.data.standardPersonNum){
							return true;
						}
						if(record.data.isLead!=copyRecord.data.isLead){
							return true;
						}
						isExist =true;
						break;
					}
				}
				if(!isExist){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 已分配岗位的过滤
	 */
	function AssignPostFilter(){
		AssignMentPostStore.filterBy(function(record) {
						if (record.data.flag === THREE) {
							return false;
						} else {
							return true;
						}
					})
		AssignMentPostGrid.getView().refresh();			
	}
	/**
	 * 数据渲染
	 */
	function renderNumber(v, argDecimal) {
		if(v===0)return "0";	
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 0;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;

			return v ;
		} else
			return "";
	}
	/**
	 * 保存处理
	 * 
	 */
	function SaveStation() {
		var record;
		var dataList = [];
		for (var i = 0; i < AssignMentPostStore.getCount(); i++) {
			record = AssignMentPostStore.getAt(i);
			if (record.data.standardPersonNum === "") {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_002, '标准人数'));
				return;
			}
		}
		Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == "yes") {
						for (var i = 0; i < AssignMentPostStore.getCount(); i++) {
							record = AssignMentPostStore.getAt(i);
							if (parseFloat(record.get("standardPersonNum")) <= 0) {
								Ext.Msg.alert(Constants.ERROR, String.format(
												Constants.COM_E_019, '标准人数'));
								return;
							}
						}
						var copyRecord;
						var newRecord;
						var isExist;
						for(var i = 0; i < AssignMentPostStore.getCount(); i++){
							record = AssignMentPostStore.getAt(i);
							isExist = false;
							for(var j = 0;j<AssignMentPostCopyStore.getCount();j++){
								copyRecord = AssignMentPostCopyStore.getAt(j);
								if(record.data.stationId===copyRecord.data.stationId &&
								record.data.depstationcorrespondid===copyRecord.data.depstationcorrespondid){
									newRecord = AssignPostStoreCopy(copyRecord);
									newRecord.set("flag","2");
									newRecord.set("standardPersonNum",record.data.standardPersonNum);
									newRecord.set("isLead",record.data.isLead);
									dataList.push(newRecord.data);
									isExist =true;
									break;
								}
							}
							if(!isExist){
								record.set("flag","1");
								dataList.push(record.data)
							}
						}
				for (var j = 0; j < AssignMentPostCopyStore.getCount(); j++) {
					copyRecord = AssignMentPostCopyStore.getAt(j);
					isExist = false;
					for (var i = 0; i < AssignMentPostStore.getCount(); i++) {
						record = AssignMentPostStore.getAt(i);
						if (copyRecord.data.stationId === record.data.stationId) {
							isExist = true;
							break;
						}
					}
					if(!isExist){
						copyRecord.set("flag","3");
						dataList.push(copyRecord.data);
					}
				}
						
						
											
						// 发送请求
						Ext.Ajax.request({
									method : 'POST',
									url : 'hr/saveAssignmentStation.action',
									params : {
										data : Ext.util.JSON.encode(dataList)
									},
									success : function(action) {
										var result = eval('('
												+ action.responseText + ')');
										if (result.msg == Constants.DATA_USING) {
											// 排他处理
											Ext.Msg.alert(Constants.ERROR,
													Constants.COM_E_015);
											return;
										}
										if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(
													MessageConstants.ERROR,
													Constants.COM_E_014);
											return;
										}
										if (result.msg == Constants.DATE_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													Constants.COM_E_023);
											return;
										}
										if (result.msg == Constants.CODE_REPEAT) {
											Ext.Msg.alert(Constants.ERROR,
													Constants.PO003_E_001);
											
													// 导入未分配岗位数据
													UnAssignMentPostStore.load(
															{
																params : {
																	deptId : deptId
																}
															});
													// 导入已分配的岗位数据
													AssignMentPostStore.load({
																params : {
																	deptId : deptId
																}
															});
											return;
										}
										
										
										// 保存成功
										Ext.Msg.alert(Constants.REMIND,
												Constants.COM_I_004,
												function() {});
										
													// 导入未分配岗位数据
													UnAssignMentPostStore.load(
															{
																params : {
																	deptId : deptId
																}
															});
													// 导入已分配的岗位数据
													AssignMentPostStore.load({
																params : {
																	deptId : deptId
																}
															});
														
									}
								});
					}
				});
}
});
	
 