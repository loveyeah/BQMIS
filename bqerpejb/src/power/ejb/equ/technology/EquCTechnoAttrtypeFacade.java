package power.ejb.equ.technology;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquCTechnoAttrtype.
 * 
 * @see power.ejb.equ.technology.EquCTechnoAttrtype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCTechnoAttrtypeFacade implements EquCTechnoAttrtypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	private boolean checkDesc(String enterpriseCode,String desc,Long... id) 
	{
		boolean isSame = false;
		String sql =
			"select count(1) from EQU_C_TECHNO_ATTRTYPE t\n" +
			"where t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'\n" + 
			"and t.attrtype_desc='"+desc+"'";
	    if(id !=null&& id.length>0){
	    	sql += "  and t.attrtype_id <> " + id[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	public EquCTechnoAttrtype save(EquCTechnoAttrtype entity) throws CodeRepeatException {
		LogUtil.log("saving EquCTechnoAttrtype instance", Level.INFO, null);
		try {
			if(!this.checkDesc(entity.getEnterpriseCode(), entity.getAttrtypeDesc()))
			{
				entity.setAttrtypeId(bll.getMaxId("EQU_C_TECHNO_ATTRTYPE", "attrtype_id"));
				entity.setIsUse("Y");
			    entityManager.persist(entity);
			    LogUtil.log("save successful", Level.INFO, null);
			    return entity;
			}
			else
			{
				throw new CodeRepeatException("属性类别名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long attrtypeId) throws CodeRepeatException {
		EquCTechnoAttrtype entity=this.findById(attrtypeId);
		if(entity!=null)
		{
		  entity.setIsUse("N");
		  this.update(entity);
		}
	}

	
	public EquCTechnoAttrtype update(EquCTechnoAttrtype entity) throws CodeRepeatException {
		LogUtil.log("updating EquCTechnoAttrtype instance", Level.INFO, null);
		try {
			if(!this.checkDesc(entity.getEnterpriseCode(), entity.getAttrtypeDesc(), entity.getAttrtypeId()))
			{
			EquCTechnoAttrtype result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("属性类别名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCTechnoAttrtype findById(Long id) {
		LogUtil.log("finding EquCTechnoAttrtype instance with id: " + id,
				Level.INFO, null);
		try {
			EquCTechnoAttrtype instance = entityManager.find(
					EquCTechnoAttrtype.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	


	
	@SuppressWarnings("unchecked")
	public List<EquCTechnoAttrtype> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCTechnoAttrtype instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from EquCTechnoAttrtype model";
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

}