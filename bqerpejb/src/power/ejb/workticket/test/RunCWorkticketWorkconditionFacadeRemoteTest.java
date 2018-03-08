package power.ejb.workticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketWorkcondition;
import power.ejb.workticket.RunCWorkticketWorkconditionFacadeRemote;

public class RunCWorkticketWorkconditionFacadeRemoteTest {
	protected static RunCWorkticketWorkconditionFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCWorkticketWorkconditionFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketWorkconditionFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException{
		RunCWorkticketWorkcondition model=new RunCWorkticketWorkcondition();
		model.setConditionName("停电");
		model.setEnterpriseCode("hfdc");
		RunCWorkticketWorkcondition entity=remote.save(model);
		System.out.print(entity.getConditionId());
	}

	@Test
	public void testDelete() {
		remote.deleteMutil("3,4");
	}

	@Test
	public void testUpdate() throws CodeRepeatException{
		RunCWorkticketWorkcondition model=remote.findById(Long.valueOf(1));
		model.setConditionName("不停电");
		remote.update(model);
	}


	@Test
	public void testFindByNameOrId() {
		List list=remote.findByNameOrId("hfdc", "机停电");
		System.out.print(list.size());
	}

}
