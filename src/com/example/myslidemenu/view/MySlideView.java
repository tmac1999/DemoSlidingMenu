package com.example.myslidemenu.view;

import java.io.Serializable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class MySlideView extends FrameLayout{
	private View menuView;// �˵�view
	private View mainView;// mainView
	private int menuViewHeight;// �˵��ĸ�
	private int menuViewWidth;// �˵��Ŀ�
	private int mainViewWidth;// mainView�Ŀ�

	private int downX;
	private int newScrollX;
	private Scroller scroller;

	public MySlideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MySlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MySlideView(Context context) {
		super(context);
		init();
	}

	private void init() {
		scroller = new Scroller(getContext());
	}

	/**
	 * 1.��onfinishinflate ���õ� ��view������ 2.��onsizechange���õ���view ���
	 * 3.��onlayout�����°ڷ���view ��λ��----����Ҫ��дonmeasure �Լ�ondraw���ñ�view ȥ�����ǲ��� �Լ���ͼ
	 * --------��Ϊ �����д��onMeasure����Ҫ����view ��ÿһ����view ��measure һ�顣�� 4
	 * ����view�еķ�����scrollto scrollby ���ƶ���----Ҳ������layout �� padding
	 * ����ƶ�����һ��offsetTopAndBottom 5.�ڳ�ʼ��ʱnew scroller
	 * �����øö����������ɻ����ƶ�������-----�ö���Ҫ�õ�compute... ��������invalidate ��ɵݹ� �γ� �����ƶ�
	 * 6.����open . close ��������װ���������û����á� ----
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = this.getChildAt(0);
		mainView = this.getChildAt(1);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		menuViewWidth = menuView.getMeasuredWidth();
		menuViewHeight = menuView.getMeasuredHeight();

		mainViewWidth = mainView.getMeasuredWidth();
	}

	// ����2����view��λ��
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mainView.layout(0, 0, right, bottom);
		menuView.layout(-menuViewWidth, top, 0, bottom);

		// menuView.layout(-menuViewWidth, 0, 0, menuViewHeight);
		// mainView.layout(0, 0, mainViewWidth, menuViewHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int deltaX = moveX - downX;

			int scrollX = getScrollX();// ���x����ƫ�Ƶ�����
			System.out.println("scrollX===" + scrollX);
			newScrollX = (int) (scrollX - deltaX);
			System.out.println("newScrollX===" + newScrollX);
			// �������������
			if (newScrollX < -menuViewWidth) {// menu �Ѿ���ȫչʾ��������Ҫ�����ƶ�
				newScrollX = -menuViewWidth;
			}
			if (newScrollX > 0) {// main�Ѿ���ȫչʾ��������Ҫ�����ƶ�
				newScrollX = 0;
			}

			// ��ʼ�����ƶ�
			this.scrollTo(newScrollX, 0);

			// scrollTo(moveX,0);//Ҫ��֤ÿ���ƶ���xֵ������� ��-- ��ָ���µ��ƶ��ľ�����������������movex--
			// ��ָ���µ����ꡣ
			// Ҫ��֤��ָ���� ���������x��ֵ ������Ļ��ƫ������������x ��ֵ ֻ������deltaX
			// ���仯��---Ҫ��֤deltax�仯ÿ�ζ���
			// ΢С����������Ҫ�������꣬���������꣬deltax��Ȼ��һ������move�������ӵ�����
			/**
			 * ��˷�����move�¼��и��������
			 * ��Ȼ��Ҫͬʱʵʱ��ÿؼ���ǰ������ֵ��Ȼ���ٵ�ǰ����ֵ�Ͻ����ۼ�---�����ܱ�֤ÿ�δ���ʱ���ؼ�λ�ò��ı� e.g.
			 * //3.����ƫ���� int dX = newX-startX; int dY = newY-startY;
			 * //4.�ؼ��ƶ���Ӧ��ƫ���������·���λ�� //��ȡ�ؼ��ƶ�ǰ�ľ�����ߺͶ������� int l =
			 * ll_dragview_address.getLeft();//��ȡ�ؼ�������ߵľ���----------- int t =
			 * ll_dragview_address.getTop();//��ȡ�ؼ����붥���ľ���-----------ʵʱ��õ�ǰλ��
			 * l+=dX; t+=dY; startX = newX; startY = newY; e.g. ���� int scrollX =
			 * getScrollX();//���x����ƫ�Ƶ�����--------------ʵʱ��õ�ǰλ�á� downX = moveX;
			 * 
			 * �������������ģ���Ȼ��������ָ����λ�ð��£��ؼ�λ��˲��ı䡣
			 * ����������ƶ������ۼ�ʽ�ģ�����ֱ�ӽ�moveX����layout�е�ƫ��ֵ�С�
			 * 
			 * 
			 * 
			 */

			// ��������
			downX = moveX;

			break;
		case MotionEvent.ACTION_UP:
			// ̧���ʱ���ж�ƫ���� ��������Ӧ�Ķ���
			// ��getScrollX??????
			if (getScrollX() > -menuViewWidth / 2) {
				// main�ķ�Χ��һЩ����������ȫ��չʾmain
				// int currX = scroller.getCurrX();
				// �ӵ�ǰƫ�����ƶ��� ȫ��չʾ��
				/*
				 * scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 200);
				 * invalidate();
				 */
				close();

			} else {
				// menu�ķ�Χ��һЩ����������ȫ��չʾmenu
				// int currX = scroller.getCurrX();
				/*
				 * scroller.startScroll(getScrollX(), 0,
				 * getScrollX()-menuViewWidth, 0, 200); invalidate();
				 */
				open();
			}

			break;
		}

		return true;
	}

	// ���������ػ棬��Ҫ��compute�������γɵݹ�
	@Override
	public void computeScroll() {
		// If it returns true, the animation is not yet finished
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
			invalidate();
		}
	}

	/**
	 * �򿪲˵�
	 */
	public void open() {
		scroller.startScroll(getScrollX(), 0, -menuViewWidth - getScrollX(), 0,
				350);// �൱��startAnimation
		invalidate();// Ŀ����Ϊ�˵���computeScroll
	}

	/**
	 * �رղ˵�
	 */
	public void close() {
		scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 350);// �൱��startAnimation
		invalidate();// Ŀ����Ϊ�˵���computeScroll
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:
			//	�ƶ���ʱ������жϣ�����������ƶ�  ��return true  �ɱ�view ִ��
			// ���� �ƶ� �ͷ��� ��listview ִ��
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}
}
