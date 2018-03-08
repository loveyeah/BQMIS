package power.ejb.productiontec.insulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.insulation.form.QxdjForm;

/**
 * Facade for entity PtJyjdJQxdj.
 * 
 * @see power.ejb.productiontec.insulation.PtJyjdJQxdj
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJyjdJQxdjFacade implements PtJyjdJQxdjFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtJyjdJQxdj save(PtJyjdJQxdj entity) {
		LogUtil.log("saving PtJyjdJQxdj instance", Level.INFO, null);
		try {
			entity.setJyqxId(bll.getMaxId("PT_JYJD_J_QXDJ", "jyqx_id"));
			entity.setFillDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "delete PT_JYJD_J_QXDJ b where b.jyqx_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public PtJyjdJQxdj update(PtJyjdJQxdj entity) {
		LogUtil.log("updating PtJyjdJQxdj instance", Level.INFO, null);
		try {
			PtJyjdJQxdj result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJyjdJQxdj findById(Long id) {
		LogUtil.log("finding PtJyjdJQxdj instance with id: " + id, Level.INFO,
				null);
		try {
			PtJyjdJQxdj instance = entityManager.find(PtJyjdJQxdj.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findQxdjList(String enterpriseCode, String equName,
			String sDate, String eDate, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select q.jyqx_id,\n" 
				+ "       q.accident_title,\n"
				+ "       q.equ_code,\n" 
				+ "       q.equ_name,\n"
				+ "       to_char(q.find_time, 'yyyy-MM-dd hh24:mi:ss'),\n" 
				+ "       to_char(q.clear_time, 'yyyy-MM-dd hh24:mi:ss'),\n" 
				+ "       q.reason_analyse,\n" 
				+ "       q.bug_status,\n"
				+ "       q.memo,\n" 
				+ "       q.annex,\n"
				+ "       q.fill_by,\n" 
				+ "       getworkername(q.fill_by),\n"
				+ "       q.fill_date\n" 
				+ "  from pt_jyjd_j_qxdj q\n"
				+ " where q.enterprise_code = '" + enterpriseCode + "'";

		String sqlCount = "select count(1) from pt_jyjd_j_qxdj q where q.enterprise_code = '"
				+ enterpriseCode + "'";

		String strWhere = "";
		if (equName != null && equName.length() > 0) {
			strWhere += " and q.equ_name like '%" + equName + "%'";
		}
		if (sDate != null && sDate.length() > 0) {
			strWhere += " and q.find_time >= to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			strWhere += "and q.find_time <= to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		sql += strWhere;
		sql = sql + " order by q.jyqx_id";
		sqlCount += strWhere;
		sqlCount = sqlCount + " order by q.jyqx_id";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				QxdjForm form = new QxdjForm();
				Object[] data = (Object[]) it.next();
				form.setJyqxId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setAccidentTitle(data[1].toString());
				if (data[2] != null)
					form.setEquCode(data[2].toString());
				if (data[3] != null)
					form.setEquName(data[3].toString());
				if (data[4] != null)
					form.setFindTime(data[4].toString());
				if (data[5] != null)
					form.setClearTime(data[5].toString());
				if (data[6] != null)
					form.setReasonAnalyse(data[6].toString());
				if (data[7] != null)
					form.setBugStatus(data[7].toString());
				if (data[8] != null)
					form.setMemo(data[8].toString());
				if (data[9] != null)
					form.setAnnex(data[9].toString());
				if (data[10] != null)
					form.setFillBy(data[10].toString());
				if (data[11] != null)
					form.setFillName(data[11].toString());
				if (data[12] != null)
					form.setFillDate(data[12].toString());
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}