package corp.ny.com.codehttp.controllers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.json.JSONException;

import corp.ny.com.codehttp.exceptions.NoInternetException;
import corp.ny.com.codehttp.exceptions.RequestException;
import corp.ny.com.codehttp.exceptions.TokenException;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.response.DefaultResponse;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 12/06/18.
 */
public class SpeedController<T> extends BaseController {

    private AsyncTask<Void, Void, DefaultResponse<T>> task;
    private DefaultResponse<T> response;
    private PrepareRequest.Method method;
    private Exception exception;
    private OnAfterExecute afterExecute;

    public SpeedController(OnAfterExecute afterExecute) {
        this.afterExecute = afterExecute;
    }

    /**
     * Run your request directly in the UIThreads safe
     *
     * @param method       of the request
     * @param response     your data object
     * @param afterExecute handle exception and server response
     * @return a new instance
     */
    public static SpeedController run(PrepareRequest.Method method, DefaultResponse response, OnAfterExecute afterExecute) {
        SpeedController controller = new SpeedController(afterExecute);
        controller.execute(method, response);
        return controller;
    }

    /**
     * Run your request
     *
     * @return the new a task
     */
    @SuppressLint("StaticFieldLeak")
    public AsyncTask<Void, Void, DefaultResponse<T>> execute(PrepareRequest.Method method, DefaultResponse response) {
        this.response = response;
        this.method = method;
        task = new AsyncTask<Void, Void, DefaultResponse<T>>() {
            @Override
            protected DefaultResponse<T> doInBackground(Void... voids) {
                try {
                    switch (SpeedController.this.method) {
                        case POST:
                            return SpeedController.super.post(SpeedController.this.response);
                        case GET:
                            return SpeedController.super.get(SpeedController.this.response);
                    }
                } catch (NoInternetException | RequestException | JSONException | TokenException e) {
                    exception = e;
                }
                return SpeedController.this.response;
            }

            @Override
            protected void onPostExecute(DefaultResponse<T> response) {
                if (exception != null)
                    afterExecute.foundException(exception);
                else
                    afterExecute.play(response);
            }
        };
        return task.execute((Void) null);
    }

    /**
     * Get last task in execute
     *
     * @return {@link AsyncTask}
     */
    public AsyncTask<Void, Void, DefaultResponse<T>> getTask() {
        return task;
    }

    /**
     * cancel request execution
     */
    public void cancel() {
        if (task != null)
            task.cancel(true);
    }

    public interface OnAfterExecute {
        /**
         * Is fire when no exception occurs
         *
         * @param response that contain data
         */
        void play(DefaultResponse response);

        /**
         * Fire when an {@link Exception} is raise<br>
         * Possible exception<br>
         * {@link NoInternetException}<br>
         * {@link RequestException}<br>
         * {@link JSONException}
         *
         * @param e the raise exception.
         */
        void foundException(Exception e);
    }
}
