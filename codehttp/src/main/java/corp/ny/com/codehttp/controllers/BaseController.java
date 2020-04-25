package corp.ny.com.codehttp.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import corp.ny.com.codehttp.R;
import corp.ny.com.codehttp.exceptions.NoInternetException;
import corp.ny.com.codehttp.exceptions.RequestException;
import corp.ny.com.codehttp.exceptions.TokenException;
import corp.ny.com.codehttp.internet.ConnectivityReceiver;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.response.DefaultResponse;
import corp.ny.com.codehttp.response.FormPart;
import corp.ny.com.codehttp.response.RequestCode;
import corp.ny.com.codehttp.system.App;
import corp.ny.com.codehttp.utils.ManifestReader;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yann-yvan on 15/11/17.
 */

public abstract class BaseController {

    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = null;
    private File cacheDir = null;

    public BaseController() {
        this.cacheDir = App.getContext().getCacheDir();
    }

    /**
     * @param response
     * @return
     * @throws NoInternetException
     * @throws RequestException
     */
    public DefaultResponse post(DefaultResponse response) throws NoInternetException, RequestException, JSONException, TokenException {
        RequestBody body = RequestBody.create(mediaType, response.getPrepareRequest().getOutgoing());
        //build our request
        Request request = new Request.Builder().
                url(response.getPrepareRequest().getRoute()).
                post(body).
                build();
        response.getPrepareRequest().setMethod(PrepareRequest.Method.POST);
        return makeRequest(request, response);
    }

    /**
     * Post data with form data such as file
     *
     * @param response the response container
     * @return response object {@link DefaultResponse}
     * @throws NoInternetException
     * @throws RequestException
     * @throws JSONException
     * @throws TokenException
     */
    public DefaultResponse post(DefaultResponse response, boolean isWithFormPart) throws NoInternetException, RequestException, JSONException, TokenException {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        JSONObject data = response.getPrepareRequest().getOutgoingJsonObject();
        if (data != null)
            for (int j = 0; j < data.length(); j++) {
                String key = data.names().getString(j);
                multipartBody.addFormDataPart(key, data.get(key).toString());
            }
        if (isWithFormPart)
            for (int i = 0; i < response.getFormParts().size(); i++) {
                FormPart part = (FormPart) response.getFormParts().get(i);
                multipartBody.addFormDataPart(part.getProperty(), part.getFileName(), part.getFile());
            }
        RequestBody body = multipartBody.build();

        //build our request
        Request request = new Request.Builder().
                url(response.getPrepareRequest().getRoute()).
                post(body).
                build();
        response.getPrepareRequest().setMethod(PrepareRequest.Method.POST);
        return makeRequest(request, response);
    }

    /**
     * @param response
     * @return
     * @throws NoInternetException
     * @throws RequestException
     */
    public DefaultResponse get(DefaultResponse response) throws NoInternetException, RequestException, JSONException, TokenException {
        //build our request
        Request request = new Request.Builder().
                url(response.getPrepareRequest().getRoute()).
                get().
                build();
        response.getPrepareRequest().setMethod(PrepareRequest.Method.GET);
        return makeRequest(request, response);
    }

    /**
     * @param request
     * @return
     * @throws NoInternetException
     * @throws RequestException
     */
    private DefaultResponse makeRequest(Request request, DefaultResponse defaultResponse) throws NoInternetException, RequestException, JSONException, TokenException {
        //get httpclient instance and prepare request execution
        Call callPost = getHttpClient().newCall(request);
        if (App.getInstance().getDebugger() != null) {
            App.getInstance().getDebugger().addToHistory(defaultResponse.getPrepareRequest());
        }

        if (!ConnectivityReceiver.isConnected()) {
            defaultResponse.getPrepareRequest().getIncomingJsonObject().put("exception", App.getContext().getString(R.string.no_internet));
            throw new NoInternetException(App.getContext().getString(R.string.no_internet));
        }

        //execute the request and wait for the answer
        Response response;
        try {
            response = callPost.execute();
        } catch (IOException e) {
            e.printStackTrace();
            defaultResponse.getPrepareRequest().getIncomingJsonObject().put("exception", e.getMessage());
            throw new RequestException(e.getMessage());
        }


        PrepareRequest result = defaultResponse.getPrepareRequest();
        //response status
        result.setCode(response.code());
        result.setMessage(response.message());

        try {
            //your response body
            result.setIncoming(response.body().string());
        } catch (IOException e) {
            defaultResponse.getPrepareRequest().getIncomingJsonObject().put("exception", e.getMessage());
        }


        //you should always close the body to enhance recycling mechanism
        response.body().close();

        defaultResponse.parseFromJson();
        if (RequestCode.tokenMessage(defaultResponse.getMessage()) == RequestCode.Token.TOKEN_EXPIRED) {
            if (result.isTokenRequired())
                result.getOutgoingJsonObject().put("token", defaultResponse.getToken());
            makeRequest(request, defaultResponse);
        } else if (RequestCode.tokenMessage(defaultResponse.getMessage()) != RequestCode.Token.UNKNOWN_CODE)
            throw new TokenException(RequestCode.tokenMessage(defaultResponse.getMessage()).toString(), defaultResponse.getMessage());


        return defaultResponse;
    }

