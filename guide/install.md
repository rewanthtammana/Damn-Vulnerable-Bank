## Install Application

### Get the application running

Download the [apk](https://github.com/rewanthtammana/Damn-Vulnerable-Bank/raw/master/dvba.apk) from the repository. Try installing and running it.

### Install the application

I'm using genymotion as my emulator to install the application.

```bash
adb install dvba.apk
```

![Install app](images/install-app.png)

Application installed successfully.

### Run the application

Try running the application. It doesn't work.

![App crashed](images/app-crash.png)

Looks like the app isn't built to work in an emulator. Let's fix it :-)
