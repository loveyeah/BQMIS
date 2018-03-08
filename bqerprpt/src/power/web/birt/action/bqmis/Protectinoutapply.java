package power.web.birt.action.bqmis;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.run.protectinoutapply.ProtectApplyApprove;
import power.ejb.run.protectinoutapply.RunJProtectinoutApprove;
import power.ejb.run.protectinoutapply.RunJProtectinoutapply;
import power.ejb.run.protectinoutapply.form.ProtectinoutapplyPrintModel;
import power.web.birt.bean.bqmis.ProtectinoutapplyBean;

public class Protectinoutapply {
	
	public ProtectinoutapplyBean getModel(String protectNo) {
		ProtectApplyApprove print =  (ProtectApplyApprove) Ejb3Factory.getInstance()
		.getFacadeRemote("ProtectApplyApproveImp");
		 ProtectinoutapplyPrintModel printmodel = print.getModel(protectNo);
		 ProtectinoutapplyBean bean = new ProtectinoutapplyBean();
		 List<RunJProtectinoutapply> baseList= printmodel.getBaseList();
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 kk时mm分");
		 for(int i = 0; i < baseList.size();i++) {
			 RunJProtectinoutapply model = baseList.get(i);
			 if("N".equals(model.getIsIn())) {
				 bean.setApplyDept(model.getApplyDept());
				 bean.setApplyBy(model.getApplyBy());
				 bean.setApplyDate(sf.format(model.getApplyDate()));
				 bean.setProtectName(model.getProtectName());
				 bean.setProtectReason(model.getProtectReason());
				 bean.setOutSafety(model.getOutSafety());
				 bean.setExecuteBy(model.getExecuteBy());
				 bean.setKeeper(model.getKeeper());
				 bean.setCheckupBy(model.getCheckupBy());
				 if(model.getProtectOutDate() != null && !"".equals(model.getProtectOutDate())) {
					 bean.setProtectOutDate(sf.format(model.getProtectOutDate()));
				 }
				 bean.setMemo(model.getMemo());
			 }else if("Y".equals(model.getIsIn())) {
				 if("".equals(bean.getApplyDept())){
					 bean.setApplyDept(model.getApplyDept());
				 }
				 if("".equals(bean.getApplyBy())){
					 bean.setApplyBy(model.getApplyBy());
				 }
				 bean.setProtectInReason(model.getProtectReason());
				 bean.setInApplyDate(sf.format(model.getApplyDate()));
				 bean.setInstruction(model.getOutSafety());
				 bean.setInExecuteBy(model.getExecuteBy());
				 bean.setInKeeper(model.getKeeper());
				 bean.setInPermitBy(model.getPermitBy());
				 bean.setInCheckupBy(model.getCheckupBy());
				 if(model.getProtectInDate() != null && !"".equals(model.getProtectInDate())) {
					 bean.setProtectInDate(sf.format(model.getProtectInDate()));
				 }
			 }
		 }
		 
//		 bean.setEquCode(model.getBaseModel().getEquCode());
//		 bean.setProtectReason(model.getBaseModel().getProtectReason());
//		 bean.setEquEffect(model.getBaseModel().getEquEffect());
//		 bean.setOutSafety(model.getBaseModel().getOutSafety());
//		 bean.setApplyStartDate(sf.format(model.getBaseModel().getApplyStartDate()));
//		 bean.setApplyEndDate(sf.format(model.getBaseModel().getApplyEndDate()));
//		 bean.setApproveStartDate(sf.format(model.getBaseModel().getApproveStartDate()));
//		 bean.setApproveEndDate(sf.format(model.getBaseModel().getApproveEndDate()));
//		 bean.setExecuteBy(model.getBaseModel().getExecuteBy());
//		 bean.setKeeper(model.getBaseModel().getKeeper());
//		 bean.setPermitBy(model.getBaseModel().getPermitBy());
//		 bean.setCheckupBy(model.getBaseModel().getCheckupBy());
//		 bean.setProtectInDate(sf.format(model.getBaseModel().getProtectInDate()));
		 
		 if(printmodel.getApproveOutList() != null ) {
			 Iterator<RunJProtectinoutApprove> it = printmodel.getApproveOutList().iterator();
			 while(it.hasNext()) {
				 RunJProtectinoutApprove approveModel = it.next();
				 if(approveModel.getApproveStatus() == 4) {
					 bean.setMechanic(approveModel.getApproveBy());
					 bean.setMechanicDate(sf.format(approveModel.getApproveDate()));
				 } else if(approveModel.getApproveStatus() == 5){
					 bean.setDirector(approveModel.getApproveBy());
					 bean.setDirectorDate(sf.format(approveModel.getApproveDate()));
				 } else if(approveModel.getApproveStatus() == 6){
					 bean.setFactoryManager(approveModel.getApproveBy());
					 bean.setFactoryManagerDate(sf.format(approveModel.getApproveDate()));
				 } else if(approveModel.getApproveStatus() == 8) {
					 bean.setPermitBy(approveModel.getApproveBy());
				 } else if(approveModel.getApproveStatus() == 9) {
					 bean.setCheckupBy(approveModel.getApproveBy());
				 }
			 }
		 }
		 if(printmodel.getApproveInList() != null ) {
			 Iterator<RunJProtectinoutApprove> it = printmodel.getApproveInList().iterator();
			 while(it.hasNext()) {
				 RunJProtectinoutApprove approveModel = it.next();
				 if(approveModel.getApproveStatus() == 4) {
					 bean.setInMechanic(approveModel.getApproveBy());
					 bean.setInMechanicDate(sf.format(approveModel.getApproveDate()));
				 } else if(approveModel.getApproveStatus() == 5){
					 bean.setInDirector(approveModel.getApproveBy());
					 bean.setInDirectorDate(sf.format(approveModel.getApproveDate()));
				 } else if(approveModel.getApproveStatus() == 6){
					 bean.setInFactoryManager(approveModel.getApproveBy());
					 bean.setInFactoryManagerDate(sf.format(approveModel.getApproveDate()));
				 } else if(approveModel.getApproveStatus() == 8) {
					 bean.setInPermitBy(approveModel.getApproveBy());
				 } else if(approveModel.getApproveStatus() == 9) {
					 bean.setInCheckupBy(approveModel.getApproveBy());
				 }
			 }
		 }
		 return bean;
	} 
}
