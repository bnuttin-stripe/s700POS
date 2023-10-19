package com.bnuttin.s700pos.api

import com.stripe.stripeterminal.external.callable.Cancelable

var discoveryCancelable: Cancelable? = null

//val isApplicationDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

//fun onDiscoverReaders() {
//    val config = DiscoveryConfiguration.LocalMobileDiscoveryConfiguration(
//        isSimulated = isApplicationDebuggable,
//    )
//
//    discoveryCancelable = Terminal.getInstance().discoverReaders(config, { readers: List<Reader> ->
//    }, object : Callback {
//        override fun onSuccess() {
//            println("Finished discovering readers")
//        }
//
//        override fun onFailure(e: TerminalException) {
//            e.printStackTrace()
//        }
//    })
//}
//
//override fun onStop() {
//    // If you're leaving the activity or fragment without selecting a reader,
//    // make sure you cancel the discovery process or the SDK will be stuck in
//    // a discover readers phase
//    discoveryCancelable?.cancel(object : Callback {
//        override fun onSuccess() { }
//        override fun onFailure(e: TerminalException) { }
//    })
//}