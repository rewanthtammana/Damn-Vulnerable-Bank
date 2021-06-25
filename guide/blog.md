# Damn Vulnerable Bank

Welcome to the Damn Vulnerable Bank blog. This is a vulnerable banking application that allows you to assess your android application hacking skills. This blog involves a lot of things like reversing the application, bypassing protections, analyzing the binaries, decrypting traffic, understanding code, intercepting requests and so on.

You need to have an android mobile or emulator like genymotion instance to process the information.

# Workflow

1. Install the application
   1. Check why the application isn't running
   2. Reverse the application code and analyze
2. TBD


## Get the application running

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

### Reverse the android application

Decompile the application with `apktool` to view the `AndroidManifest` file.

```bash
apktool d dvba.apk -o dvba-apktool
```

![apktool decompile](images/apktool-decompile.png)

Observe the `AndroidManifest` for a while to see if something is responsible for not allowing the application to run in an emulator. The `hardwareAccelerated` flag is set to `true`, that means the application is planning to utilize the mobile's GPU resources to get it running. Here's the catch! Unfortunately, emulators doesn't run with GPU. At least, the genymotion emulator which we are using doesn't have a GPU and that's causing the application to crash.

Change the `hardwareAccelerated` to `false`, re-build the application and try running it.

![apk rebuilt](images/apktool-rebuild.png)

Now, try installing the new apk.

![new apk install](images/new-apk-install.png)

Failed to install due to certificate issues. Create certificates and sign the new apk.

Create a release key keystore

```bash
keytool -genkey -v -keystore my-release-key.keystore -alias dvba-no-gpu -keyalg RSA -keysize 2048 -validity 10000
```

Sign the new built apk with the release key

```bash
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.keystore dvba-no-gpu.apk dvba-no-gpu
```

![sign apk](images/sign-apk.png)

Now, install the application on the device and try running it. Even now the app crashes but this time we get an error message, `Emulator Detected`

![new app crash](images/new-app-crash.png)

Let's reverse the apk code to see if we can find something related to emulator detection. We will use [apkx](https://github.com/b-mueller/apkx) tool.

![apkx](images/apkx.png)

We can see the error message, "Emulator Detected" and the app crashes. Let's find the string in the decompiled code.

![emulator detected cli](images/emulator-detected-cli.png)

Open the file `MainActivity.java`, and check the code.

![emulator detected code](images/emulator-detected-code.png)

As you can see from the above, if `var2_4 != 0`, the application prints, `"Emulator Detected"`. But if you observe carefully, that isn't the block of code that's killing the application. There's a function call in a few if-else blocks in the below lines, `this.finish()`. This particular call is responsible for the application to terminate. 

One of the messages on the screen after the application crashed is, `"Phone is Rooted"`. So, after the code detected we are using a rooted mobile the application crashed due to `this.finish()` call at line 260.

Our target is to bypass this check.

## Bypassing phone rooted check with frida

In line 258, we can see an `if` block that makes the application run the block of code. Our target is to toggle the application code or process in a way that it doesn't enter this block of code.

There are multiple ways to achieve this task. According to the code,the return value of `a.R()`, at line 258 decides whether the application runs the block of code or not. If we can toggle the value of `a.R()`, we can solve the problem. We will use `frida` to perform this hack.

We now understand that we need to toggle `a.R()`. We need to hook the method, `a.R()` and toggle the function's return value to make the application work. Frida comes with Javascript API support to make things easier for us.

```javascript
// @File: scripts/script.js

setTimeout(function() {
    Java.perform(function() {

        // We are loading the class here
        var Rfunc = Java.use("a.a.a.a.a");

        // Whenever a.a.a.a.a.R() is called, the below code is executed
        Rfunc.R.implementation = function() {
            // Capture the return value of R function
            let retval = this.R();
            // Printing the value we got from this function
            console.log("Original return value = ", retval);
            // Toggle the return value to make sure it doesn't enter the if loop
            return !retval
        }
    })
}, 10);
```

Run the frida command to check it

```bash
frida -U -f com.app.damnvulnerablebank -l scripts/script.js 
```

![Frida detected](images/frida-detected.png)

We can see an error message after application crashed, `Frida is running`. This confirms the application is having another check to detect if we are trying to hook the application and tamper with it. If we are trying to hook it, the application crashes. Our target is to bypass this method hooking detection check.

## Bypassing hooking method (frida) detection check

Look into the code for traces of frida to find the piece of code that looks for method hooking detection.

