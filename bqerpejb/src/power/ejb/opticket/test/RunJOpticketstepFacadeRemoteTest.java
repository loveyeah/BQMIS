package power.ejb.opticket.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.RunJOpticketstep;
import power.ejb.opticket.RunJOpticketstepFacadeRemote;

public class RunJOpticketstepFacadeRemoteTest {
	protected static RunJOpticketstepFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunJOpticketstepFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJOpticketstepFacade");
	}

	@Test
	public void testSave() {
		RunJOpticketstep entity=new RunJOpticketstep();
		entity.setDisplayNo(Long.valueOf(1));
		entity.setOperateStepName("step");
		entity.setOpticketCode("0011");
		System.out.print(remote.save(entity).getOperateStepId());
	}

	@Test
	public void testDelete() {
		RunJOpticketstep entity=remote.findById(Long.valueOf(1));
		remote.delete(entity);
	}

	@Test
	public void testUpdate() {
		RunJOpticketstep entity=remote.findById(Long.valueOf(2));
		entity.setOperateStepName("xiugai");
		System.out.print(remote.update(entity).getOperateStepName());
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

}
