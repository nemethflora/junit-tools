package hu.zforgo.junit.tools.groups;

import hu.zforgo.junit.tools.annotations.AlwaysRun;
import hu.zforgo.junit.tools.annotations.Groups;
import hu.zforgo.junit.tools.rules.GroupRule;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

@Groups("classLevel")
public class MethodRuleTest {
	@Rule
	public GroupRule groupRule = new GroupRule();

	@Test
	@Groups({"live", "slow", "daily"})
	public void groupTest() {
		System.out.println("groupTest");
//		Assert.assertTrue("Hiba van", false);
	}

	@Test
	@Groups({"live", "dev"})
	public void groupTest2() {
		System.out.println("groupTest2");
	}

	@Test
	@AlwaysRun
	public void alwaysRunTest() {
		System.out.println("alwaysRunTest");
	}


	@Test
	public void skiptest() {
		System.out.println("skiptest");
	}

	@Test
	@Groups("methodLevel")
	public void methodLevel() {
		System.out.println("methodLevel");
	}

	@Test
//	@Ignore
	public void ignoredTest() {
		System.out.println("ignoredTest");
	}

}
