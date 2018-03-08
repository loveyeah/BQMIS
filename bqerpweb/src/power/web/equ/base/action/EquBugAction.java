package power.web.equ.base.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.equ.base.EquCBug;
import power.ejb.equ.base.EquCBugFacadeRemote;
import power.ejb.equ.base.EquCBugreason;
import power.ejb.equ.base.EquCBugreasonFacadeRemote;
import power.ejb.equ.base.EquCBugsolutionFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.web.comm.AbstractAction;

public class EquBugAction extends AbstractAction{

	private  EquCBugFacadeRemote remote;
	private EquCBug equCBug;
	private String bugId;
//	private String pBugId;
	public String getBugId() {
		return bugId;
	}
	public void setBugId(String bugId) {
		this.bugId = bugId;
	}
	public EquCBug getEquCBug() {
		return equCBug;
	}
	public void setEquCBug(EquCBug equCBug) {
		this.equCBug = equCBug;
	}
//	public String getPBugId() {
//		return pBugId;
//	}
//	public void setPBugId(String bugId) {
//		pBugId = bugId;
//	}
	
	public EquBugAction()
	{
		remote=(EquCBugFacadeRemote) factory.getFacadeRemote("EquCBugFacade");
	}
	
	/**
	 * 根据id获得故障树（故障维护时）
	 * @throws JSONException 
	 */
	public void getBugTreeList() throws JSONException
	{
		String id="";
		id=request.getParameter("id");
		
		List<EquCBug> list=remote.getListByParent(Long.parseLong(id),employee.getEnterpriseCode());

		List<TreeNode> arrayList=new ArrayList();
		if(list!=null)
		{
		for(int i=0;i<list.size();i++)
		{
			EquCBug bugModel=list.get(i);
			TreeNode model=new TreeNode();
			model.setId(bugModel.getBugId().toString());
			model.setText(bugModel.getBugName());
			boolean isLeaf = (bugModel.getIfLeaf().equals("1")) ? true : false;
			model.setLeaf(isLeaf);
			arrayList.add(model);
		}
		}
		write(JSONUtil.serialize(arrayList));
	}
	
	/**
	 * 根据编码获得故障树（用做故障选择时）
	 * @throws JSONException 
	 */
	public void getBugTreeByCode() throws JSONException
	{
		String code="";
		String id="";
		code=request.getParameter("id");
		List<TreeNode> arrayList=new ArrayList();

		if(code.equals("root"))
		{
			id="0";
		}
		else
		{
		   EquCBug equmodel=remote.findByCode(code,employee.getEnterpriseCode());
		   if(equmodel!=null)
		    {
		     id=equmodel.getBugId().toString();
		   }
		}
		if(!id.equals(""))
		{
		List<EquCBug> list=remote.getListByParent(Long.parseLong(id),employee.getEnterpriseCode());
		
		if(list!=null)
		{
			for(int i=0;i<list.size();i++)
			{
				EquCBug bugModel=list.get(i);
				TreeNode model=new TreeNode();
				model.setId(bugModel.getBugCode());
				model.setText(bugModel.getBugName());
				boolean isLeaf = (bugModel.getIfLeaf().equals("1")) ? true : false;
				model.setLeaf(isLeaf);
				arrayList.add(model);
			}
		}

		}
		

		write(JSONUtil.serialize(arrayList));
	}
	
