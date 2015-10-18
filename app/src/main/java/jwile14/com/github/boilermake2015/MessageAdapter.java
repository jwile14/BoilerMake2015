package jwile14.com.github.boilermake2015;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by wilejd on 10/17/2015.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {
    protected List<ParseObject> mMessages;

    public static final String TAG = MessageAdapter.class.getSimpleName();

    public MessageAdapter(Context context, int resourceId, List<ParseObject> messages) {
        super(context, resourceId, messages);

        mMessages = messages;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_item_message, null);
        }

        final ParseObject message = getItem(mMessages.size() - position - 1);

        if (message != null) {
            ImageView avatarImageView = (ImageView) v.findViewById(R.id.avatarImageView);
            TextView messageContentLabel = (TextView) v.findViewById(R.id.commentLabel);
            TextView usernameLabel = (TextView) v.findViewById(R.id.firstnameLabel);

            if (message.get(ParseConstants.KEY_MESSAGE_SENDER) != null) {
                final ParseUser user = (ParseUser) message.get(ParseConstants.KEY_MESSAGE_SENDER);
                usernameLabel.setText("Conversation with " + user.getString(ParseConstants.KEY_FIRST_NAME));
                ParseFile imageViewFile = user.getParseFile(ParseConstants.KEY_PROFILE_PIC);
                if (user.get(ParseConstants.KEY_PROFILE_PIC) != null) {
                    // We have an avatar thumbnail file!
                    ParseFile avatarFile = user.getParseFile(ParseConstants.KEY_PROFILE_PIC);
                    if (avatarFile != null && avatarImageView != null) {
                        // Needs work
                        //avatarImageView.setImageBitmap(avatarFile);
//                        avatarImageView.loadInBackground(new GetDataCallback() {
//                            @Override
//                            public void done(byte[] bytes, ParseException e) {
//                                // Intentionally left blank
//                            }
//                        });
                    } else {
                        try {
                            avatarImageView.setImageResource(R.drawable.generic_profile_pic);
                        } catch(NullPointerException e) {
                            Log.e(TAG, "AvatarImageView is null for some reason");
                        }
                        Log.i(TAG, "Avatar View" + avatarImageView);
                    }
                } else {
                    // We don't have an avatar thumbnail..
                }

                if (user.getUsername() != null) {
                    usernameLabel.setText(user.getUsername());
                } else {
                    usernameLabel.setText("(Deleted)");
                }

                avatarImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //
                    }
                });

                usernameLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //
                    }
                });
            }

            if (message.get(ParseConstants.KEY_MESSAGE_TEXT) != null) {
                messageContentLabel.setText(message.get(ParseConstants.KEY_MESSAGE_TEXT).toString());
            }

        }
        return v;
    }
}
