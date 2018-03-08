package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.labor.HrCSsMain;

/**
 * Facade for entity HrJContractstop.
 * 
 * @see power.ejb.hr.HrJContractstop
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJContractstopFacade implements HrJContractstopFacadeRemote {
	// property constants
	public static final String WORKCONTRACTID = "workcontractid";
	public static final String CONTRACT_STOP_TYPE = "contractStopType";
	public static final String RELEASE_REASON = "releaseReason";
	public static final String CONTRACT_TERM_CODE = "contractTermCode";
	public static final String DOSSIER_DIRECTION = "dossierDirection";
	public static final String SOCIETY_INSURANCE_DIRECTION = "societyInsuranceDirection";
	public static final String MEMO = "memo";
	public static final String INSERTBY = "insertby";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String EMP_ID = "empId";
	public static final String DEPT_ID = "deptId";
	public static final String STATION_ID = "stationId";
	private String BLANK = "";
	private static final String IS_USE_Y = "Y";
	private static final String ENTERPRISE = "hfdc";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "HrJContractstopFacade")
	private HrJContractstopFacadeRemote remote1;
	@EJB(beanName = "HrJWorkcontractFacade")
	private HrJWorkcontractFacadeRemote remote2;
	public HrJContractstop  findContractstop(Long  workcontractId)//add by wpzhu 
	{
		String sql="select  * " +
				" from HR_J_CONTRACTSTOP  a" +
				" where   a.workcontractid= '"+workcontractId+"'" +
				" and a.is_use='Y'";

		List<HrJContractstop> list=bll.queryByNativeSQL(sql, HrCSsMain.class);
		if(list!=null&&list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
		
	}
	public  Long  findContractID(String workcontractCode)
	{
		String sql=
			"select a.workcontractid " +
			" from HR_J_CONTRACTSTOP  a," +
			" hr_j_workcontract b\n" +
			"where a.workcontractid=b.workcontractid\n" + 
			"and a.is_use='Y'\n" + 
			"and b.is_use='Y' " +
			" and a.contract_terminated_code='"+workcontractCode+"'";
		Object obj=bll.getSingal(sql);
		if(obj!=null)
		{
			return    Long.parseLong(obj.toString());
		}
		else {
		return null;
		}

	}
	/**
	 * Perform an initial save of a previously unsaved HrJContractstop entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJContractstop entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJContractstop entity) {
		LogUtil.log("saving HrJContractstop instance", Level.INFO, null);
		try {
			// 采番处理
			entity.setContractstopid(bll.getMaxId("HR_J_CONTRACTSTOP",
					"CONTRACTSTOPID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 劳动合同解除
	 * 
	 * @throws DataChangeException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void contractEnd(HrJContractstop entity1, HrJWorkcontract entity2)
			throws DataChangeException {
		try {
			LogUtil.log("EJB:劳动合同解除开始", Level.INFO, null);
			remote1.save(entity1);
			remote2.update(entity2);
			LogUtil.log("EJB:劳动合同解除结束", Level.INFO, null);
		} catch (DataChangeException de) {
			throw new DataChangeException(null);
		} catch (Exception e) {
			LogUtil.log("EJB:劳动合同解除失败", Level.WARNING, null);
		}

	}

	/**
	 * Delete a persistent HrJContractstop entity.
	 * 
	 * @param entity
	 *            HrJContractstop entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJContractstop entity) {
		LogUtil.log("deleting HrJContractstop instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJContractstop.class, entity
					.getContractstopid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrJContractstop entity and return it or a copy
	 * of it to the sender. A copy of the HrJContractstop entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJContractstop entity to update
	 * @return HrJContractstop the persisted HrJContractstop entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJContractstop update(HrJContractstop entity) {
		LogUtil.log("updating HrJContractstop instance", Level.INFO, null);
		try {
			HrJContractstop result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJContractstop findById(Long id) {
		LogUtil.log("finding HrJContractstop instance with id: " + id,
				Level.INFO, null);
		try {
			HrJContractstop instance = entityManager.find(
					HrJContractstop.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJContractstop entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJContractstop property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJContractstop> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJContractstop> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJContractstop instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJContractstop model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrJContractstop> findByWorkcontractid(Object workcontractid) {
		return findByProperty(WORKCONTRACTID, workcontractid);
	}

	public List<HrJContractstop> findByContractStopType(Object contractStopType) {
		return findByProperty(CONTRACT_STOP_TYPE, contractStopType);
	}

	public List<HrJContractstop> findByReleaseReason(Object releaseReason) {
		return findByProperty(RELEASE_REASON, releaseReason);
	}

	public List<HrJContractstop> findByContractTermCode(Object contractTermCode) {
		return findByProperty(CONTRACT_TERM_CODE, contractTermCode);
	}

	public List<HrJContractstop> findByDossierDirection(Object dossierDirection) {
		return findByProperty(DOSSIER_DIRECTION, dossierDirection);
	}

	public List<HrJContractstop> findBySocietyInsuranceDirection(
			Object societyInsuranceDirection) {
		return findByProperty(SOCIETY_INSURANCE_DIRECTION,
				societyInsuranceDirection);
	}

	public List<HrJContractstop> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<HrJContractstop> findByInsertby(Object insertby) {
		return findByProperty(INSERTBY, insertby);
	}

	public List<HrJContractstop> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<HrJContractstop> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrJContractstop> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<HrJContractstop> findByEmpId(Object empId) {
		return findByProperty(EMP_ID, empId);
	}

	public List<HrJContractstop> findByDeptId(Object deptId) {
		return findByProperty(DEPT_ID, deptId);
	}

	public List<HrJContractstop> findByStationId(Object stationId) {
		return findByProperty(STATION_ID, stationId);
	}

	/**
	 * Find all HrJContractstop entities.
	 * 
	 * @return List<HrJContractstop> all HrJContractstop entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJContractstop> findAll() {
		LogUtil.log("finding all HrJContractstop instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJContractstop model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 检索该合同是否终止
	 * 
	 * @params ContractId 合同Id
	 * @return List<HrJContractstop>
	 */
	@SuppressWarnings("unchecked")
	public List<HrJContractstop> findByIdIsUse(Long ContractId) {
		LogUtil.log("EJB:按合同号和是否使用查找", Level.INFO, null);
		try {
			String queryString = "select t.* from HR_J_CONTRACTSTOP t where t.WORKCONTRACTID=?";
			Object[] params = new Object[1];
			params[0] = ContractId;
			List<HrJContractstop> instance = bll.queryByNativeSQL(queryString,
					params, HrJContractstop.class);
			LogUtil.log("EJB:按合同号和是否使用结束", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:按合同号和是否使用失败", Level.SEVERE, null);
			throw re;
		}
	}

	/**
	 * 检索员工合同信息
	 * 
	 * @params empId 员工Id
	 * @return Pageobject
	 */
	public PageObject searchEmpContract(Long empId) {
		LogUtil.log("EJB:检索员工合同信息开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.WORKCONTRACTID");
			sql.append(" ,A.WROK_CONTRACT_NO");
			sql.append(",B.CONTRACT_TERM");
			sql.append(" ,C.DEPT_NAME AS DEPTNAME1");
			sql.append(",A.FRIST_ADDREST,D.CHS_NAME");
			sql.append(",A.CONTRACT_CONTINUE_MARK");
			sql.append(",F.DEPT_NAME AS DEPTNAME2");
			sql.append(",E.STATION_NAME");
			sql.append(",to_char(A.WORK_SIGN_DATE,'YYYY-mm-dd')");
			sql.append(",to_char(A.START_DATE,'YYYY-mm-dd')");
			sql.append(",to_char(A.END_DATE,'YYYY-mm-dd')");
			sql.append(",A.IF_EXECUTE");
			sql.append(",A.EMP_ID");
			sql.append(",A.DEPT_ID AS DEPTID1");
			sql.append(",A.STATION_ID AS STATIONID1");
			sql.append(",A.IS_USE ");
			sql.append(",D.STATION_ID AS STATIONID2 ");
			sql.append(",D.DEPT_ID AS DEPTID2,");
			sql.append("B.CONTRACT_TERM_ID ");
			sql.append("FROM HR_J_WORKCONTRACT A");
			sql.append(",HR_C_CONTRACTTERM B");
			sql.append(",HR_C_DEPT C");
			sql.append(" ,HR_J_EMP_INFO D");
			sql.append(" ,HR_C_STATION E");
			sql.append(",HR_C_DEPT F ");
			sql.append("WHERE A.CONTRACT_TERM_ID= B.CONTRACT_TERM_ID AND ");
			sql.append("A.FRIST_DEP_ID= C.DEPT_ID ");
			sql.append("AND A.EMP_ID= D.EMP_ID ");
			sql.append("AND A.DEPT_ID = F.DEPT_ID ");
			sql.append("AND A.STATION_ID= E.STATION_ID ");
			sql.append("AND B.IS_USE = ? ");
			sql.append("AND B.ENTERPRISE_CODE= ? ");
			sql.append("AND C.IS_USE = ? ");
			sql.append("AND C.ENTERPRISE_CODE= ? ");
//			sql.append("AND D.IS_USE = ? ");
			sql.append("AND D.ENTERPRISE_CODE= ? ");
//			sql.append("AND E.ENTERPRISE_CODE= ? ");
			sql.append("AND F.IS_USE = ? ");
			sql.append("AND F.ENTERPRISE_CODE= ? ");
			sql.append("AND A.ENTERPRISE_CODE= ? ");
			sql.append("AND A.WORKCONTRACTID =(");
			sql.append("SELECT MAX(HR_J_WORKCONTRACT.WORKCONTRACTID) FROM HR_J_WORKCONTRACT WHERE HR_J_WORKCONTRACT.EMP_ID = ?)");
//			Object[] params = new Object[11];
			Object[] params = new Object[9];
			params[0] = IS_USE_Y;
			params[1] = ENTERPRISE;
			params[2] = "Y";//update by sychen 20100901
//			params[2] = "U";
			params[3] = ENTERPRISE;
//			params[4] = IS_USE_Y;
			params[4] = ENTERPRISE;
//			params[6] = ENTERPRISE;
			params[5] = "Y";//update by sychen 20100901
//			params[5] = "U";
			params[6] = ENTERPRISE;
			params[7] = ENTERPRISE;
			params[8] = empId;
			List list = bll.queryByNativeSQL(sql.toString(), params);
			List<WorkContractEnd> arrList = new ArrayList<WorkContractEnd>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				WorkContractEnd info = new WorkContractEnd();
				// 劳动合同签订ID
				if (data[0] != null && !data[0].toString().equals(BLANK)) {
					info.setWorkcontractid(Long.parseLong(data[0].toString()));
				}
				// 劳动合同编号
				if (data[1] != null && !data[1].toString().equals(BLANK)) {
					info.setWorkContractNo(data[1].toString());
				}
				// 劳动合同有效期
				if (data[2] != null && !data[2].toString().equals(BLANK)) {
					info.setContractTerm(data[2].toString());
				}
				// 甲方部门
				if (data[3] != null && !data[3].toString().equals(BLANK)) {
					info.setFristDept(data[3].toString());
				}
				// 甲方地址
				if (data[4] != null && !data[4].toString().equals(BLANK)) {
					info.setFristAddrest(data[4].toString());
				}
				// 中文名
				if (data[5] != null && !data[5].toString().equals(BLANK)) {
					info.setChsName(data[5].toString());
				}
				// 劳动合同续签标志
				if (data[6] != null && !data[6].toString().equals(BLANK)) {
					info.setContractContinueMark(data[6].toString());
				}
				// 所属部门
				if (data[7] != null && !data[7].toString().equals(BLANK)) {
					info.setSuoDept(data[7].toString());
				}
				// 岗位名称
				if (data[8] != null && !data[8].toString().equals(BLANK)) {
					info.setStationName(data[8].toString());
				}
				// 劳动合同签字日期
				if (data[9] != null && !data[9].toString().equals(BLANK)) {
					info.setSignDate(data[9].toString());
				}
				// 开始日期
				if (data[10] != null && !data[10].toString().equals(BLANK)) {
					info.setStartDate(data[10].toString());
				}
				// 结束日期
				if (data[11] != null && !data[11].toString().equals(BLANK)) {
					info.setEndDate(data[11].toString());
				}
				// 是否劳动合同正在执行
				if (data[12] != null && !data[12].toString().equals(BLANK)) {
					info.setIfExecute(data[12].toString());
				}
				// 人员ID
				if (data[13] != null && !data[13].toString().equals(BLANK)) {
					info.setEmpId(Long.parseLong(data[13].toString()));
				}
				// 部门ID
				if (data[14] != null && !data[14].toString().equals(BLANK)) {
					info.setDeptId(Long.parseLong(data[14].toString()));
				}
				// 岗位ID
				if (data[15] != null && !data[15].toString().equals(BLANK)) {
					info.setStationId(Long.parseLong(data[15].toString()));
				}
				if (data[16] != null && !data[16].toString().equals(BLANK)) {
					info.setIsUse(data[16].toString());
				}
				//人员基本信息表里的岗位ID
				if (data[17] != null && !data[17].toString().equals(BLANK)) {
					info.setEmpInfoStation(Long.parseLong(data[17].toString()));
				}
				//人员基本信息表里的部门ID
				if (data[18] != null && !data[18].toString().equals(BLANK)) {
					info.setEmpInfoDept(Long.parseLong(data[18].toString()));
				}
				if (data[19] != null && !data[19].toString().equals(BLANK)) {
					info.setContractTermId(Long.parseLong(data[19].toString()));
				}
				
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount((long) (arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("EJB:检索员工合同信息结束", Level.INFO, null);
			return pobj;
		} catch (Exception e) {
			LogUtil.log("EJB:检索员工合同信息失败", Level.INFO, null);
			return null;
		}
	}
}