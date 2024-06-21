# Snap SDK Android


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
    implementation 'com.midtrans:uikit:2.2.0-SANDBOX' // change the number to the latest version
  }
  
dependencies {
    // For using the Midtrans Production
    implementation 'com.midtrans:uikit:2.2.0' // change the number to the latest version
}
```

### Getting Started

The complete Midtrans SDK Docs can be found here: [docs](https://docs.midtrans.com/reference/mobile-sdk-overview)

[Midtrans website]: https://midtrans.com/
