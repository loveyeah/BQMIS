package power.web.equ.workbill.action;



import java.util.List;

import power.ejb.equ.workbill.EquJWo;
import power.ejb.equ.workbill.EquJWoFacadeRemote;
import power.ejb.equ.workbill.EquJWoPenotion;
import power.ejb.equ.workbill.EquJWoPenotionFacadeRemote;
import power.ejb.equ.workbill.EquJWoer;
import power.ejb.equ.workbill.EquJWoerFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class EquJFinishedRptAction extends AbstractAction {

	private EquJWoFacadeRemote remote;
	private EquJWoPenotionFacadeRemote frremote;
	private EquJWoerFacadeRemote erremote;

	public EquJFinishedRptAction() {
		remote = (EquJWoFacadeRemote) factory.getFacadeRemote("EquJWoFacade");
		frremote = (EquJWoPenotionFacadeRemote) factory
				.getFacadeRemote("EquJWoPenotionFacade");
		erremote = (EquJWoerFacadeRemote) factory
				.getFacadeRemote("EquJWoerFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void saveEquJFinishedRpt() {

		try {
			String woCode = request.getParameter("woCode");
			String checkReasonid = request.getParameter("checkReasonid");
			String checkReportid = request.getParameter("checkReportid");
			String checkResultid = request.getParameter("checkResultid");
			String peNotion = request.getParameter("peNotion");

			EquJWo baseInfo = remote.findBywoCode(woCode, employee
					.getEnterpriseCode());
			baseInfo.setCheckReasonid(checkReasonid);
			baseInfo.setCheckReportid(checkReportid);
			baseInfo.setCheckResultid(checkResultid);
			//Modify by kzhang 20100929
//			String failureCode = erremote.findByWoCode(woCode,
//					employee.getEnterpriseCode()).get(0).getFailureCode(); 
			String failureCode=null;
			List<EquJWoer> list=erremote.findByWoCode(woCode,employee.getEnterpriseCode());
			if(list.size()>0){
				failureCode=list.get(0).getFailureCode();
			}
			//------------------------------------------------缺陷单号
			remote.update(baseInfo, failureCode);

			EquJWoPenotion model = new EquJWoPenotion();
			model.setWoCode(woCode);
			model.setPeNotion(peNotion);
			EquJWoPenotion entity = frremote.save(model);

			write("{success: true,msg:'保存成功！',id:" + entity.getId() + "}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success: false,msg:'保存失败！'}");
		}
	}

	/**
	 * 修改记录
	 * 
	 */
	public void updateEquJFinishedRpt() {
		try {
			String woCode = request.getParameter("woCode");
			String checkReasonid = request.getParameter("checkReasonid");
			String checkReportid = request.getParameter("checkReportid");
			String checkResultid = request.getParameter("checkResultid");
			String peNotion = request.getParameter("peNotion");

			EquJWo baseInfo = remote.findBywoCode(woCode, employee
					.getEnterpriseCode());
			baseInfo.setCheckReasonid(checkReasonid);
			baseInfo.setCheckReportid(checkReportid);
			baseInfo.setCheckResultid(checkResultid);
			//Modify by kzhang 20100929
//			String failureCode = erremote.findByWoCode(woCode,
//			employee.getEnterpriseCode()).get(0).getFailureCode();
			String failureCode=null;
			List<EquJWoer> list=erremote.findByWoCode(woCode,employee.getEnterpriseCode());
			if(list.size()>0){
				failureCode=list.get(0).getFailureCode();
			}
			//------------------------------------------------缺陷单号
			remote.update(baseInfo, failureCode);

			EquJWoPenotion model = frremote.findByProperty("woCode", woCode)
					.get(0);
			model.setPeNotion(peNotion);
			frremote.update(model);

			write("{success:true,msg:'更新成功'}");
		} catch (Exception e) {
			write("{success:false,msg:'更新失败'}");
		}
	}

	/**
	 * 取列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquJFinishedRpt() throws JSONException {

		String woCode = request.getParameter("woCode");

		if (frremote.findBywoCode(woCode) != null
				&& remote.findBywoCode(woCode, employee.getEnterpriseCode()) != null) {

			EquJWo baseInfo = remote.findBywoCode(woCode, employee
					.getEnterpriseCode());
			EquJWoPenotion model = frremote.findBywoCode(woCode);

			write("{success : true,baseInfo:"
					+ JSONUtil.serialize(baseInfo) + ",model:"
					+ JSONUtil.serialize(model) + "}");
			

		} else
			write("{success : false}");
	}

}
