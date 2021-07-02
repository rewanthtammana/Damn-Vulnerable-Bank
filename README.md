
# Damn-Vulnerable-Bank

**Guide:** https://rewanthtammana.com/damn-vulnerable-bank/

## About application
Damn Vulnerable Bank Android Application aims to provide an interface for everyone to get a detailed understanding with internals and security aspects of android application.

<img src="https://github.com/rewanth1997/Damn-Vulnerable-Bank/blob/master/images/screen1.jpg" align="centre" height="600" width="395"><img src="https://github.com/rewanth1997/Damn-Vulnerable-Bank/blob/master/images/screen2.jpg" align="centre" height="600" width="395">

## How to Use Application
- Clone the repository and run the [Backend Server](https://github.com/rewanth1997/Damn-Vulnerable-Bank/tree/master/BackendServer) as per instructions in the link.
- We have released the Apk so after downloading install it via adb or manual.
- After Installation open the App and add Backend IP in Homescreen
- Test running status by pressing health check
- Now create an account by signup option and then login with your credentials
- Now you can see the dashboard and perform banking operations
- Login as admin to approve beneficiary
- The database is pre-populated with a few users for quick exploration.

|  Username |  Password |  Account Number | Beneficiaries | Admin privileges |
|---|---|---|---|---|
| user1  | password1  | 111111 | 222222, 333333, 444444 | No |
| user2  | password2  | 222222 | None  | No |
| user3  | password3  | 333333 | None  | No |
| user4  | password4  | 444444 | None  | No |
| admin  | admin  | 999999 | None  | Yes |

## Features
- [x] Sign up
- [x] Login
- [x] My profile interface
- [x] Change password
- [x] Settings interface to update backend URL
- [x] Add fingerprint check before transferring/viewing funds
- [x] Add pin check before transferring/viewing funds
- [x] View balance
- [x] Transfer money
  - [x] Via manual entry
  - [ ] Via QR scan
- [x] Add beneficiary
- [ ] Delete beneficiary
- [x] View beneficiary
- [x] View transactions history
- [ ] Download transactions history

## Building the Apk with Obfuscation

- Go to Build options and select Generate Signed Bundled/Apk
- Then select Apk as option and click next
- Now we need a keystore to sign an apk
- Create a new keystore and remember its password
- After creating select that keystore and enter password
- Now select Build variant as Release and signature version as V2
- Now we can build the apk successfully

## List of vulnerabilities in the application

To keep things crisp and interesting, we hidden this section. Do not toggle this button if you want a fun and challenging experience. Try to explore the application, find all the possible vulnerabilities and then cross check your findings with this list.

<details>
  <summary>Spoiler Alert</summary>

- [x] Root and emulator detection
- [x] Anti-debugging checks (prevents hooking with frida, jdb, etc)
- [ ] SSL pinning - pin the certificate/public key
- [x] Obfuscate the entire code
- [x] Encrypt all requests and responses
- [x] Hardcoded sensitive information
- [x] Logcat leakage
- [ ] Insecure storage (saved credit card numbers maybe)
- [x] Exported activities
- [ ] JWT token
- [x] Webview integration
- [x] Deep links
- [ ] IDOR
</details>

## Backend to-do

- [x] Add profile and change-password routes
- [ ] Create different secrets for admin and other users
- [ ] Add dynamic generation of secrets to verify JWT tokens
- [ ] Introduce bug in jwt verification
- [x] Find a way to store database and mount it while using docker
- [X] Dockerize environment

## Authors

Thanks to these amazing people

|   |   |   |
|---|---|---|
| Rewanth Tammana (Rest API)  | [Github](https://github.com/rewanthtammana/)  | [LinkedIn](https://www.linkedin.com/in/rewanthtammana/)  |
| Hrushikesh Kakade (Android App)  | [Github](https://github.com/HrushikeshK/)  | [LinkedIn](https://www.linkedin.com/in/hrushikeshkakade/)  |
| Akshansh Jaiswal (Android App)  | [Github](https://github.com/jaiswalakshansh)  | [LinkedIn](https://www.linkedin.com/in/akshanshjaiswal/)  |
