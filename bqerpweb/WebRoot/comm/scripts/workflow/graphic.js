var icons = new Array(); // 所有的图元对象
var lines = new Array(); // 所有连线对象
/**
 * 给js 的Array对象增加一个remove的方法，可以直接删除数组中指定的对象
 */
Array.prototype.remove = function(obj) {
	var c = 0;
	for (var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != obj) {
			this[n++] = this[i];
		} else {
			c++;
		}
	}
	this.length -= c;
}

var graphic_icon_path = "comm/images/workflow/"; // 当前流程编辑器的图片的相对路径
var half_image_size = 20; // 工作流图标的大小的一半值

var drag_icon_x = 0; // 拖动流程中图元的开始的x位置
var drag_icon_y = 0; // 拖动流程中图元的开始的y位置

// 线上的右键菜单
var line_menus = new Array(new Array("删除连线", "javascript:removeCurLine();", "",
		0), new Array("连线属性", "", "", 0));
// 图元上的右键菜单
var icon_menus = new Array(new Array("删除活动", "javascript:removeCurIcon();", "",
		0), new Array("活动属性", "", "", 0));
// 连线右键菜单对象
// var lineMenu = new Menu();
// lineMenu.init(line_menus);

// // 图元上的右键菜单
// var iconMenu = new Menu();
// iconMenu.init(icon_menus);

var curLine = null; // 当前点击的连线
var curIcon = null; // 当前点击的图元

/**
 * 根据传入的id显示右键菜单
 */
function showMenu(id) {
	if ('lineMenu' == id) {
		popMenu(linediv, 100, "111");

	}
	if ('iconMenu' == id) {
		popMenu(icondiv, 100, "111");
	}
	event.returnValue = false;
	event.cancelBubble = true;
	return false;
}

/**
 * 
 * 显示弹出菜单 menuDiv:右键菜单的内容 width:行显示的宽度
 * rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
 */

function popMenu(menuDiv, width, rowControlString)

{
	// 创建弹出菜单
	var pop = window.createPopup();
	// 设置弹出菜单的内容
	pop.document.body.innerHTML = menuDiv.innerHTML;
	// alert(menuDiv.innerHTML);
	var rowObjs = pop.document.body.all[0].rows;

	// 获得弹出菜单的行数
	var rowCount = rowObjs.length;
	// 循环设置每行的属性
	for (var i = 0; i < rowObjs.length; i++) {
		// 如果设置该行不显示，则行数减一
		var hide = rowControlString.charAt(i) != '1';
		if (hide) {
			rowCount--;
		}
		// 设置是否显示该行
		rowObjs[i].style.display = (hide) ? "none" : "";
		// 设置鼠标滑入该行时的效果
		rowObjs[i].cells[0].onmouseover = function() {
			this.style.background = "#818181";
			this.style.color = "white";
		}

		// 设置鼠标滑出该行时的效果
		rowObjs[i].cells[0].onmouseout = function() {
			this.style.background = "#cccccc";
			this.style.color = "black";
		}

	}
	// 屏蔽菜单的菜单

	pop.document.oncontextmenu = function() {
		return false;
	}
	// 选择右键菜单的一项后，菜单隐藏
	pop.document.onclick = function() {
		pop.hide();
	}
	// 显示菜单
	pop.show(event.clientX - 1, event.clientY, width, rowCount * 25,
			document.body);
	return true;

}

function isInActivity( str,  c) 
		{   
			if(str == '')
			{
				return false;
			}
			var a = str.split(",");
		     for   (var   i=0  ; i<a.length;i++)
              {                                   
                    if(a[i] == c)
                    {
                    	return true;
                    }
              }
              return false;
		};

/**
 * 坐标对象包含x,y位置
 */
function Point(x, y) {
	this.x = x;
	this.y = y;
}

/**
 * 删除当前点中的连线
 */
function removeCurLine() {
	if (curLine != null) {
		delLine(curLine);
	}
}

/**
 * 删除当前点中的图元
 */
