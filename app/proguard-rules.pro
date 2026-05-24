# Room rules
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public <init>(...);
}
-keep class * extends androidx.room.RoomDatabase
-keep class com.freetime.dualist.data.** { *; }

# Compose rules
-keepclassmembers class * extends androidx.compose.ui.node.RootForTest { *; }

# Serialization
-keepattributes *Annotation*, EnclosingMethod, Signature
-keepclassmembers class kotlinx.serialization.json.** {
    *** serializer(...);
}
