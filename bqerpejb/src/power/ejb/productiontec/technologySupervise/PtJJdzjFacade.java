package power.ejb.productiontec.technologySupervise;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import power.ejb.productiontec.technologySupervise.form.PtJJdzjForm;

/**
 * Facade for entity PtJJdzj.
 * 
 * @see power.ejb.productiontec.technologySupervise.PtJJdzj
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJJdzjFacade implements PtJJdzjFacadeRemote {
	// property constants
	public static final String JDZY_ID = "jdzyId";
	public static final String MAIN_TOPIC = "mainTopic";
	public static final String ZJ_BY = "zjBy";
	public static final String CONTENT = "content";
	public static final String MEMO = "memo";
	public static final String FILL_BY = "fillBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJJdzj entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdzj entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdzj entity) {
		LogUtil.log("saving PtJJdzj instance", Level.INFO, null);
		try {
			entity.setJdzjId(bll.getMaxId("PT_J_JDZJ", "JDZJ_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * Delete a persistent PtJJdzj entity.
	 * 
	 * @param entity
	 *            PtJJdzj entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJJdzj entity) {
		LogUtil.log("deleting PtJJdzj instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJJdzj.class, entity
					.getJdzjId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJJdzj entity and return it or a copy of it
	 * to the sender. A copy of the PtJJdzj entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdzj entity to update
	 * @return PtJJdzj the persisted PtJJdzj entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdzj update(PtJJdzj entity) {
		LogUtil.log("updating PtJJdzj instance", Level.INFO, null);
		try {
			PtJJdzj result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJJdzj findById(Long id) {
		LogUtil
				.log("finding PtJJdzj instance with id: " + id, Level.INFO,
						null);
		try {
			PtJJdzj instance = entityManager.find(PtJJdzj.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_J_JDZJ a\n"
	    + " where a.JDZJ_ID in (" + ids + ")\n" ;
        bll.exeNativeSQL(sql);
		
	}
	/**
	 * Find all PtJJdzj entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdzj property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdzj> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJJdzj> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding PtJJdzj instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdzj model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtJJdzj> findByJdzyId(Object jdzyId) {
		return findByProperty(JDZY_ID, jdzyId);
	}

	public List<PtJJdzj> findByMainTopic(Object mainTopic) {
		return findByProperty(MAIN_TOPIC, mainTopic);
	}

	public List<PtJJdzj> findByZjBy(Object zjBy) {
		return findByProperty(ZJ_BY, zjBy);
	}

	public List<PtJJdzj> findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List<PtJJdzj> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<PtJJdzj> findByFillBy(Object fillBy) {
		return findByProperty(FILL_BY, fillBy);
	}

	public List<PtJJdzj> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String jdzyId,String topicName,String enterpriseCode,int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql = 
			"select t.*,\n" +
			"       to_char(t.zj_date, 'yyyy-MM-dd') zjDate,\n" + 
			"       getworkername(t.zj_by) zjName,\n" + 
			"       to_char(t.fill_date, 'yyyy-MM-dd') fillDate,\n" + 
			"       getworkername(t.fill_by) fillName\n" + 
			"  from pt_j_jdzj t\n" + 
			" where t.jdzy_id ="+jdzyId+"\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";
		String sqlCount = 
			"select count(*)\n" +
			"  from pt_j_jdzj t\n" + 
			" where t.jdzy_id ="+jdzyId+"\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";
		if (topicName != null && !"".equals(topicName)){
			sql += " and t.main_topic like '%"+topicName+"%'";
			sqlCount += " and t.main_topic like '%"+topicName+"%'";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJJdzj model = new PtJJdzj();
				PtJJdzjForm form= new PtJJdzjForm();
				Object []data = (Object[])it.next();
				model.setJdzjId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setJdzyId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					model.setMainTopic(data[2].toString());
				if (data[3] != null)
					model.setZjBy(data[3].toString());
				if (data[4] != null)
					model.setZjDate((Date)data[4]);
				if (data[5] != null)
					model.setContent(data[5].toString());
				if (data[6] != null)
					model.setMemo(data[6].toString());
				if (data[7] != null)
					model.setFillBy(data[7].toString());
				if (data[8] != null)
					model.setFillDate((Date)data[8]);
				if (data[10] != null)
					form.setZjDate(data[10].toString());
				if (data[11] != null)
					form.setZjName(data[11].toString());
				if (data[12] != null)
					form.setFillDate(data[12].toString());
				if (data[13] != null)
					form.setFillName(data[13].toString());
				form.setModel(model);
				arrlist.add(form);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			obj.setList(arrlist);
			obj.setTotalCount(totalCount);
		return obj;
	}
		
}