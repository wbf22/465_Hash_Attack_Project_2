import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tests {

    private boolean DEBUG = true;

    private Random random = new Random();

    @Test
    public void collisionImageAttack_8_10_16_24_bits() throws Exception {


        System.out.println("******8 bit******");
        List<DataPoint> dataPoints8 = doCollisionAttack(8);
        System.out.println();
        System.out.println("******10 bit******");
        List<DataPoint> dataPoints10 = doCollisionAttack(10);
        System.out.println();
        System.out.println("******16 bit******");
        List<DataPoint> dataPoints16 = doCollisionAttack(16);
        System.out.println();
        System.out.println("******24 bit******");
        List<DataPoint> dataPoints24 = doCollisionAttack(24);
        System.out.println();

        System.out.println("******Averages******");
        System.out.println("8-bit " + average(dataPoints8));
        System.out.println("10-bit " + average(dataPoints10));
        System.out.println("16-bit " + average(dataPoints16));
        System.out.println("24-bit " + average(dataPoints24));
        System.out.println();
    }

    @Test
    public void preImageAttack_8_10_16_24_bits() throws Exception {
        List<String> stringList = getStringList(50);

        System.out.println("******Pre Image Attack******");
        System.out.println("Secret Strings:");
        for (String s : stringList) {
            System.out.println(s);
        }
        System.out.println();


        System.out.println("******8 bit******");
        List<DataPoint> dataPoints8 = doPreImageAttack(8, stringList);
        System.out.println();
        System.out.println("******10 bit******");
        List<DataPoint> dataPoints10 = doPreImageAttack(10, stringList);
        System.out.println();
        System.out.println("******16 bit******");
        List<DataPoint> dataPoints16 = doPreImageAttack(16, stringList);
        System.out.println();
        System.out.println("******24 bit******");
        List<DataPoint> dataPoints24 = doPreImageAttack(24, stringList);
        System.out.println();

        System.out.println("******Averages******");
        System.out.println("8-bit " + average(dataPoints8));
        System.out.println("10-bit " + average(dataPoints10));
        System.out.println("16-bit " + average(dataPoints16));
        System.out.println("24-bit " + average(dataPoints24));
        System.out.println();
    }

    @Test
    public void test() throws Exception {
//        System.out.println(SHA.getBytesInHex(SHA.encrypt("ABCDEFG", 12)));
//        System.out.println(SHA.encrypt("ABCDEFG", 1).length);
//        System.out.println(SHA.getBytesInHex(SHA.encrypt("ABCDEF1", 12)));
//        System.out.println(SHA.encrypt("ABCDEF1", 1).length);


        System.out.println(SHA.asBitsArray(SHA.encrypt("ABCDEF1", 18)));
//        01000001 01111110 10000000
//        01000001 01111110 10010101
//        4170
//        417e

    }


    private List<DataPoint> doCollisionAttack(int numBits) throws Exception {

        List<DataPoint> dataPoints = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int cntr = 1;
            String one = getRandomString();
            String two = getRandomString();

            while(!areEqualByteArrays(SHA.encrypt(one, numBits), SHA.encrypt(two, numBits))) {
                one = getRandomString();
                two = getRandomString();
                cntr++;

            }

            System.out.print(".");

            dataPoints.add(new DataPoint(one, two, cntr));
        }

        if (DEBUG == true) {
            System.out.println();
            System.out.println("******Results " + numBits + " bits******");
            System.out.println("Data Points:");
            for (DataPoint i : dataPoints) {
                System.out.println("one=" + i.getOne() + " two=" + i.getTwo() + " iterations=" +
                        i.getNumIterationsUntilMatch());
            }
            System.out.println("# of iterations until match");
            for (DataPoint i : dataPoints) {
                System.out.println(i.getNumIterationsUntilMatch());
            }
        }
        return dataPoints;
    }

    private List<DataPoint> doPreImageAttack(int numBits, List<String> secretList) throws Exception {


        List<DataPoint> dataPoints = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int cntr = 1;
            String secret = secretList.get(i);
            byte[] secretHash = SHA.encrypt(secret, numBits);
            String rand = getRandomString();
            while(!areEqualByteArrays(secretHash, SHA.encrypt(rand, numBits))) {
                rand = getRandomString();
                cntr++;
            }

            System.out.print(".");

            dataPoints.add(new DataPoint(secret, rand, cntr));
        }

        if (DEBUG == true) {
            System.out.println();
            System.out.println("******Results " + numBits + " bits******");
            System.out.println("Data Points:");
            for (DataPoint i : dataPoints) {
                System.out.println("secret=" + i.getOne() + " rand=" + i.getTwo() + " iterations=" + i.getNumIterationsUntilMatch());
            }
            System.out.println("# of iterations until match");
            for (DataPoint i : dataPoints) {
                System.out.println(i.getNumIterationsUntilMatch());
            }
        }
        return dataPoints;
    }

    private List<String> getStringList(int length) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(getRandomString());
        }
        return list;
    }

    private String getRandomString() {
        return getRandomString(32);
    }

    private String getRandomString(int length) {
        byte[] array = new byte[length]; // length is bounded by 7
        for(int i = 0; i < array.length; i++) {
            array[i] = (byte) (random.nextInt(74) + 48);
        }
        return new String(array, StandardCharsets.UTF_8);
    }

    private boolean areEqualByteArrays(byte[] one, byte[] two) {
        for (int i = 0; i < one.length; i++) {
            if (one[i] != two[i]) {
                return false;
            }
        }
        return true;
    }

    private double average(List<DataPoint> dataPoints) {
        double total = 0;
        for (DataPoint d : dataPoints) {
            total += d.getNumIterationsUntilMatch();
        }
        return total / dataPoints.size();
    }

}
