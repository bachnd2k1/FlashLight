package com.flashlight.demo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flashlight.demo.R;
import com.flashlight.demo.adapter.DisplayableItem;
import com.flashlight.demo.adapter.item.CompassTypeItem;
import com.flashlight.demo.adapter.myadapter.MyAdapter;
import com.flashlight.demo.util.LocaleHelper;
import com.flashlight.demo.util.MyConstants;
import com.flashlight.demo.util.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class ShopCompassActivity extends BaseActivity {

    private Context mContext;

    private ImageView imCompassType, imCloseShopCompass;
    private TextView tvNameCompassType, tvIdType, tvFree;
    private RecyclerView rcvShop;
    private MyAdapter mAdapter;
    private List<DisplayableItem> mListData = new ArrayList<>();
    private String preview;
    private RelativeLayout rlAdsParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_compass);

        mContext = this;
        initView();
        setCompassPreview();
        initData();
        setListener();
    }

    private void initView() {
        rcvShop = findViewById(R.id.rcvShopCompass);
        tvNameCompassType = findViewById(R.id.tvNameCompassType);
        tvIdType = findViewById(R.id.tvIdType);
        tvFree = findViewById(R.id.tvFree);
        imCompassType = findViewById(R.id.imCompassType);
        imCloseShopCompass = findViewById(R.id.imCloseShopCompass);
        rlAdsParent = findViewById(R.id.rlAdsParent);

    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        mAdapter = new MyAdapter(mContext, mListData);
        rcvShop.setLayoutManager(new LinearLayoutManager(mContext));
        rcvShop.setAdapter(mAdapter);

        for (int i = 1; i < 9; i++) {
            mListData.add(new CompassTypeItem("Compass", i, i <= 5, "ic_bg_compass_" + (((i - 1) % 2) + 1), i > 4));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setListener() {
        if (mAdapter != null) {
            mAdapter.setAdapterItemClickListener((holder, displayableItem, position) -> {
                CompassTypeItem compassTypeItem = (CompassTypeItem) displayableItem;

                for (int i = 0; i < mListData.size(); i++) {
                    CompassTypeItem item = (CompassTypeItem) mListData.get(i);
                    if (item.isGet()) {
                        if (item.getIdItem() == (position + 1)) {
                            item.setSelected(true);
                        } else item.setSelected(false);
                    }
                }
                mAdapter.notifyDataSetChanged();

                tvIdType.setText(compassTypeItem.getIdItem() + "");

                //todo save type compass to SharePreference
                if (compassTypeItem.getIdItem() <= 5) {
                    if ((((compassTypeItem.getIdItem() - 1) % 2) + 1) == 1) {
                        PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_COMPASS_PREVIEW_TYPE, compassTypeItem.getSrcThumbIcon(), mContext);
                        imCompassType.setImageResource(R.drawable.ic_bg_compass_1);
                        PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_COMPASS_TYPE, "ic_compass1", mContext);
                        PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_NEEDLE_TYPE, "ic_needle1", mContext);
                        return;
                    }

                    if ((((compassTypeItem.getIdItem() - 1) % 2) + 1) == 2) {
                        PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_COMPASS_PREVIEW_TYPE, compassTypeItem.getSrcThumbIcon(), mContext);
                        imCompassType.setImageResource(R.drawable.ic_bg_compass_2);
                        PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_COMPASS_TYPE, "ic_compass2", mContext);
                        PreferencesHelper.getInstances().setStringSPr(MyConstants.KEY_NEEDLE_TYPE, "ic_needle2", mContext);
                    }
                }

            });

        }

        imCloseShopCompass.setOnClickListener(v -> onBackPressed());
    }

    private void setCompassPreview() {
        preview = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_COMPASS_PREVIEW_TYPE, mContext);
        if (!preview.equals("null")) {
            int srcPreview = getResources().getIdentifier(getPackageName() + ":drawable/" + preview, null, null);
            imCompassType.setImageResource(srcPreview);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCompassPreview();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }
}
