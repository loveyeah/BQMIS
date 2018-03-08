package power.ejb.manage.contract.business.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.contract.business.ConJPaymentPlan;
import power.ejb.manage.contract.business.ConJPaymentPlanFacadeRemote;

public class ConJPaymentPlanFacadeRemoteTest {
	protected static ConJPaymentPlanFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(ConJPaymentPlanFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ConJPaymentPlanFacade");
	}

	@Test
	public void testSave() {
		ConJPaymentPlan model=new ConJPaymentPlan();
		model.setConId(1L);
		model.setEnterpriseCode("hfdc");
		model.setIsUse("Y");
		model.setLastModifiedBy("999999");
		model.setLastModifiedDate(new Date());
		model.setPayDate(new Date());
		model.setPaymentMoment("abc");
		model.setPayPrice(12d);
		remote.save(model);
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByConId() {
		List list=remote.findByConId(1L);
		System.out.print(list.size());
	}

}
