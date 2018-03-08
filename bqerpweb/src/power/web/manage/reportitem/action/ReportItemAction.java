package power.web.manage.reportitem.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.reportitem.BpCCbmReportBlock;
import power.ejb.manage.reportitem.BpCCbmReportItem;
import power.ejb.manage.reportitem.BpCCbmReportName;
import power.ejb.manage.reportitem.BpJCbmReport;
import power.ejb.manage.reportitem.BpReportItemManage;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class ReportItemAction extends AbstractAction
{
	private BpReportItemManage remote;
	public ReportItemAction()
	{
		remote = (BpReportItemManage)factory.getFacadeRemote("BpReportItemManageImpl");
	}
	
	/**
	 * 查找所有的报表维护数据
	 * @throws JSONException 
	 */
	public void findAllReportRec() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remote.findAllReportRec(employee.getEnterpriseCode(), 
					Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = remote.findAllReportRec(employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	
	/**
	 * 批量保存修改的报表维护数据
	 * @throws JSONException 
	 */
	public void saveModiReportEntity() throws JSONException
	{
		List<BpCCbmReportName> addList = new ArrayList<BpCCbmReportName>();
		List<BpCCbmReportName> updateList = new ArrayList<BpCCbmReportName>();
		
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		List<Map> addMapList = (List<Map>)(JSONUtil.deserialize(isAdd));
		List<Map> updateMapList = (List<Map>)(JSONUtil.deserialize(isUpdate));
		
		if(addMapList != null && addMapList.size() > 0)
		{
			for(Map map : addMapList)
			{
				BpCCbmReportName entity = this.parseReportNameInstance(map);
				addList.add(entity);
			}
		}
		if(updateMapList != null && updateMapList.size() > 0)
		{
			for(Map map : updateMapList)
			{
				BpCCbmReportName entity = this.parseReportNameInstance(map);
				updateList.add(entity);
			}
		}
		remote.saveModiReportEntity(addList, updateList,ids);
	}
	
	/**
	 * 将一映射转为报表名称对象
	 * @param map
	 * @return
	 */
	public BpCCbmReportName parseReportNameInstance(Map map)
	{
		BpCCbmReportName entity = new BpCCbmReportName();
		Long reportId = null;
		String reportName = null;
		String reportType = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("reportId") != null)
			reportId = Long.parseLong(map.get("reportId").toString());
		if(map.get("reportName") != null)
			reportName = map.get("reportName").toString();
		if(map.get("reportType") != null)
			reportType = map.get("reportType").toString();
		
		entity.setReportId(reportId);
		entity.setReportName(reportName);
		entity.setReportType(reportType);
		entity.setIsUse(isUse);
		entity.setEnterpriseCode(enterpriseCode);
		
		return entity;
	}
	
	
	
	/**
	 * 批量保存指标维护数据
	 * @throws JSONException 
	 */
	public void saveReportItemModi() throws JSONException
	{
		List<BpCCbmReportItem> addList = new ArrayList<BpCCbmReportItem>();
		List<BpCCbmReportItem> updateList = new ArrayList<BpCCbmReportItem>();
		
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		List<Map> addMapList = (List<Map>)(JSONUtil.deserialize(isAdd));
		List<Map> updateMapList = (List<Map>)(JSONUtil.deserialize(isUpdate));
		
		if(addMapList != null && addMapList.size() > 0)
		{
			for(Map map : addMapList)
			{
				BpCCbmReportItem entity = this.parseItemInstance(map);
				addList.add(entity);
			}
		}
		if(updateMapList != null && updateMapList.size() > 0)
		{
			for(Map map : updateMapList)
			{
				BpCCbmReportItem entity = this.parseItemInstance(map);
				updateList.add(entity);
			}
		}
		remote.saveReportItemModi(addList, updateList,ids);
	}

	/**
	 * 通过条件查询报表指标维护数据列表
	 * @throws JSONException
	 */
	public void getAllReportItemList() throws JSONException
	{
		String theme = request.getParameter("theme"); 
		String queryText = request.getParameter("queryText"); 
		String reportId = request.getParameter("reportId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remote.getAllReportItemList(reportId,theme, queryText, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else 
			pg = remote.getAllReportItemList(reportId,theme, queryText, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	/**
	 * 将以映射转化为报表指标
	 * @param map
	 * @return
	 */
	public BpCCbmReportItem parseItemInstance(Map map)
	{
		BpCCbmReportItem entity = new BpCCbmReportItem();
		Long itemId = null;
		Long reportId = null;
		String itemCode = null;
		String itemName = null;
		Long unitId = null;
		String alias = null;
		Long topicId = null;
		String timeType = null;
		Long displayNo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("itemId") != null)
			itemId = Long.parseLong(map.get("itemId").toString());
		if(map.get("reportId") != null)
			reportId = Long.parseLong(map.get("reportId").toString());
		if(map.get("itemCode") != null)
			itemCode = map.get("itemCode").toString();
		if(map.get("itemName") != null)
			itemName = map.get("itemName").toString();
		if(map.get("unitId") != null)
			unitId = Long.parseLong(map.get("unitId").toString());
		if(map.get("alias") != null)
			alias = map.get("alias").toString();
		if(map.get("topicId") != null)
			topicId = Long.parseLong(map.get("topicId").toString());
		if(map.get("displayNo") != null)
			displayNo = Long.parseLong(map.get("displayNo").toString());
		if(map.get("timeType") != null)
			timeType = map.get("timeType").toString();
		
		entity.setItemId(itemId);
		entity.setReportId(reportId);
		entity.setItemCode(itemCode);
		entity.setItemName(itemName);
		entity.setUnitId(unitId);
		entity.setAlias(alias);
		entity.setTopicId(topicId);
		entity.setTimeType(timeType);
		entity.setDisplayNo(displayNo);
		entity.setIsUse(isUse);
		entity.setEnterpriseCode(enterpriseCode);
		return entity;
	}
	
	/**
	 * 通过报表id查找该报表机组
	 * @throws JSONException 
	 */
	public void getReportBlockList() throws JSONException
	{
		String reportId = request.getParameter("reportId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remote.getReportBlockList(reportId, employee.getEnterpriseCode(), 
					Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = remote.getReportBlockList(reportId, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	/**
	 * 批量保存或删除报表机组数据
	 * @throws JSONException 
	 */
	public void saveReportBlockModi() throws JSONException
	{
		List<BpCCbmReportBlock> addList = new ArrayList<BpCCbmReportBlock>();
		List<BpCCbmReportBlock> updateList = new ArrayList<BpCCbmReportBlock>();
		
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		List<Map> addMapList = (List<Map>)(JSONUtil.deserialize(isAdd));
		List<Map> updateMapList = (List<Map>)(JSONUtil.deserialize(isUpdate));
		
		if(addMapList != null && addMapList.size() > 0)
		{
			for(Map map : addMapList)
			{
				BpCCbmReportBlock entity = this.parseBlockInstance(map);
				addList.add(entity);
			}
		}
		if(updateMapList != null && updateMapList.size() > 0)
		{
			for(Map map : updateMapList)
			{
				BpCCbmReportBlock entity = this.parseBlockInstance(map);
				updateList.add(entity);
			}
		}
		remote.saveReportBlockModi(addList, updateList, ids);
	}
	/**
	 * 将一映射转化为机组对象
	 * @param map
	 * @return
	 */
	public BpCCbmReportBlock parseBlockInstance(Map map)
	{
		BpCCbmReportBlock entity = new BpCCbmReportBlock();
		Long blockId = null;
		Long reportId = null;
		String blockName = null;
		Long displayNo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("blockId") != null)
			blockId = Long.parseLong(map.get("blockId").toString());
		if(map.get("reportId") != null)
			reportId = Long.parseLong(map.get("reportId").toString());
		if(map.get("blockName") != null)
			blockName = map.get("blockName").toString();
		if(map.get("displayNo") != null)
			displayNo = Long.parseLong(map.get("displayNo").toString());
		
		entity.setBlockId(blockId);
		entity.setReportId(reportId);
		entity.setBlockName(blockName);
		entity.setDisplayNo(displayNo);
		entity.setIsUse(isUse);
		entity.setEnterpriseCode(enterpriseCode);
		return entity;
	}
	
	
	/**
	 * 获得报表指标录入数据
	 */
	public void getReportItemInputValue()
	{
		String reportId = request.getParameter("reportId");
		String yearMonth = request.getParameter("yearMonth");
		String topicId = request.getParameter("topicId");
		//查询报表指标录入数据 元素0，列表dataIndex数据；元素1，列表header数据；其余为数据
		List list = remote.getReportItemInputValue(reportId, yearMonth,topicId, employee.getEnterpriseCode());
		//数据拼写开始
		 String str = "{'data':[";
		if(list != null && list.size() > 2)
		{
			for(int i = 2; i < list.size(); i++)
			{
				str += "{";
				Object[] data = (Object[])list.get(i);
				Object[] dataIndex = (Object[])list.get(0);
				for(int j =0; j < dataIndex.length; j++)
				{
					// 补充控制用
					if(dataIndex[j].toString().startsWith("^*"))
						dataIndex[j] = dataIndex[j].toString().replaceAll("\\^\\*", "");
					
					str += "'"+dataIndex[j]+"':'"+(data[j] == null ? "" : data[j].toString())+"',";
				}
				if(str.endsWith(","));
					str = str.substring(0,str.length() - 1);
				str += "},";
			}
		}
		if(str.equals("{'data':["))
			str += "]";
		else
			str = str.substring(0,str.length() - 1) + "]";
//		System.out.println(str);
		// data 值拼写结束
		// 列模式拼写开始
		str += ",'columModel':[new Ext.grid.RowNumberer({header : '序号',width : 35}),";
		str += "{'header' : '主题Id','hidden' :"+(reportId.equals("2")? "true" : "true")+",'width':100,'dataIndex' : 'topicId','align':'center'},"
			 +"{'header' : '主题','hidden' : "+(reportId.equals("2")? "false" : "true")+",'width':100,'dataIndex' : 'topicName','align':'center'},"
			 +"{'header' : '时间类型','hidden' : "+(reportId.equals("2")? "true" : "true")+",'width':100,'dataIndex' : 'timeType','align':'center'},"
			 +"{'header' : '报表Id','width':100,'hidden':'true','dataIndex' : 'reportId','align':'center'},"
			 +"{'header' : '指标Id','width':100,'hidden':'true','dataIndex' : 'itemId','align':'center'},"
			 +"{'header' : '指标名称','width':100,'dataIndex' : 'alias','align':'center'},"
			 +"{'header' : '单位','width':100,'dataIndex' : 'unitName','align':'center'},"
			 +"{'header' : '年月时间','width':100,'hidden':'true','dataIndex' : 'yearMonth','align':'center'}";
		if(list != null && list.size() > 1)
		{
			Object[] dataIndex = (Object[])list.get(0);
			Object[] header = (Object[])list.get(1);
			for (int i = 10; i < header.length; i++)
				str += ",{'header':'"
						+ header[i].toString()
						+ "','width': 100,'dataIndex': '"
						+ dataIndex[i].toString()
						+ "','align':'center'"
						+ ",'editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 4})}";
			
		}
		// 列模式拼写结束
		// 对象域拼写开始
		str += "],'fieldsNames' : [";
		if(list != null && list.size() > 0)
		{
			Object[] fields = (Object[])list.get(0);
			for(int i = 0; i<= fields.length - 1; i++)
			 {
				// 补充控制用
				if(fields[i].toString().startsWith("^*"))
					fields[i] = fields[i].toString().replaceAll("\\^\\*", "");
				
				 str += "{'name':'" + fields[i] + "'},";
			 }
			 if(str.endsWith(","))
			{
				str = str.substring(0,str.length() - 1);
			}
		 }
		 str += "]}";
		write(str);
	}
	
	public void judgeAddOrUpdate()
	{
		String reportId = request.getParameter("reportId");
		String yearMonth = request.getParameter("yearMonth");
		int flag = remote.judgeAddOrUpdate(reportId,yearMonth);
		if(flag == 0)
			write("{success:true,method:'add'}");
		else
			write("{success:true,method:'update'}");
	}
	
	public void clearReportItemInput()
	{
		String reportId = request.getParameter("reportId");
		String yearMonth = request.getParameter("yearMonth");
		remote.clearReportItemInput(reportId,yearMonth,employee.getEnterpriseCode());
	}
	
	public void saveReportItemInput() throws JSONException
	{
		String mod = request.getParameter("mod");
		String method = request.getParameter("method");
		List<Map> listMap = (List<Map>)(JSONUtil.deserialize(mod));
		List<BpJCbmReport> modList = new ArrayList<BpJCbmReport>();
		
		for(Map map : listMap)
		{
			BpJCbmReport entity = this.parseItemInputInstance(map);
			modList.add(entity);
		}
		
		remote.saveReportItemInput(modList,method);
	}
	
	private BpJCbmReport parseItemInputInstance(Map map)
	{
		BpJCbmReport entity = new BpJCbmReport();
		Long itemId = null;
		Long blockId = null;
		Long reportId = null;
		String yearMonth = null;
		Double value = null;
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("itemId") != null)
			itemId = Long.parseLong(map.get("itemId").toString());
		if(map.get("blockId") != null)
			blockId = Long.parseLong(map.get("blockId").toString());
		if(map.get("reportId") != null)
			reportId = Long.parseLong(map.get("reportId").toString());
		if(map.get("yearMonth") != null)
			yearMonth = map.get("yearMonth").toString();
		if(map.get("value") != null)
			value = Double.parseDouble(map.get("value").toString());
			
		entity.setItemId(itemId);
		entity.setBlockId(blockId);
		entity.setReportId(reportId);
		entity.setYearMonth(yearMonth);
		entity.setValue(value);
		entity.setEnterpriseCode(enterpriseCode);
		return entity;
	}
}