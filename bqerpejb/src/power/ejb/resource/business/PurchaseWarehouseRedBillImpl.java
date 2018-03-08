package power.ejb.resource.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
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
import power.ejb.resource.PurJOrder;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.PurJOrderFacadeRemote;
import power.ejb.resource.form.PurchaseWarehouseDetailInfo;
@Stateless
public class PurchaseWarehouseRedBillImpl implements PurchaseWarehouseRedBill {
	
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 到货登记remote */
	@EJB(beanName = "PurJArrivalFacade")
	protected PurJArrivalFacadeRemote arrivalRemote;
	/** 到货登记明细remote */
	@EJB(beanName = "PurJArrivalDetailsFacade")
	protected PurJArrivalDetailsFacadeRemote arrivalDetailsRemote;
	@EJB(beanName = "InvJTransactionHisFacade")
	protected InvJTransactionHisFacadeRemote invJTransHisRemote;
	@EJB(beanName = "InvJWarehouseFacade")
	protected InvJWarehouseFacadeRemote invJWarehouseRemote;
	@EJB(beanName = "InvJLocationFacade")
	protected InvJLocationFacadeRemote invJLocationRemote;
	@EJB(beanName = "InvJLotFacade")
	protected InvJLotFacadeRemote invJLotRemote;
	@EJB(beanName = "PurJOrderFacade")
	protected PurJOrderFacadeRemote orderRemote;
	@EJB(beanName = "PurJOrderDetailsFacade")
	protected PurJOrderDetailsFacadeRemote orderDetailsRemote;

	public void addPurchaseWarehouseRedBill(PurJArrival purjArrivalBean,
			PurJArrivalDetails purJArrivalDetailBean,
			InvJTransactionHis transactionHisBean, InvJWarehouse warehouseBean,
			InvJLocation locationBean, InvJLot lotBean) {
		Long id = bll.getMaxId("PUR_J_ARRIVAL", "ID");
		String arrivalNo = getArrivalNo(id);
		purjArrivalBean.setId(id);
		purjArrivalBean.setArrivalNo(arrivalNo);
		Long arrivalDetailId = bll.getMaxId("PUR_J_ARRIVAL_DETAILS", "ID");
		purJArrivalDetailBean.setId(arrivalDetailId);
		purJArrivalDetailBean.setMifNo(arrivalNo);
		// 新增一条采购单信息 modify by ywliu 20100126
		List<PurJOrder> list = orderRemote.findByPurNo(purjArrivalBean.getPurNo());
		String purNo = "";
		if(list.size() > 0) {
			PurJOrder purJOrder = list.get(0);
			String purStatus = purJOrder.getPurStatus();
			purJOrder.setId(null);
			PurJOrder entity = orderRemote.save(purJOrder);
			entity.setLastModifiedBy(purjArrivalBean.getLastModifiedBy());
			entity.setPurStatus(purStatus);
			orderRemote.update(entity);
			purNo = entity.getPurNo();
			String sql = "select * from pur_j_order_details t where t.pur_no = '"+purJOrder.getPurNo()+"' and t.material_id = '"+purJArrivalDetailBean.getMaterialId()+"'";
			List<PurJOrderDetails> purJOrderDetailsList = bll.queryByNativeSQL(sql, PurJOrderDetails.class);
			if(purJOrderDetailsList.size() >0) {
				PurJOrderDetails purJOrderDetails = purJOrderDetailsList.get(0);
				purJOrderDetails.setPurNo(entity.getPurNo());
				purJOrderDetails.setLastModifiedBy(purjArrivalBean.getLastModifiedBy());
				purJOrderDetails.setPurOrderDetailsId(null);
				PurJOrderDetails detailEntity = orderDetailsRemote.save(purJOrderDetails);
				detailEntity.setPurQty(purJArrivalDetailBean.getRcvQty());
				detailEntity.setRcvQty(purJArrivalDetailBean.getRcvQty());
				detailEntity.setUnitPrice(transactionHisBean.getPrice());
				orderDetailsRemote.update(detailEntity);
				purJArrivalDetailBean.setPurLine(detailEntity.getPurOrderDetailsId());
			}
		}
		purjArrivalBean.setPurNo(purNo);
		arrivalRemote.save(purjArrivalBean);
		purJArrivalDetailBean.setPurNo(purNo);
		arrivalDetailsRemote.save(purJArrivalDetailBean);
		Long transHisId = invJTransHisRemote.getMaxId();
		transactionHisBean.setTransHisId(transHisId);
		// modify by ywliu 20100126
		transactionHisBean.setOrderNo(purNo);
		transactionHisBean.setArrivalNo(arrivalNo);
		invJTransHisRemote.save(transactionHisBean);
		invJWarehouseRemote.update(warehouseBean);
		invJLocationRemote.update(locationBean);
		invJLotRemote.update(lotBean);
	}

