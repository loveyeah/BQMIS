package power.ejb.administration;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdCCarmendWh.
 * 
 * @see power.ejb.administration.AdCCarmendWh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdCCarmendWhFacade implements AdCCarmendWhFacadeRemote {
 
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * 保存数据
	 * @param entity
	 *            AdCCarmendWh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCCarmendWh entity) throws SQLException{
		LogUtil.log("saving AdCCarmendWh instance", Level.INFO, null);
		try {
			entity.setId(getAdCCarmendWhId());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw new SQLException();
		}
	}
	/**
	 *  取得id
	 * @return
	 * @throws SQLException
	 */
    private Long getAdCCarmendWhId() throws SQLException{
    	 LogUtil.log("EJB:取得新的车辆维护单位id开始。", Level.INFO, null);
         try {
             // 取得id
             Long lngId = bll.getMaxId("AD_C_CARMEND_WH", "id");
             LogUtil.log("EJB:取得新的车辆维护单位id正常结束。", Level.INFO, null);
             // 返回id
             return lngId;
         } catch (RuntimeException er) {
             LogUtil.log("EJB:取得新的车辆维护单位id异常结束。", Level.SEVERE, er);
             throw new SQLException();
         }
	}
	/**
	 * Delete a persistent AdCCarmendWh entity.
	 * 
	 * @param entity
	 *            AdCCarmendWh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCCarmendWh entity) {
		LogUtil.log("deleting AdCCarmendWh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdCCarmendWh.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新数据
	 * @param entity
	 *            AdCCarmendWh entity to update
	 * @return AdCCarmendWh the persisted AdCCarmendWh entity instance, may not
	 *         be the same
	 * @throws DataFormatException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCCarmendWh update(AdCCarmendWh entity) throws DataFormatException, SQLException {
		LogUtil.log("updating AdCCarmendWh instance", Level.INFO, null);
		try {
			AdCCarmendWh result = new AdCCarmendWh();
				result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public AdCCarmendWh findById(Long id) {
		LogUtil.log("finding AdCCarmendWh instance with id: " + id, Level.INFO,
				null);
		try {
			AdCCarmendWh instance = entityManager.find(AdCCarmendWh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdCCarmendWh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCCarmendWh property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCCarmendWh> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdCCarmendWh> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdCCarmendWh instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdCCarmendWh model where model."
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
	 * Find all AdCCarmendWh entities.
	 * 
	 * @return List<AdCCarmendWh> all AdCCarmendWh entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdCCarmendWh> findAll() {
		LogUtil.log("finding all AdCCarmendWh instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdCCarmendWh model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
     * 通过是否使用查找车辆维护单位
     * @return
     * @throws SQLException
     */
	@SuppressWarnings("unchecked")
	public PageObject findByIsUse(String strEnterPriseCode,int start,int limit) throws SQLException {
		LogUtil.log("EJB:车辆维护单位检索开始。", Level.INFO, null);
		try {
			// 新规PageObject
			PageObject result = new PageObject();
			// 检索数据sql文
			String sql = "SELECT " 
				+ "ID, "
				+ "CP_CODE, "
				+ "CP_NAME, "
				+ "CP_ADDRESS, "
				+ "CON_TEL, "
				+ "CONNMAN, "
				+ "BSN_RANGER, "
				+ "RETRIEVE_CODE, "
				+ "IS_USE, "
				+ "UPDATE_USER, "
				+ "UPDATE_TIME, "
				+ "ENTERPRISE_CODE "
				+ "FROM AD_C_CARMEND_WH  "
				+ "WHERE IS_USE = ? "
				+ "AND ENTERPRISE_CODE = ?";
			LogUtil.log("EJB:SQL= " + sql, Level.INFO, null);
			Object[] params = {"Y",strEnterPriseCode};
			// 取得数据
			List<AdCCarmendWh> list = bll.queryByNativeSQL(sql, params,
					AdCCarmendWh.class, start, limit);
			// 检索总条数sql文
			String sqlCount = "SELECT " 
				    + "COUNT(ID) "
					+ "FROM " 
					+ "AD_C_CARMEND_WH  " 
					+ "WHERE IS_USE = ? "
					+ "AND ENTERPRISE_CODE = ?";
			// 取得总条数
			Object countList = bll.getSingal(sqlCount, params);
			result.setList(list);
			if(null != countList){
				result.setTotalCount(new Long(countList.toString()));
			}
			LogUtil.log("EJB:车辆维护单位检索正常结束。", Level.INFO, null);
			// 返回数据
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:车辆维护单位检索异常结束。", Level.SEVERE, null);
			throw new SQLException();
		}

	}

}