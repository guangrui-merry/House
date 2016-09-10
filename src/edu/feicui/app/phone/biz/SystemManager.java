package edu.feicui.app.phone.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * ϵͳ��Ϣ����� (δʹ��)
 */
public class SystemManager {

	public static final String basicInfos[] = { "�豸�ͺ�:", "ϵͳ�汾:", "�ֻ�����:", "��Ӫ��:", "�Ƿ�ROOT:" };
	public static final String CPUInfos[] = { "CPU�ͺ�:", "CPU������:", "���Ƶ��:", "���Ƶ��:", "��ǰƵ��:" };
	public static final String resolutionInfos[] = { "����ͷ����:", "��Ƭ���ߴ�:", "�����:" };
	public static final String pixelInfos[] = { "��Ļ�ֱ���:", "�����ܶ�:", "��㴥��:" };
	public static final String WIFIInfos[] = { "WIFI���ӵ�:", "WIFI��ַ:", "WIFI�����ٶ�:", "MAC��ַ:", "����״̬:" };
	private Context mContext = null;
	private TelephonyManager tm = null;
	private WifiManager wm = null;

	public SystemManager(Context context) {
		mContext = context;
		tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * ��������Ϣ
	 */
	public boolean BasicInfo(final String[] datas) {
		if (datas == null || datas.length < basicInfos.length) {
			return false;
		}
		datas[0] = basicInfos[0] + Build.MODEL;
		datas[1] = basicInfos[1] + Build.VERSION.RELEASE;
		datas[2] = basicInfos[2] + (tm.getDeviceId() == null ? "��" : tm.getDeviceId());
		datas[3] = basicInfos[3] + getProvidersName();
		datas[4] = basicInfos[4] + (isRoot() ? "��" : "��");
		return true;
	}

	/**
	 * ���CPU��Ϣ
	 */
	public boolean CPUInfo(final String[] datas) {
		if (datas == null || datas.length < CPUInfos.length) {
			return false;
		}
		datas[0] = CPUInfos[0] + getCpuName();
		datas[1] = CPUInfos[1] + getNumCores();
		datas[2] = CPUInfos[2] + getMaxCpuFreq() + "KHZ";
		datas[3] = CPUInfos[3] + getMinCpuFreq() + "KHZ";
		datas[4] = CPUInfos[4] + getCurCpuFreq() + "KHZ";
		return true;
	}

	/**
	 * ���ֱ�����Ϣ
	 */
	public boolean resolutionInfo(final String[] datas) {
		if (datas == null || datas.length < resolutionInfos.length) {
			return false;
		}
		datas[0] = resolutionInfos[0] + getCameraResolution();
		datas[1] = resolutionInfos[1] + getMaxPhotoSize();
		datas[2] = resolutionInfos[2] + (getFlashMode() == null ? "��" : getFlashMode());
		return true;
	}

	/**
	 * ���������Ϣ
	 */
	public boolean pixelInfo(final String[] datas) {
		if (datas == null || datas.length < pixelInfos.length) {
			return false;
		}
		datas[0] = pixelInfos[0] + getResolution();
		datas[1] = pixelInfos[1] + getPixDensity();
		datas[2] = pixelInfos[2] + (isSupportMultiTouch() ? "֧��" : "��֧��");
		return true;
	}

	/**
	 * ���WIFI��Ϣ
	 */
	public boolean WIFIInfo(final String[] datas) {
		if (datas == null || datas.length < WIFIInfos.length) {
			return false;
		}
		WifiInfo wifiInfo = wm.getConnectionInfo();
		datas[0] = WIFIInfos[0] + (wifiInfo == null ? "δ����" : wifiInfo.getSSID());
		datas[1] = WIFIInfos[1] + (wifiInfo == null ? "��" : wifiInfo.getIpAddress());
		datas[2] = WIFIInfos[2] + (wifiInfo == null ? "0" : wifiInfo.getLinkSpeed());
		datas[3] = WIFIInfos[3] + (wifiInfo == null ? "��" : wifiInfo.getMacAddress());
		datas[4] = WIFIInfos[4] + getBlueToothState();
		return true;
	}

	/**
	 * Role:Telecom service providers��ȡ�ֻ���������Ϣ <BR>
	 * ��Ҫ����Ȩ��<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/> <BR>
	 */
	public String getProvidersName() {
		String ProvidersName = null;
		String IMSI = tm.getSubscriberId();
		if (IMSI == null) {
			return "��";
		}
		// IMSI��ǰ��3λ460�ǹ��ң������ź���2λ00 02���й��ƶ���01���й���ͨ��03���й����š�
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
			ProvidersName = "�й��ƶ�";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "�й���ͨ";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "�й�����";
		} else {
			return "��";
		}
		return ProvidersName;
	}

