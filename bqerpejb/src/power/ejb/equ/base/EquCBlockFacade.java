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
 * Facade for entity EquCBlock.
 * 
 * @see power.ejb.equ.base.EquCBlock
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCBlockFacade implements EquCBlockFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(EquCBlock entity) {
		if(!CheckBlockCodeSame(entity.getEnterpriseCode(),entity.getBlockCode()))
		{
			if(entity.getId()==null)
			{
				entity.setId(bll.getMaxId("equ_c_block", "id"));
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

	public void delete(EquCBlock entity) {
		LogUtil.log("deleting EquCBlock instance", Level.INFO, null);
		try {
			entity = entityManager
					.getReference(EquCBlock.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}


	public EquCBlock update(EquCBlock entity) {
		LogUtil.log("updating EquCBlock instance", Level.INFO, null);
		try {
			EquCBlock result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCBlock findById(Long id) {
		LogUtil.log("finding EquCBlock instance with id: " + id, Level.INFO,
				null);
		try {
			EquCBlock instance = entityManager.find(EquCBlock.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EquCBlock> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCBlock instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCBlock model";
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
	public PageObject findEquList(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select *　from equ_c_block t\n" +
				" where (t.block_code like '%"+fuzzy+"%' or t.block_name like '%"+fuzzy+"%')\n" + 
				" and t.enterprise_code='"+enterpriseCode+"'   and t.is_use='Y'  order by t.block_code";
			List<EquCBlock> list=bll.queryByNativeSQL(sql, EquCBlock.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*)　from equ_c_block t\n" +
				" where (t.block_code like '%"+fuzzy+"%' or t.block_name like '%"+fuzzy+"%')\n" + 
				" and t.enterprise_code='"+enterpriseCode+"'   and t.is_use='Y' ";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public boolean CheckBlockCodeSame(String enterpriseCode,String equCode,Long... equid) 
	{ 
		boolean isSame = false;
		String sql =
			"select count(1) from equ_c_block t\n" +
			"where t.block_code='"+equCode+"' and t.is_use='Y' and t.enterprise_code='"+enterpriseCode+"'";

	    if(equid !=null&& equid.length>0){
	    	sql += "  and t.id <> " + equid[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}