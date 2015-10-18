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
import com.parse.SaveCallback;

import java.util.List;


public class BookActivity extends AppCompatActivity {

    private ParseObject[] books;
    private boolean[] liked;

    private ImageView mBookPicture, mSellerPicture, mStar1, mStar2, mStar3, mStar4, mStar5;
    private TextView mBookTitle, mSellerName, mSellerRating, mEmptyText;
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
        mBookPicture = (ImageView) findViewById(R.id.bookImageView);
        mSellerPicture = (ImageView) findViewById(R.id.sellerPic);
        mEmptyText = (TextView) findViewById(R.id.emptyTextView);
        mStar1 = (ImageView) findViewById(R.id.star1);
        mStar2 = (ImageView) findViewById(R.id.star2);
        mStar3 = (ImageView) findViewById(R.id.star3);
        mStar4 = (ImageView) findViewById(R.id.star4);
        mStar5 = (ImageView) findViewById(R.id.star5);

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
                    liked = new boolean[objects.size()];
                    int i = 0;
                    for (ParseObject book : objects) {
                        books[i] = book;
                        liked[i] = false;
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
        seller.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                ParseUser seller = (ParseUser) object;

                mBookTitle.setText(books[mIndex].getString(ParseConstants.KEY_BOOK_TITLE));
                mSellerName.setText(seller.getString(ParseConstants.KEY_FIRST_NAME));

                String rating = "" + seller.getDouble(ParseConstants.KEY_RATING);
                switch (rating) {
                    case "0.0":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "0.5":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "1.0":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "1.5":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "2.0":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "2.5":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "3.0":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "3.5":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "4.0":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
                        break;
                    case "4.5":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
                        break;
                    case "5.0":
                        mStar1.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar2.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar3.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar4.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        mStar5.setImageDrawable(getResources().getDrawable(R.drawable.full_star));
                        break;
                }

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
                if (!allLiked()) {
                    do {
                        mIndex += 1;
                        if (mIndex >= books.length) {
                            mIndex = 0;
                        }
                    } while (liked[mIndex]);
                    getBookAt(mIndex);
                }
            }
        });

        Button likeButton = (Button) findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!liked[mIndex]) {
                    liked[mIndex] = true;
                    if (allLiked()) {
                        mBookPicture.setVisibility(View.INVISIBLE);
                        mSellerPicture.setVisibility(View.INVISIBLE);
                        mStar1.setVisibility(View.INVISIBLE);
                        mStar2.setVisibility(View.INVISIBLE);
                        mStar3.setVisibility(View.INVISIBLE);
                        mStar4.setVisibility(View.INVISIBLE);
                        mStar5.setVisibility(View.INVISIBLE);
                        mSellerName.setVisibility(View.INVISIBLE);
                        mBookTitle.setVisibility(View.INVISIBLE);
                        mEmptyText.setVisibility(View.VISIBLE);
                    } else {
                        startConverstation();

                        while (liked[mIndex]) {
                            mIndex += 1;
                            if (mIndex >= books.length) {
                                mIndex = 0;
                            }
                        }
                        getBookAt(mIndex);
                    }
                }
            }
        });

    }

    private void startConverstation() {
        ParseUser seller = ParseUser.createWithoutData(ParseUser.class, ((ParseUser) books[mIndex].get(ParseConstants.KEY_BOOK_SELLER)).getObjectId());
        seller.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                ParseUser seller = (ParseUser) object;

                ParseObject conversation = new ParseObject(ParseConstants.KEY_CONVERSATION);
                conversation.put(ParseConstants.KEY_CONVERSATION_MEMBER1, seller);
                conversation.put(ParseConstants.KEY_CONVERSATION_MEMBER2, ParseUser.getCurrentUser());
                conversation.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(BookActivity.this, "Conversation created!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean allLiked() {
        boolean result = true;
        if (liked != null && liked.length > 0) {
            for (boolean like : liked) {
                result = result && like;
                if (!result) {
                    return result;
                }
            }
            return result;
        } else {
            return result;
        }
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
