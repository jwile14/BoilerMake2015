package jwile14.com.github.boilermake2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends ActionBarActivity {

    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstNameField = (EditText) findViewById(R.id.firstNameField);
        mLastNameField = (EditText) findViewById(R.id.lastNameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mRegisterButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED

                        String firstName = mFirstNameField.getText().toString().trim();
                        String lastName = mLastNameField.getText().toString().trim();
                        String email = mEmailField.getText().toString().trim();
                        String password = mPasswordField.getText().toString().trim();

                        if (firstName.isEmpty()) {
                            showError("Error!", "First name field is empty.");
                        } else if (lastName.isEmpty()) {
                            showError("Error!", "Last name field is empty.");
                        } else if (email.isEmpty()) {
                            showError("Error!", "Email field is empty.");
                        } else if (containsIllegalCharacters(firstName)) {
                            showError("Error!", "First name contains illegal characters.");
                        } else if (containsIllegalCharacters(lastName)) {
                            showError("Error!", "First name contains illegal characters.");
                        } else {
                            ParseUser user = new ParseUser();
                            user.put(ParseConstants.KEY_FIRST_NAME, firstName);
                            user.put(ParseConstants.KEY_LAST_NAME, lastName);
                            user.put(ParseConstants.KEY_RATING, 2.5);
                            user.setEmail(email);
                            user.setUsername(email);
                            user.setPassword(password);

                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    setProgressBarIndeterminate(false);
                                    if (e == null) {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else

                                    {
                                        //Checks to make sure user didn't switch apps and android is killing the app
                                        // or the user navigated away from the activity before callback returned
                                        if (!isFinishing()) {
                                            showError("Error!", e.getMessage());
                                        }
                                    }
                                }
                            });
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Helper to make sure our username does not contain a space
    private boolean containsIllegalCharacters(final String testCode) {
        if (testCode != null) {
            for (int i = 0; i < testCode.length(); i++) {
                char curChar = testCode.charAt(i);
                if (!(curChar >= 65 && curChar <= 90 || curChar >= 97 && curChar <= 122)) { // All letters + numbers only
                    return true;
                }
            }
        }
        return false;
    }

    private void showError(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
