package power.ejb.workticket.test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketFirelevel;
import power.ejb.workticket.RunCWorkticketFirelevelFacadeRemote;

public class RunCWorkticketFirelevelFacadeRemoteTest {
	protected static RunCWorkticketFirelevelFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCWorkticketFirelevelFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketFirelevelFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException{
		RunCWorkticketFirelevel model=new RunCWorkticketFirelevel();
		model.setEnterpriseCode("hfdc");
		model.setFirelevelName("一级动火票");
		RunCWorkticketFirelevel entity=remote.save(model);
		System.out.print(entity.getFirelevelId());
	}

	@Test
	public void testDelete() {
		remote.deleteMulti("2");
		
	}

	@Test
	public void testUpdate() throws CodeRepeatException{
		RunCWorkticketFirelevel model=remote.findById(Long.valueOf(1));
		model.setFirelevelName("二级");
		RunCWorkticketFirelevel entity=remote.update(model);
		System.out.print(entity.getFirelevelName());
	}
	
	@Test
	public void testFindByNameOrId() {
		List list=remote.findByNameOrId("hfdc", "一级动火票");
		System.out.print(list.size());
	}

}
