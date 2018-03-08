package power.ejb.manage.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.client.form.ConJAppraiseForm;

/**
 * Facade for entity ConJAppraise.
 * 
 * @see power.ejb.manage.client.ConJAppraise
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJAppraiseFacade implements ConJAppraiseFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public ConJAppraise save(ConJAppraise entity) {
		LogUtil.log("saving ConJAppraise instance", Level.INFO, null);
		try {
			entity.setAppraisalId(bll.getMaxId("CON_J_APPRAISE", "appraisal_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJAppraise update(ConJAppraise entity) {
		LogUtil.log("updating ConJAppraise instance", Level.INFO, null);
		try {
			ConJAppraise result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJAppraise findById(Long id) {
		LogUtil.log("finding ConJAppraise instance with id: " + id, Level.INFO,
				null);
		try {
			ConJAppraise instance = entityManager.find(ConJAppraise.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public ConJAppraise findModelByClientAndInterval(Long clientId,Long intervalId,String enterpriseCode)
	{
		String sql=
			"select *\n" +
			"from CON_J_APPRAISE tt\n" + 
			"where tt.cliend_id="+clientId+"\n" + 
			"and tt.interval_id="+intervalId+"\n" + 
			"and tt.enterprise_code='"+enterpriseCode+"'";
		List<ConJAppraise> list=bll.queryByNativeSQL(sql, ConJAppraise.class);
		if(list!=null&&list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String clientId,String intervalId,String enterpriseCode, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.appraisal_id,\n" +
			"       a.cliend_id,\n" + 
			"       (select b.client_name\n" + 
			"          from CON_J_CLIENTS_INFO b\n" + 
			"         where b.cliend_id = a.cliend_id) client_name,\n" + 
			"       a.interval_id,\n" + 
			"       (select c.begin_date\n" + 
			"          from CON_C_INTERVAL c\n" + 
			"         where c.interval_id = a.interval_id) begin_date,\n" + 
			"       (select c.end_date\n" + 
			"          from CON_C_INTERVAL c\n" + 
			"         where c.interval_id = a.interval_id) end_date,\n" + 
			"       a.total_score,\n" + 
			"       a.gather_flag,\n" + 
			"       a.appraise_by,\n" + 
			"       getworkername(a.appraise_by),\n" + 
			"       a.appraise_date,\n" + 
			"       a.appraisal_result,\n" + 
			"       a.gather_by,\n" + 
			"       getworkername(a.gather_by),\n" + 
			"       a.gather_date\n" + 
			"  from CON_J_APPRAISE a\n" + 
			" where a.enterprise_code = '"+enterpriseCode+"'";

		String sqlCount = "select count(1) from CON_J_APPRAISE a where a.enterprise_code = '"+enterpriseCode+"'";
		
		String strWhere = "";
		if (clientId != null && !clientId.equals("")) {
			strWhere += " and a.cliend_id = '" + clientId + "'\n";
		}
		if(intervalId != null && !"".equals(intervalId))
		{
			strWhere += " and a.interval_id = '" + intervalId + "'";
		}
		sqlCount = sqlCount + strWhere;
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		sql = sql + strWhere;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				ConJAppraiseForm model = new ConJAppraiseForm();
				Object[] data = (Object[]) it.next();
				model.setAppraisalId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					model.setCliendId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					model.setClientName(data[2].toString());
				if(data[3] != null)
					model.setIntervalId(Long.parseLong(data[3].toString()));
				if(data[4] != null)
				{
					model.setBeginDate(data[4].toString());
					model.setIntervalDate(data[4].toString() +"~" + data[5].toString());
				}
				if(data[5] != null)
					model.setEndDate(data[5].toString());
				if(data[6] != null)
					model.setTotalScore(Double.parseDouble(data[6].toString()));
				if(data[7] != null)
					model.setGatherFlag(data[7].toString());
				if(data[8] != null)
					model.setAppraiseBy(data[8].toString());
				if(data[9] != null)
					model.setAppraiseName(data[9].toString());
				if(data[10] != null)
					model.setAppraiseDate(data[10].toString());
				if(data[11] != null)
					model.setAppraisalResult(data[11].toString());
				if(data[12] != null)
					model.setGatherBy(data[12].toString());
				if(data[13] != null)
					model.setGatherName(data[13].toString());
				if(data[14] != null)
					model.setGatherDate(data[14].toString());
				arrlist.add(model);
			}
		}
		pg.setList(arrlist);
		return pg;
	}
	
	public PageObject findGatherAll(String clientId,String enterpriseCode, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.appraisal_id,\n" +
			"       a.cliend_id,\n" + 
			"       (select b.client_name\n" + 
			"          from CON_J_CLIENTS_INFO b\n" + 
			"         where b.cliend_id = a.cliend_id) client_name,\n" + 
			"       a.interval_id,\n" + 
			"       (select c.begin_date\n" + 
			"          from CON_C_INTERVAL c\n" + 
			"         where c.interval_id = a.interval_id) begin_date,\n" + 
			"       (select c.end_date\n" + 
			"          from CON_C_INTERVAL c\n" + 
			"         where c.interval_id = a.interval_id) end_date,\n" + 
			"       a.total_score,\n" + 
			"       a.gather_flag,\n" + 
			"       a.appraise_by,\n" + 
			"       getworkername(a.appraise_by),\n" + 
			"       a.appraise_date,\n" + 
			"       a.appraisal_result,\n" + 
			"       a.gather_by,\n" + 
			"       getworkername(a.gather_by),\n" + 
			"       a.gather_date\n" + 
			"  from CON_J_APPRAISE a\n" + 
			" where a.enterprise_code = '"+enterpriseCode+"'" +
			" and a.gather_flag='Y'";

		String sqlCount = "select count(1) from CON_J_APPRAISE a where a.enterprise_code = '"+enterpriseCode+"'"+
						" and a.gather_flag='Y'";
		
		String strWhere = "";
		if (clientId != null && !clientId.equals("")) {
			strWhere += " and a.cliend_id = '" + clientId + "'\n";
		}
		sqlCount = sqlCount + strWhere;
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		sql = sql + strWhere;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				ConJAppraiseForm model = new ConJAppraiseForm();
				Object[] data = (Object[]) it.next();
				model.setAppraisalId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					model.setCliendId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					model.setClientName(data[2].toString());
				if(data[3] != null)
					model.setIntervalId(Long.parseLong(data[3].toString()));
				if(data[4] != null)
				{
					model.setBeginDate(data[4].toString());
					model.setIntervalDate(data[4].toString() +"~" + data[5].toString());
				}
				if(data[5] != null)
					model.setEndDate(data[5].toString());
				if(data[6] != null)
					model.setTotalScore(Double.parseDouble(data[6].toString()));
				if(data[7] != null)
					model.setGatherFlag(data[7].toString());
				if(data[8] != null)
					model.setAppraiseBy(data[8].toString());
				if(data[9] != null)
					model.setAppraiseName(data[9].toString());
				if(data[10] != null)
					model.setAppraiseDate(data[10].toString());
				if(data[11] != null)
					model.setAppraisalResult(data[11].toString());
				if(data[12] != null)
					model.setGatherBy(data[12].toString());
				if(data[13] != null)
					model.setGatherName(data[13].toString());
				if(data[14] != null)
					model.setGatherDate(data[14].toString());
				arrlist.add(model);
			}
		}
		pg.setList(arrlist);
		return pg;
	}

}