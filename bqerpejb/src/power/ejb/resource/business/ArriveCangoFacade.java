/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.form.ArriveCangoDetailInfo;

import com.sun.org.apache.bcel.internal.Constants;

/**
 * 到货登记implements
 * 
 * @author zhaozhijie
 */
@Stateless
public class ArriveCangoFacade implements ArriveCangoFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 到货登记remote */
	@EJB(beanName = "PurJArrivalFacade")
	protected PurJArrivalFacadeRemote arrivalRemote;
	/** 到货登记明细remote */
	@EJB(beanName = "PurJArrivalDetailsFacade")
	protected PurJArrivalDetailsFacadeRemote arrivalDetailsRemote;
	/** 采购单明细remote */
	@EJB(beanName = "PurJOrderDetailsFacade")
	protected PurJOrderDetailsFacadeRemote purJOrderDetailsFacadeRemote;
	/** 事务历史remote */
	@EJB(beanName = "InvJTransactionHisFacade")
	protected InvJTransactionHisFacadeRemote invJTransactionHisFacadeRemote;

	/**
	 * 从采购单查询物资详细单
	 * 
	 * @param rowStartIdxAndCount
	 *            动态参数(开始行数和查询行数)
	 * @return PageObject 物资详细单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findMaterialDetails(String purNo, String enterpriseCode,
			final int... rowStartIdxAndCount) throws ParseException {
		try {
			StringBuilder sbd = new StringBuilder();
			StringBuilder sbdu = new StringBuilder();
			PageObject pobj = new PageObject();
			// SQL语句连接//modify by fyyang 090511 增加了是否免检
			sbd.append("SELECT ");
			sbd
					.append("A.PUR_ORDER_DETAILS_ID, A.MATERIAL_ID, B.MATERIAL_NO, ");
			sbd
					.append("B.MATERIAL_NAME, B.SPEC_NO, B.STOCK_UM_ID, A.PUR_QTY,B.qa_control_flag  ");
			sbd.append("FROM PUR_J_ORDER_DETAILS A, INV_C_MATERIAL B ");
			sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' ");
			sbd.append("AND A.PUR_NO = '");
			sbd.append(purNo);
			sbd.append("' AND B.MATERIAL_ID = A.MATERIAL_ID ");
			sbd.append("AND A.ENTERPRISE_CODE = '");
			sbd.append(enterpriseCode);
			sbd.append("' AND B.ENTERPRISE_CODE = '");
			sbd.append(enterpriseCode);
			sbd.append("'");

			// SQL语句连接
			sbdu.append("SELECT ");
			sbdu
					.append("SUM(B.RCV_QTY) AS RCV_QTY, A.MATERIAL_ID, A.PUR_ORDER_DETAILS_ID ");
			sbdu
					.append(" FROM PUR_J_ORDER_DETAILS A, PUR_J_ARRIVAL_DETAILS B ");
			sbdu.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' ");
			sbdu.append("AND B.PUR_NO = '");
			sbdu.append(purNo);
			sbdu.append("' AND B.PUR_LINE = A.PUR_ORDER_DETAILS_ID ");
			sbdu.append("AND A.ENTERPRISE_CODE = '");
			sbdu.append(enterpriseCode);
			sbdu.append("' AND B.ENTERPRISE_CODE = '");
			sbdu.append(enterpriseCode);
			sbdu.append("'");
			sbdu.append("GROUP BY A.MATERIAL_ID,A.PUR_ORDER_DETAILS_ID");

			List<ArriveCangoDetailInfo> lst = bll.queryByNativeSQL(sbd
					.toString(), rowStartIdxAndCount);
			List<ArriveCangoDetailInfo> list = bll.queryByNativeSQL(sbdu
					.toString(), rowStartIdxAndCount);

			List<ArriveCangoDetailInfo> arrlist = new ArrayList<ArriveCangoDetailInfo>();
			if (lst != null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					String datas = data[0].toString();
					double purQty = 0;
					double rcvQty = 0;
					ArriveCangoDetailInfo arriveInfo = new ArriveCangoDetailInfo();
					// 流水号
					if (null != data[0]) {
						arriveInfo.setId(Long.parseLong(data[0].toString()));
					}
					// 物料ID
					if (null != data[1]) {
						arriveInfo.setMaterialID(Long.parseLong(data[1]
								.toString()));
					}
					// 物资编码
					if (null != data[2]) {
						arriveInfo.setMaterialNo(data[2].toString());
					}
					// 物资名称
					if (null != data[3]) {
						arriveInfo.setMaterialName(data[3].toString());
					}
					// 规格型号
					if (null != data[4]) {
						arriveInfo.setSpecNo(data[4].toString());
					}
					// 存货计量单位
					if (null != data[5]) {
						arriveInfo.setPurUm(Long.parseLong(data[5].toString()));
					}
					// 采购数
					if (null != data[6]) {
						purQty = Double.parseDouble(data[6].toString());
						arriveInfo.setPurQty(purQty);
					}

					// add by fyyang 090511 是否免检
					if (data[7] != null) {
						arriveInfo.setQaControlFlag(data[7].toString());
					}
					// 到货数
					arriveInfo.setTheQty(Double.parseDouble("0"));
					if (list != null) {
						Iterator iti = list.iterator();
						while (iti.hasNext()) {
							Object[] datai = (Object[]) iti.next();
							String datasi = datai[2].toString();

							if (datas.equals(datasi)) {
								// 已收数量
								if (null != datai[0]) {
									rcvQty = Double.parseDouble(datai[0]
											.toString());
									arriveInfo.setRcvQty(rcvQty);
								}
							}
						}
					}
					// 待收数
					arriveInfo.setInsQty(purQty - rcvQty);
					// add by liuyi 20100406 需求备注 申报部门
					String strSql = "select distinct b.memo, getdeptname(c.mr_dept)\n"
							+ "  from pur_j_plan_order              a,\n"
							+ "       mrp_j_plan_requirement_detail b,\n"
							+ "       mrp_j_plan_requirement_head   c\n"
							+ " where a.is_use = 'Y'\n"
							+ "   and a.enterprise_code = 'hfdc'\n"
							+ "   and a.pur_order_details_id = '"
							+ arriveInfo.getId()
							+ "'\n"
							+ "   and a.pur_no = '"
							+ purNo
							+ "'\n"
							+ "   and a.requirement_detail_id = b.requirement_detail_id\n"
							+ "   and b.requirement_head_id = c.requirement_head_id";
					List strList = bll.queryByNativeSQL(strSql);
					if (strList != null && strList.size() > 0) {
						Object[] strdata = (Object[]) strList.get(0);
						if (strdata[0] != null)
							arriveInfo.setSbMemo(strdata[0].toString());
						if (strdata[1] != null)
							arriveInfo.setSbDeptName(strdata[1].toString());
					}
					// add by ltong 2010/04/12到货数为0时 默认为null
					if (arriveInfo.getTheQty() == 0.0)
						arriveInfo.setTheQty(null);
					if (purQty - rcvQty > 0) {
						// 待收数>0的显示，其他不显示 add by fyyang 090519
						arrlist.add(arriveInfo);
					}
				}
			}

			if (arrlist.size() > 0) {
				// 符合条件的物资详细单
				pobj.setList(arrlist);
				// 符合条件的物资详细单的总数
				pobj.setTotalCount(Long.parseLong(arrlist.size() + ""));
			}
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}

	}

	/**
	 * 从到货单查询物资详细单
	 * 
	 * @param rowStartIdxAndCount
	 *            动态参数(开始行数和查询行数)
	 * @return PageObject 物资详细单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAMaterialDetails(Long id, String enterpriseCode,
			final int... rowStartIdxAndCount) throws ParseException {
		try {
			StringBuilder sbd = new StringBuilder();

			PageObject pobj = new PageObject();
			// SQL语句连接 //modify by fyyang 090511 增加了是否免检
			sbd.append("SELECT ");
			sbd
					.append("B.MATERIAL_ID, C.MATERIAL_NO, C.MATERIAL_NAME, C.STOCK_UM_ID, C.SPEC_NO, ");
			sbd
					.append("D.CLIENT_NAME, A.CONTRACT_NO, B.PUR_NO, A.MEMO, B.THE_QTY, B.MEMO AS DETAILMEMO, ");
			sbd
					.append("A.ID AS IDA, B.ID AS IDB, B.LOT_CODE, D.CLIEND_ID,C.qa_control_flag,A.invoice_no   ");
			sbd
					.append("FROM PUR_J_ARRIVAL A, PUR_J_ARRIVAL_DETAILS B, INV_C_MATERIAL C, CON_J_CLIENTS_INFO D ");
			sbd.append("WHERE B.IS_USE = 'Y' AND C.IS_USE = 'Y' ");
			sbd.append("AND A.ID = '");
			sbd.append(id);
			sbd
					.append("' AND C.MATERIAL_ID = B.MATERIAL_ID AND A.SUPPLIER = D.CLIEND_ID ");
			sbd.append("AND A.ARRIVAL_NO = B.MIF_NO ");
			sbd.append("AND A.ENTERPRISE_CODE = '");
			sbd.append(enterpriseCode);
			sbd.append("' AND B.ENTERPRISE_CODE = '");
			sbd.append(enterpriseCode);
			sbd.append("' AND C.ENTERPRISE_CODE = '");
			sbd.append(enterpriseCode);
			sbd.append("' AND D.ENTERPRISE_CODE = '");
			sbd.append(enterpriseCode);
			sbd.append("'");

			List<ArriveCangoDetailInfo> lst = bll.queryByNativeSQL(sbd
					.toString(), rowStartIdxAndCount);
			List<ArriveCangoDetailInfo> arrlist = new ArrayList<ArriveCangoDetailInfo>();
			Iterator it = lst.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				double purQty = 0;
				double rcvQty = 0;
				double theQty = 0;
				String datas = data[0].toString();
				ArriveCangoDetailInfo arriveInfo = new ArriveCangoDetailInfo();

				if (id != null) {
					arriveInfo.setArrivalId(id);
				}
				// 物料ID
				if (null != data[0]) {
					arriveInfo
							.setMaterialID(Long.parseLong(data[0].toString()));
				}
				// 物资编码
				if (null != data[1]) {
					arriveInfo.setMaterialNo(data[1].toString());
				}
				// 物资名称
				if (null != data[2]) {
					arriveInfo.setMaterialName(data[2].toString());
				}
				// 存货计量单位
				if (null != data[3]) {
					arriveInfo.setPurUm(Long.parseLong(data[3].toString()));
				}
				// 规格型号
				if (null != data[4]) {
					arriveInfo.setSpecNo(data[4].toString());
				}
				// 供应商名称
				if (null != data[5]) {
					arriveInfo.setSupplier(data[5].toString());
				}
				// 合同编号
				if (null != data[6]) {
					arriveInfo.setContract(data[6].toString());
				}
				// 采购单号
				if (null != data[7]) {
					arriveInfo.setPurNo(data[7].toString());
					// add by liuyi 20100406 采购单存在时查询该采购对应的需求计划的备注和申报部门
					String ssSql = "select distinct b.memo, getdeptname(c.mr_dept)\n"
							+ "  from pur_j_plan_order              a,\n"
							+ "       mrp_j_plan_requirement_detail b,\n"
							+ "       mrp_j_plan_requirement_head   c\n"
							+ " where a.is_use = 'Y'\n"
							+ "   and a.enterprise_code = 'hfdc'\n"
							+ "   and b.material_id='"
							+ arriveInfo.getMaterialID()
							+ "'\n"
							+ "   and a.pur_no = '"
							+ arriveInfo.getPurNo()
							+ "'\n"
							+ "   and a.requirement_detail_id = b.requirement_detail_id\n"
							+ "   and b.requirement_head_id = c.requirement_head_id";
					List ssList = bll.queryByNativeSQL(ssSql);
					if (ssList != null && ssList.size() > 0) {
						Object[] ssdata = (Object[]) ssList.get(0);
						if (ssdata[0] != null)
							arriveInfo.setSbMemo(ssdata[0].toString());
						if (ssdata[1] != null)
							arriveInfo.setSbDeptName(ssdata[1].toString());
					}

				}
				// 备注
				if (null != data[8]) {
					arriveInfo.setMemo(data[8].toString());
				}
				// 到货数
				if (null != data[9]) {
					theQty = Double.parseDouble(data[9].toString());
					arriveInfo.setTheQty(theQty);
				}
				// 明细备注
				if (null != data[10]) {
					arriveInfo.setDetailMemo(data[10].toString());
				}
				// 到货/验收表ID
				if (null != data[11]) {
					arriveInfo
							.setArrivalID(Long.parseLong(data[11].toString()));
				}
				// 到货/验收明细表ID
				if (null != data[12]) {
					arriveInfo.setArrivalDID(Long
							.parseLong(data[12].toString()));
				}
				// 批号
				if (null != data[13]) {
					arriveInfo.setLotCode(data[13].toString());
				}
				// 合作伙伴id
				if (null != data[14]) {
					arriveInfo.setSupplierId(Long
							.parseLong(data[14].toString()));
				}
				if (data[15] != null) {
					// add by fyyang 090511 是否免检
					arriveInfo.setQaControlFlag(data[15].toString());
				}
				if (data[16] != null) {
					// add by fyyang 091109 发票号
					arriveInfo.setInvoiceNo(data[16].toString());
				}

				StringBuilder sbdu = new StringBuilder();
				// SQL语句连接
				sbdu.append("SELECT ");
				sbdu
						.append("SUM(A.RCV_QTY) AS RCV_QTY, B.PUR_ORDER_DETAILS_ID, B.MATERIAL_ID, B.PUR_QTY ");
				sbdu
						.append("FROM PUR_J_ARRIVAL_DETAILS A, PUR_J_ORDER_DETAILS B ");
				sbdu.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' ");
				sbdu.append("AND A.ENTERPRISE_CODE = '");
				sbdu.append(enterpriseCode);
				sbdu.append("' AND B.ENTERPRISE_CODE = '");
				sbdu.append(enterpriseCode);
				sbdu.append("'");
				sbdu.append(" AND A.PUR_LINE = B.PUR_ORDER_DETAILS_ID ");
				sbdu
						.append(" AND B.PUR_ORDER_DETAILS_ID = (SELECT A.PUR_LINE  ");
				sbdu.append("FROM PUR_J_ARRIVAL_DETAILS A WHERE A.ID = '");
				sbdu.append(Long.parseLong(data[12].toString()));
				sbdu.append("')");
				sbdu
						.append(" GROUP BY B.PUR_ORDER_DETAILS_ID, B.MATERIAL_ID, B.PUR_QTY ");

				List<ArriveCangoDetailInfo> list = bll.queryByNativeSQL(sbdu
						.toString(), rowStartIdxAndCount);
				Iterator iti = list.iterator();
				while (iti.hasNext()) {
					Object[] datai = (Object[]) iti.next();
					// 已收数量
					if (null != datai[0]) {
						rcvQty = Double.parseDouble(datai[0].toString());
						arriveInfo.setRcvQty(rcvQty);
					}
					// 流水号
					if (null != datai[1]) {
						arriveInfo.setId(Long.parseLong(datai[1].toString()));
					}
					// 采购数
					if (null != datai[3]) {
						purQty = Double.parseDouble(datai[3].toString());
						arriveInfo.setPurQty(purQty);
					}
					// 待收数
					if (null != datai[3] && null != datai[0]) {
						arriveInfo.setInsQty(purQty - rcvQty);
					}

					// add by ltong 2010/04/12到货数为0时 默认为null
					if (arriveInfo.getTheQty() == 0.0)
						arriveInfo.setTheQty(null);
					arrlist.add(arriveInfo);
				}

			}
			if (arrlist.size() > 0) {
				// 符合条件的物资详细单
				pobj.setList(arrlist);
				// 符合条件的物资详细单的总数
				// modify by fyyang 090507
				pobj.setTotalCount(Long.parseLong(arrlist.size() + ""));
			}
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}

	}

	/**
	 * 登记tab数据检索
	 * 
	 * @param operateBy
	 *            操作人
	 * @param rowStartIdxAndCount
	 *            动态参数(开始行数和查询行数)
	 * @return PageObject 登记单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getArriveTabData(String operateBy, String enterpriseCode,
			final int... rowStartIdxAndCount) throws ParseException {
		try {
			StringBuilder sbd = new StringBuilder();
			PageObject pobj = new PageObject();
			// SQL语句连接
			sbd.append("SELECT ");
			sbd
					.append("A.ARRIVAL_NO AS MIF_NO, A.SUPPLIER, GETCLIENTNAME(A.SUPPLIER) AS SUPPLY, ");
			sbd.append("A.LAST_MODIFIED_DATE AS SDATE, A.ID ");
			sbd.append("FROM PUR_J_ARRIVAL A ");
			sbd.append("WHERE A.IS_USE = 'Y' ");
			sbd.append("AND A.ARRIVAL_STATE = '0' ");
			sbd.append("AND A.LAST_MODIFIED_BY = '");
			sbd.append(operateBy);
			sbd.append("' AND A.ENTERPRISE_CODE = '");
			sbd.append(enterpriseCode);
			sbd.append("'order by A.ARRIVAL_NO DESC");// 增加将查询结果按降序排列 modify
			// by ywliu 2009/7/2

			StringBuilder sqlCount = new StringBuilder();
			// SQL语句连接 查询所有记录
			sqlCount.append("SELECT ");
			sqlCount.append("count(*)");
			sqlCount.append("FROM PUR_J_ARRIVAL A ");
			sqlCount.append("WHERE A.IS_USE = 'Y' ");
			sqlCount.append("AND A.ARRIVAL_STATE = '0' ");
			sqlCount.append("AND A.LAST_MODIFIED_BY = '");
			sqlCount.append(operateBy);
			sqlCount.append("' AND A.ENTERPRISE_CODE = '");
			sqlCount.append(enterpriseCode);
			sqlCount.append("'");

			List<ArriveCangoDetailInfo> lst = bll.queryByNativeSQL(sbd
					.toString(), rowStartIdxAndCount);
			List<ArriveCangoDetailInfo> arrlist = new ArrayList<ArriveCangoDetailInfo>();
			if (lst != null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					ArriveCangoDetailInfo arriveInfo = new ArriveCangoDetailInfo();
					Object[] data = (Object[]) it.next();
					// 到货单号
					if (null != data[0]) {
						arriveInfo.setMifNo(data[0].toString());
					}
					// 供应商id
					if (null != data[1]) {
						arriveInfo.setSupplierId(Long.parseLong(data[1]
								.toString()));
					}
					// 供应商
					if (null != data[2]) {
						arriveInfo.setSupplier(data[2].toString());
					}
					// 日期
					if (null != data[3]) {
						SimpleDateFormat sdfFrom = new SimpleDateFormat(
								"yyyy-MM-dd");
						arriveInfo.setDate(sdfFrom.parse(data[3].toString()));
					}
					// 流水号
					if (null != data[4]) {
						arriveInfo.setArrivalId(Long.parseLong(data[4]
								.toString()));
					}
					arrlist.add(arriveInfo);
				}
				if (arrlist.size() > 0) {
					// 符合条件的采购单
					pobj.setList(arrlist);
					// 符合条件的采购单的总数
					// modifyBy ywliu 09/05/05
					Long totalCount = Long.parseLong(bll.getSingal(
							sqlCount.toString()).toString());
					pobj.setTotalCount(totalCount);
				}
			}
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 模糊查询
	 * 
	 * @param queryString
	 *            采货单号/供应商/物资名称
	 * @param rowStartIdxAndCount
	 *            动态参数(开始行数和查询行数)
	 * @return PageObject 采购单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getstockData(String queryString, String enterpriseCode,
			final int... rowStartIdxAndCount) throws ParseException {
		try {
			StringBuilder sbd = new StringBuilder();
			PageObject pobj = new PageObject();
			if (queryString != null && !queryString.equals("")) {
				// SQL语句连接 modify by ywliu 2009/6/29
				sbd.append("SELECT DISTINCT ");
				sbd.append("A.PUR_NO, C.CLIENT_NAME, A.RELEASE_DATE, ");
				sbd.append("A.CONTRACT_NO, A.MEMO,C.CLIEND_ID ");
				sbd
						.append("FROM PUR_J_ORDER A, PUR_J_ORDER_DETAILS B, CON_J_CLIENTS_INFO C, INV_C_MATERIAL D ");
				sbd
						.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y' AND D.IS_USE = 'Y' ");
				sbd.append("AND A.SUPPLIER = C.CLIEND_ID ");
				sbd.append("AND (A.PUR_NO LIKE '%");
				sbd.append(queryString);
				sbd.append("%' OR C.CLIENT_CODE LIKE '%");
				sbd.append(queryString);
				sbd.append("%' OR C.CLIENT_NAME LIKE '%");
				sbd.append(queryString);
				sbd.append("%' OR D.MATERIAL_NO LIKE '%");
				sbd.append(queryString);
				sbd.append("%' OR D.MATERIAL_NAME LIKE '%");
				sbd.append(queryString);
				sbd.append("%') ");
				sbd
						.append("AND A.PUR_NO = B.PUR_NO AND B.MATERIAL_ID = D.MATERIAL_ID ");
				sbd
						.append("AND B.PUR_QTY - B.RCV_QTY - B.INS_QTY > 0 AND A.PUR_STATUS = '2' ");
				sbd.append("AND A.ENTERPRISE_CODE = '");
				sbd.append(enterpriseCode);
				sbd.append("' AND B.ENTERPRISE_CODE = '");
				sbd.append(enterpriseCode);
				sbd.append("' AND C.ENTERPRISE_CODE = '");
				sbd.append(enterpriseCode);
				sbd.append("' AND D.ENTERPRISE_CODE = '");
				sbd.append(enterpriseCode);
				sbd.append("'");
				sbd.append(" order by A.PUR_NO DESC");// 增加将查询结果按降序排列 modify
				// by ywliu 2009/7/2
			} else {
				// SQL语句连接 modify by ywliu 2009/6/29
				sbd.append("SELECT DISTINCT ");
				sbd
						.append("A.PUR_NO, GETCLIENTNAME(A.Supplier), A.RELEASE_DATE, ");
				sbd.append("A.CONTRACT_NO, A.MEMO,A.Supplier ");
				sbd.append("FROM PUR_J_ORDER A, PUR_J_ORDER_DETAILS B ");
				sbd.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y'");
				sbd.append("AND A.PUR_NO = B.PUR_NO ");
				sbd.append("AND B.PUR_QTY - B.RCV_QTY - B.INS_QTY > 0 ");
				sbd.append(" AND A.PUR_STATUS = '2' ");
				sbd.append("AND A.ENTERPRISE_CODE = '");
				sbd.append(enterpriseCode);
				sbd.append("' AND B.ENTERPRISE_CODE = '");
				sbd.append(enterpriseCode);
				sbd.append("'");
				sbd.append(" order by A.PUR_NO DESC");// 增加将查询结果按降序排列 modify
				// by ywliu 2009/7/2
			}
			
			StringBuilder sqlCount = new StringBuilder();
			if (queryString != null && !queryString.equals("")) {
				// SQL语句连接
				sqlCount.append("SELECT COUNT(*) ");
				// sqlCount.append("FROM (SELECT DISTINCT A.PUR_NO");
				// 5/8/09 yiliu modify by ywliu 2009/6/29
				sqlCount.append("FROM (SELECT DISTINCT A.PUR_NO ");
				sqlCount
						.append("FROM PUR_J_ORDER A, PUR_J_ORDER_DETAILS B, CON_J_CLIENTS_INFO C, INV_C_MATERIAL D ");
				sqlCount
						.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y'  AND D.IS_USE = 'Y' ");
				sqlCount.append("AND A.SUPPLIER = C.CLIEND_ID ");
				sqlCount.append("AND (A.PUR_NO LIKE '%");
				sqlCount.append(queryString);
				sqlCount.append("%' OR C.CLIENT_CODE LIKE '%");
				sqlCount.append(queryString);
				sqlCount.append("%' OR C.CLIENT_NAME LIKE '%");
				sqlCount.append(queryString);
				sqlCount.append("%' OR D.MATERIAL_NO LIKE '%");
				sqlCount.append(queryString);
				sqlCount.append("%' OR D.MATERIAL_NAME LIKE '%");
				sqlCount.append(queryString);
				sqlCount.append("%') ");
				sqlCount
						.append("AND A.PUR_NO = B.PUR_NO AND B.MATERIAL_ID = D.MATERIAL_ID ");
				sqlCount
						.append("AND B.PUR_QTY - B.RCV_QTY - B.INS_QTY > 0 AND A.PUR_STATUS = '2' ");
				sqlCount.append("AND A.ENTERPRISE_CODE = '");
				sqlCount.append(enterpriseCode);
				sqlCount.append("' AND B.ENTERPRISE_CODE = '");
				sqlCount.append(enterpriseCode);
				sqlCount.append("' AND C.ENTERPRISE_CODE = '");
				sqlCount.append(enterpriseCode);
				sqlCount.append("' AND D.ENTERPRISE_CODE = '");
				sqlCount.append(enterpriseCode);
				sqlCount.append("')");
			} else {
				// SQL语句连接 modify by ywliu 2009/6/29
				sqlCount.append("SELECT COUNT(*) ");
				sqlCount.append("FROM (SELECT DISTINCT A.PUR_NO ");
				sqlCount.append("FROM PUR_J_ORDER A, PUR_J_ORDER_DETAILS B ");
				sqlCount.append("WHERE A.IS_USE = 'Y' AND B.IS_USE = 'Y'");
				sqlCount.append("AND A.PUR_NO = B.PUR_NO ");
				sqlCount.append("AND B.PUR_QTY - B.RCV_QTY - B.INS_QTY > 0 ");
				sqlCount.append("AND A.PUR_STATUS = '2' ");
				sqlCount.append("AND A.ENTERPRISE_CODE = '");
				sqlCount.append(enterpriseCode);
				sqlCount.append("' AND B.ENTERPRISE_CODE = '");
				sqlCount.append(enterpriseCode);
				sqlCount.append("')");

			}
			List<ArriveCangoDetailInfo> lst = bll.queryByNativeSQL(sbd
					.toString(), rowStartIdxAndCount);
			List<ArriveCangoDetailInfo> arrlist = new ArrayList<ArriveCangoDetailInfo>();
			Iterator it = lst.iterator();
			while (it.hasNext()) {
				ArriveCangoDetailInfo arriveInfo = new ArriveCangoDetailInfo();
				Object[] data = (Object[]) it.next();
				// 采购单号
				if (null != data[0]) {
					arriveInfo.setPurNo(data[0].toString());
				}
				// 供应商
				if (null != data[1]) {
					arriveInfo.setSupplier(data[1].toString());
				}
				// 日期
				if (null != data[2]) {
					SimpleDateFormat sdfFrom = new SimpleDateFormat(
							"yyyy-MM-dd");
					arriveInfo.setDate(sdfFrom.parse(data[2].toString()));
				}
				// 合同号
				if (null != data[3]) {
					arriveInfo.setContract(data[3].toString());
				}
				// 备注
				if (null != data[4]) {
					arriveInfo.setMemo(data[4].toString());
				}
				// 供应商编码
				if (null != data[5]) {
					arriveInfo.setDetailMemo(data[5].toString());
					// arriveInfo.setMemo(data[5].toString());
				}
				arrlist.add(arriveInfo);
			}
			if (arrlist.size() > 0) {
				// 符合条件的采购单
				pobj.setList(arrlist);
				// 符合条件的采购单的总数
				// modifyBy ywliu 09/05/05
				Long totalCount = Long.parseLong(bll.getSingal(
						sqlCount.toString()).toString());
				pobj.setTotalCount(totalCount);
			}
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}

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
	 * 获取合作伙伴id
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String findClientId(String ClientName, String enterpriseCode) {
		String sql = "SELECT A.CLIEND_ID FROM  CON_J_CLIENT_INFO A "
				+ "WHERE A.IS_USE='Y' AND A.CLIENT_NAME ='" + ClientName + "'"
				+ " AND A.ENTERPRISE_CODE='" + enterpriseCode + "'";

		Object obj = bll.getSingal(sql);
		if (obj != null) {
			String clientId = obj.toString();
			return clientId;
		} else {
			return "";
		}
	}

	/**
	 * 从采购单查询未上报到货单
	 * 
	 * @return PageObject 未上报到货单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findReportArrivalDetails(String purNo,
			String enterpriseCode) throws ParseException {
		PageObject pobj = new PageObject();
		String sql = "SELECT A.ID,A.ARRIVAL_STATE,A.ARRIVAL_NO,A.INVOICE_NO FROM PUR_J_ARRIVAL A WHERE A.PUR_NO = '"
				+ purNo
				+ "' AND A.IS_USE='Y' AND A.ENTERPRISE_CODE = '"
				+ enterpriseCode + "'";
		List<PurJArrival> lst = bll.queryByNativeSQL(sql);
		List<PurJArrival> arrlist = new ArrayList<PurJArrival>();
		Iterator it = lst.iterator();
		while (it.hasNext()) {
			PurJArrival purInfo = new PurJArrival();
			Object[] data = (Object[]) it.next();
			// 流水号
			if (null != data[0]) {
				purInfo.setId(Long.parseLong(data[0].toString()));
			}
			// 单据状态
			if (null != data[1]) {
				purInfo.setArrivalState(data[1].toString());
			}
			// 单据编号
			if (null != data[2]) {
				purInfo.setArrivalNo(data[2].toString());
			}
			// 发票号 add by drdu 090618
			if (null != data[3]) {
				purInfo.setInvoiceNo(data[3].toString());
			} else {
				purInfo.setInvoiceNo("");
			}
			arrlist.add(purInfo);
		}

		if (arrlist.size() > 0) {
			// 符合条件的到货单
			pobj.setList(arrlist);
			// 符合条件的到货单的总数
			pobj.setTotalCount(Long.parseLong(arrlist.size() + ""));
		}
		return pobj;
	}

	/**
	 * 到货登记保存操作
	 * 
	 * @param lstUpdatePurJArrivaInfo
	 *            更新到货登记/验收表
	 * @param lstUpdatePurJArrivalDetailsInfo
	 *            更新到货登记/验收明细表
	 * @param lstSavePurJArrivalInfo
	 *            保存到货登记/验收表
	 * @param lstSavePurJArrivalDetailsInfo
	 *            保存到货登记/验收明细表
	 * @throws CodeRepeatException
	 * @throws ParseException
	 */
	public void saveRegister(List<PurJArrival> lstUpdatePurJArrivaInfo,
			List<PurJArrivalDetails> lstUpdatePurJArrivalDetailsInfo,
			List<PurJArrival> lstSavePurJArrivalInfo,
			List<PurJArrivalDetails> lstSavePurJArrivalDetailsInfo)
			throws CodeRepeatException {
		// CM 回滚操作
		try {
			String arrivalNo = "";
			// 保存到货登记/验收表
			if (lstSavePurJArrivalInfo.size() > 0) {
				PurJArrival entity = lstSavePurJArrivalInfo.get(0);
				Long id = bll.getMaxId("PUR_J_ARRIVAL", "ID");
				arrivalNo = getArrivalNo(id);
				entity.setId(id);
				entity.setArrivalNo(arrivalNo);
				arrivalRemote.save(lstSavePurJArrivalInfo.get(0));
			}
			// 保存到货登记/验收明细表
			if (lstSavePurJArrivalDetailsInfo.size() > 0) {
				Long id = bll.getMaxId("PUR_J_ARRIVAL_DETAILS", "ID");
				for (int i = 0; i < lstSavePurJArrivalDetailsInfo.size(); i++) {
					PurJArrivalDetails entity = lstSavePurJArrivalDetailsInfo
							.get(i);
					if (arrivalNo != "") {
						entity.setMifNo(arrivalNo);
					}
					entity.setId(id++);
					arrivalDetailsRemote.save(entity);
				}
			}
			// 更新到货登记/验收表
			if (lstUpdatePurJArrivaInfo.size() > 0) {
				arrivalRemote.update(lstUpdatePurJArrivaInfo.get(0));
			}
			// 更新到货登记/验收明细表
			if (lstUpdatePurJArrivalDetailsInfo.size() > 0) {
				for (int i = 0; i < lstUpdatePurJArrivalDetailsInfo.size(); i++) {
					PurJArrivalDetails entity = lstUpdatePurJArrivalDetailsInfo
							.get(i);
					arrivalDetailsRemote.update(entity);
				}
			}
		} catch (RuntimeException re) {
			throw re;
		}

	}

	/**
	 * 到货登记删除操作
	 * 
	 * @param lstDeletePurJArrivalInfo
	 *            删除到货登记/验收表
	 * @param lstDeletePurJArrivalDetailsInfo
	 *            删除到货登记/验收明细表
	 * @throws CodeRepeatException
	 * @throws ParseException
	 */
	public void deleteRegister(List<PurJArrival> lstDeletePurJArrivalInfo,
			List<PurJArrivalDetails> lstDeletePurJArrivalDetailsInfo)
			throws CodeRepeatException {
		// CM 回滚操作
		try {
			// 删除到货登记/验收表
			if (lstDeletePurJArrivalInfo.size() > 0) {
				PurJArrival purJArrival = lstDeletePurJArrivalInfo.get(0);
				arrivalRemote.delete(purJArrival);
			}
			// 删除到货登记/验收明细表
			if (lstDeletePurJArrivalDetailsInfo.size() > 0) {
				for (int i = 0; i < lstDeletePurJArrivalDetailsInfo.size(); i++) {
					PurJArrivalDetails entity = lstDeletePurJArrivalDetailsInfo
							.get(i);
					arrivalDetailsRemote.delete(entity);
				}
			}
		} catch (RuntimeException re) {
			throw re;
		}

	}

	/**
	 * 到货登记上报操作
	 * 
	 * @param lstRptUpdatePurJArrivaInfo
	 *            上报中更新到货登记/验收表
	 * @param lstRptUpdatePurJArrivalDetailsInfo
	 *            上报中更新到货登记/验收明细表
	 * @param lstUpdatePurJOrderDetailsInfo
	 *            更新采购订单明细表
	 * @param lstSaveInvJTransactionHisInfo
	 *            更新事务历史表
	 * @throws CodeRepeatException
	 * @throws ParseException
	 */
	public void reportRegister(List<PurJArrival> lstRptUpdatePurJArrivaInfo,
			List<PurJArrivalDetails> lstRptUpdatePurJArrivalDetailsInfo,
			List<PurJOrderDetails> lstUpdatePurJOrderDetailsInfo,
			List<InvJTransactionHis> lstSaveInvJTransactionHisInfo)
			throws CodeRepeatException {
		// CM 回滚操作
		try {
			// 上报中更新到货登记/验收表
			if (lstRptUpdatePurJArrivaInfo.size() > 0) {
				arrivalRemote.update(lstRptUpdatePurJArrivaInfo.get(0));
			}
			// 上报中更新到货登记/验收明细表
			if (lstRptUpdatePurJArrivalDetailsInfo.size() > 0) {
				for (int i = 0; i < lstRptUpdatePurJArrivalDetailsInfo.size(); i++) {
					PurJArrivalDetails entity = lstRptUpdatePurJArrivalDetailsInfo
							.get(i);
					arrivalDetailsRemote.update(entity);
				}
			}
			// 更新采购订单明细表
			if (lstUpdatePurJOrderDetailsInfo.size() > 0) {
				for (int i = 0; i < lstUpdatePurJOrderDetailsInfo.size(); i++) {
					PurJOrderDetails entity = lstUpdatePurJOrderDetailsInfo
							.get(i);
					purJOrderDetailsFacadeRemote.update(entity);
				}
			}
			// 更新事务历史表
			if (lstSaveInvJTransactionHisInfo.size() > 0) {
				Long id = invJTransactionHisFacadeRemote.getMaxId();
				for (int i = 0; i < lstSaveInvJTransactionHisInfo.size(); i++) {
					InvJTransactionHis entity = lstSaveInvJTransactionHisInfo
							.get(i);
					entity.setTransHisId(id++);
					invJTransactionHisFacadeRemote.save(entity);
				}
			}

		} catch (RuntimeException re) {
			throw re;
		}

	}

	/**
	 * 生成到货单编号
	 * 
	 * @param Id
	 *            流水号
	 */
	private String getArrivalNo(Long Id) {
		String arrivalNo = "DH";
		String id = String.valueOf(Id);
		if (id.length() > 6) {
			arrivalNo += id.substring(0, 6);
		} else {
			String pad = "000000";
			arrivalNo += pad.substring(0, 6 - id.length()) + id;
		}
		return arrivalNo;
	}
}
