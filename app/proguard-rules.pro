# Add project specific ProGuard rules here.

# We're not including default Android ProGuard files.
# Instead we're copying specific rules when needed.
# See: https://www.zacsweers.dev/android-proguard-rules/

# Just minimize and let R8 do its optimizations, but leave original names
# for now, because we're crashing on startup if obfuscation is enabled.
-dontobfuscate
# Write out more information during processing.
-verbose

# A few rules needed for Retrofit to work with R8 full mode.
# We have to maintain them temporarily until a new Retrofit release includes them.

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-dontwarn androidx.work.testing.TestDriver
-dontwarn androidx.work.testing.WorkManagerTestInitHelper
# R8 full mode strips generic signatures from return types if not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# End of Retrofit rules from https://github.com/square/retrofit/blob/ef8d867ffb34b419355a323e11ba89db1904f8c2/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro

# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class org.mozilla.social.core.navigation.** { *; }

-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite* {
   <fields>;
}

-keepclassmembers class * implements android.os.Parcelable {
        public static final ** CREATOR;
}