function removeCurIcon() {
	if (curIcon != null) {
		delIcon(curIcon);
	}
}

/**
 * 根据工作流的shcema中的state的id编号获得当前编辑区中的icon对象
 */
function getIcon(id) {
	for (var i = 0; i < icons.length; i++) {
		if (icons[i].id == id)
			return icons[i];
	}
	return null;
}

/**
 * 删除图元
 */
function delIcon(icon) {
	// 不能删除开始和结束活动
	if (icon.type == "StartActivity" || icon.type == "EndActivity")
		return;
	for (var i = (icon.beginLine.length - 1); i >= 0; i--) {
		delLine(icon.beginLine[i]);
	}
	for (var i = (icon.endLine.length - 1); i >= 0; i--) {
		delLine(icon.endLine[i]);
	}
	document.body.removeChild(icon.img);
	document.body.removeChild(icon.text);
	if (schema && schema != null) {
		schema.states.removeChild(icon.activity);
		// schema.exp_nodes.removeChild(icon.exp_Activity);
	}
}
// 创建图元自身到自身的连线
function createSelfToSelfLine() {

	var linelength = lines.length;
	lines[linelength] = new Line();
	lines[linelength].init(curIcon, curIcon);

	// 在schema中添加connector节点
	schema.create_connector(lines[linelength]);

}

/**
 * 删除连线
 */
function delLine(line) {
	line.iconBegin.beginLine.remove(line);
	line.iconEnd.endLine.remove(line);
	removeLine(line.lineImg);
	removeText(line.text);
	lines.remove(line);
	// 删除xml节点的引用
	if (schema && schema != null) {
		schema.connectors.removeChild(line.connector);
		// schema.exp_nodes.removeChild(line.expconnector);
	}
}

/**
 * 图元对象
 */
