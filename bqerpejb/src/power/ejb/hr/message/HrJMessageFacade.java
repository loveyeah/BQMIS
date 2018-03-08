package power.ejb.hr.message;

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



import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.message.form.HrJMessageForm;
import power.ejb.manage.plan.BpJPlanJobDepMain;
import power.ejb.manage.plan.form.BpJPlanJobDepMainForm;

/**
 * Facade for entity HrJMessage.
 * 
 * @see power.ejb.hr.message.HrJMessage
 * @author sychen 
 */
@Stateless
public class HrJMessageFacade implements HrJMessageFacadeRemote {
	// property constants
	public static final String USER_CODE = "userCode";
	public static final String USER_PSW = "userPsw";
	public static final String MSG_PERSON = "msgPerson";
	public static final String MSG_CONTENT = "msgContent";
	public static final String IS_SEND = "isSend";
	public static final String ENTRY_BY = "entryBy";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	
	public void save(HrJMessage entity) {
		LogUtil.log("saving HrJMessage instance", Level.INFO, null);
		try {
			entity.setMessageId(bll.getMaxId("hr_j_message", "message_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}


	public void delete(String ids) {

		String sql = "update hr_j_message t\n" + "   set t.is_use = 'N'\n"
				+ " where t.message_id in (" + ids + ")";

		bll.exeNativeSQL(sql);

	}

	
	public HrJMessage update(HrJMessage entity) {
		LogUtil.log("updating HrJMessage instance", Level.INFO, null);
		try {
			HrJMessage result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJMessage findById(Long id) {
		LogUtil.log("finding HrJMessage instance with id: " + id, Level.INFO,
				null);
		try {
			HrJMessage instance = entityManager.find(HrJMessage.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

		@SuppressWarnings("unchecked")
		public  String getMsgName(String workCode)
	{
		String msgName="";
		String sql="select  getworkername('"+workCode+"')\n " +
				"   from   hr_j_emp_info a\n  " +
				"   where   a.emp_code ='"+workCode+"'";
		Object obj=bll.getSingal(sql);
		if(obj!=null)
		{
			msgName=obj.toString();
			return msgName;
			
		}
		return "";
		
	}
	@SuppressWarnings("unchecked")
	public PageObject getHrJMessageList(String workerCode, String enterpriseCode,
			 int... rowStartIdxAndCount) {
		String msgName="";
		String sql = "SELECT t.message_id,\n" +
		"       t.user_code,\n" + 
		"       t.user_psw,\n" + 
		"       t.msg_person,\n" + 
		"       t.msg_content,\n" + 
		"       t.is_send,\n" + 
		"       t.entry_by,\n" + 
		"       getworkername(t.entry_by) entryName,\n" + 
		"       to_char(t.entry_date, 'yyyy-mm-dd') entry_date\n" + 
		"  FROM HR_J_MESSAGE t\n" + 
		" WHERE t.is_use = 'Y'\n" + 
		"   AND t.entry_by = '"+workerCode+"'\n" + 
		"   AND t.enterprise_code = '"+enterpriseCode+"'\n" + 
		" ORDER BY t.message_id";

	String sqlCount = "SELECT COUNT(*)\n" +
		"  FROM HR_J_MESSAGE t\n" + 
		" WHERE t.is_use = 'Y'\n" + 
		"   AND t.entry_by = '"+workerCode+"'\n" + 
		"   AND t.enterprise_code = '"+enterpriseCode+"'\n";
	
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		HrJMessageForm form = new HrJMessageForm();
		Iterator it = list.iterator();
		PageObject object = new PageObject();
		List<HrJMessageForm> arraylist = new ArrayList<HrJMessageForm>();
		while (it.hasNext()) {
			form = new HrJMessageForm();

			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				form.setMessageId(data[0].toString());
			}
			if (data[1] != null) {
				form.setUserCode(data[1].toString());
			}
			if (data[2] != null) {
				form.setUserPsw(data[2].toString());
			}
			if (data[3] != null) {
				String msgPerson=data[3].toString();
				String msgPersons[]=msgPerson.split(",");
				for(int i=0;i<msgPersons.length;i++)
				{
					msgName+=this.getMsgName(msgPersons[i])+",";
			    }
				msgName=msgName.substring(0,msgName.lastIndexOf(","));
				form.setMsgPerson(data[3].toString());
				form.setMsgName(msgName);
				msgName="";
				
			}
			if (data[4] != null) {
				form.setMsgContent(data[4].toString());
			}
			if (data[5] != null) {
				form.setIsSend(data[5].toString());
			}
			if (data[6] != null) {
				form.setEntryBy(data[6].toString());
			}
			if (data[7] != null) {
				form.setEntryName(data[7].toString());
			}
			
			if (data[8] != null) {
				form.setEntryDate(data[8].toString());
			}
			arraylist.add(form);
		}

		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		object.setList(arraylist);
		object.setTotalCount(totalCount);
		return object;

	}
	

		public void sendMessage(String messageId) {

			String sql = "update hr_j_message t\n" + "   set t.is_send = 'Y'\n"
					+ " where t.message_id in (" + messageId + ")";

			bll.exeNativeSQL(sql);

		}
		
}