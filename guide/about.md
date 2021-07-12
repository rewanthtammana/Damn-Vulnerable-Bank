# About Damn Vulnerable Bank

[Damn Vulnerable Bank](https://github.com/rewanthtammana/Damn-Vulnerable-Bank) is designed to be an intentionally vulnerable android application. Numerous resources are available on the internet to get started with android application security. This application provides an interface to assess the security knowledge you gained over time. There are multiple vulnerabilities in the application and we documented all of them in this [guide](https://rewanthtammana.com/damn-vulnerable-bank/). Take your own sweet time to explore the application and detect all the vulnerabilities.

<!-- ![Damn Vulnerable Bank Home](images/screen2.jpg) -->

## Damn Vulnerable Bank Guide Outline

- [About Damn Vulnerable Bank](about.md)
- [Tools](tools.md)
- [Installation](installation/readme.md)
    - [Bypass](installation/bypass/readme.md)
        - [GPU detection](installation/bypass/gpu-detection.md)
        - [Root Detection](installation/bypass/root-detection.md)
        - [Frida detection with Ghidra](installation/bypass/frida-detection-with-ghidra.md)
- [Footprinting](footprinting/readme.md)
    - [Decrypting response](footprinting/decrypt-response.md)
    - [Recreating crypto functions](footprinting/crypto.md)
- [Exploits](exploits/readme.md)
    - [REST API vulnerabilities](exploits/exploit-1.md)
    - [Sensitive Information Disclosure](exploits/exploit-2.md)
    - [Exported activities](exploits/exploit-3.md)
    - [Webview via deeplink](exploits/exploit-4.md)
- [Contribute](CONTRIBUTION.md)
<!-- - [Additional Resources](additional-resources.md) -->

## Authors

Damn Vulnerable Bank was created by [Rewanth Tammana](https://rewanthtammana.com), [Akshansh Jaiswal](https://akshanshjaiswal.com/), [Hrushikesh Kakade](https://hkh4cks.com/).

---

[Rewanth Tammana](https://rewanthtammana.com) is a security ninja, open-source contributor, and Senior Security Architect at Emirates NBD. He is passionate about DevSecOps, Application, and Container Security. He added 17,000+ lines of code to Nmap (famous as Swiss Army knife of network utilities). Holds industry [certifications](https://rewanthtammana.com/#certifications) like CKS (Certified Kubernetes Security Specialist), CKA (Certified Kubernetes Administrator), etc. Rewanth [speaks and delivers training](https://rewanthtammana.com/#talks) at multiple international security conferences around the world including Hack In The Box (Dubai and Amsterdam), CRESTCon UK, PHDays, Nullcon, Bsides, CISO Platform, null chapters and multiple others. He was recognized as one of the MVP researchers on Bugcrowd (2018) and identified vulnerabilities in several organizations. He also published an [IEEE research paper](https://rewanthtammana.com/#featured) on an offensive attack in Machine Learning and Security. He was also a part of the renowned Google Summer of Code program.

[Akshansh Jaiswal](https://akshanshjaiswal.com/) is a security engineer at CRED who works closely around Web ,Mobile and Cloud Security.He is also an active CTF player where he has won several CTF's such as Hackerone  CTF's -h1 100k CTF, Hacky Holidays CTF,h1-2006 CTF, BugPOC CTF's and community CTF's. He also participates actively in Bug Bounties where he is an active hacker on platforms like Hackerone and Synack Red Team where he finds and reports vulnerabilities to various organisations.He has also been part of Hackerone exclusive Live hacking event h1-2103 where selected hackers got a chance to find security issues in Amazon public applications and infrastructure.

[Hrushikesh Kakade](https://hkh4cks.com/) is a Payatu bandit who specializes in advanced assessments of Mobile Security (Android and iOS), Network Infrastructure Security, DevSecOps, Container security, Web security, and Cloud security. Hrushikesh is a member of the Synack Red Team and is a holder of renowned OSCP (Offensive Security Certified Professional) certification. He is an active member of local Cybersecurity chapters and has delivered multiple talks and workshops. He is an Open Source Contributor and has a keen understanding of Linux Internals. He has received multiple CVEs to his name for finding vulnerabilities in different applications.
