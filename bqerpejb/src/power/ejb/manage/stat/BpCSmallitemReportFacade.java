package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.Date;
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
import power.ejb.manage.stat.form.BpCSmallitemReportForm;
import power.ejb.manage.stat.form.SmallReportForm;

@Stateless
public class BpCSmallitemReportFacade implements BpCSmallitemReportFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "BpCReportTypeFacade")
	protected BpCReportTypeFacadeRemote typeRemote;
	@PersistenceContext
	private EntityManager entityManager;

	public void addItemsToReport(List<BpCSmallitemRelation> items) {
		Long id = Long.parseLong(bll.getMaxId("BP_C_SMALLITEM_RELATION", "ID")
				.toString());
		if (items != null) {
			for (BpCSmallitemRelation entity : items) {
				entity.setId(id++);
				entityManager.persist(entity);
			}
		}
	}

	public boolean checkSame(BpCSmallitemRelation model) {
		String samesql = "select count(*)\n"
				+ "  from bp_c_smallitem_relation h\n"
				+ " where h.report_id = " + model.getReportId() + "\n"
				+ "   and h.row_datatype_id = "
				+ model.getBpCSmallitemRowtype().getRowDatatypeId() + "\n"
				+ "   and h.item_alias = '" + model.getItemAlias() + "'"; 
		Long count = Long.parseLong(bll.getSingal(samesql).toString());
		if (count > 0) {
			return true;
		}
		return false;
	}

	public void saveItemToReport(BpCSmallitemRelation model) {
		if (model.getId() == null || model.getId().equals("")) {
			model.setId(Long.parseLong(bll.getMaxId("BP_C_SMALLITEM_RELATION",
					"ID").toString()));
			entityManager.persist(model);
		} else {
			entityManager.merge(model);
		}
	}

	public void updateItemsToReport(List<BpCSmallitemRelation> updateList) {
		for (BpCSmallitemRelation data : updateList) {
			entityManager.merge(data);
		}
	}

	public boolean deleteItemsFromReport(String ids) {
		try {
			String[] temp1 = ids.split(",");
			for (String i : temp1) {
				String sql = "delete from bp_c_smallitem_relation t where t.id="
						+ i + "";
				bll.exeNativeSQL(sql);
			}
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpCSmallitemRelation> getRelationItems(Long reportId) {
		String hql = "select t from BpCSmallitemRelation t left join fetch t.bpCSmallitemRowtype where t.reportId="
				+ reportId + " order by t.orderBy,t.id"; 
		Query query = entityManager.createQuery(hql); 
		// query.setParameter(1, reportId);
		return query.getResultList();
	}

	/*----------------------------------小指标报表基本信息维护----------------------------------------------------------*/
	/**
	 * 通过报表id查询对应的报表信息
	 * 
	 * @param id
	 *            报表编码
	 * @return BpCInputReport 报表信息
	 */
	public BpCSmallitemReport findById(Long id) {
		LogUtil.log("finding BpCSmallitemReport instance with id: " + id,
				Level.INFO, null);
		try {
			BpCSmallitemReport instance = entityManager.find(
					BpCSmallitemReport.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 判断报表名称是否重复
	 * 
	 * @param reportName
	 *            报表名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkReportName(String reportName, String enterpriseCode) {
		String sql = "select count(1) from bp_c_smallitem_report t"
				+ " where t.report_name= '" + reportName
				+ "' and t.enterprise_code='" + enterpriseCode
				+ "' and t.is_use='Y'";
		Long count = Long.valueOf(bll.getSingal(sql).toString());
		return count;
	}

	/**
	 * 查询小指标报表维护列表
	 */
	@SuppressWarnings("unchecked")
	public PageObject findSmallItemReportList(String typeCode,
			String enterpriseCode,String workerCode,int... rowStartIdxAndCount) {
		String countsql = "select count(*)\n"
				+ "  from bp_c_smallitem_report t,bp_c_report_type s\n"
				+ " where t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.is_use = 'Y' " + " and t.report_id=s.report_id(+) ";

		String sql = "select t.*,s.type_name\n"
				+ "  from bp_c_smallitem_report t,bp_c_report_type s\n"
				+ " where t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.is_use = 'Y' " + " and t.report_id=s.report_id(+) ";
//		if (typeCode != null && !"".equals(typeCode)) {
//			String typeWhile = " and s.type_name='" + typeCode + "'";
//			sql += typeWhile;
//			countsql += typeWhile;
//
//		}
		if(typeCode != null && !"".equals(typeCode) && typeCode.equals("query")){
			countsql+=" and (t.report_id || '_xzb' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" +
					"      t.report_id || '_xzb' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
					" )";
			sql+=" and (t.report_id || '_xzb' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" +
					"      t.report_id || '_xzb' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
					" )";

		}
		sql += " order by  t.report_id";
		Long count = Long.parseLong(bll.getSingal(countsql).toString());
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<BpCSmallitemReportForm> arrayList = new ArrayList<BpCSmallitemReportForm>();
		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] data = iterator.next();
			BpCSmallitemReportForm reportForm = new BpCSmallitemReportForm();
			if (data[0] != null) {
				reportForm.setReportId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				reportForm.setReportName(data[1].toString());
			}
			if (data[2] != null) {
				reportForm.setRowHeadName(data[2].toString());
			}
			if (data[3] != null) {
				reportForm.setColumnNum(Long.parseLong(data[3].toString()));
			}
			if (data[4] != null) {
				reportForm.setModifyBy(data[4].toString());
			}
			if (data[5] != null) {
				reportForm.setModifyDate((Date) data[5]);
			}
			if (data[6] != null) {
				reportForm.setEnterpriseCode(data[6].toString());
			}
			if (data[7] != null) {
				reportForm.setIsUse(data[7].toString());
			}
			if (data[8] != null) {
				reportForm.setDataType(data[8].toString());
			}
			if (data[9] != null) {
				reportForm.setTypeCode(data[9].toString());
			}
			arrayList.add(reportForm);
		}

		PageObject obj = new PageObject();
		obj.setList(arrayList);
		obj.setTotalCount(count);
		return obj;
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public void save(BpCSmallitemReportForm reportForm) {
		LogUtil.log("saving BpCSmallitemReport instance", Level.INFO, null);
		try {
			BpCSmallitemReport entity = new BpCSmallitemReport();
			if (entity.getReportId() == null || entity.getReportId().equals("")) {
				entity.setReportId(bll.getMaxId("bp_c_smallitem_report",
						"report_id"));
			}
			entity.setColumnNum(reportForm.getColumnNum());
			entity.setDataType(reportForm.getDataType());
			entity.setEnterpriseCode(reportForm.getEnterpriseCode());
			entity.setIsUse("Y");
			entity.setModifyBy(reportForm.getModifyBy());
			entity.setModifyDate(reportForm.getModifyDate());
			entity.setReportName(reportForm.getReportName());
			entity.setRowHeadName(reportForm.getRowHeadName());
			entityManager.persist(entity);
			// 存报表类型到类型表
			BpCReportType model = new BpCReportType();
			model.setTypeName(reportForm.getTypeCode());
			model.setEnterpriseCode(reportForm.getEnterpriseCode());
			model.setReportId(entity.getReportId());
			typeRemote.save(model);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量增加小指标报表数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void save(List<BpCSmallitemReportForm> addList) {
		if (addList != null && addList.size() > 0) {
			Long reportId = bll.getMaxId("bp_c_smallitem_report", "report_id");
			int i = 0;
			for (BpCSmallitemReportForm entity : addList) {
				entity.setReportId(reportId + (i++));
				this.save(entity);
			}
		}
	}

	/**
	 * 批量删除小指标报表数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(",");
			for (String i : temp1) {
				BpCSmallitemReport entity = new BpCSmallitemReport();
				entity = this.findById(Long.parseLong(i));
				entity.setIsUse("N");
				entityManager.merge(entity);
				typeRemote.delete(typeRemote.findByProperty("reportId",
						entity.getReportId()).get(0));
			}
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * 批量更新小指标报表数据
	 * 
	 * @param updateList
	 *            报表数据
	 */
	public void update(List<BpCSmallitemReportForm> updateList) {
		try {
			for (BpCSmallitemReportForm reportForm : updateList) {
				BpCSmallitemReport entity = new BpCSmallitemReport();
				entity.setReportId(reportForm.getReportId());
				entity.setColumnNum(reportForm.getColumnNum());
				entity.setDataType(reportForm.getDataType());
				entity.setEnterpriseCode(reportForm.getEnterpriseCode());
				entity.setIsUse(reportForm.getIsUse());
				entity.setModifyBy(reportForm.getModifyBy());
				entity.setModifyDate(reportForm.getModifyDate());
				entity.setReportName(reportForm.getReportName());
				entity.setRowHeadName(reportForm.getRowHeadName());
				entityManager.merge(entity);
				// 存报表类型到类型表
				BpCReportType model = new BpCReportType();
				model.setTypeName(reportForm.getTypeCode());
				model.setEnterpriseCode(reportForm.getEnterpriseCode());
				model.setReportId(reportForm.getReportId());
				model.setId(typeRemote.findByProperty("reportId",
						model.getReportId()).get(0).getId());
				typeRemote.update(model);
			}
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/*----------------------------------小指标报表对应的行名称维护----------------------------------------------------------*/
	/**
	 * 通过行名称设置查询对应的行设置信息
	 * 
	 * @param id
	 *            行id
	 * @return BpCSmallitemRowtype 行设置信息
	 */
	public BpCSmallitemRowtype findByRowId(Long id) {
		LogUtil.log("finding BpCSmallitemRowtype instance with id: " + id,
				Level.INFO, null);
		try {
			BpCSmallitemRowtype instance = entityManager.find(
					BpCSmallitemRowtype.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 判断行设置名称是否重复
	 * 
	 * @param reportId
	 *            报表id,rowName 行设置名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkRowName(Long reportId, String rowName) {
		String sql = "select count(1) from bp_c_smallitem_rowtype t"
				+ " where t.report_id= " + reportId
				+ " and t.row_datatype_name='" + rowName + "'";
		Long count = Long.valueOf(bll.getSingal(sql).toString());
		return count;
	}

	/**
	 * 查询小指标报表对应的行设置列表
	 */
	@SuppressWarnings("unchecked")
	public List<BpCSmallitemRowtype> findSmallReportRowSetList(Long reportId) {
		String sql = "select t.*\n" + "  from bp_c_smallitem_rowtype t\n"
				+ " where t.report_id = " + reportId + "\n"
				+ "  order by t.order_by"; 
		List<BpCSmallitemRowtype> list = bll.queryByNativeSQL(sql,
				BpCSmallitemRowtype.class);
		return list;
	}

	/**
	 * 保存报表行设置
	 * 
	 * @param entity
	 */
	public void saveRowSet(BpCSmallitemRowtype entity) {
		LogUtil.log("saving BpCSmallitemRowtype instance", Level.INFO, null);
		try {
			if (entity.getRowDatatypeId() == null
					|| entity.getRowDatatypeId().equals("")) {
				entity.setRowDatatypeId(bll.getMaxId("bp_c_smallitem_rowtype",
						"row_datatype_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量增加小指标报表行设置数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void saveRow(List<BpCSmallitemRowtype> addList) {
		if (addList != null && addList.size() > 0) {
			Long rowTypeId = bll.getMaxId("bp_c_smallitem_rowtype",
					"row_datatype_id");
			int i = 0;
			for (BpCSmallitemRowtype entity : addList) {
				entity.setRowDatatypeId(rowTypeId + (i++));
				this.saveRowSet(entity);
			}
		}
	}

	/**
	 * 批量删除小指标报表行设置数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean deleteRow(String ids) {
		try {
			String[] temp1 = ids.split(",");
			for (String i : temp1) {
				String sql = "DELETE FROM bp_c_smallitem_rowtype t\n"
						+ " WHERE t.row_datatype_id=" + i + "";
				bll.exeNativeSQL(sql);
			}
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * 批量更新小指标报表行设置数据
	 * 
	 * @param updateList
	 *            行设置列表
	 */
	public void updateRow(List<BpCSmallitemRowtype> updateList) {
		try {
			for (BpCSmallitemRowtype data : updateList) {
				entityManager.merge(data);
			}
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/*----------------------------------小指标报表查询----------------------------------------------------------*/
	public List<SmallReportForm> getSamallReportHeader(Long reportId) {
		String sql = "select distinct (r.item_alias),\n"
				+ "                r.order_by,\n"
				+ "                (select getunitname(m.unit_code)\n"
				+ "                   from bp_c_smallitem_relation t, bp_c_stat_item m\n"
				+ "                  where t.item_alias = r.item_alias\n"
				+ "                    and m.item_code = t.item_code\n"
				+ "                    and rownum = 1) unit_name\n"
				+ "  from bp_c_smallitem_relation r\n"
				+ " where r.report_id = " + reportId + "\n"
				+ " order by r.order_by";

		List list = bll.queryByNativeSQL(sql);
		List<SmallReportForm> arrayList = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			SmallReportForm model = new SmallReportForm();
			Object[] o = (Object[]) it.next();
			if (o[0] != null) {
				model.setItemAlias(o[0].toString());
			}
			if (o[2] != null) {
				model.setUnitName(o[2].toString());
			} else {
				model.setUnitName("");
			}
			arrayList.add(model);
		}
		return arrayList;

	} 
	public List<SmallReportForm> getSamallReportData(String date,
			String quarter, String dateType, Long reportId, Long rowid) {
		String params = "";
//		String tableName = "";
//		String sqlWhere = "";
//		switch (Integer.parseInt(dateType)) {
//		case 1:
//			tableName = "bp_j_stat_stz";// 时
//			sqlWhere = " r.data_date=to_date('" + date
//					+ "','yyyy-MM-dd hh24:mi:ss')";
//			break;
//		case 3:
//			tableName = "bp_j_stat_rtz";// 日
//			sqlWhere = " r.data_date=to_date('" + date + "','yyyy-MM-dd')";
//			break;
//		case 4:
//			tableName = "bp_j_stat_ytz";// 月
//			sqlWhere = " (to_char(r.data_date, 'yyyy-MM') = '" + date + "')";
//			break;
//		case 5:
//			tableName = "bp_j_stat_jtz";// 季
//			sqlWhere = " (to_char(r.data_date, 'Q') = " + quarter
//					+ ") and (to_char(r.data_date, 'yyyy') = " + date + ")";
//			break;
//		case 6:
//			tableName = "bp_j_stat_ntz";// 年
//			sqlWhere = " (to_char(r.data_date, 'yyyy') = " + date + ")";
//			break;
//		}
//		String sql = "select t.report_id,\n" + "       t.item_code,\n"
//				+ "       t.item_alias,\n" + "       t.data_type,\n"
//				+ "       t.row_datatype_id,\n" + "       m.data_value\n"
//				+ "  from bp_c_smallitem_relation t,\n"
//				+ "       (select item_code, data_value\n"
//				+ "          from (select r.item_code, r.data_value\n"
//				+ "                  from " + tableName + " r\n"
//				+ "                 where " + sqlWhere + "\n"
//				+ "         order by r.data_date desc) where rownum = 1) m\n"
//				+ " where t.report_id = " + reportId + "\n"
//				+ "   and t.row_datatype_id = " + rowid + "\n"
//				+ "   and m.item_code(+) = t.item_code\n"
//				+ " order by t.order_by";
//		List list = bll.queryByNativeSQL(sql);
		switch (Integer.parseInt(dateType)) {
		case 1:
            params = "to_date('" + date + "','yyyy-MM-dd hh24:mi:ss'),1";
			break;
		case 2:
			params = "to_date('" + date + "','yyyy-MM-dd'),2";
			break;
		case 3:
			params = "to_date('" + date + "','yyyy-MM-dd'),3";
			break;
		case 4:
//		    params = "last_day(to_date('"+ date +"','yyyy-MM')),4";
//			break;
			//20100913  月报表查询精确到天 bjxu
			params = "to_date('" + date + "','yyyy-MM-dd'),4";
			break;
		case 5:
			params = "to_date('" + date + "','yyyy-MM'),5";
			break;
		case 6:
			params = "to_date('" + date + "','yyyy'),6";
			break;
		}
		String sql = 
			"select t.report_id,\n" +
			"      t.item_code,\n" + 
			"      t.item_alias,\n" + 
			"      t.data_type,\n" + 
			"      t.row_datatype_id,\n" + 
			"      func_jhtj_report.getItemValue(t.item_code,'hfdc',"+params+")  data_value,\n" + 
			"      t.row_datatype_id,t.order_by,t.complute_mehtod,t.is_ignore_zero,b.is_item\n"+
			" from bp_c_smallitem_relation t,bp_c_smallitem_rowtype b \n" + 
			"where t.report_id = " + reportId + "\n" + 
			"and t.report_id = b.report_id\n"+
			"and t.row_datatype_id = b.row_datatype_id\n"+
			//"  and t.row_datatype_id = " + rowid + "\n" + 
			"  and t.row_datatype_id  is not null \n" + 
			"order by t.row_datatype_id, t.order_by"; 
//		System.out.println(sql);
		List list = bll.queryByNativeSQL(sql);
		List<SmallReportForm> returnlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			SmallReportForm model = new SmallReportForm();
			Object[] o = (Object[]) it.next();
			if (o[2] != null) {
				model.setItemAlias(o[2].toString());
			} else {
				model.setItemAlias("");
			}
			if (o[3] != null) {
				model.setDataType(o[3].toString());
			} else {
				model.setDataType("");
			}
			if (o[5] != null) {
				model.setDataValue(Double.parseDouble(o[5].toString()));
			}
			if (o[6] != null) {
				model.setRowDatatypeId(Long.parseLong(o[6].toString()));
			} 
			if (o[7] != null) {
				model.setOrderBy(Long.parseLong(o[7].toString()));
			} 
			if (o[8] != null) {
				model.setCompluteMethod(o[8].toString());
			} 
			if (o[9] != null) {
				model.setIsIgnoreZero(o[9].toString());
			} 
			if (o[10] != null) {
				model.setIsItem(o[10].toString());
			} 
			returnlist.add(model);
		}
		return returnlist;
	}
}
