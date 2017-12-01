import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Driver {

	public static void main(String[] args) {
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
	}

}
