import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnector {


    public String postRequest(String url, String params) throws IOException {

        // setup connection
        URL obj = new URL(url);
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();

        // set post setting
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "text/plain");
        postConnection.setDoOutput(true);

        OutputStream os = postConnection.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();

        int responseCode = postConnection.getResponseCode();
        System.out.println("POST Response Code :  " + responseCode);

        // check if possible

            // setup write answer stream
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            // print result
            System.out.println(response.toString());

            return response.toString();


    }
}
