/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.materialinquire.action;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJOutregFacadeRemote;
import power.ejb.administration.AdJOutreg;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.CodeConstants;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;

/**
 * 物资出门查询
 * 
 * @author sufeiyu
 * 
 */
@SuppressWarnings("serial")
public class MaterialInquireAction extends AbstractAction {

	/** 出门查询的实现类接口 */
	private AdJOutregFacadeRemote remote;
	/** 画面参数开始时间* */
	public java.sql.Date dteStartDate;
	/** 画面参数结束时间* */
	public java.sql.Date dteEndDate;
	/** 画面参数所在单位* */
	public String strFirm;
	/** 画面参数上报状态* */
	public Long lngDcmStatus;
	/** *画面参数开始页 */
	public Long start;
	/** 画面参数页面最大值* */
	public Long limit;
	private final static String STR_FILE_NAME = "物资出门查询";

	public MaterialInquireAction() {
		remote = (AdJOutregFacadeRemote) factory
				.getFacadeRemote("AdJOutregFacade");
	}

	/**
	 * 取得物资出门所有数据
	 * @throws JSONException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getData() throws JSONException {
		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strEnterpriseCode = employee.getEnterpriseCode();
		PageObject objResult = null;
		String str = "";

		LogUtil.log("Action:物资出门查询开始", Level.INFO, null);

		try {
			// 上报状态
			String strFirm1 = "";
			if (strFirm != null) {
				strFirm1 = strFirm.toString();
			}
			
			// 上报状态
			String strDcmStatus = "";
			if (lngDcmStatus != null) {
				strDcmStatus = lngDcmStatus.toString();
			}

			// 页面显示
			if ((start != null) && (limit != null)) {
				int intStart = Integer.parseInt(start.toString());
				int intLimit = Integer.parseInt(limit.toString());
				objResult = remote.getData(strEnterpriseCode, dteStartDate, dteEndDate, strFirm1,
						strDcmStatus, intStart, intLimit);
			} else {
				objResult = remote.getData(strEnterpriseCode, dteStartDate, dteEndDate, strFirm1,
						strDcmStatus);
			}

			// 编码转换
			if (objResult.getTotalCount() > 0) {
				List<AdJOutreg> list = objResult.getList();

				List<AdJOutreg> arrlist = new ArrayList<AdJOutreg>();

				if ((list != null) && (list.size() > 0)) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						AdJOutreg out = new AdJOutreg();
						out = (AdJOutreg) it.next();
						// 处理上报状态
						if ((out.getDcmStatus() != null)
								&& (!out.getDcmStatus().equals(""))) {
							if (out.getDcmStatus().equals(
									CodeConstants.FROM_STATUS_0)) {
								out.setDcmStatus("未上报");
							} else if (out.getDcmStatus().equals(
									CodeConstants.FROM_STATUS_1)) {
								out.setDcmStatus("已上报");
							} else if (out.getDcmStatus().equals(
									CodeConstants.FROM_STATUS_2)) {
								out.setDcmStatus("已终结");
							} else if (out.getDcmStatus().equals(
									CodeConstants.FROM_STATUS_3)) {
								out.setDcmStatus("已退回");
							}
						}
						// 处理单位数量
						if ((out.getUnit() != null) && (!out.getUnit().equals(""))){
							out.setUnit(remote.getUintById(out.getUnit().toString()));
						}
						arrlist.add(out);
					}
				}

				if ((arrlist != null) && (arrlist.size() > 0)) {
					Long lngCount = objResult.getTotalCount();
					objResult.setList(arrlist);
					objResult.setTotalCount(lngCount);
					request.getSession().setAttribute("pageObjMaterial", objResult);
					str = JSONUtil.serialize(objResult);
				} 
			} else {
				str = "{\"list\":[],\"totalCount\":null}";
			}
			write(str);
			LogUtil.log("Action:物资出门查询正常结束", Level.INFO, null);
		} catch (NumberFormatException e) {
			LogUtil.log("Action:物资出门查询异常结束", Level.SEVERE, null);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void materialExportFile() throws Exception {

		employee = (Employee) session.getAttribute("employee");
		String strEnterpriseCode = employee.getEnterpriseCode();
		
		LogUtil.log("Action:导出文件开始", Level.INFO, null);
		// 操作excel文件对象
		ExportXsl exsl = new ExportXsl();
		// 设置response
		exsl.setResponse(response);
		// 当前时间作为文件名以部分
		Date dte = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = sdf.format(dte);
		// 设定文件名
		exsl.setStrFileName(STR_FILE_NAME + strDate);
		// excel文件每列标题头
		List<String> lstHeader = new ArrayList<String>();
		lstHeader.add("行号");
		lstHeader.add("经办人");
		lstHeader.add("经办人所在单位");
		lstHeader.add("出门时间");
		lstHeader.add("物资名称");
		lstHeader.add("物资规格");
		lstHeader.add("物资数量");
		lstHeader.add("物资单位");
		lstHeader.add("备注");
		lstHeader.add("物资出入原因");
		lstHeader.add("上报状态");
		lstHeader.add("值班保安");
		exsl.setLstTitle(lstHeader);

		// excel文件中的一行
		List<String> lstRow = null;
		// excel文件中的所有行集合
		List lstRowSet = new ArrayList();
		// excel文件单行实体
		AdJOutreg tempAdJOutreg = null;
		// 获得需要导出的excel文件内容
		PageObject pageObj = new PageObject();
		// 上报状态
		String strFirm1 = "";
		if (strFirm != null) {
			strFirm1 = strFirm.toString();
		}

		// 上报状态
		String strDcmStatus = "";
		if (lngDcmStatus != null) {
			strDcmStatus = lngDcmStatus.toString();
		}

		// 取得要导出的数据
		pageObj = remote.getData(strEnterpriseCode, dteStartDate, dteEndDate, strFirm1,
				strDcmStatus);
		// 编码转换
		if (pageObj.getTotalCount() > 0) {
			List<AdJOutreg> list = pageObj.getList();
            List<AdJOutreg> arrlist = new ArrayList<AdJOutreg>();

			if ((list != null) && (list.size() > 0)) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					AdJOutreg out = new AdJOutreg();
					out = (AdJOutreg) it.next();
					// 处理上报状态
					if ((out.getDcmStatus() != null)
							&& (!out.getDcmStatus().equals(""))) {
						if (out.getDcmStatus().equals(
								CodeConstants.FROM_STATUS_0)) {
							out.setDcmStatus("未上报");
						} else if (out.getDcmStatus().equals(
								CodeConstants.FROM_STATUS_1)) {
							out.setDcmStatus("已上报");
						} else if (out.getDcmStatus().equals(
								CodeConstants.FROM_STATUS_2)) {
							out.setDcmStatus("已终结");
						} else if (out.getDcmStatus().equals(
								CodeConstants.FROM_STATUS_3)) {
							out.setDcmStatus("已退回");
						}
					}
					// 处理单位数量
					if ((out.getUnit() != null) && (!out.getUnit().equals(""))){
						out.setUnit(remote.getUintById(out.getUnit().toString()));
					}
					arrlist.add(out);
				}
			}

			if ((arrlist != null) && (arrlist.size() > 0)) {
				Long lngCount = pageObj.getTotalCount();
				pageObj.setList(arrlist);
				pageObj.setTotalCount(lngCount);
			} 
		}	
		
		try {
			for (int i = 0; i < pageObj.getList().size(); i++) {
				lstRow = new ArrayList();
				tempAdJOutreg = (AdJOutreg) pageObj.getList().get(i);
				// 设置行号
				lstRow.add(i + 1 + "");
				// 设置经办人
				if (tempAdJOutreg.getAgent() != null) {
					lstRow.add(tempAdJOutreg.getAgent());
				} else {
					lstRow.add("");
				}
				// 设置所在单位
				if (tempAdJOutreg.getFirm() != null) {
					lstRow.add(tempAdJOutreg.getFirm());
				} else {
					lstRow.add("");
				}
				// 设置出门日期
				if (tempAdJOutreg.getOutDate() != null) {
					lstRow.add(tempAdJOutreg.getOutDate().toString().substring(0,
							16));
				} else {
					lstRow.add("");
				}
				// 设置物资名称
				if (tempAdJOutreg.getWpName() != null) {
					lstRow.add(tempAdJOutreg.getWpName());
				} else {
					lstRow.add("");
				}
				// 设置物资规格
				if (tempAdJOutreg.getStandard() != null) {
					lstRow.add(tempAdJOutreg.getStandard());
				} else {
					lstRow.add("");
				}
				// 设置物资数量
				if (tempAdJOutreg.getNum() != null) {
					lstRow.add(tempAdJOutreg.getNum().toString());
				} else {
					lstRow.add("");
				}
				// 设置物资单位
				if (tempAdJOutreg.getUnit() != null) {
					lstRow.add(tempAdJOutreg.getUnit());
				} else {
					lstRow.add("");
				}
				// 设置备注
				if (tempAdJOutreg.getNote() != null) {
					lstRow.add(tempAdJOutreg.getNote());
				} else {
					lstRow.add("");
				}
				// 设置物资出入原因
				if (tempAdJOutreg.getReason() != null) {
					lstRow.add(tempAdJOutreg.getReason());
				} else {
					lstRow.add("");
				}
				// 设置上报状态
				if (tempAdJOutreg.getDcmStatus() != null) {
					lstRow.add(tempAdJOutreg.getDcmStatus());
				} else {
					lstRow.add("");
				}
				// 设置值班保安
				if (tempAdJOutreg.getOnduty() != null) {
					lstRow.add(tempAdJOutreg.getOnduty());
				} else {
					lstRow.add("");
				}
				lstRowSet.add(lstRow);
			}
			// 设置所有行内容
			exsl.setLstRow(lstRowSet);
			// 创建导出excel文件
			exsl.createXsl();
			LogUtil.log("Action:导出文件结束", Level.INFO, null);
		} catch (RuntimeException e) {
			write(Constants.IO_FAILURE);
		}
	}

	/**
	 * @return 画面参数开始时间
	 */
	public java.sql.Date getDteStartDate() {
		return dteStartDate;
	}

