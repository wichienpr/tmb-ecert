package th.co.baiwa.buckwaframework.common.util;

import java.io.OutputStream;

public class EcertFileUtils {
	
	public static void closeStream (OutputStream  out) {
		if(out == null) return ;
		try {
			out.close();
		}catch (Exception e) {
			// skip is ok
		}
	}

}
