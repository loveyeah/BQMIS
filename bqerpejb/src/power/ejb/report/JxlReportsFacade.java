package power.ejb.report;

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

/**
 * Facade for entity JxlReports.
 * 
 * @see power.ejb.report.JxlReports
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class JxlReportsFacade implements JxlReportsFacadeRemote {
	// property constants
	public static final String CONTENT = "content";
	public static final String MEMO = "memo";
	public static final String IS_USE = "isUse";
	public static final String DATE_TYPE = "dateType";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
 
	public void save(JxlReports entity) throws CodeRepeatException{ 
		try {
			JxlReports obj = entityManager.find(JxlReports.class, entity.getCode());
			if(obj != null)
			{
				throw new CodeRepeatException("保存失败,模板编号重复。");
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null); 
		} catch (CodeRepeatException re) { 
			throw re;
		}
	} 
	
	public void delete(JxlReports entity) { 
		try {
			entity = entityManager.getReference(JxlReports.class, entity
					.getCode());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public JxlReports update(JxlReports entity) { 
		try {
			JxlReports result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public JxlReports findById(String id) { 
		try {
			JxlReports instance = entityManager.find(JxlReports.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	} 
	@SuppressWarnings("unchecked")
	public List<JxlReports> findAll() { 
		try {
			final String queryString = "select model from JxlReports model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public PageObject findReportModelList(String workerCode, int... rowStartIdxAndCount) {
		String countsql = "select count(*)\n" +
                          "  from jxl_reports a, jxl_reports_right b\n" + 
                          " where a.code = b.code\n" + 
                          "   and b.worker_code = '"+workerCode+"'\n" + 
                          "   and a.is_use = 'Y'";
		String sql = "select distinct a.code, a.memo, a.date_type\n" +
			         "  from jxl_reports a, jxl_reports_right b\n" + 
			         " where a.code = b.code\n" + 
			         "   and b.worker_code = '"+workerCode+"'\n" + 
			         "   and a.is_use = 'Y'";
;

		Long count = Long.parseLong(bll.getSingal(countsql).toString());
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<JxlReports> arrayList = new ArrayList<JxlReports>();
		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] data = iterator.next();
			JxlReports model = new JxlReports();
			if (data[0] != null) {
				model.setCode(data[0].toString());
			}
			if (data[1] != null){
				model.setMemo(data[1].toString());
			}
			if (data[2] != null){
				model.setDateType(data[2].toString());
			}
			arrayList.add(model);
		}
		PageObject obj = new PageObject();
		obj.setList(arrayList);
		obj.setTotalCount(count);
		return obj;
	}

}