public class DataPoint {
    private String one;
    private String two;
    int numIterationsUntilMatch;

    public DataPoint(String secret, String rand, int numIterationsUntilMatch) {
        this.one = secret;
        this.two = rand;
        this.numIterationsUntilMatch = numIterationsUntilMatch;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public int getNumIterationsUntilMatch() {
        return numIterationsUntilMatch;
    }

    public void setNumIterationsUntilMatch(int numIterationsUntilMatch) {
        this.numIterationsUntilMatch = numIterationsUntilMatch;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "secret='" + one + '\'' +
                ", rand='" + two + '\'' +
                ", numIterationsUntilMatch=" + numIterationsUntilMatch +
                '}';
    }
}
