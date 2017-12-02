import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class TestSuite1 {
	
	AntBuildAnalyzer aba0 = new AntBuildAnalyzer(new File("test/TestBuildFile0.xml"));
	AntBuildAnalyzer aba1 = new AntBuildAnalyzer(new File("test/TestBuildFile1.xml"));
	AntBuildAnalyzer aba2 = new AntBuildAnalyzer(new File("test/TestBuildFile2.xml"));
	AntBuildAnalyzer aba3 = new AntBuildAnalyzer(new File("test/TestBuildFile3.xml"));
	AntBuildAnalyzer aba4 = new AntBuildAnalyzer(new File("test/TestBuildFile4.xml"));
	
	
	
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
		assertEquals("src/main/java", aba0.getSrcDir());
		//assertEquals("../sandwich-shared/src", aba1.getSrcDir());
		assertEquals("src/main", aba2.getSrcDir());
		assertEquals("src", aba3.getSrcDir());
		assertEquals("src", aba4.getSrcDir());
	}
	
	@Test
	public void getTestDir() {
		assertEquals("src/test/java", aba0.getTestDir());
		//assertEquals("../sandwich-shared/src", aba1.getTestDir()); //There are multiple, need to work on this
		assertEquals("src/tests/junit", aba2.getTestDir());
		assertEquals("test", aba3.getTestDir());
		assertEquals("src", aba4.getTestDir());
	}
	
	@Test
	public void getCompDir() {
		assertEquals("target/classes", aba0.getCompDir());
		assertEquals("../build/classes", aba1.getCompDir()); //There are multiple, need to work on this
		assertEquals("build/classes", aba2.getCompDir());
		assertEquals("target/classes", aba3.getCompDir());
		assertEquals("build/classes", aba4.getCompDir());
	}
	
	@Test
	public void getCompTestDir() {
		assertEquals("target/tests", aba0.getCompTestDir());
		assertEquals("../build/classes", aba1.getCompTestDir()); //There are multiple, need to work on this
		assertEquals("build/testcases", aba2.getCompTestDir());
		assertEquals("target/test-classes", aba3.getCompTestDir());
		assertEquals("build/classes", aba4.getCompTestDir());
	}
	
}
