package power.web.run.securityproduction.action;

import java.text.ParseException;
import java.util.List;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.securityproduction.SpJBriefnews;
import power.ejb.run.securityproduction.SpJBriefnewsFacadeRemote;
import power.ejb.run.securityproduction.form.BriefNewsForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BriefNewsAction extends AbstractAction {

	private SpJBriefnewsFacadeRemote remote;

	private SpJBriefnews briefnews;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public BriefNewsAction() {
		remote = (SpJBriefnewsFacadeRemote) factory
				.getFacadeRemote("SpJBriefnewsFacade");
	}

	public void addBriefNews() throws Exception {
		String month = request.getParameter("month");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
			briefnews.setEnterpriseCode(employee.getEnterpriseCode());
			briefnews.setMonth(formatter.parse(month));
		Long briefnewsId = remote.save(briefnews).getBriefnewsId();
			write("{success:true,msg:'增加成功！',briefnewsId:"+briefnewsId+"}");
	}

	public void deleteBriefnews() throws Exception {
		 String briefnewsId=request.getParameter("briefnewsId");
		 remote.delete(remote.findById(Long.parseLong(briefnewsId)));
		 write("{success:true,msg:'删除成功！'}");

	}
	public void updateBriefNews() throws ParseException {
		String month = request.getParameter("month");
		String briefNewsId = request.getParameter("briefnewsId");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		SpJBriefnews entity = remote.findById(Long.parseLong(briefNewsId));
		entity.setCommonDate(briefnews.getCommonDate());
		entity.setCommonBy(briefnews.getCommonBy());
		entity.setContent(briefnews.getContent());
		entity.setIssue(briefnews.getIssue());
		entity.setMonth(formatter.parse(month));
		remote.update(entity);
		write("{success:true,msg:'修改成功！'}");
	}
	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */

	public void getBriefNews() throws JSONException {
		String month = request.getParameter("queryString");
		List <BriefNewsForm> brfNews = remote.findAll(month);
		write(JSONUtil.serialize(brfNews));
	}

	public SpJBriefnews getBriefnews() {
		return briefnews;
	}

	public void setBriefnews(SpJBriefnews briefnews) {
		this.briefnews = briefnews;
	}

}
