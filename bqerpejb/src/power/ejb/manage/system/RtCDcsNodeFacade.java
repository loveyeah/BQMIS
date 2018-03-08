package power.ejb.manage.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.contract.form.DcsNodeInfo;

/**
 * 采集点维护
 * 
 * @author wzhyan
 */
@Stateless
public class RtCDcsNodeFacade implements RtCDcsNodeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RtCDcsNode save(RtCDcsNode entity) throws CodeRepeatException {
		try {
			if (!this.checkName(entity.getEnterpriseCode(), entity
					.getNodeCode())) {
				if (entity.getMaxValue() == null) {
					entity.setMaxValue(0.0000);
				}
				if (entity.getMinValue() == null) {
					entity.setMinValue(0.0000);
				}
				if (entity.getStandardValue() == null) {
					entity.setStandardValue(0.0000);
				}
				entityManager.persist(entity);

				return entity;
			} else {
				throw new CodeRepeatException("增加失败,节点编码不能重复!");
			}

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(RtCDcsNode entity) {
		try {
			entity = entityManager.getReference(RtCDcsNode.class, entity
					.getNodeCode());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RtCDcsNode update(RtCDcsNode entity) throws CodeRepeatException {
		try {
			RtCDcsNode result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			entityManager.persist(result);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RtCDcsNode findById(String id) {
		try {
			RtCDcsNode instance = entityManager.find(RtCDcsNode.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findDcsNodeList(String enterpriseCode, String sys,
			String queryKey, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select n.node_code,\n"
				+ "       n.node_name,\n"
				+ "       n.apart_code,\n"
				+ "       n.descriptor,\n"
				+ "       n.node_type,\n"
				+ "       n.min_value,\n"
				+ "       n.max_value,\n"
				+ "       n.standard_value,\n"
				+ "       n.collect_now,\n"
				+ "       n.collect_his,\n"
				+ "       n.if_compute,\n"
				+ "       (select c.block_name from equ_c_block c where c.block_code = n.apart_code and c.is_use = 'Y')\n"
				+ "  from rt_c_dcs_node n\n" + " where n.enterprise_code = '"
				+ enterpriseCode + "'";

		String sqlCount = "select count(1) from rt_c_dcs_node n where n.enterprise_code = '"
				+ enterpriseCode + "'";
		String strWhere = "";
		if (sys != null && sys.length() > 0) {
			strWhere += " and n.apart_code like '%" + sys + "%'";
		}
		if (queryKey != null && queryKey.length() > 0) {
			strWhere += " and n.node_code||n.node_name like '%" + queryKey
					+ "%'";
		}
		sql += strWhere;
		sqlCount += strWhere;
		sql = sql + " order by n.node_code";
		sqlCount = sqlCount + " order by n.node_code";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				DcsNodeInfo info = new DcsNodeInfo();
				Object[] data = (Object[]) it.next();
				info.setNodeCode(data[0].toString());
				if (data[1] != null)
					info.setNodeName(data[1].toString());
				if (data[2] != null)
					info.setApartCode(data[2].toString());
				if (data[3] != null)
					info.setDescriptor(data[3].toString());
				if (data[4] != null)
					info.setNodeType(data[4].toString());
				if (data[5] != null)
					info.setMinValue(Double.parseDouble(data[5].toString()));
				if (data[6] != null)
					info.setMaxValue(Double.parseDouble(data[6].toString()));
				if (data[7] != null)
					info.setStandardValue(Double
							.parseDouble(data[7].toString()));
				if (data[8] != null)
					info.setCollectNow(data[8].toString());
				if (data[9] != null)
					info.setCollectHis(data[9].toString());
				if (data[10] != null)
					info.setIfCompute(data[10].toString());
				if (data[11] != null)
					info.setBlockName(data[11].toString());
				arrlist.add(info);

			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	@SuppressWarnings("unused")
	private boolean checkName(String enterpriseCode, String nodeCode) {
		boolean isSame = false;
		String sql = "select count(1)\n" + "  from rt_c_dcs_node t\n"
				+ " where t.enterprise_code = ?\n" + "   and t.node_code = ?";
		Object result = bll.getSingal(sql, new Object[] { enterpriseCode,
				nodeCode });
		if (Long.parseLong(result.toString()) > 0) {
			isSame = true;
		}
		return isSame;
	}

	@SuppressWarnings("unused")
	private String createNodeCode() {
		String mymonth = "";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd" + " " + "hh:mm:ss");
		mymonth = tempDate.format(new java.util.Date());
		mymonth = mymonth.substring(0, 4) + mymonth.substring(5, 7);
		String no = "N" + mymonth;
		String sql = "select '"
				+ no
				+ "' ||\n"
				+ "       (select Trim(case\n"
				+ "                 when max(t.node_code) is null then\n"
				+ "                  '001'\n"
				+ "                 else\n"
				+ "                  to_char(to_number(substr(max(t.node_code), 8, 3) + 1),\n"
				+ "                          '000')\n"
				+ "               end)\n" + "          from rt_c_dcs_node t\n"
				+ "         where  substr(t.node_code, 0, 7) = '" + no + "')\n"
				+ "  from dual";
		no = bll.getSingal(sql).toString().trim();
		return no;
	}
}