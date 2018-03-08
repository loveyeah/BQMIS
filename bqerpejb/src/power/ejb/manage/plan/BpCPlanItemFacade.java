package power.ejb.manage.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;

/**
 * Facade for entity BpCPlanItem.
 * 
 * @see power.ejb.manage.plan.BpCPlanItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCPlanItemFacade implements BpCPlanItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public boolean save(BpCPlanItem entity) {
		try {
			if (("N").equals(entity.getIsItem())) {
				Object o = bll
						.getSingal("select 'C_'||(nvl(max(to_number(substr(t.item_code,3))),1)+1) from BP_C_PLAN_ITEM t where t.item_code like 'C_%' ");
				entity.setItemCode(o.toString());
			}
			String sql = "select count(1) from BP_C_PLAN_ITEM where ITEM_CODE='"
					+ entity.getItemCode() + "'";
			if (("0").equals(bll.getSingal(sql).toString())) {
				entityManager.persist(entity);
				return true;
			} else {
				return false;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean delete(BpCPlanItem entity) {
		try {
			entity = entityManager.getReference(BpCPlanItem.class, entity
					.getItemCode());
			entityManager.remove(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean update(BpCPlanItem entity, String oldItemCode) {
		try {
			String sql = "delete from BP_C_PLAN_ITEM t where t.ITEM_CODE='"
					+ oldItemCode + "'";
			// modify by fyyang 090824
			bll.exeNativeSQL(sql);
			if (("N").equals(entity.getIsItem())) {
				BpCPlanItem model = new BpCPlanItem();
				model.setItemName(entity.getItemName());
				model.setOrderBy(entity.getOrderBy());
				model.setIsItem("N");
				model.setFItemCode(entity.getFItemCode());
				Object o = bll
						.getSingal("select 'C_'||(nvl(max(to_number(substr(t.item_code,3))),1)+1) from BP_C_PLAN_ITEM t where t.item_code like 'C_%' ");
				model.setItemCode(o.toString());
				save(model);
			} else {
				save(entity);
			}
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCPlanItem findById(String id) {
		try {
			BpCPlanItem instance = entityManager.find(BpCPlanItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpCPlanItem> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from BpCPlanItem model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpCPlanItem> findAll() {
		try {
			final String queryString = "select model from BpCPlanItem model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 取得结点编码为node的所有第一层子结点
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findStatTreeList(String node, String enterpriseCode) {
		List<TreeNode> res = null;
		try {
			String sql = "SELECT t.item_code,\n" + "       t.item_name,\n"
					+ "       t.is_item,\n" + "       connect_by_isleaf\n"
					+ "  FROM BP_C_PLAN_ITEM t\n" + " WHERE LEVEL = 1\n"
					+ " START WITH t.f_item_code = '" + node + "'\n"
					+ "CONNECT BY PRIOR t.item_code = t.f_item_code\n"
					+ "       AND t.enterprise_code = '" + enterpriseCode
					+ "'\n" + " ORDER BY t.order_by,\n"
					+ "          t.item_code";
			List<Object[]> list = bll.queryByNativeSQL(sql);
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					n.setId(o[0].toString());
					if (o[1] != null)
						n.setText(o[1].toString());
					String isItem = "N";
					if (o[2] != null)
						isItem = o[2].toString();
					n.setDescription(isItem);
					if (o[3] != null)
						n.setLeaf(o[3].toString().equals("1") ? true : false);
					String icon = "";
					if (isItem.equals("N")) {
						icon = "box";
					} else {
						if (("Y").equals(isItem))
							icon = n.getLeaf() ? "my-iconCls" : "my-iconCls";
						else
							icon = n.getLeaf() ? "file" : "folder";
					}
					n.setIconCls(icon);
					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean isleaf(String itemCode) {
		String sql = "select count(*)\n" + "  from BP_C_PLAN_ITEM r\n"
				+ " where r.f_item_code = '" + itemCode + "'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	// 设置计算等级
	public BpCPlanItem setAccountOrder(BpCPlanItem entity) {
		// 公式计算
		if ("2".equals(entity.getCollectWay())) {
			// 扩展公式

			String sqlcount = "select count(*)\n"
					+ "  from bp_c_plan_formula t, bp_c_plan_item i\n"
					+ " where t.formula_content = i.item_code\n"
					+ "   and t.item_code = ?\n"
					// + " and t.fornula_type = '1'\n"
					+ "   and t.enterprise_code = i.enterprise_code\n"
					+ "   and t.enterprise_code = ?\n";
			if (bll.getSingal(
					sqlcount,
					new Object[] { entity.getItemCode(),
							entity.getEnterpriseCode() }).toString()
					.equals("0")) {
				entity.setAccountOrder(1L);
			} else {
				String sql = "select nvl(max(i.account_order), 1) + 1\n"
						+ "  from bp_c_plan_formula t, bp_c_plan_item i\n"
						+ " where t.formula_content = i.item_code\n"
						+ "   and t.item_code = ?\n"
						// + " and t.fornula_type = '1'\n"
						+ "   and t.enterprise_code = i.enterprise_code\n"
						+ "   and t.enterprise_code = ?\n";
				entity.setAccountOrder(Long.parseLong(bll.getSingal(
						sql,
						new Object[] { entity.getItemCode(),
								entity.getEnterpriseCode() }).toString()));

			}
		}

		String sqlupdate = "update bp_c_plan_item s" + " set s.account_order='"
				+ entity.getAccountOrder() + "'" + " where s.item_code='"
				+ entity.getItemCode() + "'";
		bll.exeNativeSQL(sqlupdate);

		return entity;
	}

	/**
	 * 获得所有通过公式向下关联的指标
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllReferItem(String itemCode) {
		String sql = "SELECT a.item_code,level" + " FROM bp_c_plan_formula a"
				+ " WHERE  a.item_code <> a.formula_content"
				+ " START WITH a.item_code = '" + itemCode + "'"
				+ " CONNECT BY PRIOR   a.formula_content = a.item_code "
				+ " order by level desc";
		List<Object[]> list = bll.queryByNativeSQL(sql);
		return list;

	}

}