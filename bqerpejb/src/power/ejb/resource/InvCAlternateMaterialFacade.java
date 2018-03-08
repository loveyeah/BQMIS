package power.ejb.resource;

import java.util.Date;
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

/**
 * Facade for entity InvCAlternateMaterial.
 * 
 * @see power.ejb.resource.InvCAlternateMaterial
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCAlternateMaterialFacade implements
		InvCAlternateMaterialFacadeRemote {
	// property constants
	public static final String MATERIAL_ID = "materialId";
	public static final String ALTER_MATERIAL_ID = "alterMaterialId";
	public static final String QTY = "qty";
	public static final String PRIORITY = "priority";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    
	/**
	 * Perform an initial save of a previously unsaved InvCAlternateMaterial
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvCAlternateMaterial entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCAlternateMaterial entity) {
		LogUtil.log("saving InvCAlternateMaterial instance", Level.INFO, null);
		try {
			if (entity.getAlternateMaterialId() == null) {
                // 设定主键值
                entity.setAlternateMaterialId(bll.getMaxId("INV_C_ALTERNATE_MATERIAL",
                        "ALTERNATE_MATERIAL_ID"));
            }
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
/**
	 * 新增
     * 检查替代物料和替代物料是否是唯一的
     * @param MATERIAL_ID 物料id
     * @param ALTER_MATERIAL_ID 替代物料id
     * @return false 如果替代物料id和物料id是唯一的
     */
	public boolean checkAlterMaterialForAdd(long materialId, long alterMaterialId) {

        boolean isSame = false;
        String sql = "select count(t.ALTERNATE_MATERIAL_ID) from INV_C_ALTERNATE_MATERIAL t\n"
                + "where t.MATERIAL_ID='" + materialId + "'\n"
                + "and t.ALTER_MATERIAL_ID='" + alterMaterialId + "'\n"
                + "and t.is_use='Y'";
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
    }
	/**
	 * 更新
     * 检查替代物料和替代物料是否是唯一的
     * @param MATERIAL_ID 物料id
     * @param ALTER_MATERIAL_ID 替代物料id
     * @return list 如果存在替代物料id和物料id 是相同的
     */
	@SuppressWarnings("unchecked")
	public InvCAlternateMaterial checkAlterMaterialForUpdate(long materialId, long alterMaterialId) {
		try {
			List<InvCAlternateMaterial> list = null;
        String sql = "select * from INV_C_ALTERNATE_MATERIAL t\n"
                + "where t.MATERIAL_ID='" + materialId + "'\n"
                + "and t.ALTER_MATERIAL_ID='" + alterMaterialId + "'\n"
                + "and t.is_use='Y'";
       // 执行查询
        list = bll.queryByNativeSQL(sql, InvCAlternateMaterial.class);
        if(list!= null) {
        	return list.get(0);
        }
        return null;
     } catch (RuntimeException re) {
        LogUtil.log("find failed", Level.SEVERE, re);
        throw re;
    }
    }
	
	/**
	 * 更新
     * 检查日期重叠性
     * @param MATERIAL_ID 物料id
     * @param ALTER_MATERIAL_ID 替代物料id
     * @param effectiveDate 有效开始日期
     * @param discontinueDate 有效截止日期
     * @return list
     */
	@SuppressWarnings("unchecked")
	public InvCAlternateMaterial checkDate(long materialId, long alterMaterialId,String effectiveDate,String discontinueDate,String enterpriseCode ) {
		try {
			List<InvCAlternateMaterial> list = null;
        String sql = "select * from INV_C_ALTERNATE_MATERIAL  t\n"
                + "where t.MATERIAL_ID='" + materialId + "'\n"
                + "and t.ALTER_MATERIAL_ID='" + alterMaterialId + "'\n"
                + "and( ( (t.EFFECTIVE_DATE<= to_date('" + effectiveDate+ "', 'yyyy-MM-dd') ) and  \n"
                + "      (t.DISCONTINUE_DATE >= to_date('" + effectiveDate+ "', 'yyyy-MM-dd') ) )or \n"
                + "    ( (t.EFFECTIVE_DATE<= to_date('" + discontinueDate+ "', 'yyyy-MM-dd') ) and \n"
                + "      (t.DISCONTINUE_DATE>= to_date('" + discontinueDate+ "', 'yyyy-MM-dd') ) ) )\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n" 
                + "and t.is_use='Y'";
       // 执行查询
        list = bll.queryByNativeSQL(sql, InvCAlternateMaterial.class);
        if(list.size()>0) {
        	return list.get(0);
        }
        return null;
     } catch (RuntimeException re) {
        LogUtil.log("find failed", Level.SEVERE, re);
        throw re;
    }
    }
	
	/**
	 * Delete a persistent InvCAlternateMaterial entity.
	 * 
	 * @param entity
	 *            InvCAlternateMaterial entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCAlternateMaterial entity) {
		LogUtil
				.log("deleting InvCAlternateMaterial instance", Level.INFO,
						null);
		try {
			entity = entityManager.getReference(InvCAlternateMaterial.class,
					entity.getAlternateMaterialId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvCAlternateMaterial entity and return it or
	 * a copy of it to the sender. A copy of the InvCAlternateMaterial entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCAlternateMaterial entity to update
	 * @return InvCAlternateMaterial the persisted InvCAlternateMaterial entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCAlternateMaterial update(InvCAlternateMaterial entity) {
		LogUtil
				.log("updating InvCAlternateMaterial instance", Level.INFO,
						null);
		try {
		 // 修改时间
            entity.setLastModifiedDate(new Date());
			InvCAlternateMaterial result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCAlternateMaterial findById(Long id) {
		LogUtil.log("finding InvCAlternateMaterial instance with id: " + id,
				Level.INFO, null);
		try {
			InvCAlternateMaterial instance = entityManager.find(
					InvCAlternateMaterial.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvCAlternateMaterial entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCAlternateMaterial property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCAlternateMaterial> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvCAlternateMaterial> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvCAlternateMaterial instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvCAlternateMaterial model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<InvCAlternateMaterial> findByMaterialId(Object materialId) {
		return findByProperty(MATERIAL_ID, materialId);
	}

	public List<InvCAlternateMaterial> findByAlterMaterialId(
			Object alterMaterialId) {
		return findByProperty(ALTER_MATERIAL_ID, alterMaterialId);
	}

	public List<InvCAlternateMaterial> findByQty(Object qty) {
		return findByProperty(QTY, qty);
	}

	public List<InvCAlternateMaterial> findByPriority(Object priority) {
		return findByProperty(PRIORITY, priority);
	}

	public List<InvCAlternateMaterial> findByLastModifiedBy(
			Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvCAlternateMaterial> findByEnterpriseCode(
			Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvCAlternateMaterial> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvCAlternateMaterial entities.
	 * 
	 * @return List<InvCAlternateMaterial> all InvCAlternateMaterial entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCAlternateMaterial> findAll() {
		LogUtil.log("finding all InvCAlternateMaterial instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from InvCAlternateMaterial model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

/**
	 * 根据替代物料.物料id进行查找
	 * 
	 * @return List<InvCAlternateMaterial> all InvCAlternateMaterial entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCAlternateMaterial> findByMaterialId(Long id) {
		LogUtil.log("finding all InvCAlternateMaterial instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from InvCAlternateMaterial model where MATERIAL_ID = '"+id+"'";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	  /**
     * 删除对应一个物料的所有记录
     *
     * @param materialId 替代物料id
     * @param workerCode 登录者id
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteByMaterialId(String materialId,String workerCode,String enterpriseCode) throws CodeRepeatException {
        LogUtil.log("deleting InvCLocation instance", Level.INFO, null);
        try {
            String sql=
                "update INV_C_ALTERNATE_MATERIAL t\n" +
                "set t.is_use='N'\n" +
                ", t.last_modified_by='" + workerCode + "'\n" +
                ", t.last_modified_date= sysdate\n" +
                "where t.MATERIAL_ID= '" + materialId + "' and t.is_use='Y' \n" +
                "and t.ENTERPRISE_CODE='"+ enterpriseCode +"'";
            bll.exeNativeSQL(sql);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }
}