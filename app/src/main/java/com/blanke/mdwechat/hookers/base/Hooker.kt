package com.blanke.mdwechat.hookers.base

data class Hooker(val hook: () -> Unit) {
    @Volatile var hasHooked = false
}