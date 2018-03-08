// 工作流缺省定义，包含流程描述，开始、结束、人工、路由图元定义，以及连线的定义
var default_activity = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n" +
 "<root>\n"
+ "	<state 	id='1' typeName='Process' height='48' width='48' x='10'	y='10' >\n"  //流程节点
+ "		<name >Process</name>\n"
+ "		<propertyList>\n"
+ "			<property name='ProcessCode' >\n"
+ "				<value/>\n"
+ "			</property>\n" 
+ "			<property name='ProcessName' >\n"
+ "				<value>工作流例子</value>\n"
+ "			</property>\n"
+ "			<property name='ProcessVersion' >\n"
+ "				<value>工作流版本描述</value>\n"
+ "			</property>\n"
+ "			<property name='author'	>\n"
+ "				<value>Administrator</value>\n"
+ "			</property>\n"
+ "			<property name='department'	>\n"
+ "				<value>部门</value>\n"
+ "			</property>\n"
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "	</state>\n"
+ "	<state id='2' typeName='StartActivity'	height='48'	width='48' x='260' y='260' >\n" //开始活动节点
+ "		<name >StartActivity</name>\n"
+ "		<propertyList>\n"
+ "			<property name='ActivityCode' >\n"
+ "				<value>2</value>\n"
+ "			</property>\n"
+ "			<property name='ActivityName' >\n"
+ "				<value>开始活动</value>\n"
+ "			</property>\n"
+ "         <property name='url' >\n"
+ "				<value></value>\n"
+ "			</property>\n"
+ "         <property name='timeLimit' >\n"
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='splitType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "	</state>\n"
+ "	<state id='3' typeName='EndActivity' height='48' width='48' x='930' y='260' >\n" //结束活动节点
+ "		<name >EndActivity</name>\n"
+ "		<propertyList>\n"
+ "			<property name='ActivityCode' >\n"
+ "				<value>3</value>\n"
+ "			</property>\n"
+ "			<property name='ActivityName' >\n"
+ "				<value>结束活动</value>\n"
+ "			</property>\n"
+ "         <property name='joinType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "	</state>\n"
+ "	<state id='4' typeName='ManualActivity' height='48' width='48' x='450' y='200' >\n" //人工活动节点
+ "		<name >ManualActivity</name>\n"
+ "		<propertyList>\n"
+ "			<property name='ActivityCode' >\n"           
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='ActivityName' >\n"           
+ "				<value>人工活动</value>\n"
+ "			</property>\n"
+ "         <property name='url' >\n"
+ "				<value></value>\n"
+ "			</property>\n"
+ "         <property name='timeLimit' >\n"
+ "				<value></value>\n"
+ "			</property>\n"
+ "         <property name='joinType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "			<property name='splitType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "	</state>\n"
+ "	<state id='5' typeName='AutoActivity' height='48' width='48' x='450' y='200' >\n" //自动活动节点
+ "		<name >AutoActivity</name>\n"
+ "		<propertyList>\n"
+ "			<property name='ActivityCode' >\n"           
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='ActivityName' >\n"           
+ "				<value>自动活动</value>\n"
+ "			</property>\n"
+ "         <property name='joinType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "			<property name='splitType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "			<property name='parameter' >\n"
+ "				<value/>\n"
+ "			</property>\n"
+ "			<property name='TriggerEvent' >\n"
+ "				<value/>\n"
+ "			</property>\n" 
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "	</state>\n"
+ "	<state id='6' typeName='RouterActivity' height='48' width='48' x='450' y='300' >\n" //路由活动节点
+ "		<name  >RouterActivity</name>\n"
+ "		<propertyList>\n"
+ "			<property name='ActivityCode' >\n"
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='ActivityName' >\n"
+ "				<value>路由活动</value>\n"
+ "			</property>\n"
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "	</state>\n"
+ "	<state id='7' typeName='JoinActivity' height='48' width='48' x='450' y='300' >\n" //聚合活动节点
+ "		<name >JoinActivity</name>\n"
+ "		<propertyList>\n"
+ "			<property name='ActivityCode' >\n"
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='ActivityName' >\n"
+ "				<value>聚合活动</value>\n"
+ "			</property>\n"
+ "           <property name='joinType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "	</state>\n"
+ "	<connector color='0' description='' id='-2' type='connection'>\n"
+ "		<from>Shape.ManualActivity.4</from>\n"
+ "		<to>Shape.AutoActivity.5</to>\n"
+ "		<propertyList>\n"
+ "			<property name='lineCode' >\n"
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='lineName' >\n"
+ "				<value>事件名称</value>\n"
+ "			</property>\n"
+ "			<property name='priority' >\n"
+ "				<value>18</value>\n"
+ "			</property>\n"
+ "         <property name='joinType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "			<property name='splitType' >\n"
+ "				<value>XOR</value>\n"
+ "			</property>\n"
+ "			<property name='url' >\n"    //限制       
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='changeBusiStateTo' >\n"    //限制       
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='exeBusiComponent' >\n"    //限制       
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='restrict' >\n"    //限制       
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='isDoPreFuction' >\n"          
+ "				<value>false</value>\n"
+ "			</property>\n"
+ "			<property name='preFuction' >\n"      //状态改变前执行      
+ "				<value></value>\n"
+ "			</property>\n"
+ "			<property name='isDoPostFuction' >\n"             
+ "				<value>false</value>\n"
+ "			</property>\n"
+ "			<property name='postFuction' >\n"    //状态改变后执行        
+ "				<value></value>\n"
+ "			</property>\n" 
+ "		</propertyList>\n"
+ "		<description/>\n"
+ "		<bendpoints/>\n"
+ "	</connector>\n"
+ "</root>";



