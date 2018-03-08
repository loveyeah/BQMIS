package power.web.system.action;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.hr.ca.HrJAttendanceApproveFacadeRemote;
import power.ejb.hr.reward.HrJRewardApprove;
import power.ejb.hr.reward.HrJRewardApproveFacadeRemote;
import power.ejb.item.ItemJDataFacadeRemote;
import power.ejb.manage.project.PrjJApplyFacadeRemote;
import power.ejb.manage.project.PrjJCheckFacadeRemote;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegisterFacadeRemote;
import power.web.comm.AbstractAction;

import com.opensymphony.engineassistant.po.MyJobMessage;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;
public class ContentAction extends AbstractAction {
	ItemJDataFacadeRemote itemBll;
	public void getTotalEleCol(){
		String str = 
			" <?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
			"<chart>\n" + 
			"<series>\n" + 
			" <value xid=\"101\">淮 北</value>\n" + 
			" <value xid=\"102\">淮北有限</value>\n" + 
			" <value xid=\"103\">洛 河</value>\n" + 
			" <value xid=\"104\">洛 能</value>\n" + 
			" <value xid=\"105\">陈 村</value>\n" + 
			" <value xid=\"106\">田 厂</value>\n" + 
			" <value xid=\"107\">分 公 司</value>\n" + 
			" </series>\n" + 
			"<graphs>\n" + 
			"<graph gid=\"1\" title=\"当前累计\">\n" + 
			" <value xid=\"101\">23.0</value>\n" + 
			" <value xid=\"102\">10.65</value>\n" + 
			" <value xid=\"103\">31.39</value>\n" + 
			" <value xid=\"104\">82.81</value>\n" + 
			" <value xid=\"105\">1.86</value>\n" + 
			" <value xid=\"106\">31.88</value>\n" + 
			" <value xid=\"107\">181.59</value>\n" + 
			" </graph>\n" + 
			"<graph gid=\"2\" title=\"当前计划\">\n" + 
			" <value xid=\"101\">23.3</value>\n" + 
			" <value xid=\"102\">10.39</value>\n" + 
			" <value xid=\"103\">31.78</value>\n" + 
			" <value xid=\"104\">79.8</value>\n" + 
			" <value xid=\"105\">2.67</value>\n" + 
			" <value xid=\"106\">29.7</value>\n" + 
			" <value xid=\"107\">177.64</value>\n" + 
			" </graph>\n" + 
			"<graph gid=\"3\" title=\"全年计划\">\n" + 
			" <value xid=\"101\">28.7</value>\n" + 
			" <value xid=\"102\">13.44</value>\n" + 
			" <value xid=\"103\">77.07</value>\n" + 
			" <value xid=\"104\">63.60</value>\n" + 
			" <value xid=\"105\">0</value>\n" + 
			" <value xid=\"106\">37.2</value>\n" + 
			" <value xid=\"107\">224.16</value>\n" + 
			" </graph>\n" + 
			" </graphs>\n" + 
			" </chart>";
		super.writeXml(str);
		 
	}
	
	public void getTotalElePie(){
		String str = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
			"  <pie>\n" + 
			"  <slice title=\"田 厂\">31.88</slice>\n" + 
			"  <slice title=\"陈 村\">1.86</slice>\n" + 
			"  <slice title=\"洛 能\">82.81</slice>\n" + 
			"  <slice title=\"洛 河\">31.39</slice>\n" + 
			"  <slice title=\"淮北有限\">10.65</slice>\n" + 
			"  <slice title=\"淮 北\">23.0</slice>\n" + 
			"  </pie>";
	   super.writeXml(str);
	}
	
