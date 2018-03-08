package power.web.run.timework.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.AbstractAction;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.timework.RunCTimework;
import power.ejb.run.timework.RunCTimeworkFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.shift.RunCShiftTime;
import power.ejb.run.runlog.shift.RunCShiftTimeFacadeRemote;
import power.ejb.run.runlog.shift.RunCUnitprofession;
import power.ejb.run.runlog.shift.RunCUnitprofessionFacadeRemote;
import power.ejb.run.runlog.shift.RunCShiftInitial;
import power.ejb.run.runlog.shift.RunCShiftInitialFacadeRemote;

public class TimeworkAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String method;
	protected String classSequence;
	protected String CenterMachprofCode;
	protected String CenterWorkType;
	protected String id;
	protected Date startimeDate;
	protected String ids;

	/**
	 * @return 传输过来的方法
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the classSequence
	 */
	public String getClassSequence() {
		return classSequence;
	}

	/**
	 * @param classSequence
	 *            the classSequence to set
	 */
	public void setClassSequence(String classSequence) {
		this.classSequence = classSequence;
	}

	protected RunCTimework timeworkInfo;

	/**
	 * @return the timeworkInfo
	 */
	public RunCTimework getTimeworkInfo() {
		return timeworkInfo;
	}

	/**
	 * @param timeworkInfo
	 *            the timeworkInfo to set
	 */
	public void setTimeworkInfo(RunCTimework timeworkInfo) {
		this.timeworkInfo = timeworkInfo;
	}

	/**
	 * @return the centerMachprofCode查询用 专业
	 */
	public String getCenterMachprofCode() {
		return CenterMachprofCode;
	}

	/**
	 * @param centerMachprofCode
	 *            the centerMachprofCode to set
	 */
	public void setCenterMachprofCode(String centerMachprofCode) {
		CenterMachprofCode = centerMachprofCode;
	}

	/**
	 * @return the centerWorkType查询用 工作类型
	 */
	public String getCenterWorkType() {
		return CenterWorkType;
	}

	/**
	 * @param centerWorkType
	 *            the centerWorkType to set
	 */
	public void setCenterWorkType(String centerWorkType) {
		CenterWorkType = centerWorkType;
	}

	protected RunCTimeworkFacadeRemote remote;
	protected RunCUnitprofessionFacadeRemote dll;
	protected RunCShiftInitialFacadeRemote sll;
	protected RunCShiftTimeFacadeRemote tremote;

	public TimeworkAction() {
		remote = (RunCTimeworkFacadeRemote) factory
				.getFacadeRemote("RunCTimeworkFacade");
		dll = (RunCUnitprofessionFacadeRemote) factory
				.getFacadeRemote("RunCUnitprofessionFacade");
		sll = (RunCShiftInitialFacadeRemote) factory
				.getFacadeRemote("RunCShiftInitialFacade");
		tremote = (RunCShiftTimeFacadeRemote) factory
				.getFacadeRemote("RunCShiftTimeFacade");
	}

	// 取运行专业
	@SuppressWarnings("unchecked")
	public void useprolist() throws Exception {
		List<RunCSpecials> list = dll.findUnitPList(employee
				.getEnterpriseCode());
		String str = "[";
		int i = 0;
		for (RunCSpecials model : list) {
			i++;
			str += "[\"" + model.getSpecialityName() + "\",\""
					+ model.getSpecialityCode() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
	}

	// 根据专业取班次
	public void findShfitTimeBySpecial() throws Exception {
		String spcode = request.getParameter("spcode");
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String sd = sf.format(date);
		List<RunCShiftInitial> initialList = sll.findListByDate(spcode,
				employee.getEnterpriseCode(), sd, sd);
		if (initialList.isEmpty()) {
			write("[]");
		} else {
			RunCShiftInitial model = initialList.get(0);
			List<RunCShiftTime> list = tremote.findTimeListByNoEr(model
					.getInitialNo(), model.getEnterpriseCode());
			String str = "[";
			int i = 0;
			for (RunCShiftTime cmodel : list) {
				i++;
				str += "[\"" + cmodel.getShiftTimeName() + "\",\""
						+ cmodel.getShiftTimeId() + "\"]";
				if (i < list.size()) {
					str += ",";
				}
			}
			str += "]";
			write(str);
		}
	}

	protected String cyclesquenceString() {
		String str = "";
		String classSequencestr = getClassSequence();
		int caseid = Integer.parseInt(timeworkInfo.getWorkRangeType());
		switch (caseid) {
		case 1:
			str = "每日 " + classSequencestr;
			break;
		case 2:
			str = "每月 ";
			if (timeworkInfo.getWeekNo() != null) {
				if (!timeworkInfo.getWeekNo().toString().equals("0")) {
					str += "第" + timeworkInfo.getWeekNo().toString() + "周 ";
					if (!timeworkInfo.getWeekDay().toString().equals("1")) {
						str += "周"
								+ (Integer.parseInt(timeworkInfo.getWeekDay()
										.toString()) - 1) + " ";
					} else {
						str += "周日 ";
					}
				} else {
					str += timeworkInfo.getWeekDay().toString() + "号 ";
				}
			} else {
				str += timeworkInfo.getWeekDay().toString() + "号 ";
			}
			str += classSequencestr;
			break;
		case 3:
			str = "每周 ";
			if (!timeworkInfo.getWeekDay().toString().equals("1")) {
				str += "周"
						+ (Integer.parseInt(timeworkInfo.getWeekDay()
								.toString()) - 1) + " ";
			} else {
				str += "周日 ";
			}
			str += classSequencestr;
			break;
		case 4:
			str = "每班 ";
			break;
		case 5:
			str = "偶数日 " + classSequencestr;
			break;
		case 6:
			str = "奇数日 " + classSequencestr;
			break;
		case 7:
			str = "每隔" + timeworkInfo.getCycleNumber().toString() + "日 "
					+ classSequencestr;
			break;
		case 8:
			str = "双休日 " + classSequencestr;
			break;
		case 9:
			str = "每隔" + timeworkInfo.getCycleNumber().toString() + "个月 ";
			if (timeworkInfo.getWeekNo() != null) {
				if (!timeworkInfo.getWeekNo().toString().equals("0")) {
					str += "第" + timeworkInfo.getWeekNo().toString() + "周 ";
					if (!timeworkInfo.getWeekDay().toString().equals("1")) {
						str += "周"
								+ (Integer.parseInt(timeworkInfo.getWeekDay()
										.toString()) - 1) + " ";
					} else {
						str += "周日 ";
					}
				} else {
					str += timeworkInfo.getWeekDay().toString() + "号 ";
				}
			} else {
				str += timeworkInfo.getWeekDay().toString() + "号 ";
			}
			str += classSequencestr;
			break;
		default:
			str = "未定义的周期类型";
		}
		return str;
	}

	// 添加记录
	protected boolean insert() throws Exception {
		try {
			timeworkInfo.setOperator(null);// 操作人
			timeworkInfo.setProtector(null);// 保护人
			timeworkInfo.setCycle(null);// 暂时没用
			timeworkInfo.setCycleSequence(cyclesquenceString());// 周期的文字描述
			timeworkInfo.setStatus("C");// 状态
			timeworkInfo.setEnterprisecode(employee.getEnterpriseCode());// 企业编码
			timeworkInfo.setIsUse("Y");// 删除控制
			if (remote.save(timeworkInfo) > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// 添加操作
	public void addTimework() throws Exception {
		if (insert()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{success:false,eMsg:'添 加 失 败 !'}");
		}
	}

	// 修改记录
	protected boolean update() {
		try {
			RunCTimework model = new RunCTimework();
			model = remote.findById(Long.parseLong(id));
			model.setWorkType(timeworkInfo.getWorkType());// 工作类型
			model.setWorkItemName(timeworkInfo.getWorkItemName());// 工作名称
			model.setMachprofCode(timeworkInfo.getMachprofCode());// 专业编码
			model.setWorkRangeType(timeworkInfo.getWorkRangeType());// 周期类别
			model.setCycleNumber(timeworkInfo.getCycleNumber());// 周期数值
			model.setWeekNo(timeworkInfo.getWeekNo());// 第几周
			model.setWeekDay(timeworkInfo.getWeekDay());// 第几天
			model.setClassSequence(timeworkInfo.getClassSequence());// 班次
			model.setCycleSequence(cyclesquenceString());// 周期的文字描述
			model.setStartTime(timeworkInfo.getStartTime());// 开始时间
			model.setDelaytype(timeworkInfo.getDelaytype());// 顺延类型
			model.setMemo(timeworkInfo.getMemo());// 备注
			model.setImportantlvl(timeworkInfo.getImportantlvl());// 重要程度
			model.setWorkExplain(timeworkInfo.getWorkExplain());// 试验说明
			model.setIfexplain(timeworkInfo.getIfexplain());// 工作说明是否必填
			model.setIfimage(timeworkInfo.getIfimage());// 是否要做事故预想
			model.setIfcheck(timeworkInfo.getIfcheck());// 是否要审批
			model.setIftest(timeworkInfo.getIftest());// 是否要试验
			model.setIfopticket(timeworkInfo.getIfopticket());// 是否关联操作票
			model.setOpticketCode(timeworkInfo.getOpticketCode());// 所关联的操作票票号
			remote.update(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 修改操作
	public void updateTimework() {
		if (update()) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
			write("{success:true,eMsg:'',workitemnameMsg:'"
					+ timeworkInfo.getWorkItemName() + "',startimeMsg:'"
					+ timeFormat.format(timeworkInfo.getStartTime()) + "'}");
		} else {
			write("{sucess:false,eMsg:'修 改 失 败 !'}");
		}
	}

	// 锁定记录
	protected boolean lock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimework model = new RunCTimework();
				model = remote.findById(id[i]);
				model.setStatus("L");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 锁定操作
	public void lockTimework() {
		if (lock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'锁 定 失 败 !'}");
		}
	}

	// 解锁记录
	protected boolean unlock() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimework model = new RunCTimework();
				model = remote.findById(id[i]);
				model.setStatus("C");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 解锁操作
	public void unlockTimework() {
		if (unlock()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'解 锁 失 败 !'}");
		}
	}

	// 删除记录
	protected boolean delete() {
		try {
			String[] str = ids.split(",");
			Long[] id = new Long[str.length];
			for (int i = 0; i < str.length; i++) {
				id[i] = Long.parseLong(str[i]);
				RunCTimework model = new RunCTimework();
				model = remote.findById(id[i]);
				model.setIsUse("N");
				remote.update(model);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 删除操作
	public void deleteTimework() {
		if (delete()) {
			write("{success:true,eMsg:''}");
		} else {
			write("{sucess:false,eMsg:'删 除 失 败 !'}");
		}
	}

	private int start;
	private int limit;

	// 取定期工作列表
	public void getlistTimework() throws Exception {
		PageObject obj = remote.findByIsUse((Object) ("Y"), CenterWorkType,
				CenterMachprofCode, start, limit);
		write(JSONUtil.serialize(obj));
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
	 * @return the startimeDate
	 */
	public Date getStartimeDate() {
		return startimeDate;
	}

	/**
	 * @param startimeDate
	 *            the startimeDate to set
	 */
	public void setStartimeDate(Date startimeDate) {
		this.startimeDate = startimeDate;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

}
