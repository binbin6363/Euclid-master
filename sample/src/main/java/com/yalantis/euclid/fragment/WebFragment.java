package com.yalantis.euclid.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.yalantis.euclid.sample.R;

/**
 * Created by bbwang on 2016/8/19.
 */
public class WebFragment extends Fragment {

    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //webView = (WebView) view.findViewById(R.id.webview_page);
        //webView.loadUrl("www.baidu.com");
    }
}
