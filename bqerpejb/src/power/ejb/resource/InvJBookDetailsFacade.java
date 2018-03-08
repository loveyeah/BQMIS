package power.ejb.resource;

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
 * Facade for entity InvJBookDetails.
 * 
 * @see power.ejb.logistics.InvJBookDetails
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJBookDetailsFacade implements InvJBookDetailsFacadeRemote {
	// property constants
	public static final String BOOK_NO = "bookNo";
	public static final String BOOK_DETAIL_NO = "bookDetailNo";
	public static final String MATERIAL_ID = "materialId";
	public static final String WHS_NO = "whsNo";
	public static final String LOCATION_NO = "locationNo";
	public static final String LOT_NO = "lotNo";
	public static final String BOOK_QTY = "bookQty";
	public static final String PHYSICAL_QTY = "physicalQty";
	public static final String REASON = "reason";
	public static final String BOOK_STATUS = "bookStatus";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved InvJBookDetails entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	/**
     *增加一条记录
     * @param entity 要增加的记录
     */
	public void save(InvJBookDetails entity) {
		LogUtil.log("saving InvJBookDetails instance", Level.INFO, null);
		try {
				if(entity.getId() == null) {
					entity.setId(bll.getMaxId(
						"INV_J_BOOK_DETAILS ", "ID"));
				}
                // 设定是否使用
                entity.setIsUse("Y");
                entity.setBookDate(new Date());
                entity.setLastModifiedDate(new Date());
                // 保存
                entityManager.persist(entity);
                LogUtil.log("save successful", Level.INFO, null);
                return;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * Delete a persistent InvJBookDetails entity.
	 * 
	 * @param entity
	 *            InvJBookDetails entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJBookDetails entity) {
		LogUtil.log("deleting InvJBookDetails instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvJBookDetails.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvJBookDetails entity and return it or a copy
	 * of it to the sender. A copy of the InvJBookDetails entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            InvJBookDetails entity to update
	 * @return InvJBookDetails the persisted InvJBookDetails entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJBookDetails update(InvJBookDetails entity) {
		LogUtil.log("updating InvJBookDetails instance", Level.INFO, null);
		try {
//			entity.set
			InvJBookDetails result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJBookDetails findById(Long id) {
		LogUtil.log("finding InvJBookDetails instance with id: " + id,
				Level.INFO, null);
		try {
			InvJBookDetails instance = entityManager.find(
					InvJBookDetails.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJBookDetails entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJBookDetails property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJBookDetails> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJBookDetails> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvJBookDetails instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJBookDetails model where model."
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
	 * 查询物料盘点状态
	 * @param materialId
	 * 			物料ID
	 * @param	whsNo
	 * 			仓库编码
	 * @param 	location
	 * 			库位编码
	 * @param  lotNo
	 * 			批号
	 *  @param enterpriseCode
	 *  		企业编码
	 *  @return PageObject
	 */
	public PageObject getBookStatus(String materialId,String whsNo,String location,
			String lotNo,String enterpriseCode) {
		LogUtil.log("finding BookStatus instance",
                Level.INFO, null);
        try {
        	PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * from INV_J_BOOK_DETAILS t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.MATERIAL_ID='"+materialId+"'\n" +
                "and t.WHS_NO='" + whsNo +  "'\n" +
                "and t.LOT_NO='" + lotNo +  "'\n" +
                "and t.is_use='Y'";
            if(location == null || location.equals("")) {
            	sql += " and t.LOCATION_NO is null";
            }else {
            	sql += " and t.LOCATION_NO='"+ location +"'\n";
            }
            // 执行查询
            List<InvJBookDetails> list = bll.queryByNativeSQL(sql, InvJBookDetails.class);
            // 设置PageObject
            result.setList(list);
            // 返回
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
	}
	
	public List<InvJBookDetails> findByBookNo(Object bookNo) {
		return findByProperty(BOOK_NO, bookNo);
	}

	public List<InvJBookDetails> findByBookDetailNo(Object bookDetailNo) {
		return findByProperty(BOOK_DETAIL_NO, bookDetailNo);
	}

	public List<InvJBookDetails> findByMaterialId(Object materialId) {
		return findByProperty(MATERIAL_ID, materialId);
	}

	public List<InvJBookDetails> findByWhsNo(Object whsNo) {
		return findByProperty(WHS_NO, whsNo);
	}

	public List<InvJBookDetails> findByLocationNo(Object locationNo) {
		return findByProperty(LOCATION_NO, locationNo);
	}

	public List<InvJBookDetails> findByLotNo(Object lotNo) {
		return findByProperty(LOT_NO, lotNo);
	}

	public List<InvJBookDetails> findByBookQty(Object bookQty) {
		return findByProperty(BOOK_QTY, bookQty);
	}

	public List<InvJBookDetails> findByPhysicalQty(Object physicalQty) {
		return findByProperty(PHYSICAL_QTY, physicalQty);
	}

	public List<InvJBookDetails> findByReason(Object reason) {
		return findByProperty(REASON, reason);
	}

	public List<InvJBookDetails> findByBookStatus(Object bookStatus) {
		return findByProperty(BOOK_STATUS, bookStatus);
	}

	public List<InvJBookDetails> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvJBookDetails> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvJBookDetails entities.
	 * 
	 * @return List<InvJBookDetails> all InvJBookDetails entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvJBookDetails> findAll() {
		LogUtil.log("finding all InvJBookDetails instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvJBookDetails model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}