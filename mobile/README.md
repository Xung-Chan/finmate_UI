## Environment

Config  `secret.gradle`:

GATEWAY_URL = <API_GATEWAY_URL>

### Example:
```env
ext {
    devConfig = [
        "GATEWAY_URL": "https://nsqxjbdt-9000.asse.devtunnels.ms/",
        "SHARED_PREFS_KEY": "finmate_shared_prefs_dev",
        ]
    prodConfig = [
        "GATEWAY_URL": "https://nsqxjbdt-9000.asse.devtunnels.ms/",
        "SHARED_PREFS_KEY": "finmate_shared_prefs_dev",
        ]
}

```


## How to Start

### Open CMD at mobile root

```cmd
./gradlew installDebug

```
 
