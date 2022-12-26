
object RemoteConfigConstants {

    /**
     * Active Remote Keys
     */
    const val IS_INTERSTITIAL_ACTIVE = "is_interstitial_active"
    const val IS_MEDIUM_NATIVE_ACTIVE = "is_medium_native_active"
    const val IS_SMALL_NATIVE_ACTIVE = "is_small_native_active"
    const val IS_SMALL_BANNER_ACTIVE = "is_small_banner_active"

    /**
     * Priority Remote keys
     */
    const val PRIORITY_INTERSTITIAL = "priority_interstitial"
    const val PRIORITY_MEDIUM_NATIVE = "priority_medium_native"
    const val PRIORITY_SMALL_NATIVE = "priority_small_native"
    const val PRIORITY_SMALL_BANNER = "priority_small_banner"


    /**
     * Setting Variables
     * 0 = no ads
     * 1 = fb ads
     * 2 = admob ads
     * others = no ads
     */
    var interstitialActive = true
    var mediumNativeActive = true
    var smallNativeActive = true
    var smallBannerActive = true

    var priorityInterstitial = 1
    var second_interstitial_priority = 1
    var priorityMediumNative = 2
    var prioritySmallNative = 2
    var prioritySmallBanner = 1

}