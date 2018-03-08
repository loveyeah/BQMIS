package power.ejb.hr.labor;

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
import power.ejb.resource.form.TransActionHisInfo;

/**
 * Facade for entity HrJSsHealth.
 * 
 * @see power.ejb.hr.labor.HrJSsHealth
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJSsHealthFacade implements HrJSsHealthFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	@EJB(beanName = "HrCSsMainFacade")
	private HrCSsMainFacadeRemote mainRemote;

	public HrJSsHealth save(HrJSsHealth entity) {
		LogUtil.log("saving HrJSsHealth instance", Level.INFO, null);
		try {
			entity.setDetailId(bll.getMaxId("HR_J_SS_HEALTH", "detail_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sql = "update HR_J_SS_HEALTH t\n" + "set t.is_use='N'\n"
				+ "where t.detail_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public HrJSsHealth update(HrJSsHealth entity) {
		LogUtil.log("updating HrJSsHealth instance", Level.INFO, null);
		try {
			HrJSsHealth result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJSsHealth findById(Long id) {
		LogUtil.log("finding HrJSsHealth instance with id: " + id, Level.INFO,
				null);
		try {
			HrJSsHealth instance = entityManager.find(HrJSsHealth.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void saveOrUpdateHealthInfo(HrCSsMain mainModel,
			List<HrJSsHealth> healthList) {
		boolean flag = true;
		HrCSsMain model = mainRemote.findSsMainInfo(mainModel.getMainYear(),
				mainModel.getYearType(), mainModel.getInsuranceType(),
				mainModel.getEnterpriseCode());
		if (model == null) {
			model = mainRemote.save(mainModel);
			flag = true;
		} else {
			model.setImportBy(mainModel.getImportBy());
			mainRemote.update(model);
			flag = false;
		}
		Long mainId = model.getMainId();
		for (HrJSsHealth detailModel : healthList) {
			if (flag) {
				detailModel.setMainId(mainId);
				this.save(detailModel);
				entityManager.flush();
			} else {
				HrJSsHealth detailInfo = this.getHealthInfo(mainId, detailModel
						.getMedicareCardNumber());
				if (detailInfo == null) {
					detailModel.setMainId(mainId);
					this.save(detailModel);
					entityManager.flush();
				} else {
					detailModel.setMainId(mainId);
					detailModel.setDetailId(detailInfo.getDetailId());
					detailModel.setEnterpriseCode(detailInfo
							.getEnterpriseCode());
					detailModel.setIsUse(detailInfo.getIsUse());
					this.update(detailModel);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private HrJSsHealth getHealthInfo(Long mainId, String cardNumber) {
		String sql = "select * from hr_j_ss_health t\n" + " where t.main_id="
				+ mainId + "\n" + " and t.medicare_card_number='" + cardNumber
				+ "'\n" + " and t.is_use='Y'";

		List<HrJSsHealth> list = bll.queryByNativeSQL(sql, HrJSsHealth.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public PageObject findHealthList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql = "select t.*\n"
				+ "  from hr_j_ss_health t, hr_c_ss_main a\n"
				+ " where t.main_id = a.main_id\n" + "   and t.is_use = 'Y'\n"
				+ "   and a.is_use = 'Y'\n" + "   and a.main_year = '"
				+ strYear + "'\n" + "   and a.year_type = '" + yearType + "'\n"
				+ "   and a.insurance_type = '" + insuranceType + "'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.enterprise_code = '" + enterpriseCode + "'\n";

		String whereStr = "";
		if (deptName != null && !deptName.equals("")) {
			whereStr += "   and t.dept_name like '%" + deptName + "%'\n";
		}
		if (workName != null && !workName.equals("")) {
			whereStr += "   and t.personel_name like '%" + workName + "%'";
		}
		sql += whereStr;

		List<HrJSsHealth> arraylist = bll.queryByNativeSQL(sql,
				HrJSsHealth.class, rowStartIdxAndCount);
		String sqlCount = " select count(*) from (" + sql + ")tt ";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());

		// if((totalCount-rowStartIdxAndCount[0])/rowStartIdxAndCount[1] < 1 &&
		// rowStartIdxAndCount[0]!=0)
		if (totalCount > 0) {
			if (totalCount >= rowStartIdxAndCount[0]
					&& totalCount <= rowStartIdxAndCount[0]
							+ rowStartIdxAndCount[1]) {
				String sqlSum = "select sum(t.account_num)\n"
						+ "  from hr_j_ss_health t, hr_c_ss_main a\n"
						+ " where t.main_id = a.main_id\n"
						+ "   and t.is_use = 'Y'\n" + "   and a.is_use = 'Y'\n"
						+ "   and a.main_year = '" + strYear + "'\n"
						+ "   and a.year_type = '" + yearType + "'\n"
						+ "   and a.insurance_type = '" + insuranceType + "'\n"
						+ "   and a.enterprise_code = '" + enterpriseCode
						+ "'\n" + "   and t.enterprise_code = '"
						+ enterpriseCode + "'";

				sqlSum += whereStr;
				Object objtotal = bll.getSingal(sqlSum);
				if (objtotal != null && !objtotal.equals("")) {
					HrJSsHealth model = new HrJSsHealth();
					model.setPersonelName("总合计");
					model
							.setAccountNum(Double.parseDouble(objtotal
									.toString()));
					arraylist.add(model);
				}
			}
		}
		obj.setList(arraylist);
		obj.setTotalCount(totalCount);
		return obj;

	}

	public void delAllHealthList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode) {

		String sqlString = "select  t.detail_id "
				+ "  from hr_j_ss_health t, hr_c_ss_main a\n"
				+ " where t.main_id = a.main_id\n" + "   and t.is_use = 'Y'\n"
				+ "   and a.is_use = 'Y'\n" + "   and a.main_year = '"
				+ strYear + "'\n" + "   and a.year_type = '" + yearType + "'\n"
				+ "   and a.insurance_type = '" + insuranceType + "'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.enterprise_code = '" + enterpriseCode + "'\n";
		if (deptName != null && !deptName.equals("")) {
			sqlString += "and t.dept_name like '%" + deptName + "%'";
		}
		if (workName != null && !workName.equals("")) {
			sqlString += " and t.personel_name like '%" + workName + "%'";
		}

		String sql = "update hr_j_ss_health t\n" + "   set t.is_use = 'N'\n"
				+ " where t.detail_id in (" + sqlString + ")";

		bll.exeNativeSQL(sql);

	}
}