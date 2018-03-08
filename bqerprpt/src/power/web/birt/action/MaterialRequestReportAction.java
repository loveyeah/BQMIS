/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.MaterialRequestReportBean;
import power.ejb.resource.MaterialRequestReportDetailListBean;
import power.ejb.resource.MrpJPlanRequirementHeadFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 物料需求计划登记Action
 * @author zhujie
 *
 */
public class MaterialRequestReportAction extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private MrpJPlanRequirementHeadFacadeRemote remote;
	/** 远程 */
	private BpCMeasureUnitFacadeRemote remoteComm;
	/** 远程 */
//	private CommInterfaceFacadeRemote remoteClassComm;
	private RunCSpecialsFacadeRemote remoteClassComm;

	/**
	 * 构造函数
	 */
	public MaterialRequestReportAction(){
		remote = (MrpJPlanRequirementHeadFacadeRemote)factory.getFacadeRemote("MrpJPlanRequirementHeadFacade");
		remoteComm = (BpCMeasureUnitFacadeRemote)factory.getFacadeRemote("BpCMeasureUnitFacade");
	}

	/**
	 * 物料需求计划登记账票信息查询
	 * @param enterpriseCode 企业编码
	 * @param headId 选中项ID
     * @return ReceiveGoodsBean 到货单明细
	 */
	public MaterialRequestReportBean getMaterialRequestReportBean(String enterpriseCode,String headId){
		MaterialRequestReportBean entity = remote.fillallForMaterialRequestReport(enterpriseCode,headId);
		List<MaterialRequestReportDetailListBean> lstMaterial = entity.getMaterialRequestReportDetailList();
		String patternMoney = "###,###,###,###,##0.0000";
		DecimalFormat dfMoney = new DecimalFormat(patternMoney);
		if((lstMaterial != null)&&(lstMaterial.size() > 0)){
			MaterialRequestReportDetailListBean lstBean = null;
			entity.setFlag(false);
		    double totalMonty =0.0000;
			// 详细中添加行数计数
		    for(int cnt = 0; cnt < lstMaterial.size(); cnt++){
		    	List<String> strList = new ArrayList<String>();
		    	lstBean = lstMaterial.get(cnt);
		    	// 处理物料名称
		    	String strNewMaterialName = commUtils.addBrByByteLengthForMaterial(lstBean.getMaterialName(),
		    			Constant.EIGHTPOINTPERSETY11);
		    	lstBean.setMaterialName(strNewMaterialName);
		    	strList.add(strNewMaterialName);
		    	// 处理物料编码
		    	String strNewMaterialNo = commUtils.addBrByByteLengthForMaterial(lstBean.getMaterialNo(),
		    			Constant.EIGHTPOINTPERSETY11);
		    	lstBean.setMaterialNo(strNewMaterialNo);
		    	strList.add(strNewMaterialNo);
		    	// 处理规格型号
		    	String strNewMaterialSpecNo = commUtils.addBrByByteLengthForMaterial(lstBean.getSpecNo(),
		    			Constant.EIGHTPOINTPERSETY9);
		    	lstBean.setSpecNo(strNewMaterialSpecNo);
		    	strList.add(strNewMaterialSpecNo);
		    	// 处理单位

		    	if(lstBean.getStockUmName()!=null&&!"".equals(lstBean.getStockUmName())){
		    		BpCMeasureUnit bcmu = remoteComm.findById(Long.parseLong(lstBean.getStockUmName()));
		    		if(bcmu!=null){
		    			String stockUmID = bcmu.getUnitName();
		    			String strNewMaterialStockUmID = commUtils.addBrByByteLengthForMaterial(stockUmID,
		    					Constant.EIGHTPOINTPERSETY5);
		    			lstBean.setStockUmName(strNewMaterialStockUmID);
		    			strList.add(strNewMaterialStockUmID);
		    		}
		    	}
		    	// 需求日期
		    	String strNewMaterialDueDate = commUtils.addBrByByteLengthForMaterial(lstBean.getDueDate(),
		    			Constant.EIGHTPOINTPERSETY8);
		    	lstBean.setDueDate(strNewMaterialDueDate);
		    	strList.add(strNewMaterialDueDate);
		    	// 处理费用来源
		    	String strNewMaterialMoneyFrom = commUtils.addBrByByteLengthForMaterial(lstBean.getMoneyFrom(),
		    			Constant.EIGHTPOINTPERSETY8);
		    	lstBean.setMoneyFrom(strNewMaterialMoneyFrom);
		    	strList.add(strNewMaterialMoneyFrom);
		    	// 处理用途
		    	String strNewMaterialUseFor = commUtils.addBrByByteLengthForMaterial(lstBean.getUseFor(),
		    			Constant.EIGHTPOINTPERSETY8);
		    	lstBean.setUseFor(strNewMaterialUseFor);
		    	strList.add(strNewMaterialUseFor);
		    	// 叠加后的计数
		    	lstBean.setCntRow(commUtils.countMaxContain(strList, Constant.HTML_CHANGE_LINE) + 1);
		    	// 处理合计金额
		    	totalMonty += Double.parseDouble(lstBean.getTotalMoney());
		    	// 处理日期
		    	lstBean.setDueDate(commUtils.formatTimeYMD(lstBean.getDueDate()));
		    }
		    entity.setTotalMoney(dfMoney.format(totalMonty));
		}
		entity.setMaterialRequestReportDetailList(lstMaterial);
		// 设置当前日期
		SimpleDateFormat mFormatTimeOnly = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		Date dDate = new Date();
		String strTime = mFormatTimeOnly.format(dDate);
		String chinaTime = commUtils.formatTimeYMD(strTime);
		entity.setNowDate(chinaTime);

		// 对应comminterface jar包删除
		// 由ID取归口专业Name
		remoteClassComm = (RunCSpecialsFacadeRemote) factory.getFacadeRemote("RunCSpecialsFacade");
		List<RunCSpecials> classList = new ArrayList<RunCSpecials>();
		classList = remoteClassComm.findSpeList(enterpriseCode);
		if((classList!=null)&&(classList.size()>0)){
			for(int i = 0;i<classList.size();i++){
				if(!"".equals(entity.getBelongClass())&&entity.getBelongClass().equals(classList.get(i).getSpecialityCode())){
					entity.setBelongClass(classList.get(i).getSpecialityName());
					break;
				}
			}
		}
		// 格式化需求日期
		entity.setDueDate(commUtils.formatTimeYMD(entity.getDueDate()));
		return entity;
	}
}