// 工作流的扩展属性
var flow_extconfig = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n"
+ "	<root>\n"
+ "		<Static>\n"
+ "  		<ActivityContext id=''>\n"
+ "           	<ActivityConfExt>\n"
+ "                 <isLoopRestart>false</isLoopRestart>\n"
+ "                 <standardTimeLimitValue/>\n"
+ "           	</ActivityConfExt>\n"
+ "  		</ActivityContext>\n"
+ "         <connector>\n"
+ "             <from/>\n"
+ "             <to/>\n"
+ "          	<cndesc/>\n"
+ "         </connector>\n"
+ "        <FlowContext>\n"
+ "       		 <limitIsRelData>false</limitIsRelData>\n"
+ "		   </FlowContext>\n"
+ "		</Static>\n"
+ "</root>";


//用于存放从环节库中托拽过来的xml节点
var temp_schema = new ActiveXObject("MSXML2.DOMDocument");
temp_schema.async = false;

//用于存放工作流定义文件中缺省的部分
var def_schema = new ActiveXObject("MSXML2.DOMDocument");
def_schema.async = false;
def_schema.loadXML(default_activity);

//用于存放从schema中转化出来的xml
var xmltostring_shema = "";

//用于存放从ext_schema中转化出来的xml
var xmltostring_ext_shema = "";

var def_process_node = def_schema.selectSingleNode("root/state[@typeName='Process']");
var def_start_node = def_schema.selectSingleNode("root/state[@typeName='StartActivity']");
var def_end_node = def_schema.selectSingleNode("root/state[@typeName='EndActivity']");
var def_manual_node = def_schema.selectSingleNode("root/state[@typeName='ManualActivity']");
var def_route_node = def_schema.selectSingleNode("root/state[@typeName='RouterActivity']");
var def_join_node = def_schema.selectSingleNode("root/state[@typeName='JoinActivity']");
var def_connect_node = def_schema.selectSingleNode("root/connector");



//用于存放工作流定义文件中缺省的部分
var exp_schema = new ActiveXObject("MSXML2.DOMDocument");
exp_schema.async = false;
exp_schema.loadXML(flow_extconfig);

//连接线扩展属性
var exp_connector_node=exp_schema.selectSingleNode("root/Static/connector");
//节点扩展属性
var exp_manual_node=exp_schema.selectSingleNode("root/Static/ActivityContext");
var exp_flow_node = exp_schema.selectSingleNode("root/Static/FlowContext");

/**
 * 工作流定义schema对象
 */
