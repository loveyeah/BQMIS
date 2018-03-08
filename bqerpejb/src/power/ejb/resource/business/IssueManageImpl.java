/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvCLocation;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCWarehouse;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueDetailsFacadeRemote;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJIssueHeadFacadeRemote;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementDetail;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.form.IssueHeaderInfo;
import power.ejb.resource.form.IssueMaterialDetailInfo;
import power.ejb.resource.form.IssueMaterialDetailPrintInfo;
import power.ejb.resource.form.TransActionHisInfo;

/**
 * 出库管理facade
 * 
 * @author qzhang
 */
@Stateless
public class IssueManageImpl implements IssueManage {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 领料单明细服务 */
	@EJB(beanName = "InvJIssueDetailsFacade")
	private InvJIssueDetailsFacadeRemote issueDetailRemote;
	@EJB(beanName = "InvJIssueHeadFacade")
	private InvJIssueHeadFacadeRemote issueHeadRemote;
	/** 物资需求计划明细 */
	@EJB(beanName = "MrpJPlanRequirementDetailFacade")
	private MrpJPlanRequirementDetailFacadeRemote planDetailRemote;
	/** 批号记录 */
	@EJB(beanName = "InvJLotFacade")
	private InvJLotFacadeRemote lotRemote;
	/** 事务历史 */
	@EJB(beanName = "InvJTransactionHisFacade")
	private InvJTransactionHisFacadeRemote transHisRemote;
	/** 库存物料记录 */
	@EJB(beanName = "InvJWarehouseFacade")
	private InvJWarehouseFacadeRemote wareRemote;
	/** 库位物料记录 */
	@EJB(beanName = "InvJLocationFacade")
	private InvJLocationFacadeRemote locationRemote;
	/** 入库(用于库存物资查询) */
	@EJB(beanName = "PurchaseWarehouseImpl")
	private PurchaseWarehouse purchaseWarehouse;
	// 对应 ->commonInterface jar包删除
	/** 单位 */
	@EJB(beanName = "BpCMeasureUnitFacade")
	BpCMeasureUnitFacadeRemote unitRemote;
	//领料单 add by fyyang 090708
	@EJB(beanName = "IssueHeadRegisterImpl")
	IssueHeadRegister issueRemote;
	@EJB(beanName = "InvCMaterialFacade")
	protected InvCMaterialFacadeRemote invCMaterialRemote;
	
	private static String ZERO_POINT = "0.00";

