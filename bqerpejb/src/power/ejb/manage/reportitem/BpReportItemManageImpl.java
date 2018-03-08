package power.ejb.manage.reportitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * @author liuyi 20100121
 */
@Stateless
public class BpReportItemManageImpl implements BpReportItemManage
{

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PageObject findAllReportRec(String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.report_id,\n" +
			" a.report_name,\n" + 
			" a.report_type\n" + 
			" from BP_C_CBM_REPORT_NAME a\n" + 
			" where a.is_use='Y'\n" + 
			" and a.enterprise_code='"+enterpriseCode+"' \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;

	}

	public void saveModiReportEntity(List<BpCCbmReportName> addList,
			List<BpCCbmReportName> updateList,String ids) {
		if(addList != null && addList.size() > 0)
		{
			Long id = bll.getMaxId("BP_C_CBM_REPORT_NAME", "report_id");
			for(BpCCbmReportName entity : addList)
			{	
				entity.setReportId(id++);
				entityManager.persist(entity);
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(BpCCbmReportName entity : updateList)
				entityManager.merge(entity);
		}
		if(ids != null && ids.length() > 0)
		{
			// 删除报表表中的数据
			String sql1 = "update BP_C_CBM_REPORT_NAME a set a.is_use='N' where a.report_id in ("+ids+")";
			// 删除指标表中的数据
			String sql2 = "update BP_C_CBM_REPORT_ITEM a set a.is_use='N' where a.report_id in ("+ids+") ";
			// 数据机组表中数据
			String sql3 = "update BP_C_CBM_REPORT_BLOCK a set a.is_use='N' where a.report_id in ("+ids+") ";
			// 删除指标录入表中的数据
			String sql4 = "delete from BP_J_CBM_REPORT b where b.report_id in ("+ids+") ";
			List<String> list = new ArrayList<String>(4);
			list.add(sql1);
			list.add(sql2);
			list.add(sql3);
			list.add(sql4);
			bll.exeNativeSQL(list);
		}
	}


	public PageObject getAllReportItemList(String reportId,String theme, String queryText,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.item_id,\n" +
			" a.report_id,\n" + 
			" a.item_code,\n" + 
			" a.item_name,\n" + 
			" a.unit_id,\n" + 
			" a.alias,\n" + 
			" a.topic_id,\n" + 
			" a.time_type,\n" + 
			" a.display_no,\n" + 
			" b.report_name,\n" + 
			" b.report_type,\n" + 
			" getunitname(a.unit_id),\n" + 
			" c.topic_name,\n" + 
			" c.display_no as topicdisplayno\n" + 
			" from BP_C_CBM_REPORT_ITEM a,BP_C_CBM_REPORT_NAME b,BP_C_CBM_TOPIC c\n" + 
			" where a.report_id=b.report_id\n" + 
			" and a.topic_id=c.topic_id(+) \n" + 
			" and a.is_use='Y'\n" + 
			" and b.is_use='Y'\n" + 
			" and c.is_use(+)='Y'\n" + 
			" and a.enterprise_code='"+enterpriseCode+"'\n" + 
			" and b.enterprise_code='"+enterpriseCode+"'\n" + 
			" and c.enterprise_code(+)='"+enterpriseCode+"'\n";
		if(reportId != null && !reportId.equals(""))
			sql += " and a.report_id=" + reportId + " \n";
		if(theme != null && !theme.equals(""))
			sql +=" and a.topic_id="+theme+" \n";
		if(queryText != null && !queryText.equals(""))
			sql += " and (a.item_code like '%"+queryText+"%' or a.alias like '%"+queryText+"%') \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by a.topic_id,c.display_no,a.display_no";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
		

	}


	public void saveReportItemModi(List<BpCCbmReportItem> addList,
			List<BpCCbmReportItem> updateList, String ids) {
		if(ids != null && ids.length() > 0)
		{
			// 删除指标表中数据
			String sql1 = "update BP_C_CBM_REPORT_ITEM a set a.is_use='N' where a.item_id in ("+ids+") \n";
			// 删除指标录入数据
			String sql2 = "delete from BP_J_CBM_REPORT b where b.item_id in ("+ids+") \n";
			List<String> list = new ArrayList<String>(2);
			list.add(sql1);
			list.add(sql2);
			bll.exeNativeSQL(list);
		}
		if(addList != null && addList.size() > 0)
		{
			Long tabId = Long.parseLong(bll.getMaxId("BP_C_CBM_REPORT_ITEM", "ITEM_ID").toString());
			for(BpCCbmReportItem entity : addList)
			{
				entity.setItemId(tabId++);
				entityManager.persist(entity);
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(BpCCbmReportItem entity : updateList)
				entityManager.merge(entity);
		}
	}

	public PageObject getReportBlockList(String reportId,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select a.block_id,\n" +
			" a.report_id,\n" + 
			" a.block_name,\n" + 
			" a.display_no,\n" + 
			" b.report_name,\n" + 
			" b.report_type\n" + 
			" from BP_C_CBM_REPORT_BLOCK a,BP_C_CBM_REPORT_NAME b\n" + 
			" where a.report_id=b.report_id\n" + 
			" and a.is_use='Y'\n" + 
			" and b.is_use='Y'\n" + 
			" and a.enterprise_code='"+enterpriseCode+"'\n" + 
			" and b.enterprise_code='"+enterpriseCode+"' \n";
		if(reportId != null && !reportId.equals(""))
			sql += " and a.report_id="+reportId+"  \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by a.display_no \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;

	}

	public void saveReportBlockModi(List<BpCCbmReportBlock> addList,
			List<BpCCbmReportBlock> updateList, String ids) {
		if(addList != null && addList.size() > 0)
		{
			Long nextId = Long.parseLong(bll.getMaxId("BP_C_CBM_REPORT_BLOCK", "BLOCK_ID").toString());
			for(BpCCbmReportBlock entity : addList)
			{
				entity.setBlockId(nextId++);
				entityManager.persist(entity);
			}
		}
		if(updateList != null && updateList.size() > 0)
		{
			for(BpCCbmReportBlock entity : updateList)
				entityManager.merge(entity);
		}
		if(ids != null && ids.length() > 0)
		{
			// 删除报表机组表中数据
			String sql1 = "update BP_C_CBM_REPORT_BLOCK a set a.is_use='N' where a.block_id in ("+ids+") \n";
			// 删除报表录入表中数据
			String sql2 = "delete from BP_J_CBM_REPORT b where b.block_id in ("+ids+")";
			List<String> list = new ArrayList<String>(2);
			list.add(sql1);
			list.add(sql2);
			bll.exeNativeSQL(list);
		}
	}

	@SuppressWarnings("unchecked")
	public List getReportItemInputValue(String reportId, String yearMonth,String topicId,
			String enterpriseCode) {
//		List list = new ArrayList();
		String sqlValueCount = 
			"select count(*) from BP_J_CBM_REPORT a\n" +
			" where a.enterprise_code='"+enterpriseCode+"' \n";
		if(reportId != null && !reportId.equals(""))
			sqlValueCount +=" and a.report_id="+reportId+" \n";
		if(yearMonth != null && !yearMonth.equals(""))
			sqlValueCount +=" and a.year_month='"+yearMonth+"' ";
		int valueCount = Integer.parseInt(bll.getSingal(sqlValueCount).toString());
		
		String sqlBlock = 
			"select * from BP_C_CBM_REPORT_BLOCK a\n" +
			"where a.is_use='Y'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"' \n";
		if(reportId != null && !reportId.equals(""))
			sqlBlock +=" and a.report_id="+reportId+" \n";
		sqlBlock += " order by a.DISPLAY_NO \n";
		List<BpCCbmReportBlock> blockList = bll.queryByNativeSQL(sqlBlock, BpCCbmReportBlock.class);
	 

		String sqlDataIndex = "select '^*topicId','topicName','^*timeType','reportId','^*itemId','alias','^*unitName','yearMonth','itemOrder','themeOrder'\n";
		String sqlHeader = "select '主题Id','主题名称','时间类型','报表Id','指标Id','指标名称','单位名称','年月','指标排序','主题排序'\n";
		String sql = "";
		// 新增时
		if(valueCount == 0)
		{
			if(reportId != null && !reportId.equals("2"))
				sql = "select distinct '' as topicId,'' as topicName,'' as timeType,'"+reportId+"' as report_id,b.item_id,b.alias,getunitname(b.unit_id),'"+yearMonth+"' as yearmonth,b.display_no as itemOrder,'' as themeOrder\n";
			else
				sql = "select distinct b.TOPIC_ID,c.TOPIC_NAME,b.TIME_TYPE,to_char(to_number('"+reportId+"')) as report_id,b.item_id,b.alias,getunitname(b.unit_id),'"+yearMonth+"' as yearmonth,b.display_no as itemOrder,c.DISPLAY_NO\n";
			
			if(blockList != null && blockList.size() > 0)
			{
				for(BpCCbmReportBlock block : blockList)
				{
					sqlDataIndex +=",'"+block.getBlockId()+"' ";
					sqlHeader +=",'"+block.getBlockName()+"' ";
					sql += ",'0' ";
				}
			}
			
			if (reportId != null && !reportId.equals("2"))
				sql += "from BP_C_CBM_REPORT_ITEM b\n"
						+ " where b.is_use='Y'\n" + " and b.enterprise_code='"
						+ enterpriseCode + "'\n" 
						+ " and b.report_id='"+reportId+"' \n"
						+ " order by b.display_no";
			else {
				sql += "from BP_C_CBM_REPORT_ITEM b,BP_C_CBM_TOPIC c\n"
						+ " where b.is_use='Y'\n" + " and c.is_use='Y' \n"
						+ " and b.report_id='" + reportId + "' \n"
						+ " and b.TOPIC_ID=c.TOPIC_ID \n"
						+ " and b.enterprise_code='" + enterpriseCode + "'\n"
						+ " and c.enterprise_code='" + enterpriseCode + "'\n";
				if(topicId != null && !topicId.equals(""))
					sql += " and b.TOPIC_ID=" + topicId + " \n";
				sql += " order by b.display_no,c.DISPLAY_NO \n";
			}

			

		}
		// 报表指标录入数据存在时
		else{
			sql = "select distinct b.TOPIC_ID,c.TOPIC_NAME,b.TIME_TYPE,a.report_id,a.item_id,b.alias,getunitname(b.unit_id),a.year_month,b.display_no as itemOrder,c.DISPLAY_NO themeOrder\n";
			
			if(blockList != null && blockList.size() > 0)
			{
				for(BpCCbmReportBlock block : blockList)
				{
					sqlDataIndex +=",'"+block.getBlockId()+"' ";
					sqlHeader +=",'"+block.getBlockName()+"' ";
					sql += ",(select t.value from BP_J_CBM_REPORT t where t.item_id=a.item_id and t.block_id="+block.getBlockId() +
							" and t.report_id=a.report_id and t.year_month=a.year_month)";					
				}
			}
						
			
			sql += "  from BP_J_CBM_REPORT a,BP_C_CBM_REPORT_ITEM b,BP_C_CBM_TOPIC c\n" +
				"where a.item_id=b.item_id\n" + 
				"and a.enterprise_code='"+enterpriseCode+"'\n" + 
				"and b.enterprise_code='"+enterpriseCode+"'\n" + 
				"and b.is_use='Y'\n"
				+ " and b.TOPIC_ID=c.TOPIC_ID(+) \n";
			if(reportId != null && !reportId.equals(""))
			{
				sql +=" and a.report_id="+reportId+" \n";
				if(!reportId.equals("2"))
				{
					if (yearMonth != null && !yearMonth.equals(""))
						sql += " and a.year_month='" + yearMonth + "' \n";
					sql += "  order by b.display_no \n";
				}
				else if(reportId.equals("2"))
				{
					if (yearMonth != null && !yearMonth.equals(""))
						sql += " and a.year_month like '" + yearMonth + "%' \n";
					if (topicId != null && !topicId.equals(""))
						sql += " and b.TOPIC_ID=" + topicId + " \n";
					sql += "  order by b.display_no \n";
					if (reportId != null && reportId.equals("2"))
						sql += ",c.DISPLAY_NO,a.YEAR_MONTH \n";
				}
			}
				
//			if (reportId != null && reportId.equals("2")) {
//				if(yearMonth != null && !yearMonth.equals(""))
//					sql +=" and a.year_month like '"+yearMonth+"%' \n";
//			}
//			else if(yearMonth != null && !yearMonth.equals(""))
//				sql +=" and a.year_month='"+yearMonth+"' \n";
//			if (reportId != null && reportId.equals("2")) {
//				if (topicId != null && !topicId.equals(""))
//					sql += " and b.TOPIC_ID=" + topicId + " \n";
//			}
//			sql += "  order by b.display_no \n";
//			if (reportId != null && reportId.equals("2"))
//				sql += ",c.DISPLAY_NO \n";
		}
		
		sqlDataIndex += " from dual\n";
		sqlHeader += "  from dual\n";
		
		List titleList=bll.queryByNativeSQL(sqlDataIndex+"\n union \n "+sqlHeader);
//		List dataIndex = bll.queryByNativeSQL(sqlDataIndex);
//		List header = bll.queryByNativeSQL(sqlHeader);
		List dataList = bll.queryByNativeSQL(sql);
		dataList.add(0,titleList.get(0));
		dataList.add(1,titleList.get(1));
		// 年月显示控制用
		if (valueCount == 0) {
			for (int i = 2; i < dataList.size(); i++) {
				Object[] temp = (Object[]) dataList.get(i);
				temp[7] = yearMonth;
			}
		}
		
		String[] timeArray = {yearMonth+"-01",yearMonth+"-02",yearMonth+"-03",yearMonth+"-04"
				,yearMonth+"-05",yearMonth+"-06",yearMonth+"-07",yearMonth+"-08"
				,yearMonth+"-09",yearMonth+"-10",yearMonth+"-11",yearMonth+"-12"};
		String[] itemDispArray = {"1月","2月","3月","4月","5月","6月"
				,"7月","8月","9月","10月","11月","12月"};
		// 分主题指标显示 新增
		if (valueCount == 0 && reportId != null && reportId.equals("2")) {
			List thinkTopicList = new ArrayList();
			thinkTopicList.add(0,dataList.get(0));
			thinkTopicList.add(1,dataList.get(1));
			for(int i = 2; i <= dataList.size() - 1; i++)
			{
				Object[] temp = (Object[])dataList.get(i);
//				if(i != 2)
//				{
//					Object[] preTemp = (Object[])dataList.get(i - 1);
//					if(temp[1] == preTemp[1])
//						temp[1] = "";
//				}
				thinkTopicList.add(dataList.get(i));
				
				// 1为月指标
				if(temp[2] != null && temp[2].toString().equals("1"))
				{
					for(int j=0; j <= timeArray.length - 1; j++)
					{
						Object[] cloneTemp = temp.clone();
						cloneTemp[5] = itemDispArray[j];
						cloneTemp[7] = timeArray[j];
						thinkTopicList.add(cloneTemp);
					}
				}
				
			}
			for(int i = 2; i <= thinkTopicList.size() - 1; i++)
			{
				Object[] temp = (Object[])thinkTopicList.get(i);
				if(i != 2)
				{
					Object[] preTemp = (Object[])thinkTopicList.get(i - 1);
					if(temp[0] != null && preTemp[0] != null && (temp[0].toString().equals(preTemp[0].toString())))
						temp[1] = "";
				}
			}
			return thinkTopicList;
		}
		if (reportId != null && reportId.equals("2")) {
			for (int i = 2; i <= dataList.size() - 1; i++) {
				Object[] temp = (Object[]) dataList.get(i);
				if(temp[7] != null)
				{
					int index = Arrays.binarySearch(timeArray, temp[7].toString());
					if(index > -1)
						temp[5] = itemDispArray[index];
				}
				if (i != 2) {
					Object[] preTemp = (Object[]) dataList.get(i - 1);
					if (temp[0] != null
							&& preTemp[0] != null
							&& (temp[0].toString()
									.equals(preTemp[0].toString())))
						temp[1] = "";
				}
			}
		}
		return dataList;
	}

	public int judgeAddOrUpdate(String reportId,String yearMonth) {
		String sqlValueCount = 
			"select count(*) from BP_J_CBM_REPORT a\n" +
			" where a.re_id is not null \n";
		if(reportId != null && !reportId.equals(""))
			sqlValueCount +=" and a.report_id="+reportId+" \n";
		if(yearMonth != null && !yearMonth.equals(""))
			sqlValueCount +=" and a.year_month='"+yearMonth+"' ";
		int valueCount = Integer.parseInt(bll.getSingal(sqlValueCount).toString());
		return valueCount;
	}

	public void clearReportItemInput(String reportId, String yearMonth,String enterpriseCode) {
		String sql = "delete from BP_J_CBM_REPORT a\n" +
			" where a.report_id ='"+reportId+"' \n";
		if(reportId.equals("2"))
			sql += "   and a.year_month like '"+yearMonth+"%'\n";
		else
			sql +="   and a.year_month = '"+yearMonth+"'\n"; 
		sql += "   and a.enterprise_code = '"+enterpriseCode+"'";
		bll.exeNativeSQL(sql);
	}

	public void saveReportItemInput(List<BpJCbmReport> modList, String method) {
		if(method != null)
		{
			if (method.equals("add")) {
				Long tabelId = bll.getMaxId("BP_J_CBM_REPORT", "RE_ID");
				for (BpJCbmReport entity : modList) {
					entity.setReId(tabelId++);
					entityManager.persist(entity);
				}
			} else {
				for (BpJCbmReport entity : modList) {
					String sql = "update BP_J_CBM_REPORT a\n"
							+ "   set a.value = '" + entity.getValue()
							+ "', a.enterprise_code = '"
							+ entity.getEnterpriseCode() + "'\n"
							+ " where a.item_id = '" + entity.getItemId()
							+ "'\n" + "   and a.block_id = '"
							+ entity.getBlockId() + "'\n"
							+ "   and a.report_id = '" + entity.getReportId()
							+ "'\n" + "   and a.year_month = '"
							+ entity.getYearMonth() + "'";
					bll.exeNativeSQL(sql);
				}
			}
		}
		
	}
	
	
}