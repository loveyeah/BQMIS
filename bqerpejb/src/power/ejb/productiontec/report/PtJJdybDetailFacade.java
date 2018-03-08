package power.ejb.productiontec.report;

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
 * 继电月报表（明细表）
 * @author liuyi 091010
 */
@Stateless
public class PtJJdybDetailFacade implements PtJJdybDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条继电月报表（明细表）数据
	 */
	public void save(PtJJdybDetail entity) {
		LogUtil.log("saving PtJJdybDetail instance", Level.INFO, null);
		try {
			entity.setJdybDetailId(bll.getMaxId("PT_J_JDYB_DETAIL", "JDYB_DETAIL_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条继电月报表（明细表）数据
	 */
	public void delete(PtJJdybDetail entity) {
		LogUtil.log("deleting PtJJdybDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJJdybDetail.class, entity
					.getJdybDetailId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条继电月报表（明细表）数据
	 */
	public PtJJdybDetail update(PtJJdybDetail entity) {
		LogUtil.log("updating PtJJdybDetail instance", Level.INFO, null);
		try {
			PtJJdybDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJJdybDetail findById(Long id) {
		LogUtil.log("finding PtJJdybDetail instance with id: " + id,
				Level.INFO, null);
		try {
			PtJJdybDetail instance = entityManager
					.find(PtJJdybDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJJdybDetail> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJJdybDetail instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdybDetail model where model."
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

	@SuppressWarnings("unchecked")
	public List<PtJJdybDetail> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJJdybDetail instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdybDetail model";
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
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void treatRecords(List<PtJJdybDetail> addList,List<PtJJdybDetail> updateList,String ids)
	{
		if(addList != null && addList.size() > 0)
		{
			for(PtJJdybDetail entity : addList)
			{
				this.save(entity);
				entityManager.flush();
			}
		}
		
		if(updateList != null && updateList.size() > 0)
		{
			for(PtJJdybDetail entity : updateList)
			{
				this.update(entity);
			}
		}
		
		if(ids != null && ids.length() > 0)
		{
			String sql = "delete from PT_J_JDYB_DETAIL a where a.JDYB_DETAIL_ID in (" + ids + ") \n";
			bll.exeNativeSQL(sql);
		}
	}

}