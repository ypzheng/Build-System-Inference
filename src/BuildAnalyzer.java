import java.io.File;
import java.util.Vector;
public interface BuildAnalyzer {
	public String getCompileSrcTarget();
	public String getCompileTestTarget();
	public String getSrcDir();
	public String getTestDir();
	public String getCompDir();
	public String getCompTestDir();
	public String getSrcDep();
	public String getTestDep();
	public String getTestList();
}
