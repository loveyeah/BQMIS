package power.web.system.action;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.message.SysJMessageEmpFacadeRemote;
import power.web.comm.AbstractAction;

	public class ContentAction extends AbstractAction{
		private SysJMessageEmpFacadeRemote remote;
		/*
		 * 获取我的事务查看链接串
		 */
	    public void getMyJob(){
	        String noViewUrl = "";
	        StringBuffer sb = new StringBuffer();
	        remote = (SysJMessageEmpFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("SysJMessageEmpFacade");
	        Long noViewCount = remote.getNoVeiwMessageCount(employee.getWorkerCode());
	        if(noViewCount != null&&!noViewCount.equals(0l)){
	        	noViewUrl = "message/bussiness/receivemsg/msgreceive.jsp";
	        	sb.append( "<a target=\"_blank\"   href=\""+noViewUrl+"\">"
	        			+ noViewCount + "条" + "消息未查看</a><br/><br/>");
	        }
	        write(sb.toString());
	    }
}

