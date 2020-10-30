cmake_minimum_required(VERSION 3.6.0)

add_library(frida-check
    SHARED

    src/main/jni/frida-check.c)