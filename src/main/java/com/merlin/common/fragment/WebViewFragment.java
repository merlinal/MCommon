package com.merlin.common.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.merlin.common.AbstractFragment;
import com.merlin.common.R;
import com.merlin.core.context.MContext;
import com.merlin.core.network.NetWorkType;
import com.merlin.view.MWebView;

/**
 * @author merlin
 */

public class WebViewFragment extends AbstractFragment {

    public final static String WEB_CACHE = "webCache";

    private MWebView mWebView;
    private ProgressBar mProgressBar;
    private View mErrorView;

    @Override
    protected View getLayoutView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.m_webview, viewGroup, false);
    }

    @Override
    public void initView() {
        super.initView();
        mWebView = mRoot.findViewById(R.id.m_web);
        mProgressBar = mRoot.findViewById(R.id.m_web_progress);

        webSetting();
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        mWebView.resumeTimers();
        super.onResume();
    }

    @Override
    public void onPause() {
        //onPause通知内核尝试停止所有处理，如动画和地理位置，但是不能停止Js，如果想全局停止Js，可以调用pauseTimers()全局停止Js，调用onResume()恢复。
        mWebView.onPause();
        mWebView.pauseTimers();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyWebView();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void webSetting() {
        //声明WebSettings子类
        WebSettings webSettings = mWebView.getSettings();

        //如果访问的页面中要与Javascript交互，则webView必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //支持插件
        //webSettings.setPluginsEnabled(true);
        //webSettings.setPluginState(WebSettings.PluginState.ON);

        //设置自适应屏幕，两者合用（下面这两个方法合用）
        //将图片调整到适合webView的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);

        //缩放操作
        //支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true);
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);

        //其他细节操作
        //关闭webView中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");

        //设置缓存模式
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        if (MContext.device().getNetWorkType() == NetWorkType.NO) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        //设置  Application Caches 缓存目录
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + WEB_CACHE;
        webSettings.setAppCachePath(cacheDirPath);

        /* Android 5.0上WebView默认不允许加载Http与Https混合内容
           加载的链接为Https开头，但是链接里面的内容，比如图片为Http链接，这时候，图片就会加载不出来。解决如下：
           （MIXED_CONTENT_ALWAYS_ALLOW 允许从任何来源加载内容，即使起源是不安全的；
             MIXED_CONTENT_NEVER_ALLOW 不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
             MIXED_CONTENT_COMPLTIBILITY_MODE 当涉及到混合式内容时，WebView会尝试去兼容最新Web浏览器的风格；）
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            webSettings.setMixedContentMode(webSettings.getMixedContentMode());
        }

        //开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        //设置Client
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        //1.设置html加载完之后再加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
    }

    private void destroyWebView() {
        //销毁WebView
        //在关闭了Activity时，如果WebView的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webView调用destroy时,webView仍绑定在Activity上
        //这是由于自定义webView构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webView,然后再销毁webView:
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            //销毁
            ViewGroup parentView = (ViewGroup) mWebView.getParent();
            if (parentView != null) {
                parentView.removeView(mWebView);
            }
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 是否滚动到了底部
     *
     * @return boolean
     */
    public boolean isReachBottom() {
        return mWebView.getContentHeight() * mWebView.getScale() == (mWebView.getHeight() +
                mWebView.getScrollY());
    }

    /**
     * 是否有滚动条
     *
     * @return
     */
    public boolean existVerticalScrollbar() {
        return mWebView.existVerticalScrollbar();
    }

    public void clearCache() {
        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webView而是针对整个应用程序.
        mWebView.clearCache(true);

        //清除当前webView访问的历史记录
        //只会webView访问历史记录里的所有记录除了当前访问记录
        mWebView.clearHistory();

        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        mWebView.clearFormData();
    }

    /**
     * 调用js的两种方法
     * loadUrl()	方便简洁	—— 效率低，获取返回值麻烦 —— 不需要获取返回值，对性能要求较低时
     * evaluateJavascript()	效率高 —— 向下兼容性差(仅用于4.4+)
     *
     * @param js js代码段
     */
    public void loadJs(String js) {
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mWebView.loadUrl(js);
        } else {
            mWebView.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                }
            });
        }
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void addJavascriptInterface(Object object, String name) {
        //java的object类对象映射到js的name对象
        mWebView.addJavascriptInterface(object, name);
    }

    /**
     * WebView.HitTestResult.UNKNOWN_TYPE 未知类型
     * WebView.HitTestResult.PHONE_TYPE 电话类型
     * WebView.HitTestResult.EMAIL_TYPE 电子邮件类型
     * WebView.HitTestResult.GEO_TYPE 地图类型
     * WebView.HitTestResult.SRC_ANCHOR_TYPE 超链接类型
     * WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE 带有链接的图片类型
     * WebView.HitTestResult.IMAGE_TYPE 单纯的图片类型
     * WebView.HitTestResult.EDIT_TEXT_TYPE 选中的文字类型
     *
     * @param onClickListener onClickListener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        mWebView.setOnClickListener(onClickListener);
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        mWebView.setOnLongClickListener(onLongClickListener);
    }

    public void setErrorView(View errorView) {
        this.mErrorView = errorView;
        mErrorView.setVisibility(View.GONE);
    }

    /**
     * WebViewClient类（主要作用是：处理各种通知 & 请求事件）
     */
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //使得打开网页时不调用系统浏览器， 而是在本WebView中显示
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //使得打开网页时不调用系统浏览器， 而是在本WebView中显示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //设定加载开始的操作
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //设定加载结束的操作
            //2.设置html加载完之后再加载图片
            if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            //设定加载资源的操作
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                onError(errorCode);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                onError(error.getErrorCode());
            }
        }

        /**
         * 在认证证书不被Android所接受的情况下，我们可以通过设置重写WebViewClient的onReceivedSslError方法在其中设置接受所有网站的证书来解决
         *
         * @param view    WebView
         * @param handler SslErrorHandler
         * @param error   SslError
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {//处理https请
            //接受所有网站的证书
            handler.proceed();
            //表示挂起连接，为默认方式
            // handler.cancel();
            //可做其他处理
            // handler.handleMessage(null);
        }

        private void onError(int errorCode) {
            if (mErrorView != null) {
                //清除掉默认错误页面
                mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                //设置自定义错误页面
                mErrorView.setVisibility(View.VISIBLE);
            }


            switch (errorCode) {
                default:
                    break;
            }
        }

    }


    /**
     * WebChromeClient类( 作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。)
     */
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (onTitleChangedListener != null) {
                onTitleChangedListener.onTitleChanged(title);
            }
        }

    }

    private OnTitleChangedListener onTitleChangedListener;

    public void setOnTitleChangedListener(OnTitleChangedListener onTitleChangedListener) {
        this.onTitleChangedListener = onTitleChangedListener;
    }

    public interface OnTitleChangedListener {
        /**
         * @param title title
         */
        void onTitleChanged(String title);
    }

}
