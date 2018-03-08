package power.ejb.productiontec.dependabilityAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity JsJKkxJzztwh.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.JsJKkxJzztwh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class JsJKkxJzztwhFacade implements JsJKkxJzztwhFacadeRemote {
	// property constants
	public static final String JZZT_CODE = "jzztCode";
	public static final String JZZT_NAME = "jzztName";
	public static final String DADDY_CODE = "daddyCode";
	public static final String JZZT_DESCRIPTION = "jzztDescription";
	public static final String DISPLAY_NO = "displayNo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public JsJKkxJzztwh save(JsJKkxJzztwh entity) {
		LogUtil.log("saving JsJKkxJzztwh instance", Level.INFO, null);
		try {
			entity.setJzztId(bll.getMaxId("js_j_kkx_jzztwh", "jzzt_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(JsJKkxJzztwh entity) {
		LogUtil.log("deleting JsJKkxJzztwh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(JsJKkxJzztwh.class, entity
					.getJzztId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public JsJKkxJzztwh update(JsJKkxJzztwh entity) {
		LogUtil.log("updating JsJKkxJzztwh instance", Level.INFO, null);
		try {
			JsJKkxJzztwh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public JsJKkxJzztwh findById(Long id) {
		LogUtil.log("finding JsJKkxJzztwh instance with id: " + id, Level.INFO,
				null);
		try {
			JsJKkxJzztwh instance = entityManager.find(JsJKkxJzztwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<JsJKkxJzztwh> findAll() {
		LogUtil.log("finding all JsJKkxJzztwh instances", Level.INFO, null);
		try {
			final String queryString = "select model from JsJKkxJzztwh model";
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
			String sql = "select t.*,connect_by_isleaf\n"

			+ "  from JS_J_KKX_JZZTWH t\n" + "where level = 1\n"
					+ " start with t.daddy_code = ?\n"
					+ "connect by prior t.jzzt_code = t.daddy_code\n"
					+ " order by t.display_no";
			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { node });
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					
					n.setId(o[0].toString());
					if (o[1] != null)
						n.setCode(o[1].toString());
					if (o[2] != null)
						n.setText(o[2].toString());
					// 父节点编码
					if (o[3] != null)
						n.setOpenType(o[3].toString());

					if (o[4] != null)
						n.setDescription(o[4].toString());

					// 显示顺序
					if (o[5] != null)
						n.setCls(o[5].toString());
					String icon = "";

					n.setLeaf(o[7].toString().equals("1") ? true : false);
					icon = n.getLeaf() ? "file" : "folder";

					n.setIconCls(icon);
					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	// public boolean isleaf(String jzztCode) {
	// String sql = "select count(*)\n" + " from JS_J_KKX_JZZTWH r\n"
	// + " where r.daddy_code = '" + jzztCode + "'";
	// Long count = Long.parseLong(bll.getSingal(sql).toString());
	// if (count == 0) {
	// return true;
	// } else {
	// return false;
	// }
	// }
}