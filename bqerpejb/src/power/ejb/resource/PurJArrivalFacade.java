package power.ejb.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.ArriveCangoDetailInfo;

/**
 * Facade for entity PurJArrival.
 * 
 * @see power.ejb.resource.PurJArrival
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PurJArrivalFacade implements PurJArrivalFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PurJArrival entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PurJArrival entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PurJArrival entity) {
		LogUtil.log("saving PurJArrival instance", Level.INFO, null);
		try {
//			String sql = "select case when  max(ID) is  null then 1 else max(ID)+1 end from PUR_J_ARRIVAL";
//			entity.setId(Long.parseLong(bll.getSingal(sql).toString()));
//			// 单据编号自动采番
//			String sqll = "select max(ARRIVAL_NO) from PUR_J_ARRIVAL";
//			String s = bll.getSingal(sqll).toString() + "1";
//			entity.setArrivalNo(s);
//			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PurJArrival entity.
	 * 
	 * @param entity
	 *            PurJArrival entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PurJArrival entity) {
		LogUtil.log("deleting PurJArrival instance", Level.INFO, null);

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
	 * Delete a persistent PurJArrival entity.
	 * 
	 * @param entity
	 *            PurJArrival entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void deleteMuti(Long id) {
		LogUtil.log("deleting PurJArrival instance", Level.INFO, null);

		try {
			String sql = "update PUR_J_ARRIVAL t\n"
				+ "set t.is_use='N'\n" + "where t.ID in("
				+ id + ")";
			bll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询采购单编号.
	 * 
	 * @param mifNo 到货单号
	 * @throws CodeRepeatException
	 */
	public String findByMifNo(String argMifNo) throws CodeRepeatException{
		LogUtil.log("finding PurJArrival instance with mifNo: " + argMifNo, Level.INFO,
				null);
		try {
			String purNo = null;
			String sql = "select PUR_NO from PUR_J_ARRIVAL where ARRIVAL_NO = '"+argMifNo+"'";
			List<PurJArrival> lst = bll.queryByNativeSQL(sql);
			if (lst!=null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					purNo = (String) it.next(); 
				}
			}

			return purNo;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询到货单详细id
	 * 
	 * @throws CodeRepeatException
	 */
	public Long findMaxId() throws CodeRepeatException{
		LogUtil.log("finding PurJArrival instance with id: ", Level.INFO,
				null);
		try {
			Long id = bll.getMaxId("PUR_J_ARRIVAL", "ID") - 1;
			return id;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询到货单编号
	 * 
	 * @throws CodeRepeatException
	 */
	public String findMaxArrivalNo() throws CodeRepeatException{
		LogUtil.log("finding PurJArrival instance with arrivalN0: ", Level.INFO,
				null);
		try {

			Long id = bll.getMaxId("PUR_J_ARRIVAL", "ID");
			String sql = "select max(ARRIVAL_NO) from PUR_J_ARRIVAL";
			String arrivalNo = bll.getSingal(sql).toString();
			return arrivalNo;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询到货登记bean
	 * 
	 * @param mifNo 到货单号
	 * 
	 * @throws CodeRepeatException
	 */
	public PurJArrival findByArrivalNo(String argArrivalNo) throws CodeRepeatException{
		LogUtil.log("finding PurJArrival instance with arrivalNo: " + argArrivalNo, Level.INFO,
				null);
		PurJArrival purJArrival = new PurJArrival();
		try {
			String sql = "select ID,ARRIVAL_NO,PUR_NO,CONTRACT_NO,INVOICE_NO,RECEIVE_BY," +
					"ARRIVAL_DATE,SUPPLIER,ARRIVAL_STATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE," +
					"MEMO,ENTERPRISE_CODE,IS_USE from PUR_J_ARRIVAL where ARRIVAL_NO = '"+argArrivalNo+"'";
			List<PurJArrival> lst = bll.queryByNativeSQL(sql);
			if (lst!=null) {
				Iterator it = lst.iterator();
				while (it.hasNext()) {
					Object[] data =  (Object[]) it.next(); 
					if(null != data[0]){
						purJArrival.setId(Long.parseLong(data[0].toString()));
					}
					if(null != data[1]){
						purJArrival.setArrivalNo(data[1].toString());
					}
					if(null != data[2]){
						purJArrival.setPurNo(data[2].toString());
					}
					if(null != data[3]){
						purJArrival.setContractNo(data[3].toString());
					}
					if(null != data[4]){
						purJArrival.setInvoiceNo(data[4].toString());
					}
					if(null != data[5]){
						purJArrival.setReceiveBy(data[5].toString());
					}
					if(null != data[6]){
						SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd");
						purJArrival.setArrivalDate(sdfFrom.parse(data[6].toString()));
					}
					if(null != data[7]){
						purJArrival.setSupplier(Long.parseLong(data[7].toString()));
					}
					if(null != data[8]){
						purJArrival.setArrivalState(data[8].toString());
					}
					if(null != data[9]){
						purJArrival.setLastModifiedBy(data[9].toString());
					}
					if(null != data[10]){
						SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd");
						purJArrival.setLastModifiedDate(sdfFrom.parse(data[10].toString()));
					}
					if(null != data[11]){
						purJArrival.setMemo(data[11].toString());
					}
					if(null != data[12]){
						purJArrival.setEnterpriseCode(data[12].toString());
					}
					if(null != data[13]){
						purJArrival.setIsUse(data[13].toString());
					}
				}
			}

		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return purJArrival;
	}
	/**
	 * Persist a previously saved PurJArrival entity and return it or a copy of
	 * it to the sender. A copy of the PurJArrival entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PurJArrival entity to update
	 * @return PurJArrival the persisted PurJArrival entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PurJArrival update(PurJArrival entity) {
		LogUtil.log("updating PurJArrival instance", Level.INFO, null);
		try {
			PurJArrival result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PurJArrival findById(Long id) {
		LogUtil.log("finding PurJArrival instance with id: " + id, Level.INFO,
				null);
		try {
			PurJArrival instance = entityManager.find(PurJArrival.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurJArrival entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurJArrival property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurJArrival> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PurJArrival> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PurJArrival instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PurJArrival model where model."
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
	 * Find all PurJArrival entities.
	 * 
	 * @return List<PurJArrival> all PurJArrival entities
	 */
	@SuppressWarnings("unchecked")
	public List<PurJArrival> findAll() {
		LogUtil.log("finding all PurJArrival instances", Level.INFO, null);
		try {
			final String queryString = "select model from PurJArrival model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}