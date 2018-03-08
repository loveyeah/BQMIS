<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
</head>
<body>
	<script language="javascript">
		var sFile;
		Ext.onReady(function() {
			Ext.Ajax.request({
				url:'managecontract/typeOfFile.action',
				method:'post',
				success : function(result,request){
					sFile = result.responseText;
				}
			});
			Ext.Ajax.request({
				url : 'managecontract/openContractDoc.action',
				method : 'post',
				success : function(result, request) {
					var fso,f,r;
					var ForReading=1,ForWriting=2;
					fso = new ActiveXObject("Scripting.FileSystemObject");
					alert(sFile);  
					f=fso.OpenTextFile("c:\\myDoc2." + sFile,ForWriting,true);
					//alert(result.responseText);
					//alert(1);
					f.Write(result.responseText);
					f.Close();
					//f = fso.OpenTextFile("c:\\myDoc2.doc",ForReading);
					//alert(f.ReadAll());
					//window.open("E:\\docs\\infra.txt");
					//var doc = new ActiveXObject("Msxml2.DOMDocument");     
 				    //doc.async = false;   
                    //doc.load("c:\\myDoc2.doc");   
                    //window.local.href = "c:\\myDoc2.doc";
                    //var Excel = new ActiveXObject("Word.Application");   
					//Excel.Visible=true;   
					//Excel.Open("file:\\c:/myDoc2.doc");   
					var wsh = new ActiveXObject("wscript.shell");
					wsh.run("c:\\myDoc2." + sFile);
					return(r);
				}
			});
		});	
		var open = new Ext.Button({
			text:'打开'
		});
		open.on('click',openDoc);
		function openDoc()
		{
			Ext.Ajax.request({
				url : 'managecontract/openContractDoc.action',
				method : 'post',
				success : function(result, request) {
					var fso,f,r;
					var ForReading=1,ForWriting=2;
					fso = new ActiveXObject("Scripting.FileSystemObject")  
					f=fso.OpenTextFile("c:\\myDoc2.doc",ForWriting,true);
					f.Write(result.responseText);
					f.Close();
					//f = fso.OpenTextFile("c:\\myDoc2.doc",ForReading);
					//alert(f.ReadAll());
					//window.open("E:\\docs\\infra.txt");
					//var doc = new ActiveXObject("Msxml2.DOMDocument");     
 				    //doc.async = false;   
                    //doc.load("c:\\myDoc2.doc");   
                    //window.local.href = "c:\\myDoc2.doc";
                    //var Excel = new ActiveXObject("Word.Application");   
					//Excel.Visible=true;   
					//Excel.Open("file:\\c:/myDoc2.doc");   
					var wsh = new ActiveXObject("wscript.shell");
					wsh.run("c:\\myDoc2.doc");
					return(r);
				}
			});
		}
		
		var save = new Ext.Button({
			text:'保存'
		});
		save.on('click',saveDoc);
		function saveDoc()
		{
			Ext.Ajax.request({
				url:'managecontract/svaeDocContent.action',
				method:'post',
				success:function(result,request){}
			});	
		}
		new Ext.Viewport({
		layout : "border",
		items : [open,save]
		});
	</script>

</body>
</html>