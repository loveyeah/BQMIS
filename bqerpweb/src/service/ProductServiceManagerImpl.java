package service;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.codehaus.xfire.client.Client;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.webservice.run.ticketmanage.RunJTaskInformation;
import power.ejb.webservice.run.ticketmanage.RunJTaskInformationFacadeRemote;

public class ProductServiceManagerImpl implements ProductServiceManager {
	
	private static RunJTaskInformationFacadeRemote taskFacadeRemote ;
	
	static {
		taskFacadeRemote = (RunJTaskInformationFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("RunJTaskInformationFacade");
	}
	
	public void synchronizeCreateTicket(String wsAddr,RunJTaskInformation bean)
	{ 
		Integer result = -1;
	    try {
			Client client = new Client(new URL(wsAddr));
			String IsCreatebyStand;
			if("Y".equals(bean.getIsStandardticket())) {
				IsCreatebyStand = "1";
			} else {
				IsCreatebyStand = "0";
			}
			Object[] results = client.invoke("CreateTicket",
					new Object[] { bean.getTaskNo(), bean.getTickeNo(), IsCreatebyStand,
					bean.getIsWorkticket(), bean.getTickettypeId(),bean.getTickettypeName(),bean.getWatchtypeId(),
							bean.getWatchtypeName(),bean.getMachinetypeId(),bean.getMachinetypeName()});
			result = Integer.parseInt(results[0].toString());
			bean.setExecteResult(result.longValue());
			bean.setOperator("000000");//系统自动产生
			taskFacadeRemote.update(bean);
		} catch (Exception exc) {
			System.out.println("【定时执行】上报时修改系统中所对应的任务信息出错" + exc.getMessage());
		} 
	}
	
	public void CreateTicket(String wsAddr, String enterpriseCode,
			String operator, String taskNo, String tickeNo,
			String isStandardticket, String isWorkticket, String ticketTypeID,
			String ticketTypeName, String watchTypeID, String watchTypeName,
			String machineTypeID, String machineTypeName) {
		Integer result = -1;
		
		RunJTaskInformation taskEntity = new RunJTaskInformation();
		taskEntity.setEnterprisecode(enterpriseCode);
		taskEntity.setOperator(operator);
		taskEntity.setTaskNo(taskNo);
		taskEntity.setTickeNo(tickeNo);
		taskEntity.setIsStandardticket(isStandardticket);
		taskEntity.setIsWorkticket(isWorkticket);
		taskEntity.setTickettypeId(ticketTypeID);
		taskEntity.setTickettypeName(ticketTypeName);
		taskEntity.setWatchtypeId(watchTypeID);
		taskEntity.setWatchtypeName(watchTypeName);
		taskEntity.setMachinetypeId(machineTypeID);
		taskEntity.setMachinetypeName(machineTypeName);
		taskEntity.setMethodName("CreateTicket");
		String IsCreatebyStand;
		if("Y".equals(isStandardticket)) {
			IsCreatebyStand = "1";
		} else {
			IsCreatebyStand = "0";
		}
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("CreateTicket",
					new Object[] { taskNo, tickeNo, IsCreatebyStand,
					isWorkticket, ticketTypeID, ticketTypeName, watchTypeID,
							watchTypeName, machineTypeID, machineTypeName });
			result = (Integer)results[0];
			taskEntity.setExecteResult(result.longValue());
		} catch (Exception exc) {
			System.out.println("开票或重开票时修改系统中所对应的任务信息出错" + exc.getMessage());
		} finally {
			taskFacadeRemote.save(taskEntity);
		}
	}
	public void synchronizeBeforeDestroyTask(String wsAddr,RunJTaskInformation bean)
	{
		Integer result = -1;
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("BeforeDestroyTask",
					new Object[] { bean.getTaskNo(),bean.getIsFlag(),bean.getCauses()});
			result = (Integer)results[0];
			bean.setExecteResult(result.longValue());
			bean.setOperator("000000");//系统自动产生
			taskFacadeRemote.update(bean);
		} catch (Exception exc) {
			System.out.println("【定时执行】“未执行”时，修改系统中所对应的任务信息出错" + exc.getMessage());
		}
	}
	public void BeforeDestroyTask(String wsAddr, String enterpriseCode,
			String operator, String taskNo, String flag, String causes) {
		Integer result = -1; 
		RunJTaskInformation taskEntity = new RunJTaskInformation();
		taskEntity.setEnterprisecode(enterpriseCode);
		taskEntity.setOperator(operator);
		taskEntity.setTaskNo(taskNo);
		taskEntity.setIsFlag(flag);
		taskEntity.setCauses(causes);
		taskEntity.setMethodName("BeforeDestroyTask"); 
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("BeforeDestroyTask",
					new Object[] { taskNo, flag, causes });
			result = (Integer)results[0];
			taskEntity.setExecteResult(result.longValue());
		} catch (Exception exc) {
			System.out.println("两票系统中登记票“未执行”时，根据条件修改任务单状态为:申请作废状态/作废状态（未执行）出错" + exc.getMessage());
		} finally {
			taskFacadeRemote.save(taskEntity);
		}
	}

	public void DestroyTicket(String wsAddr, String enterpriseCode,
			String operator, String taskNo) {
		Integer result = -1;

		RunJTaskInformation taskEntity = new RunJTaskInformation();
		taskEntity.setEnterprisecode(enterpriseCode);
		taskEntity.setOperator(operator);
		taskEntity.setTaskNo(taskNo);
		taskEntity.setMethodName("DestroyTicket");
		
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("DestroyTicket",
					new Object[] { taskNo });
			result = (Integer)results[0];
			taskEntity.setExecteResult(result.longValue());
		} catch (Exception exc) {
			System.out.println("两票系统废止一张票,清空原有两票传入的信息出错" + exc.getMessage());
		} finally {
			taskFacadeRemote.save(taskEntity);
		}

	}
	public void synchronizeDisuseTicket(String wsAddr,RunJTaskInformation bean)
	{
		Integer result = -1;
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("DisuseTicket",
					new Object[] { bean.getTaskNo(), bean.getDisuseTime()});
			result = (Integer)results[0];
			bean.setExecteResult(result.longValue());
			bean.setOperator("000000");//系统自动产生
			taskFacadeRemote.update(bean);
		} catch (Exception exc) {
			System.out.println("【定时执行】“终结”时，修改系统中所对应的任务信息出错" + exc.getMessage());
		}
	}
	public void DisuseTicket(String wsAddr, String enterpriseCode,
			String operator, String taskNo, String disuseTime) {
		Integer result = -1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String disuseDate = null;
		try {
			disuseDate = sdf.format(sdf.parse(disuseTime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		RunJTaskInformation taskEntity = new RunJTaskInformation();
		taskEntity.setEnterprisecode(enterpriseCode);
		taskEntity.setOperator(operator);
		taskEntity.setTaskNo(taskNo);
		try {
			java.util.Date date = sdf.parse(disuseTime);
			taskEntity.setDisuseTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskEntity.setMethodName("DisuseTicket");
		
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("DisuseTicket",
					new Object[] { taskNo, disuseDate });
			result = (Integer)results[0];
			taskEntity.setExecteResult(result.longValue());
		} catch (Exception exc) {
			System.out.println("两票系统终结一张票出错" + exc.getMessage());
		} finally {
			taskFacadeRemote.save(taskEntity);
		}

	}

	public void DisuseTicketXT(String wsAddr, String enterpriseCode,
			String operator, String taskNo, String disuseTime, String beginTime) {
		Integer result = -1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		RunJTaskInformation taskEntity = new RunJTaskInformation();
		taskEntity.setEnterprisecode(enterpriseCode);
		taskEntity.setOperator(operator);
		taskEntity.setTaskNo(taskNo);
		try {
			java.util.Date disuseDate = sdf.parse(disuseTime);
			java.util.Date beginDate = sdf.parse(beginTime);
			taskEntity.setDisuseTime(disuseDate);
			taskEntity.setBeginTime(beginDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskEntity.setMethodName("DisuseTicketXT");
		
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("DisuseTicketXT",
					new Object[] { taskNo, disuseTime, beginTime });
			result = (Integer)results[0];
			taskEntity.setExecteResult(result.longValue());
		} catch (Exception exc) {
			System.out.println("两票系统终结一张票出错" + exc.getMessage());
		} finally {
			taskFacadeRemote.save(taskEntity);
		}
	}

	public void ExtensionTicket(String wsAddr, String enterpriseCode,
			String operator, String taskNo, String finishEndTime) {
		Integer result = -1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		RunJTaskInformation taskEntity = new RunJTaskInformation();
		taskEntity.setEnterprisecode(enterpriseCode);
		taskEntity.setOperator(operator);
		taskEntity.setTaskNo(taskNo);
		try {
			java.util.Date finishEndDate = sdf.parse(finishEndTime);
			taskEntity.setFinishEndTime(finishEndDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskEntity.setMethodName("ExtensionTicket");
		
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("ExtensionTicket",
					new Object[] { taskNo, finishEndTime });
			result = (Integer)results[0];
			taskEntity.setExecteResult(result.longValue());
		} catch (Exception exc) {
			System.out.println("两票系统中工作票办理延期成功时修改计划完工时间出错" + exc.getMessage());
		} finally {
			taskFacadeRemote.save(taskEntity);
		}
	}

	public String GetBlankOutTaskInfo(String wsAddr, String enterpriseCode,
			String operator, String strYear, String strMonth) {
		String resultString = "";
		
		RunJTaskInformation taskEntity = new RunJTaskInformation();
		taskEntity.setEnterprisecode(enterpriseCode);
		taskEntity.setOperator(operator);
		taskEntity.setStryear(strYear);
		taskEntity.setStrmonth(strMonth);
		taskEntity.setMethodName("GetBlankOutTaskInfo");
		
		try {
			Client client = new Client(new URL(wsAddr));
			Object[] results = client.invoke("GetBlankOutTaskInfo",
					new Object[] { strYear, strMonth });
			resultString = results[0].toString();
			taskEntity.setExecteResult(1l);
		} catch (Exception exc) {
			taskEntity.setExecteResult(0l);
			System.out.println("根据下达时间得到已作废的任务单编号出错" + exc.getMessage());
		} finally {
			taskFacadeRemote.save(taskEntity);
		}
		return resultString;
	}

}
