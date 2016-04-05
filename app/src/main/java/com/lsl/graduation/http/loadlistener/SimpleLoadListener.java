package com.lsl.graduation.http.loadlistener;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.http.bean.BaseBean;
import com.lsl.graduation.http.context.LoadContext;
import com.lsl.graduation.utils.MLog;
import com.lsl.graduation.utils.UIHelper;


/**
 * 简单实现的LoadListener
 * @author jialg
 *
 * @param <Result>
 */
public class SimpleLoadListener<Result> implements LoadListener<Result> {

	@Override
	public void preExecut(LoadContext<Result> context) {
		
	}

	@Override
	public void postExecut(final LoadContext<Result> context) {
		if(context.getResult() instanceof BaseBean){
			final BaseBean bean = (BaseBean) context.getResult();
			if(!bean.isSuccess()&&bean.getRetcode() == 100){
				bean.setMessage(null);
			}
		}
	}

	@Override
	public void loadComplete(final LoadContext<Result> context) {
		if(context.getResult() instanceof BaseBean){
			final BaseBean bean = (BaseBean) context.getResult();
			if(!bean.isSuccess()&&bean.getRetcode() == 100){
//				AppContext.getInstance().Logout();
//				IndexFragment.dataChanged = true;
				final Dialog dialog = context.getDialog();
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
				if(context.getCtx() != null&&context.getCtx() instanceof Activity&&!((Activity) context.getCtx()).isFinishing())
//					AppUtil.loginDialog(context.getCtx());
				return;
			}else{
				final Dialog dialog = context.getDialog();
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
			}
//			if(context.getCtx() != null&&!TextUtils.isEmpty(bean.getMessage())){
//				UIHelper.showMsg(context.getCtx(), bean.getMessage());
//			}
//			if(context.getResult() instanceof BankTypeBean){
//				BankTypeBean bankTypeBean = (BankTypeBean) context.getResult();
//				if(bankTypeBean.getData()!=null){
//					Class<? extends BaseActivity> clazz = Next_Step.getClassWithCode(bankTypeBean.getData().getNextStep());
//					if (clazz != null&&context.getCtx()!=null) {
//						Intent intent = new Intent(context.getCtx(), clazz);
//						intent.putExtra("payChannel", bankTypeBean.getData().getPayChannel());
//						intent.putExtra("dataMap", bankTypeBean.getData().getDataMap());
//						context.getCtx().startActivity(intent);
//						if(context.getCtx() instanceof Activity){
//							((Activity) context.getCtx()).overridePendingTransition(R.anim.go_in_y, R.anim.go_out_alpha);
//						}
//					}
//				}
//			}
		}
	}

	@Override
	public void loadFail(LoadContext<Result> context) {
		Exception e = context.getException();
		final Dialog dialog = context.getDialog();
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		if(e != null&&(e instanceof SocketTimeoutException || e instanceof SocketException || !AppContext.getInstance().isNetworkConnected())){
			UIHelper.showMsg(AppContext.getInstance(), "网络无法连接，请检查网络设置");
		}else{
			UIHelper.showMsg(AppContext.getInstance(), "数据加载失败");
			MLog.e(e.getMessage());
		}
		if(context.getResult() instanceof BaseBean){
			final BaseBean bean = (BaseBean) context.getResult();
			if(context.getCtx() != null&&!TextUtils.isEmpty(bean.getMessage())){
				UIHelper.showMsg(context.getCtx(), bean.getMessage());
			}
			if(!bean.isSuccess()&&bean.getRetcode() == 100){
//				AppContext.getInstance().Logout();
//				IndexFragment.dataChanged = true;
//				if(context.getCtx() != null&&context.getCtx() instanceof Activity&&!((Activity) context.getCtx()).isFinishing())
//					AppUtil.loginDialog(context.getCtx());
				return;
			}
		}
	}

	
}