function Icon() {
	this.ActivityCode; // 环节图标在数据库中的主键，对应state的activityId
	this.displayName; // 环节显示名称，对应state的activityName
	// this.memo; //环节描述信息

	this.states; // 用于存放流程模板对应的states节点，主要是设定人员时，需要能够从某个环节设定，保存次引用，主要是为了取出所有的模板节点

	this.exp_Activity; // 用于存放环节的扩展属性

	this.type; // 类型
	this.activity; // 工作流schema中的state的xml节点对象
	this.id; // 工作流schema中的state的id编号唯一值
	// 图元的图片对象
	this.img = document.createElement("<img style='position:absolute;'>");
	// 图元中的文字对象
	this.text = document
			.createElement("<input type='text' readonly='true'  style='position:absolute;background:transparent;border:0;font-size:9pt;text-align:center'>");
	this.text.style.display = "none";
	this.text.icon = this;

	this.init = _init_icon; // 初始化图元方法
	this.init1 = _init_icon1;
	// this.set_icon = _set_icon; // 初始化图元方法
	this.createLine = _create_line; // 创建连线方法
	this.redrawLine = _redraw_line; // 重新画与图元连接的连线方法
	this.moveText = _move_text; // 托拽图元后移动文字对象方法
	this.setDisplayName = _set_displayName; // 设置显示名称

	this.beginLine = new Array(); // 以图元开始的连线
	this.endLine = new Array(); // 以图元结束的连线
   
	function _init_icon1(type, x, y, activityName, ActivityCode, stract1,
			stract2) {
		this.type = type;
		this.ActivityCode = ActivityCode;
		// 蓝色
		switch (type) {
			case "ManualActivity" :
				this.img.src = graphic_icon_path + "manual.gif";
				this.displayName = "人工活动";
				break;
			case "StartActivity" :
				this.img.src = graphic_icon_path + "start.gif";
				this.displayName = "开始活动";
				break;
			case "RouterActivity":
				this.img.src = graphic_icon_path + "router.gif";
				this.displayName = "聚合活动";
				break;
			case "JoinActivity" :
				this.img.src = graphic_icon_path + "join.gif";
				this.displayName = "聚合活动";
				break;
			case "EndActivity" :
				this.img.src = graphic_icon_path + "end.gif";
				this.displayName = "结束活动";
				break;
			default :
				break;
		} 
		//红
		//f (stract1.search(ActivityCode) >= 0) {
		if(isInActivity(stract1,ActivityCode)){ 
			switch (type) {
				case "ManualActivity" :
					this.img.src = graphic_icon_path + "manual_2.gif";
					this.displayName = "人工活动";
					break;
				case "StartActivity" :
					this.img.src = graphic_icon_path + "start_2.gif";
					this.displayName = "开始活动";
					break;
					case "RouterActivity":
				this.img.src = graphic_icon_path + "router.gif";
				this.displayName = "聚合活动";
				break;
				case "JoinActivity" :
					this.img.src = graphic_icon_path + "join.gif";
					this.displayName = "聚合活动";
					break;
				case "EndActivity" :
					this.img.src = graphic_icon_path + "end_2.gif";
					this.displayName = "结束活动";
					break;
				default :
					break;
			}
		}
	 
		
		// 绿色 
		//if (stract2.search(ActivityCode) >= 0) { 
	 if(isInActivity(stract2,ActivityCode)){
			switch (type) {
				case "ManualActivity" :
					this.img.src = graphic_icon_path + "manual_1.gif";
					this.displayName = "人工活动";
					break;
				case "StartActivity" : 
					this.img.src = graphic_icon_path + "start_1.gif";
					this.displayName = "开始活动";
					break;
				case "RouterActivity":
					this.img.src = graphic_icon_path + "router.gif";
					this.displayName = "聚合活动";
					break;
				case "JoinActivity" :
					this.img.src = graphic_icon_path + "join.gif";
					this.displayName = "聚合活动";
					break;
				case "EndActivity" :
					this.img.src = graphic_icon_path + "end_1.gif";
					this.displayName = "结束活动";
					break;
				default :
					break;
			}

		}
		document.body.appendChild(this.img);
		if (activityName != null)
			this.displayName = activityName;
		this.img.style.left = x;
		this.img.style.top = y;
		this.img.icon = this;
		// 定义图片对象的开始托拽事件
		this.img.ondragstart = function() {
			this.setCapture();
			drag_icon_x = event.x - this.offsetLeft;
			drag_icon_y = event.y - this.offsetTop;
		}
		// 定义图片对象的托拽事件
		this.img.ondrag = function() {
			if (drawLineStatus && this.icon.type == "EndActivity")
				return;
			if (!drawLineStatus) {
				this.style.left = event.x - drag_icon_x;
				this.style.top = event.y - drag_icon_y;
				// this.icon.redrawLine();
			}
		}
		// // 定义图片对象的鼠标落入事件
		// this.img.onmousemove = function(){
		// curIcon = this.icon;
		//			
		// showMenu('iconMenu');
		// return false;
		// }
		//		

		// 定义图片对象的托拽结束
		this.img.ondragend = function() {

			this.releaseCapture();
			if (drawLineStatus && this.icon.type == "EndActivity")
				return;
			if (drawLineStatus) {
				// 当在图元上方释放鼠标的时候，在两个节点之间画线
				// alert('drag end');
				this.icon.createLine(this, event.x, event.y);
			}
			// 可以拖动时，拖动图标后和图标连接的线需要重新绘制
			else {
				// alert('redrawline');
				this.icon.redrawLine();
			}
		}

		// 定义图片对象的右键菜单，并且将此图元对应的一些相关信息传给右键菜单对应的方法
		this.img.oncontextmenu = function() {
			curIcon = this.icon;

			showMenu('iconMenu');
			return false;
		}

		document.body.appendChild(this.text);
		this.text.style.display = "block";
		this.text.value = this.displayName;
		/*
		 * //onchage事件 this.text.onchange = function() {
		 * 
		 * this.icon.displayName = this.value;
		 * this.icon.activity.selectSingleNode("propertyList/property[@name='activityName']/value").text=this.value; }
		 */
		this.moveText();
	}

	function _init_icon(type, x, y, activityName, ActivityCode) {
		this.type = type;
		this.ActivityCode = ActivityCode;
		switch (type) {
			case "ManualActivity" :
				// 设置图片路径
				this.img.src = graphic_icon_path + "manual.gif";
				// //设置图标缺省文字
				this.displayName = "人工活动";
				break;
			case "StartActivity" :
				this.img.src = graphic_icon_path + "start.gif";
				this.displayName = "开始活动";
				break;
			case "EndActivity" :
				this.img.src = graphic_icon_path + "end.gif";
				this.displayName = "结束活动";
				break;
			case "RouterActivity" :
				this.img.src = graphic_icon_path + "router.gif";
				this.displayName = "路由活动";
				break;
			case "AutoActivity" :
				this.img.src = graphic_icon_path + "auto.gif";
				this.displayName = "自动活动";
				break;
			case "JoinActivity" :
				this.img.src = graphic_icon_path + "join.gif";
				this.displayName = "聚合活动";
				break;

			default :
				break;
		}
		document.body.appendChild(this.img);
		if (activityName != null)
			this.displayName = activityName;
		this.img.style.left = x;
		this.img.style.top = y;
		this.img.icon = this;
		// 定义图片对象的开始托拽事件
		this.img.ondragstart = function() {
			this.setCapture();
			drag_icon_x = event.x - this.offsetLeft;
			drag_icon_y = event.y - this.offsetTop;
		}
		// 定义图片对象的托拽事件
		this.img.ondrag = function() {
			if (drawLineStatus && this.icon.type == "EndActivity")
				return;
			if (!drawLineStatus) {
				this.style.left = event.x - drag_icon_x;
				this.style.top = event.y - drag_icon_y;
				// this.icon.redrawLine();
			}
		}

		// 定义图片对象的托拽结束
		this.img.ondragend = function() {

			this.releaseCapture();
			// if (drawLineStatus && this.icon.type == "EndActivity")
			// return;
			if (drawLineStatus) {
				// 当在图元上方释放鼠标的时候，在两个节点之间画线
				// alert('drag end');
				this.icon.createLine(this, event.x, event.y);
			}
			// 可以拖动时，拖动图标后和图标连接的线需要重新绘制
			else {
				// alert('redrawline');
				this.icon.redrawLine();
			}
		}

		// 定义图片对象的右键菜单，并且将此图元对应的一些相关信息传给右键菜单对应的方法
		this.img.oncontextmenu = function() {
			curIcon = this.icon;

			showMenu('iconMenu');
			return false;
		}

		document.body.appendChild(this.text);
		this.text.style.display = "block";
		this.text.value = this.displayName;
		/*
		 * //onchage事件 this.text.onchange = function() {
		 * 
		 * this.icon.displayName = this.value;
		 * this.icon.activity.selectSingleNode("propertyList/property[@name='activityName']/value").text=this.value; }
		 */
		this.moveText();
	}

	function _create_line(obj, x, y) {

		// 查找线结束的图元
		var iconEnd = findIcon(x, y);
		if (iconEnd != null && iconEnd != this) {
			var linelength = lines.length;
			lines[linelength] = new Line();
			lines[linelength].init(this, iconEnd);

			// 在schema中添加connector节点
			schema.create_connector(lines[linelength]);

		}
	}

	function _redraw_line() {
		var i = 0;
		for (i = 0; i < this.beginLine.length; i++)
			this.beginLine[i].redraw();
		for (i = 0; i < this.endLine.length; i++)
			this.endLine[i].redraw();
		this.moveText();
	}

	function _move_text() {
		this.text.style.left = this.img.offsetLeft
				+ (half_image_size - charLength(this.displayName) * 5);
		this.text.style.top = this.img.offsetTop + half_image_size * 2 + 2;
		this.text.style.width = charLength(this.displayName) * 10;
	}

	function _set_displayName(value) {
		this.displayName = value;
		this.text.value = value;
	}
}

