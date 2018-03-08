package power.ejb.workticket.business;

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
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.workticket.RunCWorkticketFireContent;
import power.ejb.workticket.form.WorkticketFireContent;

/**
 * Facade for entity RunJWorkticketFireContent.
 * 
 * @see power.ejb.workticket.business.RunJWorkticketFireContent
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorkticketFireContentFacade implements
		RunJWorkticketFireContentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;


	public RunJWorkticketFireContent save(RunJWorkticketFireContent entity) throws CodeRepeatException {
		LogUtil.log("saving RunJWorkticketFireContent instance", Level.INFO,
				null);
		try {
			if(!this.checkFirecontentId(entity.getEnterpriseCode(), entity.getFirecontentId(), entity.getWorkticketNo()))
			{
				entity.setId(bll.getMaxId("RUN_J_WORKTICKET_FIRE_CONTENT", "id"));
				entity.setIsUse("Y");
				entity.setModifyDate(new java.util.Date());
			    entityManager.persist(entity);
			    LogUtil.log("save successful", Level.INFO, null);
			    return entity;
			}
			else
			{
				throw new CodeRepeatException("作业方式不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long id) {
		RunJWorkticketFireContent entity=this.findById(id);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	
	}
	
	public void deleteMutil(String ids)
	{
		String sql=
			"update RUN_J_WORKTICKET_FIRE_CONTENT t\n" +
			"set t.is_use='N'\n" + 
			"where t.id in("+ids+")";
        bll.exeNativeSQL(sql);
	}

	
	public RunJWorkticketFireContent update(RunJWorkticketFireContent entity) {
		LogUtil.log("updating RunJWorkticketFireContent instance", Level.INFO,
				null);
		try {
			if(entity!=null)
			{
			entity.setModifyDate(new java.util.Date());
			}
			RunJWorkticketFireContent result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorkticketFireContent findById(Long id) {
		LogUtil.log(
				"finding RunJWorkticketFireContent instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorkticketFireContent instance = entityManager.find(
					RunJWorkticketFireContent.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String workticketNo,String enterpriseCode,
			final int... rowStartIdxAndCount) {
		String sql=
			"select m.id,m.workticket_no,m.firecontent_id,n.firecontent_name\n" +
			"from RUN_J_WORKTICKET_FIRE_CONTENT m,run_c_workticket_fire_content n\n" + 
			"where m.firecontent_id=n.firecontent_id(+)\n" + 
			"and m.is_use='Y'\n" + 
			"and m.enterprise_code='"+enterpriseCode+"'\n" + 
			"and m.workticket_no='"+workticketNo+"'\n" + 
			" order by m.order_by";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		String sqlCount=
			"select count(1)\n" +
			"from RUN_J_WORKTICKET_FIRE_CONTENT m,run_c_workticket_fire_content n\n" + 
			"where m.firecontent_id=n.firecontent_id(+)\n" + 
			"and m.is_use='Y'\n" + 
			"and m.enterprise_code='"+enterpriseCode+"'\n" + 
			"and m.workticket_no='"+workticketNo+"'\n" + 
			" order by m.order_by";
		
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		List arrlist = new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			WorkticketFireContent model=new WorkticketFireContent();
			RunJWorkticketFireContent firemodel=new RunJWorkticketFireContent();
			if(data[0]!=null)
			{
				firemodel.setId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				firemodel.setWorkticketNo(data[1].toString());
				
			}
			if(data[2]!=null)
			{
				firemodel.setFirecontentId(Long.parseLong(data[2].toString()));
			}
			
			if(data[3]!=null)
			{
				model.setFirecontentName(data[3].toString());
			}
			model.setFire(firemodel);
			arrlist.add(model);
		}
		PageObject result = new PageObject(); 
		result.setList(arrlist);
		result.setTotalCount(totalCount);
		return result;
	
	}
	
	
	public boolean checkFirecontentId(String enterpriseCode,Long firecontentId,String workticketNo,Long... id)
	{
		boolean isSame = false;
		String sql=
			"select count(*) from RUN_J_WORKTICKET_FIRE_CONTENT m\n" +
			"where  m.workticket_no='"+workticketNo+"'\n" + 
			"and m.firecontent_id="+firecontentId+"\n" + 
			"and m.enterprise_code='"+enterpriseCode+"'\n" + 
			"and m.is_use='Y'";

			
	    if(id !=null&& id.length>0){
	    	sql += "  and m.id <> " + id[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}