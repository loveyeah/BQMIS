/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import sun.nio.ch.SocketOpts.IP;

/**
 * 学历简历登记 远程处理对象
 * 
 * @see power.ejb.hr.HrJEducation
 * @author wangjunjie
 */
@Stateless
public class HrJEducationFacade implements HrJEducationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="HrJWorkresumeFacade")
	protected HrJWorkresumeFacadeRemote remote;

	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
	/** 是否原始学历 是 */
	private static final String IF_ORIGINALITY_Y="1";
	/**
	 * 新增学历简历
	 * 
	 * @param entity
	 *            学历简历
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJEducation entity) throws SQLException {
		LogUtil.log("EJB:新增学历简历开始", Level.INFO, null);
		try {
			if (entity.getEducationid() == null) {
				entity.setEducationid(bll.getMaxId("HR_J_EDUCATION",
						"EDUCATIONID"));
			}
			entityManager.persist(entity);
			LogUtil.log("EJB:新增学历简历正常结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增学历简历异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent HrJEducation entity.
	 * 
	 * @param entity
	 *            HrJEducation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJEducation entity) {
		LogUtil.log("deleting HrJEducation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJEducation.class, entity
					.getEducationid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改学历简历
	 * 
	 * @param entity
	 *            学历简历
	 * @return HrJEducation 修改后的学历简历
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJEducation update(HrJEducation entity)
			throws SQLException, DataChangeException {
		LogUtil.log("EJB:修改学历简历开始", Level.INFO, null);

		// 得到数据库中的这个记录
		HrJEducation education = findById(entity.getEducationid());

		// 排他
		if (!formatDate(education.getLastModifiedDate(),
				DATE_FORMAT_YYYYMMDD_TIME_SEC).equals(
				formatDate(entity.getLastModifiedDate(),
						DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}

		try {
			// 设置修改日期
			entity.setLastModifiedDate(new Date());
			HrJEducation result = entityManager.merge(entity);
			LogUtil.log("EJB:修改学历简历正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:修改学历简历异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public HrJEducation findById(Long id) {
		LogUtil.log("finding HrJEducation instance with id: " + id,
				Level.INFO, null);
		try {
			HrJEducation instance = entityManager.find(
					HrJEducation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJEducation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJEducation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJEducation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEducation> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJEducation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJEducation model where model."
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
	 * Find all HrJEducation entities.
	 * 
	 * @return List<HrJEducation> all HrJEducation entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJEducation> findAll() {
		LogUtil.log("finding all HrJEducation instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJEducation model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据员工Id, 获得其学历简历基本信息
	 * 
	 * @param argEmpId
	 *            员工Id
	 * @param argEnterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态参数(开始行数和查询行数)
	 * @return 学历简历基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEducationInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount) {
		LogUtil.log("EJB:根据员工Id, 获得其学历简历基本信息开始", Level.INFO, null);
		
		try {
			StringBuilder sbd = new StringBuilder();
			sbd.append(" SELECT ");
			sbd.append("	A.LANGUAGE_CODE_ID, ");
			sbd.append("	F.LANGUAGE_NAME, ");
			sbd.append("	A.SCHOOL_CODE_ID, ");
			sbd.append("	B.SCHOOL_NAME, ");
			sbd.append("	A.SPECIALTY_CODE_ID, ");
			sbd.append("	E.SPECIALTY_NAME, ");
			sbd.append("	A.STUDY_TYPE_CODE_ID, ");
			sbd.append("	G.STUDY_TYPE, ");
			sbd.append("	A.ENROLLMENT_DATE, ");
			sbd.append("	A.STUDY_LIMIT, ");
			sbd.append("	A.STUDY_CODE, ");
			sbd.append("	A.IF_GRADUATE, ");
			sbd.append("	A.GRADUATE_DATE, ");
			sbd.append("	A.STUDY_MONEY, ");
			sbd.append("	A.EDUCATION_RESULT, ");
			sbd.append("	A.CERTIFICATE_CODE, ");
			sbd.append("	A.IF_ORIGINALITY, ");
			sbd.append("	A.IF_HIGHTEST_XL, ");
			sbd.append("	A.MEMO, ");
			sbd.append("	A.EDUCATION_ID, ");
			sbd.append("	C.EDUCATION_NAME, ");
			sbd.append("	A.DEGREE_ID, ");
			sbd.append("	D.DEGREE_NAME, ");
			sbd.append("	to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS LAST_MODIFIED_DATE, ");
			sbd.append("	A.EDUCATIONID ");
			sbd.append(" ,A.CATEGORY ");
			sbd.append("	,A.GRADUATE_SCHOOL ");// add by sychen 20100709
			sbd.append("	,A.SPECIALTY ");//add by sychen 20100709
			sbd.append("	,H.new_emp_code ");//add by sychen 20100713
			sbd.append("FROM ");
			sbd.append("	HR_J_EDUCATION A  ");
			sbd.append("	left join	HR_C_SCHOOL B	 ");											
			sbd.append("		on A.SCHOOL_CODE_ID = B.SCHOOL_CODE_ID AND B.IS_USE = ? AND B.ENTERPRISE_CODE = ? ");													
			sbd.append("	left join	HR_C_EDUCATION C		 ");										
			sbd.append("		on A.EDUCATION_ID = C.EDUCATION_ID AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ? ");													
			sbd.append("	left join	HR_C_DEGREE D		");										
			sbd.append("		on A.DEGREE_ID = D.DEGREE_ID AND D.IS_USE = ? AND D.ENTERPRISE_CODE = ? ");													
			sbd.append("	left join	HR_C_SPECIALTY E	 ");											
			sbd.append("		on A.SPECIALTY_CODE_ID = E.SPECIALTY_CODE_ID AND E.IS_USE = ? AND E.ENTERPRISE_CODE = ? ");														
			sbd.append("	left join	HR_C_LANGUAGE F			");									
			sbd.append("		on A.LANGUAGE_CODE_ID = F.LANGUAGE_CODE_ID AND F.IS_USE = ? AND F.ENTERPRISE_CODE = ? ");													
			sbd.append("	left join	HR_C_STUDYTYPE G	 ");										
			sbd.append("		on A.STUDY_TYPE_CODE_ID = G.STUDY_TYPE_CODE_ID AND G.IS_USE = ? AND G.ENTERPRISE_CODE = ? ");														
			//add by sychen 20100713	
			sbd.append("	left join	hr_j_emp_info H	 ");									
			sbd.append("		on A.EMP_ID = H.EMP_ID AND H.IS_USE = ? AND H.ENTERPRISE_CODE = ? ");													
			sbd.append("WHERE A.IS_USE = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd.append("AND A.ENTERPRISE_CODE = ? ");
			sbd.append("ORDER BY A.EDUCATIONID ");
			
			// 打印SQL语句
			LogUtil.log("EJB:sql=" + sbd.toString(), Level.INFO, null);
			Object[] params = new Object[] {"Y", argEnterpriseCode, "Y", argEnterpriseCode,
					"Y", argEnterpriseCode, "Y", argEnterpriseCode,
					"Y", argEnterpriseCode, "Y", argEnterpriseCode, "Y", argEnterpriseCode,
					"Y", argEmpId, argEnterpriseCode};
			
			// 查询一条有参数sql语句
			List<HrJEducationBean> lstResult = remote.queryDescribeByNativeSQL(sbd.toString(),
					params, HrJEducationBean.class, rowStartIdxAndCount);
			String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.EDUCATIONID) FROM ");
			// 查询符合条件的学历简历总数
			Object objCount = bll.getSingal(sqlCount, params);
			Long totalCount = 0L;
			if (objCount != null) {
				totalCount = Long.parseLong(objCount.toString());
			}
	        
			PageObject result = new PageObject();
			// 符合条件的学历简历信息
			result.setList(lstResult);
			// 符合条件的学历简历信息的总数
			result.setTotalCount(totalCount);

			LogUtil.log("EJB:根据员工Id, 获得其学历简历基本信息正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据员工Id, 获得其学历简历基本信息异常结束", Level.SEVERE, e);
			throw e;
		}
	}
	
	/**
	 * 根据员工Id, 获得其原始学历基本信息
	 * 
	 * @param argEmpId
	 *            员工Id
	 * @param argEnterpriseCode
	 *            企业编码	
	 * @return 学历简历基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public HrJEducationBean getOriEducationInfo(Long argEmpId, String argEnterpriseCode) {
		LogUtil.log("EJB:根据员工Id, 获得其原始学历基本信息开始", Level.INFO, null);
		
		try {
			StringBuilder sbd = new StringBuilder();
			sbd.append(" SELECT ");
			sbd.append("	F.LANGUAGE_NAME, ");
			sbd.append("	B.SCHOOL_NAME, ");
			sbd.append("	E.SPECIALTY_NAME, ");
			sbd.append("	G.STUDY_TYPE, ");
			sbd.append("	A.ENROLLMENT_DATE, ");
			sbd.append("	A.STUDY_LIMIT, ");
			sbd.append("	A.STUDY_CODE, ");
			sbd.append("	A.IF_GRADUATE, ");
			sbd.append("	A.GRADUATE_DATE, ");
			sbd.append("	A.STUDY_MONEY, ");
			sbd.append("	A.EDUCATION_RESULT, ");
			sbd.append("	A.CERTIFICATE_CODE, ");
			sbd.append("	A.IF_ORIGINALITY, ");
			sbd.append("	A.IF_HIGHTEST_XL, ");
			sbd.append("	A.MEMO, ");
			sbd.append("	C.EDUCATION_NAME, ");
			sbd.append("	D.DEGREE_NAME, ");
			sbd.append("	A.EDUCATIONID ");
			sbd.append("FROM ");
			sbd.append("	HR_J_EDUCATION A  ");
			sbd.append("	left join	HR_C_SCHOOL B	 ");											
			sbd.append("		on A.SCHOOL_CODE_ID = B.SCHOOL_CODE_ID	 ");													
			sbd.append("	left join	HR_C_EDUCATION C		 ");										
			sbd.append("		on A.EDUCATION_ID = C.EDUCATION_ID	 ");													
			sbd.append("	left join	HR_C_DEGREE D		");										
			sbd.append("		on A.DEGREE_ID = D.DEGREE_ID	 ");													
			sbd.append("	left join	HR_C_SPECIALTY E	 ");											
			sbd.append("		on A.SPECIALTY_CODE_ID = E.SPECIALTY_CODE_ID ");														
			sbd.append("	left join	HR_C_LANGUAGE F			");									
			sbd.append("		on A.LANGUAGE_CODE_ID = F.LANGUAGE_CODE_ID	");													
			sbd.append("	left join	HR_C_STUDYTYPE G	 ");										
			sbd.append("		on A.STUDY_TYPE_CODE_ID = G.STUDY_TYPE_CODE_ID	");													
			sbd.append("WHERE A.IS_USE = 'Y' ");
			sbd.append("AND A.IF_ORIGINALITY = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd.append("AND A.ENTERPRISE_CODE = ? ");
			
			
			Object[] params = new Object[] {IF_ORIGINALITY_Y,argEmpId, argEnterpriseCode};											
			
			// 查询一条有参数sql语句
			List<HrJEducationBean> lstResult = remote.queryDescribeByNativeSQL(sbd.toString(),
					params, HrJEducationBean.class);
			HrJEducationBean oriEducation=null;
			if(lstResult!=null&&lstResult.size()>0){
				oriEducation=lstResult.get(0);
			}
			
			LogUtil.log("EJB:根据员工Id, 获得其原始学历基本信息正常结束", Level.INFO, null);
			return oriEducation;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据员工Id, 获得其原始学历基本信息异常结束", Level.SEVERE, e);
			throw e;
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

	public void delete(String ids) {
		if(ids != null && ids.length() > 0){
			String sql = "update hr_j_education t set t.is_use='N' where t.educationid in ("+ids+")";
			bll.exeNativeSQL(sql);
		}
		
	}

	public void importPersonnelFilesWorkResume(List<HrJEducation> educationList) {
		Long nextId = bll.getMaxId("hr_j_education", "educationid");
		for(HrJEducation  entity : educationList){
			String sql = 
				"select *\n" +
				"  from hr_j_education b\n" + 
				" where b.is_use = 'Y'\n" + 
				"   and b.emp_id = '"+entity.getEmpId()+"'\n" + 
				"   and b.education_id = '"+entity.getEducationId()+"'\n" + 
				"   and b.school_code_id = '"+entity.getSchoolCodeId()+"'\n" + 
				"   and b.specialty_code_id = '"+entity.getSpecialtyCodeId()+"'";

			List<HrJEducation> list = bll.queryByNativeSQL(sql, HrJEducation.class);
			if(list != null && list.size() > 0){
				HrJEducation updated = list.get(0);
				updated.setCategory(entity.getCategory());
				updated.setStudyTypeCodeId(entity.getStudyTypeCodeId());
				updated.setEnrollmentDate(entity.getEnrollmentDate());
				updated.setGraduateDate(entity.getGraduateDate());
				updated.setEducationResult(entity.getEducationResult());
				updated.setCertificateCode(entity.getCertificateCode());
				updated.setDegreeId(entity.getDegreeId());
				entityManager.merge(updated);
			}else{
				
				entity.setEducationid(nextId++);
				entityManager.persist(entity);
			}

		}
		
	}
}