package power.web.manage.contract.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.io.OutputStream;
import java.io.FileInputStream;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.web.comm.AbstractAction;

public class ConDocModifyAction extends AbstractAction{
	private ConJConDocFacadeRemote remote; 
	private ConJConDoc conDoc;
	private File docFile;
	//附件id
	private Long conDocId;
	
	public Long getConDocId() {
		return conDocId;
	}
	public void setConDocId(Long conDocId) {
		this.conDocId = conDocId;
	}
	public ConJConDoc getConDoc() {
		return conDoc;
	}
	public void setConDoc(ConJConDoc conDoc) {
		this.conDoc = conDoc;
	}
	
	public ConDocModifyAction()
	{
		remote=(ConJConDocFacadeRemote)factory.getFacadeRemote("ConJConDocFacade");
	}
	
	public void findDocModifyList() throws JSONException
	{
		Long keyId = null;
		if(request.getParameter("modifyId") != null)
			keyId = Long.parseLong(request.getParameter("modifyId"));
//		String keyId=request.getParameter("modifyId");
		String docType=request.getParameter("docType");
		 PageObject obj=remote.findConDocList(employee.getEnterpriseCode(),keyId,docType,conDocId);
		 String str=JSONUtil.serialize(obj);
		 write(str);
	}
	
	public void addDocModifyInfo() throws IOException
	{
		String keyId=request.getParameter("modifyId");
		String fileName=request.getParameter("fileName");
		//toByteData(conDoc,fileName);
		java.io.FileInputStream fis = new java.io.FileInputStream(docFile);
		int index =fileName.lastIndexOf(".");
        int ssdex=fileName.lastIndexOf("\\");
		byte[] filedata = new byte[(int)fis.available()];
		
		conDoc.setOriFileExt(fileName.substring(index+1));
		conDoc.setOriFileName(fileName.substring(ssdex+1, index));
		//conDoc.setDocName(fileName.substring(ssdex+1, index));
		fis.read(filedata);
		conDoc.setDocContent(filedata);
		
		conDoc.setKeyId(Long.parseLong(keyId));
		conDoc.setLastModifiedBy(employee.getWorkerCode());
		conDoc.setLastModifiedDate(new Date());
		conDoc.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(conDoc);
		write("{success:true,msg:'增加成功！'}");
	}
	
	public void updateDocModifyInfo() throws IOException
	{
		String keyId=request.getParameter("modifyId");
		String fileName=request.getParameter("fileName");
		ConJConDoc model=remote.findById(conDoc.getConDocId());
		if(docFile != null)
		{
			//this.toByteData(model, fileName);
			java.io.FileInputStream fis = new java.io.FileInputStream(docFile);
			int index =fileName.lastIndexOf(".");
	        int ssdex=fileName.lastIndexOf("\\");
			byte[] filedata = new byte[(int)fis.available()];
			fis.read(filedata);
			model.setDocContent(filedata);
			
			model.setOriFileExt(fileName.substring(index+1));
			model.setOriFileName(fileName.substring(ssdex+1, index));
		//	model.setDocName(fileName.substring(ssdex+1, index));
		}
		model.setKeyId(Long.parseLong(keyId));
		model.setDocMemo(conDoc.getDocMemo());
		model.setDocName(conDoc.getDocName());
		model.setLastModifiedBy(employee.getWorkerCode());
		model.setLastModifiedDate(new Date());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	public void deleteDocModifyInfo()
	{
		String docIds=request.getParameter("ids");
		remote.deleteMulti(docIds);
		write("{success:true,msg:'删除成功！'}");
	}
	
//	private void toByteData(ConJConDoc model,String fileName) throws IOException
//	{
//		//String fileName=new String(request.getParameter("fileName").getBytes("iso-8859-1"),("gb2312"));
//		//String fileName = request.getParameter("fileName");
//		java.io.FileInputStream fis = new java.io.FileInputStream(docFile);
//		int index =fileName.lastIndexOf(".");
//        int ssdex=fileName.lastIndexOf("\\");
//		byte[] filedata = new byte[(int)fis.available()];
//		// File myfile=new File(docFile);
//		fis.read(filedata);
//		model.setDocContent(filedata);
//	 //   model.setDocName(docFile.getName());
////		model.setOriFileName(docFile.getName().substring(0,docFile.getName().lastIndexOf(".")));
////		model.setOriFileExt(docFile.getName().substring(docFile.getName().lastIndexOf(".")+1));
//		//model.setDocType(model.getOriFileExt());
//		model.setOriFileExt(fileName.substring(index+1));
//		model.setOriFileName(fileName.substring(ssdex+1, index));
//		model.setDocName(fileName.substring(ssdex+1, index));
//     
//	}
//	
	public void typeOfFile() throws Exception
	{
		long docID = 3;
		conDoc = remote.findById(docID);
		String sFormart = conDoc.getOriFileExt();
		write(sFormart);
	}
	
	public void openContractDoc() throws IOException
	{
		long docID = 3;
		
//		Object obj = request.getParameter("id");
//		docID = Long.parseLong(obj.toString());
		conDoc = remote.findById(docID);
		String sFormart = conDoc.getOriFileExt();
		//由sFormart控制格式
		//txt: text/doc;charset=utf-8
		//doc:application/ms-doc;charset=utf-8
		//excel:application/ms-excel;charset=utf-8
		if(sFormart == "txt")
		{
			sFormart = "text/doc;charset=utf-8";
		}
		else if(sFormart == "doc")
		{
			sFormart = "application/ms-doc;charset=utf-8";
		}
		else if(sFormart == "excel")
		{
			sFormart = "application/ms-excel;charset=utf-8";
		}
		if(conDoc != null)
		{
			byte[] data = conDoc.getDocContent();
			response.setContentType(sFormart);
			OutputStream outs = response.getOutputStream();
			for(int i = 0;i < data.length;i ++)
			{
				outs.write(data[i]);
			}
			outs.close();
		}
//		write("test");
		//异常暂时不考虑
	}
   public void svaeDocContent() throws Exception {
	   long docID = 3;
		
//		Object obj = request.getParameter("id");
//		docID = Long.parseLong(obj.toString());
		conDoc = remote.findById(docID);
		String sFormart = conDoc.getOriFileExt();
		//由sFormart控制格式
		//txt: text/doc;charset=utf-8
		//doc:application/ms-doc;charset=utf-8
		//excel:application/ms-excel;charset=utf-8
		if(sFormart == "txt")
		{
			sFormart = "text/doc;charset=utf-8";
		}
		else if(sFormart == "doc")
		{
			sFormart = "application/ms-doc;charset=utf-8";
		}
		else if(sFormart == "xls")
		{
			sFormart = "application/ms-excel;charset=utf-8";
		}
	   FileInputStream fis = new FileInputStream("c:/myDoc2." + conDoc.getOriFileExt());
	   byte[] data = new byte[(int)fis.available()];
	   
	   fis.read(data);
	   conDoc = remote.findById(Long.parseLong("3"));
	   if(conDoc == null)
	   {
		   
	   }
	   else
	   {
		   conDoc.setDocContent(data);
		   remote.update(conDoc);
	   }
   }
public File getDocFile() {
	return docFile;
}
public void setDocFile(File docFile) {
	this.docFile = docFile;
}
}
