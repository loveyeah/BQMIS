package power.ejb.workticket.test;


import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketFireContent;
import power.ejb.workticket.RunCWorkticketFireContentFacadeRemote;

public class RunCWorkticketFireContentFacadeRemoteTest {
	protected static RunCWorkticketFireContentFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketFireContentFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketFireContentFacade");
	}
	@Test
	public void testSave() throws CodeRepeatException{
			RunCWorkticketFireContent model= new RunCWorkticketFireContent();
			model.setEnterpriseCode("hfdc");
			model.setFirecontentName("电焊");
			model.setModifyBy("999999");
			RunCWorkticketFireContent entity=remote.save(model);
			System.out.print(entity.getFirecontentId());
	}
	@Test
	public void testDelete(){
		remote.deleteMutil("1");
	}
	@Test
	public void testUpdate() throws CodeRepeatException{
		RunCWorkticketFireContent model= remote.findById(Long.valueOf(1));
		model.setEnterpriseCode("hfdc");
		model.setFirecontentName("电割");
		model.setModifyBy("999999");
		RunCWorkticketFireContent entity=remote.update(model);
		System.out.print(entity.getFirecontentName());
	}
	@Test
	public void testFindByNameOrId(){
		List list=remote.findByNameOrId("hfdc", "2");
		System.out.print(list.size());
	}
}
