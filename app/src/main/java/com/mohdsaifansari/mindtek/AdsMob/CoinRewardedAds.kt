package com.mohdsaifansari.mindtek.AdsMob

import android.app.Activity
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

fun rewardedAds(
    activity: Activity,
    viewModal: CoinViewModal,
    closeDialogbox: (Boolean) -> Unit,
    closeCircularloading: (Boolean) -> Unit
) {
    var rewardedAd: RewardedAd? = null
    RewardedAd.load(activity,
        "ca-app-pub-8292164393279864/9116370593",
        AdRequest.Builder().build(),
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                closeDialogbox(false)
                closeCircularloading(false)
                rewardedAd = null
                Toast.makeText(activity, "Failed to load ad", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded(loadedRewardedAd: RewardedAd) {
                closeDialogbox(false)
                closeCircularloading(false)
                rewardedAd = loadedRewardedAd
                rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        rewardedAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                    }
                }
                rewardedAd?.show(activity, object : OnUserEarnedRewardListener {
                    override fun onUserEarnedReward(p0: RewardItem) {
                        viewModal.addCoin(activity)
                        viewModal.getCoin()
                    }

                })
            }
        })
}