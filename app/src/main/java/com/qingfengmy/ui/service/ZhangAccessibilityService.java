package com.qingfengmy.ui.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Random;

/**
 * Created by Administrator on 2015/12/18.
 */
public class ZhangAccessibilityService extends AccessibilityService {
    public ZhangAccessibilityService() {
    }

    String r;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        CharSequence currentActivityName = event.getClassName();
        if ("com.qingfengmy.ui.AccessibilityActivity".equals(currentActivityName)) {
            // 无障碍服务显示的页面
            if (nodeInfo != null) {
                int eventType = event.getEventType();
                if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    r = String.valueOf(new Random().nextInt(15));
                    iterateNodesAndHandle(nodeInfo);
                }
            }
        }

    }

    private boolean iterateNodesAndHandle(AccessibilityNodeInfo nodeInfo) {

        if (nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();
            if ("android.widget.Button".equals(nodeInfo.getClassName())) {
                String nodeContent = nodeInfo.getText().toString();
                if (r.equals(nodeContent)) {
                    // 点击
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SELECT);
                    return true;
                }
            } else if ("android.widget.ScrollView".equals(nodeInfo.getClassName())) {
                // 滚动
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(i);
                if (iterateNodesAndHandle(childNodeInfo)) {
                    // 响应事件返回true，否则返回false
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }
}
