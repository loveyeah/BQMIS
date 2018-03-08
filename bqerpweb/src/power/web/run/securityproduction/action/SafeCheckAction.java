package power.web.run.securityproduction.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJAntiAccidentCheckup;
import power.ejb.run.securityproduction.SpJAntiAccidentCheckupFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SafeCheckAction extends AbstractAction
{
	private SpJAntiAccidentCheckupFacadeRemote remote;
	private SpJAntiAccidentCheckup check;
	
	public SpJAntiAccidentCheckup getCheck() {
		return check;
	}

	public void setCheck(SpJAntiAccidentCheckup check) {
		this.check = check;
	}

	public SafeCheckAction()
	{
		remote = (SpJAntiAccidentCheckupFacadeRemote)factory.getFacadeRemote("SpJAntiAccidentCheckupFacade");
	}
	
	/**
	 * 获得25项反措动态检查表中符合条件的数据
	 * @throws JSONException 
	 */
	public void getSafeCheckupList() throws JSONException
	{
		String season = request.getParameter("season");
		String startStr = request.getParameter("start");
		String limitStr = request.getParameter("limit");
		String measureCode = request.getParameter("measureCode");
		String status = request.getParameter("status");
		
		String specialCode = request.getParameter("specialCode");
		String checkBy = request.getParameter("checkBy"); 
		PageObject pg = new PageObject();
		if(startStr != null && limitStr != null)
		{
			int start = Integer.parseInt(startStr);
			int limit = Integer.parseInt(limitStr);
			pg = remote.getCheckupList(status, season, measureCode, specialCode,checkBy,employee.getEnterpriseCode(),
					start,limit);
		}
		else 
			pg = remote.getCheckupList(status, season, measureCode,specialCode,checkBy, employee.getEnterpriseCode());
		String str = JSONUtil.serialize(pg);
		write(str);
	}
	
	/** 保存25项反措动态检查数据
	 * 
	 */
	public void saveCheckup()
	{
		String checkDateString = request.getParameter("checkDate");
		String season = request.getParameter("season");
		String isProblem = request.getParameter("isProblem");
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		if(checkDateString != null)
			try {
				check.setCheckDate(sbf.parse(checkDateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		check.setSeason(season);
		check.setIsProblem(isProblem);
		check.setIsUse("Y");
		check.setEnterpriseCode(employee.getEnterpriseCode());
		check.setApproveStatus("0");
		check.setModifyBy(employee.getWorkerCode());
		check.setModifyDate(new Date());
		
		SpJAntiAccidentCheckup flag = remote.save(check);
		if(flag == null)
			write("{success:true,msg:'该季度的数据已存在！'}");
		else {
			write("{success:true,msg:'数据增加成功！'}");
		}
	}
	
	/**
	 *删除25项反措动态检查数据
	 */
	public void deleteCheckup()
	{
		String ids = request.getParameter("ids");
		remote.delete(ids);
	}
	
	/**
	 * 修改25项反措动态检查数据
	 */
	public void updateCheckup()
	{
		String checkDateString = request.getParameter("checkDate");
		String season = request.getParameter("season");
		String isProblem = request.getParameter("isProblem");
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		
		SpJAntiAccidentCheckup temp = remote.findById(check.getCheckupId());
		if(checkDateString != null)
			try {
				temp.setCheckDate(sbf.parse(checkDateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			temp.setCheckBy(check.getCheckBy());
			temp.setSeason(season);
			temp.setIsProblem(isProblem);
			temp.setEnterpriseCode(employee.getEnterpriseCode());
			temp.setApproveStatus("0");
			temp.setModifyBy(employee.getWorkerCode());
			temp.setModifyDate(new Date());
		

			SpJAntiAccidentCheckup flag = remote.update(temp);
			if (flag == null)
				write("{success:true,msg:'该季度的数据已存在！'}");
			else {
				write("{success:true,msg:'数据数据成功!'}");
			}
	}
	
	public void reportCheckup()
	{
		String idString = request.getParameter("id");
		Long id = Long.parseLong(idString);
		SpJAntiAccidentCheckup temp = remote.findById(id);
		temp.setApproveStatus("1");
		remote.update(temp);
	}
	
	/**
	 * 25项反措动态检查审核
	 * add by drdu 090920
	 */
	public void checkApprove()
	{
		String checkupId = request.getParameter("checkupId");
		String buttonId = request.getParameter("buttonId");
		String approveText = request.getParameter("approveText");
		SpJAntiAccidentCheckup model = remote.findById(Long.parseLong(checkupId));
		if(buttonId.equals("yes"))
		{
			model.setApproveStatus("2");
			model.setApproveText(approveText);
		}
		else if(buttonId.equals("no"))
		{
			model.setApproveStatus("3");
			model.setApproveText(approveText);
		}
		remote.update(model);
	}
}