	/**
	 * 确认是否正在结账
	 * 
	 * @param transCode
	 *            事务编码
	 * @return true 如果正在结帐
	 */
	@SuppressWarnings("unchecked")
	public boolean isBalanceNow(String transCode) {
		// 查找结算状态
		String sql = "SELECT BALANCE_STATUS  FROM INV_J_BALANCE  "
				+ "\n"
				+ "WHERE TRANS_HIS_MAXID =(SELECT MAX(TRANS_HIS_MAXID)   "
				+ "\n"
				+ "FROM INV_J_BALANCE  WHERE BALANCE_TYPE = 'M'   AND IS_USE = 'Y')";
		// 结算状态
		String balanceStatus = (String) bll.getSingal(sql);
		if (balanceStatus == null) {
			return false;
		}
		;
		// 结算状态ok，返回false，不在结帐
		if ("OK".equals(balanceStatus.toUpperCase())) {
			return false;
		} else if ("ON".equals(balanceStatus.toUpperCase())) {
			// 判断事务是否对结帐有影响
			sql = "SELECT IS_OPEN_BALANCE, IS_RECEIVE, IS_ADJUST, IS_ISSUES "
					+ "\n" + "FROM INV_C_TRANSACTION " + "\n"
					+ "where TRANS_CODE ='" + transCode + "'\n"
					+ "and IS_USE = 'Y'";
			List list = bll.queryByNativeSQL(sql);
			if (list.size() > 0) {
				Object[] data = (Object[]) list.get(0);
				// 没影响的话返回false
				if ("N".equals(data[0].toString())
						&& "N".equals(data[1].toString())
						&& "N".equals(data[2].toString())
						&& "N".equals(data[3].toString())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 检索所有领料单表头数据
	 * modify by fyyang 090619 计划来源字段改为单据类型字段
	 * modify by fyyang 090707 费用来源id改为费用来源编码
	 * @param rowStartIdxAndCount
	 *            分页
	 * @param enterpriseCode
	 *            企业编码
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllIssueHeadDatas(String enterpriseCode,
			int... rowStartIdxAndCount) {
		LogUtil.log("finding all InvJIssueHead instances", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			// 修改SQl 增加申请人和部门名称 add by ywliu 2009/7/10
			String sql = "SELECT DISTINCT H.ISSUE_HEAD_ID,H.ISSUE_NO,H.DUE_DATE,"
					+ "H.PLAN_ORIGINAL_ID,H.ITEM_CODE,H.MEMO,H.RECEIPT_DEP, "
					+ "H.RECEIPT_BY ,H.WO_NO, H.FEE_BY_DEP, H.IS_EMERGENCY,\n"
					+ "to_char(H.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss'), \n"
					+ "H.MR_NO, \n"
					+ "getdeptname(H.RECEIPT_DEP) as deptname,\n"
					+ "getworkername(H.RECEIPT_BY) as receiptName\n"
					+ "FROM INV_J_ISSUE_DETAILS D,INV_J_ISSUE_HEAD H,"
					+ "MRP_J_PLAN_REQUIREMENT_HEAD M \n"
					+ "WHERE D.ISSUE_HEAD_ID = H.ISSUE_HEAD_ID \n"
					+ "AND H.MR_NO = M.MR_NO(+) \n"
					+ "AND (D.APPROVED_COUNT - D.ACT_ISSUED_COUNT) > 0 \n"
					+ "AND D.IS_USE = 'Y' \n"
					+ "AND H.ISSUE_STATUS = '2' \n"
					+ "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND H.IS_USE = 'Y' \n"
					+ "AND H.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND M.IS_USE(+) = 'Y' \n"
					+ "AND M.ENTERPRISE_CODE(+) = '" + enterpriseCode + "'\n"
					+ " order by H.ISSUE_NO desc";// modify ywliu 2009/6/30 按照领料单号的倒叙排列
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			String sqlCount = "SELECT DISTINCT COUNT(DISTINCT H.ISSUE_HEAD_ID)\n"
					+ "FROM INV_J_ISSUE_DETAILS D,INV_J_ISSUE_HEAD H,"
					+ "MRP_J_PLAN_REQUIREMENT_HEAD M \n"
					+ "WHERE D.ISSUE_HEAD_ID = H.ISSUE_HEAD_ID \n"
					+ "AND H.MR_NO = M.MR_NO(+) \n"
					+ "AND (D.APPROVED_COUNT - D.ACT_ISSUED_COUNT) > 0 \n"
					+ "AND D.IS_USE = 'Y' \n"
					+ "AND H.ISSUE_STATUS = '2' \n"
					+ "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND H.IS_USE = 'Y' \n"
					+ "AND H.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND M.IS_USE(+) = 'Y' \n"
					+ "AND M.ENTERPRISE_CODE(+) = '" + enterpriseCode + "'\n";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				IssueHeaderInfo model = new IssueHeaderInfo();
				if (data[0] != null) {
					// 流水号
					model.setIssueHeadId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					// 领料单编号
					model.setIssueNo(data[1].toString());
				}
				if (data[2] != null) {
					// 申请领用日期
					model.setDueDate((Date) data[2]);
				}
				if (data[3] != null) {
					// 计划来源
					model.setPlanOriginalDesc(data[3].toString());
				}
				if (data[4] != null) {
					// 费用来源ID
					//model.setItemId(Long.parseLong(data[4].toString()));
					model.setItemCode(data[4].toString());
				}
				if (data[5] != null) {
					// 备注
					model.setMemo(data[5].toString());
				}
				if (data[6] != null) {
					// 领用部门
					model.setReceiptDep(data[6].toString());
				}
				if (data[7] != null) {
					// 申请领料人
					model.setReceiptBy(data[7].toString());
				}
				if (data[8] != null) {
					// 工单编号
					model.setWoNo(data[8].toString());
				}
				if (data[9] != null) {
					// 归口部门
					model.setFeeByDep(data[9].toString());
				}
				if (data[10] != null) {
					// 是否紧急领用
					model.setIsEmergency(data[10].toString());
				}
				if (data[11] != null) {
					// 上次修改时间
					model.setLastModifiedDate(data[11].toString());
				}
				if (data[12] != null) {
					// 需求单编号
					model.setMrNo(data[12].toString());
				}
				if (data[13] != null) {
					// 需求单编号
					model.setReceiptDepName(data[13].toString());
				}
				if (data[14] != null) {
					// 需求单编号
					model.setReceiptByName(data[14].toString());
				}
				arrlist.add(model);
			}
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据领料单编号检索数据
	 * modify by fyyang 090707 费用来源id改为费用来源编码
	 * @param enterpriseCode
	 *            企业编码
	 * @param issueNo
	 *            领料单编号
	 * @param rowStartIdxAndCount
	 *            分页
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getIssueHeadDatasByNo(String enterpriseCode,
			String issueNo,String applyBy,String materailName,int... rowStartIdxAndCount) {
		LogUtil.log("finding all InvJIssueHead instances", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			String sql = "SELECT DISTINCT H.ISSUE_HEAD_ID,H.ISSUE_NO,H.DUE_DATE,"
					+ "H.PLAN_ORIGINAL_ID,H.ITEM_CODE,H.MEMO,H.RECEIPT_DEP, "
					+ "H.RECEIPT_BY,H.WO_NO, H.FEE_BY_DEP, H.IS_EMERGENCY, \n"
					+ "to_char(H.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') , \n"
					+ "H.MR_NO, \n"
					+ "getdeptname(H.RECEIPT_DEP) as deptname,\n"
					+ "getworkername(H.RECEIPT_BY) as receiptName\n"
					+ "FROM INV_J_ISSUE_DETAILS D,INV_J_ISSUE_HEAD H,"
					+ "MRP_J_PLAN_REQUIREMENT_HEAD M \n"
					+ "WHERE D.ISSUE_HEAD_ID = H.ISSUE_HEAD_ID \n"
					+ "AND H.MR_NO = M.MR_NO(+) \n"
					+ "AND (D.APPROVED_COUNT - D.ACT_ISSUED_COUNT) > 0 \n"
					+ "AND D.IS_USE = 'Y' \n"
					+ "AND H.ISSUE_STATUS = '2' \n"
					+ "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND H.IS_USE = 'Y' \n"
					+ "AND H.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND M.IS_USE(+) = 'Y' \n"
					+ "AND M.ENTERPRISE_CODE(+) = '" + enterpriseCode + "'\n";
			
			String sqlCount = "SELECT COUNT(DISTINCT H.ISSUE_HEAD_ID) \n"
					+ "FROM INV_J_ISSUE_DETAILS D,INV_J_ISSUE_HEAD H,"
					+ "MRP_J_PLAN_REQUIREMENT_HEAD M \n"
					+ "WHERE D.ISSUE_HEAD_ID = H.ISSUE_HEAD_ID \n"
					+ "AND H.MR_NO = M.MR_NO(+) \n" 
//					+ "AND H.ISSUE_NO='"        lding20091207
//					+ issueNo + "'\n"
					+ "AND (D.APPROVED_COUNT - D.ACT_ISSUED_COUNT) > 0 \n"
					+ "AND D.IS_USE = 'Y' \n" + "AND H.ISSUE_STATUS = '2' \n"
					+ "AND D.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
					+ "AND H.IS_USE = 'Y' \n" + "AND H.ENTERPRISE_CODE = '"
					+ enterpriseCode + "'\n" 
					+"AND M.IS_USE(+) = 'Y' \n"
					+ "AND M.ENTERPRISE_CODE(+) = '" + enterpriseCode + "'\n";
			if(issueNo!=null&&!issueNo.equals(""))
			{
				sql+= "AND H.ISSUE_NO like '%"+ issueNo+ "%'\n";
				sqlCount+= "AND H.ISSUE_NO like '%"+ issueNo+ "%'\n";
			}
			if(applyBy!=null&&!applyBy.equals(""))
			{
				sql+= "AND getworkername(H.RECEIPT_BY)  like '%"+ applyBy+ "%'\n";
				sqlCount+= "AND getworkername(H.RECEIPT_BY)  like '%"+ applyBy+ "%'\n";
			
			}
			if(materailName!=null&&!materailName.equals(""))
			{
				sql+= "AND  GETMATERIALNAME(d.Material_Id)  like '%"+ materailName+ "%'\n";
				sqlCount+= "AND  GETMATERIALNAME(d.Material_Id)  like '%"+materailName+ "%'\n";
			
			}
			
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				IssueHeaderInfo model = new IssueHeaderInfo();
				if (data[0] != null) {
					// 流水号
					model.setIssueHeadId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					// 领料单编号
					model.setIssueNo(data[1].toString());
				}
				if (data[2] != null) {
					// 申请领用日期
					model.setDueDate((Date) data[2]);
				}
				if (data[3] != null) {
					// 计划来源
					model.setPlanOriginalDesc(data[3].toString());
				}
				if (data[4] != null) {
					// 费用来源
					model.setItemCode(data[4].toString());
					
					//model.setItemId(Long.parseLong(data[4].toString()));
				}
				if (data[5] != null) {
					// 备注
					model.setMemo(data[5].toString());
				}
				if (data[6] != null) {
					// 领用部门
					model.setReceiptDep(data[6].toString());
				}
				if (data[7] != null) {
					// 申请领料人
					model.setReceiptBy(data[7].toString());
				}
				if (data[8] != null) {
					// 工单编号
					model.setWoNo(data[8].toString());
				}
				if (data[9] != null) {
					// 归口部门
					model.setFeeByDep(data[9].toString());
				}
				if (data[10] != null) {
					// 是否紧急领用
					model.setIsEmergency(data[10].toString());
				}
				if (data[11] != null) {
					// 上次修改时间
					model.setLastModifiedDate(data[11].toString());
				}
				if (data[12] != null) {
					// 需求单编号
					model.setMrNo(data[12].toString());
				}
				if (data[13] != null) {
					// 需求单编号
					model.setReceiptDepName(data[13].toString());
				}
				if (data[14] != null) {
					// 需求单编号
					model.setReceiptByName(data[14].toString());
				}
				arrlist.add(model);
			}
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批号检索
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param materialId
	 *            物料id
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getLots(String enterpriseCode, Long materialId) {
		LogUtil.log("finding all Lots", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			// sql文
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT DISTINCT LOT.LOT_NO \n");
			sb.append("FROM INV_J_LOT LOT,INV_C_WAREHOUSE WHS\n");
			sb.append("WHERE LOT.MATERIAL_ID = " + materialId + "\n");
			sb
					.append("AND (LOT.OPEN_BALANCE + LOT.RECEIPT + LOT.ADJUST - LOT.ISSUE) > 0");
			sb.append("AND LOT.WHS_NO = WHS.WHS_NO \n");
			sb.append("AND WHS.IS_INSPECT = 'N' \n");
			sb.append("AND WHS.IS_COST='Y' \n");// 将是否计入仓库成本这个查询条件注释掉
			sb.append("AND LOT.IS_USE = 'Y' \n");
			sb.append("AND LOT.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("AND WHS.IS_USE = 'Y' \n");
			sb.append("AND WHS.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("ORDER BY LOT_NO");
			String sql = sb.toString();
			List list = bll.queryByNativeSQL(sql);
			List arrlist = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object data = list.get(i);
				if (data != null) {
					// 批号
					arrlist.add(data.toString());
				}
			}
			result.setList(arrlist);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据批号检索仓库信息
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param materialId
	 *            物料id
	 * @param lotNo
	 *            批号
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWareHouseByLotNo(String enterpriseCode, String lotNo,
			Long materialId) {
		LogUtil.log("finding all Lots", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			// sql文
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT DISTINCT LOT.WHS_NO,WHS.WHS_NAME\n");
			sb.append("FROM INV_J_LOT LOT,INV_C_WAREHOUSE WHS\n");
			sb.append("WHERE LOT.MATERIAL_ID = " + materialId + "\n");
			sb
					.append("AND (LOT.OPEN_BALANCE + LOT.RECEIPT + LOT.ADJUST - LOT.ISSUE) > 0");
			sb.append("AND LOT.WHS_NO = WHS.WHS_NO \n");
			sb.append("AND LOT.LOT_NO ='" + lotNo + "'\n");
			sb.append("AND WHS.IS_INSPECT = 'N' \n");
			sb.append("AND WHS.IS_COST='Y' \n");
			sb.append("AND WHS.IS_USE = 'Y' \n");
			sb.append("AND WHS.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("AND LOT.IS_USE = 'Y' \n");
			sb.append("AND LOT.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("ORDER BY LOT.WHS_NO");
			String sql = sb.toString();
			List list = bll.queryByNativeSQL(sql);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				InvCWarehouse model = new InvCWarehouse();
				if (data[0] != null) {
					// 仓库号
					model.setWhsNo(data[0].toString());
				}
				if (data[1] != null) {
					// 仓库名称
					model.setWhsName(data[1].toString());
				}
				arrlist.add(model);
			}
			result.setList(arrlist);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 获取物料的库存数量
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param lotNo
	 *            批号
	 * @param locationNo
	 *            库位号
	 * @param whsNo
	 *            仓库号
	 * @param materialId
	 *            物料id
	 * @return 物料库存数量
	 */
	public double getMaterialNums(String enterpriseCode, String lotNo,
			String locationNo, String whsNo, Long materialId) {
		StringBuilder sb = new StringBuilder();
		sb
				.append("SELECT SUM(L.OPEN_BALANCE + L.ADJUST + L.RECEIPT - L.ISSUE) \n");
		sb.append("FROM INV_J_LOT L, INV_C_WAREHOUSE WHS\n");
		sb.append("WHERE L.MATERIAL_ID = " + materialId + "\n");
		sb.append("AND L.WHS_NO = WHS.WHS_NO \n");
		sb.append("AND WHS.IS_INSPECT = 'N' \n");
		sb.append("AND WHS.IS_USE = 'Y' \n");
		sb.append("AND WHS.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
		sb.append("AND L.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
		sb.append("AND L.IS_USE = 'Y' \n");
		if (lotNo != null && lotNo.length() > 0) {
			sb.append("AND L.LOT_NO = '" + lotNo + "'\n");
		}
		if (whsNo != null && whsNo.length() > 0) {
			sb.append("AND L.WHS_NO = '" + whsNo + "'\n");
		}
		if (locationNo != null && locationNo.length() > 0) {
			sb.append("AND L.LOCATION_NO = '" + locationNo + "'\n");
		}
		// sql文
		String sql = sb.toString();
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return -1;
	}

	/**
	 * 获取物料的库存数量
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param lotNo
	 *            批号
	 * @param locationNo
	 *            库位号
	 * @param whsNo
	 *            仓库号
	 * @param materialId
	 *            物料id
	 * @return 物料库存数量
	 */
	@SuppressWarnings("unchecked")
	public PageObject getLotIdsAndCounts(String enterpriseCode, String lotNo,
			String locationNo, String whsNo, Long materialId) {
		PageObject result = new PageObject();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT L.LOT_INV_ID, (L.OPEN_BALANCE + L.ADJUST + L.RECEIPT - L.ISSUE) as storages \n");
		sb.append("FROM INV_J_LOT L, INV_C_WAREHOUSE W \n");
		sb.append("WHERE L.MATERIAL_ID = " + materialId + "\n");
		sb.append("AND W.WHS_NO = L.WHS_NO \n");
		sb.append("AND W.IS_INSPECT = 'N' \n");
		sb.append("AND L.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
		sb.append("AND L.IS_USE = 'Y' \n");
		sb.append("AND W.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
		sb.append("AND W.IS_USE = 'Y' \n");
		if (lotNo != null && lotNo.length() > 0) {
			sb.append("AND L.LOT_NO = '" + lotNo + "'\n");
		}
		if (whsNo != null && whsNo.length() > 0) {
			sb.append("AND L.WHS_NO = '" + whsNo + "'\n");
		}
		if (locationNo != null && locationNo.length() > 0) {
			sb.append("AND L.LOCATION_NO = '" + locationNo + "'\n");
		}
		sb.append(" ORDER BY L.LOT_NO,L.WHS_NO,L.LOCATION_NO");
		// sql文
		String sql = sb.toString();
		List list = bll.queryByNativeSQL(sql);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			Map map = new HashMap();
			if (data[0] != null) {
				// 批号记录表流水号
				map.put("lotId", data[0]);
			}
			if (data[1] != null) {
				// 物料当前库存数
				map.put("storages", data[1]);
			}
			arrlist.add(map);
		}
		result.setList(arrlist);
		return result;
	}

	/**
	 * 根据批号，仓库，库位和物料id检索事务历史表
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param materialId
	 *            物料id
	 * @param lotNo
	 *            批号
	 * @param whsNo
	 *            仓库号
	 * @param locationNo
	 *            库位号
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getTransHis(String enterpriseCode, Long materialId,
			String lotNo, String whsNo, String locationNo) {
		PageObject result = new PageObject();
		// 供应商ID
		String sql = "SELECT HIS.PRICE,HIS.TAX_RATE,HIS.STD_COST, HIS.SUPPLIER \n"
				+ "FROM INV_J_TRANSACTION_HIS HIS ,INV_C_TRANSACTION T\n"
				+ "WHERE T.TRANS_ID = HIS.TRANS_ID \n"
				+ "AND T.TRANS_CODE = 'P' \n"
				+ "AND HIS.MATERIAL_ID ="
				+ materialId
				+ "\n"
				+ "AND HIS.LOT_NO = '"
				+ lotNo
				+ "'\n"
				+ "AND HIS.FROM_WHS_NO = '"
				+ whsNo
				+ "'\n"
				+ "AND HIS.IS_USE = 'Y' \n"
				+ "AND HIS.ENTERPRISE_CODE = '"
				+ enterpriseCode + "'\n";
		// 库位有可能为空
		if (locationNo != null) {
			sql += "AND HIS.FROM_LOCATION_NO = '" + locationNo + "'";
		} else {
			sql += "AND HIS.FROM_LOCATION_NO IS NULL";
		}
		List list = bll.queryByNativeSQL(sql);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			Map map = new HashMap();
			if (data[0] != null) {
				// 单价
				map.put("price", data[0]);
			}
			if (data[1] != null) {
				// 税率
				map.put("taxRate", data[1]);
			}
			if (data[2] != null) {
				// 标准成本
				map.put("stdCost", data[2]);
			}
			if (data[3] != null) {
				// 供应商id
				map.put("supplier", data[3]);
			}
			arrlist.add(map);
		}
		result.setList(arrlist);
		return result;
	}

	/**
	 * 获取物料主文件中标准成本计算的几种方式
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param materialId
	 *            物料id
	 */
	@SuppressWarnings("unchecked")
	public PageObject getStdCostRelatingFields(String enterpriseCode,
			Long materialId) {
		PageObject result = new PageObject();
		String sql = "SELECT M.COST_METHOD,M.FROZEN_COST,M.STD_COST \n"
				+ "FROM INV_C_MATERIAL M \n" + "WHERE M.MATERIAL_ID = "
				+ materialId + "\n" + "AND M.IS_USE = 'Y'"
				+ "AND M.ENTERPRISE_CODE = '" + enterpriseCode + "'\n";
		List list = bll.queryByNativeSQL(sql);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			Map map = new HashMap();
			if (data[0] != null) {
				// 计价方式
				map.put("costMethod", data[0]);
			}
			if (data[1] != null) {
				// 计划价格
				map.put("frozenCost", data[1]);
			}
			if (data[2] != null) {
				// 标准成本
				map.put("stdCost", data[2]);
			}
			arrlist.add(map);
		}
		result.setList(arrlist);
		return result;
	}

	/**
	 * 
	 */
	/**
	 * 根据事务编码获得事务作用流水号
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param transCode
	 *            事务编码
	 */
	public PageObject getTransIdByTransCode(String enterpriseCode,
			String transCode) {
		String sql = "SELECT T.* \n" + "FROM INV_C_TRANSACTION T \n"
				+ "WHERE T.TRANS_CODE = '" + transCode + "'\n"
				+ "AND T.IS_USE = 'Y' \n" + "AND T.ENTERPRISE_CODE = '"
				+ enterpriseCode + "'";
		List<InvCTransaction> list = bll.queryByNativeSQL(sql,
				InvCTransaction.class);
		PageObject result = new PageObject();
		result.setList(list);
		return result;
	}

	/**
	 * 获取紧急领用领料单所需特定物资的数量
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param materialId
	 *            物料id
	 */
	public double getEmergencyNums(String enterpriseCode, Long materialId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT SUM(D.APPROVED_COUNT - D.ACT_ISSUED_COUNT) \n");
		sb.append("FROM INV_J_ISSUE_DETAILS D,INV_J_ISSUE_HEAD H \n");
		sb.append("WHERE D.ISSUE_HEAD_ID = H.ISSUE_HEAD_ID \n");
		sb.append("AND D.MATERIAL_ID = '" + materialId + "'\n");
		sb.append("AND H.IS_USE = 'Y' \n");
		sb.append("AND H.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
		sb.append("AND D.IS_USE = 'Y' \n");
		sb.append("AND D.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
		// 领料单.是否紧急领用 ='Y'
		sb.append("AND H.IS_Emergency = 'Y'");
		// sql文
		String sql = sb.toString();
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			return Double.parseDouble(obj.toString());
		}
		return -1;
	}

	/**
	 * 根据物料id检索大于优先级给定值的领料单所需物料数量
	 * 
	 * @param materialId
	 *            物料id
	 * @param planGrade
	 *            计划等级
	 * @param enterpriseCode
	 *            企业编码
	 * @return 优先级大于给定值的领料单所需物料(materialId)的数量
	 */
	public double getPlanRelateMaterialCount(String enterpriseCode,
			Long materialId, Long planGrade) {
		LogUtil.log("finding all Lots", Level.INFO, null);
		try {
			StringBuilder sb = new StringBuilder();
			sb
					.append("SELECT SUM(DETAIL.APPROVED_COUNT - DETAIL.ACT_ISSUED_COUNT) \n");
			sb.append("from MRP_J_PLAN_REQUIREMENT_HEAD mrp, "
					+ "INV_J_ISSUE_HEAD issue, INV_J_ISSUE_DETAILS detail \n");
			sb.append("where detail.MATERIAL_ID = " + materialId + "\n");
			sb.append("and detail.ISSUE_HEAD_ID = issue.ISSUE_HEAD_ID \n");
			sb.append("and issue.MR_NO = mrp.MR_NO \n");
			sb.append("AND MRP.PLAN_GRADE > " + planGrade + "\n");
			sb.append("and issue.IS_USE = 'Y' \n");
			sb.append("and issue.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("and mrp.IS_USE = 'Y' \n");
			sb.append("and mrp.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("and detail.IS_USE = 'Y' \n");
			sb.append("and detail.ENTERPRISE_CODE = '" + enterpriseCode + "'");
			String sql = sb.toString();
			Object obj = bll.getSingal(sql);
			if (obj != null) {
				return Double.parseDouble(obj.toString());
			}
			return -1;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 获取领料单的计划等级
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param issueHeadId
	 *            领料单id
	 * @return 领料单的计划等级
	 */
	public Long getIssuePlanGrade(String enterpriseCode, String issueHeadId) {
		String sql = "SELECT M.PLAN_GRADE \n"
				+ "FROM MRP_J_PLAN_REQUIREMENT_HEAD M ,INV_J_ISSUE_HEAD H \n"
				+ "WHERE H.ISSUE_HEAD_ID = " + issueHeadId + "\n"
				+ "AND H.MR_NO = M.MR_NO \n" + "AND M.IS_USE = 'Y' \n"
				+ "AND M.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "AND H.IS_USE = 'Y' \n" + "AND H.ENTERPRISE_CODE = '"
				+ enterpriseCode + "'\n";
		// 执行sql文
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			return Long.parseLong(obj.toString());
		}
		return 0L;
	}

	/**
	 * 根据批号检索仓库信息
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param materialId
	 *            物料id
	 * @param lotNo
	 *            批号
	 * @param whsNo
	 *            仓库编号
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getLocationByLotNoAndWhsNo(String enterpriseCode,
			String lotNo, String whsNo, Long materialId) {
		LogUtil.log("finding all Lots", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			// sql文
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT DISTINCT LOT.LOCATION_NO,LOC.LOCATION_NAME\n");
			sb
					.append("FROM INV_J_LOT LOT,INV_C_WAREHOUSE WHS,INV_C_LOCATION LOC\n");
			sb.append("WHERE LOT.MATERIAL_ID = " + materialId + "\n");
			sb
					.append("AND (LOT.OPEN_BALANCE + LOT.RECEIPT + LOT.ADJUST - LOT.ISSUE) > 0\n");
			sb.append("AND LOT.WHS_NO = WHS.WHS_NO\n");
			sb.append(" AND LOC.WHS_NO = WHS.WHS_NO\n");
			sb.append("AND LOT.LOCATION_NO = LOC.LOCATION_NO\n");
			sb.append("AND LOT.LOT_NO ='" + lotNo + "'\n");
			sb.append("AND LOT.WHS_NO = '" + whsNo + "'\n");
			sb.append("AND WHS.IS_INSPECT = 'N' \n");
			sb.append("AND WHS.IS_COST='Y' \n");
			sb.append("AND WHS.IS_USE = 'Y' \n");
			sb.append("AND WHS.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("AND LOC.IS_USE = 'Y' \n");
			sb.append("AND LOC.ENTERPRISE_CODE='" + enterpriseCode + "'\n");
			sb.append("AND LOT.IS_USE = 'Y' \n");
			sb.append("AND LOT.ENTERPRISE_CODE = '" + enterpriseCode + "'\n");
			sb.append("ORDER BY LOT.WHS_NO");
			String sql = sb.toString();
			List list = bll.queryByNativeSQL(sql);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				InvCLocation model = new InvCLocation();
				if (data[0] != null) {
					// 库位号
					model.setLocationNo(data[0].toString());
				}
				if (data[1] != null) {
					// 库位名称
					model.setLocationName(data[1].toString());
				}
				arrlist.add(model);
			}
			result.setList(arrlist);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据领料单编号检索物资详细信息
	 * modify by fyyang 090707 费用来源id改为费用来源编码
	 * @param issueHeadId
	 *            领料单id
	 * @param rowStartIdxAndCount
	 *            分页
	 * @return 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMaterialDetailDatas(String enterpriseCode,
			Long issueHeadId, int... rowStartIdxAndCount) {
		LogUtil.log("finding all material detail datas", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			String sql = "SELECT D.ISSUE_DETAILS_ID,M.MATERIAL_ID,M.MATERIAL_NO,M.MATERIAL_NAME \n"
					+ ",M.STOCK_UM_ID ,D.APPROVED_COUNT DEMANDCOUNT,d.ACT_ISSUED_COUNT"
					+ ",(D.APPROVED_COUNT - D.ACT_ISSUED_COUNT) WAITNUM,D.ITEM_CODE"
					+ ",D.REQUIREMENT_DETAIL_ID, \n"
					+ "to_char(D.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss'), \n"
					+ "M.DEFAULT_WHS_NO,\n" 
					+ "M.DEFAULT_LOCATION_NO,M.Spec_No \n"
					+ " FROM INV_J_ISSUE_DETAILS D ,INV_C_MATERIAL M \n"
					+ " WHERE D.ISSUE_HEAD_ID = "
					+ issueHeadId
					+ "\n"
					+ " AND D.MATERIAL_ID = M.MATERIAL_ID \n"
					+ "AND D.APPROVED_COUNT - D.ACT_ISSUED_COUNT > 0 \n"
					+ "AND D.IS_USE = 'Y' \n"
					+ "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND D.IS_USE = 'Y' \n"
					+ "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "ORDER BY M.MATERIAL_NO";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			String sqlCount = "SELECT COUNT(1) \n"
					+ " FROM INV_J_ISSUE_DETAILS D ,INV_C_MATERIAL M \n"
					+ " WHERE D.ISSUE_HEAD_ID = " + issueHeadId + "\n"
					+ " AND D.MATERIAL_ID = M.MATERIAL_ID \n"
					+ "AND D.APPROVED_COUNT - D.ACT_ISSUED_COUNT > 0 \n"
					+ "AND D.IS_USE = 'Y' \n" + "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode + "'\n" + "AND D.IS_USE = 'Y' \n"
					+ "AND D.ENTERPRISE_CODE = '" + enterpriseCode + "'";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				IssueMaterialDetailInfo model = new IssueMaterialDetailInfo();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					// 领料单明细id
					model.setIssueDetailsId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					// 物料ID
					model.setMaterialId(Long.parseLong(data[1].toString()));
				}
				if (data[2] != null) {
					// 物料编号
					model.setMaterialNo(data[2].toString());
				}
				if (data[3] != null) {
					// 物料名称
					model.setMaterialName(data[3].toString());
				}
				if (data[4] != null) {
					// 存货计量单位
					model.setStockUmId(Long.parseLong(data[4].toString()));
					// 单位名称
					// 对应 ->commonInterface jar包删除
					BpCMeasureUnit unit = unitRemote.findById(model
							.getStockUmId());
					if (unit != null) {
						model.setUnitName(unit.getUnitName());
					}
				}
				if (data[5] != null) {
					// 需求数量
					model.setDemandNum(Double.parseDouble(data[5].toString()));
				}
				if (data[6] != null) {
					// 实际数量
					model.setActIssuedCount(Double.parseDouble(data[6]
							.toString()));
				}
				if (data[7] != null) {
					// 待发货数量
					model.setWaitNum(Double.parseDouble(data[7].toString()));
				}
				if (data[8] != null) {
//					// 费用来源id
//					model.setItemId(Long.parseLong(data[8].toString()));
					model.setItemCode(data[8].toString());
				}
				if (data[9] != null) {
					// 需求计划明细ID
					model.setRequirementDetailId(Long.parseLong(data[9]
							.toString()));
				}
				if (data[10] != null) {
					// 上次修改时间
					model.setLastModifiedDate(data[10].toString());
				}
//				if (data[11] != null) {
//					// 物料主表默认仓库
//					model.setWhsNo(data[11].toString());
//				}
//				if (data[12] != null) {
//					// 物料主表默认库位
//					model.setLocationNo(data[12].toString());
//				}
				if(data[13]!=null)
				{
					model.setSpecNo(data[13].toString());
				}
				arrlist.add(model);
			}
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * db更新
	 * 
	 * @param head
	 *            领料单表头
	 * @param details
	 *            要更新的领料单明细表
	 * @param planDetails
	 *            要更新的物料需求计划明细表
	 * @param lots
	 *            批号记录表
	 * @param transHises
	 *            事务历史表
	 * @param wareHouses
	 *            库存物料记录
	 * @param locations
	 *            库位物料记录
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateIssueDbTables(InvJIssueHead head,
			List<InvJIssueDetails> details,
			List<MrpJPlanRequirementDetail> planDetails, List<InvJLot> lots,
			List<InvJTransactionHis> transHises,
			List<InvJWarehouse> wareHouses, List<InvJLocation> locations) {
		try {
			issueHeadRemote.update(head);
			// 更新领料单明细表
			for (InvJIssueDetails detail : details) {
				issueDetailRemote.update(detail);
			}
			// 更新物料需求计划明细表
			for (MrpJPlanRequirementDetail plan : planDetails) {
				planDetailRemote.update(plan);
			}
			// 更新批号记录表
			for (InvJLot lot : lots) {
				lotRemote.update(lot);
			}
			// 插入事务历史表
			// 事务id
			Long tansId = bll.getMaxId("INV_J_TRANSACTION_HIS", "TRANS_HIS_ID");
			for (InvJTransactionHis trans : transHises) {
				// 流水号
				trans.setTransHisId(tansId++);
				transHisRemote.save(trans);
			}
			// 更新库存物料记录
			for (InvJWarehouse ware : wareHouses) {
				wareRemote.update(ware);
			}
			// 更新库位物料记录
			for (InvJLocation location : locations) {
				locationRemote.update(location);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * modify by liuyi 091102 增加专业，仓库，物资类型，部门查询
	 * modify by fyyang 090707 费用来源id改为费用来源编码
	 */
	@SuppressWarnings("unchecked")
	public PageObject findIssueList(String delayStore,String materialClass,String dept,String enterpriseCode, String sDate,
			String eDate, String whsName, String materialCode,
			String materialName, String specNo, String issuNo, String issueBy,String isRedOp,
			String getPerson,String freefrom,
			final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		/*String deptName="";//add by wpzhu  100408
		if(dept!=null&&!dept.equals(""))
		{
			 deptName=dept.substring(0,2);
		}*/
		String sql = "select a.issue_details_id,\n" +
			"       a.last_modified_date,\n" + 
			"       d.issue_no,\n" + 
			"       d.ITEM_CODE,\n" + 
			"       a.material_id,\n" + 
			"       b.material_no,\n" + 
			"       b.material_name,\n" + 
			"       b.spec_no,\n" + 
			"       b.stock_um_id,\n" + 
			"       a.act_issued_count,\n" + 
			"      ( select h.dept_name   from hr_c_dept  h  where h.dept_code=d.fee_by_dep and rownum=1)deptName,\n" + //modify by wpzhu
			"       getspecialname(d.fee_by_special) specialName,\n" + 
			"       getworkername(a.last_modified_by),\n" + 
			"       d.REF_ISSUE_NO,\n" + 
			"       getworkername(d.get_real_person),\n" + 
			"       nvl(e.price, 0) price,\n" + 
			"       nvl(e.qty, 0)\n" + 
			" ,GETUNITNAME(b.stock_um_id) \n"+ //add by fyyang 20100409
			"  from inv_j_issue_details a,\n" + 
			"       inv_c_material b,\n" + 
			"       inv_j_issue_head d,\n" + 
			//----------------------------------------
			
			"       (select t.order_no,\n" + 
			"               t.material_id,\n" + 
			"               sum(t.std_cost * t.trans_qty) price,\n" + 
			"               decode(sum(t.trans_qty),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(t.std_cost * t.trans_qty) / sum(t.trans_qty)) qty\n" + 
			"          from inv_j_transaction_his t\n" + 
			"         where t.trans_id = 4\n" + 
			"           and t.is_use = 'Y'\n" + 
			"           and t.last_modified_date >=\n" + 
			"               to_date('"+sDate+"' || '00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
			"           and t.last_modified_date <=\n" + 
			"               to_date('"+eDate+"' || '23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n" + 
			"         group by t.order_no, t.material_id) e\n" + 
			
			//-------------------------------------------
			" where e.material_id = a.material_id\n" + 
			"   and e.order_no = d.issue_no\n" + 
			"   and a.issue_head_id = d.issue_head_id\n" + 
			"   and e.material_id = b.material_id\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and d.is_use = 'Y'\n" + //modify by wpzhu
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and d.enterprise_code = '"+enterpriseCode+"'";

		
		
		String whereStr = "";

//		if (sDate != null && sDate.length() > 0) {
//			whereStr += " and a.last_modified_date >= to_date('" + sDate
//					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
//		}
//		if (eDate != null && eDate.length() > 0) {
//			whereStr += "and a.last_modified_date <= to_date('" + eDate
//					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
//		}
		if (materialCode != null && !"".equals(materialCode)) {
			whereStr += " and b.material_no like '%" + materialCode + "%'";
		}
		if (materialName != null && !"".equals(materialName)) {
			whereStr += " and b.material_name like '%" + materialName + "%'";
		}
		if (specNo != null && !"".equals(specNo)) {
			whereStr += " and b.spec_no='" + specNo + "'";
		}
		if (issuNo != null && !"".equals(issuNo)) {
			whereStr += " and (select i.issue_no\n"
					+ "          from inv_j_issue_head i\n"
					+ "         where i.issue_head_id = a.issue_head_id\n"
					+ "           and i.is_use = 'Y') like '%" + issuNo + "%'";
		}
		if(isRedOp!=null&&!isRedOp.equals(""))
		{
			if(isRedOp.equals("Y"))
			{
				whereStr+=" and  d.issue_status='B'  ";
			}
			if(isRedOp.equals("N"))
			{
				whereStr+=" and  d.issue_status<>'B'  ";
			}
		}
		// add by liuyi 091102 增加专业，仓库，物资类型，部门查询 专业查询未做
		if(delayStore != null && !delayStore.equals(""))
			whereStr += "and b.default_whs_no='" + delayStore + "' \n";
		if(materialClass != null && !materialClass.equals(""))
			whereStr += "and b.material_no like '" + materialClass + "%' \n";
		if(dept != null && !dept.equals(""))
			whereStr += "and d.receipt_dep  like '" + dept + "%' \n";
		if(getPerson!=null&&!getPerson.equals(""))
		{
			whereStr+="  and  getworkername(d.get_real_person) like '%"+getPerson+"%'  \n";
		}
		if(freefrom!=null&&!freefrom.equals(""))
		{
			whereStr+=" and  d.ITEM_CODE  like '"+freefrom+"%'     \n";
		}
		if(issueBy!=null&&!issueBy.equals(""))
		{
			//add by fyyang 20100504
			whereStr+="  and getworkername(a.last_modified_by) like '%"+issueBy+"%' \n";
		}
		sql += whereStr;
		String sqlCount = "select count(1) from ("+ sql +")";//modify by wpzhu
		sql += " order by d.issue_no";
//		System.out.println("the sql"+sql);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		Iterator it = list.iterator();
		List<IssueMaterialDetailInfo> arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
			Double issuedCountTotal = 0.00;
			Double issuedPriceTotal = 0.00;
			while (it.hasNext()) {
				IssueMaterialDetailInfo info = new IssueMaterialDetailInfo();
				Object[] data = (Object[]) it.next();
				info.setIssueDetailsId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					info.setLastModifiedDate(data[1].toString());
				if (data[2] != null)
					info.setIssueNo(data[2].toString());
				if (data[3] != null)
					info.setItemCode(data[3].toString());
					//info.setItemId(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					info.setMaterialId(Long.parseLong(data[4].toString()));
				if (data[5] != null)
					info.setMaterialNo(data[5].toString());
				if (data[6] != null)
					info.setMaterialName(data[6].toString());
				if (data[7] != null) {
					info.setSpecNo(data[7].toString());
				} else {
					info.setSpecNo("");
				}
				if (data[8] != null)
					info.setStockUmId(Long.parseLong(data[8].toString()));
				if (data[9] != null) 
					info.setActIssuedCount(Double.parseDouble(data[9]
							.toString()));
				if (data[10] != null)
					info.setFreeDeptName(data[10].toString());
				if (data[11] != null)
					info.setFreeSpecialName(data[11].toString());
				if (data[12] != null)
					info.setLastModifyBy(data[12].toString());
                  if(data[13]!=null)
                	  info.setRefIssueNo(data[13].toString());
                  if(data[14]!=null)
                  {
                	  //领料人 add by fyyang 091218
                	  info.setGetPerson(data[14].toString());
                  }
                  if(data[15]!=null)
                  {
                	  info.setActPrice(Double.parseDouble(data[15].toString())); 
                  }
                  if(data[16]!=null)
                  {
                	  info.setUnitPrice(Double.parseDouble(data[16].toString()));
                  }
                  if(data[17]!=null)
                  {
                	  //add by fyyang 20100409
                	  info.setUnitName(data[17].toString());
                  }
                issuedCountTotal += info.getActIssuedCount();
                issuedPriceTotal+=info.getActPrice();
				arrlist.add(info);
			}
			// 计算当前页的合计数量 modify by ywliu 090721 
			IssueMaterialDetailInfo info = new IssueMaterialDetailInfo();
			info.setLastModifiedDate("本页合计");//add by wpzhu 100409
			info.setActIssuedCount(issuedCountTotal);
			info.setActPrice(issuedPriceTotal);
			arrlist.add(info);
		}
		// 计算总共的合计数量 modify by ywliu 090721 
		if(totalCount>=rowStartIdxAndCount[0]&&totalCount<=rowStartIdxAndCount[0]+rowStartIdxAndCount[1])//modify by wpzhu 100409
		{
//		if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1 && rowStartIdxAndCount[0]!=0) {
			String sumSql = "select sum(a.act_issued_count),sum(e.price)\n"
				+ "  from inv_j_issue_details a,\n"
				+ "       inv_c_material      b,\n"
				+ "       inv_j_issue_head    d\n"+
				//--------------------------------------


				" ,      (select t.order_no,\n" + 
				"               t.material_id,\n" + 
				"               sum(t.std_cost * t.trans_qty) price,\n" + 
				"               decode(sum(t.trans_qty),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      sum(t.std_cost * t.trans_qty) / sum(t.trans_qty)) qty\n" + 
				"          from inv_j_transaction_his t\n" + 
				"         where t.trans_id = 4\n" + 
				"           and t.is_use = 'Y'\n" + 
				"           and t.last_modified_date >=\n" + 
				"               to_date('"+sDate+"' || '00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"           and t.last_modified_date <=\n" + 
				"               to_date('"+eDate+"' || '23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"         group by t.order_no, t.material_id) e\n"  

				//-----------------------------------------
				+ " where b.material_id = a.material_id\n"
				+ "   and d.issue_head_id = a.issue_head_id\n"
				+"and e.order_no = d.issue_no    and e.material_id = a.material_id \n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and  (a.act_issued_count > 0  or d.issue_status='B')  ";
			sumSql += whereStr ; 
			List re=bll.queryByNativeSQL(sumSql);
			if(re.size()>0)
			{
			Object [] obj=(Object [] )bll.queryByNativeSQL(sumSql).get(0);
			if(obj[0]!=null&&obj[1]!=null)
			{
			Double total = Double.parseDouble(obj[0].toString());
			IssueMaterialDetailInfo info = new IssueMaterialDetailInfo();
			info.setLastModifiedDate("所有页合计");//add by wpzhu 100409
			info.setActIssuedCount(total);
			info.setActPrice(Double.parseDouble(obj[1].toString()));
			arrlist.add(info);
			}
			}
			
			
			
			/*IssueMaterialDetailInfo info = new IssueMaterialDetailInfo();
			info.setLastModifiedDate("所有页合计");//add by wpzhu 100409
			info.setActIssuedCount(total);
			info.setActPrice(Double.parseDouble(obj[1].toString()));
			arrlist.add(info);*/
		}
		
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;

	}
	
//	public List getIssueDetailsMaterialInfo(String issuNo, String whsName) {
	public List getIssueDetailsMaterialInfo(String issuNo, String whsName,String gdFlag,String materialId) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, -1);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select a.order_no,\n"
				+ "       a.std_cost,\n"
				+ "       GETWHSNAME(a.from_whs_no),\n"
				+ "       a.tax_rate,\n"
				+ "       b.material_no,\n"
				+ "       b.material_name,\n"
				+ "       b.spec_no,\n"
				+ "       GETUNITNAME(a.trans_um_id),\n"
				+ "       GETDEPTNAME(a.receive_dept),\n"
				+ "       GETWORKERNAME(c.get_real_person),\n"
				+ "       d.applied_count,\n"
				+ "       a.trans_qty,\n"
				+ "       GETWORKERNAME(a.last_modified_by),\n"
				+ "       GETPROJECTNAME(c.project_code),\n"
				+ "       c.plan_original_id,\n"
				+ "		  GETWORKERNAME(e.caller)\n"
				// add by liuyi 091130 
				+ " , a.std_cost * a.trans_qty \n"
				// add by liuyi 20100316
				+ " ,c.item_code, getitemfactname(c.item_code) \n"
				+ "  from INV_J_TRANSACTION_HIS a,\n"
				+ "       inv_c_material        b,\n"
				+ "       INV_J_ISSUE_HEAD      c,\n"
				+ "       INV_J_ISSUE_DETAILS   d,\n"
				+ "       wf_j_historyoperation e\n"
				+ " where a.material_id = b.material_id\n"
				+ "   and a.order_no = c.issue_no\n"
				+ "   and a.sequence_id = d.issue_details_id\n"
//				+ "   and to_char(a.last_modified_date, 'yyyy-MM-dd hh24:mi:ss') >= '" + sdf.format(calendar.getTime())
//				+ "'\n   and to_char(a.last_modified_date, 'yyyy-MM-dd hh24:mi:ss') <= '" + sdf.format(new Date())
				+ "   and d.act_issued_count > 0\n"
				+ "   and a.order_no = '"+issuNo+"'"
				+ "   and a.from_whs_no = '"+whsName+"'"
				+ "   and c.work_flow_no = e.entry_id"
				+ "   and e.action_id = '83'"
				+ "   and a.last_modified_date = (select max(t.last_modified_date)  from INV_J_TRANSACTION_HIS t where t.order_no='"+issuNo+"' and t.trans_id=4)";
			//modified by liuyi 20100128 
			if(gdFlag != null && gdFlag.equals("1"))
			{
				if(materialId != null && !materialId.equals(""))
					sql += " and a.material_id=" + materialId + " \n";
			}
		List<Object> list = bll.queryByNativeSQL(sql);
		List<IssueMaterialDetailPrintInfo> arraylist = new ArrayList<IssueMaterialDetailPrintInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			IssueMaterialDetailPrintInfo model = new IssueMaterialDetailPrintInfo();
			if (data[0] != null) {
				model.setIssueNo(data[0].toString());
			}
			if (data[1] != null) {
				// modified by liuyi 091130 
//				model.setUnitPrice(Double.valueOf(data[1].toString()));
				Double price = Math.rint(Double.parseDouble(data[1].toString()) * 10000) / 10000;
				model.setUnitPriceString(this.dataParseFour(price));
			}else
				model.setUnitPriceString("0.00000");
			if (data[2] != null) {
				model.setWhsNo(data[2].toString());
			}
			if (data[3] != null) {
				model.setTaxRate(data[3].toString());
			}
			if (data[4] != null) {
				model.setMaterialNo(data[4].toString());
			}
			if (data[5] != null) {
				model.setMaterialName(data[5].toString());
			}
			if (data[6] != null) {
				model.setSpecNo(data[6].toString());
			}
			if (data[7] != null) {
				model.setUnitName(data[7].toString());
			}
			if (data[8] != null) {
				model.setReceiveDept(data[8].toString());
			}
			if (data[9] != null) {
				model.setReceiveMan(data[9].toString());
			}	
			if (data[10] != null) {
				model.setDemandNum(Double.valueOf(data[10].toString()));
			}
			if (data[11] != null) {
//				model.setActIssuedCount(data[11].toString());
				// modified by liuyi 091201
				model.setActIssuedCount(this.dataParseFour(Double.parseDouble(data[11].toString())));
			}
			if (data[12] != null) {
				model.setLastModifyBy(data[12].toString());
			}
			if (data[13] != null) {
				model.setProjectName(data[13].toString());
			}
			if (data[14] != null) {
				model.setIssueType(data[14].toString());
			}
			if (data[15] != null) {
				model.setMaterialKeeper(data[15].toString());
			}
			if(data[16] != null)
			{
				Double total = Math.rint(Double.parseDouble(data[16].toString()) * 100) /100;
				model.setTotalPrice(this.dataOperate(total));
			}else {
				model.setTotalPrice("0.0");
			}
			
			// add by liuyi 20100316
			if(data[17] != null)
				model.setItemCode(data[17].toString());
			if(data[18] != null)
				model.setItemName(data[18].toString());
			else
				model.setItemName("");
			
			arraylist.add(model);
		}
		return arraylist;
	}
	
	
	/**
	 *  查询领料单审核列表
	 *  add by fyyang 090619
	 *  modify by fyyang 090707 费用来源id改为费用来源编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 * modify by drdu 091103 增加审核状态查询条件
	 */
	@SuppressWarnings("unchecked")
	public PageObject getIssueHeadListForCheck(String enterpriseCode,String checkStatus,String issueNo,
			int... rowStartIdxAndCount) {
		LogUtil.log("finding all InvJIssueHead instances", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			String sql =
				"SELECT H.ISSUE_HEAD_ID,\n" +
				"       H.ISSUE_NO,\n" + 
				"       H.DUE_DATE,\n" + 
				"       +H.PLAN_ORIGINAL_ID,\n" + 
				"       H.ITEM_CODE,\n" + 
				"       H.MEMO,\n" + 
				"       GETDEPTNAME(H.RECEIPT_DEP),\n" + 
				"       GETWORKERNAME(H.RECEIPT_BY),\n" + 
				"       H.WO_NO,\n" + 
				"       H.FEE_BY_DEP,\n" + 
				"       H.IS_EMERGENCY,\n" + 
				"       to_char(M.last_modified_date, 'yyyy-mm-dd hh24:mi:ss'),\n" + 
				"       H.MR_NO,\n" + 
				"       H.Work_Flow_No,\n" + 
				"       H.Project_Code,\n" + 
				"		H.Ref_Issue_No,\n" +
				"      (select a.act_issued_count from inv_j_issue_details a where a.issue_head_id = h.issue_head_id and rownum=1) as act_issued_count" +
				"  FROM INV_J_ISSUE_HEAD H,";
			String sqlCount=
			"select count(1)\n" +
			" FROM INV_J_ISSUE_HEAD H,";

                String strSql="";
			if(checkStatus != null && !checkStatus.equals(""))//add by drdu 091103
			{

				if("C".equals(checkStatus)) {

					strSql="(select a.order_no, a.last_modified_date\n" +
					"          from Inv_j_Transaction_His a,resourse_v_rollback_arrival b\n" + 
					"         where a.check_status='C'\n" + 
					"		  and a.TRANS_HIS_ID=b.trans_his_id(+)" +	
					"           and a.trans_id = 4\n" + 
					" and  (a.trans_qty+nvl(b.sl,0)>0 or (a.trans_qty<0 and a.ROLL_BACK_ID is null)) "+
					"           and a.check_date > (select max(b.last_modified_date) from inv_j_balance b where b.is_use = 'Y')\n" + 
					"         group by a.order_no, a.last_modified_date) M \n";

				}else{
	
					strSql=
						"(select a.order_no, a.last_modified_date\n" +
						"         from Inv_j_Transaction_His a,resourse_v_rollback_arrival b\n" + 
						"        where (a.CHECK_STATUS is null)\n" + 
						"		  and a.TRANS_HIS_ID=b.trans_his_id(+)" +
						"          and a.trans_id = 4\n" + 
						" and  (a.trans_qty+nvl(b.sl,0)>0 or (a.trans_qty<0 and a.ROLL_BACK_ID is null)) "+
						"        group by a.order_no, a.last_modified_date) M \n";
				}
			}
			else
			{
				strSql=
					"(select a.order_no, a.last_modified_date\n" +
					"         from Inv_j_Transaction_His a,resourse_v_rollback_arrival b\n" + 
					"        where (a.CHECK_STATUS is null)\n" + 
					"		  and a.TRANS_HIS_ID=b.trans_his_id(+)" +
					"          and a.trans_id = 4\n" + 
					" and  (a.trans_qty+nvl(b.sl,0)>0 or (a.trans_qty<0 and a.ROLL_BACK_ID is null)) "+
					"        group by a.order_no, a.last_modified_date) M \n";

			}
			sql+=strSql;
			sqlCount+=strSql;
			sql+=
				"WHERE H.ISSUE_NO = M.ORDER_NO\n" +
				"  and H.Is_Use = 'Y'\n" + 
				"  and H.Enterprise_Code = '"+enterpriseCode+"' \n";
			sqlCount+=
				"WHERE H.ISSUE_NO = M.ORDER_NO\n" +
			"  and H.Is_Use = 'Y'\n" + 
			"  and H.Enterprise_Code = '"+enterpriseCode+"' \n";
			if(issueNo != null && !issueNo.equals(""))//add by drdu 091126
			{
				sql += " AND H.ISSUE_NO like '%"+issueNo+"%' \n"; // modify by ywliu 20100201
				sqlCount += " AND H.ISSUE_NO like '%"+issueNo+"%' \n"; // modify by ywliu 20100201
			}
			sql+=" order by  H.ISSUE_NO asc,M.last_modified_date asc "; 
			//sql += " order by m.MATERIAL_NO, h.TRANS_HIS_ID"; // modify by ywliu 20100201
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				IssueHeaderInfo model = new IssueHeaderInfo();
				if (data[0] != null) {
					// 流水号
					model.setIssueHeadId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					// 领料单编号
					model.setIssueNo(data[1].toString());
				}
				if (data[2] != null) {
					// 申请领用日期
					model.setDueDate((Date) data[2]);
				}
				if (data[3] != null) {
					// 计划来源
					model.setPlanOriginalDesc(data[3].toString());
				}
				if (data[4] != null) {
					// 费用来源ID
					//model.setItemId(Long.parseLong(data[4].toString()));
					model.setItemCode(data[4].toString());
				}
				if (data[5] != null) {
					// 备注
					model.setMemo(data[5].toString());
				}
				if (data[6] != null) {
					// 领用部门
					model.setReceiptDep(data[6].toString());
				}
				if (data[7] != null) {
					// 申请领料人
					model.setReceiptBy(data[7].toString());
				}
				if (data[8] != null) {
					// 工单编号
					model.setWoNo(data[8].toString());
				}
				if (data[9] != null) {
					// 归口部门
					model.setFeeByDep(data[9].toString());
				}
				if (data[10] != null) {
					// 是否紧急领用
					model.setIsEmergency(data[10].toString());
				}
				if (data[11] != null) {
					// 上次修改时间
					model.setLastModifiedDate(data[11].toString());
				}
				if (data[12] != null) {
					// 需求单编号
					model.setMrNo(data[12].toString());
				}
				if(data[13] != null)
				{//工作流编号  add by drdu 091103
					model.setWorkFlowNo(data[13].toString());
				}
				if(data[14]!=null)
				{
					//add by fyyang 20100107 项目编码
					model.setProjectCode(data[14].toString());
				}
				if(data[15]!=null)
				{
					//add by fyyang 20100107 项目编码
					model.setRefIssueNo(data[15].toString());
				}
				if(data[16]!=null)
				{
					//add by fyyang 20100107 项目编码
					model.setActIssuedCount(data[16].toString());
				}
				arrlist.add(model);
//				TransActionHisInfo model = new TransActionHisInfo();
//				Object[] data = (Object[]) it.next();
//				// 流水号
//				if (data[0] != null)
//					model.setTransHisId(Long.parseLong(data[0].toString()));
//				// 单据号
//				if (data[1] != null)
//					model.setOrderNo(data[1].toString());
//				// 项号
//				if (data[2] != null)
//					model.setSequenceNo(Long.parseLong(data[2].toString()));
//				// 事务名称
//				if (data[3] != null)
//					model.setTransName(data[3].toString());
//				// 物料编码
//				if (data[4] != null)
//					model.setMaterialNo(data[4].toString());
//				// 物料名称
//				if (data[5] != null)
//					model.setMaterialName(data[5].toString());
//				// 规格型号
//				if (data[6] != null)
//					model.setSpecNo(data[6].toString());
//				// 材质参数
//				if (data[7] != null)
//					model.setParameter(data[7].toString());
//				// 异动数量
//				if (data[8] != null)
//					model.setTransQty(Double.parseDouble(data[8].toString()));
//				// 单位
//				if (data[9] != null) {
//					BpCMeasureUnit bcmu = unitRemote.findById(Long.parseLong(data[9].toString()));
//	    		    if(bcmu!=null){
//	    		    	String stockUmID = bcmu.getUnitName();
//	    		    	model.setTransUmId(stockUmID);
//	    		    }
//	    		}
//				// 操作人
//				if (data[10] != null) {
//					model.setOperatedBy(data[10].toString());
////				    Employee emp = personInfo.getEmployeeInfo(data[10].toString());
////				    if(emp != null) {
////						// 人员姓名
////						String strChsName = emp.getWorkerName();
////						model.setOperatedBy(strChsName);
////				    }
//				}
//				// 操作时间
//				if (data[11] != null)
//					model.setOperatedDate(data[11].toString());
//				// 操作仓库
//				if (data[12] != null)
//					model.setWhsName(data[12].toString());
//				// 操作库位
//				if (data[13] != null)
//					model.setLocationName(data[13].toString());
//				// 调入仓库
//				if (data[14] != null)
//					model.setWhsNameTwo(data[14].toString());
//				// 调入库位
//				if (data[15] != null)
//					model.setLocationNameTwo(data[15].toString());
//				// 批号
//				if (data[16] != null)
//					model.setLotNo(data[16].toString());
//				// 备注
//				if (data[17] != null)
//					model.setMemo(data[17].toString());
//				
//				arrlist.add(model);
			}
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 查询领料单审核的物资明细列表
	 * add by fyyang 090619
	 * modify by fyyang 090707 费用来源id改为费用来源编码
	 * @param enterpriseCode
	 * @param issueHeadId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMaterialDetailListForCheck(String enterpriseCode,
			String strIssueNo,String issueDate,String startCheckDate,String endCheckDate, int... rowStartIdxAndCount) {
		LogUtil.log("finding all material detail datas", Level.INFO, null);
		PageObject result = new PageObject();
		try {
			String sql = "SELECT D.ISSUE_DETAILS_ID,M.MATERIAL_ID,M.MATERIAL_NO,M.MATERIAL_NAME \n"
					+ ",M.STOCK_UM_ID ,D.APPROVED_COUNT DEMANDCOUNT," ;
			 if((issueDate!=null&&!issueDate.equals(""))||(startCheckDate!=null&&endCheckDate!=null))
			 {
				 sql+=	" N.ACT_ISSUED_COUNT";
			 }
			 else
			 {
				 sql+="D.ACT_ISSUED_COUNT";
			 }
			 sql+= ",(D.APPROVED_COUNT - D.ACT_ISSUED_COUNT) WAITNUM,D.ITEM_CODE"
					+ ",D.REQUIREMENT_DETAIL_ID, \n"
					+ "to_char(n.check_date,'yyyy-mm-dd hh24:mi:ss'), \n";
			 if(issueDate!=null&&!issueDate.equals(""))
			 {
				 sql+=" N.act_std_cost,N.act_price,\n";
			 }
			 else
			 {
				 if(startCheckDate!=null&&endCheckDate!=null)
				 {
					 sql+=" decode(N.ACT_ISSUED_COUNT,0,0,N.act_price/N.ACT_ISSUED_COUNT),N.act_price,\n";
				 }
				 else
				 {
				 sql+="M.Std_Cost,M.Std_Cost * D.Act_Issued_Count,\n";
				 }
			 }
				
			//add by drdu 20100408--------------------
				sql+="N.trans_his_id,decode(N.tax_count,null,N.ACT_ISSUED_COUNT*0.17,N.tax_count)\n";
			//=----------------------------------------- 
			sql+=" FROM INV_J_ISSUE_DETAILS D ,INV_C_MATERIAL M \n";
					//----------add by fyyang 20100202-----------------------
                     if(issueDate!=null&&!issueDate.equals(""))
                     {
                    	 sql+=",(\n" +
					"    select t.check_date,t.trans_his_id,t.tax_count,t.order_no,t.material_id,t.trans_qty+nvl(tb.sl,0) as  ACT_ISSUED_COUNT,  " +
					//---add by fyyang 20100226---------------
					"decode(t.trans_qty + nvl(tb.sl, 0),\n" +
					"                      0,\n" + 
					"                      0,\n" + 
					"                      (t.trans_qty * t.std_cost + nvl(tb.price, 0)) /\n" + 
					"                      (t.trans_qty + nvl(tb.sl, 0))) act_std_cost,\n" + 
					"               t.trans_qty * t.std_cost + nvl(tb.price, 0) act_price \n"+
                   //--------------------------------------------
					"from inv_j_transaction_his t,resourse_v_rollback_arrival tb\n" + 
					"    where t.trans_id=4 and t.trans_his_id=tb.trans_his_id(+)\n" + 
					"    and t.order_no='"+strIssueNo+"'\n" + 
					"    and to_char(t.last_modified_date, 'yyyy-mm-dd hh24:mi:ss')='"+issueDate+"'\n" + 
					"    and t.is_use='Y'\n" + 
					"    and t.trans_qty + nvl(tb.sl, 0)<>0"+
					" )N,inv_j_issue_head P \n"+
					" WHERE  N.order_no=P.issue_no and P.issue_head_id=D.issue_head_id and N.material_id=D.material_id\n";
                     }
                     else
                     {
                    	 if(startCheckDate!=null&&endCheckDate!=null)
                    	 {
                    		 sql+=
                    			 ",(select t.check_date,t.order_no,t.material_id,t.trans_his_id,sum(round(t.tax_count,2)) tax_count,\n" +
                    			 "                      sum(round(t.trans_qty * t.std_cost, 2)) act_price,sum(t.trans_qty) ACT_ISSUED_COUNT\n" + 
                    			 "                 from inv_j_transaction_his t\n" + 
                    			 "                where t.check_status = 'C'\n" + 
                    			 "                  and t.is_use = 'Y'   and t.enterprise_code='hfdc'\n" + 
/*                    			 "                  and t.Check_Date >=\n" + 
                    			 "                      to_date('2010-02-01' || ' 00:00:00',yyyy-mm-dd hh24:mi:ss')\n" + 
                    			 "                  and t.Check_Date <=\n" + 
                    			 "                      to_date('2010-03-26' || ' 23:59:59','yyyy-mm-dd hh24:mi:ss')\n" + */
                    			 "                group by t.check_date,t.order_no,t.material_id,t.trans_his_id)N,inv_j_issue_head P \n"+
                    			 "       where P.issue_head_id=D.ISSUE_HEAD_ID   and P.issue_no='"+strIssueNo+"'  and N.order_no=P.issue_no and  d.material_id= N.material_id  \n";

                    	 }
                    	 else
                    	 {
                    	 sql+=",inv_j_issue_head N \n"+
                    		  " where  N.issue_head_id=D.ISSUE_HEAD_ID and N.issue_no='"+strIssueNo+"' \n";
                    	 }
                     }

					//------------------------------------
			
					sql+=
					 " AND D.MATERIAL_ID = M.MATERIAL_ID  \n"
					//+ "AND D.APPROVED_COUNT - D.ACT_ISSUED_COUNT = 0 \n"
					+ "AND D.IS_USE = 'Y' \n"
					+ "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "AND D.IS_USE = 'Y' \n"
					+ "AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "ORDER BY M.MATERIAL_NO";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			String sqlCount = 
				"    select count(*) from inv_j_transaction_his t\n" + 
			"    where t.trans_id=4\n" + 
			"    and t.order_no='"+strIssueNo+"'\n" + 
			"    and to_char(t.last_modified_date, 'yyyy-mm-dd hh24:mi:ss')='"+issueDate+"'\n" + 
			"    and t.is_use='Y'\n";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				IssueMaterialDetailInfo model = new IssueMaterialDetailInfo();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					// 领料单明细id
					model.setIssueDetailsId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					// 物料ID
					model.setMaterialId(Long.parseLong(data[1].toString()));
				}
				if (data[2] != null) {
					// 物料编号
					model.setMaterialNo(data[2].toString());
				}
				if (data[3] != null) {
					// 物料名称
					model.setMaterialName(data[3].toString());
				}
				if (data[4] != null) {
					// 存货计量单位
					model.setStockUmId(Long.parseLong(data[4].toString()));
					// 单位名称
					// 对应 ->commonInterface jar包删除
					BpCMeasureUnit unit = unitRemote.findById(model
							.getStockUmId());
					if (unit != null) {
						model.setUnitName(unit.getUnitName());
					}
				}
				if (data[5] != null) {
					// 需求数量
					model.setDemandNum(Double.parseDouble(data[5].toString()));
				}
				if (data[6] != null) {
					// 实际数量
					model.setActIssuedCount(Double.parseDouble(data[6]
							.toString()));
				}
				if (data[7] != null) {
					// 待发货数量
					model.setWaitNum(Double.parseDouble(data[7].toString()));
				}
				if (data[8] != null) {
					model.setItemCode(data[8].toString());
//					// 费用来源id
//					model.setItemId(Long.parseLong(data[8].toString()));
				}
				if (data[9] != null) {
					// 需求计划明细ID
					model.setRequirementDetailId(Long.parseLong(data[9]
							.toString()));
				}
				if (data[10] != null) {
					// 上次修改时间
					model.setLastModifiedDate(data[10].toString());
				}

				//add by drdu 091126
				if(data[11] != null)
					model.setUnitPrice(Double.parseDouble(data[11].toString()));
				if(data[12] != null)
					model.setActPrice(Double.parseDouble(data[12].toString()));
				if(data[13] != null)
					model.setTransHisId(Long.parseLong(data[13].toString()));
				if(data[14] != null)
					model.setTaxCount(Double.parseDouble(data[14].toString()));
				// add by liuyi 091126 计算金额
				if(model.getUnitPrice() != null && model.getActIssuedCount() != null)
					model.setActPrice(model.getUnitPrice() * model.getActIssuedCount());
				arrlist.add(model);
			}
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	//------------------add by fyyang 090708-----------------------------
	/**
	 * 获得已审核过的领料单物资列表 (用于出库红单处理页面)
	 * add by fyyang 090708 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findIssueDetailCheckList(String enterpriseCode, String sDate,
			String eDate,String materialName, String issuNo, String isRedBill,String materialNo,
			String specNo, final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "";
		String sqlCount = "";
		String whereStr = "";
		if(isRedBill != null && isRedBill.length() > 0) {
			if("Y".equals(isRedBill)) {
				sql = "select a.issue_details_id,\n"
					+ "       a.last_modified_date,\n" + "       d.issue_no,\n"
					+ "       a.ITEM_CODE,\n" + "       a.material_id,\n"
					+ "       b.material_no,\n" + "       b.material_name,\n"
					+ "       b.spec_no,\n" + "       b.stock_um_id,\n"
					+ "       a.act_issued_count,\n"
					+ "       getdeptname(d.fee_by_dep) deptName,\n"
					+ "       getspecialname(d.fee_by_special) specialName,\n"
					+ "       getworkername(a.last_modified_by),\n"
					+ "	  	  b.default_whs_no,"
					+ "	  	  b.default_location_no"
					+",a.act_issued_count as waitNum,e.std_cost \n"
					+ "  from inv_j_issue_details a,\n"
					+ "       inv_c_material      b,\n"
					+ "       inv_j_issue_head    d\n"
					+",inv_j_transaction_his  e \n"
					  //--------------------------------
//					+",(\n" +
//					"select t.order_no,t.material_id,max(t.check_date) checkDate\n" + 
//					"  from inv_j_transaction_his t\n" + 
//					" where\n" + 
//					"    t.check_status = 'C'\n" + 
//					"   and t.is_use = 'Y'\n" + 
//					"   group by t.order_no,t.material_id\n" + 
//					")t \n"
	           //----------------------------------------
					+ " where b.material_id = a.material_id\n"
					+" and d.issue_no=e.order_no \n"
					+ "   and d.issue_head_id = a.issue_head_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
					+ enterpriseCode + "'\n" ;
//				//--月结时间>审核时间 add by fyyang 20100311
//			    +"and t.order_no=d.issue_no\n" +
//			     "and t.material_id=a.material_id\n" + 
//			     "and t.checkDate<(select max( bb.balance_date) from inv_j_balance bb where bb.is_use='Y') \n";
//				
				sqlCount = "select count(1)\n"
					+ "  from inv_j_issue_details a,\n"
					+ "       inv_c_material      b,\n"
					+ "       inv_j_issue_head    d\n"
					  //--------------------------------
//					+",(\n" +
//					"select t.order_no,t.material_id,max(t.check_date) checkDate\n" + 
//					"  from inv_j_transaction_his t\n" + 
//					" where\n" + 
//					"    t.check_status = 'C'\n" + 
//					"   and t.is_use = 'Y'\n" + 
//					"   group by t.order_no,t.material_id\n" + 
//					")t \n"
	           //----------------------------------------
					+ " where b.material_id = a.material_id\n"
					+ "   and d.issue_head_id = a.issue_head_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
					+ enterpriseCode + "'\n";
//					//--月结时间>审核时间 add by fyyang 20100311
//				    +"and t.order_no=d.issue_no\n" +
//				     "and t.material_id=a.material_id\n" + 
//				     "and t.checkDate<(select max( bb.balance_date) from inv_j_balance bb where bb.is_use='Y') \n";
				whereStr += "and d.ref_issue_no is not null AND a.act_issued_count < 0\n" +
						"   and d.issue_no not in (select a.ref_issue_no from inv_j_issue_head a ,inv_j_issue_details b\n" + 
						"\t\t\t\t\t   where a.issue_head_id = b.issue_head_id and a.ref_issue_no is not null and b.act_issued_count > 0)";

			} else if("N".equals(isRedBill)) {
				sql = "select a.issue_details_id,\n"
					+ "       a.last_modified_date,\n" + "       d.issue_no,\n"
					+ "       a.ITEM_CODE,\n" + "       a.material_id,\n"
					+ "       b.material_no,\n" + "       b.material_name,\n"
					+ "       b.spec_no,\n" + "       b.stock_um_id,\n"
					+ "       c.totalNum,\n" // a.act_issued_count modify by ywliu 20100203
					+ "       getdeptname(d.fee_by_dep) deptName,\n"
					+ "       getspecialname(d.fee_by_special) specialName,\n"
					+ "       getworkername(a.last_modified_by),\n"
					+ "	  	  b.default_whs_no,"
					+ "	  	  b.default_location_no"
					+", c.totalNum+nvl(e.redNum,0),(c.totalPrice+nvl(e.redPrice,0))/ (c.totalNum+nvl(e.redNum,0)) \n"
					+ "  from inv_j_issue_details a,\n"
					+ "       inv_c_material      b,\n"
					+ "       inv_j_issue_head    d,\n"
					+ "		  resourse_v_checknum_his c"	
					+" ,resourse_v_rednum_his e \n"
					  //--------------------------------
					+",(\n" +
					"select t.order_no,t.material_id,max(t.check_date) checkDate\n" + 
					"  from inv_j_transaction_his t\n" + 
					" where\n" + 
					"    t.check_status = 'C'\n" + 
					"   and t.is_use = 'Y'\n" + 
					"   group by t.order_no,t.material_id\n" + 
					")t \n"
	           //----------------------------------------
					+ " where b.material_id = a.material_id\n"
					+"and c.order_no=e.ref_issue_no(+) \n"
                    +"and c.totalNum+nvl(e.redNum,0)>0 \n"
                    +" and c.material_id=e.material_id(+) \n"
					+ "   and d.issue_head_id = a.issue_head_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
					+ enterpriseCode + "'\n" 
					+ "   and c.order_no = d.issue_no\n" 
					+ "   and a.material_id = c.material_id \n"
				//--月结时间>审核时间 add by fyyang 20100311
			    +"and t.order_no=d.issue_no\n" +
			     "and t.material_id=a.material_id\n" + 
			     "and t.checkDate<(select max( bb.balance_date) from inv_j_balance bb where bb.is_use='Y') \n";
				sqlCount = "select count(1)\n"
					+ "  from inv_j_issue_details a,\n"
					+ "       inv_c_material      b,\n"
					+ "       inv_j_issue_head    d,\n"
					+ "		  resourse_v_checknum_his c"	
					+" ,resourse_v_rednum_his e \n"
					  //--------------------------------
					+",(\n" +
					"select t.order_no,t.material_id,max(t.check_date) checkDate\n" + 
					"  from inv_j_transaction_his t\n" + 
					" where\n" + 
					"    t.check_status = 'C'\n" + 
					"   and t.is_use = 'Y'\n" + 
					"   group by t.order_no,t.material_id\n" + 
					")t \n"
	           //----------------------------------------
					+ " where b.material_id = a.material_id\n"
					+ "   and d.issue_head_id = a.issue_head_id\n"
					+"and c.order_no=e.ref_issue_no(+) \n"
                    +"and c.totalNum+nvl(e.redNum,0)>0 \n"
                    +" and c.material_id=e.material_id(+) \n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
					+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
					+ enterpriseCode + "'\n"
					+ "   and c.order_no = d.issue_no\n" 
					+ "   and a.material_id = c.material_id \n"
					//--月结时间>审核时间 add by fyyang 20100311
				    +"and t.order_no=d.issue_no\n" +
				     "and t.material_id=a.material_id\n" + 
				     "and t.checkDate<(select max( bb.balance_date) from inv_j_balance bb where bb.is_use='Y') \n";

			}
		} else {
			sql = "select a.issue_details_id,\n"
				+ "       a.last_modified_date,\n" + "       d.issue_no,\n"
				+ "       a.ITEM_CODE,\n" + "       a.material_id,\n"
				+ "       b.material_no,\n" + "       b.material_name,\n"
				+ "       b.spec_no,\n" + "       b.stock_um_id,\n"
				+ "       c.totalNum,\n" // a.act_issued_count modify by ywliu 20100203
				+ "       getdeptname(d.fee_by_dep) deptName,\n"
				+ "       getspecialname(d.fee_by_special) specialName,\n"
				+ "       getworkername(a.last_modified_by),\n"
				+ "	  	  b.default_whs_no,"
				+ "	  	  b.default_location_no"
				+", c.totalNum+nvl(e.redNum,0),"
				+"(c.totalPrice+nvl(e.redPrice,0))/ (c.totalNum+nvl(e.redNum,0)) \n"
				+ "  from inv_j_issue_details a,\n"
				+ "       inv_c_material      b,\n"
				+ "       inv_j_issue_head    d,\n"
				+ "		  resourse_v_checknum_his c\n"
				+" ,resourse_v_rednum_his e \n"
				  //--------------------------------
				+",(\n" +
				"select t.order_no,t.material_id,max(t.check_date) checkDate\n" + 
				"  from inv_j_transaction_his t\n" + 
				" where\n" + 
				"    t.check_status = 'C'\n" + 
				"   and t.is_use = 'Y'\n" + 
				"   group by t.order_no,t.material_id\n" + 
				")t \n"
           //----------------------------------------
				+ " where b.material_id = a.material_id\n"
				+ "   and d.issue_head_id = a.issue_head_id\n"
				+"and c.order_no=e.ref_issue_no(+) \n"
				+"  and c.totalNum+nvl(e.redNum,0)>0 \n"
				  +" and c.material_id=e.material_id(+) \n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
				+ enterpriseCode + "'\n" 
				+ "   and c.order_no = d.issue_no\n" 
				+ "   and a.material_id = c.material_id \n"
			//--月结时间>审核时间 add by fyyang 20100311
		    +"and t.order_no=d.issue_no\n" +
		     "and t.material_id=a.material_id\n" + 
		     "and t.checkDate<(select max( bb.balance_date) from inv_j_balance bb where bb.is_use='Y') \n";
			sqlCount = "select count(1)\n"
				+ "  from inv_j_issue_details a,\n"
				+ "       inv_c_material      b,\n"
				+ "       inv_j_issue_head    d,\n"
				+ "		  resourse_v_checknum_his c"	
				+" ,resourse_v_rednum_his e \n"
           //--------------------------------
				+",(\n" +
				"select t.order_no,t.material_id,max(t.check_date) checkDate\n" + 
				"  from inv_j_transaction_his t\n" + 
				" where\n" + 
				"    t.check_status = 'C'\n" + 
				"   and t.is_use = 'Y'\n" + 
				"   group by t.order_no,t.material_id\n" + 
				")t \n"
           //----------------------------------------
				+ " where b.material_id = a.material_id\n"
				+"and c.order_no=e.ref_issue_no(+) \n"
				+"  and c.totalNum+nvl(e.redNum,0)>0 \n"
				  +" and c.material_id=e.material_id(+) \n"
				+ "   and d.issue_head_id = a.issue_head_id\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
				+ enterpriseCode + "'\n"
				+ "   and c.order_no = d.issue_no\n" 
				+ "   and a.material_id = c.material_id \n"
				//--月结时间>审核时间 add by fyyang 20100311
			    +"and t.order_no=d.issue_no\n" +
			     "and t.material_id=a.material_id\n" + 
			     "and t.checkDate<(select max( bb.balance_date) from inv_j_balance bb where bb.is_use='Y') \n";

		}

		if (sDate != null && sDate.length() > 0) {
			whereStr += " and a.last_modified_date >= to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			whereStr += "and a.last_modified_date <= to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (materialName != null && !"".equals(materialName)) {
			whereStr += " and b.material_name like '%" + materialName + "%' \n";
		}
		// add by ywliu 20100203
		if (materialNo != null && !"".equals(materialNo)) {
			whereStr += " and b.material_no like '%" + materialNo + "%' \n";
		}
		// add by ywliu 20100203
		if (specNo != null && !"".equals(specNo)) {
			whereStr += " and b.spec_no like '%" + specNo + "%' \n";
		}
		
		if (issuNo != null && !"".equals(issuNo)) {
			whereStr += "  and d.issue_no like '%"+issuNo+"%' \n";
		}
		sql += whereStr;
		sqlCount += whereStr;
		sql += " order by d.issue_no";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		Iterator it = list.iterator();
		List<IssueMaterialDetailInfo> arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
			Double issuedCountTotal = 0.00;
			while (it.hasNext()) {
				IssueMaterialDetailInfo info = new IssueMaterialDetailInfo();
				Object[] data = (Object[]) it.next();
				info.setIssueDetailsId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					info.setLastModifiedDate(data[1].toString());
				if (data[2] != null)
					info.setIssueNo(data[2].toString());
				if (data[3] != null)
					info.setItemCode(data[3].toString());
					//info.setItemId(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					info.setMaterialId(Long.parseLong(data[4].toString()));
				if (data[5] != null)
					info.setMaterialNo(data[5].toString());
				if (data[6] != null)
					info.setMaterialName(data[6].toString());
				if (data[7] != null) {
					info.setSpecNo(data[7].toString());
				} else {
					info.setSpecNo("");
				}
				if (data[8] != null)
					info.setStockUmId(Long.parseLong(data[8].toString()));
				if (data[9] != null)
					info.setActIssuedCount(Double.parseDouble(data[9]
							.toString()));
				if (data[10] != null)
					info.setFreeDeptName(data[10].toString());
				if (data[11] != null)
					info.setFreeSpecialName(data[11].toString());
				if (data[12] != null)
					info.setLastModifyBy(data[12].toString());
				if (data[13] != null)
					info.setWhsNo(data[13].toString());
				if (data[14] != null)
					info.setLocationNo(data[14].toString());
				if(data[15]!=null)
					info.setWaitNum(Double.parseDouble(data[15].toString()));
				if(data[16]!=null)
					info.setUnitPrice(Double.parseDouble(data[16].toString()));
				issuedCountTotal += info.getActIssuedCount();
				arrlist.add(info);
			}
			// 计算当前页的合计数量 modify by ywliu 090722 
			IssueMaterialDetailInfo info = new IssueMaterialDetailInfo();
			info.setActIssuedCount(issuedCountTotal);
			arrlist.add(info);
		}
		// 计算总共的合计数量 modify by ywliu 090722 
		if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1 && rowStartIdxAndCount[0]!=0) {
			String sumSql = "select sum(a.act_issued_count)\n"
				+ "  from inv_j_issue_details a,\n"
				+ "       inv_c_material      b,\n"
				+ "       inv_j_issue_head    d\n"
				+ " where b.material_id = a.material_id\n"
				+ "   and d.issue_head_id = a.issue_head_id\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and d.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and b.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and d.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and  (a.act_issued_count > 0  or d.issue_status='B')  ";
			sumSql += whereStr ; 
			Double total = Double.parseDouble(bll.getSingal(sumSql).toString());
			IssueMaterialDetailInfo info = new IssueMaterialDetailInfo();
			info.setActIssuedCount(total);
			arrlist.add(info);
		}
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	/**
	 * 出库红单数据库操作
	 */
	public void updateDbTablesForIssueBack(InvJIssueHead headModel,
			InvJIssueDetails detailModel,
			 InvJLot lotModel,
			InvJTransactionHis transHisModel,
			InvJWarehouse wareHouseMdoel, InvJLocation locationModel) {
		try {
			List<InvJIssueDetails> detailList=new ArrayList<InvJIssueDetails>();
			detailList.add(detailModel);
			// modify by ywliu 20100125
			Long issueId = issueRemote.addIssueHeadAndDetails(headModel, detailList);
			// 更新批号记录表
				lotRemote.update(lotModel);
			
			// 插入事务历史表
			// 事务id
			Long tansId = bll.getMaxId("INV_J_TRANSACTION_HIS", "TRANS_HIS_ID");
			transHisModel.setTransHisId(tansId);
			// modify by ywliu 20100125
			String issueNo = "LL";
			String id = String.valueOf(issueId);
			if(id.length() > 6){
				issueNo += id.substring(0,6);
			}else{
				String pad = "000000";
				issueNo +=pad.substring(0, 6 - id.length()) + id;
			}
			transHisModel.setOrderNo(issueNo);
			// modify by ywliu 20100125
			transHisRemote.save(transHisModel);
			//更新库存记录表
				wareRemote.update(wareHouseMdoel);
			// 更新库位物料记录
				locationRemote.update(locationModel);
			
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	//--------------add by fyyang end---------------------------------------
	
	
	//========================================取消出库查询列表 add by drdu 090708===========================================//
	/**
	 *  出库回滚页面的查询  
	 *  modify by fyyang 091113
	 *  查询出已审批结束且未审核的所有进行过出库操作的物资
	 */
	@SuppressWarnings("unchecked")
	public PageObject findCancelIssueList(String enterpriseCode,String issueNo,String materialId,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = 
			"select distinct h.TRANS_HIS_ID,\n" +
			"                h.ORDER_NO,\n" + 
			"                h.SEQUENCE_ID,\n" + 
			"                t.TRANS_NAME,\n" + 
			"                m.MATERIAL_NO,\n" + 
			"                m.MATERIAL_NAME,\n" + 
			"                m.SPEC_NO,\n" + 
			"                m.PARAMETER,\n" + 
			//"                h.TRANS_QTY,\n" + 
			//modify by fyyang 091130
			"h.TRANS_QTY+ nvl(rb.sl, 0),\n"+  // modify by ywliu 20100201
			"                h.TRANS_UM_ID,\n" + 
			"                GETWORKERNAME(h.LAST_MODIFIED_BY),\n" + 
			"                h.LAST_MODIFIED_DATE,\n" + 
			"                wf.WHS_NAME wfWhsName,\n" + 
			"                lf.LOCATION_NAME lfLocationName,\n" + 
			"                wt.WHS_NAME wtWhsName,\n" + 
			"                lt.LOCATION_NAME ltLocationName,\n" + 
			"                h.LOT_NO,\n" + 
			"                h.MEMO\n" + 
			"  from INV_J_ISSUE_HEAD      i,\n" + 
			"       INV_J_TRANSACTION_HIS h,\n" + 
			"       INV_C_TRANSACTION     t,\n" + 
			"       INV_C_MATERIAL        m,\n" + 
			"       INV_C_WAREHOUSE       wf,\n" + 
			"       INV_C_LOCATION        lf,\n" + 
			"       INV_C_WAREHOUSE       wt,\n" + 
			"       INV_C_LOCATION        lt,\n" + // modify by ywliu 20100201
			"		resourse_v_rollback_arrival rb" +
			" where t.TRANS_ID = h.TRANS_ID\n" + 
			"   and h.order_no = i.issue_no\n" + 
			"   and t.trans_code = 'I'\n" + 
			"   and i.issue_status = '2'\n" + 
			"   and m.MATERIAL_ID = h.MATERIAL_ID\n" + 
			"   and wf.WHS_NO(+) = h.FROM_WHS_NO\n" + 
			"   and lf.WHS_NO(+) = h.FROM_WHS_NO\n" + 
			"   and lf.LOCATION_NO(+) = h.FROM_LOCATION_NO\n" + 
			"   and wt.WHS_NO(+) = h.TO_WHS_NO\n" + 
			"   and lt.WHS_NO(+) = h.TO_WHS_NO\n" + 
			"   and lt.LOCATION_NO(+) = h.TO_LOCATION_NO\n" + 
			"   and i.is_use = 'Y'\n" + 
			"   and h.is_use = 'Y'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and m.is_use = 'Y'\n" + 
			"   and wf.is_use(+) = 'Y'\n" + 
			"   and lf.is_use(+) = 'Y'\n" + 
			"   and wt.is_use(+) = 'Y'\n" + 
			"   and lt.is_use(+) = 'Y'\n" + 
			"   and i.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and h.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and wf.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and lf.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and wt.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and lt.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and h.trans_qty>0 \n"+
			"   and h.check_status is null"+
			"   and h.trans_his_id = rb.trans_his_id(+) \n"+ 
			"   and h.TRANS_QTY+nvl(rb.sl, 0)>0 \n"; // modify by ywliu 20100201

		//modify by fyyang 091113
//			"   and (select count(1)\n" + 
//			"          from INV_J_ISSUE_DETAILS d\n" + 
//			"         where d.issue_head_id = i.issue_head_id\n" + 
//			"           and d.approved_count <> d.act_issued_count) = 0";

		String sqlCount = 
			"select distinct count(1)\n" +
			"  from INV_J_ISSUE_HEAD      i,\n" + 
			"       INV_J_TRANSACTION_HIS h,\n" + 
			"       INV_C_TRANSACTION     t,\n" + 
			"       INV_C_MATERIAL        m,\n" + 
			"       INV_C_WAREHOUSE       wf,\n" + 
			"       INV_C_LOCATION        lf,\n" + 
			"       INV_C_WAREHOUSE       wt,\n" + 
			"       INV_C_LOCATION        lt,\n" + 
			"		resourse_v_rollback_arrival rb" +// modify by ywliu 20100201
			" where t.TRANS_ID = h.TRANS_ID\n" + 
			"   and h.order_no = i.issue_no\n" + 
			"   and t.trans_code = 'I'\n" + 
			"   and i.issue_status = '2'\n" + 
			"   and m.MATERIAL_ID = h.MATERIAL_ID\n" + 
			"   and wf.WHS_NO(+) = h.FROM_WHS_NO\n" + 
			"   and lf.WHS_NO(+) = h.FROM_WHS_NO\n" + 
			"   and lf.LOCATION_NO(+) = h.FROM_LOCATION_NO\n" + 
			"   and wt.WHS_NO(+) = h.TO_WHS_NO\n" + 
			"   and lt.WHS_NO(+) = h.TO_WHS_NO\n" + 
			"   and lt.LOCATION_NO(+) = h.TO_LOCATION_NO\n" + 
			"   and i.is_use = 'Y'\n" + 
			"   and h.is_use = 'Y'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and m.is_use = 'Y'\n" + 
			"   and wf.is_use(+) = 'Y'\n" + 
			"   and lf.is_use(+) = 'Y'\n" + 
			"   and wt.is_use(+) = 'Y'\n" + 
			"   and lt.is_use(+) = 'Y'\n" + 
			"   and i.enterprise_code = 'hfdc'\n" + 
			"   and h.enterprise_code = 'hfdc'\n" + 
			"   and t.enterprise_code = 'hfdc'\n" + 
			"   and m.enterprise_code = 'hfdc'\n" + 
			"   and wf.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and lf.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and wt.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and lt.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and h.trans_qty>0 \n"+
			"   and h.check_status is null"+
			"   and h.trans_his_id = rb.trans_his_id(+) \n"+ 
			"   and h.TRANS_QTY+nvl(rb.sl, 0)>0 \n"; // modify by ywliu 20100201
		//modify by fyyang 091113
//			"   and (select count(1)\n" + 
//			"          from INV_J_ISSUE_DETAILS d\n" + 
//			"         where d.issue_head_id = i.issue_head_id\n" + 
//			"           and d.approved_count <> d.act_issued_count) = 0";

		String strWhere = "";
		if (materialId != null && !"".equals(materialId)) {
			strWhere += " and m.MATERIAL_NAME like '%" + materialId + "%'";
		}
		
		if (issueNo != null && !issueNo.equals("")) {
			strWhere += "  and h.ORDER_NO like '%" + issueNo + "%'\n";
		}
		sql += strWhere;
		sqlCount += strWhere;
		sql += " order by m.MATERIAL_NO, h.TRANS_HIS_ID"; 
		sqlCount += " order by m.MATERIAL_NO, h.TRANS_HIS_ID"; 
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if(list !=null && list.size()>0)
		{
			Iterator it = list.iterator();
			while (it.hasNext()) {
				TransActionHisInfo model = new TransActionHisInfo();
				Object[] data = (Object[]) it.next();
				// 流水号
				if (data[0] != null)
					model.setTransHisId(Long.parseLong(data[0].toString()));
				// 单据号
				if (data[1] != null)
					model.setOrderNo(data[1].toString());
				// 项号
				if (data[2] != null)
					model.setSequenceNo(Long.parseLong(data[2].toString()));
				// 事务名称
				if (data[3] != null)
					model.setTransName(data[3].toString());
				// 物料编码
				if (data[4] != null)
					model.setMaterialNo(data[4].toString());
				// 物料名称
				if (data[5] != null)
					model.setMaterialName(data[5].toString());
				// 规格型号
				if (data[6] != null)
					model.setSpecNo(data[6].toString());
				// 材质参数
				if (data[7] != null)
					model.setParameter(data[7].toString());
				// 异动数量
				if (data[8] != null)
					model.setTransQty(Double.parseDouble(data[8].toString()));
				// 单位
				if (data[9] != null) {
					BpCMeasureUnit bcmu = unitRemote.findById(Long.parseLong(data[9].toString()));
	    		    if(bcmu!=null){
	    		    	String stockUmID = bcmu.getUnitName();
	    		    	model.setTransUmId(stockUmID);
	    		    }
	    		}
				// 操作人
				if (data[10] != null) {
					model.setOperatedBy(data[10].toString());
//				    Employee emp = personInfo.getEmployeeInfo(data[10].toString());
//				    if(emp != null) {
//						// 人员姓名
//						String strChsName = emp.getWorkerName();
//						model.setOperatedBy(strChsName);
//				    }
				}
				// 操作时间
				if (data[11] != null)
					model.setOperatedDate(data[11].toString());
				// 操作仓库
				if (data[12] != null)
					model.setWhsName(data[12].toString());
				// 操作库位
				if (data[13] != null)
					model.setLocationName(data[13].toString());
				// 调入仓库
				if (data[14] != null)
					model.setWhsNameTwo(data[14].toString());
				// 调入库位
				if (data[15] != null)
					model.setLocationNameTwo(data[15].toString());
				// 批号
				if (data[16] != null)
					model.setLotNo(data[16].toString());
				// 备注
				if (data[17] != null)
					model.setMemo(data[17].toString());
				
				arrlist.add(model);
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
		}
		return pg;
	}
	
	/**
	 * 出库回滚操作
	 * @param enterpriseCode
	 * @param tHisId
	 * @param qty
	 */
	// modified by liuyi 091109 数量留有四位小数
//	public void updateIssueQty(String enterpriseCode,Long tHisId,Long qty)
	public void updateIssueQty(String enterpriseCode,Long tHisId,Double qty,String workCode)
	{
		InvJTransactionHis his = transHisRemote.findById(tHisId);
		Long materialId;
		Long issueDetailId;
		String whsNo = "";
		String lotNo ="";
		String locationNo="";
		
		whsNo = his.getFromWhsNo();
		materialId = his.getMaterialId();
		issueDetailId = his.getSequenceId();
		lotNo = his.getLotNo();
		locationNo=his.getFromLocationNo();
		
		////修改主物料里的标准成本 add by ywliu 20091113
		//modify by fyyang 091130
		Double stdCostTemp=invCMaterialRemote.updateStdCost(materialId, enterpriseCode, qty, his.getStdCost());
//		String tempQty = purchaseWarehouse.findWarehouseQty(materialId, his.getEnterpriseCode());
//		Double warehouseQty = Double.parseDouble(tempQty);
//		// 标准成本ＴＥＭＰ add by ywliu 20091113
//		//modify by fyyang 091118
//		Double stdCostTemp = (his.getStdCost() * warehouseQty + his.getStdCost() * qty)
//				/ (warehouseQty + qty);
//		//---modify end -------
		
//		//修改物料主文件 add by ywliu 20091113
//		String sql = "update inv_c_material h\n" +
//				"         set h.std_cost = "+stdCostTemp+"\n"+
//				"       where h.material_id = '"+materialId+"'\n" +
//				"         and h.is_use = 'Y'\n" + 
//				"         and h.enterprise_code = '"+enterpriseCode+"'";
//		bll.exeNativeSQL(sql);
		
		//修改库存物料记录
		String sql1 = "update inv_j_warehouse a\n" +
			"   set a.issue = a.issue - "+qty+"\n" + 
			" where a.material_id = "+materialId+"\n" + 
			"   and a.whs_no = '"+whsNo+"'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";
		bll.exeNativeSQL(sql1);
		
		//修改领料单明细表
		String sql2 = "update inv_j_issue_details d\n" +
			"   set d.act_issued_count = d.act_issued_count - "+qty+"\n" + 
			" where d.issue_details_id = "+issueDetailId+"\n" + 
			"   and d.is_use = 'Y'\n" + 
			"   and d.enterprise_code = '"+enterpriseCode+"'";
		bll.exeNativeSQL(sql2);
		
//		//修改事务历史表
//		String sql3 = "update inv_j_transaction_his h\n" +
//			"   set h.trans_qty = h.trans_qty - "+qty+",\n" + 
//			"		h.std_cost = "+stdCostTemp+"\n"+ // modify by ywliu 20091113
//			" where h.trans_his_id = "+tHisId+"\n" + 
//			"   and h.is_use = 'Y'\n" + 
//			"   and h.enterprise_code = '"+enterpriseCode+"'";
//		bll.exeNativeSQL(sql3);
		
		//修改库位物料记录
		String sql4 =  "update inv_j_location l\n" +
			"   set l.issue = l.issue - "+qty+"\n" + 
			" where l.material_id = "+materialId+"\n" + 
			"   and l.whs_no = '"+whsNo+"'\n" + 
			"   and l.is_use = 'Y'\n" +
			"  and  l.location_no='"+locationNo+"' \n"+
			"   and l.enterprise_code = '"+enterpriseCode+"'";
		bll.exeNativeSQL(sql4);

		//修改批号记录表
		String sql5 =  "update INV_J_LOT t\n" +
			"   set t.issue = t.issue - "+qty+"\n" + 
			" where t.material_id = "+materialId+"\n" + 
			"   and t.whs_no = '"+whsNo+"'\n" + 
			"   and t.lot_no = '"+lotNo+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.location_no='"+locationNo+"'  \n"+
			"   and t.enterprise_code = '"+enterpriseCode+"'";
		bll.exeNativeSQL(sql5);

		// 修改物资需求计划明细表 add by liuyi 091124
		String sql6 = " update MRP_J_PLAN_REQUIREMENT_DETAIL a \n"
			+ " set a.iss_qty = a.iss_qty - "+qty+" \n"
			+ " where a.requirement_detail_id = ("
			+ "  select b.requirement_detail_id  from inv_j_issue_details b \n"
			+" where b.issue_details_id = "+issueDetailId+"\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"') \n";
		bll.exeNativeSQL(sql6);
		
		
		//--------add by fyyang 091130-------------------------
		//回滚时在事务历史表里增加一条数量为负数的数据。将回滚的原记录id保存在新产生的记录的备注字段里面以关联查找
		his.setTransQty(0-qty);
		his.setStdCost(stdCostTemp);
		his.setRollBackId(his.getTransHisId());// modify by ywliu 20100223
		his.setTransHisId(null);
		his.setLastModifiedDate(new Date());
		his.setLastModifiedBy(workCode);
		transHisRemote.save(his);
		//-----------------------------------------------------
	}
	//--------------add by drdu 090708 end-----------------------------------------------
	
	/**
	 * 出库管理页面中补打印列表记录
	 * add by drdu 091121
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAfterPrintIssueList(String issueNo, int start, int limit, String enterpriseCode)
	{
		String sql = "select distinct h.ORDER_NO,\n" +
			"                to_char(h.last_modified_date, 'yyyy-mm-dd hh24:mi:ss')\n" + 
			// add by liuyi 20100201 增加领料单的单据种类
			" ,aa.plan_original_id \n" + 
//			"  from INV_J_TRANSACTION_HIS h, INV_C_TRANSACTION t\n" + 
			"  from INV_J_TRANSACTION_HIS h, INV_C_TRANSACTION t,INV_J_ISSUE_HEAD aa, resourse_v_rollback_arrival tb\n" +
			" where t.TRANS_ID = h.TRANS_ID\n" + 
			" and h.trans_his_id=tb.trans_his_id(+) \n"+
			"   and h.order_no = '"+issueNo+"'\n" + 
			"   and h.is_use = 'Y'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and h.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			//"   and h.trans_qty > 0  " +
			"  and h.roll_back_id is null \n "+
			"and h.trans_qty+nvl(tb.sl,0) <> 0\n" + 
			"  and h.order_no=aa.issue_no(+) " +
			" order by to_char(h.last_modified_date, 'yyyy-mm-dd hh24:mi:ss') desc";
		
		String sqlcount = "select count(1)\n" +
		"  from ("+sql+")";
		
		List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		List<TransActionHisInfo> arraylist = new ArrayList<TransActionHisInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			TransActionHisInfo model = new TransActionHisInfo();
			if(data[0] != null)
				model.setOrderNo(data[0].toString());
			if(data[1] != null)
				model.setOperatedDate(data[1].toString());
			
			// add by liuyi 20100201 用memo存领料单的单据种类
			if(data[2] != null)
				model.setMemo(data[2].toString());
			else 
				model.setMemo("");
			// modified by liuyi 20100201 增加显示物资Id
			String sqlDetail = "";
			if(model.getMemo().equals("1"))
			{
				sqlDetail = "select distinct h.from_whs_no,\n"
						+ " getwhsname(h.from_whs_no),h.material_id\n"
						+ "  from INV_J_TRANSACTION_HIS h, resourse_v_rollback_arrival tb\n"
						+ " where h.order_no = '" + data[0].toString() + "'\n"
						+"and h.trans_his_id=tb.trans_his_id(+)  " +
						//"and h.trans_qty > 0  " +
						"  and h.roll_back_id is null \n "+
						"and h.trans_qty+nvl(tb.sl,0) <> 0 \n"
						+ "   and h.is_use = 'Y'\n"
						+ "   and h.last_modified_date =\n"
						+ "       to_date('" + data[1].toString()
						+ "', 'yyyy-MM-dd hh24:mi:ss')" +
						"   and h.trans_qty+nvl((   select t.trans_qty from inv_j_transaction_his t where t.roll_back_id=h.trans_his_id),0)>0 \n"//modify by ywliu 20100223
						;
			}
			else
			{
				sqlDetail = "select distinct h.from_whs_no,\n" +
				" getwhsname(h.from_whs_no)\n" +  
					"  from INV_J_TRANSACTION_HIS h, resourse_v_rollback_arrival tb\n" + 
					" where h.order_no = '"+data[0].toString()+"'\n" + 
					"and h.trans_his_id=tb.trans_his_id(+)  " +
					//"and h.trans_qty > 0 " +
					"  and h.roll_back_id is null \n "+
					" and h.trans_qty+nvl(tb.sl,0) <> 0 \n"+
					"   and h.is_use = 'Y'\n" + 
					"   and h.last_modified_date =\n" + 
					"       to_date('"+data[1].toString()+"', 'yyyy-MM-dd hh24:mi:ss')";
			}
			

			
			List<Object> listDetail = bll.queryByNativeSQL(sqlDetail);
			Iterator itDetail = listDetail.iterator();
			while (itDetail.hasNext()) {
				Object[] o = (Object[]) itDetail.next();
				if(o[0] != null)
				{
					if(model.getWhsNo()==null||model.getWhsNo().equals(""))
					model.setWhsNo(o[0].toString());
					else {
						model.setWhsNo(model.getWhsNo()+","+o[0].toString());
					}
				}
				if(o[1] != null)
				{
					if(model.getWhsName()==null||model.getWhsName().equals(""))
					{
						model.setWhsName(o[1].toString());
					}else{
						model.setWhsName(model.getWhsName()+","+o[1].toString());
					}
				}
				// add by liuyi 20100201 增加物资id
				if(model.getMemo().equals("1") && o[2] != null)
				{
					if(model.getMaterialNo() == null || model.getMaterialNo().equals(""))
						model.setMaterialNo(o[2].toString());
					else
						model.setMaterialNo(model.getMaterialNo() + "," + o[2].toString());
				}
				
			}
			
			arraylist.add(model);
		}
		Object countlist = bll.getSingal(sqlcount);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;
	}
	
	/**
	 * 出库补打印报表数据信息
	 * add by drdu 091121 modified by liuyi 20100201
	 * @param issueNo
	 * @param fillDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
//	public List getAfterPrintIssueInfo(String issueNo,String fillDate,String whsNo)
	public List getAfterPrintIssueInfo(String issueNo,String fillDate,String whsNo,String gdFlag,String materialId)
	{
		String sql = "select a.order_no,\n" +
			"       a.std_cost,\n" + 
			"       GETWHSNAME(a.from_whs_no),\n" + 
			"       a.tax_rate,\n" + 
			"       b.material_no,\n" + 
			"       b.material_name,\n" + 
			"       b.spec_no,\n" + 
			"       GETUNITNAME(a.trans_um_id),\n" + 
			"       GETDEPTNAME(a.receive_dept),\n" + 
			"       GETWORKERNAME(c.get_real_person),\n" + 
			"       d.applied_count,\n" + 
			" a.trans_qty+nvl(tb.sl,0),"+//modify by fyyang 20100226
			"       GETWORKERNAME(a.last_modified_by),\n" + 
			"       GETPROJECTNAME(c.project_code),\n" + 
			"       c.plan_original_id,\n" + 
			"       GETWORKERNAME(e.caller) \n" + 
			",a.std_cost*a.trans_qty+nvl(tb.price,0)"+//modify by fyyang 20100226
			 " ,c.item_code, getitemfactname(c.item_code) \n" + // add by liuyi 20100316
			"  from INV_J_TRANSACTION_HIS a,\n" + 
			"       inv_c_material        b,\n" + 
			"       INV_J_ISSUE_HEAD      c,\n" + 
			"       INV_J_ISSUE_DETAILS   d,\n" + 
			"       wf_j_historyoperation e\n" + 
			" ,resourse_v_rollback_arrival tb"+  //add by fyyang 20100226
			" where a.material_id = b.material_id\n" + 
			"   and a.order_no = c.issue_no\n" + 
			"   and a.sequence_id = d.issue_details_id\n" + 
			"   and a.trans_his_id=tb.trans_his_id(+) \n"+//add by fyyang 20100226
			"   and d.act_issued_count <> 0\n" + 
			"   and a.order_no = '"+issueNo+"'\n" + 
			"   and a.from_whs_no = '"+whsNo+"'\n" + 
			"   and c.work_flow_no = e.entry_id\n" + 
			"   and e.action_id = '83'\n" + 
			"    and a.trans_qty+nvl(tb.sl,0)<>0"+//modify by fyyang 20100226
			"   and a.last_modified_date =\n" + 
			"       to_date('"+fillDate+"', 'yyyy-MM-dd hh24:mi:ss')";
		
		// add by liuyi 20100201
		if(gdFlag != null && gdFlag.equals("1") && materialId != null && !materialId.equals(""))
			sql += " and a.material_id=" + materialId + " \n";
		List<Object> list = bll.queryByNativeSQL(sql);		
		List<IssueMaterialDetailPrintInfo> arraylist = new ArrayList<IssueMaterialDetailPrintInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			IssueMaterialDetailPrintInfo model = new IssueMaterialDetailPrintInfo();
			if (data[0] != null) {
				model.setIssueNo(data[0].toString());
			}
			if (data[1] != null) {
				// modified by liuyi 091130 
//				model.setUnitPrice(Double.valueOf(data[1].toString()));
				Double price = Math.rint(Double.parseDouble(data[1].toString()) * 10000) / 10000;
				model.setUnitPriceString(this.dataParseFour(price));
			}else
				model.setUnitPriceString("0.0000");
			if (data[2] != null) {
				model.setWhsNo(data[2].toString());
			}
			if (data[3] != null) {
				model.setTaxRate(data[3].toString());
			}
			if (data[4] != null) {
				model.setMaterialNo(data[4].toString());
			}
			if (data[5] != null) {
				model.setMaterialName(data[5].toString());
			}
			if (data[6] != null) {
				model.setSpecNo(data[6].toString());
			}
			if (data[7] != null) {
				model.setUnitName(data[7].toString());
			}
			if (data[8] != null) {
				model.setReceiveDept(data[8].toString());
			}
			if (data[9] != null) {
				model.setReceiveMan(data[9].toString());
			}	
			if (data[10] != null) {
				model.setDemandNum(Double.valueOf(data[10].toString()));
			}
			if (data[11] != null) {
//				model.setActIssuedCount(data[11].toString());
				// modified by liuyi 091201
				model.setActIssuedCount(this.dataParseFour(Double.parseDouble(data[11].toString())));
			}
			if (data[12] != null) {
				model.setLastModifyBy(data[12].toString());
			}
			if (data[13] != null) {
				model.setProjectName(data[13].toString());
			}
			if (data[14] != null) {
				model.setIssueType(data[14].toString());
			}
			if (data[15] != null) {
				model.setMaterialKeeper(data[15].toString());
			}
			// add by liuyi 091201 金额
			if(data[16] != null)
			{
				Double total = Math.rint(Double.parseDouble(data[16].toString()) * 100) /100;
				model.setTotalPrice(this.dataOperate(total));
			}else {
				model.setTotalPrice("0.0");
			}
			
			// add by liuyi 20100316
			if(data[17] != null)
				model.setItemCode(data[17].toString());
			if(data[18] != null)
				model.setItemName(data[18].toString());
			else
				model.setItemName("");
			arraylist.add(model);
		}
		return arraylist;
	}
	public List<ConApproveForm> getApproveList(String issueNo){
		InvJIssueHead model_Head =  issueHeadRemote.findByMatCode(issueNo,null);
		Long entryId = model_Head.getWorkFlowNo();
		Long planOriginalId = model_Head.getPlanOriginalId();
		String stepId ="";
		if(planOriginalId == 1l || planOriginalId == 2l){
			stepId = "4,8";
		}else if(planOriginalId == 4l || planOriginalId == 5l || planOriginalId == 12l){
			stepId = "4,8";
		}else{
			stepId = "4,8";
		}
		List list = new ArrayList();
		String sql = "SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id in ("+stepId+")"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')" + " ORDER BY t.opinion_time ";

		list = bll.queryByNativeSQL(sql);
		List<ConApproveForm> arraylist = new ArrayList<ConApproveForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			ConApproveForm model = new ConApproveForm();
//			if (data[0] != null) {
//				model.setId(Long.parseLong(data[0].toString()));
//			}
//			if (data[3] != null) {
//				model.setStepName(data[3].toString());
//			}
//			if (data[8] != null) {
//				model.setOpinion(data[8].toString());
//			}
//			if (data[9] != null) {
//				model.setOpinionTime(data[9].toString());
//				;
//			}
			if (data[10] != null) {
				model.setCaller(data[10].toString());
			}
			arraylist.add(model);
		}
		return arraylist;
	
	}
	/**
	 * add by fyyang 091224
	 * modify by fyyang 20100303 优化sql
	 */
	public PageObject queryCheckedIssueList(String sdate,String edate,String issueNo,String receiptBy,String receiptDept,String enterpriseCode,String billType,final int... rowStartIdxAndCount)
	{
		String strWhere="";
//		String sql=
//			"SELECT DISTINCT H.ISSUE_HEAD_ID,\n" +
//			"                H.ISSUE_NO,\n" + 
//			"                GETWORKERNAME(H.GET_REAL_PERSON),\n" + 
//			"                GETWORKERNAME(H.RECEIPT_BY),\n" + 
//			"                GETDEPTNAME(H.RECEIPT_DEP),\n" + 
//			"                (select sum(round(t.trans_qty * t.std_cost, 2))\n" + 
//			"                   from inv_j_transaction_his t\n" + 
//			"                  where t.order_no =  H.Issue_No" +
//			"	and t.check_status = 'C'" +	
//			"   and t.is_use='Y' \n"+
//			"   and t.Check_Date >=\n" + 
//			"       to_date('"+sdate+"' || ' 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n" +
//			"   and t.Check_Date <=\n" + 
//            "   to_date('"+edate+"' || ' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n" +
//			") totalPrice,\n" + 
//			"                H.DUE_DATE,\n" + 
//			"                H.PLAN_ORIGINAL_ID,\n" + 
//			"                H.ITEM_CODE,\n" + 
//			"                H.MEMO,\n" + 
//			"                H.RECEIPT_DEP,\n" + 
//			"                H.RECEIPT_BY,\n" + 
//			"                H.WO_NO,\n" + 
//			"                H.FEE_BY_DEP,\n" + 
//			"                H.IS_EMERGENCY,\n" + 
//			"                to_char(H.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss'),\n" + 
//			"                H.MR_NO\n" + 
//			"  FROM INV_J_ISSUE_HEAD H, Inv_j_Transaction_His B\n" + 
//			" WHERE H.IS_USE = 'Y'\n" + 
//			"   AND B.ORDER_NO = H.ISSUE_NO" +
//			"   AND H.ENTERPRISE_CODE = 'hfdc'\n" +
//			"   AND B.Check_STATUS = 'C'\n" ;
		String sql=
			"select\n" +
			"         H.ISSUE_HEAD_ID,\n" + 
			"         H.ISSUE_NO,\n" + 
			"         GETWORKERNAME(H.GET_REAL_PERSON),\n" + 
			"         GETWORKERNAME(H.RECEIPT_BY),\n" + 
			"         GETDEPTNAME(H.RECEIPT_DEP),\n" + 
			"         B.totalPrice,\n" + 
			"         H.DUE_DATE,\n" + 
			"         H.PLAN_ORIGINAL_ID,\n" + 
			"         H.ITEM_CODE,\n" + 
			"         H.MEMO,\n" + 
			"         H.RECEIPT_DEP,\n" + 
			"         H.RECEIPT_BY,\n" + 
			"         H.WO_NO,\n" + 
			"         H.FEE_BY_DEP,\n" + 
			"         H.IS_EMERGENCY,\n" + 
			"         to_char(H.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss'),\n" + 
			"         H.MR_NO\n" + 
			"          from INV_J_ISSUE_HEAD H,\n" + 
			"               (select t.order_no,\n" + 
			"                       sum(round(t.trans_qty * t.std_cost, 2)) totalPrice\n" + 
			"                  from inv_j_transaction_his t\n" + 
			"                 where t.check_status = 'C'\n" + 
			"                   and t.is_use = 'Y'   and t.enterprise_code='"+enterpriseCode+"' \n" + 
			"                   and t.Check_Date >=\n" + 
			"                       to_date('"+sdate+"' || ' 00:00:00',\n" + 
			"                               'yyyy-mm-dd hh24:mi:ss')\n" + 
			"                   and t.Check_Date <=\n" + 
			"                       to_date('"+edate+"' || ' 23:59:59',\n" + 
			"                               'yyyy-mm-dd hh24:mi:ss')\n" + 
			"                 group by t.order_no) B\n" + 
			"         where H.ISSUE_NO = B.order_no\n" + 
			"           and H.Is_Use = 'Y'\n" + 
			" and nvl(B.totalPrice,0)<>0\n"+ //add by fyyang 20100320
			"           and H.Enterprise_Code = '"+enterpriseCode+"'";

//		if(sdate!=null&&!sdate.equals(""))
//		{
//			strWhere+="   and B.Check_Date >=\n" + 
//			"       to_date('"+sdate+"' || ' 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n" ;
//		}
//		if(edate!=null&&!edate.equals(""))
//		{
//			strWhere+="   and B.Check_Date <=\n" + 
//		              "   to_date('"+edate+"' || ' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n";
//		}
		if(issueNo!=null&&!issueNo.equals(""))
		{
			strWhere+="   and H.Issue_No like '%"+issueNo.trim()+"%'\n";
		}
		if(receiptBy!=null&&!receiptBy.equals(""))
		{
			strWhere+="   and GETWORKERNAME(H.RECEIPT_BY) like '%"+receiptBy.trim()+"%'\n" ;
		}
		if(receiptDept!=null&&!receiptDept.equals(""))
		{
			strWhere+="   and GETDEPTNAME(H.RECEIPT_DEP) like '%"+receiptDept.trim()+"%'\n";
		}
		if(billType!=null&&!billType.equals(""))
		{
			strWhere+="  and  H.PLAN_ORIGINAL_ID="+billType+" \n";
		}
			
			sql+=strWhere;
			
			
			//sql+=" order by H.ISSUE_NO asc";
			
			String sqlCount="select count(*) from ("+sql+")tt";
			
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			
			// 计算总共的合计
			if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1&&rowStartIdxAndCount[0]!=0) 
			{
				sql+=
					"union\n" +
					" select  null,'总合计',null,null,null, nvl(sum(round(tt.totalPrice,2)),0),null,null,null,null,null,null,null,null,null,null,null\n" + 
					"from("+sql+")tt";

			}
			sql="select * from ("+sql+")ttt  order by ttt.ISSUE_NO asc ";
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Iterator it=list.iterator(); 
			Double price=0d;
			while(it.hasNext())
			{
				Object [] data=(Object [])it.next();
				if(!data[1].toString().equals("总合计"))
						{
					         if(data[5]!=null)
					         {
					        	 //modify by fyyang 100104
					        	 //NumberFormat formatter = new DecimalFormat(ZERO_POINT);
					        	// price=price+Double.parseDouble(formatter.format((Double.parseDouble(data[5].toString()))));
					        	 price=price+Double.parseDouble(data[5].toString());
					         }
						}
			}
			Object [] myData=new Object[]{null,"本页合计",null,null,null, price,null,null,null,null,null,null,null,null,null,null,null};
			if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1&&rowStartIdxAndCount[0]!=0) 
			{
				list.add(list.size()-1, myData);
			}
			else
			{
				list.add( myData);
			}
			
			PageObject obj=new PageObject();
			obj.setList(list);
			obj.setTotalCount(totalCount);
			return obj;

	}
	
	public PageObject queryCheckedIssueDetailList(String sdate,String edate,String issueNo,String receiptBy,String receiptDept,String enterpriseCode,String billType,final int... rowStartIdxAndCount) {
		String strWhere="";
		String sql=
			"SELECT H.ISSUE_HEAD_ID,\n" +
			"                H.ISSUE_NO,\n" + 
			"                GETWORKERNAME(H.GET_REAL_PERSON),\n" + 
			"                GETWORKERNAME(H.RECEIPT_BY),\n" + 
			"                GETDEPTNAME(H.RECEIPT_DEP),\n" + 
			"                round(B.Trans_Qty * B.Std_Cost,2)  totalPrice,\n" + 
			"                H.DUE_DATE,\n" + 
			"                H.PLAN_ORIGINAL_ID,\n" + 
			"                H.ITEM_CODE,\n" + 
			"                H.MEMO,\n" + 
			"                H.RECEIPT_DEP,\n" + 
			"                H.RECEIPT_BY,\n" + 
			"                H.WO_NO,\n" + 
			"                H.FEE_BY_DEP,\n" + 
			"                H.IS_EMERGENCY,\n" + 
			"                to_char(b.check_date, 'yyyy-mm-dd hh24:mi:ss'),\n" + 
			"                H.MR_NO\n" + 
			"  FROM INV_J_ISSUE_HEAD H, Inv_j_Transaction_His B\n" + 
			" WHERE H.IS_USE = 'Y'\n" + 
			"   AND B.ORDER_NO = H.ISSUE_NO" +
			"   AND H.ENTERPRISE_CODE = 'hfdc'\n" + 
//			"   and (select count(1)\n" + 
//			"          from INV_J_ISSUE_DETAILS d\n" + 
//			"         where d.issue_head_id = H.ISSUE_HEAD_ID\n" + 
//			"           and d.approved_count <> d.act_issued_count) = 0\n" + 
			"   AND B.Check_STATUS = 'C'\n" +
			"	AND B.Trans_Qty <> 0";
		if(sdate!=null&&!sdate.equals(""))
		{
			strWhere+="   and B.Check_Date >=\n" + 
			"       to_date('"+sdate+"' || ' 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n" ;
		}
		if(edate!=null&&!edate.equals(""))
		{
			strWhere+="   and B.Check_Date <=\n" + 
		              "   to_date('"+edate+"' || ' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n";
		}
		if(issueNo!=null&&!issueNo.equals(""))
		{
			strWhere+="   and H.Issue_No like '%"+issueNo.trim()+"%'\n";
		}
		if(receiptBy!=null&&!receiptBy.equals(""))
		{
			strWhere+="   and GETWORKERNAME(H.RECEIPT_BY) like '%"+receiptBy.trim()+"%'\n" ;
		}
		if(receiptDept!=null&&!receiptDept.equals(""))
		{
			strWhere+="   and GETDEPTNAME(H.RECEIPT_DEP) like '%"+receiptDept.trim()+"%'\n";
		}
		if(billType!=null&&!billType.equals(""))
		{
			strWhere+="  and  H.PLAN_ORIGINAL_ID="+billType+"  order by b.check_date\n";
		}
			
			sql+=strWhere;
			
			
			//sql+=" order by H.ISSUE_NO asc";
			
			String sqlCount="select count(*) from ("+sql+")tt";
			
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			
			// 计算总共的合计
			if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1&&rowStartIdxAndCount[0]!=0) 
			{
				sql+=
					"union\n" +
					" select  null,'总合计',null,null,null, nvl(sum(round(tt.totalPrice,2)),0),null,null,null,null,null,null,null,null,null,null,null\n" + 
					"from("+sql+")tt";

			}
			sql="select * from ("+sql+")ttt  order by ttt.ISSUE_NO asc ";
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Iterator it=list.iterator(); 
			Double price=0d;
			while(it.hasNext())
			{
				Object [] data=(Object [])it.next();
				if(!data[1].toString().equals("总合计"))
						{
					         if(data[5]!=null)
					         {
					        	 //modify by fyyang 100104
					        	 //NumberFormat formatter = new DecimalFormat(ZERO_POINT);
					        	// price=price+Double.parseDouble(formatter.format((Double.parseDouble(data[5].toString()))));
					        	 price=price+Double.parseDouble(data[5].toString());
					         }
						}
			}
			Object [] myData=new Object[]{null,"本页合计",null,null,null, price,null,null,null,null,null,null,null,null,null,null,null};
			if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1&&rowStartIdxAndCount[0]!=0) 
			{
				list.add(list.size()-1, myData);
			}
			else
			{
				list.add( myData);
			}
			
			PageObject obj=new PageObject();
			obj.setList(list);
			obj.setTotalCount(totalCount);
			return obj;
	}
	
	/**
	 * add by liuyi 091201
	 * 保留两位小数
	 * @param data
	 * @return
	 */
	private String dataOperate(Double data){
		NumberFormat formatter = new DecimalFormat(ZERO_POINT);
		return format(formatter.format(data.doubleValue()));
	}
	/**
	 * add by liuyi 091201
	 * 保留四位小数
	 * @param data
	 * @return
	 */
	private String dataParseFour(Double data){
		NumberFormat formatter = new DecimalFormat("0.0000");
		return format(formatter.format(data.doubleValue()));
	}
	private String format(String s) {
		int index = s.lastIndexOf(".");
		if (index < 0)
			index = s.length();
		for (int i = index; i > 3; i -= 3) {
			s = s.substring(0, i - 3) + "," + s.substring(i - 3, s.length());
		}
		return s;
	}
}