	public void getMonthEleLine(){
		String str = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
			" <chart>\n" + 
			" <series>\n" + 
			"  <value xid=\"101\">01</value>\n" + 
			"  <value xid=\"102\">02</value>\n" + 
			"  <value xid=\"103\">03</value>\n" + 
			"  <value xid=\"104\">04</value>\n" + 
			"  <value xid=\"105\">05</value>\n" + 
			"  <value xid=\"106\">06</value>\n" + 
			"  <value xid=\"107\">07</value>\n" + 
			"  <value xid=\"108\">08</value>\n" + 
			"  <value xid=\"109\">09</value>\n" + 
			"  <value xid=\"110\">10</value>\n" + 
			"  <value xid=\"111\">11</value>\n" + 
			"  <value xid=\"112\">12</value>\n" + 
			"  </series>\n" + 
			" <graphs>\n" + 
			" <graph gid=\"1\" title=\"当年\">\n" + 
			"  <value xid=\"101\" color=\"#6699FF\" description=\"当年\">207839.15</value>\n" + 
			"  <value xid=\"102\" color=\"#6699FF\" description=\"当年\">169726.55</value>\n" + 
			"  <value xid=\"103\" color=\"#6699FF\" description=\"当年\">194301.88</value>\n" + 
			"  <value xid=\"104\" color=\"#6699FF\" description=\"当年\">168368.9</value>\n" + 
			"  <value xid=\"105\" color=\"#6699FF\" description=\"当年\">180342.12</value>\n" + 
			"  <value xid=\"106\" color=\"#6699FF\" description=\"当年\">208180.51</value>\n" + 
			"  <value xid=\"107\" color=\"#6699FF\" description=\"当年\">232274.19</value>\n" + 
			"  <value xid=\"108\" color=\"#6699FF\" description=\"当年\">236845.1</value>\n" + 
			"  <value xid=\"109\" color=\"#6699FF\" description=\"当年\">218120.98</value>\n" + 
			"  <value xid=\"110\" color=\"#6699FF\" description=\"当年\">187439.61</value>\n" + 
			"  <value xid=\"111\" color=\"#6699FF\" description=\"当年\" />\n" + 
			"  <value xid=\"112\" color=\"#6699FF\" description=\"当年\" >218120.98</value>\n" + 
			"  </graph>\n" + 
			" <graph gid=\"2\" title=\"同期\">\n" + 
			"  <value xid=\"101\" color=\"#6699FF\" description=\"同期\">170118.19</value>\n" + 
			"  <value xid=\"102\" color=\"#6699FF\" description=\"同期\">117815.68</value>\n" + 
			"  <value xid=\"103\" color=\"#6699FF\" description=\"同期\">145917.67</value>\n" + 
			"  <value xid=\"104\" color=\"#6699FF\" description=\"同期\">141423.5</value>\n" + 
			"  <value xid=\"105\" color=\"#6699FF\" description=\"同期\">144828.74</value>\n" + 
			"  <value xid=\"106\" color=\"#6699FF\" description=\"同期\">151918.97</value>\n" + 
			"  <value xid=\"107\" color=\"#6699FF\" description=\"同期\">181671.57</value>\n" + 
			"  <value xid=\"108\" color=\"#6699FF\" description=\"同期\">185945.56</value>\n" + 
			"  <value xid=\"109\" color=\"#6699FF\" description=\"同期\">150337.29</value>\n" + 
			"  <value xid=\"110\" color=\"#6699FF\" description=\"同期\">150854.46</value>\n" + 
			"  <value xid=\"111\" color=\"#6699FF\" description=\"同期\">135581.42</value>\n" + 
			"  <value xid=\"112\" color=\"#6699FF\" description=\"同期\">158717.13</value>\n" + 
			"  </graph>\n" + 
			"  </graphs>\n" + 
			"  </chart>";
		super.writeXml(str);
	}
	
	public void getMyAffair() throws Exception
	{
		
	}
	
