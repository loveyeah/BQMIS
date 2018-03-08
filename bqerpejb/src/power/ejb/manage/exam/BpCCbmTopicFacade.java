package power.ejb.manage.exam;

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
 * 考核指标主题
 * @author liuyi 091207
 */
@Stateless
public class BpCCbmTopicFacade implements BpCCbmTopicFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条考核指标主题
	 */
	public void save(BpCCbmTopic entity) {
		LogUtil.log("saving BpCCbmTopic instance", Level.INFO, null);
		try {
			entity.setTopicId(bll.getMaxId("BP_C_CBM_TOPIC", "TOPIC_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条考核指标主题
	 */
	public void delete(BpCCbmTopic entity) {
		LogUtil.log("deleting BpCCbmTopic instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCCbmTopic.class, entity
					.getTopicId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条考核指标主题
	 */
	public BpCCbmTopic update(BpCCbmTopic entity) {
		LogUtil.log("updating BpCCbmTopic instance", Level.INFO, null);
		try {
			BpCCbmTopic result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCCbmTopic findById(Long id) {
		LogUtil.log("finding BpCCbmTopic instance with id: " + id, Level.INFO,
				null);
		try {
			BpCCbmTopic instance = entityManager.find(BpCCbmTopic.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<BpCCbmTopic> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding BpCCbmTopic instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCCbmTopic model where model."
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
	public List<BpCCbmTopic> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all BpCCbmTopic instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpCCbmTopic model";
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

	public PageObject findAllTopic(String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.topic_id,\n" +
			" a.topic_name,\n" + 
			" a.coefficient,\n" + 
			" a.display_no\n" + 
			" from BP_C_CBM_TOPIC a\n" + 
			" where a.is_use='Y'\n" + 
			" and a.enterprise_code='" + enterpriseCode+ "'\n"; 
			
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql +=" order by a.display_no";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));;
		return pg;
		
	}
	
	public void saveModifiedRec(List<BpCCbmTopic> addList,List<BpCCbmTopic> updateList,String ids)
	{
		if(addList != null && addList.size() > 0){
			for(BpCCbmTopic entity : addList)
			{
				this.save(entity);
				entityManager.flush();
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(BpCCbmTopic entity : updateList)
			{
				this.update(entity);
			}
		}
		if(ids != null && ids.length() > 0)
		{
			String sql = "update BP_C_CBM_TOPIC a set a.is_use='N' where a.topic_id in (" + ids+ ")";
			bll.exeNativeSQL(sql);
		}
	}

	
}