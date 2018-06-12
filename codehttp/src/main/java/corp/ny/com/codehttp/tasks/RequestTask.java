package corp.ny.com.codehttp.tasks;

import android.os.AsyncTask;

import corp.ny.com.codehttp.response.DefaultResponse;
import corp.ny.com.codehttp.views.listener.IAsyncTask;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 11/06/18.
 */
public class RequestTask extends AsyncTask<DefaultResponse, Void, DefaultResponse> implements IAsyncTask {

    @Override
    protected DefaultResponse doInBackground(DefaultResponse... defaultResponses) {
        return null;
    }

    @Override
    public void onPostExecute(DefaultResponse response) {

    }
}
