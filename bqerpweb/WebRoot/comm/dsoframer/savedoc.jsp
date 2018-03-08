<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="power.ear.comm.Employee,power.ear.comm.ejb.PageObject"%>
<%@ page language="java"
	import="java.io.*,java.util.*,power.ear.comm.ejb.Ejb3Factory,com.jspsmart.upload.*,power.ejb.manage.contract.business.*"%>
 
<%
	 Employee employee = (Employee) session.getAttribute("employee");
	// 初始化上传组件
	SmartUpload mySmartUpload = new SmartUpload();
	//mySmartUpload.initialize(config,request,response);
	mySmartUpload.initialize(pageContext); 
	mySmartUpload.upload();//.upload(); 
	
	String id = mySmartUpload.getRequest().getParameter("id");
	String type =  mySmartUpload.getRequest().getParameter("type");
	com.jspsmart.upload.File myFile = null;
	
	Files files = mySmartUpload.getFiles();
	for(int i=0;i<files.getCount();i++)
	{
	    com.jspsmart.upload.File f = files.getFile(i);
		System.out.println(f.getFieldName() +"|"+ f.getSize());
	}
	
	myFile = mySmartUpload.getFiles().getFile(0);
	String filePath = myFile.getFileName();
	if (!myFile.isMissing()) {
		myFile.saveAs(filePath, mySmartUpload.SAVE_PHYSICAL);
		java.io.File tfile = new java.io.File(filePath);
		PrintWriter pw = response.getWriter();
		InputStream inputStream = null;
		ByteArrayOutputStream outStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					tfile), 16 * 1024);
			outStream = new ByteArrayOutputStream(16 * 1024);
			byte[] buffer = new byte[16 * 1024];
			int n;
			while ((n = inputStream.read(buffer)) != -1)
				outStream.write(buffer, 0, n);
			byte[] data = outStream.toByteArray();
			if(data != null && data.length>0)
			{
				Ejb3Factory factory = Ejb3Factory.getInstance();
				ConJConDocFacadeRemote docremote = (ConJConDocFacadeRemote) factory
						.getFacadeRemote("ConJConDocFacade");
				ConJConDoc entity = null;
			    if("CON".equals(type))
			    {     
			         entity =docremote.findConDocModel(employee.getEnterpriseCode(),Long.parseLong(id),type);
			    }
			    else
			    {
				   entity = docremote.findById(Long.parseLong(id));
				}
				entity.setDocContent(data);
				docremote.update(entity);
			}
			pw.write("success");
		} catch (Exception exc) {
			exc.printStackTrace();
			pw.write("failure");
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (outStream != null)
				outStream.close();
			pw.close();
		}
	}
%>