	/**
	 * @param 画面参数开始时间
	 */
	public void setDteStartDate(java.sql.Date dteStartDate) {
		this.dteStartDate = dteStartDate;
	}

	/**
	 * @return 画面参数结束时间
	 */
	public java.sql.Date getDteEndDate() {
		return dteEndDate;
	}

	/**
	 * @param 画面参数结束时间
	 */
	public void setDteEndDate(java.sql.Date dteEndDate) {
		this.dteEndDate = dteEndDate;
	}

	/**
	 * @return 画面参数所在单位
	 */
	public String getStrFirm() {
		return strFirm;
	}

	/**
	 * @param 画面参数所在单位
	*/
	public void setStrFirm(String strFirm) {
		this.strFirm = strFirm;
	}

	/**
	 * @return 画面参数上报状态
	 */
	public Long getLngDcmStatus() {
		return lngDcmStatus;
	}

	/**
	 * @param 画面参数上报状态
	 */
	public void setLngDcmStatus(Long lngDcmStatus) {
		this.lngDcmStatus = lngDcmStatus;
	}

	/**
	 * @return the 画面参数开始页
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * @param 画面参数开始页
	 */
	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * @return 画面参数页面最大值
	 */
	public Long getLimit() {
		return limit;
	}

	/**
	 * @param 画面参数页面最大值
	 */
	public void setLimit(Long limit) {
		this.limit = limit;
	}

}
