package edu.feicui.app.phone.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.feicui.app.phone.adapter.FileAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.base.util.FileTypeUtil;
import edu.feicui.app.phone.biz.FileManager;
import edu.feicui.app.phone.entity.FileInfo;

/**
 * �ļ��б���ʾ����(��FilemgrActivity����, �������У���Ƶ�ļ���) --------------
 */
public class FilemgrShowActivity extends BaseActivity {
	private int id;//ȷ�ϵ�ǰҳ������
	private TextView textViewSize; // �ļ���С
	private TextView textViewNumber;// �ļ�����
	private Button btn_delall; // ɾ����ѡ�ļ�

	private ListView fileListView; // ���ļ��б�
	private FileAdapter fileAdapter; // �ļ��б�����

	private ArrayList<FileInfo> fileInfos;
	private long fileSize = 0;
	private long fileNumber = 0;
	private String title = "ȫ���ļ�";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemgrshow);
		// #ȡ��ID���� -- ���Ľ���ĵ�ǰ�ļ�������棨ȫ����ͼ�����֣���Ƶ?��
		id = getIntent().getIntExtra("id", -1);
		// #��ʼ��������������
		initMainData(id);
		// #��ʼ��ActionBar @see super class ActionBarActivity
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// #��ʼ����ǰҳ����Ҫ�ؼ� (�ļ��ܴ�Сtextview���ļ�����textview���ļ��б�listview��)
		initMainUI();
		// #��ʼ����ǰҳ����Ҫ�ؼ��ϵ�����
		initMainUIData();
	}

	private void initMainUIData() {
		textViewSize.setText(CommonUtil.getFileSize(fileSize));
		textViewNumber.setText(fileNumber + "��");
		fileAdapter = new FileAdapter(this);
		fileListView.setAdapter(fileAdapter);
		fileListView.setOnItemClickListener(itemClickListener);
		// ��ʼ��������
		fileAdapter.setDataToAdapter(fileInfos);
		fileAdapter.notifyDataSetChanged();
	}

	private void initMainUI() {
		textViewSize = (TextView) findViewById(R.id.tv_file_size);
		textViewNumber = (TextView) findViewById(R.id.tv_file_number);
		fileListView = (ListView) findViewById(R.id.filelistview);
		btn_delall = (Button) findViewById(R.id.btn_delall);
		btn_delall.setOnClickListener(clickListener);
	}

	private void initMainData(int viewID) {
		fileInfos = new ArrayList<FileInfo>();
		switch (viewID) {
		case R.id.file_classlist_all:
			//��ȡ�ļ���С
			fileSize = FileManager.getFileManager().getAnyFileSize();
			//��ȡ�ļ��б�
			fileInfos = FileManager.getFileManager().getAnyFileList();
			//��ȡ��������
			title = getResources().getString(R.string.filetype_all);
			break;
		case R.id.file_classlist_txt:
			fileSize = FileManager.getFileManager().getTxtFileSize();
			fileInfos = FileManager.getFileManager().getTxtFileList();
			title = getResources().getString(R.string.filetype_txt);
			break;
		case R.id.file_classlist_video:
			fileSize = FileManager.getFileManager().getVideoFileSize();
			fileInfos = FileManager.getFileManager().getVideoFileList();
			title = getResources().getString(R.string.filetype_video);
			break;
		case R.id.file_classlist_audio:
			fileSize = FileManager.getFileManager().getAudioFileSize();
			fileInfos = FileManager.getFileManager().getAudioFileList();
			title = getResources().getString(R.string.filetype_audio);
			break;
		case R.id.file_classlist_image:
			fileSize = FileManager.getFileManager().getImageFileSize();
			fileInfos = FileManager.getFileManager().getImageFileList();
			title = getResources().getString(R.string.filetype_image);
			break;
		case R.id.file_classlist_apk:
			fileSize = FileManager.getFileManager().getApkFileSize();
			fileInfos = FileManager.getFileManager().getApkFileList();
			title = getResources().getString(R.string.filetype_apk);
			break;
		case R.id.file_classlist_zip:
			fileSize = FileManager.getFileManager().getZipFileSize();
			fileInfos = FileManager.getFileManager().getZipFileList();
			title = getResources().getString(R.string.filetype_zip);
			break;
		}
		fileNumber = fileInfos.size();
	}

	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FileInfo fileInfo = fileAdapter.getItem(position);
			File file = fileInfo.getFile();
			// ȡ�����ļ��ĺ�׺������> MIMEType
			String type = FileTypeUtil.getMIMEType(file);
			// ������ļ�
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), type);
			startActivity(intent);
		}
	};

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			//���ؼ�
			case R.id.iv_left:
				finish();
				break;
			//ɾ����
			case R.id.btn_delall:
				delFile();
				break;
			default:
				break;
			}
		}
	};

	public void delFile() {
		// ������������ѡ�е�ɾ���ļ�
		List<FileInfo> delFileInfos = new ArrayList<FileInfo>();
		for (int i = 0; i < fileAdapter.getDataList().size(); i++) {
			FileInfo fileInfo = fileInfos.get(i);
			// ��ѡ�е�
			if (fileInfo.isSelect()) {
				delFileInfos.add(fileInfo);
			}
		}
		// ɾ����ѡ�е��ļ�
		for (int i = 0; i < delFileInfos.size(); i++) {
			FileInfo fileInfo = delFileInfos.get(i);
			File file = fileInfo.getFile();
			long size = file.length();
			if (file.delete()) {
				fileAdapter.getDataList().remove(fileInfo);
				FileManager.getFileManager().getAnyFileList().remove(fileInfo);
				FileManager.getFileManager().setAnyFileSize(FileManager.getFileManager().getAnyFileSize() - size);
				switch (id) {
				case R.id.file_classlist_txt:
					FileManager.getFileManager().getTxtFileList().remove(fileInfo);
					FileManager.getFileManager().setTxtFileSize(FileManager.getFileManager().getTxtFileSize() - size);
					break;
				case R.id.file_classlist_video:
					FileManager.getFileManager().getVideoFileList().remove(fileInfo);
					FileManager.getFileManager().setVideoFileSize(FileManager.getFileManager().getVideoFileSize() - size);
					break;
				case R.id.file_classlist_audio:
					FileManager.getFileManager().getAudioFileList().remove(fileInfo);
					FileManager.getFileManager().setAudioFileSize(FileManager.getFileManager().getAudioFileSize() - size);
					break;
				case R.id.file_classlist_image:
					FileManager.getFileManager().getImageFileList().remove(fileInfo);
					FileManager.getFileManager().setImageFileSize(FileManager.getFileManager().getImageFileSize() - size);
					break;
				case R.id.file_classlist_apk:
					FileManager.getFileManager().getApkFileList().remove(fileInfo);
					FileManager.getFileManager().setApkFileSize(FileManager.getFileManager().getApkFileSize() - size);
					break;
				case R.id.file_classlist_zip:
					FileManager.getFileManager().getZipFileList().remove(fileInfo);
					FileManager.getFileManager().setZipFileSize(FileManager.getFileManager().getZipFileSize() - size);
					break;
				}
			}
		}
		//�����б�
		fileAdapter.notifyDataSetChanged();
		//��ȡ�ļ�����
		fileNumber = fileAdapter.getDataList().size();
		//��ʾ
		textViewNumber.setText(fileNumber + "��");

		System.gc();
		//�����̵߳�ǰִ��Ȩ
		Thread.yield();
	}
}
