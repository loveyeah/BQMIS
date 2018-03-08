package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

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

/**
 * Facade for entity HrJEmployeeborrowIn.
 * 
 * @see power.ejb.hr.HrJEmployeeborrowIn
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJEmployeeborrowInFacade implements
		HrJEmployeeborrowInFacadeRemote {
	// property constants
	public static final String EMP_ID = "empId";
	public static final String IF_BACK = "ifBack";
	public static final String MEMO = "memo";
	public static final String BORROW_DEPT_ID = "borrowDeptId";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String DCM_STATUS = "dcmStatus";
	public static final String IS_USE_Y = "Y";
	public static final String IS_USE_N = "N";
	public static final String IF_BACK_Y = "1";
	public static final String IF_BACK_N = "0";
	public static final String BLANK_STRING = "";
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
    /** 日期格式 */
	private static final String DATE_FORMAT_YYYYMMDD="yyyy-MM-dd";
	/** 批处理增加成功*/
    private static String STR_BATCH_ADD_SUCCESS =  "success";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	/**
	 * Perform an initial save of a previously unsaved HrJEmployeeborrowIn
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrJEmployeeborrowIn entity to persist
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEmployeeborrowIn entity) throws SQLException {
		LogUtil.log("Ejb:保存员工借调登记开始", Level.INFO, null);
		try {
			// 更新时间
			entity.setHrJEmployeeborrowInId(bll.getMaxId("HR_J_EMPLOYEEBORROW_IN", "HR_J_EMPLOYEEBORROW_IN_ID"));
			entity.setLastModifiedDate(new Date());
			entity.setIsUse(IS_USE_Y);
			entityManager.persist(entity);
			LogUtil.log("Ejb:保存员工借调登记结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:保存员工借调登记失败", Level.INFO, null);
			throw new SQLException(re.getMessage());
		}
	}

	/**
	 * 批处理中增加一组数据
	 * @param empIds
	 * @param entity
	 * @return
	 * @throws SQLException 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String batchAddData(String empIds,HrJEmployeeborrowIn entity ) throws SQLException {
		LogUtil.log("Ejb:批处理员工借调登记开始", Level.INFO, null);
		try {
			String ids = BLANK_STRING;
			if(null != empIds && !BLANK_STRING.equals(empIds)) {
				String[] array = empIds.split(",");
				if(array.length > 0) {
					for(int i = 0 ; i < array.length; i++) {
						Boolean flag = checkIfBackByEmpId(array[i],entity.getEnterpriseCode());
						if(flag == true) {
							if(!ids.equals(BLANK_STRING)) 
								ids += ",";
							ids += array[i];
						}
					}
				}
				if(ids.length() > 0) {
					return ids;
				}else {
					int count = 0;
					Long id = bll.getMaxId("HR_J_EMPLOYEEBORROW_IN", 
					"HR_J_EMPLOYEEBORROW_IN_ID");
					for(int i = 0 ; i < array.length; i++) {
						HrJEmployeeborrowIn bean = new HrJEmployeeborrowIn();
						bean.setBorrowDeptId(entity.getBorrowDeptId());
						bean.setIfBack(entity.getIfBack());
						bean.setDcmStatus(entity.getDcmStatus());
						bean.setStartDate(entity.getStartDate());
						bean.setEndDate(entity.getEndDate());
						bean.setEnterpriseCode(entity.getEnterpriseCode());
						bean.setLastModifiedBy(entity.getLastModifiedBy());
						bean.setEmpId(Long.parseLong(array[i]));
						bean.setHrJEmployeeborrowInId(id + count);
						bean.setLastModifiedDate(new Date());
						bean.setIsUse(IS_USE_Y);
						entityManager.persist(bean);
						count++;
					}
				}
				LogUtil.log("Ejb:批处理员工借调登记结束", Level.INFO, null);
				return STR_BATCH_ADD_SUCCESS;
			}
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:批处理员工借调登记失败", Level.INFO, null);
			throw new SQLException(re.getMessage());
		}
		return STR_BATCH_ADD_SUCCESS;
	}
	
	/**
	 *  批处理更新员工借调登记
	 * @param borrowInArray
	 * @param endDate
	 * @param workCode
	 * @throws DataChangeException 
	 * @throws DataFormatException 
	 * @throws SQLException 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void batchUpdateData(String borrowInArray,String  endDate,String workCode) throws DataFormatException, 
	DataChangeException, SQLException
	{
		LogUtil.log("Ejb:批处理更新员工借调登记开始", Level.INFO, null);
		try {
				if(null != borrowInArray && !BLANK_STRING.equals(borrowInArray)) {
					String[] array = borrowInArray.split(",");
					if(array.length > 0) {
						for(int i = 0 ; i < array.length; i++) {
							HrJEmployeeborrowIn entity = this.findById(Long.parseLong(array[i]));
								if(entity.getHrJEmployeeborrowInId() != null && 
										!entity.getHrJEmployeeborrowInId().equals(BLANK_STRING)) {
								if(null != array[i+1] && !array[i+1].equals(BLANK_STRING)){
									if (!array[i+1]
											.equals(formatDate(entity.getLastModifiedDate(), 
													DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
										throw new DataChangeException("排它处理");
									}
								}
								entity.setIfBack(IF_BACK_Y);
								entity.setEndDate(formatStringToDate(endDate,DATE_FORMAT_YYYYMMDD));
								entity.setLastModifiedBy(workCode);
								entity.setLastModifiedDate(new Date());
								entityManager.merge(entity);
							}
							i++;
						}
					}
				}
				LogUtil.log("Ejb:批处理更新员工借调登记结束", Level.INFO, null);
			} catch (RuntimeException re) {
				LogUtil.log("Ejb:批处理更新员工借调登记失败", Level.INFO, null);
				throw new SQLException(re.getMessage());
			}
		}
	/**
	 * Delete a persistent HrJEmployeeborrowIn entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrowIn entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEmployeeborrowIn entity)throws SQLException, DataChangeException {
		LogUtil.log("Ejb:删除员工借调登记开始", Level.INFO, null);
		try {	
			// 得到数据库中的这个记录
			HrJEmployeeborrowIn salary = this.findById(entity.getHrJEmployeeborrowInId());
			if(salary.getLastModifiedDate() != null && !salary.getLastModifiedDate().toString().equals("")){
				// 排他
				if (!formatDate(salary.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC)
						.equals(formatDate(entity.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
					throw new DataChangeException("排它处理");
				}
			}
				// 更新时间
				entity.setLastModifiedDate(new Date());
				entity.setIsUse(IS_USE_N);
				entityManager.merge(entity);
				LogUtil.log("Ejb:删除员工借调登记结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:删除员工借调登记失败", Level.INFO, null);
			throw new SQLException(re.getMessage());
		}
	}
	
	/**
	 * 上报员工借调登记
	 * @param entity
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public void report(HrJEmployeeborrowIn entity)throws SQLException, DataChangeException {
		LogUtil.log("Ejb:上报员工借调登记开始", Level.INFO, null);
		try {	
			// 得到数据库中的这个记录
			HrJEmployeeborrowIn salary = this.findById(entity.getHrJEmployeeborrowInId());
			if(salary.getLastModifiedDate() != null && !salary.getLastModifiedDate().toString().equals("")){
				// 排他
				if (!formatDate(salary.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC)
						.equals(formatDate(entity.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
					throw new DataChangeException("排它处理");
				}
			}
				// 更新时间
				entity.setLastModifiedDate(new Date());
				entity.setDcmStatus(IF_BACK_Y);
				entityManager.merge(entity);
				LogUtil.log("Ejb:上报员工借调登记结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:上报员工借调登记失败", Level.INFO, null);
			throw new SQLException(re.getMessage());
		}
	}
	
	/**
	 * check所选员工是否已借调出
	 * @param empId
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkIfBackByEmpId(String empId,String enterpriseCode) throws SQLException {
		LogUtil.log("Ejb:check所选员工是否已借调出开始", Level.INFO, null);
		try {
			@SuppressWarnings("unused")
			PageObject pobj = new PageObject();	
			// 参数List
        	List listParams = new ArrayList();
			// 查询sql
        	String sql = " select count(A.HR_J_EMPLOYEEBORROW_IN_ID) \n"
        		+ " FROM HR_J_EMPLOYEEBORROW_IN A \n" 
        		+ " WHERE A.IF_BACK = ? \n"
        		+ " AND A.EMP_ID = ? \n"
        		+ " AND A.ENTERPRISE_CODE = ? \n"
        		+ " AND A.IS_USE = ? ";
        	listParams.add(IF_BACK_N);
			listParams.add(empId);
			listParams.add(enterpriseCode);
			listParams.add(IS_USE_Y);
			// 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);	
			Object[] params= listParams.toArray();
			//list
			int count = Integer.parseInt(bll.getSingal(sql,params).toString());
			if(count > 0)
				return true;
			else if(count == 0)
				return false;
			LogUtil.log("Ejb:check所选员工是否已借调出结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:check所选员工是否已借调出失败", Level.INFO, null);
			throw new SQLException(re.getMessage());
		}
		return null;
	}
	/**
	 * Persist a previously saved HrJEmployeeborrowIn entity and return it or a
	 * copy of it to the sender. A copy of the HrJEmployeeborrowIn entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJEmployeeborrowIn entity to update
	 * @return HrJEmployeeborrowIn the persisted HrJEmployeeborrowIn entity
	 *         instance, may not be the same
	 * @throws SQLException 
	 * @throws DataChangeException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(HrJEmployeeborrowIn entity) throws SQLException, DataChangeException {
		LogUtil.log("Ejb:更新员工借调登记开始", Level.INFO, null);
		try {
		// 得到数据库中的这个记录
		HrJEmployeeborrowIn salary = this.findById(entity.getHrJEmployeeborrowInId());
		// 排他
		if(salary.getLastModifiedDate() != null && !salary.getLastModifiedDate().toString().equals("")){
			if (!formatDate(salary.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC)
					.equals(formatDate(entity.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
				throw new DataChangeException("排它处理");
			}
		}
		
			// 更新时间
			entity.setLastModifiedDate(new Date());
			entityManager.merge(entity);
			LogUtil.log("Ejb:更新员工借调登记结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("Ejb:更新员工借调登记失败", Level.INFO, null);
			throw new SQLException(re.getMessage());
		}
	}

	public HrJEmployeeborrowIn findById(Long id) {
		LogUtil.log("finding HrJEmployeeborrowIn instance with id: " + id,
				Level.INFO, null);
		try {
			HrJEmployeeborrowIn instance = entityManager.find(
					HrJEmployeeborrowIn.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJEmployeeborrowIn entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEmployeeborrowIn property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJEmployeeborrowIn> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEmployeeborrowIn> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJEmployeeborrowIn instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJEmployeeborrowIn model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrJEmployeeborrowIn> findByEmpId(Object empId) {
		return findByProperty(EMP_ID, empId);
	}

	public List<HrJEmployeeborrowIn> findByIfBack(Object ifBack) {
		return findByProperty(IF_BACK, ifBack);
	}

	public List<HrJEmployeeborrowIn> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<HrJEmployeeborrowIn> findByBorrowDeptId(Object borrowDeptId) {
		return findByProperty(BORROW_DEPT_ID, borrowDeptId);
	}

	public List<HrJEmployeeborrowIn> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<HrJEmployeeborrowIn> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrJEmployeeborrowIn> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<HrJEmployeeborrowIn> findByDcmStatus(Object dcmStatus) {
		return findByProperty(DCM_STATUS, dcmStatus);
	}

	/**
	 * Find all HrJEmployeeborrowIn entities.
	 * 
	 * @return List<HrJEmployeeborrowIn> all HrJEmployeeborrowIn entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEmployeeborrowIn> findAll() {
		LogUtil.log("finding all HrJEmployeeborrowIn instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from HrJEmployeeborrowIn model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
     * 根据日期和形式返回日期字符串
     * @param argDate 日期
     * @param argFormat 日期形式字符串
     * @return 日期字符串
     */
    private String formatDate(Date argDate, String argFormat) {
        if (argDate == null) {
            return "";
        }
        
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回字符串
        String strResult = null;

        try {
            sdfFrom = new SimpleDateFormat(argFormat);
            // 格式化日期
            strResult = sdfFrom.format(argDate).toString();
        } catch (Exception e) {
            strResult = "";
        } finally {
            sdfFrom = null;
        }

        return strResult;
    }

    /**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date dtresult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			dtresult = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			dtresult = null;
		} finally {
			sdfFrom = null;
		}

		return dtresult;
	}
}