	/**
	 * 通过id查询故障信息
	 * @throws JSONException
	 */
	public void findBugById() throws JSONException
	{
		HrJEmpInfoFacadeRemote empRemote=(HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
		String id="";
		id=request.getParameter("id");
		EquCBug model=remote.findById(Long.parseLong(id));
		 String str=JSONUtil.serialize(model);
		 String workName="";
		List<HrJEmpInfo> list= empRemote.findByEmpCode(model.getEntryBy());
		if(list!=null&&list.size()>0)
		{
			workName=list.get(0).getChsName();
		}
		 write("{success: true,workName:'"+workName+"',data:"+str+"}");
		
	}
	
	/**
	 * 增加或修改设备故障信息
	 * @throws ParseException
	 */
	public void addOrUpdateBugInfo() throws ParseException
	{
		String method=request.getParameter("method");
		if(method.equals("add"))
		{
			addBugInfo();
		}
		if(method.equals("update"))
		{
			updateBugInfo();
		}
	}
	
	/**
	 * 增加故障信息
	 * @throws ParseException
	 */
	public void addBugInfo() throws ParseException
	{
		equCBug.setEnterpriseCode(employee.getEnterpriseCode());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   String mydate = sf.format(new java.util.Date());
		   equCBug.setEntryDate(sf.parse(mydate));
		   equCBug.setEntryBy("999999");
	  int i= remote.save(equCBug);
		if(i != -1)
		{
			write("{success:true,id:'"+i+"',msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,id:'-1',msg:'增加失败:编码重复！'}");
		}
	}
	
	/**
	 * 修改故障信息
	 * @throws ParseException
	 */
	public void updateBugInfo() throws ParseException
	{
		EquCBug model=new EquCBug();
		model=remote.findById(Long.parseLong(bugId));
		model.setBugDesc(equCBug.getBugDesc());
		model.setBugName(equCBug.getBugName());
		model.setEntryBy(employee.getWorkerCode());
		model.setIfLeaf(equCBug.getIfLeaf());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate = sf.format(new java.util.Date());
	    model.setEntryDate(sf.parse(mydate));
		 if(remote.update(model))
		 {
			 write("{success:true,msg:'修改成功！'}");
		 }
		 else
		 {
			 write("{success:true,msg:'修改失败:编码重复！'}");
		 }
		
	}
	
	/**
	 * 删除故障信息
	 */
	public void deleteBugInfo()
	{
		String ids=request.getParameter("id");
		 String [] bugIds= ids.split(",");
		 for(int j=0;j<bugIds.length;j++)
		 {
			 
		 
		EquCBug model=remote.findById(Long.parseLong(bugIds[j]));
		EquCBugreasonFacadeRemote reasonRemote=(EquCBugreasonFacadeRemote)factory.getFacadeRemote("EquCBugreasonFacade");
		EquCBugsolutionFacadeRemote solutionRemote=(EquCBugsolutionFacadeRemote) factory.getFacadeRemote("EquCBugsolutionFacade");
		
		if(remote.delete(Long.parseLong(bugIds[j])))
		{
			 PageObject obj=reasonRemote.findBugReasonList("%", model.getBugCode(), employee.getEnterpriseCode());
			 List<EquCBugreason> list=obj.getList();
			 if(list!=null)
			 {
				 for(int i=0;i<list.size();i++)
				 {
					 EquCBugreason entity=list.get(i);
					// 删除故障对应的所有解决方案
					 solutionRemote.deleteAllByReasonId(entity.getBugReasonId(), employee.getEnterpriseCode());
				 }
			 }
			 //删除故障对应的所有原因
			 reasonRemote.deleteReasonByBugCode(model.getBugCode(), employee.getEnterpriseCode());
			write("{success:true,msg:'删除成功！'}");
		}
		else
		{
			write("{success:true,msg:'有子节点存在，不能删除！'}");
		}
		 }
	}
	
	/**
	 * 通过编码查找一条故障信息
	 * @throws JSONException
	 */
	public void findBugByCode() throws JSONException
	{
		HrJEmpInfoFacadeRemote empRemote=(HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
		String code="";
		String workName="";
		code=request.getParameter("code");
		EquCBug model=remote.findByCode(code, employee.getEnterpriseCode());
		 String str=JSONUtil.serialize(model);
			List<HrJEmpInfo> list= empRemote.findByEmpCode(model.getEntryBy());
			if(list!=null&&list.size()>0)
			{
				workName=list.get(0).getChsName();
			}
		 write("{success: true,workName:'"+workName+"',data:"+str+"}");
	}
	
	/**
	 * 通过名称查询故障列表
	 * @throws JSONException
	 */
	public void findBugListByName() throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
		      obj=remote.findListByName(enterpriseCode, fuzzy, start,limit);
	    }
	    else
	    {
	    	obj=remote.findListByName(enterpriseCode, fuzzy);
	    }
		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	
	
	
	
}
