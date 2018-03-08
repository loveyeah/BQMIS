<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>选择专业测试</title>
	<script >
		function chooseSpecialsSingle()
		{
			var args = {selectModel:'single',rootNode:{id:'LC',text:'老厂'},specialType:'0'};
			var rvo = window.showModalDialog('specials.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{ 
				document.all.content.innerHTML ="专业编码:"+ rvo.ids+"<br/>专业名称:"+rvo.names;
			}
		}
		function chooseSpecialsMultiple()
		{
			var args = {selectModel:'multiple',rootNode:{id:'0',text:'专业树'},specialType:''};
			var rvo = window.showModalDialog('specials.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{  
				document.all.content.innerHTML ="专业编码:"+ rvo.ids+"<br/>专业名称:"+rvo.names;
			}
		} 
		function chooseSpecialsCascade()
		{
			var args = {selectModel:'cascade',rootNode:{id:'0',text:'专业树'},specialType:''};
			var rvo = window.showModalDialog('specials.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{  
				document.all.content.innerHTML ="专业编码:"+ rvo.ids+"<br/>专业名称:"+rvo.names;
			}
		 
		} 
		 
	</script>
</head>
<body>  
<input type="button" onclick="chooseSpecialsSingle();" value="单选专业(只选择老厂公用专业)" />

<hr/>
<input type="button" onclick="chooseSpecialsMultiple();" value="多选专业" />
<hr/>
<input type="button" onclick="chooseSpecialsCascade()" value="级连选专业" />
<hr/>
<div id="content"></div>
</body>
</html>