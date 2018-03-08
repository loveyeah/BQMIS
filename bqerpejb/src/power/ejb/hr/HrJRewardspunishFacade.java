/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.hr;

import java.sql.SQLException;
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

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 职工奖惩登记Facade
 * 
 * @author zhaozhijie
 */
@Stateless
public class HrJRewardspunishFacade implements HrJRewardspunishFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**是否使用*/
	private String IS_USE_Y = "Y";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 新增职工奖惩登记
	 * 
	 * @param entity 新增职工奖惩登记实体
	 * @throws SQLException 
	 */
	public void save(HrJRewardspunish entity) throws SQLException {
		LogUtil.log("EJB:新增职工奖惩登记开始", Level.INFO, null);
		try {
			// 设置新增职工奖惩ID
			entity.setRewardspunishid(bll.getMaxId("HR_J_REWARDSPUNISH", "REWARDSPUNISHID"));
			entityManager.persist(entity);
			LogUtil.log("EJB:新增职工奖惩登记结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增职工奖惩登记失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent HrJRewardspunish entity.
	 * 
	 * @param entity
	 *            HrJRewardspunish entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJRewardspunish entity) {
		LogUtil.log("deleting HrJRewardspunish instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJRewardspunish.class, entity
					.getRewardspunishid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新职工奖惩登记
	 * 
	 * @param entity 更新职工奖惩登记实体
	 * @return 职工奖惩登记实体
	 * @throws SQLException 
	 */
	public HrJRewardspunish update(HrJRewardspunish entity) throws SQLException {
		LogUtil.log("EJB:更新职工奖惩登记开始", Level.INFO, null);
		try {
			HrJRewardspunish result = entityManager.merge(entity);
			LogUtil.log("EJB:更新职工奖惩登记结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:更新职工奖惩登记失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public HrJRewardspunish findById(Long id) {
		LogUtil.log("finding HrJRewardspunish instance with id: " + id,
				Level.INFO, null);
		try {
			HrJRewardspunish instance = entityManager.find(
					HrJRewardspunish.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJRewardspunish entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJRewardspunish property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJRewardspunish> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJRewardspunish> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJRewardspunish instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJRewardspunish model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJRewardspunish entities.
	 * 
	 * @return List<HrJRewardspunish> all HrJRewardspunish entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJRewardspunish> findAll() {
		LogUtil.log("finding all HrJRewardspunish instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJRewardspunish model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询员工奖惩登记信息
	 * @param  strRewardMonth 奖惩月份
	 * @param strEnterpriseCode 企业代码
	 * @return 所有员工奖惩登记信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpRewardInfo(String strRewardMonth, String strEnterpriseCode
			,final int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:员工奖惩登记查询正常开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			StringBuffer sbd = new StringBuffer();

			int N= 6;
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("B.CHS_NAME , ");
			sbd.append("B.EMP_CODE, ");
			sbd.append("B.EMP_ID, ");
			sbd.append("C.DEPT_NAME, ");
			sbd.append("D.REWARDS_PUNISH, ");
			sbd.append("DECODE(D.REWARDS_PUNISH_TYPE,'1','奖励','2','处罚'), ");
			sbd.append("TO_CHAR(A.REWARDSPUNISH_DATE, 'yyyy-mm-dd'),");
			sbd.append("A.REWARDS_PUNISH_REASON, ");
			sbd.append("A.REWARDSPUNISHID, ");
			sbd.append("A.MEMO, ");
			sbd.append("A.LAST_MODIFIED_DATE ");

			// FROM文
			sbd.append("FROM HR_J_REWARDSPUNISH A ");
			// 左联接人员基本信息表
			sbd.append("LEFT JOIN HR_J_EMP_INFO B ");
			sbd.append("ON A.EMP_ID = B.EMP_ID ");
		//	sbd.append("AND B.ENTERPRISE_CODE = ? ");
			//sbd.append("AND B.IS_USE = ? ");  modify by fyyang 090908 HR_J_EMP_INFO 无is_use字段

			// 左联接部门设置表
			sbd.append("LEFT JOIN HR_C_DEPT C ");
			sbd.append("ON B.DEPT_ID = C.DEPT_ID ");
			sbd.append("AND C.ENTERPRISE_CODE = ? ");
			sbd.append("AND C.IS_USE = ? ");

			// 左联接奖惩类别维护
			sbd.append("LEFT JOIN HR_C_REWARDSPUNISH D ");
			sbd.append("ON A.REWARDS_PUNISH_ID = D.REWARDS_PUNISH_ID ");
			sbd.append("AND D.ENTERPRISE_CODE = ? ");
			sbd.append("AND D.IS_USE = ? ");

			// WHERE文
			sbd.append("WHERE A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			if(strRewardMonth != null && !"".equals(strRewardMonth)) {
				sbd.append("AND TO_CHAR(A.REWARDSPUNISH_DATE,'yyyy-mm') = ? ");
				N =7;
			}
			// 查询参数数组
			Object[] params = new Object[N];
			int i =0;
		//	params[i++] = strEnterpriseCode;
		//	params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = "Y";//update by sychen 20100901
//			params[i++] = "U";
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			if(strRewardMonth != null && !"".equals(strRewardMonth)) {
				params[i++] = strRewardMonth;
			}
			// 记录数
			String sqlCount = "SELECT " +
					" COUNT(A.REWARDSPUNISHID) " +sbd.substring(239, sbd.length());

			List<EmpRewardRegisterInfo> list = bll.queryByNativeSQL(sbd.toString(), params, rowStartIdxAndCount);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<EmpRewardRegisterInfo> arrList = new ArrayList<EmpRewardRegisterInfo>();
			if (list !=null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					EmpRewardRegisterInfo empRewardRegisterInfo = new EmpRewardRegisterInfo();
					Object[] data = (Object[]) it.next();
					// 员工姓名
					if(data[0] != null) {
						empRewardRegisterInfo.setEmpName(data[0].toString());
					}
					// 员工工号
					if (data[1] != null) {
						empRewardRegisterInfo.setEmpCode(data[1].toString());
					}
					// 员工ID
					if (data[2] != null) {
						empRewardRegisterInfo.setEmpID(data[2].toString());
					}
					// 所属部门
					if (data[3] != null) {
						empRewardRegisterInfo.setDeptName(data[3].toString());
					}
					// 奖惩类别名称
					if(data[4] != null) {
						empRewardRegisterInfo.setRewardsPunish(data[4].toString());
					}
					// 奖惩类别
					if(data[5] != null) {
						empRewardRegisterInfo.setRewardsPunishType(data[5].toString());
					}
					// 奖惩日期
					if (data[6] != null) {
						empRewardRegisterInfo.setRewardsDate(data[6].toString());
					}
					// 奖惩原因
					if (data[7] != null) {
						empRewardRegisterInfo.setRewardsReason(data[7].toString());
					}
					// 职工奖惩ID
					if (data[8] != null) {
						empRewardRegisterInfo.setRewardsPunishID(data[8].toString());
					}
					// 备注
					if (data[9] != null) {
						empRewardRegisterInfo.setMemo(data[9].toString());
					}
					// 修改时间
					if (data[10] != null) {
						empRewardRegisterInfo.setLastModifyDate(data[10].toString());
					}
					arrList.add(empRewardRegisterInfo);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:员工奖惩登记查询正常结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:员工奖惩登记查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 员工奖惩登记保存操作
	 * @param 新增员工奖惩登记 lstSaveHrJRewardspunish
	 * @param 更新员工奖惩登记 lstUpdateHrJRewardspunish
	 * @param 删除员工奖惩登记 lstDeleteHrJRewardspunish
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	public void save(List<HrJRewardspunish> lstSaveHrJRewardspunish,
			List<HrJRewardspunish> lstUpdateHrJRewardspunish,
			List<HrJRewardspunish> lstDeleteHrJRewardspunish) throws SQLException, DataChangeException {
		LogUtil.log("EJB:员工奖惩登记操作正常开始", Level.INFO, null);
		// CM 回滚操作
		try {
			// 日期的格式化
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			// 新增员工奖惩登记
			if(lstSaveHrJRewardspunish.size() >0) {
				LogUtil.log("EJB:新增员工奖惩登记数据开始。", Level.INFO, null);
				save(lstSaveHrJRewardspunish.get(0));
				LogUtil.log("EJB:新增员工奖惩登记数据结束。", Level.INFO, null);
			}
			// 更新员工奖惩登记
			if (lstUpdateHrJRewardspunish.size() > 0) {
				// 先前的日期
				Date frontDate = lstUpdateHrJRewardspunish.get(0).getLastModifiedDate();
				String strFrontDate = dateFormat.format(frontDate);
				// 职工奖惩ID
				Long lgnRewardId = lstUpdateHrJRewardspunish.get(0).getRewardspunishid();
				// 数据库中的日期
				HrJRewardspunish entity = findById(lgnRewardId);
				Date newDate = entity.getLastModifiedDate();
				String strNewDate = dateFormat.format(newDate);
				// 排他操作
				if (!strNewDate.equals(strFrontDate)) {
					throw new DataChangeException(null);
				} else {
					LogUtil.log("EJB:更新员工奖惩登记数据开始。", Level.INFO, null);
					// 修改时间
					lstUpdateHrJRewardspunish.get(0).setLastModifiedDate(new java.util.Date());
					update(lstUpdateHrJRewardspunish.get(0));
					LogUtil.log("EJB:更新员工奖惩登记数据结束。", Level.INFO, null);
				}
			}
			// 删除员工奖惩登记
			if (lstDeleteHrJRewardspunish.size() >0) {
				// 先前的日期
				Date frontDate = lstDeleteHrJRewardspunish.get(0).getLastModifiedDate();
				String strFrontDate = dateFormat.format(frontDate);
				// 职工奖惩ID
				Long lgnRewardId = lstDeleteHrJRewardspunish.get(0).getRewardspunishid();
				// 数据库中的日期
				HrJRewardspunish entity = findById(lgnRewardId);
				Date newDate = entity.getLastModifiedDate();
				String strNewDate = dateFormat.format(newDate);
				// 排他操作
				if (!strNewDate.equals(strFrontDate)) {
					throw new DataChangeException(null);
				} else {
					LogUtil.log("EJB:删除员工奖惩登记数据开始。", Level.INFO, null);
					// 修改时间
					lstDeleteHrJRewardspunish.get(0).setLastModifiedDate(new java.util.Date());
					update(lstDeleteHrJRewardspunish.get(0));
					LogUtil.log("EJB:删除员工奖惩登记数据结束。", Level.INFO, null);
				}
			}
		} catch (RuntimeException e) {
			throw new SQLException();
		} 
		LogUtil.log("EJB:员工奖惩登记操作正常结束", Level.INFO, null);
	}
}