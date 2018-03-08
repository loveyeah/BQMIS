package power.ejb.administration;

import java.sql.SQLException;
import java.util.Date;
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
 * Facade for entity AdCCarwhPro.
 * 
 * @see power.ejb.administration.AdCCarwhPro
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdCCarwhProFacade implements AdCCarwhProFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved AdCCarwhPro entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdCCarwhPro entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCCarwhPro entity) throws SQLException{
	LogUtil.log("EJB:维修项目维护保存数据开始", Level.INFO, null);
	try {
		entity.setId(bll.getMaxId(
				"AD_C_CARWH_PRO", "ID"));
		entity.setIsUse("Y");
		entity.setUpdateTime(new Date());
		entityManager.merge(entity);
		LogUtil.log("EJB:维修项目维护保存数据结束", Level.INFO, null);
	} catch (RuntimeException re) {
		LogUtil.log("EJB:维修项目维护保存数据失败", Level.INFO, re);
		throw new SQLException();
	}
}
	/**
	 * Delete a persistent AdCCarwhPro entity.
	 * 
	 * @param entity
	 *            AdCCarwhPro entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCCarwhPro entity) throws SQLException{
		LogUtil.log("EJB:维修项目维护删除数据开始", Level.INFO, null);
		try {
			//逻辑删除
			entity.setIsUse("N");
			entity.setUpdateTime(new Date());
			entityManager.merge(entity);
			LogUtil.log("EJB:维修项目维护删除数据结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:维修项目维护删除数据失败", Level.INFO, re);
			throw new SQLException();
		}
	}

	/**
	 * Persist a previously saved AdCCarwhPro entity and return it or a copy of
	 * it to the sender. A copy of the AdCCarwhPro entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdCCarwhPro entity to update
	 * @return AdCCarwhPro the persisted AdCCarwhPro entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCCarwhPro update(AdCCarwhPro entity) throws SQLException {
		LogUtil.log("EJB:维修项目维护修改数据开始", Level.INFO, null);
		try {
			entity.setIsUse("Y");
			entity.setUpdateTime(new Date());
			AdCCarwhPro result = entityManager.merge(entity);
			LogUtil.log("EJB:维修项目维护修改数据结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:维修项目维护修改数据失败", Level.INFO, null);
			throw new SQLException();
		}
	}

	public AdCCarwhPro findById(Long id) {
		LogUtil.log("finding AdCCarwhPro instance with id: " + id, Level.INFO,
				null);
		try {
			AdCCarwhPro instance = entityManager.find(AdCCarwhPro.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdCCarwhPro entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCCarwhPro property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCCarwhPro> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdCCarwhPro> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdCCarwhPro instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdCCarwhPro model where model."
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
	 * Find all AdCCarwhPro entities.
	 * 
	 * @return List<AdCCarwhPro> all AdCCarwhPro entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,final int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:维修项目维护查询开始", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			Object[] params = new Object[2];
			params[0] = 'Y';
			params[1] = enterpriseCode;
            // 查询sql
            String sql= "SELECT * "
            		+" FROM AD_C_CARWH_PRO t "
            		+" WHERE t.IS_USE= ? "
            		+ " and  t.enterprise_code=? "
            		+" ORDER BY t.PRO_CODE";
            String sqlCount = "SELECT COUNT(ID) "
            			+" FROM AD_C_CARWH_PRO t "
            			+ " WHERE t.IS_USE=?"
            			+ " and  t.enterprise_code=? ";
            // 执行查询
            List<AdCCarwhPro> list = bll.queryByNativeSQL(sql,params, AdCCarwhPro.class,rowStartIdxAndCount);
            // 设置PageObject
            result.setList(list);
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,params)
					.toString());
            result.setTotalCount(totalCount);
            LogUtil.log("维修项目维护查询结束", Level.SEVERE, null);
            // 返回
            return result;
		} catch (RuntimeException re) {
			LogUtil.log("维修项目维护查询失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

}