/**
 * 计算字符串的字节长度
 */
function charLength(str) {
	if (str == null || str == "")
		return 0;
	var totalCount = 0;
	for (i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) > 127)
			totalCount += 2;
		else
			totalCount++;
	}
	return totalCount;
}

/**
 * 根据坐标x,y计算当前坐标是否在某个图元上
 */
function findIcon(x, y) {
	for (var i = 0; i < icons.length; i++) {
		if (x > icons[i].img.offsetLeft && x < (icons[i].img.offsetLeft + 40)
				&& y > icons[i].img.offsetTop
				&& y < (icons[i].img.offsetTop + 40))
			return icons[i];
	}
	return null;
}

/**
 * 连线对象
 */
function Line() {
	this.iconBegin; // 连线开始图元对象
	this.iconEnd; // 连线结束图元对象
	this.begin; // 连线开始坐标（开始图元的中心点）
	this.end; // 连线结束坐标（结束图元的中心点）
	this.lineCode; // 连线上的编码
	this.lineName; // 连线上的编码
	this.displayName; // 连线上的文字
	this.color; // 连线颜色

	this.connector; // 连线对应的工作流的schema中的connector节点
	this.expconnector; // 连线扩展属性
	this.ruleList; // 用于存放规则表达式列表
	this.states; // 对应整个流程定义上的图元节点，目的是为了可以在线上选出所有节点

	// 连线图形对象
	this.lineImg = document.createElement("DIV");
	this.lineImg.style.display = "none";
	this.lineImg.line = this;
	document.body.appendChild(this.lineImg);

	// 连线中的文字对象
	this.text = document
			.createElement("<input type='text' readonly='true' style='position:absolute;background:transparent;border:0;font-size:9pt;text-align:center'>");
	this.text.style.display = "none";
	this.text.line = this;

	this.text.onchange = function() {
		this.line.lineName = this.value;
	}

	document.body.appendChild(this.text);
	this.points = new Array(); // 连线中的转折点对象

	this.init = _init_line; // 连线初始化方法
	this.show = _show_line; // 连线显示方法
	this.hidden_line = _hidden_line; // 连线隐藏方法

	this.draw = _draw; // 绘制连线方法
	this.drawLine = _draw_line; // 画线方法
	this.drawText = _draw_text; // 绘制连线文字
	this.redraw = _redraw; // 重新绘制连线方法
	this.setDisplayName = _set_displayName;
	this.startDrag = false; // 连线是否被托拽
	// this.lineImg.ondblclick = function () {
	// //alert(1);
	// delLine(this.line);
	// }
	// initiate line
	function _init_line(iconBegin, iconEnd, color) {
		this.iconBegin = iconBegin;
		this.iconEnd = iconEnd;
		this.begin = new Point(this.iconBegin.img.offsetLeft + half_image_size,
				this.iconBegin.img.offsetTop + half_image_size);
		this.end = new Point(this.iconEnd.img.offsetLeft + half_image_size,
				this.iconEnd.img.offsetTop + half_image_size);
		if (color != null)
			this.color = color;
		else
			this.color = "black";

		this.iconBegin.beginLine.push(this);
		this.iconEnd.endLine.push(this);
		this.draw();
		this.show();
	}

	function _draw() {

		var linePath = "";
		if (this.points.length <= 0) {

			if (this.begin.x == this.end.x && this.begin.y == this.end.y) {

				linePath = calSelfToSelf(this, this.begin);

			} else {
				var b = decreaseBegin(this.begin, this.end);
				var e = decrease(this.begin, this.end);
				linePath = b.x + "," + b.y + " " + e.x + "," + e.y;
			}
			this.drawLine(linePath);
		} else {
			var b = decreaseBegin(this.begin, this.points[0]);
			// this.drawLine(b.x, b.y, this.points[0].x, this.points[0].y);
			linePath = b.x + "," + b.y + " ";
			for (var i = 0; i < this.points.length; i++) {
				var p = this.points[i];
				linePath = linePath + " " + this.points[i].x + ","
						+ this.points[i].y;
				if ((i + 1) == this.points.length) {
					var e = decrease(p, this.end);
					linePath = linePath + " " + e.x + "," + e.y;

					this.drawLine(linePath);
					break;
				}
			}
		}

		this.drawText();
	}

	function _draw_text() {
		var sumX = 0;
		var sumY = 0;
		var computedTextPosition = false;
		var curBegin = new Point(this.begin.x, this.begin.y);
		var curEnd = new Point(this.end.x, this.end.y);
		if (this.points.length == 0) {
			curBegin = decreaseBegin(curBegin, curEnd);
			curEnd = decrease(curBegin, curEnd);
			sumX = parseInt((curBegin.x + curEnd.x) / 2);
			sumY = parseInt((curBegin.y + curEnd.y) / 2);
		} else {
			if ((this.points.length % 2) == 0) {
				var p = parseInt(this.points.length / 2) - 1;
				sumX = parseInt((this.points[p].x + this.points[p + 1].x) / 2);
				sumY = parseInt((this.points[p].y + this.points[p + 1].y) / 2);
			} else {
				var p = parseInt(this.points.length / 2);
				sumX = this.points[p].x;
				sumY = this.points[p].y;
			}
		}
		this.text.style.left = sumX - charLength(this.displayName) * 5;
		this.text.style.top = sumY;
		this.text.style.width = charLength(this.displayName) * 10;
	}

	function _redraw() {
		removeLine(this.lineImg);
		this.lineImg = document.createElement("DIV");
		this.lineImg.style.display = "none";
		this.lineImg.line = this;
		document.body.appendChild(this.lineImg);
		this.begin = new Point(this.iconBegin.img.offsetLeft + half_image_size,
				this.iconBegin.img.offsetTop + half_image_size);
		this.end = new Point(this.iconEnd.img.offsetLeft + half_image_size,
				this.iconEnd.img.offsetTop + half_image_size);
		this.draw();
		this.show();
	}

	function _show_line() {
		this.lineImg.style.display = "block";
		this.text.style.display = "block";
	}

	function _hidden_line() {
		this.lineImg.style.display = "none";
		this.text.style.display = "none";
	}

	function _set_displayName(code, name) {
		this.lineCode = code;
		this.lineName = name;
		this.displayName = name;
		this.text.value = name;
	}
}

