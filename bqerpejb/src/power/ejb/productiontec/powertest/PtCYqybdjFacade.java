package power.ejb.productiontec.powertest;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class PtCYqybdjFacade implements PtCYqybdjFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PtCYqybdj save(PtCYqybdj entity) {
		LogUtil.log("saving PtCYqybdj instance", Level.INFO, null);
		try {
		
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtCYqybdj update(PtCYqybdj entity) {
		LogUtil.log("updating PtCYqybdj instance", Level.INFO, null);
		try {
			PtCYqybdj result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtCYqybdj findById(Long id) {
		LogUtil.log("finding PtCYqybdj instance with id: " + id, Level.INFO,
				null);
		try {
			PtCYqybdj instance = entityManager.find(PtCYqybdj.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<PtCYqybdj> addList, List<PtCYqybdj> updateList,
			String ids) {
		if (addList.size() > 0) {
			Long bjdId = bll.getMaxId("PT_C_YQYBDJ", "yqybdj_id");
			int i = 0;
			for (PtCYqybdj entity : addList) {
				entity.setYqybdjId(bjdId + (i++));
				this.save(entity);
			}
		}
		if (updateList.size() > 0) {
			for (PtCYqybdj entity : updateList) {

				this.update(entity);
			}
		}
		if (ids.length() > 0)
			deleteMulti(ids);

	}

	public void deleteMulti(String ids) {
		String sql = "delete PT_C_YQYBDJ t\n" + "where t.yqybdj_id in (" + ids
				+ ")";
		bll.exeNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, Long specialId) {

		PageObject obj = new PageObject();

		String sql = "select * from PT_C_YQYBDJ  t\n"
				+ "where t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.jdzy_id=" + specialId;
		List<PtCYqybdj> list = bll.queryByNativeSQL(sql, PtCYqybdj.class);
		obj.setList(list);
		return obj;

	}
}