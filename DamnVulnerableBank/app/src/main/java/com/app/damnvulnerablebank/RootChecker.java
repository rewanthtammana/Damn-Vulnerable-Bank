package com.app.damnvulnerablebank;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


    public class RootChecker {

        public static boolean isRooted(banklogin context) {
            boolean isTestBuild = isTestBuild();
            boolean hasSuperuserAPK = hasSuperuserAPK();
            boolean hasChainfiresupersu = hasChainfiresupersu(context);
            boolean hasSU = hasSU();
            Log.d("RootChecker", "isTestBuild: " + isTestBuild + " hasSuperuserAPK: " + hasSuperuserAPK + " hasChainfiresupersu: " + hasChainfiresupersu + " hasSU: " + hasSU);
            return isTestBuild || hasSuperuserAPK || hasChainfiresupersu || hasSU;
        }

        /**************************************** Checker methods *************************************/
        private static boolean isTestBuild() {
            String buildTags = android.os.Build.TAGS;
            return buildTags != null && buildTags.contains("test-keys");
        }

        private static boolean hasSuperuserAPK() {
            try {
                File file = new File("/system/app/Superuser.apk");
                return file.exists();
            } catch (Exception e) {
                return false;
            }
        }

        private static boolean hasChainfiresupersu(Context context) {
            return isPackageInstalled("eu.chainfire.supersu", context);
        }

        private static boolean hasSU() {
            return findBinary("su") || executeCommand(new String[] { "/system/xbin/which", "su" }) || executeCommand(new String[] { "which", "su" });
        }

        /**************************************** Helper methods **************************************/
        private static boolean isPackageInstalled(String packagename, Context context) {
            PackageManager pm = context.getPackageManager();
            try {
                pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        private static boolean findBinary(String binaryName) {
            String[] places = {
                    "/sbin/",
                    "/system/bin/",
                    "/system/xbin/",
                    "/data/local/xbin/",
                    "/data/local/bin/",
                    "/system/sd/xbin/",
                    "/system/bin/failsafe/",
                    "/data/local/" };
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    return true;
                }
            }
            return false;
        }

        private static boolean executeCommand(String[] command) {
            Process localProcess = null;
            BufferedReader in = null;
            try {
                localProcess = Runtime.getRuntime().exec(command);
                in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
                return (in.readLine() != null);
            } catch (Exception e) {
                return false;
            } finally {
                if (localProcess != null) localProcess.destroy();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e("RootChecker", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

