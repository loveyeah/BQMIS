package power.ejb.productiontec.insulation;

import java.util.ArrayList;
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
import power.ejb.productiontec.insulation.form.SgdjForm;

/**
 * Facade for entity PtJyjdJSgdj.
 * 
 * @see power.ejb.productiontec.insulation.PtJyjdJSgdj
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJyjdJSgdjFacade implements PtJyjdJSgdjFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtJyjdJSgdj save(PtJyjdJSgdj entity) {
		LogUtil.log("saving PtJyjdJSgdj instance", Level.INFO, null);
		try {
			entity.setJysgId(bll.getMaxId("PT_JYJD_J_SGDJ", "jysg_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids)
	{
		String sql = "delete PT_JYJD_J_SGDJ b where b.jysg_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public PtJyjdJSgdj update(PtJyjdJSgdj entity) {
		LogUtil.log("updating PtJyjdJSgdj instance", Level.INFO, null);
		try {
			PtJyjdJSgdj result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJyjdJSgdj findById(Long id) {
		LogUtil.log("finding PtJyjdJSgdj instance with id: " + id, Level.INFO,
				null);
		try {
			PtJyjdJSgdj instance = entityManager.find(PtJyjdJSgdj.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllList(String enterpriseCode,String equName,String sDate,String eDate,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select a.jysg_id,\n" 
				+ "       a.accident_title,\n"
				+ "       a.equ_code,\n" 
				+ "       a.equ_name,\n"
				+ "       to_char(a.happen_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       to_char(a.handle_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       a.reason_analyse,\n" 
				+ "       a.handle_status,\n"
				+ "       a.memo,\n"
				+ "       a.annex,\n"
				+ "       getworkername(a.fill_by),\n"
				+ "       to_char(a.fill_date, 'yyyy-MM-dd hh24:mi:ss')\n"
				+ "  from PT_JYJD_J_SGDJ a\n" 
				+ " where a.enterprise_code = '"
				+ enterpriseCode + "'";

		String sqlCount = "select count(1) from PT_JYJD_J_SGDJ a where a.enterprise_code = '"+enterpriseCode+"'";
		String strWhere = "";
		if (equName != null && equName.length() > 0) {
			strWhere += " and a.equ_name like '%" + equName + "%'";
		}
		if (sDate != null && sDate.length() > 0) {
			strWhere += " and a.happen_date >= to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			strWhere += "and a.happen_date <= to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		sql += strWhere;
		sql = sql + " order by a.jysg_id";
		sqlCount += strWhere;
		sqlCount = sqlCount + " order by a.jysg_id";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				SgdjForm model = new SgdjForm();
				Object []data = (Object[])it.next();
				model.setJysgId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					model.setAccidentTitle(data[1].toString());
				if(data[2] != null)
					model.setEquCode(data[2].toString());
				if(data[3] != null)
					model.setEquName(data[3].toString());
				if(data[4] != null)
					model.setHappenDate(data[4].toString());
				if(data[5] != null)
					model.setHandleDate(data[5].toString());
				if(data[6] != null)
					model.setReasonAnalyse(data[6].toString());
				if(data[7] != null)
					model.setHandleStatus(data[7].toString());
				if(data[8] != null)
					model.setMemo(data[8].toString());
				if(data[9] != null)
					model.setAnnex(data[9].toString());
				if(data[10] != null)
					model.setFillByName(data[10].toString());
				arrlist.add(model);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
}