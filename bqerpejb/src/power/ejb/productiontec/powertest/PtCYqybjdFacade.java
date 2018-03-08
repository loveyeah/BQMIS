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
public class PtCYqybjdFacade implements PtCYqybjdFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PtCYqybjd save(PtCYqybjd entity) {
		LogUtil.log("saving PtCYqybjd instance", Level.INFO, null);
		try {
			
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtCYqybjd update(PtCYqybjd entity) {
		LogUtil.log("updating PtCYqybjd instance", Level.INFO, null);
		try {
			PtCYqybjd result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtCYqybjd findById(Long id) {
		LogUtil.log("finding PtCYqybjd instance with id: " + id, Level.INFO,
				null);
		try {
			PtCYqybjd instance = entityManager.find(PtCYqybjd.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<PtCYqybjd> addList, List<PtCYqybjd> updateList,
			String ids) {
		if (addList.size() > 0) {
			Long bjdId = bll.getMaxId("PT_C_YQYBJD", "yqybjd_id");
			int i = 0;
			for (PtCYqybjd entity : addList) {
				entity.setYqybjdId(bjdId + (i++));
				this.save(entity);
			}
		}
		if (updateList.size() > 0) {
			for (PtCYqybjd entity : updateList) {

				this.update(entity);
			}
		}
		if (ids.length() > 0)
			deleteMulti(ids);

	}

	public void deleteMulti(String ids) {
		String sql = "delete PT_C_YQYBJD t\n" + "where t.yqybjd_id in (" + ids
				+ ")";
		bll.exeNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, Long specialId) {

		PageObject obj = new PageObject();

		String sql = "select * from PT_C_YQYBJD  t\n"
				+ "where t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.jdzy_id=" + specialId;
		List<PtCYqybjd> list = bll.queryByNativeSQL(sql, PtCYqybjd.class);
		obj.setList(list);

		return obj;

	}

}