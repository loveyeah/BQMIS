/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCDept;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueDetailsFacadeRemote;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJIssueHeadFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHead;
import power.ejb.resource.form.IssueHeadRegisterInfo;
import power.ejb.resource.form.MrpJPlanRequirementDetailInfo;
import power.ejb.resource.form.MrpJPlanRequirementHeadInfo;

/**
 * 领料单登记Implements
 *
 * @author
 * @version 1.0
 */
@Stateless
public class IssueHeadRegisterImpl implements IssueHeadRegister {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 领料单表头remote*/
	@EJB(beanName = "InvJIssueHeadFacade")
	InvJIssueHeadFacadeRemote headRemote;
	/** 领料单明细remote*/
	@EJB(beanName = "InvJIssueDetailsFacade")
	InvJIssueDetailsFacadeRemote detailsRemote;
	// 对应 ->commonInterface jar包删除
	/** 单位*/
	@EJB(beanName = "BpCMeasureUnitFacade")
	BpCMeasureUnitFacadeRemote unitRemote;

	/**
	 * 查询领料单
	 *
	 * @param issueNo 领料单编号
	 * @param flag checkbox标识
	 * @param deptId 领用部门
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return 领料单
	 */
	@SuppressWarnings("unchecked")
	public PageObject findIssueHead(String issueNo, String flag,
			String deptId, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding InvJIssueHead instance", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
			String sql = "SELECT\n" + "*\n" + "FROM\n" + "INV_J_ISSUE_HEAD A\n"
					+ "WHERE\n" + "A.IS_USE = 'Y' AND\n"
					+ "A.ENTERPRISE_CODE = '" + enterpriseCode + "' AND \n"
					+ "A.RECEIPT_DEP = '" + deptId + "' \n";
			String sqlCount = "SELECT\n" + "COUNT(*)\n" + "FROM\n"
					+ "INV_J_ISSUE_HEAD A\n" + "WHERE\n"
					+ "A.IS_USE = 'Y' AND\n" + "A.ENTERPRISE_CODE = '"
					+ enterpriseCode + "' AND \n" + "A.RECEIPT_DEP = '"
					+ deptId + "' \n";
			if (issueNo != null && !("".equals(issueNo))) {
				sql += "AND A.ISSUE_NO = '" + issueNo + "'\n";
				sqlCount += "AND A.ISSUE_NO = '" + issueNo + "'\n";
			}
			if ("0".equals(flag)) {
				sql += "AND A.ISSUE_STATUS IN ('0', '9') \n";
				sqlCount += "AND A.ISSUE_STATUS IN ('0', '9') \n";
			}
			sql +=" ORDER BY A.ISSUE_NO desc";
			List list = bll.queryByNativeSQL(sql, InvJIssueHead.class,
					rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			object.setList(list);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	/**
	 * 查询领料单审批列表
	 * modify by fyyang 090722
	 * modify by fyyang 091022 999999不做部门过滤
	 * @param issueNo 领料单编号
	 * @param flag checkbox标识
	 * @param deptId 领用部门
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return 领料单
	 */
	@SuppressWarnings("unchecked")
	public PageObject findIssueHeadApproveList(String deptCode, String enterpriseCode,String issueNo,String status,String workflowno,String workCode,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding InvJIssueHead instance", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
			String sql = 
				"select a.issue_head_id,\n" +
				"       a.issue_status,\n" + 
				"       a.issue_no,\n" + 
				"       a.project_code,\n" + 
				"       a.wo_no,\n" + 
				"       a.cost_item_id,\n" + 
				"       a.item_code,\n" + 
				"       a.mr_no,\n" + 
				"       a.receipt_by,\n" + 
				"       a.receipt_dep,\n" + 
				"       a.due_date,\n" + 
				"       a.fee_by_dep,\n" + 
				"       a.fee_by_special,\n" + 
				"       a.memo,\n" + 
				"       a.is_emergency,\n" + 
				"       a.last_modified_date,\n" + 
				"       a.work_flow_no,\n" + 
				"       a.plan_original_id,\n" + 
				"       GETDEPTNAME(a.receipt_dep) \n"+

			     "FROM\n" + "INV_J_ISSUE_HEAD A\n"
					+ "WHERE\n" + "A.IS_USE = 'Y' AND\n"
					+ "A.ENTERPRISE_CODE = '" + enterpriseCode + "'  \n";
				//	+ "AND A.RECEIPT_DEP = '" + deptCode + "' \n";
					
					//+" and A.issue_no like '%"+issueNo+"%' and A.issue_status='"+status+"'";
			String sqlCount = "SELECT\n" + "COUNT(*)\n" + "FROM\n"
					+ "INV_J_ISSUE_HEAD A\n" + "WHERE\n"
					+ "A.IS_USE = 'Y' AND\n" + "A.ENTERPRISE_CODE = '"
					+ enterpriseCode + "' " ;
//					"AND \n" + "A.RECEIPT_DEP = '"
//					+ deptCode + "' \n";
				sql += " AND A.work_flow_no in ("+workflowno+")\n";
				sqlCount += " AND A.work_flow_no in ("+workflowno+") \n";
				
				if(issueNo!=null&&!issueNo.equals(""))
				{
					sql+=" and A.issue_no like '%"+issueNo+"%'  \n";
					sqlCount += " and A.issue_no like '%"+issueNo+"%'  \n";
				}
				if(status!=null&&!status.equals(""))
				{
					sql+=" and A.issue_status='"+status+"'  \n";
					sqlCount += " and A.issue_status='"+status+"'  \n";
				}
				if(!workCode.equals("999999"))
				{
				if(deptCode!=null&&deptCode.length()>1)
				{
					//本部门领导审批：审批人与申请人在同一个公司且在同一个部门下
					//------//发电、实业、检修综合部审批：审批人与申请人在同一个公司下
					//modify by fyyang 091127
				// String sqlWhere="and A.Receipt_Dep like (decode(A.ISSUE_STATUS,'1','"+deptCode+"','3',decode(A.Plan_Original_Id,5,substr('"+deptCode+"',0,2)||'%',A.Receipt_Dep),A.Receipt_Dep)) \n";
					String sqlWhere="and A.Receipt_Dep like (decode(A.ISSUE_STATUS,'1','"+deptCode+"',A.Receipt_Dep)) \n";
					sql+=sqlWhere;
				 sqlCount +=sqlWhere;
				}
				}
			    
			sql +=" ORDER BY A.ISSUE_NO desc";
//			List list = bll.queryByNativeSQL(sql, InvJIssueHead.class,
//					rowStartIdxAndCount);
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			object.setList(list);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 查询领料单明细
	 *
	 * @param issueHeadId 领料单编号
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return 领料单明细
	 */
	@SuppressWarnings("unchecked")
	public PageObject findIssueHeadDetails(String issueHeadId,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding IssueHeadRegisterInfo instance", Level.INFO, null);
		PageObject object = new PageObject();
		try {
			// modify by ywliu ,将A.ITEM_ID改为A.ITEM_CODE 2009/7/6
			// 查询sql
			String sql = "SELECT\n" + "A.ISSUE_DETAILS_ID,\n"
					+ "A.APPLIED_COUNT,\n" + "A.APPROVED_COUNT,\n"
					+ "A.ACT_ISSUED_COUNT,\n" + "A.COST_ITEM_ID,\n"
					+ "A.ITEM_CODE,\n" + "B.MATERIAL_ID,\n" + "B.MATERIAL_NO,\n"
					+ "B.MATERIAL_NAME,\n" + "B.SPEC_NO,\n" + "B.PARAMETER,\n"
					+ "B.STOCK_UM_ID,\n"
					+ "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss'),\n"
					+ "(select sum(C.OPEN_BALANCE + C.RECEIPT + C.ADJUST - C.ISSUE) from inv_j_warehouse C where C.material_id = A.MATERIAL_ID group by C.material_id) as stock \n"//add by ywliu 20091019
					+" ,A.modify_memo \n" //add by fyyang 100112
					+", nvl(E.stdCost, 0),nvl(E.price, 0) \n"
					+ "FROM\n" + "INV_J_ISSUE_DETAILS A,\n"+
					//---------------------------------------------

					"(select D.Issue_Head_Id,\n" +
					"               t.material_id,\n" + 
					"               sum(t.trans_qty * t.std_cost) price,\n" + 
					"               decode(sum(t.trans_qty),\n" + 
					"                      0,\n" + 
					"                      0,\n" + 
					"                      sum(t.trans_qty * t.std_cost) / sum(t.trans_qty)) stdCost\n" + 
					"          from inv_j_transaction_his t, inv_j_issue_head D\n" + 
					"         where t.trans_id = 4\n" + 
					"           and t.order_no = D.Issue_No   and D.Is_Use='Y' \n" + 
					"         group by D.Issue_Head_Id, t.material_id) E, \n"

					//----------------------------------------------
					+ "INV_C_MATERIAL B \n" + "WHERE\n"
					+ "A.ISSUE_HEAD_ID = ? AND\n"+
					" A.Issue_Head_Id = E.Issue_Head_Id(+)\n" +
					"  and A.Material_Id = E.material_id(+) and\n"
					+ "A.MATERIAL_ID = B.MATERIAL_ID AND\n"
					+ "A.IS_USE = 'Y' AND\n" + "B.IS_USE = 'Y' AND\n"
					+ "A.ENTERPRISE_CODE = ? AND\n" + "B.ENTERPRISE_CODE = ?";
			String sqlCount = "SELECT\n" + "COUNT(1)\n" + "FROM\n"
					+ "INV_J_ISSUE_DETAILS A,\n" + "INV_C_MATERIAL B \n"
					+ "WHERE\n" + "A.ISSUE_HEAD_ID = ? AND\n"
					+ "A.MATERIAL_ID = B.MATERIAL_ID AND\n"
					+ "A.IS_USE = 'Y' AND\n" + "B.IS_USE = 'Y' AND\n"
					+ "A.ENTERPRISE_CODE = ? AND\n" + "B.ENTERPRISE_CODE = ?";

			List list = bll.queryByNativeSQL(sql, new Object[] { issueHeadId,
					enterpriseCode, enterpriseCode }, rowStartIdxAndCount);
			List<IssueHeadRegisterInfo> arrlist = new ArrayList<IssueHeadRegisterInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				IssueHeadRegisterInfo info = new IssueHeadRegisterInfo();
				Object[] data = (Object[]) it.next();
				if (null != data[0]) {
					info.setIssueDetailsId(Long.parseLong(data[0].toString()));
				}
				if (null != data[1]) {
					info
							.setAppliedCount(Double.parseDouble(data[1]
									.toString()));
				}else{
					info.setAppliedCount(0.0);
				}
				if (null != data[2]) {
					info.setApprovedCount(Double
							.parseDouble(data[2].toString()));
				}else{
					info.setApprovedCount(0.0);
				}
				if (null != data[3]) {
					info.setActIssuedCount(Double.parseDouble(data[3]
							.toString()));
					if(info.getApprovedCount() != null && info.getActIssuedCount() != null){
						info.setWaitCount(info.getApprovedCount() - info.getActIssuedCount());
					}else{
						info.setWaitCount(0.0);
					}
				}else{
					info.setActIssuedCount(0.0);
					info.setWaitCount(0.0);
				}
				if (null != data[4]) {
					info.setCostItemId(Long.parseLong(data[4].toString()));
				}
				if (null != data[5]) {
					info.setItemId(data[5].toString());// modify by ywliu , 2009/7/6
				}
				if (null != data[6]) {
					info.setMaterialId(Long.parseLong(data[6].toString()));
				}
				if (null != data[7]) {
					info.setMaterialNo(data[7].toString());
				}
				if (null != data[8]) {
					info.setMaterialName(data[8].toString());
				}
				if (null != data[9]) {
					info.setSpecNo(data[9].toString());
				}
				if (null != data[10]) {
					info.setParameter(data[10].toString());
				}
				if (null != data[11]) {
					info.setStockUmId(Long.parseLong(data[11].toString()));
					// 单位名称
//					PageObject units = comm.findUnitList(info.getStockUmId().toString(),enterpriseCode);
//					if(units.getList().size() > 0){
//						BpCMeasureUnit unit = (BpCMeasureUnit)units.getList().get(0);
//						info.setUnitName(unit.getUnitName());
//					}
					// 对应 ->commonInterface jar包删除
					BpCMeasureUnit unit = unitRemote.findById(info.getStockUmId());
					if(unit != null){
						info.setUnitName(unit.getUnitName());
					}
				}
				if (null != data[12]) {
					info.setLastModifiedDate(data[12].toString());
				}
				if (null != data[13]) {//add by ywliu 20091019
					info.setStock(data[13].toString());
				} else {
					info.setStock("0");
				}
				if(data[14]!=null)
				{
					info.setModifyMemo(data[14].toString());
				}
				if(data[15]!=null)
				{
					info.setUnitPrice(Double.parseDouble(data[15].toString()));
				}
				if(data[16]!=null)
				{
					info.setPrice(Double.parseDouble(data[16].toString()));
				}
				arrlist.add(info);
			}
			Long totalCount = Long.parseLong(bll
					.getSingal(
							sqlCount,
							new Object[] { issueHeadId, enterpriseCode,
									enterpriseCode }).toString());
			object.setList(arrlist);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 查询物料需求计划主表
	 *modify by fyyang 090623 增加了申请人查询条件
	 *modify by fyyang 090623 查已入库且本人或本部门的数据
	 * @param deptId 申请部门
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return 物料需求计划主表
	 */
	@SuppressWarnings("unchecked")
	public PageObject findPlanRequirementHead(String deptId,
			String enterpriseCode,String applyByName,String workCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding MrpJPlanRequirementHeadInfo instance", Level.INFO,
				null);
		try {
			PageObject object = new PageObject();
			// 查询sql
			//modify by ywliu  2009/7/6 将A.ITEM_ID改为A.ITEM_CODE
//			String sql = "SELECT\n" + "A.MR_NO,\n" + "A.WO_NO,\n"
//					+ "A.ITEM_CODE,\n" + "A.MR_BY,\n" + "A.MR_DEPT,\n"
//					+ "A.DUE_DATE,\n" + "A.COST_SPECIAL,\n" + "A.COST_DEPT,\n"
//					+ "A.REQUIREMENT_HEAD_ID,\n" + "A.LAST_MODIFIED_DATE,\n" + "A.plan_original_id,\n"
//					 //add by fyyang 090512
//					+"GETWORKERNAME(A.Mr_By),GETDEPTNAME(A.Cost_Dept),\n" 
//					+"getspecialname(A.Cost_Special),GETDEPTNAME(A.Mr_Dept),\n" 
//					+"A.MR_REASON  \n"
//					+ "FROM\n" + "MRP_J_PLAN_REQUIREMENT_HEAD A\n" + "WHERE\n"
//					+ "A.IS_USE = 'Y' AND\n" + "A.ENTERPRISE_CODE = ? AND\n"
//					+ "A.MR_DEPT = ? AND\n" + "A.MR_STATUS = '2' AND\n"
//					+ "A.MR_NO NOT IN\n" + 
//					"(SELECT\n" + "C.MR_NO\n" + "FROM\n"
//					+ "INV_J_ISSUE_HEAD C\n" + "WHERE\n"
//					+ "C.IS_USE = 'Y' AND\n" + "C.ENTERPRISE_CODE = ? AND\n"
//					+ "C.MR_NO IS NOT NULL) \n" ;
			String sql=
				"SELECT A.MR_NO,\n" +
				"       A.WO_NO,\n" + 
				"       A.ITEM_CODE,\n" + 
				"       A.MR_BY,\n" + 
				"       A.MR_DEPT,\n" + 
				"       A.DUE_DATE,\n" + 
				"       A.COST_SPECIAL,\n" + 
				"       A.COST_DEPT,\n" + 
				"       A.REQUIREMENT_HEAD_ID,\n" + 
				"       A.LAST_MODIFIED_DATE,\n" + 
				"       A.plan_original_id,\n" + 
				"       GETWORKERNAME(A.Mr_By),\n" + 
				"       GETDEPTNAME(A.Cost_Dept),\n" + 
				"       getspecialname(A.Cost_Special),\n" + 
				"       GETDEPTNAME(A.Mr_Dept),\n" + 
				"       A.MR_REASON,\n" +
				"  A.prj_no \n"+ //add by fyyang 20100507
				"  FROM MRP_J_PLAN_REQUIREMENT_HEAD A\n" + 
				" WHERE A.IS_USE = 'Y'\n" + 
				"   AND A.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
				"   AND ( A.MR_DEPT = '"+deptId+"' or a.mr_by='"+workCode+"')\n" + 
				"   AND A.MR_STATUS = '2'\n" + 
				"and a.requirement_head_id in\n" +
				"       (select distinct aa.REQUIREMENT_HEAD_ID\n" + 
				"          from MRP_J_PLAN_REQUIREMENT_DETAIL aa,\n" + 
				"               pur_j_plan_order b,\n" + 
				"               (select c.pur_no, c.material_id, sum(c.rec_qty) recqty\n" + 
				"                  from pur_j_arrival_details c\n" + 
				"                 where c.is_use = 'Y'\n" + 
				"                 and c.enterprise_code='"+enterpriseCode+"'\n" + 
				"                 group by c.pur_no, c.material_id) d\n" + 
				"         where aa.approved_qty <> nvl(aa.iss_qty, 0)\n" + 
				"           and nvl(aa.approved_qty, 0) <> 0\n" + 
				"           and aa.is_use = 'Y'\n" + 
				"           and aa.enterprise_code='"+enterpriseCode+"'\n" + 
				"           and aa.requirement_detail_id = b.requirement_detail_id\n" + 
				"           and d.pur_no = b.pur_no\n" + 
				"           and d.material_id = aa.material_id\n" + 
				"           and d.recqty >= aa.approved_qty\n" + 
				"           and (select count(1)\n" + 
				"                  from inv_j_issue_details e\n" + 
				"                 where\n" + 
				"                 e.requirement_detail_id = aa.requirement_detail_id\n" + 
				"              and e.material_id = aa.material_id\n" + 
				"              and e.is_use = 'Y') = 0) \n";


			if(applyByName!=null&&!applyByName.equals(""))
			{
			   sql+="  and  GETWORKERNAME(A.Mr_By) like '%"+applyByName.trim()+"%' \n";
			}
			sql+="ORDER BY A.MR_NO DESC";
//			String sqlCount = "SELECT COUNT(1)\n" + "FROM\n"
//					+ "MRP_J_PLAN_REQUIREMENT_HEAD A\n" + "WHERE\n"
//					+ "A.IS_USE = 'Y' AND\n" + "A.ENTERPRISE_CODE = ? AND\n"
//					+ "A.MR_DEPT = ? AND\n" + "A.MR_STATUS = '2' AND\n"
//					+ "A.MR_NO NOT IN\n" + "(SELECT\n" + "C.MR_NO\n" + "FROM\n"
//					+ "INV_J_ISSUE_HEAD C\n" + "WHERE\n"
//					+ "C.IS_USE = 'Y' AND\n" + "C.ENTERPRISE_CODE = ? AND\n"
//					+ "C.MR_NO IS NOT NULL)";
			String sqlCount = 
				"select count(1)\n" +
				" FROM MRP_J_PLAN_REQUIREMENT_HEAD A\n" + 
				"WHERE A.IS_USE = 'Y'\n" + 
				"  AND A.ENTERPRISE_CODE = 'hfdc'\n" + 
				"  AND  (A.MR_DEPT = '"+deptId+"'   or a.mr_by='"+workCode+"')\n" + 
				"  AND A.MR_STATUS = '2'\n" + 
				"and a.requirement_head_id in\n" +
				"       (select distinct aa.REQUIREMENT_HEAD_ID\n" + 
				"          from MRP_J_PLAN_REQUIREMENT_DETAIL aa,\n" + 
				"               pur_j_plan_order b,\n" + 
				"               (select c.pur_no, c.material_id, sum(c.rec_qty) recqty\n" + 
				"                  from pur_j_arrival_details c\n" + 
				"                 where c.is_use = 'Y'\n" + 
				"                 and c.enterprise_code='"+enterpriseCode+"'\n" + 
				"                 group by c.pur_no, c.material_id) d\n" + 
				"         where aa.approved_qty <> nvl(aa.iss_qty, 0)\n" + 
				"           and nvl(aa.approved_qty, 0) <> 0\n" + 
				"           and aa.is_use = 'Y'\n" + 
				"           and aa.enterprise_code='"+enterpriseCode+"'\n" + 
				"           and aa.requirement_detail_id = b.requirement_detail_id\n" + 
				"           and d.pur_no = b.pur_no\n" + 
				"           and d.material_id = aa.material_id\n" + 
				"           and d.recqty >= aa.approved_qty\n" + 
				"           and (select count(1)\n" + 
				"                  from inv_j_issue_details e\n" + 
				"                 where\n" + 
				"                 e.requirement_detail_id = aa.requirement_detail_id\n" + 
				"              and e.material_id = aa.material_id\n" + 
				"              and e.is_use = 'Y') = 0)\n";


			if(applyByName!=null&&!applyByName.equals(""))
			{
				sqlCount+="   and  GETWORKERNAME(A.Mr_By) like '%"+applyByName.trim()+"%' \n";
			}
//			List list = bll.queryByNativeSQL(sql, new Object[] {
//					enterpriseCode, deptId, enterpriseCode },
//					rowStartIdxAndCount);
			List list =bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<MrpJPlanRequirementHeadInfo> arrlist = new ArrayList<MrpJPlanRequirementHeadInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				MrpJPlanRequirementHeadInfo info = new MrpJPlanRequirementHeadInfo();
				Object[] data = (Object[]) it.next();
				if (null != data[0]) {
					info.setMrNo(data[0].toString());
				}
				if (null != data[1]) {
					info.setWoNo(data[1].toString());
				}
				if (null != data[2]) {
					info.setItemIdHead(data[2].toString());//modify by ywliu  2009/7/6
				}
				if (null != data[3]) {
					info.setMrBy(data[3].toString());
				}
				if (null != data[4]) {
					info.setMrDept(data[4].toString());
				}
				if (null != data[5]) {
					info.setDueDate(data[5].toString());
				}
				if (null != data[6]) {
					info.setCostSpecial(data[6].toString());
				}
				if (null != data[7]) {
					info.setCostDept(data[7].toString());
				}
				if (null != data[8]) {
					info.setRequimentHeadId(Long.parseLong(data[8].toString()));
				}
				if (null != data[9]) {
					info.setLastModifiedDate(data[9].toString());
				}
				if (null != data[10]) {
					info.setPlanOriginalId(Long.parseLong(data[10].toString()));
				}
				
				//add by fyyang 090512
				if(data[11]!=null)
				{
					info.setMrByName(data[11].toString());
				}
				if(data[12]!=null)
				{
					info.setCostDeptName(data[12].toString());
				}
				if(data[13]!=null)
				{
					info.setCostSpecialName(data[13].toString());
				}
				if(data[14]!=null)
				{
					info.setMrDeptName(data[14].toString());
				}
				if(data[15]!=null)
				{
					info.setMrReason(data[15].toString());
				}
				if(data[16]!=null)
				{
					//add by fyyang 20100507
					info.setProjectNo(data[16].toString());
				}
				//info.setProjectNo("");
				arrlist.add(info);
			}
//			Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
//					new Object[] { enterpriseCode, deptId, enterpriseCode })
//					.toString());
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			object.setList(arrlist);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	/**
	 * 根据需求计划单编号查询需求计划单详细
	 * @param mrNo 需求计划单编号
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public MrpJPlanRequirementHead findPlanRequirementHeadModel(String mrNo,String enterpriseCode)
	{
		try{
			String sql="select *\n" +
						"  from MRP_J_PLAN_REQUIREMENT_HEAD r\n" + 
						" where r.mr_no = '"+mrNo+"'\n" + 
						"   and r.enterprise_code = '"+enterpriseCode+"'\n" + 
						"   and r.is_use = 'Y'";
			List<MrpJPlanRequirementHead> list= bll.queryByNativeSQL(sql, MrpJPlanRequirementHead.class);
			if(list.size() >0)
			{
				return list.get(0);
			}
			else
			{
				return null;
			}
		}
		catch (RuntimeException e)
		{
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	/**
	 * 查询领料单需求明细
	 * 
	 * @param detailIds 申请单明细IDs
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
	 * @return 领料单需求明细
	 */
	@SuppressWarnings("unchecked")
	public PageObject findIssueRequimentDetail(String detailIds,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding IssueHeadRegisterInfo instance", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
			//modify by ywliu  2009/7/6 将A.ITEM_ID改为A.ITEM_CODE
			String sql = "SELECT\n" + "C.MATERIAL_NO,\n" + "C.MATERIAL_NAME,\n"
					+ "C.SPEC_NO,\n" + "C.PARAMETER,\n" + "C.STOCK_UM_ID,\n"
					+ "B.APPLIED_QTY,\n" + "B.APPROVED_QTY,\n" + "B.ISS_QTY,\n"
					+ "B.ITEM_CODE,\n" + "B.REQUIREMENT_DETAIL_ID, \n"
					+ "C.MATERIAL_ID, \n"
					+ "(select sum(A.OPEN_BALANCE + A.RECEIPT + A.ADJUST - A.ISSUE) from inv_j_warehouse A where A.material_id = C.MATERIAL_ID group by A.material_id) as stock \n"//add by ywliu 20091019
					+ "FROM\n"
					+ "MRP_J_PLAN_REQUIREMENT_DETAIL B,\n"
					+ "INV_C_MATERIAL C\n" + "WHERE\n" + "B.IS_USE = 'Y' AND\n"
					+ "C.IS_USE = 'Y' AND\n" + "B.ENTERPRISE_CODE = ? AND\n"
					+ "C.ENTERPRISE_CODE = ? AND\n"
					+ "b.requirement_detail_id in ( "+detailIds+" ) AND\n"
					+ "B.MATERIAL_ID = C.MATERIAL_ID \n"
			        +" and B.approved_qty<>0 \n";//addby fyyang 091210
			String sqlCount = "SELECT\n" + "COUNT(1)\n" + "FROM\n"
					+ "MRP_J_PLAN_REQUIREMENT_DETAIL B,\n"
					+ "INV_C_MATERIAL C\n" + "WHERE\n" + "B.IS_USE = 'Y' AND\n"
					+ "C.IS_USE = 'Y' AND\n" + "B.ENTERPRISE_CODE = ? AND\n"
					+ "C.ENTERPRISE_CODE = ? AND\n"
					+ " b.requirement_detail_id in ( "+detailIds+" ) AND\n"
					+ "B.MATERIAL_ID = C.MATERIAL_ID \n"
					 +"and B.approved_qty<>0 \n";//addby fyyang 091210
			List list = bll.queryByNativeSQL(sql, new Object[] {
					enterpriseCode, enterpriseCode },
					rowStartIdxAndCount);
			List<IssueHeadRegisterInfo> arrlist = new ArrayList<IssueHeadRegisterInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				IssueHeadRegisterInfo info = new IssueHeadRegisterInfo();
				Object[] data = (Object[]) it.next();
				if (null != data[0]) {
					info.setMaterialNo(data[0].toString());
				}
				if (null != data[1]) {
					info.setMaterialName(data[1].toString());
				}
				if (null != data[2]) {
					info.setSpecNo(data[2].toString());
				}
				if (null != data[3]) {
					info.setParameter(data[3].toString());
				}
				if (null != data[4]) {
					info.setStockUmId(Long.parseLong(data[4].toString()));
					// 单位名称
//					PageObject units = comm.findUnitList(info.getStockUmId().toString(),enterpriseCode);
//					if(units.getList().size() > 0){
//						BpCMeasureUnit unit = (BpCMeasureUnit)units.getList().get(0);
//						info.setUnitName(unit.getUnitName());
//					}
					// 对应 ->commonInterface jar包删除
					BpCMeasureUnit unit = unitRemote.findById(info.getStockUmId());
					if(unit != null){
						info.setUnitName(unit.getUnitName());
					}
				}
				if (null != data[5]) {
				}
				if (null != data[6]) {
					info.setAppliedCount(Double.parseDouble(data[6]
					         									.toString()));
					info.setApprovedCount(Double
							.parseDouble(data[6].toString()));
				}else{
					info.setApprovedCount(0.0);
					info.setAppliedCount(0.0);
				}
				if (null != data[7]) {
					info.setActIssuedCount(Double.parseDouble(data[7]
							.toString()));
					if(info.getApprovedCount() != null && info.getActIssuedCount() != null){
						info.setWaitCount(info.getApprovedCount() - info.getActIssuedCount());
					}else{
						info.setWaitCount(0.0);
					}
				}else{
					info.setActIssuedCount(0.0);
					info.setWaitCount(0.0);
				}
				if (null != data[8]) {
					info.setItemId(data[8].toString());
				}
				// 物料需求明细id
				if (null != data[9]) {
					info.setRequirementDetailId(Long.parseLong(data[9]
							.toString()));
				}
				if (null != data[10]) {
					info.setMaterialId(Long.parseLong(data[10].toString()));
				}
				if (null != data[11]) {
					info.setStock(data[11].toString());//add by ywliu 20091019
				} else {
					info.setStock("0");
				}
				arrlist.add(info);
			}
			Long totalCount = Long.parseLong(bll.getSingal(
					sqlCount,
					new Object[] { enterpriseCode, enterpriseCode
							}).toString());
			object.setList(arrlist);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 增加领料单和领料单明细数据
	 * @param head 领料单表头
	 * @param details 领料单明细
	 * @return 新规的领料单的流水号
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Long addIssueHeadAndDetails(InvJIssueHead head,
			List<InvJIssueDetails> details) {
		try {
			// 流水号
			Long id = bll.getMaxId("INV_J_ISSUE_HEAD", "ISSUE_HEAD_ID");
			// 新增领料单表头
			head.setIssueNo(getIssueNo(id));
			headRemote.save(head);
			Long detailId = bll.getMaxId("INV_J_ISSUE_DETAILS",
					"ISSUE_DETAILS_ID");
			// 新增领料单明细
			for (InvJIssueDetails detail : details) {
				// 领料单流水号
				detail.setIssueHeadId(id);
				// 流水号
				detail.setIssueDetailsId(detailId++);
				detailsRemote.save(detail);
			}
			return id;
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 更新领料单和领料单明细数据
	 * @param head 表头数据
	 * @param newDetails 新增明细数据
	 * @param upDetails 修改明细数据
	 * @param delDetails 删除明细数据
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateIssueHeadAndDetails(InvJIssueHead head,
			List<InvJIssueDetails> newDetails,
			List<InvJIssueDetails> upDetails, List<InvJIssueDetails> delDetails) {
		try {
			// 更新表头
			headRemote.update(head);
			Long detailId = bll.getMaxId("INV_J_ISSUE_DETAILS",
			"ISSUE_DETAILS_ID");
			//新增明细数据
			for (InvJIssueDetails detail : newDetails) {
				// 领料单号
				detail.setIssueHeadId(head.getIssueHeadId());
				// 流水号
				detail.setIssueDetailsId(detailId++);
				detailsRemote.save(detail);
			}
			// 更新明细数据
			for (InvJIssueDetails detail : upDetails) {
				detailsRemote.update(detail);
			}
			// 删除明细数据
			for (InvJIssueDetails detail : delDetails) {
				detailsRemote.update(detail);
			}
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 根据领料单编号删除领料单及其对应的领料单明细
	 * @param enterpriseCode 企业编码
	 * @param workerCode 工号
	 * @param issueHeadId 领料单id
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteIssueRecords(String enterpriseCode, String workerCode,
			String issueHeadId) {
		// 领料单id
		Long issueId = Long.parseLong(issueHeadId);
		// 删除表头数据
		InvJIssueHead head = headRemote.findById(issueId);
		deleteIssueHeadRecord(head, workerCode);
		// 检索明细
		List<InvJIssueDetails> details = detailsRemote
				.findByIssueHeadId(issueId);
		// 删除明细
		for (InvJIssueDetails detail : details) {
			deleteIssueDetailRecord(detail, workerCode);
		}
	}

	/**
	 * 根据人员工号找部门信息
	 * @param empCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HrCDept findDeptInfo(String empCode) {
		if (!"".equals(empCode)) {
			String sql = "SELECT A.DEPT_ID FROM HR_J_EMP_INFO A WHERE A.EMP_CODE ='"
					+ empCode + "'";
			List<Object> list = bll.queryByNativeSQL(sql);
			if (list.size() > 0) {
				String sqlN = "select *"
						+ "  from hr_c_dept d, hr_c_dept_type dt\n"
						+ " where d.dept_type_id = dt.dept_type_id\n"
						+ " and d.dept_id='" + list.get(0).toString() + "'";
				List<HrCDept> listDept = bll.queryByNativeSQL(sqlN,
						HrCDept.class);
				if (listDept.size() > 0) {
					return listDept.get(0);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 删除领料单表头数据
	 * @param head 表头记录
	 * @param workerCode 工号
	 */
	private void deleteIssueHeadRecord(InvJIssueHead head, String workerCode) {
		// 是否使用
		head.setIsUse("N");
		// 上次修改人
		head.setLastModifiedBy(workerCode);
		// 上次修改日期
		head.setLastModifiedDate(new Date());
		headRemote.update(head);
	}

	/**
	 * 删除领料单明细数据
	 * @param detail 表头记录
	 * @param workerCode 工号
	 */
	private void deleteIssueDetailRecord(InvJIssueDetails detail,
			String workerCode) {
		// 是否使用
		detail.setIsUse("N");
		// 上次修改人
		detail.setLastModifiedBy(workerCode);
		// 上次修改日期
		detail.setLastModifiedDate(new Date());
		detailsRemote.update(detail);
	}
	/**
	 * 生成领料单编号
	 * @param issueId 流水号
	 */
	private String  getIssueNo(Long issueId){
		String issueNo = "LL";
		String id = String.valueOf(issueId);
		if(id.length() > 6){
			issueNo += id.substring(0,6);
		}else{
			String pad = "000000";
			issueNo +=pad.substring(0, 6 - id.length()) + id;
		}
		return issueNo;
	}
	
     /*
	 * 查询领料单信息(即物资领料单综合查询<专用>)
	 * modify by fyyang 091120
	 * @param fromDate 开始日期
	 * @param toDate 结束日期
	 * @param appBy 申请人
	 * @param acceptDept 申请部门
	 * @param materialName 物料名称
	 * @param receiveStatus 接收状态
	 * @param issueStatus 审批状态
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 *            动态查询参数(开始行和查询行)
	 * @return 领料单      
	 * @author yiliu    05/15/09
	 */
	public PageObject getIssueInfo(String fromDate, String toDate,
			 String appBy, String acceptDept,
			String materialName, String receiveStatus, String issueStatus,
			String queryType, String workCode,//add by fyyang 091119
			String enterpriseCode, String issueNo,int... rowStartIdxAndCount) {
		try {
			PageObject object = new PageObject();
			StringBuffer sql = new StringBuffer();
			sql.append(" select DISTINCT A.ISSUE_HEAD_ID,A.ISSUE_NO,A.PROJECT_CODE, ");
			sql.append(" A.WO_NO,A.COST_ITEM_ID,A.ITEM_CODE,A.MR_NO,A.RECEIPT_BY,");
			sql.append(" GETDEPTNAME(A.RECEIPT_DEP)||'/'||A.RECEIPT_DEP RECEIPT_DEP,A.DUE_DATE,A.FEE_BY_DEP,A.FEE_BY_SPECIAL,A.MEMO, ");
			sql.append(" A.GET_REAL_PERSON,A.CHECKED_BY,A.CHECKED_DATE,A.WORK_FLOW_NO, ");
			sql.append(" A.ISSUE_STATUS,A.IS_EMERGENCY,A.LAST_MODIFIED_BY,A.LAST_MODIFIED_DATE, ");
			sql.append("decode( (select nvl(sum(f.act_issued_count),0) from INV_J_ISSUE_DETAILS f where  f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y'),0,'1',");
			sql.append("(select sum(f.approved_count) from INV_J_ISSUE_DETAILS f where  f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y'),'2','3') as  ENTERPRISE_CODE,");
			//sql.append(" '1' as  ENTERPRISE_CODE," );
		    sql.append("A.IS_USE,A.PLAN_ORIGINAL_ID,E.REQUIREMENT_HEAD_ID,A.REF_ISSUE_NO ");  //add REF_ISSUE_NO 090709 fyyang
			sql.append(" from INV_J_ISSUE_HEAD A,HR_C_DEPT B,INV_C_MATERIAL C,INV_J_ISSUE_DETAILS D,MRP_J_PLAN_REQUIREMENT_DETAIL E");
			sql.append(" where A.IS_USE = 'Y' and A.ENTERPRISE_CODE = '" + enterpriseCode +"' ");
			sql.append(" and to_char(A.Due_Date,'yyyy-mm-dd') >='" +fromDate + "'");
			sql.append(" and to_char(A.Due_Date,'yyyy-mm-dd') <='" +toDate + "'");

			sql.append(" and D.MATERIAL_ID = C.MATERIAL_ID(+) and A.ISSUE_HEAD_ID = D.ISSUE_HEAD_ID(+) " +
					"and D.REQUIREMENT_DETAIL_ID = E.REQUIREMENT_DETAIL_ID(+)  ");
			if(appBy != null && !("".equals(appBy)))
			{
				sql.append(" and A.RECEIPT_BY ='" + appBy +"'");
			}
			if(acceptDept != null && !("".equals(acceptDept)))
			{
				sql.append(" and B.DEPT_NAME like '%" + acceptDept +"%'");
				sql.append(" and B.DEPT_CODE = A.RECEIPT_DEP");
			}
			if(materialName != null && !("".equals(materialName)))
			{
				sql.append(" and C.MATERIAL_NAME like '%" + materialName +"%'");
				
			}
			if(receiveStatus != null && !("".equals(receiveStatus)))
			{
				if(receiveStatus.equals("1"))
				{
					//sql.append(" and D.ACT_ISSUED_COUNT = 0 ");
					sql.append(" \n and  (select count(1) from INV_J_ISSUE_DETAILS f where f.act_issued_count=0  and f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y')\n" +
					"   =(select count(1) from INV_J_ISSUE_DETAILS f where  f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y') \n");
				}
				else if(receiveStatus.equals("3"))
				{

					sql.append("and  (select sum(f.approved_count) from INV_J_ISSUE_DETAILS f where  f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y')\n" );
					sql.append("     > (select sum(f.act_issued_count) from INV_J_ISSUE_DETAILS f where  f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y')\n" ); 
					sql.append("     and (select sum(f.act_issued_count) from INV_J_ISSUE_DETAILS f where  f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y')>0 \n");

					//sql.append(" and D.ACT_ISSUED_COUNT !=0 and D.ACT_ISSUED_COUNT < D.APPROVED_COUNT ");
				}
				else if(receiveStatus.equals("2"))
				{
					//sql.append(" and D.ACT_ISSUED_COUNT = D.APPROVED_COUNT and D.ACT_ISSUED_COUNT <> 0");

					sql.append(" \n and  (select count(1) from INV_J_ISSUE_DETAILS f where f.ACT_ISSUED_COUNT<>0 and f.act_issued_count=f.approved_count and f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y')\n" +
					"   =(select count(1) from INV_J_ISSUE_DETAILS f where  f.issue_head_id=A.ISSUE_HEAD_ID and f.is_use='Y') \n");

				}
				else
				;
			}
			if(issueStatus != null && !("".equals(issueStatus)))
			{
				sql.append(" and A.ISSUE_STATUS = '"+issueStatus + "'\n");
			}
			if(queryType.equals("1"))
			{
			 //我上报的	
				// modified by liuyi 20100317 我的 上报的及未上报的
//				sql.append(" and A.RECEIPT_BY ='" + workCode +"'    and A.issue_status<>'0' \n");
				sql.append(" and A.RECEIPT_BY ='" + workCode +"'   \n");
			}
			else if(queryType.equals("2"))
			{
				//我审批的
				sql.append(" and a.work_flow_no  in(select distinct a.entry_id from wf_c_entry a, wf_j_historyoperation b \n " );
				sql.append(" where a.entry_id = b.entry_id \n" );
				sql.append(" and (a.flow_type = 'hfResourceGetGDZC-v1.0' or a.flow_type = 'hfResourceGetSC-v1.0' )\n" );
				sql.append(" and b.caller = '"+workCode+"')  \n" );
				sql.append(" and A.issue_status not in ('0','1')");
				
			}else if(queryType.equals("4"))
			{
				//本部门的
				sql.append(" and A.RECEIPT_DEP = (select b.dept_code from hr_j_emp_info a,hr_c_dept b  where a.dept_id = b.dept_id and  a.emp_code = '"+workCode+"')\n");
			}
			
			if(issueNo!=null&&!issueNo.trim().equals(""))
			{
				sql.append("  and A.ISSUE_NO  like '%"+issueNo.trim()+"%'  ");
			}
			//String sqlCount = sql.toString().replaceFirst("select.*? from ", "SELECT COUNT(DISTINCT A.ISSUE_HEAD_ID) FROM ");
			String sqlCount=" select count(*) from ("+sql.toString()+") tt";
			
			List list = bll.queryByNativeSQL(sql.toString(), InvJIssueHead.class,
					rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			object.setList(list);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	/* 
	 * 根据登陆人查询领料单
	 * add by ywliu 20091110
	 */
	public PageObject findIssueListByLogin(String dateFrom, String dateTo,
			String appBy, String status,String issueNo,String enterpriseCode, int... rowStartIdxAndCount) {
		LogUtil.log("finding InvJIssueHead instance", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
			String sql = "SELECT\n" + "*\n" + "FROM\n" + "INV_J_ISSUE_HEAD A\n"
					+ "WHERE\n" + "A.IS_USE = 'Y' AND\n"
					+ "A.ENTERPRISE_CODE = '" + enterpriseCode + "'  \n";
			String sqlCount = "SELECT\n" + "COUNT(*)\n" + "FROM\n"
					+ "INV_J_ISSUE_HEAD A\n" + "WHERE\n"
					+ "A.IS_USE = 'Y' AND\n" + "A.ENTERPRISE_CODE = '"
					+ enterpriseCode + "' " ;
			if (dateFrom != null && !"".equals(dateFrom)) {
				sql += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') >='" +dateFrom + "' \n";
				sqlCount += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') >='" +dateFrom + "' \n";
			}
			
			if (dateTo != null && !dateTo.equals("")) {
				sql += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') <='" +dateTo + "' \n";
				sqlCount += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') <='" +dateTo + "' \n";
			}
			
			if(issueNo!=null&&!issueNo.equals("")) {
				sql+=" and A.issue_no like '%"+issueNo+"%'  \n";
				sqlCount += " and A.issue_no like '%"+issueNo+"%'  \n";
			}
			
			if(status!=null&&!status.equals("")) {
				sql+=" and A.issue_status='"+status+"'  \n";
				sqlCount += " and A.issue_status='"+status+"'  \n";
			}
			
			if(appBy != null && !("".equals(appBy))) {
				sql+=" and A.RECEIPT_BY ='"+appBy+"'  \n";
				sqlCount += " and A.RECEIPT_BY ='"+appBy+"'  \n";
			}
			
			sql +=" ORDER BY A.ISSUE_NO desc";
			List list = bll.queryByNativeSQL(sql, InvJIssueHead.class,
					rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			object.setList(list);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	/* 
	 * 根据登陆人查询登陆人参与审批的领料单
	 * add by ywliu 20091110
	 */
	public PageObject findIssueListByLoginJoin(String dateFrom, String dateTo,
			String appBy, String status,String issueNo,String enterpriseCode, int... rowStartIdxAndCount) {
		LogUtil.log("finding InvJIssueHead instance", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
			String sql = "SELECT\n" + "*\n" + "FROM\n" + "INV_J_ISSUE_HEAD A\n"
					+ "WHERE\n" + "A.IS_USE = 'Y' AND\n"
					+ "A.ENTERPRISE_CODE = '" + enterpriseCode + "'  \n";
			String sqlCount = "SELECT\n" + "COUNT(*)\n" + "FROM\n"
					+ "INV_J_ISSUE_HEAD A\n" + "WHERE\n"
					+ "A.IS_USE = 'Y' AND\n" + "A.ENTERPRISE_CODE = '"
					+ enterpriseCode + "' " ;
			if (dateFrom != null && !"".equals(dateFrom)) {
				sql += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') >='" +dateFrom + "' \n";
				sqlCount += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') >='" +dateFrom + "' \n";
			}
			
			if (dateTo != null && !dateTo.equals("")) {
				sql += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') <='" +dateTo + "' \n";
				sqlCount += " and to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') <='" +dateTo + "' \n";
			}
			
			if(issueNo!=null&&!issueNo.equals("")) {
				sql+=" and A.issue_no like '%"+issueNo+"%'  \n";
				sqlCount += " and A.issue_no like '%"+issueNo+"%'  \n";
			}
			
			if(status!=null&&!status.equals("")) {
				sql+=" and A.issue_status='"+status+"'  \n";
				sqlCount += " and A.issue_status='"+status+"'  \n";
			}
			
			if(appBy != null && !("".equals(appBy))) {
				sql += " and a.work_flow_no in(select distinct a.entry_id from wf_c_entry a, wf_j_historyoperation b \n " +
					" where a.entry_id = b.entry_id \n" +
					" and (a.flow_type = 'hfResourceGetGDZC-v1.0' or a.flow_type = 'hfResourceGetSC-v1.0' or a.flow_type = 'hfResourceGetXZ-v1.0')\n" +
	                " and b.caller = '"+appBy+"') ";
				sqlCount += " and a.work_flow_no in(select distinct a.entry_id from wf_c_entry a, wf_j_historyoperation b \n " +
					" where a.entry_id = b.entry_id \n" +
					" and (a.flow_type = 'hfResourceGetGDZC-v1.0' or a.flow_type = 'hfResourceGetSC-v1.0' or a.flow_type = 'hfResourceGetXZ-v1.0')\n" +
	                " and b.caller = '"+appBy+"') ";
			}
			
			sql +=" ORDER BY A.ISSUE_NO desc";
			List list = bll.queryByNativeSQL(sql, InvJIssueHead.class,
					rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			object.setList(list);
			object.setTotalCount(totalCount);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
	
	/**
	 * add by fyyang 20100315
	 * @param enterpriseCode
	 * @param deptCode
	 * @param year
	 * @return
	 */
	public String getYearIssuePriceByDept(String enterpriseCode,String deptCode,String itemCode)
	{	
	//add by  lding  20100412 材料费修理费下的子节点累加费用
	String subitemCode;
	String str="   and a.item_code like '"+itemCode+"'" ;
	if(itemCode.length()>=15){
		 subitemCode = itemCode.substring(0,15);
		if (subitemCode.equals("002021007003015")||subitemCode.equals("002021007003001"))
			//材料费修理费下的子节点累加费用
			str="   and (a.item_code like '002021007003015%' or a.item_code like '002021007003001%') "; 
		}
	if(itemCode.length()>=12){
		 subitemCode = itemCode.substring(0,12);
		if (subitemCode.equals("002021001007"))
			//化学药品下的子节点累加费用
			str="   and a.item_code like '002021001007%'  "; 
		}
		String sql=
			"select (select round(nvl(sum(t.trans_qty * t.std_cost), 0),2)\n" +
			"  from inv_j_transaction_his t, inv_j_issue_head a\n" + 
			" where t.trans_id = 4\n" + 
			"   and t.order_no = a.issue_no\n" + 
			"   and a.receipt_dep = '"+deptCode+"'\n" + 
			"   and to_char(t.last_modified_date, 'yyyy') = to_char(sysdate,'yyyy')\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and a.plan_original_id not in(1,2)\n" + //不包含专项物资和固定资产类
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'  \n" +
			str+
			"\n) price1,\n"+
			
			"(select round(nvl(sum(t.trans_qty * t.std_cost), 0),2)\n" +
			"  from inv_j_transaction_his t, inv_j_issue_head a\n" + 
			" where t.trans_id = 4\n" + 
			"   and t.order_no = a.issue_no\n" + 
			"   and a.receipt_dep = '"+deptCode+"'\n" + 
			"   and to_char(t.check_date, 'yyyy') = to_char(sysdate,'yyyy')\n" + 
			"   and t.check_status='C' \n"+
			"   and t.is_use = 'Y'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and a.plan_original_id not in(1,2)\n" + //不包含专项物资和固定资产类
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"' \n" +
			str+
			"\n ) price2 \n"+
			" from dual ";
		
		Object [] obj=(Object []) bll.getSingal(sql);
		return obj[0].toString()+","+obj[1].toString();

	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject getMaterialDetailForIssueSelect(String enterpriseCode, Long headId)
	{
		PageObject result = new PageObject();
		String sql=
			"SELECT a.REQUIREMENT_DETAIL_ID as requirementDetailId,\n" +
			"       b.MATERIAL_NO as materialNo,\n" + 
			"       b.MATERIAL_NAME as materialName,\n" + 
			"       b.SPEC_NO as materSize,\n" + 
			"       a.APPLIED_QTY as appliedQty,\n" + 
			"       a.APPROVED_QTY as apprpvedQty,\n" + 
			"       a.ISS_QTY as issQty,\n" + 
			"       a.ESTIMATED_PRICE as estimatedPrice,\n" + 
			"       (a.APPLIED_QTY * a.ESTIMATED_PRICE) as estimatedSum,\n" + 
			"       tempSum.purQty as purQty,\n" + 
			"       b.STOCK_UM_ID as stockUmName,\n" + 
			"       a.USAGE as usage,\n" + 
			"       a.memo as memo,\n" + 
			"       to_char(a.DUE_DATE, 'YYYY-mm-dd') as needDate,\n" + 
			"       b.PARAMETER as parameter,\n" + 
			"       b.DOC_NO as docNo,\n" + 
			"       c.WHS_NAME as whsName,\n" + 
			"       b.QUALITY_CLASS as qualityClass,\n" + 
			"       temp.stock as\n" + 
			"  left, tempSum.tempNum as tempNum, a.ITEM_CODE as itemId, a.cancel_reason, a.is_use, a.supplier, b.factory, a.modify_memo\n" + 
			"  FROM MRP_J_PLAN_REQUIREMENT_DETAIL a,\n" + 
			"       INV_C_MATERIAL b,\n" + 
			"       INV_C_WAREHOUSE c,\n" + 
			"       (SELECT MATERIAL_ID as id,\n" + 
			"               sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock\n" + 
			"          FROM INV_J_WAREHOUSE, INV_C_WAREHOUSE\n" + 
			"         WHERE INV_J_WAREHOUSE.IS_USE = 'Y'\n" + 
			"           AND INV_J_WAREHOUSE.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"           AND INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO\n" + 
			"           AND INV_C_WAREHOUSE.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"           AND INV_C_WAREHOUSE.IS_USE = 'Y'\n" + 
			"           AND INV_C_WAREHOUSE.IS_INSPECT = 'N'\n" + 
			"         GROUP BY MATERIAL_ID) temp,\n" + 
			"       (SELECT d.REQUIREMENT_DETAIL_ID as id,\n" + 
			"               sum(d.MR_QTY) as purQty,\n" + 
			"               sum(e.INS_QTY) as tempNum\n" + 
			"          FROM PUR_J_PLAN_ORDER d,\n" + 
			"               PUR_J_ORDER_DETAILS e,\n" + 
			"               (SELECT a.REQUIREMENT_DETAIL_ID\n" + 
			"                  FROM MRP_J_PLAN_REQUIREMENT_DETAIL a,\n" + 
			"                       INV_C_MATERIAL b,\n" + 
			"                       INV_C_WAREHOUSE c,\n" + 
			"                       (SELECT MATERIAL_ID as id,\n" + 
			"                               sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock\n" + 
			"                          FROM INV_J_WAREHOUSE, INV_C_WAREHOUSE\n" + 
			"                         WHERE INV_J_WAREHOUSE.IS_USE = 'Y'\n" + 
			"                           AND INV_J_WAREHOUSE.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"                           AND INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO\n" + 
			"                           AND INV_C_WAREHOUSE.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"                           AND INV_C_WAREHOUSE.IS_USE = 'Y'\n" + 
			"                           AND INV_C_WAREHOUSE.IS_INSPECT = 'N'\n" + 
			"                         GROUP BY MATERIAL_ID) temp\n" + 
			"                 WHERE a.REQUIREMENT_HEAD_ID = '"+headId+"'\n" + 
			"                   AND a.MATERIAL_ID = b.MATERIAL_ID\n" + 
			"                   AND a.MATERIAL_ID = temp.id(+)\n" + 
			"                   AND b.DEFAULT_WHS_NO = c.WHS_NO(+)\n" + 
			"                   AND a.IS_USE='Y'\n" + 
			"                   AND b.IS_USE = 'Y'\n" + 
			"                   AND c.IS_USE(+) = 'Y'\n" + 
			"                   AND a.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"                   AND b.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"                   AND c.ENTERPRISE_CODE(+) = '"+enterpriseCode+"') g\n" + 
			"         WHERE d.IS_USE = 'Y'\n" + 
			"           AND e.IS_USE = 'Y'\n" + 
			"           AND d.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"           AND e.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"           AND d.REQUIREMENT_DETAIL_ID = g.REQUIREMENT_DETAIL_ID\n" + 
			"           AND d.PUR_ORDER_DETAILS_ID = e.PUR_ORDER_DETAILS_ID\n" + 
			"         GROUP BY d.REQUIREMENT_DETAIL_ID) tempSum\n" + 
			" WHERE a.REQUIREMENT_HEAD_ID = '"+headId+"'\n" + 
			"   AND tempSum.id(+) = a.REQUIREMENT_DETAIL_ID\n" + 
			"   AND a.MATERIAL_ID = b.MATERIAL_ID\n" + 
			"   AND a.MATERIAL_ID = temp.id(+)\n" + 
			"   AND b.DEFAULT_WHS_NO = c.WHS_NO(+)\n" + 
			"   AND a.IS_USE in ('Y')\n" + 
			"   AND b.IS_USE = 'Y'\n" + 
			"   AND c.IS_USE(+) = 'Y'\n" + 
			"   AND a.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"   AND b.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
			"   AND c.ENTERPRISE_CODE(+) = '"+enterpriseCode+"'\n" + 
			"   and a.requirement_detail_id in\n" + 
			"   (\n" + 
			"      select distinct aa.requirement_detail_id\n" + 
			"          from MRP_J_PLAN_REQUIREMENT_DETAIL aa,\n" + 
			"               pur_j_plan_order b,\n" + 
			"               (select c.pur_no, c.material_id, sum(c.rec_qty) recqty\n" + 
			"                  from pur_j_arrival_details c\n" + 
			"                 where c.is_use = 'Y'\n" + 
			"                 and c.enterprise_code='"+enterpriseCode+"'\n" + 
			"                 group by c.pur_no, c.material_id) d\n" + 
			"         where aa.approved_qty <> nvl(aa.iss_qty, 0)\n" + 
			"           and nvl(aa.approved_qty, 0) <> 0\n" + 
			"           and aa.is_use = 'Y'\n" + 
			"           and aa.enterprise_code='"+enterpriseCode+"'\n" + 
			"           and aa.requirement_detail_id = b.requirement_detail_id\n" + 
			"           and d.pur_no = b.pur_no\n" + 
			"           and d.material_id = aa.material_id\n" + 
			"           and d.recqty >= aa.approved_qty\n" + 
			"           and (select count(1)\n" + 
			"                  from inv_j_issue_details e\n" + 
			"                 where\n" + 
			"                 e.requirement_detail_id = aa.requirement_detail_id\n" + 
			"              and e.material_id = aa.material_id\n" + 
			"              and e.is_use = 'Y') = 0\n" + 
			"\n" + 
			"   )\n" + 
			" ORDER BY a.REQUIREMENT_DETAIL_ID";

		List<MrpJPlanRequirementDetailInfo> list = bll.queryByNativeSQL(sql);
		List<MrpJPlanRequirementDetailInfo> arrlist = new ArrayList<MrpJPlanRequirementDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			MrpJPlanRequirementDetailInfo model = new MrpJPlanRequirementDetailInfo();
			Object[] data = (Object[]) it.next();
			model.setRequirementDetailId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setMaterialNo(data[1].toString());
			if (data[2] != null)
				model.setMaterialName(data[2].toString());
			if (data[3] != null)
				model.setMaterSize(data[3].toString());
			if (data[4] != null)
				model.setAppliedQty(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
				model.setApprovedQty(Double.parseDouble(data[5].toString()));
			if (data[6] != null)
				model.setIssQty(Double.parseDouble(data[6].toString()));
			if (data[7] != null)
				model.setEstimatedPrice(Double.parseDouble(data[7].toString()));
			if (data[8] != null)
				model.setEstimatedSum(Double.parseDouble(data[8].toString()));
			if (data[9] != null)
				model.setPurQty(Double.parseDouble(data[9].toString()));
			if (data[10] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[10]
						.toString()));
				model.setStockUmName(bp.getUnitName());
			}
			if (data[11] != null)
				model.setUsage(data[11].toString());
			if (data[12] != null)
				model.setMemo(data[12].toString());
			if (data[13] != null)
				model.setNeedDate(data[13].toString());
			if (data[14] != null)
				model.setParameter(data[14].toString());
			if (data[15] != null)
				model.setDocNo(data[15].toString());
			if (data[16] != null)
				model.setWhsName(data[16].toString());
			if (data[17] != null)
				model.setQualityClass(data[17].toString());
			if (data[18] != null)
				model.setLeft(Double.parseDouble(data[18].toString()));
			if (data[19] != null)
				model.setTempNum(Double.parseDouble(data[19].toString()));
			if (data[20] != null)
				model.setItemId(data[20].toString());
			
			if (data[21] != null)
				model.setCancelReason(data[21].toString());
			if (data[22] != null)
				model.setUseFlag(data[22].toString());
			// add by fyyang 091214
			if (data[23] != null)
				model.setSupplier(data[23].toString());
			if (data[24] != null)
				model.setFactory(data[24].toString());
			if (data[25] != null)
				model.setModifyMemo(data[25].toString());
			arrlist.add(model);
		}
		if (arrlist.size() > 0) {
			result.setList(arrlist);
		}
		return result;
	}
	
	
}