function WFSchema()
{
	
	this.schema;				// shcema的XMLdocument对象
	this.schemaExt;				//工作流的扩展属性
	this.maxId = 0;				// 最大id编号
	this.states = null;			// 图元节点（xml node）
	this.connectors = null;		// 连线节点（xml node）

	this.exp_nodes = null;     //
	this.ProcessCode;          //流程编码
	this.ProcessName;          //流程名称
	this.ProcessVersion;          //流程版本描述
	this.author;
	this.department;
	
	this.clear = function() {
		
		this.schema = null;
		this.schemaExt = null;
		
		for (var i=0; i<icons.length; i++) {
			document.body.removeChild(icons[i].img);
			document.body.removeChild(icons[i].text);
		}
		for (var i=0; i<lines.length; i++) {
			document.body.removeChild(lines[i].lineImg);
			document.body.removeChild(lines[i].text);
		}
		icons = new Array();
		lines = new Array();
	}
	
	// 新创建一个流程定义模板，初始化开始活动和结束活动
	this.newSchema = function(ProcessCode,ProcessName,ProcessVersion) { 
		this.clear(); 
		if(ProcessCode==null)
		   this.ProcessCode="";
		if(ProcessName==null)
		   this.ProcessName="";
		   
		//初始化流程模板
		this.schema = new ActiveXObject("MSXML2.DOMDocument");
		this.schema.async = false;
		this.schema.loadXML("<?xml version='1.0' encoding='UTF-8' standalone='no'?><autoGraphics><graphic name='"+ProcessCode+"' type='wfg' router='0'><states/><connectors/><groups/></graphic></autoGraphics>");
		//this.schema.loadXML("<autoGraphics><graphic name='"+ProcessCode+"' type='wfg' router='0'><states/><connectors/><groups/></graphic></autoGraphics>");
		
		
		//初始化流程模板扩展属性
		this.schemaExt = new ActiveXObject("MSXML2.DOMDocument");
		this.schemaExt.async = false;
		this.schemaExt.loadXML("<?xml version='1.0' encoding='UTF-8' standalone='no'?><root><Static></Static></root>");
		this.exp_nodes = this.schemaExt.selectSingleNode("root/Static");
		this.exp_nodes.appendChild(exp_flow_node.cloneNode(true));
		
		this.states = this.schema.selectSingleNode("autoGraphics/graphic/states");
		this.connectors = this.schema.selectSingleNode("autoGraphics/graphic/connectors");
		
		
		//取得流程定义节点
		this.states.appendChild(def_process_node.cloneNode(true));
		
		this.states.selectSingleNode("state[@typeName='Process']/propertyList/property[@name='ProcessCode']/value").text = ProcessCode;
		this.states.selectSingleNode("state[@typeName='Process']/propertyList/property[@name='ProcessName']/value").text=ProcessName;
		this.states.selectSingleNode("state[@typeName='Process']/propertyList/property[@name='ProcessVersion']/value").text=ProcessVersion;
		
		//取得开始节点
		this.states.appendChild(def_start_node.cloneNode(true));
		
		//取得结束节点
		this.states.appendChild(def_end_node.cloneNode(true));
		
		// 根据schema绘制流程图
		this.draw();
		 
		
	}

	// 从一个文件装载流程定义文件，并显示流程
	this.load = function(fileName) {
		this.clear();
		var fso, f, ts;
		fso = new ActiveXObject("Scripting.FileSystemObject");
		f = fso.GetFile(fileName);
		ts = f.OpenAsTextStream(1, -2);
		this.schema = new ActiveXObject("MSXML2.DOMDocument");
		this.schema.async=false;
		//alert(ts.ReadAll());
		this.schema.loadXML(ts.ReadAll());
		ts.Close();
		this.states = this.schema.selectSingleNode("autoGraphics/graphic/states");
		this.connectors = this.schema.selectSingleNode("autoGraphics/graphic/connectors");
        this.ProcessCode = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessCode']/value").text;
        this.ProcessName = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessName']/value").text;
        this.ProcessVersion = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessVersion']/value").text;
		this.author = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='author']/value").text;
		this.department = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='department']/value").text;
		this.draw();
	}
	this.loadByXmlData= function(strxml) {
		this.clear();
	    this.schema = new ActiveXObject("MSXML2.DOMDocument");
	    this.schema.async=false;
	    this.schema.loadXML(strxml); 
	    this.states = this.schema.selectSingleNode("autoGraphics/graphic/states"); 
	    this.connectors = this.schema.selectSingleNode("autoGraphics/graphic/connectors");
        this.ProcessCode = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessCode']/value").text;
        this.ProcessName = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessName']/value").text;
        this.ProcessVersion = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessVersion']/value").text;
        this.draw();
	}
	this.xmlload = function(strxml,stract1,stract2) {
	    this.clear();
	    this.schema = new ActiveXObject("MSXML2.DOMDocument");
	    this.schema.async=false;
	    this.schema.loadXML(strxml);
	    this.states = this.schema.selectSingleNode("autoGraphics/graphic/states"); 
	    this.connectors = this.schema.selectSingleNode("autoGraphics/graphic/connectors");
        this.ProcessCode = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessCode']/value").text;
        this.ProcessName = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessName']/value").text;
        this.ProcessVersion = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessVersion']/value").text;
     
        //初始化流程模板扩展属性
//		this.schemaExt = new ActiveXObject("MSXML2.DOMDocument");
//		this.schemaExt.async = false;
//		this.schemaExt.loadXML("<?xml version='1.0' encoding='UTF-8' standalone='no'?><root><Static></Static></root>");
//		this.exp_nodes = this.schemaExt.selectSingleNode("root/Static");
//		this.exp_nodes.appendChild(exp_flow_node.cloneNode(true));
	    this.draw1(stract1,stract2);
	}
	
	this.xmlload1 = function(strxml) {
		
	    this.clear();
	  
	    this.schema = new ActiveXObject("MSXML2.DOMDocument");
	    this.schema.async=false;
	   
	    this.schema.loadXML(strxml);
	    
	  
	    this.states = this.schema.selectSingleNode("autoGraphics/graphic/states");
	    this.connectors = this.schema.selectSingleNode("autoGraphics/graphic/connectors");
        this.ProcessCode = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessCode']/value").text;
        this.ProcessName = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessName']/value").text;
        this.ProcessVersion = this.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessVersion']/value").text;
        //初始化流程模板扩展属性
		this.schemaExt = new ActiveXObject("MSXML2.DOMDocument");
		this.schemaExt.async = false;
		this.schemaExt.loadXML("<?xml version='1.0' encoding='UTF-8' standalone='no'?><root><Static></Static></root>");
		this.exp_nodes = this.schemaExt.selectSingleNode("root/Static");
		this.exp_nodes.appendChild(exp_flow_node.cloneNode(true));
	    this.draw1("","");
	}
	
	// 根据schema绘制流程图
	this.draw = function() { 
		var acts = this.states.selectNodes("state[@typeName!='Process']");  
		for (var i=0; i < acts.length; i++) {
			var icon = new Icon();
			var type = acts[i].getAttribute("typeName");
			var x = acts[i].getAttribute("x");
			var y = acts[i].getAttribute("y");
			var id= acts[i].getAttribute("id"); 
			var ActivityCode = acts[i].selectSingleNode("propertyList/property[@name='ActivityCode']/value").text;
			var ActivityName = acts[i].selectSingleNode("propertyList/property[@name='ActivityName']/value").text; 
			if (id > this.maxId) this.maxId = parseInt(id); 
			//图元初始化
			icon.init(type, x, y, ActivityName,ActivityCode);  
			icon.activity = acts[i];
			icon.states=this.states; 
			//icon.exp_Activity = this.exp_nodes.selectSingleNode("ActivityContext[@id='"+ActivityCode+"']");
			icon.id = id;
			icons.push(icon);  
		}
		
		
	
		var conns = this.connectors.selectNodes("connector");
	 
		for (var i=0; i < conns.length; i++) {
			var from = conns[i].selectSingleNode("from").text;
			var to = conns[i].selectSingleNode("to").text;
//			var fromId = (from.split("."))[2];
//			var toId = (to.split("."))[2];
			//根据Iconid获取图元
			var beginIcon = getIcon(from);
			var endIcon = getIcon(to);
			var l = new Line();
			//bendpoint是图形中是否包含折线
			var pts = conns[i].selectNodes("bendpoints/bendpoint");
			if (pts != null) {
				for (var j=0; j < pts.length; j++) {
					var w1 = pts[j].getAttribute("w1");
					var h1 = pts[j].getAttribute("h1");
					l.points.push(new Point(beginIcon.img.offsetLeft + half_image_size + parseInt(w1), beginIcon.img.offsetTop + half_image_size + parseInt(h1) ));
				}
			}
			var code = conns[i].selectSingleNode("propertyList/property[@name='lineCode']/value").text;
			var name = conns[i].selectSingleNode("propertyList/property[@name='lineName']/value").text;
			//alert(code);
			l.setDisplayName(code,name); 
			l.init(beginIcon, endIcon); 
			l.states=this.states;
			//l.expconnector = this.exp_nodes.selectSingleNode("connector[from='"+from+"' and to='"+to+"']");
			l.connector = conns[i];
			lines.push(l);
		}
	}

	// 根据schema绘制流程图
	this.draw1 = function(stract1,stract2) { 
		var acts = this.states.selectNodes("state[@typeName!='Process']");
		for (var i=0; i < acts.length; i++) { 
			var icon = new Icon();
			var type = acts[i].getAttribute("typeName");
			var x = acts[i].getAttribute("x")-250;
			var y = acts[i].getAttribute("y");
			var id= acts[i].getAttribute("id");
			var ActivityCode = acts[i].selectSingleNode("propertyList/property[@name='ActivityCode']/value").text; 
			var ActivityName = acts[i].selectSingleNode("propertyList/property[@name='ActivityName']/value").text;
			if (id > this.maxId) this.maxId = parseInt(id);
			//图元初始化  
			icon.init1(type, x, y, ActivityName,ActivityCode,stract1,stract2);
			icon.activity = acts[i];
			icon.states=this.states;
			//icon.exp_Activity = this.exp_nodes.selectSingleNode("ActivityContext[@id='"+ActivityCode+"']");
			icon.id = id;
			icons.push(icon);
		}
		
		var conns = this.connectors.selectNodes("connector");
		for (var i=0; i < conns.length; i++) {
			var from = conns[i].selectSingleNode("from").text;
			var to = conns[i].selectSingleNode("to").text;
//			var fromId = (from.split("."))[2];
//			var toId = (to.split("."))[2]; 
//			var fromId = from;
//			var toId = to;
			
			//根据Iconid获取图元
			var beginIcon = getIcon(from);
			var endIcon = getIcon(to);
			var l = new Line();
			//bendpoint是图形中是否包含折线
			var pts = conns[i].selectNodes("bendpoints/bendpoint");
			if (pts != null) {
				for (var j=0; j < pts.length; j++) {
					var w1 = pts[j].getAttribute("w1");
					var h1 = pts[j].getAttribute("h1");
					l.points.push(new Point(beginIcon.img.offsetLeft + half_image_size + parseInt(w1), beginIcon.img.offsetTop + half_image_size + parseInt(h1) ));
				}
			}
			var code = conns[i].selectSingleNode("propertyList/property[@name='lineCode']/value").text;
			var name = conns[i].selectSingleNode("propertyList/property[@name='lineName']/value").text;
			l.setDisplayName(code,name);
			
			l.init(beginIcon, endIcon);
			
			l.states=this.states;
			//l.expconnector = this.exp_nodes.selectSingleNode("connector[from='"+fromId+"' and to='"+toId+"']");
			l.connector = conns[i];
			lines.push(l);
		}
	}

	// 根据icon图片创建活动
	this.create_state = function(icon) {
	
		var state = def_manual_node;		
		switch (icon.type) {
			case "ManualActivity":
				state = def_manual_node;
				break;
			case "RouterActivity":
				state = def_route_node;
				break;
			case "JoinActivity":
				state = def_join_node;
				break;
			case "StartActivity":
				state = def_start_node;
				break; 
			case "EndActivity":
				state = def_end_node;
				break;
			default:
				state = def_manual_node;
				break;
		} 
		this.maxId++;
		var newState = state.cloneNode(true);
		newState.setAttribute("x", icon.img.offsetLeft);
		newState.setAttribute("y", icon.img.offsetTop);
		newState.setAttribute("id", this.maxId);
		icon.id = this.maxId;
		
		newState.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text=icon.ActivityCode ;//+ "_" + icon.id;

//		//设置子流程的定义id
//		if(icon.type == "SubActivity"){
//			newState.selectSingleNode("propertyList/property[@name='subProcess']/value").text=icon.ActivityCode;
//		}

//		icon.ActivityCode=icon.ActivityCode + "_" + icon.id;
//		
		//设置ActivityName
		newState.selectSingleNode("propertyList/property[@name='ActivityName']/value").text=icon.displayName;
		icon.activity = newState;
		icon.setDisplayName(icon.displayName);
		icon.states = this.states; //将当前所有环节节点的引用设置给icon
		
		icon.exp_Activity = exp_manual_node.cloneNode(true);
		icon.exp_Activity.setAttribute("id", icon.ActivityCode);
		
		//将此节点加入schema中
		this.states.appendChild(newState);
		//添加扩展属性
		this.exp_nodes.appendChild(icon.exp_Activity);
	}

	// 根据连接线创建connector节点
	this.create_connector = function(line) {
	
		var conn = def_connect_node.cloneNode(true);
		var exp_conn = exp_connector_node.cloneNode(true);		
		
		var from = conn.selectSingleNode("from");
		var to   = conn.selectSingleNode("to");
		
//		from.text = "Shape." + line.iconBegin.type + "." + line.iconBegin.id;
//		to.text   = "Shape." + line.iconEnd.type + "." + line.iconEnd.id;
		
		from.text =  line.iconBegin.id;
		to.text     =  line.iconEnd.id;
		
//		from.text = line.iconBegin.type ;
//		to.text   =  line.iconEnd.type ;

		exp_conn.selectSingleNode("from").text=line.iconBegin.id;
		exp_conn.selectSingleNode("to").text=line.iconEnd.id;
		//exp_conn.selectSingleNode("propertyList/property[@name='lineCode']/value").text=line.lineCode;
		//alert(line.displayName);
		//exp_conn.selectSingleNode("propertyList/property[@name='lineName']/value").text=line.lineName;
		
		line.connector = conn;
		line.expconnector = exp_conn;
		line.states = this.states; //将当前流程上所有图元的节点设置给连接线
		
		this.connectors.appendChild(conn);
		this.exp_nodes.appendChild(exp_conn);
	}
	
	this.writeToFile = function(fileName) {
	    var fso, f, ts;
		fso = new ActiveXObject("Scripting.FileSystemObject");
		f = fso.OpenTextFile(fileName, 2, true);
		this.setWFSchemaFromGraphic();
		f.Write(this.schema.xml.replace("<?xml version=\"1.0\"","<?xml version=\"1.0\" encoding=\"UTF-8\""));
		f.Close();
		alert("Save OK!");
	}

	//将schema中的xml节点转化为字符串
	this.writeToString = function() {
		this.setWFSchemaFromGraphic();
		xmltostring_shema = this.schema.xml.replace("<?xml version=\"1.0\"", "<?xml version=\"1.0\" encoding=\"UTF-8\"");
	}
	
	
	//将schema中的xml节点转化为字符串
	this.writeToStringExt = function() {
		xmltostring_ext_shema = this.schemaExt.xml.replace("<?xml version=\"1.0\"", "<?xml version=\"1.0\" encoding=\"UTF-8\""); 
	}
	
	// 根据ie流程定义界面中的图元和连线元素设置工作流流程定义schema的xml数据
	this.setWFSchemaFromGraphic = function() {
		var n = this.schema.selectSingleNode("autoGraphics/graphic");
		n.setAttribute("name", this.ProcessCode);
		var i;
		for (i=0; i<icons.length; i++) {
			var icon = icons[i];
			var state = icon.activity;
			var actName = state.selectSingleNode("propertyList/property[@name='ActivityName']/value");
			state.setAttribute("x", icon.img.offsetLeft);
			state.setAttribute("y", icon.img.offsetTop);
			actName.text = icon.displayName;
		}
		for (i=0; i<lines.length; i++) {
			var line = lines[i];
			var conn = line.connector;
			var from = conn.selectSingleNode("from");
			var to   = conn.selectSingleNode("to");
			var lineCode   = conn.selectSingleNode("propertyList/property[@name='lineCode']/value");
			var lineName   = conn.selectSingleNode("propertyList/property[@name='lineName']/value");
 
//			from.text = "Shape." + line.iconBegin.type + "." + line.iconBegin.id;
//			to.text   = "Shape." + line.iconEnd.type + "." + line.iconEnd.id;
			
			from.text =  line.iconBegin.id;
			to.text   =  line.iconEnd.id;
			
			var dispName = conn.selectSingleNode("propertyList/property[@name='lineName']/value");
			dispName.text = line.lineName;
			var bendpoints = conn.selectSingleNode("bendpoints");
			removeAllChild(bendpoints);
			for (var j=0; j<line.points.length; j++) {
				var bendpoint = this.schema.createNode("element", "bendpoint", "");
				var p = line.points[j];
				var w1 = p.x - line.begin.x;
				var h1 = p.y - line.begin.y;
				var w2 = p.x - line.end.x;
				var h2 = p.y - line.end.y;
				bendpoint.setAttribute("w1", w1);
				bendpoint.setAttribute("h1", h1);
				bendpoint.setAttribute("w2", w2);
				bendpoint.setAttribute("h2", h2);
				bendpoints.appendChild(bendpoint);
			}
		}
	}
}
