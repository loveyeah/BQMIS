package power.web.manage.project.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.File;//add by ypan 20100909
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.UploadFileAbstractAction;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.project.PrjJRegister;
import power.ejb.manage.project.PrjJRegisterFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class  ProjectRegisterAction  extends UploadFileAbstractAction
{

	
	private  PrjJRegisterFacadeRemote  remote;
	//add by ypan 20100909
    private File solutionFile;
	
	public File getSolutionFile() {
		return solutionFile;
	}

	public void setSolutionFile(File solutionFile) {
		this.solutionFile = solutionFile;
	}
	public ProjectRegisterAction()
	{
		remote = (PrjJRegisterFacadeRemote) factory.getFacadeRemote("PrjJRegisterFacade");
	}
	
	
	
	//add by ypan 20100909
	public void extraCommit(){
		String filePath= request.getParameter("filepath"); 
		String prjId= request.getParameter("prjId");  
//		System.out.println(prjId);
		if (!filePath.equals("")) {  
			String fileName =filePath.substring(filePath.lastIndexOf("\\")+1);
			String calluploadPath = uploadFile(solutionFile,fileName,"inquire");
			if(prjId != null && !prjId.equals("")){
				PrjJRegister entity = remote.findById(Long.parseLong(prjId));
				entity.setAnnex(calluploadPath);
				remote.update(entity);
				write("{success:true,msg:'增加附件成功！'}");
			}
		} 
	}
	
	public void  getPrjRegister() throws JSONException
	{
		Object str= request.getParameter("start");
		Object lim= request.getParameter("limit");
		int start=0;
		int limit=0;
		
		PageObject obj=new PageObject();
		String year = request.getParameter("year");
		String prjType = request.getParameter("prjType");
		String isFunds = request.getParameter("isFunds");
		String prjName = request.getParameter("prjName");
		String flag=request.getParameter("flag");
		if(str!=null&&lim!=null)
		{
			start=Integer.parseInt(str.toString());
			limit=Integer.parseInt(lim.toString());
			if (flag!=null&&"register".equals(flag)) {
				obj=remote.getPrjRegister(prjName,year, prjType, isFunds, employee.getEnterpriseCode(),employee.getWorkerCode(),start,limit);
			}else if (flag!=null&&"query".equals(flag)) {
				obj=remote.getPrjRegister(prjName,year, prjType, isFunds, employee.getEnterpriseCode(),null,start,limit);
			}
			
		}else
		{
			if (flag!=null&&"register".equals(flag)) {
				obj=remote.getPrjRegister(prjName,year, prjType, isFunds, employee.getEnterpriseCode(),employee.getWorkerCode());
			}else if (flag!=null&&"query".equals(flag)) {
				obj=remote.getPrjRegister(prjName,year, prjType, isFunds, employee.getEnterpriseCode(),null);
			}
		}
		
		if (obj != null) {
			write(JSONUtil.serialize(obj));
			
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}
	public void delPrjRegister()
	{
		String ids = request.getParameter("ids");
		remote.delPrjRegister(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	public void savePrjRegister() throws JSONException
	{
		String str = request.getParameter("isUpdate");
        Object object=new Object();	
        if(str!=null&&!"".equals(str))
        {
        	object = JSONUtil.deserialize(str);
        }
		 
	
		List<Map> list = (List<Map>) object;
		List<PrjJRegister> addList = null;
		List<PrjJRegister> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<PrjJRegister>();
			updateList = new ArrayList<PrjJRegister>();
			for (Map data : list) {
				String prjId = null;
				String prjNo = null;
				String prjName = null;
				String prjDept = null;
				String prjBy = null;
				String prjTypeName = null;
				String prjTypeId=null;
				String applyFunds = null;
				String approvedFunds = null;
				String isFundsFinish = null;
				String prjYear = null;
				String duration = null;

				if (data.get("prjId") != null&&!"".equals(data.get("prjId")))
					prjId = data.get("prjId").toString();
				if (data.get("prjNo") != null&&!"".equals(data.get("prjNo")))
					prjNo = data.get("prjNo").toString();

				if (data.get("prjName") != null&&!"".equals(data.get("prjName")))
					prjName = data.get("prjName").toString();
				if (data.get("prjDept") != null&&!"".equals(data.get("prjDept")))
					prjDept = data.get("prjDept").toString();
				if (data.get("prjBy") != null&&!"".equals(data.get("prjBy")))
					prjBy = data.get("prjBy").toString();
				
				if (data.get("prjTypeId") != null&&!"".equals(data.get("prjTypeId")))
					prjTypeId = data.get("prjTypeId").toString();
				if (data.get("prjTypeName") != null&&!"".equals(data.get("prjTypeName")))
					prjTypeName = data.get("prjTypeName").toString();
				if (data.get("applyFunds") != null&&!"".equals(data.get("applyFunds")))
					applyFunds = data.get("applyFunds").toString();
				if (data.get("approvedFunds") != null&&!"".equals(data.get("approvedFunds")))
					approvedFunds = data.get("approvedFunds").toString();
				if (data.get("isFundsFinish") != null&&!"".equals(data.get("isFundsFinish")))
					isFundsFinish = data.get("isFundsFinish").toString();
				if (data.get("prjYear") != null&&!"".equals(data.get("prjYear")))
					prjYear = data.get("prjYear").toString();
				if (data.get("duration") != null&&!"".equals(data.get("duration")))
					duration = data.get("duration").toString();

				PrjJRegister model = new PrjJRegister();
				if (prjId == null) {
					if (prjNo != null )
					   model.setPrjNo(prjNo);
					if (prjName != null )
						model.setPrjName(prjName);
					if (prjDept != null)
						model.setPrjDept(prjDept);
					if (prjBy != null)
						model.setPrjBy(prjBy);
					if (prjTypeId != null) {
						model.setPrjTypeId(Long.parseLong(prjTypeId));
					}
					if (applyFunds != null) {
				    model.setApplyFunds(Double.parseDouble(applyFunds));
					}
					if (approvedFunds != null) {
					    model.setApprovedFunds((Double.parseDouble(approvedFunds)));
					}
					if (isFundsFinish != null) {
					    model.setIsFundsFinish(isFundsFinish);
						}
					if (prjYear != null) {
					    model.setPrjYear(prjYear);
						}
					if (duration != null) {
					    model.setDuration(duration);
						}
					model.setEntryBy(employee.getWorkerCode());
					model.setEntryDate(new Date());
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remote.findById(Long.parseLong(prjId));
					if (prjNo != null )
						   model.setPrjNo(prjNo);
						if (prjName != null )
							model.setPrjName(prjName);
						if (prjDept != null)
							model.setPrjDept(prjDept);
						if (prjBy != null)
							model.setPrjBy(prjBy);
						if (prjTypeId != null) {
							model.setPrjTypeId(Long.parseLong(prjTypeId));
						}
						
						/*if (prjTypeName != null) {
							model.setPrjTypeId(Long.parseLong(prjTypeName));
						}*/
						if (applyFunds != null) {
					    model.setApplyFunds(Double.parseDouble(applyFunds));
						}
						if (approvedFunds != null) {
						    model.setApprovedFunds((Double.parseDouble(approvedFunds)));
						}
						if (isFundsFinish != null) {
						    model.setIsFundsFinish(isFundsFinish);
							}
						if (prjYear != null) {
						    model.setPrjYear(prjYear);
							}
						if (duration != null) {
						    model.setDuration(duration);
							}
						model.setEntryBy(employee.getWorkerCode());
						model.setEntryDate(new Date());
						model.setEnterpriseCode(employee.getEnterpriseCode());
						updateList.add(model);
		}
			}
			remote.savePrjRegister(addList, updateList);
			write("{success:true,msg:'操作成功！'}");

			}
		}
	
}

	
	