	/**
	 * �жϵ�ǰ�ֻ��Ƿ���ROOTȨ��
	 * 
	 * @return
	 */
	public boolean isRoot() {
		boolean bool = false;

		try {
			if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
				bool = false;
			} else {
				bool = true;
			}
		} catch (Exception e) {

		}
		return bool;
	}

	// ��ȡCPU���Ƶ�ʣ���λKHZ��
	// "/system/bin/cat" ������
	// "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" �洢���Ƶ�ʵ��ļ���·��
	public String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// ��ȡCPU��СƵ�ʣ���λKHZ��
	public String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// ʵʱ��ȡCPU��ǰƵ�ʣ���λKHZ��
	public String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// ��ȡCPU����
	public static String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			for (int i = 0; i < array.length; i++) {
			}
			return array[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the number of cores available in this device, across all processors.
	 * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
	 * 
	 * @return The number of cores, or 1 if failed to get result
	 */
	private int getNumCores() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			// Default to return 1 core
			return 1;
		}
	}

	/**
	 * ��ȡ�ֻ��ֱ���
	 */
	public String getResolution() {
		String resolution = "";
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		resolution = metrics.widthPixels + "*" + metrics.heightPixels;
		return resolution;
	}

	/**
	 * ��ȡ��Ƭ���ֱ���
	 */
	public String getMaxPhotoSize() {
		String maxSize = "";
		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		List<Size> sizes = parameters.getSupportedPictureSizes();
		Size size = null;
		for (Size s : sizes) {
			if (size == null) {
				size = s;
			} else if (size.height * s.width < s.height * s.width) {
				size = s;
			}
		}
		maxSize = size.width + "*" + size.height;
		camera.release();
		return maxSize;
	}

	/**
	 * ��ȡ������ߴ�
	 */
	public String getCameraResolution() {
		String cameraResolution = "";
		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		List<Size> sizes = parameters.getSupportedPictureSizes();
		Size size = null;
		for (Size s : sizes) {
			if (size == null) {
				size = s;
			} else if (size.height * s.width < s.height * s.width) {
				size = s;
			}
		}
		cameraResolution = (size.width * size.height) / 10000 + "������";
		camera.release();
		return cameraResolution;
	}

	/**
	 * ��ȡ�����״̬
	 */
	public String getFlashMode() {
		String flashMode = "";
		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		flashMode = parameters.getFlashMode();
		camera.release();
		return flashMode;
	}

	/**
	 * ��ȡ�����ܶ�
	 */
	public float getPixDensity() {
		float density = 0;
		density = mContext.getResources().getDisplayMetrics().density;
		return density;
	}

	/**
	 * �ж��豸�Ƿ�֧�ֶ�㴥��
	 */
	public boolean isSupportMultiTouch() {
		PackageManager pm = mContext.getPackageManager();
		boolean isSupportMultiTouch = pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
		return isSupportMultiTouch;
	}

	/**
	 * ��ȡ��������״̬
	 */
	public String getBlueToothState() {
		BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bAdapter == null) {
			return "�豸��֧������";
		}
		int state = bAdapter.getState();
		switch (state) {
		case BluetoothAdapter.STATE_TURNING_OFF:
			return "�����ر���";
		case BluetoothAdapter.STATE_TURNING_ON:
			return "����������";
		case BluetoothAdapter.STATE_OFF:
			return "�����ر�";
		case BluetoothAdapter.STATE_ON:
			return "��������";
		}
		return "δ֪";
	}

	/** �豸Ʒ��(moto?) */
	public static String getPhoneName() {
		return Build.BRAND;
	}

	/** �豸�ͺ�����(xt910) */
	public static String getPhoneModelName() {
		// �������� PRODUCT
		return Build.MODEL + " Android" + getPhoneSystemVersion();
	}

	/** �豸ϵͳ�汾�� (4.1.2?) */
	public static String getPhoneSystemVersion() {
		return Build.VERSION.RELEASE;
	}

}
