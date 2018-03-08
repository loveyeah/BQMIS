package power.ejb.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity InvCReason.
 * 
 * @see power.ejb.resource.InvCReason
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCReasonFacade implements InvCReasonFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved InvCReason entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvCReason entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCReason entity) {
		LogUtil.log("saving InvCReason instance", Level.INFO, null);
		try {
			if (entity.getReasonId() == null) {
                // 设定主键值
                entity.setReasonId(bll.getMaxId("INV_C_REASON",
                        "REASON_ID"));
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
	 * Delete a persistent InvCReason entity.
	 * 
	 * @param entity
	 *            InvCReason entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCReason entity) {
		LogUtil.log("deleting InvCReason instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvCReason.class, entity
					.getReasonId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvCReason entity and return it or a copy of
	 * it to the sender. A copy of the InvCReason entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvCReason entity to update
	 * @return InvCReason the persisted InvCReason entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCReason update(InvCReason entity) {
		LogUtil.log("updating InvCReason instance", Level.INFO, null);
		try {
			// 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
			InvCReason result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCReason findById(Long id) {
		LogUtil.log("finding InvCReason instance with id: " + id, Level.INFO,
				null);
		try {
			InvCReason instance = entityManager.find(InvCReason.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvCReason entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCReason property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCReason> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvCReason> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvCReason instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvCReason model where model."
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
	 * Find all InvCReason entities.
	 * 
	 * @return List<InvCReason> all InvCReason entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCReason> findAll() {
		LogUtil.log("finding all InvCReason instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvCReason model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * Find all InvCReason entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCReason property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCReason> found by query
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByTransId(String transId,String enterpriseCode,final int... rowStartIdxAndCount) {
		LogUtil.log("finding InvCReason instance with transId: " + transId, Level.INFO,null);
		try {
			PageObject result = new PageObject();
			// 查询
			String sql = 
			    "select * from INV_C_REASON t\n" +
                "where  t.TRANS_ID='"+transId + "'\n" +
                "and t.is_use='Y' \n" +
				"and t.ENTERPRISE_CODE = '"+enterpriseCode+"' \n" +
				"order by t.REASON_CODE";
			// 数量
			String sqlCount = 
				"select count(t.TRANS_ID) from INV_C_REASON t\n" +
                "where  t.TRANS_ID='"+transId + "'\n" +
                "and t.is_use='Y' \n" +
				"and t.ENTERPRISE_CODE = '"+enterpriseCode+"' \n";
				// 执行查询
                List<InvCReason> list = bll.queryByNativeSQL(sql, InvCReason.class,rowStartIdxAndCount);
                Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
    			result.setList(list);
    			result.setTotalCount(totalCount);
    			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查询事物原因码是否是唯一的
	 * 
	 * @param reansonCode
	 * @param enterpriseCode          
	 * @return boolean
	 */
	public boolean checkReasonCode(String reansonCode,String enterpriseCode) {
		boolean isSame = false;
        String sql = "select count(REASON_CODE) from INV_C_REASON t\n"
                + "where t.REASON_CODE='" + reansonCode + "'\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.is_use='Y'";
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
	}
	/**
	 * 查询事物原因名称是否是唯一的
	 * 
	 * @param reansonName
	 * @param enterpriseCode          
	 * @return boolean
	 */
	public boolean checkReasonName(String reansonName,String enterpriseCode) {
		boolean isSame = false;
        String sql = "select count(REASON_NAME) from INV_C_REASON t\n"
                + "where t.REASON_NAME='" + reansonName + "'\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.is_use='Y'";
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
	}
	/**
	 * 更新
     * 查询事物原因名称是否是唯一的
     * @param reasonName
     * @
     * @return list 
     */
	@SuppressWarnings("unchecked")
	public InvCReason checkReasonNameForUpdate(String reasonName, String enterpriseCode) {
		try {
			List<InvCReason> list = null;
        String sql = 
        	"select * from INV_C_REASON \n" +
        	"where REASON_NAME = '"+reasonName+"' \n" +
        	"and  is_use = 'Y' \n" +
        	"and  ENTERPRISE_CODE like '"+enterpriseCode+"'";
       // 执行查询
        list = bll.queryByNativeSQL(sql, InvCReason.class);
        if(list.size()!=0) {
        	return list.get(0);
        }
        return null;
     } catch (RuntimeException re) {
        LogUtil.log("find failed", Level.SEVERE, re);
        throw re;
     }
    }
	 /**
     * 删除对应一个事务作用对应的所有事务原因码记录
     *
     * @param transId 流水号
     * @param workerCode 登录者id
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteByTransId(String transId, String workerCode) throws CodeRepeatException {
        LogUtil.log("deleting InvCReason instance", Level.INFO, null);
        try {
            String sql=
                "update INV_C_REASON t\n" +
                "set t.is_use='N'\n" +
                ", t.LAST_MODIFIED_BY='" + workerCode + "'\n" +
                ", t.LAST_MODIFIED_DATE= sysdate\n" +
                "where t.TRANS_ID= '" + transId + "' and t.is_use='Y'";
            bll.exeNativeSQL(sql);
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }
}