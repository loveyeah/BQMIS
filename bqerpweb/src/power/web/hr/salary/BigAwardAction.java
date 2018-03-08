package power.web.hr.salary;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCBigAward;
import power.ejb.hr.salary.HrCBigAwardFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BigAwardAction extends AbstractAction {
	private HrCBigAwardFacadeRemote maintRemote;
	private HrCBigAward bigAward;
	private int start;
	private int limit;

	public BigAwardAction() {
		maintRemote = (HrCBigAwardFacadeRemote) factory
				.getFacadeRemote("HrCBigAwardFacade");
	}

	public void saveBigAward() {
		String awardMonth = request.getParameter("awardMonth");
		boolean existFlag = false;
		existFlag = maintRemote.checkAwardName(awardMonth);
		if (existFlag == true) {
			write("{success:true,existFlag:" + existFlag + "}");
		} else {
			bigAward.setEnterpriseCode(employee.getEnterpriseCode());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				bigAward.setAwardMonth(sdf.parse(awardMonth));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			maintRemote.save(bigAward);
			write("{success:true,msg:'增加成功！'}");
		}
	}

	public void updateBigAward() throws CodeRepeatException {
		HrCBigAward entity = maintRemote.findById(bigAward.getBigAwardId());
		if (bigAward.getAssessmentFrom() != null
				&& !("".equals(bigAward.getAssessmentFrom()))) {
			entity.setAssessmentFrom(bigAward.getAssessmentFrom());
		}
		if (bigAward.getAssessmentTo() != null
				&& !("".equals(bigAward.getAssessmentTo()))) {
			entity.setAssessmentTo(bigAward.getAssessmentTo());
		}
		entity.setBigAwardName(bigAward.getBigAwardName());//add by sychen 20100721
		entity.setAllStandarddays(bigAward.getAllStandarddays());//add by wpzhu 20100716
		entity.setHalfStandarddays(bigAward.getHalfStandarddays());
		entity.setBigAwardBase(bigAward.getBigAwardBase());
		entity.setMemo(bigAward.getMemo());
		maintRemote.update(entity);
		write("{success:true,msg:'修改成功！'}");
	}

	public void deleteBigAward() {

		String ids = request.getParameter("ids");
		String awardMonth = request.getParameter("awardMonth");
		maintRemote.delete(ids, awardMonth);
		write("{success:true,msg:'删除成功！'}");
	}

	public void findBigAwardList() throws NumberFormatException, Exception {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String awardMonth = request.getParameter("awardTime");
		PageObject pg = null;
		if (start != null && limit != null && start.equals("null"))
			pg = maintRemote.findBigAwardList(awardMonth, employee
					.getEnterpriseCode(), Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			pg = maintRemote.findBigAwardList(awardMonth, employee
					.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
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

	public HrCBigAward getBigAward() {
		return bigAward;
	}

	public void setBigAward(HrCBigAward bigAward) {
		this.bigAward = bigAward;
	}

}
