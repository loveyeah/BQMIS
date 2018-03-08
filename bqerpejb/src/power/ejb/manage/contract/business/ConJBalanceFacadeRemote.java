package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.form.BpAppDetailForm;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ConBalanceForm;
import power.ejb.manage.contract.form.ConBalanceFullForm;
import power.ejb.manage.contract.form.ContractForm;

/**
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConJBalanceFacadeRemote {
	/**
	 * 增加结算信息
	 * 
	 */
	public ConJBalance save(ConJBalance entity) throws CodeRepeatException;

	/**
	 * 删除结算信息
	 * 
	 */
	public void delete(ConJBalance entity) throws CodeRepeatException;

	/**
	 * 修改结算信息
	 * 
	 */
	public ConJBalance update(ConJBalance entity) throws CodeRepeatException;

	public void updateBalance(ConJBalance entity);

	public ConJBalance findById(Long id);

	/**
	 * 根据合同Id草找结算信息列表
	 * 
	 * @param String
	 *            enterpriseCode
	 * @param Long
	 *            conId 合同ID
	 * @return List<ConBalanceForm>
	 */
	public List<ConBalanceForm> findBalanceListByConId(Long conId,
			String enterpriseCode);

	/**
	 * 根据合同Id草找合同信息
	 * 
	 * @param Long
	 *            conId 合同ID
	 * @return ContractForm
	 */
	public ContractForm findContractByConId(Long conId);

	public ConBalanceFullForm findBalanceByBalanceId(Long balanceId);

	/**
	 * 查询合同实际付款明细列表
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param sDate
	 *            履行开始时间
	 * @param eDate
	 *            履行结束时间
	 * @param conNO
	 *            合同编号
	 * @param conName合同名称
	 * @param client
	 *            供应商
	 * @param operaterBy经办人
	 * @param rowStartIdxAndCount
	 *            动态页码
	 * @return
	 * 
	 * add by drdu
	 * @20090105
	 */
	public PageObject findConPayDetailsList(Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String conNO,
			String conName, String client, String operaterBy,
			int... rowStartIdxAndCount);

	/**
	 * 查询合同执行情况列表
	 * 
	 * @param enterpriseCode企业编码
	 * @param sDate
	 *            履行开始时间
	 * @param eDate
	 *            履行结束时间
	 * @param conNO
	 *            合同编号
	 * @param conName
	 *            合同名称
	 * @param client
	 *            供应商
	 * @param operaterBy经办人
	 * @param rowStartIdxAndCount动态页码
	 * @return * add by drdu
	 * @20090108
	 */
	public PageObject findConPaymentList(Long conTypeId, String enterpriseCode,
			String sDate, String eDate, String conNO, String conName,
			String client, String operaterBy, int... rowStartIdxAndCount);

	/**
	 * 查询合同结算审批列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @param enterprisecode
	 * @param woflowNo
	 * @param type
	 * @param start
	 * @param limit
	 * @return
	 */
	public PageObject findConBalanceApproveList(Long conTypeId,
			String startDate, String endDate, String enterprisecode,
			String woflowNo, String type, int start, int limit);

	// 取审批记录
	public List<ConApproveForm> getApproveList(Long id);

	/**
	 * add by liuyi 091120 批量修改采购合同付款申请 返回经过数据库后的appId
	 * 
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public Long saveModiRec(Long appIdFlag, List<ConJBalance> addList,
			List<ConJBalance> updateList, String ids);

	/**
	 * 采购结算合同查询付款申请记录 bq
	 * 
	 * @param conTypeId
	 * @param rowStartIdxAndCount
	 * @return
	 */

	public PageObject bqfindAppConList(String status,String approved, String rightIds,
			String appId, String entryBy, String enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * 采购结算合同查询付款申请明细 bq
	 * 
	 * @param appId
	 *            采购合同申请主单价Id
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject bqfindBalanceListByAppId(String appId,
			String enterpriseCode, int... rowStartIdxAndCount);

	/**
	 * add by liuyi 091120 删除采购合同付款申请数据
	 * 
	 * @param appId
	 */
	public void deleteRecByAppId(String appId);

	/**
	 * 一条主单据对应的结算记录 bq
	 * 
	 * @param appId
	 * @return
	 */
	public List<ConJBalance> getBalaceListByAppId(String appId,
			String enterpriseCode);

	/**
	 * 采购合同增加选择 bq
	 */
	public PageObject bqContractSelect(String fuzzy, int... rowStartIdxAndCount);

	/**
	 * 报表列表信息 bq
	 * 
	 * @param appId
	 * @return
	 */
	public List<BpAppDetailForm> bqFindBalaceReportByAppId(String appId);

	/**
	 * 审批信息 bq
	 * 
	 * @param entrtyId
	 * @return
	 */
	public List<ConApproveForm> getCgApproveList(String entrtyId);
	
	/**
	 * 结算明细查询 bq
	 * @param startDate
	 * @param endDate
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject balanceQueryList(String startDate,String endDate,int... rowStartIdxAndCount);
}