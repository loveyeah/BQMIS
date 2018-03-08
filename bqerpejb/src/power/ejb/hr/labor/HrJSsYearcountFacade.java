package power.ejb.hr.labor;

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
 * Facade for entity HrJSsYearcount.
 * 
 * @see power.ejb.hr.labor.HrJSsYearcount
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJSsYearcountFacade implements HrJSsYearcountFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	@EJB(beanName = "HrCSsMainFacade")
	HrCSsMainFacadeRemote remote;

	public HrJSsYearcount save(HrJSsYearcount entity) {
		LogUtil.log("saving HrJSsYearcount instance", Level.INFO, null);
		try {
			Long detailId = bll.getMaxId("HR_J_SS_YEARCOUNT", "detail_id");
			entity.setDetailId(detailId);
			entity.setIsUse("Y");
			entityManager.persist(entity);
			entityManager.flush();

			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sql = "update hr_j_ss_yearcount  b " + "set b.is_use='N'\n"
				+ "where b.detail_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public HrJSsYearcount update(HrJSsYearcount entity) {
		LogUtil.log("updating HrJSsYearcount instance", Level.INFO, null);
		try {
			HrJSsYearcount result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJSsYearcount findById(Long id) {
		LogUtil.log("finding HrJSsYearcount instance with id: " + id,
				Level.INFO, null);
		try {
			HrJSsYearcount instance = entityManager.find(HrJSsYearcount.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void saveOrUpdateYearcountInfo(HrCSsMain mainModel,
			List<HrJSsYearcount> yearCountList) {
		boolean flag = true;
		HrCSsMain model = remote.findSsMainInfo(mainModel.getMainYear(),
				mainModel.getYearType(), mainModel.getInsuranceType(),
				mainModel.getEnterpriseCode());
		if (model == null) {
			// 增加社保管理主表主表
			model = remote.save(mainModel);
			flag = true;
		} else {// 修改主表

			model.setImportBy(mainModel.getImportBy());
			remote.update(model);
			flag = false;
		}

		Long mainID = model.getMainId();
		for (HrJSsYearcount detailModel : yearCountList) {
			if (flag) {
				detailModel.setMainId(mainID);
				if(detailModel.getYearCountCode()!=null&&!detailModel.getYearCountCode().equals(""))
				{
				this.save(detailModel);
				entityManager.flush();
				}
			} else {
				HrJSsYearcount detailInf = this.getDetailInf(mainID,
						detailModel.getYearCountCode());

				if (detailInf == null) {
					detailModel.setMainId(mainID);
					if(detailModel.getYearCountCode()!=null&&!detailModel.getYearCountCode().equals(""))
					{
					this.save(detailModel);
					entityManager.flush();
					}
				} else {
					detailInf.setIsUse("Y");
					detailModel.setMainId(mainID);
					detailModel.setDetailId(detailInf.getDetailId());
					this.update(detailModel);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public HrJSsYearcount getDetailInf(Long mainID, String yearCountCode) {
		String sql = "select * " + "from hr_j_ss_yearcount  b "
				+ "where b.main_id='" + mainID + "'\n"
				+ "and b.year_count_code='" + yearCountCode + "'\n"
				+ "and b.is_use='Y'";
		List<HrJSsYearcount> list = bll.queryByNativeSQL(sql,
				HrJSsYearcount.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public PageObject findYearcountList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select b.* "
				+ "from hr_j_ss_yearcount  b  ,hr_c_ss_main a\n"
				+ "where b.main_id=a.main_id\n" + "and  a.main_year='"
				+ strYear + "'\n" + "and a.year_type='" + yearType + "'\n"
				+ "and a.insurance_type='" + insuranceType + "'\n"
				+ "and a.enterprise_code='" + enterpriseCode + "'\n"
				+ "and b.enterprise_code='" + enterpriseCode + "'\n"
				+ "and a.is_use='Y'\n" + "and b.is_use='Y'\n";

		String whereStr = "";
		if (deptName != null && !deptName.equals("")) {
			whereStr += "and b.dept_name like '%" + deptName + "%'";
		}
		if (workName != null && !workName.equals("")) {
			whereStr += " and b.personel_name like '%" + workName + "%'";
		}
		sql += whereStr;
		String sqlCount = "select count(*) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, HrJSsYearcount.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

		if (count > 0) {
			if (count >= rowStartIdxAndCount[0]
					&& count <= rowStartIdxAndCount[0] + rowStartIdxAndCount[1]) {
				String sqlSum = "select sum(b.month_enter_account),\n"
						+ "       sum(b.month_personnel_account),\n"
						+ "       sum(b.month_total)\n"
						+ "  from hr_j_ss_yearcount b, hr_c_ss_main a\n"
						+ " where b.main_id = a.main_id\n"
						+ "   and a.main_year = '" + strYear + "'\n"
						+ "   and a.year_type = '" + yearType + "'\n"
						+ "   and a.insurance_type = '" + insuranceType + "'\n"
						+ "   and a.enterprise_code = '" + enterpriseCode
						+ "'\n" + "   and b.enterprise_code = '"
						+ enterpriseCode + "'\n" + "   and a.is_use = 'Y'\n"
						+ "   and b.is_use = 'Y'";
				sqlSum += whereStr;
				Object objtotal = bll.getSingal(sqlSum);
				if (objtotal != null && !objtotal.equals("")) {
					Object[] obj = (Object[]) bll.queryByNativeSQL(sqlSum).get(
							0);
					HrJSsYearcount model = new HrJSsYearcount();
					model.setPersonelName("总合计");
					model.setMonthEnterAccount(Double.parseDouble(obj[0]
							.toString()));
					model.setMonthPersonnelAccount(Double.parseDouble(obj[1]
							.toString()));
					model.setMonthTotal(Double.parseDouble(obj[2].toString()));
					list.add(model);
				}
			}
		}
		result.setList(list);
		result.setTotalCount(count);
		return result;

	}

	public void delAllYearcountList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode) {

		String sqlString = "select  b.detail_id "
				+ "from hr_j_ss_yearcount  b  ,hr_c_ss_main a\n"
				+ "where b.main_id=a.main_id\n" + "and  a.main_year='"
				+ strYear + "'\n" + "and a.year_type='" + yearType + "'\n"
				+ "and a.insurance_type='" + insuranceType + "'\n"
				+ "and a.enterprise_code='" + enterpriseCode + "'\n"
				+ "and b.enterprise_code='" + enterpriseCode + "'\n"
				+ "and a.is_use='Y'\n" + "and b.is_use='Y'\n";
		if (deptName != null && !deptName.equals("")) {
			sqlString += "and b.dept_name like '%" + deptName + "%'";
		}
		if (workName != null && !workName.equals("")) {
			sqlString += " and b.personel_name like '%" + workName + "%'";
		}

		String sql = "update hr_j_ss_yearcount t\n" + "   set t.is_use = 'N'\n"
				+ " where t.detail_id in (" + sqlString + ")";

		bll.exeNativeSQL(sql);

	}

}