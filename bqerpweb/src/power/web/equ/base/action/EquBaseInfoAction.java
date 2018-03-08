package power.web.equ.base.action;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquJBaseAnnex;
import power.ejb.equ.base.EquJBaseAnnexFacadeRemote;
import power.ejb.equ.base.EquJBaseInfo;
import power.ejb.equ.base.EquJBaseInfoFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class EquBaseInfoAction extends UploadFileAbstractAction{
	private EquJBaseInfo baseInfo;
	private EquJBaseInfoFacadeRemote infoRemote;
	private EquJBaseAnnexFacadeRemote annexRemote;
	private File annex;
	public EquBaseInfoAction(){
		infoRemote=(EquJBaseInfoFacadeRemote) factory.getFacadeRemote("EquJBaseInfoFacade");
		annexRemote=(EquJBaseAnnexFacadeRemote) factory.getFacadeRemote("EquJBaseAnnexFacade");
	}
	/**
	 * 保存设备基础信息
	 */
	public void saveOrUpdateBaseInfo(){
		Long EquBaseId=baseInfo.getEquBaseId();
		if(EquBaseId!=null){
			EquJBaseInfo model=infoRemote.findById(EquBaseId);
			if (baseInfo.getManufacturer()!=null&&!baseInfo.getManufacturer().equals("")) {
				model.setManufacturer(baseInfo.getManufacturer());
			}
			if (baseInfo.getModel()!=null&&!baseInfo.getModel().equals("")) {
				model.setModel(baseInfo.getModel());
			}
			if (baseInfo.getFactoryDate()!=null&&!baseInfo.getFactoryDate().equals("")) {
				model.setFactoryDate(baseInfo.getFactoryDate());
			}
			if (baseInfo.getInstallationDate()!=null&&!baseInfo.getInstallationDate().equals("")) {
				model.setInstallationDate(baseInfo.getInstallationDate());
			}
			if (baseInfo.getInstallationCode()!=null&&!baseInfo.getInstallationCode().equals("")) {
				model.setInstallationCode(baseInfo.getInstallationCode());
			}
			if (baseInfo.getPrice()!=null&&!baseInfo.getPrice().equals("")) {
				model.setPrice(baseInfo.getPrice());
			}
			if (baseInfo.getUseYear()!=null&&!baseInfo.getUseYear().equals("")) {
				model.setUseYear(baseInfo.getUseYear());
			}
			if (baseInfo.getAssetCode()!=null&&!baseInfo.getAssetCode().equals("")) {
				model.setAssetCode(baseInfo.getAssetCode());
			}
			if (baseInfo.getTechnicalParameters()!=null&&!baseInfo.getTechnicalParameters().equals("")) {
				model.setTechnicalParameters(baseInfo.getTechnicalParameters());
			}
			if (baseInfo.getOneResponsible()!=null&&!baseInfo.getOneResponsible().equals("")) {
				model.setOneResponsible(baseInfo.getOneResponsible());
			}
			if (baseInfo.getTwoResponsible()!=null&&!baseInfo.getTwoResponsible().equals("")) {
				model.setTwoResponsible(baseInfo.getTwoResponsible());
			}
			if (baseInfo.getThreeResponsible()!=null&&!baseInfo.getThreeResponsible().equals("")) {
				model.setThreeResponsible(baseInfo.getThreeResponsible());
			}
			model.setLastModifyBy(employee.getWorkerCode());
			model.setLastModifyDate(new java.util.Date());
			baseInfo=infoRemote.update(model);
		}else{
			baseInfo.setIsUse("Y");
			baseInfo.setLastModifyBy(employee.getWorkerCode());
			baseInfo.setLastModifyDate(new java.util.Date());
			baseInfo.setEnterpriseCode(employee.getEnterpriseCode());
			baseInfo=infoRemote.save(baseInfo);
		}
		write("{success:true,msg:'设备基础信息保存成功！',EquBaseId:'"+baseInfo.getEquBaseId()+"'}");
	}
	
	public void saveOrUpdateBaseAnnex() throws IOException{
		String annexId=request.getParameter("annexId");
		String baseInfoId=request.getParameter("infoId");
		if (annex != null) {
			String filePath = request.getParameter("filePath");
			String fileName=request.getParameter("annexName");
			int ssdex = filePath.lastIndexOf("\\");
			java.io.FileInputStream fis = new java.io.FileInputStream(annex);
			byte[] data = new byte[(int) fis.available()];
			fis.read(data);
			String Temp = uploadFile(annex, filePath.substring(ssdex + 1), "EquBaseAnnex");
			EquJBaseAnnex baseAnnex=new EquJBaseAnnex();
			if (annexId!=null&&!annexId.equals("")) {
				baseAnnex=annexRemote.findById(Long.parseLong(annexId));
				
			}
			if (fileName!=null&&!fileName.equals("")) {
				baseAnnex.setFileName(fileName);
			}
			if (baseInfoId!=null&&!baseInfoId.equals("")) {
				baseAnnex.setEquBaseId(Long.parseLong(baseInfoId));
			}
			baseAnnex.setAnnex(Temp);
			baseAnnex.setEnterpriseCode(employee.getEnterpriseCode());
			baseAnnex.setIsUse("Y");
			baseAnnex.setLastModifiedBy(employee.getWorkerCode());
			baseAnnex.setLastModifiedDate(new java.util.Date());
			if (baseAnnex.getAnnexId()!=null) {
				annexRemote.update(baseAnnex);
			} else {
				annexRemote.save(baseAnnex);
			}
			write("{success:true,msg:'附件保存成功！'}");
		}
	}
	/**
	 * 删除设备基础信息
	 */
	public void deleteBaseInfo(){
		String EquBaseId=request.getParameter("EquBaseId");
		infoRemote.delete(EquBaseId);
		write("{success:true,msg:'设备基础信息删除成功！'}");
	}
	/**
	 * 删除设备附件信息
	 */
	public void deleteBaseAnnex(){
		String ids=request.getParameter("ids");
		annexRemote.delete(ids);
		write("{success:true,msg:'设备附件信息删除成功！'}");
	}
	/**
	 * 查找设备基础信息
	 * By 设备编码
	 * @throws JSONException 
	 * @throws ParseException 
	 */
	public void findBaseInfoByAttributeCode() throws JSONException, ParseException{
		String code=request.getParameter("code");
		List list=infoRemote.findByAttributeCode(code, employee.getEnterpriseCode());
		if (list!=null&&list.size()>0) {
			baseInfo=new EquJBaseInfo();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Object[] objects=(Object[])list.get(0);
			if (objects!=null) {
				if (objects[0]!=null) {
					baseInfo.setEquBaseId(Long.parseLong(objects[0].toString()));
				}
				if (objects[1]!=null) {
					baseInfo.setAttributeCode(objects[1].toString());
				}
				if (objects[2]!=null) {
					baseInfo.setManufacturer(objects[2].toString());
				}
				if (objects[3]!=null) {
					baseInfo.setModel(objects[3].toString());
				}
				if (objects[4]!=null) {
					baseInfo.setFactoryDate(format.parse(objects[4].toString()));
				}
				if (objects[5]!=null) {
					baseInfo.setInstallationDate(format.parse(objects[5].toString()));
				}
				if (objects[6]!=null) {
					baseInfo.setInstallationCode(objects[6].toString());
				}
				if (objects[8]!=null) {
					baseInfo.setPrice(Double.parseDouble(objects[8].toString()));
				}
				if (objects[9]!=null) {
					baseInfo.setUseYear(objects[9].toString());
				}
				if (objects[10]!=null) {
					baseInfo.setAssetCode(objects[10].toString());
				}
				if (objects[11]!=null) {
					baseInfo.setTechnicalParameters(objects[11].toString());
				}
				if (objects[12]!=null) {
					baseInfo.setOneResponsible(objects[12].toString());
				}
				if (objects[15]!=null) {
					baseInfo.setTwoResponsible(objects[15].toString());
				}
				if (objects[18]!=null) {
					baseInfo.setThreeResponsible(objects[18].toString());
				}
				String Str=JSONUtil.serialize(baseInfo);
				write("{success:true,installationDesc:'"+objects[7]+"',oneResponsibleName:'"+objects[13]+"',oneDeptName:'"+objects[14]+"',twoResponsibleName:'"+
						objects[16]+"',twoDeptName:'"+objects[17]+"',threeResponsibleName:'"+objects[19]+"',threeDeptName:'"+objects[20]+"',data:'"+Str+"'}");
			}
		}else{
			write("{success:false}");
		}
	}
	/**
	 * 查找设备附件信息
	 * By 设备基础信息Id
	 * @throws JSONException 
	 */
	public void findBaseAnnexByBaseId() throws JSONException{
		String EquBaseId=request.getParameter("equBaseId");
		PageObject pg=annexRemote.findByBaseId(EquBaseId, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	//get/set 方法
	public EquJBaseInfo getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(EquJBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
}
