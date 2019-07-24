package corp.ny.com.httpflowers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import corp.ny.com.codehttp.controllers.SpeedController;
import corp.ny.com.codehttp.exceptions.NoInternetException;
import corp.ny.com.codehttp.exceptions.RequestException;
import corp.ny.com.codehttp.internet.ConnectivityReceiver;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.response.DefaultResponse;
import corp.ny.com.codehttp.system.App;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 31/05/18.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private TextView console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initialize();
        App.getInstance().addConnectivityListener(new ConnectivityReceiver.ConnectivityReceiverListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                Log.e("Online", isConnected + "");
                Toast.makeText(MainActivity.this, "Online : " + isConnected + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        console = findViewById(R.id.console);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_post).setOnClickListener(this);
        findViewById(R.id.btn_fast_get).setOnClickListener(this);
        findViewById(R.id.btn_fast_post).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        TestController controller = new TestController();
        switch (view.getId()) {
            case R.id.btn_get:
                controller.makeGet(console);
                break;
            case R.id.btn_post:
                controller.makePost(console);
                break;
            case R.id.btn_fast_get:
                SpeedController.run(
                        PrepareRequest.Method.GET,
                        new DefaultResponse("job-search", true), new SpeedController.OnAfterExecute() {
                            @Override
                            public void play(DefaultResponse response) {
                                if (response == null) {
                                    return;
                                }
                                response.canPaginateLaravel();
                                console.append(String.format("\n\tRoute : %s", response.getPrepareRequest().getRoute()));
                                console.append(String.format("\n\tReceive : %s", response.getPrepareRequest().getIncoming()));
                                console.append(String.format("\n\tMessage : %s", response.getMessage()));
                                console.append("\nConsole:~");
                            }

                            @Override
                            public void foundException(Exception e) {
                                if (e instanceof NoInternetException) {
                                    console.append("\nConsole:~ no internet");
                                }

                            }
                        });

                break;
            case R.id.btn_fast_post:
                /*try {
                    SpeedController.run(
                            PrepareRequest.Method.POST,
                            new DefaultResponse("job-search", new JSONObject(), true), new SpeedController.OnAfterExecute() {
                                @Override
                                public void play(DefaultResponse response) {
                                    response.canPaginateLaravel();
                                    try {
                                        Log.e("New query", response.getPrepareRequest().getOutgoingJsonObject().toString(5));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(MainActivity.this, "Good no error", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void foundException(Exception e) {
                                    if (e instanceof NoInternetException) {
                                        Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else if (e instanceof RequestException) {
                                        Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {//JSonException
                                        Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    pagination(new DefaultResponse("job-search", new JSONObject(), true));

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                try {

                    JSONObject data = new JSONObject();
                    data.put("job_description_id", 1)
                            .put("phone", "687686886");

                    DefaultResponse defaultResponse = new DefaultResponse("job-apply", data, true);
                    defaultResponse.addFile("extResume", "file.pdf", "/storage/emulated/0/Telegram/Telegram Documents/4_5886697143858627953.pdf");
                    defaultResponse.addFile("otherFiles[]", "file.pdf", "/storage/emulated/0/Telegram/Telegram Documents/4_5886697143858627953.pdf");
                    formPost(defaultResponse);
                } catch (Exception e) {
                    console.append("\nConsole:~ no internet");
                }
                break;
        }
    }


    private void pagination(DefaultResponse response) {

        SpeedController.run(
                PrepareRequest.Method.POST,
                response, new SpeedController.OnAfterExecute() {
                    @Override
                    public void play(DefaultResponse response) {

                        if (response.canPaginateLaravel())
                            pagination(response);
                        try {
                            console.append(response.getPrepareRequest().getIncomingJsonObject().toString(5));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "Good no error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void foundException(Exception e) {
                        if (e instanceof NoInternetException) {
                            Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (e instanceof RequestException) {
                            Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {//JSonException
                            Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void formPost(DefaultResponse response) {

        SpeedController.run(
                PrepareRequest.Method.POST_FORM,
                response, new SpeedController.OnAfterExecute() {
                    @Override
                    public void play(DefaultResponse response) {
                        console.append("\nConsole:~ " + response.getPrepareRequest().getIncomingJsonObject().toString());
                        Toast.makeText(MainActivity.this, "Good no error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void foundException(Exception e) {
                        console.append("\nConsole:~ " + e.getMessage());
                        if (e instanceof NoInternetException) {
                            Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (e instanceof RequestException) {
                            Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {//JSonException
                            Toast.makeText(MainActivity.this, "OOPS " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
