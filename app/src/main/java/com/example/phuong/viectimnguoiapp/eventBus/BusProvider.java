package com.example.phuong.viectimnguoiapp.eventBus;

import com.squareup.otto.Bus;

/**
 * Created by phuong on 28/02/2017.
 */

public final class BusProvider {
    private static Bus mBus;

    private BusProvider() {
    }

    public static synchronized Bus getInstance() {
        if (mBus == null) {
            mBus = new Bus();
        }
        return mBus;
    }
}
