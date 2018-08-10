#FastJson
-dontwarn com.alibaba.fastjson.**
-keepattributes Signature
-keepattributes *Annotation*
#字体
-keep class com.joanzapata.iconify.** { *; }
#fragmenation
-keep class me.yokeyword.** { *; }
#ButterKnife
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
#Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.* { ; }
-keep interface okhttp3.* { ; }
-dontwarn okhttp3.

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }