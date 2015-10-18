package jwile14.com.github.boilermake2015;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class BookActivity extends AppCompatActivity {

    private ParseObject[] books;

    private ImageView mBookPicture, mSellerPicture;
    private TextView mBookTitle, mSellerName, mSellerRating;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        View exitButton = findViewById(R.id.xButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBookTitle = (TextView) findViewById(R.id.bookTitle);
        mSellerName = (TextView) findViewById(R.id.userNameField);
        mSellerRating = (TextView) findViewById(R.id.ratingTextField);
        mBookPicture = (ImageView) findViewById(R.id.bookImageView);
        mSellerPicture = (ImageView) findViewById(R.id.sellerPic);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.KEY_BOOK);
        query.whereNotEqualTo(ParseConstants.KEY_BOOK_SELLER, ParseUser.getCurrentUser());
        query.orderByAscending(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects == null) {
                    Toast.makeText(BookActivity.this, "No books available :/", Toast.LENGTH_SHORT).show();
                } else {
                    books = new ParseObject[objects.size()];
                    int i = 0;
                    for (ParseObject book : objects) {
                        books[i] = book;
                        i++;
                    }

                    setupLayout();
                }
            }
        });
    }

    private void setupLayout() {
        if (books.length > 0) {
            mIndex = 0;
            getBookAt(mIndex);
        } else {

        }
    }

    private void getBookAt(int i) {
        ParseUser seller = ParseUser.createWithoutData(ParseUser.class, ((ParseUser) books[i].get(ParseConstants.KEY_BOOK_SELLER)).getObjectId());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.KEY_USER);
        seller.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                ParseUser seller = (ParseUser) object;

                mBookTitle.setText(books[mIndex].getString(ParseConstants.KEY_BOOK_TITLE));
                mSellerName.setText(seller.getString(ParseConstants.KEY_FIRST_NAME));

                mSellerRating.setText(seller.get(ParseConstants.KEY_RATING).toString());

                ParseFile bookImage = (ParseFile) books[mIndex].get(ParseConstants.KEY_BOOK_PICTURE);
                bookImage.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        mBookPicture = (ImageView) findViewById(R.id.bookImageView);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        mBookPicture.setImageBitmap(bitmap);
                    }
                });

                try {
                    ParseFile sellerImage = (ParseFile) seller.get(ParseConstants.KEY_PROFILE_PIC);

                    sellerImage.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            mSellerPicture = (ImageView) findViewById(R.id.sellerPic);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            mSellerPicture.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception er) {

                }
            }
        });

        Button skipButton = (Button) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndex += 1;
                if (mIndex >= books.length) {
                    mIndex = 0;
                }
                getBookAt(mIndex);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book, menu);
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
}
