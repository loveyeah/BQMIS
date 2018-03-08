package power.ejb.manage.plan.itemplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class TecItemPlanManagerImpl implements TecItemPlanManager {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	// 维护页面

	// 过滤已经存在部门
	private boolean judgeDeptByDeptCode(String deptCode, Long dId) {
		String sql = "select count(1) from BP_C_ITEMPLAN_TEC_DEP a where a.is_use='Y' and a.DEP_CODE='"
				+ deptCode + "'";
		if (dId != null) {
			sql += " and a.DEP_ID <>" + dId + "";

		}
		return Long.parseLong(bll.getSingal(sql).toString()) == 0l ? true
				: false;
	}

	public void saveTecDept(List<BpCItemplanTecDep> addList,
			List<BpCItemplanTecDep> updateList, String ids) {
		if (addList.size() > 0) {
			for (BpCItemplanTecDep model : addList) {
				if (this.judgeDeptByDeptCode(model.getDepCode(), null)) {
					model.setDepId(bll.getMaxId("BP_C_ITEMPLAN_TEC_DEP",
							"DEP_ID"));
					entityManager.persist(model);
					entityManager.flush();
				}
			}
		}
		if (updateList.size() > 0) {
			for (BpCItemplanTecDep model : updateList) {
				if (this.judgeItemByItemCode(model.getDepCode(), model
						.getDepId()))
					entityManager.merge(model);
			}
		}
		if (ids.length() > 0) {

			String sql = "update BP_C_ITEMPLAN_TEC_DEP a set a.is_use ='N' where a.DEP_ID in("
					+ ids + ")";

			String deptSql = "update bp_j_itemplan_tec_detail a set a.is_use='N' where a.DEP_ID in("
					+ ids + ")";

			bll.exeNativeSQL(sql);
			bll.exeNativeSQL(deptSql);
		}

	}

	// 过滤已经存在指标
	private boolean judgeItemByItemCode(String itemCode, Long tId) {
		String sql = "select count(1) from BP_C_ITEMPLAN_TEC_ITEM a where a.is_use='Y' and a.item_code='"
				+ itemCode + "'";
		if (tId != null) {
			sql += " and a.technology_item_id <>" + tId + "";

		}
		return Long.parseLong(bll.getSingal(sql).toString()) == 0l ? true
				: false;
	}

	public void saveTecItem(List<BpCItemplanTecItem> addList,
			List<BpCItemplanTecItem> updateList, String ids) {
		if (addList.size() > 0) {
			for (BpCItemplanTecItem model : addList) {
				if (this.judgeItemByItemCode(model.getItemCode(), model
						.getTechnologyItemId())) {
					model.setTechnologyItemId(bll.getMaxId(
							"BP_C_ITEMPLAN_TEC_ITEM", "TECHNOLOGY_ITEM_ID"));
					entityManager.persist(model);
					entityManager.flush();
				}
			}
		}
		if (updateList.size() > 0) {
			for (BpCItemplanTecItem model : updateList) {
				if (this.judgeItemByItemCode(model.getItemCode(), model
						.getTechnologyItemId()))
					entityManager.merge(model);
			}
		}
		if (ids.length() > 0) {

			String sql = "update BP_C_ITEMPLAN_TEC_ITEM a set a.is_use ='N' where a.technology_item_id in("
					+ ids + ")";

			String deptSql = "update bp_j_itemplan_tec_detail a set a.is_use='N' where a.technology_item_id in("
					+ ids + ")";
			bll.exeNativeSQL(sql);
			bll.exeNativeSQL(deptSql);
			entityManager.flush();
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getTecDeptList(int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select * from BP_C_ITEMPLAN_TEC_DEP a where a.is_use='Y' order by a.display_no";
		String sqlCount = "select count(1)from BP_C_ITEMPLAN_TEC_DEP a where a.is_use='Y'";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(count);
		return pg;
	}

	@SuppressWarnings("unchecked")
	public PageObject getTecItemList(int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.*,getunitname(a.unit_id) from BP_C_ITEMPLAN_TEC_ITEM a where a.is_use='Y' order by a.display_no";
		String sqlCount = "select count(1)from BP_C_ITEMPLAN_TEC_ITEM a where a.is_use='Y'";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(count);
		return pg;
	}

	// 月度全厂技术指标

	// 判断部门、指标是否存在改月份下是否存在
	private boolean judgeTecDetail(Long deptId, Long tecItemId, Long detailId,
			Long mainId) {
		String sql = "select count(1) from BP_J_ITEMPLAN_TEC_DETAIL a where a.is_use = 'Y'"
				+ " and a.dep_id='"
				+ deptId
				+ "'and a.technology_item_id='"
				+ tecItemId + "' and a.tec_main_id='" + mainId + "'";
		if (detailId != null) {
			sql += " and a.tec_detail_id <>" + detailId + "";

		}
		return Long.parseLong(bll.getSingal(sql).toString()) == 0l ? true
				: false;
	}

	// 增加明细主表
	private Long addTecMain(String month, String enterpriseCode)
			throws ParseException {
		Long tecMainId;
		String sql = "select a.tec_main_id from BP_J_ITEMPLAN_TEC_MAIN a where to_char(a.month,'yyyy-mm')='"
				+ month + "'";
		if (bll.getSingal(sql) == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			BpJItemplanTecMain entity = new BpJItemplanTecMain();
			tecMainId = bll.getMaxId("BP_J_ITEMPLAN_TEC_MAIN", "TEC_MAIN_ID");
			entity.setTecMainId(tecMainId);
			entity.setIsUse("Y");
			entity.setMonth(df.parse(month));
			entity.setWorkflowStatusPlan(0l);
			entity.setEnterpriseCode(enterpriseCode);
			entityManager.persist(entity);

		} else {
			tecMainId = Long.parseLong(bll.getSingal(sql).toString());
		}
		return tecMainId;
	}

	public void saveTecDetail(List<BpJItemplanTecDetail> addList,
			List<BpJItemplanTecDetail> updateList, String month, String ids,
			String enterpriseCode) throws ParseException {
		if (addList.size() > 0) {
			Long tecMainId = this.addTecMain(month, enterpriseCode);
			for (BpJItemplanTecDetail model : addList) {
				if (this.judgeTecDetail(model.getDepId(), model
						.getTechnologyItemId(), null, tecMainId)) {
					model.setTecDetailId(bll.getMaxId(
							"BP_J_ITEMPLAN_TEC_DETAIL", "TEC_DETAIL_ID"));
					model.setTecMainId(tecMainId);
					entityManager.persist(model);
					entityManager.flush();
				}
			}
		}
		if (updateList.size() > 0) {
			for (BpJItemplanTecDetail model : updateList) {
				if (this.judgeTecDetail(model.getDepId(), model
						.getTechnologyItemId(), model.getTecDetailId(), model
						.getTecMainId()))
					entityManager.merge(model);
			}
		}
		if (ids.length() > 0) {
			String sql = "update BP_J_ITEMPLAN_TEC_DETAIL a set a.is_use ='N' where a.tec_detail_id in("
					+ ids + ")";
			bll.exeNativeSQL(sql);
			entityManager.flush();
		}

	}

	@SuppressWarnings("unchecked")
	public PageObject getTecDeptItemList(String entryIds, String month,
			String status, String deptId, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.tec_detail_id,a.tec_main_id,a.dep_id,a.technology_item_id,(select c.dep_name\n"
				+ "          from BP_C_ITEMPLAN_TEC_DEP c\n"
				+ "         where c.dep_id = a.dep_id) deptName,\n"
				+ "       (select d.alias\n"
				+ "          from BP_C_ITEMPLAN_TEC_ITEM d\n"
				+ "         where d.technology_item_id = a.technology_item_id) itemName,\n"
				+ "         a.tec_plan,a.tec_fact,b.workflow_status_plan,b.workflow_no_plan,b.workflow_status_fact,b.workflow_no_fact\n"
				+ "  from BP_J_ITEMPLAN_TEC_DETAIL a, BP_J_ITEMPLAN_TEC_MAIN b\n"
				+ " where a.tec_main_id = b.tec_main_id and a.is_use ='Y'\n"
				+ "   and to_char(b.month ,'yyyy-mm') = '" + month + "'";
		// 填写审批
		if (status != null && status.equals("approve")) {

			sql += "  and b.workflow_no_plan in(" + entryIds + ")";
		}
		// if(status!=null && status.equals("finish")){
		// sql+=" and b.workflow_status_fact in(0,3)";
		// }
		// 完成情况审批
		if (status != null && status.equals("Fapprove")) {
			sql += "  and b.workflow_no_fact in(" + entryIds + ")";
		}

		// 技术指标填写数据查询
		if (deptId != null && !deptId.equals("")) {
			sql += "  and a.dep_id = '" + deptId + "'";
		}
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		String sqlconut = "select count(1) from (" + sql + ")";
		Long count = Long.parseLong(bll.getSingal(sqlconut).toString());
		pg.setList(list);
		pg.setTotalCount(count);
		return pg;
	}
}
