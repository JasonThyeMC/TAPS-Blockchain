package com.TAPS.blockchain.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRefreshFragment extends Fragment {

    public static final int REFRESH_DELAY = 500;

    public static final String KEY_ICON = "icon";
    public static final String KEY_COLOR = "color";
    public static final String KEY_PAYABLE = "payable";

    protected List<Map<String, Integer>> mSampleList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, Integer> map;
        mSampleList = new ArrayList<>();

        int[] icons = {
                R.drawable.scale,
                R.drawable.scale_send,
                R.drawable.scale_req
        };

        int[] colors = {
                R.color.saffron,
                R.color.eggplant,
                R.color.sienna};

        int[] payable = {
                R.drawable.scale_dining,
                R.drawable.scale_shopping,
                R.drawable.scale_parking
        };

        for (int i = 0; i < icons.length; i++) {
            map = new HashMap<>();
            map.put(KEY_ICON, icons[i]);
            map.put(KEY_COLOR, colors[i]);
            map.put(KEY_PAYABLE, payable[i]);

            mSampleList.add(map);
        }
    }
}
