package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 电动工具和电气安全用具清册 实现接口类
 * 
 * @author liuyi
 * 
 */
@Stateless
public class SpJToolsManagerImpl implements SpJToolsManager {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 删除 电气安全用具检修记录
	 * 
	 * @param ids
	 * @return
	 */
	public String deleteRepairEntity(String ids) {
		ToolLogger.log("删除电气安全用具检修记录开始", Level.INFO, null);
		String sql = "update SP_J_TOOLS_REPAIR a set a.is_use='N' where a.repair_id in ("
				+ ids + ")";
		try {
			bll.exeNativeSQL(sql);
			ToolLogger.log("删除电气安全用具检修记录结束", Level.INFO, null);
			return "数据删除成功！";
		} catch (Exception e) {
			e.printStackTrace();
			ToolLogger.log("删除电气安全用具检修记录失败", Level.SEVERE, e);
			return "数据删除失败！";
		}
	}

	/**
	 * 删除 电动工具和电气安全用具清册 多条删除
	 * 
	 * @param ids
	 *            删除ids
	 * @return
	 */
	public String deleteToolsEntity(String ids) {
		ToolLogger.log("删除电动工具和电气安全用具清册开始", Level.INFO, null);
		String sql = "update SP_C_TOOLS a set a.is_use='N' where a.tool_id in ("
				+ ids + ")";
		try {
			bll.exeNativeSQL(sql);
			ToolLogger.log("删除电动工具和电气安全用具清册结束", Level.INFO, null);
			return "数据删除成功！";
		} catch (Exception e) {
			e.printStackTrace();
			ToolLogger.log("删除电动工具和电气安全用具清册失败", Level.SEVERE, e);
			return "数据删除失败！";
		}
	}

