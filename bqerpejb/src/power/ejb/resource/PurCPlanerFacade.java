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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PurCPlaner.
 * 
 * @see power.ejb.resource.PurCPlaner
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PurCPlanerFacade implements PurCPlanerFacadeRemote {
	// property constants
	public static final String PLANER = "planer";
	public static final String PLANER_NAME = "planerName";
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
	public void save(PurCPlaner entity) {
		LogUtil.log("saving PurCPlaner instance", Level.INFO, null);
		try {
			if(entity.getPlanerId()==null)
            {
                // 设置流水号
                entity.setPlanerId(bll.getMaxId("PUR_C_PLANER", "PLANER_ID"));
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
	 * Delete a persistent PurCPlaner entity.
	 * 
	 * @param entity
	 *            PurCPlaner entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PurCPlaner entity) {
		LogUtil.log("deleting PurCPlaner instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PurCPlaner.class, entity
					.getPlanerId());
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
	 * @return PurCPlaner 修改的记录
	 * @throws RuntimeException 
	 */
	public PurCPlaner update(PurCPlaner entity) {
		LogUtil.log("updating PurCPlaner instance", Level.INFO, null);
		try {
			// 修改时间
            entity.setLastModifiedDate(new Date());
			PurCPlaner result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PurCPlaner findById(Long id) {
		LogUtil.log("finding PurCPlaner instance with id: " + id, Level.INFO,
				null);
		try {
			PurCPlaner instance = entityManager.find(PurCPlaner.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurCPlaner entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurCPlaner property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurCPlaner> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PurCPlaner> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PurCPlaner instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PurCPlaner model where model."
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
	 * 根据计划员编码查询计划员
	 * 
	 * @param planer 计划员编码
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 计划员
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByPlaner(String planer, String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PurCPlaner instance with planer: "
				+ planer, Level.INFO, null);
		try {
			PageObject result = new PageObject();
			// 查询sql
			String sql = "select * from pur_c_planer t\n"
				+ "where t.planer='" + planer + "'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
			List<PurCPlaner> list = bll.queryByNativeSQL(sql, PurCPlaner.class, rowStartIdxAndCount);
			String sqlCount = "select count(t.planer_id) from pur_c_planer t\n"
				+ "where t.planer='" + planer + "'\n"
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

	public List<PurCPlaner> findByPlanerName(Object planerName) {
		return findByProperty(PLANER_NAME, planerName);
	}

	public List<PurCPlaner> findByMaterialOrClassNo(Object materialOrClassNo) {
		return findByProperty(MATERIAL_OR_CLASS_NO, materialOrClassNo);
	}

	public List<PurCPlaner> findByIsMaterialClass(Object isMaterialClass) {
		return findByProperty(IS_MATERIAL_CLASS, isMaterialClass);
	}

	public List<PurCPlaner> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<PurCPlaner> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * 根据是否使用查询计划员
	 * 
	 * @param isUse 是否使用
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return PageObject 计划员
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByIsUse(String isUse, String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PurCPlaner instance with isUse: "
				+ isUse, Level.INFO, null);
		try {
			PageObject result = new PageObject();
			// 查询sql
			String sql = "select distinct t.planer from pur_c_planer t\n"
					+ "where t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'";
			List<PurCPlaner> list = new ArrayList<PurCPlaner>();
			PurCPlaner purCPlaner = new PurCPlaner();
			List listResult = bll.queryByNativeSQL(sql);
			for(int i = 0; i < listResult.size(); i++) {
				purCPlaner = new PurCPlaner();
				purCPlaner.setPlaner((listResult.get(i)).toString());
				list.add(purCPlaner);
			}
			String sqlCount = "select count(distinct t.planer) from pur_c_planer t\n"
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
	 * Find all PurCPlaner entities.
	 * 
	 * @return List<PurCPlaner> all PurCPlaner entities
	 */
	@SuppressWarnings("unchecked")
	public List<PurCPlaner> findAll() {
		LogUtil.log("finding all PurCPlaner instances", Level.INFO, null);
		try {
			final String queryString = "select model from PurCPlaner model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 查询所有数据
	 * return PageObject
	 */
	public PageObject findAllList(String enterpriseCode) {
		LogUtil.log("finding all PurCPlaner instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			// 查询sql
			String sql = "select * from pur_c_planer t\n"
				+ " where t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.is_use='Y'";
			List<PurCPlaner> list = bll.queryByNativeSQL(sql, PurCPlaner.class);
			String sqlCount = "select count(t.planer_id) from pur_c_planer t\n"
				+ "where t.enterprise_code='" + enterpriseCode + "'\n"
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
	 * 查询计划员为物料盘点打印做选择
	 * 
	 * @param isUse 是否使用
	 * @param enterpriseCode 企业编码
	 * @param 动态查询参数
	 * @return List 计划员
	 * add By ywliu 09/05/19
	 */
	@SuppressWarnings("unchecked")
	public List findPlanerForMaterialInventory(String enterpriseCode, final int... rowStartIdxAndCount) {
		try {
			// 查询sql
			String sql = "select distinct planer,planer_name from pur_c_planer t\n"
					+ "where t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'";
			List<PurCPlaner> list = new ArrayList<PurCPlaner>();
			PurCPlaner purCPlaner = new PurCPlaner();
			List listResult = bll.queryByNativeSQL(sql);
			for(int i = 0; i < listResult.size(); i++) {
				purCPlaner = new PurCPlaner();
				Object [] data = (Object[]) listResult.get(i);
				if(data[0] != null ) {
					purCPlaner.setPlaner(data[0].toString());
				}
				if(data[1] != null ) {
					purCPlaner.setPlanerName(data[1].toString());
				}
				list.add(purCPlaner);
			}
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

}