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
import power.ejb.hr.form.PositionForm;

/**
 *  职务任免登记表
 * @author liuyi 091203
 */
@Stateless
public class HrJPositionFacade implements HrJPositionFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 新增职务任免登记表记录 
	 */
	public void save(HrJPosition entity) {
		LogUtil.log("saving HrJPosition instance", Level.INFO, null);
		try {
			entity.setPositionId(bll.getMaxId("HR_J_POSITION", "position_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除职务任免登记表记录
	 */
	public void delete(HrJPosition entity) {
		LogUtil.log("deleting HrJPosition instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJPosition.class, entity
					.getPositionId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新职务任免登记表记录
	 */
	public HrJPosition update(HrJPosition entity) throws SQLException, DataChangeException {
		LogUtil.log("updating HrJPosition instance", Level.INFO, null);
		
		// 得到数据库中的这个记录
		HrJPosition position = this.findById(entity.getPositionId());

		// 排他
		if (!formatDate(position.getLastModifiedDate(),
				DATE_FORMAT_YYYYMMDD_TIME_SEC).equals(
				formatDate(entity.getLastModifiedDate(),
						DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}
		try {
			// 设置修改日期
			entity.setLastModifiedDate(new Date());
			HrJPosition result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public HrJPosition findById(Long id) {
		LogUtil.log("finding HrJPosition instance with id: " + id, Level.INFO,
				null);
		try {
			HrJPosition instance = entityManager.find(HrJPosition.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<HrJPosition> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrJPosition instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJPosition model where model."
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
	public List<HrJPosition> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrJPosition instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJPosition model";
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
	
	/**
	 * 删除一条或多条职务任免登记表记录
	 */
	public void delete(String ids) {
		String sql = "update hr_j_position a set a.is_use='N' where a.position_id in (" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 查找指定员工的所有职务任免信息
	 * @param empId
	 * @param enterprise
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEmpAllPosition(Long empId, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.position_id,\n" +
			"a.emp_id,\n" + 
			"to_char(a.rm_date,'yyyy-mm-dd'),\n" + 
			"a.position_name,\n" + 
			"a.is_position,\n" + 
			"a.position_code,\n" + 
			"a.is_now,\n" + 
			"a.position_level,\n" + 
			"a.approve_dept,\n" + 
			"a.rm_mode,\n" + 
			"a.rm_reason,\n" + 
			"a.rm_view,\n" + 
			"a.memo,\n" + 
			"b.chs_name,\n" + 
			"c.dept_name,\n" + 
			"(select f.station_name from hr_c_station f where f.is_use='Y' and a.position_name=f.station_id),\n" + //update by sychen 20100901
//			"(select f.station_name from hr_c_station f where f.is_use='U' and a.position_name=f.station_id),\n" + 
			"(select h.station_level_name from hr_c_station_level h where h.is_use='Y' and a.position_level=h.station_level_id) \n" + //update by sychen 20100901
//			"(select h.station_level_name from hr_c_station_level h where h.is_use='U' and a.position_level=h.station_level_id) \n" + 
			" ,(select d.dept_name from hr_c_dept d where a.approve_dept=d.dept_id and d.is_use='Y') \n" + //update by sychen 20100901 
//			" ,(select d.dept_name from hr_c_dept d where a.approve_dept=d.dept_id and d.is_use='U') \n" +
			" from HR_J_POSITION a,hr_j_emp_info b,hr_c_dept c\n" + 
			"\n" + 
			" where a.is_use='Y'\n" + 
			" and b.is_use='Y'\n" + 
			" and a.emp_id=b.emp_id\n" + 
			"\n" + 
			" and c.is_use='Y'\n" +  //update by sychen 20100901 
//			" and c.is_use='U'\n" + 
			" and b.dept_id=c.dept_id\n" + 
			"\n" + 
			" and a.enterprise_code='"+ enterpriseCode +"'\n" + 
			" and b.enterprise_code='"+ enterpriseCode +"'\n" + 
			" and c.enterprise_code='"+ enterpriseCode +"'\n" + 
			"\n" + 
			" and a.emp_id=" + empId + " \n" + 
			" order by a.position_id";
		
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<PositionForm> arrlist = new ArrayList<PositionForm>();
		if(list != null && list.size() > 0){
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				Object[] data = (Object[])it.next();
				PositionForm form = new PositionForm();
//				HrJPosition position = new HrJPosition();
				if(data[0] != null)
					form.setPositionId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					form.setEmpId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					form.setRmDateString(data[2].toString());
				if(data[3] != null)
					form.setPositionName(data[3].toString());
				if(data[4] != null)
					form.setIsPosition(data[4].toString());
				if(data[5] != null)
					form.setPositionCode(data[5].toString());
				if(data[6] != null)
					form.setIsNow(data[6].toString());
				if(data[7] != null)
					form.setPositionLevel(Long.parseLong(data[7].toString()));
				if(data[8] != null)
					form.setApproveDept(data[8].toString());
				if(data[9] != null)
					form.setRmMode(Long.parseLong(data[9].toString()));
				if(data[10] != null)
					form.setRmReason(data[10].toString());
				if(data[11] != null)
					form.setRmView(data[11].toString());
				if(data[12] != null)
					form.setMemo(data[12].toString());
				if(data[13] != null)
					form.setEmpName(data[13].toString());
				if(data[14] != null)
					form.setEmpDeptName(data[14].toString());
				if(data[15] != null)
					form.setPositionDescri(data[15].toString());
				if(data[16] != null)
					form.setLevelDescri(data[16].toString());
				if(data[17] != null)
					form.setApproveDeptName(data[17].toString());
//				form.setPosition(position);
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