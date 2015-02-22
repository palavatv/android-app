package tv.palava.android.client.ui;

import com.example.palavaclient.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    protected static final String TAG = MainActivity.class.getSimpleName();
    protected static final String ROOM_ID = "tv.palava.android.client.message_room_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View
        onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final EditText palavaRoomField = (EditText) rootView.findViewById(R.id.palavaRoomSelectorInputField);
            final Button palavaJoinRoomButton = (Button) rootView.findViewById(R.id.palavaJoinRoomButton);
            palavaRoomField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() > 0) {
                        palavaJoinRoomButton.setEnabled(true);
                    } else {
                        palavaJoinRoomButton.setEnabled(false);
                    }
                }
            });

            palavaJoinRoomButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String palavaRoomId = palavaRoomField.getText().toString();
                    Log.i(TAG, "Sending intent to video chat activity with room id " + palavaRoomId);
                    Intent startVideoCHatIntent = new Intent(getActivity(), PalavaVideoChatActivity.class);
                    startVideoCHatIntent.putExtra(ROOM_ID, palavaRoomId);
                    startActivity(startVideoCHatIntent);
                }
            });
            return rootView;
        }
    }
}