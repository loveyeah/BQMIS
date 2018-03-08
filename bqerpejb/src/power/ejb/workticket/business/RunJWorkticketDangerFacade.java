package power.ejb.workticket.business;

import java.util.ArrayList;
import java.util.Date;
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
import power.ejb.workticket.form.WorkticketDanger;

/**
 * Facade for entity RunJWorkticketDanger.
 * 
 * @see power.ejb.workticket.business.RunJWorkticketDanger
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorkticketDangerFacade implements
		RunJWorkticketDangerFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public RunJWorkticketDanger save(RunJWorkticketDanger entity) throws CodeRepeatException { 
		try {
//			if (!this.checkSame(entity.getWorkticketNo(), entity
//					.getDangerName(), entity.getPDangerId())) {
				if(entity.getDangerContentId() == null)
				{
					entity.setDangerContentId(bll.getMaxId("run_j_workticket_danger", "danger_content_id"));
				}
				if (entity.getOrderBy() == null) {
					entity.setOrderBy(entity.getDangerContentId());
				}
				entity.setModifyDate(new Date());
				entity.setIsUse("Y");
				entityManager.persist(entity); 
				return entity;
//			} else {
//				throw new CodeRepeatException("名称不能重复!");
//			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 一次保存增加/删除/修改
	 * @param addList 
	 * @param updateList
	 * @param delIds
	 */
	public void save(List<RunJWorkticketDanger> addList,List<RunJWorkticketDanger> updateList,String delIds)
	{
		if(addList!=null && addList.size()>0)
		{
			Long id = bll.getMaxId("run_j_workticket_danger", "danger_content_id");
			int i=0;
			for(RunJWorkticketDanger danger :addList)
			{
				try {
					danger.setDangerContentId(id+(i++));
					this.save(danger); 
				} catch (CodeRepeatException e) {
					LogUtil.log("程序忽略错误:", Level.ALL, e); 
				}
			}
		}
		for(RunJWorkticketDanger danger :updateList)
		{
			try {
				this.update(danger);
			} catch (CodeRepeatException e) {
				LogUtil.log("程序忽略错误:", Level.ALL, e); 
			}
		}
		if(delIds !=null && !delIds.trim().equals(""))
		{
			String sql = "delete from run_j_workticket_danger t where t.danger_content_id in ("+delIds+")";
			bll.exeNativeSQL(sql);
		}
	}
	
	public void deleteControlMulti(String controlIds)
	{
		String sql=
			"update run_j_workticket_danger t\n" +
			"set t.is_use='N'\n" + 
			"where t.danger_content_id in ("+controlIds+")";
		bll.exeNativeSQL(sql);

	}
	/**
	 * 删除危险点,及其控制措施
	 */
	public void deleteDangerMulti(String dangerIds)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		String sql=
			"delete from run_j_workticket_danger t\n" + 
			"where t.danger_content_id in ("+dangerIds+");"; 
		sb.append(sql);
		String controlsql=
			"delete from run_j_workticket_danger t\n" + 
			"where t.p_danger_id in ("+dangerIds+");"; 
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
		
	}


	
	public RunJWorkticketDanger update(RunJWorkticketDanger entity) throws CodeRepeatException {
		LogUtil.log("updating RunJWorkticketDanger instance", Level.INFO, null);
		try {
//			if(!this.checkSame(entity.getWorkticketNo(),entity.getDangerName(), entity.getPDangerId(), entity.getDangerContentId()))
//			{
				if(entity.getOrderBy()==null)
				{
					entity.setOrderBy(entity.getDangerContentId());
				}
			RunJWorkticketDanger result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
//			}
//			else
//			{
//				throw new CodeRepeatException("名称不能重复!");
//			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorkticketDanger findById(Long id) {
		LogUtil.log("finding RunJWorkticketDanger instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorkticketDanger instance = entityManager.find(
					RunJWorkticketDanger.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findDangerControlList(String dangerId,final int... rowStartIdxAndCount)
	{
		String sql=
			"select * from run_j_workticket_danger a\n" +
			"where a.p_danger_id="+dangerId+"\n" + 
			"and a.is_use='Y'  order by a.order_by";
		String sqlCount=
			"select count(*) from run_j_workticket_danger a\n" +
			"where a.p_danger_id="+dangerId+"\n" + 
			"and a.is_use='Y'";
		List<RunJWorkticketDanger> list=bll.queryByNativeSQL(sql, RunJWorkticketDanger.class, rowStartIdxAndCount);
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		PageObject result = new PageObject(); 
		result.setList(list);
		result.setTotalCount(totalCount);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findDangerList(String workticketNo)
	{
		List dangerList=new ArrayList();
//		String sql=
//			"select a.danger_content_id,a.danger_name,GETDANGERMEASURE(a.danger_content_id)\n" +
//			"from run_j_workticket_danger a\n" + 
//			"where a.workticket_no='"+workticketNo+"'\n" + 
//			"and a.p_danger_id=0  and a.is_use='Y' order by a.order_by";
		String sql=
			"select a.danger_content_id,a.danger_name dangername,b.danger_name,a.order_by dangercontrol,a.is_runadd from\n" +
			"run_j_workticket_danger a,run_j_workticket_danger b\n" + 
			"where a.workticket_no='"+workticketNo+"'\n" + 
			"and a.danger_content_id=b.p_danger_id(+)\n" + 
			"and a.p_danger_id=0\n" + 
			"and a.is_use='Y'\n" + 
			"and b.is_use(+)='Y'\n" + 
			"order by a.order_by,b.order_by";


		String sqlCount=
			"select count(*)\n" +
			"from run_j_workticket_danger a\n" + 
			"where a.workticket_no='"+workticketNo+"'\n" + 
			"and a.p_danger_id=0\n" + 
			"and a.is_use='Y'";
		
		List list=bll.queryByNativeSQL(sql);
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		Iterator it=list.iterator();
		while(it.hasNext())
 		{
			Object[] data=(Object[])it.next();
			WorkticketDanger model=new WorkticketDanger();
			if(data[0]!=null)
			{
				model.setDangerId(data[0].toString());
			}
			if(data[1]!=null)
			{
				model.setDangerName(data[1].toString());
			}
			if(data[2]!=null)
			{
				model.setDangerMeasure(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setOrderBy(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setIsRunadd(data[4].toString());
			}
			dangerList.add(model);
			
 		}
		
		PageObject result = new PageObject(); 
		result.setList(dangerList);
		result.setTotalCount(totalCount);
		return result;
	}
	
	private boolean checkSame(String workticketNo,String dangerName,Long parentId,Long...dangerId)
	{
		boolean check=false;
		String sql=
			"select count(*) from run_j_workticket_danger t\n" +
			"where  t.is_use='Y'\n" + 
			"and t.workticket_no='"+workticketNo+"'\n" + 
			"and t.danger_name=?\n" + 
			"and t.p_danger_id="+parentId;
		if(dangerId!=null&&dangerId.length>0)
		{
			sql=sql+"  and danger_content_id<>"+dangerId[0];
		}
        if(Integer.parseInt(bll.getSingal(sql,new Object[]{dangerName}).toString())>0)
        {
        	check=true;
        }
        else
        {
        	check=false;
        }
		return check;
	}
	
	public void deleteDangerByNo(String workticketNo)
	{
		String sql=
			"update run_j_workticket_danger a\n" +
			"set a.is_use='N'\n" + 
			"where a.workticket_no='"+workticketNo+"'\n" + 
			"and a.is_use='Y'";
		bll.exeNativeSQL(sql);

	}
}