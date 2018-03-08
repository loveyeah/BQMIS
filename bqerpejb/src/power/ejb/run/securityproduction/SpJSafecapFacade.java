package power.ejb.run.securityproduction;

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

/**
 * Facade for entity SpJSafecap.
 * 
 * @see power.ejb.run.securityproduction.SpJSafecap
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafecapFacade implements SpJSafecapFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加一条安全帽领用信息
	 * @param entity
	 * @return
	 */
	public SpJSafecap save(SpJSafecap entity) throws CodeRepeatException {
		try {
			if (!this.checkDangerTypeName(entity.getSafeCapNo())) {
				if (entity.getSafecapOfferId() == null) {
					entity.setSafecapOfferId(bll.getMaxId("SP_J_SAFECAP",
							"SAFECAP_OFFER_ID"));
				}
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("安全帽编号不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一个或多个安全帽领用信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from SP_J_SAFECAP a\n"
			+ "where a.SAFECAP_OFFER_ID in ("
			+ ids + ")";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 修改一条安全帽领用信息
	 * @param entity
	 * @return SpJSafecap 
	 */
	public SpJSafecap update(SpJSafecap entity) throws CodeRepeatException {
		try {
			if (!checkDangerTypeName(entity.getSafeCapNo(), entity.getSafecapOfferId())) {
				SpJSafecap result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("安全帽编号不能重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条安全帽领用信息
	 * @param id
	 * @return
	 */
	public SpJSafecap findById(Long id) {
		LogUtil.log("finding SpJSafecap instance with id: " + id, Level.INFO,
				null);
		try {
			SpJSafecap instance = entityManager.find(SpJSafecap.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 模糊查询安全帽领用信息列表
	 * @param recipientsNameOrSafeCapNo 领用人姓名或者安全帽编号
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String recipientsNameOrSafeCapNo,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SpJSafecap instances",
				Level.INFO, null);
		try {
			if ("".equals(recipientsNameOrSafeCapNo)
					|| recipientsNameOrSafeCapNo == null) {
				recipientsNameOrSafeCapNo = "%";
			}
			PageObject result = new PageObject();
			String sqlCount = "select count(*) from SP_J_SAFECAP t\n"
				+ "where t.SAFE_CAP_NO like '%"
				+ recipientsNameOrSafeCapNo
				+ "%'\n"
				+ "or GETWORKERNAME(t.RECIPIENTS_BY) like  '%"
				+ recipientsNameOrSafeCapNo
				+ "%'\n"
				+ "and t.enterprise_code='"
				+ enterpriseCode
				+ "' " + "order by t.SAFECAP_OFFER_ID";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			if(totalCount >0 ) {
				String sql = "select t.SAFECAP_OFFER_ID,t.SAFE_CAP_NO,t.SAFE_CAP_TYPE,t.SAFE_CAP_COLOR,"
					+ "nvl(GETWORKERNAME(t.RECIPIENTS_BY),t.RECIPIENTS_BY) RECIPIENTS_BY,"
					+ "nvl(GETWORKERNAME(t.OFFER_BY),t.OFFER_BY) OFFER_BY,"
					+ "nvl(GETWORKERNAME(t.WRITE_BY),t.OFFER_BY) WRITE_BY,"
					+ "t.RECIPIENTS_DATE ,t.MEMO,t.enterprise_code from SP_J_SAFECAP t\n"
					+ "where t.SAFE_CAP_NO like '%"
					+ recipientsNameOrSafeCapNo
					+ "%'\n"
					+ "or GETWORKERNAME(t.RECIPIENTS_BY) like  '%"
					+ recipientsNameOrSafeCapNo
					+ "%'\n"
					+ "and t.enterprise_code='"
					+ enterpriseCode
					+ "' " + "order by t.SAFECAP_OFFER_ID";
				List<SpJSafecap> list = bll.queryByNativeSQL(sql, SpJSafecap.class,
						rowStartIdxAndCount);
				result.setList(list);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据安全帽名称判断名称是否重复
	 * @param safeCapNo
	 * @param safecapOfferId
	 * @return
	 */
	public boolean checkDangerTypeName(String safeCapNo, Long... safecapOfferId) {
		boolean isSame = false;
		String sql = "select count(*) from SP_J_SAFECAP t\n"
				+ "where t.SAFE_CAP_NO = '" + safeCapNo + "'";

		if (safecapOfferId != null && safecapOfferId.length > 0) {
			sql += "  and t.SAFECAP_OFFER_ID <> " + safecapOfferId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	/**
	 * 根据人名查询工号和所在的部门名称
	 * @param workName 人员名称
	 */
	@SuppressWarnings("unchecked")
	public List getDeptNameAndWorkCode(String workName) {
		String sql = "select t.emp_code ,t.chs_name  ,GETDEPTNAME(GETDEPTBYWORKCODE(t.emp_code) )dept_name "
					+ "from hr_j_emp_info t where t.chs_name = '"+workName+"'";
		List list = bll.queryByNativeSQL(sql);
		return list;
	}

}