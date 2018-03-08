package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class PtKkxCFjcodeFacade implements PtKkxCFjcodeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public PtKkxCFjcode save(PtKkxCFjcode entity) throws CodeRepeatException {
		LogUtil.log("saving PtKkxCFjcode instance", Level.INFO, null);
		try {
			if(!this.checkCodeSame(entity.getFjCode()))
			{
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("辅机编码不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(String fjCodes) {
		String sql=
			"delete PT_KKX_C_FJCODE t\n" +
			"where t.fj_code in ("+fjCodes+")";
        bll.exeNativeSQL(sql);
	}

	
	public PtKkxCFjcode update(PtKkxCFjcode entity) {
		LogUtil.log("updating PtKkxCFjcode instance", Level.INFO, null);
		try {
			PtKkxCFjcode result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxCFjcode findById(String id) {
		LogUtil.log("finding PtKkxCFjcode instance with id: " + id, Level.INFO,
				null);
		try {
			PtKkxCFjcode instance = entityManager.find(PtKkxCFjcode.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String fjName,String enterpriseCode, final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(*)\n" +
			"  from PT_KKX_C_FJCODE t\n" + 
			" where t.enterprise_code = '"+enterpriseCode+"'\n" ;
			if(fjName!=null)
			{
				sqlCount+="   and t.fj_name like '%"+fjName+"%'";
			}
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			String sql=
				"select * \n" +
				"  from PT_KKX_C_FJCODE t\n" + 
				" where t.enterprise_code = '"+enterpriseCode+"'\n" ;
				if(fjName!=null)
				{
					sql+="   and t.fj_name like '%"+fjName+"%' \n";
				}
				sql+=" order by t.order_by asc";
			List<PtKkxCFjcode> list=bll.queryByNativeSQL(sql, PtKkxCFjcode.class, rowStartIdxAndCount);
			obj.setList(list);
			return obj;
		}
		else
		{
			return null;
		}
	}
	
	private boolean checkCodeSame(String fjCode)
	{
		String sql=
			"select count(*)\n" +
			"  from PT_KKX_C_FJCODE t\n" + 
			" where t.fj_code='"+fjCode+"'";
        if(Long.parseLong(bll.getSingal(sql).toString())>0)
        	return true;
        else return false;
	}
	

}