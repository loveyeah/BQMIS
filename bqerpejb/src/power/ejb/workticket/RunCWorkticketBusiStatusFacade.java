package power.ejb.workticket;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunCWorkticketBusiStatus.
 * 
 * @see power.ejb.workticket.RunCWorkticketBusiStatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketBusiStatusFacade implements
		RunCWorkticketBusiStatusFacadeRemote {
	// property constants
	public static final String WORKTICKET_STATUS_NAME = "workticketStatusName";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	@SuppressWarnings("unchecked")
	public List<RunCWorkticketBusiStatus> findAll() throws ParseException {
		try {
			String sql = "select t.* from run_c_workticket_busi_status t";
			List<RunCWorkticketBusiStatus> list = bll.queryByNativeSQL(sql,
					RunCWorkticketBusiStatus.class);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
}