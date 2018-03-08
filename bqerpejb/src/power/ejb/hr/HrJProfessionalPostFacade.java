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
import power.ejb.hr.form.ProfessionalPostForm;

/**
 * 技术职称表
 * @author liuyi 091203
 */
@Stateless
public class HrJProfessionalPostFacade implements
		HrJProfessionalPostFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 新增技术职称表记录
	 */
	public void save(HrJProfessionalPost entity) {
		LogUtil.log("saving HrJProfessionalPost instance", Level.INFO, null);
		try {
			entity.setId(bll.getMaxId("HR_J_PROFESSIONAL_POST", "id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除技术职称表记录
	 */
	public void delete(HrJProfessionalPost entity) {
		LogUtil.log("deleting HrJProfessionalPost instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJProfessionalPost.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新技术职称表记录
	 */
	public HrJProfessionalPost update(HrJProfessionalPost entity) throws SQLException, DataChangeException {
		LogUtil.log("updating HrJProfessionalPost instance", Level.INFO, null);
		// 得到数据库中的这个记录
		HrJProfessionalPost post = this.findById(entity.getId());

		// 排他
		if (!formatDate(post.getLastModifiedDate(),
				DATE_FORMAT_YYYYMMDD_TIME_SEC).equals(
				formatDate(entity.getLastModifiedDate(),
						DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}
		try {
			entity.setLastModifiedDate(new Date());
			HrJProfessionalPost result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJProfessionalPost findById(Long id) {
		LogUtil.log("finding HrJProfessionalPost instance with id: " + id,
				Level.INFO, null);
		try {
			HrJProfessionalPost instance = entityManager.find(
					HrJProfessionalPost.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<HrJProfessionalPost> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrJProfessionalPost instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJProfessionalPost model where model."
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

	
	@SuppressWarnings("unchecked")
	public List<HrJProfessionalPost> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrJProfessionalPost instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from HrJProfessionalPost model";
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

	public void delete(String ids) {
		String sql = "update HR_J_PROFESSIONAL_POST a set a.is_use='N' where a.id in (" + ids+") \n";
		bll.exeNativeSQL(sql);
		
	}

	/**
	 * 通过员工id获得该员工的所有技术职称信息
	 * @param empId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEmpProfessionalPost(Long empId,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.id,\n" +
			" a.emp_id,\n" + 
			" to_char(a.recruitment_date,'yyyy-mm-dd'),\n" + 
			" a.professional_post,\n" + 
			" a.professional_level,\n" + 
			" a.is_now,\n" + 
			" a.judge_mode,\n" + 
			" to_char(a.valid_start_date,'yyyy-mm-dd'),\n" + 
			" to_char(a.valid_end_date,'yyyy-mm-dd'),\n" + 
			" a.certificate_name,\n" + 
			" a.certificate_code,\n" + 
			" a.certificate_dept,\n" + 
			" a.approve_code,\n" + 
			" a.judge_approve_dept,\n" + 
			" a.memo,\n" + 
			" b.chs_name,\n" + 
			" c.technology_titles_name\n" + 
			" from HR_J_PROFESSIONAL_POST a,hr_j_emp_info b,HR_C_TECHNOLOGY_TITLES c\n" + 
			"\n" + 
			" where a.is_use='Y'\n" + 
			" and b.is_use='Y'\n" + 
			" and a.emp_id=b.emp_id\n" + 
			" and c.is_use='Y'\n" +  //update by sychen 20100901 
//			" and c.is_use='U'\n" + 
			" and a.professional_post=c.technology_titles_id\n" + 
			" and a.enterprise_code='" + enterpriseCode +"'\n" + 
			" and b.enterprise_code='" + enterpriseCode +"'\n" + 
			" and b.enterprise_code='" + enterpriseCode +"' \n"
			+ " and a.emp_id=" + empId;

		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<ProfessionalPostForm> arrlist = new ArrayList<ProfessionalPostForm>();
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				Object[] data = (Object[])it.next();
				ProfessionalPostForm form = new ProfessionalPostForm();
//				HrJProfessionalPost post = new HrJProfessionalPost();
				if(data[0] != null)
					form.setId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					form.setEmpId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					form.setRecruitmentDateString(data[2].toString());
				if(data[3] != null)
					form.setProfessionalPost(Long.parseLong(data[3].toString()));
				if(data[4] != null)
					form.setProfessionalLevel(Long.parseLong(data[4].toString()));
				if(data[5] != null)
					form.setIsNow(data[5].toString());
				if(data[6] != null)
					form.setJudgeMode(data[6].toString());
				if(data[7] != null)
					form.setValidStartDateString(data[7].toString());
				if(data[8] != null)
					form.setValidEndDateString(data[8].toString());
				if(data[9] != null)
					form.setCertificateName(data[9].toString());
				if(data[10] != null)
					form.setCertificateCode(data[10].toString());
				if(data[11] != null)
					form.setCertificateDept(data[11].toString());
				if(data[12] != null)
					form.setApproveCode(data[12].toString());
				if(data[13] != null)
					form.setJudgeApproveDept(data[13].toString());
				if(data[14] != null)
					form.setMemo(data[14].toString());
				if(data[15] != null)
					form.setEmpName(data[15].toString());
				if(data[16] != null)
					form.setTechnologyName(data[16].toString());
//				form.setPost(post);
				arrlist.add(form);
			}
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
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

}