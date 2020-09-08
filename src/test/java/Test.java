import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            String url = "https://mparticle.uc.cn/article.html?uc_param_str=frdnsnpfvecpntnwprdssskt&wm_aid=c4998e6d654a4019b2ef568b994be91b";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet(url);
                // Request configuration can be overridden at the request level.
                // They will take precedence over the one set at the client level.
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(3000)
                        .setConnectTimeout(3000)
                        .build();
                httpGet.setConfig(requestConfig);

                CloseableHttpResponse response = httpClient.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    System.out.print("访问一次.");
                }
                httpClient.close();

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
