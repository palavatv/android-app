package tv.palava.android.client.ui;

import com.example.palavaclient.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PalavaVideoChatActivity extends Activity {

    protected static final String TAG = PalavaVideoChatActivity.class.getSimpleName();
    protected static final String ROOT_URL = "https://palava.tv/";
    protected String roomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palavavideochat);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new ChatFragment())
                    .commit();
        }

        roomId = this.getIntent().getStringExtra(MainActivity.ROOM_ID);
        Log.i(TAG, "Received start intent with Room ID " + roomId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
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

    public String getRoomId() {
        if (roomId == null)
            return "";
        return roomId;
    }

    public static class ChatFragment extends Fragment {

        private WebView palavaWebRTCView;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public ChatFragment() {
        }

        public static ChatFragment newInstance(int sectionNumber) {
            ChatFragment fragment = new ChatFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View
        onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_videochat, container, false);
            palavaWebRTCView = (WebView) rootView.findViewById(R.id.palavaRTCView);
            setUpWebViewDefaults(palavaWebRTCView);
            palavaWebRTCView.loadUrl(ROOT_URL + ((PalavaVideoChatActivity) getActivity()).getRoomId());
            palavaWebRTCView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    Log.d(TAG, "processing onPermissionRequest: " + request.getResources().toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            if (request.getOrigin().toString().contains(ROOT_URL)) {
                                request.grant(request.getResources());
                            } else {
                                request.deny();
                                Log.e(TAG, "Origin unknown: " + request.getOrigin() + "\n" + request.getResources());
                            }
                        }
                    });
                }
            });
            return rootView;
        }

        /**
         * Convenience method to set some generic defaults for a given WebView
         *
         * @param webView
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private static void setUpWebViewDefaults(WebView webView) {
            WebSettings settings = webView.getSettings();
            // Enable Javascript
            settings.setJavaScriptEnabled(true);
            // Use WideViewport and Zoom out if there is no viewport defined
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            // Enable pinch to zoom without the zoom buttons
            settings.setBuiltInZoomControls(true);
            // Allow use of Local Storage
            settings.setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                // Hide the zoom controls for HONEYCOMB+
                settings.setDisplayZoomControls(false);
            }
            // Enable remote debugging via chrome://inspect
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
            webView.setWebViewClient(new WebViewClient());
            // AppRTC requires third party cookies to work
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
    }
}