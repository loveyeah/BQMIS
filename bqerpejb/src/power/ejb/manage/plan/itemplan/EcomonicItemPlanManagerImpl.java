package power.ejb.manage.plan.itemplan;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class EcomonicItemPlanManagerImpl implements EcomonicItemPlanManager {
	private static final List List = null;
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 查询全厂，分部门，计划，实际值，共通方法 返回的数组的取值顺序
	 * 0：主题ID，1：主题名称，2：主题显示顺序，3：经济指标ID，4：指标名称，5：指标别名，
	 * 6：单位Id，7：单位名称，8：分类，9：指标显示顺序，10：指标计划主ID，11：月份，12：计划工作流序号，
	 * 13：计划工作流状态，14：完成情况工作流序号，15：完成情况工作流状态，16：指标计划明细ID，
	 * 17：#11#12计划值，18：#1#2计划值，19：#11#12完成情况，20：#1#2完成情况
	 * @param isInsertData 分部门 插入最近数据  modified by liuyi 20100607
	 * @param topic
	 * @param month
	 * @param planStatus
	 *            计划状态 reported:已上报，approved:已审批
	 * @param realStatus
	 *            实际值状态 reported:已上报，approved:已审批
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount   isInsertData
	 * @return
	 */
//	public PageObject findItemByCondition(Long topic, String month,
//			String planStatus, String realStatus, String enterpriseCode,
//			int... rowStartIdxAndCount) {
	public PageObject findItemByCondition(String isInsertData,Long topic, String month,
			String planStatus, String realStatus, String enterpriseCode,
			int... rowStartIdxAndCount) {
		// 对比月份
		String monthFlag = month;
		PageObject pg = new PageObject();
		String sql = null;
		if (topic != null && topic == 1) {
			sql = "select a.topic_id,\n"
					+ "       a.topic_name,\n"
					+ "       a.display_no as topic_display,\n"
					+ "       b.economic_item_id,\n"
					+ "       b.item_name,\n"
					+ "       b.alias,\n"
					+ "       b.unit_id,\n"
					+ "       getunitname(b.unit_id) as unit_name,\n"
					+ "       b.item_type,\n"
					+ "       b.display_no as item_display,\n"
					+ "       c.plant_main_id,\n"
					+ "       c.ccmonth,\n"
					+ "       c.workflow_no_plan,\n"
					+ "       c.workflow_status_plan,\n"
					+ "       c.workflow_no_fact,\n"
					+ "       c.workflow_status_fact,\n"
					+ "       c.plant_detail_id,\n"
					+ "       c.plant_plan_1112,\n"
					+ "       c.plant_plan_12,\n"
					+ "       c.plant_fact_1112,\n"
					+ "       c.plant_fact_12\n"
					+ "  from BP_C_ITEMPLAN_TOPIC a,\n"
					+ "       BP_C_ITEMPLAN_ECO_ITEM b,\n"
					+ "       (select cc.plant_main_id,\n"
					+ "               to_char(cc.month, 'yyyy-mm') as ccmonth,\n"
					+ "               cc.workflow_no_plan,\n"
					+ "               cc.workflow_status_plan,\n"
					+ "               cc.workflow_no_fact,\n"
					+ "               cc.workflow_status_fact,\n"
					+ "               dd.plant_detail_id,\n"
					+ "               dd.economic_item_id,\n"
					+ "               dd.plant_plan_1112,\n"
					+ "               dd.plant_plan_12,\n"
					+ "               dd.plant_fact_1112,\n"
					+ "               dd.plant_fact_12\n"
					+ "          from BP_J_ITEMPLAN_PLANT_MAIN cc, BP_J_ITEMPLAN_PLANT_DETAIL dd\n"
					+ "         where cc.plant_main_id = dd.plant_main_id\n"
					+ "           and cc.is_use = 'Y'\n"
					+ "           and dd.is_use = 'Y'\n"
					+ "           and cc.enterprise_code = '" + enterpriseCode
					+ "'\n" + "           and dd.enterprise_code = '"
					+ enterpriseCode + "'\n";
			if (month != null)
				sql += "           and to_char(cc.month, 'yyyy-mm') = '"
						+ month + "' \n";
			if (planStatus != null && planStatus.equals("reported"))
				sql += " and cc.workflow_status_plan in ('1','2') \n";
			else if (planStatus != null && planStatus.equals("approved"))
				sql += " and cc.workflow_status_plan='2'";

			if (realStatus != null && realStatus.equals("reported"))
				sql += " and cc.workflow_status_fact in ('1','2') \n";
			else if (realStatus != null && realStatus.equals("approved"))
				sql += " and cc.workflow_status_fact='2' \n";
			sql += ") c \n " + " where a.topic_id = b.topic_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ "\n"
					+ "   and b.economic_item_id = c.economic_item_id(+) \n"
					+ " and a.display_no=1 \n"
					+ " order by b.display_no,b.item_type \n";
		} else if (topic != null && topic == 2) {
			
			// add by liuyi 20100607 取插入最近数据的月份
			if("Y".equals(isInsertData) && month != null){
				String insertDataSql = 
					"select max(to_char(a.month, 'yyyy-mm'))\n" +
					"  from bp_j_itemplan_dep_main a\n" + 
					" where a.is_use = 'Y'\n" + 
					"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
					"   and to_char(a.month, 'yyyy-mm') <= '"+month+"'";
				Object obj = bll.getSingal(insertDataSql);
				if(obj == null)
					monthFlag = month;
				else
					monthFlag = obj.toString();
			}
			

			
			sql = "select a.topic_id,\n"
					+ "       a.topic_name,\n"
					+ "       a.display_no as topic_display,\n"
					+ "       b.economic_item_id,\n"
					+ "       b.item_name,\n"
					+ "       b.alias,\n"
					+ "       b.unit_id,\n"
					+ "       getunitname(b.unit_id) as unit_name,\n"
					+ "       b.item_type,\n"
					+ "       b.display_no as item_display,\n"
					+ "       c.dep_main_id,\n"
					+ "       c.ccmonth,\n"
					+ "       c.workflow_no_plan,\n"
					+ "       c.workflow_status_plan,\n"
					+ "       c.workflow_no_fact,\n"
					+ "       c.workflow_status_fact,\n"
					+ "       c.dep_detail_id,\n"
					+ "       c.dep_plan_1112,\n"
					+ "       c.dep_plan_12,\n"
					+ "       c.dep_fact_1112,\n"
					+ "       c.dep_fact_12\n"
					+ "  from BP_C_ITEMPLAN_TOPIC a,\n"
					+ "       BP_C_ITEMPLAN_ECO_ITEM b,\n"
					+ "       (select cc.dep_main_id,\n"
					+ "               to_char(cc.month, 'yyyy-mm') as ccmonth,\n"
					+ "               cc.workflow_no_plan,\n"
					+ "               cc.workflow_status_plan,\n"
					+ "               cc.workflow_no_fact,\n"
					+ "               cc.workflow_status_fact,\n"
					+ "               dd.dep_detail_id,\n"
					+ "               dd.economic_item_id,\n"
					+ "               dd.dep_plan_1112,\n"
					+ "               dd.dep_plan_12,\n"
					+ "               dd.dep_fact_1112,\n"
					+ "               dd.dep_fact_12\n"
					+ "          from BP_J_ITEMPLAN_DEP_MAIN cc, BP_J_ITEMPLAN_DEP_DETAIL dd\n"
					+ "         where cc.dep_main_id = dd.dep_main_id\n"
					+ "           and cc.is_use = 'Y'\n"
					+ "           and dd.is_use = 'Y'\n"
					+ "           and cc.enterprise_code = '" + enterpriseCode
					+ "'\n" + "           and dd.enterprise_code = '"
					+ enterpriseCode + "'\n";
			if (month != null)
			{
				sql += "           and to_char(cc.month, 'yyyy-mm') = '"
					// modified by liuyi 20100607
//						+ month + "' \n ";
					+ monthFlag + "' \n ";
			}
			if (planStatus != null && planStatus.equals("reported"))
				sql += " and cc.workflow_status_plan in ('1','2') \n";
			else if (planStatus != null && planStatus.equals("approved"))
				sql += " and cc.workflow_status_plan = '2' ";

			if (realStatus != null && realStatus.equals("reported"))
				sql += " and cc.workflow_status_fact in ('1','2') \n";
			else if (realStatus != null && realStatus.equals("approved"))
				sql += " and cc.workflow_status_fact = '2' \n";

			sql += ") c\n where a.topic_id = b.topic_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.economic_item_id = c.economic_item_id(+)\n"
					+ "   and a.display_no=2\n"
					+ "   order by b.display_no,b.item_type";
		} else {
			sql = "select *\n"
					+ "from\n"
					+ "((select a.topic_id,\n"
					+ "       a.topic_name,\n"
					+ "       a.display_no as topic_display,\n"
					+ "       b.economic_item_id,\n"
					+ "       b.item_name,\n"
					+ "       b.alias,\n"
					+ "       b.unit_id,\n"
					+ "       getunitname(b.unit_id) as unit_name,\n"
					+ "       b.item_type,\n"
					+ "       b.display_no as item_display,\n"
					+ "       c.plant_main_id,\n"
					+ "       c.ccmonth,\n"
					+ "       c.workflow_no_plan,\n"
					+ "       c.workflow_status_plan,\n"
					+ "       c.workflow_no_fact,\n"
					+ "       c.workflow_status_fact,\n"
					+ "       c.plant_detail_id,\n"
					+ "       c.plant_plan_1112,\n"
					+ "       c.plant_plan_12,\n"
					+ "       c.plant_fact_1112,\n"
					+ "       c.plant_fact_12\n"
					+ "  from BP_C_ITEMPLAN_TOPIC a,\n"
					+ "       BP_C_ITEMPLAN_ECO_ITEM b,\n"
					+ "       (select cc.plant_main_id,\n"
					+ "               to_char(cc.month, 'yyyy-mm') as ccmonth,\n"
					+ "               cc.workflow_no_plan,\n"
					+ "               cc.workflow_status_plan,\n"
					+ "               cc.workflow_no_fact,\n"
					+ "               cc.workflow_status_fact,\n"
					+ "               dd.plant_detail_id,\n"
					+ "               dd.economic_item_id,\n"
					+ "               dd.plant_plan_1112,\n"
					+ "               dd.plant_plan_12,\n"
					+ "               dd.plant_fact_1112,\n"
					+ "               dd.plant_fact_12\n"
					+ "          from BP_J_ITEMPLAN_PLANT_MAIN cc, BP_J_ITEMPLAN_PLANT_DETAIL dd\n"
					+ "         where cc.plant_main_id = dd.plant_main_id\n"
					+ "           and cc.is_use = 'Y'\n"
					+ "           and dd.is_use = 'Y'\n"
					+ "           and cc.enterprise_code = '" + enterpriseCode
					+ "'\n" + "           and dd.enterprise_code = '"
					+ enterpriseCode + "'\n";
			if (month != null)
				sql += "           and to_char(cc.month, 'yyyy-mm') = '"
						+ month + "'\n";
			if (planStatus != null && planStatus.equals("reported"))
				sql += " and cc.workflow_status_plan in ('1','2') \n";
			else if (planStatus != null && planStatus.equals("approved"))
				sql += " and cc.workflow_status_plan='2'";

			if (realStatus != null && realStatus.equals("reported"))
				sql += " and cc.workflow_status_fact in ('1','2') \n";
			else if (realStatus != null && realStatus.equals("approved"))
				sql += " and cc.workflow_status_fact='2' \n";

			sql += "           ) c\n" + " where a.topic_id = b.topic_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and b.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and b.economic_item_id = c.economic_item_id(+)\n"
					+ "   and a.display_no=1\n"
					+ "   )\n"
					+ "   union\n"
					+ "   (select a.topic_id,\n"
					+ "       a.topic_name,\n"
					+ "       a.display_no as topic_display,\n"
					+ "       b.economic_item_id,\n"
					+ "       b.item_name,\n"
					+ "       b.alias,\n"
					+ "       b.unit_id,\n"
					+ "       getunitname(b.unit_id) as unit_name,\n"
					+ "       b.item_type,\n"
					+ "       b.display_no as item_display,\n"
					+ "       c.dep_main_id,\n"
					+ "       c.ccmonth,\n"
					+ "       c.workflow_no_plan,\n"
					+ "       c.workflow_status_plan,\n"
					+ "       c.workflow_no_fact,\n"
					+ "       c.workflow_status_fact,\n"
					+ "       c.dep_detail_id,\n"
					+ "       c.dep_plan_1112,\n"
					+ "       c.dep_plan_12,\n"
					+ "       c.dep_fact_1112,\n"
					+ "       c.dep_fact_12\n"
					+ "  from BP_C_ITEMPLAN_TOPIC a,\n"
					+ "       BP_C_ITEMPLAN_ECO_ITEM b,\n"
					+ "       (select cc.dep_main_id,\n"
					+ "               to_char(cc.month, 'yyyy-mm') as ccmonth,\n"
					+ "               cc.workflow_no_plan,\n"
					+ "               cc.workflow_status_plan,\n"
					+ "               cc.workflow_no_fact,\n"
					+ "               cc.workflow_status_fact,\n"
					+ "               dd.dep_detail_id,\n"
					+ "               dd.economic_item_id,\n"
					+ "               dd.dep_plan_1112,\n"
					+ "               dd.dep_plan_12,\n"
					+ "               dd.dep_fact_1112,\n"
					+ "               dd.dep_fact_12\n"
					+ "          from BP_J_ITEMPLAN_DEP_MAIN cc, BP_J_ITEMPLAN_DEP_DETAIL dd\n"
					+ "         where cc.dep_main_id = dd.dep_main_id\n"
					+ "           and cc.is_use = 'Y'\n"
					+ "           and dd.is_use = 'Y'\n"
					+ "           and cc.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "           and dd.enterprise_code = '"
					+ enterpriseCode + "'\n";
			if (month != null)
				sql += "           and to_char(cc.month, 'yyyy-mm') = '"
						+ month + "'";
			if (planStatus != null && planStatus.equals("reported"))
				sql += " and cc.workflow_status_plan in ('1','2') \n";
			else if (planStatus != null && planStatus.equals("approved"))
				sql += " and cc.workflow_status_plan='2'";

			if (realStatus != null && realStatus.equals("reported"))
				sql += " and cc.workflow_status_fact in ('1','2') \n";
			else if (realStatus != null && realStatus.equals("approved"))
				sql += " and cc.workflow_status_fact='2' \n";
			sql += ") c\n  where a.topic_id = b.topic_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.economic_item_id = c.economic_item_id(+)\n"
					+ "   and a.display_no=2\n" + ")\n" + ")tt\n"
					+ "order by tt.topic_display,tt.item_type,tt.item_display";

		}

		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		
		// add by liuyi 20100607  对插入最近修改数据处理
		if("Y".equals(isInsertData) && month != null && !month.equals(monthFlag)){
			for(Object obj : list){
				Object[] arrObjects = (Object[])obj;
				for(int j = 10; j<= 16; j++)
					arrObjects[j] = null;
			}
		}
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}

	/**
	 * 取得所有的维护主题
	 * 
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 */
	public PageObject getAllTopicRecord(String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.topic_id,\n" + " a.topic_name,\n"
				+ " a.topic_memo,\n" + " a.display_no\n"
				+ " from BP_C_ITEMPLAN_TOPIC a\n" + " where a.is_use='Y'\n"
				+ " and a.enterprise_code='" + enterpriseCode + "'\n";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by a.display_no";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		;
		return pg;
	}

	/**
	 * 通过主题id取得该主题下的所有指标,只为维护用
	 * 
	 * @param topic
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getItemByTopic(Long topic, String enterpriseCode,
			int... rowStartIdxAndCount) {

		PageObject pg = new PageObject();
		String sql = "select a.economic_item_id,\n"
				+ " a.topic_id,\n"
				+ " a.item_code,\n"
				+ " a.item_name,\n"
				+ " a.alias,\n"
				+ " a.unit_id,\n"
				+ "(select t.unit_name from bp_c_measure_unit t where t.unit_id=a.unit_id) unit_name,\n"
				+ " a.item_type,\n" + " a.display_no orderBy\n"
				+ " from BP_C_ITEMPLAN_ECO_ITEM a\n" + " where a.is_use='Y'\n"
				+ " and a.topic_id='" + topic + "'"
				+ " and a.enterprise_code='" + enterpriseCode + "'\n";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by a.display_no,a.item_type";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		;
		return pg;

	}

	/**
	 * 批量修改维护指标数据
	 * 
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveModifiedItem(List<BpCItemplanEcoItem> addList,
			List<BpCItemplanEcoItem> updateList, String ids) {
		if (addList != null && addList.size() > 0) {
			for (BpCItemplanEcoItem entity : addList) {
				entity.setEconomicItemId(bll.getMaxId("BP_C_ITEMPLAN_ECO_ITEM",
						"ECONOMIC_ITEM_ID"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (BpCItemplanEcoItem entity : updateList) {
				entityManager.merge(entity);
			}
		}
		if (ids != null && ids.length() > 0) {
			String sql = "update BP_C_ITEMPLAN_ECO_ITEM a set a.is_use='N' where a.ECONOMIC_ITEM_ID in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);
		}

	}

	/**
	 * 批量修改维护主题数据
	 * 
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public void saveModifiedTopic(List<BpCItemplanTopic> addList,
			List<BpCItemplanTopic> updateList, String ids) {

		if (addList != null && addList.size() > 0) {
			for (BpCItemplanTopic entity : addList) {
				entity.setTopicId(bll.getMaxId("BP_C_ITEMPLAN_TOPIC",
						"topic_id"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (BpCItemplanTopic entity : updateList) {
				entityManager.merge(entity);
			}
		}
		if (ids != null && ids.length() > 0) {
			String sql = "update BP_C_ITEMPLAN_TOPIC a set a.is_use='N' where a.topic_id in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);
		}

		// TODO Auto-generated method stub

	}

	/**
	 * 批量保存各部门指标计划
	 * 
	 * @param basic
	 *            主表数据
	 * @param addList
	 *            明细增加数据
	 * @param updateList
	 *            明细修改数据
	 */
	public void savePartItemPlanAndActual(BpJItemplanDepMain basic,
			List<BpJItemplanDepDetail> addList,
			List<BpJItemplanDepDetail> updateList) {
		if (basic.getDepMainId() == null) {
			basic.setDepMainId(bll.getMaxId("BP_J_ITEMPLAN_DEP_MAIN",
					"DEP_MAIN_ID"));
			entityManager.persist(basic);
		} else
			entityManager.merge(basic);
		if (addList != null && addList.size() > 0) {
			for (BpJItemplanDepDetail entity : addList) {
				entity.setDepMainId(basic.getDepMainId());
				entity.setDepDetailId(bll.getMaxId("BP_J_ITEMPLAN_DEP_DETAIL",
						"DEP_DETAIL_ID"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (BpJItemplanDepDetail entity : updateList) {
				entityManager.merge(entity);
			}
		}
	}

	/**
	 * 批量保存全厂指标计划数据
	 * 
	 * @param basic
	 *            主表数据
	 * @param addList
	 *            明细增加数据
	 * @param updateList
	 *            明细修改数据
	 */
	public void saveWholeItemPlanAndActual(BpJItemplanPlantMain basic,
			List<BpJItemplanPlantDetail> addList,
			List<BpJItemplanPlantDetail> updateList) {
		if (basic.getPlantMainId() == null) {
			basic.setPlantMainId(bll.getMaxId("BP_J_ITEMPLAN_PLANT_DETAIL",
					"PLANT_DETAIL_ID"));
			entityManager.persist(basic);
		} else
			entityManager.merge(basic);
		if (addList != null && addList.size() > 0) {
			for (BpJItemplanPlantDetail entity : addList) {
				entity.setPlantMainId(basic.getPlantMainId());
				entity.setPlantDetailId(bll.getMaxId(
						"BP_J_ITEMPLAN_PLANT_DETAIL", "PLANT_DETAIL_ID"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (BpJItemplanPlantDetail entity : updateList) {
				entityManager.merge(entity);
			}
		}

	}

	public BpJItemplanPlantMain findWholeObject(Long plantId) {
		BpJItemplanPlantMain instance = null;
		if (plantId != null)
			instance = entityManager.find(BpJItemplanPlantMain.class, plantId);
		else
			instance = new BpJItemplanPlantMain();
		return instance;
	}

	/**
	 * 通过id找到全厂指标中的对象
	 * 
	 * @param plantId
	 * @return
	 */
	public BpJItemplanDepMain findPartObject(Long depId) {
		BpJItemplanDepMain instance = null;
		if (depId != null)
			instance = entityManager.find(BpJItemplanDepMain.class, depId);
		else
			instance = new BpJItemplanDepMain();
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List judgeApprovePlan(String planId, String entityIds1,
			String entityIds2) {
		String sql = "select count(c.dep_main_id)\n"
				+ "          from BP_J_ITEMPLAN_DEP_MAIN c\n"
				+ "where c.workflow_no_plan in (" + entityIds1 + ")\n"
				+ "and c.dep_main_id= " + planId + "\n" + "union all\n"
				+ "select count(d.dep_main_id)\n"
				+ "          from BP_J_ITEMPLAN_DEP_MAIN d\n"
				+ "where d.workflow_no_fact in(" + entityIds2 + ")\n"
				+ "and d.dep_main_id= " + planId + "\n";
		List list1 = bll.queryByNativeSQL(sql);
		return list1;
	}

	public List judgeApproveFact(String planId, String entityIds1,
			String entityIds2) {

		String sql = "select count(c.PLANT_MAIN_ID)\n"
				+ "          from BP_J_ITEMPLAN_PLANT_MAIN c\n"
				+ "where c.workflow_no_plan in (" + entityIds1 + ")\n"
				+ "and c.PLANT_MAIN_ID= " + planId + "\n" + "union all\n"
				+ "select count(d.PLANT_MAIN_ID)\n"
				+ "          from BP_J_ITEMPLAN_PLANT_MAIN d\n"
				+ "where d.workflow_no_fact in(" + entityIds2 + ")\n"
				+ "and d.PLANT_MAIN_ID= " + planId + "\n";
		List list1 = bll.queryByNativeSQL(sql);
		return list1;
	}
}