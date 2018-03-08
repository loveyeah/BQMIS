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
 * Facade for entity EquCEquipments.
 * 
 * @see power.ejb.equ.base.EquCEquipments
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCEquipmentsFacade implements EquCEquipmentsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	
	public int save(EquCEquipments entity) {
		if(!CheckAttributeCodeSame(entity.getEnterpriseCode(),entity.getAttributeCode()))
		{
			if(entity.getEquId()==null)
			{
				entity.setEquId(bll.getMaxId("equ_c_equipments", "equ_id"));
				entity.setIsUse("Y");
			
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getEquId().toString());
		}
		else
		{
			return -1;
		}
		
	}


	public void delete(EquCEquipments entity) {
		LogUtil.log("deleting EquCEquipments instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquCEquipments.class, entity
					.getEquId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}


	public boolean update(EquCEquipments entity) {
		if(!CheckAttributeCodeSame(entity.getEnterpriseCode(),entity.getAttributeCode(),entity.getEquId()))
		{
		
			entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return true;
		}
		else
		{
			return false;
		}
	}

	public EquCEquipments findById(Long id) {
		LogUtil.log("finding EquCEquipments instance with id: " + id,
				Level.INFO, null);
		try {
			EquCEquipments instance = entityManager.find(EquCEquipments.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EquCEquipments> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCEquipments instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCEquipments model";
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
			String sql="select * from  equ_c_equipments \n";
			if(strWhere!="")
			{
				sql=sql+" where  "+strWhere;
			}
			List<EquCEquipments> list=bll.queryByNativeSQL(sql, EquCEquipments.class, rowStartIdxAndCount);
			String sqlCount="select count(*)　from equ_c_equipments \n";
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
	public List<EquCEquipments> getListByParent(String equCode,String enterpriseCode)
	{
		//modify by fyyang 090317
		String strWhere="";
		if(equCode.equals("root"))
		{
			 strWhere="   length(attribute_code)=2  \n";

		}
		else 
		{
			if(equCode.length()==4)
			{
				strWhere="  p_attribute_code='"+equCode+"'  \n";
			}
			else
			{
				strWhere="  attribute_code like '"+equCode+"%'  \n";
				if(equCode.length()==2)  strWhere=strWhere+"  and length(attribute_code)=4 \n";
				if(equCode.length()==5 && !equCode.equals("00BHF"))
				{	
					strWhere=strWhere+"  and (length(attribute_code)=12 or  length(attribute_code)=7)\n";
				}
				if(equCode.length()==5 && equCode.equals("00BHF"))
				{	if(equCode.equals("00BHF"))
					{
						strWhere=strWhere+"  and length(attribute_code)=7\n";
					}
					else
					{
						strWhere=strWhere+"  and (length(attribute_code)=12 or  length(attribute_code)=7)\n";
					}
				}
				if(equCode.length()==7 && equCode.equals("00BHF01") || equCode.equals("00BHF02"))
				{
					if(equCode.equals("00BHF01"))
					{
						strWhere="  attribute_code like '00BHF%'";
						strWhere = strWhere + "and length(attribute_code)= 12 and mod(to_number(substr(attribute_code,6,2)),2)=1";
					}
					else if(equCode.equals("00BHF02"))
					{
						strWhere="  attribute_code like '00BHF%'";
						strWhere = strWhere + "and length(attribute_code)= 12 and mod(to_number(substr(attribute_code,6,2)),2)=0";
					}
					else
					{
						strWhere=strWhere+" and ((length(attribute_code)=12 or length(attribute_code)=13 )) \n";
					}
				}
				if(equCode.length()==7 && !equCode.equals("00BHF01") )
				{
						strWhere=strWhere+" and ((length(attribute_code)=12 or length(attribute_code)=13 )) \n";
				}
				if(equCode.length()==12||equCode.length()==13)   strWhere=strWhere+"  and ((length(attribute_code)=16 or length(attribute_code)=17 )) \n";
				if(equCode.length()==16||equCode.length()==17)  strWhere=strWhere+" and 1<>1";
			}
		}
		 strWhere=strWhere+
			"and  enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y' order by attribute_code";

		PageObject result= findList(strWhere);
		return result.getList();
		
	}
	
	public boolean IfHasChild(String equCode,String enterpriseCode)
	{
		//modify by fyyang 090317
		boolean isSame = false;
		
		String strWhere="";
		if(equCode.equals("root"))
		{
			 strWhere="   length(attribute_code)=2  \n";

		}
		else 
		{
			if(equCode.length()==4)
			{
				strWhere="  p_attribute_code='"+equCode+"'  \n";
			}
			else
			{
				strWhere="  attribute_code like '"+equCode+"%'  \n";
				if(equCode.length()==2)  strWhere=strWhere+"  and length(attribute_code)=4 \n";
				if(equCode.length()==5)   strWhere=strWhere+"  and length(attribute_code)=12 or length(attribute_code)=7\n";
				if(equCode.length()==7)   strWhere=strWhere+"  and (length(attribute_code)=12 or length(attribute_code)=13 ) \n";
				if(equCode.length()==12||equCode.length()==13)   strWhere=strWhere+"  and (length(attribute_code)=16 or length(attribute_code)=17 ) \n";
				if(equCode.length()==16||equCode.length()==17)  strWhere=strWhere+" and 1<>1";
			}
		}
		 strWhere=strWhere+
			"and  enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y' order by attribute_code";
		String sql=
			"select count(1)\n" +
			"  from equ_c_equipments t\n" + 
			" where "+strWhere;
		
		 if(Long.parseLong((bll.getSingal(sql).toString()))>0)
			{
		    	isSame = true;
			}
		    return isSame;

	}
	
	public EquCEquipments findByCode(String attributeCode,String enterpriseCode)
	{
		String strWhere="  attribute_code='"+attributeCode+"' \n" +
				" and  enterprise_code='"+enterpriseCode+"' \n"+
				" and is_use='Y'";
		PageObject result= findList(strWhere);
		if(result.getList()!=null)
		{
			if(result.getList().size()>0)
			{
			return  (EquCEquipments)result.getList().get(0);
			}
		}
		return null;
	}
	
	public boolean CheckAttributeCodeSame(String enterpriseCode,String attributeCode,Long... equid) 
	{ 
		boolean isSame = false;
		String sql =
			"select count(*) from equ_c_equipments t\n" +
			"where t.attribute_code='"+attributeCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
	    if(equid !=null&& equid.length>0){
	    	sql += "  and t.equ_id <> " + equid[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

	public PageObject findListByNameOrCode(String NameOrCode,String enterpriseCode,int start,int limit)
	{
		String strWhere=
			" (attribute_code like '%"+NameOrCode+"%' or equ_name like '%"+NameOrCode+"%' )\n" + 
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'  order by attribute_code";

		PageObject result= findList(strWhere,start,limit);
		return result;
	}
	
	public PageObject findListByLocationCode(String locationCode,String enterpriseCode,int start,int limit)
	{
		String strWhere=
			"  location_code like '"+locationCode+"' \n" + 
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y' order by attribute_code";

		PageObject result=new PageObject();
		 
		 result= findList(strWhere,start,limit);
		
		return result;
	}
	
	public PageObject findListByInstallCode(String installCode,String enterpriseCode,int start,int limit)
	{
		String strWhere=
			"  installation_code like '"+installCode+"' \n" + 
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y' order by attribute_code";

		PageObject result= findList(strWhere,start,limit);
		return result;
	}
	
	public void deleteLocationCode(String locationCode,String enterpriseCode)
	{
		String sql=
			"update equ_c_equipments t\n" +
			"set t.location_code=''\n" + 
			"where t.location_code='"+locationCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
       bll.exeNativeSQL(sql);
	}
	
	public void deleteInstallationCode(String installCode,String enterpriseCode)
	{
		String sql=
			"update equ_c_equipments t\n" +
			"set t.installation_code=''\n" + 
			"where t.installation_code='"+installCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
       bll.exeNativeSQL(sql);
	}
	
	


	

}