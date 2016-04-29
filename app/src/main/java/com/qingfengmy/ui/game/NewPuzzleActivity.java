package com.qingfengmy.ui.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.BaseActivity;
import com.qingfengmy.ui.PictureActivity;
import com.qingfengmy.ui.game.views.NewPuzzleLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/3/3.
 */
public class NewPuzzleActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.mGamePintuLayout)
    NewPuzzleLayout mGamePintuLayout;
    @InjectView(R.id.id_level)
    TextView mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_puzzle);
        ButterKnife.inject(this);
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mGamePintuLayout.setOnGamePintuListener(new NewPuzzleLayout.GamePintuListener() {

            @Override
            public void nextLevel(final int nextLevel) {
                new AlertDialog.Builder(NewPuzzleActivity.this)
                        .setTitle("Game Info").setMessage("LEVEL UP !!!")
                        .setPositiveButton("NEXT LEVEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mGamePintuLayout.nextLevel();
                                mLevel.setText("" + nextLevel);
                            }
                        }).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_puzzle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change) {
            Intent intent = new Intent(this, PictureActivity.class);
            intent.putExtra("from", true);
            startActivityForResult(intent, 1000);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bitmap bitmap = data.getParcelableExtra("bitmap");
            mGamePintuLayout.setBitmap(bitmap);

        }
    }
}
