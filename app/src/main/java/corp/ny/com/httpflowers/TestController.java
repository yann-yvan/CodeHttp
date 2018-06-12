package corp.ny.com.httpflowers;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import corp.ny.com.codehttp.controllers.BaseController;
import corp.ny.com.codehttp.exceptions.NoInternetException;
import corp.ny.com.codehttp.exceptions.RequestException;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.response.DefaultResponse;
import corp.ny.com.codehttp.response.RequestCode;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 30/05/18.
 */
public class TestController extends BaseController {


    public void makeGet(final TextView console) {
        final DefaultResponse response = new DefaultResponse("test", false);

        console.append(String.format("\n\tRoute : %s", response.getPrepareRequest().getRoute()));
        console.append(String.format("\n\tSend : %s", response.getPrepareRequest().getOutgoing()));
        new AsyncTask<Void, Void, DefaultResponse>() {
            String exception;

            @Override
            protected DefaultResponse doInBackground(Void... voids) {
                try {
                    return TestController.this.get(response);
                } catch (NoInternetException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                } catch (RequestException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                } catch (JSONException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(DefaultResponse response) {
                if (response == null) {
                    console.append(String.format("\n\t%s", exception));
                    return;
                }
                console.append(String.format("\n\tRoute : %s", response.getPrepareRequest().getRoute()));
                console.append(String.format("\n\tReceive : %s", response.getPrepareRequest().getIncoming()));
                console.append(String.format("\n\tMessage : %s", response.getMessage()));
                console.append("\nConsole:~");
                switch (RequestCode.requestMessage(response.getMessage())) {

                    case SUCCESS:
                        System.out.print(response.getPrepareRequest().getIncoming());
                        break;
                    case FAILURE:
                        break;
                    case MISSING_DATA:
                        break;
                    case EXPIRED:
                        break;
                    case UNKNOWN_CODE:
                        break;
                }
            }
        }.execute((Void) null);
    }

    public void makePost(final TextView console) {
        final DefaultResponse response = new DefaultResponse("login", false);

        console.append(String.format("\n\tRoute : %s", response.getPrepareRequest().getRoute()));
        console.append(String.format("\n\tSend : %s", response.getPrepareRequest().getOutgoing()));
        new AsyncTask<Void, Void, DefaultResponse>() {
            String exception;

            @Override
            protected DefaultResponse doInBackground(Void... voids) {

                try {
                    PrepareRequest request = new PrepareRequest();
                    request.setOutgoing("json data");
                    //or you can set a JSonObject directly like this
                    request.getOutgoingJsonObject().put("email", "foo@codehttp.com");
                    response.setPrepareRequest(request);
                    response.getPrepareRequest().getOutgoingJsonObject().put("user", new JSONObject()
                            .put("email", "foo@codehttp.com")
                            .put("password", 12345678)
                            .put("password_float", 12345678f)
                            .put("password_double", 12345678l)
                            .put("active", false)
                            .put("object", new JSONObject()
                                    .put("email", "foo@codehttp.com")
                                    .put("password", "12345678")
                                    .put("active", false))

                    );
                    return TestController.this.post(response);
                } catch (NoInternetException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                } catch (RequestException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                } catch (JSONException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(DefaultResponse response) {
                if (response == null) {
                    console.append(String.format("\n\t%s", exception));
                    return;
                }
                console.append(String.format("\n\tRoute : %s", response.getPrepareRequest().getRoute()));
                console.append(String.format("\n\tReceive : %s", response.getPrepareRequest().getIncoming()));
                console.append(String.format("\n\tMessage : %s", response.getMessage()));
                console.append("\nConsole:~");
                switch (RequestCode.requestMessage(response.getMessage())) {

                    case SUCCESS:
                        System.out.print(response.getPrepareRequest().getIncoming());
                        break;
                    case FAILURE:
                        break;
                    case MISSING_DATA:
                        break;
                    case EXPIRED:
                        break;
                    case UNKNOWN_CODE:
                        break;
                }
            }
        }.execute((Void) null);
    }
}