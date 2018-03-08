<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>选择人员测试</title>
	<script >
		function chooseWorkerSingle()
		{
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '大唐灞桥热电厂'
				}
			} 
			var rvo = window.showModalDialog('workerByDept.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{
				showReturnValue(args.selectModel,rvo);
			}
		}
		function chooseWorkerMultiple()
		{
			var args = {
				selectModel : 'multiple',
				notIn : "'999999','0010'",
				rootNode : {
					id : '2',
					text : '检修专业'
				}
			} 
			var rvo = window.showModalDialog('workerByDept.jsp', args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if(typeof(rvo) !="undefined")
			{
				showReturnValue(args.selectModel,rvo);
			}
		}
		
		function showReturnValue(selectModel,rvo)
		{
			if (selectModel == 'multiple') {
					document.all.content.innerHTML = 
						"所选人员工号:  "+rvo.codes+"</br>" +
						"所选人员名称:  "+rvo.names+"</br>" + 
						spellDetails(rvo.list);
						
				} else {
					document.all.content.innerHTML =  spellDetail(rvo);
				}
		}
		
		function spellDetails(list)
		{
			var html = "";
			for(var i=0;i<list.length;i++)
			{
				html += spellDetail(list[i]);
				html += "<hr/>";
			}
			return html;
		}
		
		function spellDetail(record)
		{
			return "工号:"+record.workerCode+"</br>" +
					"名称:"+record.workerName+"</br>" +
					"部门ID:"+record.deptId+"</br>" +
					"部门编码:"+record.deptCode+"</br>" +
					"部门名称:"+record.deptName+"</br>" ;
		} 
	</script>
</head>
<body>    
<input type="button" onclick="chooseWorkerSingle();" value="单选人员并将工号为999999的记录除去" />

<hr/>
<input type="button" onclick="chooseWorkerMultiple();" value="多选人员并将工号为999999,0010的记录除去(只选择检修专业下的人员)" />
<hr/>
<div id="content"></div>
</body>
</html>