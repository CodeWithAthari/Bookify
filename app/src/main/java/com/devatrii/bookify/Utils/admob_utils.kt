package com.devatrii.bookify.Utils


import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


private const val AD_RETRY_DELAY = 10_000L // ms

private var isAdUnitsLoaded = false
private const val TAG = "AdmobUtils"

private var currentClicks = 0

data class AdmobAds(
    var bannerAd: String? = null,
    var interstitialAd: String? = null,
    var appOpenAd: String? = null,
    var interstitialAdDelay: Int = 3,
    // int ad controllers
    var interstitialAdRetriesThreshold: Int = 10,
    var showInterstitialAdInstantly: Boolean = false,
    var showProgressInterstitialAdInstantly: Boolean = true,
    var permenantHideInterstitialAdProgress: Boolean = false,
    var loadInterstitialAdAgainAfterShowing: Boolean = true,
    var dialogMessage: String = "Please Wait Showing Ad...",
    var dialogCancellable: Boolean = false,
    // banner ad controllers
    var bannerAdRetriesThreshold: Int = 10,
    var bannerAdShowWaterMark: Boolean = true,
    var bannerAdWaterMarkText: String = "<br>Loading Banner Ad<br>",
    var bannerAdAnimation: Boolean = true,
    var bannerAdHideWaterMarkOnError: Boolean = true,
    var bannerAdAnimationDuration: Long = 500L,
    var bannerAdWaterMarkBG: String? = null,
    var bannerAdWaterMarkFG: String? = null
)

private var admobAds = AdmobAds()

/**
 * Loads Ad units & store them in admobAds private data class variable
 *
 * @param onSuccess when loads successfully.
 * @author 
 * This admob util file was created by Dev Atrii
 *
 * The AdmobUtils file, authored by Dev Atrii, is a comprehensive utility for integrating AdMob advertisements seamlessly into Android applications.
 * This utility simplifies the process of managing banner, interstitial, and app open ads, offering a range of customizable options.
 * It supports Firebase integration, allowing for dynamic loading of ad units through a database.
 * Notable features include automatic retries, watermarked banners, configurable animations, and an intuitive interface for handling interstitial ads with progress dialogs.
 * The utility ensures a smooth ad experience, offering fine-tuned controls such as delays, thresholds, and click requirements.
 * With concise and organized code, this AdmobUtils file is a valuable tool for developers seeking efficient AdMob integration within their Android apps.
 * Follow Dev Atrii on YouTube for insightful development content.
 * Created Date: Jumada I 24, 1445 AH
 * Last Modified: Jumada I 23, 1445 AH
 * Youtube: https://www.youtube.com/@devatrii
 */
fun loadAdUnits(onSuccess: () -> Unit = {}) {
    
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val ref = firebaseDatabase.getReference("admob")

    ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {
                Log.d(TAG, "onDataChange: Admob Units Doesn't Exist")
                return
            }
            admobAds = snapshot.getValue(AdmobAds::class.java) ?: admobAds
            Log.d(TAG, "onDataChange: $admobAds")
            Log.d(TAG, "onDataChange: Ad Units Loaded From Firebase")
            delayAndRun(500) {
                onSuccess()
            }
            isAdUnitsLoaded = true
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: Unable to load ad units reason ${error.message}")
        }

    })

}

private var bannerAdRetries = 0
private var isFirstBannerAdLoaded = false

/**
 * Load Banner Ads into any view group e.g LinearLayout or Relative Layout etc
 * Only Linear or Relative Layout is recommended but you can use any
 * Make sure ViewGroup doesn't contain anything else
 *
 * @param waterMark: should show banner ad watermark or not; recommended for admob
 */
