package power.web.comm;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.commodel.SysCParametersFacadeRemote;
import power.ejb.webservice.run.ticketmanage.RunJTaskInformation;
import power.ejb.webservice.run.ticketmanage.RunJTaskInformationFacadeRemote;
import service.ProductServiceManager;
import service.ProductServiceManagerImpl;

class SampleTask extends TimerTask {

	private ServletContext context;
	private RunJTaskInformationFacadeRemote remote;
	private ProductServiceManager productManager;
	private SysCParametersFacadeRemote parameterRemote; 

	public SampleTask(ServletContext context) {
		this.context = context;
		this.remote = (RunJTaskInformationFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("RunJTaskInformationFacade");
		this.parameterRemote =(SysCParametersFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("SysCParametersFacade");
	}

	public void run() {
		Object pamValue = parameterRemote.findBypamNo("ISAUTO", "hfdc");
		if("Y".equals(pamValue.toString())) {
			String wsAddr = context.getInitParameter("productWebServiceAddr");
			List<RunJTaskInformation> list = remote.findListForResend();
			Iterator<RunJTaskInformation> it = list.iterator();
			while (it.hasNext()) {
				RunJTaskInformation bean = it.next();
				if(bean.getTaskNo() == null || bean.getTickeNo()==null)
					continue;
				System.out.println("【定时执行】同步到任务系统:"+bean.getTickeNo());
				bean.setExecteResult(1l);
				productManager = new ProductServiceManagerImpl();
				if ("CreateTicket".equals(bean.getMethodName())) {
					productManager.synchronizeCreateTicket(wsAddr, bean);
//					productManager.CreateTicket(wsAddr, bean.getEnterprisecode(),
//							bean.getOperator(), bean.getTaskNo(),
//							bean.getTickeNo(), bean.getIsStandardticket(), bean
//									.getIsWorkticket(), bean.getTickettypeId(),
//							bean.getTickettypeName(), bean.getWatchtypeId(), bean
//									.getWatchtypeName(), bean.getMachinetypeId(),
//							bean.getMachinetypeName());
				} else if ("DisuseTicket".equals(bean.getMethodName())) {
					productManager.synchronizeDisuseTicket(wsAddr, bean);
//					SimpleDateFormat sdf = new SimpleDateFormat(
//							"yyyy-MM-dd HH:mm:ss");
//					if(bean.getDisuseTime() == null) {
//						productManager.DisuseTicket(wsAddr, bean.getEnterprisecode(),
//								bean.getOperator(), bean.getTaskNo(), sdf.format(new Date()));
//					} else {
//						productManager.DisuseTicket(wsAddr, bean.getEnterprisecode(),
//								bean.getOperator(), bean.getTaskNo(), sdf.format(bean.getDisuseTime()));
//					}
				} else if ("BeforeDestroyTask".equals(bean.getMethodName())) {
					productManager.synchronizeBeforeDestroyTask(wsAddr, bean);
//					productManager.BeforeDestroyTask(wsAddr, bean
//							.getEnterprisecode(), bean.getOperator(), bean
//							.getTaskNo(), bean.getIsFlag(), bean.getCauses());
				}
				remote.update(bean);
			}
		} 
	}
}
