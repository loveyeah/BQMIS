package power.web.run.timework.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.AbstractAction;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.timework.RunJTimework;
import power.ejb.run.timework.RunJTimeworkFacadeRemote;
import power.ejb.run.timework.RunJTimeworkInfo;
import power.ejb.system.SysCUlFacadeRemote;

@SuppressWarnings("serial")
public class TimeworkjAction extends AbstractAction {

	private int start;
	private int limit;
	protected String queryWorkType;// 工作类型 对应字段WORK_TYPE
	protected String queryRunType;// 是否需要审批 对应字段IFCHECK
	protected String queryApproveType;// 工作记录的状态 对应字段STATUS
	protected String queryDelayType;// 是否申请延期 对应字段IFDELAY
	protected String querystimeDate;// 查询开始时间 对应字段work_date
	protected String queryetimeDate;// 查询结束时间 对应字段work_date
	protected String queryWorkitemName;
	protected String queryMachprofCode;
	protected String queryWorkResult;
	protected RunJRunlogMainFacadeRemote bll;
	protected RunJTimeworkFacadeRemote remote;
	protected String id;
	protected String username;
	protected String password;
	protected String password1;
	protected String password2;

	protected String operator;
	protected String protector;
	protected String memo;
	protected String imagecode;
	protected String workExplain;
	protected String RungridWorkresult;

	protected RunJTimework timeworkInfoTemp;

	public TimeworkjAction() {
		remote = (RunJTimeworkFacadeRemote) factory
				.getFacadeRemote("RunJTimeworkFacade");
		bll = (RunJRunlogMainFacadeRemote) factory
				.getFacadeRemote("RunJRunlogMainFacade");
	}

