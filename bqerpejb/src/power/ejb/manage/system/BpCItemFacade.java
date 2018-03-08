package power.ejb.manage.system;

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
 * Facade for entity BpCItem.
 * 
 * @see power.ejb.manage.system.BpCItem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCItemFacade implements BpCItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public String save(BpCItem entity) {
		try {
			entity.setItemCode(this.getItemcode(entity.getItemName()));
			if (entity.getRetrieveCode() == null)
				entity.setRetrieveCode(this.findRetrieveCode(entity
						.getItemName()));
			entityManager.persist(entity);
			return entity.getItemCode();
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(BpCItem entity) {
		LogUtil.log("deleting BpCItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCItem.class, entity
					.getItemCode());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCItem update(BpCItem entity) {
		LogUtil.log("updating BpCItem instance", Level.INFO, null);
		try {
			entity.setItemCode(this.getItemcode(entity.getItemName()));
			BpCItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCItem findById(String id) {
		LogUtil
				.log("finding BpCItem instance with id: " + id, Level.INFO,
						null);
		try {
			BpCItem instance = entityManager.find(BpCItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpCItem> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding BpCItem instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCItem model where model."
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
	public List<BpCItem> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all BpCItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpCItem model";
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
	
	@SuppressWarnings("unchecked")
	public List<TreeNode> findItemTreeList(String node, String enterpriseCode,String searchKey) {
		try {
			String sql = "";
			if(searchKey == null || "".equals(searchKey))
			{
			 sql = "select t.item_code, t.item_name, t.is_item, connect_by_isleaf,t.unit_code,t.retrieve_code ,getunitname(t.unit_code)\n"
					+ "  from bp_c_item t\n"
					+ " where level = 1\n"
					+ " start with t.parent_item_code = '"+node+"' and t.enterprise_code='"+enterpriseCode+"' \n"
					+ "connect by prior t.item_code = t.parent_item_code\n"
					+ " order by t.order_by";
			}
			else
			{
				sql = "select distinct t.item_code, t.item_name, t.is_item, connect_by_isleaf,t.unit_code,t.retrieve_code,getunitname(t.unit_code)\n"
					+ " ,t.order_by from ("+
					"select *\n" +
					"  from bp_c_item t\n" + 
					" where t.enterprise_code = '"+enterpriseCode+"'\n" + 
					" start with t.item_code || t.item_name || t.retrieve_code like '%"+searchKey+"%'\n" + 
					"connect by prior t.parent_item_code = t.item_code" 
					+") t\n"
					+ " where level = 1\n"
					+ " start with t.parent_item_code = '"+node+"' and t.enterprise_code='"+enterpriseCode+"' \n"
					+ "connect by prior t.item_code = t.parent_item_code\n"
					+ " order by t.order_by";

			} 
			List<Object[]> res = bll.queryByNativeSQL(sql);
			List<TreeNode> list = null;
			if (res != null && res.size() > 0) {
				list = new ArrayList<TreeNode>();
				for (Object[] o : res) {
					TreeNode tn = new TreeNode();
					tn.setId(o[0].toString());
					if (o[1] != null)
						tn.setText(o[1].toString());
					// 把是否指标属性存入说明中
					if (o[2] != null)
						tn.setDescription(o[2].toString());
					tn.setLeaf("0".equals(o[3].toString()) ? false : true);
					list.add(tn);
					// 把指标单位临时存入地址中
					if (o[4] != null)
						tn.setHref(o[4].toString());
					// 把检索码临时存入打开方式中
					if (o[5] != null)
						tn.setOpenType(o[5].toString());
					
					if(o[4] != null && o[6] != null)
						tn.setCls(o[6].toString());
				}
			}
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("取指标数据失败", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean isLeaf(String node) {
		String sql = "select count(*)\n" + " from bp_c_item r\n"
				+ " where r.parent_item_code = '" + node + "'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	public Object findItemInfo(String node) {
		String sql = "select tt.item_code,\n"
				+ "              tt.item_name,\n"
				+ "              tt.is_item,\n"
				+ "              (select mm.item_name\n"
				+ "                 from bp_c_item mm\n"
				+ "                where mm.item_code = tt.parent_item_code) parentItemName,\n"
				+ "              tt.unit_code,\n"
				+ "              (select a.unit_name from bp_c_measure_unit a where a.unit_id = tt.unit_code and a.is_used = 'Y'),\n"
				+ "              tt.item_memo,\n"
				+ "              tt.retrieve_code,\n"
				+ "              tt.order_by,\n"
				+ "              tt.enterprise_code\n"
				+ "         from bp_c_item tt\n"
				+ "        where tt.item_code = '" + node + "'";

		return bll.getSingal(sql);
	}

	public String findRetrieveCode(String name) {
		String sql = String
				.format("select fun_spellcode('%s') from dual", name);
		return bll.getSingal(sql).toString();
	}
	

	@SuppressWarnings("unused")
	private boolean isNameRepeat(String name, String enterpriseCode) {
		String sql = "select count(*)\n" + "  from bp_c_item r\n"
				+ " where r.item_name = '" + name + "'\n"
				+ "   and r.enterprise_code = '" + enterpriseCode + "'";
		int count = Integer.parseInt(bll.getSingal(sql).toString());
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	private String getItemcode(String name) {
		String sql = "SELECT lower(FUN_SPELLCODE('"
				+ name
				+ "')) ||\n"
				+ "       decode((SELECT COUNT(1)\n"
				+ "                FROM bp_c_item t\n"
				+ "               WHERE REGEXP_LIKE(t.item_code, ('^' ||\n"
				+ "                                  lower(FUN_SPELLCODE('"
				+ name
				+ "')) ||\n"
				+ "                                  '[0-9]*$'))), 0, '', (SELECT COUNT(1)\n"
				+ "                  FROM bp_c_item t\n"
				+ "                 WHERE REGEXP_LIKE(t.item_code, ('^' ||\n"
				+ "                                    lower(FUN_SPELLCODE('"
				+ name
				+ "')) ||\n"
				+ "                                    '[0-9]*$'))), (SELECT COUNT(1)\n"
				+ "                  FROM bp_c_item t\n"
				+ "                 WHERE REGEXP_LIKE(t.item_code, ('^' ||\n"
				+ "                                    lower(FUN_SPELLCODE('"
				+ name + "')) ||\n"
				+ "                                    '[0-9]*$'))))\n"
				+ "  FROM dual";
		String str = bll.getSingal(sql).toString();
		return str;
	}

	// add bjxu
	public boolean checkItem(String itemCode) {
		String sql = "select count(1)\n"
				+ "  from bp_c_item a, bp_c_stat_item b\n"
				+ " where a.item_code = '" + itemCode + "'\n"
				+ "   and b.item_code = a.item_code";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}
}