package edu.feicui.app.phone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.base.util.FileTypeUtil;
import edu.feicui.app.phone.biz.FileManager;

public class FilemgrActivity extends BaseActivity {
	private Thread thread;// ������ȡ�ļ����߳�
	private FileManager fileManager;// �ļ�������(�߼�)

	private TextView tv_textsize; // �ļ��ܴ�С

	private View view_all; // ȫ����item(�����ؽ����󣬽��е����¼�)
	private View view_txt; // �ĵ���item(ͬ��)
	private View view_video; // ��Ƶ��item(ͬ��)
	private View view_audio; // ���ֵ�item(ͬ��)
	private View view_image; // ͼ���item(ͬ��)
	private View view_apk; // apk��item(ͬ��)
	private View view_zip; // zip��item(ͬ��)
	private TextView tv_all_size; // ȫ��item�Ĵ�С(�����ع����У�ʵʱ���´˷�����ļ���С)
	private TextView tv_txt_size;// �ĵ�item�Ĵ�С(ͬ��)
	private TextView tv_video_size;// ��Ƶitem�Ĵ�С(ͬ��)
	private TextView tv_audio_size;// ����item�Ĵ�С(ͬ��)
	private TextView tv_image_size;// ͼ��item�Ĵ�С(ͬ��)
	private TextView tv_apk_size;// apk item�Ĵ�С(ͬ��)
	private TextView tv_zip_size;// zip item�Ĵ�С(ͬ��)
	private ProgressBar pb_all_loading;// ȫ��item�ļ�����ͼ��(�����ع����л���ʾ,�����������)
	private ProgressBar pb_txt_loading;// �ĵ�item�ļ�����ͼ��(ͬ��)
	private ProgressBar pb_video_loading;// ��Ƶitem�ļ�����ͼ��(ͬ��)
	private ProgressBar pb_audio_loading;// ����item�ļ�����ͼ��(ͬ��)
	private ProgressBar pb_image_loading;// ͼ��item�ļ�����ͼ��(ͬ��)
	private ProgressBar pb_apk_loading;// apk item�ļ�����ͼ��(ͬ��)
	private ProgressBar pb_zip_loading;// zip item�ļ�����ͼ��(ͬ��)
	private ImageView iv_all_righticon; // ȫ��item���Ҳ�ͼ��(�����ؽ����󣬽���ʾ����(��������ʾ����loading))
	private ImageView iv_txt_righticon; // �ĵ�item���Ҳ�ͼ��(ͬ��)
	private ImageView iv_video_righticon; // ��Ƶitem���Ҳ�ͼ��(ͬ��)
	private ImageView iv_audio_righticon; // ����item���Ҳ�ͼ��(ͬ��)
	private ImageView iv_image_righticon; // ͼ��item���Ҳ�ͼ��(ͬ��)
	private ImageView iv_apk_righticon; // apk item���Ҳ�ͼ��(ͬ��)
	private ImageView iv_zip_righticon; // zip item���Ҳ�ͼ��(ͬ��)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemgr);
		// ��ʼ��ActionBar @see super class ActionBarActivity
		String title = getResources().getString(R.string.filemgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		initMainUI();
		asynLoadData();
	}

	private void initMainUI() {
		//
		tv_textsize = (TextView) findViewById(R.id.tv_filesize);
		//
		view_all = findViewById(R.id.file_classlist_all);
		view_txt = findViewById(R.id.file_classlist_txt);
		view_video = findViewById(R.id.file_classlist_video);
		view_audio = findViewById(R.id.file_classlist_audio);
		view_image = findViewById(R.id.file_classlist_image);
		view_apk = findViewById(R.id.file_classlist_apk);
		view_zip = findViewById(R.id.file_classlist_zip);
		//textView
		tv_all_size = (TextView) findViewById(R.id.file_all_size);
		tv_txt_size = (TextView) findViewById(R.id.file_txt_size);
		tv_video_size = (TextView) findViewById(R.id.file_video_size);
		tv_audio_size = (TextView) findViewById(R.id.file_audio_size);
		tv_image_size = (TextView) findViewById(R.id.file_image_size);
		tv_apk_size = (TextView) findViewById(R.id.file_apk_size);
		tv_zip_size = (TextView) findViewById(R.id.file_zip_size);
		//ͼƬ
		iv_all_righticon = (ImageView) findViewById(R.id.file_all_righticon);
		iv_txt_righticon = (ImageView) findViewById(R.id.file_txt_righticon);
		iv_video_righticon = (ImageView) findViewById(R.id.file_video_righticon);
		iv_audio_righticon = (ImageView) findViewById(R.id.file_audio_righticon);
		iv_image_righticon = (ImageView) findViewById(R.id.file_image_righticon);
		iv_apk_righticon = (ImageView) findViewById(R.id.file_apk_righticon);
		iv_zip_righticon = (ImageView) findViewById(R.id.file_zip_righticon);
		//������
		pb_all_loading = (ProgressBar) findViewById(R.id.file_all_loading);
		pb_txt_loading = (ProgressBar) findViewById(R.id.file_txt_loading);
		pb_video_loading = (ProgressBar) findViewById(R.id.file_video_loading);
		pb_audio_loading = (ProgressBar) findViewById(R.id.file_audio_loading);
		pb_image_loading = (ProgressBar) findViewById(R.id.file_image_loading);
		pb_apk_loading = (ProgressBar) findViewById(R.id.file_apk_loading);
		pb_zip_loading = (ProgressBar) findViewById(R.id.file_zip_loading);
	}
	