```bash
grep -r -i frida
```

![Frida find library](images/frida-find-library.png)

We can see from the above image, there is a `lib/` folder and `System.loadLibrary((String)"frida-check")`. This means the application is calling a method written in native code to perform the method hooking/frida check. Let's open the `lib/` folder to see what it is having. Since we are using genymotion and it runs on `x86` architecture, let's check that directory. We can see some `*.so` files and they are executable files.

![x86 genymotion](images/x86-genymotion.png)

Our target is to analyze these library files. To analyze these library files, you will need a tool like IDA/ghidra. Let's use ghidra because it has support to multiple architectures with free of cost.

## Binary analysis

Drag and drop the `libfrida-check.so` file to ghidra, select all the default options, and once its loaded double click on the file name to open ghidra. 

![ghidra drag and drop](images/ghidra-drag-drop.png)

This isn't a full strech course on binary analysis, so we skip the basics and try to get started with the analysis.

![ghidra analysis](images/ghidra-analysis.png)

You can see the pseudo code of the `fridaCheck` function on the right side. In line 20, we can see a socket connection. The application is trying to connect to particular port and depending on the status, its returning some value. In order to perform socket connection, it requires a `host` and `port` value and we can see in line 20, its utilizing data in `local_28` which is `0xa2690002`.

In most cases, the binaries contain code in big-endian format. So, we need to convert `0xa2690002` to actual value. This is having 8 bits, we have to split it into half. Now, it will become `0xa269` and `0x0002`. In order to convert them from big-endian to decimal value, we have to swap the first 2 and last 2 bits.

`0xa269` becomes `0x69a2` whose decimal equivaluent is `27042`.

These values are being passed to `connect` function which makes a socket connection. So, this must be the port number, the application is trying to connect. As we know frida runs on client server architecture. Frida's default port is `27042`.

In short, the application is trying to make a socket connection to port `27042` to check if frida is running or not. If frida is running, `27042` will be occupied. Depending on whether the application is able to make socket connection to port `27042`, we decide if frida is running or not.

## Bypassing frida detection check

Ideally, the application is looking for default port of frida only. Bypassing this is simple, we will just need to run frida on another port.

Kill the frida server in the emulator and try running it another port like `1337`.

```bash
emulator$ ./frida-server-14.2.18-android-x86 -l 0.0.0.0:1337
```

```bash
ubuntu$ frida -H $DEVICE_IP:1337 -f com.app.damnvulnerablebank -l scripts/script.js 
```

![Frida bypass](images/frida-check-bypass.png)

You can see from the above image, even when we are hooking the application with frida, it still says `frida is not running`. We bypassed both frida detection and root detection checks.

## Footprinting the application

Start the backend server, enter the URL in the designated field and check for health status. Perform health check to make sure the API is up and running.

![Health Check](images/health-check.png)

There are some pre-created accounts for testing but nevertheless you can still sign up for the new account.

![Home page](images/home-page.png)

You can continue to explore the application but all the data is being sent to the server. Let's use burpsuite to intercept the traffic.

Turn on the interceptor in burp suite, make sure you are listening on all interfaces and try re-running the application with frida. Now, login into the application and you can see similar output on your burpsuite.

![Intercept Burpsuite](images/intercept-burp.png)

The application is using REST API as its backend and in every API in the body of POST request, we will usually see data but here instead both the request and response are encrypted. It means the application is performing encryption and decryption before sending the requests.

Consdering we are seeing REST API, we need to look for API vulnerabilities. But both request and response are encrypted and we can't tamper them. In order for us to tamper with the request and response, we have to decrypt them first. Explore the code till you find this encryption and decryption functions.

## Decrypting response

We can see all the intercepted data is encrypted. It means when we get the response from the server, the application has to decrypt it inorder to perform subsequent operations. We extracted java code from the apk with apkx tool. Since, the decryption is inevitable, let's open a random API endpoint and analyze the java code. I'm opening `ViewBalance.java` for analysis.

If you remember, in the previous section, we have seen the intercepted burpsuite data is having `enc_data` as key. So, the application has to pick the value of, `enc_data` for it to decrypt. Search for `enc_data` in `ViewBalance.java`.

![View Balance decryption code](images/view-balance-decrypt-java.png)

A small snippet from the above screenshot.

```java
...
import c.b.a.e;
...
object = new JSONObject(e.a(object.get("enc_data").toString()));
int n2 = object.getJSONObject("status").getInt("code");
...
```

