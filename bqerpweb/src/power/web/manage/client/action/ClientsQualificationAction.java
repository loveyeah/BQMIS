package power.web.manage.client.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConJClientsQualification;
import power.ejb.manage.client.ConJClientsQualificationFacadeRemote;
import power.ejb.manage.client.form.ConJClientsQualificationForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ClientsQualificationAction extends AbstractAction{
	
	private ConJClientsQualification qualification;
	protected ConJClientsQualificationFacadeRemote remote;
	private Long qualificationId;
	
	public ClientsQualificationAction()
	{
		remote = (ConJClientsQualificationFacadeRemote)factory.getFacadeRemote("ConJClientsQualificationFacade");
	}
	
	/**
	 * 增加
	 * @return
	 */
	public void addQualification()
	{
		qualification.setEnterpriseCode(employee.getEnterpriseCode());
		qualification.setLastModifiedBy(employee.getWorkerCode());
		try{
			qualification = remote.save(qualification);
			write("{success:true,id:'"+qualification.getQualificationId()+"',msg:'增加成功！'}");
		}catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:资质名称不能重复！'}");
		}
	}

	/**
	 * 修改
	 * @return
	 */
	public void updateQualification()
	{
		ConJClientsQualification model = remote.findById(qualification.getQualificationId());
		model.setAptitudeName(qualification.getAptitudeName());
		model.setCliendId(qualification.getCliendId());
		model.setQualificationOrg(qualification.getQualificationOrg());
		model.setSendPaperDate(qualification.getSendPaperDate());
		model.setBeginDate(qualification.getBeginDate());
		model.setEndDate(qualification.getEndDate());
		model.setMemo(qualification.getMemo());
		model.setLastModifiedBy(employee.getWorkerCode());
		model.setLastModifiedDate(qualification.getLastModifiedDate());
		try{
			remote.update(model);
			write("{success:true,id:'"+qualification.getQualificationId()+"',msg:'修改成功！'}");
		}catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:资质名称不能重复！'}");
		}
	}
	
	/**
	 * 删除
	 * @return
	 */
	public void deleteQualification()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 列表显示
	 * @return
	 * @throws JSONException 
	 */
	public void findClientsQualificationList() throws JSONException {
		String fuzzy = request.getParameter("fuzzy");
		String clientId = request.getParameter("clientId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findClientsQualificationList(employee
					.getEnterpriseCode(), fuzzy,clientId, start,
					limit);
		} else {
			obj = remote.findClientsQualificationList(employee
					.getEnterpriseCode(), fuzzy,clientId);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 根据资质ID查询信息
	 * @throws JSONException
	 */
	public void findQualificationById() throws JSONException
	{
		ConJClientsQualificationForm model = remote.findQualificationById(qualificationId, employee.getEnterpriseCode());
		write("{success:true,data:" + JSONUtil.serialize(model) + "}");
	}
	
	public ConJClientsQualification getQualification() {
		return qualification;
	}

	public void setQualification(ConJClientsQualification qualification) {
		this.qualification = qualification;
	}

	public Long getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(Long qualificationId) {
		this.qualificationId = qualificationId;
	}
	
}
