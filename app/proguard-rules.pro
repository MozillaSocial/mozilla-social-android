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

# End of Retrofit rules from https://github.com/square/retrofit/blob/029cbb419553d6d9f5fc642a2c716556d630b4d4/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro

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

# This is required for retrofit.  It's merged into retrofit's master branch, but not released yet
# https://github.com/square/retrofit/pull/3910/files/9831d5c3f9d28c61d29078d89645bce8ea258366#diff-50d428633d98e235831f5f9b75d7aa48897d5edc2bed30c55c9bb9ec20b36f82
-keep,allowobfuscation,allowshrinking class retrofit2.Response