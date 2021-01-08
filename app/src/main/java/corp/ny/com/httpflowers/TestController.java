package corp.ny.com.httpflowers;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import corp.ny.com.codehttp.controllers.BaseController;
import corp.ny.com.codehttp.exceptions.NoInternetException;
import corp.ny.com.codehttp.exceptions.RequestException;
import corp.ny.com.codehttp.exceptions.TokenException;
import corp.ny.com.codehttp.exceptions.UnknownFileExtensionException;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.response.DefaultResponse;
import corp.ny.com.codehttp.response.FileType;
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
                } catch (TokenException e) {
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
        final DefaultResponse response = new DefaultResponse("user/login", true);
        String[] files = {"a.pdf", "a.docx", "a.doc", "a.ppt", "a.png", "a.pdt"};
        for (String s : files
        ) {
            try {
                Log.e("MIME", FileType.getMediaType(s));
            } catch (UnknownFileExtensionException e) {
                e.printStackTrace();
            }
        }

        console.append(String.format("\n\tRoute : %s", response.getPrepareRequest().getRoute()));
        console.append(String.format("\n\tSend : %s", response.getPrepareRequest().getOutgoing()));
        new AsyncTask<Void, Void, DefaultResponse>() {
            String exception;

            @Override
            protected DefaultResponse doInBackground(Void... voids) {

                try {
                    PrepareRequest request = new PrepareRequest();
                    //or you can set a JSonObject directly like this
                    request.getOutgoingJsonObject().put("email", "yann.ngalle@outlook.com").put("password", "Password");
                    response.setPrepareRequest(request);

                    return TestController.this.postForm(response);
                } catch (NoInternetException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                } catch (RequestException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                } catch (JSONException e) {
                    exception = e.getMessage();
                    e.printStackTrace();
                } catch (TokenException e) {
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

                response.canPaginateLaravel();
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
