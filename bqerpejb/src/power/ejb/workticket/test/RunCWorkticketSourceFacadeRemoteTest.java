package power.ejb.workticket.test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketSource;
import power.ejb.workticket.RunCWorkticketSourceFacadeRemote;

public class RunCWorkticketSourceFacadeRemoteTest {

	protected static RunCWorkticketSourceFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCWorkticketSourceFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketSourceFacade");
	} 
	@Test
	public void testSave() {
		RunCWorkticketSource model= new RunCWorkticketSource();
		model.setEnterpriseCode("hfdc");
		model.setSourceName("检修");
		model.setModifyBy("999999");
		RunCWorkticketSource entity;
		try {
			entity = remote.save(model);
			System.out.print(entity.getSourceId());
		} catch (CodeRepeatException e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testDelete() {
		remote.deleteMutil("2");
	}

	@Test
	public void testUpdate() {
		RunCWorkticketSource model=  remote.findById(Long.valueOf(3));
		model.setEnterpriseCode("hfdc");
		model.setSourceName("定检");
		model.setModifyBy("999999");
		RunCWorkticketSource entity;
		try {
			entity = remote.update(model);
			System.out.print(entity.getSourceId());
		} catch (CodeRepeatException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindById() {
		//
	}
 
	@Test
	public void testFindByNameOrId() {
		List list =remote.findByNameOrId("hfdc", "大修");
		System.out.println(list.size());
	}

}
