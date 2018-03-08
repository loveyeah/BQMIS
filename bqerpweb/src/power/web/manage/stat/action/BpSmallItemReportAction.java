package power.web.manage.stat.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCReportTypeFacadeRemote;
import power.ejb.manage.stat.BpCSmallitemRelation;
import power.ejb.manage.stat.BpCSmallitemReport;
import power.ejb.manage.stat.BpCSmallitemReportFacadeRemote;
import power.ejb.manage.stat.BpCSmallitemRowtype;
import power.ejb.manage.stat.form.BpCSmallitemReportForm;
import power.ejb.manage.stat.form.SmallReportForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpSmallItemReportAction extends AbstractAction {

	protected BpCSmallitemReportFacadeRemote remote;
	protected BpCReportTypeFacadeRemote typeRemote;

	public BpSmallItemReportAction() {
		remote = (BpCSmallitemReportFacadeRemote) factory
				.getFacadeRemote("BpCSmallitemReportFacade");
		typeRemote = (BpCReportTypeFacadeRemote) factory
				.getFacadeRemote("BpCReportTypeFacade");
	}

	/**
	 * 查找小指标报表列表
	 * 
	 * @throws JSONException
	 */
	public void findSmallItemReportList() throws JSONException {
		int start = Integer.parseInt(request.getParameter("start").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		String typeCode = request.getParameter("typeCode");
		PageObject obj = remote.findSmallItemReportList(typeCode, employee
				.getEnterpriseCode(),employee.getWorkerCode(), start, limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 小指标报表名称维护
	 */
	@SuppressWarnings("unchecked")
	public void smallItemReportMaint() throws Exception {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpCSmallitemReportForm> addlist = new ArrayList<BpCSmallitemReportForm>();
			List<BpCSmallitemReportForm> updatelist = new ArrayList<BpCSmallitemReportForm>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				BpCSmallitemReportForm model = new BpCSmallitemReportForm();
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setIsUse("Y");
				model.setModifyBy(employee.getWorkerCode());
				model.setModifyDate(new Date());
				if (data.get("reportId") != null
						&& !"".equals(data.get("reportId"))) {
					model.setReportId(Long.valueOf(data.get("reportId")
							.toString()));
				}
				if (data.get("reportName") != null
						&& !"".equals(data.get("reportName"))) {
					model.setReportName(data.get("reportName").toString());
				}
				if (data.get("dataType") != null
						&& !"".equals(data.get("dataType"))) {
					model.setDataType(data.get("dataType").toString());
				}
				if (data.get("rowHeadName") != null
						&& !"".equals(data.get("rowHeadName"))) {
					model.setRowHeadName(data.get("rowHeadName").toString());
				}
				if (data.get("columnNum") != null
						&& !"".equals(data.get("columnNum"))) {
					model.setColumnNum((Long) data.get("columnNum"));
				}

				if (data.get("typeCode") != null
						&& !"".equals(data.get("typeCode"))) {
					model.setTypeCode(data.get("typeCode").toString());
				}
				// 增加
				if (model.getReportId() == null
						|| "".equals(model.getReportId())) {
					if (remote.checkReportName(model.getReportName(), employee
							.getEnterpriseCode()) > 0) {
						write("{success: false,msg:'报表名称不能重复！'}");
						return;
					} else {
						addlist.add(model);
					}
				} else {
					if (!model.getReportName().equals(
							remote.findById(model.getReportId())
									.getReportName())) {
						if (remote.checkReportName(model.getReportName(),
								employee.getEnterpriseCode()) > 0) {
							write("{success: false,msg:'报表名称不能重复！'}");
							return;
						} else {
							updatelist.add(model);
						}
					} else {
						updatelist.add(model);
					}
				}
			}

			if (addlist.size() > 0)
				remote.save(addlist);

			if (updatelist.size() > 0)
				remote.update(updatelist);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				remote.delete(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;
		}
	}

	/**
	 * 查找小指标报表行设置列表
	 * 
	 * @throws JSONException
	 */
	public void findSmallItemReportRowTypeList() throws JSONException {
		String reportId = request.getParameter("reportId");
		List<BpCSmallitemRowtype> list = remote.findSmallReportRowSetList(Long
				.parseLong(reportId));
		write(JSONUtil.serialize(list));
	}

	/**
	 * 小指标报表行类型维护
	 */
	@SuppressWarnings("unchecked")
	public void smallItemReportRowTypeMaint() throws Exception {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpCSmallitemRowtype> addlist = new ArrayList<BpCSmallitemRowtype>();
			List<BpCSmallitemRowtype> updatelist = new ArrayList<BpCSmallitemRowtype>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				BpCSmallitemRowtype model = new BpCSmallitemRowtype();
				if (data.get("rowDatatypeId") != null
						&& !"".equals(data.get("rowDatatypeId"))) {
					model.setRowDatatypeId(Long.valueOf(data.get(
							"rowDatatypeId").toString()));
				}
				if (data.get("rowDatatypeName") != null
						&& !"".equals(data.get("rowDatatypeName"))) {
					model.setRowDatatypeName(data.get("rowDatatypeName")
							.toString());
				}
				if (data.get("reportId") != null
						&& !"".equals(data.get("reportId"))) {
					model.setReportId(Long.valueOf(data.get("reportId")
							.toString()));
				}
				if (data.get("orderBy") != null
						&& !"".equals(data.get("orderBy"))) {
					model.setOrderBy((Long) data.get("orderBy"));
				}
				if(data.get("isItem") != null ) {
					model.setIsItem(data.get("isItem").toString());
				}
				// 增加
				if (model.getRowDatatypeId() == null
						|| "".equals(model.getRowDatatypeId())) {
					if (remote.checkRowName(model.getReportId(), model
							.getRowDatatypeName()) > 0) {
						write("{success: false,msg:'行名称不能重复！'}");
						return;
					} else {
						addlist.add(model);
					}
				} else {
					if (!model.getRowDatatypeName().equals(
							remote.findByRowId(model.getRowDatatypeId())
									.getRowDatatypeName())) {
						if (remote.checkRowName(model.getReportId(), model
								.getRowDatatypeName()) > 0) {
							write("{success: false,msg:'行名称不能重复！'}");
							return;
						} else {
							updatelist.add(model);
						}
					} else {
						updatelist.add(model);
					}
				}
			}

			if (addlist.size() > 0)
				remote.saveRow(addlist);

			if (updatelist.size() > 0)

				remote.updateRow(updatelist);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.deleteRow(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;
		}
	}

	/**
	 * 取得小指标报表中关联的指标
	 * 
	 * @throws JSONException
	 */
	public void getRelationItems() throws JSONException {
		Long reportId = Long.parseLong(request.getParameter("reportId"));
		List<BpCSmallitemRelation> list = remote.getRelationItems(reportId); 
		write(JSONUtil.serialize(list));
	}

	/**
	 * 小指标报表指标维护
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void smallReprtItemsMaint() throws Exception {
		try {
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpCSmallitemRelation> addlist = new ArrayList<BpCSmallitemRelation>();
			List<BpCSmallitemRelation> updatelist = new ArrayList<BpCSmallitemRelation>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				BpCSmallitemRelation model = new BpCSmallitemRelation();
				if (data.get("id") != null && !"".equals(data.get("id"))) {
					model.setId(Long.valueOf(data.get("id").toString()));
				}
				if (data.get("reportId") != null
						&& !"".equals(data.get("reportId"))) {
					model.setReportId(Long.valueOf(data.get("reportId")
							.toString()));
				}
				if (data.get("itemCode") != null
						&& !"".equals(data.get("itemCode"))) {
					model.setItemCode(data.get("itemCode").toString());
				}
				if (data.get("itemAlias") != null
						&& !"".equals(data.get("itemAlias"))) {
					model.setItemAlias(data.get("itemAlias").toString());
				}
				if (data.get("dataType") != null
						&& !"".equals(data.get("dataType"))) {
					model.setDataType(data.get("dataType").toString());
				}
				if (data.get("orderBy") != null
						&& !"".equals(data.get("orderBy"))) {
					model.setOrderBy((Long) data.get("orderBy"));
				}
				if (data.get("bpCSmallitemRowtype.rowDatatypeId") != null
						&& !"".equals(data
								.get("bpCSmallitemRowtype.rowDatatypeId"))) {
					model.setBpCSmallitemRowtype(remote.findByRowId(Long
							.valueOf(data.get(
									"bpCSmallitemRowtype.rowDatatypeId")
									.toString())));
				}
				if(data.get("compluteMethod") != null)
				{
					model.setCompluteMethod(data.get("compluteMethod").toString());
				}
				if(data.get("isIgnoreZero") != null)
				{
					model.setIsIgnoreZero(data.get("isIgnoreZero").toString());
				}
				// 增加
				if (model.getId() == null || "".equals(model.getId())) {
					addlist.add(model);
				} else {
					updatelist.add(model);
				}
			}

			if (addlist.size() > 0) {
				for (int i = 0; i < addlist.size(); i++) {
					BpCSmallitemRelation model = addlist.get(i);
					if (remote.checkSame(model)) {
						write("{success: false,msg:'保存失败，报表同一行列只能对应一个指标！'}");
						return;
					} else {
						remote.saveItemToReport(model);
					}
				}

			}

			if (updatelist.size() > 0)

				for (int i = 0; i < updatelist.size(); i++) {
					BpCSmallitemRelation model2 = updatelist.get(i);
//					if (remote.checkSame(model2)) {
//						write("{success: false,msg:'保存失败，报表同一行列只能对应一个指标！！'}");
//						return;
//					} else {
						remote.saveItemToReport(model2);
//					}
				}

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.deleteItemsFromReport(deleteIds);

			write("{success: true,msg:'保存成功！'}");
		} catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;
		}
	}
	/**
	 * 计算值
	 */
	private void handTotalValue(List<SmallReportForm> datalists)
	{
//		  try{
//			  System.out.println(JSONUtil.serialize(datalists));
			  if(datalists !=null)
			  {
				  for(SmallReportForm srf : datalists)
				  {
					  //计算值时
					  if("0".equals(srf.getIsItem()))
					  {
						  srf.setDataValue(this.getValue(datalists, srf.getOrderBy(), srf.getCompluteMethod(),srf.getIsIgnoreZero()));
					  }
				  }
			  }
			  
//		  }catch(JSONException exc)
//		  {
//			  
//		  }
	}
	/**
	 * 
	 * @param datalists   
	 * @param columnCard 列标识
	 * @param compluteMethod 计算方法
	 */
	private Double getValue(List<SmallReportForm> datalists,Long columnCard,String compluteMethod,String isIgnoreZero)
	{
		Double d = null;
		int notZeroCount = 0;
		for(SmallReportForm srf : datalists)
		  {
			if("0".equals(srf.getIsItem()))
				continue;
			if (srf.getOrderBy().equals(columnCard)) {
				if ("ADD".equalsIgnoreCase(compluteMethod)
						|| "AVG".equalsIgnoreCase(compluteMethod)) {
					if (d == null)
						d = 0d;
					if (srf.getDataValue() != null)
						d += srf.getDataValue();
					if ("AVG".equalsIgnoreCase(compluteMethod)
							&& ((srf.getDataValue() != null && !srf
									.getDataValue().equals(0d)) || "0"
									.equals(isIgnoreZero))) {
						notZeroCount++;
					}
				} else if ("MAX".equalsIgnoreCase(compluteMethod)) {
					if (d == null
							|| (srf.getDataValue() != null && srf
									.getDataValue() > d)) {
						d = srf.getDataValue();
					}
				} else if ("MIN".equalsIgnoreCase(compluteMethod)) {
					if (d == null
							|| (srf.getDataValue() != null && srf
									.getDataValue() < d)) {
						d = srf.getDataValue();
					}
				}
			}
		  }
		if("AVG".equalsIgnoreCase(compluteMethod))
		{ 
			if(notZeroCount != 0)
			d = d/notZeroCount;
		} 
		return d;
	}

	private List<SmallReportForm> filterList(List<SmallReportForm> datalists,
			Long typeId) {
		List<SmallReportForm> list = new ArrayList<SmallReportForm>();
		if (datalists != null) {
			for (SmallReportForm row : datalists) {
				if (row.getRowDatatypeId().equals(typeId)) {
					list.add(row);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public void smallItemReportQuery() {
		String reportId = request.getParameter("reportId");
		String querydate = request.getParameter("date");
		String dateType = request.getParameter("dateType");
		String quarter = request.getParameter("quarter");

		BpCSmallitemReport reportmodel = remote.findById(Long
				.parseLong(reportId));

		List<SmallReportForm> headerlist = remote.getSamallReportHeader(Long
				.parseLong(reportId));

		Long maxcolumn = reportmodel.getColumnNum();// 显示列数
		long totalcolumn = headerlist.size();// 总列数
		BigDecimal b1 = new BigDecimal(totalcolumn);
		BigDecimal b2 = new BigDecimal(maxcolumn);
		long gridnum = b1.divide(b2, 0, BigDecimal.ROUND_CEILING).intValue();// grid数

		List<BpCSmallitemRowtype> rowlist = remote
				.findSmallReportRowSetList(Long.parseLong(reportId));
		String str = "{ 'data':[";
		List<SmallReportForm> datalists = remote.getSamallReportData(querydate,
				quarter, dateType, Long.parseLong(reportId), null);
		handTotalValue(datalists); 
		for (int i = 0; i < rowlist.size(); i++) {
			BpCSmallitemRowtype rowmodel = rowlist.get(i);
			str += "{'rowname':'" + rowmodel.getRowDatatypeName() + "'";
			List<SmallReportForm> datalist = null;
//			if ("合计".equals(rowmodel.getRowDatatypeName())) {
//				datalist = filterList(datalists, rowmodel.getRowDatatypeId());
//			} else { 
//				datalist = filterList(datalists, rowmodel.getRowDatatypeId());
//			}
			datalist = filterList(datalists, rowmodel.getRowDatatypeId());
				
				for (int j = 0; j < datalist.size(); j++) {
					SmallReportForm datamodel = datalist.get(j);
					DecimalFormat df1 = new DecimalFormat("0");
					if ("0".equals(datamodel.getDataType()))
						df1 = new DecimalFormat("0");
					if ("1".equals(datamodel.getDataType()))
						df1 = new DecimalFormat("0.0");
					if ("2".equals(datamodel.getDataType()))
						df1 = new DecimalFormat("0.00");
					if ("3".equals(datamodel.getDataType()))
						df1 = new DecimalFormat("0.000");
					if ("4".equals(datamodel.getDataType()))
						df1 = new DecimalFormat("0.0000");
					if (datamodel.getDataValue() != null) {
						str += ",'" + datamodel.getItemAlias() + "':'"
								+ df1.format(datamodel.getDataValue()) + "'";
					} else {
						str += ",'" + datamodel.getItemAlias() + "':''";
					}
				}
				str += "},"; 
		}

		if (str.equals("{ 'data':[")) {
			str += "]";
		} else {
			str = str.substring(0, str.length() - 1) + "]";
		}

		if (gridnum == 0) {
			str += ",'columModle':[]";
		} else {
			for (int z = 1; z <= gridnum; z++) {
				str += ",'columModle" + z + "':["; 
				long start = (z - 1) * maxcolumn + 1;
				long end = z * maxcolumn;
				str += "{'header' : '"
						+ reportmodel.getRowHeadName()
						+ "','width':180,'dataIndex' : 'rowname','align':'center','resizable': false,'fixed':true}";
				while ((start <= end) && (start <= totalcolumn)) {
					SmallReportForm model = headerlist.get((int) (start - 1));
					str += ",{'header' : '"
							+ model.getItemAlias()
							+ "<br>("
							+ model.getUnitName()
							+ ")','width':180,'dataIndex' : '"// modify by
							// ywliu
							// 20090911
							+ model.getItemAlias()
							+ "','align':'center','resizable': false}";
					start++;
				}
				str += "]";
			}
		}
		str += ",'fieldsNames' : [";
		if (headerlist.size() > 0) {
			Iterator it = headerlist.iterator();
			str += "{'name':'rowname'}";
			while (it.hasNext()) {
				SmallReportForm model = (SmallReportForm) it.next();
				str += ",{'name':'" + model.getItemAlias() + "'}";
			}
		}
		str += "]";
		str += ",'gridnum': '" + gridnum + "'}";
		write(str);
	}
}