We can see from the above snippet that this piece of code fetches data from `enc_data`, passes it to `e.a` function, makes it a JSON object and stores in `object` variable. It means our encrypted data is being passed to `e.a` function for processing.

Our primary task is to hook the `e.a` function to see the data it receives and sends. Similar to root detection bypass, we need to leverage frida to achieve this goal. Do not tamper anything yet.

```javascript
/*
Root detection bypass script here
*/

// Decrypting response
setTimeout(function() {
    Java.perform(function() {

        var decryptFunc = Java.use("c.b.a.e");

        decryptFunc.a.implementation = function(enc_data) {
            console.log("Recevied data = ", enc_data)
            let retval = this.a(enc_data);
            console.log("Sending data = ", retval);
            return retval
        }
    })
}, 10);
```

![Decrypted response frida](images/decrypted-response-frida.png)

We can successfully see the decrypted response but its not useful for us. For us to perform pentesting, we need to tamper with the requests but unfortunately even the request body is being encrypted before sending it to the server. It means, the application is encrypting it before sending it to the server. Our task is to find the function encrypting the data.

If you analyze the code of `ViewBalance.java`, we can't see any request parameters in it because it doesn't have any request parameters. We need to find another API that contains request body. For example, let's randomly pick `BankLogin.java` as they will likely have username and password as body parameters. Analyze the code to find the encryption snippet.

![Bank login code](images/bank-login-code.png)

As we can see similar to decrypt function, we have encrypt function here but really obfuscated. The real data here, i.e username and password are being passed to `e.b()` for further processing. So, we need to hook `e.b()` to view unencrypted data.

```javascript
/*
Root detection bypass script here
*/

/*
Decrypt response script here
*/

// Decrypting request
setTimeout(function() {
    Java.perform(function() {

        var encryptFunc = Java.use("c.b.a.e");

        encryptFunc.b.implementation = function(enc_data) {
            console.log("Encryptfunc sending data = ", enc_data)
            let retval = this.b(enc_data);
            console.log("Encryptfunc received data = ", retval);
            return retval
        }
    })
}, 10);
```

![Frida encrypt decrypt data](images/frida-encrypt-decrypt-data.png)

## Recreating encrypt and decrypt functions locally

We achieved great things so far. We managed to view the decrypted data of both request, response and now we even have the ability to tamper with it. But writing our test cases in frida scripts for all endpoints with different parameters is chaos job. If we can somehow manage to replicate the encrypt and decrypt functions on our local systems, we can encrypt and decrypt intercepted data with ease.

From the above analysis, we can conclude the `e.a()` and `e.b()` functions inside class `c.b.a.e` are responsible for encrypt and decrypt functions. Open the file in code editor for further analysis.

![Encrypt decrypt code](images/encrypt-decrypt-code.png)

Woah! We found the encrypt, decrypt logic and our goal is to replicate this locally to ease the pentesting process. Re-build equivalent code locally. I prefer coding in NodeJS.

```javascript
const SECRET = 'amazing';
const SECRET_LENGTH = SECRET.length;

const operate = (input) => {
  let result = "";
  for (let i in input) {
    result += String.fromCharCode(input.charCodeAt(i)^SECRET.charCodeAt(i%SECRET_LENGTH));
  }
  return result;
}

const decrypt = (encodedInput) => {  
  let input = Buffer.from(encodedInput, 'base64').toString();
  let dec = operate(input);
  console.log(dec);
  return dec;
}

const encrypt = (input) => {
  let enc = operate(input.toString());
  let b64 = Buffer.from(enc).toString('base64');
  console.log(b64);
  return b64;
}
```

Now, we have successfully re-built the encryption and decryption functions locally. When we intercept the request, we can use these to decrypt the encrypted data.

![View balance burpsuite](images/view-balance-burp.png)

We have the below string in the response.

```json
{"enc_data":"Gk8SDggaEhJPWwFLDQgFCENAW15XTU8MHxodBgYIQ0BLPRICDgQJGkwaTU8FGx0PRVsWQxgIAgYPDgRYU19XUV1RVksPBAICFBQdMQkUAAMfG0xdUllQQlFdGhw="}
```

Pass the above encrypted text to the `decrypt` function you built locally.

```javascript
decrypt("Gk8SDggaEhJPWwFLDQgFCENAW15XTU8MHxodBgYIQ0BLPRICDgQJGkwaTU8FGx0PRVsWQxgIAgYPDgRYU19XUV1RVksPBAICFBQdMQkUAAMfG0xdUllQQlFdGhw=")
```

