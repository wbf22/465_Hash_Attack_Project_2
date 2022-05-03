public class DataPoint {
    private String secret;
    private String rand;
    int numIterationsUntilMatch;

    public DataPoint(String secret, String rand, int numIterationsUntilMatch) {
        this.secret = secret;
        this.rand = rand;
        this.numIterationsUntilMatch = numIterationsUntilMatch;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRand() {
        return rand;
    }

    public void setRand(String rand) {
        this.rand = rand;
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
                "secret='" + secret + '\'' +
                ", rand='" + rand + '\'' +
                ", numIterationsUntilMatch=" + numIterationsUntilMatch +
                '}';
    }
}
