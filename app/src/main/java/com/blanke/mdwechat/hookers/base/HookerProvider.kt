package com.blanke.mdwechat.hookers.base

interface HookerProvider {
    fun provideStaticHookers(): List<Hooker>? = null
    fun provideEventHooker(event: String): Hooker? = null
}