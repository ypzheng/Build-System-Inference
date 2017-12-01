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
		
		
		assertEquals("", aba0.getCompSrcDir());
	}
	
	@Test
	public void getCompileTestTarget() {
		assertEquals("compile.tests", aba0.getCompileTestTarget());
		assertEquals("compile", aba1.getCompileTestTarget());
		assertEquals("compile-tests", aba2.getCompileTestTarget());
		assertEquals("test", aba3.getCompileTestTarget());
		assertEquals("unittests", aba4.getCompileTestTarget());
	}
}
