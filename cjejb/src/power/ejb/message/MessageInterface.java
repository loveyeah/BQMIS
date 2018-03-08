package power.ejb.message;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.message.form.Message;
import power.ejb.message.form.WorkerInfo;
@Remote 
public interface MessageInterface {
	/**
	 * ���ݵ�¼�˹��ţ�ʱ���ѯ��Ϣ�б�
	 * @param wokerCode ����
	 * @param start ��ʼʱ��
	 * @param end ����ʱ��
	 * @param statusFlag ״̬��ʶ��(
	 * ���磺��0��1��2)��
	 * @return List<SysJMessageEmp> 
	 */
//	public  List<Message> findMessageList(String wokerCode,String start,String end,String statusFlag);
	public  PageObject findMessageList(String wokerCode,String starttime,String endtime,String statusFlag,int... rowStartIdxAndCount);
	/**
	 * ���ݵ�¼�˹��ţ�ʱ���ѯ��Ϣ�б�
	 * @param wokerCode ����
	 * @param start ��ʼʱ��
	 * @param end ����ʱ��
	 * @param statusFlag ״̬��ʶ��(���磺��0��1��2)��
	 * @param rowStartIdxAndCount ҳ��������С����
	 * @return List<SysJMessageEmp> 
	 */
	public  PageObject findReceiveMessageList(String wokerCode,String start,String end,String statusFlag,int... rowStartIdxAndCount);
	/**
	 * ���棬������Ϣ
	 * @param sysmessage ��Ϣʵ��
	 * @param List<SysJMessageEmp> list��Ϣ��Ӧ��ʵ���б�
	 */

	public void saveMessage(SysJMessage sysmessage, List<SysJMessageEmp> list);
	
	
	/*
	*�жϵ�¼��
	*@param workerCode ����
	*@return List<WorkerInfo> 
	*/
	public List<WorkerInfo> judgeAdminCus(String workerCode);
//	public boolean judgeAdminCus(String workerCode);
}
