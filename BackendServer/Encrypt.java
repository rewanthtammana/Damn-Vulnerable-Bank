import java.util.*;
import org.json.simple.*;

public class Encrypt {
    static public String secret = "amazing";
    static public int secretLength = secret.length();

    public static void main(String []args)  {
        System.out.println(0 ^ 1);
        String response = "{\"enc_data\":\"GmdBWksdEwAZFAlLVEdDX1FKS0JtQU1DHggaBkNXQQFjTkdBTUMJBgMCFQUIFA5MXUFPDxUdBg4PCkNWY05HQU1DFAYaDwgDBlhTTkUSAgwfHQcJBk9rWkkTbRw=\"}";
        JSONObject data = new JSONObject();
        String decryptedString = decrypt(data.getJSONObject("enc_data").toString());
        // String enc = operate("foobar");
        // String msg = "foobar";
        // String encMsg = encrypt(msg);
        // String decMsg = decrypt(encMsg);
        // System.out.println("Encrypted Value: " + encMsg);
        System.out.println("Decrypted Value: " + decryptedString);
    }

    public static String operate(String input) {
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            int xorVal = (int) input.charAt(i) ^ (int) secret.charAt(i % secretLength);

            char xorChar =  (char) xorVal;

            // System.out.println("Char: " + input.charAt(i) + " Value: " + (int) input.charAt(i) + " Xor: "  + xorVal + " Xor Char: " + xorChar);

            result += xorChar; 

        }       
        return result;
    }

    public static String encrypt(String input) {
        String encVal = operate(input);
        String val = Base64.getEncoder().encodeToString(encVal.getBytes());
        

        return val;
    }

    public static String decrypt(String input) {
        byte[] decodeByte = Base64.getDecoder().decode(input);
        String decodeString = new String(decodeByte);
        String decString = operate(decodeString);

        return decString;
    }
}

