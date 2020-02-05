package de.unidue.phrasedetector;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.frequency.phrasedetection.PhraseAnnotator;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

public class PhraseAnnotationPipeline {
    private static final String COUNTS_PATH = PhraseAnnotationPipeline.class.getClassLoader().getResource("counts.xz");
    //private static final String COUNTS_PATH = "counts.xz";
    public static void main(String[] args)
            throws Exception {
        PhraseAnnotationPipeline analysis = new PhraseAnnotationPipeline();
        analysis.run( analysis.createParamString() );
    }

    public JCas run(String jsonString) throws Exception {
        /* a lower threshold yields more multi-token phrases */
        float threshold = (float) 100.0;
        System.out.println("We entered into phrase annotator");

        // setup gson parser
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        System.out.println(jsonObject);

        // get parameters out of the json object
        String text = jsonObject.get("text").toString();
        System.out.println(text);

        // create analysis engines
        AnalysisEngine segmenter = createEngine(OpenNlpSegmenter.class);
        AnalysisEngine phraseAnnotator = createEngine(PhraseAnnotator.class,
                PhraseAnnotator.PARAM_MODEL_LOCATION, COUNTS_PATH,
                PhraseAnnotator.PARAM_FEATURE_PATH, Token.class.getCanonicalName(),
                PhraseAnnotator.PARAM_THRESHOLD, threshold,
                PhraseAnnotator.PARAM_DISCOUNT, FrequencyCountPipeline.MIN_COUNT,
                PhraseAnnotator.PARAM_LOWERCASE, FrequencyCountPipeline.LOWERCASE);

        // List all engines in an iterator
        List<AnalysisEngine> engines = new ArrayList<AnalysisEngine>();
        engines.add(segmenter);
        engines.add(phraseAnnotator);

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

        FrequencyCountParameter paramter = new FrequencyCountParameter();
        // tokenizedParameter.language = "de";
        paramter.text = "Bacon ipsum dolor amet picanha porchetta boudin, jowl tail biltong brisket corned beef turducken beef meatloaf. Ball tip buffalo ham hock ribeye bresaola short loin swine shankle doner frankfurter. Pork loin short ribs salami ground round sausage short loin beef shank cow ham chuck rump buffalo leberkas. Bresaola alcatra cupim ham, shankle tongue beef ribs porchetta filet mignon leberkas venison prosciutto pork chuck. ";
        System.out.println(new Gson().toJson(paramter));
        return new Gson().toJson(paramter);
    }
}
