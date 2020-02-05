import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.XMLSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class TestHelper {

    public String getXMIFromJCas(JCas result) throws Exception {

        ByteArrayOutputStream outStream = null;

        try {
            // create out stream
            outStream = new ByteArrayOutputStream();
            XMLSerializer xmlSer = new XMLSerializer(outStream, false);
            // create cas from jcas (result)
            CAS resultCas = result.getCas();

            // get current cas type system
            TypeSystem resultType = resultCas.getTypeSystem();

            // set up CAS serializer with type system
            XmiCasSerializer xmi_cas = new XmiCasSerializer(resultType);


            xmi_cas.serialize(resultCas, xmlSer.getContentHandler());

            String resultXMLString = outStream.toString();

            return resultXMLString;


        } catch (Exception e) {

            e.printStackTrace();
            return "JCas to String hat nicht funktioniert";

        } finally {

            if (outStream != null) {
                outStream.close();
            }
        }

    }

    public JCas getJCasFromXMI (String xmi) throws RuntimeException {
        try {

            // setup dependencies
            InputStream stream = new ByteArrayInputStream(xmi.getBytes());
            CAS Cas = JCasFactory.createJCas().getCas();
            XmiCasDeserializer.deserialize(stream, Cas);
            return Cas.getJCas();

        } catch (Exception e){

            throw new RuntimeException(e);

        }
    }

    public Boolean isEqual(String cloud_xmi, String local_xmi) {

        if(cloud_xmi.equals(local_xmi)) {
            return true;
        } else {
            return false;
        }
    }


}
