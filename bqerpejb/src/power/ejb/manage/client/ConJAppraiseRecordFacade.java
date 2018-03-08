package power.ejb.manage.client;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.manage.client.form.ConJAppraiseRecordForm;

/**
 * Facade for entity ConJAppraiseRecord.
 * 
 * @see power.ejb.manage.client.ConJAppraiseRecord
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJAppraiseRecordFacade implements ConJAppraiseRecordFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public ConJAppraiseRecord save(ConJAppraiseRecord entity) {
		LogUtil.log("saving ConJAppraiseRecord instance", Level.INFO, null);
		try {
			
			entity.setRecordId(bll.getMaxId("CON_J_APPRAISE_RECORD", "record_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

	
	public ConJAppraiseRecord update(ConJAppraiseRecord entity) {
		LogUtil.log("updating ConJAppraiseRecord instance", Level.INFO, null);
		try {
			ConJAppraiseRecord result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJAppraiseRecord findById(Long id) {
		LogUtil.log("finding ConJAppraiseRecord instance with id: " + id,
				Level.INFO, null);
		try {
			ConJAppraiseRecord instance = entityManager.find(
					ConJAppraiseRecord.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void saveRecord(List<ConJAppraiseRecord> list)
	{
		for(ConJAppraiseRecord model:list)
		{
			if(model.getRecordId()!=null)
			{
				this.update(model);
			}
			else
			{
				this.save(model);
				entityManager.flush();
			}
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String clientId,String intervalId,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount=
		"select count(*)\n" +
		"from CON_J_APPRAISE_RECORD t,CON_J_APPRAISE a,CON_C_APPRAISE_ITEM b,CON_J_CLIENTS_INFO c\n" + 
		"where t.interval_id=a.interval_id(+) and t.cliend_id=a.cliend_id(+)\n" + 
		"and t.event_id=b.event_id(+)\n" + 
		"and t.cliend_id=c.cliend_id(+)\n" + 
		"and t.interval_id="+intervalId+"  and t.cliend_id="+clientId+" \n" + 
		"and t.enterprise_code='"+enterpriseCode+"'\n" + 
		"and a.enterprise_code='"+enterpriseCode+"'";
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			//从评价记录表里查找
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			List objList=new ArrayList();
		   String sql=
			"select t.record_id,t.event_id,t.interval_id,t.cliend_id,\n" +
			"t.appraise_point,t.memo,t.enterprise_code,\n" + 
			"a.gather_flag,b.appraise_item,c.client_name\n" + 
			",b.appraise_mark,b.appraise_criterion \n"+
			"from CON_J_APPRAISE_RECORD t,CON_J_APPRAISE a,CON_C_APPRAISE_ITEM b,CON_J_CLIENTS_INFO c\n" + 
			"where t.interval_id=a.interval_id(+) and t.cliend_id=a.cliend_id(+)\n" + 
			"and t.event_id=b.event_id(+)\n" + 
			"and t.cliend_id=c.cliend_id(+)\n" + 
			"and t.interval_id="+intervalId+"  and t.cliend_id="+clientId+" \n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"' \n"+
			"order by b.display_no(+) asc";
		
	
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			ConJAppraiseRecordForm model=new ConJAppraiseRecordForm();
			Object[] data=(Object [])it.next();
			if(data[0]!=null)
			{
				model.setRecordId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setEventId(Long.parseLong(data[1].toString()));
			}
			if(data[2]!=null)
			{
				model.setIntervalId(Long.parseLong(data[2].toString()));
			}
			if(data[3]!=null)
			{
				model.setCliendId(Long.parseLong(data[3].toString()));
			}
			if(data[4]!=null)
			{
				model.setAppraisePoint(Double.parseDouble(data[4].toString()));
			}
			if(data[5]!=null)
			{
				model.setMemo(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setEnterpriseCode(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setGatherFlag(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setEventName(data[8].toString());
			}
			if(data[9]!=null)
			{
				model.setClientName(data[9].toString());
			}
			if(data[10]!=null)
			{
				model.setAppraiseMark(Long.parseLong(data[10].toString()));
			}
			if(data[11]!=null)
			{
				model.setAppraiseCriterion(data[11].toString());
			}
			objList.add(model);
		}
		
		obj.setList(objList);
		return obj;
		}
		else
		{
			//从评价项目表里查找
			String eventCountSql=
			"select count(*)\n" +
	    	 "from CON_C_APPRAISE_ITEM tt\n" + 
	    	 "where \n" + 
	    	 " tt.enterprise_code='"+enterpriseCode+"'\n" ;
	    	Long eventTotalCount=Long.parseLong(bll.getSingal(eventCountSql).toString());
	    	if(eventTotalCount>0)
	    	{
	    		PageObject obj=new PageObject();
				obj.setTotalCount(eventTotalCount);
				List objList=new ArrayList();
	           String sql=
		    	 "select tt.event_id,tt.appraise_item,tt.appraise_mark,tt.appraise_criterion\n" +
		    	 "from CON_C_APPRAISE_ITEM tt\n" + 
		    	 "where \n" + 
		    	 " tt.enterprise_code='"+enterpriseCode+"'\n" +
		    	 " and tt.is_use = 'Y' "+ // 当评分项目启用后才能查询出评分项目
		    	 "order by tt.display_no asc";
			      List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			      Iterator it=list.iterator();
			   	   while(it.hasNext())
			   		{
			   			ConJAppraiseRecordForm model=new ConJAppraiseRecordForm();
			   			model.setCliendId(Long.parseLong(clientId));
			   			model.setIntervalId(Long.parseLong(intervalId));
			   			Object[] data=(Object [])it.next();
			   			if(data[0]!=null)
			   			{
			   				model.setEventId(Long.parseLong(data[0].toString()));
			   			}
			   			if(data[1]!=null)
			   			{
			   				model.setEventName(data[1].toString());
			   			}
			   			if(data[2]!=null)
			   			{
			   				model.setAppraiseMark(Long.parseLong(data[2].toString()));
			   			}
			   			if(data[3]!=null)
			   			{
			   				model.setAppraiseCriterion(data[3].toString());
			   			}
			   			objList.add(model);
			   			
			   		}
			   	obj.setList(objList);
				return obj;
	    	}
	    	else
	    	{
	    		return null;
	    	}

		}
	}
	
	public Double getTotalRecordScore(Long clientId,Long intervalId,String enterpriseCode)
	{
		String sql=
			"select sum(t.appraise_point)\n" +
			"from  CON_J_APPRAISE_RECORD t\n" + 
			"where t.interval_id="+intervalId+"\n" + 
			"and t.cliend_id="+clientId+"\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'";
		Object objCount=bll.getSingal(sql);
		if(objCount!=null&&!objCount.equals(""))
		{
			return Double.parseDouble(objCount.toString());
		}
		else
		{
			return 0d;
		}

	}

}