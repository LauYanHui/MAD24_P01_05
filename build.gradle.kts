
buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
// Top-level build file where you can icon_add_24 configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}

