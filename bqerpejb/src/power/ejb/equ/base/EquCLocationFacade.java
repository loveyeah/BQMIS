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
 * Facade for entity EquCLocation.
 * 
 * @see power.ejb.equ.base.EquCLocation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCLocationFacade implements EquCLocationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(EquCLocation entity) {
		if(!this.CheckLocationCodeSame(entity.getEnterpriseCode(), entity.getLocationCode()))
		{
			if(entity.getLocationId()==null)
			{
				entity.setLocationId(bll.getMaxId("equ_c_location", "location_id"));
				entity.setIsUse("Y");
			
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getLocationId().toString());
			
		}
		else
		{
			return -1;
		}
	}


	public boolean delete(Long locationId)
	{
		EquCLocation entity=this.findById(locationId);
		if(!this.ifHasChild(entity.getLocationCode(), entity.getEnterpriseCode()))
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

	public boolean update(EquCLocation entity) {
		
		try {
			if(!this.CheckLocationCodeSame(entity.getEnterpriseCode(), entity.getLocationCode(),entity.getLocationId()))
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

	public EquCLocation findById(Long id) {
		LogUtil.log("finding EquCLocation instance with id: " + id, Level.INFO,
				null);
		try {
			EquCLocation instance = entityManager.find(EquCLocation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}



	@SuppressWarnings("unchecked")
	public List<EquCLocation> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCLocation instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCLocation model";
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
	
	public boolean CheckLocationCodeSame(String enterpriseCode,String locationCode,Long... locationId) 
	{ 
		boolean isSame = false;
		String sql =
			"select count(*) from equ_c_location t\n" +
			"where t.location_code='"+locationCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";

	    if(locationId !=null&& locationId.length>0){
	    	sql += "  and t.location_id <> " + locationId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findList(String strWhere,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql="select * from  equ_c_location \n";
			if(strWhere!="")
			{
				sql=sql+" where  "+strWhere;
			}
			List<EquCLocation> list=bll.queryByNativeSQL(sql, EquCLocation.class, rowStartIdxAndCount);
			String sqlCount="select count(*)　from equ_c_location \n";
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
	
	@SuppressWarnings("unchecked")
	public List<EquCLocation> getListByParent(String locationCode,String enterpriseCode)
	{
		String strWhere=
			" p_location_code='"+locationCode+"'\n" + 
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'  order by location_code";
		PageObject result=findList(strWhere);
		return result.getList();

	}
	
	public boolean ifHasChild(String locationCode,String enterpriseCode)
	{
		String strWhere=
			" p_location_code='"+locationCode+"'\n" + 
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'";
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
	
	public EquCLocation findByCode(String locationCode,String enterpriseCode)
	{
		String strWhere=
			"  location_code='"+locationCode+"'\n" + 
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'";
		PageObject result=findList(strWhere);
		if(result.getList()!=null)
		{
			if(result.getList().size()>0)
			{
			return (EquCLocation)result.getList().get(0);
			}
		}
		return null;
	}
	

}