	public void getElectricLine() throws Exception
	{
		itemBll = (ItemJDataFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ItemJDataFacade");
		List list = itemBll.GetElectricList("hfdc", "0");
		List listPre = itemBll.GetPreElectricList("hfdc", "0");
		String slice = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
						" <chart>\n" + 
						" <series>\n" + 
						"  <value xid=\"101\">01</value>\n" + 
						"  <value xid=\"102\">02</value>\n" + 
						"  <value xid=\"103\">03</value>\n" + 
						"  <value xid=\"104\">04</value>\n" + 
						"  <value xid=\"105\">05</value>\n" + 
						"  <value xid=\"106\">06</value>\n" + 
						"  <value xid=\"107\">07</value>\n" + 
						"  <value xid=\"108\">08</value>\n" + 
						"  <value xid=\"109\">09</value>\n" + 
						"  <value xid=\"110\">10</value>\n" + 
						"  <value xid=\"111\">11</value>\n" + 
						"  <value xid=\"112\">12</value>\n" + 
						"  </series>\n" +
						"<graphs>\n";
						
		String value;
		String month;
		if(list != null && list.size()>0)
		{
			slice += " <graph gid=\"1\" title=\"当年\">\n";
			for(int i=0;i < list.size();i ++)
			{
				Object[] item = (Object[])list.get(i);
				value = (item[1]==null?"0":item[1].toString());
				month = (item[0].toString().substring(5, 7));
				slice += "<value xid=\"1" + month + "\" color=\"#6699FF\" description=\"当年\">";
				slice += value;
				slice += "</value>";
			}
			slice += "</graph>\n";
		}
		if(listPre != null && listPre.size()>0)
		{
			slice += " <graph gid=\"2\" title=\"同期\">\n";
			for(int i=0;i < listPre.size();i ++)
			{
				Object[] item = (Object[])listPre.get(i);
				value = (item[1]==null?"0":item[1].toString());
				month = (item[0].toString().substring(5, 7));
				slice += "<value xid=\"1" + month + "\" color=\"#6699FF\" description=\"同期\">";
				slice += value;
				slice += "</value>";
			}
			slice += "</graph>\n";
		}
		slice += "  </graphs></chart>";
		
	   super.writeXml(slice);
	}
	public void test() throws Exception
	{
		itemBll = (ItemJDataFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ItemJDataFacade");
		List list = itemBll.GetElectricList("hfdc", "0");
		List listPre = itemBll.GetPreElectricList("hfdc", "0");
		String slice = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
						" <chart>\n" + 
						" <series>\n" + 
						"  <value xid=\"101\">01</value>\n" + 
						"  <value xid=\"102\">02</value>\n" + 
						"  <value xid=\"103\">03</value>\n" + 
						"  <value xid=\"104\">04</value>\n" + 
						"  <value xid=\"105\">05</value>\n" + 
						"  <value xid=\"106\">06</value>\n" + 
						"  <value xid=\"107\">07</value>\n" + 
						"  <value xid=\"108\">08</value>\n" + 
						"  <value xid=\"109\">09</value>\n" + 
						"  <value xid=\"110\">10</value>\n" + 
						"  <value xid=\"111\">11</value>\n" + 
						"  <value xid=\"112\">12</value>\n" + 
						"  </series>\n" +
						"<graphs>\n";
						
		String value;
		String month;
		if(list != null && list.size()>0)
		{
			slice += " <graph gid=\"1\" title=\"当年\">\n";
			for(int i=0;i < list.size();i ++)
			{
				Object[] item = (Object[])list.get(i);
				value = (item[1]==null?"0":item[1].toString());
				month = (item[0].toString().substring(5, 6));
				slice += "<value xid=\"1" + month + "\" color=\"#6699FF\" description=\"当年\">";
				slice += value;
				slice += "</value>";
			}
			slice += "</graph>\n";
		}
		if(listPre != null && listPre.size()>0)
		{
			slice += " <graph gid=\"2\" title=\"同期\">\n";
			for(int i=0;i < listPre.size();i ++)
			{
				Object[] item = (Object[])listPre.get(i);
				value = (item[1]==null?"0":item[1].toString());
				month = (item[0].toString().substring(5, 6));
				slice += "<value xid=\"1" + month + "\" color=\"#6699FF\" description=\"同期\">";
				slice += value;
				slice += "</value>";
			}
			slice += "</graph>\n";
		}
		slice += "  </graphs></chart>";
		
	   super.writeXml(slice);
	}
	public void getElectricPieOnUnit() throws Exception
	{
		//modify by fyyang 20100920
		itemBll = (ItemJDataFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ItemJDataFacade");
		List list = itemBll.GetElectricListOnUnit("", "1");
		String strPie = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
					"  <pie>\n";
		String unit;
		String value;
		if(list != null && list.size() > 0)
		{
			for(int i=0;i < list.size();i ++)
			{
				Object[] item = (Object[])list.get(i);
				unit = item[0].toString();
				//System.out.println(unit);
				value = item[1].toString();
				strPie += "<slice title=\"";
//				if( "1#机组".equals(unit))
//				{
//					 unit = "#11机组";
//				}
//				if("2#机组".equals(unit))
//				{
//					 unit = "#12机组";
//				}
//				if("3#机组".equals(unit))
//				{
//					 unit = "#1机组";
//				}
//				if("4#机组".equals(unit))
//				{
//					 unit = "#2机组";
//				}
				//System.out.println(unit);
				strPie += unit;
				strPie += "\">" + value + "</slice>\n"; 
			}
			
			strPie += "</pie>";
		}
		super.writeXml(strPie);
	}
	public void getMyJob() {
		StringBuffer sb = new StringBuffer();
		WorkflowService workflowService = new WorkflowServiceImpl();
		List<MyJobMessage> messages = workflowService.getMyJobMessage(employee.getWorkerCode());
		// 加入大奖月奖通知
		HrJRewardApproveFacadeRemote approveFacade = (HrJRewardApproveFacadeRemote)factory.getFacadeRemote("HrJRewardApproveFacade");
		List<HrJRewardApprove> list = approveFacade.findAll(employee.getDeptId(), employee.getWorkerId());
		//考勤事务提示
		HrJAttendanceApproveFacadeRemote attendance=(HrJAttendanceApproveFacadeRemote)factory.getFacadeRemote("HrJAttendanceApproveFacade");
		String attendMsg=attendance.getMsgList(employee.getWorkerCode());
		// 重大危险源事务提示
		SpJDangerDeptRegisterFacadeRemote dangerFacade = (SpJDangerDeptRegisterFacadeRemote) factory.getFacadeRemote("SpJDangerDeptRegisterFacade");
		List workList = dangerFacade.getApproveWork(employee.getWorkerId(), employee.getDeptId(), employee.getWorkerCode());
		
		//工程项目竣工验收单事务提醒
		PrjJCheckFacadeRemote prjCheckFacade = (PrjJCheckFacadeRemote)factory.getFacadeRemote("PrjJCheckFacade");		
		String prjCheckMsg = prjCheckFacade.getCheckList(employee.getWorkerCode(), employee.getDeptId());
		
		//工程项目开工申请单事务提醒
		PrjJApplyFacadeRemote prjApplyFacade  = (PrjJApplyFacadeRemote)factory.getFacadeRemote("PrjJApplyFacade");
		String prjApplyMsg = prjApplyFacade.getApplyList(employee.getWorkerCode(), employee.getDeptId());
		
		if (messages != null && messages.size() > 0) {
			sb.append("<marquee onmouseover=stop() onmouseout=start()  direction=up scrollamount=2><" + employee.getWorkerName() + ">您当前有:<br/>");
			for (MyJobMessage o : messages) {
				sb.append("<a target=\"_blank\"  href=\"" + o.getFlowListUrl() + "\">"
						+ o.getCount() + "张" + o.getFlowName()
//						+ "("
//						+ o.getFlowType() 
						+ "等待审批</a><br/><br/>");
			}
			
			//工程项目竣工验收单提醒
			if(!prjCheckMsg.equals(""))
			{
				sb.append(prjCheckMsg);
			}
			//工程项目开工申请单提醒
			if(!prjApplyMsg.equals(""))
			{
				sb.append(prjApplyMsg);
			}
			
			// 加入大奖月奖通知
			if (list != null && list.size() > 0) {
				for (HrJRewardApprove o : list) {
					sb.append("<a target=\"_blank\"  href=\"" + o.getFlowListUrl() + "\">"
							+ "1张" + o.getContent()
	//						+ "("
	//						+ o.getFlowType() 
							+ "</a><br/><br/>");
				}
			}
			//加入考勤事务提示
			if(!attendMsg.equals(""))
			{
				sb.append(attendMsg);
			}
			//加入重大危险源事务提示
			if(workList != null && workList.size() >0) {
				Iterator iterator = workList.iterator();
				while(iterator.hasNext()) {
					String count="";
					String fileUrl="";
					Object[] data = (Object[])iterator.next();
					if(data[1] != null) {
						count = data[1].toString();
					}
					if(data[2] != null) {
						fileUrl = data[2].toString();
					}
					sb.append("<a target=\"_blank\"  href=\"" + fileUrl + "\">"
							+count +"张" + "重大危险源等待审批"
	//						+ "("
	//						+ o.getFlowType() 
							+ "</a><br/><br/>");
				}
			}
		
			sb.append("</marquee>");
		}
		if (messages == null || messages.size() <= 0) {
			if (list != null && list.size() > 0) {
				sb.append("<marquee onmouseover=stop() onmouseout=start()  direction=up scrollamount=2><" + employee.getWorkerName() + ">您当前有:<br/>");
				for (HrJRewardApprove o : list) {
					sb.append("<a target=\"_blank\"  href=\"" + o.getFlowListUrl() + "\">"
							+ "1张" + o.getContent()
	//						+ "("
	//						+ o.getFlowType() 
							+ "</a><br/><br/>");
				}

				//工程项目竣工验收单提醒
				if(!prjCheckMsg.equals(""))
				{
					sb.append(prjCheckMsg);
				}
				
				//工程项目开工申请单提醒
				if(!prjApplyMsg.equals(""))
				{
					sb.append(prjApplyMsg);
				}
				//加入考勤事务提示
				if(!attendMsg.equals(""))
				{
					sb.append(attendMsg);
				}
				//加入重大危险源事务提示
				if(workList != null && workList.size() >0) {
					Iterator iterator = workList.iterator();
					while(iterator.hasNext()) {
						String count="";
						String fileUrl="";
						Object[] data = (Object[])iterator.next();
						if(data[1] != null) {
							count = data[1].toString();
						}
						if(data[2] != null) {
							fileUrl = data[2].toString();
						}
						sb.append("<a target=\"_blank\"  href=\"" + fileUrl + "\">"
								+count +"张" + "重大危险源等待审批"
		//						+ "("
		//						+ o.getFlowType() 
								+ "</a><br/><br/>");
					}
				}
				
				sb.append("</marquee>");
			}
		}
		write(sb.toString());
	}
	
	/**
	 * 得到发电量年度分析柱状图数据
	 * 
	 * @throws Exception
	 */
	public void getElectricColumnOnUnit() throws Exception
	{
		itemBll = (ItemJDataFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ItemJDataFacade");
		List list = itemBll.GetElectricListOnUnit("", "2");
		List listUnit = itemBll.GetElectricUnit("", "");
		String strCol = " <?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
				"<chart>\n" + 
				"<series>\n";
		String unit;
		String value;
		if(list != null && list.size() > 0)
		{
			for(int i = 0;i < list.size();i ++)
			{
				Object[] item = (Object[])list.get(i);
				unit = item[0].toString();

				if(i < 10)
				{
					strCol += "<value xid=\"10" + Integer.toString(i) + "\">" + unit + "</value>\n";
				}
				else
				{
					strCol += "<value xid=\"1" + Integer.toString(i) + "\">" + unit + "</value>\n";
				}
			}
		}
		strCol += " </series>\n" + 
					"<graphs>\n" +
					"<graph gid=\"1\" title=\"当年累计\">\n";
		if(list != null && list.size() > 0)
		{
			for(int i=0;i < list.size();i ++)
			{
				Object[] item = (Object[])list.get(i);
				value = item[1].toString();
				if(i < 10)
				{
					strCol += "<value xid=\"10" + Integer.toString(i) +"\">" + value + "</value>\n";
				}
				else
				{
					strCol += "<value xid=\"1" + Integer.toString(i) +"\">" + value + "</value>\n";
				}
			}
		}
		strCol += "</graph>\n";
		strCol += "<graph gid=\"2\" title=\"当月累计\">\n";
		if(listUnit != null && listUnit.size() > 0)
		{
			for(int i=0;i < listUnit.size();i ++)
			{
				Object[] item = (Object[])listUnit.get(i);
				value = item[1].toString();
				if(i < 10)
				{
					strCol += "<value xid=\"10" + Integer.toString(i) +"\">" + value + "</value>\n";
				}
				else
				{
					strCol += "<value xid=\"1" + Integer.toString(i) +"\">" + value + "</value>\n";
				}
			}
		}
		strCol += "</graph>\n";
//		strCol += "<graph gid=\"3\" title=\"年度计划\">\n";
//		if(list != null && list.size() > 0)
//		{
//			for(int i = 0;i < list.size(); i ++)
//			{
//				if(i < 10)
//				{
//					strCol += "<value xid=\"10" + Integer.toString(i) +"\">" + "1500" + "</value>\n";
//				}
//				else
//				{
//					strCol += "<value xid=\"1" + Integer.toString(i) +"\">" + "1500" + "</value>\n";
//				}
//			}
//		}
//		strCol += "</graph>\n";
		strCol += "</graphs>\n";
		strCol += "</chart>";
		super.writeXml(strCol);
	}
}
