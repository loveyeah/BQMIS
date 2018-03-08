package power.ejb.equ.failure; 
import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 *
 * @author lyu  
 */
@Remote
public interface BqEquJfailuresRemote {
	/**
	 * 查询缺陷上报列表
	 * @param status
	 * @param entrepriseCode
	 * @param sdate
	 * @param edate
	 * @param start
	 * @param limit
	 * @param belongBlock
	 * @param dominationProfession
	 * @param repairDep
	 * @param workerCode
	 * @return
	 */
	public PageObject findListByStatus(String status, String entrepriseCode,
			String sdate, String edate, int start, int limit,
			String belongBlock, String dominationProfession, String repairDep,
			String workerCode); 
	/**
	 * 查询缺陷审批列表
	 * @param workflowno
	 * @param entrepriseCode
	 * @param sdate
	 * @param edate
	 * @param status
	 * @param start
	 * @param limit
	 * @param belongBlock
	 * @param specialityCode
	 * @param deptCode
	 * @param whereDep
	 * @param whereProfession
	 * @return
	 */
	public PageObject findApproveList(String workflowno, String entrepriseCode,
			String sdate, String edate, String status, int start, int limit,
			String belongBlock, String specialityCode, String deptCode,String whereDep,String whereProfession)throws ParseException;
	/**
	 * 缺陷查询时取得各种状态的累计数
	 * @param sdate
	 * @param edate
	 * @param status
	 * @param entrepriseCode
	 * @param start
	 * @param limit
	 * @param belongBlock
	 * @param specialityCode
	 * @param deptCode
	 * @return
	 */
	public List<Object[]> getToptipMsg(String sdate, String edate,
			String status, String entrepriseCode, 
			String belongBlock, String specialityCode, String deptCode);
	/**
	 * 根据状态查询缺陷列表  
	 * @param status
	 * @param entrepriseCode
	 * @param start
	 * @param limit
	 * @param belongBlock
	 * @param specialityCode
	 * @param deptCode
	 * @return PageObject
	 * @throws ParseException
	 */
	public PageObject queryListByStatus(String stop,String sdate, String edate,
			String status, String entrepriseCode, int start, int limit,
			String belongBlock, String specialityCode, String deptCode,String findDeptId);
	
	public PageObject queryListByType(String strDate,String  endDate, 
			String enterpriseCode,int  start,int  limit,String type,String queryType);
	
	
	/**
	 * 根据id显示缺陷详细信息
	 * @param id
	 * @param entrepriseCode
	 */
	public PageObject findFailureById(String id, String entrepriseCode);
}
