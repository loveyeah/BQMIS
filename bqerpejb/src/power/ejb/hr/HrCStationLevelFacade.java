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

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCStationLevel.
 * 
 * @see power.ejb.hr.HrCStationLevel
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCStationLevelFacade implements HrCStationLevelFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public HrCStationLevel save(HrCStationLevel entity)
			throws CodeRepeatException {
		LogUtil.log("saving HrCStationLevel instance", Level.INFO, null);
		try {
			if (!this.checkCodeSame(entity.getStationLevel())) {
				entity.setStationLevelId(bll.getMaxId("hr_c_station_level", "station_level_id"));
				entity.setIsUse("Y");//update by sychen 20100831
//				entity.setIsUse("U");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("编号不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids)
	{
		String sql="update hr_c_station_level t\n" +
			"   set t.is_use = 'N'\n" + 
			" where t.station_level_id in ("+ids+")";

       bll.exeNativeSQL(sql);
	}

	public HrCStationLevel update(HrCStationLevel entity) throws CodeRepeatException {
		LogUtil.log("updating HrCStationLevel instance", Level.INFO, null);
		try {
			if(!this.checkCodeSame(entity.getStationLevel(), entity.getStationLevelId()))
			{
			HrCStationLevel result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}else {
				throw new CodeRepeatException("编号不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCStationLevel findById(Long id) {
		LogUtil.log("finding HrCStationLevel instance with id: " + id,
				Level.INFO, null);
		try {
			HrCStationLevel instance = entityManager.find(
					HrCStationLevel.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findStationLevelList(String enterpriseCode,String stationLevelName,final int... rowStartIdxAndCount)
	{
		PageObject result = new PageObject();
		String sqlCount = "select count(*)\n" +
			"  from hr_c_station_level a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + //update by sychen 20100831
//			" where a.is_use = 'U' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.station_level_name like '%"+stationLevelName+"%'";
		Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(count > 0)
		{
			String sql = "select *\n" +
			"  from hr_c_station_level a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + //update by sychen 20100831
//			" where a.is_use = 'U' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.station_level_name like '%"+stationLevelName+"%'";
			List<HrCStationLevel> list = bll.queryByNativeSQL(sql, HrCStationLevel.class,
					rowStartIdxAndCount);
			result.setList(list);
			result.setTotalCount(count);
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	private boolean checkCodeSame(Long stationLevelCode,Long ... id)
	{
		String sql="select count(*)\n" +
			"from hr_c_station_level t\n" + 
			"where t.station_level = "+stationLevelCode+"";

		if(id!=null&&id.length>0)
		{
			sql=sql+" and t.station_level_id <>"+id[0];
		}
       int count=Integer.parseInt(bll.getSingal(sql).toString());
       if(count>0) return true;
       else return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<HrCStationLevel> findAllList(String enterpriseCode)
	{
		String sql = "select *\n" +
		"  from hr_c_station_level a\n" + 
		" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'";//update by sychen 20100831
//		" where a.is_use = 'U' and a.enterprise_code = '"+enterpriseCode+"'";
		List<HrCStationLevel> list = bll.queryByNativeSQL(sql, HrCStationLevel.class);
		
		return list;
	}
	
	  	/**
	  	 *  根据岗位ID查找级别
	  	 * @param deptId
	  	 * @param enterpriseCode
	  	 * @return
	  	 * @throws SQLException 
	  	 */
	  	@SuppressWarnings("unchecked")
	  	public PageObject findByStationId(String stationId,String enterpriseCode) throws SQLException {
	  		LogUtil.log("根据岗位ID查找级别信息开始: ",Level.INFO, null);
	          try {
	          	PageObject result = new PageObject();
	              // 查询sql
	              String sql=
	            	  "select b.station_level_id, b.station_level_name\n" +
	            	  "  from HR_C_STATION a, HR_C_STATION_LEVEL b\n" + 
	            	  " where a.station_level_id = b.station_level_id\n" + 
	            	  "   and a.is_use = 'Y'\n" + //update by sychen 20100831
	            	  "   and b.is_use = 'Y'\n" + 
//	            	  "   and a.is_use = 'U'\n" + 
//	            	  "   and b.is_use = 'U'\n" +
	            	  "   and b.enterprise_code = '"+enterpriseCode+"'\n" + 
	            	  "   and a.station_id = "+stationId+"";

	              // 打印sql文
	  			LogUtil.log("sql 文："+sql, Level.INFO, null);
	              // 执行查询
	              List list = bll.queryByNativeSQL(sql);
	              List<HrCStationLevel> arraylist = new ArrayList<HrCStationLevel>(
	  					list.size());
	  			Iterator it = list.iterator();
	  			while (it.hasNext()) {
	  				Object[] data = (Object[]) it.next();
	  				HrCStationLevel model = new HrCStationLevel();
	  				if (data[0] != null) {
	  					model.setStationLevelId(Long.parseLong(data[0].toString()));
	  				}
	  				if (data[1] != null) {
	  					model.setStationLevelName(data[1].toString());
	  				}
	  				arraylist.add(model);
	  			}
	              // 查询sql
	              String sqlCount =
	            	  "select count(1)\n" +
	            	  "  from (select b.station_level_id, b.station_level_name\n" + 
	            	  "          from HR_C_STATION a, HR_C_STATION_LEVEL b\n" + 
	            	  "         where a.station_level_id = b.station_level_id\n" + 
	            	  "           and a.is_use = 'Y'\n" +  //update by sychen 20100831
	            	  "           and b.is_use = 'Y'\n" + 
//	            	  "           and a.is_use = 'U'\n" + 
//	            	  "           and b.is_use = 'U'\n" +
	            	  "           and b.enterprise_code = '"+enterpriseCode+"'\n" + 
	            	  "           and a.station_id = "+stationId+")";

	              // 执行查询
	              Long totalCount = Long
	  				.parseLong(bll.getSingal(sqlCount).toString());
	              // 设置PageObject
	              result.setList(arraylist);
	              result.setTotalCount(totalCount);
	              // 返回
	              LogUtil.log("根据岗位ID查找级别信息结束: ",Level.INFO, null);
	              return result;
	          }catch(RuntimeException e){
	          	LogUtil.log("根据岗位ID查找级别信息失败: ",Level.INFO, null);
	  			throw new SQLException(e.getMessage());
	  		}
	  	}
	  	
	  	
	  	/**
	  	 * add by liuyi 091123
		 * 查找岗位级别
		 * @param enterpriseCode 企业编码
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public PageObject findAllStationLevels(String enterpriseCode){
			try{
				String sql = "SELECT S.STATION_LEVEL_ID,S.STATION_LEVEL_NAME" +
						"  FROM HR_C_STATION_LEVEL S" +
						" WHERE S.IS_USE = ? AND S.ENTERPRISE_CODE = ?" ;

				LogUtil.log("所有岗位级别id和名称开始。SQL=" + sql, Level.INFO, null);
				List list = bll.queryByNativeSQL(sql,new Object[] {"Y",enterpriseCode});
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
				LogUtil.log("查找所有岗位级别id和名称结束。", Level.INFO, null);
				return result;
			}catch (RuntimeException re) {
				LogUtil.log("查找所有岗位级别id和名称失败", Level.SEVERE, re);
				throw re;
			}
		}
}