/**
 * 画连线
 */
function _draw_line(p_linePath) {

	var lineStr = "<v:PolyLine filled='false' Points='" + p_linePath
			+ "' style='position:absolute;' ></v:PolyLine>";
	var line = document.createElement(lineStr);
	var stroke = document
			.createElement("<v:stroke  EndArrow='Classic' dashstyle='Solid' LineStyle='ThinThin'/>");

	line.insertBefore(stroke);
	line.line = this;

	line.oncontextmenu = function() {
		curLine = this.line;
		showMenu('lineMenu');
		return false;
	}
	line.onclick = function() {
		curLine = this.line;
	}
	this.lineImg.appendChild(line);
}

/**
 * 当开始图元和结束图元相同的时候，需要计算开始图元的起始节点和结束节点 算法就是通过计算原点偏离水平和数值分别为45度角的位置
 */
function calSelfToSelf(curLine, begin) {

	// 等边直角三角形的直角边长，斜边长为半径
	var v_len = parseInt(Math.sqrt(Math.pow(half_image_size, 2) / 2));

	var v_left_x = begin.x - 2 * half_image_size;
	var v_left_y = begin.y - 2 * half_image_size;

	var v_right_x = begin.x + 2 * half_image_size;
	var v_right_y = begin.y - 2 * half_image_size;

	var v_linePath = (begin.x - v_len) + "," + (begin.y - v_len) + " "
			+ v_left_x + "," + v_left_y + " " + v_right_x + "," + v_right_y
			+ " " + (begin.x + v_len) + "," + (begin.y - v_len);

	// 将结果保存在points中
	curLine.points.push(new Point(v_left_x, v_left_y));
	curLine.points.push(new Point(v_right_x, v_right_y));

	return v_linePath;
}

