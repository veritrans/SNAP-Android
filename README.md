# Snap SDK Android

>[!important]
>
>❗️IMPORTANT NOTICE
>
>Starting June 2026, we will gradually cease support for Mobile SDK in Midtrans. We encourage you in the meantime to start migrating to our Snap Checkout or Core API to ensure you're getting the best experience and security standard for your payment gateway implementation.
>
>For new merchants onboarding/integrating after March 6th 2026, please use our Core API or Snap Checkout instead as newer integration will not be supported. This page will be maintained for our existing users, until support is completely ceased, to give enough time for merchants to migrate.



### Overview

Midtrans's mobile Snap SDK helps integrating Midtrans's services into a mobile app. This makes it easier for merchants to add payment capabilities to their mobile app, without having to develop everything from scratch. By using Midtrans's mobile Snap SDK, merchants can focus on just building their own app without worrying about the payment integration.

### Demo App

You can run the the demo `app` in this project to see it in action.

Other sample app can be found [here](https://github.com/veritrans/midtrans-SDK-SampleApp)

### installation

```
repositories {
    jcenter()
        maven { url "https://jitpack.io" }
    }
    
    
dependencies {
    // For using the Midtrans Sandbox
    implementation 'com.midtrans:uikit:2.4.0-SANDBOX' // change the number to the latest version
  }
  
dependencies {
    // For using the Midtrans Production
    implementation 'com.midtrans:uikit:2.4.0' // change the number to the latest version
}
```

### Getting Started

The complete Midtrans SDK Docs can be found here: [docs](https://docs.midtrans.com/reference/mobile-sdk-overview)

[Midtrans website]: https://midtrans.com/
