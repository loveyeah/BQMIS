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

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCDegree.
 * 
 * @see powereai.po.hr.HrCDegree
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCDegreeFacade implements HrCDegreeFacadeRemote {
	// property constants
	public static final String DEGREE_NAME = "degreeName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加
	 */
	public HrCDegree save(HrCDegree entity) throws CodeRepeatException { 
		try {
			
			if(checkNameSameForAdd(entity.getDegreeName()))
			{
				throw new CodeRepeatException("学位名称已经存在，请重新输入!");
			} 
			if(entity.getDegreeId() == null)
			{
				entity.setDegreeId(bll.getMaxId("hr_c_degree", "degree_id"));
			}
			entityManager.persist(entity); 
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 修改
	 */
	public HrCDegree update(HrCDegree entity) throws CodeRepeatException { 
		try {
			if(checkNameSameForUpdate(entity.getDegreeId(),entity.getDegreeName()))
			{
				throw new CodeRepeatException("学位名称已经存在，请重新输入!");
			} 
			HrCDegree result = entityManager.merge(entity); 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	private boolean checkNameSameForAdd(String degreeName)
	{
		String sql = "select count(1) from hr_c_degree t where t.degree_name=?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{degreeName}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	private boolean checkNameSameForUpdate(Long degreeId,String degreeName)
	{
		String sql = "select count(1) from hr_c_degree t where t.degree_name=? and t.degree_id <> ?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{degreeName,degreeId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}

	 
	public void delete(HrCDegree entity) {
		 
		try {
			entity = entityManager.getReference(HrCDegree.class, entity
					.getDegreeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void delete(String ids){
		String sql = "delete from hr_c_degree t where t.degree_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	 
	

	public HrCDegree findById(Long id) {
		LogUtil.log("finding HrCDegree instance with id: " + id, Level.INFO,
				null);
		try {
			HrCDegree instance = entityManager.find(HrCDegree.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCDegree entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCDegree property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCDegree> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCDegree> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCDegree instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCDegree model where model."
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

	public List<HrCDegree> findByDegreeName(Object degreeName,
			int... rowStartIdxAndCount) {
		return findByProperty(DEGREE_NAME, degreeName, rowStartIdxAndCount);
	}

	public List<HrCDegree> findByIsUse(Object isUse, int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCDegree> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCDegree entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCDegree> all HrCDegree entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(final int... rowStartIdxAndCount) { 
		PageObject result = new PageObject();
		try {
			final String queryString = "select model from HrCDegree model";
			Query query = entityManager.createQuery(queryString);
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
			List<HrCDegree> list = query.getResultList();
			if(list != null && list.size()>0)
			{
				result.setList(list);
				String sql = " select count(1) from hr_c_degree"; 
				result.setTotalCount(Long.parseLong(bll.getSingal(sql).toString()));
			} 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	/**
	 * 查找所有学位编码
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllDegrees(String enterpriseCode){
		try{
			String sql = "SELECT L.DEGREE_ID ,L.DEGREE_NAME FROM HR_C_DEGREE L" +
			// modified by liuyi is_use 和enterprise_code 问题 已解决
					" WHERE L.IS_USE = 'Y' AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
//			" WHERE L.IS_USE = 'Y' ";
			LogUtil.log("所有学位编码id和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql);
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
			LogUtil.log("查找所有学位编码id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有学位编码id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
	public Long getDegreeIdByName(String name, String enterpriseCode) {
		Long id = null;
		String sql = 
			"select a.degree_id\n" +
			"  from hr_c_degree a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.degree_name='"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			id = Long.parseLong(obj.toString());

		return id;
	}
}