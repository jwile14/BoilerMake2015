package jwile14.com.github.boilermake2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment implements AbsListView.OnItemClickListener {

    public static final String TAG = ListFragment.class.getSimpleName().toString();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LIST_KEY = "list";

    private static ArrayList<String> mListItems;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(LIST_KEY, mListItems);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mListItems = getArguments().getStringArrayList(LIST_KEY);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO: Change Adapter to display your content
        if(ParseUser.getCurrentUser() != null) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.KEY_CONVERSATION);
            query.whereEqualTo(ParseConstants.KEY_CONVERSATION_MEMBER1, ParseUser.getCurrentUser());

            ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>(ParseConstants.KEY_CONVERSATION);
            query2.whereEqualTo(ParseConstants.KEY_CONVERSATION_MEMBER2, ParseUser.getCurrentUser());

            List<ParseQuery<ParseObject>> queryList = new ArrayList<>();
            queryList.add(query);
            queryList.add(query2);

            ParseQuery<ParseObject> mainQuery = ParseQuery.or(queryList);
            mainQuery.orderByDescending(ParseConstants.KEY_CREATED_AT);
            mainQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects != null) {
                        Log.d(TAG, "Found " + objects.size() + " conversations!");
                        mAdapter = new ConversationAdapter(getActivity(), R.id.messageLayout, objects);
                        mListView.setAdapter(mAdapter);

                        mListView.invalidateViews();
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "CLICKED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
}