	public PageObject findPurchasehouseCheckList(String enterpriseCode,String sDate, String eDate,
			String materialNo,String materialName,String whsName,String specNo,String supplierName,
			String purBy,String whsBy,String isRedBill,final int...rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql = "SELECT A.ID,\n"
				+ "       A.ARRIVAL_NO,\n"
				+ "       B.MEMO,\n"
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
				+ "       B.RCV_QTY AS RCVQTY,\n" //modify by fyyang 090804
				+ "       D.UNIT_PRICE,\n"
				+ "       to_char(D.DUE_DATE, 'yyyy-mm-dd hh24:mi:ss') DUE_DATE,\n"
				+ "       D.TAX_RATE,\n"
				+ "       D.INS_QTY,\n"
				+ "       B.ID AS ARRIVALDETAILID,\n"
				+ "       B.REC_QTY,\n"
				+ "       B.LAST_MODIFIED_DATE,\n"
				+ "       to_char(D.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss') OrderDetailModifiedDate,\n"
				+ "       to_char(C.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss') MaterialModifiedDate,\n"
				+ "       D.CURRENCY_ID,\n"
				+ "       (select s.currency_name\n"
				+ "          from sys_c_currency s\n"
				+ "         where s.currency_id = d.currency_id\n"
				+ "           and s.is_use = 'Y') currency_name,\n"
				+ "       A.SUPPLIER,\n"
				+ "       getclientname(A.SUPPLIER) cliend_name,\n"
				+ "       getworkername(O.BUYER),\n"
				+ "       A.CONTRACT_NO,\n"
				+ "       A.INVOICE_NO,\n"
				+ "       (D.UNIT_PRICE * abs(D.RCV_QTY)) estimatedSum,\n"// add by abs()取数量的绝对值
				+ "       O.PUR_NO,\n"
				//-----------add by fyyang ----------------------------------
				+"nvl((select sum(t.rcv_qty)\n" +
				"                    from PUR_J_ARRIVAL_DETAILS t\n" + 
				"                   where t.material_id = B.MATERIAL_ID\n" + 
				"                     and t.mif_no in\n" + 
				"                         (select tt.arrival_no\n" + 
				"                            from pur_j_arrival tt\n" + 
				"                           where tt.relation_arrival_no = A.arrival_no)),0)+B.RCV_QTY \n"
			

				//-----------add end----------------------------------------
				// + " (select w.whs_name\n"
				// + " from INV_C_WAREHOUSE w\n"
				// + " where w.whs_no = c.default_whs_no\n"
				// + " and w.is_use = 'Y') whs_name\n"
				+ "  FROM PUR_J_ARRIVAL         A,\n"
				+ "       PUR_J_ORDER_DETAILS   D,\n"
				+ "       PUR_J_ARRIVAL_DETAILS B,\n"
				+ "       INV_C_MATERIAL        C,\n"
				+ "       PUR_J_ORDER           O\n"
				+ " WHERE A.IS_USE = 'Y'\n" + "   AND B.IS_USE = 'Y'\n"
				+ "   AND C.IS_USE = 'Y'\n" + "   AND D.IS_USE = 'Y'\n"
				+ "   AND O.IS_USE = 'Y'\n"
				+ "   AND A.ARRIVAL_NO = B.MIF_NO\n"
				+ "   AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID\n"
				+ "   AND B.MATERIAL_ID = C.MATERIAL_ID\n"
				+ "   AND B.PUR_NO = O.PUR_NO\n"
				+ "   AND A.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND B.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND C.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND D.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND O.ENTERPRISE_CODE = '" + enterpriseCode + "'\n";
		if(isRedBill != null && isRedBill.length() > 0) {
			if("Y".equals(isRedBill)) {
				sql += " and A.RELATION_ARRIVAL_NO is not null AND B.Rcv_Qty < 0 \n" +
					   " and A.ARRIVAL_NO not in (select a.relation_arrival_no from PUR_J_ARRIVAL a ,pur_j_arrival_details b " +
					   "where a.arrival_no = b.mif_no and a.relation_arrival_no is not null and b.rcv_qty > 0)";
			} else if("N".equals(isRedBill)) {
				sql	+= "   AND A.check_state='2'"
					//+ "and d.material_id not in (select aa.material_id from PUR_J_ARRIVAL_DETAILS aa,PUR_J_ARRIVAL  bb where aa.mif_no = bb.arrival_no and bb.relation_arrival_no = A.ARRIVAL_NO) "
					//modify by fyyang 090804 允许多次红单
					+"    and B.RCV_QTY+(select nvl((select sum(t.rcv_qty) from PUR_J_ARRIVAL_DETAILS t\n" +
					"where t.material_id=B.MATERIAL_ID and t.mif_no in (\n" + 
					"select tt.arrival_no  from pur_j_arrival tt\n" + 
					"where tt.relation_arrival_no=A.arrival_no)),0) from dual)>0 \n"
					//----------------------------------
					+ "   AND D.RCV_QTY > 0";
			}
		} else {
			sql	+= "   AND A.check_state='2'"
				//+ "and d.material_id not in (select aa.material_id from PUR_J_ARRIVAL_DETAILS aa,PUR_J_ARRIVAL  bb where aa.mif_no = bb.arrival_no and bb.relation_arrival_no = A.ARRIVAL_NO) "
				//modify by fyyang 090804 允许多次红单
				+"    and B.RCV_QTY+(select nvl((select sum(t.rcv_qty) from PUR_J_ARRIVAL_DETAILS t\n" +
				"where t.material_id=B.MATERIAL_ID and t.mif_no in (\n" + 
				"select tt.arrival_no  from pur_j_arrival tt\n" + 
				"where tt.relation_arrival_no=A.arrival_no)),0) from dual)>0 \n"
				//----------------------------------
				+ "   AND D.RCV_QTY > 0";
		}		
		String sqlcount = "SELECT COUNT(1)\n"
				+ "  FROM PUR_J_ARRIVAL         A,\n"
				+ "       PUR_J_ORDER_DETAILS   D,\n"
				+ "       PUR_J_ARRIVAL_DETAILS B,\n"
				+ "       INV_C_MATERIAL        C,\n"
				+ "       PUR_J_ORDER           O\n"
				+ " WHERE A.IS_USE = 'Y'\n" + "   AND B.IS_USE = 'Y'\n"
				+ "   AND C.IS_USE = 'Y'\n" + "   AND D.IS_USE = 'Y'\n"
				+ "   AND O.IS_USE = 'Y'\n"
				+ "   AND A.ARRIVAL_NO = B.MIF_NO\n"
				+ "   AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID\n"
				+ "   AND B.MATERIAL_ID = C.MATERIAL_ID\n"
				+ "   AND B.PUR_NO = O.PUR_NO\n"
				+ "   AND A.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND B.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND C.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND D.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND O.ENTERPRISE_CODE = '" + enterpriseCode + "'\n";
		if(isRedBill != null && isRedBill.length() > 0) {
			if("Y".equals(isRedBill)) {
				sqlcount += " and A.RELATION_ARRIVAL_NO is not null AND B.Rcv_Qty < 0 \n" +
				   " and A.ARRIVAL_NO not in (select a.relation_arrival_no from PUR_J_ARRIVAL a ,pur_j_arrival_details b " +
				   "where a.arrival_no = b.mif_no and a.relation_arrival_no is not null and b.rcv_qty > 0)";
			} else if("N".equals(isRedBill)) {
				sqlcount += "   AND A.check_state='2'"
					//+ "and d.material_id not in (select aa.material_id from PUR_J_ARRIVAL_DETAILS aa,PUR_J_ARRIVAL  bb where aa.mif_no = bb.arrival_no and bb.relation_arrival_no = A.ARRIVAL_NO) "
					//modify by fyyang 090804 允许多次红单
					+"    and B.RCV_QTY+(select nvl((select sum(t.rcv_qty) from PUR_J_ARRIVAL_DETAILS t\n" +
					"where t.material_id=B.MATERIAL_ID and t.mif_no in (\n" + 
					"select tt.arrival_no  from pur_j_arrival tt\n" + 
					"where tt.relation_arrival_no=A.arrival_no)),0) from dual)>0 \n"
					//----------------------------------
					+ "   AND D.RCV_QTY > 0";
			}
		} else {
			sqlcount += "   AND A.check_state='2'"
				//+ "and d.material_id not in (select aa.material_id from PUR_J_ARRIVAL_DETAILS aa,PUR_J_ARRIVAL  bb where aa.mif_no = bb.arrival_no and bb.relation_arrival_no = A.ARRIVAL_NO) "
				//modify by fyyang 090804 允许多次红单
				+"    and B.RCV_QTY+(select nvl((select sum(t.rcv_qty) from PUR_J_ARRIVAL_DETAILS t\n" +
				"where t.material_id=B.MATERIAL_ID and t.mif_no in (\n" + 
				"select tt.arrival_no  from pur_j_arrival tt\n" + 
				"where tt.relation_arrival_no=A.arrival_no)),0) from dual)>0 \n"
				//----------------------------------
				+ "   AND D.RCV_QTY > 0";
		}		

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
			whereStr += " and getclientname(A.SUPPLIER) like '%"
					+ supplierName + "%'";
		}
		if (purBy != null && purBy.length() > 0) {
			whereStr += " and getworkername(O.BUYER) like '%" + purBy + "%'";
		}

