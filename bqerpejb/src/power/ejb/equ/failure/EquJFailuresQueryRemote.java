package power.ejb.equ.failure;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface EquJFailuresQueryRemote {
	
	//缺陷综合统计
	public List<EquFailuresQueryForm> failureIntegerQuery(String startDate,String endDate,String type,String enterpriseCode);
	//缺陷个人发现查询
	public PageObject failureFindQuery(String from,String end, String enterpriseCode,String workercode,int start,int limit);
	//缺陷个人消缺
	public PageObject failueEliminateQuery(String from,String end, String enterpriseCode,String workercode,int start,int limit);
	//无kks编码缺陷统计
	public PageObject noKKSfailureQuery(String enterprisecode,int start,int limit);
	public PageObject findNoKKSfailureByWorker(String enterpriseCode,String workercode,int start,int limit);
	//缺陷统计（按系统）
	public PageObject failureQueryBySystem(String startDate,String end,String enterprisecode,int start,int limit);
	//缺陷统计（按设备）
	public PageObject failureQueryByEqu(String startDate,String end,String enterprisecode,int start,int limit);
	//缺陷待处理查询
	public PageObject awaitFailureQuery(String from,String to,String reparidep,String domProfession,String awaitType,String status,String enterprisecode,int start,int limit);
	//缺陷统计月报
	public List<EquFailuresQueryForm> failureMonthReport(String month,String enterprisecode);
	
	/**
	 * 查询已消除缺陷数列、未消除缺陷数、退回缺陷数、超时缺陷数详细明细
	 * add by sychen 20100915
	 * @param specialty
	 * @param defectType
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEquDetailList(String writeDate,String specialty,String defectType,String enterpriseCode,int... rowStartIdxAndCount);
	
	//缺陷年度统计报表
	public List<EquFailuresQueryForm> failureYearReport(String year,String enterpriseCode,String speicalcode);
	public List<EquYearReportForm> yearReportQuery(String year,String enterpriseCode);
	public List<EquYearReportForm> yearReportQueryByDept(String year,String enterpriseCode);
	public EquFailuresQueryForm zongYearEliRate(String year,String enterpriseCode);
	//泄漏缺陷统计报表
	public List<EquYearReportForm> bugReportQuery(String year,String enterpriseCode);
	public EquFailuresQueryForm zongBugEliRate(String year,String enterpriseCode);
}
