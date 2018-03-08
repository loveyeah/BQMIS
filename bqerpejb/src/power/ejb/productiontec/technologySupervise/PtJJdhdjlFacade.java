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
import power.ejb.productiontec.technologySupervise.form.PtJJdhdjlForm;

/**
 * Facade for entity PtJJdhdjl.
 * 
 * @see power.ejb.productiontec.technologySupervise.PtJJdhdjl
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJJdhdjlFacade implements PtJJdhdjlFacadeRemote {
	// property constants
	public static final String JDZY_ID = "jdzyId";
	public static final String MAIN_TOPIC = "mainTopic";
	public static final String EMCEE_MAN = "emceeMan";
	public static final String JOIN_MAN = "joinMan";
	public static final String PLACE = "place";
	public static final String CONTENT = "content";
	public static final String MEMO = "memo";
	public static final String FILL_BY = "fillBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJJdhdjl entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdhdjl entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdhdjl entity) {
		LogUtil.log("saving PtJJdhdjl instance", Level.INFO, null);
		try {
			entity.setJdhdId(bll.getMaxId("PT_J_JDHDJL", "JDHD_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJJdhdjl entity.
	 * 
	 * @param entity
	 *            PtJJdhdjl entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJJdhdjl entity) {
		LogUtil.log("deleting PtJJdhdjl instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJJdhdjl.class, entity
					.getJdhdId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJJdhdjl entity and return it or a copy of it
	 * to the sender. A copy of the PtJJdhdjl entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdhdjl entity to update
	 * @return PtJJdhdjl the persisted PtJJdhdjl entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdhdjl update(PtJJdhdjl entity) {
		LogUtil.log("updating PtJJdhdjl instance", Level.INFO, null);
		try {
			PtJJdhdjl result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJJdhdjl findById(Long id) {
		LogUtil.log("finding PtJJdhdjl instance with id: " + id, Level.INFO,
				null);
		try {
			PtJJdhdjl instance = entityManager.find(PtJJdhdjl.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_J_JDHDJL a\n"
	    + " where a.JDHD_ID in (" + ids + ")\n" ;
        bll.exeNativeSQL(sql);
		
	}

	/**
	 * Find all PtJJdhdjl entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdhdjl property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdhdjl> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJJdhdjl> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtJJdhdjl instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdhdjl model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtJJdhdjl> findByJdzyId(Object jdzyId) {
		return findByProperty(JDZY_ID, jdzyId);
	}

	public List<PtJJdhdjl> findByMainTopic(Object mainTopic) {
		return findByProperty(MAIN_TOPIC, mainTopic);
	}

	public List<PtJJdhdjl> findByEmceeMan(Object emceeMan) {
		return findByProperty(EMCEE_MAN, emceeMan);
	}

	public List<PtJJdhdjl> findByJoinMan(Object joinMan) {
		return findByProperty(JOIN_MAN, joinMan);
	}

	public List<PtJJdhdjl> findByPlace(Object place) {
		return findByProperty(PLACE, place);
	}

	public List<PtJJdhdjl> findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List<PtJJdhdjl> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<PtJJdhdjl> findByFillBy(Object fillBy) {
		return findByProperty(FILL_BY, fillBy);
	}

	public List<PtJJdhdjl> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all PtJJdhdjl entities.
	 * 
	 * @return List<PtJJdhdjl> all PtJJdhdjl entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String jdzyId,String topicName,String enterpriseCode,int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select t.*,\n" +
			"       to_char(t.hd_date, 'yyyy-MM-dd') hdDate,\n" + 
			"       getworkername(t.emcee_man) emceeName,\n" + 
			"       to_char(t.fill_date,'yyyy-MM-dd') fillDate,\n" + 
			"       getworkername(t.fill_by) fillName\n" + 
			"  from pt_j_jdhdjl t\n" + 
			" where t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.jdzy_id ="+ jdzyId+"";
		String sqlCount = 
			"select count(*)\n" +
			"  from pt_j_jdhdjl t\n" + 
			"  where t.jdzy_id ="+jdzyId+"\n" + 
			"  and t.enterprise_code = '"+enterpriseCode+"'";
		if (topicName != null && !"".equals(topicName)){
			sql += "  and t.main_topic like '%"+topicName+"%' \n";
			sqlCount += "  and t.main_topic like '%"+topicName+"%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJJdhdjl model = new PtJJdhdjl();
				PtJJdhdjlForm form= new PtJJdhdjlForm();
				Object []data = (Object[])it.next();
				model.setJdhdId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setJdzyId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					model.setMainTopic(data[2].toString());
				if (data[3] != null)
					model.setHdDate((Date)data[3]);
				if (data[4] != null)
					model.setEmceeMan(data[4].toString());
				if (data[5] != null)
					model.setJoinMan(data[5].toString());
				if (data[6] != null)
					model.setPlace(data[6].toString());
				if (data[7] != null)
					model.setContent(data[7].toString());
				if (data[8] != null)
					model.setMemo(data[8].toString());
				if (data[9] != null)
					model.setFillBy(data[9].toString());
				if (data[10] != null)
					model.setFillDate((Date)data[10]);
				if (data[12] != null)
					form.setHdDate(data[12].toString());
				if (data[13] != null)
					form.setEmceeName(data[13].toString());
				if (data[14] != null)
					form.setFillDate(data[14].toString());
				if (data[15] != null)
					form.setFillName(data[15].toString());
				form.setModel(model);
				arrlist.add(form);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
		return pg;
	}

}