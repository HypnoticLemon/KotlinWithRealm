package com.example.vikrant.demoone.Utils

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .name("kotlin_demo.realm")
                .schemaVersion(0)
                .build()
        Realm.setDefaultConfiguration(realmConfig)

        /*val realmConfiguration = RealmConfiguration.Builder(this)
            .name("demo.realm")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()
    Realm.setDefaultConfiguration(realmConfiguration)*/
    }


}