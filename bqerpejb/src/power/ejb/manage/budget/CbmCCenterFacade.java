package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.CbmCenterForm;

/**
 * Facade for entity CbmCCenter.
 * 
 * @see power.ejb.manage.budget.CbmCCenter
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCCenterFacade implements CbmCCenterFacadeRemote {
	// property constants
	public static final String DEP_CODE = "depCode";
	public static final String DEP_NAME = "depName";
	public static final String MANAGER = "manager";
	public static final String IF_DUTY = "ifDuty";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public CbmCCenter save(CbmCCenter entity) throws CodeRepeatException {
		LogUtil.log("saving CbmCCenter instance", Level.INFO, null);
		try {
			if (!this
					.checkSame(entity.getDepName(), entity.getEnterpriseCode())) {
				entity.setCenterId(bll.getMaxId("CBM_C_CENTER", "center_id"));
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("该部门已加入预算！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "update CBM_C_CENTER  t\n" + "set t.is_use='N'\n"
				+ "where t.center_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public CbmCCenter update(CbmCCenter entity) throws CodeRepeatException {
		LogUtil.log("updating CbmCCenter instance", Level.INFO, null);
		try {
			if (!this.checkSame(entity.getDepName(),
					entity.getEnterpriseCode(), entity.getCenterId())) {
				CbmCCenter result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("该部门已加入预算！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCCenter findById(Long id) {
		LogUtil.log("finding CbmCCenter instance with id: " + id, Level.INFO,
				null);
		try {
			CbmCCenter instance = entityManager.find(CbmCCenter.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCCenter entities.
	 * 
	 * @return List<CbmCCenter> all CbmCCenter entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String deptName,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmCCenter instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			String sql = "select a.dep_code,\n" + "       a.dep_name,\n"
					+ "       a.manager,\n"
					+ "       getworkername(a.manager),\n"
					+ "       a.if_duty,\n" + "       a.center_id,\n"
					+ "       a.cost_code\n" + "  from CBM_C_CENTER a\n"
					+ " where a.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'";

			String sqlCount = "select count(1)\n" + "  from CBM_C_CENTER a\n"
					+ " where a.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'";

			String strWhere = "";
			if (deptName != null && !deptName.equals("")) {
				strWhere += " and a.dep_name like '%" + deptName + "%'\n";
			}
			sqlCount = sqlCount + strWhere;
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);

			sql = sql + strWhere;
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					CbmCenterForm form = new CbmCenterForm();
					Object[] data = (Object[]) it.next();
					form.setDepCode(data[0].toString());
					if (data[1] != null)
						form.setDepName(data[1].toString());
					if (data[2] != null)
						form.setManager(data[2].toString());
					if (data[3] != null)
						form.setManageName(data[3].toString());
					if (data[4] != null)
						form.setIfDuty(data[4].toString());
					if (data[5] != null)
						form.setCenterId(Long.parseLong(data[5].toString()));
					if (data[6] != null)
						form.setCostCode(data[6].toString());
					arrlist.add(form);
				}
			}
			result.setList(arrlist);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unused")
	private boolean checkSame(String deptName, String enterpriseCode,
			Long... centerId) {
		boolean isSame = false;
		String sql = "select count(1) from CBM_C_CENTER a\n"
				+ "where a.dep_name='" + deptName + "' and a.enterprise_code='"
				+ enterpriseCode + "'" + " and a.is_use='Y'";
		if (centerId != null && centerId.length > 0) {
			sql += "  and a.center_id <> " + centerId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	public PageObject findAllDuty(String enterpriseCode, String deptName,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmCCenter instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			String sql = "select a.dep_code,\n" + "       a.dep_name,\n"
					+ "       a.manager,\n"
					+ "       getworkername(a.manager),\n"
					+ "       a.if_duty,\n" + "       a.center_id\n"
					+ "  from CBM_C_CENTER a\n" + " where a.is_use = 'Y'\n"
					+ " and a.if_duty = 'Y' \n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'";

			String sqlCount = "select count(1)\n" + "  from CBM_C_CENTER a\n"
					+ " where a.is_use = 'Y'\n" + " and a.if_duty = 'Y' \n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'";

			String strWhere = "";
			if (deptName != null && !deptName.equals("")) {
				strWhere += " and a.dep_name like '%" + deptName + "%'\n";
			}
			sqlCount = sqlCount + strWhere;
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);

			sql = sql + strWhere;
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					CbmCenterForm form = new CbmCenterForm();
					Object[] data = (Object[]) it.next();
					form.setDepCode(data[0].toString());
					if (data[1] != null)
						form.setDepName(data[1].toString());
					if (data[2] != null)
						form.setManager(data[2].toString());
					if (data[3] != null)
						form.setManageName(data[3].toString());
					if (data[4] != null)
						form.setIfDuty(data[4].toString());
					if (data[5] != null)
						form.setCenterId(Long.parseLong(data[5].toString()));
					arrlist.add(form);
				}
			}
			result.setList(arrlist);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
}