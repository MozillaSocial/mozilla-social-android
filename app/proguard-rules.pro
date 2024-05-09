# Add project specific ProGuard rules here.

# We're not including default Android ProGuard files.
# Instead we're copying specific rules when needed.
# See: https://www.zacsweers.dev/android-proguard-rules/

# Just minimize and let R8 do its optimizations, but leave original names
# for now, because we're crashing on startup if obfuscation is enabled.
-dontobfuscate
# Write out more information during processing.
-verbose

# Work Manager

-dontwarn androidx.work.testing.TestDriver
-dontwarn androidx.work.testing.WorkManagerTestInitHelper


# Ktor
-dontwarn org.slf4j.impl.StaticLoggerBinder


# Our app

-keep class social.firefly.core.navigation.** { *; }

-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite* {
   <fields>;
}

-keepclassmembers class * implements android.os.Parcelable {
        public static final ** CREATOR;
}