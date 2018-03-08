package power.ejb.resource.business;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.resource.InvCLocation;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvCWarehouse;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.form.PurchaseWarehouseDetailInfo;
import power.ejb.resource.form.PurchaseWarehouseInfo;
import power.ejb.resource.form.TransActionHisInfo;

@Stateless
public class PurchaseWarehouseImpl implements PurchaseWarehouse {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "PurJArrivalFacade")
	protected PurJArrivalFacadeRemote purJArrivalRemote;
	@EJB(beanName = "PurJArrivalDetailsFacade")
	protected PurJArrivalDetailsFacadeRemote purJArrivalDetailsRemote;
	@EJB(beanName = "PurJOrderDetailsFacade")
	protected PurJOrderDetailsFacadeRemote purJOrderDetailsRemote;
	@EJB(beanName = "InvJTransactionHisFacade")
	protected InvJTransactionHisFacadeRemote invJTransHisRemote;
	@EJB(beanName = "InvJWarehouseFacade")
	protected InvJWarehouseFacadeRemote invJWarehouseRemote;
	@EJB(beanName = "InvJLocationFacade")
	protected InvJLocationFacadeRemote invJLocationRemote;
	@EJB(beanName = "InvJLotFacade")
	protected InvJLotFacadeRemote invJLotRemote;
	@EJB(beanName = "InvCMaterialFacade")
	protected InvCMaterialFacadeRemote invCMaterialRemote;

	/**
	 * 画面初始化数据检索 modify by fyyang 090629 取供应商名称的表已修改
	 */
	@SuppressWarnings("unchecked")
	public PageObject findPurchaseOrderList(String fuzzy, int start, int limit,
			String operator, String enterpriseCode) {
		String sql = "SELECT DISTINCT(A.ID),A.ARRIVAL_NO,A.PUR_NO,A.CONTRACT_NO,"
				+ "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd')OPERATE_DATE,A.SUPPLIER,GETCLIENTNAME(A.SUPPLIER),A.MEMO,"
				+ "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')OPERATE_DATE2,A.INVOICE_NO "
				+ "FROM PUR_J_ARRIVAL A,PUR_J_ARRIVAL_DETAILS B,PUR_J_ORDER_DETAILS D,INV_C_MATERIAL E\n"
				+ "WHERE D.IS_USE='Y' AND A.IS_USE='Y' AND B.IS_USE='Y' "
				+ " AND A.ARRIVAL_STATE='2'  "
				+ "  and B.MATERIAL_ID=E.MATERIAL_ID   and  (E.qa_control_flag='Y' or B.item_status='1') "
				+ "AND A.ARRIVAL_NO=B.MIF_NO AND A.PUR_NO=D.PUR_NO AND B.PUR_LINE=D.PUR_ORDER_DETAILS_ID AND B.RCV_QTY > 0 AND B.RCV_QTY>B.REC_QTY"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='" + enterpriseCode + "'";

		// 5月5日15:50 增加一个表 ,INV_C_MATERIAL E liuyi
		String recordCountSql = "SELECT Count(DISTINCT(A.ID)) FROM PUR_J_ARRIVAL A,"
				+ "PUR_J_ARRIVAL_DETAILS B,PUR_J_ORDER_DETAILS D,INV_C_MATERIAL E\n"
				+ "WHERE D.IS_USE='Y' AND A.IS_USE='Y' AND B.IS_USE='Y' "
				+ " AND A.ARRIVAL_STATE='2'"
				// 5月5日15:50 增加查询条件 liuyi
				+ "  and B.MATERIAL_ID=E.MATERIAL_ID   and  (E.qa_control_flag='Y' or B.item_status='1') "
				+ "AND A.ARRIVAL_NO=B.MIF_NO AND A.PUR_NO=D.PUR_NO AND B.PUR_LINE=D.PUR_ORDER_DETAILS_ID AND B.RCV_QTY > 0 AND B.RCV_QTY>B.REC_QTY"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='" + enterpriseCode + "'";
		if ((fuzzy != null) && (!fuzzy.equals(""))) {
			// modify by fyyang 091231 改为按照到货单号、供应商、物料模糊查询
			sql = sql + "\n AND  (A.ARRIVAL_NO like '%" + fuzzy
					+ "%'  or  GETCLIENTNAME(A.SUPPLIER) like '%" + fuzzy
					+ "%'  or  E.MATERIAL_NAME like '%" + fuzzy + "%') \n";
			recordCountSql = recordCountSql + "\n AND  (A.ARRIVAL_NO ='"
					+ fuzzy + "'   or  GETCLIENTNAME(A.SUPPLIER) like '%"
					+ fuzzy + "%'   or  E.MATERIAL_NAME like '%" + fuzzy
					+ "%' ) \n";
		}
		sql += " order by A.ARRIVAL_NO desc ";// 增加将查询结果按降序排列 modify by ywliu
		// 2009/7/2

		List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		List<PurchaseWarehouseInfo> arraylist = new ArrayList<PurchaseWarehouseInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseInfo model = new PurchaseWarehouseInfo();
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setArrivalNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setPurNo(data[2].toString());
			}
			if (data[3] != null) {
				model.setContractNo(data[3].toString());
			}

			if (data[4] != null) {
				model.setOperateDate(data[4].toString());
			}
			if (data[5] != null) {
				model.setSupplier(data[5].toString());
			}
			if (data[6] != null) {
				model.setSupplyName(data[6].toString());
			}
			if (data[7] != null) {
				model.setMemo(data[7].toString());
			}
			if (data[8] != null) {
				model.setOperateDate2(data[8].toString());
			}
			// 发票号 add by drdu 090618
			if (data[9] != null) {
				model.setInvoiceNo(data[9].toString());
			}

			model.setLoginName(operator);
			arraylist.add(model);
		}

		Object countlist = bll.getSingal(recordCountSql);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;
	}

	/**
	 * 事务名称获取
	 */
	public String findTransName(String transCode, String enterpriseCode) {
		String sql = "SELECT A.TRANS_NAME FROM  INV_C_TRANSACTION A "
				+ "WHERE A.IS_USE='Y' AND A.TRANS_CODE ='" + transCode + "'"
				+ " AND A.ENTERPRISE_CODE='" + enterpriseCode + "'";
		Object obj = bll.getSingal(sql);
		String transName = obj.toString();
		return transName;
	}

	/**
	 * 获取事务流水号
	 * 
	 */
	public String findTransId(String transCode, String enterpriseCode) {
		String sql = "SELECT A.TRANS_ID FROM  INV_C_TRANSACTION A "
				+ "WHERE A.IS_USE='Y' AND A.TRANS_CODE ='" + transCode + "'"
				+ " AND A.ENTERPRISE_CODE='" + enterpriseCode + "'";

		Object obj = bll.getSingal(sql);
		if (obj != null) {
			String transName = obj.toString();
			return transName;
		} else {
			return "";
		}

	}

	/**
	 * 仓库获取
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllWarehouse(String flag, String enterpriseCode) {
		// 对应式样变更
		PageObject obj = new PageObject();
		String sql = "";
		sql = "SELECT * FROM INV_C_WAREHOUSE A WHERE A.IS_USE='Y'"
				+ " AND A.ENTERPRISE_CODE='" + enterpriseCode + "'";
		if ("1".equals(flag)) {
			sql = sql + " AND A.IS_INSPECT='N' AND A.IS_COST='Y'";
		}
		sql += " \n  order by A.whs_no asc ";
		List<InvCWarehouse> list = bll.queryByNativeSQL(sql,
				InvCWarehouse.class);
		obj.setList(list);

		return obj;
	}

	/**
	 * 库位获取
	 */
	@SuppressWarnings("unchecked")
	public PageObject findLocation(String whsNo, String enterpriseCode) {
		PageObject obj = new PageObject();
		String sql = "SELECT * FROM INV_C_LOCATION A WHERE A.IS_USE='Y' AND A.WHS_NO='"
				+ whsNo
				+ "'"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'";
		List<InvCLocation> list = bll.queryByNativeSQL(sql, InvCLocation.class);
		obj.setList(list);

		return obj;
	}

	/**
	 * 入库详细信息检索
	 */
	@SuppressWarnings("unchecked")
	public PageObject findPurchaseOrderDetailList(Long id, int start,
			int limit, String enterpriseCode) {
		String sql = "SELECT A.ID,A.ARRIVAL_NO,B.MEMO,B.LOT_CODE,B.RCV_QTY,B.LAST_MODIFIED_BY,\n"
				+ " C.MATERIAL_ID,C.MATERIAL_NO,C.MATERIAL_NAME,C.IS_LOT,C.MAERTIAL_CLASS_ID,\n"
				+ "C.COST_METHOD,C.STD_COST,C.DEFAULT_WHS_NO,C.DEFAULT_LOCATION_NO,C.SPEC_NO,\n"
				+ "C.STOCK_UM_ID,C.FROZEN_COST,D.PUR_ORDER_DETAILS_ID,D.PUR_QTY,D.RCV_QTY AS RCVQTY,D.UNIT_PRICE,\n"
				+ "D.DUE_DATE,D.TAX_RATE,D.CURRENCY_ID,D.INS_QTY,B.ID AS ARRIVALDETAILID,B.REC_QTY,\n"
				+ "to_char(B.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')ArrivalDetailModifiedDate,\n"
				+ "to_char(D.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')OrderDetailModifiedDate,\n"
				+ "to_char(C.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')MaterialModifiedDate\n"
				// add by liuyi 20100406 增加采购单号
				+ " ,d.pur_no \n"
				+ "FROM PUR_J_ARRIVAL A,PUR_J_ORDER_DETAILS D,PUR_J_ARRIVAL_DETAILS B,INV_C_MATERIAL C "
				+ "WHERE A.IS_USE='Y' AND B.IS_USE='Y' AND C.IS_USE='Y' AND D.IS_USE='Y' AND "
				+ "A.ARRIVAL_NO=B.MIF_NO AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID AND B.MATERIAL_ID=C.MATERIAL_ID AND "
				+ "A.ID="
				+ id
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND C.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ "   and (C.qa_control_flag='Y' or B.item_status='1')   AND B.RCV_QTY>B.REC_QTY  ";
		String countSql = "SELECT COUNT(A.ID)"
				+ "FROM PUR_J_ARRIVAL A,PUR_J_ORDER_DETAILS D,PUR_J_ARRIVAL_DETAILS B,INV_C_MATERIAL C "
				+ "WHERE A.IS_USE='Y' AND B.IS_USE='Y' AND C.IS_USE='Y' AND D.IS_USE='Y' AND "
				+ "A.ARRIVAL_NO=B.MIF_NO AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID AND B.MATERIAL_ID=C.MATERIAL_ID AND "
				+ "A.ID="
				+ id
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND C.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ "   and (C.qa_control_flag='Y' or B.item_status='1')    AND B.RCV_QTY>B.REC_QTY ";
		// List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		// 5/6/09 不分页，yiliu
		List<Object> list = bll.queryByNativeSQL(sql);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setArrivalNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setMemo(data[2].toString());
			}
			if (data[3] != null) {
				model.setLotCode(data[3].toString());
			}
			if (data[4] != null) {
				model.setRcvQty(Double.parseDouble(data[4].toString()));
			}
			if (data[5] != null) {
				model.setOperateBy(data[5].toString());
			}
			if (data[6] != null) {
				model.setMaterialId(Long.parseLong(data[6].toString()));
			}
			if (data[7] != null) {
				model.setMaterialNo(data[7].toString());
			}
			if (data[8] != null) {
				model.setMaterialName(data[8].toString());
			}
			if (data[9] != null) {
				model.setIsLot(data[9].toString());
			}
			if (data[10] != null) {
				model.setMaterialClassId(Long.parseLong(data[10].toString()));
			}
			if (data[11] != null) {
				model.setCostMethod(data[11].toString());
			}
			if (data[12] != null) {
				model.setStdCost(Double.parseDouble(data[12].toString()));
			}
			if (data[13] != null) {
				model.setDefaultWhsNo(data[13].toString());
				model.setWhsNo(data[13].toString());// modify by ywliu
				// 将入库选择的仓库和库位默认设置成物料主表中的缺省仓库，缺省库位
			}
			if (data[14] != null) {
				model.setDefaultLocationNo(data[14].toString());
				model.setLocationNo(data[14].toString());// modify by ywliu
				// 将入库选择的仓库和库位默认设置成物料主表中的缺省仓库，缺省库位
			}
			if (data[15] != null) {
				model.setSpecNo(data[15].toString());
			}
			if (data[16] != null) {
				model.setStockUmId(Long.parseLong(data[16].toString()));
			}
			if (data[17] != null) {
				model.setFrozenCost(Double.parseDouble(data[17].toString()));
			}
			if (data[18] != null) {
				model.setPurOrderDetailsId(Long.parseLong(data[18].toString()));
			}
			if (data[19] != null) {
				model.setPurQty(Double.parseDouble(data[19].toString()));
			}
			if (data[20] != null) {
				model.setPurOrderDetailsRcvQty(Double.parseDouble(data[20]
						.toString()));
			}
			if (data[21] != null) {
				model.setUnitPrice(Double.parseDouble(data[21].toString()));
			}
			if (data[22] != null) {
				model.setDueDate(data[22].toString());
			}
			if (data[23] != null) {
				model.setTaxRate(data[23].toString());
			}
			// TODO
			if (data[24] != null) {
				model.setCurrencyType(data[24].toString());
			}

			if (data[25] != null) {
				model.setInsqty(Double.parseDouble(data[25].toString()));
			}

			if (data[26] != null) {
				model.setArrivalDetailID(Long.parseLong(data[26].toString()));
			}
			if (data[27] != null) {
				model.setRecQty(Double.parseDouble(data[27].toString()));
			}
			if (data[28] != null) {
				model.setArrivalDetailModifiedDate(data[28].toString());
			}
			if (data[29] != null) {
				model.setOrderDetailModifiedDate(data[29].toString());
			}
			if (data[30] != null) {
				model.setMaterialModifiedDate(data[30].toString());
			}

			// add by liuyi 20100406
			if (data[31] != null) {
				model.setCGPurNo(data[31].toString());
				String ssSql = "select distinct b.memo, getdeptname(c.mr_dept)\n"
					// add by liuyi 20100430 需求计划来源 
					+ " ,c.plan_original_id \n"
						+ "  from pur_j_plan_order              a,\n"
						+ "       mrp_j_plan_requirement_detail b,\n"
						+ "       mrp_j_plan_requirement_head   c\n"
						+ " where a.is_use = 'Y'\n"
						+ "   and a.enterprise_code = '"+enterpriseCode+"'\n"
						+ "   and b.material_id='"
						+ model.getMaterialId()
						+ "'\n"
						+ "   and a.pur_no = '"
						+ model.getCGPurNo()
						+ "'\n"
						+ "   and a.requirement_detail_id = b.requirement_detail_id\n"
						+ "   and b.requirement_head_id = c.requirement_head_id";
				List ssList = bll.queryByNativeSQL(ssSql);
				if (ssList != null && ssList.size() > 0) {
					Object[] ssdata = (Object[]) ssList.get(0);
					if (ssdata[0] != null)
						model.setSbMemo(ssdata[0].toString());
					if (ssdata[1] != null)
						model.setSbDeptName(ssdata[1].toString());
					if(ssdata[2] != null)
						model.setPlanOriginalId(ssdata[2].toString());
				}

			}
			// TODO
			// 待入库数 本次入库数
			if (data[19] != null && data[20] != null) {
				model.setWaitQty(Double.parseDouble(data[19].toString())
						- Double.parseDouble(data[20].toString()));
			}
			if (data[4] != null && data[27] != null) {
				model.setCanQty(Double.parseDouble(data[4].toString())
						- Double.parseDouble(data[27].toString()));
			}
			// TODO
			// 保管员
			model.setThisQty(model.getCanQty());// 090720 modify by ywliu
			// 给本次入库一个默认值
			model.setSaveName("");
			// modify by ywliu 将入库选择的仓库和库位默认设置成物料主表中的缺省仓库，缺省库位
			if (model.getWhsNo() == null || "".equals(model.getWhsNo())) {
				model.setWhsNo("");
			}
			if (model.getLocationNo() == null
					|| "".equals(model.getLocationNo())) {
				model.setLocationNo("");
			}
			model.setGridMemo(model.getMemo());
			arraylist.add(model);
		}
		Object countlist = bll.getSingal(countSql);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;

	}

	// ------------------------------add 090506 by drdu
	// -------------------------------------------------
	@SuppressWarnings("unchecked")
	public PageObject findPurchasehouseList(String enterpriseCode,
			String sDate, String eDate, String materialNo, String materialName,
			String whsName, String specNo, String supplierName, String purBy,
			String whsBy, String isRedBill, String purNo, String arrivalNo,
			final int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql = "SELECT O.ID,\n"
				+ "       A.ARRIVAL_NO,\n"
				+ "       O.memo,\n"
				+ "       B.LOT_CODE,\n"
				+ "       B.RCV_QTY,\n"
				+ "       getworkername(B.LAST_MODIFIED_BY),\n"
				+ "       C.MATERIAL_ID,\n"
				+ "       C.MATERIAL_NO,\n"
				+ "       C.MATERIAL_NAME,\n"
				+ "       C.IS_LOT,\n"
				+ "       C.MAERTIAL_CLASS_ID,\n"
				+ "       C.COST_METHOD,\n"
				+ "       C.STD_COST,\n"
				+ "       C.DEFAULT_WHS_NO,\n"
				+ "       C.DEFAULT_LOCATION_NO,\n"
				+ "       C.SPEC_NO,\n"
				+ "       C.STOCK_UM_ID,\n"
				+ "       C.FROZEN_COST,\n"
				+ "       D.PUR_ORDER_DETAILS_ID,\n"
				+ "       D.PUR_QTY,\n"
				// + " D.RCV_QTY AS RCVQTY,\n"
				+ "         B.rcv_qty AS RCVQTY,\n" // modify by fyyang 090804
				+ "       D.UNIT_PRICE,\n"
				+ "       to_char(D.DUE_DATE, 'yyyy-mm-dd hh24:mi:ss') DUE_DATE,\n"
				+ "       D.TAX_RATE,\n"
				+ "       D.INS_QTY,\n"
				+ "       B.ID AS ARRIVALDETAILID,\n"
				+ "       B.REC_QTY,\n"
				+ "       B.LAST_MODIFIED_DATE,\n"
				+ "       to_char(O.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss') OrderDetailModifiedDate,\n"
				+ "       to_char(C.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss') MaterialModifiedDate,\n"
				+ "       D.CURRENCY_ID,\n"
				+ "       (select s.currency_name\n"
				+ "          from sys_c_currency s\n"
				+ "         where s.currency_id = d.currency_id\n"
				+ "           and s.is_use = 'Y') currency_name,\n"
				+ "       A.SUPPLIER,\n"
				+ "       getclientname(A.SUPPLIER) cliend_name,\n"
				+ "       getworkername(O.BUYER),\n"
				+ "       O.contract_no,\n"
				+ "       O.invoice_no,\n"
				+ "       (D.UNIT_PRICE * B.RCV_QTY) estimatedSum,\n" // modify
				// by
				// ywliu
				// 20100203
				// D.RCV_QTY
				+ "       O.PUR_NO,\n"
				+ "		  A.RELATION_ARRIVAL_NO"// add by ywliu 20100129
				// + " (select w.whs_name\n"
				// + " from INV_C_WAREHOUSE w\n"
				// + " where w.whs_no = c.default_whs_no\n"
				// + " and w.is_use = 'Y') whs_name\n"
				+ "  FROM PUR_J_ARRIVAL         A,\n"
				+ "       PUR_J_ORDER_DETAILS   D,\n"
				+ "       PUR_J_ARRIVAL_DETAILS B,\n"
				+ "       INV_C_MATERIAL        C,\n"
				+ "       PUR_J_ORDER           O\n"
				+ " WHERE A.IS_USE = 'Y'\n"
				+ "   AND B.IS_USE = 'Y'\n"
				+ "   AND C.IS_USE = 'Y'\n"
				+ "   AND D.IS_USE = 'Y'\n"
				+ "   AND O.IS_USE = 'Y'\n"
				+ "   AND A.ARRIVAL_NO = B.MIF_NO\n"
				+ "   AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID\n"
				+ "   AND B.MATERIAL_ID = C.MATERIAL_ID\n"
				+ "   AND B.PUR_NO = O.PUR_NO\n"
				+ "   AND A.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND B.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND C.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND D.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND O.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"

				+ "   AND (D.RCV_QTY > 0 or D.Rcv_Qty < 0)  and (B.rcv_qty>0 or B.Rcv_Qty <0) "; // modify
		// by
		// ywliu
		// 20100127
		String sqlcount = "SELECT COUNT(1)\n"
				+ "  FROM PUR_J_ARRIVAL         A,\n"
				+ "       PUR_J_ORDER_DETAILS   D,\n"
				+ "       PUR_J_ARRIVAL_DETAILS B,\n"
				+ "       INV_C_MATERIAL        C,\n"
				+ "       PUR_J_ORDER           O\n"
				+ " WHERE A.IS_USE = 'Y'\n"
				+ "   AND B.IS_USE = 'Y'\n"
				+ "   AND C.IS_USE = 'Y'\n"
				+ "   AND D.IS_USE = 'Y'\n"
				+ "   AND O.IS_USE = 'Y'\n"
				+ "   AND A.ARRIVAL_NO = B.MIF_NO\n"
				+ "   AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID\n"
				+ "   AND B.MATERIAL_ID = C.MATERIAL_ID\n"
				+ "   AND B.PUR_NO = O.PUR_NO\n"
				+ "   AND A.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND B.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND C.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND D.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"
				+ "   AND O.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\n"

				+ "   AND (D.RCV_QTY > 0 or D.Rcv_Qty < 0)  and (B.rcv_qty>0 or B.Rcv_Qty <0)  ";// modify
		// by
		// ywliu
		// 20100129

		String whereStr = "";
		if (sDate != null && sDate.length() > 0) {
			whereStr += " and B.last_modified_date>to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			whereStr += "and B.last_modified_date<to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (materialNo != null && materialNo.length() > 0) {
			whereStr += " and C.MATERIAL_NO like '%" + materialNo + "%'";
		}
		if (materialName != null && materialName.length() > 0) {
			whereStr += " and C.MATERIAL_NAME like '%" + materialName + "%'";
		}
		if (specNo != null && specNo.length() > 0) {
			whereStr += " and C.SPEC_NO like '%" + specNo + "%'";
		}
		if (supplierName != null && supplierName.length() > 0) {
			whereStr += " and getclientname(A.SUPPLIER) like '%" + supplierName
					+ "%'";
		}
		if (purBy != null && purBy.length() > 0) {
			whereStr += " and getworkername(O.BUYER) like '%" + purBy + "%'";
		}
		if (isRedBill != null && isRedBill.length() > 0) {
			if ("Y".equals(isRedBill)) {
				whereStr += " and A.RELATION_ARRIVAL_NO is not null and B.Rcv_Qty <0 \n"; // modify
				// by
				// ywliu
				// 20100129
			} else if ("N".equals(isRedBill)) {
				whereStr += " and B.Rcv_Qty > 0 \n"; // modify by ywliu
				// 20100129
				// A.RELATION_ARRIVAL_NO
				// is null
			}
		}
		if (purNo != null && purNo.length() > 0) {
			whereStr += " and O.PUR_NO like '%" + purNo + "%' \n";
		}
		if (arrivalNo != null && arrivalNo.length() > 0) {
			whereStr += " and A.ARRIVAL_NO like '%" + arrivalNo + "%' \n";
		}

		sql += whereStr;
		sqlcount += whereStr;
		sql += " order by A.ARRIVAL_NO";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			Double totalPurOrderDetailsRcvQty = 0.00;
			Double totalUnitPrice = 0.00;
			Double totalEstimatedSum = 0.00;
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setArrivalNo(data[1].toString());
				}
				if (data[2] != null) {
					model.setMemo(data[2].toString());
				}
				if (data[3] != null) {
					model.setLotCode(data[3].toString());
				}
				if (data[4] != null) {
					model.setRcvQty(Double.parseDouble(data[4].toString()));
				}
				if (data[5] != null) {
					model.setOperateBy(data[5].toString());
				}
				if (data[6] != null) {
					model.setMaterialId(Long.parseLong(data[6].toString()));
				}
				if (data[7] != null) {
					model.setMaterialNo(data[7].toString());
				}
				if (data[8] != null) {
					model.setMaterialName(data[8].toString());
				}
				if (data[9] != null) {
					model.setIsLot(data[9].toString());
				}
				if (data[10] != null) {
					model.setMaterialClassId(Long
							.parseLong(data[10].toString()));
				}
				if (data[11] != null) {
					model.setCostMethod(data[11].toString());
				}
				if (data[12] != null) {
					model.setStdCost(Double.parseDouble(data[12].toString()));
				}
				if (data[13] != null) {
					model.setDefaultWhsNo(data[13].toString());
				} else {
					model.setDefaultWhsNo("");
				}
				if (data[14] != null) {
					model.setDefaultLocationNo(data[14].toString());
				} else {
					model.setDefaultLocationNo("");
				}
				if (data[15] != null) {
					model.setSpecNo(data[15].toString());
				} else {
					model.setSpecNo("");
				}
				if (data[16] != null) {
					model.setStockUmId(Long.parseLong(data[16].toString()));
				}
				if (data[17] != null) {
					model
							.setFrozenCost(Double.parseDouble(data[17]
									.toString()));
				}
				if (data[18] != null) {
					model.setPurOrderDetailsId(Long.parseLong(data[18]
							.toString()));
				}
				if (data[19] != null) {
					model.setPurQty(Double.parseDouble(data[19].toString()));
				}
				if (data[20] != null) {
					model.setPurOrderDetailsRcvQty(Double.parseDouble(data[20]
							.toString()));
				}
				if (data[21] != null) {
					model.setUnitPrice(Double.parseDouble(data[21].toString()));
				}
				if (data[22] != null) {
					model.setDueDate(data[22].toString());
				}
				if (data[23] != null) {
					model.setTaxRate(data[23].toString());
				}
				if (data[24] != null) {
					model.setInsqty(Double.parseDouble(data[24].toString()));
				}
				if (data[25] != null) {
					model.setArrivalDetailID(Long
							.parseLong(data[25].toString()));
				}
				if (data[26] != null) {
					model.setRecQty(Double.parseDouble(data[26].toString()));
				}
				if (data[27] != null) {
					model.setArrivalDetailModifiedDate(data[27].toString());
				}
				if (data[28] != null) {
					model.setOrderDetailModifiedDate(data[28].toString());
				}
				if (data[29] != null) {
					model.setMaterialModifiedDate(data[29].toString());
				}
				if (data[30] != null) {
					model.setCurrencyId(Long.parseLong(data[30].toString()));
				}
				if (data[31] != null) {
					model.setCurrencyType(data[31].toString());
				}
				if (data[32] != null) {
					model.setSupplier(data[32].toString());
				}
				if (data[33] != null) {
					model.setSupplyName(data[33].toString());
				}
				if (data[34] != null) {
					model.setBuyerName(data[34].toString());
				}
				if (data[35] != null) {
					model.setContractNo(data[35].toString());
				} else {
					model.setContractNo("");
				}
				if (data[36] != null) {
					model.setInvoiceNo(data[36].toString());
				} else {
					model.setInvoiceNo("");
				}
				if (data[37] != null) {
					model.setEstimatedSum(Double.parseDouble(data[37]
							.toString()));
				}
				if (data[38] != null)
					model.setPurNo(data[38].toString());
				if (data[39] != null) {// add by ywliu 20100129
					model.setRelationArrivalNo(data[39].toString());
				}
				// if(data[38] != null)
				// model.setWhsName(data[38].toString());
				totalPurOrderDetailsRcvQty += model.getPurOrderDetailsRcvQty();
				totalUnitPrice += model.getUnitPrice();
				totalEstimatedSum += model.getEstimatedSum();
				arraylist.add(model);
			}
			PurchaseWarehouseDetailInfo info = new PurchaseWarehouseDetailInfo();
			info.setPurOrderDetailsRcvQty(totalPurOrderDetailsRcvQty);
			// modified by liuyi 091110 单价不用合计
			// info.setUnitPrice(totalUnitPrice);
			info.setEstimatedSum(totalEstimatedSum);
			arraylist.add(info);
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlcount).toString());
		// 计算总共的合计
		if ((totalCount - rowStartIdxAndCount[0]) / rowStartIdxAndCount[1] < 1
				&& rowStartIdxAndCount[0] != 0) {
			String sqlTotal = "select sum(D.Rcv_Qty),sum( D.UNIT_PRICE),sum(D.UNIT_PRICE * D.RCV_QTY)\n"
					+ "  FROM PUR_J_ARRIVAL         A,\n"
					+ "       PUR_J_ORDER_DETAILS   D,\n"
					+ "       PUR_J_ARRIVAL_DETAILS B,\n"
					+ "       INV_C_MATERIAL        C,\n"
					+ "       PUR_J_ORDER           O\n"
					+ " WHERE A.IS_USE = 'Y'\n"
					+ "   AND B.IS_USE = 'Y'\n"
					+ "   AND C.IS_USE = 'Y'\n"
					+ "   AND D.IS_USE = 'Y'\n"
					+ "   AND O.IS_USE = 'Y'\n"
					+ "   AND A.ARRIVAL_NO = B.MIF_NO\n"
					+ "   AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID\n"
					+ "   AND B.MATERIAL_ID = C.MATERIAL_ID\n"
					+ "   AND B.PUR_NO = O.PUR_NO\n"
					+ "   AND A.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "   AND B.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "   AND C.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "   AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "   AND O.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'\n"
					+ "   AND D.RCV_QTY > 0 \n";
			sqlTotal += whereStr;
			List totalList = bll.queryByNativeSQL(sqlTotal);
			Iterator totalIt = totalList.iterator();

			while (totalIt.hasNext()) {
				PurchaseWarehouseDetailInfo info = new PurchaseWarehouseDetailInfo();
				Object[] data = (Object[]) totalIt.next();
				if (data[0] != null)
					info.setPurOrderDetailsRcvQty(Double.parseDouble(data[0]
							.toString()));
				if (data[1] != null)
					// modified by liuyi 091110 单价不用合计
					// info.setUnitPrice(Double.parseDouble(data[1].toString()));
					;
				if (data[2] != null)
					info
							.setEstimatedSum(Double.parseDouble(data[2]
									.toString()));
				arraylist.add(info);
			}

		}
		obj.setList(arraylist);
		obj.setTotalCount(totalCount);
		return obj;
	}

	public String findWarehouseName(String whsNo, String enterpriseCode) {

		String sql = "SELECT A.WHS_NAME FROM INV_C_WAREHOUSE A WHERE A.IS_USE='Y' AND A.WHS_NO='"
				+ whsNo
				+ "'"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'";
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * 库位名称获取
	 * 
	 */
	public String findLocationName(String whsNo, String locationNo,
			String enterpriseCode) {
		String sql = "SELECT A.LOCATION_NAME FROM INV_C_LOCATION A WHERE A.IS_USE='Y' AND A.WHS_NO='"
				+ whsNo
				+ "'"
				+ "AND A.LOCATION_NO='"
				+ locationNo
				+ "'"
				+ " AND A.ENTERPRISE_CODE='" + enterpriseCode + "'";
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * 仓库保管人获取
	 */
	public String findWarehouseSaveName(String whsNo, String enterpriseCode) {
		String sql = "SELECT A.CONTACT_MAN FROM INV_C_WAREHOUSE A WHERE A.IS_USE='Y' AND A.WHS_NO='"
				+ whsNo
				+ "'"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'";
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * 库存物料的数量
	 */
	public String findWarehouseQty(Long materialId, String enterpriseCode) {
		String sql = "SELECT SUM(A.OPEN_BALANCE+A.RECEIPT+A.ADJUST-A.ISSUE) AS TEMP\n"
				+ "FROM INV_J_WAREHOUSE A\n"
				+ "WHERE A.MATERIAL_ID="
				+ materialId
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'";
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * 确认当前的仓库中是否有该种物料
	 */
	public String findInvJWarehouse(Long materialId, String whsNo,
			String enterpriseCode) {
		String sql = "SELECT A.WAREHOUSE_INV_ID  FROM INV_J_WAREHOUSE A \n"
				+ "WHERE A.IS_USE='Y' AND A.MATERIAL_ID=" + materialId
				+ " AND A.WHS_NO='" + whsNo + "'" + " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode + "'";
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * 确认当前的库位中是否有该种物料
	 */
	public String findInvJLocation(Long materialId, String whsNo,
			String locationNo, String enterpriseCode) {
		String sql = "SELECT A.LOCATION_INV_ID  FROM INV_J_LOCATION A \n"
				+ "WHERE A.IS_USE='Y' AND A.MATERIAL_ID=" + materialId
				+ " AND A.WHS_NO='" + whsNo + "'" + " AND A.LOCATION_NO='"
				+ locationNo + "'" + " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode + "'";
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * 批号记录表的ID
	 */
	public String findInvJLotId(Long materialId, String whsNo,
			String locationNo, String enterpriseCode) {
		// BG eBT_PowerERP_UTBUG_RS001-022
		String sql = "SELECT A.LOT_INV_ID FROM INV_J_LOT A \n"
				+ "WHERE A.IS_USE='Y' AND A.MATERIAL_ID=" + materialId
				+ " AND A.WHS_NO='" + whsNo + "'" + " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode + "'" + " AND A.LOT_NO='0'";
		if (!"".equals(locationNo)) {
			sql = sql + " AND A.LOCATION_NO='" + locationNo + "'";
		}
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;

	}

	/**
	 * 库位所有记录检索
	 */
	public String findLocationAllRecord(String enterpriseCode) {
		String sql = "SELECT COUNT(A.LOCATION_ID) FROM INV_C_LOCATION A"
				+ " WHERE A.IS_USE='Y' AND A.ENTERPRISE_CODE='"
				+ enterpriseCode + "'";
		Object obj = bll.getSingal(sql);
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	/**
	 * 到货入库管理所有数据库操作
	 */
	public void saveORUpdateAllDB(PurJArrival purjArrivalBean,
			List<PurJArrivalDetails> updatePurJArrivalDetailList,
			List<PurJOrderDetails> updatePurJOrderDetailList,
			List<InvJTransactionHis> saveInvJTransactionHisList,
			List<InvJWarehouse> saveInvJWarehouseList,
			List<InvJWarehouse> updateInvJWarehouseList,
			List<InvJLocation> saveInvJLocationList,
			List<InvJLocation> updateInvJLocationList,
			List<InvJLot> saveInvJLotList, List<InvJLot> updateInvJLotList,
			List<InvCMaterial> updateInvCMaterialList) {
		// CM 回滚操作
		try {

			if (purjArrivalBean != null) {
				purJArrivalRemote.update(purjArrivalBean);
			}
			if (updatePurJArrivalDetailList.size() > 0) {
				for (int i = 0; i < updatePurJArrivalDetailList.size(); i++) {
					purJArrivalDetailsRemote.update(updatePurJArrivalDetailList
							.get(i));
				}
			}
			if (updatePurJOrderDetailList.size() > 0) {
				for (int i = 0; i < updatePurJOrderDetailList.size(); i++) {
					purJOrderDetailsRemote.update(updatePurJOrderDetailList
							.get(i));
				}
			}
			if (saveInvJTransactionHisList.size() > 0) {
				Long id = invJTransHisRemote.getMaxId();
				for (int i = 0; i < saveInvJTransactionHisList.size(); i++) {
					InvJTransactionHis entity = saveInvJTransactionHisList
							.get(i);
					entity.setTransHisId(id++);
					invJTransHisRemote.save(entity);
				}
			}
			if (saveInvJWarehouseList.size() > 0) {
				Long id = bll.getMaxId("INV_J_WAREHOUSE", "WAREHOUSE_INV_ID");
				for (int i = 0; i < saveInvJWarehouseList.size(); i++) {
					InvJWarehouse entity = saveInvJWarehouseList.get(i);
					entity.setWarehouseInvId(id++);
					invJWarehouseRemote.save(entity);
				}
			}
			if (updateInvJWarehouseList.size() > 0) {
				for (int i = 0; i < updateInvJWarehouseList.size(); i++) {
					invJWarehouseRemote.update(updateInvJWarehouseList.get(i));
				}
			}

			if (saveInvJLocationList.size() > 0) {
				Long id = bll.getMaxId("INV_J_LOCATION", "LOCATION_INV_ID");
				for (int i = 0; i < saveInvJLocationList.size(); i++) {
					InvJLocation entity = saveInvJLocationList.get(i);
					entity.setLocationInvId(id++);
					invJLocationRemote.save(entity);
				}
			}
			if (updateInvJLocationList.size() > 0) {
				for (int i = 0; i < updateInvJLocationList.size(); i++) {
					invJLocationRemote.update(updateInvJLocationList.get(i));
				}
			}

			if (saveInvJLotList.size() > 0) {
				Long id = bll.getMaxId("INV_J_LOT", "LOT_INV_ID");
				for (int i = 0; i < saveInvJLotList.size(); i++) {
					InvJLot entity = saveInvJLotList.get(i);
					entity.setLotInvId(id++);
					invJLotRemote.save(entity);
				}
			}
			if (updateInvJLotList.size() > 0) {
				for (int i = 0; i < updateInvJLotList.size(); i++) {
					invJLotRemote.update(updateInvJLotList.get(i));
				}
			}

			if (updateInvCMaterialList.size() > 0) {
				for (int i = 0; i < updateInvCMaterialList.size(); i++) {
					invCMaterialRemote.update(updateInvCMaterialList.get(i));
				}
			}

		} catch (RuntimeException re) {

			throw re;
		}
	}

	/**
	 * 获取汇率
	 */
	@SuppressWarnings("unchecked")
	public String findExchangeRate(Long currentId, String enterpriseCode,
			Long dstCurrencyId) {
		// TODO
		// CM 取得汇率
		String sql = "SELECT A.RATE FROM SYS_C_EXCHANGE_RATE A"
				+ " WHERE A.ORI_CURRENCY_ID ="
				+ currentId
				+ " AND A.DST_CURRENCY_ID="
				+ dstCurrencyId
				+ " AND A.IS_USE='Y'"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "' AND "
				+ "TO_CHAR(A.EFFECTIVE_DATE,'yyyy-mm-dd') <= TO_CHAR(SYSDATE,'yyyy-mm-dd') AND TO_CHAR(A.DISCONTINUE_DATE,'yyyy-mm-dd') >= TO_CHAR(SYSDATE,'yyyy-mm-dd')";
		List<Object> list = bll.queryByNativeSQL(sql);
		if (list.size() > 0) {
			return list.get(0).toString();
		} else {
			return "0";
		}

	}

	/**
	 * 获取人员信息
	 */
	@SuppressWarnings("unchecked")
	public String findPersonInfo(String empCode) {
		// CM 取得人员信息
		String sql = "SELECT A.CHS_NAME FROM HR_J_EMP_INFO A WHERE A.EMP_CODE ='"
				+ empCode + "'";
		List<Object> list = bll.queryByNativeSQL(sql);
		if (list.size() > 0) {
			return list.get(0).toString();
		} else {
			return "";
		}
	}

	/**
	 * 批号记录表的最大批号
	 * 
	 */
	public String findMaxLotNo(Long materialId) {
		String sql = "SELECT MAX(B.LOT_NO) FROM INV_J_LOT B"
				+ " WHERE B.MATERIAL_ID="
				+ materialId
				+ " AND LENGTH(B.LOT_NO) IN (SELECT  MAX(LENGTH(LOT_NO)) FROM INV_J_LOT A WHERE A.MATERIAL_ID="
				+ materialId + ")";
		Object obj = bll.getSingal(sql);
		String value = "0";
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	// ----------------------add by fyyang
	// 090422-----------------------------------------------------------
	/**
	 * 画面初始化数据检索
	 */
	@SuppressWarnings("unchecked")
	public PageObject findArrivalListForArrivalCheck(String fuzzy, int start,
			int limit, String operator, String enterpriseCode) {
		String sql = "SELECT DISTINCT(A.ID),A.ARRIVAL_NO,A.PUR_NO,A.CONTRACT_NO,"
				+ "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd')OPERATE_DATE,A.SUPPLIER,GETCLIENTNAME(A.SUPPLIER),A.MEMO,"
				+ "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')OPERATE_DATE2 "
				+ "FROM PUR_J_ARRIVAL A,PUR_J_ARRIVAL_DETAILS B,PUR_J_ORDER_DETAILS D,INV_C_MATERIAL E\n"
				+ "WHERE D.IS_USE='Y' AND A.IS_USE='Y' AND B.IS_USE='Y' "
				+ " AND A.ARRIVAL_STATE='2'  "
				+ "  and B.MATERIAL_ID=E.MATERIAL_ID   and  E.qa_control_flag='N' "
				+ "AND A.ARRIVAL_NO=B.MIF_NO AND A.PUR_NO=D.PUR_NO AND B.PUR_LINE=D.PUR_ORDER_DETAILS_ID AND B.RCV_QTY > 0 AND B.REC_QTY=0"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " order by A.ARRIVAL_NO DESC ";

		String recordCountSql = "SELECT Count(DISTINCT(A.ID)) FROM PUR_J_ARRIVAL A,"
				+ "PUR_J_ARRIVAL_DETAILS B,PUR_J_ORDER_DETAILS D,INV_C_MATERIAL E\n"
				+ "WHERE D.IS_USE='Y' AND A.IS_USE='Y' AND B.IS_USE='Y' "
				+ " AND A.ARRIVAL_STATE='2' "
				+ "  and B.MATERIAL_ID=E.MATERIAL_ID   and  E.qa_control_flag='N' "
				+ "AND A.ARRIVAL_NO=B.MIF_NO AND A.PUR_NO=D.PUR_NO AND B.PUR_LINE=D.PUR_ORDER_DETAILS_ID AND B.RCV_QTY > 0 AND B.REC_QTY=0"
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " order by A.ARRIVAL_NO DESC ";
		if ((fuzzy != null) && (!fuzzy.equals(""))) {
			sql = sql + "\n AND A.ARRIVAL_NO ='" + fuzzy + "'";
			recordCountSql = recordCountSql + "\n AND A.ARRIVAL_NO ='" + fuzzy
					+ "'";
		}
		List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		List<PurchaseWarehouseInfo> arraylist = new ArrayList<PurchaseWarehouseInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseInfo model = new PurchaseWarehouseInfo();
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setArrivalNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setPurNo(data[2].toString());
			}
			if (data[3] != null) {
				model.setContractNo(data[3].toString());
			}

			if (data[4] != null) {
				model.setOperateDate(data[4].toString());
			}
			if (data[5] != null) {
				model.setSupplier(data[5].toString());
			}
			if (data[6] != null) {
				model.setSupplyName(data[6].toString());
			}
			if (data[7] != null) {
				model.setMemo(data[7].toString());
			}
			if (data[8] != null) {
				model.setOperateDate2(data[8].toString());
			}
			model.setLoginName(operator);
			arraylist.add(model);
		}

		Object countlist = bll.getSingal(recordCountSql);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;
	}

	/**
	 * 到货验收详细信息检索 0已到货,1已验收合格,2已入库 3、验收不合格
	 */
	@SuppressWarnings("unchecked")
	public PageObject findMaterialDetailListForArrivalCheck(Long id, int start,
			int limit, String enterpriseCode) {
		String sql = "SELECT B.ID,A.ARRIVAL_NO,B.MEMO,B.LOT_CODE,B.RCV_QTY,B.LAST_MODIFIED_BY,\n"
				+ " C.MATERIAL_ID,C.MATERIAL_NO,C.MATERIAL_NAME,C.IS_LOT,C.MAERTIAL_CLASS_ID,\n"
				+ "C.COST_METHOD,C.STD_COST,C.DEFAULT_WHS_NO,C.DEFAULT_LOCATION_NO,C.SPEC_NO,\n"
				+ "C.STOCK_UM_ID,C.FROZEN_COST,D.PUR_ORDER_DETAILS_ID,D.PUR_QTY,D.RCV_QTY AS RCVQTY,D.UNIT_PRICE,\n"
				+ "D.DUE_DATE,D.TAX_RATE,D.CURRENCY_ID,D.INS_QTY,B.ID AS ARRIVALDETAILID,B.REC_QTY,\n"
				+ "to_char(B.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')ArrivalDetailModifiedDate,\n"
				+ "to_char(D.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')OrderDetailModifiedDate,\n"
				+ "to_char(C.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')MaterialModifiedDate,\n"
				+ " B.item_status \n"
				+ "FROM PUR_J_ARRIVAL A,PUR_J_ORDER_DETAILS D,PUR_J_ARRIVAL_DETAILS B,INV_C_MATERIAL C "
				+ "WHERE A.IS_USE='Y' AND B.IS_USE='Y' AND C.IS_USE='Y' AND D.IS_USE='Y' AND "
				+ "A.ARRIVAL_NO=B.MIF_NO AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID AND B.MATERIAL_ID=C.MATERIAL_ID AND "
				+ "A.ID="
				+ id
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND C.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ "   and  B.item_status<>'2'  and B.REC_QTY=0   and C.qa_control_flag='N'";
		String countSql = "SELECT COUNT(A.ID)"
				+ "FROM PUR_J_ARRIVAL A,PUR_J_ORDER_DETAILS D,PUR_J_ARRIVAL_DETAILS B,INV_C_MATERIAL C "
				+ "WHERE A.IS_USE='Y' AND B.IS_USE='Y' AND C.IS_USE='Y' AND D.IS_USE='Y' AND "
				+ "A.ARRIVAL_NO=B.MIF_NO AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID AND B.MATERIAL_ID=C.MATERIAL_ID AND "
				+ "A.ID="
				+ id
				+ " AND A.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND B.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND C.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ " AND D.ENTERPRISE_CODE='"
				+ enterpriseCode
				+ "'"
				+ "   and  B.item_status<>'2'  and B.REC_QTY=0  and C.qa_control_flag='N'";
		// List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		// 不分页，直接滚动 5/6/09 16：46 yiliu
		List<Object> list = bll.queryByNativeSQL(sql);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setArrivalNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setMemo(data[2].toString());
			}
			if (data[3] != null) {
				model.setLotCode(data[3].toString());
			}
			if (data[4] != null) {
				model.setRcvQty(Double.parseDouble(data[4].toString()));
			}
			if (data[5] != null) {
				model.setOperateBy(data[5].toString());
			}
			if (data[6] != null) {
				model.setMaterialId(Long.parseLong(data[6].toString()));
			}
			if (data[7] != null) {
				model.setMaterialNo(data[7].toString());
			}
			if (data[8] != null) {
				model.setMaterialName(data[8].toString());
			}
			if (data[9] != null) {
				model.setIsLot(data[9].toString());
			}
			if (data[10] != null) {
				model.setMaterialClassId(Long.parseLong(data[10].toString()));
			}
			if (data[11] != null) {
				model.setCostMethod(data[11].toString());
			}
			if (data[12] != null) {
				model.setStdCost(Double.parseDouble(data[12].toString()));
			}
			if (data[13] != null) {
				model.setDefaultWhsNo(data[13].toString());
			}
			if (data[14] != null) {
				model.setDefaultLocationNo(data[14].toString());
			}
			if (data[15] != null) {
				model.setSpecNo(data[15].toString());
			}
			if (data[16] != null) {
				model.setStockUmId(Long.parseLong(data[16].toString()));
			}
			if (data[17] != null) {
				model.setFrozenCost(Double.parseDouble(data[17].toString()));
			}
			if (data[18] != null) {
				model.setPurOrderDetailsId(Long.parseLong(data[18].toString()));
			}
			if (data[19] != null) {
				model.setPurQty(Double.parseDouble(data[19].toString()));
			}
			if (data[20] != null) {
				model.setPurOrderDetailsRcvQty(Double.parseDouble(data[20]
						.toString()));
			}
			if (data[21] != null) {
				model.setUnitPrice(Double.parseDouble(data[21].toString()));
			}
			if (data[22] != null) {
				model.setDueDate(data[22].toString());
			}
			if (data[23] != null) {
				model.setTaxRate(data[23].toString());
			}
			// TODO
			if (data[24] != null) {
				model.setCurrencyType(data[24].toString());
			}

			if (data[25] != null) {
				model.setInsqty(Double.parseDouble(data[25].toString()));
			}

			if (data[26] != null) {
				model.setArrivalDetailID(Long.parseLong(data[26].toString()));
			}
			if (data[27] != null) {
				model.setRecQty(Double.parseDouble(data[27].toString()));
			}
			if (data[28] != null) {
				model.setArrivalDetailModifiedDate(data[28].toString());
			}
			if (data[29] != null) {
				model.setOrderDetailModifiedDate(data[29].toString());
			}
			if (data[30] != null) {
				model.setMaterialModifiedDate(data[30].toString());
			}
			if (data[31] != null) {
				// 物料状态
				model.setItemStatus(data[31].toString());
			}
			// TODO
			// 待入库数 本次入库数
			if (data[19] != null && data[20] != null) {
				model.setWaitQty(Double.parseDouble(data[19].toString())
						- Double.parseDouble(data[20].toString()));
			}
			if (data[4] != null && data[27] != null) {
				model.setCanQty(Double.parseDouble(data[4].toString())
						- Double.parseDouble(data[27].toString()));
			}
			// TODO
			// 保管员
			model.setThisQty(0d);
			model.setSaveName("");
			model.setWhsNo("");
			model.setLocationNo("");
			model.setGridMemo(model.getMemo());
			if (model.getCanQty() > 0) {
				// add by fyyang 090519 到货数>0才显示
				arraylist.add(model);
			}
		}
		Object countlist = bll.getSingal(countSql);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;

	}

	public PageObject findArrivalCheckList(String enterpriseCode, String date,
			String date2, String checkPerson, String supplierName,
			String IsPurchasehouse, String purBy, String arrialNo,
			int... rowStartIdxAndCount) {
		String sql = "select distinct a.arrival_no , getclientname(a.supplier) client_name, c.pur_no ,c.contract_no,nvl(GETWORKERNAME(c.buyer),c.buyer) buyer , a.last_modified_date ,a.arrival_date ,a.Invoice_No \n "
				+ " from  pur_j_arrival a ,  pur_j_arrival_details b ,pur_j_order c ,con_j_clients_info    d\n"
				+ "where a.pur_no = b.pur_no and a.pur_no = c.pur_no and d.cliend_id = a.supplier \n"
				+ "  and a.is_use='Y'\n"
				+ "      and b.is_use='Y'\n"
				+ "      and c.is_use='Y'\n"
				+ "      and a.enterprise_code='"
				+ enterpriseCode
				+ "'\n"
				+ "      and b.enterprise_code='"
				+ enterpriseCode
				+ "'\n"
				+ "      and c.enterprise_code='"
				+ enterpriseCode
				+ "'\n"
				+ "      and d.enterprise_code='"
				+ enterpriseCode + "' \n";

		if (!"".equals(purBy)) {
			sql += "and c.buyer= '" + purBy + "'";
		}
		if (!"".equals(checkPerson)) {
			sql += "and b.last_modified_by = '" + checkPerson + "'";
		}
		sql += "and  a.last_modified_date > to_date('"
				+ date
				+ "','yyyy-MM-dd hh24:mi:ss') and a.last_modified_date <  to_date('"
				+ date2 + "','yyyy-MM-dd hh24:mi:ss')";
		if ("0".equals(IsPurchasehouse)) {
			sql += "and b.item_status in(1,3)";
		} else if ("1".equals(IsPurchasehouse)) {
			sql += "and b.item_status in(2)";
		}
		if (!"".equals(supplierName)) {
			sql += "and d.CLIEND_ID = '" + supplierName + "'";
		}
		if (arrialNo != null && !arrialNo.equals("")) {
			sql += " \n and  a.arrival_no like '%" + arrialNo + "%'  \n";
		}
		String sqlCount = "select count(*) from (" + sql + ")";
		List<Object> list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<PurchaseWarehouseInfo> arraylist = new ArrayList<PurchaseWarehouseInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			PurchaseWarehouseInfo model = new PurchaseWarehouseInfo();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				model.setArrivalNo(data[0].toString());
			}
			if (data[1] != null) {
				model.setSupplyName(data[1].toString());
			}
			if (data[2] != null) {
				model.setPurNo(data[2].toString());
			}
			if (data[3] != null) {
				model.setContractNo(data[3].toString());
			}
			if (data[4] != null) {
				model.setBuyerName(data[4].toString());
			}
			if (data[5] != null) {
				model.setEntryDate(data[5].toString());
			}
			if (data[6] != null) {
				model.setArrivalDate(data[6].toString());
			}
			if (data[7] != null) {
				model.setInvoiceNo(data[7].toString());
			}
			arraylist.add(model);
		}
		PageObject object = new PageObject();
		object.setList(arraylist);
		object.setTotalCount(Long.valueOf(bll.getSingal(sqlCount).toString()));
		return object;
	}

	public PageObject findArrivalCheckMaterialDetailList(String enterpriseCode,
			String arrivalNo, int... rowStartIdxAndCount) {
		String sql = "select c.material_no ,c.material_name,c.spec_no  ,b.rec_qty ,d.class_name ,GETWHSNAME(c.default_whs_no) as whs_name ,b.last_modified_date,\n"
				+ "GETWORKERNAME( b.last_modified_by) as last_modified_by ,f.unit_name\n"
				+ "from pur_j_arrival a , pur_j_arrival_details b, inv_c_material c,inv_c_material_class d  ,BP_C_MEASURE_UNIT f\n"
				+ "where a.arrival_no = b.mif_no and b.material_id = c.material_id and c.maertial_class_id = d.maertial_class_id and c.stock_um_id = f.unit_id\n"
				+ "      and a.arrival_no = '"
				+ arrivalNo
				+ "'  and b.the_qty<>0 ";
		String sqlCount = "select count(*) from (" + sql + ")";
		List<Object> list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setMaterialNo(data[0].toString());
			}
			if (data[1] != null) {
				model.setMaterialName(data[1].toString());
			}
			if (data[2] != null) {
				model.setSpecNo(data[2].toString());
			}
			if (data[3] != null) {
				model.setRecQty(Double.valueOf(data[3].toString()));
			}
			if (data[4] != null) {
				model.setClassName(data[4].toString());
			}
			if (data[5] != null) {
				model.setWhsName(data[5].toString());
			}
			if (data[6] != null) {
				model.setArrivalDetailModifiedDate(data[6].toString());
			}
			if (data[7] != null) {
				model.setOperateByName(data[7].toString());
			}
			if (data[8] != null) {
				model.setStockUmIdName(data[8].toString());
			}
			arraylist.add(model);
		}
		PageObject object = new PageObject();
		object.setList(arraylist);
		object.setTotalCount(Long.valueOf(bll.getSingal(sqlCount).toString()));
		return object;
	}

	@SuppressWarnings("unchecked")
	public PageObject findArrivalCheckListByMaterial(String enterpriseCode,
			String date, String date2, String checkPerson, String materialNo,
			String whsNo, String IsPurchasehouse, int... rowStartIdxAndCount) {
		String sql = "select distinct a.arrival_no ,getclientname( a.supplier) client_name, \n"
				+ "g.pur_no ,g.contract_no,nvl(GETWORKERNAME(g.buyer),g.buyer) buyer , a.last_modified_date ,a.arrival_date ,a.Invoice_No ,\n"
				+ "c.material_no ,c.material_name,c.spec_no  ,b.rec_qty ,d.class_name ,GETWHSNAME(c.default_whs_no) as whs_name ,b.last_modified_date as lastModifiedDate,\n"
				+ "GETWORKERNAME( b.last_modified_by) as last_modified_by ,f.unit_name\n"
				+ "from  pur_j_arrival a ,pur_j_arrival_details b, inv_c_material c ,inv_c_material_class d  ,BP_C_MEASURE_UNIT f ,pur_j_order g\n"
				+ "where a.arrival_no = b.mif_no and b.material_id = c.material_id and c.maertial_class_id = d.maertial_class_id and  c.stock_um_id = f.unit_id and a.pur_no = g.pur_no \n";
		String whereStr = "";
		if (!"".equals(materialNo)) {
			whereStr += "and c.material_no = '" + materialNo + "'";
		}
		// 是否入库（条件已去掉）
		if ("1".equals(IsPurchasehouse)) {
			whereStr += "and b.item_status in(1,3)";
		} else if ("2".equals(IsPurchasehouse)) {
			whereStr += "and b.item_status in(2)";
		}
		if (!"".equals(whsNo)) {
			whereStr += "and c.default_whs_no = '" + whsNo + "'";
		}
		if (!"".equals(checkPerson)) {
			whereStr += "and b.last_modified_by = '" + checkPerson + "'";
		}
		whereStr += "and  a.last_modified_date > to_date('"
				+ date
				+ "','yyyy-MM-dd hh24:mi:ss') and a.last_modified_date <  to_date('"
				+ date2 + "','yyyy-MM-dd hh24:mi:ss')";
		sql += whereStr;
		String sqlCount = "select count(*) from (" + sql + ")";
		List<Object> list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();
		Iterator it = list.iterator();
		Double recQtyTotal = 0.00;
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setArrivalNo(data[0].toString());
			}
			if (data[1] != null) {
				model.setSupplyName(data[1].toString());
			}
			if (data[2] != null) {
				model.setPurNo(data[2].toString());
			}
			if (data[3] != null) {
				model.setContractNo(data[3].toString());
			}
			if (data[4] != null) {
				model.setBuyerName(data[4].toString());
			}
			if (data[5] != null) {
				model.setEntryDate(data[5].toString());
			}
			if (data[6] != null) {
				model.setArrivalDate(data[6].toString());
			}
			if (data[7] != null) {
				model.setInvoiceNo(data[7].toString());
			}
			if (data[8] != null) {
				model.setMaterialNo(data[8].toString());
			}
			if (data[9] != null) {
				model.setMaterialName(data[9].toString());
			}
			if (data[10] != null) {
				model.setSpecNo(data[10].toString());
			}
			if (data[11] != null) {
				model.setRecQty(Double.valueOf(data[11].toString()));
			}
			if (data[12] != null) {
				model.setClassName(data[2].toString());
			}
			if (data[13] != null) {
				model.setWhsName(data[13].toString());
			}
			if (data[14] != null) {
				model.setArrivalDetailModifiedDate(data[14].toString());
			}
			if (data[15] != null) {
				model.setOperateByName(data[15].toString());
			}
			if (data[16] != null) {
				model.setStockUmIdName(data[16].toString());
			}
			recQtyTotal += model.getRecQty();
			arraylist.add(model);
		}
		// 计算当前页的合计数量 modify by ywliu 090721
		PurchaseWarehouseDetailInfo info = new PurchaseWarehouseDetailInfo();
		info.setRecQty(recQtyTotal);
		arraylist.add(info);
		// 计算总共的合计数量 modify by ywliu 090721
		if ((rowStartIdxAndCount != null
				&& rowStartIdxAndCount.length > 1
				&& (totalCount - rowStartIdxAndCount[0])
						/ rowStartIdxAndCount[1] < 1 && rowStartIdxAndCount[0] != 0)) {
			String sumSql = "select sum(b.rec_qty) \n"
					+ "from  pur_j_arrival a ,pur_j_arrival_details b, inv_c_material c ,inv_c_material_class d  ,BP_C_MEASURE_UNIT f ,pur_j_order g\n"
					+ "where a.arrival_no = b.mif_no and b.material_id = c.material_id and c.maertial_class_id = d.maertial_class_id and  c.stock_um_id = f.unit_id and a.pur_no = g.pur_no \n";
			sumSql += whereStr;
			Double total = Double.parseDouble(bll.getSingal(sumSql).toString());
			PurchaseWarehouseDetailInfo bean = new PurchaseWarehouseDetailInfo();
			bean.setRecQty(total);
			arraylist.add(bean);
		}
		PageObject object = new PageObject();
		object.setList(arraylist);
		object.setTotalCount(totalCount);
		return object;
	}
	
	// modified by liuyi 20100430 
