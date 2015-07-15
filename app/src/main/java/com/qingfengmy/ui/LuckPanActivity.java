package com.qingfengmy.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.databinding.ActivityLuckpanBinding;

/**
 * Created by Administrator on 2015/7/15.
 */
public class LuckPanActivity extends BaseActivity {
    ActivityLuckpanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_luckpan);
        binding.toolbar.setTitle("抽奖了");
        setSupportActionBar(binding.toolbar);

        binding.bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.luckPanView.isStart()){
                    binding.luckPanView.luckyStart(1);
                    binding.bton.setImageResource(R.drawable.stop);
                }else{
                    // 还在转
                    if (!binding.luckPanView.isShouldEnd()){
                        // 停止按钮还没按
                        binding.luckPanView.luckyEnd();
                        binding.bton.setImageResource(R.drawable.start);
                    }
                }
            }
        });
    }
}