fun ViewGroup.loadBannerAd(
    waterMark: Boolean = admobAds.bannerAdShowWaterMark
) {

    this.removeAllViews()
    if (this is LinearLayout)
        this.gravity = Gravity.CENTER
    val waterMarkText = admobAds.bannerAdWaterMarkText
    val txtWaterMark = TextView(this.context).apply {
       text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(waterMarkText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(waterMarkText)
        }
        gravity = Gravity.CENTER
        val layoutParameters =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams = layoutParameters
       if (admobAds.bannerAdWaterMarkFG != null && admobAds.bannerAdWaterMarkFG != "null") {
            try {
                setTextColor(Color.parseColor(admobAds.bannerAdWaterMarkFG))
            } catch (e: Exception) {
                setTextColor(Color.WHITE)
            }
        } else {
            setTextColor(Color.WHITE)
        }

        if (admobAds.bannerAdWaterMarkBG != null && admobAds.bannerAdWaterMarkBG != "null") {
            try {
                this.setBackgroundColor(Color.parseColor(admobAds.bannerAdWaterMarkBG))
            } catch (e: Exception) {
                this.setBackgroundColor(Color.GRAY)
            }
        } else {
            this.setBackgroundColor(Color.GRAY)
        }

    }
    if (waterMark) {
        if (txtWaterMark.parent !=null)
            (txtWaterMark.parent as ViewGroup).removeView(txtWaterMark)
        addView(txtWaterMark)
    }
    val delay: Long = if (isFirstBannerAdLoaded) 0 else 500
    delayAndRun(delay) {
        if (!isAdUnitsLoaded) {
            if (bannerAdRetries == admobAds.bannerAdRetriesThreshold) {
                Log.i(TAG, "loadBannerAd: MAX RETRIES REACHED")
                return@delayAndRun
            }

            Log.d(
                TAG,
                "loadBannerAd: Ad Units are not loaded i'll try loading ad units after 10_000ms"
            )
            delayAndRun(AD_RETRY_DELAY) {
                bannerAdRetries++
                Log.d(TAG, "loadBannerAd: Retrying $bannerAdRetries times")
                loadAdUnits()
                delayAndRun((AD_RETRY_DELAY / 2)) {
                    loadBannerAd()
                }
            }

            return@delayAndRun
        }

        if (admobAds.bannerAd == null) {
            Log.i(TAG, "loadBannerAd: Banner Ad Units Not Found.. Returning")
            return@delayAndRun
        }

        val bannerLayout = this
        val adView = AdView(context)
        adView.setAdSize(AdSize.BANNER)


        adView.adUnitId = admobAds.bannerAd!!
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (waterMark) {
                    if (admobAds.bannerAdAnimation) {
                        txtWaterMark.fadeOut()
                        delayAndRun(admobAds.bannerAdAnimationDuration) {
                            txtWaterMark.visibility = View.GONE
                            if (adView.parent !=null)
                                (adView.parent as ViewGroup).removeView(adView)
                            bannerLayout.addView(adView)
                            adView.fadeIn()
                        }
                    } else {
                        txtWaterMark.visibility = View.GONE
                        if (adView.parent !=null)
                                (adView.parent as ViewGroup).removeView(adView)
                        bannerLayout.addView(adView)
                    }
                } else {
                    if (adView.parent !=null)
                                (adView.parent as ViewGroup).removeView(adView)
                    bannerLayout.addView(adView)
                }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                if (admobAds.bannerAdHideWaterMarkOnError) {
                    txtWaterMark.visibility = View.GONE
                }
            }
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        isFirstBannerAdLoaded = true
    }

}

private var mInterstitialAd: InterstitialAd? = null

/**
 * PreLoads interstitial ad to use it in future
 *
 * @param context context is just a context.
 * @param onSuccess when loads successfully.
 * @param onFailed when unable to load.
 */
fun loadInterstitialAdIfNull(
    context: Context, onSuccess: (ad: InterstitialAd) -> Unit = {}, onFailed: () -> Unit = {}
) {
    if (mInterstitialAd != null) {
        Log.d(TAG, "loadInterstitialAdIfNull: Ad is already loaded")
        return
    }
    context.loadInterstitialAd(onSuccess = {
        onSuccess(it)
        mInterstitialAd = it
    }, onFailed = {
        Log.d(TAG, "loadInterstitialAdIfNull: $it")
        onFailed()
    })

}

