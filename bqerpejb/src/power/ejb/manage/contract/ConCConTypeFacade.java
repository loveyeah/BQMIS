package power.ejb.manage.contract;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.ContypeInfo;

@Stateless
public class ConCConTypeFacade implements ConCConTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public ConCConType save(ConCConType entity) {
		LogUtil.log("saving ConCConType instance", Level.INFO, null);
		try {
			if (entity.getContypeId() == null) {
				entity.setContypeId(bll
						.getMaxId("CON_C_CON_TYPE", "CONTYPE_ID"));
			}
			entity.setLastModifiedDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ConCConType entity) {
		entity.setIsUse("N");
		this.update(entity);
	}

	public ConCConType update(ConCConType entity) {
		LogUtil.log("updating ConCConType instance", Level.INFO, null);
		try {
			ConCConType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCConType findById(Long id) {
		LogUtil.log("finding ConCConType instance with id: " + id, Level.INFO,
				null);
		try {
			ConCConType instance = entityManager.find(ConCConType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	// @SuppressWarnings("unchecked")
	// public List<ConCConType> findAll(final int... rowStartIdxAndCount) {
	//
	// }

	public ContypeInfo findInfoById(Long id) {
		String sql = "select\n" + "t.contype_id,\n" + "t.p_contype_id,\n"
				+ "t.con_type_desc,\n" + "t.mark_code,\n" + "t.memo,\n"
				+ "t.last_modified_by,\n" + "t.enterprise_code,\n"
				+ "t.is_use,\n"
				+ "to_char(t.last_modified_date,'yyyy-mm-dd'),\n"
				+ "getworkername(t.last_modified_by)\n"
				+ "from CON_C_CON_TYPE t\n" + "where t.contype_id = " + id;
		Object[] ob = (Object[]) bll.getSingal(sql);
		ContypeInfo info = new ContypeInfo();
		info.setContypeId(Long.parseLong(ob[0].toString()));
		if (ob[1] != null)
			info.setPContypeId(Long.parseLong(ob[1].toString()));
		if (ob[2] != null)
			info.setConTypeDesc(ob[2].toString());
		if (ob[3] != null)
			info.setMarkCode(ob[3].toString());
		if (ob[4] != null)
			info.setMemo(ob[4].toString());
		if (ob[5] != null)
			info.setLastModifiedBy(ob[5].toString());
		if (ob[6] != null)
			info.setEnterpriseCode(ob[6].toString());
		if (ob[7] != null)
			info.setIsUse(ob[7].toString());
		if (ob[8] != null)
			info.setLastModifiedDate(ob[8].toString());
		if (ob[9] != null)
			info.setLastModifiedName(ob[9].toString());
		return info;
	}

	public List<ConCConType> findByPContypeId(Long PContypeId) {
		String sql = "select t.* from CON_C_CON_TYPE t where t.p_contype_id="
				+ PContypeId;
		return bll.queryByNativeSQL(sql, ConCConType.class);
	}
	public boolean getByPContypeId(Long PContypeId){
		int count=0;
		String sql = "select count(1) from CON_C_CON_TYPE t where t.p_contype_id="
			+ PContypeId;
		count=Integer.parseInt(bll.getSingal(sql).toString());
		if(count!=0){
			return true;
		}
		else
			return false;
	}
}