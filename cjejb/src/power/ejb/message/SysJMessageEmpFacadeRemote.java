package power.ejb.message;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.message.form.Message;

 /**
 * @author bjXu
 *
 */
@Remote
public interface SysJMessageEmpFacadeRemote {
	/**
	 * ����һ����Ϣ
	 * @param entity ��Ϣʵ�����
	 * @return void 
	 */
	public void save(SysJMessageEmp entity);

	/**
	 * ɾ��һ����Ϣ
	 * @param entity ��Ϣʵ�����
	 * @return void 
	 */
	public void delete(SysJMessageEmp entity);

	/**
	 * ����һ����Ϣ
	 * @param entity ��Ϣʵ�����
	 * @return void 
	 */
	public SysJMessageEmp update(SysJMessageEmp entity);

	public SysJMessageEmp findById(Long id);
	
	/**
	 * ������ϢID��ѯ�ѱ���δ���͵���Ϣ�б�
	 * @param messageId ��ϢID
	 * return List<SysJMessageEmp> ��Ϣ�б�
	 */
	public List<SysJMessageEmp> findByMessageId(Long messageId);
	
	/**
	 * ������ϢId����Ϣ������code��ѯ�ѱ���δ���͵���Ϣʵ��
	 * @param messageId ��ϢId
	 * @param reveiveId ������code
	 * return SysJMessageEmp
	 */
	public SysJMessageEmp findMessage(Long messageId,String reveiveId);
	/**
	 * ������ϢId��ѯ�ѱ���δ�鿴����Ϣʵ��
	 * @param messageId ��ϢId
	 * return SysJMessageEmp
	 */
	public SysJMessageEmp findMessage(Long messageId);
	/**
	 * ���ݽ�����Id��ѯδ�鿴��Ϣ��Ŀ
	 * @param receiveId ������Id(workerCode)
	 * return Long
	 */
	public Long getNoVeiwMessageCount(String receiveId);
}