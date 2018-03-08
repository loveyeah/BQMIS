package power.ejb.manage.contract.business.test;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.ejb.manage.contract.form.ConDocForm;

public class ConJConDocFacadeRemoteTest {

	private static ConJConDocFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (ConJConDocFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ConJConDocFacade");
	}
	@Test
	public void testSave(){
		ConJConDoc model=new ConJConDoc();
		model.setKeyId(4L);
		model.setDocType("CONEVI");
		model.setDocName("hello");
		model.setOriFileName("hello");
		model.setOriFileExt("txt");
		model.setLastModifiedDate(new Date());
		model.setEnterpriseCode("hfdc");
		model.setIsUse("Y");
		model.setLastModifiedBy("999999");
		ConJConDoc entity = remote.save(model);
		System.out.println(entity.getConDocId());
		
	}
	
	@Test
	public void testDelete(){
		Long id=1l;
		remote.delete(id);
	}
	@Test
	public void testdeleteMulti(){
	
		remote.deleteMulti("1,2");
	}
	@Test
	public void testUpdate()
	{
		ConJConDoc model=new ConJConDoc();
		model = remote.findById(2L);
		model.setKeyId(32L);
		model.setDocType("CONEVI");
		model.setDocName("gg");
		model.setOriFileExt("doc");
		model.setOriFileName("hh");
		model.setLastModifiedDate(new Date());
		model.setLastModifiedBy("999999");
		remote.update(model);
	}
	
	@Test
	public void testFindConDocList()
	{
		PageObject result=remote.findConDocList("hfdc",5L, "CON",Long.parseLong("15"));	
		System.out.print(result.getList().size());
		
	}
	@Test
	public void testfindConDocModel()
	{
		ConJConDoc docform= remote.findConDocModel("hfdc", 4l, "CONEVI");
		System.out.print(docform);
	}
}
