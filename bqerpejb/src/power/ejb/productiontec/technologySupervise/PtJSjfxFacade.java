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
import power.ejb.productiontec.technologySupervise.form.PtJSjfxForm;

/**
 * Facade for entity PtJSjfx.
 * 
 * @see power.ejb.productiontec.technologySupervise.PtJSjfx
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJSjfxFacade implements PtJSjfxFacadeRemote {
	// property constants
	public static final String JDZY_ID = "jdzyId";
	public static final String MAIN_TOPIC = "mainTopic";
	public static final String FX_BY = "fxBy";
	public static final String CONTENT = "content";
	public static final String MEMO = "memo";
	public static final String FILL_BY = "fillBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJSjfx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJSjfx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJSjfx entity) {
		LogUtil.log("saving PtJSjfx instance", Level.INFO, null);
		try {
			entity.setSjfxId(bll.getMaxId("PT_J_SJFX", "SJFX_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJSjfx entity.
	 * 
	 * @param entity
	 *            PtJSjfx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJSjfx entity) {
		LogUtil.log("deleting PtJSjfx instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJSjfx.class, entity
					.getSjfxId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJSjfx entity and return it or a copy of it
	 * to the sender. A copy of the PtJSjfx entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJSjfx entity to update
	 * @return PtJSjfx the persisted PtJSjfx entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJSjfx update(PtJSjfx entity) {
		LogUtil.log("updating PtJSjfx instance", Level.INFO, null);
		try {
			PtJSjfx result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJSjfx findById(Long id) {
		LogUtil
				.log("finding PtJSjfx instance with id: " + id, Level.INFO,
						null);
		try {
			PtJSjfx instance = entityManager.find(PtJSjfx.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_J_SJFX a\n"
	    + " where a.SJFX_ID in (" + ids + ")\n" ;
        bll.exeNativeSQL(sql);
		
	}
	/**
	 * Find all PtJSjfx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJSjfx property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJSjfx> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJSjfx> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding PtJSjfx instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJSjfx model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtJSjfx> findByJdzyId(Object jdzyId) {
		return findByProperty(JDZY_ID, jdzyId);
	}

	public List<PtJSjfx> findByMainTopic(Object mainTopic) {
		return findByProperty(MAIN_TOPIC, mainTopic);
	}

	public List<PtJSjfx> findByFxBy(Object fxBy) {
		return findByProperty(FX_BY, fxBy);
	}

	public List<PtJSjfx> findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List<PtJSjfx> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<PtJSjfx> findByFillBy(Object fillBy) {
		return findByProperty(FILL_BY, fillBy);
	}

	public List<PtJSjfx> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all PtJSjfx entities.
	 * 
	 * @return List<PtJSjfx> all PtJSjfx entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String jdzyId,String topicName,String enterpriseCode,int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql =
			"select t.*,\n" +
			"       to_char(t.fx_date, 'yyyy-MM-dd') fxDate,\n" + 
			"       getworkername(t.fx_by) fxName,\n" + 
			"       to_char(t.fill_date, 'yyyy-MM-dd') fillDate,\n" + 
			"       getworkername(t.fill_by) fillName\n" + 
			"  from pt_j_sjfx t\n" + 
			"  where t.jdzy_id = "+jdzyId+"\n" + 
			"  and t.enterprise_code ='"+enterpriseCode+"'";
		String sqlCount =
			"select count(*)\n" +
			"  from pt_j_sjfx t\n" + 
			"  where t.jdzy_id = "+jdzyId+"\n" + 
			"  and t.enterprise_code ='"+enterpriseCode+"'";
		if (topicName != null && !"".equals(topicName)){
			sql += " and t.main_topic like '%"+topicName+"%'";
			sqlCount += " and t.main_topic like '%"+topicName+"%'";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJSjfx model = new PtJSjfx();
				PtJSjfxForm form= new PtJSjfxForm();
				Object []data = (Object[])it.next();
				model.setSjfxId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setJdzyId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					model.setMainTopic(data[2].toString());
				if (data[3] != null)
					model.setFxBy(data[3].toString());
				if (data[4] != null)
					model.setFxDate((Date)data[4]);
				if (data[5] != null)
					model.setContent(data[5].toString());
				if (data[6] != null)
					model.setMemo(data[6].toString());
				if (data[7] != null)
					model.setFillBy(data[7].toString());
				if (data[8] != null)
					model.setFillDate((Date)data[8]);
				if (data[10] != null)
					form.setFxDate(data[10].toString());
				if (data[11] != null)
					form.setFxName(data[11].toString());
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