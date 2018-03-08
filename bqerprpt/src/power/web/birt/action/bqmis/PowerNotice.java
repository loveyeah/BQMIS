package power.web.birt.action.bqmis;

import java.text.ParseException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.run.powernotice.RunJPowerNoticeApproveFacadeRemote;
import power.ejb.run.powernotice.form.PowerNoticeForPrint;
import power.web.birt.constant.commUtils;

public class PowerNotice {

	/** 联系单号 */
	private String noticeNo;

	public static Ejb3Factory factory;

	public RunJPowerNoticeApproveFacadeRemote remote;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public PowerNotice() {
		remote = (RunJPowerNoticeApproveFacadeRemote) factory
				.getFacadeRemote("RunJPowerNoticeApproveFacade");
	}

	/**
	 * @return the noticeNo
	 */
	public String getNoticeNo() {
		return noticeNo;
	}

	/**
	 * @param noticeNo
	 *            the noticeNo to set
	 */
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	
	public PowerNoticeForPrint setPowerNoticeForPrint(String enterpriseCode,String noticeNo) throws ParseException{
		PowerNoticeForPrint entity = remote.findByNoForPrint(enterpriseCode, noticeNo);
		String contactContent = commUtils.replaceWithBR(commUtils.checkString(entity.getContactContent()));
		if (contactContent != null) {
				entity.setContactContent(contactContent);
		}
		entity.setContactContent(contactContent);
		return entity;
	}

}
