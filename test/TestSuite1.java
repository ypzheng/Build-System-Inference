import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;

public class TestSuite1 {
	
	AntBuildAnalyzer aba0 = new AntBuildAnalyzer(new File("test/TestBuildFile0.xml"),"");
	AntBuildAnalyzer aba1 = new AntBuildAnalyzer(new File("test/TestBuildFile1.xml"),"");
	AntBuildAnalyzer aba2 = new AntBuildAnalyzer(new File("test/TestBuildFile2.xml"),"");
	AntBuildAnalyzer aba3 = new AntBuildAnalyzer(new File("test/TestBuildFile3.xml"),"");
	AntBuildAnalyzer aba4 = new AntBuildAnalyzer(new File("test/TestBuildFile4.xml"),"");
	
	
	
	@Test
	public void getCompileSrcTarget() {
		
		assertEquals("compile", aba0.getCompileSrcTarget());
		assertEquals("compile", aba1.getCompileSrcTarget());
		assertEquals("build", aba2.getCompileSrcTarget());
		assertEquals("compile", aba3.getCompileSrcTarget());
		assertEquals("compile", aba4.getCompileSrcTarget());
		
	}
	
	@Test
	public void getCompileTestTarget() {
		assertEquals("compile.tests", aba0.getCompileTestTarget());
		assertEquals("compile", aba1.getCompileTestTarget());
		assertEquals("compile-tests", aba2.getCompileTestTarget());
		assertEquals("test", aba3.getCompileTestTarget());
		assertEquals("unittests", aba4.getCompileTestTarget());
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
	}
	
	@Test
	public void getCompDir() {
		assertEquals(Paths.get("target/classes").toString(), aba0.getCompDir());
		assertEquals(Paths.get("../build/classes").toString(), aba1.getCompDir()); //There are multiple, need to work on this
		assertEquals(Paths.get("build/classes").toString(), aba2.getCompDir());
		assertEquals(Paths.get("target/classes").toString(), aba3.getCompDir());
		assertEquals(Paths.get("build/classes").toString(), aba4.getCompDir());
	}
	
	@Test
	public void getCompTestDir() {
		assertEquals(Paths.get("target/tests").toString(), aba0.getCompTestDir());
		assertEquals(Paths.get("../build/classes").toString(), aba1.getCompTestDir()); //There are multiple, need to work on this
		assertEquals(Paths.get("build/testcases").toString(), aba2.getCompTestDir());
		assertEquals(Paths.get("target/test-classes").toString(), aba3.getCompTestDir());
		assertEquals(Paths.get("build/classes").toString(), aba4.getCompTestDir());
	}
	
	@Test
	public void getDep() {
		assertEquals("",aba0.getTestDep());
		
	}
	
}
