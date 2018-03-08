package power.ejb.comm;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;

public class NativeSqlHelperRemoteTest {
    static NativeSqlHelperRemote bll  ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance()
		.getFacadeRemote("NativeSqlHelper");
	}

	@Test
	public void testGetSingalString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetMaxId() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSingalStringObjectArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testQueryByNativeSQLStringIntArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testQueryByNativeSQLStringObjectArrayIntArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testQueryByNativeSQLStringClassIntArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testQueryByNativeSQLStringObjectArrayClassIntArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testExeNativeSQLString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testExeNativeSQLStringObjectArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testExeNativeSQLListOfString() {
		String sql = "select * from run_j_runlog_main t order by t.run_logno  desc,t.run_logid desc";
		List list = bll.queryByNativeSQL(sql,0,5);
		for(int i=0;i<list.size();i++)
		{
			Object[] obj = (Object[]) list.get(i);
			System.out.println(obj[1] +"  "+obj[2]+"\n"); 
		}
		list = bll.queryByNativeSQL(sql,5,5);
		for(int i=0;i<list.size();i++)
		{
			Object[] obj = (Object[]) list.get(i);
			System.out.println(obj[1] +"  "+obj[2]+"\n"); 
		}
		list = bll.queryByNativeSQL(sql,10,5);
		for(int i=0;i<list.size();i++)
		{
			Object[] obj = (Object[]) list.get(i);
			System.out.println(obj[1] +"  "+obj[2]+"\n"); 
		}
	}

}
