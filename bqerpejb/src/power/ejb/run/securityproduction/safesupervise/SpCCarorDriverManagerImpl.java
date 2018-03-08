package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrJEmpPhoto;
import power.ejb.hr.LogUtil;

/**
 * @author sychen 20100421
 */

@Stateless
public class SpCCarorDriverManagerImpl implements SpCCarorDriverManager {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	@SuppressWarnings("unchecked")
	public PageObject findSpCcarfileList(String likestr, String deptCode,
			String flag, String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.car_id,\n" + "t.car_no,\n" + "t.belong_to,\n"
				+ "t.right_code,\n" + "t.factory_type,\n" + "t.car_type,\n"
				+ "t.see_size,\n" + "t.car_color,\n" + "t.fuel_type,\n"
				+ "t.tire_type,\n" + "t.wheelbase,\n"
				+ "t.passerger_capacity,\n" + "t.in_out,\n"
				+ "to_char(t.out_factory_date,'yyyy-MM-dd'),\n"
				+ "to_char(t.first_register_date,'yyyy-MM-dd'),\n"
				+ "t.engine_code,\n" + "t.discern_code,\n" + "t.supplier,\n"
				+ "t.last_modified_by,\n"
				+ "getworkername(t.last_modified_by)lastModifiedName,\n"
				+ "to_char(t.last_modified_time,'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "t.dept_code,\n" + "getdeptname(t.dept_code)deptName\n"
				+ "from sp_c_carfile t\n" + "where t.is_use='Y'\n" +
				// "and t.dept_code='"+deptCode+"'" +
				"and t.enterprise_code='" + enterpriseCode + "'";
		if (flag != null && flag.equals("F"))// add by drdu 20100429
		{
			sql += " and t.dept_code in\n" + "       (select d.dept_code\n"
					+ "          from hr_c_dept d\n"
					+ "         where d.is_use = 'Y'\n" //update by sychen 20100902
//					+ "         where d.is_use = 'U'\n" 
					+ "         start with d.dept_code = '" + deptCode + "'\n"
					+ "        connect by prior d.dept_id = d.pdept_id)";
		}
		// add by sychen 20100612 车辆档案查询列表
		else if (flag != null && flag.equals("query"))
		{
			if (deptCode != null && !deptCode.equals("")){
				sql += "and t.dept_code='" + deptCode + "'";
			}
		}
		
		
		// add by sychen 20100612 end
		else {
			sql += "and t.dept_code='" + deptCode + "'";
		}
		if (likestr != null && !likestr.equals("")) {
			sql += "and t.car_no  like '%" + likestr + "%'";
		}
		String sqlCount = "select count(*) from (" + sql + ")\n";
		sql += " order by t.car_id";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public SpCCarordriverPhoto findByPhotoId(Long id, String type) {
		try {
			SpCCarordriverPhoto instance = null;
			String sql = "select t.* from sp_c_carordriver_photo t\n"
					+ "where t.car_driver_id=" + id + " and t.type='" + type
					+ "'";
			List<SpCCarordriverPhoto> list = bll.queryByNativeSQL(sql,
					SpCCarordriverPhoto.class);
			if (list != null && list.size() > 0)
				instance = list.get(0);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void saveCarordriverPhoto(SpCCarordriverPhoto entity) {
		try {
			entity.setId(bll.getMaxId("SP_C_CARORDRIVER_PHOTO", "id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpCCarordriverPhoto updateCarordriverPhoto(SpCCarordriverPhoto entity) {
		try {
			SpCCarordriverPhoto result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpCCarfile findByCarId(Long id) {
		try {
			SpCCarfile instance = entityManager.find(SpCCarfile.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean checkSpCcarfileCardNo(SpCCarfile entity) {
		String st = "select t.car_no  from  sp_c_carfile t  where t.car_no='"
				+ entity.getCarNo() + "'and t.is_use='Y'";
		if (entity.getCarId() != null)
			st += " and t.car_id <>" + entity.getCarId();
		int a = bll.exeNativeSQL(st);
		if (a > 0) {
			return true;
		} else

			return false;
	}

	public SpCCarfile saveSpCcarfile(SpCCarfile entity) {

		try {
			entity.setCarId(bll.getMaxId("sp_c_carfile", "car_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	public SpCCarfile updateSpCcarfile(SpCCarfile entity) {
		try {
			SpCCarfile result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void deleteSpCcarfile(String ids) {

		String sql = "update sp_c_carfile t\n" + "   set t.is_use = 'N'\n"
				+ " where t.car_id in (" + ids + ")";

		bll.exeNativeSQL(sql);

	}

	public long GetMaxCarId() throws Exception {

		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		long id = 0;
		String sql = "select nvl(max(car_id)+1,1) from sp_c_carfile";
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			id = Long.parseLong(obj.toString());
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public PageObject findSpCDriverList(String flag,String deptQuery,String likestr, String deptCode,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.driver_id,\n"
				+ "t.driver_name,\n"
				+ "t.sex,\n"
				+ "t.native_place_id,\n"
				+ "(select a.native_place_name from HR_C_NATIVE_PLACE a where t.native_place_id=a.native_place_id)nativePlace,\n"
				+ "to_char(t.brithday,'yyyy-MM-dd'),\n"
				+ "t.dept_code,\n"
				+ "getdeptname(t.dept_code)deptName,\n"
				+ "to_char(t.work_time,'yyyy-MM-dd'),\n"
				+ "t.home_phone,\n"
				+ "t.politics_id,\n"
				+ "(select a.politics_name from HR_C_POLITICS a where t.politics_id=a.politics_id)politics,\n"
				+ "to_char(t.join_in_time,'yyyy-MM-dd'),\n"
				+ "t.mobile_phone,\n" + "t.drive_code,\n"
				+ "t.allow_driver_type,\n" + "t.memo\n"
				+ "from sp_c_driver t\n" + "where t.is_use='Y'\n"
//				+ "and t.dept_code='" + deptCode + "'"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n";
		//update by ltong
		if (likestr != null && !likestr.equals("")) {
			sql += "and t.driver_name  like '%" + likestr + "%'";
		}
		
		// add by sychen 20100612 司机档案查询列表
		 if (flag != null && flag.equals("query"))
		{
			if (deptQuery != null && !deptQuery.equals("")){
				sql += "and t.dept_code='" + deptQuery + "'";
			}
		}
		 else {
			sql+= " and t.dept_code in\n" + "       (select d.dept_code\n"
				+ "          from hr_c_dept d\n"
				+ "         where d.is_use = 'Y'\n" //update by sychen 20100902
//				+ "         where d.is_use = 'U'\n" 
				+ "         start with d.dept_code = '" + deptCode + "'\n"
				+ "        connect by prior d.dept_id = d.pdept_id)";
		}
		// add by sychen 20100612 end
		
		// String sqlCount = "select count(*) from sp_c_driver t where
		// t.is_use='Y'\n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by t.driver_id";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	public SpCDriver findByDriverId(Long id) {
		try {
			SpCDriver instance = entityManager.find(SpCDriver.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean checkSpCDriverDriverCode(SpCDriver entity) {
		String st = "select t.drive_code  from  sp_c_driver t  where t.drive_code='"
				+ entity.getDriveCode() + "'and t.is_use='Y'";
		if (entity.getDriverId() != null)
			st += " and t.driver_id <>" + entity.getDriverId();
		int a = bll.exeNativeSQL(st);
		if (a > 0) {
			return true;
		} else

			return false;
	}

	public SpCDriver saveSpCDriver(SpCDriver entity) {

		try {
			entity.setDriverId(bll.getMaxId("sp_c_driver", "driver_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	public SpCDriver updateSpCDriver(SpCDriver entity) {
		try {
			SpCDriver result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void deleteSpCDriver(String ids) {

		String sql = "update sp_c_driver t\n" + "   set t.is_use = 'N'\n"
				+ " where t.driver_id in (" + ids + ")";

		bll.exeNativeSQL(sql);

	}

	public long GetMaxDriverId() throws Exception {

		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		long id = 0;
		String sql = "select nvl(max(driver_id)+1,1) from sp_c_driver";
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			id = Long.parseLong(obj.toString());
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public List getNativePlaceData() {
		String sql = "select t.native_place_id,t.native_place_name  from HR_C_NATIVE_PLACE t where t.is_use='Y'";
		List list = bll.queryByNativeSQL(sql);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List getPoliticsData() {
		String sql = "select t.politics_id,t.politics_name from HR_C_POLITICS t where t.is_use='Y'";
		List list = bll.queryByNativeSQL(sql);
		return list;
	}
	
	/**
	 * 车辆司机档案查询列表部门combox数据源
	 * add by sychen 20100612
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getDeptListForCarOrDriver(String flag) {
		String sql = "";
		if(flag!=null && flag.equals("car"))
			sql="SELECT DISTINCT a.dept_code,\n" +
				"                a.dept_name\n" + 
				"  FROM SP_C_CARFILE t,\n" + 
				"       hr_c_dept    a\n" + 
				" WHERE a.dept_code = t.dept_code";
		else if(flag!=null && flag.equals("driver")){
			sql="SELECT DISTINCT a.dept_code,\n" +
				"                a.dept_name\n" + 
				"  FROM SP_C_DRIVER t,\n" + 
				"       hr_c_dept   a\n" + 
				" WHERE a.dept_code = t.dept_code";
		}

		List list = bll.queryByNativeSQL(sql);
		return list;
	}
}
