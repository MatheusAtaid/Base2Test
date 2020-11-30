package Suporte;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Generator {
	
	public static String dataHora() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		return new SimpleDateFormat("yyyyMMddhhmmss").format(ts);
	}
}
