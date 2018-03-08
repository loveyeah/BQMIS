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
 * Facade for entity EquCClass.
 * 
 * @see power.ejb.equ.base.EquCClass
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCClassFacade implements EquCClassFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(EquCClass entity) {
		if(!this.CheckClassCodeSame(entity.getEnterpriseCode(), entity.getClassCode(), entity.getClassLevel()))
		{
			if(entity.getEquClassId()==null)
			{
				entity.setEquClassId(bll.getMaxId("equ_c_class", "equ_class_id"));
				entity.setIsUse("Y");
			
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getEquClassId().toString());
			
		}
		else
		{
			return -1;
		}
	}


//	public void delete(EquCClass entity) {
//		LogUtil.log("deleting EquCClass instance", Level.INFO, null);
//		try {
//			entity = entityManager.getReference(EquCClass.class, entity
//					.getEquClassId());
//			entityManager.remove(entity);
//			LogUtil.log("delete successful", Level.INFO, null);
//		} catch (RuntimeException re) {
//			LogUtil.log("delete failed", Level.SEVERE, re);
//			throw re;
//		}
//	}

	public void delete(Long classId)
	{
		EquCClass model=findById(classId);
		model.setIsUse("N");
		update(model);
		
	}
	
	
	
	public boolean update(EquCClass entity) {
		if(!this.CheckClassCodeSame(entity.getEnterpriseCode(), entity.getClassCode(), entity.getClassLevel(),entity.getEquClassId()))
		{
			
			entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return true;
		} 
		else
		{
			return  false;
		}
			
	}


	public EquCClass findById(Long id) {
		LogUtil.log("finding EquCClass instance with id: " + id, Level.INFO,
				null);
		try {
			EquCClass instance = entityManager.find(EquCClass.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	@SuppressWarnings("unchecked")
	public List<EquCClass> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCClass instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCClass model";
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
	public PageObject findClassList(String fuzzy,String classLevel, String enterpriseCode,final int... rowStartIdxAndCount)
	{
		
		try {
			PageObject result = new PageObject(); 
			String sql="select  * from equ_c_class t\n" +
				       " where (t.class_code like '%"+fuzzy+"%' or t.class_name like '%"+fuzzy+"%')\n" + 
				       " and t.class_level='"+classLevel+"'\n" + 
				       " and t.enterprise_code='"+enterpriseCode+"'\n" + 
				       " and t.is_use='Y'  order by t.class_code";

			List<EquCClass> list=bll.queryByNativeSQL(sql, EquCClass.class, rowStartIdxAndCount);
			String sqlCount="select  count(*) from equ_c_class t\n" +
			                " where (t.class_code like '%"+fuzzy+"%' or t.class_name like '%"+fuzzy+"%')\n" + 
			                " and t.class_level='"+classLevel+"'\n" + 
			                " and t.enterprise_code='"+enterpriseCode+"'\n" + 
			                " and t.is_use='Y'  order by t.class_code";
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
	public PageObject getClassListForSelect(String fuzzy,String code,int length,String classLevel,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from equ_c_class a\n" +
				" where a.class_level='"+classLevel+"'\n" + 
				" and a.class_code like '"+code+"%'\n" + 
				" and length(a.class_code)="+length+"\n" + 
				" and  (a.class_code like '%"+fuzzy+"%' or a.class_name like '%"+fuzzy+"%')\n" + 
				" and a.enterprise_code='"+enterpriseCode+"'\n" + 
				" and a.is_use='Y'";
			List<EquCClass> list=bll.queryByNativeSQL(sql, EquCClass.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from equ_c_class a\n" +
				" where a.class_level='"+classLevel+"'\n" + 
				" and a.class_code like '"+code+"%'\n" + 
				" and length(a.class_code)="+length+"\n" + 
				" and  (a.class_code like '%"+fuzzy+"%' or a.class_name like '%"+fuzzy+"%')\n" + 
				" and a.enterprise_code='"+enterpriseCode+"'\n" + 
				" and a.is_use='Y'";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
	public boolean CheckClassCodeSame(String enterpriseCode,String classCode,String classLevel,Long... classid) 
	{ 
		boolean isSame = false;
		String sql =
			"select count(*) from equ_c_class t\n" +
			" where t.class_code='"+classCode+"' and t.enterprise_code='"+enterpriseCode+"' \n"+
			"and t.class_level='"+classLevel+"' and t.is_use='Y'";
	    if(classid !=null&& classid.length>0){
	    	sql += "  and t.equ_class_id <> " + classid[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}