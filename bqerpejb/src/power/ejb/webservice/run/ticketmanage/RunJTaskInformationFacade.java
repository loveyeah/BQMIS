package power.ejb.webservice.run.ticketmanage;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.workticket.RunCWorkticketDanger;

/**
 * Facade for entity RunJTaskInformation.
 * 
 * @see power.ejb.webservice.run.ticketmanage.RunJTaskInformation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJTaskInformationFacade implements
		RunJTaskInformationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunJTaskInformation entity) {
		LogUtil.log("saving RunJTaskInformation instance", Level.INFO, null);
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("run_j_task_information t", "ID"));
			}
			entity.setCreateDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunJTaskInformation> findListForResend() {
		try {
			String sql = "select a.rowid,a.* from run_j_task_information a where a.execte_result is null or  a.execte_result not in('1')";
			List<RunJTaskInformation> list = bll.queryByNativeSQL(sql,
					RunJTaskInformation.class);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

	public RunJTaskInformation update(RunJTaskInformation entity) {
		try {
			RunJTaskInformation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}

	}

}