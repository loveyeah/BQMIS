/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.hr;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.DismissionStaffListBean;
import power.ejb.hr.HrJNewAndDimissionFacadeRemote;
import power.ejb.hr.HrJTotalBean;
import power.ejb.hr.NewStaffListBean;
import power.web.birt.bean.hr.GotAndLostWorkerBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * 新进/离职员工统计(3张报表)Action
 * @author wangpeng
 *
 */
public class GotAndLostWorkerAction extends AbstractAction {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 新进/离职员工统计信息远程取得接口 */
    HrJNewAndDimissionFacadeRemote remote;
    /** 数字格式 */
    private static final String strFormat = "###,###,###,###,###";
    /** 员工姓名截断字节数 */
    private static final int NUM_10 = 10;
    /** HTML空格 */
    private static final String HTML_BLANK = "&nbsp;";

    /**
     * 构造方法
     */
    public GotAndLostWorkerAction() {

        // 新进/离职员工统计信息远程取得对象初始化
        remote = (HrJNewAndDimissionFacadeRemote) factory.getFacadeRemote("HrJNewAndDimissionFacade");
    }

    /**
     * 新进/离职员工统计表（报表）信息取得
     * @param argYear 年度
     * @param argDept 部门
     * @param argEnCode 企业编码
     * @return 新进/离职员工统计表（报表）信息
     */
    @SuppressWarnings("unchecked")
    public GotAndLostWorkerBean getGotAndLostWorkerInfo(String argYear, String argDept, String argEnCode) {

        // 新进/离职员工统计表打印数据
        GotAndLostWorkerBean entity = new GotAndLostWorkerBean();
        DecimalFormat DigitalFormat = new DecimalFormat(strFormat);
        // 新进/离职员工统计表详细信息List
        List<HrJTotalBean> lstGotAndLost = new ArrayList<HrJTotalBean>();
        List<HrJTotalBean> lstTempList = new ArrayList<HrJTotalBean>();
        // 调用EJB取得新进/离职员工信息
        PageObject obj = remote.findTotal(argYear, argDept, argEnCode);
        if (obj != null) {
            lstGotAndLost = (List<HrJTotalBean>) obj.getList();
        }
        // 调用EJB取得新进/离职员工信息
        PageObject pobj = remote.findTotal(argYear, argDept, argEnCode);
        if (obj != null) {
        	lstTempList = (List<HrJTotalBean>) pobj.getList();
        }
        // 对数据做适应画面的处理
        if (lstGotAndLost != null && lstGotAndLost.size() > 0) {
            // 单行字符串集
            List<String> lstStr = null;
            // 单行信息
            HrJTotalBean gotAndLostWorker = null;
            lstGotAndLost = delectDeptName(lstGotAndLost);
            for (int i = 0; i < lstGotAndLost.size(); i++) {
                lstStr = new ArrayList<String>();
                gotAndLostWorker = lstGotAndLost.get(i);
                // 对所属部门进行换行处理
                String strDept = commUtils.addBrByByteLengthForHR(gotAndLostWorker
                    .getDeptName(), Constant.EIGHTPOINTPERSETY24);
                lstStr.add(strDept);
                gotAndLostWorker.setDeptName(strDept.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对岗位名称进行换行处理
                String strJobName = commUtils.addBrByByteLengthForHR(gotAndLostWorker
                    .getStationName(), Constant.EIGHTPOINTPERSETY24);
                lstStr.add(strJobName);
                gotAndLostWorker.setStationName(strJobName.replaceAll(Constant.BLANK, HTML_BLANK));
                // 岗位标准人数
                if (gotAndLostWorker.getStationNum() != null) {
                    // 逗号分割处理
                    String strStationNum = DigitalFormat.format(Long.parseLong(gotAndLostWorker.getStationNum()));
                    gotAndLostWorker.setStationNum(strStationNum);
                }
                // 现人数
                if (gotAndLostWorker.getNowNum() != null) {
                    // 逗号分割处理
                    String strNowNum = DigitalFormat.format(Long.parseLong(gotAndLostWorker.getNowNum()));
                    gotAndLostWorker.setNowNum(strNowNum);
                }
                // 新进人数
                if (gotAndLostWorker.getNewNum() != null) {
                    // 逗号分割处理
                    String strNewNum = DigitalFormat.format(Long.parseLong(gotAndLostWorker.getNewNum()));
                    gotAndLostWorker.setNewNum(strNewNum);
                }
                // 离职人数
                if (gotAndLostWorker.getDimissionNum() != null) {
                    // 逗号分割处理
                    String strLostNum = DigitalFormat.format(Long.parseLong(gotAndLostWorker.getDimissionNum()));
                    gotAndLostWorker.setDimissionNum(strLostNum);
                }
                // 叠加后的计数
                gotAndLostWorker.setCntRow(commUtils.countMaxContain(lstStr, Constant.HTML_CHANGE_LINE) + 1);
            }
            lstGotAndLost = commUtils.newDeptName(lstGotAndLost, 40.3,lstTempList);
            entity.setLstGotAndLostWorker(lstGotAndLost);
        }
        return entity;
    }

    /**
     * 新进员工花名册（报表）信息取得
     * @param argYear 年度
     * @param argDept 部门
     * @param argEnCode 企业编码
     * @return 新进员工花名册（报表）信息
     */
    @SuppressWarnings("unchecked")
    public GotAndLostWorkerBean getGotWorkerInfo(String argYear, String argDept, String argEnCode) {

        // 新进员工花名册打印数据
        GotAndLostWorkerBean entity = new GotAndLostWorkerBean();
        // 新进员工花名册详细信息List
        List<NewStaffListBean> lstGot = new ArrayList<NewStaffListBean>();
        // 调用EJB取得新进员工信息
        PageObject obj = remote.findNew(argYear, argDept, argEnCode);
        if (obj != null) {
            lstGot = (List<NewStaffListBean>) obj.getList();
        }
        // 对数据做适应画面的处理
        if (lstGot != null && lstGot.size() > 0) {
            // 单行字符串集
            List<String> lstStr = null;
            // 单行信息
            NewStaffListBean gotWorker = null;
            for (int i = 0; i < lstGot.size(); i++) {
                lstStr = new ArrayList<String>();
                gotWorker = lstGot.get(i);
                // 取得员工姓名前5个汉字（或者前10个字节）
                if(gotWorker.getWorkerName() != null && gotWorker.getWorkerName().getBytes().length > NUM_10){
                	String strWorkerName = commUtils.cutByByte(gotWorker.getWorkerName(), NUM_10);
                	gotWorker.setWorkerName(strWorkerName.replaceAll(Constant.BLANK, HTML_BLANK));
                }
                // 对员工类别进行换行处理
                String strWorkerType = commUtils.addBrByByteLengthForHR(gotWorker
                    .getWorkerType(), Constant.EIGHTPOINTPERSETY7);
                lstStr.add(strWorkerType);
                gotWorker.setWorkerType(strWorkerType.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对进厂类别进行换行处理
                String strJoinType = commUtils.addBrByByteLengthForHR(gotWorker
                    .getMissionType(), Constant.EIGHTPOINTPERSETY7);
                lstStr.add(strJoinType);
                gotWorker.setMissionType(strJoinType.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对所属部门进行换行处理
                String strDept = commUtils.addBrByByteLengthForHR(gotWorker
                    .getDepartment(), Constant.EIGHTPOINTPERSETY9);
                lstStr.add(strDept);
                gotWorker.setDepartment(strDept.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对岗位名称进行换行处理
                String strJobName = commUtils.addBrByByteLengthForHR(gotWorker
                    .getJobName(), Constant.EIGHTPOINTPERSETY9);
                lstStr.add(strJobName);
                gotWorker.setJobName(strJobName.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对备注进行换行处理
                String strMemo = commUtils.addBrByByteLengthForHR(gotWorker
                    .getMemo(), Constant.EIGHTPOINTPERSETY14);
                lstStr.add(strMemo);
                gotWorker.setMemo(strMemo.replaceAll(Constant.BLANK, HTML_BLANK));
                // 叠加后的计数
                gotWorker.setCntRow(commUtils.countMaxContain(lstStr, Constant.HTML_CHANGE_LINE) + 1);
            }
            entity.setLstGotWorker(lstGot);
        }
        return entity;
    }

    /**
     * 离职员工花名册（报表）信息取得
     * @param argYear 年度
     * @param argDept 部门
     * @param argEnCode 企业编码
     * @return 离职员工花名册（报表）信息
     */
    @SuppressWarnings("unchecked")
    public GotAndLostWorkerBean getLostWorkerInfo(String argYear, String argDept, String argEnCode,String typeId,String advicenoteNo) {

        // 离职员工花名册打印数据
        GotAndLostWorkerBean entity = new GotAndLostWorkerBean();
        // 离职员工花名册详细信息List
        List<DismissionStaffListBean> lstLost = new ArrayList<DismissionStaffListBean>();
        // 调用EJB取得离职员工信息
        PageObject obj = remote.findDimission(argYear, argDept, argEnCode,typeId,advicenoteNo);
        if (obj != null) {
            lstLost = (List<DismissionStaffListBean>) obj.getList();
        }
        // 对数据做适应画面的处理
        if (lstLost != null && lstLost.size() > 0) {
            // 单行字符串集
            List<String> lstStr = null;
            // 单行信息
            DismissionStaffListBean lostWorker = null;
            for (int i = 0; i < lstLost.size(); i++) {
                lstStr = new ArrayList<String>();
                lostWorker = lstLost.get(i);
                // 取得员工姓名前5个汉字（或者前10个字节）
                if(lostWorker.getWorkerName() != null && lostWorker.getWorkerName().getBytes().length > NUM_10) {
                	String strWorkerName = commUtils.cutByByte(lostWorker.getWorkerName(), NUM_10);
                    lostWorker.setWorkerName(strWorkerName.replaceAll(Constant.BLANK, HTML_BLANK));
                }
                // 对员工类别进行换行处理
                String strWorkerType = commUtils.addBrByByteLengthForHR(lostWorker
                    .getWorkerType(), Constant.EIGHTPOINTPERSETY9);
                lstStr.add(strWorkerType);
                lostWorker.setWorkerType(strWorkerType.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对离职类别进行换行处理
                String strLeaveType = commUtils.addBrByByteLengthForHR(lostWorker
                    .getDismissionType(), Constant.EIGHTPOINTPERSETY9);
                lstStr.add(strLeaveType);
                lostWorker.setDismissionType(strLeaveType.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对所属部门进行换行处理
                String strDept = commUtils.addBrByByteLengthForHR(lostWorker
                    .getDepartment(), Constant.EIGHTPOINTPERSETY9);
                lstStr.add(strDept);
                lostWorker.setDepartment(strDept.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对岗位名称进行换行处理
                String strJobName = commUtils.addBrByByteLengthForHR(lostWorker
                    .getJobName(), Constant.EIGHTPOINTPERSETY9);
                lstStr.add(strJobName);
                lostWorker.setJobName(strJobName.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对离职原因进行换行处理
                String strLeaveReason = commUtils.addBrByByteLengthForHR(lostWorker
                    .getDismissionReason(), Constant.EIGHTPOINTPERSETY9);
                lstStr.add(strLeaveReason);
                lostWorker.setDismissionReason(strLeaveReason.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对离职后去向进行换行处理
                String strWhither = commUtils.addBrByByteLengthForHR(lostWorker
                    .getWhither(), Constant.EIGHTPOINTPERSETY10);
                lstStr.add(strWhither);
                lostWorker.setWhither(strWhither.replaceAll(Constant.BLANK, HTML_BLANK));
                // 对备注进行换行处理
                String strMemo = commUtils.addBrByByteLengthForHR(lostWorker
                    .getMemo(), Constant.EIGHTPOINTPERSETY20);
                lstStr.add(strMemo);
                lostWorker.setMemo(strMemo.replaceAll(Constant.BLANK, HTML_BLANK));
                // 叠加后的计数
                lostWorker.setCntRow(commUtils.countMaxContain(lstStr, Constant.HTML_CHANGE_LINE) + 1);
            }
            entity.setLstLostWorker(lstLost);
        }
        return entity;
    }

	 /**
	  * 将相同所属部门去掉
	  * @param List<HrJTotalBean> oldList 未处理List
	  * @return List<HrJTotalBean> newList 处理后List
	  */
	public static List<HrJTotalBean> delectDeptName(List<HrJTotalBean> oldList){
		List<HrJTotalBean> newList = new ArrayList<HrJTotalBean>();
		if(oldList!=null&&oldList.size()>0){
			String strDeptName = oldList.get(0).getDeptName();
			for(int i=1;i<oldList.size();i++){
				if(strDeptName!=null&&strDeptName.equals(oldList.get(i).getDeptName())){
					HrJTotalBean lstBean = oldList.get(i);
					lstBean.setDeptName("");
				}else{
					strDeptName = oldList.get(i).getDeptName();
				}
			}
			return oldList;
		}else{
			return newList;
		}
	}

}
