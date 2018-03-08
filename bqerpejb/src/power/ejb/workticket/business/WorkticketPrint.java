package power.ejb.workticket.business;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.workticket.form.SecurityMeasureCardModel;
import power.ejb.workticket.form.WorkticketCountForm;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketPrintModel;

@Remote 
public interface WorkticketPrint {
	
	
	/**
	 * 工作票票面需要的数据
	 * @param workticketNo 工作票号
	 * @return
	 */
	public WorkticketPrintModel getWorkticketPrintInfo(String workticketNo);
	/**
	 * 安措打印卡
	 * @param workticketNo
	 * @return
	 */
	public SecurityMeasureCardModel getSecurityMeasureCardInfo(String workticketNo);
	/**
	 * 审批记录
	 * modify by fyyang 090317
	 * @param workticketNo
	 * @return
	 */
	public List<WorkticketHisForPrint> findApproveHisList(String workticketNo,String workticketTypeCode);
	/**
	 * 工作票各种状态统计一览表
	 * @param enterpriseCode 企业编码
	 * @param date 时间
	 * @param depts 部门
	 * @return List<WorkticketCountForm>
	 */
	public List<WorkticketCountForm> getStatusDataOfDept(String stop,String enterpriseCode,String date,String depts);
	
	/**
	 * 取得工作票部门合格率统计
	 * @param enterpriseCode 企业编码
	 * @param date 时间
	 * @param depts 部门
	 * @return List<WorkticketCountForm>
	 */
	public List<WorkticketCountForm> getRateDataOfDept(String stop,String enterpriseCode,String date,String depts);
	
}
