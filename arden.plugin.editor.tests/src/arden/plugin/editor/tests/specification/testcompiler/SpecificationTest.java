package arden.plugin.editor.tests.specification.testcompiler;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import org.junit.Rule;

import arden.plugin.editor.tests.specification.testcompiler.TestCompilerResult.TestCompilerOutputText;
import arden.plugin.editor.tests.specification.testcompiler.impl.TestCompilerPluginImpl;

/**
 * Base test class which all tests extend. Adds useful asserts, annotations and shared access
 * to the compiler.
 */
public abstract class SpecificationTest {
	
	/** Initialise your compiler here. It will be used by all tests. */
	private final TestCompiler compiler = new TestCompilerPluginImpl();
	
	// Decorator which intercepts calls to the compiler to skip unsupported tests
	private TestCompiler runtimeCheckedCompiler = new TestCompiler() {
		public TestCompilerResult compileAndRun(String code) throws TestCompilerException {
			// only run tests if runtime is supported
			assumeTrue("Compiler doesn't support runtime tests", isRuntimeSupported());
			return compiler.compileAndRun(code);
		}
		@Override
		public boolean isRuntimeSupported() {
			return compiler.isRuntimeSupported();
		}
		@Override
		public boolean isVersionSupported(ArdenVersion version) {
			return compiler.isVersionSupported(version);
		}
		@Override
		public void compile(String code) throws TestCompilerCompiletimeException {
			compiler.compile(code);
		}
		@Override
		public TestCompilerMappings getMappings() {
			TestCompilerMappings mappings = compiler.getMappings();
			// only run test if mappings are provided
			assumeNotNull("Compiler doesn't support tests with mappings", mappings);
			return compiler.getMappings();
		};
	};
	
	protected TestCompiler getCompiler() {
		return runtimeCheckedCompiler;
	}
	
	protected TestCompilerMappings getMappings() {
		return runtimeCheckedCompiler.getMappings();
	}
	
	// Used for backward compatibility tests with the <code>@Compatibility</code> annotation.
	@Rule
	public CompatibilityRule comaptibilityRule = new CompatibilityRule(getCompiler());
	
	
	/**
	 * Tests if the result of the evaluated expressions is equal to the expected
	 * string (case insensitive).
	 */
	protected void assertEvaluatesTo(String expression, String expected) throws TestCompilerException {
		assertEvaluatesToWithData(null, expression, expected);
	}
	
	protected void assertEvaluatesToWithData(String dataCode, String expression, String expected) throws TestCompilerException {
		ArdenCodeBuilder builder;
		if(dataCode != null) {
			builder = new ArdenCodeBuilder(dataCode);
		} else {
			builder = new ArdenCodeBuilder();
		}
		String code = builder.addExpression(expression).toString();
		assertReturns(code, expected);
	}
	
	protected void assertReturns(String code, String... expected) throws TestCompilerException {
		if(!getCompiler().isRuntimeSupported()) {
			assertValid(code);
			return;
		}
		
		TestCompilerResult result = getCompiler().compileAndRun(code);
		if(expected.length == 0) {
			// no return values
			if(result.returnValues.isEmpty()) {
				// test passed
			} else {
				// a single "NULL" is also okay
				assertEquals("Too many return values.", 1, result.returnValues.size());
				assertEquals("null", result.returnValues.get(0).toLowerCase());
			}
		} else if (expected.length == 1) {
			// single return value
			assertEquals("Wrong number of return values.", 1, result.returnValues.size());
			String returnValue = result.returnValues.get(0);
			assertEquals(expected[0].toLowerCase(), returnValue.toLowerCase());
		} else {
			// multiple return values
			assertEquals("Too many or few return values.", expected.length, result.returnValues.size());
			String[] expected_lowercase = new String[expected.length];
			String[] returnValues_lowercase = new String[result.returnValues.size()];
			for (int i = 0; i < expected.length; i++) {
				expected_lowercase[i] = expected[i].toLowerCase();
				returnValues_lowercase[i] = result.returnValues.get(i).toLowerCase();
			}
			assertArrayEquals(expected_lowercase, returnValues_lowercase);
		}
	}
	
	protected void assertNoReturn(String code) throws TestCompilerException {
		assertReturns(code); // no expected values
	}
	
	protected void assertWrites(String code, String expected) throws TestCompilerException {
		if(!getCompiler().isRuntimeSupported()) {
			assertValid(code);
			return;
		}
		
		TestCompilerResult result = getCompiler().compileAndRun(code);
		TestCompilerOutputText outputText = result.outputTexts.get(0);
		assertEquals(expected, outputText.text);
	}
	
	protected void assertValidStatement(String statement) throws TestCompilerException {
		String code = new ArdenCodeBuilder().addAction(statement).toString();
		assertValid(code);
	}
	
	protected void assertInvalidStatement(String statement) throws TestCompilerException {
		String code = new ArdenCodeBuilder().addAction(statement).toString();
		assertInvalid(code);
	}
	
	protected void assertInvalidExpression(String expression) throws TestCompilerException {
		String code = new ArdenCodeBuilder().addExpression(expression).toString();
		assertInvalid(code);
	}
	
	protected void assertValid(String code) throws TestCompilerCompiletimeException {
		getCompiler().compile(code);
	}
	
	protected void assertInvalid(String code) {
		try {
			getCompiler().compile(code);
			fail("Expected a " + TestCompilerCompiletimeException.class.getSimpleName() + " to be thrown.");
		} catch(TestCompilerCompiletimeException e) {
			// test passed
		}
	}
	
}
