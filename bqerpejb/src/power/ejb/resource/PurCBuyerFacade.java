package power.ejb.resource;

import java.util.ArrayList;
import java.util.Date;
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
 * Facade for entity PurCBuyer.
 * 
 * @see power.ejb.logistics.PurCBuyer
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PurCBuyerFacade implements PurCBuyerFacadeRemote {
	// property constants
	public static final String BUYER = "buyer";
	public static final String BUYER_NAME = "buyerName";
	public static final String MATERIAL_OR_CLASS_NO = "materialOrClassNo";
	public static final String IS_MATERIAL_CLASS = "isMaterialClass";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * 保存一条记录
	 * 
	 * @param entity 要保存的记录
	 * @throws RuntimeException
	 */
	public void save(PurCBuyer entity) {
		LogUtil.log("saving PurCBuyer instance", Level.INFO, null);
		try {
			if(entity.getId()==null)
            {
                // 设置流水号
                entity.setId(bll.getMaxId("PUR_C_BUYER", "ID"));
            }
            // 设置修改时间
            entity.setLastModifiedDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PurCBuyer entity.
	 * 
	 * @param entity
	 *            PurCBuyer entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PurCBuyer entity) {
		LogUtil.log("deleting PurCBuyer instance", Level.INFO, null);
		try {
			entity = entityManager
					.getReference(PurCBuyer.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改一条记录
	 * 
	 * @param entity 要修改的记录
	 * @return PurCBuyer 修改的记录
	 * @throws RuntimeException 
	 */
	public PurCBuyer update(PurCBuyer entity) {
		LogUtil.log("updating PurCBuyer instance", Level.INFO, null);
		try {
			// 修改时间
            entity.setLastModifiedDate(new Date());
            PurCBuyer result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PurCBuyer findById(Long id) {
		LogUtil.log("finding PurCBuyer instance with id: " + id, Level.INFO,
				null);
		try {
			PurCBuyer instance = entityManager.find(PurCBuyer.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurCBuyer entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurCBuyer property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurCBuyer> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PurCBuyer> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PurCBuyer instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PurCBuyer model where model."
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
	 * 根据采购员编码查询采购员
	 * 
	 * @param buyer 采购员编码
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 采购员
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByBuyer(String buyer, String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PurCBuyer instance with buyer: "
				+ buyer, Level.INFO, null);
		try {
			PageObject result = new PageObject();
			// 查询sql
			String sql = "select * from pur_c_buyer t\n"
				+ "where t.buyer='" + buyer + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
			List<PurCBuyer> list = bll.queryByNativeSQL(sql, PurCBuyer.class, rowStartIdxAndCount);
			String sqlCount = "select count(t.id) from pur_c_buyer t\n"
				+ "where t.buyer='" + buyer + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PurCBuyer> findByBuyerName(Object buyerName) {
		return findByProperty(BUYER_NAME, buyerName);
	}

	public List<PurCBuyer> findByMaterialOrClassNo(Object materialOrClassNo) {
		return findByProperty(MATERIAL_OR_CLASS_NO, materialOrClassNo);
	}

	public List<PurCBuyer> findByIsMaterialClass(Object isMaterialClass) {
		return findByProperty(IS_MATERIAL_CLASS, isMaterialClass);
	}

	public List<PurCBuyer> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<PurCBuyer> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * 根据是否使用查询采购员
	 * 
	 * @param isUse 是否使用
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 采购员
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByIsUse(String isUse, String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PurCBuyer instance with isUse: "
				+ isUse, Level.INFO, null);
		try {
			PageObject result = new PageObject();
			// 查询sql
			String sql = "select distinct t.buyer from pur_c_buyer t\n"
					+ "where t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'";
			List<PurCBuyer> list = new ArrayList<PurCBuyer>();
			PurCBuyer purCBuyer = new PurCBuyer();
			List listResult = bll.queryByNativeSQL(sql);
			for(int i = 0; i < listResult.size(); i++) {
				purCBuyer = new PurCBuyer();
				purCBuyer.setBuyer((listResult.get(i)).toString());
				list.add(purCBuyer);
			}
			String sqlCount = "select count(distinct t.buyer) from pur_c_buyer t\n"
					+ "where  t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurCBuyer entities.
	 * 
	 * @return List<PurCBuyer> all PurCBuyer entities
	 */
	@SuppressWarnings("unchecked")
	public List<PurCBuyer> findAll() {
		LogUtil.log("finding all PurCBuyer instances", Level.INFO, null);
		try {
			final String queryString = "select model from PurCBuyer model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}