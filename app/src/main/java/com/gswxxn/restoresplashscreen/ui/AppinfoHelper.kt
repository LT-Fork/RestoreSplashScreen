package com.gswxxn.restoresplashscreen.ui

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable

class AppInfoHelper(_checkedList : Set<String>) {
    private val checkedList = _checkedList
    private lateinit var mAppInfoList: MutableList<MyAppInfo>

    data class MyAppInfo(
        val appName: String,
        val packageName: String,
        val icon: Drawable,
        var isChecked: Int,
        val isSystemApp: Boolean
    )

    @SuppressLint("QueryPermissionsNeeded")
    fun getAppInfoList(): MutableList<MyAppInfo> {
        if (::mAppInfoList.isInitialized)
            return mAppInfoList.apply {
                sortBy { it.appName }
                sortByDescending {it.isChecked }
            }.toMutableList()
        mAppInfoList = mutableListOf()
        val pm = MainActivity.appContext.packageManager
        for (appInfo in pm.getInstalledApplications(0)) {
            MyAppInfo(
                appInfo.loadLabel(pm).toString(),
                appInfo.packageName,
                appInfo.loadIcon(pm),
                if (appInfo.packageName in checkedList) 1 else 0,
                appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
            ).also { mAppInfoList.add(it) }
        }
        return mAppInfoList.apply {
            sortBy { it.appName }
            sortByDescending { it.isChecked }
        }.toMutableList()
    }

    fun setChecked(info : MyAppInfo, status : Boolean) {
        mAppInfoList[mAppInfoList.indexOf(info)].isChecked = if (status) 1 else 0
    }
}