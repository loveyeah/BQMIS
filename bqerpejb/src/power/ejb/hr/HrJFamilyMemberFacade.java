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

/**
 * 社会关系登记 远程处理对象
 * 
 * @see power.ejb.hr.HrJFamilymember
 * @author wangjunjie
 */
@Stateless
public class HrJFamilyMemberFacade implements HrJFamilyMemberFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="HrJWorkresumeFacade")
	protected HrJWorkresumeFacadeRemote remote;

	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 新增社会关系
	 * 
	 * @param entity
	 *            社会关系
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJFamilymember entity) throws SQLException {
		LogUtil.log("EJB:新增社会关系开始", Level.INFO, null);
		try {
			if (entity.getFamilymemberid() == null) {
				entity.setFamilymemberid(bll.getMaxId("HR_J_FAMILYMEMBER",
						"FAMILYMEMBERID"));
			}
			entityManager.persist(entity);
			LogUtil.log("EJB:新增社会关系正常结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增社会关系异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent HrJFamilymember entity.
	 * 
	 * @param entity
	 *            HrJFamilymember entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJFamilymember entity) {
		LogUtil.log("deleting HrJFamilymember instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJFamilymember.class, entity
					.getFamilymemberid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改社会关系
	 * 
	 * @param entity
	 *            社会关系
	 * @return HrJFamilymember 修改后的社会关系
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJFamilymember update(HrJFamilymember entity)
			throws SQLException, DataChangeException {
		LogUtil.log("EJB:修改社会关系开始", Level.INFO, null);

		// 得到数据库中的这个记录
		HrJFamilymember familyMember = findById(entity.getFamilymemberid());

		// 排他
		if (!formatDate(familyMember.getLastModifiedDate(),
				DATE_FORMAT_YYYYMMDD_TIME_SEC).equals(
				formatDate(entity.getLastModifiedDate(),
						DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}

		try {
			// 设置修改日期
			entity.setLastModifiedDate(new Date());
			HrJFamilymember result = entityManager.merge(entity);
			LogUtil.log("EJB:修改社会关系正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:修改社会关系异常结束", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public HrJFamilymember findById(Long id) {
		LogUtil.log("finding HrJFamilymember instance with id: " + id,
				Level.INFO, null);
		try {
			HrJFamilymember instance = entityManager.find(
					HrJFamilymember.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJFamilymember entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJFamilymember property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJFamilymember> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJFamilymember> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJFamilymember instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJFamilymember model where model."
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
	 * Find all HrJFamilymember entities.
	 * 
	 * @return List<HrJFamilymember> all HrJFamilymember entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJFamilymember> findAll() {
		LogUtil.log("finding all HrJFamilymember instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJFamilymember model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据员工Id, 获得其社会关系基本信息
	 * 
	 * @param argEmpId
	 *            员工Id
	 * @param argEnterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            动态参数(开始行数和查询行数)
	 * @return 社会关系基本信息
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@SuppressWarnings("unchecked")
	public PageObject getFamilyMemberInfo(Long argEmpId, String argEnterpriseCode, final int ...rowStartIdxAndCount) {
		LogUtil.log("EJB:根据员工Id, 获得其社会关系基本信息开始", Level.INFO, null);
		
		try {
			StringBuilder sbd = new StringBuilder();

			sbd.append(" SELECT ");
			sbd.append(" 	A.CALLS_CODE_ID,		");
			sbd.append(" 	F.CALLS_NAME,		");
			sbd.append(" 	A.NATION_CODE_ID,	");
			sbd.append(" 	E.NATION_NAME,	");
			sbd.append(" 	A.NAME,		");
			sbd.append(" 	A.SEX,	");
			sbd.append(" 	A.BIRTHDAY,	");
			sbd.append(" 	A.IF_MARRIED,	");
			sbd.append(" 	A.EDUCATION_ID, ");
			sbd.append(" 	C.EDUCATION_NAME, ");
			sbd.append(" 	A.UNIT,			");
			sbd.append(" 	A.HEADSHIP_NAME,		");
			sbd.append(" 	A.ZXQS_MARK,	");
			sbd.append(" 	A.IF_LINEALLY,		");
			sbd.append(" 	A.NATIVE_PLACE_ID,	");
			sbd.append(" 	D.NATIVE_PLACE_NAME,	");
			sbd.append(" 	A.POLITICS_ID,		");
			sbd.append(" 	B.POLITICS_NAME,		");
			sbd.append(" 	to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') AS LAST_MODIFIED_DATE,		");
			sbd.append(" 	A.FAMILYMEMBERID, 	");
			sbd.append("    A.MEMO   ");
			sbd.append(" FROM ");
			sbd.append(" 	HR_J_FAMILYMEMBER A ");
			sbd.append(" 	left join	HR_C_POLITICS B		");
			sbd.append(" 		on A.POLITICS_ID = B.POLITICS_ID AND B.IS_USE = ? AND B.ENTERPRISE_CODE = ? ");
			sbd.append(" 		left join		HR_C_EDUCATION C			");
			sbd.append(" 		on A.EDUCATION_ID = C.EDUCATION_ID AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ? ");
			sbd.append(" 		left join		HR_C_NATIVE_PLACE D		");
			sbd.append(" 		on A.NATIVE_PLACE_ID = D.NATIVE_PLACE_ID AND D.IS_USE = ? AND D.ENTERPRISE_CODE = ? ");
			sbd.append(" 		left join		HR_C_NATION E	");
			sbd.append(" 		on A.NATION_CODE_ID = E.NATION_CODE_ID AND E.IS_USE = ? AND E.ENTERPRISE_CODE = ? ");
			sbd.append(" 		left join		HR_C_APPELLATION F	");
			sbd.append(" 		on A.CALLS_CODE_ID = F.CALLS_CODE_ID AND F.IS_USE = ? AND F.ENTERPRISE_CODE = ? ");
			sbd.append("WHERE A.IS_USE = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd.append("AND A.ENTERPRISE_CODE = ? ");
			sbd.append("ORDER BY A.FAMILYMEMBERID ");
			
			
			Object[] params = new Object[] {"Y", argEnterpriseCode,
					"Y", argEnterpriseCode, "Y", argEnterpriseCode,
					"Y", argEnterpriseCode, "Y", argEnterpriseCode,
					"Y", argEmpId, argEnterpriseCode};											
			
			// 查询一条有参数sql语句
			List<HrJFamilymemberBean> lstResult = remote.queryDescribeByNativeSQL(sbd.toString(), params,
					HrJFamilymemberBean.class, rowStartIdxAndCount);
			String sqlCount = sbd.toString().replaceFirst("SELECT.*? FROM ",
					"SELECT COUNT(DISTINCT A.FAMILYMEMBERID) FROM ");
			// 查询符合条件的社会关系总数
			Object objCount = bll.getSingal(sqlCount, params);
			Long totalCount = 0L;
			if (objCount != null) {
				totalCount = Long.parseLong(objCount.toString());
			}

			PageObject result = new PageObject();
			// 符合条件的社会关系信息
			result.setList(lstResult);
			// 符合条件的社会关系信息的总数
			result.setTotalCount(totalCount);

			LogUtil.log("EJB:根据员工Id, 获得其社会关系基本信息正常结束", Level.INFO, null);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:根据员工Id, 获得其社会关系基本信息异常结束", Level.SEVERE, e);
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
			String sql = "update hr_j_familymember t set t.is_use='N' where t.familymemberid in ("+ids+")";
			bll.exeNativeSQL(sql);
		}
	}

	public void importPersonnelFilesWorkResume(
			List<HrJFamilymember> familyMemberList) {

		Long nextId = bll.getMaxId("hr_j_familymember", "familymemberid");
		for(HrJFamilymember  entity : familyMemberList){
			String sql = 

				"select b.*\n" +
				"  from hr_j_familymember b\n" + 
				" where b.is_use = 'Y'\n" + 
				"   and b.emp_id = '"+entity.getEmpId()+"'\n" + 
				"   and b.name='"+entity.getName()+"'\n" + 
				"   and b.calls_code_id='"+entity.getCallsCodeId()+"'";


			List<HrJFamilymember> list = bll.queryByNativeSQL(sql, HrJFamilymember.class);
			if(list != null && list.size() > 0){
				HrJFamilymember updated = list.get(0);
				updated.setNationCodeId(entity.getNationCodeId());
				updated.setEducationId(entity.getEducationId());
				updated.setHeadshipName(entity.getHeadshipName());
				updated.setPoliticsId(entity.getPoliticsId());
				updated.setBirthday(entity.getBirthday());
				updated.setMemo(entity.getMemo());
				entityManager.merge(updated);
			}else{
				
				entity.setFamilymemberid(nextId++);
				entityManager.persist(entity);
			}

		}
		
	
		
	}
}