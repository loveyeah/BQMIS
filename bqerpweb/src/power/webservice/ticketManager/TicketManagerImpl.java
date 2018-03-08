package power.webservice.ticketManager;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.webservice.run.ticketmanage.WsJTask;
import power.ejb.webservice.run.ticketmanage.WsJTaskFacadeRemote;

public class TicketManagerImpl implements TicketManager{
	
	private static WsJTaskFacadeRemote taskFacadeRemote ;
	
	static {
		taskFacadeRemote = (WsJTaskFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("WsJTaskFacade");
	}
	
	public int createTask(int taskType, String taskNo, String taskName,
			String receiver) throws CodeRepeatException {
		try {
			WsJTask taskEntity = new WsJTask();
			Integer taskType1 = taskType;
			Long type1 = taskType1.longValue();
			taskEntity.setTaskType(type1);
			taskEntity.setReceiver(receiver);
			taskEntity.setTaskName(taskName);
			taskEntity.setTaskNo(taskNo);
			int result = taskFacadeRemote.save(taskEntity);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int sendBlankOutTaskNo(String taskNo) throws CodeRepeatException {
		try {
//			WsJTaskFacadeRemote taskFacadeRemote = (WsJTaskFacadeRemote) Ejb3Factory.getInstance()
//			.getFacadeRemote("WsJTaskFacade");
			int result = taskFacadeRemote.delete(taskNo);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
