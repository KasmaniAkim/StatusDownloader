package com.HashTagApps.WATool;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

public class WhatsappAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";
    private static final String TAGEVENTS = "TAGEVENTS";
    private final AccessibilityServiceInfo info = new AccessibilityServiceInfo();

    @Override
    public void onAccessibilityEvent (AccessibilityEvent accessibilityEvent) {

        String str = TAG;
        Log.d(str, "onAccessibilityEvent");

        if (accessibilityEvent.getPackageName() != null && accessibilityEvent.getPackageName().equals("com.whatsapp")) {
            StringBuilder sb = new StringBuilder();
            sb.append("parcelable:");
            sb.append(accessibilityEvent.getText().toString());
            Log.d(str, sb.toString());
            if (accessibilityEvent.getText().toString().contains("°")) {

                String str2 = "test";
                Log.e(str2, "here3");
                AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
                Log.d(str2, "here4");
                List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
                Log.d(str2, "here5");
                if (messageNodeList == null || messageNodeList.isEmpty()) {
                    Log.d(str2, "here6");
                    return;
                }
                Log.d(str2, "here9");
                List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty()) {
                    Log.d(str2, "here10");
                    return;
                }
                Log.d(str2, "here11");
                AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get(0);
                if (!sendMessageButton.isVisibleToUser()) {
                    Log.d(str2, "here12");
                    return;
                }
                sendMessageButton.performAction(16);
                Log.d(str2, "here13");
                try {
                    Log.d(str2, "here14");
                    Thread.sleep(200);
                    performGlobalAction(1);
                    Log.d(str2, "here15");
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(str2, "here16");
                }
                performGlobalAction(1);
            }
            int eventType = accessibilityEvent.getEventType();
            String str3 = TAGEVENTS;
            if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                Log.d(str3, "TYPE_VIEW_TEXT_CHANGED");
            } else if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("TYPE_WINDOW_STATE_CHANGED:");
                sb2.append(accessibilityEvent.getContentDescription());
                Log.d(str3, sb2.toString());
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d("akimakimakim","akimakimakim");
    }

    public void onServiceConnected() {
        AccessibilityServiceInfo accessibilityServiceInfo = info;
        accessibilityServiceInfo.eventTypes = -1;
        accessibilityServiceInfo.feedbackType = 16;
        accessibilityServiceInfo.notificationTimeout = 100;
        setServiceInfo(accessibilityServiceInfo);
    }
}
