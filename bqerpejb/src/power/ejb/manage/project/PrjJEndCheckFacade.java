package power.ejb.manage.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PrjJEndCheck.
 * 
 * @see power.ejb.manage.project.PrjJEndCheck
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJEndCheckFacade implements PrjJEndCheckFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PrjJEndCheck save(PrjJEndCheck entity) {
		LogUtil.log("saving PrjJEndCheck instance", Level.INFO, null);
		try {
			entity.setCheckId(bll.getMaxId("PRJ_J_END_CHECK", "CHECK_ID"));
			entity.setIsUse("Y");
			entity.setEntryDate(new Date());
			entityManager.persist(entity);
			String sql = "update PRJ_J_REGISTER t set t.status_id = 2 where t.prj_id = "
					+ entity.getPrjId();
			bll.exeNativeSQL(sql);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long id) {
		PrjJEndCheck entity = this.findById(id);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}

	}

	public PrjJEndCheck update(PrjJEndCheck entity) {
		LogUtil.log("updating PrjJEndCheck instance", Level.INFO, null);
		try {
			PrjJEndCheck result = entityManager.merge(entity);
			String sql = "update PRJ_J_REGISTER t set t.status_id = 2 where t.prj_id = "
					+ entity.getPrjId();
			bll.exeNativeSQL(sql);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJEndCheck findById(Long id) {
		LogUtil.log("finding PrjJEndCheck instance with id: " + id, Level.INFO,
				null);
		try {
			PrjJEndCheck instance = entityManager.find(PrjJEndCheck.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PrjJEndCheck> findlist(int... rowStartIdxAndCount) {
		LogUtil.log("finding all InvCTempMaterial instances", Level.INFO, null);
		try {
			final String queryString = "select model from PrjJEndCheck model";
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

		/*
		 * PageObject obj=new PageObject(); obj.setList(list);
		 * obj.setTotalCount(totalCount); return obj;
		 */
	}

	public void deleteMulti(String ids) {
		String sql = "update PRJ_J_END_CHECK a\n" + "   set a.is_use = 'N'\n"
				+ " where a.CHECK_ID in (" + ids + ")\n"
				+ "   and a.IS_USE = 'Y'";
		bll.exeNativeSQL(sql);
	}

	public PageObject findPrjEndCheckList(String workerCode,
			String enterpriseCode, String conName, String contractorName,
			String flag, int... rowStartIdxAndCount) throws ParseException {
		PageObject pg = new PageObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "select a.CHECK_ID,\n"
				+ "       a.CON_ID,\n"
				+ "       a.CON_NAME,\n"
				+ "       a.REPORT_CODE,\n"
				+ "       a.PROJECT_PRICE,\n"
				+ "       a.CONTRACTOR_ID,\n"
				+ "       a.CONTRACTOR_NAME,\n"
				+ "       a.PRJ_LOCATION,\n"
				+ "       a.QUANTITIES,\n"
				+ "       GETWORKERNAME(a.ENTRY_BY) AS ENTRY_BY,\n"
				+ "       to_char(a.START_DATE, 'yyyy-MM-dd'),\n"
				+ "       to_char(a.END_DATE, 'yyyy-MM-dd'),\n"
				+ "       to_char(a.CHECK_DATE, 'yyyy-MM-dd'),\n"
				+ "       to_char(a.ENTRY_DATE, 'yyyy-MM-dd'),\n"
				+ "       a.PROJECT_CONTENT,\n"
				+ "       a.END_INFO,\n"
				+ "       a.CHECK_TEXT,\n"
				+ "       a.QUALITY_ASSESS,\n"
				+ "       GETWORKERNAME(a.CONSTRUCT_CHARGE_BY),\n"
				+ "       a.CONTRACTOR_CHARGE_BY,\n"
				+ "       a.BACK_ENTRY_BY,\n"
				+ " a.prj_id ,\n"
				+ " (select t.prj_name from PRJ_J_REGISTER t where t.prj_id = a.prj_id) as prj_name"
				+ "  from PRJ_J_END_CHECK a\n";
		String sqlCount = "select count(1)\n" + "  from PRJ_J_END_CHECK a\n";

		String strWhere = " a.enterprise_code='" + enterpriseCode
				+ "' and a.is_use='Y'";

		if (conName != null && conName.length() > 0) {
			strWhere += " and a.con_name like '%" + conName + "%'";
		}
		if (contractorName != null && contractorName.length() > 0) {
			strWhere += " and a.contractor_name like '%" + contractorName
					+ "%'  \n";
		}
		if (flag.equals("fillQuery")) {
			if (workerCode != null && !workerCode.equals("")
					&& !workerCode.equals("999999"))
				strWhere += " and a.entry_by='" + workerCode + "' \n";
		}
		if (strWhere != "") {
			sql = sql + " where  " + strWhere;
			sqlCount = sqlCount + " where  " + strWhere;
		}
		System.out.println("rowStartIdxAndCount======================"
				+ rowStartIdxAndCount);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		System.out.println("list======================" + list);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				PrjJEndCheck model = new PrjJEndCheck();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					model.setCheckId(Long.parseLong(data[0].toString()));
				} else {
					model.setCheckId(bll
							.getMaxId("PRJ_J_END_CHECK", "CHECK_ID"));
				}
				if (data[1] != null)
					model.setConId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					model.setConName(data[2].toString());
				if (data[3] != null)
					model.setReportCode(data[3].toString());
				if (data[4] != null)
					model.setProjectPrice(Double
							.parseDouble(data[4].toString()));
				if (data[5] != null)
					model.setContractorId(Long.parseLong(data[5].toString()));
				if (data[6] != null)
					model.setContractorName(data[6].toString());
				if (data[7] != null)
					model.setPrjLocation(data[7].toString());
				if (data[8] != null)
					model.setQuantities(data[8].toString());
				if (data[9] != null)
					model.setEntryBy(data[9].toString());
				if (data[10] != null)
					model.setStartDate(format.parse(data[10].toString()));
				if (data[11] != null)
					model.setEndDate(format.parse(data[11].toString()));
				if (data[12] != null)
					model.setCheckDate(format.parse(data[12].toString()));
				if (data[13] != null)
					model.setEntryDate(format.parse(data[13].toString()));
				if (data[14] != null)
					model.setProjectContent(data[14].toString());
				if (data[15] != null)
					model.setEndInfo(data[15].toString());
				if (data[16] != null)
					model.setCheckText(data[16].toString());
				if (data[17] != null)
					model.setQualityAssess(data[17].toString());
				if (data[18] != null)
					model.setConstructChargeBy(data[18].toString());
				if (data[19] != null)
					model.setContractorChargeBy(data[19].toString());
				if (data[20] != null)
					model.setBackEntryBy(data[20].toString());
				if (data[21] != null)
					model.setPrjId(data[21].toString() + ","
							+ data[22].toString());
				arrlist.add(model);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	public String findProjectNo(String dept, String date) {
		String firstLever="select GETDEPTNAME(getfirstlevelbyid("+Long.parseLong(dept)+")) from dual";
		List deptNameList=bll.queryByNativeSQL(firstLever);
		String FirstdeptName=deptNameList.get(0).toString();
		String sql1 = String.format("select fun_spellcode('%s') from dual",
				FirstdeptName);
		String deptName = bll.getSingal(sql1).toString();
		System.out.println("部门名称首字母拼写为：" + deptName);
		String keyword = "Q/CDT-" + deptName + "-" + date;
		int num = Integer.parseInt((bll.getSingal("select count(*) from"
				+ " PRJ_J_END_CHECK" + " where REPORT_CODE like '%" + keyword
				+ "%'").toString()));
		System.out.println("编号序号为：" + num);
		keyword += "-0" + (num + 1);
		return keyword;
	}

}