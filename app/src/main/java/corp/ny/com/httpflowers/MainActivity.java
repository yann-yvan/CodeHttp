package corp.ny.com.httpflowers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
