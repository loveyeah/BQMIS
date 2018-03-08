package power.web.equ.base.action;

import java.io.File;
import java.io.UnsupportedEncodingException;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBugsolution;
import power.ejb.equ.base.EquCBugsolutionFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
@SuppressWarnings("serial")
public class EquBugSolutionAction extends UploadFileAbstractAction{
	
	private EquCBugsolutionFacadeRemote remote;
	private EquCBugsolution bugSolution;
	private String equSolutionId;
	private File solutionFile;
	public EquCBugsolution getBugSolution() {
		return bugSolution;
	}
	public void setBugSolution(EquCBugsolution bugSolution) {
		this.bugSolution = bugSolution;
	}

	
	public String getEquSolutionId() {
		return equSolutionId;
	}
	public void setEquSolutionId(String equSolutionId) {
		this.equSolutionId = equSolutionId;
	}
	
	/**
	 * 构造函数
	 */
	public EquBugSolutionAction()
	{
		remote=(EquCBugsolutionFacadeRemote) factory.getFacadeRemote("EquCBugsolutionFacade");
	}
	
	/**
	 * 获得某故障原因对应的故障解决方案
	 * @throws JSONException
	 */
	public void findBugSolutionList()
	{ 
		try {
			String fuzzy = request.getParameter("fuzzy");
			String reasonId = request.getParameter("reasonId");
			String enterpriseCode = employee.getEnterpriseCode();
			String objstart = request.getParameter("start");
			String objlimit = request.getParameter("limit");
			int start = 0;
			int limit = 18; 
			if (objstart != null && objlimit != null) {
				start = Integer.parseInt(request.getParameter("start"));
				limit = Integer.parseInt(request.getParameter("limit")); 
			}  
			PageObject result = remote.findBugSolutionList(fuzzy, reasonId,
					enterpriseCode, start, limit);
			if(result != null && result.getTotalCount()>0)
			{
				write(JSONUtil.serialize(result));
			}
			else
			{
				write("{}");
			}

//			List<EquCBugsolution> list = obj.getList();
//			StringBuffer JSONStr = new StringBuffer();
//			JSONStr.append("{list:[");
//			String fileName = "";
//			String fileView = "";
//			for (int i = 0; i < list.size(); i++) {
//				fileView = "";
//				fileName = "";
//				EquCBugsolution model = list.get(i);
//				if (model.getFilePath() != null
//						&& !model.getFilePath().equals("")) {
//					// File f = new File(model.getFilePath());
//					// if(f.exists())
//					// {
//					fileName = model.getFileName();
//					fileView = model.getFilePath();// calluploadPath+"/equ/"+fileName;
//					// }
//				}
//				JSONStr.append("{equSolutionId:" + model.getEquSolutionId()
//						+ ",equSolutionDesc:'" + model.getEquSolutionDesc()
//						+ "',memo:'" + model.getMemo() + "',fileName:'"
//						+ fileName + "',fileView:'" + fileView + "'},");
//			}
//			if (JSONStr.length() > 7) {
//				JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
//			}
//
//			JSONStr.append("],totalCount:" + obj.getTotalCount() + "}");
//			write(JSONStr.toString());
		}catch(Exception exc)
		{
			exc.printStackTrace();
			write("[]");
		}
		
		
	} 
	/**
	 * 增加某原因的解决方案
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public void addSolution() throws UnsupportedEncodingException 
	{ 
//		String filePath=new String(request.getParameter("filepath").getBytes("iso-8859-1"),("gb2312"));
		String filePath= request.getParameter("filepath"); 
		String reasonId= request.getParameter("reasonId");  
		if (!filePath.equals("")) {  
			String fileName =filePath.substring(filePath.lastIndexOf("\\")+1);
			String calluploadPath = uploadFile(solutionFile,fileName,"equ"); 
			bugSolution.setFilePath(calluploadPath); 
			bugSolution.setFileName(fileName);
		} 
		bugSolution.setBugReasonId(Long.parseLong(reasonId));
		bugSolution.setEnterpriseCode(employee.getEnterpriseCode());
		bugSolution.setEntryBy(employee.getWorkerCode());
		bugSolution.setEntryDate(new java.util.Date());
		
		remote.save(bugSolution);
		write("{success:true,msg:'增加成功！'}");
	
	}  
	   /**
		 * 修改故障解决方案
		 * 
		 * @throws UnsupportedEncodingException
		 */
	public void updateSolution() throws UnsupportedEncodingException
	{
		EquCBugsolution model=remote.findById(Long.parseLong(equSolutionId));
		String filePath= request.getParameter("filepath");  
		if (!filePath.equals("")) {  
			String fileName =filePath.substring(filePath.lastIndexOf("\\")+1);
			String calluploadPath = uploadFile(solutionFile,fileName,"equ"); 
			model.setFilePath(calluploadPath); 
			model.setFileName(fileName);
		} 
		model.setEntryBy(employee.getWorkerCode());
		model.setEntryDate(new java.util.Date());
		model.setEquSolutionDesc(bugSolution.getEquSolutionDesc()); 
		model.setMemo(bugSolution.getMemo()); 
		remote.update(model); 
		write("{success:true,msg:'修改成功！'}");
	}
	
	/**
	 * 删除故障解决方案
	 */
	public void deleteSolution()
	{
		String ids= request.getParameter("ids");
	     String [] solutionids= ids.split(",");
			for(int i=0;i<solutionids.length;i++)
			{
			  remote.delete(Long.parseLong(solutionids[i]));	
			}
			String	str = "{success: true,msg:'删除成功'}";
		    write(str);
	}
	public File getSolutionFile() {
		return solutionFile;
	}
	public void setSolutionFile(File solutionFile) {
		this.solutionFile = solutionFile;
	}
	

}
