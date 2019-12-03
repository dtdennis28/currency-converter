object ProjectConstants {
    val compileSdkVersion = 28
    val buildToolsVersion = "28.0.3"

    // Need at least 16 for AppCenter
    val minSdkVersion = 16

    val targetSdkVersion = 28

    // Semantic version; as a convention, for determining version code, parts are powers of 10 (e.g. 1.0.0 == '1' 0 '0' 0 '0')
    val versionCode = 10000
    val versionName = "1.0.0"

    val jacocoVersion = "0.8.4"
}