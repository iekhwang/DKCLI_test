public class TestRunner {
    String localServerAddress = "http://localhost:8080/analysis";
    String localCloudAddress = "http://localhost:3000/analysis";

    public static void main (String [] args) throws Exception {
        runFrequencyCountTest();
    }
    

    public static void runFrequencyCountTest() throws Exception {

        String localServerAddress = "http://localhost:8085/analysis";
        String localCloudAddress = "http://localhost:3000/analysis";

        // setup test
        FrequenyCountTest frequenyCountTest = new FrequenyCountTest(localCloudAddress, localServerAddress);
        TestResponse testResponse = frequenyCountTest.runTest();

        // response
        testResponse.print();
    }
}
