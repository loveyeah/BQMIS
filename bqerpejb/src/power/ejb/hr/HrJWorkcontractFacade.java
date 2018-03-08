/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

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

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 劳动合同登记EJB方法体
 * 
 * @see power.ejb.hr.HrJWorkcontract
 * @author zhouxu
 */
@Stateless
public class HrJWorkcontractFacade implements HrJWorkcontractFacadeRemote {
	// property constants
	public static final String EMP_ID = "empId";
	public static final String DEPT_ID = "deptId";
	public static final String STATION_ID = "stationId";
	public static final String WROK_CONTRACT_NO = "wrokContractNo";
	public static final String FRIST_DEP_ID = "fristDepId";
	public static final String FRIST_ADDREST = "fristAddrest";
	public static final String CONTRACT_TERM_ID = "contractTermId";
	public static final String IF_EXECUTE = "ifExecute";
	public static final String CONTRACT_CONTINUE_MARK = "contractContinueMark";
	public static final String MEMO = "memo";
	public static final String INSERTBY = "insertby";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	private String BLANK = "";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	/** 是否使用: 1 */
	private static final String IF_EXECUTE_1 = "1";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrJWorkcontract entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJWorkcontract entity to persist
	 * @throws DataChangeException
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJWorkcontract entity) throws DataChangeException {
		LogUtil.log("EJB:新增劳动合同开始。", Level.INFO, null);
		try {
			PageObject checkObject = this.findByEmpId(entity.getEmpId(), entity
					.getEnterpriseCode());
			if (checkObject.getTotalCount() > 0) {
				HrJWorkcontract model = (HrJWorkcontract) checkObject.getList().get(0);
				model.setIfExecute(entity.getIfExecute());
				model.setEmpId(entity.getEmpId());
				model.setOwner(entity.getOwner());
				model.setSignedInstitutions(entity.getSignedInstitutions());
				model.setWrokContractNo(entity.getWrokContractNo());
				model.setWorkSignDate(entity.getWorkSignDate());
				model.setContractTermId(entity.getContractTermId());
				model.setContractPeriod(entity.getContractPeriod());
				model.setLaborType(entity.getLaborType());
				model.setContractType(entity.getContractType());
				model.setStartDate(entity.getStartDate());
				model.setEndDate(entity.getEndDate());
				entityManager.merge(model);
				entityManager.flush();
//				model.set
			} else {
				if (entity.getWorkcontractid() == null) {
					entity.setWorkcontractid(bll.getMaxId("HR_J_WORKCONTRACT",
							"WORKCONTRACTID"));
				}

				entity.setInsertdate(new Date());
				entity.setLastModifiedDate(new Date());
				entityManager.persist(entity);
				LogUtil.log("EJB:新增劳动合同结束。", Level.INFO, null);
			}

		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增劳动合同错误。", Level.SEVERE, re);
			throw new DataChangeException(entity.getEmpId().toString());
			// throw re;
		}
	}

	/**
	 * Delete a persistent HrJWorkcontract entity.
	 * 
	 * @param entity
	 *            HrJWorkcontract entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJWorkcontract entity) {
		LogUtil.log("deleting HrJWorkcontract instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJWorkcontract.class, entity
					.getWorkcontractid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新劳动合同返回该合同
	 * 
	 * @param entity
	 *            需要被更新的bean的内容
	 * 
	 * @throws RuntimeException
	 *             更新失败
	 * 
	 * @throws DataChangeException
	 *             排他失败
	 */
	public HrJWorkcontract update(HrJWorkcontract entity)
			throws DataChangeException {
		LogUtil.log("EJB:更新劳动合同开始。", Level.INFO, null);
		try {
			HrJWorkcontract lastBean = this.findById(
					entity.getWorkcontractid(), entity.getEnterpriseCode());
			HrJWorkcontract result;
			SimpleDateFormat sdfFrom = new SimpleDateFormat(
					DATE_FORMAT_YYYYMMDD_HHMMSS);
			String dtNowTime = sdfFrom.format(lastBean.getLastModifiedDate());
			String dtOldTime = sdfFrom.format(entity.getLastModifiedDate());
			if (dtNowTime.equals(dtOldTime)) {
				// 排他成功
				lastBean.setWrokContractNo(entity.getWrokContractNo());
				lastBean.setFristDepId(entity.getFristDepId());
				lastBean.setFristAddrest(entity.getFristAddrest());
				lastBean.setContractTermId(entity.getContractTermId());
				lastBean.setWorkSignDate(entity.getWorkSignDate());
				lastBean.setStartDate(entity.getStartDate());
				lastBean.setEndDate(entity.getEndDate());
				lastBean.setMemo(entity.getMemo());
				lastBean.setLastModifiedDate(new Date());
				lastBean.setLastModifiedBy(entity.getLastModifiedBy());
				lastBean.setIfExecute(entity.getIfExecute());
				lastBean.setIsUse(entity.getIsUse());
				// add by ywliu 20100611
				lastBean.setOwner(entity.getOwner());
				lastBean.setSignedInstitutions(entity.getSignedInstitutions());
				lastBean.setContractPeriod(entity.getContractPeriod());
				lastBean.setLaborType(entity.getLaborType());
				lastBean.setContractType(entity.getContractType());
				// end
				result = entityManager.merge(lastBean);
			} else {
				// 排他失败
				throw new DataChangeException(null);
			}
			LogUtil.log("EJB:更新劳动合同结束。", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:更新劳动合同失败。", Level.SEVERE, re);
			throw re;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:更新劳动合同失败。", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 更新劳动合同返回该合同
	 * 
	 * @param entity
	 *            需要被更新的bean的内容
	 * 
	 * @throws RuntimeException
	 *             更新失败
	 * 
	 * @throws DataChangeException
	 *             排他失败
	 */
	public void update1(HrJWorkcontract entity) throws DataChangeException {
		LogUtil.log("EJB:更新劳动合同开始。", Level.INFO, null);
		try {
			HrJWorkcontract lastBean = this.findById(
					entity.getWorkcontractid(), entity.getEnterpriseCode());
			SimpleDateFormat sdfFrom = new SimpleDateFormat(
					DATE_FORMAT_YYYYMMDD_HHMMSS);
			String dtNowTime = sdfFrom.format(lastBean.getLastModifiedDate());
			String dtOldTime = sdfFrom.format(entity.getLastModifiedDate());
			if (dtNowTime.equals(dtOldTime)) {
				// 排他成功
				entity.setLastModifiedDate(new Date());
				entityManager.merge(entity);
			} else {
				// 排他失败
				throw new DataChangeException(null);
			}
			LogUtil.log("EJB:更新劳动合同结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:更新劳动合同失败。", Level.SEVERE, re);
			throw re;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:更新劳动合同失败。", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 按合同id查找合同信息
	 * 
	 * @param id
	 *            合同id
	 * @param enterpriseCode
	 *            企业代码
	 * @return 合同bean
	 */
	@SuppressWarnings("unchecked")
	public HrJWorkcontract findById(Long id, String enterpriseCode) {
		LogUtil.log("EJB:按合同id查找合同信息开始", Level.INFO, null);
		try {
			// 查询sql
			HrJWorkcontract tempBean = new HrJWorkcontract();
			String sql = "SELECT distinct * " + "FROM "
					+ "HR_J_WORKCONTRACT A " + "WHERE " + "A.IS_USE = ? AND "
					+ "A.WORKCONTRACTID = ? AND " + "A.ENTERPRISE_CODE = ?";
			LogUtil.log("SQL:" + sql, Level.INFO, null);
			List<HrJWorkcontract> list = bll.queryByNativeSQL(sql,
					new Object[] { "Y", id, enterpriseCode },
					HrJWorkcontract.class);
			if (list == null) {
				tempBean = null;
			} else {
				tempBean = list.get(0);
			}
			LogUtil.log("EJB:按合同ID查询合同结束", Level.INFO, null);
			return tempBean;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:按合同ID查询合同错误", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * Find all HrJWorkcontract entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJWorkcontract property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJWorkcontract> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkcontract> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJWorkcontract instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJWorkcontract model where model."
					+ propertyName + "= :propertyValue and model";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 按员工ID查询合同
	 * 
	 * @param empId
	 * @param enterpriseCode
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByEmpId(Long empId, String enterpriseCode)
			throws RuntimeException {
		LogUtil.log("EJB:按员工ID查询合同开始", Level.INFO, null);
		try {
			PageObject object = new PageObject();
			// 查询sql
			String sql = "SELECT * " + "FROM " + "HR_J_WORKCONTRACT A "
					+ "WHERE " + "A.IS_USE = ? AND " + "A.EMP_ID = ? AND "
					+ "A.ENTERPRISE_CODE = ? AND " + "A.IF_EXECUTE = ? ";
			String sqlCount = "SELECT COUNT(A.WORKCONTRACTID) " + "FROM "
					+ "HR_J_WORKCONTRACT A " + "WHERE " + "A.IS_USE = ? AND "
					+ "A.EMP_ID = ? AND " + "A.ENTERPRISE_CODE = ? AND "
					+ "A.IF_EXECUTE = ? ";
			LogUtil.log("EJB:按员工ID查询合同开始", Level.INFO, null);
			List<HrJWorkcontract> list = bll.queryByNativeSQL(sql,
					new Object[] { "Y", empId, enterpriseCode, "1" },
					HrJWorkcontract.class);

			Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
					new Object[] { "Y", empId, enterpriseCode, "1" })
					.toString());
			if (list == null) {
				list = new ArrayList<HrJWorkcontract>();
			}
			object.setList(list);
			object.setTotalCount(totalCount);
			LogUtil.log("EJB:按员工ID查询合同结束", Level.INFO, null);
			return object;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:按员工ID查询合同错误", Level.SEVERE, e);
			throw e;
		}
	}

	public List<HrJWorkcontract> findByDeptId(Object deptId) {
		return findByProperty(DEPT_ID, deptId);
	}

	public List<HrJWorkcontract> findByStationId(Object stationId) {
		return findByProperty(STATION_ID, stationId);
	}

	public List<HrJWorkcontract> findByWrokContractNo(Object wrokContractNo) {
		return findByProperty(WROK_CONTRACT_NO, wrokContractNo);
	}

	public List<HrJWorkcontract> findByFristDepId(Object fristDepId) {
		return findByProperty(FRIST_DEP_ID, fristDepId);
	}

	public List<HrJWorkcontract> findByFristAddrest(Object fristAddrest) {
		return findByProperty(FRIST_ADDREST, fristAddrest);
	}

	public List<HrJWorkcontract> findByContractTermId(Object contractTermId) {
		return findByProperty(CONTRACT_TERM_ID, contractTermId);
	}

	public List<HrJWorkcontract> findByIfExecute(Object ifExecute) {
		return findByProperty(IF_EXECUTE, ifExecute);
	}

	public List<HrJWorkcontract> findByContractContinueMark(
			Object contractContinueMark) {
		return findByProperty(CONTRACT_CONTINUE_MARK, contractContinueMark);
	}

	public List<HrJWorkcontract> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<HrJWorkcontract> findByInsertby(Object insertby) {
		return findByProperty(INSERTBY, insertby);
	}

	public List<HrJWorkcontract> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<HrJWorkcontract> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrJWorkcontract> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	/**
	 * Find all HrJWorkcontract entities.
	 * 
	 * @return List<HrJWorkcontract> all HrJWorkcontract entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkcontract> findAll() {
		LogUtil.log("finding all HrJWorkcontract instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJWorkcontract model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据人员ID查找部门,岗位,合同信息
	 * 
	 * @param empId
	 *            人员Id
	 * @param enterpriseCode
	 *            企业编码
	 * @return
	 * @throws RuntimeException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkContractInfo(Long empId, String enterpriseCode)
			throws RuntimeException, ParseException {
		LogUtil.log("Ejb:根据人员ID查找部门,岗位,级别,合同信息开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 参数List
			List listParams = new ArrayList();
			// 查询sql
			// 签过合同，能查到数据
			String sql = "SELECT "
					+ "A.EMP_ID, "
					+ "B.CHS_NAME, "
					+ "A.DEPT_ID, "
					+ "C.DEPT_NAME AS DEPT, "
					+ "A.STATION_ID, "
					+ "D.STATION_NAME, "
					+ "A.WORKCONTRACTID, "
					+ "A.WROK_CONTRACT_NO, "
					+ "A.FRIST_DEP_ID, "
					+ "E.DEPT_NAME AS FIRSTDEPT, "
					+ "A.FRIST_ADDREST, "
					+ "A.CONTRACT_TERM_ID, "
					+ "to_char(A.WORK_SIGN_DATE,'yyyy-mm-dd') , "
					+ "to_char(A.START_DATE,'yyyy-mm-dd'), "
					+ "to_char(A.END_DATE,'yyyy-mm-dd'), "
					+ "A.IF_EXECUTE, "
					+ "A.CONTRACT_CONTINUE_MARK, "
					+ "A.MEMO, "
					+ "A.INSERTBY, "
					+ "A.INSERTDATE, "
					+ "A.ENTERPRISE_CODE, "
					+ "A.IS_USE, "
					+ "A.LAST_MODIFIED_BY, "
//					+ " to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd') AS lastModifiedDate, \n"
					//update by sychen 20100707
					+ " to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS lastModifiedDate, \n"
					// add by ywliu 20100611
					+ "A.OWNER, "
					+ "A.SIGNED_INSTITUTIONS, "
					+ "A.CONTRACT_PERIOD, "
					+ "A.LABOR_TYPE, "
					+ "A.CONTRACT_TYPE "
					+ " ,F.CONTRACT_TERMINATED_CODE "//add by sychen 20100713
					// end
					+ "FROM "
					+ "HR_J_WORKCONTRACT A "
					+ "LEFT JOIN HR_C_DEPT C ON A.DEPT_ID = C.DEPT_ID AND C.IS_USE=? AND C.ENTERPRISE_CODE=?   "
					+ "LEFT JOIN HR_J_EMP_INFO B ON A.EMP_ID= B.EMP_ID AND A.IF_EXECUTE=? AND A.IS_USE=? AND A.ENTERPRISE_CODE=?  "
					+ "LEFT JOIN HR_C_STATION D ON D.STATION_ID=A.STATION_ID    "
					+ "LEFT JOIN HR_C_DEPT E ON A.FRIST_DEP_ID = E.DEPT_ID AND E.IS_USE=? AND E.ENTERPRISE_CODE=?   "
					+ "LEFT JOIN hr_j_contractstop F ON F.WORKCONTRACTID=A.WORKCONTRACTID "//add by sychen 20100713
					+ "WHERE "
					// + "B.IS_USE=? AND "
					+ "B.ENTERPRISE_CODE=? AND " + "B.EMP_ID = ? ";
			// 没签过合同，没有数据，则从人员基本信息表里面读
			String sql1 = "SELECT "
					+ "B.EMP_ID, "
					+ "B.CHS_NAME, "
					+ "B.DEPT_ID, "
					+ "C.DEPT_NAME AS DEPT, "
					+ "B.STATION_ID, "
					+ "D.STATION_NAME, "
					+ "A.WORKCONTRACTID, "
					+ "A.WROK_CONTRACT_NO, "
					+ "A.FRIST_DEP_ID, "
					+ "E.DEPT_NAME AS FIRSTDEPT, "
					+ "A.FRIST_ADDREST, "
					+ "A.CONTRACT_TERM_ID, "
					+ "to_char(A.WORK_SIGN_DATE,'yyyy-mm-dd') , "
					+ "to_char(A.START_DATE,'yyyy-mm-dd'), "
					+ "to_char(A.END_DATE,'yyyy-mm-dd'), "
					+ "A.IF_EXECUTE, "
					+ "A.CONTRACT_CONTINUE_MARK, "
					+ "A.MEMO, "
					+ "A.INSERTBY, "
					+ "A.INSERTDATE, "
					+ "A.ENTERPRISE_CODE, "
					+ "A.IS_USE, "
					+ "A.LAST_MODIFIED_BY, "
					+ " to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS lastModifiedDate, \n"
					// add by ywliu 20100611
					+ "A.OWNER, "
					+ "A.SIGNED_INSTITUTIONS, "
					+ "A.CONTRACT_PERIOD, "
					+ "A.LABOR_TYPE, "
					+ "A.CONTRACT_TYPE "
					+ " ,F.CONTRACT_TERMINATED_CODE "//add by sychen 20100713
					// end
					+ "FROM "
					+ "HR_J_EMP_INFO B "
					+ "LEFT JOIN HR_C_DEPT C ON B.DEPT_ID = C.DEPT_ID AND C.IS_USE=? AND C.ENTERPRISE_CODE=?   "
					+ "LEFT JOIN HR_J_WORKCONTRACT A ON A.EMP_ID= B.EMP_ID AND A.IF_EXECUTE=? AND A.IS_USE=? AND A.ENTERPRISE_CODE=?  "
					+ "LEFT JOIN HR_C_STATION D ON D.STATION_ID=B.STATION_ID   "
					+ "LEFT JOIN HR_C_DEPT E ON A.FRIST_DEP_ID = E.DEPT_ID AND E.IS_USE=? AND E.ENTERPRISE_CODE=?   "
					+ "LEFT JOIN hr_j_contractstop F ON F.WORKCONTRACTID=A.WORKCONTRACTID "//add by sychen 20100713
					+ "WHERE "
					// + "B.IS_USE=? AND "
					+ "B.ENTERPRISE_CODE=? AND " + "B.EMP_ID = ? ";
			listParams.add('Y');//update by sychen 20100901
//			listParams.add('U');
			listParams.add(enterpriseCode);
			listParams.add(IF_EXECUTE_1);
			listParams.add('Y');
			listParams.add(enterpriseCode);
			// listParams.add(enterpriseCode);
			 listParams.add('Y');//update by sychen 20100901
//			listParams.add('U');
			listParams.add(enterpriseCode);
			// listParams.add('Y');
			listParams.add(enterpriseCode);
			listParams.add(empId);
			// 打印sql文
			LogUtil.log("sql 文：" + sql, Level.INFO, null);
			Object[] params = listParams.toArray();
			// list
			List list = bll.queryByNativeSQL(sql, params);
			if (null == list || 0 == list.size()) {
				list = bll.queryByNativeSQL(sql1, params);
			}
			List<WorkContract> arrList = new ArrayList<WorkContract>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				WorkContract info = new WorkContract();
				// 人员id
				if (data[0] != null && !data[0].toString().equals(BLANK)) {
					info.setEmpId(Long.parseLong(data[0].toString()));
				}
				// 人员code
				if (data[1] != null && !data[1].toString().equals(BLANK)) {
					info.setEmpName(data[1].toString());
				}
				// 部门id
				if (data[2] != null && !data[2].toString().equals(BLANK)) {
					info.setDeptId(Long.parseLong(data[2].toString()));
				}
				// 部门名称
				if (data[3] != null && !data[3].toString().equals(BLANK)) {
					info.setDeptName(data[3].toString());
				}
				// 岗位id
				if (data[4] != null && !data[4].toString().equals(BLANK)) {
					info.setStationId(Long.parseLong(data[4].toString()));
				}
				// 岗位名称
				if (data[5] != null && !data[5].toString().equals(BLANK)) {
					info.setStationName(data[5].toString());
				}
				// 合同ID
				if (data[6] != null && !data[6].toString().equals(BLANK)) {
					info.setWorkcontractid(Long.parseLong(data[6].toString()));
				}
				// 合同编号
				if (data[7] != null && !data[7].toString().equals(BLANK)) {
					info.setWrokContractNo(data[7].toString());
				}
				// 甲方部门id
				if (data[8] != null && !data[8].toString().equals(BLANK)) {
					info.setFristDepId(Long.parseLong(data[8].toString()));
				}
				// 甲方部门
				if (data[9] != null && !data[9].toString().equals(BLANK)) {
					info.setFristDepName(data[9].toString());
				}
				// 甲方地址
				if (data[10] != null && !data[10].toString().equals(BLANK)) {
					info.setFristAddrest(data[10].toString());
				}
				// 有效期id
				if (data[11] != null && !data[11].toString().equals(BLANK)) {
					info.setContractTermId(Long.parseLong(data[11].toString()));
				}
				// 签约日期
				if (data[12] != null && !data[12].toString().equals(BLANK)) {
					info.setWorkSignDate(data[12].toString());
				}
				// 开始日期
				if (data[13] != null && !data[13].toString().equals(BLANK)) {
					info.setStartDate(data[13].toString());
				}
				// 结束日期
				if (data[14] != null && !data[14].toString().equals(BLANK)) {
					info.setEndDate(data[14].toString());
				}
				// 是否劳动合同正在执行
				if (data[15] != null && !data[15].toString().equals(BLANK)) {
					info.setIfExecute(data[15].toString());
				}
				// 是否续签
				if (data[16] != null && !data[16].toString().equals(BLANK)) {
					info.setContractContinueMark(data[16].toString());
				}
				// 备注
				if (data[17] != null && !data[17].toString().equals(BLANK)) {
					info.setMemo(data[17].toString());
				}
				// 插入人
				if (data[18] != null && !data[18].toString().equals(BLANK)) {
					info.setInsertby(data[18].toString());
				}
				// 插入时间
				if (data[19] != null && !data[19].toString().equals(BLANK)) {
					info.setInsertdate(data[19].toString());
				}
				// 企业编号
				if (data[20] != null && !data[20].toString().equals(BLANK)) {
					info.setEnterpriseCode(data[20].toString());
				}
				// 是否使用
				if (data[21] != null && !data[21].toString().equals(BLANK)) {
					info.setIsUse(data[21].toString());
				}
				// 最后修改人
				if (data[22] != null && !data[22].toString().equals(BLANK)) {
					info.setLastModifiedBy(data[22].toString());
				}
				// 最后修改时间
				if (data[23] != null && !data[23].toString().equals(BLANK)) {
					SimpleDateFormat sdfFrom = new SimpleDateFormat(
							DATE_FORMAT_YYYYMMDD_HHMMSS);
					Date dtNowTime = sdfFrom.parse(data[23].toString());
					info.setLastModifiedDate(dtNowTime);
				}
				// 甲方
				if (data[24] != null && !data[24].toString().equals(BLANK)) {
					info.setOwner(data[24].toString());
				}
				// 签订机构
				if (data[25] != null && !data[25].toString().equals(BLANK)) {
					info.setSignedInstitutions(data[25].toString());
				}
				// 合同期限
				if (data[26] != null && !data[26].toString().equals(BLANK)) {
					info.setContractPeriod(data[26].toString());
				}
				// 用工形式
				if (data[27] != null && !data[27].toString().equals(BLANK)) {
					info.setLaborType(data[27].toString());
				}
				// 合同类别
				if (data[28] != null && !data[28].toString().equals(BLANK)) {
					info.setContractType(data[28].toString());
				}
				// 合同解除文号 add by sychen 20100714
				if (data[29] != null && !data[29].toString().equals(BLANK)) {
					info.setContractTerminatedCode(data[29].toString());
				}
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount((long) (arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:根据人员ID查找部门,岗位,级别,合同信息结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log(" Ejb:根据人员ID查找部门,岗位,级别,合同信息失败", Level.SEVERE, null);
			throw e;
		} catch (ParseException e) {
			LogUtil.log(" Ejb:根据人员ID查找部门,岗位,级别,合同信息失败", Level.SEVERE, e);
			throw e;
		}
	}

	public void saveBat(List<HrJWorkcontract> list) throws DataChangeException {
		LogUtil.log("EJB:批量新增劳动合同开始。", Level.INFO, null);
		try {
			List<Long> existEmpId = new ArrayList<Long>();
			Long maxId = bll.getMaxId("HR_J_WORKCONTRACT", "WORKCONTRACTID");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setWorkcontractid(maxId + i);
				try {
					this.save(list.get(i));
				} catch (DataChangeException e) {
					existEmpId.add(list.get(i).getEmpId());

				}
			}
			if (existEmpId.size() > 0) {
				throw new DataChangeException(existEmpId.toString());
			}
			LogUtil.log("EJB:批量新增劳动合同结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:批量新增劳动合同错误。", Level.SEVERE, re);
			throw re;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:批量新增劳动合同错误。", Level.SEVERE, null);
			throw e;
		}
	}

	/**
	 * 批量查找合同信息
	 * 
	 * @param empIds
	 * @param enterpriseCode
	 * @return
	 * @throws ParseException
	 */
	public PageObject getWorkContractInfos(List<Long> empIds,
			String enterpriseCode) throws ParseException {
		LogUtil.log("Ejb:根据人员ID,批量查找部门,岗位,级别,合同信息开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			List<WorkContract> arrList = new ArrayList<WorkContract>();
			for (int i = 0; i < empIds.size(); i++) {
				arrList.add((WorkContract) this.getWorkContractInfo(
						empIds.get(i), enterpriseCode).getList().get(0));
			}
			pobj.setList(arrList);
			pobj.setTotalCount((long) arrList.size());
			LogUtil.log("Ejb:根据人员ID,批量查找部门,岗位,级别,合同信息结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log(" Ejb:根据人员ID,批量查找部门,岗位,级别,合同信息失败", Level.SEVERE, null);
			throw e;
		} catch (ParseException e) {
			LogUtil.log(" Ejb:根据人员ID,批量查找部门,岗位,级别,合同信息失败", Level.SEVERE, null);
			throw e;
		}
	}

	/**
	 * 按员工Id查询
	 */
	@SuppressWarnings("unchecked")
	public HrJWorkcontract findByEmpId(Long empId, Long contractId) {
		LogUtil.log("EJB:按员工ID查询合同开始", Level.INFO, null);
		try {
			HrJWorkcontract instance;
			String sql = "SELECT * FROM HR_J_WORKCONTRACT WHERE EMP_ID = ? and WORKCONTRACTID = ?";
			Object[] params = new Object[2];
			params[0] = empId;
			params[1] = contractId;
			List<HrJWorkcontract> list = bll.queryByNativeSQL(sql, params,
					HrJWorkcontract.class);
			if (null != list && list.size() > 0) {
				instance = (HrJWorkcontract) list.get(0);
			} else {
				return null;
			}
			LogUtil.log("EJB:按员工ID查询合同结束", Level.INFO, null);
			return instance;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:按员工ID查询合同错误", Level.SEVERE, e);
			throw e;
		}

	}
}