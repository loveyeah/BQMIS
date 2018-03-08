package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;

/**
 * 统计指标管理
 * 
 * @author jyan
 * @modify wzhyan
 */
@Stateless
public class BpCStatItemFacade implements BpCStatItemFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	// @EJB(beanName = "BpCItemCollectSetupFacade")
	// private BpCItemCollectSetupFacadeRemote setupRemote;

	/**
	 * 保存
	 */
	public BpCStatItem save(BpCStatItem entity) {
		try {
			if (("Y").equals(entity.getIsItem())) {
				String dateSql = createStartData(entity.getItemCode(), entity
						.getDataTimeType(), entity.getEnterpriseCode());
				if (dateSql != null && !"".equals(dateSql)) {
					bll.exeNativeSQL(dateSql);
				}
				setAccountOrder(entity);
			} else {
				Object o = bll
						.getSingal("select 'C_'||(nvl(max(to_number(substr(t.item_code,3))),1)+1) from bp_c_stat_item t where t.item_code like 'C_%' ");
				entity.setItemCode(o.toString());
			}
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	// 设置计算等级
	public BpCStatItem setAccountOrder(BpCStatItem entity) {
		// 公式计算
		if ("3".equals(entity.getDataCollectWay())) {
			// 扩展公式
			if ("3".equals(entity.getDeriveDataType())) {
				String sqlcount = "select count(*)\n"
						+ "  from bp_c_extend_formula t, bp_c_stat_item i\n"
						+ " where t.formula_content = i.item_code\n"
						+ "   and t.item_code = ?\n"
						+ "   and t.fornula_type =  '1'\n"
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
							+ "  from bp_c_extend_formula t, bp_c_stat_item i\n"
							+ " where t.formula_content = i.item_code\n"
							+ "   and t.item_code = ?\n"
							+ "   and t.fornula_type =  '1'\n"
							+ "   and t.enterprise_code = i.enterprise_code\n"
							+ "   and t.enterprise_code = ?\n";
					entity.setAccountOrder(Long.parseLong(bll.getSingal(
							sql,
							new Object[] { entity.getItemCode(),
									entity.getEnterpriseCode() }).toString()));

				}

			}
			// 表值公式,时段公式
			else if ("2".equals(entity.getDeriveDataType())
					|| "5".equals(entity.getDeriveDataType())) {
				String sqlcount = "select count(*)\n"
						+ "  from bp_c_run_formula t, bp_c_stat_item i\n"
						+ " where t.run_data_code = i.item_code\n"
						+ "   and t.item_code = ?\n"
						+ "   and t.derive_data_type = ?\n"
						+ "   and t.enterprise_code = i.enterprise_code\n"
						+ "   and t.enterprise_code = ? ";
				if (bll.getSingal(
						sqlcount,
						new Object[] { entity.getItemCode(),
								entity.getDeriveDataType(),
								entity.getEnterpriseCode() }).toString()
						.equals("0")) {
					entity.setAccountOrder(1L);
				} else {
					String sql = "select nvl(max(i.account_order), 1) + 1\n"
							+ "  from bp_c_run_formula t, bp_c_stat_item i\n"
							+ " where t.run_data_code = i.item_code\n"
							+ "   and t.item_code = ?\n"
							+ "   and t.derive_data_type = ?\n"
							+ "   and t.enterprise_code = i.enterprise_code\n"
							+ "   and t.enterprise_code = ? ";
					Long tempLong = Long.parseLong(bll.getSingal(
							sql,
							new Object[] { entity.getItemCode(),
									entity.getDeriveDataType(),
									entity.getEnterpriseCode() }).toString());
					entity.setAccountOrder(Long.parseLong(bll.getSingal(
							sql,
							new Object[] { entity.getItemCode(),
									entity.getDeriveDataType(),
									entity.getEnterpriseCode() }).toString()));

				}
			}
			// 焓值公式
			else if ("6".equals(entity.getDeriveDataType())) {
				String sqlcount = "select count(*)\n"
						+ "  from bp_c_enthalpy_formula t, bp_c_stat_item i\n"
						+ " where t.yl_zbbm  in (select m.item_Code "
						+ " from bp_c_stat_item m where  m.enterprise_code=? )\n"
						+ " and t.wd_zbbm in (select m.item_Code "
						+ " from bp_c_stat_item m where  m.enterprise_code=? )\n"
						+ "   and t.item_code = ?\n"
						// + " and t.derive_data_type = ?\n"

						+ "   and t.enterprise_code = i.enterprise_code\n"
						+ "   and t.enterprise_code = ? ";

				if (bll.getSingal(
						sqlcount,
						new Object[] { entity.getEnterpriseCode(),
								entity.getEnterpriseCode(),
								entity.getItemCode(),
								// entity.getDeriveDataType(),
								entity.getEnterpriseCode() }).toString()
						.equals("0")) {
					entity.setAccountOrder(1L);
				} else {
					String sql = "	select max(accountlevel) from("
							+ "  select nvl(max(i.account_order), 1)+1 accountlevel\n"
							+ "  from bp_c_enthalpy_formula t, bp_c_stat_item i\n"
							+ " where t.yl_zbbm = i.item_code\n"
							+ "   and t.item_code = ?\n"
							+ "   and t.enterprise_code = i.enterprise_code\n"
							+ "   and t.enterprise_code = ? "
							+ " union select nvl(max(i.account_order), 1) + 1\n"
							+ "  from bp_c_enthalpy_formula t, bp_c_stat_item i\n"
							+ " where t.wd_zbbm = i.item_code\n"
							+ "   and t.item_code = ?\n"
							+ "   and t.enterprise_code = i.enterprise_code\n"
							+ "   and t.enterprise_code = ? )";

					Long tempLong = Long.parseLong(bll.getSingal(
							sql,
							new Object[] { entity.getItemCode(),
									entity.getEnterpriseCode(),
									entity.getItemCode(),
									entity.getEnterpriseCode() }).toString());
					entity.setAccountOrder(Long.parseLong(bll.getSingal(
							sql,
							new Object[] { entity.getItemCode(),

							entity.getEnterpriseCode(), entity.getItemCode(),
									entity.getEnterpriseCode() }).toString()));
				}
			} 
			// add by liuyi 20100521 有效时点公式
			else if("7".equals(entity.getDeriveDataType())){
				String sql = "select * from bp_c_valid_formula tt where tt.item_code ='"+entity.getItemCode()+"'";
				List<BpCValidFormula> validList = bll.queryByNativeSQL(sql,BpCValidFormula.class);
				if(validList != null && validList.size() > 0){
					String  ddsql = "select * from bp_c_stat_item y where y.item_code='"+validList.get(0).getConnItemCode()+"'";
					List<BpCStatItem> itemList = bll.queryByNativeSQL(ddsql, BpCStatItem.class);
					if(itemList != null && itemList.size() > 0){
						entity.setAccountOrder(itemList.get(0).getAccountOrder() + 1);
					}else{
						entity.setAccountOrder(1L);
					}
				}else{
					entity.setAccountOrder(1L);
				}
			}
			else {
				entity.setAccountOrder(1L);
			}
		} else {
			entity.setAccountOrder(1L);
		}
		String sqlupdate = "update bp_c_stat_item s" + " set s.account_order='"
				+ entity.getAccountOrder() + "'" + " where s.item_code='"
				+ entity.getItemCode() + "'";
		bll.exeNativeSQL(sqlupdate);

		return entity;
	}

	/**
	 * 获得所有通过公式向下关联的指标
	 */
	public List<Object[]> getAllReferItem(String itemCode) {
		String sql = "SELECT a.refer_item_code,level,a.derive_data_type"
				+ " FROM bp_j_view_formula a"
				+ " WHERE  a.refer_item_code <> a.effect_item_code"
				+ " START WITH a.refer_item_code = '" + itemCode + "'"
				+ " CONNECT BY PRIOR   a.effect_item_code = a.refer_item_code "
				+ " order by level desc";
		List<Object[]> list = bll.queryByNativeSQL(sql);
		return list;

	}

	/**
	 * 修改统计指标信息
	 */
	public BpCStatItem update(BpCStatItem entity) {
		if ("Y".equals(entity.getIsItem())) {
			String sql = "";
			BpCStatItem dbobj = this.findById(entity.getItemCode());
			// 判断"时间类型"有没有改变
			// 原来有,但是与现在的不相等
			if (dbobj.getDataTimeType() != null
					&& !dbobj.getDataTimeType()
							.equals(entity.getDataTimeType())) {
				sql = " delete from bp_c_item_collect_setup t where t.item_code=? ";
				bll.exeNativeSQL(sql, new Object[] { entity.getItemCode() });
			}
			// 现在有了,且与原来的不等
			if (entity.getDataTimeType() != null
					&& !entity.getDataTimeType().trim().equals("")
					&& !entity.getDataTimeType()
							.equals(dbobj.getDataTimeType())) {
				sql = createStartData(entity.getItemCode(), entity
						.getDataTimeType(), entity.getEnterpriseCode());
				if (sql != null && !"".equals(sql)) {
					bll.exeNativeSQL(sql);
				}
			}

			// 产生方式
			// 老的为"采集指标",而新的改变为了其它的
			if ("2".equals(dbobj.getDataCollectWay())
					&& !"2".equals(entity.getDataCollectWay())) {
				sql = "delete from BP_C_STAT_ITEM_REALTIME t where t.item_code=?";
				bll.exeNativeSQL(sql, new Object[] { entity.getItemCode() });
			}
			// 公式类型
			if (entity.getDataCollectWay().equals("3")
					&& dbobj.getDataCollectWay().equals("3"))
				// --------update by sychen 20100429------------------//
//				if (!dbobj.getDeriveDataType().equals(
//						entity.getDeriveDataType())) {
				if ((dbobj.getDeriveDataType()!=null && !dbobj.getDeriveDataType().equals(
						entity.getDeriveDataType())) ) {
			    //-----------update end ---------------------------//
					String sqlString = "";
					if (dbobj.getDeriveDataType().equals("1")) {
						// 目前没有=1的情况
					} else if (dbobj.getDeriveDataType().equals("2")
							|| dbobj.getDeriveDataType().equals("5")) {
						sqlString += "delete from bp_c_run_formula t where t.item_code=?";
					} else if (dbobj.getDeriveDataType().equals("3")) {
						sqlString += "delete from bp_c_extend_formula t where t.item_code=?";
					} else if (dbobj.getDeriveDataType().equals("4")) {
						// 目前没有=1的情况

					} else if (dbobj.getDeriveDataType().equals("6")) {
						sqlString += "delete from bp_c_enthalpy_formula t where t.item_code=?";
					}
					bll.exeNativeSQL(sqlString, new Object[] { dbobj
							.getItemCode() });
				}
			// setAccountOrder(entity);
			//

		}
		BpCStatItem result = entityManager.merge(entity);
		return result;
	}

	/**
	 * 获取采集指标初始值SQL
	 */
	public String createStartData(String itemcode, String itemcodeType,
			String enterpriseCode) {
		String strSQL = "";
		try {
			switch (Integer.parseInt(itemcodeType)) {
			// 时数据
			case 1:
				strSQL = "INSERT INTO bp_c_item_collect_setup";
				for (int i = 1; i < 24; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','1','"
							+ itemcodeType + "','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',24,'1','1','"
						+ itemcodeType + "','" + enterpriseCode + "'FROM dual";
				break;

			case 2:
				strSQL = "INSERT INTO bp_c_item_collect_setup";
				for (int i = 1; i < 6; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','1','"
							+ itemcodeType + "','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',6,'1','1','"
						+ itemcodeType + "','" + enterpriseCode + "'FROM dual";
				break;
			// 日数据
			case 3:
				strSQL = "INSERT INTO bp_c_item_collect_setup";
				for (int i = 1; i < 31; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','1','"
							+ itemcodeType + "','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',31,'1','1','"
						+ itemcodeType + "','" + enterpriseCode + "'FROM dual";
				break;
			case 4:
				strSQL = "INSERT INTO bp_c_item_collect_setup";
				for (int i = 1; i < 12; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','1','"
							+ itemcodeType + "','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',12,'1','1','"
						+ itemcodeType + "','" + enterpriseCode + "'FROM dual";
				break;
			case 5:
				strSQL = "INSERT INTO bp_c_item_collect_setup";
				for (int i = 1; i < 4; i++) {
					strSQL += " SELECT '" + itemcode + "'," + i + ",'1','1','"
							+ itemcodeType + "','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + itemcode + "',4,'1','1','"
						+ itemcodeType + "','" + enterpriseCode + "'FROM dual";
				break;
			case 6:
				strSQL = "INSERT INTO bp_c_item_collect_setup\n" + "VALUES\n"
						+ "  ('" + itemcode + "',\n" + "   1,\n" + "   '1',\n"
						+ "   '1',\n" + "   '" + itemcodeType + "','"
						+ enterpriseCode + "')";

				break;
			default:
				break;
			}
		} catch (Exception e) {

		}
		return strSQL;
	}

	/**
	 * 删除统计指标
	 */
	public void delete(BpCStatItem entity) {
		try {
			String itemCode = entity.getItemCode();
			String sql = "begin\n"
					+ "  delete BP_C_STAT_ITEM_REALTIME t where t.item_code = ?;\n"
					+ "  delete BP_C_ITEM_COLLECT_SETUP t where t.item_code = ?;\n"
					+ "  delete BP_C_METRIC_TABLE t where t.item_code = ?;\n"
					+ "  delete BP_C_METRIC_TABLE t where t.item_code = ?;\n"
					+ "  delete BP_C_ENTHALPY_FORMULA t where t.item_code = ?;\n"
					+ "  delete BP_C_EXTEND_FORMULA t where t.item_code = ?;\n"
					+ "  delete BP_C_RUN_FORMULA t where t.item_code = ?;\n"
					+ "  delete BP_C_STAT_REPORT_ITEM t where t.item_code = ?;\n"
					+ "  delete BP_C_INPUT_REPORT_ITEM t where t.item_code = ?;\n"
					+ "  delete BP_C_ANALYSE_ACCOUNT_ITEM t where t.item_code = ?;\n"
					+ "  delete bp_c_stat_item t where t.item_code=?;\n"
					+ "  commit;\n" + "end;";
			bll.exeNativeSQL(sql, new Object[] { itemCode, itemCode, itemCode,
					itemCode, itemCode, itemCode, itemCode, itemCode, itemCode,
					itemCode, itemCode });
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	// 删除公式表数据
	public void deleteOldFormula(BpCStatItem entity) {

		if (entity.getDeriveDataType().equals("3")) {
			bll.exeNativeSQL("delete from bp_c_extend_formula t"
					+ " where t.item_code='" + entity.getItemCode() + "'");
		} else if (entity.getDeriveDataType().equals("2")
				|| entity.getDeriveDataType().equals("5")) {
			bll.exeNativeSQL("delete from bp_c_run_formula t"
					+ " where t.item_code='" + entity.getItemCode() + "'");
		} else if (entity.getDeriveDataType().equals("6")) {
			bll.exeNativeSQL("delete from bp_c_enthalpy_formula t"
					+ " where t.item_code='" + entity.getItemCode() + "'");
		}
	}

	// /**
	// * 保存
	// */
	// public BpCStatItem update(BpCStatItem entity, Boolean isChange) {
	// try {
	// BpCStatItem result = entityManager.merge(entity);
	// // 判断是否指标，并且指标中日期类型改变，清空日期类型数据，并重新保存新得日期类型数据 addBy ywliu 2009/06/09
	// if (("Y").equals(entity.getIsItem()) && isChange) {
	// Boolean isDetele = setupRemote.delete(entity.getItemCode());
	// if (isDetele) {
	// String dateSql = createStartData(entity.getItemCode(),
	// entity.getDataTimeType(), entity
	// .getEnterpriseCode());
	// bll.exeNativeSQL(dateSql);
	// }
	// }
	// return result;
	// } catch (RuntimeException re) {
	// LogUtil.log("update failed", Level.SEVERE, re);
	// throw re;
	// }
	// }
	// public BpCStatItem update(BpCStatItem entity, String itemCode,
	// boolean datetypeChange) {
	// try {
	// if (datetypeChange) {
	// this.update(entity, datetypeChange);
	// }
	// // 公式类型变化时，删除原公式表数据
	// if (this.findById(entity.getItemCode()).getDeriveDataType() != null) {
	// if (!this.findById(entity.getItemCode()).getDeriveDataType()
	// .equals(entity.getDeriveDataType())) {
	//
	// this.deleteOldFormula(this.findById(entity.getItemCode()));
	//
	// }
	// }
	// // 修改产生方式和数据类型时清楚子表数据的方法
	// // if (("Y").equals(collectWayChange)) {
	// // bll
	// // .exeNativeSQL("delete from bp_c_stat_item_realtime t where
	// // t.item_code= '"
	// // + itemCode + "'");
	// // bll.exeNativeSQL("delete from bp_c_extend_formula t"
	// // + " where t.item_code='" + itemCode + "'");
	// // bll.exeNativeSQL("delete from bp_c_metric_table t"
	// // + " where t.item_code='" + itemCode + "'");
	// // }
	// // if (("Y").equals(itemTypeChange)) {
	// // bll
	// // .exeNativeSQL("delete from bp_c_stat_item_realtime t where
	// // t.item_code= '"
	// // + itemCode + "'");
	// // bll
	// // .exeNativeSQL("delete from bp_c_extend_formula t where
	// // t.item_code='"
	// // + itemCode + "'");
	// // bll.exeNativeSQL("delete from bp_c_metric_table t"
	// // + " where t.item_code='" + itemCode + "'");
	// // }
	//
	// entity.setItemCode(entity.getItemCode() == null ? "" : entity
	// .getItemCode());
	// entity.setItemName(entity.getItemName() == null ? "" : entity
	// .getItemName());
	// entity.setItemType(entity.getItemType() == null ? "" : entity
	// .getItemType());
	// entity.setDataTimeType(entity.getDataTimeType() == null ? ""
	// : entity.getDataTimeType());
	// entity.setDataCollectWay(entity.getDataCollectWay() == null ? ""
	// : entity.getDataCollectWay());
	// entity.setDataTimeType(entity.getDataTimeType() == null ? ""
	// : entity.getDataTimeType());
	// entity
	// .setDeriveDataType((entity.getDeriveDataType() == null || ("null")
	// .equals(entity.getDeriveDataType())) ? "" : entity
	// .getDeriveDataType());
	// entity.setDataAttribute(entity.getDataAttribute() == null ? ""
	// : entity.getDataAttribute());
	// entity.setTotalDataType(entity.getTotalDataType() == null ? ""
	// : entity.getTotalDataType());
	// entity.setIgnoreZero(entity.getIgnoreZero() == null ? "" : entity
	// .getIgnoreZero());
	// entity.setComputeMethod(entity.getComputeMethod() == null ? ""
	// : entity.getComputeMethod());
	// entity.setRetrieveCode(entity.getRetrieveCode() == null ? ""
	// : entity.getRetrieveCode());
	// entity.setParentItemCode(entity.getParentItemCode() == null ? ""
	// : entity.getParentItemCode());
	// entity.setIsItem(entity.getIsItem() == null ? "" : entity
	// .getIsItem());
	// entity.setEnterpriseCode(entity.getEnterpriseCode() == null ? ""
	// : entity.getEnterpriseCode());
	//
	// String sql = "UPDATE bp_c_stat_item t\n"
	// + " SET ITEM_CODE = '"
	// + entity.getItemCode()
	// + "',\n"
	// + " ITEM_NAME = '"
	// + entity.getItemName()
	// + "',\n"
	// + " UNIT_CODE = "
	// + entity.getUnitCode()
	// + ",\n"
	// + " ITEM_TYPE = '"
	// + entity.getItemType()
	// + "',\n"
	// + " DATA_TIME_TYPE = '"
	// + entity.getDataTimeType()
	// + "',\n"
	// + " DATA_COLLECT_WAY = '"
	// + entity.getDataCollectWay()
	// + "',\n"
	// + " DERIVE_DATA_TYPE = '"
	// + entity.getDeriveDataType()
	// + "',\n"
	// + " DATA_ATTRIBUTE = '"
	// + entity.getDataAttribute()
	// + "',\n"
	// + " TOTAL_DATA_TYPE = '"
	// + entity.getTotalDataType()
	// + "',\n"
	// + " IGNORE_ZERO = '"
	// + entity.getIgnoreZero()
	// + "',\n"
	// + " COMPUTE_METHOD = '"
	// + entity.getComputeMethod()
	// + "',\n"
	// + " ACCOUNT_ORDER = "
	// + entity.getAccountOrder()
	// + ",\n"
	// + " RETRIEVE_CODE = '"
	// + entity.getRetrieveCode()
	// + "',\n"
	// + " PARENT_ITEM_CODE = '"
	// + entity.getParentItemCode()
	// + "',\n"
	// + " IS_ITEM = '"
	// + entity.getIsItem()
	// + "',\n"
	// + " ORDER_BY = "
	// + entity.getOrderBy()
	// + ",\n"
	// + " ENTERPRISE_CODE = '"
	// + entity.getEnterpriseCode()
	// + "'\n"
	// + " WHERE t.item_code = '"
	// + itemCode
	// + "'\n"
	// + " AND rownum = 1";
	// bll.exeNativeSQL(sql);
	//
	// // String sql2 = "UPDATE bp_c_stat_item t\n"
	// // + " SET t.parent_item_code = '" + entity.getItemCode()
	// // + "'\n" + " WHERE t.parent_item_code = '" + itemCode + "'";
	// // bll.exeNativeSQL(sql2);
	// //
	// // String sql3="UPDATE bp_c_analyse_account_item t set
	// // t.item_code='"+entity.getItemCode()+"'" +
	// // " where t.item_code='"+itemCode+"'";
	// // bll.exeNativeSQL(sql3);
	// BpCStatItem result = entityManager.merge(entity);
	// return result;
	// } catch (RuntimeException re) {
	// LogUtil.log("update failed", Level.SEVERE, re);
	// throw re;
	// }
	// }

	public BpCStatItem findById(String id) {
		try {
			BpCStatItem instance = entityManager.find(BpCStatItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 取得结点编码为node的所有第一层子结点
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findStatTreeList(String node, String enterpriseCode,
			String searchKey) {
		List<TreeNode> res = null;
		try {
			String sql = "";
			if (searchKey == null || "".equals(searchKey)) {
				sql = "select t.item_code,\n"
						+ "       t.item_name,\n"
						+ "       t.is_item,\n"
						+ "       connect_by_isleaf,t.data_time_type,t.data_collect_way,t.item_type \n"
						+ "  from bp_c_stat_item t\n" + "where level = 1\n"
						+ " start with t.parent_item_code = ?\n"
						+ "connect by prior t.item_code = t.parent_item_code\n"
						+ " order by t.order_by";
			} else {
				sql = "select distinct t.item_code,\n"
						+ "       t.item_name,\n"
						+ "       t.is_item,\n"
						+ "       connect_by_isleaf,t.data_time_type,t.data_collect_way,t.item_type,t.order_by \n"
						+ "  from ("
						+ " select *\n"
						+ "  from bp_c_stat_item t\n"
						+ " start with t.item_code || t.item_name || t.retrieve_code like '%"
						+ searchKey + "%'\n"
						+ "connect by prior t.parent_item_code = t.item_code"
						+ ") t\n" + "where level = 1\n"
						+ " start with t.parent_item_code = ?\n"
						+ "connect by prior t.item_code = t.parent_item_code\n"
						+ " order by t.order_by";

			}
			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { node });
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
					// 时间点类型
					if (o[4] != null)
						n.setCode(o[4].toString());

					// 产生方式
					if (o[5] != null)
						n.setOpenType(o[5].toString());
					// 指标类型（运行指标还是表值指标）
					if (o[6] != null)
						n.setCls(o[6].toString());

					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean isleaf(String itemCode) {
		String sql = "select count(*)\n" + "  from bp_c_stat_item r\n"
				+ " where r.PARENT_ITEM_CODE = '" + itemCode + "'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getItemcode(String name) {
		String sql = "SELECT lower(FUN_SPELLCODE('"
				+ name
				+ "')) ||\n"
				+ "       decode((SELECT COUNT(1)\n"
				+ "                FROM bp_c_stat_item t\n"
				+ "               WHERE REGEXP_LIKE(t.item_code, ('^' ||\n"
				+ "                                  lower(FUN_SPELLCODE('"
				+ name
				+ "')) ||\n"
				+ "                                  '[0-9]*$'))), 0, '', (SELECT COUNT(1)\n"
				+ "                  FROM bp_c_stat_item t\n"
				+ "                 WHERE REGEXP_LIKE(t.item_code, ('^' ||\n"
				+ "                                    lower(FUN_SPELLCODE('"
				+ name
				+ "')) ||\n"
				+ "                                    '[0-9]*$'))), (SELECT COUNT(1)\n"
				+ "                  FROM bp_c_stat_item t\n"
				+ "                 WHERE REGEXP_LIKE(t.item_code, ('^' ||\n"
				+ "                                    lower(FUN_SPELLCODE('"
				+ name + "')) ||\n"
				+ "                                    '[0-9]*$'))))\n"
				+ "  FROM dual";
		String str = bll.getSingal(sql).toString();
		return str;
	}
}