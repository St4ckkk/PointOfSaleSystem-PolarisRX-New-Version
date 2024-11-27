import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpClientExample {

    // one instance, reuse
    
    
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws Exception {

        HttpClientExample obj = new HttpClientExample();

        try {
            System.out.println("Testing 1 - Send Http GET request");
            obj.sendGet();

            //System.out.println("Testing 2 - Send Http POST request");
            obj.sendPost();
        } finally {
            obj.close();
        }
    }

    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGet() throws Exception {

        HttpGet request = new HttpGet("http://localhost/gatepass_api/get_users.php");

        // add request headers
        request.addHeader("custom-key", "mkyong");
        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            //System.out.println(headers);
            
            

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                JSONObject temp1 = new JSONObject(result);
                JSONArray arr = temp1.getJSONArray("accounts");
                for(int a = 0; a < arr.length(); a++){
                    JSONObject license = arr.getJSONObject(a);
                    System.out.println(license.getString("license"));
                }
                
            }

        }

    }

    private void sendPost() throws Exception {

        HttpPost post = new HttpPost("http://localhost/gatepass_api/sync_users.php");
        // add request parameter, form parameters
        ArrayList<NameValuePair> postParameters;
        postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("qrcode", "abc"));
        postParameters.add(new BasicNameValuePair("fullname", "123"));
        postParameters.add(new BasicNameValuePair("license", "secret"));

        post.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
        }

    }

}