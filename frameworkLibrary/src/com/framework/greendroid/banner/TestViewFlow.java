package com.framework.greendroid.banner;


/**
 * banner的用法
 * @author shaxiaoning
 *
 */
public class TestViewFlow {

	public TestViewFlow() {
	
	}

	private void test() {

//		mViewFlow = (ViewFlow) this.findViewById(R.id.banner_main_view_flow);// 获得viewFlow对象
//		mIndic = (CircleFlowIndicator) this
//				.findViewById(R.id.banner_main_circleflow_indicator);// viewFlow下的indic
//		mAdapter = new ViewFlowAdapter(this, tmpList);
//		mViewFlow.setFlowIndicator(mIndic);
//		mViewFlow.setCanHandTouch(true);
//		mViewFlow.setTimeSpan(4500);
//		mViewFlow.setSelection(3 * 1000); // 设置初始位置
//		mViewFlow.startAutoFlowTimer(); // 启动自动播放
//		int[] dip = ApplicationInfo.getDisplayMetrics(this);
//		int width = dip[0];
//		LayoutParams lp = mViewFlow.getLayoutParams();
//		lp.height = width / 2;
//		mViewFlow.setLayoutParams(lp);

	}
	
//	private void doHomeBannerRequest() {
//		HttpTask task = new HttpTask(new HttpHandlerListener() {
//
//			@Override
//			public void onSuccess(String responseText) {
//				try {
//					HomeBanners banners = new Parse<HomeBanners>(
//							HomeBanners.class).onParse(responseText);
//					if (banners != null && banners.getBanners() != null) {
//						tmpList.addAll(banners.getBanners());
//						mViewFlow.setAdapter(mAdapter);
//						mViewFlow.setmSideBuffer(tmpList.size());
//						mViewFlow.setFlowIndicator(mIndic);
//						mAdapter.notifyDataSetChanged();
//					}
//				} catch (Exception e) {
//					ExceptionHandle.selectErrorDialog(MainActivity.this, e);
//				}
//			}
//
//			@Override
//			public void onFailure(String errorText) {
//
//				try {
//					throw new ServerFailedException(
//							ActionFilterFlag.FILTER_FLAG_MESSAGE_FAILURE + "",
//							errorText);
//				} catch (Exception e) {
//					ExceptionHandle.selectErrorDialog(MainActivity.this, e);
//				}
//
//			}
//		});
//		task.setRequest(this, NetConstants.HTTP_TASK_URL,
//				NetConstants.ACTION_HOME_BANNER, TaskType.GET, null);
//
//	}

}
