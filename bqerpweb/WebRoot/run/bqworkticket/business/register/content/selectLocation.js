Ext.onReady(function() {
			var method = "one";
			var show = true;
            var strBlockCode = "";
			if (getParameter("op") != "") {
				method = getParameter("op");
			}
			if (method != "one") {
				show = false;
			}
            if(getParameter("blockCode") != ""){
                strBlockCode = getParameter("blockCode")
            }
			// -----------区域列表--------------------

			var MyRecord = Ext.data.Record.create([{
						name : 'locationId'
					}, {
						name : 'locationName'
					}, {
						name : 'blockCode'
					}]);

			var dataProxy = new Ext.data.HttpProxy(

			{
						url : 'workticket/getContentAreaContent.action'
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
			var fuuzy = new Ext.form.TextField({
						id : "fuzzy",
						name : "fuzzy"
					});
			var sm = new Ext.grid.CheckboxSelectionModel({
						hidden : show
					});
            // 查询
           queryRecord();
			var grid = new Ext.grid.GridPanel({
						layout : 'fit',
						store : store,
						enableColumnHide : true,
						columns : [sm, {
									header : "区域编码",
									width : 30,
									sortable : true,
									dataIndex : 'locationId'
								}, {
									header : "区域名称",
									width : 115,
									sortable : true,
									dataIndex : 'locationName'
								}, {
                                    header : "机组编码",
                                    width : 30,
                                    sortable : true,
                                    dataIndex : 'blockCode'
                                }],
						sm : sm,
						autoSizeColumns : true,
						viewConfig : {
							forceFit : true
						},
						//title : '区域列表',
						tbar : ['区域名称:', fuuzy, {
									text : "查询",
									iconCls:'query',
									handler : queryRecord
								}, {
									text : '确定',
									iconCls:"confirm",
									id : 'gridadd',
									hidden : show,
									handler : selectMany

								}]

					});

			grid.on("rowdblclick", selectOneRecord);

			// ------------------

			// 查询
			function queryRecord() {
				var fuzzytext = fuuzy.getValue();
				store.baseParams = {
					fuzzy : fuzzytext
				};
				store.load({
                    params:{blockCode: strBlockCode}
                });
			}

			function selectOneRecord() {

				if (grid.selModel.hasSelection()) {

					var records = grid.selModel.getSelections();
					var recordslen = records.length;
					if (recordslen > 1) {
						Ext.Msg.alert("系统提示信息", "请先选择一个区域！");
					} else {
						var record = grid.getSelectionModel().getSelected();
						var loc = new Object();
						loc.id = record.get("locationId");
						loc.name = record.get("locationName");

						window.returnValue = loc;
						window.close();
					}
				} else {
					Ext.Msg.alert("提示", "请先选择区域!");
				}
			}

			function selectMany() {
				if (method == "many") {
					var mysm = grid.getSelectionModel();
					var selected = mysm.getSelections();
					var ids = [];
					var names = [];
					var codes = [];
					if (selected.length == 0) {
						Ext.Msg.alert("提示", "请选择区域！");
					} else {

						for (var i = 0; i < selected.length; i += 1) {
							var member = selected[i].data;
							if (member.locationId) {
								ids.push(member.locationId);
								names.push(member.locationName);
							} else {

								store.remove(store.getAt(i));
							}

						}

						var loc = new Object();
						loc.id = ids.toString();
						loc.code = codes.toString();
						loc.name = names.toString();

						window.returnValue = loc;
						window.close();

					}
				}

			}
			var panel2 = new Ext.Panel({
						id : 'tab2',
						layout : 'fit',
						items : [grid]
					});
			new Ext.Viewport({
						enableTabScroll : true,
						layout : "fit",
						items : [panel2]
					});

		});
