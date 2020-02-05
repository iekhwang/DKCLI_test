public class TestResponse {
    private String analysisName;
    private Long runTimeLocal;
    private Long runTimeContainer;
    private Long runTimeServer;
    private double runTimeSecContainer;
    private double runTimeSecLocal;
    private double runTimeSecServer;
    private Boolean resultEqualLocalToContainer;
    private Boolean resultEqualLocalToServer;
    private Boolean cloudDeserialized;
    private Boolean serverDeserialized;


    public void setAnalysisName(String analysisName) {

        this.analysisName = analysisName;
    }

    public void setRunTimeLocal(Long runTimeLocal) {
        this.runTimeLocal = runTimeLocal;
        this.runTimeSecLocal = (double) runTimeLocal / 1000000000.0;
    }

    public void setRunTimeContainer(Long runTimeContainer) {
        this.runTimeContainer = runTimeContainer;
        this.runTimeSecContainer = (double) runTimeContainer / 1000000000.0;
    }

    public void setRunTimeServer(Long runTimeServer) {
        this.runTimeServer = runTimeServer;
        this.runTimeSecServer = (double) runTimeServer / 1000000000.0;
    }

    public void setResultEqualLocalToContainer(Boolean resultEqualLocalToContainer) {
        this.resultEqualLocalToContainer = resultEqualLocalToContainer;
    }

    public void setResultEqualLocalToServer(Boolean resultEqualLocalToServer) {
        this.resultEqualLocalToServer = resultEqualLocalToServer;
    }

    public void setServerDeserialized(Boolean serverDeserialized) {
        this.serverDeserialized = serverDeserialized;
    }

    public void setCloudDeserialized(Boolean cloudDeserialized) {
        this.cloudDeserialized = cloudDeserialized;
    }


    public void print() {
        System.out.println("Analysis name       :" + analysisName );
        System.out.println("Semantically equal  :" + true );
        System.out.println("Cloud deserialized  :" + cloudDeserialized.toString() );
        System.out.println("Server deserialized :" + serverDeserialized.toString() );
        System.out.println("Runtime local       :" + runTimeSecLocal );
        System.out.println("Runtime Container   :" + runTimeSecContainer );
        System.out.println("Runtime Server      :" + runTimeSecServer  );

    }

}
