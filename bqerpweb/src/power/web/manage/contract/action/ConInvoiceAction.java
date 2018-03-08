package power.web.manage.contract.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ejb.manage.contract.ConCInvoice;
import power.ejb.manage.contract.ConCInvoiceFacadeRemote;
import power.ejb.manage.contract.form.BusinessManangerInfo;
import power.web.comm.AbstractAction;

public class ConInvoiceAction extends AbstractAction{
	private ConCInvoice invoice;
	protected ConCInvoiceFacadeRemote remote;
	public ConInvoiceAction(){
		remote=(ConCInvoiceFacadeRemote)factory.getFacadeRemote("ConCInvoiceFacade");
	}
	public void addInvoice() throws CodeRepeatException,JSONException{
		try{
			invoice.setEnterpriseCode(employee.getEnterpriseCode());
			invoice.setLastModifiedBy(employee.getWorkerCode());
			ConCInvoice model=remote.save(invoice);
			String str=JSONUtil.serialize(model);
			str="{success:true,ioc:"+str+"}";
			write(str);
		}catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}
	public void updateInvoice() throws CodeRepeatException,JSONException{
		try{
			ConCInvoice model=remote.findById(invoice.getInvoiceId());
			model.setInvoiceName(invoice.getInvoiceName());
			model.setLastModifiedDate(new Date());
			model.setMemo(invoice.getMemo());
			model.setTax(invoice.getTax());
			remote.update(model);
			String str=JSONUtil.serialize(model);
		str="{success:true,ioc:"+str+"}";
			write(str);
		}catch(CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}	
	}
	public void deleteInvoice() throws CodeRepeatException{
		ConCInvoice model=remote.findById(invoice.getInvoiceId());
		model.setIsUse("N");
		remote.delete(model);
	}
	public void findInvoice(){
		try{
			ConCInvoice model=remote.findById(invoice.getInvoiceId());
			String str=JSONUtil.serialize(model);
			write(str);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	public void findAllInvoices(){
		try{
			List<BusinessManangerInfo> list=remote.findAll();
			String str=JSONUtil.serialize(list);
			System.out.print(str);
			write(str);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	public ConCInvoice getInvoice() {
		return invoice;
	}
	public void setInvoice(ConCInvoice invoice) {
		this.invoice = invoice;
	}
}
