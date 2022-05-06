import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Tests {

    private boolean DEBUG = true;

    private Random random = new Random();

    //TODO try this algorithm https://www.geeksforgeeks.org/birthday-attack-in-cryptography/
    @Test
    public void collisionImageAttack_8_10_16_24_28_bits() {


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
        System.out.println("******28 bit******");
        List<DataPoint> dataPoints28 = doCollisionAttack(28);
        System.out.println();

        System.out.println("******Averages******");
        System.out.println("8-bit " + average(dataPoints8));
        System.out.println("10-bit " + average(dataPoints10));
        System.out.println("16-bit " + average(dataPoints16));
        System.out.println("24-bit " + average(dataPoints24));
        System.out.println("28-bit " + average(dataPoints28));
        System.out.println();
    }

    @Test
    public void preImageAttack_8_10_16_24_bits() {
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
    public void test() {
        System.out.println("******64 bit******");
        List<DataPoint> dataPoints = doCollisionAttack(14);
        System.out.println();

        System.out.println("******Averages******");
        System.out.println("64-bit " + average(dataPoints));
    }


    private List<DataPoint> doCollisionAttack(int numBits){

        List<DataPoint> dataPoints = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int oTimeConstant = (int)Math.pow(2, numBits/2);
            int cntr = 1;
            String one = null;
            String two = null;

            while (one == null && two == null) {
                List<String> strings = getStringList(oTimeConstant);
                List<byte[]> encrypted = strings.stream().map(t -> SHA.encrypt(t, numBits)).collect(Collectors.toList());
//                cntr += oTimeConstant;

                for (int o = 0; o < encrypted.size(); o++) {
                    cntr++;
                    for(int t = 0; t < encrypted.size(); t++){
                        if (o != t && areEqualByteArrays(encrypted.get(o), encrypted.get(t))) {
                            one = strings.get(o);
                            two = strings.get(t);
                            break;
                        }
                    }
                    if (one != null && two != null) break;
                }
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

    private List<DataPoint> doPreImageAttack(int numBits, List<String> secretList) {


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

    private List<DataPoint> doCollisionAttack(int numBits, int iterations){

        List<DataPoint> dataPoints = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            int oTimeConstant = (int)Math.pow(2, numBits/2);
            int cntr = 1;
            String one = null;
            String two = null;

            while (one == null && two == null) {
                List<String> strings = getStringList(oTimeConstant);
                List<byte[]> encrypted = strings.stream().map(t -> SHA.encrypt(t, numBits)).collect(Collectors.toList());

                for (int o = 0; o < encrypted.size(); o++) {
                    cntr++;
                    for(int t = 0; t < encrypted.size(); t++){
                        if (o != t && areEqualByteArrays(encrypted.get(o), encrypted.get(t))) {
                            one = strings.get(o);
                            two = strings.get(t);
                            break;
                        }
                    }
                    if (one != null && two != null) break;
                }
                if (cntr % (oTimeConstant/256) == 0) {
                    System.out.print(".");
                }
            }

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
