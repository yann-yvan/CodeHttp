package corp.ny.com.codehttp.views.listener;

import corp.ny.com.codehttp.response.DefaultResponse;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 11/06/18.
 */
public interface IAsyncTask {
    void onPostExecute(DefaultResponse response);
}
