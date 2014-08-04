package utils;

public class GlobalUtil {

	public static Integer tryParseInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
	public static Long tryParseLong(String text) {
		try {
			return Long.parseLong(text);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}
