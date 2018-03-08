package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sun.org.apache.bcel.internal.classfile.CodeException;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity CbmCCostItem.
 * 
 * @see power.ejb.manage.budget.CbmCCostItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCCostItemFacade implements CbmCCostItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved CbmCCostItem entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCCostItem entity to persist
	 * @throws CodeRepeatException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCCostItem entity) throws CodeRepeatException {
		try {
			String sql = "select count(*) from CBM_C_COST_ITEM a \n"
				+ "where a.is_use='Y' \n"
				+ "and a.item_code='" + entity.getItemCode() + "' \n"
				+ "and a.item_type='" + entity.getItemType() + "' \n";
			Long temp = Long.parseLong(bll.getSingal(sql).toString());
			if(temp > 0)
			{
				throw new CodeRepeatException("该类型的指标已存在！");
			}
			entity.setItemId(bll.getMaxId("CBM_C_COST_ITEM", "ITEM_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent CbmCCostItem entity.
	 * 
	 * @param entity
	 *            CbmCCostItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCCostItem entity) {
		try {
//			entity.setIsUse("N");
//			this.update(entity);
			String sql = "update CBM_C_COST_ITEM a set a.is_use='N' \n"
				+ "where a.item_id=" + entity.getItemId();
			bll.exeNativeSQL(sql);
			// entity = entityManager.getReference(CbmCCostItem.class, entity
			// .getItemId());
			// entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved CbmCCostItem entity and return it or a copy of
	 * it to the sender. A copy of the CbmCCostItem entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            CbmCCostItem entity to update
	 * @return CbmCCostItem the persisted CbmCCostItem entity instance, may not
	 *         be the same
	 * @throws CodeRepeatException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCCostItem update(CbmCCostItem entity) throws CodeRepeatException {
		try {
			String sql = "select count(*) from CBM_C_COST_ITEM a \n"
				+ "where a.is_use='Y' \n"
				+ "and a.item_code='" + entity.getItemCode() + "' \n"
				+ "and a.item_type='" + entity.getItemType() + "' \n"
				+ "and a.item_id<>" + entity.getItemId();
			Long temp = Long.parseLong(bll.getSingal(sql).toString());
			if(temp > 0)
			{
				throw new CodeRepeatException("该类型的指标已存在！");
			}
			CbmCCostItem result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCCostItem findById(Long id) {
		try {
			CbmCCostItem instance = entityManager.find(CbmCCostItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCCostItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCCostItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCCostItem> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCCostItem> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding CbmCCostItem instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmCCostItem model where model."
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
	 * Find all CbmCCostItem entities.
	 * 
	 * @return List<CbmCCostItem> all CbmCCostItem entities
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCCostItem> findAll(String id) {
		try {
			String sql ="select * from cbm_c_cost_item t where t.is_use='Y' and " +
					"t.item_id = '"+id+"' ";
			return bll.queryByNativeSQL(sql, CbmCCostItem.class);
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> findCostTreeList(String node) {
		List<TreeNode> res = null;
		String sql = "";
		sql = "select t.ITEM_ID,\n" + " t.item_code,\n"
				+ "       t.item_name,\n" + "       connect_by_isleaf \n"
				+ "  from cbm_c_cost_item t\n"
				+ "where is_use='Y' and level = 1\n"
				+ " start with t.P_ITEM_ID = ?\n"
				+ "connect by prior t.ITEM_ID = t.P_ITEM_ID\n"
				+ " order by t.order_by";
		List<Object[]> list = bll.queryByNativeSQL(sql, new Object[] { node });
		if (list != null && list.size() > 0) {
			res = new ArrayList<TreeNode>();
			for (Object[] o : list) {
				TreeNode n = new TreeNode();
				n.setId(o[0].toString());
				if (o[1] != null)
					n.setCode(o[1].toString());
				if (o[2] != null)
					n.setText(o[2].toString());
				if (o[3] != null)
					n.setLeaf(o[3].toString().equals("1") ? true : false);
				res.add(n);
			}
		}
		return res;
	}
	
	public Long getaccountOrder(Long id) {

		String sqlString = "select (max(a.account_order)+1) accountorder  from cbm_c_cost_item a\n"
				+ " where a.item_code in (select t.formula_content from\n"
				+ " Cbm_c_Cost_Formula t where t.fornula_type = 1 and t.item_id='"
				+ id + "'and t.is_use = 'Y')";
		if (bll.getSingal(sqlString) != null) {
			return Long.parseLong(bll.getSingal(sqlString).toString());
		} else {
			return 1l;
		}
	}
}