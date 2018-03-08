package power.ejb.hr;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrJDimission.
 * 
 * @see power.ejb.hr.HrJDimission
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJDimissionFacade implements HrJDimissionFacadeRemote {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * Perform an initial save of a previously unsaved HrJDimission entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *                HrJDimission entity to persist
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void save(HrJDimission entity) {
	LogUtil.log("saving HrJDimission instance", Level.INFO, null);
	try {
	    // 设置流水号
	    entity.setDimissionid(bll.getMaxId("HR_J_DIMISSION",
			    "DIMISSIONID"));
	    // 设置插入时间
	    entity.setInsertdate(new Date());
	    // 设置上次修改时间
	    entity.setLastModifiedDate(new Date());
	    entityManager.persist(entity);
	    entityManager.persist(entity);
	    LogUtil.log("save successful", Level.INFO, null);
	} catch (RuntimeException re) {
	    LogUtil.log("save failed", Level.SEVERE, re);
	    throw re;
	}
    }

    /**
     * Delete a persistent HrJDimission entity.
     * 
     * @param entity
     *                HrJDimission entity to delete
     * @throws RuntimeException
     *                 when the operation fails
     */
    public void delete(HrJDimission entity) {
	LogUtil.log("deleting HrJDimission instance", Level.INFO, null);
	try {
	    entity = entityManager.getReference(HrJDimission.class, entity
		    .getDimissionid());
	    entityManager.remove(entity);
	    LogUtil.log("delete successful", Level.INFO, null);
	} catch (RuntimeException re) {
	    LogUtil.log("delete failed", Level.SEVERE, re);
	    throw re;
	}
    }

    /**
     * Persist a previously saved HrJDimission entity and return it or a copy of
     * it to the sender. A copy of the HrJDimission entity parameter is returned
     * when the JPA persistence mechanism has not previously been tracking the
     * updated entity.
     * 
     * @param entity
     *                HrJDimission entity to update
     * @return HrJDimission the persisted HrJDimission entity instance, may not
     *         be the same
     * @throws RuntimeException
     *                 if the operation fails
     */
    public HrJDimission update(HrJDimission entity) {
	LogUtil.log("updating HrJDimission instance", Level.INFO, null);
	try {
	    HrJDimission result = entityManager.merge(entity);
	    LogUtil.log("update successful", Level.INFO, null);
	    return result;
	} catch (RuntimeException re) {
	    LogUtil.log("update failed", Level.SEVERE, re);
	    throw re;
	}
    }

    public HrJDimission findById(Long id) {
	LogUtil.log("finding HrJDimission instance with id: " + id, Level.INFO,
		null);
	try {
	    HrJDimission instance = entityManager.find(HrJDimission.class, id);
	    return instance;
	} catch (RuntimeException re) {
	    LogUtil.log("find failed", Level.SEVERE, re);
	    throw re;
	}
    }

    /**
     * Find all HrJDimission entities with a specific property value.
     * 
     * @param propertyName
     *                the name of the HrJDimission property to query
     * @param value
     *                the property value to match
     * @return List<HrJDimission> found by query
     */
    @SuppressWarnings("unchecked")
    public List<HrJDimission> findByProperty(String propertyName,
	    final Object value) {
	LogUtil.log("finding HrJDimission instance with property: "
		+ propertyName + ", value: " + value, Level.INFO, null);
	try {
	    final String queryString = "select model from HrJDimission model where model."
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
     * Find all HrJDimission entities.
     * 
     * @return List<HrJDimission> all HrJDimission entities
     */
    @SuppressWarnings("unchecked")
    public List<HrJDimission> findAll() {
	LogUtil.log("finding all HrJDimission instances", Level.INFO, null);
	try {
	    final String queryString = "select model from HrJDimission model";
	    Query query = entityManager.createQuery(queryString);
	    return query.getResultList();
	} catch (RuntimeException re) {
	    LogUtil.log("find all failed", Level.SEVERE, re);
	    throw re;
	}
    }

    /**
     * 更新离职员工登记信息(含排他)
     * 
     * @param entity
     *                实体信息
     * @param lastModifiedDate
     *                画面端的上次修改时间
     * @return
     * @throws DataChangeException
     */
    public HrJDimission update(HrJDimission entity, String lastModifiedDate)
	    throws DataChangeException {
	try {
	    LogUtil.log("EJB:员工离职登记修改开始。", Level.INFO, null);
	    // 通过id取得DB端的上次修改时间
	    HrJDimission oldBeen = findById(entity.getDimissionid());
	    String dbLastDate = oldBeen.getLastModifiedDate().toString()
		    .substring(0, 19);
	    if (!lastModifiedDate.equals(dbLastDate)) {
		throw new DataChangeException(null);
	    }
	    // 设置上次修改时间
	    entity.setLastModifiedDate(new Date());
	    HrJDimission result = update(entity);
	    LogUtil.log("EJB:员工离职登记修改结束。", Level.INFO, null);
	    return result;
	} catch (RuntimeException re) {
	    LogUtil.log("EJB:员工离职登记修改失败。", Level.INFO, re);
	    throw re;
	}
    }

}