	// 取定期工作运行列表
	@SuppressWarnings("unchecked")
	public void getlistTimeworkj() throws Exception {
		List<Object[]> list = null;
		if (employee != null) {
			list = bll.findRunLogByWorker(employee.getEnterpriseCode(),
					employee.getWorkerCode());
		}
		if (list.size() == 0) {
		} else {
			PageObject obj = new PageObject();
			if (("delay").equals(queryWorkType)) {
				obj = (PageObject) remote.findByIsOther((Object) ("Y"),
						queryWorkType, queryRunType, queryApproveType,
						queryDelayType, querystimeDate, queryetimeDate,
						queryWorkitemName, queryMachprofCode, queryWorkResult,
						start, limit);
			} else if (("temp").equals(queryWorkType)) {
				obj = (PageObject) remote.findByIsOther((Object) ("Y"),
						queryWorkType, queryRunType, queryApproveType,
						queryDelayType, querystimeDate, queryetimeDate,
						queryWorkitemName, queryMachprofCode, queryWorkResult,
						start, limit);
			} else if (("ed").equals(queryWorkType)) {
				obj = (PageObject) remote.findByIsOther((Object) ("Y"),
						queryWorkType, queryRunType, queryApproveType,
						queryDelayType, querystimeDate, queryetimeDate,
						queryWorkitemName, queryMachprofCode, queryWorkResult,
						start, limit);
			} else {
				obj = (PageObject) remote.findByIsUse((Object) ("Y"),
						queryWorkType, queryRunType, queryApproveType,
						queryDelayType, querystimeDate, queryetimeDate,
						queryWorkitemName, queryMachprofCode, queryWorkResult,
						start, limit);
			}

			PageObject result = new PageObject();
			List alist = obj.getList();
			List arrlist = new ArrayList();
			Iterator it = alist.iterator();
			while (it.hasNext()) {
				RunJTimework model = new RunJTimework();
				RunJTimeworkInfo omodel = new RunJTimeworkInfo();
				Object[] data = (Object[]) it.next();
				model.setId(Long.parseLong(data[0].toString()));
				model.setWorkItemCode(data[1].toString());
				model.setMachprofCode(data[2].toString());
				if (data[3] != null) {
					model.setWorkType(data[3].toString());
				}
				if (data[4] != null) {
					model.setWorkItemName(data[4].toString());
				}
				if (data[5] != null) {
					model
							.setWorkDate(java.sql.Date.valueOf(data[5]
									.toString()));
				}
				if (data[6] != null) {
					model.setCycle(data[6].toString());
				}
				if (data[7] != null) {
					model
							.setClassSequence(Long.parseLong((data[7]
									.toString())));
				}
				if (data[8] != null) {
					model.setClassTeam(data[8].toString());
				}
				if (data[9] != null) {
					model.setDutytype((Long.parseLong(data[9].toString())));
				}
				if (data[10] != null) {
					model.setIfdelay((data[10].toString()));
				}
				if (data[11] != null) {
					model.setDelayresult((data[11].toString()));
				}
				if (data[12] != null) {
					model.setDelayman((data[12].toString()));
				}
				if (data[13] != null) {
					model.setDelaydate(java.sql.Date.valueOf(data[13]
							.toString()));
				}
				if (data[14] != null) {
					model.setDelaytype(data[14].toString());
				}
				if (data[15] != null) {
					model.setOpTicket(data[15].toString());
				}
				if (data[16] != null) {
					model.setWorkresult(data[16].toString());
				}
				if (data[17] != null) {
					model.setWorkExplain(data[17].toString());
				}
				if (data[18] != null) {
					model.setIfcheck(data[18].toString());
				}
				if (data[19] != null) {
					model.setCheckdate(java.sql.Date.valueOf(data[19]
							.toString()));
				}
				if (data[20] != null) {
					model.setCheckman(data[20].toString());
				}
				if (data[21] != null) {
					model.setCheckresult(data[21].toString());
				}
				if (data[22] != null) {
					model.setIfimage(data[22].toString());
				}
				if (data[23] != null) {
					model.setImagecode(data[23].toString());
				}
				if (data[24] != null) {
					model.setIfExplain(data[24].toString());
				}
				if (data[25] != null) {
					model.setMemo(data[25].toString());
				}
				if (data[26] != null) {
					model.setOperator(data[26].toString());
				}
				if (data[27] != null) {
					model.setDelingDate(java.sql.Date.valueOf(data[27]
							.toString()));
				}
				if (data[28] != null) {
					model.setDelayDate(java.sql.Date.valueOf(data[28]
							.toString()));
				}
				if (data[29] != null) {
					model.setProtector(data[29].toString());
				}
				if (data[30] != null) {
					model.setIfOpticket(data[30].toString());
				}
				if (data[31] != null) {
					model.setConman(data[31].toString());
				}
				if (data[32] != null) {
					model
							.setCondate(java.sql.Date.valueOf(data[32]
									.toString()));
				}
				if (data[33] != null) {
					model.setStatus(data[33].toString());
				}
				if (data[34] != null) {
					model.setEnterprisecode(data[34].toString());
				}
				if (data[35] != null) {
					model.setIsUse(data[35].toString());
				}
				if (data[40] != null) {
					omodel.setMachprofName(data[40].toString());
				}
				if (data[41] != null) {
					omodel.setWorktypeName(data[41].toString());
				}
				if (data[42] != null) {
					omodel.setClasssequenceName(data[42].toString());
				}
				if (data[43] != null) {
					omodel.setDutytypeName(data[43].toString());
				}
				if (data[44] != null) {
					omodel.setIfdelayNm(data[44].toString());
				}
				if (data[45] != null) {
					omodel.setDelayresultNm(data[45].toString());
				}
				if (data[46] != null) {
					omodel.setDelaymanName(data[46].toString());
				}
				if (data[47] != null) {
					omodel.setWorkresultNm(data[47].toString());
				}
				if (data[48] != null) {
					omodel.setIfcheckNm(data[48].toString());
				}
				if (data[49] != null) {
					omodel.setCheckmanName(data[49].toString());
				}
				if (data[50] != null) {
					omodel.setCheckresultNm(data[50].toString());
				}
				if (data[51] != null) {
					omodel.setIfimageNm(data[51].toString());
				}
				if (data[52] != null) {
					omodel.setIfexplainNm(data[52].toString());
				}
				if (data[53] != null) {
					omodel.setOperatorName(data[53].toString());
				}
				if (data[54] != null) {
					omodel.setProtectorName(data[54].toString());
				}
				if (data[55] != null) {
					omodel.setIfopticketNm(data[55].toString());
				}
				if (data[56] != null) {
					omodel.setConmanName(data[56].toString());
				}
				if (data[57] != null) {
					omodel.setStatusNm(data[57].toString());
				}
				if (data[58] != null) {
					omodel.setWorkDateNm(data[58].toString());
				}
				if (data[59] != null) {
					omodel.setDelaydateNm(data[59].toString());
				}
				if (data[60] != null) {
					omodel.setCheckdateNm(data[60].toString());
				}
				if (data[61] != null) {
					omodel.setDelingDateNm(data[61].toString());
				}
				if (data[62] != null) {
					omodel.setDelayDateNm(data[62].toString());
				}
				if (data[63] != null) {
					omodel.setCondateNm(data[63].toString());
				}
				omodel.setTimeworkjInfo(model);
				arrlist.add(omodel);
			}
			result.setList(arrlist);
			result.setTotalCount(obj.getTotalCount());
			write(JSONUtil.serialize(result));
		}
	}

