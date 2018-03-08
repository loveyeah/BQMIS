package power.ejb.hr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCAppellation.
 *
 * @see power.ejb.hr.HrCAppellation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCAppellationFacade implements HrCAppellationFacadeRemote {
	// property constants
	public static final String CALLS_NAME = "callsName";
	public static final String RETRIEVE_CODE = "retrieveCode";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String INSERTBY = "insertby";
	public static final String ORDER_BY = "orderBy";
	public static final String IS_USE_Y = "Y";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved HrCAppellation entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCAppellation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCAppellation entity) {
		LogUtil.log("saving HrCAppellation instance", Level.INFO, null);
		try {
			entity.setCallsCodeId(bll.getMaxId("HR_C_APPELLATION", "CALLS_CODE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCAppellation entity.
	 *
	 * @param entity
	 *            HrCAppellation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCAppellation entity) {
		LogUtil.log("deleting HrCAppellation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCAppellation.class, entity
					.getCallsCodeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCAppellation entity and return it or a copy
	 * of it to the sender. A copy of the HrCAppellation entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 *
	 * @param entity
	 *            HrCAppellation entity to update
	 * @return HrCAppellation the persisted HrCAppellation entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCAppellation update(HrCAppellation entity) {
		LogUtil.log("updating HrCAppellation instance", Level.INFO, null);
		try {
			HrCAppellation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCAppellation findById(Long id) {
		LogUtil.log("finding HrCAppellation instance with id: " + id,
				Level.INFO, null);
		try {
			HrCAppellation instance = entityManager.find(HrCAppellation.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCAppellation entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCAppellation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCAppellation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCAppellation> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCAppellation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCAppellation model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCAppellation> findByCallsName(Object callsName) {
		return findByProperty(CALLS_NAME, callsName);
	}

	public List<HrCAppellation> findByRetrieveCode(Object retrieveCode) {
		return findByProperty(RETRIEVE_CODE, retrieveCode);
	}

	public List<HrCAppellation> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<HrCAppellation> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrCAppellation> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<HrCAppellation> findByInsertby(Object insertby) {
		return findByProperty(INSERTBY, insertby);
	}

	public List<HrCAppellation> findByOrderBy(Object orderBy) {
		return findByProperty(ORDER_BY, orderBy);
	}

	/**
	 * Find all HrCAppellation entities.
	 *
	 * @return List<HrCAppellation> all HrCAppellation entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCAppellation> findAll() {
		LogUtil.log("finding all HrCAppellation instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCAppellation model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查找所有称谓
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllAppellations(String enterpriseCode){
		try{
			String sql = "SELECT L.CALLS_CODE_ID,L.CALLS_NAME FROM HR_C_APPELLATION L \n" +
					" WHERE L.IS_USE = 'Y'  AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有称谓id和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql);
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有称谓id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有称谓id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	
	/** **********员工档案 基础表维护专用*开始**************** */
	/**
	 * 基础表维护（员工档案）查询
	 * 
	 * @param tableName
	 *            查询表的名字
	 * @param argEnterpriseCode
	 *            企业编码
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getRecordList(String tableName, String argEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("Ejb:基础表维护（员工档案）查询开始", Level.INFO, null);
		String BLANK = "";
		String TABLE1 = "称谓编码表";
		String TABLE2 = "学位编码表";
		String TABLE3 = "学历编码表";
		String TABLE4 = "语种编码表";
		String TABLE5 = "民族编码表";
		String TABLE6 = "籍贯编码表";
		String TABLE7 = "政治面貌表";
		String TABLE8 = "学校名称表";
		String TABLE9 = "学习类别表";
		String TABLEa = "学习专业表";
		String TABLEb = "技术等级表";
		String TABLEc = "员工身份表";
		String TABLEd = "员工类别表";
		try {
			PageObject pobj = new PageObject();
			List myList;
			// 查询sql
			String sql = "";
			// 表名
			String from = "";
			// 统计列
			String distinct = "";
			// 查询称谓编码表
			if (TABLE1.equals(tableName)) {
				sql = "SELECT A.CALLS_CODE_ID, A.CALLS_NAME, ";
				from = "FROM HR_C_APPELLATION A ";
//				distinct = "A.CALLS_CODE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCAppellation>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;

				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCAppellation app = new HrCAppellation();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setCallsCodeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setCallsName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询学位编码表
			} else if (TABLE2.equals(tableName)) {
				sql = "SELECT A.DEGREE_ID, A.DEGREE_NAME, ";
				from = "FROM HR_C_DEGREE A ";
//				distinct = "A.DEGREE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCDegree>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCDegree app = new HrCDegree();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setDegreeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setDegreeName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					// modified by liuyi 091123 表中无该属性 已解决
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询学历编码表
			} else if (TABLE3.equals(tableName)) {
				sql = "SELECT A.EDUCATION_ID, A.EDUCATION_NAME, ";
				from = "FROM HR_C_EDUCATION A ";
//				distinct = "A.EDUCATION_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCEducation>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCEducation app = new HrCEducation();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setEducationId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setEducationName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					// modified by liuyi 091123 表中无该属性 已解决
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询语种编码表
			} else if (TABLE4.equals(tableName)) {
				sql = "SELECT A.LANGUAGE_CODE_ID, A.LANGUAGE_NAME, ";
				from = "FROM HR_C_LANGUAGE A ";
//				distinct = "A.LANGUAGE_CODE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCLanguage>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCLanguage app = new HrCLanguage();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setLanguageCodeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setLanguageName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询民族编码表
			} else if (TABLE5.equals(tableName)) {
				sql = "SELECT A.NATION_CODE_ID, A.NATION_NAME, ";
				from = "FROM HR_C_NATION A ";
//				distinct = "A.NATION_CODE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCNation>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCNation app = new HrCNation();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setNationCodeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setNationName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询籍贯编码表
			} else if (TABLE6.equals(tableName)) {
				sql = "SELECT A.NATIVE_PLACE_ID, A.NATIVE_PLACE_NAME, ";
				from = "FROM HR_C_NATIVE_PLACE A ";
//				distinct = "A.NATIVE_PLACE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCNativePlace>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCNativePlace app = new HrCNativePlace();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setNativePlaceId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setNativePlaceName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询政治面貌表
			} else if (TABLE7.equals(tableName)) {
				sql = "SELECT A.POLITICS_ID, A.POLITICS_NAME, ";
				from = "FROM HR_C_POLITICS A ";
//				distinct = "A.POLITICS_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCPolitics>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCPolitics app = new HrCPolitics();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setPoliticsId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setPoliticsName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询学校编码表
			} else if (TABLE8.equals(tableName)) {
				sql = "SELECT A.SCHOOL_CODE_ID, A.SCHOOL_NAME, ";
				from = "FROM HR_C_SCHOOL A ";
//				distinct = "A.SCHOOL_CODE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCSchool>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCSchool app = new HrCSchool();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setSchoolCodeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setSchoolName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询学习类别表
			} else if (TABLE9.equals(tableName)) {
				sql = "SELECT A.STUDY_TYPE_CODE_ID, A.STUDY_TYPE, ";
				from = "FROM HR_C_STUDYTYPE A ";
//				distinct = "A.STUDY_TYPE_CODE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCStudytype>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCStudytype app = new HrCStudytype();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setStudyTypeCodeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setStudyType(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询学习专业表
			} else if (TABLEa.equals(tableName)) {
				sql = "SELECT A.SPECIALTY_CODE_ID, A.SPECIALTY_NAME, ";
				from = "FROM HR_C_SPECIALTY A ";
//				distinct = "A.SPECIALTY_CODE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCSpecialty>();
				sql += "A.RETRIEVE_CODE ,A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCSpecialty app = new HrCSpecialty();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setSpecialtyCodeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setSpecialtyName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询技术等级表
			} else if (TABLEb.equals(tableName)) {
				sql = "SELECT A.TECHNOLOGY_GRADE_ID, A.TECHNOLOGY_GRADE_NAME, ";
				from =  "FROM HR_C_TECHNOLOGY_GRADE A ";
//				distinct = "A.TECHNOLOGY_GRADE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCTechnologyGrade>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCTechnologyGrade app = new HrCTechnologyGrade();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setTechnologyGradeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setTechnologyGradeName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					// modified by liuyi 091123 表中无该属性 已解决
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询员工身份表
			} else if (TABLEc.equals(tableName)) {
				sql = "SELECT A.WORK_ID, A.WORK_NAME, ";
				from = "FROM HR_C_WORKID A ";
//				distinct = "A.WORK_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCWorkid>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCWorkid app = new HrCWorkid();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setWorkId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setWorkName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
				// 查询员工类别表
			} else if (TABLEd.equals(tableName)) {
				sql = "SELECT A.EMP_TYPE_ID, A.EMP_TYPE_NAME, ";
				from = "FROM HR_C_EMP_TYPE A ";
//				distinct = "A.EMP_TYPE_ID";
				// modified by liuyi 091201 按显示顺序排序
				distinct = "A.order_by";
				myList = new ArrayList<HrCEmpType>();
				sql += "A.RETRIEVE_CODE, " + "A.ORDER_BY "
				+ from + "WHERE A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE = ? "
				+ "ORDER BY " + distinct;
				List list = bll.queryByNativeSQL(sql,
						new Object[] { IS_USE_Y, argEnterpriseCode }, rowStartIdxAndCount);
	
				
				
				Iterator it = list.iterator();
	
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					HrCEmpType app = new HrCEmpType();
					if (data[0] != null && !data[0].toString().equals(BLANK)) {
						app.setEmpTypeId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null && !data[1].toString().equals(BLANK)) {
						app.setEmpTypeName(data[1].toString());
					}
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						app.setRetrieveCode(data[2].toString());
					}
					// modified by liuyi 091123 表中无该属性  已解决
					if (data[3] != null && !data[3].toString().equals(BLANK)) {
						app.setOrderBy(Long.parseLong(data[3].toString()));
					}
					myList.add(app);
				}
				pobj.setList(myList);
			}

			sql = sql.replaceFirst("SELECT.*? FROM ", 
					"SELECT COUNT(" + distinct + ") FROM ");
			
			Object obj = bll.getSingal(sql, new Object[] { IS_USE_Y, argEnterpriseCode });
			if (null != obj) {
				pobj.setTotalCount(Long.parseLong(obj.toString()));
			} else {
				pobj.setTotalCount(0L);
			}
			LogUtil.log("Ejb:基础表维护（员工档案）查询结束", Level.INFO, null);
			// 返回PageObject
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("Ejb:基础表维护（员工档案）查询失败", Level.INFO, null);
			throw new SQLException();
		}

	}

	public Long getCallsCodeIdByName(String name, String enterpriseCode) {
		Long id = null;
		String sql = 
			"select a.calls_code_id\n" +
			"  from hr_c_appellation a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.calls_name='"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			id = Long.parseLong(obj.toString());

		return id;
	}

	/** **********员工档案 基础表维护专用*结束**************** */
}