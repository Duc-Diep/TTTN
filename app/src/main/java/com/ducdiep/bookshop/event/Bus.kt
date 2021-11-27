package com.ducdiep.bookshop.event

import org.greenrobot.eventbus.EventBus

class Bus {
    companion object{
        var instance: Bus? = null

        fun getInstanceBus(): Bus? {
            if (instance == null) instance = Bus()
            return instance
        }

        fun post(o: Any?) {
            EventBus.getDefault().post(o)
        }

        fun register(o: Any?) {
            if (!EventBus.getDefault().isRegistered(o)) {
                EventBus.getDefault().register(o)
            }
        }

        fun unRegister(o: Any?) {
            EventBus.getDefault().unregister(o)
        }
    }
}