	// 取定期工作运行列表
	@SuppressWarnings("unchecked")
	public void querylistTimeworkj() throws Exception {
		PageObject obj = new PageObject();
		if (("delay").equals(queryWorkType)) {
			obj = (PageObject) remote.findByIsOther((Object) ("Y"),
					queryWorkType, queryRunType, queryApproveType,
					queryDelayType, querystimeDate, queryetimeDate,
					queryWorkitemName, queryMachprofCode, queryWorkResult,
					start, limit);
		} else if (("temp").equals(queryWorkType)) {
			obj = (PageObject) remote.findByIsOther((Object) ("Y"),
					queryWorkType, queryRunType, queryApproveType,
					queryDelayType, querystimeDate, queryetimeDate,
					queryWorkitemName, queryMachprofCode, queryWorkResult,
					start, limit);
		} else if (("ed").equals(queryWorkType)) {
			obj = (PageObject) remote.findByIsOther((Object) ("Y"),
					queryWorkType, queryRunType, queryApproveType,
					queryDelayType, querystimeDate, queryetimeDate,
					queryWorkitemName, queryMachprofCode, queryWorkResult,
					start, limit);
		} else {
			obj = (PageObject) remote.findByIsUse((Object) ("Y"),
					queryWorkType, queryRunType, queryApproveType,
					queryDelayType, querystimeDate, queryetimeDate,
					queryWorkitemName, queryMachprofCode, queryWorkResult,
					start, limit);
		}

		PageObject result = new PageObject();
		List alist = obj.getList();
		List arrlist = new ArrayList();
		Iterator it = alist.iterator();
		while (it.hasNext()) {
			RunJTimework model = new RunJTimework();
			RunJTimeworkInfo omodel = new RunJTimeworkInfo();
			Object[] data = (Object[]) it.next();
			model.setId(Long.parseLong(data[0].toString()));
			model.setWorkItemCode(data[1].toString());
			model.setMachprofCode(data[2].toString());
			if (data[3] != null) {
				model.setWorkType(data[3].toString());
			}
			if (data[4] != null) {
				model.setWorkItemName(data[4].toString());
			}
			if (data[5] != null) {
				model.setWorkDate(java.sql.Date.valueOf(data[5].toString()));
			}
			if (data[6] != null) {
				model.setCycle(data[6].toString());
			}
			if (data[7] != null) {
				model.setClassSequence(Long.parseLong((data[7].toString())));
			}
			if (data[8] != null) {
				model.setClassTeam(data[8].toString());
			}
			if (data[9] != null) {
				model.setDutytype((Long.parseLong(data[9].toString())));
			}
			if (data[10] != null) {
				model.setIfdelay((data[10].toString()));
			}
			if (data[11] != null) {
				model.setDelayresult((data[11].toString()));
			}
			if (data[12] != null) {
				model.setDelayman((data[12].toString()));
			}
			if (data[13] != null) {
				model.setDelaydate(java.sql.Date.valueOf(data[13].toString()));
			}
			if (data[14] != null) {
				model.setDelaytype(data[14].toString());
			}
			if (data[15] != null) {
				model.setOpTicket(data[15].toString());
			}
			if (data[16] != null) {
				model.setWorkresult(data[16].toString());
			}
			if (data[17] != null) {
				model.setWorkExplain(data[17].toString());
			}
			if (data[18] != null) {
				model.setIfcheck(data[18].toString());
			}
			if (data[19] != null) {
				model.setCheckdate(java.sql.Date.valueOf(data[19].toString()));
			}
			if (data[20] != null) {
				model.setCheckman(data[20].toString());
			}
			if (data[21] != null) {
				model.setCheckresult(data[21].toString());
			}
			if (data[22] != null) {
				model.setIfimage(data[22].toString());
			}
			if (data[23] != null) {
				model.setImagecode(data[23].toString());
			}
			if (data[24] != null) {
				model.setIfExplain(data[24].toString());
			}
			if (data[25] != null) {
				model.setMemo(data[25].toString());
			}
			if (data[26] != null) {
				model.setOperator(data[26].toString());
			}
			if (data[27] != null) {
				model.setDelingDate(java.sql.Date.valueOf(data[27].toString()));
			}
			if (data[28] != null) {
				model.setDelayDate(java.sql.Date.valueOf(data[28].toString()));
			}
			if (data[29] != null) {
				model.setProtector(data[29].toString());
			}
			if (data[30] != null) {
				model.setIfOpticket(data[30].toString());
			}
			if (data[31] != null) {
				model.setConman(data[31].toString());
			}
			if (data[32] != null) {
				model.setCondate(java.sql.Date.valueOf(data[32].toString()));
			}
			if (data[33] != null) {
				model.setStatus(data[33].toString());
			}
			if (data[34] != null) {
				model.setEnterprisecode(data[34].toString());
			}
			if (data[35] != null) {
				model.setIsUse(data[35].toString());
			}
			if (data[40] != null) {
				omodel.setMachprofName(data[40].toString());
			}
			if (data[41] != null) {
				omodel.setWorktypeName(data[41].toString());
			}
			if (data[42] != null) {
				omodel.setClasssequenceName(data[42].toString());
			}
			if (data[43] != null) {
				omodel.setDutytypeName(data[43].toString());
			}
			if (data[44] != null) {
				omodel.setIfdelayNm(data[44].toString());
			}
			if (data[45] != null) {
				omodel.setDelayresultNm(data[45].toString());
			}
			if (data[46] != null) {
				omodel.setDelaymanName(data[46].toString());
			}
			if (data[47] != null) {
				omodel.setWorkresultNm(data[47].toString());
			}
			if (data[48] != null) {
				omodel.setIfcheckNm(data[48].toString());
			}
			if (data[49] != null) {
				omodel.setCheckmanName(data[49].toString());
			}
			if (data[50] != null) {
				omodel.setCheckresultNm(data[50].toString());
			}
			if (data[51] != null) {
				omodel.setIfimageNm(data[51].toString());
			}
			if (data[52] != null) {
				omodel.setIfexplainNm(data[52].toString());
			}
			if (data[53] != null) {
				omodel.setOperatorName(data[53].toString());
			}
			if (data[54] != null) {
				omodel.setProtectorName(data[54].toString());
			}
			if (data[55] != null) {
				omodel.setIfopticketNm(data[55].toString());
			}
			if (data[56] != null) {
				omodel.setConmanName(data[56].toString());
			}
			if (data[57] != null) {
				omodel.setStatusNm(data[57].toString());
			}
			if (data[58] != null) {
				omodel.setWorkDateNm(data[58].toString());
			}
			if (data[59] != null) {
				omodel.setDelaydateNm(data[59].toString());
			}
			if (data[60] != null) {
				omodel.setCheckdateNm(data[60].toString());
			}
			if (data[61] != null) {
				omodel.setDelingDateNm(data[61].toString());
			}
			if (data[62] != null) {
				omodel.setDelayDateNm(data[62].toString());
			}
			if (data[63] != null) {
				omodel.setCondateNm(data[63].toString());
			}
			omodel.setTimeworkjInfo(model);
			arrlist.add(omodel);
		}
		result.setList(arrlist);
		result.setTotalCount(obj.getTotalCount());
		write(JSONUtil.serialize(result));
	}