/**
 * 计算连线开始位置（因为连线的开始坐标在图元中，连线开始位置不能在图元中间，需要重新计算）
 */
function decreaseBegin(begin, end) {
	var len = half_image_size;
	var dx = end.x - begin.x;
	var dy = end.y - begin.y;
	var xIsLarge = false;
	var yIsLarge = false;
	if (dx < 0)
		xIsLarge = true;
	if (dy < 0)
		yIsLarge = true;
	if (dx == 0 && dy == 0)
		return begin;
	if (dx == 0)
		if (yIsLarge)
			return new Point(begin.x, begin.y - len);
		else
			return new Point(begin.x, begin.y + len);
	if (dy == 0)
		if (xIsLarge)
			return new Point(begin.x - len, begin.y);
		else
			return new Point(begin.x + len, begin.y);

	dx = Math.abs(dx);
	dy = Math.abs(dy);
	var decline = parseInt(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
	var _x = parseInt((len * dx) / decline);
	var _y = parseInt((len * dy) / decline);
	if (xIsLarge && yIsLarge)
		return new Point(begin.x - _x, begin.y - _y);
	if (xIsLarge)
		return new Point(begin.x - _x, begin.y + _y);
	if (yIsLarge)
		return new Point(begin.x + _x, begin.y - _y);
	else
		return new Point(begin.x + _x, begin.y + _y);
}

/**
 * 计算连线结束位置（因为连线的结束坐标在图元中，连线结束位置不能在图元中间，需要重新计算）
 */
function decrease(ptBegin, ptEnd) {
	var len = half_image_size;
	var dx = ptEnd.x - ptBegin.x;
	var dy = ptEnd.y - ptBegin.y;
	var xIsLarge = false;
	var yIsLarge = false;
	if (dx < 0)
		xIsLarge = true;
	if (dy < 0)
		yIsLarge = true;
	if (dx == 0 && dy == 0)
		return ptEnd;
	if (dx == 0)
		if (yIsLarge)
			return new Point(ptEnd.x, ptEnd.y + len);
		else
			return new Point(ptEnd.x, ptEnd.y - len);
	if (dy == 0)
		if (xIsLarge)
			return new Point(ptEnd.x + len, ptEnd.y);
		else
			return new Point(ptEnd.x - len, ptEnd.y);
	dx = Math.abs(dx);
	dy = Math.abs(dy);
	var decline = parseInt(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
	var _x = parseInt((len * dx) / decline);
	var _y = parseInt((len * dy) / decline);
	if (xIsLarge && yIsLarge)
		return new Point(ptEnd.x + _x, ptEnd.y + _y);
	if (xIsLarge)
		return new Point(ptEnd.x + _x, ptEnd.y - _y);
	if (yIsLarge)
		return new Point(ptEnd.x - _x, ptEnd.y + _y);
	else
		return new Point(ptEnd.x - _x, ptEnd.y - _y);
}

function removeAllChild(obj) {
	for (var i = obj.childNodes.length - 1; i >= 0; i--)
		obj.removeChild(obj.childNodes[i]);
}

// 删除线
function removeLine(obj) {

	obj.line = null;
	obj.ondblclick = null;
	obj.oncontextmenu = null;
	obj.onclick = null;
	document.body.removeChild(obj);
}

// 删除图元
function removeIcon(obj) {
	obj.icon = null;
	obj.ondragstart = null;
	obj.ondrag = null;
	obj.ondragend = null;
	obj.oncontextmenu = null;
	document.body.removeChild(obj);
}

// 删除文本
function removeText(obj) {
	obj.line = null;
	obj.icon = null;
	obj.onchange = null;
	document.body.removeChild(obj);
}