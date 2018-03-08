package power.ejb.equ.base;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquRBug.
 * 
 * @see power.ejb.equ.base.EquRBug
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquRBugFacade implements EquRBugFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(EquRBug entity) {
		LogUtil.log("saving EquRBug instance", Level.INFO, null);
		try {
			if(!this.checkSame(entity.getBugCode(), entity.getAttributeCode(), entity.getEnterpriseCode()))
			{
			 if(entity.getId()==null)
				{
					entity.setId(bll.getMaxId("equ_r_bug", "id"));
					entity.setIsUse("Y");
				
				}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}


	public void delete(Long id) {
		LogUtil.log("deleting EquRBug instance", Level.INFO, null);
		try {
			EquRBug entity=this.findById(id);
			entity.setIsUse("N");
			this.update(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public EquRBug update(EquRBug entity) {
		LogUtil.log("updating EquRBug instance", Level.INFO, null);
		try {
			EquRBug result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquRBug findById(Long id) {
		LogUtil
				.log("finding EquRBug instance with id: " + id, Level.INFO,
						null);
		try {
			EquRBug instance = entityManager.find(EquRBug.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}




	
	@SuppressWarnings("unchecked")
	public List<EquRBug> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquRBug instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquRBug model";
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
	
	public boolean checkSame(String bugCode,String attributeCode,String enterpriseCode, Long... Id)
	{
		boolean isSame = false;
		String sql =
			"select count(*) from equ_r_bug t\n" +
			"where t.bug_code='"+bugCode+"'\n" + 
			"and t.attribute_code='"+attributeCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
		
	    if(Id !=null&& Id.length>0){
	    	sql += "  and t.id <> " + Id[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findEquRBugList(String bugCode,String enterprisecode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from equ_r_bug t\n" +
				"where t.bug_code='"+bugCode+"'\n" + 
				"and t.enterprise_code='"+enterprisecode+"'\n" + 
				"and t.is_use='Y'";
           List<EquRBug> list=bll.queryByNativeSQL(sql, EquRBug.class, rowStartIdxAndCount);
           String sqlCount="select count(*) from equ_r_bug t\n" +
			"where t.bug_code='"+bugCode+"'\n" + 
			"and t.enterprise_code='"+enterprisecode+"'\n" + 
			"and t.is_use='Y'";
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}