package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;

/**
 * Facade for entity CbmCModel.
 * 
 * @see power.ejb.manage.budget.CbmCModel
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCModelFacade implements CbmCModelFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved CbmCModel entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCModel entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCModel entity) {
		try {
			entity.setModelOrder(1l);
			entity.setIsUse("Y");
			entity.setModelItemId(bll.getMaxId("CBM_C_MODEL", "MODEL_ITEM_ID"));
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent CbmCModel entity.
	 * 
	 * @param entity
	 *            CbmCModel entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCModel entity) {
		try {
			entity = entityManager.getReference(CbmCModel.class, entity
					.getModelItemId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved CbmCModel entity and return it or a copy of it
	 * to the sender. A copy of the CbmCModel entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            CbmCModel entity to update
	 * @return CbmCModel the persisted CbmCModel entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCModel update(CbmCModel entity) {
		try {

			if (entity.getIsItem().equals("Y")
					&& entity.getModelItemExplain().length() > 0) {
				entity.setModelOrder(this.getmodelOrder(entity.getModelItemId()
						.toString()));
			}
			CbmCModel result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCModel findById(Long id) {
		try {
			CbmCModel instance = entityManager.find(CbmCModel.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCModel entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCModel property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCModel> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCModel> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from CbmCModel model where model."
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
	 * Find all CbmCModel entities.
	 * 
	 * @return List<CbmCModel> all CbmCModel entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String argFuzzy) {
		try {
			PageObject pg = new PageObject();
			final String queryString = "select * from cbm_c_model a\n"
					+ "where a.model_item_code like '" + argFuzzy + "%'\n"
					+ " or a.model_item_name like '" + argFuzzy + "%'"
					+ " and is_item ='Y'";
			String sqlcount = "select count(1) from cbm_c_model a\n"
					+ "where a.model_item_code like '" + argFuzzy + "%'\n"
					+ " or a.model_item_name like '" + argFuzzy + "%'"
					+ " and is_item ='Y'";

			List<CbmCModel> query = bll.queryByNativeSQL(queryString,
					CbmCModel.class);
			Long count = null;
			if (bll.getSingal(sqlcount) != null)
				count = Long.parseLong(bll.getSingal(sqlcount).toString());
			pg.setList(query);
			pg.setTotalCount(count);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> findModelTreeList(String node, String enterpriseCode) {
		List<TreeNode> res = null;
		try {
			String sql = "select t.MODEL_ITEM_ID,\n"
					+ "       t.MODEL_ITEM_NAME,\n" + "       t.MODEL_TYPE,\n"
					+ "       t.is_item,\n" + "       connect_by_isleaf,\n"
					+ "       t.DISPLAY_NO \n" + "  from CBM_C_MODEL t\n"
					+ "where level = 1\n" + " start with t.DADDY_ITEM_ID = ?\n"
					+ "connect by prior t.MODEL_ITEM_ID = t.DADDY_ITEM_ID\n"
					+ " order by t.DISPLAY_NO";
			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { node });
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					n.setId(o[0].toString());
					if (o[1] != null)
						n.setText(o[1].toString());
					if (o[2] != null)
						n.setCode(o[2].toString());
					String isItem = "N";
					if (o[3] != null)
						isItem = o[3].toString();
					n.setDescription(isItem);

					if (o[4] != null)
						n.setLeaf(o[4].toString().equals("1") ? true : false);
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
					if (o[5] != null)
						n.setOpenType(o[5].toString());
					res.add(n);
				}

			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	// 计算等级
	private Long getmodelOrder(String modelId) {
		String sqlString = "select (max(t.model_order) + 1) accountorder\n"
				+ "  from cbm_c_model t\n" + " where t.model_item_code in\n"
				+ "       (select d.formula_content\n"
				+ "          from cbm_c_model_formula d\n"
				+ "         where d.model_item_id = '" + modelId + "'\n"
				+ "         and d.fornula_type='1'\n"
				+ "         and d.is_use ='Y')";

		return Long.parseLong(bll.getSingal(sqlString).toString());
	}
}