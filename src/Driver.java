import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Driver {

	public static void main(String[] args) throws IOException {
		Ex_Get_Depend parser = new Ex_Get_Depend();
        Map<String, ArrayList<String>> dependencies = parser.getDependencies();
        for(String key : dependencies.keySet()) {
            ArrayList<String> values = dependencies.get(key);
            System.out.print(key +" is dependent on ");
            for (String d: values) {
                System.out.print(d+", ");
            }
            System.out.println();
        }
//		File buildFile = new File("src/build.xml");
//		File outputFile = new File("build.properties");
//		PropertyWriter pw = new PropertyWriter("ant", buildFile, outputFile);
	}

}
