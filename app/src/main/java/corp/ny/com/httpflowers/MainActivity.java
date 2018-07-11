package corp.ny.com.httpflowers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import corp.ny.com.codehttp.controllers.SpeedController;
import corp.ny.com.codehttp.exceptions.NoInternetException;
import corp.ny.com.codehttp.exceptions.RequestException;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.response.DefaultResponse;

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
                        new DefaultResponse("test", false), new SpeedController.OnAfterExecute() {
                            @Override
                            public void play(DefaultResponse response) {
                                if (response == null) {
                                    return;
                                }
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
                SpeedController.run(
                        PrepareRequest.Method.POST,
                        new DefaultResponse("test", true), new SpeedController.OnAfterExecute() {
                            @Override
                            public void play(DefaultResponse response) {
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
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
