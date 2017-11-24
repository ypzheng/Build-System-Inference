import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class TestSuite1 {
	File file = new File("test/TestBuildFile1.xml");
	AntBuildAnalyzer aba = new AntBuildAnalyzer(file);
	
	
	
	@Test
	public void srcCompileTarget() {
		
		assertEquals("compile", aba.getCompileSrcTarget());
	}

}
