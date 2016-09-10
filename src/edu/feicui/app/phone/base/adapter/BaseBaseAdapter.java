package edu.feicui.app.phone.base.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * �Զ������������BaseBaseAdapter
 * 
 * @author yuanc
 * 
 * @param <DataType>
 */
public abstract class BaseBaseAdapter<DataType> extends BaseAdapter {
	protected Context context;
	protected LayoutInflater layoutInflater;
	/** ��ǰ���������������� */
	private List<DataType> dataList = new ArrayList<DataType>();

	public BaseBaseAdapter(Context context) {
		this.context = context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/** ��ȡ�����������������б�(List����) */
	public List<DataType> getDataList() {
		return dataList;
	}

	/** �������������������б�(List����),���һ������ */
	public void addDataToAdapter(DataType data) {
		dataList.add(data);
	}

	public void addDataToAdapterHead(List<DataType> datas) {
		dataList.addAll(0, datas);
	}
	
	public void addDataToAdapterEnd(List<DataType> datas) {
		dataList.addAll(dataList.size(), datas);
	}

	/** ���������������������б�(List����)������,Ϊָ������(��������ԭ����) */
	public void setDataToAdapter(DataType data) {
		dataList.clear();
		dataList.add(data);
	}

	/** ���������������������б�(List����)������,Ϊָ������(��������ԭ����) */
	public void setDataToAdapter(List<DataType> datas) {
		dataList.clear();
		dataList.addAll(datas);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public DataType getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getItemView(position, convertView, parent);
	}

	/**
	 * ���ص�ǰ�б�����ͼЧ��(��ҪĿ�꣺����ǰ�����������ݶ�Ӧ�����䡱�����ָ��ؼ���)
	 * 
	 * @see �ڲ�ʹ�ù۲���ģʽʵ�ֵ�callBack����
	 * @see ���б����һ����ʾ���������б����������Ļʱ�����б������ݱ仯������notifyDataSetChanged()����ʱ ������������
	 * @param position
	 *            ��ǰ�б������б��е�λ��
	 */
	public abstract View getItemView(int position, View convertView, ViewGroup parent);
}
