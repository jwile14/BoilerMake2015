package jwile14.com.github.boilermake2015;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by wilejd on 10/17/2015.
 */
public class ConversationAdapter extends ArrayAdapter<ParseObject> {
    protected List<ParseObject> mConversations;

    public static final String TAG = MessageAdapter.class.getSimpleName();
    private ImageView mAvatarImageView;
    private TextView mMessageContentLabel;
    private TextView mConversationLabel;
    private Context mContext;

    public ConversationAdapter(Context context, int resourceId, List<ParseObject> conversations) {
        super(context, resourceId, conversations);
        mContext = context;
        mConversations = conversations;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_item_conversation, null);
        }

        RelativeLayout messageLayout = (RelativeLayout)v.findViewById(R.id.commentLayout);
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                Toast.makeText(mContext, "Opening messages", Toast.LENGTH_SHORT).show();
            }
        });

        final ParseObject conversation = getItem(mConversations.size() - position - 1);

        if (conversation != null) {
            mAvatarImageView = (ImageView) v.findViewById(R.id.avatarImageView);
            mMessageContentLabel =
                    (TextView) v.findViewById(R.id.commentLabel);
            mConversationLabel = (TextView) v.findViewById(R.id.firstnameLabel);


            ParseUser seller = ParseUser.createWithoutData(ParseUser.class, ((ParseUser) conversation.get(ParseConstants.KEY_CONVERSATION_MEMBER1)).getObjectId());
            seller.fetchInBackground(new GetCallback<ParseObject>() {

                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                        final ParseUser user = (ParseUser) object;
                        mConversationLabel.setText(user.getUsername());
                        ParseFile imageViewFile = user.getParseFile(ParseConstants.KEY_PROFILE_PIC);
                        if (user.get(ParseConstants.KEY_PROFILE_PIC) != null) {
                            // We have an avatar thumbnail file!
                            ParseFile avatarFile = user.getParseFile(ParseConstants.KEY_PROFILE_PIC);
                            if (avatarFile != null && mAvatarImageView != null) {
                                ParseFile image = user.getParseFile(ParseConstants.KEY_PROFILE_PIC);
                                image.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        mAvatarImageView.setImageBitmap(bitmap);

                                    }
                                });
                            } else {
                                mAvatarImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.generic_profile_pic));
                            }
                        } else {
                            // We don't have an avatar thumbnail..
                        }

                        if (user.getUsername() != null) {
                            mConversationLabel.setText(user.getUsername());
                        } else {
                            mConversationLabel.setText("(Deleted)");
                        }

                        mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //
                            }
                        });

                        mConversationLabel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //
                            }
                        });
                    }
                }
            });


            mMessageContentLabel.setText("Tap to talk!");

        }
        return v;
    }
}
