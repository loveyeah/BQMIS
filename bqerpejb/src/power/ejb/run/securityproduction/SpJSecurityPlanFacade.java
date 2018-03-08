package power.ejb.run.securityproduction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import power.ejb.run.securityproduction.form.SpJSecurityPlanInfo;
import power.ejb.run.securityproduction.form.SpJSpecialoperatorsInfo;

/**
 * Facade for entity SpJSecurityPlan.
 * 
 * @see power.ejb.run.securityproduction.SpJSecurityPlan
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSecurityPlanFacade implements SpJSecurityPlanFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public SpJSecurityPlan save(SpJSecurityPlan entity) {
		LogUtil.log("saving SpJSecurityPlan instance", Level.INFO, null);
		try {
			String sql = "select count(*) from sp_j_security_plan t\n"
				+ "where t.plan_name = '" + entity.getPlanName() + "'";
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			else
			{
				entity.setSecurityPlanId(bll.getMaxId("SP_J_SECURITY_PLAN", "security_plan_id"));
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			}
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	
	public SpJSecurityPlan update(SpJSecurityPlan entity) {
		LogUtil.log("updating SpJSecurityPlan instance", Level.INFO, null);
		try {
			String sql = "select count(*) from sp_j_security_plan t\n"
				+ "where t.plan_name = '" + entity.getPlanName() + "'"
				+ " and t.security_plan_id !=" + entity.getSecurityPlanId();
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			SpJSecurityPlan result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}


	public void deleteMulti(String ids) {
		String sql = "delete from  "+
			"SP_J_SECURITY_PLAN a\n"
			+ " where a.security_plan_id in (" + ids
			+ ")\n" ;
			bll.exeNativeSQL(sql);
	}

	public SpJSecurityPlan findById(Long id) {
		LogUtil.log("finding SpJSecurityPlan instance with id: " + id,
				Level.INFO, null);
		try {
			SpJSecurityPlan instance = entityManager.find(
					SpJSecurityPlan.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	






	public PageObject findAll(String queryString,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.security_plan_id,\n"
			+ "       a.plan_name,\n"
			+ "       a.plan_basis,\n"
			+ "       a.fee,\n"
			+ "       a.year,\n"
			+ "       a.finish_date,\n"
			+ "       a.charge_by,\n"
			+ "       a.charge_dep,\n"
			+ "       a.memo,\n"
			+ "       a.fill_by,\n"
			+ "       a.fill_dep,\n"
			+ "       a.fill_date, \n"
			+ "       a.finish_state,\n"
			+ "       a.finish_appraisal,\n"
			+ "       a.appraisal_by,\n"
			+ "       a.appraisal_date,\n"
			+ "       a.enterprise_code,\n"
			+ "       to_char(a.finish_date, 'yyyy-MM-dd'), \n"
			+ "       getworkername(a.charge_by),\n"
			+ "       GETDEPTNAME(a.charge_dep),\n"
			+ "       getworkername(a.fill_by),\n"
			+ "       GETDEPTNAME(a.fill_dep),\n"
			+ "       to_char(a.fill_date, 'yyyy-MM-dd'), \n"
			+ "       getworkername(a.appraisal_by),\n"
			+ "       to_char(a.appraisal_date, 'yyyy-MM-dd') \n"
			+ "  from SP_J_SECURITY_PLAN a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.security_plan_id)\n"
			+ "  from SP_J_SECURITY_PLAN a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		if(queryString !=null && (!queryString.equals("")))
		{
			sql = sql + " and a.plan_name like '%" + queryString + "%' \n"
			          + " or a.year like '%" + queryString + "%' \n";
			sqlCount = sqlCount + " and a.plan_name like '%" + queryString + "%' \n"
								+ " or a.year like '%" + queryString + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				SpJSecurityPlan spj = new SpJSecurityPlan();
				SpJSecurityPlanInfo info = new SpJSecurityPlanInfo();
				Object []data = (Object[])it.next();
				spj.setSecurityPlanId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					spj.setPlanName(data[1].toString());
				if(data[2] != null)
					spj.setPlanBasis(data[2].toString());
				if(data[3] != null)
					spj.setFee(Double.parseDouble(data[3].toString()));
				if(data[4] != null)
					spj.setYear(Long.parseLong(data[4].toString()));
				if(data[5] != null)
					try {
						spj.setFinishDate(sdf.parse(data[5].toString()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				if(data[6] != null)
					spj.setChargeBy(data[6].toString());
				if(data[7] != null)
					spj.setChargeDep(data[7].toString());
				if(data[8] != null)
					spj.setMemo(data[8].toString());
				if(data[9] != null)
					spj.setFillBy(data[9].toString());
				if(data[10] != null)
					spj.setFillDep(data[10].toString());
				if(data[11] != null)
					try {
						spj.setFillDate(sdf.parse(data[11].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[12] != null)
					spj.setFinishState(data[12].toString());
				if(data[13] != null)
					spj.setFinishAppraisal(data[13].toString());
				if(data[14] != null)
					spj.setAppraisalBy(data[14].toString());
				if(data[15] != null)
					try {
						spj.setAppraisalDate(sdf.parse(data[15].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[16] != null)
					spj.setEnterpriseCode(data[16].toString());
				if(data[17] != null)
					info.setFinishDate(data[17].toString());
				if(data[18] != null)
					info.setChargeName(data[18].toString());
				if(data[19] != null)
					info.setChargeDepName(data[19].toString());
				if(data[20] != null)
					info.setFillName(data[20].toString());
				if(data[21] != null)
					info.setFillDepName(data[21].toString());
				if(data[22] != null)
					info.setFillDate(data[22].toString());
				if(data[23] != null)
					info.setAppraisalName(data[23].toString());
				if(data[24] != null)
					info.setAppraisalDate(data[24].toString());
				info.setSpjs(spj);
				arrlist.add(info);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
	}

}