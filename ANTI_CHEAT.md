# Android Tournament Anti-Cheat Strategy

To secure live tournaments and prevent score manipulation on the Android platform, we implement native security checks and validation mechanisms.

---

## 1. Device and App Integrity (Google Play Integrity API)

We integrate Google's **Play Integrity API** to verify the environment before letting a user join a live tournament:
* **Device Integrity**: Confirms the app is running on a certified Android device with Google Play Services (blocking custom ROMs, uncertified systems, or bootloaders that bypass safety checks).
* **App Integrity**: Verifies the running binary matches the official version signed and published on Google Play. Any modified or repacked APK will fail this check.
* **Licensing**: Validates that the active user acquired the app legally through Google Play.

---

## 2. Runtime Tampering & Hooking Detection

We implement runtime checks to identify instrumentation tools that modify game memory or variables:
* **Frida Detection**: Scan `/proc/self/maps` at runtime to check for loaded dynamic libraries associated with Frida (e.g., searching for `frida-agent.so` or `libfrida`) and block execution.
* **Xposed Framework Detection**: Scan the classloader for Xposed classes (specifically `de.robv.android.xposed.XposedBridge`) and check call stacks for Xposed method invocations.
* **Debugger Attachment**: Check `android.os.Debug.isDebuggerConnected()` and verify that the `ApplicationInfo.FLAG_DEBUGGABLE` flag is disabled in the app manifest to block active code debugging.

---

## 3. Emulator Detection

Live tournaments are restricted to physical mobile devices to prevent emulator-based macros and scripts:
* **Build Properties Check**: Scan system properties for emulator keys (such as `ro.hardware` matching `goldfish` or `ranchu`, `ro.kernel.qemu` set to `1`, or `ro.product.model` containing `sdk_gphone`).
* **Sensor Check**: Emulators often lack physical hardware sensors. Querying the `SensorManager` to ensure the presence of standard components (accelerometer, gyroscope) helps verify physical hardware.

---

## 4. Code Obfuscation & Sideloading Protections

* **DexGuard / ProGuard**: Obfuscate code paths and encrypt strings (API endpoints, encryption keys) to prevent static analysis using tools like Jadx or Apktool.
* **Installer Verification**: Verify the installer package name using `context.packageManager.getInstallSourceInfo()` to check if the app was sideloaded via ADB or third-party package managers instead of the Google Play Store.

---

## 5. Network Traffic Security

* **Network Security Config**: Configure `network_security_config.xml` to enforce strict HTTPS and pin SSL certificate hashes (Certificate Pinning) directly in the Android network layer, blocking packet sniffers like Charles Proxy or HTTP Canary.
