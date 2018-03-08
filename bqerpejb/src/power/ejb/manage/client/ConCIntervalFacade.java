package power.ejb.manage.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * Facade for entity ConCInterval.
 * 
 * @see power.ejb.manage.client.ConCInterval
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCIntervalFacade implements ConCIntervalFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public ConCInterval save(ConCInterval entity) throws ParseException, CodeRepeatException {
		LogUtil.log("saving ConCInterval instance", Level.INFO, null);
		try {
			if(this.checkDate(entity.getBeginDate(),entity.getEnterpriseCode()))
			{
			entity.setIntervalId(bll.getMaxId("CON_C_INTERVAL", "interval_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("开始日期必须大于上条记录结束日期！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String ids)
	{
		String sql=
			"delete CON_C_INTERVAL t\n" +
			"where t.interval_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	public ConCInterval update(ConCInterval entity) throws ParseException, CodeRepeatException {
		LogUtil.log("updating ConCInterval instance", Level.INFO, null);
		try {
			if(this.checkDateForUpdate(entity.getBeginDate(), entity.getEndDate(), entity.getEnterpriseCode(), entity.getIntervalId()))
			{
			ConCInterval result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("‘开始日期必须大于上条记录结束日期’且‘结束日期必须小于下条记录的开始日期’！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCInterval findById(Long id) {
		LogUtil.log("finding ConCInterval instance with id: " + id, Level.INFO,
				null);
		try {
			ConCInterval instance = entityManager.find(ConCInterval.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(1)\n" +
			"from CON_C_INTERVAL t\n" + 
			"where t.enterprise_code='"+enterpriseCode+"'";
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			String sql=
				"select t.interval_id,t.begin_date,t.end_date,t.evaluation_days,t.memo,t.record_date,t.enterprise_code,GETWORKERNAME(t.record_by) record_by \n" +
				"from CON_C_INTERVAL t\n" + 
				"where t.enterprise_code='"+enterpriseCode+"'   order by t.interval_id asc";

			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			List<ConCInterval> list=bll.queryByNativeSQL(sql, ConCInterval.class, rowStartIdxAndCount);
			obj.setList(list);
			return obj;
		}
		else
		{
			return null;
		}

		
	}
	
	private boolean checkDate(Date beginDate,String enterpriseCode) throws ParseException
	{
		String sql="";
		
			sql="select t.end_date from CON_C_INTERVAL t\n" +
			"where  t.enterprise_code='"+enterpriseCode+"'  and  rownum=1  \n" + 
			"order by t.interval_id desc";
		
		
	    Object obj=bll.getSingal(sql);
	    if(obj==null)
	    {
	    	return true;
	    }
	    else
	    {
	    	String strEndDate=obj.toString();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	Date endDate=df.parse(strEndDate);
	    	if(beginDate.compareTo(endDate)==1)
	    	{
	    		return true;
	    	}
	    	else 
	    	{
	    		return false;
	    	}
	    }

	}
	
	private boolean checkDateForUpdate(Date beginDate,Date endDate,String enterpriseCode,Long intervalId) throws ParseException
	{
		boolean check=true;
		String sql=
			"select t.end_date from CON_C_INTERVAL t\n" +
			"where  t.enterprise_code='"+enterpriseCode+"'  and  t.interval_id<"+intervalId+"   and   rownum=1\n" + 
			"order by t.interval_id desc";
		Object obj=bll.getSingal(sql);
	    if(obj==null)
	    {
	    	check= true;
	    }
	    else
	    {
	    	String strEndDate=obj.toString();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	Date lastEndDate=df.parse(strEndDate);
	    	if(beginDate.compareTo(lastEndDate)==1)
	    	{
	    		check= true;
	    	}
	    	else 
	    	{
	    		check= false;
	    	}
	    }
	    
	    if(check==false)
	    {
	    	return check;
	    }
	    else{
	    sql=
			"select t.begin_date from CON_C_INTERVAL t\n" +
			"where  t.enterprise_code='"+enterpriseCode+"'  and  t.interval_id>"+intervalId+"   and   rownum=1\n" + 
			"order by t.interval_id asc";
	    
	     obj=bll.getSingal(sql);
	     
	       if(obj==null)
	       {
	    	   return true;
	       }
	       else
	       {
	    	   String strBeginDate=obj.toString();
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    	Date nextBeginDate=df.parse(strBeginDate);
		    	if(nextBeginDate.compareTo(endDate)==1)
		    	{
		    		return true;
		    	}
		    	else 
		    	{
		    		return false;
		    	}
	       }
	    }
		
	}
	
	

}