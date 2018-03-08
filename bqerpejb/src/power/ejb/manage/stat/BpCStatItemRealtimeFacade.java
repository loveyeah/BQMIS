package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.manage.stat.form.StatItemFrom;

/**
 * Facade for entity BpCStatItemRealtime.
 * 
 * @see power.ejb.manage.stat.BpCStatItemRealtime
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCStatItemRealtimeFacade implements
		BpCStatItemRealtimeFacadeRemote {
	// property constants
	public static final String NODE_CODE = "nodeCode";
	public static final String APART_CODE = "apartCode";
	public static final String USEDEFAULT = "usedefault";
	public static final String DEFAULT_VALUE = "defaultValue";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "BpCStatItemFacade")
	protected BpCStatItemFacadeRemote statRemote;

	public BpCStatItemRealtime save(BpCStatItemRealtime entity) {
		try {
			BpCStatItem statItem = statRemote.findById(entity.getItemCode());
			if (!"2".equals(statItem.getDataCollectWay())) {
				statItem.setDataCollectWay("2");
				entityManager.merge(statItem);
			}
			entity.setEnterpriseCode("hfdc");
			entity.setDefaultValue(0l);
			entity.setUsedefault("0");
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCStatItemRealtime delete(BpCStatItemRealtime entity) {
		try {
			entity = entityManager.getReference(BpCStatItemRealtime.class,
					entity.getItemCode());
			entityManager.remove(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 修改
	 */
	public BpCStatItemRealtime update(BpCStatItemRealtime entity) {
		try {
			BpCStatItem statItem = statRemote.findById(entity.getItemCode());
			if (!"2".equals(statItem.getDataCollectWay())) {
				statItem.setDataCollectWay("2");
				entityManager.merge(statItem);
			}
			BpCStatItemRealtime result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCStatItemRealtime findById(String id) {
		LogUtil.log("finding BpCStatItemRealtime instance with id: " + id,
				Level.INFO, null);
		try {
			BpCStatItemRealtime instance = entityManager.find(
					BpCStatItemRealtime.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCStatItemRealtime entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCStatItemRealtime property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<BpCStatItemRealtime> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCStatItemRealtime> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding BpCStatItemRealtime instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCStatItemRealtime model where model."
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

	public List<BpCStatItemRealtime> findByNodeCode(Object nodeCode,
			int... rowStartIdxAndCount) {
		return findByProperty(NODE_CODE, nodeCode, rowStartIdxAndCount);
	}

	public List<BpCStatItemRealtime> findByApartCode(Object apartCode,
			int... rowStartIdxAndCount) {
		return findByProperty(APART_CODE, apartCode, rowStartIdxAndCount);
	}

	public List<BpCStatItemRealtime> findByUsedefault(Object usedefault,
			int... rowStartIdxAndCount) {
		return findByProperty(USEDEFAULT, usedefault, rowStartIdxAndCount);
	}

	public List<BpCStatItemRealtime> findByDefaultValue(Object defaultValue,
			int... rowStartIdxAndCount) {
		return findByProperty(DEFAULT_VALUE, defaultValue, rowStartIdxAndCount);
	}

	public List<BpCStatItemRealtime> findByEnterpriseCode(
			Object enterpriseCode, int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode,
				rowStartIdxAndCount);
	}

	/**
	 * Find all BpCStatItemRealtime entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<BpCStatItemRealtime> all BpCStatItemRealtime entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpCStatItemRealtime> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all BpCStatItemRealtime instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from BpCStatItemRealtime model";
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

	public PageObject findStatItemForCorrespond(String argFuzzy,
			int... rowStartIdxAndCount) {
		PageObject bean = new PageObject();
		List<StatItemFrom> arrList = new ArrayList<StatItemFrom>();
		if ("".equals(argFuzzy) || argFuzzy == null) {
			argFuzzy = "%";
		}
		String sql = "select a.item_code,\n"
				+ "       a.item_name,\n"
				+ "       a.data_time_type,\n"
				+ "       c.node_code,\n"
				+ "       c.descriptor,\n"
				+ "       c.node_name,\n"
				+ "       c.apart_code,\n"
				+ "       decode(nvl(b.item_code, 'N'), 'N', 'N', 'Y')\n"
				+ "  from bp_c_stat_item a, bp_c_stat_item_realtime b,RT_C_DCS_NODE c \n"
				+ " where a.data_collect_way = 2 and a.item_code = b.item_code(+) and b.node_code = c.node_code(+) "
				+ " and a.item_code like '%" + argFuzzy +

				"%'  order by decode(nvl(b.item_code, 'N'), 'N', 'N', 'Y')";
		String sqlCount = "select count(*)  "
				+ "from bp_c_stat_item t where t.data_collect_way = 2 ";
		List<StatItemFrom> list = bll
				.queryByNativeSQL(sql, rowStartIdxAndCount);
		if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				StatItemFrom statItemFrom = new StatItemFrom();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					statItemFrom.setItemCode(data[0].toString());
				}
				if (data[1] != null) {
					statItemFrom.setItemName(data[1].toString());
				}
				if (data[2] != null) {
					statItemFrom.setDataTimeType(data[2].toString());
				}
				if (data[3] != null) {
					statItemFrom.setNodeCode(data[3].toString());
				}
				if (data[4] != null) {
					statItemFrom.setDescriptor(data[4].toString());
				}
				if (data[5] != null) {
					statItemFrom.setNodeName(data[5].toString());
				}
				if (data[6] != null) {
					statItemFrom.setApartCode(data[6].toString());
				}
				if (data[7] != null) {
					statItemFrom.setIsCorrespond(data[7].toString());
				}
				arrList.add(statItemFrom);
			}
		}
		bean.setList(arrList);
		bean.setTotalCount(Long.valueOf(bll.getSingal(sqlCount).toString()));
		return bean;
	}

	// public PageObject findDcsNodeForCorrespond(int... rowStartIdxAndCount) {
	// PageObject bean = new PageObject();
	// List<DcsNodeForm> arrList = new ArrayList<DcsNodeForm>();
	// String sql = "select a.node_code,\n" +
	// " a.descriptor,\n" +
	// " a.node_name,\n" +
	// " a.apart_code,\n" +
	// " decode(nvl(b.item_code, 'N'), 'N', 'N', 'Y'),\n" +
	// " getblockbycode(a.apart_code) blockName\n" +
	// " from RT_C_DCS_NODE a, bp_c_stat_item_realtime b\n" +
	// " where a.node_code = b.node_code(+) order by decode(nvl(b.node_code,
	// 'N'), 'N', 'N', 'Y')";
	//
	// String sqlCount = "select count(*) " +
	// "from RT_C_DCS_NODE t ";
	// List<DcsNodeForm> list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	// if(list != null) {
	// Iterator it = list.iterator();
	// while(it.hasNext()) {
	// DcsNodeForm dcsNodeForm = new DcsNodeForm();
	// Object[] data = (Object[]) it.next();
	// if(data[0] != null ) {
	// dcsNodeForm.setNodeCode(data[0].toString());
	// }
	// if(data[1] != null) {
	// dcsNodeForm.setDescriptor(data[1].toString());
	// }
	// if(data[2] != null) {
	// dcsNodeForm.setNodeName(data[2].toString());
	// }
	// if(data[3] != null) {
	// dcsNodeForm.setApartCode(data[3].toString());
	// }
	// if(data[4] != null) {
	// dcsNodeForm.setIsCorrespond(data[4].toString());
	// }
	// if(data[5] != null) {
	// dcsNodeForm.setApartName(data[5].toString());
	// }
	// arrList.add(dcsNodeForm);
	// }
	// }
	// bean.setList(arrList);
	// bean.setTotalCount(Long.valueOf(bll.getSingal(sqlCount).toString()));
	// return bean;
	// }

	public Boolean deleteByItemCode(String itemCode) {
		String sql = "delete from bp_c_stat_item_realtime t where t.item_code= '"
				+ itemCode + "'";
		int exeRow = bll.exeNativeSQL(sql);
		if (exeRow > 0) {
			LogUtil.log("delete successful", Level.INFO, null);
			return true;
		} else {
			return false;
		}
	}

	public StatItemFrom findItemCorrespondInfo(String itemCode) {
		String sql = "select a.item_code,\n" + "       a.node_code,\n"
				+ "       b.node_name,\n" + "       b.descriptor,\n"
				+ "       a.apart_code,\n"
				+ "       getblockbycode(a.apart_code) blockName\n"
				+ "  from bp_c_stat_item_realtime a, rt_c_dcs_node b\n"
				+ " where a.item_code = '" + itemCode + "'\n"
				+ "   and b.node_code = a.node_code\n"
				+ "   and b.enterprise_code = a.enterprise_code";
		List list = bll.queryByNativeSQL(sql);
		if (list.size() > 0) {
			StatItemFrom form = new StatItemFrom();
			Object[] data = (Object[]) list.get(0);
			if (data[0] != null) {
				form.setItemCode(data[0].toString());
			}
			if (data[1] != null) {
				form.setNodeCode(data[1].toString());
			}
			if (data[2] != null) {
				form.setNodeName(data[2].toString());
			}
			if (data[3] != null) {
				form.setDescriptor(data[3].toString());
			}
			if (data[4] != null) {
				form.setApartCode(data[4].toString());
			}
			if (data[5] != null) {
				form.setApartName(data[5].toString());
			}
			return form;
		} else {
			return null;
		}

	}
}