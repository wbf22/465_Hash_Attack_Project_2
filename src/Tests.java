import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tests {

    private boolean DEBUG = true;

    @Test
    public void collisionAttack_8bits() throws Exception {
        String secret = getRandomString();
        doCollisionAttack(1, secret);
    }

    @Test
    public void collisionAttack_16bits() throws Exception {
        String secret = getRandomString();
        doCollisionAttack(2, secret);
    }

    @Test
    public void collisionAttack_24bits() throws Exception {
        String secret = getRandomString();
        doCollisionAttack(3, secret);
    }

    @Test
    public void collisionAttack_32bits() throws Exception {
        String secret = getRandomString();
        doCollisionAttack(4, secret);
    }

    @Test
    public void collisionAttack_8_16_24_32_bits() throws Exception {
        String secret = getRandomString();

        System.out.println("******Secret******");
        System.out.println(secret);
        System.out.println(SHA.getBytesInHex(SHA.encrypt(secret, 1)));
        System.out.println(SHA.getBytesInHex(SHA.encrypt(secret, 2)));
        System.out.println(SHA.getBytesInHex(SHA.encrypt(secret, 3)));
        System.out.println(SHA.getBytesInHex(SHA.encrypt(secret, 4)));
        System.out.println();


        System.out.println("******8 bit******");
        List<DataPoint> dataPoints8 = doCollisionAttack(1, secret);
        System.out.println();
        System.out.println("******16 bit******");
        List<DataPoint> dataPoints16 = doCollisionAttack(2, secret);
        System.out.println();
        System.out.println("******24 bit******");
        List<DataPoint> dataPoints24 = doCollisionAttack(3, secret);
        System.out.println();
        System.out.println("******32 bit******");
        List<DataPoint> dataPoints32 = doCollisionAttack(4, secret);
        System.out.println();

        System.out.println("******Averages******");
        System.out.println("8-bit " + average(dataPoints8));
        System.out.println("16-bit " + average(dataPoints16));
        System.out.println("24-bit " + average(dataPoints24));
        System.out.println("32-bit " + average(dataPoints32));
        System.out.println();
    }

    @Test
    public void test() throws Exception {
        String secret = "wo(eK";
        byte[] encryption = SHA.encrypt(secret, 1);
        System.out.println(SHA.getBytesInHex(encryption));

    }



    private List<DataPoint> doCollisionAttack(int numBytes, String secret) throws Exception {
        byte[] secretHash = SHA.encrypt(secret, numBytes);

        List<DataPoint> dataPoints = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int cntr = 1;
            String rand = getRandomString();
            while(!areEqualByteArrays(secretHash, SHA.encrypt(rand, numBytes))) {
                rand = getRandomString();
                cntr++;

            }

            System.out.print(".");

            dataPoints.add(new DataPoint(secret, rand, cntr));
        }

        if (DEBUG == true) {
            System.out.println();
            System.out.println("******Results " + numBytes * 8 + " bits******");
            System.out.println("Data Points:");
            for (DataPoint i : dataPoints) {
                System.out.println("rand=" + i.getRand() + " iterations=" + i.getNumIterationsUntilMatch());
            }
            System.out.println("# of iterations until match");
            for (DataPoint i : dataPoints) {
                System.out.println(i.getNumIterationsUntilMatch());
            }
        }
        return dataPoints;
    }

    private String getRandomString() {
        Random random = new Random();//System.nanoTime()
        byte[] array = new byte[random.nextInt(32)]; // length is bounded by 7
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
