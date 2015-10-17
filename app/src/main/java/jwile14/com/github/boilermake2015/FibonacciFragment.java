package jwile14.com.github.boilermake2015;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link FibonacciFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FibonacciFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FibonacciFragment extends Fragment {

    private Button mButton;
    private TextView mTextView;
    private int old1 = 0, old2 = 0;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FibonacciFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FibonacciFragment newInstance(String param1, String param2) {
        FibonacciFragment fragment = new FibonacciFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FibonacciFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fibonacci, container, false);

        //Find the +1 button
        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        v.setBackgroundColor(Color.parseColor("#e07474"));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        v.setBackgroundColor(Color.parseColor("#e0dede"));
                        changeNumber();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        // Find the textView
        mTextView = (TextView) view.findViewById(R.id.numberField);


        return view;
    }

    private void changeNumber() {
        if(!mTextView.getText().toString().equals("Overflow :/")) {
            int curNum = Integer.parseInt(mTextView.getText().toString());
            if (curNum == 0) {
                mTextView.setText("1");
            } else {
                old2 = old1;
                old1 = Integer.parseInt(mTextView.getText().toString());
                if (old1 + old2 < 0) {
                    mTextView.setText("Overflow :/");
                } else {
                    mTextView.setText("" + (old1 + old2));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        old1 = 0;
        old2 = 0;
        mTextView.setText("0");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        public void onFragmentInteraction(Uri uri);
    }

}