/**
 * Load interstitial ad if null other wise use already loaded and show it with progress bar
 *
 * @param cancellable should progressBar be cancellable
 * @param showInstantlyAfterLoading should wait for click threshold requirements to meet or show instantly after loading ad.
 * @param onShown when ad is shown.
 * @param onDismissed when ad is dismissed.
 * @param onError when error occurred.
 */
fun Activity.loadAndShowInterstitialAdWithProgressDialog(
    cancellable: Boolean = admobAds.dialogCancellable,
    showInstantlyAfterLoading: Boolean = admobAds.showProgressInterstitialAdInstantly,
    loadAgainAfterShowing: Boolean = true,
    onShown: () -> Unit = {},
    onDismissed: () -> Unit = {},
    onError: () -> Unit = {},
    customCode: () -> Unit = {},
) {

    val prg = ProgressDialog(this).apply {
        setCancelable(cancellable)
        setMessage(admobAds.dialogMessage)
        if (showInstantlyAfterLoading && !admobAds.permenantHideInterstitialAdProgress)
            show()
    }

    mInterstitialAd?.let {
        Log.d(TAG, "loadAndShowInterstitialAdWithProgressDialog: Ad is already loaded")
        showInterstitialAd(
            showInstantly = showInstantlyAfterLoading,
            onShown = {
                onShown()
                prg.dismiss()
            },
            onDismissed = onDismissed,
            onError = {
                onError()
                prg.dismiss()
            }, customCode = customCode,
            onClickRequirementMatches = {
                if (!showInstantlyAfterLoading && !admobAds.permenantHideInterstitialAdProgress)
                    prg.show()
            }
        )

        return
    }
    if (adLoadCounter == admobAds.interstitialAdDelay && !prg.isShowing && !admobAds.permenantHideInterstitialAdProgress)
        prg.show()

    Log.d(TAG, "loadAndShowInterstitialAdWithProgressDialog: Trying to load ad 1st")
    loadInterstitialAdIfNull(this, onSuccess = {
        prg.dismiss()
        Log.i(TAG, "loadAndShowInterstitialAdWithProgressDialog: on success $adLoadCounter")
        showInterstitialAd(
            showInstantly = showInstantlyAfterLoading,
            loadAgainAfterShowing = loadAgainAfterShowing,
            onShown = onShown,
            onDismissed = onDismissed,
            onError = onError,
            customCode = customCode
        )
    }, onFailed = {
        customCode()
        prg.dismiss()
        onError()
    })


}

/**
 * if ad is already loaded then it shows the ad otherwise load the ad first then shows
 *
 * @param showInstantly should wait for click threshold requirements to meet or show instantly after loading ad.
 * @param onShown when ad is shown.
 * @param onDismissed when ad is dismissed.
 * @param onError when error occurred.
 */
fun Activity.showInterstitialAd(
    showInstantly: Boolean = admobAds.showInterstitialAdInstantly,
    loadAgainAfterShowing: Boolean = admobAds.loadInterstitialAdAgainAfterShowing,
    onShown: () -> Unit = {},
    onDismissed: () -> Unit = {},
    onError: () -> Unit = {},
    customCode: () -> Unit = {},
    onClickRequirementMatches: () -> Unit = {},
) {

    if (!showInstantly) currentClicks++
    if (!showInstantly) {
        if (currentClicks != admobAds.interstitialAdDelay) {
            Log.d(
                TAG,
                "showInterstitialAd: $showInstantly Click requirements doesn't meet || Current Clicks $currentClicks || Threshold ${admobAds.interstitialAdDelay}"
            )
            onError()
            customCode()
            return
        }
    }
    onClickRequirementMatches()
    currentClicks = 0
    delayAndRun(500L) {

        mInterstitialAd?.let {

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "showInterstitialAd: Ad was clicked.")

                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    Log.d(TAG, "showInterstitialAd: Ad dismissed fullscreen content.")
                    mInterstitialAd = null
                    onDismissed()
                    customCode()
                    if (loadAgainAfterShowing)
                        loadInterstitialAdIfNull(
                            this@showInterstitialAd,

                            )
                }

                override fun onAdFailedToShowFullScreenContent(adErr: AdError) {
                    // Called when ad fails to show.
                    Log.e(
                        TAG,
                        "showInterstitialAd: Ad failed to show fullscreen content. Reason ${adErr.message}"
                    )
                    mInterstitialAd = null
                    onError()
                    customCode()
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "showInterstitialAd: Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "showInterstitialAd: Ad showed fullscreen content.")
                    onShown()
                }
            }
            Log.i(TAG, "showInterstitialAd: Trying to Show the Ad")
            mInterstitialAd?.show(this)
            return@delayAndRun
        }


        customCode()
        Log.i(TAG, "showInterstitialAd: Interstitial Ad is Null Loading It")
        loadInterstitialAdIfNull(
            this,
            onSuccess = {
                showInterstitialAd(
                    showInstantly = showInstantly,
                    loadAgainAfterShowing = loadAgainAfterShowing,
                    onShown = onShown,
                    onDismissed = onDismissed,
                    onError = onError, customCode = customCode
                )
            },
            onFailed = {

            },

            )

    }


}


