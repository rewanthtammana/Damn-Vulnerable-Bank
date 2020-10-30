//
// Created by hrushi on 10/30/20.
//


	// Client side C/C++ program to demonstrate Socket programming
#include <unistd.h>
#include <stdio.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>
#include <jni.h>

#define PORT 27042 // Frida Port

    JNIEXPORT jint JNICALL Java_com_app_damnvulnerablebank_FridaCheckJNI_fridaCheck(JNIEnv * env, jobject jobj) {
    	int sock = 0;
    	struct sockaddr_in serv_addr;

    	if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    	{
    	    // Socket creation
    	    // Pass
    		return 0;
    	}

    	serv_addr.sin_family = AF_INET;
    	serv_addr.sin_port = htons(PORT);

    	// Convert IPv4 and IPv6 addresses from text to binary form
    	if(inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr)<=0)
    	{
    	    // Pass
    	    // Address resolution check
    		return 0;
    	}

    	if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
    	{
    	    // Pass
    	    // Could not connect
    		return 0;
    	}

    	// Fail (Frida is running)
    	return 1;
    }




