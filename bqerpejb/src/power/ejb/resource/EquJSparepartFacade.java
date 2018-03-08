package power.ejb.resource;

import java.util.Date;
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
 * Facade for entity EquJSparepart.
 * 
 * @see power.ejb.resource.EquJSparepart
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJSparepartFacade implements EquJSparepartFacadeRemote {
    // property constants
    public static final String EQUCLASS_CODE = "equclassCode";
    public static final String ATTRIBUTE_CODE = "attributeCode";
    public static final String MATERIAL_ID = "materialId";
    public static final String QTY = "qty";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String ENTERPRISE_CODE = "enterpriseCode";
    public static final String IS_USE = "isUse";

    @PersistenceContext
    private EntityManager entityManager;
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;


    /**
     * Perform an initial save of a previously unsaved EquJSparepart entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            EquJSparepart entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(EquJSparepart entity) {
        LogUtil.log("saving EquJSparepart instance", Level.INFO, null);
        try {
            entityManager.persist(entity);
            LogUtil.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Delete a persistent EquJSparepart entity.
     * 
     * @param entity
     *            EquJSparepart entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(EquJSparepart entity) {
        LogUtil.log("deleting EquJSparepart instance", Level.INFO, null);
        try {
            entity = entityManager.getReference(EquJSparepart.class, entity.getEquSparepartId());
            entityManager.remove(entity);
            LogUtil.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Persist a previously saved EquJSparepart entity and return it or a copy
     * of it to the sender. A copy of the EquJSparepart entity parameter is
     * returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *            EquJSparepart entity to update
     * @return EquJSparepart the persisted EquJSparepart entity instance, may
     *         not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public EquJSparepart update(EquJSparepart entity) {
        LogUtil.log("updating EquJSparepart instance", Level.INFO, null);
        try {
            EquJSparepart result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public EquJSparepart findById(Long id) {
        LogUtil.log("finding EquJSparepart instance with id: " + id, Level.INFO, null);
        try {
            EquJSparepart instance = entityManager.find(EquJSparepart.class, id);
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
     * Find all EquJSparepart entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the EquJSparepart property to query
     * @param value
     *            the property value to match
     * @return List<EquJSparepart> found by query
     */
    @SuppressWarnings("unchecked")
    public List<EquJSparepart> findByProperty(String propertyName, final Object value) {
        LogUtil.log("finding EquJSparepart instance with property: " + propertyName + ", value: " + value, Level.INFO,
                null);
        try {
            final String queryString = "select model from EquJSparepart model where model." + propertyName
                    + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<EquJSparepart> findByEquclassCode(Object equclassCode) {
        return findByProperty(EQUCLASS_CODE, equclassCode);
    }

    public List<EquJSparepart> findByAttributeCode(Object attributeCode) {
        return findByProperty(ATTRIBUTE_CODE, attributeCode);
    }

    public List<EquJSparepart> findByMaterialId(Object materialId) {
        return findByProperty(MATERIAL_ID, materialId);
    }

    public List<EquJSparepart> findByQty(Object qty) {
        return findByProperty(QTY, qty);
    }

    public List<EquJSparepart> findByLastModifiedBy(Object lastModifiedBy) {
        return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
    }

    public List<EquJSparepart> findByEnterpriseCode(Object enterpriseCode) {
        return findByProperty(ENTERPRISE_CODE, enterpriseCode);
    }

    public List<EquJSparepart> findByIsUse(Object isUse) {
        return findByProperty(IS_USE, isUse);
    }

    /**
     * Find all EquJSparepart entities.
     * 
     * @return List<EquJSparepart> all EquJSparepart entities
     */
    @SuppressWarnings("unchecked")
    public List<EquJSparepart> findAll() {
        LogUtil.log("finding all EquJSparepart instances", Level.INFO, null);
        try {
            final String queryString = "select model from EquJSparepart model";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList();
        } catch (RuntimeException re) {
            LogUtil.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

    public EquJSparepart findByMaterialIdAndAttributeCode(Long materialId, String attributeCode ,String enterpriseCode,String user) {
        LogUtil.log("findByMaterialIdAndAttributeCode EquJSparepart instances", Level.INFO, null);
        try {
            String sql = "select  a.* \n"
                +"from EQU_J_SPAREPART a \n"
                +" where a.MATERIAL_ID = '" +materialId + "' \n"
                +"      and a.ATTRIBUTE_CODE = '"+ attributeCode + "' \n"
                + "     and a.ENTERPRISE_CODE = '"+ enterpriseCode + "' \n"
                + "     and a.IS_USE = 'Y' ";

            List<EquJSparepart> arrlist = bll.queryByNativeSQL(sql, EquJSparepart.class);
            if (arrlist.size() == 1) {
                 return arrlist.get(0);
            }else if(arrlist.size()<1){
                long maxId = bll.getMaxId("EQU_J_SPAREPART", "EQU_SPAREPART_ID");
                EquJSparepart entity= new EquJSparepart();
                entity.setLastModifiedBy(user);
                entity.setLastModifiedDate(new Date());
                entity.setMaterialId(materialId);
                entity.setAttributeCode(attributeCode);
                entity.setIsUse("Y");
                entity.setEquSparepartId(maxId);
                entity.setEnterpriseCode(enterpriseCode);
                this.save(entity);
                return entity;
            }
        } catch (RuntimeException re) {
            throw re;
        }
        return null;
    }

}