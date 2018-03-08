package power.web.run.timework.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import power.web.comm.AbstractAction;
import power.ejb.run.timework.RunCTimework;
import power.ejb.run.timework.RunCTimeworkd;
import power.ejb.run.timework.RunCTimeworkdFacadeRemote;
import power.ejb.run.timework.RunJTimework;
import power.ejb.run.timework.RunCTimeworkFacadeRemote;
import power.ejb.run.timework.RunJTimeworkFacadeRemote;

public class TimeworkGenerateAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected RunCTimeworkFacadeRemote cll;
	protected RunCTimeworkdFacadeRemote dll;
	protected RunJTimeworkFacadeRemote jll;

	// 取周期计算相关数据

	// 取几月
	protected Long monthofyear(String runlogID) throws ParseException {
		String datetime = runlogID.substring(0, 8);
		DateFormat today = new SimpleDateFormat("yyyyMMdd");
		Date value = today.parse(datetime);

		SimpleDateFormat timeFormat = new SimpleDateFormat("M");
		Long result = Long.parseLong(timeFormat.format(value));
		return result;
	}

	// 取这个月第几周
	protected Long weekofmonth(String runlogID) throws ParseException {
		String datetime = runlogID.substring(0, 8);
		DateFormat today = new SimpleDateFormat("yyyyMMdd");
		Date value = today.parse(datetime);

		SimpleDateFormat timeFormat = new SimpleDateFormat("W");
		Long result = Long.parseLong(timeFormat.format(value));
		return result;
	}

	// 取几号
	protected Long dayofmonth(String runlogID) throws ParseException {
		String datetime = runlogID.substring(0, 8);
		DateFormat today = new SimpleDateFormat("yyyyMMdd");
		Date value = today.parse(datetime);

		SimpleDateFormat timeFormat = new SimpleDateFormat("d");
		Long result = Long.parseLong(timeFormat.format(value));
		return result;
	}

	// 取周几
	protected Long dayofweek(String runlogID) throws ParseException {
		String datetime = runlogID.substring(0, 8);
		DateFormat today = new SimpleDateFormat("yyyyMMdd");
		Date value = today.parse(datetime);
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(value);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		long result = dayOfWeek;
		return result;
	}

	// 取模拟出来的日期
	protected Date today(String runlogID) {
		String datetime = runlogID.substring(0, 8);
		DateFormat today = new SimpleDateFormat("yyyyMMdd");
		Date value = null;
		try {
			value = today.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}

	public TimeworkGenerateAction() {
		cll = (RunCTimeworkFacadeRemote) factory
				.getFacadeRemote("RunCTimeworkFacade");
		dll = (RunCTimeworkdFacadeRemote) factory
				.getFacadeRemote("RunCTimeworkdFacade");
		jll = (RunJTimeworkFacadeRemote) factory
				.getFacadeRemote("RunJTimeworkFacade");
	}

	// ---------------------- 利用java的方法实现,做操作日志方便 ----------------------

	// 执行定期工作列表生成
	public void generateTimeworklist() {
		// 验证时间差
		// int i, j;
		// Date startday = today("200812122");
		// Date endday = today("200811122");
		// i = daygap(startday, endday);
		// j = monthgap(startday, endday);
		// @SuppressWarnings("unused")
		// String str;
		// str = "相隔天数" + i + "相隔月数" + j;

		// 测试生成
		// generateTimework("ZZ", 2l, 2l, "hfdc", "200812191");
	}

	public void generateTimework(String spcode, Long shifttimeid, Long shiftid,
			String enterpriseCode, String runlogID) {
		int[] i = new int[2];
		int[] j = new int[2];

		try {
			i = generateMain(spcode, shifttimeid, shiftid, enterpriseCode,
					runlogID);
			j = generateKid(spcode, shifttimeid, shiftid, enterpriseCode,
					runlogID);
			@SuppressWarnings("unused")
			String str;
			str = "成功生成满足条件的记录:" + (i[0] + j[0]) + "条<br>" + "忽略掉不满足条件的记录"
					+ (i[1] + j[1]) + "条";
			// write(str);
			// main(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 計算相隔的天數
	protected int daygap(Date startday, Date endday) {
		// 确保startday在endday之前
		if (startday.after(endday)) {
			Date cal = startday;
			startday = endday;
			endday = cal;
		}
		// 分别得到两个时间的毫秒数
		long sl = startday.getTime();
		long el = endday.getTime();

		long ei = el - sl;
		// 根据毫秒数计算间隔天数
		return (int) (ei / (1000 * 60 * 60 * 24));
	}

	// 计算相隔的月数
	protected int monthgap(Date startdate, Date enddate) {
		if (startdate.after(enddate)) {
			Date cal = startdate;
			startdate = enddate;
			enddate = cal;
		}
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		int smonth, emonth, syear, eyear;
		syear = Integer.parseInt(yearFormat.format(startdate));
		eyear = Integer.parseInt(yearFormat.format(enddate));
		smonth = Integer.parseInt(monthFormat.format(startdate));
		emonth = Integer.parseInt(monthFormat.format(enddate));
		return (eyear - syear) * 12 + emonth - smonth;
	}

	// 对周期进行判定
	protected boolean ifdo(String worktyperange, Date startdate,
			Long cyclenumber, Long weekno, Long weekday, String runlogID)
			throws ParseException {
		Long weekofmonth = weekofmonth(runlogID);
		Long dayofweek = dayofweek(runlogID);
		Long dayofmonth = dayofmonth(runlogID);
		Date today = today(runlogID);
		if (weekno == null) {
			weekno = 0l;
		}
		switch (Integer.parseInt(worktyperange)) {
		case 1:
			return true;
		case 2:
			if (weekno != 0) {
				return (weekofmonth.equals(weekno) && dayofweek.equals(weekday)) ? true
						: false;
			} else {
				return (dayofmonth.equals(weekday)) ? true : false;
			}
		case 3:
			if (dayofweek.equals(weekday)) {
				return true;
			} else {
				return false;
			}
		case 4:
			return true;
		case 5:
			if (dayofmonth % 2 == 0) {
				return true;
			} else {
				return false;
			}
		case 6:
			if (dayofmonth % 2 == 1) {
				return true;
			} else {
				return false;
			}
		case 7:
			if (daygap(startdate, today) % (cyclenumber + 1) == 0) {
				return true;
			} else {
				return false;
			}
		case 8:
			if (dayofweek == 1 || dayofweek == 7) {
				return true;
			} else {
				return false;
			}
		case 9:
			if (monthgap(startdate, today) % (cyclenumber + 1) == 0) {
				if (dayofmonth.equals(weekday) && weekno == 0) {
					return true;
				} else if (weekofmonth.equals(weekno)
						&& dayofweek.equals(weekday)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		default:
			break;
		}
		return true;
	}

	// 取定期工作运行列表主表
	protected int[] generateMain(String spcode, Long shifttimeid, Long shiftid,
			String enterpriseCode, String runlogID) throws Exception {
		int[] backvalue = new int[2];
		Date today = today(runlogID);

		int i = 0;// 计算有多少条记录被执行插入
		int j = 0;// 计算有多少条记录被跳过插入
		List<RunCTimework> list = cll.findToGenerate(enterpriseCode);
		for (RunCTimework cmodel : list) {
			if (spcode.equals(cmodel.getMachprofCode())) {
				if (shifttimeid.equals(cmodel.getClassSequence())
						|| (cmodel.getClassSequence() == null)) {
					if (cmodel.getStartTime().compareTo(today) <= 0) {
						if (ifdo(cmodel.getWorkRangeType(), cmodel
								.getStartTime(), cmodel.getCycleNumber(),
								cmodel.getWeekNo(), cmodel.getWeekDay(),
								runlogID)) {
							RunJTimework jmodel = new RunJTimework();
							jmodel.setCheckdate(null);// 审批时间

							jmodel.setStatus("0");// 0,不需上报;1,未上报;2,已上报;3,已结束;4,已发送;5,已下发.
							// if (("Y").equals(cmodel.getIfcheck())) {
							// jmodel.setCheckresult("0");// 审批结果
							// // 0,未审批;1,同意;2,不同意
							// jmodel.setStatus("1");// 定期工作状态
							// // 0,不需上报;1,未上报;2,已上报;3,已结束;4,已发送;5,已下发.
							// } else {
							// jmodel.setCheckresult(null);
							// jmodel.setStatus("0");// 定期工作状态
							// // 0,不需上报;1,未上报;2,已上报;3,已结束;4,已发送;5,已下发.
							// }

							jmodel.setClassSequence(shifttimeid);// 班次
							jmodel.setIfdelay("N");
							jmodel.setClassTeam(null);// 是否临时下发
							jmodel.setCycle(cmodel.getWorkRangeType());// 周期类型
							jmodel.setDelayDate(null);// 判断已经延期的时间
							jmodel.setDelaytype(null);// 延迟类别
							jmodel.setDelingDate(null);// 判断逾期中的时间
							jmodel.setDutytype(shiftid);// 值别
							jmodel
									.setEnterprisecode(cmodel
											.getEnterprisecode());// 企业编码
							jmodel.setIfcheck(cmodel.getIfcheck());// 是否需要审批
							jmodel.setIfExplain(cmodel.getIfexplain());// 是否需要说明
							jmodel.setIfimage(cmodel.getIfimage());// 是否做事故预想
							jmodel.setIfOpticket(cmodel.getIfopticket());// 是否关联操作票
							jmodel.setIsUse("Y");// 删除字段
							jmodel.setMachprofCode(cmodel.getMachprofCode());// 专业编码
							jmodel.setMemo(cmodel.getMemo());// 备注
							jmodel.setOperator(null);// 操作人
							jmodel.setOpTicket(cmodel.getOpticketCode());// 操作票票号
							jmodel.setProtector(null);// 监护人
							jmodel.setWorkDate(new java.util.Date());// 操作时间
							jmodel.setWorkExplain(cmodel.getWorkExplain());// 工作说明
							jmodel.setWorkItemCode(cmodel.getWorkItemCode());// 定期工作的编号
							jmodel.setWorkItemName(cmodel.getWorkItemName());// 定期工作内容
							jmodel.setWorkresult(null);// 工作结果
							// 1,正常;2,不正常;3,因故取消;4,取消
							jmodel.setWorkType(cmodel.getWorkType());// 工作类型
							jll.save(jmodel);
							i++;
						} else {
							j++;
						}
					} else {
						j++;
					}
				} else {
					j++;
				}
			} else {
				j++;
			}
		}
		backvalue[0] = i;
		backvalue[1] = j;
		return backvalue;
	}

	protected boolean ifdokid(Long month, Long weekno, Long day, String runlogID)
			throws ParseException {
		Long monthofyear = monthofyear(runlogID);
		Long weekofmonth = weekofmonth(runlogID);
		Long dayofweek = dayofweek(runlogID);
		Long dayofmonth = dayofmonth(runlogID);
		if (weekno == null) {
			weekno = 0l;
		}
		if (monthofyear.equals(month)) {
			if (weekno != 0) {
				return (weekofmonth.equals(weekno) && dayofweek.equals(day)) ? true
						: false;
			} else {
				return (dayofmonth.equals(day)) ? true : false;
			}
		} else {
			if (weekno != 0) {
				return (weekofmonth.equals(weekno) && dayofweek.equals(day)) ? true
						: false;
			} else {
				return (dayofmonth.equals(day)) ? true : false;
			}
		}
	}

	// 取定期工作运行列表子表
	protected int[] generateKid(String spcode, Long shifttimeid, Long shiftid,
			String enterpriseCode, String runlogID) throws ParseException {
		int[] backvalue = new int[2];
		Date today = today(runlogID);

		int i = 0;// 计算有多少条记录被执行插入
		int j = 0;// 计算有多少条记录被跳过插入
		List<RunCTimeworkd> list = dll.findToGenerate(enterpriseCode);
		for (RunCTimeworkd dmodel : list) {
			RunCTimework cmodel = new RunCTimework();
			cmodel = cll.findByWorkItemCode(dmodel.getWorkItemCode());
			if (spcode.equals(cmodel.getMachprofCode())) {
				if (shifttimeid.equals(dmodel.getClassSequence())
						|| (dmodel.getClassSequence() == null)) {
					if (cmodel.getStartTime().compareTo(today) <= 0) {
						if (ifdokid(dmodel.getMonth(), dmodel.getWeekNo(),
								dmodel.getTestDay(), runlogID)) {
							RunJTimework jmodel = new RunJTimework();
							jmodel.setCheckdate(null);// 审批时间

							jmodel.setStatus("0");// 0,不需上报;1,未上报;2,已上报;3,已结束;4,已发送;5,已下发.
							// if (("Y").equals(cmodel.getIfcheck())) {
							// jmodel.setCheckresult("0");// 审批结果
							// // 0,未审批;1,同意;2,不同意
							// jmodel.setStatus("1");// 定期工作状态
							// // 0,不需上报;1,未上报;2,已上报;3,已结束;4,已发送;5,已下发.
							// } else {
							// jmodel.setCheckresult(null);
							// jmodel.setStatus("0");// 定期工作状态
							// // 0,不需上报;1,未上报;2,已上报;3,已结束;4,已发送;5,已下发.
							// }

							jmodel.setClassSequence(shifttimeid);// 班次
							jmodel.setIfdelay("N");
							jmodel.setClassTeam(null);// 是否临时下发
							jmodel.setCycle(cmodel.getWorkRangeType());// 周期类型
							jmodel.setDelayDate(null);// 判断已经延期的时间
							jmodel.setDelaytype(null);// 延迟类别
							jmodel.setDelingDate(null);// 判断逾期中的时间
							jmodel.setDutytype(shiftid);// 值别
							jmodel
									.setEnterprisecode(cmodel
											.getEnterprisecode());// 企业编码
							jmodel.setIfcheck(cmodel.getIfcheck());// 是否需要审批
							jmodel.setIfExplain(cmodel.getIfexplain());// 是否需要说明
							jmodel.setIfimage(cmodel.getIfimage());// 是否做事故预想
							jmodel.setIfOpticket(cmodel.getIfopticket());// 是否关联操作票
							jmodel.setIsUse("Y");// 删除字段
							jmodel.setMachprofCode(cmodel.getMachprofCode());// 专业编码
							jmodel.setMemo(cmodel.getMemo());// 备注
							jmodel.setOperator(null);// 操作人
							jmodel.setOpTicket(cmodel.getOpticketCode());// 操作票票号
							jmodel.setProtector(null);// 监护人
							jmodel.setWorkDate(new java.util.Date());// 操作时间
							jmodel.setWorkExplain(cmodel.getWorkExplain());// 工作说明
							jmodel.setWorkItemCode(cmodel.getWorkItemCode());// 定期工作的编号
							jmodel.setWorkItemName(cmodel.getWorkItemName());// 定期工作内容
							jmodel.setWorkresult(null);// 工作结果
							// 1,正常;2,不正常;3,因故取消;4,取消
							jmodel.setWorkType(cmodel.getWorkType());// 工作类型
							jll.save(jmodel);
							i++;
						} else {
							j++;
						}
					} else {
						j++;
					}
				} else {
					j++;
				}
			} else {
				j++;
			}
		}

		backvalue[0] = i;
		backvalue[1] = j;
		return backvalue;
	}

	protected long insert() {
		RunJTimework model = new RunJTimework();
		return jll.save(model);
	}

	// ---------------------- 利用纯SQL的方法实现,效率高 ----------------------

}
