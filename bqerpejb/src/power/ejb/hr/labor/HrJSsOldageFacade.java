package power.ejb.hr.labor;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrJSsOldage.
 * 
 * @see power.ejb.hr.labor.HrJSsOldage
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJSsOldageFacade implements HrJSsOldageFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	@EJB(beanName = "HrCSsMainFacade")
	HrCSsMainFacadeRemote remote;

	public HrJSsOldage save(HrJSsOldage entity) {
		LogUtil.log("saving HrJSsOldage instance", Level.INFO, null);
		try {
			Long detailID = bll.getMaxId("hr_j_ss_oldage", "detail_id");
			entity.setDetailId(detailID);
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
		String sql = "update hr_j_ss_oldage b " + "set b.is_use='N'\n"
				+ "where b.detail_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public HrJSsOldage update(HrJSsOldage entity) {
		LogUtil.log("updating HrJSsOldage instance", Level.INFO, null);
		try {
			HrJSsOldage result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJSsOldage findById(Long id) {
		LogUtil.log("finding HrJSsOldage instance with id: " + id, Level.INFO,
				null);
		try {
			HrJSsOldage instance = entityManager.find(HrJSsOldage.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Long findMainID(String strYear, String yearType, String insuranceType) {
		String sql = "select  a.main_id  " + " from hr_c_ss_main a\n"
				+ "where a.main_year='" + strYear + "'\n" + "and a.year_type='"
				+ yearType + "'\n" + "and a.insurance_type='" + insuranceType
				+ "'\n" + "and a.is_use='Y'";
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			Long mainID = Long.parseLong(obj.toString());
			return mainID;
		} else {
			return null;
		}

	}

	public void saveOrUpdateOldageInfo(HrCSsMain mainModel,
			List<HrJSsOldage> oldageList) {

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
		for (HrJSsOldage detailModel : oldageList) {
			if (flag) {
				detailModel.setMainId(mainID);
				this.save(detailModel);
				entityManager.flush();
			} else {
				HrJSsOldage detailInfo = this.getOldageInfo(mainID, detailModel
						.getPersonnelCode());
				if (detailInfo == null) {
					detailModel.setMainId(mainID);
					this.save(detailModel);
					entityManager.flush();
				} else {
					detailModel.setIsUse("Y");
					detailModel.setMainId(detailInfo.getMainId());
					detailModel.setDetailId(detailInfo.getDetailId());
					detailModel.setEnterpriseCode(detailInfo
							.getEnterpriseCode());
					this.update(detailModel);
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private HrJSsOldage getOldageInfo(Long mainId, String personnelCode) {
		String sql = "select *" + " from  hr_j_ss_oldage  a "
				+ "where a.main_id='" + mainId + "'\n"
				+ "and a.personnel_code='" + personnelCode + "'\n"
				+ "and a.is_use='Y'";

		List<HrJSsOldage> list = bll.queryByNativeSQL(sql, HrJSsOldage.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageObject findOldageList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select  b.* "
				+ "from  hr_c_ss_main  a  ,hr_j_ss_oldage  b\n"
				+ "where b.main_id=a.main_id\n" + "and  a.main_year='"
				+ strYear + "'\n" + "and a.year_type='" + yearType + "'\n"
				+ "and a.insurance_type='" + insuranceType + "'\n"
				+ "and a.enterprise_code='" + enterpriseCode + "'\n"
				+ "and b.enterprise_code='" + enterpriseCode + "'\n"
				+ "and a.is_use = 'Y'\n" + "and b.is_use = 'Y'\n";
		if (deptName != null && !deptName.equals("")) {
			sql += "and b.dept_name like '%" + deptName + "%'";
		}
		if (workName != null && !workName.equals("")) {
			sql += " and b.personel_name like '%" + workName + "%'";
		}
		String sqlCount = "select count(*) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, HrJSsOldage.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(count);
		return result;
	}

	public void delAllOldageList(String strYear, String yearType,
			String insuranceType, String deptName, String workName,
			String enterpriseCode) {

		String sqlString = "select  b.detail_id "
				+ "from  hr_c_ss_main  a  ,hr_j_ss_oldage  b\n"
				+ "where b.main_id=a.main_id\n" + "and  a.main_year='"
				+ strYear + "'\n" + "and a.year_type='" + yearType + "'\n"
				+ "and a.insurance_type='" + insuranceType + "'\n"
				+ "and a.enterprise_code='" + enterpriseCode + "'\n"
				+ "and b.enterprise_code='" + enterpriseCode + "'\n"
				+ "and a.is_use = 'Y'\n" + "and b.is_use = 'Y'\n";
		if (deptName != null && !deptName.equals("")) {
			sqlString += "and b.dept_name like '%" + deptName + "%'";
		}
		if (workName != null && !workName.equals("")) {
			sqlString += " and b.personel_name like '%" + workName + "%'";
		}

		String sql = "update hr_j_ss_oldage t\n" + "   set t.is_use = 'N'\n"
				+ " where t.detail_id in (" + sqlString + ")";

		bll.exeNativeSQL(sql);

	}
}