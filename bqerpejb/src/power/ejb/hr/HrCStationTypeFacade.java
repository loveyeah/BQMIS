package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCStationType.
 * 
 * @see powereai.po.hr.HrCStationType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCStationTypeFacade implements HrCStationTypeFacadeRemote {
	// property constants
	public static final String STATION_TYPE_NAME = "stationTypeName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	 
	public void save(HrCStationType entity) {
		LogUtil.log("saving HrCStationType instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	 
	public void delete(HrCStationType entity) {
		LogUtil.log("deleting HrCStationType instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCStationType.class, entity
					.getStationTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	 
	public HrCStationType update(HrCStationType entity) {
		LogUtil.log("updating HrCStationType instance", Level.INFO, null);
		try {
			HrCStationType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCStationType findById(Long id) {
		LogUtil.log("finding HrCStationType instance with id: " + id,
				Level.INFO, null);
		try {
//			HrCStationType instance = entityManager.find(HrCStationType.class,
//					id);
			final String sql = "select hr.* from HR_C_STATION_TYPE hr where hr.STATION_TYPE_ID="+id+" and hr.is_use='Y'";
			HrCStationType instance=(HrCStationType)bll.queryByNativeSQL(sql,HrCStationType.class).get(0); 
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCStationType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCStationType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCStationType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCStationType> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCStationType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCStationType model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCStationType> findByStationTypeName(Object stationTypeName,
			int... rowStartIdxAndCount) {
		return findByProperty(STATION_TYPE_NAME, stationTypeName,
				rowStartIdxAndCount);
	}

	public List<HrCStationType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCStationType> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCStationType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCStationType> all HrCStationType entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCStationType> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCStationType instances", Level.INFO, null);
		try {
//		final String queryString = "select model from HrCStationType model";
			final String queryString = "select hr.* from HR_C_STATION_TYPE hr where  hr.is_use='Y'";
			final String sqlCount = "select count(1) from HR_C_STATION_TYPE hr where  hr.is_use='Y'";
			if(Integer.parseInt(bll.getSingal(sqlCount).toString())!=0){
				return bll.queryByNativeSQL(queryString, HrCStationType.class, rowStartIdxAndCount);
			}
			else{
				return null;
			}
			
			
//			Query query = entityManager.createQuery(queryString);
//			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
//				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
//				if (rowStartIdx > 0) {
//					query.setFirstResult(rowStartIdx);
//				}
//
//				if (rowStartIdxAndCount.length > 1) {
//					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
//					if (rowCount > 0) {
//						query.setMaxResults(rowCount);
//					}
//				}
//			}
//			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	/**
	 * add by liuyi 091123
	 * 查找岗位类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllStationTypes(String enterpriseCode){
		try{
			String sql = "SELECT S.STATION_TYPE_ID ,S.STATION_TYPE_NAME" +
					"  FROM HR_C_STATION_TYPE S" +
					" WHERE S.IS_USE = ? AND S.ENTERPRISE_CODE = ?" ;
			LogUtil.log("所有岗位类别id和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql,new Object[] {"Y",enterpriseCode});//update by sychen 20100831
//			List list = bll.queryByNativeSQL(sql,new Object[] {"U",enterpriseCode});
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有岗位类别id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有岗位类别id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

}