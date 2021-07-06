## Installation and usage manual

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

## Building the Apk with Obfuscation

- Go to Build options and select Generate Signed Bundled/Apk
- Then select Apk as option and click next
- Now we need a keystore to sign an apk
- Create a new keystore and remember its password
- After creating select that keystore and enter password
- Now select Build variant as Release and signature version as V2
- Now we can build the apk successfully
