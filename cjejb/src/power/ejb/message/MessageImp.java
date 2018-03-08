package power.ejb.message;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.message.form.Message;
import power.ejb.message.form.WorkerInfo;

@Stateless
public class MessageImp implements MessageInterface {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "SysJMessageEmpFacade")
	protected SysJMessageEmpFacadeRemote remote;
	@EJB(beanName = "SysJMessageFacade")
	protected SysJMessageFacadeRemote mremote;

	public PageObject findMessageList(String wokerCode, String starttime,
			String endtime, String statusFlag, int... rowStartIdxAndCount) {
		String sql = "";
		String sqlCount = "";
		PageObject result = new PageObject();

		sql += "select"
				+ "       t.message_id,\n"
				+ "       t.send_by_id,\n"
				+ "       getworkername(t.send_by_id),\n"
				+ "       t.receive_by_id,\n"
				+ "       getworkername(t.receive_by_id),\n"
				+ "       to_char(t.send_date,'yyyy-MM-dd HH24:MI:SS'),\n"
				+ "       t.message_status,\n"
				+ "      decode(t.message_status,\n"
				+ "       0 ,'撰写',\n"
				+ "       1 , '已发送',\n"
				+ "       2 , '已查看','未知'\n"
				+ "      ),\n"
				+ "      m.title,\n"
				+ "      m.text,\n"
				+ "      m.doc_type_id,\n"
				+ "      m.doc_name,\n"
				+ "      t.id,\n"
				+ "(select j.zbbmtx_name\n"
				+ "          from jljf_c_object j, hr_j_company_worker e\n"
				+ "         where e.worker_code = t.receive_by_id\n"
				+ "           and j.is_use = 'Y'\n"
				+ "           and j.zbbmtx_code = e.zbbmtx_code and rownum=1) zbbmtx_name"
				+ "  from sys_j_message_emp t,sys_j_message m\n"
				+ " where t.send_by_id = ?\n"
				+ "   and m.message_id=t.message_id\n"
				+ "   and t.send_date > to_date('" + starttime
				+ "'||' 00:00:00','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.send_date < to_date('" + endtime
				+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.message_status in (" + statusFlag + ")\n"
				+ "   and m.is_use='Y'\n" + " order by t.message_status";

		sqlCount += "select" + "	count(1)\n"
				+ "  from sys_j_message_emp t,sys_j_message m\n"
				+ "   where t.send_by_id = '" + wokerCode + "'\n"
				+ "     and m.message_id=t.message_id\n"
				+ "     and t.send_date > to_date('" + starttime
				+ "'||' 00:00:00','yyyy-MM-dd HH24:MI:SS')\n"
				+ "     and t.send_date < to_date('" + endtime
				+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n"
				+ "     and t.message_status in (" + statusFlag + ")\n"
				+ "     and m.is_use='Y'\n" + "   order by t.message_status";

		List list = bll.queryByNativeSQL(sql, new Object[] { wokerCode },
				rowStartIdxAndCount);
		if (list != null) {
			List<Message> arr = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] ob = (Object[]) list.get(i);
				Message model = new Message();
				if (ob[0] != null)
					model.setMessageId(Long.parseLong(ob[0].toString()));
				if (ob[1] != null)
					model.setSendById(ob[1].toString());
				if (ob[2] != null)
					model.setSenderName(ob[2].toString());
				if (ob[3] != null)
					model.setReceiveById(ob[3].toString());
				if (ob[4] != null)
					model.setReceiverName(ob[4].toString());
				if (ob[5] != null)
					model.setSendDate(ob[5].toString());
				if (ob[6] != null)
					model.setMessageStatus(Long.parseLong(ob[6].toString()));
				if (ob[7] != null)
					model.setStatusFlag(ob[7].toString());
				if (ob[8] != null)
					model.setTitle(ob[8].toString());
				if (ob[9] != null)
					model.setText(ob[9].toString());
				if (ob[10] != null)
					model.setDocTypeId(Long.parseLong(ob[10].toString()));
				if (ob[11] != null)
					model.setDocName(ob[11].toString());
				if (ob[12] != null)
					model.setId(Long.parseLong(ob[12].toString()));
				if (ob[13] != null)
					model.setZbbmtxName(ob[13].toString());
				arr.add(model);
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(arr);
			result.setTotalCount(totalCount);
			return result;
		} else {
			return null;
		}
	}

	public PageObject findReceiveMessageList(String wokerCode, String start,
			String end, String statusFlag, int... rowStartIdxAndCount) {
		String sql = "";
		String sqlCount = "";
		PageObject result = new PageObject();
		sql = "select"
				+ "       t.message_id,\n"
				+ "       t.send_by_id,\n"
				+ "       getworkername(t.send_by_id),\n"
				+ "       t.receive_by_id,\n"
				+ "       getworkername(t.receive_by_id),\n"
				+ "       to_char(t.send_date,'yyyy-MM-dd HH24:MI:SS'),\n"
				+ "       t.message_status,\n"
				+ "      decode(t.message_status,\n"
				+ "       1 , '未查看',\n"
				+ "       2 , '已查看','未知'\n"
				+ "      ),\n"
				+ "      m.title,\n"
				+ "      m.text,\n"
				+ "      m.doc_type_id,\n"
				+ "      m.doc_name,\n"
				+ "      t.id,\n"
				+ "(select j.zbbmtx_name\n"
				+ "          from jljf_c_object j, hr_j_company_worker e\n"
				+ "         where e.worker_code = t.send_by_id\n"
				+ "           and j.is_use = 'Y'\n"
				+ "           and j.zbbmtx_code = e.zbbmtx_code and rownum=1) zbbmtx_name"
				+ "  from sys_j_message_emp t,sys_j_message m\n"
				+ " where t.receive_by_id = ?\n"
				+ "   and m.message_id=t.message_id\n"
				+ "   and t.send_date > to_date('" + start
				+ "'||' 00:00:00','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.send_date < to_date('" + end
				+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.message_status in (" + statusFlag + ")\n"
				+ "   and m.is_use='Y'\n" + " order by t.message_status";
		sqlCount = "select" + "  count(1)\n"
				+ "  from sys_j_message_emp t,sys_j_message m\n"
				+ " where t.receive_by_id = ?\n"
				+ "   and m.message_id=t.message_id\n"
				+ "   and t.send_date > to_date('" + start
				+ "'||' 00:00:00','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.send_date < to_date('" + end
				+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.message_status in (" + statusFlag + ")\n"
				+ "   and m.is_use='Y'";

		List list = bll.queryByNativeSQL(sql, new Object[] { wokerCode },
				rowStartIdxAndCount);
		List<Message> arr = new ArrayList();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Object[] ob = (Object[]) list.get(i);
				Message model = new Message();
				if (ob[0] != null)
					model.setMessageId(Long.parseLong(ob[0].toString()));
				if (ob[1] != null)
					model.setSendById(ob[1].toString());
				if (ob[2] != null)
					model.setSenderName(ob[2].toString());
				if (ob[3] != null)
					model.setReceiveById(ob[3].toString());
				if (ob[4] != null)
					model.setReceiverName(ob[4].toString());
				if (ob[5] != null)
					model.setSendDate(ob[5].toString());
				if (ob[6] != null)
					model.setMessageStatus(Long.parseLong(ob[6].toString()));
				if (ob[7] != null)
					model.setStatusFlag(ob[7].toString());
				if (ob[8] != null)
					model.setTitle(ob[8].toString());
				if (ob[9] != null)
					model.setText(ob[9].toString());
				if (ob[10] != null)
					model.setDocTypeId(Long.parseLong(ob[10].toString()));
				if (ob[11] != null)
					model.setDocName(ob[11].toString());
				if (ob[12] != null)
					model.setId(Long.parseLong(ob[12].toString()));
				if (ob[13] != null)
					model.setZbbmtxName(ob[13].toString());
				arr.add(model);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
				new Object[] { wokerCode }).toString());
		result.setList(arr);
		result.setTotalCount(totalCount);
		return result;
	}


	public void saveMessage(SysJMessage sysmessage, List<SysJMessageEmp> list) {
		Long messageId = mremote.save(sysmessage);
		if (messageId != null) {
			long count=bll.getMaxId("SYS_J_MESSAGE_EMP", "id");
			for (SysJMessageEmp emp : list) {
				emp.setMessageId(messageId);
				emp.setId(count++);
				remote.save(emp);
			}
		}
	}

	/*
	 * 判断取得登录人
	 */
	public List<WorkerInfo> judgeAdminCus(String workerCode) {
		String sql = "select t.* from hrj_company_worker t"
				+ " where t.worker_code=?" + " and t.zbbmtx_code=''";
		List list = bll.queryByNativeSQL(sql, new Object[] { workerCode });
		if (list != null) {
			return list;
		} else {
			return null;
		}
	}

	// public boolean judgeAdminCus(String workerCode){
	// String sql= "select t.* from hr_j_company_worker t" +
	// " where t.worker_code=? " +
	// " and t.zbbmtx_code=''";
	// int size = Integer.parseInt(bll.getSingal(sql, new Object[]
	// {workerCode}).toString());
	// if(size > 0){
	// return true;
	// }
	// return false;
	// }

}
