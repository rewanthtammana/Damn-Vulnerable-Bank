# Damn-Vulnerable-Bank

## About application
Damn Vulnerable Bank Android Application aims to provide an interface for everyone to get a detailed understanding with internals and security aspects of android application.

## Features
- [ ] Sign up
- [ ] Login
- [ ] My profile interface
- [ ] Settings interface to update backend URL
- [ ] Add fingerprint check before transferring/viewing funds
- [ ] Add pin check before transferring/viewing funds
- [ ] View balance
- [ ] Transfer money
  - [ ] Via manual entry
  - [ ] Via QR scan
- [ ] Add beneficiary
- [ ] Delete beneficiary
- [ ] View beneficiary
- [ ] View transactions history
- [ ] Download transactions history

## List of possible vulns in the application

- [ ] Root and emulator detection
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
- [ ] Webview integration
- [ ] Deep links
- [ ] IDOR

## Backend to-do

- [ ] Add profile and change-password routes
- [ ] Create different secrets for admin and other users
- [ ] Add dynamic generation of secrets to verify JWT tokens
- [ ] Introduce bug in jwt verficiation
- [ ] Find a way to store database and mount it while using docker
