package com.flashlight.demo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.flashlight.demo.R;
import com.flashlight.demo.adapter.DisplayableItem;
import com.flashlight.demo.adapter.item.FlashTypeItem;
import com.flashlight.demo.adapter.myadapter.MyAdapter;
import com.flashlight.demo.api.APIServiceWrapper;
import com.flashlight.demo.api.request.FlashType;
import com.flashlight.demo.dowloadmanager.DownloadFile;
import com.flashlight.demo.dowloadmanager.DownloadMultiFile;
import com.flashlight.demo.util.LocaleHelper;
import com.flashlight.demo.util.MyConstants;
import com.flashlight.demo.util.PreferencesHelper;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShopFlashlightActivity extends BaseActivity {

    private Context mContext;

    private ImageView imFlashlightType, imCloseFlashlight;
    private TextView tvNameFlashType, tvIdType, tvFree;
    private RecyclerView rcvShop;
    private MyAdapter mAdapter;
    private List<DisplayableItem> mListData = new ArrayList<>();
    private String preview = "";
    private LinearLayout rlAdsParent;
    private int rootId = 10000;
    private long adsDismissTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_flashlight);

        mContext = this;
        preview = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_FLASHLIGHT_PREVIEW_TYPE, mContext);
        adsDismissTime = PreferencesHelper.getInstances().getTimeToDismissAds(MyConstants.ADS_DISMISS_TIME, mContext);

        initView();
        setFlashlightPreview(preview);
        initData();
        setListener();
    }

    private void initView() {
        rcvShop = findViewById(R.id.rcvShopFlashlight);
        tvNameFlashType = findViewById(R.id.tvNameFlashType);
        tvIdType = findViewById(R.id.tvIdType);
        tvFree = findViewById(R.id.tvFree);
        imFlashlightType = findViewById(R.id.imFlashlightType);
        imCloseFlashlight = findViewById(R.id.imCloseShopFlashlight);
        rlAdsParent = findViewById(R.id.rlAdsParent);
    }

    @SuppressLint("SetTextI18n")

    //todo get type flashlight from API
    private void initData() {
        mAdapter = new MyAdapter(mContext, mListData);
        APIServiceWrapper.getInstance(mContext).getFlashTypeFromApi(new ParsedRequestListener<List<FlashType>>() {
            @Override
            public void onResponse(List<FlashType> flashTypeList) {
                if (flashTypeList != null && flashTypeList.size() > 0) {
                    mListData.clear();
                    for (int i = 0; i < flashTypeList.size(); i++) {
                        Log.d("dataAPI", flashTypeList.get(i).getEventName());
                        String dirPath = mContext.getFilesDir().getAbsolutePath() + File.separator + "FlashType/" + flashTypeList.get(i).getEventName();
                        File file = new File(dirPath);
                        boolean isGet = file.exists();
                        boolean selected = preview.equals(new File(file + "/shop_preview.png").getAbsolutePath());
                        mListData.add(new FlashTypeItem(flashTypeList.get(i).getEventName()
                                , flashTypeList.get(i).getEventId() - rootId
                                , flashTypeList.get(i).getUrlShop()
                                , flashTypeList.get(i).getToggleON()
                                , flashTypeList.get(i).getToggleOFF()
                                , flashTypeList.get(i).getUrlShop()
                                , flashTypeList.get(i).isPro(), isGet, selected));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });

        rcvShop.setLayoutManager(new LinearLayoutManager(mContext));
        rcvShop.setAdapter(mAdapter);

    }

    @SuppressLint("SetTextI18n")
    private void setListener() {
        if (mAdapter != null) {
            mAdapter.setAdapterItemClickListener((holder, displayableItem, position) -> {
                FlashTypeItem flashlightItem = (FlashTypeItem) displayableItem;
                String dirPath = getFilesDir().getAbsolutePath() + File.separator + "FlashType/" + flashlightItem.getNameItem();
                File projDir = new File(dirPath);
                for (int i = 0; i < mListData.size(); i++) {
                    FlashTypeItem item = (FlashTypeItem) mListData.get(i);
                    if (item.isGet()) {
                        if (item.getIdItem() == flashlightItem.getIdItem()) {
                            item.setSelected(true);
                            //todo save type flashlight to SharePreference
                            PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_FLASHLIGHT_PREVIEW_TYPE, new File(projDir + "/shop_preview.png").getAbsolutePath(), mContext);
                            PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_FLASHLIGHT_TYPE, new File(projDir + "/preview.png").getAbsolutePath(), mContext);
                            PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_SWITCH_ON_TYPE, new File(projDir + "/switch_on.png").getAbsolutePath(), mContext);
                            PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_SWITCH_OFF_TYPE, new File(projDir + "/switch_off.png").getAbsolutePath(), mContext);
                            //
                            setFlashlightPreview(new File(projDir + "/shop_preview.png").getAbsolutePath());
                            tvFree.setVisibility(item.isPro() ? View.GONE : View.VISIBLE);
                            tvNameFlashType.setText(item.getNameItem());
                            tvIdType.setText(item.getIdItem() + "");
                        } else item.setSelected(false);

                    } else item.setSelected(false);
                }
                if (!flashlightItem.isGet()) {
                    downloadFlashType(flashlightItem);
                }
                mAdapter.notifyDataSetChanged();
            });
        }

        imCloseFlashlight.setOnClickListener(v -> onBackPressed());
    }

    //todo download flashlight type from api
    private void downloadFlashType(FlashTypeItem flashTypeItem) {
        String dirPath = getFilesDir().getAbsolutePath() + File.separator + "FlashType/" + flashTypeItem.getNameItem();
        File projDir = new File(dirPath);
        if (!projDir.exists()) {
            projDir.mkdirs();
        }
        List<String> listLocation = new ArrayList<>();
        listLocation.add(projDir.getAbsolutePath() + "/thumb.png");
        listLocation.add(projDir.getAbsolutePath() + "/switch_on.png");
        listLocation.add(projDir.getAbsolutePath() + "/switch_off.png");
        listLocation.add(projDir.getAbsolutePath() + "/shop_preview.png");

        String[] urls = {flashTypeItem.getUrlThumb(), flashTypeItem.getUrlToggleOn(), flashTypeItem.getUrlToggleOff(), flashTypeItem.getUrlShop()};
        new DownloadMultiFile(listLocation, mContext, true, () -> {
            for (int i = 0; i < mListData.size(); i++) {
                FlashTypeItem item = (FlashTypeItem) mAdapter.getDatas().get(i);
                if (flashTypeItem.getNameItem().equals(item.getNameItem())) {
                    item.setGet(true);
                    item.setSelected(true);
                    //todo save type flashlight to SharePreference
                    PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_FLASHLIGHT_PREVIEW_TYPE, new File(projDir + "/shop_preview.png").getAbsolutePath(), mContext);
                    PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_FLASHLIGHT_TYPE, new File(projDir + "/preview.png").getAbsolutePath(), mContext);
                    PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_SWITCH_ON_TYPE, new File(projDir + "/switch_on.png").getAbsolutePath(), mContext);
                    PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_SWITCH_OFF_TYPE, new File(projDir + "/switch_off.png").getAbsolutePath(), mContext);
                    //
                    ShopFlashlightActivity.this.setFlashlightPreview(new File(projDir + "/shop_preview.png").getAbsolutePath());
                    tvFree.setVisibility(item.isPro() ? View.GONE : View.VISIBLE);
                    tvNameFlashType.setText(item.getNameItem());
                    tvIdType.setText(item.getIdItem() + "");
                }
            }
            mAdapter.notifyDataSetChanged();
        }).execute(urls);
    }

    private void setFlashlightPreview(String path) {

        if (!preview.equals("null")) {
            Glide.with(mContext).asBitmap().load(path).into(imFlashlightType);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFlashlightPreview(preview);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }
}
