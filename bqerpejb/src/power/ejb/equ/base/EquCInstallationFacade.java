package power.ejb.equ.base;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity EquCInstallation.
 * 
 * @see power.ejb.equ.base.EquCInstallation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCInstallationFacade implements EquCInstallationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(EquCInstallation entity) {
		if(!this.CheckInstallCodeSame(entity.getEnterpriseCode(), entity.getInstallationCode()))
		{
			if(entity.getId()==null)
			{
				entity.setId(bll.getMaxId("equ_c_installation", "id"));
				entity.setIsUse("Y");
			
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getId().toString());
			
		}
		else
		{
			return -1;
		}
	}


	public boolean delete(Long installId) {
		
		EquCInstallation entity=this.findById(installId);
		if(!this.ifHasChild(entity.getInstallationCode(), entity.getEnterpriseCode()))
		{
		entity.setIsUse("N");
		this.update(entity);
		return true;
		}
		else
		{
			return false;
		}
	}

	
	public boolean update(EquCInstallation entity) {
		try {
			if(!this.CheckInstallCodeSame(entity.getEnterpriseCode(), entity.getInstallationCode(), entity.getId()))
			{
			    entityManager.merge(entity);
			    LogUtil.log("update successful", Level.INFO, null);
			    return true;
			}
			else
			{
				return false;
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCInstallation findById(Long id) {
		LogUtil.log("finding EquCInstallation instance with id: " + id,
				Level.INFO, null);
		try {
			EquCInstallation instance = entityManager.find(
					EquCInstallation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	@SuppressWarnings("unchecked")
	public List<EquCInstallation> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCInstallation instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCInstallation model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject findList(String strWhere,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql="select * from  equ_c_installation \n";
			if(strWhere!="")
			{
				sql=sql+" where  "+strWhere;
			}
			List<EquCInstallation> list=bll.queryByNativeSQL(sql, EquCInstallation.class, rowStartIdxAndCount);
			String sqlCount="select count(*)　from equ_c_installation \n";
			if(strWhere!="")
			{
				sqlCount=sqlCount+" where  "+strWhere;
			}
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
			
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
	
	public boolean CheckInstallCodeSame(String enterpriseCode,String installCode,Long... installId)
	{
		boolean isSame = false;
		String sql =
			"select count(*) from equ_c_installation a\n" +
			"where a.installation_code='"+installCode+"'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"'\n" + 
			"and a.is_use='Y'";

	    if(installId !=null&& installId.length>0){
	    	sql += "  and a.id <> " + installId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	public EquCInstallation findByCode(String installCode,String enterpriseCode)
	{
		String strWhere=
			"  installation_code='"+installCode+"'\n" +
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'";
		PageObject result=findList(strWhere);
		if(result.getList()!=null)
		{
			if(result.getList().size()>0)
			{
			return (EquCInstallation)result.getList().get(0);
			}
		}
		return null;
	}
	
	public List<EquCInstallation> getListByParent(String installCode,String enterpriseCode)
	{
		String strWhere=
			" father_code='"+installCode+"'\n" +
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'\n" + 
			"order by installation_code";
		PageObject result=findList(strWhere);
		return result.getList();

	}
	
	public boolean ifHasChild(String installCode,String enterpriseCode)
	{
		String strWhere=
			" father_code='"+installCode+"'\n" +
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'\n" + 
			"order by installation_code";
		PageObject result=findList(strWhere);
		if(result.getTotalCount()==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}