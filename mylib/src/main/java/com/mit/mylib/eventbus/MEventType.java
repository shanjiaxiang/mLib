package com.mit.mylib.eventbus;

/**
 * Created by shanjiaxiang on 2018\11\30 0030.
 */

public class MEventType {
    public static final String EVENT_LOGIN = "event_login";
    public static final String EVENT_LOGOUT = "event_logout";
    public static final String EVENT_REPEAT_LOGIN = "event_repeat_login";
    public static final String EVENT_NET_ERROR = "event_net_error";
    public static final String EVENT_NET_OK = "event_net_ok";
    public static final String MAIN_THREAD = "MainThread";
    public static final String BACKGROUND = "BackgroundThread";
    public static final String POSTING = "PostingThread";
    public static final String ASYNC = "AsyncThread";

    public MEventType() {
    }
}
