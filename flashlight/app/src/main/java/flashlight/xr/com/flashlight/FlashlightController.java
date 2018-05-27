/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package flashlight.xr.com.flashlight;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

/**
 * Manages the flashlight.
 */
public class FlashlightController {

    private static final String TAG = "FlashlightController";

    private static final String FLASH_LIGHT_ENABLE = "/sys/class/flashlight/torch/enable";

    private static File file = new File(FLASH_LIGHT_ENABLE);

    public static final String SWITCH_ON = "1";
    public static final String SWITCH_OFF = "0";

    public static String readFile() {
        String str = new String("");
        if (file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    str = str + line;
                }
            } catch (Exception e) {
                Log.d(TAG, "Read file error!!!");
                str = "readError";
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            Log.d(TAG, "read value is " + str.trim());
        } else {
            Log.d(TAG, "File is not exist");
        }
        return str.trim();
    }

    public static boolean writeFile(String str) {
        boolean flag = true;
        FileOutputStream out = null;
        PrintStream p = null;

        if (file.exists()) {
            try {
                out = new FileOutputStream(FLASH_LIGHT_ENABLE);
                p = new PrintStream(out);
                p.print(str);
            } catch (Exception e) {
                flag = false;
                Log.d(TAG, "Write file error!!!");
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (p != null) {
                    try {
                        p.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } else {
            Log.d(TAG, "File is not exist");
        }
        return flag;
    }
}
