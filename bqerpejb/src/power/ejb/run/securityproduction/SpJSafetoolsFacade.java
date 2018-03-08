package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.run.securityproduction.form.SafeToolInfo;

/**
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafetoolsFacade implements SpJSafetoolsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public SpJSafetools save(SpJSafetools entity) throws CodeRepeatException {
		LogUtil.log("saving SpJSafetools instance", Level.INFO, null);
		try {
			if(!this.checkCodeSame(entity.getToolsCode()))
			{
			entity.setToolsId(bll.getMaxId("SP_J_SAFETOOLS", "tools_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("编号不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	

	
	public SpJSafetools update(SpJSafetools entity) throws CodeRepeatException {
		LogUtil.log("updating SpJSafetools instance", Level.INFO, null);
		try {
			if(!this.checkCodeSame(entity.getToolsCode(),entity.getToolsId()))
			{
			SpJSafetools result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("编号不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String ids)
	{
		String sql=
			"delete SP_J_SAFETOOLS t\n" +
			"where t.tools_id in ("+ids+")";
       bll.exeNativeSQL(sql);
	}

	public SpJSafetools findById(Long id) {
		LogUtil.log("finding SpJSafetools instance with id: " + id, Level.INFO,
				null);
		try {
			SpJSafetools instance = entityManager.find(SpJSafetools.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String toolsNameOrChargeMan,String enterpriseCode,final int... rowStartIdxAndCount) {
	
	String sqlCount="select count(*)  \n"+
		" from SP_J_SAFETOOLS t\n" + 
		"where t.enterprise_code='"+enterpriseCode+"'\n" + 
		"and (t.tools_names like '%"+toolsNameOrChargeMan+"%' or GETWORKERNAME(t.charge_man) like '%"+toolsNameOrChargeMan+"%')";
	Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
	if(count>0)
	{
		PageObject obj=new PageObject();
		obj.setTotalCount(count);
		List objList=new ArrayList();
	   String sql=
		"select t.tools_id,t.tools_names,t.tools_code,\n" +
		"t.tools_sizes,t.factory,\n" + 
		"to_char(t.manu_date,'yyyy-MM-dd'),\n" + 
		"t.charge_man,\n"+
		"GETWORKERNAME(t.charge_man),\n" + 
		"to_char(t.check_date,'yyyy-MM-dd'),\n" + 
		"t.state,t.memo,t.enterprise_code\n" + 
		" from SP_J_SAFETOOLS t\n" + 
		"where t.enterprise_code='"+enterpriseCode+"'\n" + 
		"and (t.tools_names like '%"+toolsNameOrChargeMan+"%' or GETWORKERNAME(t.charge_man) like '%"+toolsNameOrChargeMan+"%')";
	 
	   List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	   Iterator it=  list.iterator();
	   while(it.hasNext())
	   {
		   SafeToolInfo info=new SafeToolInfo();
		   SpJSafetools model=new SpJSafetools();
		   Object [] data=( Object [])it.next();
		   model.setToolsId(Long.parseLong(data[0].toString()));
		   if(data[1]!=null)
		   {
			   model.setToolsNames(data[1].toString());
		   }
		   if(data[2]!=null)
		   {
			   model.setToolsCode(data[2].toString());
		   }
		   if(data[3]!=null)
		   {
			   model.setToolsSizes(data[3].toString());
		   }
		   if(data[4]!=null)
		   {
			   model.setFactory(data[4].toString());
		   }
		   if(data[5]!=null)
		   {
			   info.setStrManuDate(data[5].toString());
		   }
		   if(data[6]!=null)
		   {
			   model.setChargeMan(data[6].toString());
		   }
		   if(data[7]!=null)
		   {
			   info.setChargeName(data[7].toString());
		   }
		   if(data[8]!=null)
		   {
			info.setStrCheckDate(data[8].toString());   
		   }
		   if(data[9]!=null)
		   {
			   model.setState(data[9].toString());
		   }
		   if(data[10]!=null)
		   {
			   model.setMemo(data[10].toString());
		   }
		   info.setTool(model);
		   objList.add(info);
		   
	   }
	   obj.setList(objList);
	  return obj;
	}
	else
	{
		return null;
	}
	}
	
	private boolean checkCodeSame(String toolsCode,Long ... id)
	{
		String sql=
			"select count(*)\n" +
			"from SP_J_SAFETOOLS t\n" + 
			"where t.tools_code='"+toolsCode+"' \n";
		if(id!=null&&id.length>0)
		{
			sql=sql+" and t.tools_id <>"+id[0];
		}
       int count=Integer.parseInt(bll.getSingal(sql).toString());
       if(count>0) return true;
       else return false;
		
	}

}