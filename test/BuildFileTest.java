 import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class BuildFileTest {

	AntBuildAnalyzer aba0 = new AntBuildAnalyzer(new File("test/TestBuildFile0.xml"),"");
	AntBuildAnalyzer aba1 = new AntBuildAnalyzer(new File("test/TestBuildFile1.xml"),"");
	AntBuildAnalyzer aba2 = new AntBuildAnalyzer(new File("test/TestBuildFile2.xml"),"");
	AntBuildAnalyzer aba3 = new AntBuildAnalyzer(new File("test/TestBuildFile3.xml"),"");
	AntBuildAnalyzer aba4 = new AntBuildAnalyzer(new File("test/TestBuildFile4.xml"),"");
	AntBuildAnalyzer aba5 = new AntBuildAnalyzer(new File("test/TestBuildFile5.xml"),"");

	//Duplicate
	AntBuildAnalyzer aba6 = new AntBuildAnalyzer(new File("test/TestBuildFile6.xml"),"");
	AntBuildAnalyzer aba7 = new AntBuildAnalyzer(new File("test/Consumer2/build.xml"),"");

	//Duplicate
	AntBuildAnalyzer aba8 = new AntBuildAnalyzer(new File("test/TestBuildFile8.xml"),"");
	AntBuildAnalyzer aba9 = new AntBuildAnalyzer(new File("test/TestBuildFile9.xml"),"");


	@Test
	public void getCompileSrcTarget() {

		assertEquals("compile", aba0.getCompileSrcTarget());
		assertEquals("compile", aba1.getCompileSrcTarget());
		assertEquals("build", aba2.getCompileSrcTarget());
		assertEquals("compile", aba3.getCompileSrcTarget());
		assertEquals("compile", aba4.getCompileSrcTarget());

		assertEquals("compile", aba5.getCompileSrcTarget());
		assertEquals("compile", aba6.getCompileSrcTarget());


		assertEquals("compile", aba7.getCompileSrcTarget());
		assertEquals("compile", aba8.getCompileSrcTarget());

		assertEquals("compile", aba9.getCompileSrcTarget());

		//Failed
		//assertEquals("build", aba9.getCompileSrcTarget());

	}

	@Test
	public void getCompileTestTarget() {
		assertEquals("compile.tests", aba0.getCompileTestTarget());
		assertEquals("compile", aba1.getCompileTestTarget());
		assertEquals("compile-tests", aba2.getCompileTestTarget());
		assertEquals("test", aba3.getCompileTestTarget());
		assertEquals("unittests", aba4.getCompileTestTarget());

		assertEquals("test", aba5.getCompileTestTarget());
		assertEquals("compile.tests", aba6.getCompileTestTarget());

		assertEquals("compile", aba7.getCompileTestTarget());
		assertEquals("compile-tests", aba8.getCompileTestTarget());
		assertEquals("compile-test", aba9.getCompileTestTarget());

		//Failed
		//assertEquals("build", aba9.getCompileTestTarget());
	}

	@Test
	public void getSrcDir() {
		assertEquals(Paths.get("src/main/java").toString(), aba0.getSrcDir());

		String aba1_expected=	Paths.get("../sandwich-shared/src").toString() + ", " +
								Paths.get("../sandwich-server/src").toString() + ", " +
								Paths.get("../sandwich-client/src").toString() + ", " +
								Paths.get("../sandwich-test/src").toString() + ", " +
								Paths.get("../sandwich-tests/src").toString() + ", " +
								Paths.get("../sandwich-tests/test").toString() + ", " +
								Paths.get("../sharedvalidation/src").toString() + ", " +
								Paths.get("../sharedvalidationexample/src").toString() + ", " +
								Paths.get("../sharedvalidationexample/test").toString() + ", " +
								Paths.get("../framework/src").toString() + ", " +
								Paths.get("src").toString() + ", " +
								Paths.get("unit").toString() + ", " +
								Paths.get("integration").toString();
		assertEquals(aba1_expected, aba1.getSrcDir());
		assertEquals(Paths.get("src/main").toString(), aba2.getSrcDir());
		assertEquals(Paths.get("src").toString(), aba3.getSrcDir());
		assertEquals(Paths.get("src").toString(), aba4.getSrcDir());

		assertEquals(Paths.get("src").toString(), aba5.getSrcDir());
		assertEquals(Paths.get("src/main/java").toString(), aba6.getSrcDir());

		String aba7_expected=	Paths.get("src\\com\\gurock\\testrail").toString() + ", " +
								Paths.get("src\\com\\d3\\testrails").toString() + ", " +
								Paths.get("src\\com\\d3\\utils").toString() + ", " +
								Paths.get("src\\com\\d3\\login").toString() + ", " +
								Paths.get("src\\com\\d3\\accounts").toString() + ", " +
								Paths.get("src\\com\\d3\\help").toString() + ", " +
								Paths.get("src\\com\\d3\\dashboard").toString() + ", " +
								Paths.get("src\\com\\d3\\messages").toString() + ", " +
								Paths.get("src\\com\\d3\\transactions").toString() + ", " +
								Paths.get("src\\com\\d3\\moneyMovement").toString() + ", " +
								Paths.get("src\\com\\d3\\planning").toString() + ", " +
								Paths.get("src\\com\\d3\\settings").toString() + ", " +
								Paths.get("src\\com\\d3\\endToEnd").toString();

		assertEquals(aba7_expected, aba7.getSrcDir());
		assertEquals(Paths.get("src/main/java").toString(), aba9.getSrcDir());

		//Failed because <javac><src><pathelement location = xxxx></src></javac> pattern not handled
//		assertEquals(Paths.get("src/java"), aba8.getSrcDir());

		//Failed
		//assertEquals(Paths.get("").toString(), aba9.getSrcDir());

	}

	@Test
	public void getTestDir() {
		assertEquals(Paths.get("src/test/java").toString(), aba0.getTestDir());

		String aba1_expected=	Paths.get("../sandwich-shared/src").toString() + ", " +
				Paths.get("../sandwich-server/src").toString() + ", " +
				Paths.get("../sandwich-client/src").toString() + ", " +
				Paths.get("../sandwich-test/src").toString() + ", " +
				Paths.get("../sandwich-tests/src").toString() + ", " +
				Paths.get("../sandwich-tests/test").toString() + ", " +
				Paths.get("../sharedvalidation/src").toString() + ", " +
				Paths.get("../sharedvalidationexample/src").toString() + ", " +
				Paths.get("../sharedvalidationexample/test").toString() + ", " +
				Paths.get("../framework/src").toString() + ", " +
				Paths.get("src").toString() + ", " +
				Paths.get("unit").toString() + ", " +
				Paths.get("integration").toString();
		assertEquals(aba1_expected, aba1.getTestDir()); //There are multiple, need to work on this
		assertEquals(Paths.get("src/tests/junit").toString(), aba2.getTestDir());
		assertEquals(Paths.get("test").toString(), aba3.getTestDir());
		assertEquals(Paths.get("src").toString(), aba4.getTestDir());

		assertEquals(Paths.get("test").toString(), aba5.getTestDir());
		assertEquals(Paths.get("src/test/java").toString(), aba6.getTestDir());

		String aba7_expected=	Paths.get("src\\com\\gurock\\testrail").toString() + ", " +
				Paths.get("src\\com\\d3\\testrails").toString() + ", " +
				Paths.get("src\\com\\d3\\utils").toString() + ", " +
				Paths.get("src\\com\\d3\\login").toString() + ", " +
				Paths.get("src\\com\\d3\\accounts").toString() + ", " +
				Paths.get("src\\com\\d3\\help").toString() + ", " +
				Paths.get("src\\com\\d3\\dashboard").toString() + ", " +
				Paths.get("src\\com\\d3\\messages").toString() + ", " +
				Paths.get("src\\com\\d3\\transactions").toString() + ", " +
				Paths.get("src\\com\\d3\\moneyMovement").toString() + ", " +
				Paths.get("src\\com\\d3\\planning").toString() + ", " +
				Paths.get("src\\com\\d3\\settings").toString() + ", " +
				Paths.get("src\\com\\d3\\endToEnd").toString();
		assertEquals(aba7_expected, aba7.getTestDir());
		assertEquals(Paths.get("src/test/java").toString(), aba9.getTestDir());

		//Failed because <javac><src><pathelement location = xxxx></src></javac> pattern not handled
//		assertEquals("src/test", aba8.getTestDir());
		//Failed
		//assertEquals(Paths.get("").toString(), aba9.getTestDir());
	}

	@Test
	public void getCompDir() {
		assertEquals(Paths.get("target/classes").toString(), aba0.getCompDir());
		assertEquals(Paths.get("../build/classes").toString(), aba1.getCompDir()); //There are multiple, need to work on this
		assertEquals(Paths.get("build/classes").toString(), aba2.getCompDir());
		assertEquals(Paths.get("target/classes").toString(), aba3.getCompDir());
		assertEquals(Paths.get("build/classes").toString(), aba4.getCompDir());

		assertEquals(Paths.get("target/classes").toString(), aba5.getCompDir());
		assertEquals(Paths.get("target/classes").toString(), aba6.getCompDir());
		assertEquals(Paths.get("build").toString(), aba7.getCompDir());
		assertEquals(Paths.get("target/classes").toString(), aba8.getCompDir());
		assertEquals(Paths.get("target/classes").toString(), aba9.getCompDir());

		//Failed
		//assertEquals(Paths.get("").toString(), aba9.getCompDir());
	}

	@Test
	public void getCompTestDir() {
		assertEquals(Paths.get("target/tests").toString(), aba0.getCompTestDir());
		assertEquals(Paths.get("../build/classes").toString(), aba1.getCompTestDir()); //There are multiple, need to work on this
		assertEquals(Paths.get("build/testcases").toString(), aba2.getCompTestDir());
		assertEquals(Paths.get("target/test-classes").toString(), aba3.getCompTestDir());
		assertEquals(Paths.get("build/classes").toString(), aba4.getCompTestDir());

		assertEquals(Paths.get("target/test-classes").toString(), aba5.getCompTestDir());
		assertEquals(Paths.get("target/tests").toString(), aba6.getCompTestDir());
		assertEquals(Paths.get("build").toString(), aba7.getCompTestDir());
		assertEquals(Paths.get("target/test-classes").toString(), aba8.getCompTestDir());
		assertEquals(Paths.get("target/test-classes").toString(), aba9.getCompTestDir());
		//Failed
		//assertEquals(Paths.get("").toString(), aba9.getCompTestDir());
	}

	@Test
	public void getSrcDep() {
		assertEquals("",aba0.getSrcDep());
		assertEquals("",aba1.getSrcDep());
		assertEquals("",aba2.getSrcDep());
		assertEquals("",aba3.getSrcDep());

		assertEquals("",aba5.getSrcDep());
		assertEquals("",aba6.getSrcDep());
		String aba7_expected = Paths.get("libs/selendroid-client-0.13.0.jar").toString()+";" +
							   Paths.get("libs/selendroid-standalone-0.13.0-with-dependencies.jar").toString()+";" +
							   Paths.get("libs/testng-6.8.8.jar").toString()+";" +
							   Paths.get("libs/json-simple-1.1.1.jar").toString()+";" +
							   Paths.get("libs/selenium-java-2.44.0.jar").toString() + ";"+
							   Paths.get("libs/selenium-server-standalone-2.44.0.jar").toString();
		assertEquals(aba7_expected,aba7.getSrcDep());
		assertEquals("",aba9.getSrcDep());

		String aba8_expected = Paths.get("xerces-2.4.0.jar").toString()+";" +
				   				Paths.get("servletapi-2.4.jar").toString()+";" +
				   				Paths.get("jsp-api-2.0.jar").toString()+";" +
				   				Paths.get("xml-apis-2.0.2.jar").toString()+";" +
				   				Paths.get("jdom-1.0.jar").toString() + ";"+
				   				Paths.get("commons-logging-1.1.jar").toString() + ";"+
				   				Paths.get("junit-3.8.1.jar").toString() + ";"+
				   				Paths.get("mockrunner-jdk1.3-j2ee1.3-0.4.jar").toString() + ";"+
				   				Paths.get("commons-beanutils-1.7.0.jar").toString();

		//Failed because getting dependencies from online library case is not handled
		//assertEquals(aba8_expected,aba8.getSrcDep());
		//Failed
		//assertEquals("",aba9.getSrcDep());

	}

	@Test
	public void getTestDep() {
		assertEquals(Paths.get("${junit.home}/junit-3.8.1.jar").toString(),aba0.getTestDep());
		assertEquals("",aba1.getTestDep());
		assertEquals("",aba2.getTestDep());
		assertEquals("",aba3.getTestDep());

		assertEquals("",aba5.getTestDep());
		assertEquals("${junit.home}/junit-3.8.1.jar",aba6.getTestDep());
		String aba7_expected = Paths.get("libs/selendroid-client-0.13.0.jar").toString()+";" +
							   Paths.get("libs/selendroid-standalone-0.13.0-with-dependencies.jar").toString()+";" +
							   Paths.get("libs/testng-6.8.8.jar").toString()+";" +
							   Paths.get("libs/json-simple-1.1.1.jar").toString()+";" +
							   Paths.get("libs/selenium-java-2.44.0.jar").toString() + ";"+
							   Paths.get("libs/selenium-server-standalone-2.44.0.jar").toString();
		assertEquals(aba7_expected,aba7.getTestDep());
		assertEquals("",aba9.getTestDep());

		String aba8_expected = Paths.get("xerces-2.4.0.jar").toString()+";" +
   				Paths.get("servletapi-2.4.jar").toString()+";" +
   				Paths.get("jsp-api-2.0.jar").toString()+";" +
   				Paths.get("xml-apis-2.0.2.jar").toString()+";" +
   				Paths.get("jdom-1.0.jar").toString() + ";"+
   				Paths.get("commons-logging-1.1.jar").toString() + ";"+
   				Paths.get("junit-3.8.1.jar").toString() + ";"+
   				Paths.get("mockrunner-jdk1.3-j2ee1.3-0.4.jar").toString() + ";"+
   				Paths.get("commons-beanutils-1.7.0.jar").toString();

		//Failed because getting dependencies from online library case is not handled
//		assertEquals(aba8_expected, aba8.getTestDep());
		//Failed
		//assertEquals("",aba9.getTestDep());


	}

	@Test
	public void getTestList() {
		Map<String, String> keyVal0 = new HashMap<String, String>();
		keyVal0.put("include", "**/*Test.java;");
		keyVal0.put("exclude", "**/Abstract*Test.java;**/EntitiesPerformanceTest.java;**/RandomUtilsFreqTest.java;");
		keyVal0.put("dir", Paths.get("src/test/java").toString());

		Map<String, String> keyVal1 = new HashMap<String, String>();
		keyVal1.put("include", "com.sandwich.test.AllTests;");
		keyVal1.put("exclude", "");
		keyVal1.put("dir", "");

		Map<String, String> keyVal3 = new HashMap<String, String>();
		keyVal3.put("include", "**/*Test.class");
		keyVal3.put("exclude", "");
		keyVal3.put("dir", Paths.get("target/test-classes").toString());

		Map<String, String> keyVal4 = new HashMap<String, String>();
		keyVal4.put("include", "com.pensioenpage.jynx.ods2csv.tests.AllTests;");
		keyVal4.put("exclude", "");
		keyVal4.put("dir", "");

		Map<String, String> keyVal5 = new HashMap<String, String>();
		keyVal5.put("include", "**/*Test.class");
		keyVal5.put("exclude", "");
		keyVal5.put("dir", Paths.get("target/test-classes").toString());

		Map<String, String> keyVal6 = new HashMap<String, String>();
		keyVal6.put("include", "**/*Test.java;");
		keyVal6.put("exclude", "**/*AbstractTest.java;");
		keyVal6.put("dir", Paths.get("src/test/java").toString());

		Map<String, String> keyVal8 = new HashMap<String, String>();
		keyVal6.put("include", "**/*Test.java;");
		keyVal6.put("exclude", "");
		keyVal6.put("dir", Paths.get("src/test").toString());

		assertEquals(keyVal0.toString(), aba0.getTestList());
		assertEquals(keyVal1.toString(),aba1.getTestList());
		assertEquals("",aba2.getTestList());
		assertEquals(keyVal3.toString(),aba3.getTestList());
		assertEquals(keyVal4.toString(),aba4.getTestList());
		assertEquals(keyVal5.toString(),aba5.getTestList());
		assertEquals(keyVal6.toString(),aba6.getTestList());
		assertEquals("",aba7.getTestList());
		assertEquals(keyVal8.toString(),aba8.getTestList());

		Map<String, String> keyVal10 = new HashMap<String, String>();
		keyVal10.put("include", "**/*Test*.java;");
		keyVal10.put("exclude", "");
		keyVal10.put("dir", "src\\test\\java");

		assertEquals(keyVal10.toString(),aba9.getTestList());




		//Failed
		//assertEquals("",aba9.getTestList());
	}
}
