package power.ejb.resource;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity InvJBalance.
 * 
 * @see power.ejb.resource.InvJBalance
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJBalanceFacade implements InvJBalanceFacadeRemote {
	// property constants
	public static final String BALANCE_TYPE = "balanceType";
	public static final String WHS_NO = "whsNo";
	public static final String LOCATION_NO = "locationNo";
	public static final String BALANCE_YEAR_MONTH = "balanceYearMonth";
	public static final String BALANCE_YEAR = "balanceYear";
	public static final String TRANS_HIS_MINID = "transHisMinid";
	public static final String TRANS_HIS_MAXID = "transHisMaxid";
	public static final String BALANCE_STATUS = "balanceStatus";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 事务历史表remote */
	@EJB(beanName = "InvJTransactionHisFacade")
	protected InvJTransactionHisFacadeRemote transRemote;
	/** 批号物料记录表bean */
	@EJB(beanName = "InvJLotFacade")
	protected InvJLotFacadeRemote lotRemote;
	/** 库位物料记录表bean */
	@EJB(beanName = "InvJLocationFacade")
	protected InvJLocationFacadeRemote locationRemote;
	/** 库存物料记录表remote */
	@EJB(beanName = "InvJWarehouseFacade")
	protected InvJWarehouseFacadeRemote warehouseRemote;
	
	/**
	 * Perform an initial save of a previously unsaved InvJBalance entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJBalance entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJBalance entity) {
		LogUtil.log("saving InvJBalance instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvJBalance entity.
	 * 
	 * @param entity
	 *            InvJBalance entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJBalance entity) {
		LogUtil.log("deleting InvJBalance instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvJBalance.class, entity
					.getBalanceId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvJBalance entity and return it or a copy of
	 * it to the sender. A copy of the InvJBalance entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvJBalance entity to update
	 * @return InvJBalance the persisted InvJBalance entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJBalance update(InvJBalance entity) {
		LogUtil.log("updating InvJBalance instance", Level.INFO, null);
		try {
			InvJBalance result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJBalance findById(Long id) {
		LogUtil.log("finding InvJBalance instance with id: " + id, Level.INFO,
				null);
		try {
			InvJBalance instance = entityManager.find(InvJBalance.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJBalance entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJBalance property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJBalance> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJBalance> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvJBalance instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJBalance model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<InvJBalance> findByBalanceType(Object balanceType) {
		return findByProperty(BALANCE_TYPE, balanceType);
	}

	public List<InvJBalance> findByWhsNo(Object whsNo) {
		return findByProperty(WHS_NO, whsNo);
	}

	public List<InvJBalance> findByLocationNo(Object locationNo) {
		return findByProperty(LOCATION_NO, locationNo);
	}

	public List<InvJBalance> findByBalanceYearMonth(Object balanceYearMonth) {
		return findByProperty(BALANCE_YEAR_MONTH, balanceYearMonth);
	}

	public List<InvJBalance> findByBalanceYear(Object balanceYear) {
		return findByProperty(BALANCE_YEAR, balanceYear);
	}

	public List<InvJBalance> findByTransHisMinid(Object transHisMinid) {
		return findByProperty(TRANS_HIS_MINID, transHisMinid);
	}

	public List<InvJBalance> findByTransHisMaxid(Object transHisMaxid) {
		return findByProperty(TRANS_HIS_MAXID, transHisMaxid);
	}

	public List<InvJBalance> findByBalanceStatus(Object balanceStatus) {
		return findByProperty(BALANCE_STATUS, balanceStatus);
	}

	public List<InvJBalance> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvJBalance> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvJBalance> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvJBalance entities.
	 * 
	 * @return List<InvJBalance> all InvJBalance entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvJBalance> findAll() {
		LogUtil.log("finding all InvJBalance instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvJBalance model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查找结算主表的最大流水号，以插入新的结算记录
	 * @return Long 最大流水号
	 * @throws Exception
	 */
	public Long getMaxId(){
		return (Long) bll.getMaxId("INV_J_BALANCE", "BALANCE_ID");
	}
	/**
	 * 条件查找结算主表中最近的一条记录，获得其流水号（is_use='Y' and 企业编码='hfdc'）
	 * @param enterprisecode 企业编码
	 * @return 流水号
	 */
	public Long getLatestId(String enterprisecode) {
		String sql = 
		"SELECT\n" + 
		    "MAX(A.BALANCE_ID)\n" + 
		"FROM\n" + 
		    "INV_J_BALANCE A\n" + 
		"WHERE\n" + 
		    "A.IS_USE = 'Y' AND\n" + 
		    "A.BALANCE_TYPE = 'M' AND\n" + 
		    "A.ENTERPRISE_CODE = '" + enterprisecode + "'\n";
		Object obj = bll.getSingal(sql);
		if (null != obj) {
			return Long.parseLong(obj.toString());
		} else {
			return null;
		}
	}
	
	/**
	 * 事务方式保存下列表中数据
	 * @param listUpdateLot 批号记录表
	 * @param listUpdateLocation 库位记录表
	 * @param listUpdateWarehouse 库存记录表
	 * @param listSaveTrans 事务历史表
	 * @param balance 结算主表
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void saveAll(List<InvJLot> listUpdateLot, 
			List<InvJLocation> listUpdateLocation, 
			List<InvJWarehouse> listUpdateWarehouse,
			List<InvJTransactionHis> listSaveTrans,
			InvJBalance balance
			) throws Exception {
		try {
			// 更新批号记录表
			Iterator it1 = listUpdateLot.iterator();
			while(it1.hasNext()) {
				InvJLot entity = (InvJLot)it1.next();
				lotRemote.update(entity);
			}
			// 更新库位记录表
			Iterator it2 = listUpdateLocation.iterator();
			while(it2.hasNext()) {
				InvJLocation entity = (InvJLocation)it2.next();
				locationRemote.update(entity);
			}
			// 更新库存记录表
			Iterator it3 = listUpdateWarehouse.iterator();
			while(it3.hasNext()) {
				InvJWarehouse entity = (InvJWarehouse)it3.next();
				warehouseRemote.update(entity);
			}
			// 更新事务历史表
			Iterator it4 = listSaveTrans.iterator();
			long l = 0l;
			while(it4.hasNext()) {
				InvJTransactionHis entity = (InvJTransactionHis)it4.next();
	        	// 设置流水号
				entity.setTransHisId(transRemote.getMaxId() + l);
				transRemote.save(entity);
				l++;
			}
		} catch (Exception e){
			throw e;
		} finally {
			// 更新结算表
			update(balance);
		}
	}
	
	/**
	 * 检查批号记录表
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String  checkInvLot(String enterpriseCode,Long lastId)
	{
		String msg="";
//		String sql=
//			"select * from\n" +
//			"(\n" + 
//			"\n" + 
//			" select t.lot_no,t.whs_no,t.location_no,t.material_id,\n" + 
//			"   sum(t.receipt) receipt ,sum(t.adjust) adjust,sum(t.issue) issue,\n" + 
//			"  nvl(\n" + 
//			"  (\n" + 
//			"   select  sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and (c.trans_code='P' or c.trans_code='R')\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			" and  b.lot_no=t.lot_no and b.from_whs_no=t.whs_no and b.from_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"   ),0)  as receiptnow1,\n" + 
//			"   nvl(\n" + 
//			"\n" + 
//			"   (\n" + 
//			"    select sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.to_whs_no=d.whs_no\n" + 
//			"and c.trans_code='TT'\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"   and  b.lot_no=t.lot_no and b.to_whs_no=t.whs_no and b.to_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"   ),0) as receiptnow2,\n" + 
//			"   nvl(\n" + 
//			"   (\n" + 
//			"   select sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and c.trans_code='A'\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"   and  b.lot_no=t.lot_no and b.from_whs_no=t.whs_no and b.from_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"   ),0)  as adjustnow,\n" + 
//			"   nvl(\n" + 
//			"   (\n" + 
//			"\n" + 
//			"    select  sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and (c.trans_code='I' or c.trans_code='TT')\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			" and  b.lot_no=t.lot_no and b.from_whs_no=t.whs_no and b.from_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"\n" + 
//			"   ),0) as issuenow\n" + 
//			"\n" + 
//			" from inv_j_lot t,inv_c_warehouse a\n" + 
//			" where t.enterprise_code='"+enterpriseCode+"'\n" + 
//			" and t.is_use='Y'\n" + 
//			" and t.whs_no=a.whs_no\n" + 
//			" and a.enterprise_code='"+enterpriseCode+"'\n" + 
//			" and a.is_use='Y'\n" + 
//			" and a.is_inspect='N'\n" + 
//			" and a.is_cost='Y'\n" + 
//			" group by  t.lot_no,t.whs_no,t.location_no,t.material_id\n" + 
//			" )tt\n" + 
//			" where  tt.receipt<>tt.receiptnow1+receiptnow2\n" + 
//			" or tt.adjust<>adjustnow\n" + 
//			" or tt.issue<>issuenow";
		String sql=
			"select t2.lot_no,\n" +
			"       t2.whs_no,\n" + 
			"       t2.location_no,\n" + 
			"       t2.material_id,\n" + 
			"       t2.receipt,\n" + 
			"       t2.adjust,\n" + 
			"       t2.issue,\n" + 
			"       t1.receiptnow1,\n" + 
			"       nvl(t3.receiptnow2, 0),\n" + 
			"       t1.adjustnow,\n" + 
			"       t1.issuenow\n" + 
			"  from (\n" + 
			"\n" + 
			"        select b.lot_no,\n" + 
			"                b.from_whs_no,\n" + 
			"                b.from_location_no,\n" + 
			"                b.material_id,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'P' then\n" + 
			"                       b.trans_qty\n" + 
			"                      when 'R' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) receiptnow1,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'A' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) adjustnow,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'I' then\n" + 
			"                       b.trans_qty\n" + 
			"                      when 'TT' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) issuenow\n" + 
			"\n" + 
			"          from inv_j_transaction_his b,\n" + 
			"                inv_c_transaction     c,\n" + 
			"                inv_c_warehouse       d\n" + 
			"         where b.trans_id = c.trans_id\n" + 
			"           and b.from_whs_no = d.whs_no\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"           and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and d.is_use = 'Y'\n" + 
			"           and d.is_inspect = 'N'\n" + 
			"           and d.is_cost = 'Y'\n" + 
			"           and b.trans_his_id > "+lastId+"\n" + 
			"\n" + 
			"         group by b.lot_no, b.from_whs_no, b.from_location_no, b.material_id) t1,\n" + 
			"       (select t.lot_no,\n" + 
			"               t.whs_no,\n" + 
			"               t.location_no,\n" + 
			"               t.material_id,\n" + 
			"               sum(t.receipt) receipt,\n" + 
			"               sum(t.adjust) adjust,\n" + 
			"               sum(t.issue) issue\n" + 
			"          from inv_j_lot t\n" + 
			"         where t.is_use = 'Y'\n" + 
			"           and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"         group by t.lot_no, t.whs_no, t.location_no, t.material_id) t2,\n" + 
			"       (select b.lot_no,\n" + 
			"               b.to_whs_no,\n" + 
			"               b.to_location_no,\n" + 
			"               b.material_id,\n" + 
			"               sum(b.trans_qty) receiptnow2\n" + 
			"          from inv_j_transaction_his b,\n" + 
			"               inv_c_transaction     c,\n" + 
			"               inv_c_warehouse       d\n" + 
			"         where b.trans_id = c.trans_id\n" + 
			"           and b.from_whs_no = d.whs_no\n" + 
			"           and c.trans_code = 'TT'\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"           and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and d.is_use = 'Y'\n" + 
			"           and d.is_inspect = 'N'\n" + 
			"           and d.is_cost = 'Y'\n" + 
			"           and b.trans_his_id > "+lastId+"\n" + 
			"\n" + 
			"         group by b.lot_no, b.to_whs_no, b.to_location_no, b.material_id) t3\n" + 
			" where t1.lot_no = t2.lot_no\n" + 
			"   and t1.from_whs_no = t2.whs_no\n" + 
			"   and t1.from_location_no = t2.location_no\n" + 
			"   and t1.material_id = t2.material_id\n" + 
			"   and t2.material_id = t3.material_id(+)\n" + 
			"   and t2.lot_no = t3.lot_no(+)\n" + 
			"   and t3.to_whs_no(+) = t2.whs_no\n" + 
			"   and t3.to_location_no(+) = t2.location_no\n" + 
			"   and (t2.receipt <> t1.receiptnow1 + nvl(t3.receiptnow2, 0) or\n" + 
			"       t2.adjust <> t1.adjustnow or t2.issue <> t1.issuenow)";

			
		List list=bll.queryByNativeSQL(sql);
		if(list!=null&&list.size()>0)
		{
		Iterator it=list.iterator();
		String str="";
		while(it.hasNext())
		{
			str="";
			Object [] data=(Object [])it.next();
			if(data[4]!=null)
			{
				if(Double.parseDouble(data[4].toString())!=(Double.parseDouble(data[7].toString())+Double.parseDouble(data[8].toString())))
				{
					str+="<br/>id为："+data[3].toString()+"的物资接收数量不正确。";
				}
			}
			if(data[5]!=null)
			{
				if(!data[5].toString().equals(data[9].toString()))
				{
					str+="<br/>id为："+data[3].toString()+"的物资调整数量不正确。";
				}
			}
			if(data[6]!=null)
			{
				if(!data[6].toString().equals(data[10].toString()))
				{
					str+="<br/>id为："+data[3].toString()+"的物资出库数量不正确。";
				}
			}
			msg+=str;
		}

		}
		if(!msg.equals(""))
		{
			msg="批号记录表："+msg;
		}
		return msg;
		
	}
	/**
	 * 仓库物料记录表
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String checkInvWarehouse(String enterpriseCode,Long lastId )
	{
		String msg="";
//		String sql=
//			"select * from\n" +
//			"(\n" + 
//			"\n" + 
//			" select t.whs_no,t.material_id,\n" + 
//			"   sum(t.receipt) receipt ,sum(t.adjust) adjust,sum(t.issue) issue,\n" + 
//			"  nvl(\n" + 
//			"  (\n" + 
//			"   select  sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and (c.trans_code='P' or c.trans_code='R')\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			" and   b.from_whs_no=t.whs_no  and b.material_id=t.material_id\n" + 
//			"   ),0)  as receiptnow1,\n" + 
//			"   nvl(\n" + 
//			"\n" + 
//			"   (\n" + 
//			"    select sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.to_whs_no=d.whs_no\n" + 
//			"and c.trans_code='TT'\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"     and b.to_whs_no=t.whs_no  and b.material_id=t.material_id\n" + 
//			"   ),0) as receiptnow2,\n" + 
//			"   nvl(\n" + 
//			"   (\n" + 
//			"   select sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and c.trans_code='A'\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"   and b.from_whs_no=t.whs_no  and b.material_id=t.material_id\n" + 
//			"   ),0)  as adjustnow,\n" + 
//			"   nvl(\n" + 
//			"   (\n" + 
//			"\n" + 
//			"    select  sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and (c.trans_code='I' or c.trans_code='TT')\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"  and b.from_whs_no=t.whs_no  and b.material_id=t.material_id\n" + 
//			"\n" + 
//			"   ),0) as issuenow\n" + 
//			"\n" + 
//			" from inv_j_warehouse t,inv_c_warehouse a\n" + 
//			" where t.enterprise_code='"+enterpriseCode+"'\n" + 
//			" and t.is_use='Y'\n" + 
//			" and t.whs_no=a.whs_no\n" + 
//			" and a.enterprise_code='"+enterpriseCode+"'\n" + 
//			" and a.is_use='Y'\n" + 
//			" and a.is_inspect='N'\n" + 
//			" and a.is_cost='Y'\n" + 
//			" group by  t.whs_no,t.material_id\n" + 
//			" )tt\n" + 
//			" where\n" + 
//			" tt.receipt<>tt.receiptnow1+receiptnow2\n" + 
//			"or tt.adjust<>adjustnow\n" + 
//			" or tt.issue<>issuenow";
		String sql=
			"select t2.whs_no,\n" +
			"       t2.material_id,\n" + 
			"       t2.receipt,\n" + 
			"       t2.adjust,\n" + 
			"       t2.issue,\n" + 
			"       t1.receiptnow1,\n" + 
			"       nvl(t3.receiptnow2, 0),\n" + 
			"       t1.adjustnow,\n" + 
			"       t1.issuenow\n" + 
			"  from (\n" + 
			"\n" + 
			"        select b.from_whs_no,\n" + 
			"\n" + 
			"                b.material_id,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'P' then\n" + 
			"                       b.trans_qty\n" + 
			"                      when 'R' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) receiptnow1,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'A' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) adjustnow,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'I' then\n" + 
			"                       b.trans_qty\n" + 
			"                      when 'TT' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) issuenow\n" + 
			"\n" + 
			"          from inv_j_transaction_his b,\n" + 
			"                inv_c_transaction     c,\n" + 
			"                inv_c_warehouse       d\n" + 
			"         where b.trans_id = c.trans_id\n" + 
			"           and b.from_whs_no = d.whs_no\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"           and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and d.is_use = 'Y'\n" + 
			"           and d.is_inspect = 'N'\n" + 
			"           and d.is_cost = 'Y'\n" + 
			"           and b.trans_his_id > "+lastId+"\n" + 
			"\n" + 
			"         group by b.from_whs_no, b.material_id) t1,\n" + 
			"       (select t.whs_no,\n" + 
			"               t.material_id,\n" + 
			"               sum(t.receipt) receipt,\n" + 
			"               sum(t.adjust) adjust,\n" + 
			"               sum(t.issue) issue\n" + 
			"          from inv_j_warehouse t\n" + 
			"         where t.is_use = 'Y'\n" + 
			"           and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"         group by t.whs_no, t.material_id) t2,\n" + 
			"       (select b.to_whs_no,\n" + 
			"\n" + 
			"               b.material_id,\n" + 
			"               sum(b.trans_qty) receiptnow2\n" + 
			"          from inv_j_transaction_his b,\n" + 
			"               inv_c_transaction     c,\n" + 
			"               inv_c_warehouse       d\n" + 
			"         where b.trans_id = c.trans_id\n" + 
			"           and b.from_whs_no = d.whs_no\n" + 
			"           and c.trans_code = 'TT'\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"           and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and d.is_use = 'Y'\n" + 
			"           and d.is_inspect = 'N'\n" + 
			"           and d.is_cost = 'Y'\n" + 
			"           and b.trans_his_id > "+lastId+"\n" + 
			"\n" + 
			"         group by b.to_whs_no, b.material_id) t3\n" + 
			" where t1.from_whs_no = t2.whs_no\n" + 
			"   and t1.material_id = t2.material_id\n" + 
			"   and t2.material_id = t3.material_id(+)\n" + 
			"   and t3.to_whs_no(+) = t2.whs_no\n" + 
			"   and (t2.receipt <> t1.receiptnow1 + nvl(t3.receiptnow2, 0) or\n" + 
			"       t2.adjust <> t1.adjustnow or t2.issue <> t1.issuenow)";


		List list=bll.queryByNativeSQL(sql);
		if(list!=null&&list.size()>0)
		{
		Iterator it=list.iterator();
		String str="";
		while(it.hasNext())
		{
			str="";
			Object [] data=(Object [])it.next();
			if(data[2]!=null)
			{
				if(Double.parseDouble(data[2].toString())!=(Double.parseDouble(data[5].toString())+Double.parseDouble(data[6].toString())))
				{
					str+="<br/>id为："+data[1].toString()+"的物资接收数量不正确。";
				}
			}
			if(data[3]!=null)
			{
				if(!data[3].toString().equals(data[7].toString()))
				{
					str+="<br/>id为："+data[1].toString()+"的物资调整数量不正确。";
				}
			}
			if(data[4]!=null)
			{
				if(!data[4].toString().equals(data[8].toString()))
				{
					str+="<br/>id为："+data[1].toString()+"的物资出库数量不正确。";
				}
			}
			msg+=str;
		}

		}
		if(!msg.equals(""))
		{
			msg="仓库物料记录表："+msg;
		}
		return msg;
	}
	
	/**
	 * 库位物料记录表
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String checkInvLocation(String enterpriseCode,Long lastId)
	{
		String msg="";
//		String sql=
//			"select * from\n" +
//			"(\n" + 
//			"\n" + 
//			" select t.whs_no,t.location_no,t.material_id,\n" + 
//			"   sum(t.receipt) receipt ,sum(t.adjust) adjust,sum(t.issue) issue,\n" + 
//			"  nvl(\n" + 
//			"  (\n" + 
//			"   select  sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and (c.trans_code='P' or c.trans_code='R')\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			" and   b.from_whs_no=t.whs_no and b.from_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"   ),0)  as receiptnow1,\n" + 
//			"   nvl(\n" + 
//			"\n" + 
//			"   (\n" + 
//			"    select sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.to_whs_no=d.whs_no\n" + 
//			"and c.trans_code='TT'\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"     and b.to_whs_no=t.whs_no and b.to_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"   ),0) as receiptnow2,\n" + 
//			"   nvl(\n" + 
//			"   (\n" + 
//			"   select sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and c.trans_code='A'\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"   and b.from_whs_no=t.whs_no and b.from_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"   ),0)  as adjustnow,\n" + 
//			"   nvl(\n" + 
//			"   (\n" + 
//			"\n" + 
//			"    select  sum(b.trans_qty)\n" + 
//			"from inv_j_transaction_his b,inv_c_transaction c,inv_c_warehouse d\n" + 
//			"where b.trans_id=c.trans_id and b.from_whs_no=d.whs_no\n" + 
//			"and (c.trans_code='I' or c.trans_code='TT')\n" + 
//			"and b.enterprise_code='"+enterpriseCode+"' and b.is_use='Y'\n" + 
//			"and c.enterprise_code='"+enterpriseCode+"' and c.is_use='Y'\n" + 
//			"and d.enterprise_code='"+enterpriseCode+"' and d.is_use='Y'\n" + 
//			"and d.is_inspect='N'\n" + 
//			"and d.is_cost='Y'\n" + 
//			"and  b.trans_his_id> "+lastId+"\n" + 
////			" nvl((select t.trans_his_maxid from inv_j_balance t\n" + 
////			"where t.balance_id=(select max(balance_id) from inv_j_balance where is_use='Y' and enterprise_code='"+enterpriseCode+"')),1)\n" + 
//			"  and b.from_whs_no=t.whs_no and b.from_location_no=t.location_no and b.material_id=t.material_id\n" + 
//			"\n" + 
//			"   ),0) as issuenow\n" + 
//			"\n" + 
//			" from inv_j_location t,inv_c_warehouse a\n" + 
//			" where t.enterprise_code='"+enterpriseCode+"'\n" + 
//			" and t.is_use='Y'\n" + 
//			" and t.whs_no=a.whs_no\n" + 
//			" and a.enterprise_code='"+enterpriseCode+"'\n" + 
//			" and a.is_use='Y'\n" + 
//			" and a.is_inspect='N'\n" + 
//			" and a.is_cost='Y'\n" + 
//			" group by  t.whs_no,t.location_no,t.material_id\n" + 
//			" )tt\n" + 
//			" where\n" + 
//			" tt.receipt<>tt.receiptnow1+receiptnow2\n" + 
//			"or tt.adjust<>adjustnow\n" + 
//			" or tt.issue<>issuenow";

		String sql=
			"select t2.whs_no,\n" +
			"       t2.location_no,\n" + 
			"       t2.material_id,\n" + 
			"       t2.receipt,\n" + 
			"       t2.adjust,\n" + 
			"       t2.issue,\n" + 
			"       t1.receiptnow1,\n" + 
			"       nvl(t3.receiptnow2, 0),\n" + 
			"       t1.adjustnow,\n" + 
			"       t1.issuenow\n" + 
			"  from (\n" + 
			"\n" + 
			"        select b.from_whs_no,\n" + 
			"                b.from_location_no,\n" + 
			"                b.material_id,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'P' then\n" + 
			"                       b.trans_qty\n" + 
			"                      when 'R' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) receiptnow1,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'A' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) adjustnow,\n" + 
			"                sum(case c.trans_code\n" + 
			"                      when 'I' then\n" + 
			"                       b.trans_qty\n" + 
			"                      when 'TT' then\n" + 
			"                       b.trans_qty\n" + 
			"                      else\n" + 
			"                       0\n" + 
			"                    end) issuenow\n" + 
			"\n" + 
			"          from inv_j_transaction_his b,\n" + 
			"                inv_c_transaction     c,\n" + 
			"                inv_c_warehouse       d\n" + 
			"         where b.trans_id = c.trans_id\n" + 
			"           and b.from_whs_no = d.whs_no\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"           and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and d.is_use = 'Y'\n" + 
			"           and d.is_inspect = 'N'\n" + 
			"           and d.is_cost = 'Y'\n" + 
			"           and b.trans_his_id > "+lastId+"\n" + 
			"\n" + 
			"         group by b.from_whs_no, b.from_location_no, b.material_id) t1,\n" + 
			"       (select t.whs_no,\n" + 
			"               t.location_no,\n" + 
			"               t.material_id,\n" + 
			"               sum(t.receipt) receipt,\n" + 
			"               sum(t.adjust) adjust,\n" + 
			"               sum(t.issue) issue\n" + 
			"          from inv_j_location t\n" + 
			"         where t.is_use = 'Y'\n" + 
			"           and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"         group by t.whs_no, t.location_no, t.material_id) t2,\n" + 
			"       (select b.to_whs_no,\n" + 
			"               b.to_location_no,\n" + 
			"               b.material_id,\n" + 
			"               sum(b.trans_qty) receiptnow2\n" + 
			"          from inv_j_transaction_his b,\n" + 
			"               inv_c_transaction     c,\n" + 
			"               inv_c_warehouse       d\n" + 
			"         where b.trans_id = c.trans_id\n" + 
			"           and b.from_whs_no = d.whs_no\n" + 
			"           and c.trans_code = 'TT'\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"           and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"           and d.is_use = 'Y'\n" + 
			"           and d.is_inspect = 'N'\n" + 
			"           and d.is_cost = 'Y'\n" + 
			"           and b.trans_his_id > "+lastId+"\n" + 
			"\n" + 
			"         group by b.to_whs_no, b.to_location_no, b.material_id) t3\n" + 
			" where t1.from_whs_no = t2.whs_no\n" + 
			"   and t1.from_location_no = t2.location_no\n" + 
			"   and t1.material_id = t2.material_id\n" + 
			"   and t2.material_id = t3.material_id(+)\n" + 
			"   and t3.to_whs_no(+) = t2.whs_no\n" + 
			"   and t3.to_location_no(+) = t2.location_no\n" + 
			"   and (t2.receipt <> t1.receiptnow1 + nvl(t3.receiptnow2, 0) or\n" + 
			"       t2.adjust <> t1.adjustnow or t2.issue <> t1.issuenow)";


		List list=bll.queryByNativeSQL(sql);
		if(list!=null&&list.size()>0)
		{
		Iterator it=list.iterator();
		String str="";
		while(it.hasNext())
		{
			str="";
			Object [] data=(Object [])it.next();
			if(data[3]!=null)
			{
				if(Double.parseDouble(data[3].toString())!=(Double.parseDouble(data[6].toString())+Double.parseDouble(data[7].toString())))
				{
					str+="<br/>id为："+data[2].toString()+"的物资接收数量不正确。";
				}
			}
			if(data[4]!=null)
			{
				if(!data[4].toString().equals(data[8].toString()))
				{
					str+="<br/>id为："+data[2].toString()+"的物资调整数量不正确。";
				}
			}
			if(data[5]!=null)
			{
				if(!data[5].toString().equals(data[9].toString()))
				{
					str+="<br/>id为："+data[2].toString()+"的物资出库数量不正确。";
				}
			}
			msg+=str;
		}

		}
		if(!msg.equals(""))
		{
			msg="库位物料记录表："+msg;
		}
		return msg;
	}
	
	public void updateForBalance(String enterpriseCode,String workCode)
	{
		String sqlLot=
			"update inv_j_lot t\n" +
			"set t.open_balance=t.open_balance+t.receipt+t.adjust-t.issue,t.receipt=0,t.adjust=0,t.issue=0\n" + 
			"where t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
		bll.exeNativeSQL(sqlLot);
		String sqlWarehouse=
			"update inv_j_warehouse a\n" +
			"set a.open_balance=a.open_balance+a.receipt+a.adjust-a.issue,a.receipt=0,a.adjust=0,a.issue=0\n" + 
			"where a.enterprise_code='"+enterpriseCode+"'\n" + 
			"and a.is_use='Y'";
		bll.exeNativeSQL(sqlWarehouse);
		String sqlLocation=
			"update inv_j_location a\n" +
			"set a.open_balance=a.open_balance+a.receipt+a.adjust-a.issue,a.receipt=0,a.adjust=0,a.issue=0\n" + 
			"where a.enterprise_code='"+enterpriseCode+"'\n" + 
			"and a.is_use='Y'";
		bll.exeNativeSQL(sqlLocation);
		
		String sqlHis=
			"insert into inv_j_transaction_his t\n" +
			"(t.trans_his_id,t.order_no,t.sequence_id,t.material_id,\n" + 
			"t.trans_id,t.trans_qty,t.std_cost,t.last_modified_by,\n" + 
			"t.last_modified_date,t.enterprise_code,t.is_use)\n" + 
			"select  (select nvl(max(a.trans_his_id),0) from  inv_j_transaction_his a)+ row_number() over(order by tt.material_id),\n" + 
			" '0',0,tt.material_id,6,tt.qty,d.std_cost,'"+workCode+"',sysdate,'"+enterpriseCode+"','Y'\n" + 
			"\n" + 
			" from\n" + 
			" (\n" + 
			"\n" + 
			" select b.material_id,sum(b.open_balance+b.receipt+b.adjust-b.issue) as qty\n" + 
			"  from inv_j_warehouse b,inv_c_warehouse c\n" + 
			" where b.enterprise_code='"+enterpriseCode+"'\n" + 
			" and b.is_use='Y'\n" + 
			" and b.whs_no=c.whs_no\n" + 
			" and c.enterprise_code='"+enterpriseCode+"'\n" + 
			" and c.is_use='Y'\n" + 
			" and c.is_inspect='N'\n" + 
			" and c.is_cost='Y'\n" + 
			" group by b.material_id\n" + 
			" ) tt,inv_c_material d\n" + 
			" where\n" + 
			"   d.material_id(+)=tt.material_id\n" + 
			" and d.enterprise_code(+)='"+enterpriseCode+"' and d.is_use(+)='Y'";
		bll.exeNativeSQL(sqlHis);
		
		//add by fyyang 20100309
		String moneyHis=
			"insert into inv_j_transaction_his_money tm\n" +
			"  (tm.trans_his_id,\n" + 
			"   tm.balance_id,\n" + 
			"   tm.trans_id,\n" + 
			"   tm.material_id,\n" + 
			"   tm.std_cost,\n" + 
			"   tm.enterprise_code,\n" + 
			"   tm.is_use,\n" + 
			"   tm.openbalance_qty,\n" + 
			"   tm.openbalance_price,\n" + 
			"   tm.whs_no,\n" + 
			"   tm.location_no,\n" + 
			"   tm.nocheck_in_qty,\n" + 
			"   tm.nocheck_in_price,\n" + 
			"   tm.nocheck_out_qty,\n" + 
			"   tm.nocheck_out_price,\n" + 
			"   tm.check_out_qty,\n" + 
			"   tm.check_out_price,\n" + 
			"   tm.check_in_qty,\n" + 
			"   tm.check_in_price,tm.LAST_MODIFY_DATE)\n" + 
			"\n" + 
			"  select (select nvl(max(a.trans_his_id), 0)\n" + 
			"            from inv_j_transaction_his_money a) + row_number() over(order by tt.material_id),\n" + 
			"         (select max(ba.balance_id) \n"+
            "                from inv_j_balance ba \n"+
            "              where ba.is_use = 'Y'),\n" + 
			"         6,\n" + 
			"         tt.material_id,\n" + 
			"         d.std_cost,\n" + 
			"         '"+enterpriseCode+"',\n" + 
			"         'Y',\n" + 
			"         round(nvl(m.openbalance_qty, 0),2) + round(nvl(m.check_in_qty, 0),2) -\n" + 
			"         round(nvl(m.check_out_qty, 0),2) obqty, --期初数量\n" + 
			"         round(nvl(m.openbalance_price, 0),2) + round(nvl(m.check_in_price, 0),2) -\n" + 
			"         round(nvl(m.check_out_price, 0),2) obprice, --期初金额，\n" + 
			"         d.default_whs_no, --仓库\n" + 
			"         d.default_location_no, --库位\n" + 
			"         nvl(bb.nocheckinqty, 0), ---未审核的入库数量\n" + 
			"         nvl(bb.nocheckinprice, 0), ----未审核的入库金额\n" + 
			"         nvl(cc.nocheckoutqty, 0), --未审核的出库数量\n" + 
			"         nvl(cc.nocheckoutprice, 0), --未审核的出库金额\n" + 
			"         nvl(dd.checkoutqty, 0), ---审核的出库数量\n" + 
			"         nvl(dd.checkoutprice, 0), ---审核的出库金额\n" + 
			"         nvl(ee.checkinqty, 0), ---审核的入库数量\n" + 
			"         nvl(ee.checkinprice, 0) ---审核的入库金额\n" + 
			",sysdate \n"+
			"\n" + 
			"from (\n" +
			"\n" + 
			"       select b.material_id,\n" + 
			"               sum(b.open_balance + b.receipt + b.adjust - b.issue) as qty\n" + 
			"         from inv_j_warehouse b, inv_c_warehouse c\n" + 
			"        where b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"          and b.is_use = 'Y'\n" + 
			"          and b.whs_no = c.whs_no\n" + 
			"          and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"          and c.is_use = 'Y'\n" + 
			"          and c.is_inspect = 'N'\n" + 
			"          and c.is_cost = 'Y'\n" + 
			"        group by b.material_id) tt,\n" + 
			"      inv_c_material d,\n" + 
			"      (select *\n" + 
			"         from inv_j_transaction_his_money a\n" + 
			"        where a.is_use = 'Y'\n" + 
			"          and a.balance_id = (select max(ba.balance_id) - 1\n" + 
			"                                from inv_j_balance ba\n" + 
			"                               where ba.is_use = 'Y')) m,\n" + 
			"      (select aa.material_id,\n" + 
			"              sum(aa.trans_qty) nocheckinqty,\n" + 
			"              sum(aa.trans_qty * aa.price) nocheckinprice\n" + 
			"         from inv_j_transaction_his aa, pur_j_arrival d\n" + 
			"        where aa.trans_id = 1\n" + 
			"          and d.is_use = 'Y'\n" + 
			"          and aa.enterprise_code = '"+enterpriseCode+"'\n" + 
			"          and aa.is_use = 'Y'\n" + 
			"          and aa.arrival_no = d.arrival_no\n" + 
			"          and d.is_use = 'Y'\n" + 
			"          and (d.check_state <> '2' or d.check_state is null)\n" + 
			"        group by aa.material_id) bb,\n" + 
			"\n" + 
			"      (select aa.material_id,\n" + 
			"              sum(aa.trans_qty) nocheckoutqty,\n" + 
			"              sum(aa.trans_qty * aa.std_cost) nocheckoutprice\n" + 
			"         from inv_j_transaction_his aa\n" + 
			"        where aa.trans_id = 4\n" + 
			"          and aa.is_use = 'Y'\n" + 
			"          and (aa.check_status <> 'C' or aa.check_status is null)\n" + 
			"        group by aa.material_id) cc,\n" + 
			"      (select a.material_id,\n" + 
			"              sum(a.trans_qty) checkoutqty,\n" + 
			"              sum(a.trans_qty * a.std_cost) checkoutprice\n" + 
			"         from inv_j_transaction_his a\n" + 
			"        where a.check_status = 'C'\n" + 
			"          and a.trans_id = 4\n" + 
			"          and a.is_use = 'Y'\n" + 
			"          and a.check_date >=\n" + 
			"              (select bl.balance_date\n" + 
			"                 from inv_j_balance bl\n" + 
			"                where bl.is_use = 'Y'\n" + 
			"                  and bl.balance_id =\n" + 
			"                      (select max(ba.balance_id) - 1\n" + 
			"                         from inv_j_balance ba\n" + 
			"                        where ba.is_use = 'Y'))\n" + 
			"          and a.check_date <= sysdate\n" + 
			"        group by a.material_id) dd,\n" + 
			"      (\n" + 
			"\n" + 
			"       select a.material_id,\n" + 
			"               sum(a.trans_qty) checkinqty,\n" + 
			"               sum(a.trans_qty * a.price) checkinprice\n" + 
			"         from inv_j_transaction_his a, pur_j_arrival d\n" + 
			"        where d.check_state = '2'\n" + 
			"          and a.arrival_no = d.arrival_no\n" + 
			"          and a.trans_id = 1\n" + 
			"          and a.is_use = 'Y'\n" + 
			"          and d.is_use = 'Y'\n" + 
			"          and d.check_date >=\n" + 
			"              (select bl.balance_date\n" + 
			"                 from inv_j_balance bl\n" + 
			"                where bl.is_use = 'Y'\n" + 
			"                  and bl.balance_id =\n" + 
			"                      (select max(ba.balance_id) - 1\n" + 
			"                         from inv_j_balance ba\n" + 
			"                        where ba.is_use = 'Y'))\n" + 
			"          and d.check_date <= sysdate\n" + 
			"        group by a.material_id\n" + 
			"\n" + 
			"       ) ee\n" + 
			"where d.material_id(+) = tt.material_id\n" + 
			"  and tt.material_id = m.material_id(+)\n" + 
			"  and tt.material_id = bb.material_id(+)\n" + 
			"  and tt.material_id = cc.material_id(+)\n" + 
			"  and tt.material_id = dd.material_id(+)\n" + 
			"  and tt.material_id = ee.material_id(+)\n" + 
			"  and d.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"  and d.is_use(+) = 'Y'";

		
		bll.exeNativeSQL(moneyHis);

		
		String sqlBalance=
			"\n" +
			"update inv_j_balance tt\n" + 
			"set tt.balance_status='OK', tt.last_modified_date=sysdate,tt.last_modified_by='"+workCode+"'\n" + 
			"where tt.balance_status='ON'";
		bll.exeNativeSQL(sqlBalance);





	}
	
	public void deleteLastRecord()
	{
		String sql=
			"delete inv_j_balance t\n" +
			" where t.balance_id = (select max(balance_id) from inv_j_balance)";
       bll.exeNativeSQL(sql);
	}
}