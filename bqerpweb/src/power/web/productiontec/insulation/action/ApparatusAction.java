package power.web.productiontec.insulation.action;

import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.insulation.PtJyjdJYqybtzlh;
import power.ejb.productiontec.insulation.PtJyjdJYqybtzlhFacadeRemote;
import power.ejb.run.securityproduction.SpJSpecialoperators;
import power.ejb.run.securityproduction.SpJSpecialoperatorsFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 绝缘仪器仪表台帐
 * @author liuyi 090706
 *
 */
public class ApparatusAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1456564465464L;
	private PtJyjdJYqybtzlh pjj;
	private PtJyjdJYqybtzlhFacadeRemote remote; 
	
	public ApparatusAction()
	{
		remote = (PtJyjdJYqybtzlhFacadeRemote)factory.getFacadeRemote("PtJyjdJYqybtzlhFacade");
	}
	
	/**
	 * 增加一条绝缘仪器仪表信息
	 */
	public void addPtJyjdJYqybtzlh()
	{
		
		pjj.setEnterpriseCode(employee.getEnterpriseCode());
		pjj = remote.save(pjj);
		if(pjj == null)
		{
			write("{failure:true,msg:'该绝缘仪器仪表名称已存在！'}");
		}
		else
		write("{success:true,id:'"+pjj.getRegulatorId()+"',msg:'数据增加成功！'}");
	}
	
	/**
	 * 修改一条绝缘仪器仪表信息
	 */
	public void updatePtJyjdJYqybtzlh()
	{
		PtJyjdJYqybtzlh temp = remote.findById(pjj.getRegulatorId());
		temp.setNames(pjj.getNames());
		temp.setRegulatorNo(pjj.getRegulatorNo());
		temp.setFactory(pjj.getFactory());
		temp.setSizes(pjj.getSizes());
		temp.setUserRange(pjj.getUserRange());
		temp.setTestCycle(pjj.getTestCycle());
		temp.setMemo(pjj.getMemo());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		pjj = remote.update(temp);
		if(pjj == null)
		{
			write("{failure:true,msg:'该绝缘仪器仪表名称已存在！'}");
		}
		else
		write("{success:true,msg:'数据修改成功！'}");
	}
	
	/**
	 * 删除一条或多条绝缘仪器仪表信息
	 */
	public void deletePtJyjdJYqybtzlh()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 根据姓名和企业编码查询绝缘仪器仪表信息列表
	 * @throws JSONException 
	 */
	public void findPtJyjdJYqybtzlhList() throws JSONException
	{
        PageObject obj=new  PageObject();
		
		String name = request.getParameter("name");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findAll(name, employee.getEnterpriseCode(), start,limit);
	    }
	    else
	    {
	    	obj=remote.findAll(name, employee.getEnterpriseCode());
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 根据姓名和企业编码查询绝缘仪器仪表信息列表以及预试计划
	 * @throws JSONException 
	 */
	public void findPjjTryList() throws JSONException
	{
        PageObject obj=new  PageObject();
		
		String name = request.getParameter("name");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findPjjTryAll(name, employee.getEnterpriseCode(), start,limit);
	    }
	    else
	    {
	    	obj=remote.findPjjTryAll(name, employee.getEnterpriseCode());
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 对绝缘仪器仪表台帐和预试计划表中的一条信息进行编辑
	 */
	public void editPjjTryInfo() throws JSONException
	{
		String regulatorId = request.getParameter("regulatorId");
		System.out.println(regulatorId);
		String operateBy = request.getParameter("operateBy");
		System.out.println(operateBy);
		String operateDate = request.getParameter("operateDate");
		System.out.println(operateDate);
		String memo = request.getParameter("memo");
		System.out.println(memo);
		String test = request.getParameter("testCycle");
		if(test == null || test.equals(""))
		{
			test ="0";
		}
		System.out.println(test);
		Long id = Long.parseLong(regulatorId);
		int testCycle = Integer.parseInt(test);
		boolean bool = remote.editPjjTryInfo(id,operateBy,operateDate,memo,testCycle);
		if(!bool)
		{
			write("{failure:true,msg:'数据保存失败！'}");
		}
		else
		write("{success:true,msg:'数据保存成功！'}");
	}
	
	public PtJyjdJYqybtzlh getPjj() {
		return pjj;
	}

	public void setPjj(PtJyjdJYqybtzlh pjj) {
		this.pjj = pjj;
	}


}
