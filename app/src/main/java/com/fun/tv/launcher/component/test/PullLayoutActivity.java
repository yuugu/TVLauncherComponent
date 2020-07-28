package com.fun.tv.launcher.component.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import com.fun.tv.launcher.component.R;
import com.fun.tv.launcher.component.utils.Utils;
import com.fun.tv.launcher.tangram.TangramBuilder;
import com.fun.tv.launcher.tangram.TangramEngine;
import com.fun.tv.launcher.tangram.structure.viewcreator.ViewHolderCreator;
import org.json.JSONArray;
import org.json.JSONException;

public class PullLayoutActivity extends AppCompatActivity {

    private RecyclerView pullLayoutRv;

    private TangramEngine tangramEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_layout);
        pullLayoutRv = findViewById(R.id.rv);
        initTangram();
    }

    private void initTangram() {
        //首先初始化构造器，通过构造器可以个性化自己的布局样式
        TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(this);
        builder.registerCell("customVeiwByInterface", CustomViewByInterface.class);
        builder.registerCell("customViewByAnnotation", CustomViewByAnnotation.class);
        builder.registerCell("customViewByCustomCell", CustomCell.class, CustomViewByCustomCell.class);
        builder.registerCell("customViewByViewHolder", CustomHolderCell.class, new ViewHolderCreator<>(R.layout.custom_view_by_custom_holder_layout, CustomViewHolderForCustomHolderCell.class, LinearLayout.class));
        builder.registerCell("CustomTimerView", CustomTimerView.class);
        //获得tangramEngine，绘制引擎
        tangramEngine = builder.build();
        tangramEngine.bindView(pullLayoutRv);
        pullLayoutRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                tangramEngine.onScrolled();
            }
        });
        byte[] bytes = Utils.getAssertsFile(this, "pullLayout.json");
        if (bytes != null) {
            String json = new String(bytes);
            try {
                JSONArray data = new JSONArray(json);
                tangramEngine.setData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tangramEngine != null) {
            tangramEngine.destroy();
        }
    }
}
