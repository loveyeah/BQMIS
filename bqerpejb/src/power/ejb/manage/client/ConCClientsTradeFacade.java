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
 * Facade for entity ConCClientsTrade.
 * 
 * @see power.ejb.manage.client.ConCClientsTrade
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCClientsTradeFacade implements ConCClientsTradeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public ConCClientsTrade save(ConCClientsTrade entity) throws CodeRepeatException {
		LogUtil.log("saving ConCClientsTrade instance", Level.INFO, null);
		try {
			if(!this.checkSame(entity.getTradeName(), entity.getEnterpriseCode()))
			{
				entity.setTradeId(bll.getMaxId("CON_C_CLIENTS_TRADE", "trade_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("行业名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
    public void deleteMulti(String ids)
    {
    	String sql=
    		"delete  CON_C_CLIENTS_TRADE a\n" +
    		"where a.trade_id in ("+ids+")";
    	bll.exeNativeSQL(sql);
    }

	
	public ConCClientsTrade update(ConCClientsTrade entity) throws CodeRepeatException {
		LogUtil.log("updating ConCClientsTrade instance", Level.INFO, null);
		try {
			if(!this.checkSame(entity.getTradeName(), entity.getEnterpriseCode(), entity.getTradeId()))
			{
			ConCClientsTrade result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("行业名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCClientsTrade findById(Long id) {
		LogUtil.log("finding ConCClientsTrade instance with id: " + id,
				Level.INFO, null);
		try {
			ConCClientsTrade instance = entityManager.find(
					ConCClientsTrade.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String tradeName,String enterpriseCode,int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			if(tradeName == null || "".equals(tradeName)) {
				tradeName = "%";
			}
			String sqlCount = "select count(*) from CON_C_CLIENTS_TRADE t where t.TRADE_NAME like '%"+tradeName+"%'  and t.enterprise_code='"+enterpriseCode+"'"
							+ " or t.TRADE_ID like '"+ tradeName + "'";
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);
			if(totalCount > 0 ) {
				String sql = "select * from CON_C_CLIENTS_TRADE t where t.TRADE_NAME like '%"+tradeName+"%'  and t.enterprise_code='"+enterpriseCode+"'"
							+ " or t.TRADE_ID like '"+ tradeName + "'";
				List<ConCClientsTrade> list = bll.queryByNativeSQL(sql, ConCClientsTrade.class, rowStartIdxAndCount);
				result.setList(list);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private boolean checkSame(String tradeName,String enterpriseCode,Long ... id)
	{
		String sql=
			"select count(1) from CON_C_CLIENTS_TRADE a\n" +
			"where a.trade_name='"+tradeName+"' and a.enterprise_code='"+enterpriseCode+"'";
		if(id!=null&&id.length>0)
		{
			sql+=" \n and a.trade_id <>"+id[0];
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