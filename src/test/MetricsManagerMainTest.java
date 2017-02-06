package test;

import java.lang.reflect.Method;

import org.junit.Test;

import manager.MetricsManager;

public class MetricsManagerMainTest {
	@Test
	public void testConfirmComment() throws Exception {
		MetricsManager metricsManager  = new MetricsManager();
		String line = "org.apache.derby.authentication.SystemPrincipal";
		Method method = MetricsManager.class.getDeclaredMethod(
				"extractClassName", String.class );
		method.setAccessible(true);
		String actual = (String)method.invoke(metricsManager, line);
		System.out.println( actual );
	}

}
