import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;


public class SHA {

    public static byte[] encrypt(String x, int numBytes) throws Exception {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        d.reset();
        byte[] xBytes = Arrays.copyOfRange(x.getBytes(StandardCharsets.UTF_8), 0, numBytes);
        d.update(xBytes);
        return d.digest();

//        // Convert byte array into signum representation
//        BigInteger no = new BigInteger(1, messageDigest);
//
//        // Convert message digest into hex value
//        String hashtext = no.toString(16);
//
//        //convert to binary and truncate to numbits
//        String binary = asBits(hashtext);
//        return binary.substring(32, 32 + numbits);
    }


    //Auxiliary Functions
    public static String asBits(String s) {
        return asBitsArray(s.getBytes(StandardCharsets.UTF_8));
    }

    public static String asBits(int by) {

        long[] binary = getAsArrayOf1sAndOs(by);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            stringBuilder.append(binary[i]);
        }

        return stringBuilder.toString();
    }

    public static String asBits(int by, int bits) {

        long[] binary = getAsArrayOf1sAndOs(by);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = bits - 1; i >= 0; i--) {
            stringBuilder.append(binary[i]);
        }

        return stringBuilder.toString();
    }

    public static String asBitsArray(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for( byte b : bytes){
            stringBuilder.append(asBits(b));
        }
        return stringBuilder.toString();
    }

    public static void asBitsPrint(int by) {

        long[] binary = getAsArrayOf1sAndOs(by);

        for (int i = 7; i >= 0; i--) {
            System.out.print(binary[i] + "");
        }

        System.out.println();
    }

    public static void asBitsPrint(int by, int bits) {

        long[] binary = getAsArrayOf1sAndOs(by);

        for (int i = bits - 1; i >= 0; i--) {
            System.out.print(binary[i] + "");
        }

        System.out.println();
    }

    public static void asBitsPrintArray(byte[] bytes) {
        for( byte b : bytes){
            System.out.println(asBits(b));
        }
    }

    public static byte[] convertIntArrayToByteArray(int[] source) {
        byte[] bytes = new byte[source.length * 4];
        for (int i = 0; i < source.length; i++) {
            byte[] row = toBytes(source[i]);
            bytes[i*4] = row[0];
            bytes[i*4 + 1] = row[1];
            bytes[i*4 + 2] = row[2];
            bytes[i*4 + 3] = row[3];
        }
        return bytes;
    }

    public static int[] convertByteArrayToIntArray(byte[] source) {
        int[] ints = new int[source.length / 4];
        for (int i = 0; i < ints.length; i++) {
            int key = makeInt(asBits(source[i*4]) + asBits(source[i*4 + 1]) + asBits(source[i*4 + 2]) + asBits(source[i*4 + 3]));
            ints[i] = key;
        }
        return ints;
    }

    public static byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }

    public static String getBytesInHex(byte[] bytes) {
        byte[] t = transpose(bytes);
        StringBuilder bytesAsBinary = new StringBuilder();
        for (byte b : t) {
            bytesAsBinary.append(asBits(b));
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytesAsBinary.toString().length(); i+=4) {
            switch (bytesAsBinary.substring(i, i+4)){
                case "0000":
                    stringBuilder.append("0");
                    break;
                case "0001":
                    stringBuilder.append("1");
                    break;
                case "0010":
                    stringBuilder.append("2");
                    break;
                case "0011":
                    stringBuilder.append("3");
                    break;
                case "0100":
                    stringBuilder.append("4");
                    break;
                case "0101":
                    stringBuilder.append("5");
                    break;
                case "0110":
                    stringBuilder.append("6");
                    break;
                case "0111":
                    stringBuilder.append("7");
                    break;
                case "1000":
                    stringBuilder.append("8");
                    break;
                case "1001":
                    stringBuilder.append("9");
                    break;
                case "1010":
                    stringBuilder.append("a");
                    break;
                case "1011":
                    stringBuilder.append("b");
                    break;
                case "1100":
                    stringBuilder.append("c");
                    break;
                case "1101":
                    stringBuilder.append("d");
                    break;
                case "1110":
                    stringBuilder.append("e");
                    break;
                case "1111":
                    stringBuilder.append("f");
                    break;
            }
        }
        return stringBuilder.toString();
    }

    public static void printBytesInHexOneLine(byte[] bytes) {
        System.out.println(getBytesInHex((bytes)));
    }

    public static void printBytesInHex(byte[] bytes) {
        for (int i = 0; i < bytes.length; i += 4) {
            int asInt = makeInt("0000000000000000" + asBits(bytes[i]) + asBits(bytes[i + 1]) );
            System.out.print(Integer.toString(asInt, 16));
            asInt = makeInt("0000000000000000"  + asBits(bytes[i + 2]) + asBits(bytes[i + 3]) );
            System.out.println(Integer.toString(asInt, 16));
        }
        System.out.println();
    }

    public static void printByteInHex(byte b) {
        int asInt = makeInt("000000000000000000000000" + asBits(b) );
        System.out.println(Integer.toString(asInt, 16));
    }

    public static byte[] transpose(byte[] bytes) {
        byte[] newArray = new byte[bytes.length];
        int offset = 0;
        for (int i = 0; i < 4; i++) {
            newArray[i] = bytes[offset];
            newArray[i + 4] = bytes[offset + 1];
            newArray[i + 8] = bytes[offset + 2];
            newArray[i + 12] = bytes[offset + 3];
            offset +=4 ;
        }
        return newArray;
    }


    //private methods
    private static long[] getAsArrayOf1sAndOs(int by) {
        // Creating and assigning binary array size
        long[] binary = new long[35];
        int id = 0;

        long us = Integer.toUnsignedLong(by);
        // Number should be positive
        while (us > 0) {
            binary[id++] = us % 2;
            us = us / 2;
        }
        return binary;
    }

    public static byte makeByte(String bits) {
        byte by = 0;
        for (int i = bits.length() - 1; i >= 0 ; i--) {
            if (Integer.parseInt("" + bits.charAt(i)) == 1) {
                by = (byte) setBit(by, bits.length() - 1 -i);
            }
        }
        return by;
    }

    public static int makeInt(String bits) {
        int by = 0;
        for (int i = bits.length() - 1; i >= 0 ; i--) {
            if (Integer.parseInt("" + bits.charAt(i)) == 1) {
                by = (int) setBit(by, bits.length() - 1 -i);
            }
        }
        return by;
    }

    private static long setBit(long by, int bit){
        return (by | (1<<bit) );
    }
}
