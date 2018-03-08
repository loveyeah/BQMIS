package power.ejb.administration;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJEmployeesignpicture.
 * 
 * @see power.ejb.administration.AdJEmployeesignpicture
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJEmployeesignpictureFacade implements
		AdJEmployeesignpictureFacadeRemote {
	// property constants

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved AdJEmployeesignpicture
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            AdJEmployeesignpicture entity to persist
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJEmployeesignpicture entity) throws SQLException {
		LogUtil.log("EJB:保存个性签名信息开始。", Level.INFO, null);
		try {
			if(entity.getId()==null)
            {
                // 设置流水号
                entity.setId(bll.getMaxId("AD_J_EMPLOYEESIGNPICTURE", "ID"));
            }
            // 设置修改时间
            entity.setUpdateTime(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("EJB:保存个性签名信息结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:保存个性签名信息失败。", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdJEmployeesignpicture entity.
	 * 
	 * @param entity
	 *            AdJEmployeesignpicture entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJEmployeesignpicture entity) {
		LogUtil.log("deleting AdJEmployeesignpicture instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(AdJEmployeesignpicture.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJEmployeesignpicture entity and return it or
	 * a copy of it to the sender. A copy of the AdJEmployeesignpicture entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            AdJEmployeesignpicture entity to update
	 * @return AdJEmployeesignpicture the persisted AdJEmployeesignpicture
	 *         entity instance, may not be the same
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJEmployeesignpicture update(AdJEmployeesignpicture entity) throws SQLException {
		LogUtil.log("EJB:更新个性签名信息开始。", Level.INFO,
				null);
		try {
			// 设置修改时间
            entity.setUpdateTime(new java.util.Date());
			AdJEmployeesignpicture result = entityManager.merge(entity);
			LogUtil.log("更新个性签名信息结束。", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("更新个性签名信息失败。", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public AdJEmployeesignpicture findById(Long id) {
		LogUtil.log("finding AdJEmployeesignpicture instance with id: " + id, Level.INFO,
				null);
		try {
			AdJEmployeesignpicture instance = entityManager.find(AdJEmployeesignpicture.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * Find all AdJEmployeesignpicture entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJEmployeesignpicture property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJEmployeesignpicture> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJEmployeesignpicture> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJEmployeesignpicture instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJEmployeesignpicture model where model."
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
	 * Find all AdJEmployeesignpicture entities.
	 * 
	 * @return List<AdJEmployeesignpicture> all AdJEmployeesignpicture entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJEmployeesignpicture> findAll() {
		LogUtil.log("finding all AdJEmployeesignpicture instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from AdJEmployeesignpicture model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 从人员编码查询个性签名
	 *
	 * @param strWorkCode 人员编码
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 派车单
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByWorkCode(String strWorkCode, final int... rowStartIdxAndCount)
	throws ParseException {
		LogUtil.log("EJB:查询个性签名实例开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		String sql;
		Object[] params = new Object[2];
		params[0] = 'Y';
		params[1] = strWorkCode;
		sql = "SELECT A.ID " +
				"FROM AD_J_EMPLOYEESIGNPICTURE  A " +
				"WHERE A.IS_USE = ? AND " +
				"A.WORKER_CODE = ? ";
		List<AdJEmployeesignpicture> list = bll.queryByNativeSQL(sql, params);
		List<AdJEmployeesignpicture> lstADJ= new ArrayList<AdJEmployeesignpicture>();
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				Object[] data = (Object[]) it.next();
				AdJEmployeesignpicture adJEmployeesignpicture = new AdJEmployeesignpicture();
				// 序号
				if (data[0] != null) {
					Long id = Long.parseLong(data[0].toString());
					adJEmployeesignpicture = findById(id);
				}

				lstADJ.add(adJEmployeesignpicture);
			}
		}
		if(lstADJ.size()>0)
		{
			// 符合条件的个性签名
			pobj.setList(lstADJ);
			// 符合条件的个性签名的总数 
			pobj.setTotalCount(Long.parseLong(lstADJ.size() + ""));
		}	
		LogUtil.log("EJB:查询个性签名实例结束", Level.INFO, null);
		return pobj;
	}
}