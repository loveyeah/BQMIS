<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>选择部门测试</title>
	<script >
		function chooseWorkerSingle()
		{
			var args = {selectModel:'single',rootNode:{id:'2',text:'检修部门'}};
			var rvo = window.showModalDialog('dept.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{ 
				document.all.content.innerHTML ="部门ID:"+ rvo.ids +"<br/>部门编码:"+ rvo.codes +"<br/>部门名称:"+rvo.names;
			}
		}
		function chooseWorkerMultiple()
		{
			var args = {selectModel:'multiple',rootNode:{id:'0',text:'漕泾电厂'}};
			var rvo = window.showModalDialog('dept.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{  
				document.all.content.innerHTML ="部门ID:"+ rvo.ids+"<br/>部门编码:"+ rvo.codes +"<br/>部门名称:"+rvo.names;
			}
		} 
		function chooseWorkerCascade()
		{
			var args = {selectModel:'cascade',rootNode:{id:'0',text:'漕泾电厂'}};
			var rvo = window.showModalDialog('dept.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{  
				document.all.content.innerHTML ="部门ID:"+ rvo.ids+"<br/>部门编码:"+ rvo.codes +"<br/>部门名称:"+rvo.names;
			}
		 
		} 
		function chooseLeafNodeOnly()
		{
			var args = {selectModel:'single',rootNode:{id:'0',text:'漕泾电厂'},onlyLeaf:true};
			var rvo = window.showModalDialog('dept.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{  
				document.all.content.innerHTML ="部门ID:"+ rvo.ids+"<br/>部门编码:"+ rvo.codes +"<br/>部门名称:"+rvo.names;
			}
		}
		 
	</script>
</head>
<body>  
<input type="button" onclick="chooseWorkerSingle();" value="单选部门(只选择检修专业下部门)" />

<hr/>
<input type="button" onclick="chooseWorkerMultiple();" value="多选部门" />
<hr/>
<input type="button" onclick="chooseWorkerCascade()" value="级连选部门" />
<hr/>
<input type="button" onclick="chooseLeafNodeOnly()" value="只能选择叶子部门(单选)" />
<hr/>
<div id="content"></div>
</body>
</html>