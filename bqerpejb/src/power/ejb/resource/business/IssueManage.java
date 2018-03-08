/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource.business;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.MrpJPlanRequirementDetail;
/**
 * 出库管理remote
 * @author qzhang
 */
@Remote
public interface IssueManage {
	/**
	 * 确认是否正在结账
	 * @param transCode 事务编码
	 * @return true 如果正在结帐
	 */
	public boolean isBalanceNow(String transCode);
	/**
	 * 检索所有领料单表头数据
	 * @param rowStartIdxAndCount 分页
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllIssueHeadDatas(String enterpriseCode,int... rowStartIdxAndCount) ;
	/**
	 * 根据领料单编号检索数据
	 * @param issueNo 领料单编号
	 * @param rowStartIdxAndCount 分页
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getIssueHeadDatasByNo(String enterpriseCode,String issueNo,String applyBy,String materailName, int... rowStartIdxAndCount);
	/**
	 * 根据领料单编号检索物资详细信息
	 * @param issueHeadId 领料单id
	 * @param rowStartIdxAndCount 分页
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMaterialDetailDatas(String enterpriseCode,Long issueHeadId, int... rowStartIdxAndCount );
	/**
	 * 根据批号检索仓库信息
	 * @param enterpriseCode 企业编码
	 * @param materialId 物料id
	 * @param lotNo 批号
	 * @param whsNo 仓库编号
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getLocationByLotNoAndWhsNo(String enterpriseCode,
			String lotNo, String whsNo, Long materialId) ;
	/**
	 * 根据批号检索仓库信息
	 * @param enterpriseCode 企业编码
	 * @param materialId 物料id
	 * @param lotNo 批号
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWareHouseByLotNo(String enterpriseCode,String lotNo, Long materialId);
	/**
	 * 批号检索
	 * @param enterpriseCode 企业编码
	 * @param materialId 物料id
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getLots(String enterpriseCode, Long materialId);
	/**
	 * 获取物料的库存数量
	 * @param enterpriseCode 企业编码
	 * @param lotNo 批号
	 * @param locationNo 库位号
	 * @param whsNo 仓库号
	 * @param materialId 物料id
	 * @return 物料库存数量
	 */
	public double getMaterialNums(String enterpriseCode, String lotNo, String locationNo, String whsNo, Long materialId);
	/**
	 * 获取紧急领用领料单所需特定物资的数量
	 * @param enterpriseCode 企业编码
	 * @param materialId 物料id
	 */
	public double getEmergencyNums(String enterpriseCode, Long materialId);
	/**
	 * 根据物料id检索大于优先级给定值的领料单所需物料数量
	 * @param materialId 物料id
	 * @param planGrade 计划等级
	 * @param enterpriseCode 企业编码
	 * @return 优先级大于给定值的领料单所需物料(materialId)的数量
	 */
	public double getPlanRelateMaterialCount(String enterpriseCode, Long materialId, Long planGrade);
	/**
	 * 获取领料单的计划等级
	 * @param enterpriseCode 企业编码
	 * @param issueHeadId 领料单id
	 * @return 领料单的计划等级
	 */
	public Long getIssuePlanGrade(String enterpriseCode, String issueHeadId);
	/**
	 * 获取物料的库存数量和批号记录表流水号
	 * @param enterpriseCode 企业编码
	 * @param lotNo 批号
	 * @param locationNo 库位号
	 * @param whsNo 仓库号
	 * @param materialId 物料id
	 * @return 物料库存数量
	 */
	@SuppressWarnings("unchecked")
	public PageObject getLotIdsAndCounts(
			String enterpriseCode, String lotNo, String locationNo, String whsNo, Long materialId);
	/**
	 * 根据批号，仓库，库位和物料id检索事务历史表
	 * @param enterpriseCode 企业编码
	 * @param materialId 物料id
	 * @param lotNo 批号
	 * @param whsNo 仓库号
	 * @param locationNo 库位号
	 *
	 */
	@SuppressWarnings("unchecked")
	public PageObject getTransHis(String enterpriseCode,
			Long materialId, String lotNo, String whsNo, String locationNo);
	/**
	 * 获取物料主文件中标准成本计算的几种方式
	 * @param enterpriseCode 企业编码
	 * @param materialId 物料id
	 */
	@SuppressWarnings("unchecked")
	public PageObject getStdCostRelatingFields(String enterpriseCode,Long materialId);
	/**
	 * 根据事务编码获得事务作用记录
	 * @param enterpriseCode 企业编码
	 * @param transCode 事务编码
	 */
	public PageObject getTransIdByTransCode(String enterpriseCode,String transCode);
	/**
	 * db更新
	 * @param head 领料单表头
	 * @param details 要更新的领料单明细表
	 * @param planDetails 要更新的物料需求计划明细表
	 * @param lots 批号记录表
	 * @param transHises 事务历史表
	 * @param wareHouses 库存物料记录
	 * @param locations 库位物料记录
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateIssueDbTables(
			InvJIssueHead head,
			List<InvJIssueDetails> details ,
			List<MrpJPlanRequirementDetail> planDetails,
			List<InvJLot> lots,
			List<InvJTransactionHis> transHises,
			List<InvJWarehouse> wareHouses,
			List<InvJLocation> locations);
	
	/**
	 * modify by liuyi 091102 增加专业，仓库，物资类型，部门查询 专业查询未做
	 * modify by fyyang 091218 增加领料人查询条件
	 * 出库物资对账查询 add by drdu  20090512
	 * @param enterpriseCode 企业编码
	 * @param sDate 出库日期时间段
	 * @param eDate
	 * @param whsName仓库名称
	 * @param materialCode物资编码
	 * @param materialName物资名称
	 * @param specNo规格型号
	 * @param issuNo领料单
	 * @param issueBy出库人员
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findIssueList(String delayStore,String materialClass,String dept,String enterpriseCode, String sDate,
			String eDate, String whsName, String materialCode,
			String materialName, String specNo, String issuNo, String issueBy,String isRedOp,
			String getPerson,String freefrom,
			final int... rowStartIdxAndCount);
	
	/**
	 * 得到领料报表的物资明细 modified by liuyi 20100128
	 * @param issuNo
	 * @param whsName
	 * @return
	 */
//	public List getIssueDetailsMaterialInfo(String issuNo, String whsName);
	 public List getIssueDetailsMaterialInfo(String issuNo, String whsName,String gdFlag,String materialId);
	