	/**
	 * 查询符合条件的电动工具和电气安全用具清册
	 * 
	 * @param toolName
	 *            名称
	 * @param toolModel
	 *            规格型号
	 * @param toolType
	 *            类别
	 * @param rowStartAndIdxCount
	 * @return
	 */
	public PageObject findToolsByCondi(String toolName, String toolModel,
			String toolType, String enterpriseCode, String fillBy,
			String isFiltrate, int... rowStartIdxAndCount) {
		ToolLogger.log("查询电动工具和电气安全用具清册开始", Level.INFO, null);
		PageObject pg = new PageObject();
		String sql = "select a.tool_id," + " a.tool_code," + " a.tool_name,"
				+ " a.tool_type," + " a.tool_model,"
				+ " to_char(a.factory_date,'yyyy-mm-dd')," + " a.memo,"
				+ " a.charge_by," + " getworkername(a.charge_by)"
				+ " from SP_C_TOOLS a " + " where a.is_use='Y' "
				+ " and a.enterprise_code='" + enterpriseCode + "' ";
		if (toolName != null && !toolName.equals(""))
			sql += " and a.tool_name like '%" + toolName + "%' ";
		if (toolModel != null && !toolModel.equals(""))
			sql += " and a.tool_model like '%" + toolModel + "%' ";
		if (toolType != null && !toolType.equals("") && !"0".equals(toolType))
			sql += " and a.tool_type='" + toolType + "'  ";
		if ("1".equals(isFiltrate)) {
			sql += " and a.charge_by='" + fillBy + "'  ";
			// add by ltong 填写人过滤
		}
		String sqlCount = sql
				.replaceAll("select.*from", "select count(*) from");
		sql += " order by a.tool_type,a.tool_id  ";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);

		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));

		ToolLogger.log("查询电动工具和电气安全用具清册结束", Level.INFO, null);
		return pg;

	}

	/**
	 * 查询 电气安全用具检修记录
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param toolCode
	 *            编号
	 * @param toolType
	 *            类别
	 * @param rowStartAndIdxCount
	 * @return
	 */
	public PageObject findToolsRepairObject(String beginTime, String endTime,
			String toolCode, String toolType, String enterpriseCode,
			String isMaint, String fillBy, int... rowStartIdxAndCount) {
		ToolLogger.log("查询电气安全用具检修记录开始", Level.INFO, null);
		PageObject pg = new PageObject();
		String sql = "select a.repair_id," + "a.tool_id," + "a.belong_dep,"
				+ "getdeptname(a.belong_dep)," + "a.repair_result,"
				+ "to_char(a.repair_begin,'yyyy-mm-dd'),"
				+ "to_char(a.repair_end,'yyyy-mm-dd')," + "a.repair_by,"
				+ "getworkername(a.repair_by)," + "a.repair_dep,"
				+ "getdeptname(a.repair_dep),"
				+ "to_char(a.next_time,'yyyy-mm-dd')," + "a.memo,"
				+ "b.tool_code," + "b.tool_name," + "b.tool_type,"
				+ "b.tool_model," + "a.FILL_BY, " + "getworkername(a.fill_by) "
				+ "from SP_J_TOOLS_REPAIR a,SP_C_TOOLS b "
				+ "where a.tool_id=b.tool_id " + "and a.is_use='Y' "
				+ "and b.is_use='Y' " + "and a.enterprise_code='"
				+ enterpriseCode + "' " + "and b.enterprise_code='"
				+ enterpriseCode + "' ";
		if (beginTime != null && !beginTime.equals(""))
			sql += "and to_char(a.repair_begin,'yyyy-mm-dd') >= '" + beginTime
					+ "' ";
		if (endTime != null && !endTime.equals(""))
			sql += "and to_char(a.repair_end,'yyyy-mm-dd') <= '" + endTime
					+ "' ";
		if (toolCode != null && !toolCode.equals(""))
			sql += "and b.tool_code like '%" + toolCode + "%' ";
		if (toolType != null && !toolType.equals(""))
			sql += "and b.tool_type='" + toolType + "' ";
		if (isMaint != null && isMaint.equals("1"))
			sql += " and a.fill_by='" + fillBy + "'  ";

		String sqlCount = sql
				.replaceAll("select.*from", "select count(*) from");
		sql += "order by a.repair_id";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		ToolLogger.log("查询电气安全用具检修记录结束", Level.INFO, null);
		return pg;
	}

	/**
	 * 保存 电气安全用具检修记录
	 * 
	 * @param entity
	 * @return
	 */
	public String saveRepairEntity(SpJToolsRepair entity) {
		ToolLogger.log("保存电气安全用具检修记录开始", Level.INFO, null);
		String string = "";
		try {
			if (entity.getRepairId() == null)

			{
				entity.setRepairId(bll.getMaxId("SP_J_TOOLS_REPAIR",
						"REPAIR_ID"));
				entityManager.persist(entity);
				string = "数据新增成功！";
			} else {
				entityManager.merge(entity);
				string = "数据修改成功！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToolLogger.log("保存电气安全用具检修记录失败", Level.SEVERE, e);
			string = "数据保存失败！";
		}
		ToolLogger.log("保存电气安全用具检修记录结束", Level.INFO, null);
		return string;
	}

	/**
	 * 保存 电动工具和电气安全用具清册
	 * 
	 * @param entity
	 *            保存对象
	 * @return
	 */
	public String saveToolsEntity(SpCTools entity) {
		ToolLogger.log("保存电动工具和电气安全用具清册开始", Level.INFO, null);
		String string = "";
		try {
			if (entity.getToolId() == null)

			{
				entity.setToolId(bll.getMaxId("SP_C_TOOLS", "TOOL_ID"));
				entityManager.persist(entity);
				string = "数据新增成功！";
			} else {
				entityManager.merge(entity);
				string = "数据修改成功！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToolLogger.log("保存电动工具和电气安全用具清册失败", Level.SEVERE, e);
			string = "数据保存失败！";
		}
		ToolLogger.log("保存电动工具和电气安全用具清册结束", Level.INFO, null);
		return string;
	}

	/**
	 * 通过id查找检修记录
	 * 
	 * @param repairId
	 * @return
	 */
	public SpJToolsRepair findRepairById(Long repairId) {
		ToolLogger.log("通过id查找检修记录开始", Level.INFO, null);
		SpJToolsRepair entity = entityManager.find(SpJToolsRepair.class,
				repairId);
		ToolLogger.log("通过id查找检修记录结束", Level.INFO, null);
		return entity;
	}

	/**
	 * 通过id查找工具
	 * 
	 * @param toolId
	 * @return
	 */
	public SpCTools findToolById(Long toolId) {
		ToolLogger.log("通过id查找工具开始", Level.INFO, null);
		SpCTools entity = entityManager.find(SpCTools.class, toolId);
		ToolLogger.log("通过id查找工具结束", Level.INFO, null);
		return entity;
	}

}