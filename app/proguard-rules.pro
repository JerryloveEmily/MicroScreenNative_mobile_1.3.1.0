# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes *Annotation*

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

#-libraryjars libs/BaiduLBS_Android.jar
#-libraryjars libs/gson-2.2.4.jar
#-libraryjars libs/volley.jar
#-libraryjars libs/com.umeng.fb.5.4.0.jar

#项目里面包含的包也不能混淆
-keep class com.baidu.** { *; }
-dontwarn com.baidu.**
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class com.google.gson.**{*;}
-dontwarn com.google.gson.**
-keep class com.android.volley.**{*;}
-keep class com.alimama.**{*;}
-keep class com.umeng.**{*;}
-dontwarn com.umeng.**
-keep class com.umeng.fb.**{*;}
-dontwarn com.umeng.fb.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.boding.microscreen.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

#Serializable 的配置
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

#-dontskipnonpubliclibraryclasses

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep class * extends java.lang.annotation.Annotation{*;}
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver