package power.web.run.securityproduction.action;

import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSpecialoperators;
import power.ejb.run.securityproduction.SpJSpecialoperatorsFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 特种作业人员管理
 * @author fyyang 090602
 *
 */
public class SpecialoperatorsAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1465465256465464L;
	private SpJSpecialoperators spj;
	private SpJSpecialoperatorsFacadeRemote remote; 
	
	public SpecialoperatorsAction()
	{
		remote = (SpJSpecialoperatorsFacadeRemote)factory.getFacadeRemote("SpJSpecialoperatorsFacade");
	}
	
	/**
	 * 增加一条特种作业人员信息
	 */
	public void addSpecialoperatorsInfo()
	{
		
		spj.setEnterpriseCode(employee.getEnterpriseCode());
		spj = remote.save(spj);
		if(spj == null)
		{
			write("{failure:true,msg:'数据增加失败！'}");
		}
		else
		write("{success:true,id:'"+spj.getOfferId()+"',msg:'数据增加成功！'}");
	}
	
	/**
	 * 修改一条特种作业人员信息
	 */
	public void updateSpecialoperatorsInfo()
	{
		SpJSpecialoperators temp = remote.findById(spj.getOfferId());
		temp.setWorkerCode(spj.getWorkerCode());
		temp.setProjectOperation(spj.getProjectOperation());
		temp.setPostYear(spj.getPostYear());
		temp.setOfferName(spj.getOfferName());
		temp.setOfferCode(spj.getOfferCode());
		temp.setOfferDate(spj.getOfferDate());
		temp.setOfferStartDate(spj.getOfferStartDate());
		temp.setOfferEndDate(spj.getOfferEndDate());
		temp.setMedicalDate(spj.getMedicalDate());
		temp.setMedicalResult(spj.getMedicalResult());
		temp.setMemo(spj.getMemo());
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		spj = remote.update(temp);
		if(spj == null)
		{
			write("{failure:true,msg:'数据修改失败！'}");
		}
		else
		write("{success:true,msg:'数据修改成功！'}");
	}
	
	/**
	 * 删除一条或多条特种作业人员信息
	 */
	public void deleteSpecialoperatorsInfo()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'数据删除成功！'}");
	}
	
	/**
	 * 根据姓名和企业编码查询特种人员信息列表
	 * @throws JSONException 
	 */
	public void findSpecialoperatorsList() throws JSONException
	{
        PageObject obj=new  PageObject();
		
		String workName = request.getParameter("workerName");
		
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findAll(workName, employee.getEnterpriseCode(), start,limit);
	    }
	    else
	    {
	    	obj=remote.findAll(workName, employee.getEnterpriseCode());
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}

	public SpJSpecialoperators getSpj() {
		return spj;
	}

	public void setSpj(SpJSpecialoperators spj) {
		this.spj = spj;
	}
}
