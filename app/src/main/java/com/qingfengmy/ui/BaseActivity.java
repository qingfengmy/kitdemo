package com.qingfengmy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.qingfengmy.R;

/**
 * 所有activity的父类
 * 
 * @author zhangtao(qingfengmy@126.com)
 * 
 *         2014-3-15
 */
public class BaseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置actionBar
		initActionBar(this);
	}

	private void initActionBar(Activity activity) {
		ActionBar bar = getSupportActionBar();
		if (activity instanceof MainActivity) {
			// 设置显示应用图标和应用标题
			bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
					| ActionBar.DISPLAY_SHOW_HOME);
		} else {
			// 设置显示返回图标和应用标题
			bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
					| ActionBar.DISPLAY_HOME_AS_UP);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	/**
	 * menu的点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// 应用图标
			 onBackPressed();
			return true;
		case R.id.menu_about:
			// 关于
			Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showToast(String text){
		Toast.makeText(this, text, 0).show();
	}
}
