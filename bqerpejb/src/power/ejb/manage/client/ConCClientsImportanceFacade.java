package power.ejb.manage.client;

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
 * Facade for entity ConCClientsImportance.
 * 
 * @see power.ejb.manage.client.ConCClientsImportance
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCClientsImportanceFacade implements
		ConCClientsImportanceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public ConCClientsImportance save(ConCClientsImportance entity) throws CodeRepeatException {
		LogUtil.log("saving ConCClientsImportance instance", Level.INFO, null);
		try {
			if(!this.checkSame(entity.getImportanceName(), entity.getEnterpriseCode()))
			{
			entity.setImportanceId(bll.getMaxId("CON_C_CLIENTS_IMPORTANCE", "importance_id"));
			entityManager.persist(entity);
			
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("重要程度描述不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void deleteMulti(String ids)
	{
		String sql=
			"delete CON_C_CLIENTS_IMPORTANCE t\n" +
			"where t.importance_id in ("+ids+")";
		bll.exeNativeSQL(sql);

	}
	
	

	
	public ConCClientsImportance update(ConCClientsImportance entity) throws CodeRepeatException {
		LogUtil
				.log("updating ConCClientsImportance instance", Level.INFO,
						null);
		try {
			if(!this.checkSame(entity.getImportanceName(), entity.getEnterpriseCode(), entity.getImportanceId()))
			{
			ConCClientsImportance result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("重要程度描述不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCClientsImportance findById(Long id) {
		LogUtil.log("finding ConCClientsImportance instance with id: " + id,
				Level.INFO, null);
		try {
			ConCClientsImportance instance = entityManager.find(
					ConCClientsImportance.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String importanceName,String enterpriseCode,int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			if(importanceName == null || "".equals(importanceName)) {
				importanceName = "%";
			}
			String sqlCount = "select count(*) from CON_C_CLIENTS_IMPORTANCE t where t.IMPORTANCE_NAME like '%"+importanceName+"%'  and t.enterprise_code='"+enterpriseCode+"'"
							+ " or t.IMPORTANCE_id like '" + importanceName +"'";
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);
			if(totalCount > 0 ) {
				String sql = "select * from CON_C_CLIENTS_IMPORTANCE t where t.IMPORTANCE_NAME like '%"+importanceName+"%'   and t.enterprise_code='"+enterpriseCode+"'"
							+ " or t.IMPORTANCE_id like '" + importanceName +"'";;
				List<ConCClientsImportance> list = bll.queryByNativeSQL(sql, ConCClientsImportance.class, rowStartIdxAndCount);
				result.setList(list);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private boolean checkSame(String importanceName,String enterpriseCode,Long ...id)
	{
		String sql=
			"select count(1) from CON_C_CLIENTS_IMPORTANCE t\n" +
			"where t.importance_name='"+importanceName+"' and t.enterprise_code='"+enterpriseCode+"'";
		if(id!=null&&id.length>0)
		{
			sql+="\n and t.importance_id <>"+id[0];
		}
		Long count=Long.parseLong(bll.getSingal(sql).toString());
		if(count>0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}