//	public List<PurchaseWarehouseDetailInfo> getReceiptBillMaterialInfo(
//			String purNo, String whsNo, String arrivalNo) {
	public List<PurchaseWarehouseDetailInfo> getReceiptBillMaterialInfo(
			String purNo, String whsNo, String arrivalNo,String metailIdNotIn) {
		// 091218
		// Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.MINUTE, -1);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select  a.trans_qty,a.price/(1+a.tax_rate),GETWHSNAME(a.from_whs_no) ,a.tax_rate,b.material_no,b.material_name ,b.spec_no,GETUNITNAME(a.trans_um_id) ,GETWORKERNAME(c.buyer), d.invoice_no,getclientname(d.supplier)\n"
				+ ",a.trans_qty*a.price/(1+a.tax_rate),a.trans_qty*a.price,a.price \n"
				// add by ltong 20100413 增加采购单号 申报部门
				+ ",b.material_id ,d.pur_no \n"
				+ "from INV_J_TRANSACTION_HIS a,inv_c_material b ,PUR_J_ORDER c ,PUR_J_ARRIVAL d \n"
				+ "where a.material_id = b.material_id and a.order_no = c.pur_no and  a.order_no = '"
				+ purNo
				// + "' and to_char(a.last_modified_date, 'yyyy-MM-dd
				// hh24:mi:ss') >= '" + sdf.format(calendar.getTime())
				// + "'\n and to_char(a.last_modified_date, 'yyyy-MM-dd
				// hh24:mi:ss') <= '" + sdf.format(new Date())
				+ "'\n  and a.from_whs_no = '"
				+ whsNo
				+ "'"
				+ " and d.arrival_no='"
				+ arrivalNo
				+ "'"
				+ " and a.trans_qty >0"
				+ " and a.last_modified_date >= "
				+ "  (select max(t.last_modified_date) - 2/24/3600 from pur_j_arrival_details  t where t.mif_no='"
				+ arrivalNo
				+ "' and t.pur_no = '"
				+ purNo
				+ "')"
				+ "  and a.last_modified_date <= "
				+ " (select max(t.last_modified_date) + 2/24/3600 from pur_j_arrival_details  t where t.mif_no='"
				+ arrivalNo + "' and t.pur_no = '" + purNo + "')";
		
		// add by liuyi 20100430 剔除计划来源为固定资产类的物资
		if(metailIdNotIn != null && !metailIdNotIn.equals("")){
			sql += " and b.material_id not in ("+metailIdNotIn+") \n";
		}
		List<Object> list = bll.queryByNativeSQL(sql);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();
		Iterator it = list.iterator();
		Double totalPrice = 0d;
		Double totalTax = 0d;
		Double totalInputTax = 0d;
		NumberFormat formatter = new DecimalFormat("0.00");
		NumberFormat formatter1 = new DecimalFormat("0.0000");

		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setRecQty(Double.valueOf(data[0].toString()));
			}
			if (data[1] != null) {
				// model.setUnitPrice(Double.valueOf(data[1].toString()));

				Double price = Double.valueOf(data[1].toString());
				// model.setUnitPrice(Double.valueOf(data[1].toString()));
				model.setStrUnitPrice(formatter1.format(price.doubleValue()));
			}
			if (data[2] != null) {
				model.setWhsName(data[2].toString());
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
				model.setStockUmIdName(data[7].toString());
			}
			if (data[8] != null) {
				model.setBuyerName(data[8].toString());
			}
			if (data[9] != null) {
				model.setInvoiceNo(data[9].toString());
			}
			if (data[10] != null)
				model.setSupplyName(data[10].toString());
			if (data[11] != null) {
				model.setStrTotalPrice(formatter.format(Double.valueOf(
						data[11].toString()).doubleValue()));
				totalPrice += Double.valueOf(data[11].toString());
				if (model.getTaxRate() != null) {
					Double inputTax = Double.parseDouble(data[11].toString())
							* Double.parseDouble(data[3].toString());
					model.setStrInputTax(formatter.format(inputTax
							.doubleValue()));
					totalInputTax += inputTax;
				}
			}
			if (data[12] != null) {
				model.setStrTotalTax(formatter.format(Double.valueOf(
						data[12].toString()).doubleValue()));
				totalTax += Double.valueOf(data[12].toString());
			}
			// add by ltong 20100413 增加申报部门
			if (data[14] != null) {
				model.setMaterialId(Long.parseLong(data[14].toString()));
			}
			if (data[15] != null) {
				model.setCGPurNo(data[15].toString());
				String ssSql = "select distinct b.memo, getdeptname(c.mr_dept)\n"
						+ "  from pur_j_plan_order              a,\n"
						+ "       mrp_j_plan_requirement_detail b,\n"
						+ "       mrp_j_plan_requirement_head   c\n"
						+ " where a.is_use = 'Y'\n"
						+ "   and a.enterprise_code = 'hfdc'\n"
						+ "   and b.material_id='"
						+ model.getMaterialId()
						+ "'\n"
						+ "   and a.pur_no = '"
						+ model.getCGPurNo()
						+ "'\n"
						+ "   and a.requirement_detail_id = b.requirement_detail_id\n"
						+ "   and b.requirement_head_id = c.requirement_head_id";
				List ssList = bll.queryByNativeSQL(ssSql);
				if (ssList != null && ssList.size() > 0) {
					Object[] ssdata = (Object[]) ssList.get(0);
					if (ssdata[0] != null)
						model.setSbMemo(ssdata[0].toString());
					if (ssdata[1] != null)
						model.setSbDeptName(ssdata[1].toString());
				}

			}
			arraylist.add(model);
		}

		PurchaseWarehouseDetailInfo entity = new PurchaseWarehouseDetailInfo();
		entity.setMaterialNo("合计");
		entity.setStrInputTax(formatter.format(totalInputTax));
		entity.setStrTotalPrice(formatter.format(totalPrice));
		entity.setStrTotalTax(formatter.format(totalTax));
		arraylist.add(entity);

		return arraylist;

	}

	/**
	 * 查询入库审核列表 modify by fyyang 090803 增加合同号及发票号查询条件 modify by fyyang 091209
	 * 改成查询采购单 modify by fyyang 091215 增加采购单号、供应商查询条件 合同号查询修改为到货单号查询 update bjxu
	 * 091210
	 * 
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findArrivalBillList(int start, int limit,
			String operator, String checkStatus, String enterpriseCode,
			String contractNo, String invoiceNo, String purNo, String clientName) {
		// String sqlWhere="";
		String sqlWhere = " and   b.invoice_no is not null and b.invoice_no <> ' ' \n";
		if (purNo != null && !purNo.equals("")) {
			sqlWhere += " and  b.pur_no like '%" + purNo.trim() + "%' \n";
		}
		if (clientName != null && !clientName.equals("")) {
			sqlWhere += " and  GETCLIENTNAME(b.supplier) like '%"
					+ clientName.trim() + "%' \n";
		}
		if (contractNo != null && !contractNo.trim().equals("")) {
			sqlWhere += "  and d.MIF_NO like '%" + contractNo.trim() + "%' \n";
		}
		if (invoiceNo != null && !invoiceNo.trim().equals("")) {
			sqlWhere += "  and b.invoice_no like '%" + invoiceNo.trim()
					+ "%' \n";
		}

		// String sql = "SELECT
		// DISTINCT(A.ID),A.ARRIVAL_NO,A.PUR_NO,A.CONTRACT_NO,"
		// +
		// "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd')OPERATE_DATE,A.SUPPLIER,GETCLIENTNAME(A.SUPPLIER),A.MEMO,"
		// + "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd
		// hh24:mi:ss')OPERATE_DATE2,A.INVOICE_NO "
		// + "FROM PUR_J_ARRIVAL A,PUR_J_ARRIVAL_DETAILS B,PUR_J_ORDER_DETAILS
		// D,INV_C_MATERIAL E\n"
		// + "WHERE D.IS_USE='Y' AND A.IS_USE='Y' AND B.IS_USE='Y' "
		// + " AND A.ARRIVAL_STATE='2' "
		// + " and B.MATERIAL_ID=E.MATERIAL_ID "
		// + "AND A.ARRIVAL_NO=B.MIF_NO AND A.PUR_NO=D.PUR_NO AND
		// B.PUR_LINE=D.PUR_ORDER_DETAILS_ID AND B.RCV_QTY > 0 AND
		// B.RCV_QTY=B.REC_QTY"
		// + " AND A.ENTERPRISE_CODE='"
		// + enterpriseCode
		// + "'"
		// + " AND B.ENTERPRISE_CODE='"
		// + enterpriseCode
		// + "'"
		// + " AND D.ENTERPRISE_CODE='" + enterpriseCode + "'"
		// //+ " and (A.check_state <> '2' or a.check_state is null)"
		// + " and a.arrival_no not in (select distinct a.mif_no from
		// PUR_J_ARRIVAL_DETAILS a where a.is_use = 'Y' and a.enterprise_code =
		// 'hfdc'and a.rec_qty < a.rcv_qty )";

		String sql = "select distinct b.id,\n"
				+ "                b.pur_no,\n"
				+ "                b.supplier,\n"
				+ "                GETCLIENTNAME(b.supplier),\n"
				+ "                to_char(b.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss'),b.contract_no,b.invoice_no,b.memo,GETWORKERNAME(b.buyer),\n"
				+ "				 (select a.rcv_qty from (select t.pur_no, sum(t.rcv_qty) as rcv_qty from pur_j_arrival_details t where t.rcv_qty <> 0 group by t.pur_no) a where a.pur_no = d.pur_no and rownum = 1),\n"
				+ "                (select m.relation_arrival_no\n"
				+ "                   from pur_j_arrival m\n"
				+ "                  where m.arrival_no = d.mif_no\n"
				+ "                    and rownum = 1) relation_arrival_no"
				+ "  from pur_j_order b, pur_j_arrival_details d\n"
				+ " where (select count(*)\n"
				+ "          from pur_j_order_details a\n"
				+ "         where a.rcv_qty >= a.pur_qty\n"
				+ "           and a.is_use = 'Y'\n"
				+ "           and a.enterprise_code = '" + enterpriseCode
				+ "'\n" + "           and b.pur_no = a.pur_no) =\n"
				+ "       (select count(*)\n"
				+ "          from pur_j_order_details a\n"
				+ "         where a.is_use = 'Y'\n"
				+ "           and a.enterprise_code = '" + enterpriseCode
				+ "'\n" + "           and b.pur_no = a.pur_no)\n" + "\n"
				+ "   and b.pur_no = d.pur_no\n" + "   and b.is_use = 'Y'\n"
				+ "   and b.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and d.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and d.is_use = 'Y'";

		if (checkStatus != null && !checkStatus.equals("")
				&& checkStatus.equals("2"))// add by drdu 091103
		{
			// 已审核未月结的
			sqlWhere += "   and (select count(*)\n"
					+ "          from pur_j_arrival c\n"
					+ "         where c.pur_no = b.pur_no\n"
					+ "           and c.check_state = '2'\n"
					+ "           and c.is_use = 'Y'\n"
					+ " and c.check_date  > (select max(t.last_modified_date) from inv_j_balance t) \n"
					+ "           and c.enterprise_code = '" + enterpriseCode
					+ "') = \n" + "    (select count(*)\n"
					+ "          from pur_j_arrival cc\n"
					+ "         where cc.pur_no = b.pur_no\n"
					+ "           and cc.is_use = 'Y'\n"
					+ "           and cc.enterprise_code = '" + enterpriseCode
					+ "') \n";

		} else if (checkStatus != null && !checkStatus.equals("")
				&& checkStatus.equals("1")) {
			// 未审核的
			sqlWhere += "   and (select count(*)\n"
					+ "          from pur_j_arrival c\n"
					+ "         where c.pur_no = b.pur_no\n"
					+ "           and c.check_state = '2'\n"
					+ "           and c.is_use = 'Y'\n"
					+ "           and c.enterprise_code = '" + enterpriseCode
					+ "') = 0\n";

		}
		sql += sqlWhere;
		// modify by ywliu 20100126
		// sql+="union\n" +
		// "select distinct b.id,\n" +
		// " b.pur_no,\n" +
		// " b.supplier,\n" +
		// " GETCLIENTNAME(b.supplier),\n" +
		// " to_char(b.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss'),\n" +
		// " b.contract_no,\n" +
		// " b.invoice_no,\n" +
		// " b.memo,\n" +
		// " GETWORKERNAME(b.buyer)\n" +
		// "(select n.rcv_qty\n" +
		// " from pur_j_arrival_details n\n" +
		// " where n.mif_no = a.arrival_no\n" +
		// " and rownum = 1) rcv_qty,\n" +
		// " a.relation_arrival_no" +
		// " from pur_j_order b, pur_j_arrival a\n" +
		// " where a.pur_no = b.pur_no\n" +
		// " and a.check_state = 'B')";
		sql += "  order by pur_no desc ";

		List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		List<PurchaseWarehouseInfo> arraylist = new ArrayList<PurchaseWarehouseInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseInfo model = new PurchaseWarehouseInfo();
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			// if (data[1] != null) {
			// model.setArrivalNo(data[1].toString());
			// }
			if (data[1] != null) {
				model.setPurNo(data[1].toString());
			}

			if (data[2] != null) {
				model.setSupplier(data[2].toString());
			}
			if (data[3] != null) {
				model.setSupplyName(data[3].toString());
			}
			if (data[4] != null) {
				model.setOperateDate(data[4].toString());
			}
			if (data[5] != null) {
				model.setContractNo(data[5].toString());
			}
			if (data[6] != null) {
				model.setInvoiceNo(data[6].toString());
			}
			if (data[7] != null) {
				model.setMemo(data[7].toString());
			}
			if (data[8] != null) {
				model.setLoginName(data[8].toString());
			}
			if (data[9] != null) {
				model.setRcvQty(data[9].toString());
			}
			if (data[10] != null) {
				model.setRelationArrivalNo(data[10].toString());
			}

			arraylist.add(model);
		}

		String recordCountSql = "select count(*) from (" + sql + ") \n";
		Object countlist = bll.getSingal(recordCountSql);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;
	}

	/**
	 * 查询入库审核详细信息列表 modify by fyyang 091209
	 * 
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findArrivalBillDetailList(Long id, int start, int limit,
			String enterpriseCode) {
		// String sql = "SELECT distinct
		// A.ID,A.ARRIVAL_NO,B.MEMO,B.LOT_CODE,B.RCV_QTY,B.LAST_MODIFIED_BY,\n"
		// + "
		// C.MATERIAL_ID,C.MATERIAL_NO,C.MATERIAL_NAME,C.IS_LOT,C.MAERTIAL_CLASS_ID,\n"
		// +
		// "C.COST_METHOD,C.STD_COST,C.DEFAULT_WHS_NO,C.DEFAULT_LOCATION_NO,C.SPEC_NO,\n"
		// +
		// "C.STOCK_UM_ID,C.FROZEN_COST,D.PUR_ORDER_DETAILS_ID,D.PUR_QTY,D.RCV_QTY
		// AS RCVQTY,D.UNIT_PRICE,\n"
		// + "D.DUE_DATE,D.TAX_RATE,D.CURRENCY_ID,D.INS_QTY,B.ID AS
		// ARRIVALDETAILID,B.REC_QTY,\n"
		// + "to_char(B.LAST_MODIFIED_DATE,'yyyy-mm-dd
		// hh24:mi:ss')ArrivalDetailModifiedDate,\n"
		// + "to_char(D.LAST_MODIFIED_DATE,'yyyy-mm-dd
		// hh24:mi:ss')OrderDetailModifiedDate,\n"
		// + "to_char(C.LAST_MODIFIED_DATE,'yyyy-mm-dd
		// hh24:mi:ss')MaterialModifiedDate,\n"
		// + "GETWHSNAME(c.default_whs_no),\n"
		// + "e.from_location_no\n"
		// // add by liuyi 091126 增加单价和金额
		// + ",(select sum(t.trans_qty*t.price)/sum(t.trans_qty) \n"
		// + "from inv_j_transaction_his t where t.material_id=c.material_id \n"
		// + "and t.Arrival_No=a.arrival_no ) unitCost, \n"
		// + " (select sum(t.trans_qty*t.price) from inv_j_transaction_his t \n"
		// + "where t.material_id=c.material_id \n"
		// + "and t.Arrival_No=a.arrival_no ) acoutnPrice \n"
		// + "FROM PUR_J_ARRIVAL A,PUR_J_ORDER_DETAILS D,PUR_J_ARRIVAL_DETAILS
		// B,INV_C_MATERIAL C ,inv_j_transaction_his e "
		// + "WHERE A.IS_USE='Y' AND B.IS_USE='Y' AND C.IS_USE='Y' AND
		// D.IS_USE='Y' AND "
		// + "A.ARRIVAL_NO=B.MIF_NO AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID AND
		// B.MATERIAL_ID=C.MATERIAL_ID AND "
		// + "A.ID="
		// + id
		// + " AND A.ENTERPRISE_CODE='"
		// + enterpriseCode
		// + "'"
		// + " AND B.ENTERPRISE_CODE='"
		// + enterpriseCode
		// + "'"
		// + " AND C.ENTERPRISE_CODE='"
		// + enterpriseCode
		// + "'"
		// + " AND D.ENTERPRISE_CODE='"
		// + enterpriseCode
		// + "'"
		// + " and e.order_no = d.pur_no and B.The_Qty<>0";

		String sql = "select a.pur_order_details_id,\n"
				+ "       a.pur_no,\n"
				+ "       a.memo,\n"
				+ "       a.material_id,\n"
				+ "       c.material_no,\n"
				+ "       c.material_name,\n"
				+ "       c.spec_no,\n"
				+ "       c.stock_um_id,\n"
				+ "       a.pur_qty,\n"
				+ "       a.rcv_qty,\n"
				+ "       a.unit_price,\n"
				+ "       a.unit_price * a.rcv_qty ,to_char(a.due_date,'yyyy-mm-dd'),\n"
				+ "       a.tax_count,\n" // add by drdu 20100408
				+ "       a.pur_order_details_id\n" // add by sychen  20100427
				+ "  from pur_j_order_details a, pur_j_order b, inv_c_material c\n"
				+ " where a.pur_no = b.pur_no\n" + "   and b.id = " + id + "\n"
				+ "   and c.material_id = a.material_id\n"
				+ "   and a.is_use = 'Y'\n"
				+ "   and a.enterprise_code = 'hfdc'\n"
				+ "   and b.is_use = 'Y'\n"
				+ "   and b.enterprise_code = 'hfdc'\n"
				+ "   and c.is_use = 'Y'\n"
				+ "   and c.enterprise_code = 'hfdc'";

		List<Object> list = bll.queryByNativeSQL(sql);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setArrivalNo(data[1].toString());
			}
			if (data[2] != null) {
				model.setMemo(data[2].toString());
			}
			// if (data[3] != null) {
			// model.setLotCode(data[3].toString());
			// }
			// if (data[4] != null) {
			// model.setRcvQty(Double.parseDouble(data[4].toString()));
			// }
			// if (data[5] != null) {
			// model.setOperateBy(data[5].toString());
			// }
			if (data[3] != null) {
				model.setMaterialId(Long.parseLong(data[3].toString()));
			}
			if (data[4] != null) {
				model.setMaterialNo(data[4].toString());
			}
			if (data[5] != null) {
				model.setMaterialName(data[5].toString());
			}
			// if (data[9] != null) {
			// model.setIsLot(data[9].toString());
			// }
			// if (data[10] != null) {
			// model.setMaterialClassId(Long.parseLong(data[10].toString()));
			// }
			// if (data[11] != null) {
			// model.setCostMethod(data[11].toString());
			// }
			// if (data[12] != null) {
			// model.setStdCost(Double.parseDouble(data[12].toString()));
			// }
			// if (data[13] != null) {
			// model.setDefaultWhsNo(data[13].toString());
			// }
			// if (data[14] != null) {
			// model.setDefaultLocationNo(data[14].toString());
			// }
			if (data[6] != null) {
				model.setSpecNo(data[6].toString());
			}
			if (data[7] != null) {
				model.setStockUmId(Long.parseLong(data[7].toString()));
			}
			// if (data[17] != null) {
			// model.setFrozenCost(Double.parseDouble(data[17].toString()));
			// }
			// if (data[18] != null) {
			// model.setPurOrderDetailsId(Long.parseLong(data[18].toString()));
			// }
			if (data[8] != null) {
				model.setPurQty(Double.parseDouble(data[8].toString()));
			}
			if (data[9] != null) {
				model.setPurOrderDetailsRcvQty(Double.parseDouble(data[9]
						.toString()));
			}
			if (data[10] != null) {
				model.setUnitCost(Double.parseDouble(data[10].toString()));
			}

			// if (data[23] != null) {
			// model.setTaxRate(data[23].toString());
			// }
			// // TODO
			// if (data[24] != null) {
			// model.setCurrencyType(data[24].toString());
			// }
			//	
			// if (data[25] != null) {
			// model.setInsqty(Double.parseDouble(data[25].toString()));
			// }
			//	
			// if (data[26] != null) {
			// model.setArrivalDetailID(Long.parseLong(data[26].toString()));
			// }
			// if (data[27] != null) {
			// model.setRecQty(Double.parseDouble(data[27].toString()));
			// }
			// if (data[28] != null) {
			// model.setArrivalDetailModifiedDate(data[28].toString());
			// }
			// if (data[29] != null) {
			// model.setOrderDetailModifiedDate(data[29].toString());
			// }
			// if (data[30] != null) {
			// model.setMaterialModifiedDate(data[30].toString());
			// }
			// // TODO
			// // 待入库数 本次入库数
			// if (data[19] != null && data[20] != null) {
			// model.setWaitQty(Double.parseDouble(data[19].toString())
			// - Double.parseDouble(data[20].toString()));
			// }
			// if (data[4] != null && data[27] != null) {
			// model.setCanQty(Double.parseDouble(data[4].toString())
			// - Double.parseDouble(data[27].toString()));
			// }
			// // TODO
			// // 保管员
			// model.setThisQty(0d);
			// model.setSaveName("");
			// if(data[31] != null) {
			// model.setWhsNo(data[31].toString());
			// }
			// if(data[32] != null) {
			// model.setLocationNo(data[32].toString());
			// }
			//			
			// // add by liuyi 091126 增加单价和金额
			// if(data[33] != null){
			// model.setUnitCost(Double.parseDouble(data[33].toString()));
			// }
			// else
			// model.setUnitCost(0.0);
			if (data[11] != null) {
				model.setAccoutPrice(Double.parseDouble(data[11].toString()));
			} else
				model.setAccoutPrice(0.0);
			if (data[12] != null) {
				model.setDueDate(data[12].toString());
			}
			if (data[13] != null)
				model.setTaxCount(Double.parseDouble(data[13].toString()));
			// add by sychen 20100427
			if (data[14] != null)
			model.setPurOrderDetailsId(Long.parseLong(data[14].toString()));
			
			model.setGridMemo(model.getMemo());
			arraylist.add(model);
		}

		PageObject obj = new PageObject();
		obj.setList(arraylist);
		return obj;
	}

	/**
	 * 批量审核入库单 modify by fyyang 091209
	 * 
	 * @param checkList
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkArrivalBillList(List checkList, String workCodeName,
			List<Map> list) {
		String purNo = "";
		// --------add by drdu 20100408---------------------
		try {
			for (Map data : list) {
				PurJOrderDetails model = entityManager.find(
						PurJOrderDetails.class, Long.parseLong(data.get("id")
								.toString()));
				if (data.get("tc") != null) {
					model.setTaxCount(Double.parseDouble(data.get("tc")
							.toString()));
				} else {
					model.setTaxCount(0.0000);
				}
				entityManager.merge(model);
			}

			// ------------------end----------------------------
			for (int i = 0; i < checkList.size(); i++) {
				if (purNo.equals("")) {
					purNo = "'" + checkList.get(i).toString() + "'";
				} else {
					purNo = purNo + "," + "'" + checkList.get(i).toString()
							+ "'";
				}
			}
			String sql = "update pur_j_arrival t\n"
					+ "   set t.check_state = '2', t.check_date = sysdate, t.check_by = '"
					+ workCodeName + "'\n" + " where t.pur_no in (" + purNo
					+ ")\n" + "   and t.is_use = 'Y'";
			bll.exeNativeSQL(sql);
		} catch (RuntimeException re) {
			throw re;
		}
		return true;
	}

	/**
	 * 批量取消审核 add by drdu 091103 modify by fyyang 091209
	 * 
	 * @param checkList
	 * @param workCodeName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean cancelCheckArrival(List checkList, String workCodeName) {
		String purNo = "";
		for (int i = 0; i < checkList.size(); i++) {
			if (purNo.equals("")) {
				purNo = "'" + checkList.get(i).toString() + "'";
			} else {
				purNo = purNo + "," + "'" + checkList.get(i).toString() + "'";
			}
		}
		String sql = "update pur_j_arrival t\n"
				+ "   set t.check_state = '1', t.check_date = sysdate, t.check_by = '"
				+ workCodeName + "'\n" + " where t.pur_no in (" + purNo + ")\n"
				+ "   and t.is_use = 'Y'";
		bll.exeNativeSQL(sql);
		return true;
	}

	// ========================================取消入库查询列表 add by ywliu
	// 090708===========================================//
	@SuppressWarnings("unchecked")
	public PageObject findCancelPurchaseWarehouseList(String enterpriseCode,
			String issueNo, String materialId, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select h.TRANS_HIS_ID,\n"
				+ "                h.ORDER_NO,\n"
				+ "                h.SEQUENCE_ID,\n"
				+ "                t.TRANS_NAME,\n"
				+ "                m.MATERIAL_NO,\n"
				+ "                m.MATERIAL_NAME,\n"
				+ "                m.SPEC_NO,\n"
				+ "                m.PARAMETER,\n"
				+
				// " h.TRANS_QTY,\n" + //modify by fyyang 091130
				// " h.TRANS_QTY+nvl((select sum(aa.TRANS_QTY) from
				// INV_J_TRANSACTION_HIS aa where
				// aa.roll_back_id=h.trans_his_id),0),"+ // modify by ywliu
				// 20100223
				"h.TRANS_QTY + nvl(rb.sl,0),\n"
				+ // modify by fyyang 20100303
				"                getunitname(h.TRANS_UM_ID),\n"
				+ "                GETWORKERNAME(h.LAST_MODIFIED_BY),\n"
				+ "                h.LAST_MODIFIED_DATE,\n"
				+ "                wf.WHS_NAME wfWhsName,\n"
				+ "                lf.LOCATION_NAME lfLocationName,\n"
				+ "                wt.WHS_NAME wtWhsName,\n"
				+ "                lt.LOCATION_NAME ltLocationName,\n"
				+ "                h.LOT_NO,\n"
				+ "                h.MEMO,\n"
				+ "	             h.arrival_no,\n"
				+ " 				 m.material_id,\n"
				+ "				 h.from_whs_no,\n"
				+ "			     h.from_location_no\n"
				+ " ,nvl(h.price,0) price, nvl(m.std_cost,0) stdCost \n"
				+ // add by fyyang 20100303
				"  from \n"
				+ "       INV_J_TRANSACTION_HIS h,\n"
				+ "       INV_C_TRANSACTION     t,\n"
				+ "       INV_C_MATERIAL        m,\n"
				+ "       INV_C_WAREHOUSE       wf,\n"
				+ "       INV_C_LOCATION        lf,\n"
				+ "       INV_C_WAREHOUSE       wt,\n"
				+
				// " resourse_v_not_check_pur vp,\n"+
				"       PUR_J_ARRIVAL a,\n"
				+ "       INV_C_LOCATION        lt\n"
				+ " ,RESOURSE_V_ROLLBACK_ARRIVAL rb \n"
				+ // add by fyyang 20100303
				" where t.TRANS_ID = h.TRANS_ID\n"
				+ " and h.trans_his_id=rb.trans_his_id(+) \n "
				+ // add by fyyang 20100303
				"   and t.trans_code = 'P'\n"
				+ "   and m.MATERIAL_ID = h.MATERIAL_ID\n"
				+ "   and wf.WHS_NO(+) = h.FROM_WHS_NO\n"
				+ "   and lf.WHS_NO(+) = h.FROM_WHS_NO\n"
				+ "   and lf.LOCATION_NO(+) = h.FROM_LOCATION_NO\n"
				+ "   and wt.WHS_NO(+) = h.TO_WHS_NO\n"
				+ "   and lt.WHS_NO(+) = h.TO_WHS_NO\n"
				+ "   and lt.LOCATION_NO(+) = h.TO_LOCATION_NO\n"
				+ "   and h.is_use = 'Y'\n"
				+ "   and t.is_use = 'Y'\n"
				+ "   and m.is_use = 'Y'\n"
				+ "   and wf.is_use(+) = 'Y'\n"
				+ "   and lf.is_use(+) = 'Y'\n"
				+ "   and wt.is_use(+) = 'Y'\n"
				+ "   and lt.is_use(+) = 'Y'\n"
				+ "   and h.enterprise_code = 'hfdc'\n"
				+ "   and t.enterprise_code = 'hfdc'\n"
				+ "   and m.enterprise_code = 'hfdc'\n"
				+ "   and wf.enterprise_code(+) = 'hfdc'\n"
				+ "   and lf.enterprise_code(+) = 'hfdc'\n"
				+ "   and wt.enterprise_code(+) = 'hfdc'\n"
				+ "   and lt.enterprise_code(+) = 'hfdc'\n"
				+
				// " and h.ORDER_NO in (\n" +
				// "SELECT A.PUR_NO\n" +
				// " FROM PUR_J_ARRIVAL A,\n" +
				// " PUR_J_ARRIVAL_DETAILS B,\n" +
				// " INV_C_MATERIAL E\n" +
				// " WHERE A.IS_USE = 'Y'\n" +
				// " AND B.IS_USE = 'Y'\n" +
				// " AND A.ARRIVAL_STATE = '2'\n" +
				// " and B.MATERIAL_ID = E.MATERIAL_ID\n" +
				// " AND A.ARRIVAL_NO = B.MIF_NO\n" +
				// " AND B.RCV_QTY > 0\n" +
				// " AND B.RCV_QTY = B.REC_QTY\n" +
				// " AND A.ENTERPRISE_CODE = 'hfdc'\n" +
				// " AND B.ENTERPRISE_CODE = 'hfdc'\n" +
				// " and (A.check_state <> '2' or a.check_state is null))\n" +
				// " and h.ORDER_NO=vp.PUR_NO\n"+ //modify by fyyang 20100303
				"   and h.TRANS_QTY > 0 \n" + "    and a.pur_no=h.order_no \n"
				+ " and (a.check_state<>'2' or a.check_state is null) \n " +
				// add by fyyang 091130
				// "and h.TRANS_QTY+ nvl((select sum(aa.TRANS_QTY) from
				// INV_J_TRANSACTION_HIS aa where
				// aa.roll_back_id=h.trans_his_id),0)>0 \n";// modify by ywliu
				// 20100223
				"and h.TRANS_QTY + nvl(rb.sl,0) > 0\n";// modify by fyyang
		// 20100303

		String sqlCount = "select count(1)\n"
				+ "  from \n"
				+ "       INV_J_TRANSACTION_HIS h,\n"
				+ "       INV_C_TRANSACTION     t,\n"
				+ "       INV_C_MATERIAL        m,\n"
				+ "       INV_C_WAREHOUSE       wf,\n"
				+ "       INV_C_LOCATION        lf,\n"
				+ "       INV_C_WAREHOUSE       wt,\n"
				+ "       INV_C_LOCATION        lt,\n"
				+
				// " resourse_v_not_check_pur vp,\n" +
				"       PUR_J_ARRIVAL a,\n"
				+ "       resourse_v_rollback_arrival rb"
				+ " where t.TRANS_ID = h.TRANS_ID\n"
				+ "   and t.trans_code = 'P'\n"
				+ "   and m.MATERIAL_ID = h.MATERIAL_ID\n"
				+ "   and wf.WHS_NO(+) = h.FROM_WHS_NO\n"
				+ "   and lf.WHS_NO(+) = h.FROM_WHS_NO\n"
				+ "   and lf.LOCATION_NO(+) = h.FROM_LOCATION_NO\n"
				+ "   and wt.WHS_NO(+) = h.TO_WHS_NO\n"
				+ "   and lt.WHS_NO(+) = h.TO_WHS_NO\n"
				+ "   and lt.LOCATION_NO(+) = h.TO_LOCATION_NO\n"
				+ "   and h.is_use = 'Y'\n"
				+ "   and t.is_use = 'Y'\n"
				+ "   and m.is_use = 'Y'\n"
				+ "   and wf.is_use(+) = 'Y'\n"
				+ "   and lf.is_use(+) = 'Y'\n"
				+ "   and wt.is_use(+) = 'Y'\n"
				+ "   and lt.is_use(+) = 'Y'\n"
				+ "   and h.enterprise_code = 'hfdc'\n"
				+ "   and t.enterprise_code = 'hfdc'\n"
				+ "   and m.enterprise_code = 'hfdc'\n"
				+ "   and wf.enterprise_code(+) = 'hfdc'\n"
				+ "   and lf.enterprise_code(+) = 'hfdc'\n"
				+ "   and wt.enterprise_code(+) = 'hfdc'\n"
				+ "   and lt.enterprise_code(+) = 'hfdc'\n"
				+
				// " and h.ORDER_NO in (\n" +
				// "SELECT A.PUR_NO\n" +
				// " FROM PUR_J_ARRIVAL A,\n" +
				// " PUR_J_ARRIVAL_DETAILS B,\n" +
				// " INV_C_MATERIAL E\n" +
				// " WHERE A.IS_USE = 'Y'\n" +
				// " AND B.IS_USE = 'Y'\n" +
				// " AND A.ARRIVAL_STATE = '2'\n" +
				// " and B.MATERIAL_ID = E.MATERIAL_ID\n" +
				// " AND A.ARRIVAL_NO = B.MIF_NO\n" +
				// " AND B.RCV_QTY > 0\n" +
				// " AND B.RCV_QTY = B.REC_QTY\n" +
				// " AND A.ENTERPRISE_CODE = 'hfdc'\n" +
				// " AND B.ENTERPRISE_CODE = 'hfdc'\n" +
				// " and (A.check_state <> '2' or a.check_state is null))\n" +
				// " and a.arrival_no not in (select distinct a.mif_no\n" +
				// " from PUR_J_ARRIVAL_DETAILS a\n" +
				// " where a.is_use = 'Y'\n" +
				// " and a.enterprise_code = 'hfdc'\n" +
				// " and a.rec_qty < a.rcv_qty\n" +
				// " and a.rec_qty > 0))\n" + // modify by ywliu 20091106
				"   and h.TRANS_QTY > 0 \n"
				+
				// " and h.order_no = vp.PUR_NO\n" +
				"    and a.pur_no=h.order_no \n"
				+ " and (a.check_state<>'2' or a.check_state is null) \n "
				+ "   and h.trans_his_id = rb.trans_his_id(+)\n"
				+ "   and nvl(rb.sl,0) + h.trans_qty>0";

		// add by fyyang 091130
		// "and h.TRANS_QTY+ nvl((select sum(aa.TRANS_QTY) from
		// INV_J_TRANSACTION_HIS aa where aa.memo=to_char(h.trans_his_id)),0)>0
		// \n";

		String strWhere = "";
		if (materialId != null && !"".equals(materialId)) {
			strWhere += " and m.MATERIAL_NAME like '%" + materialId + "%'";
		}

		if (issueNo != null && !issueNo.equals("")) {
			strWhere += "  and h.arrival_no like '%" + issueNo + "%'\n";
		}
		sql += strWhere;
		sqlCount += strWhere;
		// sql += " order by h.TRANS_HIS_ID desc";
		// sqlCount += " order by m.MATERIAL_NO, h.TRANS_HIS_ID";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
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
					model.setTransUmId(data[9].toString());
				}
				// 操作人
				if (data[10] != null) {
					model.setOperatedBy(data[10].toString());
					// Employee emp =
					// personInfo.getEmployeeInfo(data[10].toString());
					// if(emp != null) {
					// // 人员姓名
					// String strChsName = emp.getWorkerName();
					// model.setOperatedBy(strChsName);
					// }
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
				// 到货单号
				if (data[18] != null)
					model.setArrivalNo(data[18].toString());
				if (data[19] != null)
					model.setMaterialId(data[19].toString());
				if (data[20] != null)
					model.setWhsNameTwo(data[20].toString());
				if (data[21] != null)
					model.setLocationNameTwo(data[21].toString());
				if (data[22] != null)
					model.setPrice(Double.parseDouble(data[22].toString()));
				if (data[23] != null)
					model.setStdCost(Double.parseDouble(data[23].toString()));

				arrlist.add(model);
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
		}
		return pg;
	}

	/**
	 * 入库回滚操作 modify by fyyang 091109 qty 改为 double型 modify by fyyang 091130
	 * 
	 * @param enterpriseCode
	 * @param tHisId
	 * @param qty
	 */
	public void updatePurchaseWarehouseQty(String enterpriseCode, Long tHisId,
			Double qty, String workCode) {
		InvJTransactionHis his = invJTransHisRemote.findById(tHisId);
		Long materialId;
		String whsNo = "";
		String lotNo = "";
		String locationNo = "";

		whsNo = his.getFromWhsNo();
		materialId = his.getMaterialId();
		lotNo = his.getLotNo();
		locationNo = his.getFromLocationNo();
		String arrivalNo = his.getArrivalNo();
		String orderNo = his.getOrderNo();// modify by ywliu 20091010

		// 修改主物料里的标准成本 add by ywliu 20091113
		// modify by fyyang 091130
		Double stdCostTemp = invCMaterialRemote.updateStdCost(materialId,
				enterpriseCode, 0 - qty, his.getPrice());
		// String tempQty = this.findWarehouseQty(materialId,
		// his.getEnterpriseCode());
		// Double warehouseQty = Double.parseDouble(tempQty); //库存
		// Double stdCostNow=0d;
		// InvCMaterial
		// materialModel=invCMaterialRemote.findById(his.getMaterialId());
		// if(materialModel!=null)
		// {
		// stdCostNow=materialModel.getStdCost();
		// }
		//		
		// // 标准成本ＴＥＭＰ add by ywliu 20091113
		// //---------modify by fyyang 091118 start------
		//		
		// if(warehouseQty >qty)
		// {
		// // stdCostTemp=(his.getStdCost() * warehouseQty - his.getPrice() *
		// qty)
		// // / (warehouseQty - qty);
		// stdCostTemp=(stdCostNow * warehouseQty - his.getPrice() * qty)
		// / (warehouseQty - qty);
		// }
		// else if(warehouseQty == qty)
		// {
		// //stdCostTemp=(his.getStdCost() * warehouseQty - his.getPrice() *
		// qty);
		// stdCostTemp=(stdCostNow * warehouseQty - his.getPrice() * qty);
		// }
		// else
		// {
		// stdCostTemp=0d;
		// }

		// ---------modify by fyyang 091118 end------
		// //修改物料主文件 add by ywliu 20091113
		// String sql = "update inv_c_material h\n" +
		// " set h.std_cost = "+stdCostTemp+"\n"+
		// " where h.material_id = '"+materialId+"'\n" +
		// " and h.is_use = 'Y'\n" +
		// " and h.enterprise_code = '"+enterpriseCode+"'";
		// bll.exeNativeSQL(sql);

		// 修改到货单明细表
		String sql2 = "update pur_j_arrival_details d\n"
				+ "   set d.rec_qty = d.rec_qty - " + qty + "\n"
				+ " where d.mif_no = '" + arrivalNo + "'\n"
				+ "   and d.material_id = '" + materialId + "'\n"
				+ "   and d.is_use = 'Y'\n" + "   and d.enterprise_code = '"
				+ enterpriseCode + "'";
		bll.exeNativeSQL(sql2);

		// 修改订货单明细表
		String sql6 = "update PUR_J_ORDER_DETAILS d\n"
				+ "   set d.rcv_qty = d.rcv_qty - " + qty + ",\n"
				+ "   d.ins_qty = d.ins_qty + " + qty + "\n"
				+ " where d.pur_no = '" + orderNo + "'\n"
				+ "   and d.material_id = '" + materialId + "'\n"
				+ "   and d.is_use = 'Y'\n" + "   and d.enterprise_code = '"
				+ enterpriseCode + "'";
		bll.exeNativeSQL(sql6);// modify by ywliu 20091010

		// 修改库存物料记录
		String sql1 = "update inv_j_warehouse a\n"
				+ "   set a.receipt = a.receipt - " + qty + "\n"
				+ " where a.material_id = " + materialId + "\n"
				+ "   and a.whs_no = '" + whsNo + "'\n"
				+ "   and a.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'";
		bll.exeNativeSQL(sql1);

		// modify by fyyang 091130 事务历史表的此条记录不能改变
		// //修改事务历史表
		// String sql3 = "update inv_j_transaction_his h\n" +
		// " set h.trans_qty = h.trans_qty - "+qty+",\n" +
		// " h.std_cost = "+stdCostTemp+"\n"+ // modify by ywliu 20091113
		// " where h.trans_his_id = "+tHisId+"\n" +
		// " and h.is_use = 'Y'\n" +
		// " and h.enterprise_code = '"+enterpriseCode+"'";
		// bll.exeNativeSQL(sql3);

		// 修改库位物料记录
		String sql4 = "update inv_j_location l\n"
				+ "   set l.receipt = l.receipt - " + qty + "\n"
				+ " where l.material_id = " + materialId + "\n"
				+ "   and l.whs_no = '" + whsNo + "'\n"
				+ "   and l.is_use = 'Y'\n" + "  and  l.location_no='"
				+ locationNo + "' \n" + "   and l.enterprise_code = '"
				+ enterpriseCode + "'";
		bll.exeNativeSQL(sql4);

		// 修改批号记录表
		String sql5 = "update INV_J_LOT t\n"
				+ "   set t.receipt = t.receipt - " + qty + "\n"
				+ " where t.material_id = " + materialId + "\n"
				+ "   and t.whs_no = '" + whsNo + "'\n" + "   and t.lot_no = '"
				+ lotNo + "'\n" + "   and t.is_use = 'Y'\n"
				+ "   and t.location_no='" + locationNo + "'  \n"
				+ "   and t.enterprise_code = '" + enterpriseCode + "'";
		bll.exeNativeSQL(sql5);

		// --------add by fyyang 091130-------------------------
		// 回滚时在事务历史表里增加一条数量为负数的数据。讲回滚的原记录id保存在新产生的记录的备注字段里面以关联查找
		his.setTransQty(0 - qty);
		his.setStdCost(stdCostTemp);
		his.setRollBackId(his.getTransHisId());// modify by ywliu 20100223
		his.setTransHisId(null);
		his.setLastModifiedDate(new Date());
		his.setLastModifiedBy(workCode);
		invJTransHisRemote.save(his);
		// -----------------------------------------------------

	}

	// --------------add by ywliu 090708
	// end-----------------------------------------------

	/**
	 * 采购入库页面中补打印列表记录 add by drdu 091120
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAfterPrintList(String arrivalNo, int start,
			int limit, String enterpriseCode) {
		// modified by liuyi 20100413 过滤回滚数据
		String sql = "select distinct h.ORDER_NO,\n" +
		"                to_char(h.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss'),\n" + 
		"                H.ARRIVAL_NO,\n" + 
		"                H.SUPPLIER,\n" + 
		"                getclientname(H.SUPPLIER)\n" + 
		"  from INV_J_TRANSACTION_HIS h, resourse_v_rollback_arrival rb\n" + 
		" where H.ARRIVAL_NO = '"+arrivalNo+"'\n" + 
		"   AND H.IS_USE = 'Y'\n" + 
		"   and h.trans_his_id = rb.trans_his_id(+)\n" + 
		"   and h.trans_qty + nvl(rb.sl, 0) <> 0\n" + 
		"   AND H.ENTERPRISE_CODE = '"+enterpriseCode+"'\n" + 
		"   and h.roll_back_id is null\n" + 
		" order by to_char(h.last_modified_date, 'yyyy-mm-dd hh24:mi:ss') desc";
		String sqlcount = "select count(1)\n" + "  from (" + sql + ")";

		List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		List<PurchaseWarehouseInfo> arraylist = new ArrayList<PurchaseWarehouseInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseInfo model = new PurchaseWarehouseInfo();
			if (data[0] != null)
				model.setPurNo(data[0].toString());
			if (data[1] != null)
				model.setOperateDate(data[1].toString());
			if (data[2] != null)
				model.setArrivalNo(data[2].toString());
			if (data[3] != null)
				model.setSupplier(data[3].toString());
			if (data[4] != null)
				model.setSupplyName(data[4].toString());

			// modified by liuyi 20100430 处理来源为固定资产类的明细数据
//			String sqlDetail = "select distinct h.from_whs_no,\n"
//					+ " getwhsname(h.from_whs_no)\n"
//					+ "  from INV_J_TRANSACTION_HIS h\n"
//					+ " where h.order_no = '" + data[0].toString() + "'\n"
//					+ "   and h.is_use = 'Y'\n"
//					+ "   and h.last_modified_date =\n" + "       to_date('"
//					+ data[1].toString() + "', 'yyyy-MM-dd hh24:mi:ss')";
//			List<Object> listDetail = bll.queryByNativeSQL(sqlDetail);
//			Iterator itDetail = listDetail.iterator();
//			while (itDetail.hasNext()) {
//				Object[] o = (Object[]) itDetail.next();
//				if (o[0] != null) {
//					if (model.getWhsNo() == null || model.getWhsNo().equals(""))
//						model.setWhsNo(o[0].toString());
//					else {
//						model
//								.setWhsNo(model.getWhsNo() + ","
//										+ o[0].toString());
//					}
//				}
//				if (o[1] != null) {
//					if (model.getWhsName() == null
//							|| model.getWhsName().equals("")) {
//						model.setWhsName(o[1].toString());
//					} else {
//						model.setWhsName(model.getWhsName() + ","
//								+ o[1].toString());
//					}
//				}
//			}
			String sqlDetail = "select distinct h.from_whs_no,\n"
				+ " getwhsname(h.from_whs_no),h.material_id\n"
				+ "  from INV_J_TRANSACTION_HIS h\n"
				+ " where h.order_no = '" + data[0].toString() + "'\n"
				+ "   and h.is_use = 'Y'\n"
				+ "   and h.last_modified_date =\n" + "       to_date('"
				+ data[1].toString() + "', 'yyyy-MM-dd hh24:mi:ss')"
				+ " order by h.from_whs_no \n";
			List<Object> listDetail = bll.queryByNativeSQL(sqlDetail);
			Iterator itDetail = listDetail.iterator();
			String whsNo = "";
			String whsName = "";
			String allMetailIds = "";
			String gdMetailIds = "";
			while (itDetail.hasNext()) {
				Object[] o = (Object[]) itDetail.next();
				if (o[0] != null) {
					if (whsNo.equals(""))
					{
						whsNo += o[0].toString();
						if(o[2] != null){
							allMetailIds +=  o[2].toString();
							String judgeSql = 
								"select  c.plan_original_id\n" +
								"        from pur_j_plan_order              a,\n" + 
								"             mrp_j_plan_requirement_detail b,\n" + 
								"             mrp_j_plan_requirement_head   c\n" + 
								"       where a.is_use = 'Y'\n" + 
								"         and b.material_id = '"+o[2].toString()+"'\n" + 
								"         and a.pur_no = '"+ model.getPurNo() + "'\n" + 
								"         and a.requirement_detail_id = b.requirement_detail_id\n" + 
								"         and b.requirement_head_id = c.requirement_head_id";
							List judgeList = bll.queryByNativeSQL(judgeSql);
							// 判断需求计划来源是否为固定资产类
							if(judgeList != null && judgeList.size() > 0 && judgeList.get(0).toString().equals("3")){								
								gdMetailIds +=  o[2].toString();
							}

						}
					}
					else {// 考虑第一个仓库为空时，此时 whoNo 为 ","
						String[] whsArr = whsNo.split(","); 
						// 下一个仓库和上一个仓库相同
						if (whsArr[whsArr.length - 1].equals(o[0])) {
							if(o[2] != null){
								allMetailIds += "," + o[2].toString();
								String judgeSql = 
									"select  c.plan_original_id\n" +
									"        from pur_j_plan_order              a,\n" + 
									"             mrp_j_plan_requirement_detail b,\n" + 
									"             mrp_j_plan_requirement_head   c\n" + 
									"       where a.is_use = 'Y'\n" + 
									"         and b.material_id = '"+o[2].toString()+"'\n" + 
									"         and a.pur_no = '"+ model.getPurNo() + "'\n" + 
									"         and a.requirement_detail_id = b.requirement_detail_id\n" + 
									"         and b.requirement_head_id = c.requirement_head_id";
								List judgeList = bll.queryByNativeSQL(judgeSql);
								// 判断需求计划来源是否为固定资产类
								if(judgeList != null && judgeList.size() > 0 && judgeList.get(0).toString().equals("3")){
									if(gdMetailIds.equals("") || gdMetailIds.endsWith(";"))
										gdMetailIds += o[2].toString();
									else
										gdMetailIds += "," + o[2].toString();
								}
							}
						}else{
							whsNo += "," + o[0].toString();
							// 不同仓库间用物资 ; 分隔
							allMetailIds += ";" ;
							gdMetailIds += ";";
							if(o[2] != null){
								allMetailIds +=  o[2].toString();
								String judgeSql = 
									"select  c.plan_original_id\n" +
									"        from pur_j_plan_order              a,\n" + 
									"             mrp_j_plan_requirement_detail b,\n" + 
									"             mrp_j_plan_requirement_head   c\n" + 
									"       where a.is_use = 'Y'\n" + 
									"         and b.material_id = '"+o[2].toString()+"'\n" + 
									"         and a.pur_no = '"+ model.getPurNo() + "'\n" + 
									"         and a.requirement_detail_id = b.requirement_detail_id\n" + 
									"         and b.requirement_head_id = c.requirement_head_id";
								List judgeList = bll.queryByNativeSQL(judgeSql);
								// 判断需求计划来源是否为固定资产类
								if(judgeList != null && judgeList.size() > 0 && judgeList.get(0).toString().equals("3")){
									gdMetailIds += o[2].toString();
								}
							}
						}
						
					}
				}
				if (o[1] != null) {
					if(whsName.equals(""))
					{
						whsName += o[1].toString();
					} else {
						String[] whsNameArr = whsName.split(",");
						// 下一个仓库和上一个仓库相同
						if (whsNameArr[whsNameArr.length - 1].equals(o[1])) {
							;
						} else {
							whsName += "," + o[1].toString();
						}
					}
				}
			}
			model.setWhsNo(whsNo);
			model.setWhsName(whsName);
			model.setAllMetailIds(allMetailIds);
			model.setGdMetailIds(gdMetailIds);

			
			

			arraylist.add(model);
		}
		Object countlist = bll.getSingal(sqlcount);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;
	}

	/**
	 * 采购入库补打印报表数据信息 add by drdu 091120
	 * modified by liuyi 20100430 
	 * @param operateDate
	 * @param arrivalNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
//	public List<PurchaseWarehouseDetailInfo> getAfterReceiptBillMaterialInfo(
//			String operateDate, String arrivalNo, String whsNo) {
	public List<PurchaseWarehouseDetailInfo> getAfterReceiptBillMaterialInfo(
			String operateDate, String arrivalNo, String whsNo,String metailIdNotIn) {
		String sql = "select a.trans_qty +(select nvl(sum(b.trans_qty),0) from inv_j_transaction_his b where b.roll_back_id=a.trans_his_id),\n"
				+ // modify by ywliu 20100223
				"       a.price/(1+a.tax_rate),\n"
				+ "       GETWHSNAME(a.from_whs_no),\n"
				+ "       a.tax_rate,\n"
				+ "       b.material_no,\n"
				+ "       b.material_name,\n"
				+ "       b.spec_no,\n"
				+ "       GETUNITNAME(a.trans_um_id),\n"
				+ "       GETWORKERNAME(c.buyer),\n"
				+ "       d.invoice_no,\n"
				+ "       to_char(a.last_modified_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       getclientname(d.supplier)\n"
				+ ",a.trans_qty*a.price/(1+a.tax_rate),a.trans_qty*a.price,a.price \n"
				// add by ltong 20100413 增加采购单号 申报部门
				+ ",b.material_id ,d.pur_no \n"
				+ "  from INV_J_TRANSACTION_HIS       a,\n"
				+ "       inv_c_material              b,\n"
				+ "       PUR_J_ORDER                 c,\n"
				+ "       PUR_J_ARRIVAL               d\n"
				+ " where a.material_id = b.material_id\n"
				+ "   and a.order_no = c.pur_no\n"
				+ "   and d.arrival_no = '"
				+ arrivalNo
				+ "'\n"
				+ "   and a.from_whs_no = '"
				+ whsNo
				+ "'\n"
				+ "   and a.arrival_no=d.arrival_no \n"
				+ "   and a.trans_qty <> 0\n"
				+ // modify by ywliu 20100205 查询红单数据
				"   and  a.trans_qty +(select nvl(sum(b.trans_qty),0) from inv_j_transaction_his b where b.roll_back_id=a.trans_his_id)<>0 \n"
				+ // modify by ywliu 20100205 查询红单数据
				"   and a.last_modified_date =\n"
				+ "       to_date('"
				+ operateDate + "', 'yyyy-MM-dd hh24:mi:ss')";
		
		if(metailIdNotIn != null && !metailIdNotIn.equals("")){
			sql += " and b.material_id not in (" + metailIdNotIn + ") \n";
  		}

		List<Object> list = bll.queryByNativeSQL(sql);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();

		Double totalPrice = 0d;
		Double totalTax = 0d;
		Double totalInputTax = 0d;
		NumberFormat formatter = new DecimalFormat("0.00");
		NumberFormat formatter1 = new DecimalFormat("0.0000");
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setRecQty(Double.valueOf(data[0].toString()));
			}
			if (data[1] != null) {

				Double price = Double.valueOf(data[1].toString());
				// model.setUnitPrice(Double.valueOf(data[1].toString()));
				model.setStrUnitPrice(formatter1.format(price.doubleValue()));
			}
			if (data[2] != null) {
				model.setWhsName(data[2].toString());
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
				model.setStockUmIdName(data[7].toString());
			}
			if (data[8] != null) {
				model.setBuyerName(data[8].toString());
			}
			if (data[9] != null) {
				model.setInvoiceNo(data[9].toString());
			}
			if (data[11] != null)
				model.setSupplyName(data[11].toString());

			if (data[12] != null) {
				model.setStrTotalPrice(formatter.format(Double.valueOf(
						data[12].toString()).doubleValue()));
				totalPrice += Double.valueOf(data[12].toString());
				if (model.getTaxRate() != null) {
					Double inputTax = Double.parseDouble(data[12].toString())
							* Double.parseDouble(data[3].toString());
					model.setStrInputTax(formatter.format(inputTax
							.doubleValue()));
					totalInputTax += inputTax;
				}
			}
			if (data[13] != null) {
				model.setStrTotalTax(formatter.format(Double.valueOf(
						data[13].toString()).doubleValue()));
				totalTax += Double.valueOf(data[13].toString());
			}
			// add by ltong 20100413 增加申报部门
			if (data[15] != null) {
				model.setMaterialId(Long.parseLong(data[15].toString()));
			}
			if (data[16] != null) {
				model.setCGPurNo(data[16].toString());
				String ssSql = "select distinct b.memo, getdeptname(c.mr_dept)\n"
						+ "  from pur_j_plan_order              a,\n"
						+ "       mrp_j_plan_requirement_detail b,\n"
						+ "       mrp_j_plan_requirement_head   c\n"
						+ " where a.is_use = 'Y'\n"
						+ "   and a.enterprise_code = 'hfdc'\n"
						+ "   and b.material_id='"
						+ model.getMaterialId()
						+ "'\n"
						+ "   and a.pur_no = '"
						+ model.getCGPurNo()
						+ "'\n"
						+ "   and a.requirement_detail_id = b.requirement_detail_id\n"
						+ "   and b.requirement_head_id = c.requirement_head_id";
				List ssList = bll.queryByNativeSQL(ssSql);
				if (ssList != null && ssList.size() > 0) {
					Object[] ssdata = (Object[]) ssList.get(0);
					if (ssdata[0] != null)
						model.setSbMemo(ssdata[0].toString());
					if (ssdata[1] != null)
						model.setSbDeptName(ssdata[1].toString());
				}

			}
			arraylist.add(model);
		}
		PurchaseWarehouseDetailInfo entity = new PurchaseWarehouseDetailInfo();
		entity.setMaterialNo("合计");
		entity.setStrInputTax(formatter.format(totalInputTax));
		entity.setStrTotalPrice(formatter.format(totalPrice));
		entity.setStrTotalTax(formatter.format(totalTax));
		arraylist.add(entity);
		return arraylist;
	}

	public PageObject queryCheckedPurList(String sdate, String edate,
			String purNo, String buyer, String invoiceNo,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		String strWhere = "";
		String sql = "select distinct b.id,\n"
				+ "                b.pur_no,\n"
				+ "                b.supplier,\n"
				+ "                GETCLIENTNAME(b.supplier),\n"
				+ "                to_char(b.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss'),\n"
				+ "                b.contract_no,\n"
				+ "                b.invoice_no,\n"
				+ "                b.memo,\n"
				+ "                GETWORKERNAME(b.buyer),\n"
				+ "                (select sum(t.pur_qty * t.unit_price)\n"
				+ "                   from pur_j_order_details t\n"
				+ "                  where t.pur_no = b.pur_no and t.is_use='Y') as totalPrice\n"
				+ "\n" + "  from pur_j_order b, pur_j_arrival d\n"
				+ " where b.pur_no = d.pur_no\n" + "   and d.is_use = 'Y'\n"
				+ "   and d.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.is_use = 'Y'\n" + "   and (select count(*)\n"
				+ "          from pur_j_arrival c\n"
				+ "         where c.pur_no = b.pur_no\n"
				+ "           and c.check_state = '2'\n"
				+ "           and c.is_use = 'Y'\n"
				+ "           and c.enterprise_code = '" + enterpriseCode
				+ "') =\n" + "       (select count(*)\n"
				+ "          from pur_j_arrival cc\n"
				+ "         where cc.pur_no = b.pur_no\n"
				+ "           and cc.is_use = 'Y'\n"
				+ "           and cc.enterprise_code = '" + enterpriseCode
				+ "')\n";
		if (purNo != null && !purNo.equals("")) {
			strWhere = "   and b.pur_no like '%" + purNo.trim() + "%'\n";
		}

		if (buyer != null && !buyer.equals("")) {
			strWhere += "   and GETWORKERNAME(b.buyer) like '%" + buyer.trim()
					+ "%'\n";
		}
		if (invoiceNo != null && !invoiceNo.equals("")) {
			strWhere += "   and b.invoice_no like '%" + invoiceNo.trim()
					+ "%'\n";
		}
		if (sdate != null && !sdate.equals("")) {
			strWhere += "   and d.check_date >=\n" + "       to_date('" + sdate
					+ "' || ' 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n";
		}
		if (edate != null && !edate.equals("")) {
			strWhere += "   and d.check_date <=\n" + "   to_date('" + edate
					+ "' || ' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n";
		}
		sql = sql + strWhere;
		// sql+=" order by b.pur_no desc";
		String sqlCount = "select count(*) from (" + sql + ")tt";

		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());

		// 计算总共的合计
		if ((totalCount - rowStartIdxAndCount[0]) / rowStartIdxAndCount[1] < 1
				&& rowStartIdxAndCount[0] != 0) {
			sql += "union\n"
					+ " select  null,'总合计',null,null,null,null,null,null,null,nvl(sum(tt.totalPrice),0)\n"
					+ "from(" + sql + ")tt";

		}
		sql = "select * from (" + sql + ")ttt  order by ttt.pur_no asc ";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);

		Iterator it = list.iterator();
		Double price = 0d;
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			if (!data[1].toString().equals("总合计")) {
				if (data[9] != null) {
					price = price + Double.parseDouble(data[9].toString());
				}
			}
		}
		Object[] myData = new Object[] { null, "本页合计", null, null, null, null,
				null, null, null, price };
		if ((totalCount - rowStartIdxAndCount[0]) / rowStartIdxAndCount[1] < 1
				&& rowStartIdxAndCount[0] != 0) {
			list.add(list.size() - 1, myData);
		} else {
			list.add(myData);
		}

		PageObject obj = new PageObject();
		obj.setList(list);
		obj.setTotalCount(totalCount);
		return obj;

	}

	// add by liuyi 20100430 
	public List<PurchaseWarehouseDetailInfo> getReceiptGdMaterialInfo(
			String purNo, String whsNo, String arrivalNo, String materialId) {
		String sql = "select  a.trans_qty,a.price/(1+a.tax_rate),GETWHSNAME(a.from_whs_no) ,a.tax_rate,b.material_no,b.material_name ,b.spec_no,GETUNITNAME(a.trans_um_id) ,GETWORKERNAME(c.buyer), d.invoice_no,getclientname(d.supplier)\n"
				+ ",a.trans_qty*a.price/(1+a.tax_rate),a.trans_qty*a.price,a.price \n"
				+ ",b.material_id ,d.pur_no \n"
				+ "from INV_J_TRANSACTION_HIS a,inv_c_material b ,PUR_J_ORDER c ,PUR_J_ARRIVAL d \n"
				+ "where a.material_id = b.material_id and a.order_no = c.pur_no and  a.order_no = '"
				+ purNo
				+ "'\n  and a.from_whs_no = '"
				+ whsNo
				+ "'"
				+ " and d.arrival_no='"
				+ arrivalNo
				+ "'"
				+ " and a.trans_qty >0"
				+ " and a.last_modified_date >= "
				+ "  (select max(t.last_modified_date) - 2/24/3600 from pur_j_arrival_details  t where t.mif_no='"
				+ arrivalNo
				+ "' and t.pur_no = '"
				+ purNo
				+ "')"
				+ "  and a.last_modified_date <= "
				+ " (select max(t.last_modified_date) + 2/24/3600 from pur_j_arrival_details  t where t.mif_no='"
				+ arrivalNo + "' and t.pur_no = '" + purNo + "')";
		
		// add by liuyi 20100430 计划来源为固定资产类的物资
		if(materialId != null && !materialId.equals("")){
			sql += " and b.material_id ="+materialId+" \n";
		}
		List<Object> list = bll.queryByNativeSQL(sql);
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();
		Iterator it = list.iterator();
		Double totalPrice = 0d;
		Double totalTax = 0d;
		Double totalInputTax = 0d;
		NumberFormat formatter = new DecimalFormat("0.00");
		NumberFormat formatter1 = new DecimalFormat("0.0000");

		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
			if (data[0] != null) {
				model.setRecQty(Double.valueOf(data[0].toString()));
			}
			if (data[1] != null) {
				Double price = Double.valueOf(data[1].toString());
				model.setStrUnitPrice(formatter1.format(price.doubleValue()));
			}
			if (data[2] != null) {
				model.setWhsName(data[2].toString());
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
				model.setStockUmIdName(data[7].toString());
			}
			if (data[8] != null) {
				model.setBuyerName(data[8].toString());
			}
			if (data[9] != null) {
				model.setInvoiceNo(data[9].toString());
			}
			if (data[10] != null)
				model.setSupplyName(data[10].toString());
			if (data[11] != null) {
				model.setStrTotalPrice(formatter.format(Double.valueOf(
						data[11].toString()).doubleValue()));
				totalPrice += Double.valueOf(data[11].toString());
				if (model.getTaxRate() != null) {
					Double inputTax = Double.parseDouble(data[11].toString())
							* Double.parseDouble(data[3].toString());
					model.setStrInputTax(formatter.format(inputTax
							.doubleValue()));
					totalInputTax += inputTax;
				}
			}
			if (data[12] != null) {
				model.setStrTotalTax(formatter.format(Double.valueOf(
						data[12].toString()).doubleValue()));
				totalTax += Double.valueOf(data[12].toString());
			}
			if (data[14] != null) {
				model.setMaterialId(Long.parseLong(data[14].toString()));
			}
			if (data[15] != null) {
				model.setCGPurNo(data[15].toString());
				String ssSql = "select distinct b.memo, getdeptname(c.mr_dept)\n"
						+ "  from pur_j_plan_order              a,\n"
						+ "       mrp_j_plan_requirement_detail b,\n"
						+ "       mrp_j_plan_requirement_head   c\n"
						+ " where a.is_use = 'Y'\n"
						+ "   and a.enterprise_code = 'hfdc'\n"
						+ "   and b.material_id='"
						+ model.getMaterialId()
						+ "'\n"
						+ "   and a.pur_no = '"
						+ model.getCGPurNo()
						+ "'\n"
						+ "   and a.requirement_detail_id = b.requirement_detail_id\n"
						+ "   and b.requirement_head_id = c.requirement_head_id";
				List ssList = bll.queryByNativeSQL(ssSql);
				if (ssList != null && ssList.size() > 0) {
					Object[] ssdata = (Object[]) ssList.get(0);
					if (ssdata[0] != null)
						model.setSbMemo(ssdata[0].toString());
					if (ssdata[1] != null)
						model.setSbDeptName(ssdata[1].toString());
				}

			}
			arraylist.add(model);
		}

		PurchaseWarehouseDetailInfo entity = new PurchaseWarehouseDetailInfo();
		entity.setMaterialNo("合计");
		entity.setStrInputTax(formatter.format(totalInputTax));
		entity.setStrTotalPrice(formatter.format(totalPrice));
		entity.setStrTotalTax(formatter.format(totalTax));
		arraylist.add(entity);

		return arraylist;

	
	}

	public List<PurchaseWarehouseDetailInfo> getAfterReceiptGdMaterialInfo(
			String operateDate, String arrivalNo, String whsNo,
			String materialId) {
		String sql = "select a.trans_qty +(select nvl(sum(b.trans_qty),0) from inv_j_transaction_his b where b.roll_back_id=a.trans_his_id),\n"
			+ // modify by ywliu 20100223
			"       a.price/(1+a.tax_rate),\n"
			+ "       GETWHSNAME(a.from_whs_no),\n"
			+ "       a.tax_rate,\n"
			+ "       b.material_no,\n"
			+ "       b.material_name,\n"
			+ "       b.spec_no,\n"
			+ "       GETUNITNAME(a.trans_um_id),\n"
			+ "       GETWORKERNAME(c.buyer),\n"
			+ "       d.invoice_no,\n"
			+ "       to_char(a.last_modified_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
			+ "       getclientname(d.supplier)\n"
			+ ",a.trans_qty*a.price/(1+a.tax_rate),a.trans_qty*a.price,a.price \n"
			// add by ltong 20100413 增加采购单号 申报部门
			+ ",b.material_id ,d.pur_no \n"
			+ "  from INV_J_TRANSACTION_HIS       a,\n"
			+ "       inv_c_material              b,\n"
			+ "       PUR_J_ORDER                 c,\n"
			+ "       PUR_J_ARRIVAL               d\n"
			+ " where a.material_id = b.material_id\n"
			+ "   and a.order_no = c.pur_no\n"
			+ "   and d.arrival_no = '"
			+ arrivalNo
			+ "'\n"
			+ "   and a.from_whs_no = '"
			+ whsNo
			+ "'\n"
			+ "   and a.arrival_no=d.arrival_no \n"
			+ "   and a.trans_qty <> 0\n"
			+ // modify by ywliu 20100205 查询红单数据
			"   and  a.trans_qty +(select nvl(sum(b.trans_qty),0) from inv_j_transaction_his b where b.roll_back_id=a.trans_his_id)<>0 \n"
			+ // modify by ywliu 20100205 查询红单数据
			"   and a.last_modified_date =\n"
			+ "       to_date('"
			+ operateDate + "', 'yyyy-MM-dd hh24:mi:ss')";
	
	if(materialId != null && !materialId.equals("")){
		sql += " and b.material_id  =" + materialId + " \n";
		}

	List<Object> list = bll.queryByNativeSQL(sql);
	List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList<PurchaseWarehouseDetailInfo>();

	Double totalPrice = 0d;
	Double totalTax = 0d;
	Double totalInputTax = 0d;
	NumberFormat formatter = new DecimalFormat("0.00");
	NumberFormat formatter1 = new DecimalFormat("0.0000");
	Iterator it = list.iterator();
	while (it.hasNext()) {
		Object[] data = (Object[]) it.next();
		PurchaseWarehouseDetailInfo model = new PurchaseWarehouseDetailInfo();
		if (data[0] != null) {
			model.setRecQty(Double.valueOf(data[0].toString()));
		}
		if (data[1] != null) {

			Double price = Double.valueOf(data[1].toString());
			// model.setUnitPrice(Double.valueOf(data[1].toString()));
			model.setStrUnitPrice(formatter1.format(price.doubleValue()));
		}
		if (data[2] != null) {
			model.setWhsName(data[2].toString());
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
			model.setStockUmIdName(data[7].toString());
		}
		if (data[8] != null) {
			model.setBuyerName(data[8].toString());
		}
		if (data[9] != null) {
			model.setInvoiceNo(data[9].toString());
		}
		if (data[11] != null)
			model.setSupplyName(data[11].toString());

		if (data[12] != null) {
			model.setStrTotalPrice(formatter.format(Double.valueOf(
					data[12].toString()).doubleValue()));
			totalPrice += Double.valueOf(data[12].toString());
			if (model.getTaxRate() != null) {
				Double inputTax = Double.parseDouble(data[12].toString())
						* Double.parseDouble(data[3].toString());
				model.setStrInputTax(formatter.format(inputTax
						.doubleValue()));
				totalInputTax += inputTax;
			}
		}
		if (data[13] != null) {
			model.setStrTotalTax(formatter.format(Double.valueOf(
					data[13].toString()).doubleValue()));
			totalTax += Double.valueOf(data[13].toString());
		}
		// add by ltong 20100413 增加申报部门
		if (data[15] != null) {
			model.setMaterialId(Long.parseLong(data[15].toString()));
		}
		if (data[16] != null) {
			model.setCGPurNo(data[16].toString());
			String ssSql = "select distinct b.memo, getdeptname(c.mr_dept)\n"
					+ "  from pur_j_plan_order              a,\n"
					+ "       mrp_j_plan_requirement_detail b,\n"
					+ "       mrp_j_plan_requirement_head   c\n"
					+ " where a.is_use = 'Y'\n"
					+ "   and a.enterprise_code = 'hfdc'\n"
					+ "   and b.material_id='"
					+ model.getMaterialId()
					+ "'\n"
					+ "   and a.pur_no = '"
					+ model.getCGPurNo()
					+ "'\n"
					+ "   and a.requirement_detail_id = b.requirement_detail_id\n"
					+ "   and b.requirement_head_id = c.requirement_head_id";
			List ssList = bll.queryByNativeSQL(ssSql);
			if (ssList != null && ssList.size() > 0) {
				Object[] ssdata = (Object[]) ssList.get(0);
				if (ssdata[0] != null)
					model.setSbMemo(ssdata[0].toString());
				if (ssdata[1] != null)
					model.setSbDeptName(ssdata[1].toString());
			}

		}
		arraylist.add(model);
	}
	PurchaseWarehouseDetailInfo entity = new PurchaseWarehouseDetailInfo();
	entity.setMaterialNo("合计");
	entity.setStrInputTax(formatter.format(totalInputTax));
	entity.setStrTotalPrice(formatter.format(totalPrice));
	entity.setStrTotalTax(formatter.format(totalTax));
	arraylist.add(entity);
	return arraylist;
}

}
