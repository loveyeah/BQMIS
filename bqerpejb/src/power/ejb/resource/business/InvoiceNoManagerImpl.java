package power.ejb.resource.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.form.PurchaseWarehouseDetailInfo;
import power.ejb.resource.form.PurchaseWarehouseInfo;

@Stateless
public class InvoiceNoManagerImpl implements InvoiceNoManager{
	@PersistenceContext
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "PurJArrivalFacade")
	protected PurJArrivalFacadeRemote purJArrivalRemote;

	public PageObject findArrivalBillList(int start, int limit, String enterpriseCode,String buyer,String purNo,String supplyName, String materialName) {
		// modified by liuyi 091209
//		String sql = "SELECT DISTINCT(A.ID),A.ARRIVAL_NO,A.PUR_NO,A.CONTRACT_NO,"
//			+ "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd')OPERATE_DATE,A.SUPPLIER,GETCLIENTNAME(A.SUPPLIER),A.MEMO,"
//			+ "to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')OPERATE_DATE2,A.INVOICE_NO "
//			+" ,GETWORKERNAME(F.BUYER)\n" //add by fyyang 091123
//			+ "FROM PUR_J_ARRIVAL A,PUR_J_ARRIVAL_DETAILS B,PUR_J_ORDER_DETAILS D,INV_C_MATERIAL E\n"
//			+" ,pur_j_order F \n" //add by fyyang 091123
//			+ "WHERE D.IS_USE='Y' AND A.IS_USE='Y' AND B.IS_USE='Y' "
//			+ " AND A.ARRIVAL_STATE='2'  "
//			+ "  and B.MATERIAL_ID=E.MATERIAL_ID "
//			+ "AND A.ARRIVAL_NO=B.MIF_NO AND A.PUR_NO=D.PUR_NO AND B.PUR_LINE=D.PUR_ORDER_DETAILS_ID AND B.RCV_QTY > 0 AND B.RCV_QTY=B.REC_QTY"
//			+ " AND A.ENTERPRISE_CODE='"
//			+ enterpriseCode
//			+ "'"
//			+ " AND B.ENTERPRISE_CODE='"
//			+ enterpriseCode
//			+ "'"
//			+ " AND D.ENTERPRISE_CODE='" + enterpriseCode + "'"
//			+ " and (A.check_state <> '2' or a.check_state is null)" 
//			+ " and a.arrival_no not in (select distinct a.mif_no from PUR_J_ARRIVAL_DETAILS a where a.is_use = 'Y' and a.enterprise_code = 'hfdc'and a.rec_qty < a.rcv_qty and a.rcv_qty >0)"
//			//+ " and A.INVOICE_NO is null" //modify  by fyyang 091123
//			+ " and A.LAST_MODIFIED_DATE>=to_date('" + sDate
//			+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
//			+ "and A.LAST_MODIFIED_DATE<=to_date('" + eDate
//			+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n" 
//			+" and D.PUR_NO=F.PUR_NO and F.Is_Use='Y' and F.Enterprise_Code='"+enterpriseCode+"'"; //add by fyyang 091123
//		//	+ " order by A.ARRIVAL_NO DESC ";
//			
//		String whereStr = "";
//		if (buyer != null && buyer.length() > 0) { // add by drdu 091124
//			whereStr += " and GETWORKERNAME(F.BUYER) like "
//					+ "'%" + buyer + "%'\n";
//		}
//		
//		String recordCountSql = "SELECT Count(DISTINCT(A.ID)) FROM PUR_J_ARRIVAL A,"
//				+ "PUR_J_ARRIVAL_DETAILS B,PUR_J_ORDER_DETAILS D,INV_C_MATERIAL E,pur_j_order F\n"
//				+ "WHERE D.IS_USE='Y' AND A.IS_USE='Y' AND B.IS_USE='Y' "
//				+ " AND A.ARRIVAL_STATE='2'  "
//				+ "  and B.MATERIAL_ID=E.MATERIAL_ID "
//				+ "AND A.ARRIVAL_NO=B.MIF_NO AND A.PUR_NO=D.PUR_NO AND B.PUR_LINE=D.PUR_ORDER_DETAILS_ID AND B.RCV_QTY > 0 AND B.RCV_QTY=B.REC_QTY"
//				+ " AND A.ENTERPRISE_CODE='"
//				+ enterpriseCode
//				+ "'"
//				+ " AND B.ENTERPRISE_CODE='"
//				+ enterpriseCode
//				+ "'"
//				+ " AND D.ENTERPRISE_CODE='" + enterpriseCode + "'"
//				+ " and (A.check_state <> '2' or a.check_state is null)" 
//				+ " and a.arrival_no not in (select distinct a.mif_no from PUR_J_ARRIVAL_DETAILS a where a.is_use = 'Y' and a.enterprise_code = 'hfdc'and a.rec_qty < a.rcv_qty and a.rcv_qty >0)"
//				//+ " and A.INVOICE_NO is null"
//				+ " and A.LAST_MODIFIED_DATE>=to_date('" + sDate
//				+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
//				+ "and A.LAST_MODIFIED_DATE<=to_date('" + eDate
//				+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
//				+" and D.PUR_NO=F.PUR_NO and F.Is_Use='Y' and F.Enterprise_Code='"+enterpriseCode+"'"; //add by drdu 091124
//				//+ " order by A.ARRIVAL_NO DESC ";
//		
//		sql += whereStr;
//		recordCountSql += whereStr;
//		sql += " order by A.ARRIVAL_NO DESC";
//		recordCountSql += " order by A.ARRIVAL_NO DESC";
//		String sqlWhere=" and   b.invoice_no is not null and b.invoice_no <> ' ' \n";
		String sqlWhere="  \n";
		String sql=
			"select distinct b.id,\n" +
			"                b.pur_no,\n" + 
			"                b.supplier,\n" + 
			"                GETCLIENTNAME(b.supplier),\n" + 
			"                to_char(b.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss'),b.contract_no,b.invoice_no,b.memo,getworkername(b.buyer)\n" + 
			"\n" + 
			"  from pur_j_order b, pur_j_arrival_details d,inv_c_material t\n" + 
			" where d.material_id=t.material_id" +
//			" and (select count(*)\n" +             //modify by drdu 20100428
//			"          from pur_j_order_details a\n" + 
//			"         where a.rcv_qty >= a.pur_qty\n" + 
//			"           and a.is_use = 'Y'\n" + 
//			"           and a.enterprise_code = 'hfdc'\n" + 
//			"           and b.pur_no = a.pur_no) =\n" + 
//			"       (select count(*)\n" + 
//			"          from pur_j_order_details a\n" + 
//			"         where a.is_use = 'Y'\n" + 
//			"           and a.enterprise_code = 'hfdc'\n" + 
//			"           and b.pur_no = a.pur_no)\n" + 
//			"\n" + 
			"   and b.pur_no = d.pur_no\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = 'hfdc'\n" + 
			"   and d.enterprise_code = 'hfdc'\n" + 
			"   and d.is_use = 'Y'";
		sqlWhere +="    and (select count(*)\n" +
			"         from pur_j_arrival c\n" + 
			"        where c.pur_no = b.pur_no\n" + 
			"          and c.check_state = '2'\n" + 
			"          and c.is_use = 'Y'\n" + 
			"          and c.enterprise_code = 'hfdc') = 0\n" + 
			//----------add by drdu 20100428--------------
			"  and (select count(*)\n" + 
			"         from pur_j_arrival_details s\n" + 
			"        where s.pur_no = b.pur_no\n" + 
			"          and s.rec_qty <= 0\n" + 
			"          and s.is_use = 'Y'\n" + 
			"          and s.enterprise_code = 'hfdc') = 0";
			//--------------------end---------------------
	//	"           and d.rcv_qty > 0";      //modify by drdu 20100428
		
		
//		+ " and B.LAST_MODIFIED_DATE>=to_date('" + sDate
//		+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
//		+ "and B.LAST_MODIFIED_DATE<=to_date('" + eDate
//		+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n" ;
		if (buyer != null && buyer.length() > 0) { // add by drdu 091124
			sqlWhere += " and GETWORKERNAME(B.BUYER) like "
					+ "'%" + buyer + "%'\n";
		}
		if(purNo!=null&&purNo.length()>0){
			sqlWhere+="  and  b.pur_no  like '%"+purNo+"%' \n";
		}
		if(supplyName!=null&&supplyName.length()>0)
		{
			sqlWhere+="  and  GETCLIENTNAME(b.supplier) like '%"+supplyName+"%' \n";
		}
		if(materialName!=null&&materialName.length()>0)
		{
			sqlWhere+=" and t.material_name like '%"+materialName+"%' \n";
		}
		sql+=sqlWhere;
		String recordCountSql = "select count(*) from (" + sql + ") \n";
		sql+= "  order by b.pur_no desc ";
		List<Object> list = bll.queryByNativeSQL(sql, start, limit);
		List<PurchaseWarehouseInfo> arraylist = new ArrayList<PurchaseWarehouseInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			PurchaseWarehouseInfo model = new PurchaseWarehouseInfo();
			// modified by liuyi 091209
//			if (data[0] != null) {
//				model.setId(Long.parseLong(data[0].toString()));
//			}
//			if (data[1] != null) {
//				model.setArrivalNo(data[1].toString());
//			}
//			if (data[2] != null) {
//				model.setPurNo(data[2].toString());
//			}
//			if (data[3] != null) {
//				model.setContractNo(data[3].toString());
//			}
//	
//			if (data[4] != null) {
//				model.setOperateDate(data[4].toString());
//			}
//			if (data[5] != null) {
//				model.setSupplier(data[5].toString());
//			}
//			if (data[6] != null) {
//				model.setSupplyName(data[6].toString());
//			}
//			if (data[7] != null) {
//				model.setMemo(data[7].toString());
//			}
//			if (data[8] != null) {
//				model.setOperateDate2(data[8].toString());
//			}
//			if (data[9] != null) {
//				model.setInvoiceNo(data[9].toString());
//			}
//			//add by fyyang 091123
//	        if(data[10]!=null)
//	        {
//	        	model.setBuyerName(data[10].toString());
//	        }
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
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
			if(data[8] != null)
			{
				model.setLoginName(data[8].toString());
			}
			arraylist.add(model);
		}
	
		Object countlist = bll.getSingal(recordCountSql);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;
	}
	
	/**
	 * 查询入库审核详细信息列表
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findArrivalBillDetailList(Long id,int start,int limit,String enterpriseCode) {
		String sql = "SELECT distinct A.ID,A.ARRIVAL_NO,B.MEMO,B.LOT_CODE,B.RCV_QTY,B.LAST_MODIFIED_BY,\n"
			+ " C.MATERIAL_ID,C.MATERIAL_NO,C.MATERIAL_NAME,C.IS_LOT,C.MAERTIAL_CLASS_ID,\n"
			+ "C.COST_METHOD,C.STD_COST,C.DEFAULT_WHS_NO,C.DEFAULT_LOCATION_NO,C.SPEC_NO,\n"
			+ "C.STOCK_UM_ID,C.FROZEN_COST,D.PUR_ORDER_DETAILS_ID,D.PUR_QTY,D.RCV_QTY AS RCVQTY,D.UNIT_PRICE,\n"
			+ "D.DUE_DATE,D.TAX_RATE,D.CURRENCY_ID,D.INS_QTY,B.ID AS ARRIVALDETAILID,B.REC_QTY,\n"
			+ "to_char(B.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')ArrivalDetailModifiedDate,\n"
			+ "to_char(D.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')OrderDetailModifiedDate,\n"
			+ "to_char(C.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')MaterialModifiedDate,\n"
			+ "GETWHSNAME(c.default_whs_no)\n"
		  //  + "e.from_location_no\n" //modify by fyyang 091123
			+ "FROM PUR_J_ARRIVAL A,PUR_J_ORDER_DETAILS D,PUR_J_ARRIVAL_DETAILS B,INV_C_MATERIAL C ,inv_j_transaction_his e "
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
			+ " and e.order_no = d.pur_no \n"
			+" and B.The_Qty<>0 ";  //add by fyyang 091123
		String countSql = "SELECT COUNT(A.ID)"
				+ "FROM PUR_J_ARRIVAL A,PUR_J_ORDER_DETAILS D,PUR_J_ARRIVAL_DETAILS B,INV_C_MATERIAL C ,inv_j_transaction_his e "
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
				+ " and e.order_no = d.pur_no \n"
				+" and B.The_Qty<>0 ";  //add by fyyang 091123
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
			if(data[31] != null) {
				model.setWhsNo(data[31].toString());
			}
//			if(data[32] != null) {
//				model.setLocationNo(data[32].toString());
//			}
			model.setGridMemo(model.getMemo());
			arraylist.add(model);
		}
		Object countlist = bll.getSingal(countSql);
		PageObject obj = new PageObject();
		obj.setList(arraylist);
		obj.setTotalCount(new Long(countlist.toString()));
		return obj;
	}

}
