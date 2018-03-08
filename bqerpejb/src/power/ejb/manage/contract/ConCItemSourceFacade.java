package power.ejb.manage.contract;

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

/**
 * Facade for entity ConCItemSource.
 * 
 * @see power.ejb.manage.contract.ConCItemSource
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCItemSourceFacade implements ConCItemSourceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public ConCItemSource save(ConCItemSource entity) {
		LogUtil.log("saving ConCItemSource instance", Level.INFO, null);
		try {
			entity.setItemId(bll.getMaxId("CON_C_ITEM_SOURCE", "ITEM_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ConCItemSource entity) {
		entity.setIsUse("N");
		this.update(entity);
	}

	public ConCItemSource update(ConCItemSource entity) {
		LogUtil.log("updating ConCItemSource instance", Level.INFO, null);
		try {
			ConCItemSource result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCItemSource findById(Long id) {
		LogUtil.log("finding ConCItemSource instance with id: " + id,
				Level.INFO, null);
		try {
			ConCItemSource instance = entityManager.find(ConCItemSource.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public ConCItemSource findInfoById(Long id)
	{
		String sql = 
			"select a.item_id,\n" +
			"       a.p_item_id,\n" + 
			"       a.item_code,\n" + 
			"       a.item_name,\n" + 
			"       a.memo,\n" + 
			"       a.display_no\n" + 
			"  from CON_C_ITEM_SOURCE a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.item_id = "+id+"";
		Object[] data = (Object[]) bll.getSingal(sql);
		ConCItemSource model = new ConCItemSource();
		model.setItemId(Long.parseLong(data[0].toString()));
		if(data[1] != null)
			model.setPItemId(Long.parseLong(data[1].toString()));
		if(data[2] != null)
			model.setItemCode(data[2].toString());
		if(data[3] != null)
			model.setItemName(data[3].toString());
		if(data[4] != null)
			model.setMemo(data[4].toString());
		if(data[5] != null)
			model.setDisplayNo(Long.parseLong(data[5].toString()));
	
		return model;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConCItemSource> findByPItemId(Long PItemId) {
		String sql = "select t.* from CON_C_ITEM_SOURCE t where t.p_item_id="
				+ PItemId;
		sql += " and t.is_use='Y' ";
		return bll.queryByNativeSQL(sql, ConCItemSource.class);
	}
	
	public boolean getByPItemId(Long PItemId){
		int count=0;
		String sql = "select count(1) from CON_C_ITEM_SOURCE t where t.p_item_id="
			+ PItemId;
		sql += " and t.is_use='Y' ";
		count=Integer.parseInt(bll.getSingal(sql).toString());
		if(count!=0){
			return true;
		}
		else
			return false;
	}

	public ConCItemSource getRecordByCode(String code) {
		String sql = "select a.* from CON_C_ITEM_SOURCE a where a.item_code='"
			+ code + "' \n";
		sql += " and a.is_use='Y' ";
		List<ConCItemSource> list = bll.queryByNativeSQL(sql, ConCItemSource.class);
		if(list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return new ConCItemSource();
	}
}