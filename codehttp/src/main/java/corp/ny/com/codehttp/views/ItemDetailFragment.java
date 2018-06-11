package corp.ny.com.codehttp.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import corp.ny.com.codehttp.R;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.system.App;

import static corp.ny.com.codehttp.utils.SyntaxHighLight.highLightData;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private PrepareRequest mItem;
    private String htmlBody;
    private String htmlRespose;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = App.getInstance().getDebugger().getHistory().get(getArguments().getLong(ARG_ITEM_ID));

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            if (mItem.getMethod().equals(PrepareRequest.Method.GET)) {
                rootView.findViewById(R.id.fl_body_container).setVisibility(View.GONE);
                ((ImageView) rootView.findViewById(R.id.full_screen_response)).setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
            }
            ((TextView) rootView.findViewById(R.id.tv_route)).setText(mItem.getRoute());
            ((TextView) rootView.findViewById(R.id.tv_method)).setText(mItem.getMethod().name());
            ((TextView) rootView.findViewById(R.id.tv_status)).setText(String.format("Status : %s %s", mItem.getCode(), mItem.getMessage()));

            final WebView body = rootView.findViewById(R.id.web_body);
            body.getSettings().setJavaScriptEnabled(true);
            body.loadUrl("file:///android_asset/index.html");
            body.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (mItem.getOutgoingJsonObject() != null)
                        highLightData(mItem.getOutgoingJsonObject(), view);
                }
            });


            WebView response = rootView.findViewById(R.id.web_response);
            response.getSettings().setJavaScriptEnabled(true);
            response.loadUrl("file:///android_asset/index.html");
            response.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (mItem.getIncomingJsonObject() != null) {
                        highLightData(mItem.getIncomingJsonObject(), view);
                    }
                }
            });

            rootView.findViewById(R.id.full_screen_body).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rootView.findViewById(R.id.fl_response_container).getVisibility() == View.VISIBLE) {
                        rootView.findViewById(R.id.fl_response_container).setVisibility(View.GONE);
                        ((ImageView) rootView.findViewById(R.id.full_screen_body)).setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
                    } else {
                        rootView.findViewById(R.id.fl_response_container).setVisibility(View.VISIBLE);
                        ((ImageView) rootView.findViewById(R.id.full_screen_body)).setImageResource(R.drawable.ic_fullscreen_black_24dp);
                    }
                }
            });
            rootView.findViewById(R.id.full_screen_response).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rootView.findViewById(R.id.fl_body_container).getVisibility() == View.VISIBLE) {
                        rootView.findViewById(R.id.fl_body_container).setVisibility(View.GONE);
                        ((ImageView) rootView.findViewById(R.id.full_screen_response)).setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
                    } else {
                        rootView.findViewById(R.id.fl_body_container).setVisibility(View.VISIBLE);
                        ((ImageView) rootView.findViewById(R.id.full_screen_response)).setImageResource(R.drawable.ic_fullscreen_black_24dp);
                    }
                }
            });
        }

        return rootView;
    }


}
