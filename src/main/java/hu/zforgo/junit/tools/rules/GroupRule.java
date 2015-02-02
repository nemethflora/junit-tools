package hu.zforgo.junit.tools.rules;

import hu.zforgo.junit.tools.annotations.AlwaysRun;
import hu.zforgo.junit.tools.annotations.Groups;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class GroupRule implements TestRule {

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				List<Throwable> errors = new ArrayList<>();
				try {
					if (shouldEvaluate(description)) {
						base.evaluate();
					}
				} catch (AssumptionViolatedException e) {
					errors.add(e);
					skipped(e, description, errors);
				} catch (Throwable e) {
					errors.add(e);
				}

				MultipleFailureException.assertEmpty(errors);
			}
		};
	}

	private void skipped(AssumptionViolatedException e, Description description, List<Throwable> errors) {
		try {
			Logger.getLogger(getClass().getName()).finer(e.getMessage());
		} catch (Throwable e1) {
			errors.add(e1);
		}
	}

	private boolean shouldEvaluate(final Description desc) {
		final String groupsProperty = System.getProperty("junit.groups", "").trim();
		final Class<?> testClass = desc.getTestClass();
		if (null != desc.getAnnotation(AlwaysRun.class) || null != testClass.getAnnotation(AlwaysRun.class) || groupsProperty.length() == 0) {
			return true;
		}
		Groups methodGroups = desc.getAnnotation(Groups.class);
		Groups classGroups = testClass.getAnnotation(Groups.class);
		if (methodGroups == null && classGroups == null) {
			return false;
		}

		Set<String> allGroups = new HashSet<>();
		if (null != methodGroups) {
			Collections.addAll(allGroups, methodGroups.value());
		}
		if (null != classGroups) {
			Collections.addAll(allGroups, classGroups.value());
		}
		List<String> enabledGroups = parsedParams(groupsProperty);
		for (String g : allGroups) {
			if (enabledGroups.contains(g.trim().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private List<String> parsedParams(final String orig) {
		String[] split = orig.split(",");
		List<String> result = new ArrayList<>(split.length);
		for (String item : split) {
			result.add(item.trim().toLowerCase());
		}
		return result;
	}
}
