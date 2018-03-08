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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.Employee;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.ca.EmpFollowBaseInfo;
import power.ejb.hr.form.EmpInfo;
import power.ejb.hr.form.EmployeeInfo;
/**
 * 人员基本信息维护
 *
 * @author ghzhou  ; modify by wzhyan
 */
@Stateless
public class HrJEmpInfoFacade implements HrJEmpInfoFacadeRemote {
	
	// add by liuyi 090914
	private static final Object IS_USE = "Y";
	// property constants
	public static final String CHS_NAME = "chsName";
	public static final String EN_NAME = "enName";
	public static final String EMP_CODE = "empCode";
	public static final String DEPT_ID = "deptId";
	public static final String RETRIEVE_CODE = "retrieveCode";
	public static final String NATION_ID = "nationId";
	public static final String NATIVE_PLACE_ID = "nativePlaceId";
	public static final String POLITICS_ID = "politicsId";
	public static final String SEX = "sex";
	public static final String IS_WEDDED = "isWedded";
	public static final String ARCHIVES_ID = "archivesId";
	public static final String EMP_STATION_ID = "empStationId";
	public static final String STATION_ID = "stationId";
	public static final String STATION_LEVEL = "stationLevel";
	public static final String GRADATION = "gradation";
	public static final String EMP_TYPE_ID = "empTypeId";
	public static final String TECHNOLOGY_TITLES_TYPE_ID = "technologyTitlesTypeId";
	public static final String TECHNOLOGY_GRADE_ID = "technologyGradeId";
	public static final String TYPE_OF_WORK_ID = "typeOfWorkId";
	public static final String IDENTITY_CARD = "identityCard";
	public static final String TIME_CARD_ID = "timeCardId";
	public static final String PAY_CARD_ID = "payCardId";
	public static final String SOCIAL_INSURANCE_ID = "socialInsuranceId";
	public static final String MOBILE_PHONE = "mobilePhone";
	public static final String FAMILY_TEL = "familyTel";
	public static final String QQ = "qq";
	public static final String MSN = "msn";
	public static final String POSTALCODE = "postalcode";
	public static final String FAMILY_ADDRESS = "familyAddress";
	public static final String OFFICE_TEL1 = "officeTel1";
	public static final String OFFICE_TEL2 = "officeTel2";
	public static final String FAX = "fax";
	public static final String _EMAIL = "EMail";
	public static final String INSTANCY_MAN = "instancyMan";
	public static final String INSTANCY_TEL = "instancyTel";
	public static final String GRADUATE_SCHOOL = "graduateSchool";
	public static final String EDUCATION_ID = "educationId";
	public static final String DEGREE_ID = "degreeId";
	public static final String SPECIALITY = "speciality";
	public static final String IS_VETERAN = "isVeteran";
	public static final String IS_BORROW = "isBorrow";
	public static final String IS_RETIRED = "isRetired";
	public static final String EMP_STATE = "empState";
	public static final String ORDER_BY = "orderBy";
	public static final String RECOMMEND_MAN = "recommendMan";
	public static final String ASSISTANT_MANAGER_UNITS_ID = "assistantManagerUnitsId";
	public static final String ONE_STRONG_SUIT = "oneStrongSuit";
	public static final String MEMO = "memo";
	public static final String CREATE_BY = "createBy";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";
	public static final String CURRICULUM_VITAE = "curriculumVitae";
	public static final String SOCIETY_INFO = "societyInfo";
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "HrJWorkresumeFacade")
	protected HrJWorkresumeFacadeRemote remote;
	@PersistenceContext
	private EntityManager entityManager; 
	public PageObject  getChairmanList(String empName)
	{
		PageObject result=new PageObject();
		String sql=
			"select " +
			" emp_id ," +
			" a.emp_code ," +
			" a.chs_name," +
			" a.dept_id ," +
			//" b.dept_name " +
			" nvl( GETDEPTNAME(GETFirstLevelBYID(a.dept_id)),b.dept_name) \n "+
			"from HR_J_EMP_INFO  a,hr_c_dept  b\n" +
			"where  a.dept_id=b.dept_id\n" + 
			"and a.is_chairman='Y'\n" + 
			"and a.is_use='Y'\n" + 
			"and b.is_use='Y'";//update by sychen 20100831
//		    "and b.is_use='U'";
		if(empName!=null&&!empName.equals(""))
		{
			sql+="and ( a.chs_name   || a.en_name)  like '%"+empName+"%' ";
		}
//		System.out.println("the query sql"+sql);
	List list=bll.queryByNativeSQL(sql);
	result.setList(list);
		return result;
		
	}
	public void saveUnionPersideng(String modifyids)
	{
		String sql="update HR_J_EMP_INFO  a " +
				"set a.is_chairman ='Y'" +
				"where a.emp_id in ("+modifyids+")";
		System.out.println("the save sql"+sql);
		bll.exeNativeSQL(sql);
	 
	}
	public void delUnionPersident(String ids)
	{
		String sql="update HR_J_EMP_INFO  a " +
				"  set a.is_chairman='N'" +
				"where a.emp_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}
	public void save(HrJEmpInfo entity) { 
		try {
			if(entity.getEmpId() == null){
				entity.setEmpId(bll.getMaxId("HR_J_EMP_INFO", "EMP_ID"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
 
	public void delete(HrJEmpInfo entity) { 
		try {
			entity = entityManager.getReference(HrJEmpInfo.class, entity
					.getEmpId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public HrJEmpInfo update(HrJEmpInfo entity) { 
		try {
			HrJEmpInfo result = entityManager.merge(entity);
			// 将权限表中的login_code 保存为新工号
			String sql = " update sys_c_ul b set  b.login_code = '"+entity.getNewEmpCode()
			+"' where b.worker_id='"+entity.getEmpId()+"'  and b.worker_code='"+entity.getEmpCode()+"' \n";
			bll.exeNativeSQL(sql);
			
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJEmpInfo findById(Long id) {  
		try {
			HrJEmpInfo instance = entityManager.find(HrJEmpInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
 
	@SuppressWarnings("unchecked")
	public List<HrJEmpInfo> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrJEmpInfo instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJEmpInfo model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

//	public List<HrJEmpInfo> findByChsName(Object chsName,
//			int... rowStartIdxAndCount) {
//		return findByProperty(CHS_NAME, chsName, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByEnName(Object enName,
//			int... rowStartIdxAndCount) {
//		return findByProperty(EN_NAME, enName, rowStartIdxAndCount);
//	}
//
	public List<HrJEmpInfo> findByEmpCode(Object empCode,
			int... rowStartIdxAndCount) {
		return findByProperty(EMP_CODE, empCode, rowStartIdxAndCount);
	} 
	public List<HrJEmpInfo> findByDeptId(Object deptId,
			int... rowStartIdxAndCount) {
		return findByProperty(DEPT_ID, deptId, rowStartIdxAndCount);
	}
//
//	public List<HrJEmpInfo> findByRetrieveCode(Object retrieveCode,
//			int... rowStartIdxAndCount) {
//		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByNationId(Object nationId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(NATION_ID, nationId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByNativePlaceId(Object nativePlaceId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(NATIVE_PLACE_ID, nativePlaceId,
//				rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByPoliticsId(Object politicsId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(POLITICS_ID, politicsId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findBySex(Object sex, int... rowStartIdxAndCount) {
//		return findByProperty(SEX, sex, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByIsWedded(Object isWedded,
//			int... rowStartIdxAndCount) {
//		return findByProperty(IS_WEDDED, isWedded, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByArchivesId(Object archivesId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(ARCHIVES_ID, archivesId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByEmpStationId(Object empStationId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(EMP_STATION_ID, empStationId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByStationId(Object stationId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(STATION_ID, stationId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByStationLevel(Object stationLevel,
//			int... rowStartIdxAndCount) {
//		return findByProperty(STATION_LEVEL, stationLevel, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByGradation(Object gradation,
//			int... rowStartIdxAndCount) {
//		return findByProperty(GRADATION, gradation, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByEmpTypeId(Object empTypeId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(EMP_TYPE_ID, empTypeId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByTechnologyTitlesTypeId(
//			Object technologyTitlesTypeId, int... rowStartIdxAndCount) {
//		return findByProperty(TECHNOLOGY_TITLES_TYPE_ID,
//				technologyTitlesTypeId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByTechnologyGradeId(Object technologyGradeId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(TECHNOLOGY_GRADE_ID, technologyGradeId,
//				rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByTypeOfWorkId(Object typeOfWorkId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(TYPE_OF_WORK_ID, typeOfWorkId,
//				rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByIdentityCard(Object identityCard,
//			int... rowStartIdxAndCount) {
//		return findByProperty(IDENTITY_CARD, identityCard, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByTimeCardId(Object timeCardId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(TIME_CARD_ID, timeCardId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByPayCardId(Object payCardId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(PAY_CARD_ID, payCardId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findBySocialInsuranceId(Object socialInsuranceId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(SOCIAL_INSURANCE_ID, socialInsuranceId,
//				rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByMobilePhone(Object mobilePhone,
//			int... rowStartIdxAndCount) {
//		return findByProperty(MOBILE_PHONE, mobilePhone, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByFamilyTel(Object familyTel,
//			int... rowStartIdxAndCount) {
//		return findByProperty(FAMILY_TEL, familyTel, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByQq(Object qq, int... rowStartIdxAndCount) {
//		return findByProperty(QQ, qq, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByMsn(Object msn, int... rowStartIdxAndCount) {
//		return findByProperty(MSN, msn, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByPostalcode(Object postalcode,
//			int... rowStartIdxAndCount) {
//		return findByProperty(POSTALCODE, postalcode, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByFamilyAddress(Object familyAddress,
//			int... rowStartIdxAndCount) {
//		return findByProperty(FAMILY_ADDRESS, familyAddress,
//				rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByOfficeTel1(Object officeTel1,
//			int... rowStartIdxAndCount) {
//		return findByProperty(OFFICE_TEL1, officeTel1, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByOfficeTel2(Object officeTel2,
//			int... rowStartIdxAndCount) {
//		return findByProperty(OFFICE_TEL2, officeTel2, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByFax(Object fax, int... rowStartIdxAndCount) {
//		return findByProperty(FAX, fax, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByEMail(Object EMail,
//			int... rowStartIdxAndCount) {
//		return findByProperty(_EMAIL, EMail, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByInstancyMan(Object instancyMan,
//			int... rowStartIdxAndCount) {
//		return findByProperty(INSTANCY_MAN, instancyMan, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByInstancyTel(Object instancyTel,
//			int... rowStartIdxAndCount) {
//		return findByProperty(INSTANCY_TEL, instancyTel, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByGraduateSchool(Object graduateSchool,
//			int... rowStartIdxAndCount) {
//		return findByProperty(GRADUATE_SCHOOL, graduateSchool,
//				rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByEducationId(Object educationId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(EDUCATION_ID, educationId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByDegreeId(Object degreeId,
//			int... rowStartIdxAndCount) {
//		return findByProperty(DEGREE_ID, degreeId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findBySpeciality(Object speciality,
//			int... rowStartIdxAndCount) {
//		return findByProperty(SPECIALITY, speciality, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByIsVeteran(Object isVeteran,
//			int... rowStartIdxAndCount) {
//		return findByProperty(IS_VETERAN, isVeteran, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByIsBorrow(Object isBorrow,
//			int... rowStartIdxAndCount) {
//		return findByProperty(IS_BORROW, isBorrow, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByIsRetired(Object isRetired,
//			int... rowStartIdxAndCount) {
//		return findByProperty(IS_RETIRED, isRetired, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByEmpState(Object empState,
//			int... rowStartIdxAndCount) {
//		return findByProperty(EMP_STATE, empState, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByOrderBy(Object orderBy,
//			int... rowStartIdxAndCount) {
//		return findByProperty(ORDER_BY, orderBy, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByRecommendMan(Object recommendMan,
//			int... rowStartIdxAndCount) {
//		return findByProperty(RECOMMEND_MAN, recommendMan, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByAssistantManagerUnitsId(
//			Object assistantManagerUnitsId, int... rowStartIdxAndCount) {
//		return findByProperty(ASSISTANT_MANAGER_UNITS_ID,
//				assistantManagerUnitsId, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByOneStrongSuit(Object oneStrongSuit,
//			int... rowStartIdxAndCount) {
//		return findByProperty(ONE_STRONG_SUIT, oneStrongSuit,
//				rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByMemo(Object memo, int... rowStartIdxAndCount) {
//		return findByProperty(MEMO, memo, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByCreateBy(Object createBy,
//			int... rowStartIdxAndCount) {
//		return findByProperty(CREATE_BY, createBy, rowStartIdxAndCount);
//	}
//
//	public List<HrJEmpInfo> findByLastModifiyBy(Object lastModifiyBy,
//			int... rowStartIdxAndCount) {
//		return findByProperty(LAST_MODIFIY_BY, lastModifiyBy,
//				rowStartIdxAndCount);
//	}

	/**
	 * Find all HrJEmpInfo entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrJEmpInfo> all HrJEmpInfo entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEmpInfo> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrJEmpInfo instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJEmpInfo model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public List<HrJEmpInfo> findUsersByName(String empName,int... rowStartIdxAndCount){
		final String sql="select p.* from HR_J_EMP_INFO p where p.CHS_NAME||EMP_CODE like '%"+empName+"%' and p.emp_state='U'";
		final String countsql="select count(1) from HR_J_EMP_INFO p where p.CHS_NAME||EMP_CODE like '%"+empName+"%'";
		if(Integer.parseInt(bll.getSingal(countsql).toString())>0){
			return bll.queryByNativeSQL(sql, HrJEmpInfo.class, rowStartIdxAndCount);
		}
		else
			return null;
	}
	
	public PageObject queryByFuzzy(String enterpriseCode,String deptId,String fuzzy,int ...rowStartIdxAndCount)
	{
		try
		{
			PageObject result = new PageObject();
			String sql = 
					"select * from (select * from hr_j_emp_info t where t.emp_state = 'U' and  t.dept_id = '"+deptId+"') a\n" +
					"where a.chs_name like '%"+fuzzy + "%' or a.emp_code like '%"+ fuzzy +"%'";

//			List<HrJEmpInfo> list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<HrJEmpInfo> list = bll.queryByNativeSQL(sql, HrJEmpInfo.class,rowStartIdxAndCount);
			String sqlCount = "select count(*) from (select * from hr_j_emp_info t where t.emp_state = 'U' and t.dept_id = '"+deptId+"') a\n" +
				"where a.chs_name like '%"+fuzzy + "%' or a.emp_code like '%"+ fuzzy +"%'";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result; 
		}
		catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public List getListByDeptCode(String enterpriseCode,String deptCode)
	{
		try
		{
			String sql = "select t.* from hr_j_emp_info t where  t.dept_id =\n" +
			"(select d.dept_id from hr_c_dept d where d.dept_code = '" + deptCode+"')";
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
			.getInstance().getFacadeRemote("NativeSqlHelper");
			List<HrJEmpInfo> list=bll.queryByNativeSQL(sql,HrJEmpInfo.class);
			return list;
		}
		catch(RuntimeException re){
			throw re;
		}
	}
	
	public PageObject queryByDeptCode(String enterpriseCode, String deptCode, int... rowStartIdxAndCount)
	{
		try
		{
			PageObject result = new PageObject();
			String sql = 
				"select t.* from hr_j_emp_info t where t.dept_id =\n" +
				"(select d.dept_id from hr_c_dept d where d.dept_code = '" + deptCode+"')";


//			List<HrJEmpInfo> list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<HrJEmpInfo> list = bll.queryByNativeSQL(sql, HrJEmpInfo.class,rowStartIdxAndCount);
			String sqlCount = "select count(1) from hr_j_emp_info t where t.dept_id =\n" +
							  "(select d.dept_id from hr_c_dept d where d.dept_code = '" + deptCode+"')";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result; 
		}
		catch (RuntimeException re) {
			throw re;
		}
	}
	
	public PageObject queryByDeptTypeId(String enterpriseCode, Long deptTypeId,
			String userName, int... rowStartIdxAndCount){
		try
		{
			PageObject result = new PageObject();
			String sql = 
				"select info.*\n" +
				"    from hr_j_emp_info info\n" + 
				"   where info.emp_state = 'U'\n" + 
				"     and info.emp_code||info.en_name like ?\n" + 
				"     and info.dept_id in (select p.dept_id\n" + 
				"                            from hr_c_dept p\n" + 
				"                           where p.dept_type_id = ?\n" + 
		   		"                             and p.is_use = 'Y')";//update by sychen 20100831
//			    "                             and p.is_use = 'U')";

			List<HrJEmpInfo> list = bll.queryByNativeSQL(sql, new Object[]{"%"+userName+"%",deptTypeId},HrJEmpInfo.class,rowStartIdxAndCount);
			String sqlCount = 
				"select count(1)\n" +
				"    from hr_j_emp_info info\n" + 
				"   where info.emp_state = 'U'\n" + 
				"     and info.emp_code||info.en_name like ?\n" + 
				"     and info.dept_id in (select p.dept_id\n" + 
				"                            from hr_c_dept p\n" + 
				"                           where p.dept_type_id = ?\n" + 
				"                             and p.is_use = 'Y')";//update by sychen 20100831
//				"                             and p.is_use = 'U')";

			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, new Object[]{"%"+userName+"%",deptTypeId}).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result; 
		}
		catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public List<HrJEmpInfo> findEmpListByDept(String fuzzy){
		String sql="select r.*\n" +
			"  from hr_j_emp_info r\n" + 
			" where r.chs_name like '%"+fuzzy+"%'\n" + 
			"    or r.emp_code like '%"+fuzzy+"%'";

		return bll.queryByNativeSQL(sql, HrJEmpInfo.class);
	}
	
	/**
	 * 由部门查询人员(级连查询)
	 * @param deptId 部门编码 
	 * @param queryKey 模糊查询
	 * @return PageObject 其中 list 
	 */
	public PageObject getWorkersByDeptId(Long deptId, String queryKey,
			 final int... rowStartIdxAndCount) { 
		PageObject object = null;
		List<EmpInfo> result = null;
		List list = null;
		String sql = "";
		if (queryKey != null && !"".equals(queryKey)) {
			queryKey = "%" + queryKey + "%";
		}
		else{
			queryKey = "%";
		}
		if(deptId.intValue() ==-2)
		{
			//没有部门和部门被删除的人员 
			sql =  "select a.emp_id,\n" +
				"       a.emp_code,\n" + 
				"       a.chs_name,\n" + 
				"       decode(a.sex, 'M', '男', 'W', '女') sex,\n" + 
				"       a.identity_card,\n" + 
				"       a.graduate_school,\n" + 
				"       a.speciality,\n" + 
				"       b.station_name,\n" + 
				"       decode(a.emp_state, 'U', '在职', 'L', '注销', 'N', '离职 ') state\n" + 
				"  from hr_j_emp_info a, hr_c_station b\n" + 
				" where a.station_id = b.station_id(+)\n" + 
				"   and a.emp_code || a.chs_name like ?\n" + 
				"   and (a.dept_id not in\n" + 
				"       (select t.dept_id from hr_c_dept t where t.is_use = 'Y') or\n" + //update by sychen 20100831
//				"       (select t.dept_id from hr_c_dept t where t.is_use = 'U') or\n" +
				"       a.dept_id is null)\n" + 
				" order by a.order_by, a.emp_code"; 
			list = bll.queryByNativeSQL(sql, new Object[] {queryKey}, rowStartIdxAndCount);
			if (list != null && list.size() > 0) {
				object = new PageObject();
				result = new ArrayList<EmpInfo>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					EmpInfo myempinfo = new EmpInfo();
					if (o[0] != null) {
						myempinfo.setEmpId(o[0].toString());
					}
					if (o[1] != null) {
						myempinfo.setEmpCode(o[1].toString());
					}
					if (o[2] != null) {
						myempinfo.setChsName(o[2].toString());
					}
					if (o[3] != null) {

						myempinfo.setSex(o[3].toString());

					}
					if (o[4] != null) {
						myempinfo.setIdentityCard(o[4].toString());
					}
					if (o[5] != null) {

						myempinfo.setGraduateSchool(o[5].toString());
					}
					if (o[6] != null) {
						myempinfo.setSpeciality(o[6].toString());
					}
					 
					if (o[7] != null) { 
						myempinfo.setStationId(o[7].toString()); 
					} 
					if (o[8] != null) {
						myempinfo.setEmpState(o[8].toString());
					} 
					result.add(myempinfo);
				} 
				object.setList(result);
				sql = "select count(1)\n" + 
				"  from hr_j_emp_info a, hr_c_station b\n" + 
				" where a.station_id = b.station_id(+)\n" + 
				"   and a.emp_code || a.chs_name like ?\n" + 
				"   and (a.dept_id not in\n" + 
				"       (select t.dept_id from hr_c_dept t where t.is_use = 'Y') or\n" + //update by sychen 20100831
//				"       (select t.dept_id from hr_c_dept t where t.is_use = 'U') or\n" + 
				"       a.dept_id is null)\n" + 
				" order by a.order_by, a.emp_code";
				object.setTotalCount(Long.parseLong(bll.getSingal(sql,
						new Object[] { queryKey }).toString())); 
			}
			 
		}
		else
		{
			sql =  "select a.emp_id,\n" +
				"       a.emp_code,\n" + 
				"       a.chs_name,\n" + 
				"       decode(a.sex, 'M', '男', 'W', '女') sex,\n" + 
				"       a.identity_card,\n" + 
				"       a.graduate_school,\n" + 
				"       a.speciality,\n" + 
				"       b.dept_name,\n" + 
				"       c.station_name,\n" + 
				"       decode(a.emp_state, 'U', '在职', 'L', '注销', 'N', '离职 ') state,\n" + 
				"       a.dept_id,\n" + 
				"       b.dept_code\n" + 
				"  from hr_j_emp_info a, hr_c_dept b, hr_c_station c\n" + 
				" where a.dept_id = b.dept_id\n" + 
				"   and a.station_id = c.station_id(+)\n" + 
				"   and b.is_use = 'Y'\n" + //update by sychen 20100831
//				"   and b.is_use = 'U'\n" + 
				"   and a.emp_code || a.chs_name like ?\n" + 
				"   and a.dept_id in (select t.dept_id\n" + 
				"                       from hr_c_dept t\n" + 
				"                      where t.is_use = 'Y'\n" + //update by sychen 20100831
//				"                      where t.is_use = 'U'\n" +
				"                      start with t.dept_id =?\n" + 
				"                     connect by prior t.dept_id = t.pdept_id)\n" + 
				" order by b.order_by, a.order_by, a.emp_code";  
			list = bll.queryByNativeSQL(sql, new Object[] { queryKey, deptId },
					rowStartIdxAndCount); 
			if (list != null && list.size() > 0) {
				object = new PageObject();
				result = new ArrayList<EmpInfo>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					EmpInfo myempinfo = new EmpInfo();
					if (!o[0].toString().equals("")) {
						myempinfo.setEmpId(o[0].toString());
					}
					if (o[1] != null) {
						myempinfo.setEmpCode(o[1].toString());
					}
					if (o[2] != null) {
						myempinfo.setChsName(o[2].toString());
					}
					if (o[3] != null) {

						myempinfo.setSex(o[3].toString());

					}
					if (o[4] != null) {
						myempinfo.setIdentityCard(o[4].toString());
					}
					if (o[5] != null) {

						myempinfo.setGraduateSchool(o[5].toString());
					}
					if (o[6] != null) {
						myempinfo.setSpeciality(o[6].toString());
					}
					if (o[7] != null) {
						myempinfo.setDeptName(o[7].toString());
					}
					if (o[8] != null) {

						myempinfo.setStationId(o[8].toString());

					}
					if (o[9] != null) {
						myempinfo.setEmpState(o[9].toString());
					}
					if (o[10] != null) {
						myempinfo.setDeptId(o[10].toString());
					}
					if (o[11] != null) {
						myempinfo.setDeptCode(o[11].toString());
					}
					result.add(myempinfo);
				} 
				object.setList(result);
				sql =  "select count(1)\n" + 
				"  from hr_j_emp_info a, hr_c_dept b, hr_c_station c\n" + 
				" where a.dept_id = b.dept_id\n" + 
				"   and a.station_id = c.station_id(+)\n" + 
				"   and b.is_use = 'Y'\n" + //update by sychen 20100831
//				"   and b.is_use = 'U'\n" + 
				"   and a.emp_code || a.chs_name like ?\n" + 
				"   and a.dept_id in (select t.dept_id\n" + 
				"                       from hr_c_dept t\n" + 
				"                      where t.is_use = 'Y'\n" + //update by sychen 20100831
//				"                      where t.is_use = 'U'\n" + 
				"                      start with t.dept_id =?\n" + 
				"                     connect by prior t.dept_id = t.pdept_id)" ;
				object.setTotalCount(Long.parseLong(bll.getSingal(sql,
						new Object[] { queryKey, deptId }).toString()));
	
			}
		}
		return object;
	} 
	
	/**
	 * add by liuyi 090929
	 * 取得人员详细信息
	 * @param workerCode 工号
	 * @return EmployeeInfo
	 */
	public  EmployeeInfo getEmpInfoDetail(String workerCode)
	{
		EmployeeInfo result = null;
//		String sql = 
//			"select *\n" +
//			"  from hr_j_emp_info t\n" + 
//			" where t.emp_code = ?\n" + 
//			"   and t.emp_state = 'U'\n" + 
//			"   and rownum = 1";
//		 List<HrJEmpInfo> obj = bll.queryByNativeSQL(sql,new Object[]{workerCode},HrJEmpInfo.class);
//		if(obj!=null&& obj.size()>0)
//		{
//			result = new EmployeeInfo();
//			result.setModel(obj.get(0));
			String sql =  "select j.nation_name,\n" +
		    	"       c.native_place_name,\n" + 
		    	"       g.politics_name,\n" + 
		    	"       n.dept_name,\n" + 
		    	"       d.station_name,\n" + 
		    	"       h.station_level_name,\n" + 
		    	"       l.type_of_work_name,\n" + 
		    	"       f.emp_type_name,\n" + 
		    	"       k.technology_titles_type_name,\n" + 
		    	"       i.technology_grade_name,\n" + 
		    	"       e.education_name,\n" + 
		    	"       b.degree_name,\n" + 
		    	"       m.assistant_manager_units_name\n" + 
		    	"  from hr_j_emp_info                a,\n" + 
		    	"       hr_c_degree                  b,\n" + 
		    	"       hr_c_native_place            c,\n" + 
		    	"       hr_c_station                 d,\n" + 
		    	"       hr_c_education               e,\n" + 
		    	"       hr_c_emp_type                f,\n" + 
		    	"       hr_c_politics                g,\n" + 
		    	"       hr_c_station_level           h,\n" + 
		    	"       hr_c_technology_grade        i,\n" + 
		    	"       com_c_nation                 j,\n" + 
		    	"       hr_c_technology_titles_type  k,\n" + 
		    	"       hr_c_type_of_work            l,\n" + 
		    	"       hr_c_assistant_manager_units m,\n" + 
		    	"       hr_c_dept                    n\n" + 
		    	" where a.degree_id = b.degree_id(+)\n" + 
		    	"   and a.native_place_id = c.native_place_id(+)\n" + 
		    	"   and a.station_id = d.station_id(+)\n" + 
		    	"   and a.education_id = e.education_id(+)\n" + 
		    	"   and a.emp_type_id = f.emp_type_id(+)\n" + 
		    	"   and a.politics_id = g.politics_id(+)\n" + 
		    	"   and a.station_level = h.station_level_id(+)\n" + 
		    	"   and a.technology_grade_id = i.technology_grade_id(+)\n" + 
		    	"   and a.nation_id = j.nation_id(+)\n" + 
		    	"   and a.technology_titles_type_id = k.technology_titles_type_id(+)\n" + 
		    	"   and a.type_of_work_id = l.type_of_work_id(+)\n" + 
		    	"   and a.assistant_manager_units_id = m.assistant_manager_units_id(+)\n" + 
		    	"   and a.dept_id = n.dept_id(+)\n" + 
		    	"   and a.emp_code = ?\n" + 
		    	"   and b.is_use(+) = 'Y'\n" + //update by sychen 20100831
		    	"   and c.is_use(+) = 'Y'\n" + 
		    	"   and d.is_use(+) = 'Y'\n" + 
		    	"   and e.is_use(+) = 'Y'\n" + 
		    	"   and f.is_use(+) = 'Y'\n" + 
		    	"   and g.is_use(+) = 'Y'\n" + 
		    	"   and h.is_use(+) = 'Y'\n" + 
		    	"   and i.is_use(+) = 'Y'\n" + 
		    	"   and k.is_use(+) = 'Y'\n" + 
		    	"   and l.is_use(+) = 'U'\n" + 
		    	"   and m.is_use(+) = 'Y'\n" + 
		    	"   and n.is_use(+) = 'Y'";
//		    	"   and b.is_use(+) = 'U'\n" + 
//		    	"   and c.is_use(+) = 'U'\n" + 
//		    	"   and d.is_use(+) = 'U'\n" + 
//		    	"   and e.is_use(+) = 'U'\n" + 
//		    	"   and f.is_use(+) = 'U'\n" + 
//		    	"   and g.is_use(+) = 'U'\n" + 
//		    	"   and h.is_use(+) = 'U'\n" + 
//		    	"   and i.is_use(+) = 'U'\n" + 
//		    	"   and k.is_use(+) = 'U'\n" + 
//		    	"   and l.is_use(+) = 'U'\n" + 
//		    	"   and m.is_use(+) = 'U'\n" + 
//		    	"   and n.is_use(+) = 'U'";
		    List list = bll.queryByNativeSQL(sql,new Object[]{workerCode});
		    if(list !=null && list.size()>0)
		    {
		    	result = new EmployeeInfo();
		    	Object[] r = (Object[])list.get(0);
		    	if(r[0] !=null)
		    	{
		    		result.setNationName(r[0].toString());
		    		
		    	}
		    	if(r[1] !=null)
		    	{
		    		result.setCityName(r[1].toString());
		    		
		    	}
		    	if(r[2] !=null)
		    	{
		    		result.setPoliticsName(r[2].toString());
		    		
		    	}
		    	if(r[3] !=null)
		    	{
		    		result.setBelongDeptName(r[3].toString());
		    		
		    	}
		    	if(r[4] !=null)
		    	{
		    		result.setWorkStationName(r[4].toString());
		    		
		    	}
		    	if(r[5] !=null)
		    	{
		    		result.setStationLevelName(r[5].toString());
		    		
		    	}
		    	if(r[6] !=null)
		    	{
		    		result.setWorkTypeName(r[6].toString());
		    		
		    	}
		    	if(r[7] !=null)
		    	{
		    		result.setWorkerTypeName(r[7].toString());
		    		
		    	}
		    	if(r[8] !=null)
		    	{
		    		result.setSkillTypeName(r[8].toString());
		    		
		    	}
		    	if(r[9] !=null)
		    	{
		    		result.setSkillLevelName(r[9].toString());
		    		
		    	}
		    	if(r[10] !=null)
		    	{
		    		result.setEducationName(r[10].toString());
		    		
		    	}
		    	if(r[11] !=null)
		    	{
		    		result.setDegreeName(r[11].toString());
		    		
		    	}
		    	if(r[12] !=null)
		    	{
		    		result.setManagerUnit(r[12].toString());
		    	}
		    }
//		} 
		return result; 
	}
	
	// add by liuyi 090914
	 public HrJEmpInfo update2(HrJEmpInfo bean)throws Exception{
	    	LogUtil.log("EJB:更新员工是否外借字段开始。", Level.INFO, null);
	    	try{
	    		String strSql = "UPDATE HR_J_EMP_INFO SET IS_BORROW = ?,LAST_MODIFIY_BY=?,LAST_MODIFIY_DATE=? WHERE "
	    			          + " EMP_ID = "+bean.getEmpId()
	    			          + " AND IS_BORROW = ? AND IS_USE = ? AND  ENTERPRISE_CODE = ? ";
	    		List lstParams = new ArrayList();
	    		lstParams.add("N");
	    		lstParams.add(bean.getLastModifiyBy());
	    		lstParams.add(new Date());
	    		lstParams.add("Y");
	    		lstParams.add("Y");
	    		// modify by liuyi 090914 无该属性
//	    		lstParams.add(bean.getEnterpriseCode());
	    		LogUtil.log("EJB:SQL=" +strSql, Level.INFO, null);
	        	bll.exeNativeSQL(strSql, lstParams.toArray());
	    		LogUtil.log("EJB:更新员工是否外借字段结束。", Level.INFO, null);
	    		return bean;
	    	}catch(Exception e){
	    		LogUtil.log("EJB:更新员工是否外借字段失败。", Level.SEVERE, null);
	    		throw e;
	    	}
		}
	 
	 /**
	 	 * add by liuyi 090914 
		 * 按人员ID查找
		 * 
		 * @param id,enterpriseCode
		 * @return HrJEmpInfo
		 */
		public HrJEmpInfo findByEmpId(Long id, String enterpriseCode) {
			try {
				HrJEmpInfo hrjEmpBean;
				String sql = "select t.* from  hr_j_emp_info t where t.EMP_ID=? "
						+ "and t.IS_USE = ? and t.ENTERPRISE_CODE=?";
				Object[] params = new Object[3];
				params[0] = id;
				params[1] = IS_USE;
				params[2] = enterpriseCode;
				Object instance = bll.getSingal(sql, params);
				// 查找为空
				if (instance != null) {
					hrjEmpBean = entityManager.find(HrJEmpInfo.class, id);
				} else {
					hrjEmpBean = null;
				}
				return hrjEmpBean;
			} catch (RuntimeException re) {
				LogUtil.log("EJB:员工进厂类别维护查询失败。", Level.WARNING, null);
				throw re;
			}
		}
		
		/**
		 * add by liuyi 090915
		 * 由考勤部门id或部门id查询为该部门的所有人员的部门附带信息。
		 * 
		 * @param deptId
		 *            部门编码
		 * @param enterpriseCode
		 *            企业编码
		 * @return PageObject 其中 list为List<Employee>.返回直属于该部门的所有人员。
		 */
		@SuppressWarnings("unchecked")
		public PageObject getEmpFollowInfoByDeptId(Long deptId, boolean isAttendanceDept, String enterpriseCode,
				final int... rowStartIdxAndCount) {
			PageObject result = null;
			// 2009-3-16 郑智鹏 UT-BUG-KQ003-007修改begin
			String sql = "";
			String sqlCount = "";
			int paramsCount;
			if(isAttendanceDept){
				sql = "select a.emp_code, a.chs_name,  a.DEPT_ID, c.DEPT_NAME,"
						+ " a.emp_id,a.sex \n"
						+ "  from hr_j_emp_info a, HR_C_DEPT c  \n"
						+ " where a.DEPT_ID = c.DEPT_ID \n"
						+ "   and c.IS_USE = 'Y'\n"
						+ "   and c.ENTERPRISE_CODE =?\n"
						+ "   and a.is_use = 'Y'\n"
						+ "   and a.enterprise_code =?\n"
                         //update by sychen 20100713 
						+ "   and a.ATTENDANCE_DEPT_ID =?\n"
//						+ "   and nvl(a.ATTENDANCE_DEPT_ID,a.DEPT_ID) =?\n"
						+ " order by a.emp_code";
				paramsCount = 3;
			} else {
				// 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改begin
				sql = "select a.emp_code, a.chs_name, a.DEPT_ID, c.DEPT_NAME,"
					+ " a.emp_id,a.sex \n"
					+ "  from hr_j_emp_info a, HR_C_DEPT c  \n"
					+ " where a.DEPT_ID = c.DEPT_ID \n"
					+ "   and c.IS_USE = 'Y'\n"
					+ "   and c.ENTERPRISE_CODE =?\n"
					+ "   and a.is_use = 'Y'\n"
					+ "   and a.enterprise_code =?\n"
					+ "   and a.DEPT_ID =?\n"
					+ " order by a.emp_code";
				paramsCount = 3;
				// 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改end
			}
			// 查询参数数组
			Object[] params = new Object[paramsCount];
			int j = 0;
			params[j++] = enterpriseCode;
			params[j++] = enterpriseCode;
			params[j++] = deptId;
			List list = new ArrayList();
				list = bll.queryByNativeSQL(sql, params, rowStartIdxAndCount);
			// 2009-3-16 郑智鹏 UT-BUG-KQ003-007修改end
			//LogUtil.log("EJB-getEmpFollowInfoByDeptId:SQL=" + sql, Level.INFO, null);
			LogUtil.log("EJB:para=" + deptId, Level.INFO, null);
			if (list != null && list.size() > 0) {
				result = new PageObject();
				List emps = new ArrayList<EmpFollowBaseInfo>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					EmpFollowBaseInfo e = new EmpFollowBaseInfo();
					if (o[0] != null) {
						e.setEmpCode(o[0].toString());
					}
					if (o[1] != null) {
						e.setChsName(o[1].toString());
					}
					if (o[2] != null) {
						e.setDeptId(o[2].toString());
					}
					if (o[3] != null) {
						e.setDeptName(o[3].toString());
					}
					if (o[4] != null) {
						// empID
						e.setEmpId(o[4].toString());
					}
					if (o[5] != null) {
						// empID
						e.setSex(o[5].toString());
					}
					emps.add(e);
				}
				result.setList(emps);
				if(isAttendanceDept){
					sqlCount = 
						"select count(1)\n"
						+ "  from hr_j_emp_info a, HR_C_DEPT c  \n"
						+ " where a.DEPT_ID = c.DEPT_ID \n"
						+ "   and c.IS_USE = 'Y'\n"
						+ "   and c.ENTERPRISE_CODE =?\n"
						+ "   and a.is_use = 'Y'\n"
						+ "   and a.enterprise_code =?\n"
						+ "   and nvl(a.ATTENDANCE_DEPT_ID,a.DEPT_ID) =?\n"
						+ " order by a.emp_code";
				}else {
					sqlCount = 
						"select count(1)\n"
						+ "  from hr_j_emp_info a, HR_C_DEPT c  \n"
						+ " where a.DEPT_ID = c.DEPT_ID \n"
						+ "   and c.IS_USE = 'Y'\n"
						+ "   and c.ENTERPRISE_CODE =?\n"
						+ "   and a.is_use = 'Y'\n"
						+ "   and a.enterprise_code =?\n"
						+ "   and a.DEPT_ID =?\n"
						+ " order by a.emp_code";
				}
				result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount,
						new Object[] { enterpriseCode, enterpriseCode, deptId })
						.toString()));
			}

			return result;
		}
		
		/**
		 * add by liuyi 091123
		 * 根据员工Id, 获得其基本维护信息
		 * 
		 * @param argEmpId
		 *            员工Id
		 * @param argEnterpriseCode
		 *            企业编码
		 * @return 员工基本信息
		 */
		@SuppressWarnings("unchecked")
//		public HrJEmpInfoBean getEmpMaintBaseInfo(Long argEmpId,
		public List<HrJEmpInfoBean> getEmpMaintBaseInfo(Long argEmpId,
				String argEnterpriseCode) {
			LogUtil.log("EJB:根据员工Id, 获得其基本维护信息开始", Level.INFO, null);
//			HrJEmpInfoBean result = null;

			try {
				StringBuilder sbd = new StringBuilder();
				sbd.append("SELECT B.DEPT_NAME, ");
				sbd.append("A.DEPT_ID, ");
				sbd.append("A.EMP_ID, ");
				sbd.append("A.CHS_NAME, ");
				sbd.append("A.EN_NAME, ");
				sbd.append("A.EMP_CODE, ");
				sbd.append("A.NATIVE_PLACE_ID, ");
			//	sbd.append("O.NATIVE_PLACE_NAME, ");
				sbd.append("A.NATIVE_PLACE_NAME, "); //modify by fyyang 20100714 籍贯直接取名称
				sbd.append("A.POLITICS_ID, ");
				sbd.append("P.POLITICS_NAME, ");
				sbd.append("to_char(A.BRITHDAY,'yyyy-mm-dd') AS BRITHDAY, ");
				sbd.append("A.SEX, ");
				sbd.append("A.IS_WEDDED, ");
				sbd.append("A.ARCHIVES_ID, ");
				sbd.append("A.STATION_ID, ");
				sbd.append("C.STATION_NAME, ");
				sbd.append("A.EMP_TYPE_ID, ");
				sbd.append("D.EMP_TYPE_NAME, ");
				sbd.append("to_char(A.WORK_DATE,'yyyy-mm-dd') AS WORK_DATE, ");
				sbd
						.append("to_char(A.MISSION_DATE,'yyyy-mm-dd') AS MISSION_DATE, ");
				sbd.append("A.TECHNOLOGY_TITLES_TYPE_ID AS TECHNOLOGY_TITLES_ID, ");
				sbd.append("E.TECHNOLOGY_TITLES_NAME, ");
				sbd.append("A.TECHNOLOGY_GRADE_ID, ");
				sbd.append("F.TECHNOLOGY_GRADE_NAME, ");
				sbd.append("A.TYPE_OF_WORK_ID, ");
				sbd.append("G.TYPE_OF_WORK_NAME, ");
				sbd.append("A.IDENTITY_CARD, ");
				sbd.append("A.TIME_CARD_ID, ");
				sbd.append("A.PAY_CARD_ID, ");
				sbd.append("A.SOCIAL_INSURANCE_ID, ");
				sbd.append("A.SOCIAL_INSURANCE_DATE, ");
				sbd.append("A.MOBILE_PHONE, ");
				sbd.append("A.FAMILY_TEL, ");
				sbd.append("A.POSTALCODE, ");
				sbd.append("A.FAMILY_ADDRESS, ");
				sbd.append("A.OFFICE_TEL1, ");
				sbd.append("A.OFFICE_TEL2, ");
				sbd.append("A.FAX, ");
				sbd.append("A.E_MAIL, ");
				sbd.append("A.INSTANCY_MAN, ");
				sbd.append("A.INSTANCY_TEL, ");
				// MODIFIED BY LIUYI 091125 表中有存学校id属性
//				sbd.append("A.GRADUATE_SCHOOL AS SCHOOL_CODE_ID, ");
				sbd.append("A.GRADUATE_SCHOOL_ID AS SCHOOL_CODE_ID, ");
				sbd.append("H.SCHOOL_NAME, ");
				sbd.append("A.EDUCATION_ID, ");
				sbd.append("I.EDUCATION_NAME, ");
				sbd.append("A.DEGREE_ID, ");
				sbd.append("J.DEGREE_NAME, ");
				sbd.append("A.WORK_ID, ");
				sbd.append("K.WORK_NAME, ");
				sbd.append("A.LB_WORK_ID, ");
				sbd.append("N.LB_WORK_NAME, ");
				sbd.append("A.GRADUATE_DATE, ");
				sbd.append("A.IS_VETERAN, ");
				sbd.append("A.ORDER_BY, ");
				sbd.append("A.RECOMMEND_MAN, ");
				sbd.append("A.ONE_STRONG_SUIT, ");
				sbd.append("A.MEMO, ");
				sbd.append("A.NATION_CODE_ID, ");
				sbd.append("L.NATION_NAME, ");
				sbd.append("A.SPECIALTY_CODE_ID, ");
				sbd.append("M.SPECIALTY_NAME, ");
				sbd.append("A.STATION_GRADE, ");
				sbd.append("A.SALARY_LEVEL, ");
				sbd.append("A.CHECK_STATION_GRADE, ");
				sbd.append("A.EMP_STATE, ");
				sbd
						.append("to_char(A.LAST_MODIFIY_DATE,'yyyy-mm-dd hh24:mi:ss') AS LAST_MODIFIED_DATE ");
				// add by liuyi 20100406 新工号
				sbd.append(" ,A.NEW_EMP_CODE ");
				// add by liuyi 20100603 技能等级 是否主业 入党时间 入党转正时间
				sbd.append(" , a.jn_grade,a.is_main_work,to_char(a.into_part_date,'yyyy-mm-dd') as into_part_date ,to_char(a.part_positive_date,'yyyy-mm-dd') as part_positive_date ");
				sbd.append(" ,a.HOUSEHOLD,a.COUNTRY,a.BLOOD_TYPE,a.COMPONENT ");
				sbd.append(" ,a.is_special_trades,a.is_cadres  ");//add by drdu 20100629 是否特殊工种，是否干部
				
				sbd.append("FROM HR_J_EMP_INFO A ");
				sbd
						.append("LEFT JOIN HR_C_DEPT B ON A.DEPT_ID =B.DEPT_ID AND B.IS_USE = ? AND B.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_STATION C ON A.STATION_ID = C.STATION_ID AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_EMP_TYPE D ON A.EMP_TYPE_ID = D.EMP_TYPE_ID AND D.IS_USE = ? AND D.ENTERPRISE_CODE = ? ");
				sbd
//						.append("LEFT JOIN HR_C_TECHNOLOGY_TITLES E ON A.TECHNOLOGY_TITLES_TYPE_ID = E.TECHNOLOGY_TITLES_ID AND E.IS_USE = ? AND E.ENTERPRISE_CODE = ? ");
				.append("LEFT JOIN HR_C_TECHNOLOGY_TITLES E ON A.TECHNOLOGY_TITLES_TYPE_ID = E.TECHNOLOGY_TITLES_ID AND E.IS_USE = ?  ");
				sbd
						.append("LEFT JOIN HR_C_TECHNOLOGY_GRADE F ON A.TECHNOLOGY_GRADE_ID =F.TECHNOLOGY_GRADE_ID AND F.IS_USE = ? AND F.ENTERPRISE_CODE = ? ");
				sbd
//						.append("LEFT JOIN HR_C_TYPE_OF_WORK G ON A.TYPE_OF_WORK_ID =G.TYPE_OF_WORK_ID AND G.IS_USE = ? AND G.ENTERPRISE_CODE = ? ");
				.append("LEFT JOIN HR_C_TYPE_OF_WORK G ON A.TYPE_OF_WORK_ID =G.TYPE_OF_WORK_ID AND G.IS_USE = ?  ");
				sbd
				// modified by liuyi 091125 表中增加了schoolId属性
//						.append("LEFT JOIN HR_C_SCHOOL H ON A.GRADUATE_SCHOOL = H.SCHOOL_CODE_ID AND H.IS_USE = ? AND H.ENTERPRISE_CODE = ? ");
				.append("LEFT JOIN HR_C_SCHOOL H ON A.GRADUATE_SCHOOL_ID = H.SCHOOL_CODE_ID AND H.IS_USE = ? AND H.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_EDUCATION I ON A.EDUCATION_ID = I.EDUCATION_ID AND I.IS_USE = ? AND I.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_DEGREE J ON A.DEGREE_ID = J.DEGREE_ID AND J.IS_USE = ? AND J.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_WORKID K ON A.WORK_ID = K.WORK_ID AND K.IS_USE = ? AND K.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_NATION L ON A.NATION_CODE_ID = L.NATION_CODE_ID AND L.IS_USE = ? AND L.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_SPECIALTY M ON A.SPECIALTY_CODE_ID =M.SPECIALTY_CODE_ID AND M.IS_USE = ? AND M.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_LBGZBM N ON A.LB_WORK_ID = N.LB_WORK_ID AND N.IS_USE = ? AND N.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_NATIVE_PLACE O ON A.NATIVE_PLACE_ID=O.NATIVE_PLACE_ID AND O.IS_USE = ? AND O.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_POLITICS P ON A.POLITICS_ID =P.POLITICS_ID AND P.IS_USE = ? AND P.ENTERPRISE_CODE = ? ");
				// modified by liuyi 091125 存在被删除的人员 操作过程为 离职
//				sbd.append("WHERE A.IS_USE = ? ");
//				sbd.append("AND A.EMP_ID = ? ");
				sbd.append("WHERE ");
				sbd.append(" A.ENTERPRISE_CODE = ? ");
				if(argEmpId != null)
					sbd.append("AND A.EMP_ID = "+argEmpId+" ");
				// add by liuyi 20100604 order by 
				sbd.append(" and a.is_use='Y' ");
				sbd.append(" order by a.EMP_CODE ");

				LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);
				// modified by liuyi 091125 数据库表中有些无企业编码属性 有些使用中用U标示
//				Object[] params = new Object[] { "Y", argEnterpriseCode, "Y",
				Object[] params = new Object[] { "U", argEnterpriseCode, "U",
//						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "U",
						/*argEnterpriseCode,*/ "Y", argEnterpriseCode, "U",
						/*argEnterpriseCode,*/ "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, /*"Y",*/argEnterpriseCode,
						 };
				// 查询一条有参数sql语句
				List<HrJEmpInfoBean> lstResult = remote.queryDescribeByNativeSQL(
						sbd.toString(), params, HrJEmpInfoBean.class);

//				if (lstResult != null && lstResult.size() > 0) {
//					result = lstResult.get(0);
//				}
				LogUtil.log("EJB:根据员工Id, 获得其基本维护信息正常结束", Level.INFO, null);
//				return result;
				return lstResult;
			} catch (RuntimeException e) {
				LogUtil.log("EJB:根据员工Id, 获得其基本维护信息异常结束", Level.SEVERE, e);
				throw e;
			}
		}
		
		/**
		 * add by liuyi 091123
		 * 查询所有在职员工信息
		 * 
		 * @param enterpriseCode
		 *            企业编码
		 * @return EmployeeInfo
		 */
		public PageObject queryAllEmployee(String enterpriseCode,
				String empState, String isUse,String chsName,final int... rowStartIdxAndCount) {
			try {
				PageObject result = new PageObject();
				String sql = "select * from  hr_j_emp_info t where (t.emp_state != ? or t.EMP_STATE is null ) and  t.is_use = ?  and "
						+ "t.enterprise_code=  ? ";
				Object[] params = new Object[3];
				params[0] = empState;
				params[1] = isUse;
				params[2] = enterpriseCode;
				if(chsName!=null&&!chsName.equals(""))
				{
					sql+="  and  t.chs_name like '%"+chsName.trim()+"%' \n";
				}
				String sqlCount="select count(*) from ("+sql+")tt ";
				sql+=" order by t.EMP_CODE";
				
				List<HrJEmpInfo> list = bll.queryByNativeSQL(sql, params,
						HrJEmpInfo.class,rowStartIdxAndCount);
				result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount,params).toString()));
				result.setList(list);
				return result;
			} catch (RuntimeException re) {
				LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
			}
		}
		
		/** add by liuyi 091123
		 * 按员工编码查找
		 * 
		 * @param empCode,enterpriseCode
		 * @return int instance.size()
		 */
		@SuppressWarnings("unchecked")
		public int findByEmpCode(String empCode, String enterpriseCode) {
			try {
				// modified by liuyi 20100406  用新员工的编码进行唯一性判断
//				String sql = "select * from  hr_j_emp_info t where t.EMP_CODE=?  and t.IS_USE=? and t.ENTERPRISE_CODE= ?";
				String sql = "select * from  hr_j_emp_info t where t.new_emp_code=?  and t.IS_USE=? and t.ENTERPRISE_CODE= ?";
				Object[] params = new Object[3];
				params[0] = empCode;
				params[1] = IS_USE;
				params[2] = enterpriseCode;
				List<HrJEmpInfo> instance = bll.queryByNativeSQL(sql, params);
				return instance.size();
			} catch (RuntimeException re) {
				LogUtil.log("EJB:员工进厂类别维护查询失败。", Level.WARNING, null);
				throw re;
			}
		}
		
		/**
		 * add by liuyi 091123
		 * 将xls文件数据导入数据库
		 * 
		 * @params  list  xls文件里的数据
		 *          insertBy  数据插入者
		 *          enterpriseCode 企业代码
		 * @return boolean 插入是否全部成功         
		 */
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public boolean importToDatabase(List<HrJEmpInfo> list,
				String insertBy, String enterpriseCode) {
			LogUtil.log("EJB:导入员工开始。", Level.INFO, null);
			final String isUse = "Y";
			Long empId =bll.getMaxId("HR_J_EMP_INFO", "EMP_ID");
			LogUtil.log("save successful", Level.INFO, null);
			for (int i = 0; i < list.size(); i++) {
				HrJEmpInfo empInfo = new HrJEmpInfo();	
				try {
					empInfo.setEmpCode(list.get(i).getEmpCode());
					empInfo.setEnterpriseCode(enterpriseCode);
					empInfo.setChsName(list.get(i).getChsName());
					empInfo.setRetrieveCode(list.get(i).getRetrieveCode());
					empInfo.setIsUse(isUse);
					// 设置创建时间
					empInfo.setCreateDate(new java.util.Date());
					// 设置创建者
					// modified by liuyi 091123 表中属性为number
//					empInfo.setCreateBy(insertBy);
					// 修改人和修改时间
					// modified by liuyi 091123 表中属性为number
//					empInfo.setLastModifiyBy(insertBy);
					empInfo.setLastModifiyDate(new java.util.Date());
					empInfo.setEmpId(empId++);
					entityManager.persist(empInfo);
				} catch (Exception e) {
					LogUtil.log("EJB:导入员工失败。", Level.WARNING, null);
					return false;
				}
				LogUtil.log("EJB:导入员工成功。", Level.INFO, null);
			}
			LogUtil.log("EJB:所有员工信息已导入成功。", Level.INFO, null);
			return true;
		}
		
		
		/**
		 * 取得该部门下的员工的显示顺序
		 */
		@SuppressWarnings("unchecked")
		public PageObject findEmpOrderby(Long deptId, String enterpriseCode)
				throws SQLException {
			try {
				String strNotNullSql = "SELECT A.EMP_ID," + "A.CHS_NAME,"
						+ "A.ORDER_BY FROM HR_J_EMP_INFO A "
						+ "WHERE A.DEPT_ID = ? AND A.IS_USE=? "
						+ "AND A.ENTERPRISE_CODE =? AND A.ORDER_BY IS NOT NULL "
						+ "ORDER BY A.ORDER_BY";
				LogUtil.log("EJB:查找员工位置号维护显示顺序不为空的信息开始。", Level.INFO, null);
				List<Object> list = bll.queryByNativeSQL(strNotNullSql,
						new Object[] { deptId, "Y", enterpriseCode });
				PageObject obj = new PageObject();

				List<EmpOrderbyMaitenInfo> arraylist = new ArrayList<EmpOrderbyMaitenInfo>(
						list.size());
				Iterator it = list.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					EmpOrderbyMaitenInfo model = new EmpOrderbyMaitenInfo();
					if (data[0] != null) {
						model.setEmpId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null) {
						model.setChsName(data[1].toString());
					}
					if (data[2] != null) {
						model.setOrderBy(Long.parseLong((data[2].toString())));
						model.setOrderByBak(Long.parseLong((data[2].toString())));
					}
					model.setFlag("0");
					arraylist.add(model);
				}
				LogUtil.log("EJB:查找员工位置号维护显示顺序不为空的信息结束。", Level.INFO, null);

				Long maxOrderby = 0l;
				if (list.size() > 0) {
					maxOrderby = arraylist.get(list.size() - 1).getOrderBy();
				}

				String strNullSql = "SELECT A.EMP_ID," + "A.CHS_NAME "
						+ "FROM HR_J_EMP_INFO A "
						+ "WHERE A.DEPT_ID = ? AND A.IS_USE=? "
						+ "AND A.ENTERPRISE_CODE =? AND A.ORDER_BY IS NULL "
						+ "ORDER BY A.EMP_ID";
				LogUtil.log("EJB:查找员工位置号维护显示顺序为空的信息开始。", Level.INFO, null);
				List<Object> list1 = bll.queryByNativeSQL(strNullSql, new Object[] {
						deptId, "Y", enterpriseCode });
				Iterator it1 = list1.iterator();
				while (it1.hasNext()) {
					Object[] data = (Object[]) it1.next();
					EmpOrderbyMaitenInfo model = new EmpOrderbyMaitenInfo();
					if (data[0] != null) {
						model.setEmpId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null) {
						model.setChsName(data[1].toString());
					}
					model.setOrderBy(++maxOrderby);
					// model.setOrderByBak(maxOrderby);
					model.setFlag("1");
					arraylist.add(model);
				}
				obj.setList(arraylist);
				LogUtil.log("EJB:查找员工位置号维护显示顺序不为空的信息结束。", Level.INFO, null);
				return obj;

			} catch (Exception e) {
				LogUtil.log("EJB:查找员工位置号维护显示顺序信息失败。", Level.SEVERE, e);
				throw new SQLException(e.getMessage());
			}
		}

		/**
		 * 更新该部门下的员工的显示顺序
		 * 
		 * @throws SQLException
		 */
//		public void updateEmpOrderBy(String workCode,
		public void updateEmpOrderBy(Long workId,
				List<EmpOrderbyMaitenInfo> updateOrderByList) throws SQLException {
			try {
				LogUtil.log("更新员工位置号维护显示顺序信息开始。", Level.INFO, null);
				for (int i = 0; i < updateOrderByList.size(); i++) {
					EmpOrderbyMaitenInfo bean = updateOrderByList.get(i);
					HrJEmpInfo entity = findById(bean.getEmpId());
					if (entity != null) {
						entity.setOrderBy(bean.getOrderBy());
						entity.setLastModifiyBy(workId);
						entity.setLastModifiyDate(new Date());
						update(entity);
					}
				}
				LogUtil.log("更新员工位置号维护显示顺序信息结束。", Level.INFO, null);
			} catch (Exception e) {
				LogUtil.log("EJB:更新员工位置号维护显示顺序信息失败。", Level.SEVERE, e);
				throw new SQLException(e.getMessage());
			}
		}
		
		
		//------------------------------ 退休预警查询 add by drdu 091223---------------------------
		@SuppressWarnings("unchecked")
		public PageObject finRetirementDateQueryList(String retirementDate,String enterpriseCode,final int... rowStartIdxAndCount)
		{
			PageObject pg = new PageObject();
			String sql = "select *  from (select a.emp_id,\n" +
				"       a.emp_code,\n" + 
				"       a.chs_name,\n" + 
				"       decode(a.sex, 'M', '男', 'W', '女') sex,\n" + 
				"       (select  decode(GETFirstLevelBYID(c.dept_id),c.dept_code,'',null,'','','',GETDEPTNAME(GETFirstLevelBYID(c.dept_id))||'_')||c.dept_name\n" +
//				" c.dept_name\n" + //modify by wpzhu 20100731
				"          from hr_c_dept c\n" + 
				"         where c.dept_id = a.dept_id\n" + 
				"           and c.is_use = 'Y') deptName,\n" + //update by sychen 20100831
//				"           and c.is_use = 'U') deptName,\n" + 
				"       to_char(a.brithday,'yyyy-mm-dd'),\n" + 
				"       (to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy')+1 ) age,\n" + 
				"       to_char(a.mission_date,'yyyy-mm-dd'),\n" + 
				"       (select b.station_name\n" + 
				"          from hr_c_station b\n" + 
				"         where b.station_id = a.station_id\n" + 
				"           and b.is_use = 'Y') stationName,\n" + //update by sychen 20100831
//				"           and b.is_use = 'U') stationName,\n" + 
				"       to_char(a.retirement_date,'yyyy-mm-dd'),\n" + 
				"        a.retirement_date - trunc(sysdate) countdown\n" + 
				//add by sychen 20100629
				"        ,(to_char(SYSDATE, 'yyyy') - to_char(a.work_date, 'yyyy')+1) workDate\n" + 
				"        ,decode(a.is_special_trades,'Y','是','否')\n" + 
				"         ,decode(a.is_cadres,'Y','是','否')\n" + 
                " ,decode(a.sex,'M',decode(a.is_special_trades,'Y',55,60),decode(a.is_special_trades,'Y',45,decode(a.is_cadres,'Y',55,50))) agestandard \n"+
				"        , to_char(a.work_date,'yyyy-mm-dd')work_date," +
				"" +
				"a.politics_id," +
				"(select politics_name  from   hr_c_politics  p where p.politics_id=a.politics_id  and p.is_use='Y')politicsName," +
				"(select b.station_id from hr_c_station b   where b.station_id = a.station_id    and b.is_use = 'Y') stationId," +//update by sychen 20100831
//				"(select b.station_id from hr_c_station b   where b.station_id = a.station_id    and b.is_use = 'U') stationId," +
				"a.dept_id,a.is_retired  \n" + //add by wpzhu
				//add by sychen 20100629 end
				"  from hr_j_emp_info a\n" + 
				" where a.is_use = 'Y'\n" + 
				//update by sychen 20100629
//				"   and a.retirement_date < to_date('"+retirementDate+"', 'yyyy-MM')\n" + 
//				"  and (to_char(a.retirement_date, 'yyyy')-to_char(SYSDATE, 'yyyy'))<=2\n" + 
//				"  and a.emp_state != '3'\n" + 
//				"   and (a.emp_state = '3' or a.emp_state = 'U')\n" + 
				//update by sychen 20100629 end
				"   and a.is_retired !='1' or   a.is_retired  is  null \n" + //add by wpzhu 
				"   and a.enterprise_code = '"+enterpriseCode+"')b\n " +
				"   where  b.age>=b.agestandard-2";//modify by  wpzhu 201007010
			String sqlCount = "select count(1) from (" + sql + ") \n";
			sql +=" order by b.emp_code";
//			System.out.println("the sql"+sql);
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);	
			
			pg.setList(list);
			pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
			return pg;
		}

		/**
		 * add by liuyi 091231 通过员工编号，姓名过滤人员
		 * @param fuzzy
		 * @param enterpriseCode
		 * @param rowStartIdxAndCount
		 * @return
		 */
		public PageObject getEmpListByFilter(String fuzzy,
				String enterpriseCode, int... rowStartIdxAndCount) {
			PageObject pg = new PageObject();
			String sql = "select a.emp_id,a.emp_code, a.chs_name, b.dept_id, b.dept_code, b.dept_name,a.new_emp_code \n"
				+ "  from hr_j_emp_info a, hr_c_dept b\n"
				+ " where a.dept_id = b.dept_id(+)\n"
				+ "   and b.is_use(+) = 'Y'\n"//update by sychen 20100831
//				+ "   and b.is_use(+) = 'U'\n"
				+ " and a.is_use = 'Y' \n";
			if(fuzzy != null && !fuzzy.equals(""))
				sql += "  and (a.chs_name like '%" + fuzzy+"%' or a.new_emp_code like '%" + fuzzy+"%') \n";
			sql	+= " order by b.order_by, a.order_by, a.new_emp_code";
			String sqlCount = "select count(*) from (" + sql + ") \n";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(list);
			pg.setTotalCount(totalCount);
			return pg;
		}

		public void importPersonnelFilesInfo(List<HrJEmpInfo> empList) {
			Long nextId = bll.getMaxId("hr_j_emp_info", "emp_id");
			
			for(HrJEmpInfo emp : empList){
				String sql = "select a.* from hr_j_emp_info a where a.new_emp_code='"+emp.getNewEmpCode()+"'";
				List<HrJEmpInfo> selectList = bll.queryByNativeSQL(sql, HrJEmpInfo.class);
				if(selectList != null && selectList.size() > 0){
					HrJEmpInfo updated = selectList.get(0);
					 // 名字
					updated.setChsName(emp.getChsName());
					if(emp.getBrithday()!=null&&!emp.getBrithday().equals(""))
					{
					updated.setBrithday(emp.getBrithday());
					}
			        // 性别
					if(emp.getSex()!=null&&!emp.getSex().equals(""))
					{
					updated.setSex(emp.getSex());
					}
			        // 参加工作日期
					if(emp.getWorkDate()!=null&&!emp.getWorkDate().equals(""))
					{
					updated.setWorkDate(emp.getWorkDate());
					}
			        // 是否主业
					if(emp.getIsMainWork()!=null&&!emp.getIsMainWork().equals(""))
					{
					updated.setIsMainWork(emp.getIsMainWork());
					}
			        // 政治面貌
					if(emp.getPoliticsId()!=null&&!emp.getPoliticsId().equals(""))
					{
					updated.setPoliticsId(emp.getPoliticsId());
					}
			        // 入党转正时间
					if(emp.getPartPositiveDate()!=null&&!emp.getPartPositiveDate().equals(""))
					{
					updated.setPartPositiveDate(emp.getPartPositiveDate());
					}
			        // 民族
					if(emp.getNationCodeId()!=null&&!emp.getNationCodeId().equals(""))
					{
					updated.setNationCodeId(emp.getNationCodeId());
					}
			        // 当前学历
					if(emp.getEducationId()!=null&&!emp.getEducationId().equals(""))
					{
					updated.setEducationId(emp.getEducationId());
					}
			        // 婚否状况
					if(emp.getIsWedded()!=null&&!emp.getIsWedded().equals(""))
					{
					updated.setIsWedded(emp.getIsWedded());
					}
			        // 入党时间
					if(emp.getIntoPartDate()!=null&&!emp.getIntoPartDate().equals(""))
					{
					updated.setIntoPartDate(emp.getIntoPartDate());
					}
			        // 修改人
					if(emp.getLastModifiyBy()!=null&&!emp.getLastModifiyBy().equals(""))
					{
					updated.setLastModifiyBy(emp.getLastModifiyBy());
					}
			        // 修改时间
					if(emp.getLastModifiyDate()!=null&&!emp.getLastModifiyDate().equals(""))
					{
					updated.setLastModifiyDate(emp.getLastModifiyDate());
					}
					
					//******************************************
			        // 部门 
					if(emp.getDeptId()!=null&&!emp.getDeptId().equals(""))
					{
					updated.setDeptId(emp.getDeptId());
					}
			        // 工作岗位
					if(emp.getStationId()!=null&&!emp.getStationId().equals(""))
					{
					updated.setStationId(emp.getStationId());
					}
			        // 员工身份
					if(emp.getWorkId()!=null&&!emp.getWorkId().equals(""))
					{
					updated.setWorkId(emp.getWorkId());
					}
			     // 籍贯
					if(emp.getNativePlaceId()!=null&&!emp.getNativePlaceId().equals(""))
					{
					updated.setNativePlaceId(emp.getNativePlaceId());
					}
			     // 员工工号  老
					if(emp.getEmpCode()!=null&&!emp.getEmpCode().equals(""))
					{
					updated.setEmpCode(emp.getEmpCode());
					}
					if(emp.getNewEmpCode()!=null&&!emp.getNewEmpCode().equals(""))
					{
					updated.setNewEmpCode(emp.getNewEmpCode());
					}
			     // 户口
					if(emp.getHousehold()!=null&&!emp.getHousehold().equals(""))
					{
					updated.setHousehold(emp.getHousehold());
					}
			     // 家庭住址
					if(emp.getFamilyAddress()!=null&&!emp.getFamilyAddress().equals(""))
					{
					updated.setFamilyAddress(emp.getFamilyAddress());
					}
			     // 血型
					if(emp.getBloodType()!=null&&!emp.getBloodType().equals(""))
					{
					updated.setBloodType(emp.getBloodType());
					}
			     // 国籍 
					if(emp.getCountry()!=null&&!emp.getCountry().equals(""))
					{
					updated.setCountry(emp.getCountry());
					}
			     // 本人成分
					if(emp.getComponent()!=null&&!emp.getComponent().equals(""))
					{
					updated.setComponent(emp.getComponent());
					}
			        
			        
			        //*******************************************
					
					entityManager.merge(updated);
					System.out.println("测试");
				}else{
					emp.setEmpId(nextId++);
					entityManager.persist(emp);
				}
			}
		}
		


	
		public Long getEmpIdByNewEmpCode(String newEmpCode) {
			Long empId = null;
			String sql = "select t.emp_id from hr_j_emp_info t where t.is_use='Y' and t.new_emp_code='"+newEmpCode+"'";
			Object obj = bll.getSingal(sql);
			if(obj != null)
				empId = Long.parseLong(obj.toString());
			return empId;
		}
		
	//根据部门找员工详细信息
		public List<HrJEmpInfoBean> getEmpMaintBaseInfoList(Long deptId,
				String argEnterpriseCode){

			LogUtil.log("EJB:根据员工Id, 获得其基本维护信息开始", Level.INFO, null);
//			HrJEmpInfoBean result = null;

			try {
				StringBuilder sbd = new StringBuilder();
				sbd.append("SELECT B.DEPT_NAME, ");
				sbd.append("A.DEPT_ID, ");
				sbd.append("A.EMP_ID, ");
				sbd.append("A.CHS_NAME, ");
				sbd.append("A.EN_NAME, ");
				sbd.append("A.EMP_CODE, ");
				sbd.append("A.NATIVE_PLACE_ID, ");
				sbd.append("O.NATIVE_PLACE_NAME, ");
				sbd.append("A.POLITICS_ID, ");
				sbd.append("P.POLITICS_NAME, ");
				sbd.append("to_char(A.BRITHDAY,'yyyy-mm-dd') AS BRITHDAY, ");
				sbd.append("A.SEX, ");
				sbd.append("A.IS_WEDDED, ");
				sbd.append("A.ARCHIVES_ID, ");
				sbd.append("A.STATION_ID, ");
				sbd.append("C.STATION_NAME, ");
				sbd.append("A.EMP_TYPE_ID, ");
				sbd.append("D.EMP_TYPE_NAME, ");
				sbd.append("to_char(A.WORK_DATE,'yyyy-mm-dd') AS WORK_DATE, ");
				sbd.append("to_char(A.MISSION_DATE,'yyyy-mm-dd') AS MISSION_DATE, ");
				sbd.append("A.TECHNOLOGY_TITLES_TYPE_ID AS TECHNOLOGY_TITLES_ID, ");
				sbd.append("E.TECHNOLOGY_TITLES_NAME, ");
				sbd.append("A.TECHNOLOGY_GRADE_ID, ");
				sbd.append("F.TECHNOLOGY_GRADE_NAME, ");
				sbd.append("A.TYPE_OF_WORK_ID, ");
				sbd.append("G.TYPE_OF_WORK_NAME, ");
				sbd.append("A.IDENTITY_CARD, ");
				sbd.append("A.TIME_CARD_ID, ");
				sbd.append("A.PAY_CARD_ID, ");
				sbd.append("A.SOCIAL_INSURANCE_ID, ");
				sbd.append("A.SOCIAL_INSURANCE_DATE, ");
				sbd.append("A.MOBILE_PHONE, ");
				sbd.append("A.FAMILY_TEL, ");
				sbd.append("A.POSTALCODE, ");
				sbd.append("A.FAMILY_ADDRESS, ");
				sbd.append("A.OFFICE_TEL1, ");
				sbd.append("A.OFFICE_TEL2, ");
				sbd.append("A.FAX, ");
				sbd.append("A.E_MAIL, ");
				sbd.append("A.INSTANCY_MAN, ");
				sbd.append("A.INSTANCY_TEL, ");
				// MODIFIED BY LIUYI 091125 表中有存学校id属性
//				sbd.append("A.GRADUATE_SCHOOL AS SCHOOL_CODE_ID, ");
				sbd.append("A.GRADUATE_SCHOOL_ID AS SCHOOL_CODE_ID, ");
				sbd.append("H.SCHOOL_NAME, ");
				sbd.append("A.EDUCATION_ID, ");
				sbd.append("I.EDUCATION_NAME, ");
				sbd.append("A.DEGREE_ID, ");
				sbd.append("J.DEGREE_NAME, ");
				sbd.append("A.WORK_ID, ");
				sbd.append("K.WORK_NAME, ");
				sbd.append("A.LB_WORK_ID, ");
				sbd.append("N.LB_WORK_NAME, ");
				sbd.append("A.GRADUATE_DATE, ");
				sbd.append("A.IS_VETERAN, ");
				sbd.append("A.ORDER_BY, ");
				sbd.append("A.RECOMMEND_MAN, ");
				sbd.append("A.ONE_STRONG_SUIT, ");
				sbd.append("A.MEMO, ");
				sbd.append("A.NATION_CODE_ID, ");
				sbd.append("L.NATION_NAME, ");
				sbd.append("A.SPECIALTY_CODE_ID, ");
				sbd.append("M.SPECIALTY_NAME, ");
				sbd.append("A.STATION_GRADE, ");
				sbd.append("A.SALARY_LEVEL, ");
				sbd.append("A.CHECK_STATION_GRADE, ");
				sbd.append("A.EMP_STATE, ");
				sbd
						.append("to_char(A.LAST_MODIFIY_DATE,'yyyy-mm-dd hh24:mi:ss') AS LAST_MODIFIED_DATE ");
				// add by liuyi 20100406 新工号
				sbd.append(" ,A.NEW_EMP_CODE ");
				// add by liuyi 20100603 技能等级 是否主业 入党时间 入党转正时间
				sbd.append(" , a.jn_grade,a.is_main_work,to_char(a.into_part_date,'yyyy-mm-dd') as into_part_date ,to_char(a.part_positive_date,'yyyy-mm-dd') as part_positive_date ");
				sbd.append(" ,a.HOUSEHOLD,a.COUNTRY,a.BLOOD_TYPE,a.COMPONENT ");
				sbd.append("FROM HR_J_EMP_INFO A ");
				sbd
						.append("LEFT JOIN HR_C_DEPT B ON A.DEPT_ID =B.DEPT_ID AND B.IS_USE = ? AND B.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_STATION C ON A.STATION_ID = C.STATION_ID AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_EMP_TYPE D ON A.EMP_TYPE_ID = D.EMP_TYPE_ID AND D.IS_USE = ? AND D.ENTERPRISE_CODE = ? ");
				sbd
//						.append("LEFT JOIN HR_C_TECHNOLOGY_TITLES E ON A.TECHNOLOGY_TITLES_TYPE_ID = E.TECHNOLOGY_TITLES_ID AND E.IS_USE = ? AND E.ENTERPRISE_CODE = ? ");
				.append("LEFT JOIN HR_C_TECHNOLOGY_TITLES E ON A.TECHNOLOGY_TITLES_TYPE_ID = E.TECHNOLOGY_TITLES_ID AND E.IS_USE = ?  ");
				sbd
						.append("LEFT JOIN HR_C_TECHNOLOGY_GRADE F ON A.TECHNOLOGY_GRADE_ID =F.TECHNOLOGY_GRADE_ID AND F.IS_USE = ? AND F.ENTERPRISE_CODE = ? ");
				sbd
//						.append("LEFT JOIN HR_C_TYPE_OF_WORK G ON A.TYPE_OF_WORK_ID =G.TYPE_OF_WORK_ID AND G.IS_USE = ? AND G.ENTERPRISE_CODE = ? ");
				.append("LEFT JOIN HR_C_TYPE_OF_WORK G ON A.TYPE_OF_WORK_ID =G.TYPE_OF_WORK_ID AND G.IS_USE = ?  ");
				sbd
				// modified by liuyi 091125 表中增加了schoolId属性
//						.append("LEFT JOIN HR_C_SCHOOL H ON A.GRADUATE_SCHOOL = H.SCHOOL_CODE_ID AND H.IS_USE = ? AND H.ENTERPRISE_CODE = ? ");
				.append("LEFT JOIN HR_C_SCHOOL H ON A.GRADUATE_SCHOOL_ID = H.SCHOOL_CODE_ID AND H.IS_USE = ? AND H.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_EDUCATION I ON A.EDUCATION_ID = I.EDUCATION_ID AND I.IS_USE = ? AND I.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_DEGREE J ON A.DEGREE_ID = J.DEGREE_ID AND J.IS_USE = ? AND J.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_WORKID K ON A.WORK_ID = K.WORK_ID AND K.IS_USE = ? AND K.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_NATION L ON A.NATION_CODE_ID = L.NATION_CODE_ID AND L.IS_USE = ? AND L.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_SPECIALTY M ON A.SPECIALTY_CODE_ID =M.SPECIALTY_CODE_ID AND M.IS_USE = ? AND M.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_LBGZBM N ON A.LB_WORK_ID = N.LB_WORK_ID AND N.IS_USE = ? AND N.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_NATIVE_PLACE O ON A.NATIVE_PLACE_ID=O.NATIVE_PLACE_ID AND O.IS_USE = ? AND O.ENTERPRISE_CODE = ? ");
				sbd
						.append("LEFT JOIN HR_C_POLITICS P ON A.POLITICS_ID =P.POLITICS_ID AND P.IS_USE = ? AND P.ENTERPRISE_CODE = ? ");
				// modified by liuyi 091125 存在被删除的人员 操作过程为 离职
//				sbd.append("WHERE A.IS_USE = ? ");
//				sbd.append("AND A.EMP_ID = ? ");
				sbd.append("WHERE ");
				sbd.append(" A.ENTERPRISE_CODE = ? ");
				if(deptId != null)
					sbd.append("AND a.dept_id in (select t.dept_id from hr_c_dept t where t.is_use = 'Y' start with t.dept_id ='"+deptId+"' connect by prior t.dept_id = t.pdept_id)");//update by sychen 20100831
//				    sbd.append("AND a.dept_id in (select t.dept_id from hr_c_dept t where t.is_use = 'U' start with t.dept_id ='"+deptId+"' connect by prior t.dept_id = t.pdept_id)");
				// add by liuyi 20100604 order by 
				sbd.append(" and a.is_use='Y' ");
				sbd.append(" order by b.dept_code ,a.EMP_CODE");

				LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);
				// modified by liuyi 091125 数据库表中有些无企业编码属性 有些使用中用U标示
//				Object[] params = new Object[] { "Y", argEnterpriseCode, "Y",
				Object[] params = new Object[] { "U", argEnterpriseCode, "U",
//						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "U",
						/*argEnterpriseCode,*/ "Y", argEnterpriseCode, "U",
						/*argEnterpriseCode,*/ "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, "Y",
						argEnterpriseCode, "Y", argEnterpriseCode, /*"Y",*/argEnterpriseCode,
						 };
				// 查询一条有参数sql语句
				List<HrJEmpInfoBean> lstResult = remote.queryDescribeByNativeSQL(
						sbd.toString(), params, HrJEmpInfoBean.class);
				return lstResult;
			} catch (RuntimeException e) {
				LogUtil.log("EJB:根据员工Id, 获得其基本维护信息异常结束", Level.SEVERE, e);
				throw e;
			}
			
		}
		
		public List getEmpDeptAll(String deptId) {
			String sql = "select *\n" +
				"  from hr_c_dept d\n" + 
				" where d.pdept_id != -1\n" + 
				" start with d.dept_id = '"+deptId+"'\n" + 
				"connect by prior d.pdept_id = d.dept_id \n" +
				"order by  d.dept_code";
			return bll.queryByNativeSQL(sql);
		}
		
		//------------------------------ 中层干部退休预警查询（部门，姓名） add by kzhang 20100806---------------------------
		@SuppressWarnings("unchecked")
		public PageObject finRetirementListByDeptAndName(String deptId,String empName,String enterpriseCode,int... rowStartIdxAndCount)
		{
			PageObject pg = new PageObject();
			String name=empName+"%";
			String sql = "select distinct b.* from (select a.emp_id,a.emp_code,a.chs_name," +
					"decode(a.sex, 'M', '男', 'W', '女') sex," +
					"(select decode(GETDEPTNAME(GETFirstLevelBYID(c.dept_id)),null,c.dept_name,GETDEPTNAME(GETFirstLevelBYID(c.dept_id))) " +
					"from hr_c_dept c " +
					"where c.dept_id = a.dept_id " +
					"and c.is_use = 'Y') deptName," +//update by sychen 20100831
//					"and c.is_use = 'U') deptName," +
					"(to_char(a.brithday, 'yyyy-mm-dd')) brithday," +
					//update by sychen 20100817 年龄计算
					"decode((substr(to_char(sysdate, 'yyyy-mm'),6,8)-substr(to_char(a.brithday, 'yyyy-mm'),6,8)),\n" +
					"                    '1',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '2',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '3',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '4',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '5',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '6',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '7',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '8',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '9',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '10',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                    '11',(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1),\n" + 
					"                        (to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy')))age\n," + 
//					"(to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1) age," +
					//update by sychen 20100817 年龄计算 end
					"(to_char(a.mission_date, 'yyyy-mm-dd')) missiondate," +
					" (select b.station_name " +
					"	from hr_c_station b " +
					"	where b.station_id = a.station_id " +
					"	and b.is_use = 'Y') stationName," +//update by sychen 20100831
//					"	and b.is_use = 'U') stationName," +
					"(to_char(a.retirement_date, 'yyyy-mm-dd')) retirementdate," +
					"(decode(a.sex,'M',decode((select b.station_name " +
					"							from hr_c_station b " +
					"							where b.station_id = a.station_id " +
					"							and b.is_use = 'Y'),'副总师',55,53)," +//update by sychen 20100831
//					"							and b.is_use = 'U'),'副总师',55,53)," +
					"				decode((select b.station_name " +
					"							from hr_c_station b " +
					"							where b.station_id = a.station_id " +
					"							and b.is_use = 'Y'),'副总师',50,48)) - (to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1)) countdown," +//update by sychen 20100831
//					"							and b.is_use = 'U'),'副总师',50,48)) - (to_char(sysdate, 'yyyy') - to_char(a.brithday, 'yyyy') + 1)) countdown," +
					//update by sychen 20100817 工龄计算
					"decode((substr(to_char(sysdate, 'yyyy-mm'),6,8)-substr(to_char(a.work_date, 'yyyy-mm'),6,8)),\n" +
					"                    '1',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '2',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '3',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '4',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '5',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '6',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '7',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '8',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '9',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '10',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                    '11',(to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy') + 1),\n" + 
					"                        (to_char(sysdate, 'yyyy') - to_char(a.work_date, 'yyyy')))workAge,\n"+
			      
//					"(to_char(SYSDATE, 'yyyy') - to_char(a.work_date, 'yyyy') + 1) workAge," + 
					//update by sychen 20100817 工龄计算 end
					"decode(a.is_special_trades, 'Y', '是', '否') isSpecial," +
					"decode(a.is_cadres, 'Y', '是', '否') isCadres," +
					"decode(a.sex,'M',decode((select b.station_name " +
					"							from hr_c_station b " +
					"							where b.station_id = a.station_id " +
					"							and b.is_use = 'Y'),'副总师',55,53)," +//update by sychen 20100831
//					"							and b.is_use = 'U'),'副总师',55,53)," +
					"				decode((select b.station_name " +
					"							from hr_c_station b " +
					"							where b.station_id = a.station_id " +
					"							and b.is_use = 'Y'),'副总师',50,48)) agestandard," +//update by sychen 20100831
//					"							and b.is_use = 'U'),'副总师',50,48)) agestandard," +
					"(to_char(a.work_date, 'yyyy-mm-dd')) work_date," +
					" (select b.station_id " +
					"	from hr_c_station b" +
					"	where b.station_id = a.station_id" +
					"	and b.is_use = 'Y') stationId," +//update by sychen 20100831
//					"	and b.is_use = 'U') stationId," +
					"a.dept_id," +
					"decode(a.is_retired, '1', '是', '否') is_retired" +
					"	from hr_j_emp_info a" +
					"	where a.is_use = 'Y'" +
					//"	and a.is_retired != '1'" +
					//"	or a.is_retired is null" +
					"	and a.enterprise_code = '"+enterpriseCode+"') b, hr_j_depstationcorrespond ds " +
					"where b.stationid = ds.station_id" +
					" and ds.is_lead = 'Y'" +
					" and b.age >= b.agestandard - 1";
					
					
			if (deptId!=null&&!deptId.equals("")){
				sql+=" and b.dept_id in (select t.dept_id" +
				" from hr_c_dept t" +
				" where t.is_use = 'Y'" +//update by sychen 20100831
//				" where t.is_use = 'U'" +
				"start with t.dept_id = '"+deptId+"'" +
				"connect by prior t.dept_id = t.pdept_id)";
			}
			if (name!=null&&!name.equals("")){
				sql+=" and b.chs_name like ?";
			}
			String sqlCount = "select count(1) from (" + sql + ") \n";
			sql +=" order by b.emp_code";
//			System.out.println("the sql"+sql);
			//List list = bll.queryByNativeSQL(sql);	
			List list=bll.queryByNativeSQL(sql,new Object[]{name},rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount,new Object[]{name}).toString()));
			return pg;
		}
}