private var interstitialAdRetries = 0


var adLoadCounter = 0
private fun Context.loadInterstitialAd(
    onSuccess: (ad: InterstitialAd) -> Unit, onFailed: (error: String) -> Unit,
) {
    if (!isAdUnitsLoaded) {
        if (interstitialAdRetries == admobAds.interstitialAdRetriesThreshold) {
            Log.i(TAG, "loadInterstitialAd: MAX RETRIES REACHED")
            return
        }

        Log.d(
            TAG,
            "loadInterstitialAd: Ad Units are not loaded i'll try loading ad units after 10_000ms"
        )
        delayAndRun(AD_RETRY_DELAY) {
            interstitialAdRetries++
            Log.d(TAG, "loadInterstitialAd: Retrying $interstitialAdRetries times")
            loadAdUnits()
            delayAndRun((AD_RETRY_DELAY / 2)) {
                loadInterstitialAd(onSuccess, onFailed)
            }
        }

        return
    }

    if (admobAds.interstitialAd == null) {
        Log.i(TAG, "loadInterstitialAd: Interstitial Ad Units Not Found.. Returning")
        onFailed("Interstitial Ad Units Not Found")
        return
    }
    val adRequest = AdRequest.Builder().build()
    Log.d(TAG, "loadInterstitialAd: Loading Ad")

    InterstitialAd.load(this,
        admobAds.interstitialAd!!,
        adRequest,
        object : InterstitialAdLoadCallback() {

            override fun onAdFailedToLoad(adError: LoadAdError) {
                onFailed(adError.message)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                adLoadCounter++
                Log.d(
                    TAG,
                    "loadInterstitialAd: Ad was loaded. $adLoadCounter times"
                )
                onSuccess(interstitialAd)
            }
        })

}

private fun delayAndRun(delay: Long, code: () -> Unit) {
    android.os.Handler(Looper.myLooper()!!).postDelayed(code, delay)
}

fun getCurrentClicks() = currentClicks


// Animations


private fun View.fadeView(
    from: Float = 1.0f,
    to: Float = 0.8f,
    duration: Long = 1000,
    infinite: Boolean = false,
    reverse: Boolean = true,
    shouldStart: Boolean = true
): AlphaAnimation {
    val animation = AlphaAnimation(from, to)
    animation.duration = duration
    animation.repeatCount = if (infinite) {
        Animation.INFINITE
    } else {
        0
    }
    animation.repeatMode = if (reverse) {
        Animation.REVERSE
    } else {
        Animation.RESTART
    }
    animation.fillAfter = true
    if (shouldStart)
        this.startAnimation(animation)

    return animation

}


private fun View.fadeIn(): AlphaAnimation {
    return fadeView(
        from = 0f,
        to = 1f,
        duration = admobAds.bannerAdAnimationDuration,
        infinite = false,
        reverse = false,
        shouldStart = true
    )
}

private fun View.fadeOut(): AlphaAnimation {
    return fadeView(
        from = 1f,
        to = 0f,
        duration = admobAds.bannerAdAnimationDuration,
        infinite = false,
        reverse = false,
        shouldStart = true
    )
}























