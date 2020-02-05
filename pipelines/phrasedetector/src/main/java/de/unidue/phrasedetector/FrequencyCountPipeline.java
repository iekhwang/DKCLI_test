package de.unidue.phrasedetector;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.tudarmstadt.ukp.dkpro.core.frequency.phrasedetection.FrequencyCounter;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

public class FrequencyCountPipeline {

    /* ignore all n-grams that occur less frequently */
    static final int MIN_COUNT = 3;

    /* lowercase all tokens. Important: set this parameter in the phrase annotator to the same value! */
    static boolean LOWERCASE = true;

    /* target file, compression is determined by the file name suffix */
    static String COUNTS_FILE = "target/counts.txt";

    public static void main(String[] args)
            throws Exception {

        FrequencyCountPipeline analysis = new FrequencyCountPipeline();
        analysis.run( analysis.createParamString() );


    }

    public JCas run(String jsonString) throws Exception {

        // setup gson parser
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // get parameters out of the json object
        String text = jsonObject.get("text").toString();


        // create analysis engines
        AnalysisEngine segmenter = createEngine(OpenNlpSegmenter.class);
        AnalysisEngine freqCounter = createEngine(FrequencyCounter.class,
                FrequencyCounter.PARAM_TARGET_LOCATION, COUNTS_FILE,
                FrequencyCounter.PARAM_SORT_BY_COUNT, true,
                FrequencyCounter.PARAM_MIN_COUNT, MIN_COUNT,
                FrequencyCounter.PARAM_LOWERCASE, LOWERCASE);

        // List all engines in an iterator
        List<AnalysisEngine> engines = new ArrayList<AnalysisEngine>();
        engines.add(segmenter);
        engines.add(freqCounter);

        // JWeb1TIndexer indexCreator = new JWeb1TIndexer(outpcd .. utLocation, 3);
        JCas jcas = process(text, engines);
        System.out.println(jcas);
        return jcas;
    }

    // return Jcas after iterated over all given engines
    private JCas process(String aText, List<AnalysisEngine> engines) throws UIMAException {

        JCas jcas = JCasFactory.createText(aText, "en");

        for (AnalysisEngine engine : engines)
            engine.process(jcas);

        return jcas;
    }

    private String createParamString() {

        FrequencyCountParameter frequencyCountParameter = new FrequencyCountParameter();
        // tokenizedParameter.language = "de";
        frequencyCountParameter.text = "Bacon ipsum dolor amet picanha porchetta boudin, jowl tail biltong brisket corned beef turducken beef meatloaf. Ball tip buffalo ham hock ribeye bresaola short loin swine shankle doner frankfurter. Pork loin short ribs salami ground round sausage short loin beef shank cow ham chuck rump buffalo leberkas. Bresaola alcatra cupim ham, shankle tongue beef ribs porchetta filet mignon leberkas venison prosciutto pork chuck. ";

        return new Gson().toJson(frequencyCountParameter);
    }
}