	/**
	 * 查询领料单审核列表
	 * add by fyyang 090619
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * modify by drdu 091103 增加审核状态查询
	 * modify by drdu 091126增加领料单号模糊查询
	 */
	public PageObject getIssueHeadListForCheck(String enterpriseCode,String checkStatus,String issueNo,
			int... rowStartIdxAndCount);
	
	/**
	 * 查询领料单审核的物资明细列表
	 * add by fyyang 090619
	 * @param enterpriseCode
	 * @param issueHeadId 领料单Id
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getMaterialDetailListForCheck(String enterpriseCode,
			String strIssueNo,String issueDate,String startCheckDate,String endCheckDate,int... rowStartIdxAndCount);
	
	/**
	 * 获得已审核过的领料单物资列表 (用于出库红单处理页面)
	 * add by fyyang 090708 
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param materialName
	 * @param issuNo
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findIssueDetailCheckList(String enterpriseCode, String sDate,
			String eDate,String materialName, String issuNo, String isRedBill, String materialNo,
			String specNo, final int... rowStartIdxAndCount);
	
	/**
	 * 出库红单数据库操作
	 * add by fyyang 090708
	 * @param headModel
	 * @param detailModel
	 * @param lotModel
	 * @param transHisModel
	 * @param wareHouseMdoel
	 * @param locationModel
	 */
	public void updateDbTablesForIssueBack(InvJIssueHead headModel,
			InvJIssueDetails detailModel,
			 InvJLot lotModel,
			InvJTransactionHis transHisModel,
			InvJWarehouse wareHouseMdoel, InvJLocation locationModel);
	
	/**
	 * 查询取消出库列表
	 * add by drdu 090708
	 * @param enterpriseCode
	 * @param issueNo
	 * @param materialId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findCancelIssueList(String enterpriseCode,String issueNo,String materialId,final int... rowStartIdxAndCount);
	
	/**
	 * 修改数量（出库回滚操作）
	 * add by drdu 090708
	 * @param enterpriseCode
	 * @param tHisId
	 * @param qty
	 */
	// modified by liuyi 091109 数量留有四位小数
//	public void updateIssueQty(String enterpriseCode,Long tHisId,Long qty);
	public void updateIssueQty(String enterpriseCode,Long tHisId,Double qty,String workCode);
	
	/**
	 * 出库管理中补打印列表
	 * add by drdu 091121
	 * @param issueNo
	 * @param start
	 * @param limit
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findAfterPrintIssueList(String issueNo, int start, int limit, String enterpriseCode);
	
	/**
	 * 出库补打印报表信息
	 * add by drdu 091121
	 * @param issueNo
	 * @param fillDate
	 * @param whsNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
//	public List getAfterPrintIssueInfo(String issueNo,String fillDate,String whsNo);
	public List getAfterPrintIssueInfo(String issueNo,String fillDate,String whsNo,String gdFlag,String materialId);

	/**
	 * 领料单审批记录
	 * @param issueNo
	 * @return
	 */
	public List<ConApproveForm> getApproveList(String issueNo);
	
	/**
	 * 查询已审核的领料单列表 
	 * add by fyyang 091224
	 * @param sdate
	 * @param edate
	 * @param issueNo
	 * @param receiptBy
	 * @param receiptDept
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryCheckedIssueList(String sdate,String edate,String issueNo,String receiptBy,String receiptDept,String enterpriseCode,String billType,final int... rowStartIdxAndCount);
	
	/**
	 * 查询已审核的领料单明细列表 
	 * add by ywliu 100202
	 * @param sdate
	 * @param edate
	 * @param issueNo
	 * @param receiptBy
	 * @param receiptDept
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryCheckedIssueDetailList(String sdate,String edate,String issueNo,String receiptBy,String receiptDept,String enterpriseCode,String billType,final int... rowStartIdxAndCount);
}