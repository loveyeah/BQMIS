package power.web.hr.message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.message.HrJMessage;
import power.ejb.hr.message.HrJMessageFacadeRemote;
import power.ejb.hr.message.form.HrJMessageForm;
import power.ejb.manage.plan.BpJPlanJobCompleteMain;
import power.ejb.run.securityproduction.form.SpJAntiAccidentForm;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

@SuppressWarnings("serial")
public class HrJMessageAction extends AbstractAction {
	private HrJMessageFacadeRemote remote;
	private int start;
	private int limit;
	private HrJMessage message;

	

	public HrJMessageAction() {
		remote = (HrJMessageFacadeRemote) factory.getFacadeRemote("HrJMessageFacade");
	}

	public void saveMessage() throws Exception {
		
			message.setEnterpriseCode(employee.getEnterpriseCode());
			message.setIsSend("N");
			message.setEntryBy(employee.getWorkerCode());
			message.setEntryDate(new Date());
			remote.save(message);

			write("{success:true,msg:'增加成功！'}");
	}

	public void updateMessage() throws Exception {
	
		    String messageId = request.getParameter("messageId");
		
			HrJMessage entity = remote.findById(Long.parseLong(messageId));
			entity.setIsUse("Y");
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			entity.setIsSend("N");
			entity.setEntryBy(employee.getWorkerCode());
			entity.setEntryDate(new Date());
			
			entity.setUserCode(message.getUserCode());
			entity.setUserPsw(message.getUserPsw());
			if (message.getMsgPerson() != null && !"".equals(message.getMsgPerson())
					&& !"null".equals(message.getMsgPerson())) {
				entity.setMsgPerson(message.getMsgPerson());
			}
			entity.setMsgContent(message.getMsgContent());
			remote.update(entity);
			write("{success:true,msg:'修改成功！'}");
	
	}

	public void deleteMessage() {
		String ids = request.getParameter("ids");
		remote.delete(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void getHrJMessageList() throws JSONException {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
	
		PageObject pg = null;
		if (start != null && limit != null && !start.equals(""))
			pg = remote.getHrJMessageList(employee.getWorkerCode(), employee.getEnterpriseCode(),
					Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = remote.getHrJMessageList(employee.getWorkerCode(), employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));

	}
	

	public void sendMessage() {
		String ids = request.getParameter("ids");
		String sendPersons = request.getParameter("sendPersons");
		String sendContents = request.getParameter("sendContents");
	     String [] messageIds= ids.split(";");
	     String [] pensons= sendPersons.split(";");
	     String [] contents= sendContents.split(";");
			for(int i=0;i<messageIds.length;i++)
			{
				if(!messageIds[i].equals(""))
				{
				   String messageId=messageIds[i];
					remote.sendMessage(messageId);
				   
				   String sendPerson=pensons[i];
				   String sendContent=contents[i];
					PostMessage postMsg = new PostMessage();
					
			        if(sendPerson!=null&&!sendPerson.equals(""))
			        {
			        	postMsg.sendMsgByWorker(sendPerson, sendContent);
			        }
					write("{success:true,msg:'短信通知发送成功！'}");
				}
			}
	
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public HrJMessage getMessage() {
		return message;
	}

	public void setMessage(HrJMessage message) {
		this.message = message;
	}
}
