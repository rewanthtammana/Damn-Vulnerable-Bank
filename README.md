# Damn-Vulnerable-Bank

## About application
Damn Vulnerable Bank Android Application aims to provide an interface for everyone to get a detailed understanding with internals and security aspects of android application.

## Features
- [x] Sign up
- [x] Login
- [ ] My profile interface
- [x] Change password
- [x] Settings interface to update backend URL
- [x] Add fingerprint check before transferring/viewing funds
- [ ] Add pin check before transferring/viewing funds
- [x] View balance
- [x] Transfer money
  - [x] Via manual entry
  - [ ] Via QR scan
- [x] Add beneficiary
- [ ] Delete beneficiary
- [x] View beneficiary
- [x] View transactions history
- [ ] Download transactions history

## List of possible vulns in the application

- [x] Root and emulator detection
- [ ] Anti-debugging checks (prevents hooking with frida, jdb, etc)
- [ ] SSL pinning - pin the certificate/public key
- [ ] Obfuscate the entire code
- [ ] Hardcoded sensitive information
- [ ] Logcat leakage
- [ ] Insecure storage (saved credit card numbers maybe)
- [ ] Exported activities
- [ ] Exported services
- [ ] Exported broadcasts
- [ ] Exported content providers
- [ ] JWT token
- [x] Webview integration
- [x] Deep links
- [ ] IDOR

## Backend to-do

- [ ] Add profile and change-password routes
- [ ] Create different secrets for admin and other users
- [ ] Add dynamic generation of secrets to verify JWT tokens
- [ ] Introduce bug in jwt verficiation
- [ ] Find a way to store database and mount it while using docker
- [X] Dockerize environment

## Authors

Thanks to these amazing people

|   |   |   |
|---|---|---|
| Rewanth Cool (Rest API)  | [Github](https://github.com/rewanth1997/)  | [LinkedIn](https://www.linkedin.com/in/rewanthcool/)  |
| Hrushikesh Kakade (Android App)  | [Github](https://github.com/HrushikeshK/)  | [LinkedIn](https://www.linkedin.com/in/hrushikeshkakade/)  |
| Akshansh Jaiswal (Android App)  | [Github](https://github.com/jaiswalakshansh)  | [LinkedIn](https://www.linkedin.com/in/akshanshjaiswal/)  |