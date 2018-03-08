package power.ejb.workticket.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.workticket.form.workticketHisInfo;

/**
 * Facade for entity RunJWorktickethis.
 * 
 * @see power.ejb.workticket.business.RunJWorktickethis
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorktickethisFacade implements RunJWorktickethisFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private RunJWorkticketsFacadeRemote remote;


	public void save(RunJWorktickethis entity) {
		LogUtil.log("saving RunJWorktickethis instance", Level.INFO, null);
		try {
			entity.setId(bll.getMaxId("RUN_J_WORKTICKETHIS", "id"));
			entity.setApproveDate(new Date());
			entityManager.persist(entity);
			
			LogUtil.log("save successful", Level.INFO, null);
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunJWorktickethis entity) {
		LogUtil.log("deleting RunJWorktickethis instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJWorktickethis.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorktickethis update(RunJWorktickethis entity) {
		LogUtil.log("updating RunJWorktickethis instance", Level.INFO, null);
		try {
			RunJWorktickethis result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorktickethis findById(Long id) {
		LogUtil.log("finding RunJWorktickethis instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorktickethis instance = entityManager.find(
					RunJWorktickethis.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<RunJWorktickethis> findAll(final int... rowStartIdxAndCount) {
		LogUtil
				.log("finding all RunJWorktickethis instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from RunJWorktickethis model";
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
	public List<workticketHisInfo> findHisInfo(String workticketNo)
	{
		List<workticketHisInfo> hisList=new ArrayList();
		String sql=
			" select t.id,\n" +
			" t.workticket_no,\n" + 
			" t.old_charge_by,nvl(GETWORKERNAME(t.old_charge_by),t.old_charge_by),\n" + 
			" t.new_charge_by,nvl(GETWORKERNAME(t.new_charge_by),t.new_charge_by),\n" + 
			"to_char(t.old_approved_finish_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"to_char(t.new_approved_finish_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			" t.reason,\n" + 
			" t.approve_by, nvl(GETWORKERNAME(t.approve_by),t.approve_by),\n" + 
			" to_char(t.approve_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			" t.approve_text,\n" + 
			" t.change_status,\n" + 
			" t.approve_status,\n" + 
			" t.duty_charge_by,nvl(GETWORKERNAME(t.duty_charge_by),t.duty_charge_by),\n" + 
			" t.fire_by,nvl(GETWORKERNAME(t.fire_by),t.fire_by),\n" + 
			" t.total_line,\n" + 
			" t.nobackout_line,\n" + 
			" t.nobackout_num\n" + 
			"  from run_j_worktickethis t\n" + 
			" where t.workticket_no='"+workticketNo+"'";
		List list=bll.queryByNativeSQL(sql);
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			workticketHisInfo his=new workticketHisInfo();
			RunJWorktickethis model=new RunJWorktickethis();
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
				model.setOldChargeBy(data[2].toString());
			}
			if(data[3]!=null)
			{
				his.setOldChargeByName(data[3].toString());
			}
			if(data[4]!=null)
			{
			 model.setNewChargeBy(data[4].toString());	
			}
			if(data[5]!=null)
			{
				his.setNewChargeByName(data[5].toString());
			}
			if(data[6]!=null)
			{
				his.setStrOldApprovedFinishDate(data[6].toString());
			}
			if(data[7]!=null)
			{
				his.setStrNewApprovedFinishDate(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setReason(data[8].toString());
			}
			if(data[9]!=null)
			{
				model.setApproveBy(data[9].toString());
			}
			if(data[10]!=null)
			{
				his.setApproveByName(data[10].toString());
			}
			if(data[11]!=null)
			{
				his.setStrApproveDate(data[11].toString());
			}
			if(data[12]!=null)
			{
				model.setApproveText(data[12].toString());
			}
			if(data[13]!=null)
			{
				model.setChangeStatus(data[13].toString());
			}
			if(data[14]!=null)
			{
				model.setApproveStatus(data[14].toString());
			}
			if(data[15]!=null)
			{
				model.setDutyChargeBy(data[15].toString());
			}
			if(data[16]!=null)
			{
				his.setDutyChargeByName(data[16].toString());
			}
			if(data[17]!=null)
			{
				model.setFireBy(data[17].toString());
			}
			if(data[18]!=null)
			{
				his.setFireByName(data[18].toString());
			}
			if(data[19]!=null)
			{
			model.setTotalLine(Long.parseLong(data[19].toString()));	
			}
			if(data[20]!=null)
			{
				model.setNobackoutLine(Long.parseLong(data[20].toString()));
			}
			if(data[21]!=null)
			{
				//model.setNobackoutNum(Long.parseLong(data[21].toString()));
				model.setNobackoutNum(data[21].toString());
			}
			his.setModel(model);
			hisList.add(his);
		}
 
		return hisList;
	}
	
	public void updateApproveInfo(RunJWorktickethis endHisModel,RunJWorktickethis pemitHisModel,
			RunJWorktickethis changeChargeModel,RunJWorktickethis delayHisModel,
			RunJWorktickethis safetyExeHisModel,RunJWorktickethis dhModel)
	{
		if(endHisModel!=null)
		{
			//终结
			if(endHisModel.getId()!=null)
			{
				RunJWorktickethis model=this.findById(endHisModel.getId());
				model.setDutyChargeBy(endHisModel.getDutyChargeBy());
				model.setOldApprovedFinishDate(endHisModel.getOldApprovedFinishDate());
				model.setOldChargeBy(endHisModel.getOldChargeBy());
				model.setNewChargeBy(endHisModel.getNewChargeBy());
				model.setFireBy(endHisModel.getFireBy());
				model.setTotalLine(endHisModel.getTotalLine());
				model.setNobackoutLine(endHisModel.getNobackoutLine());
				model.setNobackoutNum(endHisModel.getNobackoutNum());
				this.update(model);
			}
			else
			{
				endHisModel.setApproveStatus("8");
				this.save(endHisModel);
				entityManager.flush();
			}
			
		}
		if(dhModel!=null)
		{
			//动火票终结
			if(dhModel.getId()!=null)
			{
				RunJWorktickethis model=this.findById(dhModel.getId());
				model.setOldApprovedFinishDate(dhModel.getOldApprovedFinishDate());
				model.setOldChargeBy(dhModel.getOldChargeBy());
				model.setNewChargeBy(dhModel.getNewChargeBy());
				model.setFireBy(dhModel.getFireBy());
				model.setDutyChargeBy(dhModel.getDutyChargeBy());
				this.update(model);
			}
			else
			{
				dhModel.setChangeStatus("28");
				this.save(dhModel);
				entityManager.flush();
			}
			
		}
		if(changeChargeModel!=null)
		{
			if(changeChargeModel.getId()!=null)
			{
				RunJWorktickethis model=this.findById(changeChargeModel.getId());
				model.setOldChargeBy(changeChargeModel.getOldChargeBy());
				model.setNewChargeBy(changeChargeModel.getNewChargeBy());
				model.setApproveBy(changeChargeModel.getApproveBy());
				model.setDutyChargeBy(changeChargeModel.getDutyChargeBy());
				model.setOldApprovedFinishDate(changeChargeModel.getOldApprovedFinishDate());
				this.update(model);
			}
			else
			{
				changeChargeModel.setChangeStatus("2");
				this.save(changeChargeModel);
				entityManager.flush();
			}
		}
		if(delayHisModel!=null)
		{
			if(delayHisModel.getId()!=null)
			{
				RunJWorktickethis model=this.findById(delayHisModel.getId());
				model.setOldApprovedFinishDate(delayHisModel.getOldApprovedFinishDate());
				model.setNewApprovedFinishDate(delayHisModel.getNewApprovedFinishDate());
				model.setOldChargeBy(delayHisModel.getOldChargeBy());
				model.setNewChargeBy(delayHisModel.getNewChargeBy());
				model.setDutyChargeBy(delayHisModel.getDutyChargeBy());
				this.update(model);
			}
			else
			{
				delayHisModel.setChangeStatus("1");
				this.save(delayHisModel);
				entityManager.flush();
			}
		}
		if(safetyExeHisModel!=null)
		{
			if(safetyExeHisModel.getId()!=null)
			{
				RunJWorktickethis model=this.findById(safetyExeHisModel.getId());
				model.setOldChargeBy(safetyExeHisModel.getOldChargeBy());
				model.setDutyChargeBy(safetyExeHisModel.getDutyChargeBy());
				this.update(model);
			}
			else
			{
				safetyExeHisModel.setChangeStatus("7");
				this.save(safetyExeHisModel);
				entityManager.flush();
			}
		}
		if(pemitHisModel!=null)
		{
			if(pemitHisModel.getId()!=null)
			{
				RunJWorktickethis model=this.findById(pemitHisModel.getId());
				model.setApproveBy(pemitHisModel.getApproveBy());
				model.setOldChargeBy(pemitHisModel.getOldChargeBy());
				model.setDutyChargeBy(pemitHisModel.getDutyChargeBy());
				model.setOldApprovedFinishDate(pemitHisModel.getOldApprovedFinishDate());
				this.update(model);
			}
			else
			{
				pemitHisModel.setChangeStatus("4");
				this.save(pemitHisModel);
				entityManager.flush();
			}
		}
		
		
	}

	
	
}