	// 运行执行页面数据
	@SuppressWarnings("unchecked")
	public void getlistRun() throws JSONException {
		List<Object[]> list = bll.findRunLogByWorker(employee
				.getEnterpriseCode(), employee.getWorkerCode());
		if (list.size() == 0) {
		} else {
			PageObject obj = (PageObject) remote.getlistRun((Object) ("Y"),
					queryWorkType, queryRunType, start, limit);
			write(JSONUtil.serialize(obj));
		}
	}

	public boolean handup() throws JSONException {
		try {
			RunJTimework model = new RunJTimework();
			model = remote.findById(Long.parseLong(id));
			model.setStatus("2");
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 上报
	public void handupTimeworkj() throws JSONException {
		if (handup()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'上 报 失 败 !'}");
		}
	}

	public boolean operate() throws JSONException {
		try {
			RunJTimework model = new RunJTimework();
			model = remote.findById(Long.parseLong(id));
			model.setOperator(operator);
			model.setProtector(protector);
			model.setWorkDate(new java.util.Date());
			model.setMemo(memo);
			model.setWorkresult(RungridWorkresult);
			model.setWorkExplain(workExplain);
			model.setImagecode(imagecode);
			model.setStatus("7");
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 执行
	public void operateTimeworkj() throws JSONException {
		// if (bothcheckUserInfo()) {
		if (operate()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'执 行 失 败 !'}");
		}
		// } else {
		// write("{sucess:false,eMsg:'密 码 验 证 有 误 !'}");
		// }
	}

	public boolean checkUserInfo() {
		SysCUlFacadeRemote userRemote = (SysCUlFacadeRemote) factory
				.getFacadeRemote("SysCUlFacade");
		return userRemote.checkUserRight(employee.getEnterpriseCode(),
				username, password);
	}

	public boolean bothcheckUserInfo() {
		SysCUlFacadeRemote userRemote = (SysCUlFacadeRemote) factory
				.getFacadeRemote("SysCUlFacade");
		boolean tf = false;
		boolean tt = false;
		tf = userRemote.checkUserRight(employee.getEnterpriseCode(), operator,
				password1);
		tt = userRemote.checkUserRight(employee.getEnterpriseCode(), protector,
				password2);

		return tf && tt;
	}

	public boolean agree() throws JSONException {
		try {
			if (checkUserInfo()) {
				RunJTimework model = new RunJTimework();
				model = remote.findById(Long.parseLong(id));
				model.setCheckdate(new java.util.Date());
				model.setCheckman(username);
				model.setStatus("5");
				model.setCheckresult("1");
				remote.update(model);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean disagree() throws JSONException {
		try {
			if (checkUserInfo()) {
				RunJTimework model = new RunJTimework();
				model = remote.findById(Long.parseLong(id));
				model.setCheckdate(new java.util.Date());
				model.setCheckman(username);
				model.setStatus("3");
				model.setCheckresult("2");
				remote.update(model);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// 同意
	public void agreeTimeworkj() throws JSONException {
		if (agree()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'审 批 失 败 !'}");
		}
	}

	// 不同意
	public void disagreeTimeworkj() throws JSONException {
		if (disagree()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'审 批 失 败 !'}");
		}
	}

	public boolean agreedelay() throws JSONException {
		try {
			if (checkUserInfo()) {
				RunJTimework model = new RunJTimework();
				model = remote.findById(Long.parseLong(id));
				model.setDelaydate(new java.util.Date());
				model.setDelayman(username);
				model.setDelayresult("2");
				remote.update(model);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean disagreedelay() throws JSONException {
		try {
			if (checkUserInfo()) {
				RunJTimework model = new RunJTimework();
				model = remote.findById(Long.parseLong(id));
				model.setDelaydate(new java.util.Date());
				model.setDelayman(username);
				model.setDelayresult("3");
				remote.update(model);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// 同意延期
	public void agreedelayTimeworkj() throws JSONException {
		if (agreedelay()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'审 批 失 败 !'}");
		}
	}

	// 不同意延期
	public void disagreedelayTimeworkj() throws JSONException {
		if (disagreedelay()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'审 批 失 败 !'}");
		}
	}

	// 延期
	public boolean askdelay() throws JSONException {
		try {
			RunJTimework model = new RunJTimework();
			model = remote.findById(Long.parseLong(id));
			model.setDelayresult("1");
			model.setIfdelay("Y");
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 延期
	public void askdelayTimeworkj() throws JSONException {
		if (askdelay()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'申 请 延 期 失 败 !'}");
		}
	}

	// 确认
	public boolean confirm() throws JSONException {
		try {
			RunJTimework model = new RunJTimework();
			model = remote.findById(Long.parseLong(id));
			model.setStatus("3");
			model.setConman(username);
			model.setCondate(new java.util.Date());
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 确认
	public void confirmTimeworkj() throws JSONException {
		if (checkUserInfo()) {
			if (confirm()) {
				write("{success:true,eMsg:''}");
			} else {
				write("{sucess:false,eMsg:'确 认 工 作 操 作 失 败 !'}");
			}
		} else {
			write("{sucess:false,eMsg:'密 码 错 误 !'}");
		}
	}

	// 添加临时定期工作
	protected boolean addTemp() {
		try {
			timeworkInfoTemp.setStatus("0");
			timeworkInfoTemp.setIsUse("Y");
			timeworkInfoTemp.setEnterprisecode(employee.getEnterpriseCode());
			timeworkInfoTemp.setClassTeam("Y");
			remote.saveTemp(timeworkInfoTemp);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void addTempTimework() {
		if (addTemp()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'操 作 失 败 !'}");
		}
	}

	// 编辑临时定期工作
	public void updateTempTimework() {
		write("{sucess:false,eMsg:'操 作 失 败 !'}");
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the remote
	 */
	public RunJTimeworkFacadeRemote getRemote() {
		return remote;
	}

	/**
	 * @param remote
	 *            the remote to set
	 */
	public void setRemote(RunJTimeworkFacadeRemote remote) {
		this.remote = remote;
	}

	/**
	 * @return the queryWorkType
	 */
	public String getQueryWorkType() {
		return queryWorkType;
	}

	/**
	 * @param queryWorkType
	 *            the queryWorkType to set
	 */
	public void setQueryWorkType(String queryWorkType) {
		this.queryWorkType = queryWorkType;
	}

	/**
	 * @return the queryRunType
	 */
	public String getQueryRunType() {
		return queryRunType;
	}

	/**
	 * @param queryRunType
	 *            the queryRunType to set
	 */
	public void setQueryRunType(String queryRunType) {
		this.queryRunType = queryRunType;
	}

	/**
	 * @return the queryApproveType
	 */
	public String getQueryApproveType() {
		return queryApproveType;
	}

	/**
	 * @param queryApproveType
	 *            the queryApproveType to set
	 */
	public void setQueryApproveType(String queryApproveType) {
		this.queryApproveType = queryApproveType;
	}

	/**
	 * @return the queryDelayType
	 */
	public String getQueryDelayType() {
		return queryDelayType;
	}

	/**
	 * @param queryDelayType
	 *            the queryDelayType to set
	 */
	public void setQueryDelayType(String queryDelayType) {
		this.queryDelayType = queryDelayType;
	}

	/**
	 * @return the querystimeDate
	 */
	public String getQuerystimeDate() {
		return querystimeDate;
	}

	/**
	 * @param querystimeDate
	 *            the querystimeDate to set
	 */
	public void setQuerystimeDate(String querystimeDate) {
		this.querystimeDate = querystimeDate;
	}

	/**
	 * @return the queryetimeDate
	 */
	public String getQueryetimeDate() {
		return queryetimeDate;
	}

	/**
	 * @param queryetimeDate
	 *            the queryetimeDate to set
	 */
	public void setQueryetimeDate(String queryetimeDate) {
		this.queryetimeDate = queryetimeDate;
	}

	/**
	 * @return the queryWorkitemName
	 */
	public String getQueryWorkitemName() {
		return queryWorkitemName;
	}

	/**
	 * @param queryWorkitemName
	 *            the queryWorkitemName to set
	 */
	public void setQueryWorkitemName(String queryWorkitemName) {
		this.queryWorkitemName = queryWorkitemName;
	}

	/**
	 * @return the queryMachprofCode
	 */
	public String getQueryMachprofCode() {
		return queryMachprofCode;
	}

	/**
	 * @param queryMachprofCode
	 *            the queryMachprofCode to set
	 */
	public void setQueryMachprofCode(String queryMachprofCode) {
		this.queryMachprofCode = queryMachprofCode;
	}

	/**
	 * @return the queryWorkResult
	 */
	public String getQueryWorkResult() {
		return queryWorkResult;
	}

	/**
	 * @param queryWorkResult
	 *            the queryWorkResult to set
	 */
	public void setQueryWorkResult(String queryWorkResult) {
		this.queryWorkResult = queryWorkResult;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the protector
	 */
	public String getProtector() {
		return protector;
	}

	/**
	 * @param protector
	 *            the protector to set
	 */
	public void setProtector(String protector) {
		this.protector = protector;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the imagecode
	 */
	public String getImagecode() {
		return imagecode;
	}

	/**
	 * @param imagecode
	 *            the imagecode to set
	 */
	public void setImagecode(String imagecode) {
		this.imagecode = imagecode;
	}

	/**
	 * @return the workExplain
	 */
	public String getWorkExplain() {
		return workExplain;
	}

	/**
	 * @param workExplain
	 *            the workExplain to set
	 */
	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}

	/**
	 * @return the rungridWorkresult
	 */
	public String getRungridWorkresult() {
		return RungridWorkresult;
	}

	/**
	 * @param rungridWorkresult
	 *            the rungridWorkresult to set
	 */
	public void setRungridWorkresult(String rungridWorkresult) {
		RungridWorkresult = rungridWorkresult;
	}

	/**
	 * @return the password1
	 */
	public String getPassword1() {
		return password1;
	}

	/**
	 * @param password1
	 *            the password1 to set
	 */
	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	/**
	 * @return the password2
	 */
	public String getPassword2() {
		return password2;
	}

	/**
	 * @param password2
	 *            the password2 to set
	 */
	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	/**
	 * @return the timeworkInfoTemp
	 */
	public RunJTimework getTimeworkInfoTemp() {
		return timeworkInfoTemp;
	}

	/**
	 * @param timeworkInfoTemp
	 *            the timeworkInfoTemp to set
	 */
	public void setTimeworkInfoTemp(RunJTimework timeworkInfoTemp) {
		this.timeworkInfoTemp = timeworkInfoTemp;
	}

}
