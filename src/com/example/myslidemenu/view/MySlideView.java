package com.example.myslidemenu.view;

import java.io.Serializable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class MySlideView extends FrameLayout{
	private View menuView;// 菜单view
	private View mainView;// mainView
	private int menuViewHeight;// 菜单的高
	private int menuViewWidth;// 菜单的宽
	private int mainViewWidth;// mainView的宽

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
	 * 1.在onfinishinflate 中拿到 子view　引用 2.在onsizechange中拿到子view 宽高
	 * 3.在onlayout中重新摆放子view 的位置----不需要重写onmeasure 以及ondraw，让本view 去帮我们测量 以及画图
	 * --------因为 如果复写了onMeasure，需要将本view 的每一个子view 都measure 一遍。。 4
	 * 利用view中的方法，scrollto scrollby 来移动。----也可以用layout 和 padding
	 * 完成移动还有一个offsetTopAndBottom 5.在初始化时new scroller
	 * 对象，让该对象帮我们完成缓慢移动动画。-----该动画要用到compute... 两处调用invalidate 完成递归 形成 缓慢移动
	 * 6.定义open . close 方法，封装动画，供用户调用。 ----
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

	// 控制2个子view的位置
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

			int scrollX = getScrollX();// 获得x方向偏移的坐标
			System.out.println("scrollX===" + scrollX);
			newScrollX = (int) (scrollX - deltaX);
			System.out.println("newScrollX===" + newScrollX);
			// 添加限制条件，
			if (newScrollX < -menuViewWidth) {// menu 已经完全展示出来，还要往右移动
				newScrollX = -menuViewWidth;
			}
			if (newScrollX > 0) {// main已经完全展示出来，还要往左移动
				newScrollX = 0;
			}

			// 开始左右移动
			this.scrollTo(newScrollX, 0);

			// scrollTo(moveX,0);//要保证每次移动的x值都是相对 量-- 手指按下到移动的距离的相对量，而不是movex--
			// 手指按下的坐标。
			// 要保证手指不管 按下在哪里，x的值 都是屏幕的偏移量。因此这个x 的值 只能随着deltaX
			// 而变化。---要保证deltax变化每次都是
			// 微小的量，必须要更新坐标，不更新坐标，deltax必然是一个随着move不断增加的量。
			/**
			 * 因此凡是在move事件中更新坐标的
			 * 必然是要同时实时获得控件当前的坐标值，然后再当前坐标值上进行累加---这样能保证每次触摸时，控件位置不改变 e.g.
			 * //3.计算偏移量 int dX = newX-startX; int dY = newY-startY;
			 * //4.控件移动相应的偏移量，重新分配位置 //获取控件移动前的距离左边和顶部距离 int l =
			 * ll_dragview_address.getLeft();//获取控件距离左边的距离----------- int t =
			 * ll_dragview_address.getTop();//获取控件距离顶部的距离-----------实时获得当前位置
			 * l+=dX; t+=dY; startX = newX; startY = newY; e.g. 本例 int scrollX =
			 * getScrollX();//获得x方向偏移的坐标--------------实时获得当前位置。 downX = moveX;
			 * 
			 * 如果不更新坐标的，必然会随着手指随意位置按下，控件位置瞬间改变。
			 * 这种情况的移动不是累加式的，而是直接将moveX放入layout中的偏移值中。
			 * 
			 * 
			 * 
			 */

			// 更新坐标
			downX = moveX;

			break;
		case MotionEvent.ACTION_UP:
			// 抬起的时候判断偏移量 ，播放响应的动画
			// 用getScrollX??????
			if (getScrollX() > -menuViewWidth / 2) {
				// main的范围多一些，动画缓缓全部展示main
				// int currX = scroller.getCurrX();
				// 从当前偏移量移动到 全部展示。
				/*
				 * scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 200);
				 * invalidate();
				 */
				close();

			} else {
				// menu的范围多一些，动画缓缓全部展示menu
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

	// 动画触发重绘，需要在compute方法中形成递归
	@Override
	public void computeScroll() {
		// If it returns true, the animation is not yet finished
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
			invalidate();
		}
	}

	/**
	 * 打开菜单
	 */
	public void open() {
		scroller.startScroll(getScrollX(), 0, -menuViewWidth - getScrollX(), 0,
				350);// 相当于startAnimation
		invalidate();// 目的是为了调用computeScroll
	}

	/**
	 * 关闭菜单
	 */
	public void close() {
		scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 350);// 相当于startAnimation
		invalidate();// 目的是为了调用computeScroll
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:
			//	移动的时候进行判断，如果是左右移动  就return true  由本view 执行
			// 上下 移动 就放行 让listview 执行
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}
}
