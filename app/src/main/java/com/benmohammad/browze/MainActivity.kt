package com.benmohammad.browze

import android.app.Application
import android.app.ApplicationErrorReport
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.engineView
import mozilla.components.browser.engine.system.SystemEngine
import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.concept.engine.DefaultSettings
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.EngineSessionState
import mozilla.components.concept.engine.EngineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app.engine.warmUp()
        val engineSession = app.engine.createSession()
        engineView.render(engineSession)

        app.sessionManager.add(Session("https://google.com"), true, engineSession)
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? = when (name) {
        EngineView::class.java.name -> app.engine.createView(context, attrs).asView()
        else -> super.onCreateView(parent, name, context, attrs)
    }

    override fun onBackPressed() {
        if(!app.sessionManager.onBackPressed()) super.onBackPressed()
    }
}


class App: Application() {
    val engine: SystemEngine by lazy {
        SystemEngine(
                this, DefaultSettings(
                javascriptEnabled = true,
                domStorageEnabled = true,
                displayZoomControls = false,
                loadWithOverviewMode = true,

                allowFileAccess = false,
                allowContentAccess = false,
                allowFileAccessFromFileURLs = false,
                requestInterceptor = AppRequestInterceptor(
                        this@App
                ),
                trackingProtectionPolicy = EngineSession.TrackingProtectionPolicy.recommended()
        )
        )

    }
    val sessionManager: SessionManager by lazy {SessionManager(engine, BrowserStore())}
}