		//add by fyyang 20100311
		whereStr+="  and A.Check_Date<(select max( bb.balance_date) from inv_j_balance bb where bb.is_use='Y') \n";
		sql += whereStr;
		sqlcount += whereStr;
		sql += " order by A.ARRIVAL_NO";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlcount).toString());
		List<PurchaseWarehouseDetailInfo> arraylist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			Double pageTotalCount = 0.00;
			Double pageTotalMoney = 0.00;
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
					model.setFrozenCost(Double.parseDouble(data[17]
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
				//add by fyyang 入库红单总操作数量
				 if(data[39]!=null)
				 {

					 
						 model.setSumRedQty(Double.parseDouble(data[39].toString()));
						
				 }
				
				// if(data[38] != null)
				// model.setWhsName(data[38].toString());
				pageTotalCount += model.getPurOrderDetailsRcvQty();
				pageTotalMoney += model.getEstimatedSum();
				arraylist.add(model);
			}
			// 计算当前页的合计数量 modify by ywliu 090721 
			PurchaseWarehouseDetailInfo info = new PurchaseWarehouseDetailInfo();
			info.setPurOrderDetailsRcvQty(pageTotalCount);
			info.setEstimatedSum(pageTotalMoney);
			arraylist.add(info);
		}
		// 计算总共的合计数量 modify by ywliu 090721 
		if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1 && rowStartIdxAndCount[0] != 0) {
			String sumSql = "select sum((D.UNIT_PRICE * D.RCV_QTY)) estimatedSum ,\n"
				+ "  sum(D.RCV_QTY) AS RCVQTY \n"
				+ "  FROM PUR_J_ARRIVAL         A,\n"
				+ "       PUR_J_ORDER_DETAILS   D,\n"
				+ "       PUR_J_ARRIVAL_DETAILS B,\n"
				+ "       INV_C_MATERIAL        C,\n"
				+ "       PUR_J_ORDER           O\n"
				+ " WHERE A.IS_USE = 'Y'\n" + "   AND B.IS_USE = 'Y'\n"
				+ "   AND C.IS_USE = 'Y'\n" + "   AND D.IS_USE = 'Y'\n"
				+ "   AND O.IS_USE = 'Y'\n"
				+ "   AND A.ARRIVAL_NO = B.MIF_NO\n"
				+ "   AND B.PUR_LINE = D.PUR_ORDER_DETAILS_ID\n"
				+ "   AND B.MATERIAL_ID = C.MATERIAL_ID\n"
				+ "   AND B.PUR_NO = O.PUR_NO\n"
				+ "   AND A.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND B.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND C.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND D.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND O.ENTERPRISE_CODE = '" + enterpriseCode + "'\n"
				+ "   AND A.check_state='2'"
				+ "and d.material_id not in (select aa.material_id from PUR_J_ARRIVAL_DETAILS aa,PUR_J_ARRIVAL  bb where aa.mif_no = bb.arrival_no and bb.relation_arrival_no = A.ARRIVAL_NO) "
				+ "   AND D.RCV_QTY > 0";
			sumSql += whereStr ; 
			Object[] object =(Object[]) bll.getSingal(sumSql);
			
			PurchaseWarehouseDetailInfo info = new PurchaseWarehouseDetailInfo();
			info.setPurOrderDetailsRcvQty(Double.valueOf(object[1].toString()));
			info.setEstimatedSum(Double.valueOf(object[0].toString()));
			arraylist.add(info);
		}
		obj.setList(arraylist);
		obj.setTotalCount(totalCount);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public InvJTransactionHis getTransactionHisBean(String arrivalNo,
			String materialId) {
		String sql = "select *\n" +
				"  from inv_j_transaction_his t, pur_j_arrival b\n" + 
				" where t.order_no = b.pur_no and  b.arrival_no = '"+arrivalNo+"'\n" + 
				"   and t.material_id = '"+materialId+"'";
		List<InvJTransactionHis> beanList = bll.queryByNativeSQL(sql, InvJTransactionHis.class);
		InvJTransactionHis bean = new InvJTransactionHis();
		if(beanList != null && beanList.size() > 0) {
			bean = beanList.get(0);
		}
		return bean;
	}

	/**
	 * 生成到货单编号
	 * @param Id 流水号
	 */
	private String  getArrivalNo(Long Id){
		String arrivalNo = "DH";
		String id = String.valueOf(Id);
		if(id.length() > 6){
			arrivalNo += id.substring(0,6);
		}else{
			String pad = "000000";
			arrivalNo +=pad.substring(0, 6 - id.length()) + id;
		}
		return arrivalNo;
	}
}
