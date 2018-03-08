package power.ejb.resource;

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
 * Facade for entity MrpCPlanOriginal.
 * 
 * @see power.ejb.resource.MrpCPlanOriginal
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class MrpCPlanOriginalFacade implements MrpCPlanOriginalFacadeRemote {
    // property constants
    public static final String PARENT_ID = "parentId";
    public static final String PLAN_ORIGINAL_DESC = "planOriginalDesc";
    public static final String ORIGINAL_TYPE = "originalType";
    public static final String IS_USE = "isUse";
    public static final String ENTERPRISE_CODE = "enterpriseCode";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /**
     * Perform an initial save of a previously unsaved MrpCPlanOriginal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            MrpCPlanOriginal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(MrpCPlanOriginal entity) {
        LogUtil.log("saving MrpCPlanOriginal instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent MrpCPlanOriginal entity.
     * 
     * @param entity
     *            MrpCPlanOriginal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(MrpCPlanOriginal entity) {
        LogUtil.log("deleting MrpCPlanOriginal instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(MrpCPlanOriginal.class, entity.getPlanOriginalId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved MrpCPlanOriginal entity and return it or a
     * copy of it to the sender. A copy of the MrpCPlanOriginal entity parameter
     * is returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *            MrpCPlanOriginal entity to update
     * @return MrpCPlanOriginal the persisted MrpCPlanOriginal entity instance,
     *         may not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public MrpCPlanOriginal update(MrpCPlanOriginal entity) {
        LogUtil.log("updating MrpCPlanOriginal instance", Level.INFO, null);
        try {
            MrpCPlanOriginal result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public MrpCPlanOriginal findById(Long id) {
        LogUtil.log("finding MrpCPlanOriginal instance with id: " + id, Level.INFO, null);
        try {
            MrpCPlanOriginal instance = entityManager.find(MrpCPlanOriginal.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all MrpCPlanOriginal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the MrpCPlanOriginal property to query
     * @param value
     *            the property value to match
     * @return List<MrpCPlanOriginal> found by query
     */
    @SuppressWarnings("unchecked")
    public List<MrpCPlanOriginal> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding MrpCPlanOriginal instance with property: " + propertyName + ", value: " + value,
                Level.INFO, null);
        try {
            final String queryString = "select model from MrpCPlanOriginal model where model." + propertyName
                    + "= :propertyValue and IS_USE= 'Y'";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<MrpCPlanOriginal> findByParentId(Long parentId, String enterpriseCode) {
        try {
            String sql = "select model.* from MRP_C_PLAN_ORIGINAL model " +
            		"where model.PARENT_ID= '"+parentId+"'"
            		+ "and model.IS_USE= 'Y' "
            		+ "and model.ENTERPRISE_CODE='"+enterpriseCode+"' "
            		+"order by model.PLAN_ORIGINAL_ID";
            List<MrpCPlanOriginal> listOriainal=bll.queryByNativeSQL(sql,MrpCPlanOriginal.class);
            return listOriainal;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public List<MrpCPlanOriginal> findByPlanOriginalDesc(Object planOriginalDesc) {
        return findByProperty(PLAN_ORIGINAL_DESC, planOriginalDesc);
    }

    public List<MrpCPlanOriginal> findByOriginalType(Object originalType) {
        return findByProperty(ORIGINAL_TYPE, originalType);
    }

    public List<MrpCPlanOriginal> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    public List<MrpCPlanOriginal> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    public List<MrpCPlanOriginal> findByLastModifiedBy(Object lastModifiedBy) {
        return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
    }

    /**
     * Find all MrpCPlanOriginal entities.
     * 
     * @return List<MrpCPlanOriginal> all MrpCPlanOriginal entities
     */
    @SuppressWarnings("unchecked")
    public List<MrpCPlanOriginal> findAll() {
        LogUtil.log("finding all MrpCPlanOriginal instances", Level.INFO, null);
        try {
            final String queryString = "select model from MrpCPlanOriginal model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

}