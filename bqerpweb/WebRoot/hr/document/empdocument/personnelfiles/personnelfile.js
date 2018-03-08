Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	var empIdUsing = null;
 // 全局变量
    var empParam = null;
Ext.onReady(function() {
	
	Ext.form.TextField.prototype.width = 180;
    Ext.QuickTips.init();
    function getSex(v)
	{
		if(v=="M")
		{
			return "男";
		}else  if(v=="W")
		{
			return "女";
		}
	}
	
	
	function getisWed(v)
	{
			 if(v=="0")
    {
     return "已婚";
    }
     else if(v=="1")
     {
     return "未婚";
     }else if(v=="2")
     {
     	return "离异";
     }else if(v=="3")
     {
      return "丧偶";
     }
		
	}
  function getisMain(v)
  { if(v=="Y")
  {
     return "是";
  }
     else if(v=="N")
     {
     return "否";
     }else if(v=="")
     {
     	return "";
     }
  }
  function getHouse(v)
  { 
    if(v=="1")
    {
     return "农业";
    }
     else if(v=="2")
     {
     return "非农";
     }else if(v=="")
     {
     	return "";
     }
  }
 function  getCompent(v)
  {
  		
  		 if(v=="1")
    {
     return "工人";
    }
     else if(v=="2")
     {
     return "农民";
     }else if(v=="3")
     {
     	return "知识分子";
     }else if(v=="4")
     {
      return "其它";
     }else if(v=="")
     {
     	return "";
     }
  }
  
  	function getage(v) {
		var value = v;
		if (!value) {
			return '';
		}
		if (value instanceof Date) {
			value = value.dateFormat('Y-m');
		}
		var now = new Date();
		var age = now.dateFormat('Y') - Number(value.substring(0, 4))+1;
		return age;
	}
	
  var isPhoto="";
	function getPhoto()
	{
		 Ext.Ajax.request({
			method : Constants.POST,
			url :  "hr/getPhotoInfo.action?empId="
            + empIdUsing + "&time=" + new Date().getTime(),
			params : {
//				empId : empId
			},
			success : function(result, request) {
				var record = eval('(' + result.responseText + ')');
				if(record!=""&&record.msg=="成功")
				{
				isPhoto="有";
				}else
				{
					isPhoto="无";
				}
				
			},
			failure : function() {
				
			}
		}); 
	}
    var rootNode = new Ext.tree.AsyncTreeNode({
        text : Constants.POWER_NAME,
        id : '0',
        isRoot : true
    });
    
    var treePanel = new Ext.tree.TreePanel({
        renderTo : "treePanel",
        autoScroll : true,
//        autoHeight : true,
        root : rootNode,
        border : false,
        rootVisible : false,
        loader : new Ext.tree.TreeLoader({
            dataUrl : "hr/getDeptEmpTreeList.action"
        })
    });

    treePanel.on("click", treeClick);
    treePanel.render();
    rootNode.expand();
	treePanel.on('contextmenu', function(node, e) {
				if(node.leaf){
				return false
				}
				node.select();// 很重要
				e.stopEvent();
				var menu = new Ext.menu.Menu({
							id : 'mainMenu',
							items : [new Ext.menu.Item({
												text : '导出',
												iconCls : 'export',
												handler : function(){
			var curNode = treePanel.getSelectionModel().getSelectedNode();
			Ext.Ajax.request({
				url : 'hr/getEmpMaintBaseInfoList.action',
				params : {
				strDeptId : curNode.id
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText)
				if(curNode.id==null||curNode.id=="")
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			                  return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 11>人字档案信息基本信息导出</th></tr>";
				var html = [tableHeader];
				html
						.push("<tr><th>姓名</th><th>人员编码 </th><th>内部编号</th><th>部门名称</th><th>岗位名称</th><th>出生日期</th>" +
								"<th>年龄 </th><th>性别</th><th>民族</th><th >员工身份</th><th >是否有照片</th><th >政治面貌</th>" +
								"<th>是否主业 </th><th>籍贯</th><th>自定义编码</th><th >户籍地址</th><th >户口</th><th >婚姻状况</th>" +
								"<th>国籍 </th><th>血型</th><th>现有文化程度</th><th >本人成分</th><th >入党时间</th><th >入党转正时间</th></tr>")
					for (var i = 0; i <res.length; i++) {
					var rec = res[i];
   	        	    html.push('<tr><td align=left >' + (rec.chsName==null?"":rec.chsName) + '</td><td align=left>'
	 						+( rec.newEmpCode==null?"":rec.newEmpCode)+ '&nbsp;</td><td align=left>'
							+(( rec.newEmpCode==null?"":rec.newEmpCode).substr(6,6))+ '&nbsp;</td><td align=left>'
							+ (rec.deptName==null?"":rec.deptName )+ '</td><td align=left>'
							+ (rec.stationName==null?"":rec.stationName)+ '</td><td align=left>'
							+( rec.brithday==null?"":rec.brithday )+ '</td><td align=left>'
							+( rec.brithday==null?"": getage(rec.brithday) ) + '</td><td align=left>'
							+ (rec.sex==null?"":getSex(rec.sex)) + '</td><td align=left>'
							+ (rec.nationName==null?"":rec.nationName ) + '</td><td align=left>' 
							+ (rec.workName==null?"": rec.workName )+ '</td><td align=left>' 	
							+ (isPhoto)+ '</td><td align=left>'
							+ (rec.politicsName==null?"":rec.politicsName) + '</td><td align=left>'
							+ (rec.isMainWork==null?"":getisMain(rec.isMainWork))+ '</td><td align=left>'
							+ (rec.nativePlaceName==null?"":rec.nativePlaceName) + '</td><td align=left>'
							+ (rec.empCode==null?"":rec.empCode) + '&nbsp;</td><td align=left>' 
							+ (rec.familyAddress==null?"":rec.familyAddress) + '</td><td align=left>' 
							
							+ (rec.household==null?"":getHouse(rec.household))+ '</td><td align=left>'
							+ (rec.isWedded==null?"":getisWed(rec.isWedded) ) + '</td><td align=left>'
							+ (rec.country==null?"":rec.country)+ '</td><td align=left>'
							+ (rec.bloodType==null?"":rec.bloodType) + '</td><td align=left>'
							+ (rec.educationName==null?"":rec.educationName) + '</td><td align=left>' 
							+ (rec.component==null?"":getCompent(rec.component) )+ '</td><td align=left>' 
							+ (rec.intoPartDate==null?"":rec.intoPartDate ) + '</td><td align=left>' 
							+( rec.partPositiveDate==null?"":rec.partPositiveDate)+ '</td></tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
			
						
			  }else
						{
						      Ext.Msg.alert('提示', '无数据进行导出！');
			                  return;
						}
					
				}},
				failure : function(response,options){
					
							Ext.Msg.alert('提示',"导出失败！");
					}
			})
		
												}
											})]
						});
				var coords = e.getXY();
				menu.showAt([coords[0], coords[1]]);
			}, this);
			
    function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
    //-------------add by liuyi 091231 人员列表----------------
	var MyRecord = Ext.data.Record.create([{
				name : 'empId',
				mapping : 0
			}, {
				name : 'empCode',
				mapping : 1
			}, {
				name : 'chsName',
				mapping : 2
			}, {
				name : 'deptId',
				mapping : 3
			}, {
				name : 'deptCode',
				mapping : 4
			}, {
				name : 'deptName',
				mapping : 5
			},{
				name  : 'newEmpCode',
				mapping : 6
			}]);

	var dataProxy = new Ext.data.HttpProxy(
			{
				url:'hr/getEmpListByFilter.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
//分页
	store.load({
			params : {
				start : 0,
				limit : 18		
			}
		});
    var sm = new Ext.grid.CheckboxSelectionModel({
    	singleSelect : true
    });
   var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		width : 100,
		name : "fuzzy"
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [
		sm, 
		{
			header : "员工编码",
			hidden : true,
			sortable : true,
			width : 75,
			dataIndex : 'empCode',
			align:'center'
		}, 
		{
			header : "员工编码",
			sortable : true,
			width : 75,
			dataIndex : 'newEmpCode',
			align:'center'
		}, 
		{
			header : "员工姓名",
			sortable : true,
			width : 80,
			dataIndex : 'chsName',
			align:'center'
		}, 
		{
			header : "部门",
			sortable : true,
			dataIndex : 'deptName',
			align:'center'
		}
		],
		sm : sm,
		tbar : [
		fuzzy, {
			text : "查询",
			 iconCls : 'query',
			handler : queryRecord
		}],	
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "",
			emptyMsg : ""
		})
	});
	grid.on('rowclick',function(Grid,rowIndex,e){
		var rec = store.getAt(rowIndex)
		if(rec.get('empId')){
			tabPanel.setActiveTab("infoForm");
			empIdUsing = rec.get('empId');
			initEmp();
			empParam.set('empId',rec.get('empId'));
			empParam.set('chsName',rec.get('chsName'));
			empParam.set('newEmpCode',rec.get('newEmpCode'));
			baseInfoObj.loadEmp(empIdUsing)
			baseInfoObj.modiBtu.setDisabled(false)
//			employee.loadEmpInfo(rec.get('empId'));
		}else
			empIdUsing = null;
	})
	function queryRecord() {
		var fuzzytext = fuzzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	
	var baseInfoObj = new Personnel.BaseInfo();
	var workResumeObj = new Personnel.WorkResume();
	var specialtyObj = new Personnel.EmpSpecialty();

	var  rewardObj=new  Personnel.EmpReward();
    var  punishObj=new   Personnel.EmpPunish();
	var relationObj = new Personnel.EmpSocialRelations();
	var contractObj = new Personnel.Workcontract();
	
	var tabPanel = new Ext.TabPanel({
		id : 'tabPanel',
		activeItem : 0,
		autoScroll : true,
		items : [baseInfoObj.infoForm, workResumeObj.workPanel
//			{
//			id : 'tab2',
//			title : '工作经历',
//			location : 'about:blank;'
//		}
		, {
            id : 'studyResume',
            title : '学习简历',
            html : "<iframe name='studyResume' " +
//            		"src='hr/document/empdocument/personnelfiles/personnel_studyResume/personnel_studyResume.jsp'" +
            		" style='width:100%;height:100%;border:0px;'></iframe>"
        
//			listeners : {
//				activate : function() {
//					if (empIdUsing != null
//							&& document.iframe3.empIdUsing != empIdUsing) {
//						document.iframe3.location = "" + "?empId=" + empIdUsing;
//					} else {
//						Ext.Msg.alert('提示', '请先选择人员!');
//						document.iframe3.location = 'about:blank'
//					}
//				}
//			}
		}, rewardObj.rewardPanel/*,{
			id : 'tab4',
			title : '奖励情况',
			html : '<iframe="iframe4" id="iframe4" name="iframe" frameborder=0 width="100%" height="100%"/>'
		}*/,punishObj.punishPanel,/*{
			id : 'tab5',
			title : '处罚情况',
			html : '<iframe="iframe5" id="iframe5" name="iframe" frameborder=0 width="100%" height="100%"/>'
		},*/{
			id : 'familyMember',
			title : '家庭主要成员',
			html : '<iframe name="familyMember" frameborder=0 width="100%" height="100%"/>'
		},relationObj.relationPanel,{
			id : 'contactFashion',
			title : '联系方式',
			html : '<iframe name="contactFashion" frameborder=0 width="100%" height="100%""/>'},specialtyObj.spePanel,
//			{
//			id : 'contractPanel',
//			title : '劳动合同',
//			html : '<iframe="iframe10" id="iframe10" name="iframe" frameborder=0 width="100%" height="100%"/>'
//		},
		contractObj.contractPanel]
	})
    
	tabPanel.on('tabchange',function(tab,panel){
		if(panel.getId() == 'workPanel'){
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
			{
				workResumeObj.loadEmp(empParam)
			}
		}else if(panel.getId() == 'studyResume'){
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
				document.studyResume.location = "personnel_studyResume/personnel_studyResume.jsp";
		}else if(panel.getId() =='spePanel')
		{
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
			{
				specialtyObj.loadEmp(empParam)
			}
		}else if(panel.getId() == 'relationPanel')
		{
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
			{
				relationObj.loadEmp(empParam)
			}
		}
		else if(panel.getId() == "familyMember"){
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
				document.familyMember.location = "personnel_family/personnel_familyMember.jsp";
		}else if(panel.getId()=="rewardPanel")
		{
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
			{
				rewardObj.loadEmp(empParam);
			}
		}else if(panel.getId()=="punishPanel")
		{
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
			{
				punishObj.loadEmp(empParam);
			}
		}else if(panel.getId() == "contactFashion"){
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else
				document.contactFashion.location = "personnel_contact/personnel_contactFashion.jsp";
		}else if(panel.getId() == "contractPanel"){
			if(empIdUsing == null){
				Ext.Msg.alert('提示', '请先选择人员!');
				tab.setActiveTab(0);
			}else {
				contractObj.loadContractInfo(empParam);
			}
		}
	});
   
	
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "border",
        border : false,
        items : [
            {
                region : 'west',
                layout : 'fit',
                width :270,
                autoScroll : true,
                containerScroll : true,
                split : true,
                collapsible : true,
                items : [new Ext.TabPanel({
							tabPosition : 'bottom',
										activeTab : 1,
										layoutOnTabChange : true,
										items:[{title : '列表方式',
												    border : false,
													autoScroll : true,
													layout : 'border',
													items : [{
													region : 'center',
													layout : 'fit',
													items : [grid]
												}]
												},{
													title : '树形方式',
													border : false,
													autoScroll : true,
													items : [treePanel]
												}]
							})]
           
            },
             {
                region : 'center',
                layout : 'fit',
                border : true,
                frame : false,
                collapsible : false,
                items : [tabPanel]
        }]
    });
    baseInfoObj.setEnable(false);
     function treeClick(node, e) {
    	 e.stopEvent();
        if (node.isLeaf()) {
            e.stopEvent();
            initEmp();
            tabPanel.setActiveTab(0);
            empIdUsing = node.id;
            var str = node.text.split('(');
            if(str && str.length > 0){
            	empParam.set('chsName',str[0])
            	// 树中放的是新工号
            	empParam.set('newEmpCode',node.attributes.code);
            }
            empParam.set('empId',empIdUsing);
            
            baseInfoObj.loadEmp(empIdUsing);
            rewardObj.loadEmp(empParam);
            punishObj.loadEmp(empParam);
            baseInfoObj.modiBtu.setDisabled(false);
        }else
        	empIdUsing = null;
    }
    
    
   
    function initEmp() {
		empParam = new MyRecord({
			empId : null,
			newEmpCode : null,
			chsName : null
		});
	}
});
