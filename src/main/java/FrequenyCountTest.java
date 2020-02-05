import com.google.gson.Gson;
import de.unidue.phrasedetector.FrequencyCountPipeline;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.uima.jcas.JCas;


public class FrequenyCountTest {

    public String addressCloud;
    public String addressServer;
    private HttpConnector httpConnector;
    private String params;
    private FrequencyCountPipeline frequenyCountPipeline;
    private TestHelper helper;

    public FrequenyCountTest(String addressCloud, String addressServer){
        this.addressCloud = addressCloud;
        this.addressServer = addressServer;
        this.httpConnector = new HttpConnector();
        this.params = createParamString();
        this.frequenyCountPipeline = new FrequencyCountPipeline();
        this.helper = new TestHelper();
    }

    public TestResponse runTest() throws Exception {

        TestResponse testResponse = new TestResponse();
        StopWatch localWatch = new StopWatch();
        StopWatch cloudWatch = new StopWatch();
        StopWatch serverWatch = new StopWatch();

        Long localExecutionTime;
        Long cloudExecutionTime;
        Long serverExecutionTime;


        // run local test
        localWatch.start();
        JCas local_response = frequenyCountPipeline.run(this.params);
        localWatch.stop();

        // evaluate time of local execution
        localExecutionTime = localWatch.getNanoTime();

        // run cloud test
        cloudWatch.start();
        String cloud_response = httpConnector.postRequest(addressCloud, params);
        cloudWatch.stop();

        // evaluate time of cloud execution
        cloudExecutionTime = cloudWatch.getNanoTime();

        // run cloud test
        serverWatch.start();
        String server_response = httpConnector.postRequest(addressServer, params);
        serverWatch.stop();
        serverExecutionTime = serverWatch.getNanoTime();

        String local_response_xmi = helper.getXMIFromJCas(local_response);
        JCas cloud_response_jcas  = helper.getJCasFromXMI(cloud_response);
        JCas server_response_jcas  = helper.getJCasFromXMI(server_response);

        // are the same ?
        Boolean cloud_local_equl = helper.isEqual(cloud_response, local_response_xmi);
        Boolean server_local_equl = helper.isEqual(server_response, local_response_xmi);
        System.out.println(local_response_xmi);
        // fill response object
        testResponse.setAnalysisName("FrequencyCountPipeline");
        testResponse.setRunTimeContainer(cloudExecutionTime);
        testResponse.setRunTimeLocal(localExecutionTime);
        testResponse.setRunTimeServer(serverExecutionTime);
        testResponse.setResultEqualLocalToServer( server_local_equl );
        testResponse.setServerDeserialized( server_response_jcas instanceof JCas );
        testResponse.setCloudDeserialized( cloud_response_jcas instanceof JCas );

        return testResponse;

    }


    private String createParamString() {

        ParameterTestRunner testParameter = new ParameterTestRunner();
        testParameter.language = "de";
        testParameter.text = "Bacon ipsum dolor amet picanha porchetta boudin, jowl tail biltong brisket corned beef turducken beef meatloaf. Ball tip buffalo ham hock ribeye bresaola short loin swine shankle doner frankfurter. Pork loin short ribs salami ground round sausage short loin beef shank cow ham chuck rump buffalo leberkas. Bresaola alcatra cupim ham, shankle tongue beef ribs porchetta filet mignon leberkas venison prosciutto pork chuck. ";

        return new Gson().toJson(testParameter);
    }


}
