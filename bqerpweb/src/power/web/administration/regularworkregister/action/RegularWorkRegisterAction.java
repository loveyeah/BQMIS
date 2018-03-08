/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.regularworkregister.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import power.ear.comm.DataChangeException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJTimework;
import power.ejb.administration.AdJTimeworkFacadeRemote;
import power.ejb.administration.business.AdCTimeWorkAllFacadeRemote;
import power.ejb.administration.business.RegularWorkRegisterFacadeRemote;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.comm.ComAdCRight;
import power.ejb.administration.form.AdCTimeWorkAllInfo;
import power.ejb.administration.form.AllRegularWorkInfo;
import power.ejb.administration.form.RegularWorkRegisterInfo;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.CodeConstants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物资出门登记action
 * 
 * @author daichunlin
 * @version 1.0
 */
public class RegularWorkRegisterAction extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 定期工作画面entity */
	private RegularWorkRegisterInfo regularWorkRegisterInfo;
	/** 定期工作登记表entity */
	private AdJTimework adJTimework;
	/** 定期工作维护表entity */
	private AdCTimeWorkAllInfo adCTimework;
	/** 定期工作画面remote */
	private RegularWorkRegisterFacadeRemote typeRemote;
	/** 定期工作登记表remote */
	private AdJTimeworkFacadeRemote adJTimeworkFacadeRemote;
	/** 定期工作维护表remote */
	private AdCTimeWorkAllFacadeRemote adCTimeworkFacadeRemote;
	/** 行政管理共通远程接口 Remote */
	private ADCommonFacadeRemote adCommonFacadeRemote;
	/** 画面参数开始页 */
	public Long start;
	/** 画面参数页面最大值 */
	public Long limit;
	/** 画面参数区分 */
	public Long flag;
	/** 画面查询参数 */
	public String fuzzy;
	/** 获得要更新的数据* */
	public String strUpdate;

	/**
	 * @return 获得要更新的数据
	 */
	public String getStrUpdate() {
		return strUpdate;
	}

	/**
	 * @param 获得要更新的数据
	 */
	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}

	/**
	 * @return 画面查询参数
	 */
	public String getFuzzy() {
		return fuzzy;
	}

	/**
	 * @param 画面查询参数
	 */
	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}

	/**
	 * @return 开始页
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * @param 开始页
	 */
	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * @return 页面最大值
	 */
	public Long getLimit() {
		return limit;
	}

	/**
	 * @param 页面最大值
	 */
	public void setLimit(Long limit) {
		this.limit = limit;
	}

	/**
	 * @param 定期工作画面
	 */
	public RegularWorkRegisterInfo getRegularWorkRegisterInfo() {
		return regularWorkRegisterInfo;
	}

	/**
	 * @param 定期工作画面
	 */
	public void setRegularWorkRegisterInfo(
			RegularWorkRegisterInfo regularWorkRegisterInfo) {
		this.regularWorkRegisterInfo = regularWorkRegisterInfo;
	}

	/**
	 * @param 定期工作登记表
	 */
	public AdJTimework getAdJTimework() {
		return adJTimework;
	}

	/**
	 * @param 定期工作登记表
	 */
	public void setAdJTimework(AdJTimework adJTimework) {
		this.adJTimework = adJTimework;
	}

	/**
	 * @return 画面参数区分
	 */
	public Long getFlag() {
		return flag;
	}

	/**
	 * @param 画面参数区分
	 */
	public void setFlag(Long flag) {
		this.flag = flag;
	}

	/**
	 * @return 定期工作维护表
	 */
	public AdCTimeWorkAllInfo getAdCTimework() {
		return adCTimework;
	}

	/**
	 * @param 定期工作维护表
	 */
	public void setAdCTimework(AdCTimeWorkAllInfo adCTimework) {
		this.adCTimework = adCTimework;
	}

	/**
	 * 构造函数
	 */
	public RegularWorkRegisterAction() {
		typeRemote = (RegularWorkRegisterFacadeRemote) factory
				.getFacadeRemote("RegularWorkRegisterFacade");
		adCTimeworkFacadeRemote = (AdCTimeWorkAllFacadeRemote) factory
				.getFacadeRemote("AdCTimeWorkAllFacade");
		adJTimeworkFacadeRemote = (AdJTimeworkFacadeRemote) factory
				.getFacadeRemote("AdJTimeworkFacade");
		adCommonFacadeRemote = (ADCommonFacadeRemote) factory
				.getFacadeRemote("ADCommonFacade");
	}

	/**
	 * 定期工作登记页面加载
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getRegisterList() throws JSONException, ParseException {
		LogUtil.log("Action:定期工作登记页面加载开始", Level.INFO, null);
		try {
			// 页面显示
			int intStart = Integer.parseInt(start.toString());
			int intLimit = Integer.parseInt(limit.toString());
			// 取得查询参数: flag
			int intflag = 2;
			if (flag != null) {
				intflag = Integer.parseInt(flag.toString());
			}else{
				intflag = 0;
			}
			// 添加人
			employee = (Employee) session.getAttribute("employee");			
			String strUserID = employee.getWorkerCode().toString();
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 工作类别取得
			PageObject pobjWorkType = adCommonFacadeRemote
					.getUserRight(strUserID,employee.getEnterpriseCode());
			List listWorkType = pobjWorkType.getList();
			String strWorkType = "";
			if (listWorkType.size() > 0) {
				ComAdCRight dataWorkType = (ComAdCRight) listWorkType.get(0);
				strWorkType = dataWorkType.getWorktypeCode();
			}
			// 初期化
			if (intflag == 0) {
				PageObject obj = typeRemote.getRegisterList(strEnterpriseCode,strWorkType,
						intStart, intLimit);
				// 查询结果为null,设置页面显示
				if (obj.getTotalCount() < 0) {
					String str = "{\"list\":[],\"totalCount\":0}";
					write(str);
				} else {
					String str = JSONUtil.serialize(obj);
					write(str);
				}
				LogUtil.log("Action:定期工作登记页面加载结束", Level.INFO, null);

			} else {
				LogUtil.log("Action:定期工作登记登记当天工作开始", Level.INFO, null);
				// 取得定期工作登记数据
				PageObject obj = typeRemote.getAllRegularWorkList(strEnterpriseCode,strWorkType);
				// 系统当前日期
				Date sysDate = new Date();
				DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String date = simpleDate.format(sysDate);
				// 取得定期工作登记数据list
				List list = obj.getList();
				Iterator it = list.iterator();

				List<AllRegularWorkInfo> arrlist = new ArrayList<AllRegularWorkInfo>();
				while (it.hasNext()) {
					AllRegularWorkInfo data = (AllRegularWorkInfo) it.next();
					String workRangeType = data.getWorkRangeType();
					int workType = 100;
					// 周期类别 0 没有设置
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_0)) {
						workType = 0;
					}
					// 周期类别 1 每日
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_1)) {
						workType = 1;
					}
					// 周期类别 2 隔日
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_2)) {
						workType = 2;
					}
					// 周期类别 3 每周
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_3)) {
						workType = 3;
					}
					// 周期类别 4 隔周
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_4)) {
						workType = 4;
					}
					// 周期类别 5 每月
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_5)) {
						workType = 5;
					}
					// 周期类别 6 隔月
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_6)) {
						workType = 6;
					}
					// 周期类别 7 隔N天
					if (workRangeType.equals(CodeConstants.CYCLE_TYPE_7)) {
						workType = 7;
					}
					switch (workType) {
					case 0:
						if (data.getStartTime().substring(0, 10).equals(date)) {
							arrlist.add(data);
							break;
						}
						break;
					case 1:
						arrlist.add(data);
						break;
					case 2:
						if (data.getStartTime().substring(0, 10).equals(date)) {
							arrlist.add(data);
							break;
						} else {
							// 定期工作开始时间
							Calendar rd1 = Calendar.getInstance();
							rd1.setTime(simpleDate.parse(data.getStartTime()));
							Date newStartDate1 = rd1.getTime();
							// 系统时间
							Calendar sd1 = Calendar.getInstance();
							sd1.setTime(simpleDate.parse(date));
							// 时间增量
							int dateNum1 = 2;
							do {
								rd1.add(Calendar.DATE, dateNum1);
								newStartDate1 = rd1.getTime();
								if (simpleDate.format(newStartDate1).toString()
										.equals(date)) {
									arrlist.add(data);
									break;
								}
							} while (sd1.getTime().compareTo(newStartDate1) > 0);
							break;
						}
					case 3:
						// 系统星期
						Calendar sd2 = Calendar.getInstance();
						sd2.setTime(new Date());
						Date sdDate = sd2.getTime();
						int sysWeek = sd2.get(Calendar.DAY_OF_WEEK) - 1;
						// 定期工作的星期
						Calendar rd2 = Calendar.getInstance();
						rd2.setTime(simpleDate.parse(data.getStartTime()));
						Date rdDate = rd2.getTime();						
						int regWeek = Integer.parseInt(data.getRangeNumber());
						if(regWeek == 7){
							regWeek = 0;
						}
						// 是否相等
						if ((sdDate.compareTo(rdDate) > 0)
								&& regWeek == sysWeek) {
							arrlist.add(data);
							break;
						}
						break;
					case 4:
						// 系统星期
						Calendar sd3 = Calendar.getInstance();
						sd3.setTime(new Date());
						int sysWeek3 = sd3.get(Calendar.DAY_OF_WEEK) - 1;
						DateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
						String sysDate3 = format3.format(sd3.getTime())
								.substring(0, 10);
						// 定期工作的星期
						Calendar rd3 = Calendar.getInstance();
						rd3.setTime(simpleDate.parse(data.getStartTime()));
						int rdWeekrd = rd3.get(Calendar.DAY_OF_WEEK) - 1;
//						String newDate3 = format3.format(rd3.getTime())
//								.substring(0, 10);
						Date newStartDate3 = rd3.getTime();
						int rdWeek3 = Integer.parseInt(data.getRangeNumber());					
						// 星期增量
						int weekNum = 0;
						if(rdWeek3 > rdWeekrd){
							weekNum = rdWeek3 - rdWeekrd;
						}
						if(rdWeek3 < rdWeekrd){
							weekNum = 7 - (rdWeekrd - rdWeek3);
						}
						if (rdWeek3 == 7) {
							rdWeek3 = 0;
						}
						// 安排工作的时间
						rd3.add(Calendar.DATE, weekNum);
						String newDate3 = format3.format(rd3.getTime())
						.substring(0, 10);
						// 时间增量
						int dateNum2 = 14;
						// 是否相等

						do {
							rd3.add(Calendar.DATE, dateNum2);
							newStartDate3 = rd3.getTime();
							String new3 = format3.format(newStartDate3)
									.substring(0, 10);
							// 是否相等
							if (rdWeek3 == sysWeek3
									&& (sysDate3.equals(newDate3) || sysDate3.equals(new3))) {
								arrlist.add(data);
								break;
							}
						} while (sd3.getTime().compareTo(newStartDate3) > 0);
						break;

					case 5:
						// 系统日期
						Calendar sd4 = Calendar.getInstance();
						sd4.setTime(new Date());
						int sysDay4 = sd4.get(Calendar.DAY_OF_MONTH);
						// 定期工作的日期
						Calendar rd4 = Calendar.getInstance();
						rd4.setTime(simpleDate.parse(data.getStartTime()));
						Date newStartDate4 = rd4.getTime();
						int rdDay4 = Integer.parseInt(data.getRangeNumber());
						// 时间增量
						int dateNum3 = 1;

						do {
							rd4.add(Calendar.MONTH, dateNum3);
							newStartDate4 = rd4.getTime();
							// 是否相等
							if (rdDay4 == sysDay4) {
								arrlist.add(data);
								break;
							}
						} while (sd4.getTime().compareTo(newStartDate4) > 0);
						break;

					case 6:
						// 系统日期
						Calendar sd5 = Calendar.getInstance();
						sd5.setTime(new Date());
						int sysDay5 = sd5.get(Calendar.DAY_OF_MONTH);
						int sysMonth = sd5.get(Calendar.MONTH) + 1;
						// 定期工作的星期
						Calendar rd5 = Calendar.getInstance();
						rd5.setTime(simpleDate.parse(data.getStartTime()));
						int rdMonth = rd5.get(Calendar.MONTH) + 1;
						Date newStartDate5 = rd5.getTime();
						int rdDay5 = Integer.parseInt(data.getRangeNumber());
						// 时间增量
						int dateNum5 = 2;
						do {
							rd5.add(Calendar.MONTH, dateNum5);
							newStartDate5 = rd5.getTime();
							DateFormat formatLong = new SimpleDateFormat("yyyy-MM-dd");
							String dingDate = formatLong.format(newStartDate5).substring(5, 7);
							int rdMonthNew = Integer.parseInt(dingDate);
							// 是否相等
							if ((sysDay5 == rdDay5) && ((sysMonth == rdMonthNew) || (sysMonth == rdMonth))) {
								arrlist.add(data);
								break;
							}
						} while (sd5.getTime().compareTo(newStartDate5) > 0);
						break;
					case 7:
						if (data.getStartTime().substring(0, 10).equals(date)) {
							arrlist.add(data);
							break;
						} else {
							// 定期开始工作时间
							Calendar rd7 = Calendar.getInstance();
							rd7.setTime(simpleDate.parse(data.getStartTime()));
							Date newStartDate7 = rd7.getTime();
							// 系统时间
							Calendar sd7 = Calendar.getInstance();
							sd7.setTime(simpleDate.parse(date));
							// 时间增量
							int dateNum7 = Integer.parseInt(data
									.getRangeNumber()) + 1;
							do {
								rd7.add(Calendar.DATE, dateNum7);
								newStartDate7 = rd7.getTime();
								if (simpleDate.format(newStartDate7).toString()
										.equals(date)) {
									arrlist.add(data);
									break;
								}
							} while (sd7.getTime().compareTo(newStartDate7) > 0);
							break;
						}
					default:
						break;
					}

				}
				// 得到向定期工作登记表里插入数据的LIST
				List adJTimeworklist = arrlist;
				Iterator adJTimeworkIt = adJTimeworklist.iterator();
				List<AdJTimework> timeworklist = new ArrayList<AdJTimework>();
				
				// 给定期工作entity赋值
				while (adJTimeworkIt.hasNext()) {
					AdJTimework adJTimeworkSet = new AdJTimework();
					AllRegularWorkInfo data = (AllRegularWorkInfo) adJTimeworkIt
							.next();
					// 工作说明
					if ((data.getWorkExplain() != null)
							&& !(data.getWorkExplain().equals(""))) {
						adJTimeworkSet.setWorkExplain(data.getWorkExplain());
					}
					// 标志N(未完成)
					adJTimeworkSet.setMark("N");
					// 是否使用
					adJTimeworkSet.setIsUse("Y");
					// 类别编码
					if (!(data.getWorkTypeCode() == null)
							|| !(data.getWorkTypeCode() == "")) {
						adJTimeworkSet.setWorktypeCode(data.getWorkTypeCode());
					}
					// 子类别编码
					if (!(data.getSubWorkTypeCode() == null)
							|| !(data.getSubWorkTypeCode() == "")) {
						adJTimeworkSet.setSubWorktypeCode(data
								.getSubWorkTypeCode());
					}
					// 工作项目编码
					if (!(data.getWorkItemCode() == null)
							|| !(data.getWorkItemCode() == "")) {
						adJTimeworkSet.setWorkitemCode(data.getWorkItemCode());
					}
					// 修改人
					adJTimeworkSet.setUpdateUser(employee.getWorkerCode()
							.toString());
					// 企业code
					adJTimeworkSet.setEnterpriseCode(employee.getEnterpriseCode());
					adJTimeworkFacadeRemote.save(adJTimeworkSet);
				}
				// 重新页面初始化
				PageObject pobjList = typeRemote.getRegisterList(strEnterpriseCode,strWorkType,
						intStart, intLimit);

				// 查询结果为null,设置页面显示
				if (pobjList.getTotalCount() < 0) {
					String str = "{\"list\":[],\"totalCount\":0}";
					write(str);
				} else {
					String str = JSONUtil.serialize(pobjList);
					write(str);
				}
				LogUtil.log("Action:定期工作登记登记当天工作结束", Level.INFO, null);
			}
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 子画面全部定期工作查询
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getAllRegularQuery() throws JSONException, ParseException {
		LogUtil.log("Action:子画面全部定期工作查询开始", Level.INFO, null);
		try {
			// 页面显示
			int intStart = Integer.parseInt(start.toString());
			int intLimit = Integer.parseInt(limit.toString());
			String fuzzyQuery = fuzzy;
			// 添加人
			employee = (Employee) session.getAttribute("employee");
			String strUserID = employee.getWorkerCode().toString();
			String strEnterpriseCode = employee.getEnterpriseCode();
			// 工作类别取得
			PageObject pobjWorkType = adCommonFacadeRemote
					.getUserRight(strUserID,employee.getEnterpriseCode());
			List listWorkType = pobjWorkType.getList();
			String strWorkType = "";
			if (listWorkType.size() > 0) {
				ComAdCRight dataWorkType = (ComAdCRight) listWorkType.get(0);
				strWorkType = dataWorkType.getWorktypeCode();
			}
			// 初期化
			PageObject obj = adCTimeworkFacadeRemote.getAllRegularQuery(strEnterpriseCode,fuzzy,
					strWorkType, intStart, intLimit);
			// 查询结果为null,设置页面显示
			if (obj.getTotalCount() < 0) {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			} else {
				String str = JSONUtil.serialize(obj);
				write(str);

			}
			LogUtil.log("Action:子画面全部定期工作查询结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:子画面全部定期工作查询失败", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 用户权限取得
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	public void getUserWorkTypeInfo() throws JSONException, ParseException {
		LogUtil.log("Action:用户权限取得开始", Level.INFO, null);
		try {			
			// 添加人
			employee = (Employee) session.getAttribute("employee");
			String strUserID = employee.getWorkerCode().toString();
			// 工作类别取得
			PageObject pobjWorkType = adCommonFacadeRemote
					.getUserRight(strUserID,employee.getEnterpriseCode());
			List listWorkType = pobjWorkType.getList();
			String strWorkType = "";
			if (listWorkType.size() > 0) {
				ComAdCRight dataWorkType = (ComAdCRight) listWorkType.get(0);
				strWorkType = dataWorkType.getWorktypeCode();
			}
			
			// 查询结果
			write("{workType:'" + strWorkType + "'}");
			LogUtil.log("Action:用户权限取得结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:用户权限取得失败", Level.SEVERE, e);			
		}
	}

	/**
	 * 修改定期工作登记表信息
	 * 
	 * @throws ParseException
	 */
	public void updateRegisterData() throws ParseException {
		LogUtil.log("Action:修改定期工作登记表信息开始", Level.INFO, null);
		try {
			employee = (Employee) session.getAttribute("employee");
			AdJTimework adJTimeworkUpdate = new AdJTimework();
			// 添加人
			regularWorkRegisterInfo.setUpdateUser(employee.getWorkerCode()
					.toString());
			adJTimeworkUpdate.setUpdateUser(regularWorkRegisterInfo
					.getUpdateUser());
			// 工作说明
			adJTimeworkUpdate.setWorkExplain(regularWorkRegisterInfo
					.getWorkExplain());
			// 工作结果
			adJTimeworkUpdate.setResult(regularWorkRegisterInfo.getResult());
			// 标志
			String strMark = regularWorkRegisterInfo.getMark();
			if (strMark.equals("完成")) {
				adJTimeworkUpdate.setMark(CodeConstants.ACHIEVE_FLG_Y);
			} else if (strMark.equals("未完成")) {
				adJTimeworkUpdate.setMark(CodeConstants.ACHIEVE_FLG_N);
			}
			// 操作人
			adJTimeworkUpdate
					.setOperator(regularWorkRegisterInfo.getOperator());
			// 备注
			adJTimeworkUpdate.setMemo(regularWorkRegisterInfo.getMemo());
			// 序号
			adJTimeworkUpdate.setId(regularWorkRegisterInfo.getId());
			// 取得参数DB中原有数据
			PageObject regularInfo = new PageObject();
			regularInfo = typeRemote.findById(regularWorkRegisterInfo.getId());
			List regularInfoList1 = regularInfo.getList();
			AdJTimework regularInfoList2 = (AdJTimework) regularInfoList1
					.get(0);
			// 前台取得时间
			String lastmodifiedDate = regularWorkRegisterInfo.getUpdateTime();

			// 工作日期
			adJTimeworkUpdate.setWorkDate(regularInfoList2.getWorkDate());
			// 班次
			adJTimeworkUpdate.setClassSequence(regularInfoList2
					.getClassSequence());
			// 是否使用
			adJTimeworkUpdate.setIsUse(regularInfoList2.getIsUse());
			// 类别编码
			adJTimeworkUpdate.setWorktypeCode(regularInfoList2
					.getWorktypeCode());
			// 子类别编码
			adJTimeworkUpdate.setSubWorktypeCode(regularInfoList2
					.getSubWorktypeCode());
			// 工作项目编码
			adJTimeworkUpdate.setWorkitemCode(regularInfoList2
					.getWorkitemCode());
			// 登记人
			adJTimeworkUpdate.setCrtUser(regularInfoList2.getCrtUser());
			// 单据状态
			adJTimeworkUpdate.setDcmStatus(regularInfoList2.getDcmStatus());
			adJTimeworkUpdate.setEnterpriseCode(employee.getEnterpriseCode());
			try {
				try {
					// 修改一条记录
					adJTimeworkFacadeRemote.update(adJTimeworkUpdate,
							lastmodifiedDate);
				} catch (SQLException e) {
					write(Constants.SQL_FAILURE);
				}
			} catch (DataChangeException e) {
				write(Constants.DATA_USING);
			}
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:修改定期工作登记表信息结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("Action:修改定期工作登记表信息失败", Level.SEVERE, null);
		}

	}

	/**
	 * 删除定期工作登记信息
	 * 
	 * @throws ParseException
	 */
	public void deleteRegisterData() throws ParseException {
		LogUtil.log("Action:定期工作登记删除正常开始", Level.INFO, null);
		try {
			employee = (Employee) session.getAttribute("employee");
			String strEmployee = employee.getWorkerCode().toString();
			// 取得序号
			Long lngId = Long.parseLong(request.getParameter("id").toString());

			// 上次修改时间
			String strUpdateTime = request.getParameter("updateTime")
					.substring(0, 10)
					+ " " + request.getParameter("updateTime").substring(11);

			// 删除记事信息
			try {
				try {
					adJTimeworkFacadeRemote.delete(strEmployee, lngId,
							strUpdateTime);
				} catch (SQLException e) {
					write(Constants.SQL_FAILURE);
				}
			} catch (DataChangeException e) {
				write(Constants.DATA_USING);
			}
			// 显示删除成功信息
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:定期工作登记删除正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("Action:定期工作登记删除异常结束", Level.SEVERE, e);
		}
	}

	/**
	 * 取消当天工作登记
	 * 
	 * @throws JSONException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void cancelRegisterData() throws JSONException, ParseException {
		LogUtil.log("Action：取消当天工作登记开始", Level.INFO, null);
		try {
			// 取得用户ID
			employee = (Employee) session.getAttribute("employee");
			String strEmployee = employee.getWorkerCode();

			// 转换从前台获取的数据
			Object objUpdate = JSONUtil.deserialize(strUpdate);
			if (objUpdate != null) {
				Map temMap = null;
				AdJTimework objAddData = new AdJTimework();
				if (List.class.isAssignableFrom(objUpdate.getClass())) {
					List lstObj = (List) objUpdate;
					for (int i = 0; i < lstObj.size(); i++) {
						temMap = (Map) (lstObj.get(i));
						// 设置entity
						objAddData = adJTimeworkFacadeRemote.findById(Long
								.parseLong(temMap.get("id").toString()));				

						// 是否使用
						objAddData.setIsUse("N");
						// 修改人
						objAddData.setUpdateUser(strEmployee);
						// 时间
						String strLastmodifyTime = (String) temMap
								.get("updateTime");
						// 更新数据
						try {
							try {
								adJTimeworkFacadeRemote.update(objAddData,
										strLastmodifyTime);
							} catch (SQLException e) {
								write(Constants.SQL_FAILURE);
							}
						} catch (DataChangeException e) {
							write(Constants.DATA_USING);
						}
					}
				}
			}
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action：取消当天工作登记结束", Level.INFO, null);
		} catch (NumberFormatException ne) {
			LogUtil.log("Action：取消当天工作登记失败", Level.SEVERE, ne);
		}
	}
}