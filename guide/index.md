<meta property='twitter:title' content="Damn Vulnerable Bank Guide"/>
<meta property='twitter:image' content="https://raw.githubusercontent.com/rewanthtammana/Damn-Vulnerable-Bank/master/images/damn-vulnerable-bank-guide.png"/>
<meta name="twitter:card" content="summary_large_image"/>

## About Damn Vulnerable Bank

[Damn Vulnerable Bank](https://github.com/rewanthtammana/Damn-Vulnerable-Bank) is designed to be an intentionally vulnerable android application. Numerous resources are available on the internet to get started with android application security. This application provides an interface to assess the security knowledge you gained over time. There are multiple vulnerabilities in the application and we documented all of them in this [guide](https://rewanthtammana.com/damn-vulnerable-bank/). Take your own sweet time to explore the application and detect all the vulnerabilities.

<style>
img.resize {
  max-width:50%;
  max-height:50%;
  display: block;
  margin-left: auto;
  margin-right: auto;
  width: 50%;
}
img {
  border: 5px solid #555;
}
</style>


<img class="resize" align="center" src="https://rewanthtammana.com/damn-vulnerable-bank/images/ui.jpg" alt="Splash screen here">

## Outline

<!-- - [About Damn Vulnerable Bank](about.md) -->
<!-- - [Introduction](README.md) -->
- [Authors](authors.md)
- [Tools](tools.md)
- [Installation](installation/index.md)
    - [Bypass](installation/bypass/index.md)
        - [GPU detection](installation/bypass/gpu-detection.md)
        - [Root Detection](installation/bypass/root-detection.md)
        - [Frida detection with Ghidra](installation/bypass/frida-detection-with-ghidra.md)
- [Footprinting](footprinting/index.md)
    - [Decrypting response](footprinting/decrypt-response.md)
    - [Recreating crypto functions](footprinting/crypto.md)
- [Exploits](exploits/index.md)
    - [REST API vulnerabilities](exploits/exploit-1.md)
    - [Sensitive Information Disclosure](exploits/exploit-2.md)
    - [Exported activities](exploits/exploit-3.md)
    - [Webview via deeplink](exploits/exploit-4.md)
- [Contribute](contribution.md)
- [Additional Resources](additional-resources.md)
