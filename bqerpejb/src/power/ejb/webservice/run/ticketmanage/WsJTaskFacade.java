package power.ejb.webservice.run.ticketmanage;

import java.util.ArrayList;
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
import power.ejb.workticket.RunCWorkticketDanger;

/**
 * Facade for entity WsJTask.
 * 
 * @see power.ejb.webservice.run.ticketmanage.WsJTask
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class WsJTaskFacade implements WsJTaskFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(WsJTask entity) {
		LogUtil.log("saving WsJTask instance", Level.INFO, null);
		try {
			if (!checkTaskNo(entity.getTaskNo())) {
				if (entity.getId() == null) {
					entity.setId(bll.getMaxId("ws_j_task t", "ID"));
				}
				entity.setReceiveDate(new java.util.Date());
				entity.setTaskStatus("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return 1;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return 0;
	}

	public int delete(String taskNo) {
		List<WsJTask> list = this.findByProperty(taskNo);
		if (list.size() > 0) {
			String sql = "update  ws_j_task a\n" + "set a.task_status='N' \n"
					+ "where a.task_no in ('" + taskNo + "')";
			bll.exeNativeSQL(sql);
			return 1;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<WsJTask> findByProperty(String taskNo,
			final int... rowStartIdxAndCount) {
		try {
			final String queryString = "select * from ws_j_task t where t.task_status = 'Y' and t.task_no='"
					+ taskNo + "'";
			List<WsJTask> list = bll.queryByNativeSQL(queryString,
					WsJTask.class, rowStartIdxAndCount);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean checkTaskNo(String taskNo) {
		boolean isSame = false;
		String sql = "select count(*) from ws_j_task t\n"
				+ "where t.task_no = '" + taskNo + "'\n"
				+ "and t.task_status = 'Y'";

		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public PageObject findListForSelect(String taskNo, int taskType,String workerCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			if (taskNo != null || "".equals(taskNo)) {
				taskNo = "%";
			}
			String sql = "select *\n" + "  from ws_j_task t\n"
					+ " where t.task_no like '%"
					+ taskNo
					+ "%'"
					+ "   and t.task_status = 'Y' and t.receiver like '%" +workerCode+"'\n";
			String sqlCount = "select count(*) from ws_j_task t \n"
					+ "where t.task_status='Y' and t.task_no like '%"
					+ taskNo + "%' and t.receiver like '%" +workerCode+"'\n";
			if (taskType == 0) {
				sql += "and t.task_type is not null";
				sqlCount += "and t.task_type is not null";
			} else {
				sql += "and t.task_type = " + "'" + taskType + "'";
				sqlCount += "and t.task_type = " + "'" + taskType + "'";
			}
			
			if(taskType==1)
			{
				sql+=
					" \n and  t.task_no not in (\n" +
					"select distinct tt.apply_no from run_j_worktickets tt\n" + 
					"where tt.workticket_staus_id<>14 and tt.apply_no is not null and tt.is_use='Y')";
				
				sqlCount +=" \n and  t.task_no not in (\n" +
				"select distinct tt.apply_no from run_j_worktickets tt\n" + 
				"where tt.workticket_staus_id<>14 and tt.apply_no is not null )";

			}
			if(taskType==2)
			{
				sql+=
				"and t.task_no not in(\n" +
				"select  distinct a.applyno from run_j_opticket a\n" + 
				"where a.opticket_status<>'Z' and a.applyno is not null and a.is_use='Y'\n" + 
				")";
				sqlCount +=
					"and t.task_no not in(\n" +
					"select  distinct a.applyno from run_j_opticket a\n" + 
					"where a.opticket_status<>'Z' and a.applyno is not null\n" + 
					")";
			}

			List<WsJTask> list = bll.queryByNativeSQL(sql, WsJTask.class,rowStartIdxAndCount);

			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

}