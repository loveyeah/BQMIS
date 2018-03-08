package power.ejb.run.powernotice;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 停送电通知单
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCPowerNoticeFacadeRemote {
	
	public RunCPowerNotice save(RunCPowerNotice entity);

	public void delete(Long noticeId);
	public void deleteMulti(String noticeIds);
	public RunCPowerNotice update(RunCPowerNotice entity);
	//add by ypan 20100810
    public PageObject findData(final int... rowStartIdxAndCount);
	public RunCPowerNotice findById(Long id);
	/**
	 * 上报列表
	 * @param enterpriseCode
	 * @param contactDept
	 * @param busiState
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findNoReportList(String enterpriseCode,String contactDept,String busiState,final int... rowStartIdxAndCount);
	/**
	 * 审批列表
	 * @param enterpriseCode
	 * @param contactDept
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findApproveList(String enterpriseCode,String contactDept,String entryIds,final int... rowStartIdxAndCount);
	
	/**
	 * 查询列表
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param contactDept
	 * @param busiState
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findQueryList(String enterpriseCode,String sdate,String edate,String contactDept,String busiState,String teamValue,String apply,final int... rowStartIdxAndCount);
	
	/**
	 * 上报
	 * @param busitNo
	 * @param flowCode
	 * @param workerCode
	 * @param actionId
	 */
	
	public void reportTo(String busitNo,String flowCode,String workerCode,Long actionId);
	/**
	 * 值班负责人审批
	 * @param his
	 * @param noticeNo
	 * @param workflowNo
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @throws ParseException
	 */
	public void approveSign(RunJPowerNoticeApprove his,String noticeNo,String workflowNo,String workerCode,
			Long actionId,String responseDate,String nextRoles,String eventIdentify) throws ParseException ;

}