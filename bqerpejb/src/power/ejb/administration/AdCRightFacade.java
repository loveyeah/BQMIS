/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.sql.SQLException;
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
 * Facade for entity AdCRight.
 * 
 * @see power.ejb.administration.AdCRight
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdCRightFacade implements AdCRightFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 权限重复检查
     * 
     * @param strUserCode 人员编码
     * @param strEnterpriseCode 企业代码
     * @return boolean 是否重复
     */
    public boolean checkRight(String strUserCode, String strEnterpriseCode)
			throws SQLException {
		try {
			LogUtil.log("EJB:权限重复检查开始", Level.INFO, null);
			// 是否重复标志
			boolean isOnly = false;
			// 查询SQL语句
			String strSql = "";
			// 构造查询SQL语句
			strSql = "SELECT "
				+ "COUNT(A.ID) "
				+ "FROM AD_C_RIGHT A "
				+ "WHERE "
				+ "A.USER_CODE=? "
				+ "AND A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE=?";
			// SQL语句参数
			Object[] params = new Object[3];
			params[0] = strUserCode;
			params[1] = "Y";
			params[2] = strEnterpriseCode;
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 执行查询
			Long lngTotalCount = Long.parseLong(bll.getSingal(strSql, params)
					.toString());
			// 是否存在记录
			if (lngTotalCount == 0) {
				isOnly = true;
			}
			LogUtil.log("EJB:权限重复检查结束", Level.INFO, null);
			return isOnly;
		} catch (Exception re) {
			LogUtil.log("EJB:新增失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}
    /**
	 * 新增工作权限
	 * 
	 * @param entity 新增工作权限实体
	 */
    public void save(AdCRight entity) throws SQLException {
        LogUtil.log("EJB:新增工作权限开始", Level.INFO, null);
        try {
            // 设置新工作权限序号
            entity.setId(bll.getMaxId("AD_C_RIGHT", "ID"));
            entityManager.persist(entity);
            LogUtil.log("EJB:新增工作权限结束", Level.INFO, null);
        } catch (Exception re) {
			LogUtil.log("EJB:新增失败", Level.SEVERE, re);
			throw new SQLException();
		}
    }

    /**
     * Delete a persistent AdCRight entity.
     * 
     * @param entity
     *            AdCRight entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(AdCRight entity) {
        LogUtil.log("deleting AdCRight instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(AdCRight.class, entity.getId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * 更新工作权限
     * 
     * @param entity 更新工作权限实体
     * @return 工作权限实体
     */
    public AdCRight update(AdCRight entity) throws SQLException {
        LogUtil.log("EJB:更新工作权限开始", Level.INFO, null);
        try {
            AdCRight result = entityManager.merge(entity);
            LogUtil.log("EJB:更新工作权限结束", Level.INFO, null);
            return result;
        } catch (Exception re) {
			LogUtil.log("EJB:更新失败", Level.SEVERE, re);
			throw new SQLException();
		}
    }

    /**
     * 按序号查找工作权限
     * 
     * @param id 序号
     * @return
     */
    public AdCRight findById(Long id) throws SQLException {
        LogUtil.log("EJB:按序号查找工作权限开始 序号:" + id, Level.INFO, null);
        try {
            AdCRight instance = entityManager.find(AdCRight.class, id);
            LogUtil.log("EJB:按序号查找工作权限结束", Level.INFO, null);
            return instance;
        } catch (Exception re) {
			LogUtil.log("EJB:查找失败", Level.SEVERE, re);
			throw new SQLException();
		}
    }

    /**
     * Find all AdCRight entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the AdCRight property to query
     * @param value
     *            the property value to match
     * @return List<AdCRight> found by query
     */
    @SuppressWarnings("unchecked")
    public List<AdCRight> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding AdCRight instance with property: " + propertyName
                + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from AdCRight model where model."
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
     * Find all AdCRight entities.
     * 
     * @return List<AdCRight> all AdCRight entities
     */
    @SuppressWarnings("unchecked")
    public List<AdCRight> findAll() {
        LogUtil.log("finding all AdCRight instances", Level.INFO, null);
        try {
            final String queryString = "select model from AdCRight model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

}