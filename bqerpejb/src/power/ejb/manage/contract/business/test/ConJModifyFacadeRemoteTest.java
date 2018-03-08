package power.ejb.manage.contract.business.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJModify;
import power.ejb.manage.contract.business.ConJModifyFacadeRemote;
import power.ejb.manage.contract.form.ConModifyForm;

public class ConJModifyFacadeRemoteTest {
	private static ConJModifyFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(ConJModifyFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ConJModifyFacade");
	}

	@Test
	public void testSave() {
		ConJModify model =  new ConJModify();
		model.setConomodifyType(1L);
		model.setEnterpriseCode("hfdc");	
		model.setEntryBy("999999");
		model.setEntryDate(new Date());	
		model.setFileStatus("DRF");
		model.setFileBy("abc");
		model.setFileDate(new Date());
		model.setFileNo("000");
		model.setConId(1L);		
		remote.save(model);
	}

	@Test
	public void testDelete() {
		remote.delete(remote.findById(1l));
	}

	@Test
	public void testUpdate() {
		ConJModify model =  new ConJModify();
		model = remote.findById(1l);
		model.setConomodifyType(1L);
		model.setEnterpriseCode("hfdc");	
		model.setEntryBy("999999");
		model.setEntryDate(new Date());	
		model.setFileStatus("DDD");
		model.setFileBy("test");
		model.setFileDate(new Date());
		model.setFileNo("008");
		model.setConId(2L);		
		remote.update(model);
	}
	@Test
	public void findConModifyModel() {
		ConModifyForm form = remote.findConModifyModel(15L);
		List list=form.getMconAttlist();
		System.out.print(list.size());
		//System.out.print(form);
	}
	@Test
	public void testFindConModifyList() {
		
		PageObject obj = remote.findConModifyList("",Long.parseLong("2"),"hfdc","2006-10-01", "2009-12-30", "%");
		System.out.print(obj.getTotalCount());
	}

}
