package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ContractFullInfo;

/**
 * @author slTang
 */
@Remote
public interface ConJContractInfoFacadeRemote {
	/**
	 * 增加合同
	 * 
	 * @param String
	 *            markCode合同类别标识码
	 * @param ConJContractInfo
	 *            entity合同对象实体
	 * @return ConJContractInfo合同对象
	 */
	public ConJContractInfo save(String markCode, ConJContractInfo entity,ConJConDoc condoc,String secondcharge)throws CodeRepeatException;

	/**
	 * 删除合同
	 * 
	 * @param ConJContractInfo
	 *            entity合同对象实体
	 */
	public void delete(ConJContractInfo entity) throws CodeRepeatException;

	/**
	 * 更新合同
	 * 
	 * @param ConJContractInfo
	 *            entity合同对象实体
	 */
	public ConJContractInfo update(ConJContractInfo entity,ConJConDoc condoc,String secondcharge)throws CodeRepeatException;

	public ConJContractInfo findById(Long id);

	/**
	 * 查询合同列表
	 * 
	 * @param String
	 *            enterpriseCode 企业编码
	 * @param String
	 *            sDate会签开始时间
	 * @param String
	 *            eDate会签开始时间
	 * @param String
	 *            likeName模糊查询参数（合同编号，合同名称，供应商名称）
	 * @param String
	 *            workflowStatus会签状态（未上报，会签中，退回）
	 * @param String
	 *            oprateDept操作人部门（经办人部门）
	 * @param String
	 * 			  entryBy  起草人        
	 * @return PageObject
	 */
	public PageObject findContractInfos(Long conTypeId,String enterpriseCode, String sDate,
			String eDate, String likeName, String workflowStatus,String entryBy, String oprateDept,int...rowStartIdxAndCount);
	/**
	 * 根据id查找完全信息
	 * @param  Long conId  合同ID 
	 * @return     ContractFullInfo 
	 */
	public ContractFullInfo getConFullInfoById(Long conId);
	/**
	 * 查询合同上报、会签中、已会签列表
	 * @param enterpriseCode 企业编码
	 * @param sDate 
	 * @param eDate
	 * @param likeName
	 * @param oprateDept
	 * @param entryBy
	 * @param status "report 上报列表，approve 会签中列表，endsign 已会签列表"
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findReportContractList(String workCode,Long conTypeId,String enterpriseCode, String sDate,
			String eDate, String likeName,  String oprateDept,String entryBy,String status,String entryIds,int...rowStartIdxAndCount);


	/**
	 * 查询合同验收终止列表
	 * @param enterpriseCode 企业编码
	 * @param fuzzy 查询条件：合同编号，合同名称，供应商名称
	 * @param rowStartIdxAndCount 动态分页
	 * @return
	 * 
	 * *  add by drdu 
	 */
	public PageObject findContractTerminateList(Long conTypeId,String enterpriseCode,String fuzzy,final int... rowStartIdxAndCount);
	
	/**
	 * 合同终止
	 * @param entity
	 * 
	 * add by drdu
	 */
	public ConJContractInfo saveConTerminate(ConJContractInfo entity) throws CodeRepeatException;
	/**
	 * 查询合同归档确认列表
	 * @param startDate
	 * @param endDate
	 * @param enterprisecode
	 * @param status
	 * @param conNo
	 * @param conName
	 * @param client
	 * @param fileNo
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findArchiveList(String startDate,String endDate,String enterprisecode,String status,String conNo,String conName,String client,String fileNo,Long conTypeId,int...rowStartIdxAndCount);
	/**
	 * 查询合同归档申请列表
	 * @param startDate
	 * @param endDate
	 * @param enterprisecode
	 * @param fuzzy
	 * @param rowStartIdxAndCount
	 * add by lyu @ 20081225
	 * modified by dswang @ 2009521
	 * @return
	 */
	public PageObject findContractArchiveList(Long conTypeId,String startDate,String endDate,String enterprisecode,String fuzzy,final int... rowStartIdxAndCount);
	/**
	 * 查询执行中合同列表
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param fuzzy
	 * @param oprateDept
	 * @param rowStartIdxAndCount
	 * * add by lyu @ 20081225
	 * @return
	 */
	public PageObject findExecContractList(String enterpriseCode,String sDate, String eDate,
			String fuzzy, String oprateDept, int...rowStartIdxAndCount);
	/**
	 * 查询所有合同列表（变更信息）
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param conNO
	 * @param conName
	 * @param client
	 * @param operaterBy
	 * @param status
	 * @param rowStartIdxAndCount
	 *  * * add by lyu @ 20081226
	 * @return
	 */
	public PageObject findAllContractList(Long conTypeId,String enterpriseCode,String sDate, String eDate,
			String conNO, String conName,String client,String operaterBy,Long status, int...rowStartIdxAndCount);
	
	/**
	 * 查询合同付款计划列表
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param conNO
	 * @param conName
	 * @param client
	 * @param operaterBy
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 * *   add by drdu @20090105
	 * 
	 */
	public PageObject findContractPayPlayList(Long conTypeId,String enterpriseCode,String sDate, String eDate,
			String conNO, String conName,String client,String operaterBy, int...rowStartIdxAndCount);
	
	/**
	 * 采购合同综合统计
	 * @param enterpriseCode企业编码
	 * @param sDate		登记开始日期
	 * @param eDate		登记结束日期
	 * @param conNO		合同编号
	 * @param conName	合同名称
	 * @param client	供应商
	 * @param operaterBy经办人
	 * @param execFlag	合同状态
	 * @param fileStatus归档状态
	 * @param rowStartIdxAndCount动态页码
	 * @return
	 * 
	 * *  add by drdu  @20090106
	 */
	public PageObject findConIntegrateList(Long conTypeId,String enterpriseCode,String sDate, String eDate,
			String conNO, String conName,String client,String operaterBy,String execFlag,
			String fileStatus,int...rowStartIdxAndCount);
	/**
	 *合同是否归档
	 *@param undertakeNo 档号
	 *@return boolean (true : 以归档，false ：未归档)   
	 */
	public boolean isArchive(String undertakeNo);
	
	/**
	 * 选择执行中的合同列表
	 * @param enterpriseCode
	 * @param likeName
	 * @param workflowStatus
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 *  add by drdu  @20090320
	 */
	public PageObject findContractSelect(Long conTypeId,String enterpriseCode, String likeName,
			String workflowStatus,int... rowStartIdxAndCount);
	
	 //取审批记录
	public List<ConApproveForm>	getApproveList(Long id);
	//根据币种编码去币种名称
	 public String	getCurrencyNameByItsCode(Long Currency);
	 //bjxu20091017 自动审批部门记录
	 public List<ConApproveForm> getApproveTableList(Long id);
	 
	 /**
	  * 根据合同委托管理审批列表
	  * add by drdu 091109
	  * @param workCode
	  * @param conTypeId
	  * @param enterpriseCode
	  * @param sDate
	  * @param eDate
	  * @param likeName
	  * @param oprateDept
	  * @param entryBy
	  * @param status
	  * @param entryIds
	  * @param rowStartIdxAndCount
	  * @return
	  */
	 public PageObject findDelegationList(String workCode, Long conTypeId,
				String enterpriseCode, String sDate, String eDate, String likeName,
				String oprateDept, String entryBy, String status, String entryIds,
				int... rowStartIdxAndCount);
	 /**
	  * 委托审批记录
	  * @param id
	  * @return
	  */
		public List<ConApproveForm>	getdelegationApprove(Long id);
		
		public PageObject findContactList(String CON_YEAR,String CONTRACT_NAME ,int start ,int limit);
}