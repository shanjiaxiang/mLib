package com.mit.mlib.eventbus;

/**
 * Created by shanjiaxiang on 2018\11\30 0030.
 */

public class MEvent {
    public String mEventType;
    public Object mEventObject;

    public MEvent() {
    }

    public MEvent(String eventType, Object eventObject) {
        this.mEventType = eventType;
        this.mEventObject = eventObject;
    }
}
