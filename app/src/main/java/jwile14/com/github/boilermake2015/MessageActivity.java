package jwile14.com.github.boilermake2015;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class MessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        View exitButton = findViewById(R.id.xButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