	/**�첽��������*/
	private void asynLoadData() {
		fileManager = FileManager.getFileManager();
		fileManager.setSearchFileListener(searchFileListener);
		//�����߳̽����ļ�����
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				fileManager.searchSDCardFile();
			}
		});
		thread.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		fileManager.setStopSearch(true);
		//�ն��߳��ͷ���Դ
		thread.interrupt();
		thread = null;
	}
	//ʵʱ���¸����ļ���С
	@Override
	protected void myHandleMessage(Message msg) {
		// TODO Auto-generated method stub
		//�ļ�ʵʱ���²���
		if (msg.what == 1) {
			tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			String typeName = (String) msg.obj;
			if (typeName.equals(FileTypeUtil.TYPE_APK)) {
				tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
			} else if (typeName.equals(FileTypeUtil.TYPE_AUDIO)) {
				tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
			} else if (typeName.equals(FileTypeUtil.TYPE_IMAGE)) {
				tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
			} else if (typeName.equals(FileTypeUtil.TYPE_TXT)) {
				tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
			} else if (typeName.equals(FileTypeUtil.TYPE_VIDEO)) {
				tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
			} else if (typeName.equals(FileTypeUtil.TYPE_ZIP)) {
				tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
			}
		}
		//�ļ��������²���
		if (msg.what == 2) {
			tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
			tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
			tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
			tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
			tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
			tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
			pb_all_loading.setVisibility(View.GONE);
			pb_txt_loading.setVisibility(View.GONE);
			pb_video_loading.setVisibility(View.GONE);
			pb_audio_loading.setVisibility(View.GONE);
			pb_image_loading.setVisibility(View.GONE);
			pb_apk_loading.setVisibility(View.GONE);
			pb_zip_loading.setVisibility(View.GONE);
			iv_all_righticon.setVisibility(View.VISIBLE);
			iv_txt_righticon.setVisibility(View.VISIBLE);
			iv_video_righticon.setVisibility(View.VISIBLE);
			iv_audio_righticon.setVisibility(View.VISIBLE);
			iv_image_righticon.setVisibility(View.VISIBLE);
			iv_apk_righticon.setVisibility(View.VISIBLE);
			iv_zip_righticon.setVisibility(View.VISIBLE);
			view_all.setOnClickListener(clickListener);
			view_txt.setOnClickListener(clickListener);
			view_video.setOnClickListener(clickListener);
			view_audio.setOnClickListener(clickListener);
			view_image.setOnClickListener(clickListener);
			view_apk.setOnClickListener(clickListener);
			view_zip.setOnClickListener(clickListener);
		}
	};
	//�ص��ӿڵĳ�ʼ��
	private FileManager.SearchFileListener searchFileListener = new FileManager.SearchFileListener() {
		@Override
		public void searching(String typeName) {
			Message msg = mainHandler.obtainMessage();
			msg.what = 1;
			msg.obj = typeName;
			mainHandler.sendMessage(msg);
		}

		@Override
		public void end(boolean isExceptionEnd) {
			// TODO Auto-generated method stub
			mainHandler.sendEmptyMessage(2);
		}
	};
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.file_classlist_all:
			case R.id.file_classlist_txt:
			case R.id.file_classlist_video:
			case R.id.file_classlist_audio:
			case R.id.file_classlist_image:
			case R.id.file_classlist_apk:
			case R.id.file_classlist_zip:
				Intent intent = new Intent(FilemgrActivity.this, FilemgrShowActivity.class);
				intent.putExtra("id", viewID);
				startActivityForResult(intent, 1);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
		if (requestCode == 1) {
			tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
			tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
			tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
			tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
			tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
			tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
		}
	}
}
