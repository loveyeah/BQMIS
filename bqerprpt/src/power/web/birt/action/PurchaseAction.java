/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.text.SimpleDateFormat;

import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.PurchaseBean;
import power.ejb.resource.PurchaseListBean;

/**
 * 采购单表Action
 * @author zhujie
 *
 */
public class PurchaseAction extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private PurJOrderDetailsFacadeRemote remote;
	
	/**
	 * 构造函数
	 */
	public PurchaseAction(){
		remote = (PurJOrderDetailsFacadeRemote)factory.getFacadeRemote("PurJOrderDetailsFacade");
	}
	
	/**
	 * 获取采购单信息
	 *
	 * @param purNo 采购单号
	 * @param enterpriseCode 企业编码
     * @return PurchaseBean 采购单明细
	 */
	public PurchaseBean setPurchaseBean(String purNo,String enterpriseCode){
		PurchaseBean entity = new PurchaseBean();
		entity = remote.findAllForPurchase(purNo, enterpriseCode);
		List<PurchaseListBean> purchaseList = entity.getPurchaseList();
		if((purchaseList != null)&&(purchaseList.size() > 0)){
			PurchaseListBean lstBean = null;
			for(int cnt = 0; cnt < purchaseList.size(); cnt++){
		    	List<String> strList = new ArrayList<String>();
		    	lstBean = purchaseList.get(cnt);
		    	// 处理物料名称
		    	String strNewMaterialName = commUtils.addBrByByteLengthForMaterial(lstBean.getMaterialName(),
		    			Constant.EIGHTPOINTPERSETY10);
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
		    	// 处理交期
		    	String strNewMaterialDueDate = commUtils.addBrByByteLengthForMaterial(lstBean.getDueDate(),
		    			Constant.EIGHTPOINTPERSETY8);
		    	lstBean.setDueDate(strNewMaterialDueDate);
		    	strList.add(strNewMaterialDueDate);
		    	// 叠加后的计数
		    	lstBean.setCntRow(commUtils.countMaxContain(strList, Constant.HTML_CHANGE_LINE) + 1);
			}
		}
		entity.setPurchaseList(purchaseList);
		// 设置当前日期
		SimpleDateFormat mFormatTimeOnly = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());   
		Date dDate = new Date();   
		String strTime = mFormatTimeOnly.format(dDate);
		String chinaTime = commUtils.formatTimeYMD(strTime); 
		entity.setNowDate(chinaTime);
		return entity;
	}
}
