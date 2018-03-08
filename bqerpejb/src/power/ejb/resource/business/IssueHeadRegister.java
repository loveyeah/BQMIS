/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCDept;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.MrpJPlanRequirementHead;

/**
 * 领料单登记Remote
 *
 * @author
 * @version 1.0
 */
@Remote
public interface IssueHeadRegister {

	/**
	 * 查询领料单
	 *
	 * @param issueHeadId
	 *            领料单编号
	 * @param flag
	 *            checkbox标识
	 * @param deptId
	 *            领用部门
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 领料单
	 */
	public PageObject findIssueHead(String issueHeadId, String flag,
			String deptId, String enterpriseCode,
			final int... rowStartIdxAndCount);
	/**
	 * 查询领料单信息
	 * @param fromDate 开始日期
	 * @param toDate 结束日期
	 * @param appBy 申请人
	 * @param acceptDept 申请部门
	 * @param materialName 物料名称
	 * @param receiveStatus 接收状态
	 * @param issueStatus 审批状态
	 * @param enterpriseCode 企业编码
	 * @param issueNo 领料单号 add by fyyang 100113
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 领料单   
	 * @author yiliu   05/15/09    
	 */
	public PageObject getIssueInfo(String fromDate, String toDate,
			String appBy,String acceptDept,String materialName,String receiveStatus,
			String issueStatus,String queryType,String workCode,  String enterpriseCode, 
			String issueNo,
			final int... rowStartIdxAndCount);
	/**
	 * 查询领料单审批列表
	 * modify by fyyang 090526 增加领料单号和审批状态查询条件
	 * modify by fyyang 091022 999999不做部门过滤
	 * @param deptId
	 * @param enterpriseCode
	 * @param workflowno
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findIssueHeadApproveList(String deptId, String enterpriseCode,String issueNo,String status,String workflowno,
			String workCode,final int... rowStartIdxAndCount);
	/**
	 * 查询领料单明细
	 *
	 * @param issueHeadId
	 *            领料单编号
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 领料单明细
	 */
	public PageObject findIssueHeadDetails(String issueHeadId,
			String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 查询物料需求计划主表
	 *modify by fyyang 090623 增加了申请人查询条件
	 * @param deptId
	 * 			  申请部门
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 物料需求计划主表
	 */
	public PageObject findPlanRequirementHead(String deptId, String enterpriseCode,String applyByName,String workCode,
			final int... rowStartIdxAndCount);

	/**
	 * 查询领料单需求明细
	 * modify by fyyang 20100408
	 * @param detailIds 申请单明细IDs
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return 领料单需求明细
	 */
	public PageObject findIssueRequimentDetail(String detailIds, String enterpriseCode,
			final int... rowStartIdxAndCount);
	/**
	 * 根据领料单编号删除领料单及其对应的领料单明细
	 * @param enterpriseCode 企业编码
	 * @param workerCode 工号
	 * @param issueHeadId 领料单id
	 */
	public void deleteIssueRecords(String enterpriseCode, String workerCode, String issueHeadId);
	/**
	 * 增加领料单和领料单明细数据
	 * @param head 领料单表头
	 * @param details 领料单明细
	 * @return 新规的领料单的流水号
	 */
	public Long addIssueHeadAndDetails(InvJIssueHead head,
			List<InvJIssueDetails> details);
	  /**
	   * 更新领料单和领料单明细数据
	   * @param head 表头数据
	   * @param newDetails 新增明细数据
	   * @param upDetails 修改明细数据
	   * @param delDetails 删除明细数据
	   */
	  public void updateIssueHeadAndDetails(InvJIssueHead head, List<InvJIssueDetails> newDetails,
			  List<InvJIssueDetails> upDetails, List<InvJIssueDetails> delDetails );
	    /**
	     * 根据人员工号找部门信息
	     * @param empCode
	     * @return
	     */
	    @SuppressWarnings("unchecked")
		public HrCDept findDeptInfo (String empCode);
	    /**
	     * 根据计划单编号查询物料需求详细
	     * @param mrNo
	     * @param enterpriseCode
	     * @return
	     */
	    public MrpJPlanRequirementHead findPlanRequirementHeadModel(String mrNo,String enterpriseCode);
	    
    /**
     * 根据登陆人查询领料单
     * @param mrNo
     * @param enterpriseCode
     * @return
     */
    public PageObject findIssueListByLogin(String dateFrom,String dateTo,String appBy,String status,String issueNo,String enterpriseCode, final int... rowStartIdxAndCount);
    
    /**
     * 根据登陆人查询登陆人参与审批的领料单
     * @param mrNo
     * @param enterpriseCode
     * @return
     */
    public PageObject findIssueListByLoginJoin(String dateFrom,String dateTo,String appBy,String status,String issueNo,String enterpriseCode, final int... rowStartIdxAndCount);
    
    /**
     * 查询某部门某一年的实际领用金额及财务审核金额
     * modify by fyyang 20100419
     * add by fyyang 20100315
     * @param enterpriseCode
     * @param deptCode
     * @param year
     * @return 实际金额,财务审核金额
     */
    public String getYearIssuePriceByDept(String enterpriseCode,String deptCode,String itemCode);
    
    /**
     * 根据需求计划单id查询出已经入库的明细
     * add by fyyang 20100408
     * @param enterpriseCode
     * @param headId
     * @return
     */
    public PageObject getMaterialDetailForIssueSelect(String enterpriseCode, Long headId);
}