![View balance decrypt nodejs](images/view-balance-decrypt-nodejs.png)

Perfect! Our code is working :-)

## Looking for REST API vulnerabilities

In one of the above sections, we explored `view balance` API.

```javascript
decrypt("Gk8SDggaEhJPWwFLDQgFCENAW15XTU8MHxodBgYIQ0BLPRICDgQJGkwaTU8FGx0PRVsWQxgIAgYPDgRYU19XUV1RVksPBAICFBQdMQkUAAMfG0xdUllQQlFdGhw=")
```

![View balance decrypt nodejs](images/view-balance-decrypt-nodejs.png)

Let's see if we can view balance of other users. If you see the intercepted request, there is no body or query string in the request made to view balance. But we can see the `Authorization` header.

In my case, the authorization header is `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIxIiwiaXNfYWRtaW4iOmZhbHNlLCJpYXQiOjE2MjMzMzU4NDV9.lHjjBagQ2OYREBBDl0LXUNLNn1nD6nI4KMxgFlfXRWU`. This looks like  JWT token. Let's decrypt this JWT token.

![Decode JWT user1](images/decode-jwt-user1.png)

As you can see we have a field name `"username":"user1"`. Since there is no body/query parameter in the intercepted request, there is high possibility that the application is extracting username from the authorization header. Let's change the `username` to `user2`, rebuild the request and then send it.

![Encode JWT user2](images/encode-jwt-user2.png)

Copy this JWT token and replace the `Authorization` header with this value and resend the request.

![User2 JWT view balance repeater](images/user2-jwt-view-balance-repeater.png)

We will receive an encrypted response, copy that and pass it to your NodeJS script for decryption.

![User2 response decryption](images/user2-jwt-response-decrypt.png)

As we can see above, it throws `403` error and says the `signature` doesn't match. It means the signature is getting validated on the backend and we can't tamper with the `Authorization` token. It doesn't mean authorization is happening in all endpoints. It might happen, it might not happen, as a security expert its your job to verify them all.

Similarly you can explore the remaining scenarios.

## Sensitive information disclosure

There are multiple ways for sensitive information leakage in android applications. The most common ones are:

* Hardcoded sensitive information
* Leakage via logcat/debug logs

### Hardcoded sensitive information

We have already seen this in the section where we were replicating the cryptographic functions locally. The secret key that's used for crypting purposes is hardcoded in the source code of the application which allowed us to decrypt the encrypted request and responses.

### Adb logcat

We have been using the application for a while. So, let's check the logs via `adb logcat`.

![adb logcat](images/adb-logcat.png)

In the above image, we can see logs of multiple processes. Our target is to find the logs specific to our application. Search for the Process ID (PID) of the damn vulnerable bank application.

```bash
adb shell ps -ef | grep damn
```

![adb shell ps](images/adb-shell-ps.png)

Now, grep `adb logcat` output with the process ID (PID) specific to our target application.

```bash
adb logcat | grep 5021
```

![adb logcat access token](images/adb-logcat-access-token.png)

## Exploiting exported activities

Everything that you see in an android app is kind of being done via an activity. For example, in the given application the login screen, the splash screen, etc. everything is an activity. According to Android developer docs, *An activity is a single, focused thing that the user can do.*

### Exported activities

An exported activity is the one that can be accessed by external components or apps. We can say that exported activities are like `Public` functions in Java, any other parent class or even package can call them. Some activities are built to be user specific, but if those activities are exported and not protected, that creates issue.

We will use drozer to perform this check. Refer to tools page.

![Exported activity no permission](images/exported-activity-no-permission.png)

Interesting, we can see multiple exported activities with no permission checks. If an attacker can invoke these activities without authentication, it can create potential financial impact.

![Exported activity invoke](images/exported-activity-invoke.png)

That's amazing. We are able to invoke the `SendMoney` activity without giving in the credentials. Let's see if we can transfer money to other accounts.

We can get the account numbers list from the github page. Enter one of the valid account numbers to transfer the amount. For ex, user4 account number is 444444.

![Exported activity transfer](images/exported-activity-transfer.png)

Login as `user4` and get confirmation on the exploit.

![Exported activity transaction](images/exported-activity-transactions.png)

Without authneticating into the application, we are able to exploit the sensitive exported activity and transfer amount to other bank accounts.

## Exploiting webview

## Exploiting deeplinks

