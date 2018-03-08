package power.ejb.workticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketFlag;
import power.ejb.workticket.RunCWorkticketFlagFacadeRemote;

public class RunCWorkticketFlagFacadeRemoteTest {
	protected static RunCWorkticketFlagFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCWorkticketFlagFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketFlagFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException{
		RunCWorkticketFlag model= new RunCWorkticketFlag();
		model.setEnterpriseCode("hfdc");
		model.setFlagName(":");
		RunCWorkticketFlag entity=remote.save(model);
		System.out.print(entity.getFlagId());
	}

	@Test
	public void testDelete() {
		remote.deleteMulti("1,2");
	}

	@Test
	public void testUpdate() throws CodeRepeatException{
		RunCWorkticketFlag model= remote.findById(Long.valueOf(2));
		model.setFlagName("!");
		RunCWorkticketFlag entity=remote.update(model);
		System.out.print(entity.getFlagName());
	}
	@Test
	public void testFindByNameOrId() {
		List list=remote.findByNameOrId("hfdc", "!");
		System.out.print(list.size());
	}

}