    /**
     * Create new OkHttpClient instance
     *
     * @return OkHttpClient
     */
    private OkHttpClient getHttpClient() {
        if (client == null) {
            //Assigning a CacheDirectory
            File myCacheDir = new File(cacheDir, "OkHttpCache");
            //define cache size
            int cacheSize = 100 * 1024 * 1024; // 50 MiB
            Cache cacheDir = new Cache(myCacheDir, cacheSize);

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .cache(cacheDir)
                    .retryOnConnectionFailure(true)
                    .readTimeout(ManifestReader.getMetadataInt("HTTP_READ_TIMEOUT", 30000), TimeUnit.MILLISECONDS)
                    .addInterceptor(getInterceptor())
                    //.addInterceptor(new GzipInterceptor())
                    ;

            /*try {
                TLSSocketFactory tlsSocketFactory=new TLSSocketFactory();
                if (tlsSocketFactory.getTrustManager() != null && ManifestReader.getMetadataBoolean("SSL_REQUIRED")) {
                    Log.e("SSL_REQUIRED","ENABLE");
                    builder.sslSocketFactory(tlsSocketFactory,tlsSocketFactory.getTrustManager());
                }
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }*/


            client = builder.build();
        }
        //now it's using the cache response.body().string()
        return client;
    }

    private X509TrustManager generateTrustManagers() throws KeyStoreException, NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }

        return (X509TrustManager) trustManagers[0];
    }

    /**
     * Convert InputStream into String
     *
     * @param inputStream the target
     * @return string decoded
     */
    private String getStringFromInputStream(InputStream inputStream) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }


    public File getCacheDir() {
        return cacheDir;
    }


    /**
     * How to save a base64 encode bitmap on the disk
     *
     * @param fileName      the bitmap to encode
     * @param base64Picture the base64 string we want to save
     * @throws IOException is throw when there is an error when opening stream for wri
     */
    public String saveBase64Picture(String fileName, String base64Picture, String subFolder) throws IOException {
        //Second save the picture
        //--------------------------
        //Check if it is a base64 picture
        if (base64Picture.equals("default")) {
            return "default";
        }
        //Find the external storage directory
        File filesDir = getCacheDir();
        //Retrieve the name of the subfolder where your store your picture
        //(You have set it in your string ressources)
        String pictureFolderName = "Pictures/" + subFolder.toLowerCase();
        //then create the subfolder
        File pictureDir = new File(filesDir, pictureFolderName);
        //Check if this subfolder exists
        if (!pictureDir.exists()) {
            //if it doesn't create it
            pictureDir.mkdirs();
        }
        //Define the file to store your picture in
        File filePicture = new File(pictureDir, fileName);
        //Open an OutputStream on that file
        FileOutputStream fos = null;
        fos = new FileOutputStream(filePicture);

        //Write in that stream your bitmap in png with the max quality (100 is max, 0 is min quality)
        //convert base 64 image to bitmap
        byte[] decodedString = Base64.decode(base64Picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        //The close properly your stream
        Log.i("pictures path", filePicture.getPath());

        fos.flush();
        fos.close();

        //return picture path
        return filePicture.getPath();
    }

    /**
     * Convert Bitmap to Base64 encoded for json transfer
     *
     * @param bitmap the bitmap to encode
     * @return the encoded bitmap as string
     */
    public String bitmapToBase64(Bitmap bitmap, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    /**
     * How to save a Bitmap on the disk
     *
     * @param fileName the name of the file we want to save
     * @param bitmap   the bitmap we want to save
     * @throws IOException is throw when there is an error when opening stream for write file
     */
    public String saveBitmapPicture(String fileName, Bitmap bitmap) throws IOException {
        //Second save the picture
        //--------------------------
        //Find the external storage directory
        File filesDir = getCacheDir();
        //Retrieve the name of the subfolder where your store your picture
        //(You have set it in your string ressources)
        String pictureFolderName = "Pictures";
        //then create the subfolder
        File pictureDir = new File(filesDir, pictureFolderName);
        //Check if this subfolder exists
        if (!pictureDir.exists()) {
            //if it doesn't create it
            pictureDir.mkdirs();
        }
        //Define the file to store your picture in
        File filePicture = new File(pictureDir, fileName);
        //Open an OutputStream on that file
        FileOutputStream fos = new FileOutputStream(filePicture);
        //Write in that stream your bitmap in png with the max quality (100 is max, 0 is min quality)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        //The close properly your stream
        Log.i("pictures path", filePicture.getPath());
        fos.flush();
        fos.close();
        //return picture pathreturn response()->json(['status' => false,
        return filePicture.getPath();
    }

    private Interceptor getInterceptor() {
        return new LoggingInterceptor();
    }


    /**
     * this class helps us to log all ours requests
     */
    private static class LoggingInterceptor implements Interceptor {
        //Code pasted from okHttp webSite itself
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.i("Interceptor request", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.i("Interceptor request", String.format("Received response for %s in .1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }
}
