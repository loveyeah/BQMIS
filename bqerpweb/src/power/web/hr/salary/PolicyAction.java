package power.web.hr.salary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCSalaryPolicy;
import power.ejb.hr.salary.HrCSalaryPolicyFacadeRemote;
import power.web.comm.AbstractAction;
import sun.net.www.ParseUtil;

public class PolicyAction extends AbstractAction
{
	private HrCSalaryPolicyFacadeRemote remote;
	private Long policyId;
	private Long stationId;
	private Double increaseRange;
	private String memo;
	
	public PolicyAction()
	{
		remote = (HrCSalaryPolicyFacadeRemote)factory.getFacadeRemote("HrCSalaryPolicyFacade");
	}
	
	/**
	 * 新增一条运行岗位倾斜政策维护记录
	 */
	public void addPolicy()
	{
		HrCSalaryPolicy temp = new HrCSalaryPolicy();
		temp.setPolicyId(policyId);
		temp.setStationId(stationId);
		temp.setIncreaseRange(increaseRange);
		temp.setMemo(memo);
		temp.setIsUse("Y");
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		temp = remote.save(temp);
		if(temp == null)
			write("{success:true,msg:'该岗位的增加幅度已存在！'}");
		else 
			write("{success : true, msg : '数据增加成功'}");
	}
	
	/**
	 * 更新一条运行岗位倾斜政策维护记录仪
	 */
	public void updatePolicy()
	{
		HrCSalaryPolicy temp = remote.findById(policyId);
		temp.setPolicyId(policyId);
		temp.setStationId(stationId);
		temp.setIncreaseRange(increaseRange);
		temp.setMemo(memo);
		temp.setIsUse("Y");
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		temp = remote.update(temp);
		if(temp == null)
			write("{success:true,msg:'该岗位的增加幅度已存在！'}");
		else 
			write("{success : true, msg : '数据修改成功'}");
	}
	
	
	//add by ming_lian 2010-8-11
	/**
	 * 保存所有运行岗位倾斜政策修改记录
	 * @throws JSONException 
	 */
	public void savePolicy() throws JSONException
	{
		String str = request.getParameter("isUpdateData");
		Object obj = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) obj;
		for (Map data : list) {
			
			HrCSalaryPolicy temp = new HrCSalaryPolicy();
			if (data.get("stationId") != null&& !data.get("stationId").equals("")) {
				temp.setStationId(Long.parseLong(data.get("stationId").toString()));
			}
			if (data.get("increaseRange") != null&& !data.get("increaseRange").equals("")) {
				temp.setIncreaseRange(Double.parseDouble(data.get("increaseRange").toString()));
			}
			if (data.get("memo") != null&& !data.get("memo").equals("")) {
				temp.setMemo(data.get("memo").toString());
			}
			temp.setIsUse("Y");
			temp.setEnterpriseCode(employee.getEnterpriseCode());
			if(data.get("policyId") == null||data.get("policyId").equals("")){
			   temp = remote.save(temp);
			}else{
				temp.setPolicyId(Long.parseLong(data.get("policyId").toString()));
				temp = remote.update(temp);
			}
			
			if(temp == null)
				write("{success:true,msg:'该岗位的增加幅度已存在！'}");
			else 
				write("{success : true, msg : '数据增加成功'}");
		}
	}
	
	
	
	/**
	 * 删除一条或多条运行岗位倾斜政策维护记录
	 */
	public void deletePolicy()
	{
		String ids = request.getParameter("ids");
		remote.delete(ids);
	}
	
	/**
	 * 查找所有运行岗位倾斜政策维护记录
	 * @throws JSONException 
	 */
	public void findAllPolicy() throws JSONException
	{
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String stationName = request.getParameter("stationName");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(stationName,employee.getEnterpriseCode(),start,limit);
		} else {
			object = remote.findAll(stationName,employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		strOutput = JSONUtil.serialize(object);
		write(strOutput);
	}

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Double getIncreaseRange() {
		return increaseRange;
	}

	public void setIncreaseRange(Double increaseRange) {
		this.increaseRange = increaseRange;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}