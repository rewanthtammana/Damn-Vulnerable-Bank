.PHONY: all install clean

all:
	/root/Android/Sdk/ndk-bundle/ndk-build
	rm -rf ../jniLibs/*
	cp -r ../libs/* ../jniLibs/
clean:
	/root/Android/Sdk/ndk-bundle/ndk-build clean
	rm -rf ../libs ../obj
	rm -rf ../jniLibs/*
