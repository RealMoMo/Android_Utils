package com.realmo.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import com.mstar.android.storage.MStorageManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RealMo
 * @date 创建时间：2017-8-31 上午9:09:45
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class UsbUtils {

	
	
	public static final String SAME_FILE="same_file";
	/**
	 * 
	 * @param context
	 * @return usb root path array
	 */
	public static String[] getStoragePaths(Context context) {
		List<String> pathsList = new ArrayList<String>();

		StorageManager storageManager = (StorageManager) context
				.getSystemService(Context.STORAGE_SERVICE);
		try {
			Method method = StorageManager.class
					.getDeclaredMethod("getVolumePaths");
			method.setAccessible(true);
			Object result = method.invoke(storageManager);
			if (result != null && result instanceof String[]) {
				String[] pathes = (String[]) result;
				for (String path : pathes) {

					if (path.contains("/sdcard") || path.contains("/storage"))
						continue;
					pathsList.add(path);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			File externalFolder = Environment.getExternalStorageDirectory();
			if (externalFolder != null) {
				pathsList.add(externalFolder.getAbsolutePath());
			}
		}
		for (String string : pathsList) {
			Log.d("realmo", "usb path-->:" + string);
		}
		return pathsList.toArray(new String[pathsList.size()]);
	}

	/**
	 * 
	 * @param
	 * @return usb devices label/name
	 */
	public static String getUsbDeviceLabel(Context context, String strMountPath) {
		String strLabel = "USB";
		StorageManager mStorageManager = (StorageManager) context
				.getSystemService(Context.STORAGE_SERVICE);
		MStorageManager sm = MStorageManager.getInstance(context);
		if (Environment.MEDIA_MOUNTED.equals(sm.getVolumeState(strMountPath))) {
			strLabel = sm.getVolumeLabel(strMountPath);
		}

		if ((strLabel == null) || strLabel.isEmpty()) {
			strLabel = "USB";
		}
		Log.d("realmo", "strMountPath-->" + strMountPath);
		Log.d("realmo", "strLabel-->" + strLabel);
		return strLabel;
	}

	/**
	 * 
	 * @param childPath
	 *            like:x9/x9.png
	 * @param usbRootPathArray
	 *            like:/mnt/usb/sdc1
	 * @param selectUsbType
	 * @return filePath:usbRootPath+childPath
	 */
	public static String getSelectUsbDeviceFilePath(String childPath,
			String[] usbRootPathArray, String selectUsbType) {
		int usbDevicesCount = usbRootPathArray.length;
		// only one usb device situation
		if (usbDevicesCount == 1) {
			return usbRootPathArray[0] + "/" + childPath;
		}

		// more usb devices situation

		if (usbDevicesCount > 1) {
			List<File> exsitFiles = new ArrayList<File>();
			File[] files = new File[usbDevicesCount];
			for (int i = 0; i < usbDevicesCount; i++) {
				files[i] = new File(usbRootPathArray[i] + "/" + childPath);
				if (files[i].exists()) {
					exsitFiles.add(files[i]);
				}
			}

			
			if (exsitFiles.size() == 1) {
				return exsitFiles.get(0).getAbsolutePath();
			} 
			// Determine whether there is the same file
			else if (exsitFiles.size() > 1) {
				// Determine usbType  realmo:no way
//				for (int i = 0; i < exsitFiles.size(); i++) {
//					if (Uri.fromFile(exsitFiles.get(i))
//							.getEncodedPath().contains(selectUsbType)) {
//						return exsitFiles.get(i).getAbsolutePath();
//
//					}
//
//				}
				
				// return constant flag
				return SAME_FILE;
				
				
			}

		}

		return null;
	}
}
