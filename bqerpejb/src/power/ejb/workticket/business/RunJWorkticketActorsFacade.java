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
import power.ejb.workticket.form.WorkticketActors;

/**
 * Facade for entity RunJWorkticketActors.
 * 
 * @see power.ejb.workticket.business.RunJWorkticketActors
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorkticketActorsFacade implements
		RunJWorkticketActorsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public RunJWorkticketActors save(RunJWorkticketActors entity) throws CodeRepeatException {
		LogUtil.log("saving RunJWorkticketActors instance", Level.INFO, null);
		try {
			if(!this.checkActorCode(entity.getEnterpriseCode(), entity.getActorCode(), entity.getWorkticketNo()))
			{
			entity.setId(bll.getMaxId("RUN_J_WORKTICKET_ACTORS", "id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("人员不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long id) throws CodeRepeatException {
		RunJWorkticketActors entity=this.findById(id);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	}
	
	public void deleteMulti(String ids)
	{
		String sql=
			"update RUN_J_WORKTICKET_ACTORS t\n" +
			"set t.is_use='N'\n" + 
			"where t.id in("+ids+")";
        bll.exeNativeSQL(sql);
	}

	
	public RunJWorkticketActors update(RunJWorkticketActors entity) throws CodeRepeatException {
		LogUtil.log("updating RunJWorkticketActors instance", Level.INFO, null);
		try {
			if(!this.checkActorCode(entity.getEnterpriseCode(), entity.getActorCode(), entity.getWorkticketNo(), entity.getId()))
			{
			RunJWorkticketActors result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("人员不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorkticketActors findById(Long id) {
		LogUtil.log("finding RunJWorkticketActors instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorkticketActors instance = entityManager.find(
					RunJWorkticketActors.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	



	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,String workticketNo,final int... rowStartIdxAndCount) {
		String sql=
			"select b.id,b.workticket_no,b.actor_type,b.actor_code,b.actor_name,b.actor_dept,b.enterprise_code,b.is_use, nvl(getdeptname(b.actor_dept),b.actor_dept)\n" +
			"from RUN_J_WORKTICKET_ACTORS b\n" + 
			"where b.workticket_no='"+workticketNo+"'\n" + 
			"and b.enterprise_code='"+enterpriseCode+"'\n" + 
			"and b.is_use='Y'";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		String sqlCount=
			"select count(1)\n" +
			"from RUN_J_WORKTICKET_ACTORS b\n" + 
			"where b.workticket_no='"+workticketNo+"'\n" + 
			"and b.enterprise_code='"+enterpriseCode+"'\n" + 
			"and b.is_use='Y'";
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		List arrlist = new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			WorkticketActors actor=new WorkticketActors();
			RunJWorkticketActors model=new RunJWorkticketActors();
			Object[] data=(Object[])it.next();
			if(data[0]!=null)
			{
				model.setId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setWorkticketNo(data[1].toString());
			}
			
			if(data[2]!=null)
			{
				model.setActorType(Long.parseLong(data[2].toString()));
			}
			
			if(data[3]!=null)
			{
				model.setActorCode(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setActorName(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setActorDept(data[5].toString());
			}
			
			if(data[6]!=null)
			{
				model.setEnterpriseCode(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setIsUse(data[7].toString());
			}
			if(data[8]!=null)
			{
				actor.setDeptName(data[8].toString());
			}
			
			if(model.getActorType()==1)
			{
				actor.setActortypename("正式员工");
			}
			if(model.getActorType()==2)
			{
				actor.setActortypename("临时员工");
			}
			actor.setActor(model);
			arrlist.add(actor);
		}
		PageObject result = new PageObject(); 
		result.setList(arrlist);
		result.setTotalCount(totalCount);
		return result;
	
	}
	
	
	public boolean checkActorCode(String enterpriseCode,String actorCode,String workticketNo,Long... actorId)
	{
		boolean isSame = false;
		String sql=
			"select count(*) from RUN_J_WORKTICKET_ACTORS b\n" +
			"where b.workticket_no='"+workticketNo+"'\n" + 
			"and b.actor_code='"+actorCode+"'\n" + 
			"and b.enterprise_code='"+enterpriseCode+"'\n" + 
			"and b.is_use='Y'";
	    if(actorId !=null&& actorId.length>0){
	    	sql += "  and b.id <> " + actorId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
 

}