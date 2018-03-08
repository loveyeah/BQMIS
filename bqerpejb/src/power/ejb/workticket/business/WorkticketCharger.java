package power.ejb.workticket.business;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.hr.HrJEmpInfo;
import power.ejb.workticket.form.ChargerForm;
@Remote 
public interface WorkticketCharger {
	
	/**
	 * 查询在某个部门下的某种工作票负责人
	 * @param workticketTypeName 工作票类型名称
	 * @param userName 用户工号或者姓名
	 * @return
	 */
	public List<ChargerForm> findCharger(String workticketTypeName,String userName);
	/**
	 * 监护人
	 * @param userName
	 * @param rowStartIdxAndCount
	 * @return List<ChargerForm>
	 */
	public List<ChargerForm> findWatcher(String userName,final int... rowStartIdxAndCount);

}
