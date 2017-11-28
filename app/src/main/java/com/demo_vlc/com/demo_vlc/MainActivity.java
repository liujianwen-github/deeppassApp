package com.demo_vlc.com.demo_vlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.view.ViewGroup.LayoutParams;
import android.util.DisplayMetrics;


import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private Handler handler = null;
    private Context context;
    private Button btn;
    LibVLC libVLC = null;
    private MediaPlayer mediaPlayer;
    SurfaceView srfc;
    SurfaceHolder holder;

    WebView mwebView;
    WebSettings mWebSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        顶部导航栏颜色设置{沉浸式导航，有空看}
//        Window window = getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        导航栏颜色在style中设置
        mwebView = (WebView) findViewById(R.id.webView);
//        beginLoading = (TextView) findViewById(R.id.text_beginLoading);
//        endLoading = (TextView) findViewById(R.id.text_endLoading);
//        loading = (TextView) findViewById(R.id.text_Loading);
        btn = (Button) findViewById(R.id.button);
        handler = new Handler();
        //初始化videobox隐藏
        LinearLayout videoBox = (LinearLayout) findViewById(R.id.videoBox);
        LinearLayout webViewBox = (LinearLayout) findViewById(R.id.webViewBox);
//        videoBox.setVisibility(View.VISIBLE);
//        webViewBox.setVisibility(View.GONE);
        videoBox.setVisibility(View.GONE);
        webViewBox.setVisibility(View.VISIBLE);

//        webview相关设置
        mWebSettings = mwebView.getSettings();
//        mwebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        mwebView.getSettings().setJavaScriptEnabled(true);

//        mwebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mwebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        // 开启 DOM storage API 功能
        mwebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mwebView.getSettings().setDatabaseEnabled(true);
        //开启 Application Caches 功能
        mwebView.getSettings().setAppCacheEnabled(true);
        mwebView.getSettings().setBlockNetworkImage(false);
        mwebView.getSettings().setLoadsImagesAutomatically(true); //自动加载图片
        mwebView.getSettings().setAllowUniversalAccessFromFileURLs(true);//
//        mwebView.getSettings().setPluginState(PluginState.OFF);
//        获取屏幕相关信息
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        webview导入需要显示的html文件
        mwebView.loadUrl("file:///android_asset/deep/index.html");
//        mwebView.loadUrl("https://www.baidu.com");
//        添加js接口
        mwebView.addJavascriptInterface(new StartVideo(), "jsInterface");
        ArrayList options = new ArrayList<>();
//        options.add(":fullscreen");
//        options.add("--effect-width=<int>[1000] ");
        libVLC = new LibVLC(getApplication(),options);






        //设置webViewClient类
//        mwebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                System.out.println(mwebView);
//                System.out.println(url);
//
//                view.loadUrl(url);
//
//                return true;
//            }
//            //设置加载前的函数
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                System.out.println("webview开始加载");
//            }
//
//            //设置结束加载函数
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                System.out.println("webview加载完成");
//            }
//        });

        //设置WebChromeClient类
        mwebView.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
            }

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println("这里是加载进度" + newProgress);
            }
        });



//








    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mwebView.canGoBack()) {
            mwebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁webView
    @Override
    protected void onDestroy() {
        if (mwebView != null) {
            mwebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mwebView.clearHistory();

            ((ViewGroup) mwebView.getParent()).removeView(mwebView);
            mwebView.destroy();
            mwebView = null;
        }
        super.onDestroy();
    }

    //    播放、停止视频功能
    private final class StartVideo {
        private Context context;

        @JavascriptInterface
        public void showToast(String text) {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void showJsText(final String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mwebView.loadUrl("javascript:jsText('" + text + "')");
                }
            });
        }

        @JavascriptInterface
        public void openVideo(String text) {
            final String thisText = text;


            handler.post(new Runnable() {
                @Override
                public void run() {
                    Button btn = (Button) findViewById(R.id.button);

                    final LinearLayout videoBox = (LinearLayout) findViewById(R.id.videoBox);
//                    final LayoutParams videoBoxStyle = videoBox.getLayoutParams();
                    final LinearLayout webViewBox = (LinearLayout) findViewById(R.id.webViewBox);
//                    final LayoutParams webViewBoxStyle = webViewBox.getLayoutParams();
                    final DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    System.out.println("获取的屏幕信息" + metrics);
                    //video展示，webview隐藏

                    webViewBox.setVisibility(View.GONE);
                    videoBox.setVisibility(View.VISIBLE);


                    srfc =(SurfaceView)findViewById(R.id.vlcVideo);
//                    //surfaceview高度莫名其妙被改变了
//                    LayoutParams lp=srfc.getLayoutParams();
//                    lp.width = metrics.widthPixels;
//                    lp.height =metrics.heightPixels/2;
//                    srfc.setLayoutParams(lp);

                    holder = srfc.getHolder();
                    holder.setKeepScreenOn(true);

//                    holder.setFixedSize(1080,720);
                    System.out.println("surfaceholder"+srfc.getHolder());
                    try {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }

                        mediaPlayer = new MediaPlayer(libVLC);
//                        mediaPlayer.setAspectRatio("1080:720");
//                        mediaPlayer.setScale(0);
//                        mediaPlayer.setPosition(3);
//                        mediaPlayer.
//                        srfc.invalidate();
                        System.out.println("surfaceview height"+srfc.getLayoutParams());


//                        String url = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";
                        String url = thisText;
                        mediaPlayer.getVLCVout().setVideoView(srfc);
                        //设置视频窗口大小
                        mediaPlayer.getVLCVout().setWindowSize(metrics.widthPixels,metrics.heightPixels/2);
//                        mediaPlayer.getVLCVout().addCallback(this);
                        //播放前还要调用这个方法
                        mediaPlayer.getVLCVout().attachViews();
                        Media media = new Media(libVLC, Uri.parse(url));
                        mediaPlayer.setMedia(media);
                        mediaPlayer.play();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    返回主页按钮时间监听
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("stop");

                            videoBox.setVisibility(View.GONE);
                            webViewBox.setVisibility(View.VISIBLE);
//                            videoView.stopPlayback();
                            mediaPlayer.stop();

                        }
                    });

                    Toast.makeText(getApplicationContext(), thisText, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
