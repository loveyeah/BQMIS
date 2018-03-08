package power.ejb.resource;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.ArriveCangoDetailInfo;
import power.ejb.resource.form.MRPArriveCangoDetailInfo;

/**
 * 到货登记/验收明细表实体.
 * 
 * @see power.ejb.resource.PurJArrivalDetails
 * @author zhaozhijie
 */
@Stateless
public class PurJArrivalDetailsFacade implements PurJArrivalDetailsFacadeRemote {

	public static final String MIF_NO = "mifNo";
	public static final String PUR_LINE = "purLine";
	public static final String MATERIAL_ID = "materialId";
	@PersistenceContext
	private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PurJArrivalDetails
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            PurJArrivalDetails entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PurJArrivalDetails entity) {
		LogUtil.log("saving PurJArrivalDetails instance", Level.INFO, null);
		try {
//			String sql = "select case when  max(ID) is  null then 1 else max(ID)+1 end from PUR_J_ARRIVAL_DETAILS";
//			entity.setId(Long.parseLong(bll.getSingal(sql).toString()));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条记录.
	 * 
	 * @param ardid 流水号
	 * @throws CodeRepeatException
	 */
	public void delete(PurJArrivalDetails entity) throws CodeRepeatException {
		LogUtil.log("deleting PurJArrivalDetails instance", Level.INFO, null);
		try {
			if (entity != null) {
				// is_use设为N
				entity.setIsUse("N");
				this.update(entity);
				LogUtil.log("delete successful", Level.INFO, null);
			}

		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条记录.
	 * 
	 * @param ardid 流水号
	 * @throws CodeRepeatException
	 */
	public void deleteMuti(Long ardid) throws CodeRepeatException {
		LogUtil.log("deleting PurJArrivalDetails instance", Level.INFO, null);
		try {
			String sql = "update PUR_J_ARRIVAL_DETAILS t\n"
				+ "set t.is_use='N'\n" + "where t.ID = '"
				+ ardid + "'";
			bll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);

		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询到货登记明细bean
	 * 
	 * @param mifNo 到货单号
	 * 
	 * @throws CodeRepeatException
	 */
	public PageObject findByArrivalNo(String mifNo, String enterpriseCode) throws CodeRepeatException{
		LogUtil.log("finding PurJArrival instance with mifNo: " + mifNo, Level.INFO,
				null);
		PageObject pobj = new PageObject();
		try {
			String sql = "select ID,MIF_NO,PUR_NO,PUR_LINE,MATERIAL_ID, " +
					"LOT_CODE,BAD_QTY,RCV_QTY,THE_QTY,ITEM_STATUS," +
					"LAST_MODIFIED_BY,LAST_MODIFIED_DATE,MEMO,ENTERPRISE_CODE," +
					"IS_USE,REC_QTY from PUR_J_ARRIVAL_DETAILS where MIF_NO = '"+mifNo
					+"' AND ENTERPRISE_CODE = '" +
					enterpriseCode + "' AND IS_USE = 'Y'";
			List<PurJArrivalDetails> lst = bll.queryByNativeSQL(sql);
			List<PurJArrivalDetails> arrlist = new ArrayList<PurJArrivalDetails>();
			if (lst!=null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					PurJArrivalDetails purJArrivalDetails = new PurJArrivalDetails();
					Object[] data =  (Object[]) it.next(); 
					if(null != data[0]){
						purJArrivalDetails.setId(Long.parseLong(data[0].toString()));
					}
					if(null != data[1]){
						purJArrivalDetails.setMifNo(data[1].toString());
					}
					if(null != data[2]){
						purJArrivalDetails.setPurNo(data[2].toString());
					}
					if(null != data[3]){
						purJArrivalDetails.setPurLine(Long.parseLong(data[3].toString()));
					}
					if(null != data[4]){
						purJArrivalDetails.setMaterialId(Long.parseLong(data[4].toString()));
					}
					if(null != data[5]){
						purJArrivalDetails.setLotCode(data[5].toString());
					}
					if(null != data[6]){
						purJArrivalDetails.setBadQty(Double.parseDouble(data[6].toString()));
					}
					if(null != data[7]){
						purJArrivalDetails.setRcvQty(Double.parseDouble(data[7].toString()));
					}
					if(null != data[8]){
						purJArrivalDetails.setTheQty(Double.parseDouble(data[8].toString()));
					}
					if(null != data[9]){
						purJArrivalDetails.setItemStatus(data[9].toString());
					}
					if(null != data[10]){
						purJArrivalDetails.setLastModifiedBy(data[10].toString());
					}
					if(null != data[11]){
						SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd");
						purJArrivalDetails.setLastModifiedDate(sdfFrom.parse(data[11].toString()));
					}
					if(null != data[12]){
						purJArrivalDetails.setMemo(data[12].toString());
					}
					if(null != data[13]){
						purJArrivalDetails.setEnterpriseCode(data[13].toString());
					}
					if(null != data[14]){
						purJArrivalDetails.setIsUse(data[14].toString());
					}
					if (null != data[15]) {
						purJArrivalDetails.setRecQty(Double.parseDouble(data[15].toString()));
					}
					arrlist.add(purJArrivalDetails);
				}
			}
			if(arrlist.size()>0)
			{
				// 符合条件的物资详细单
				pobj.setList(arrlist);
				// 符合条件的物资详细单的总数 
				pobj.setTotalCount(Long.parseLong(arrlist.size() + ""));
			}	
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pobj;
	}
	/**
	 * 更新一条记录
	 * 
	 * @param entity 到货登记/验收明细表实体
	 * @return PurJArrivalDetails 更新数据库的数据
	 * @throws 
	 */
	public PurJArrivalDetails update(PurJArrivalDetails entity) {
		LogUtil.log("updating PurJArrivalDetails instance", Level.INFO, null);
		try {         
			// 修改时间
            entity.setLastModifiedDate(new java.util.Date());
			PurJArrivalDetails result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PurJArrivalDetails findById(Long id) {
		LogUtil.log("finding PurJArrivalDetails instance with id: " + id,
				Level.INFO, null);
		try {
			PurJArrivalDetails instance = entityManager.find(
					PurJArrivalDetails.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurJArrivalDetails entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurJArrivalDetails property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurJArrivalDetails> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PurJArrivalDetails> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PurJArrivalDetails instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PurJArrivalDetails model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurJArrivalDetails entities.
	 * 
	 * @return List<PurJArrivalDetails> all PurJArrivalDetails entities
	 */
	@SuppressWarnings("unchecked")
	public List<PurJArrivalDetails> findAll() {
		LogUtil.log("finding all PurJArrivalDetails instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from PurJArrivalDetails model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过采购单编号得到采购单明细
	 *
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	@SuppressWarnings("unchecked")
	public double findByMifNo(String mifNo, final int... rowStartIdxAndCount) {
		StringBuilder sbd = new StringBuilder();
		double rcvQty = 0;
		// SQL语句连接
		String sql = "SELECT SUM(RCV_QTY) AS RCV_QTY FROM PUR_J_ARRIVAL_DETAILS  WHERE MIF_NO = '"
			+ mifNo +"' AND IS_USE = 'Y' ";

		List<PurJArrivalDetails> lst = bll.queryByNativeSQL(sql);  

		if (lst!=null) {
			Iterator it = lst.iterator();
			while (it.hasNext()) {
				rcvQty = Double.parseDouble(it.next().toString()); 
			}
		}

		return rcvQty;
	}
	
	/**
	 * 单据编号自动采番
	 *
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	@SuppressWarnings("unchecked")
	public String  findBy(final int... rowStartIdxAndCount) {
		StringBuilder sbd = new StringBuilder();
		// SQL语句连接
		String sql = "select max(MIF_NO) from PUR_J_ARRIVAL_DETAILS";
		String mifNo = bll.getSingal(sql).toString() + "1";   
//		String mifNo = "nova0001";

		return mifNo;
	}
	
	/**
	 * 到货单信息查询
	 *
	 * @param mifNo 到货单号
	 * @param enterpriseCode 企业编码
     * @return ReceiveGoodsBean 到货单明细
	 */
	public ReceiveGoodsBean findForReceiveGoods(String mifNo,String enterpriseCode){
		
		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		ReceiveGoodsBean receiveGoodsBean = new ReceiveGoodsBean(); 
		List receiveGoodsList=new ArrayList();
		// 数字输出格式化
		String pattern = "###,###,###,###,##0.00";
		DecimalFormat df = new DecimalFormat(pattern);
		String nullNumber = "0.00";
		try{
			String sql="select a.MATERIAL_NO,a.MATERIAL_NAME,a.SPEC_NO,a.STOCK_UM_ID,c.PUR_QTY, \n"+
					           "r.RCV_QTY,c.PUR_QTY-r.RCV_QTY,d.THE_QTY,d.LOT_CODE,d.MEMO \n"+
                       "from  INV_C_MATERIAL a,PUR_J_ORDER_DETAILS c,PUR_J_ARRIVAL_DETAILS d, \n"+
                              "(select sum(b.RCV_QTY) RCV_QTY,b.PUR_LINE PUR_LINE \n"+
	                           "from PUR_J_ARRIVAL_DETAILS b \n"+
	                           "where b.IS_USE='Y' and b.ENTERPRISE_CODE='"+ enterpriseCode +"' and b.PUR_NO = \n"+
									"(select PUR_J_ARRIVAL_DETAILS.PUR_NO \n"+
									 "from PUR_J_ARRIVAL_DETAILS \n"+
									 "where PUR_J_ARRIVAL_DETAILS.MIF_NO = '"+ mifNo +"' \n"+
									       "and PUR_J_ARRIVAL_DETAILS.IS_USE = 'Y' \n"+
										   "and PUR_J_ARRIVAL_DETAILS.ENTERPRISE_CODE = '"+ enterpriseCode +"' \n"+
									       "and rownum = 1) \n"+
	                          "group by b.PUR_LINE) r \n"+
                       "where a.IS_USE='Y' \n"+
                              "and c.IS_USE='Y' \n"+
                              "and d.IS_USE='Y' \n"+
                              "and a.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
                              "and c.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
                              "and d.ENTERPRISE_CODE='"+ enterpriseCode +"' \n"+
                              "and r.PUR_LINE = c.PUR_ORDER_DETAILS_ID \n"+
                              "and a.MATERIAL_ID = d.MATERIAL_ID \n"+
                              "and d.PUR_LINE = r.PUR_LINE \n"+
                              "and d.MIF_NO='"+ mifNo +"' \n"+
                              "order by a.MATERIAL_NO ASC";
			List list=bll.queryByNativeSQL(sql);
			Iterator it=list.iterator();
			while(it.hasNext()){
				Object[] data=(Object[])it.next();
				ReceiveGoodsListBean model = new ReceiveGoodsListBean();
				if(data[0]!=null){
					model.setMaterialNo(data[0].toString());
				}
				if(data[1]!=null){
					model.setMaterialName(data[1].toString());
				}
				if(data[2]!=null){
					model.setSpecNo(data[2].toString());
				}
				if(data[3]!=null){
					model.setStockUmID(data[3].toString());
				}
				if(data[4]!=null){
					model.setPurcharseQuantity(df.format(data[4]));
				}else{
					model.setPurcharseQuantity(nullNumber);
				}
				if(data[5]!=null){
					model.setAchieveQuantity(df.format(data[5]));
				}else{
					model.setAchieveQuantity(nullNumber);
				}
				if(data[6]!=null){
					model.setPrepareQuantity(df.format(data[6]));
				}else{
					model.setPrepareQuantity(nullNumber);
				}
				if(data[7]!=null){
					model.setTheQuantity(df.format(data[7]));
				}else{
					model.setTheQuantity(nullNumber);
				}
				if(data[8]!=null){
					model.setBatchNumber(data[8].toString());
				}
				if(data[9]!=null){
					model.setMeno(data[9].toString());
				}
				receiveGoodsList.add(model);
			}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}
		receiveGoodsBean.setReceiveGoodsList(receiveGoodsList);
		try{
			String sql = "select a.PUR_NO,a.CONTRACT_NO,b.CLIENT_NAME,a.MEMO\n"+
            			 "from PUR_J_ARRIVAL a,CON_J_CLIENTS_INFO b\n"+
            			 "where a.ARRIVAL_NO = '"+ mifNo +"'\n"+
            			 "and a.IS_USE = 'Y'\n"+
            			 "and b.IS_USE = 'Y'\n"+
            			 "and a.ENTERPRISE_CODE = '"+ enterpriseCode +"'\n"+ 
            			 "and b.ENTERPRISE_CODE = '"+ enterpriseCode +"'\n"+
            			 "and b.CLIEND_ID = a.SUPPLIER";
			List list=bll.queryByNativeSQL(sql);
			Iterator it=list.iterator();
			while(it.hasNext()){
				Object[] data=(Object[])it.next();
				if(data[0]!=null){
					receiveGoodsBean.setPurNo(data[0].toString());
				}
				if(data[1]!=null){
					receiveGoodsBean.setContractNo(data[1].toString());
				}
				if(data[2]!=null){
					receiveGoodsBean.setClientName(data[2].toString());
				}
				if(data[3]!=null){
					receiveGoodsBean.setMeno(data[3].toString());
				}
			}
		}catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}return receiveGoodsBean;
	}

	public List<MRPArriveCangoDetailInfo> findForReceiveGoodsByDetailID(
			Long requirementDetailId) {
		List<MRPArriveCangoDetailInfo> receiveGoodsList=new ArrayList<MRPArriveCangoDetailInfo>();
		String sql = "select distinct d.mif_no,d.BAD_QTY,d.rcv_qty,d.rec_qty,d.the_qty,d.item_status\n" +
			"from mrp_j_plan_requirement_detail a , PUR_J_PLAN_ORDER b ,pur_j_order_details c ,PUR_J_ARRIVAL_DETAILS d\n" + 
			"where a.requirement_detail_id =b.requirement_detail_id and c.pur_no = b.pur_no and d.pur_no = c.pur_no " + 
			"and a.requirement_detail_id = '" +requirementDetailId+"'\n"+
			// add by liuyi 091104 
			 " and c.pur_order_details_id=b.pur_order_details_id \n"
			+ " and d.material_id=a.material_id " + 
			"order by d.mif_no";
		List list=bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Object[] data = (Object[]) it.next();
			MRPArriveCangoDetailInfo model = new MRPArriveCangoDetailInfo();
			if(data[0] != null) {
				model.setMifNo(data[0].toString());
			}
			if(data[1] != null) {
				model.setBadQty(data[1].toString());
			}
			if(data[2] != null) {
				model.setRcvQty(data[2].toString());
			}
			if(data[3] != null) {
				model.setRecQty(data[3].toString());
			}
			if(data[4] != null) {
				model.setTheQty(data[4].toString());
			}
			if(data[5] != null) {
				model.setItemStatus(data[5].toString());
			}
			receiveGoodsList.add(model);
		}
		return receiveGoodsList;
	}
	
}