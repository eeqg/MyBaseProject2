package com.example.resource.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.example.resource.base.BaseApp;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchyInflater;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class PictureView extends SimpleDraweeView {
	private float placeholderAspectRatio;
	private float actualAspectRatio = 0;
	
	public PictureView(Context context, GenericDraweeHierarchy hierarchy) {
		super(context, hierarchy);
	}
	
	public PictureView(Context context) {
		super(context);
	}
	
	public PictureView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PictureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public PictureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
	
	@Override
	protected void inflateHierarchy(Context context, AttributeSet attrs) {
		GenericDraweeHierarchyBuilder builder =
				GenericDraweeHierarchyInflater.inflateBuilder(context, attrs);
		if (builder.getPlaceholderImage() == null) {
			// builder.setPlaceholderImage(R.drawable.img_holder_square);
			new ColorDrawable(0x808080);
		}
		builder.setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY);
		//builder.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
		setAspectRatio(builder.getDesiredAspectRatio());
		
		if (builder.getPlaceholderImage() != null) {
			int intrinsicWidth = builder.getPlaceholderImage().getIntrinsicWidth();
			int intrinsicHeight = builder.getPlaceholderImage().getIntrinsicHeight();
			this.placeholderAspectRatio = (float) intrinsicWidth / intrinsicHeight;
		}
		
		setHierarchy(builder.build());
	}
	
	@BindingAdapter({"pictureUrl"})
	public static void loadPicture(PictureView pictureView, String pictureUrl) {
		loadPicture(pictureView,
				pictureUrl == null ? null : Uri.parse(pictureUrl),
				0, 0);
	}
	
	@BindingAdapter({"pictureUrl"})
	public static void loadPicture(PictureView pictureView, Uri pictureUri) {
		loadPicture(pictureView, pictureUri, 0, 0);
	}
	
	@BindingAdapter({"pictureUrl", "pictureWidth", "pictureHeight"})
	public static void loadPicture(PictureView pictureView, String pictureUrl, int pictureWidth, int pictureHeight) {
		loadPicture(pictureView,
				pictureUrl == null ? null : Uri.parse(pictureUrl),
				pictureWidth, pictureHeight);
	}
	
	@BindingAdapter({"pictureUrl", "wrapHeight"})
	public static void loadPicture(PictureView pictureView, String pictureUrl, boolean wrapHeight) {
		loadPicture(pictureView,
				pictureUrl == null ? null : Uri.parse(pictureUrl)
				, 0, 0,
				wrapHeight);
	}
	
	@BindingAdapter({"pictureUrl", "pictureWidth", "pictureHeight"})
	public static void loadPicture(PictureView pictureView,
	                               Uri pictureUri,
	                               int pictureWidth,
	                               int pictureHeight) {
		loadPicture(pictureView, pictureUri, pictureWidth, pictureHeight, false);
	}
	
	@BindingAdapter({"pictureUrl", "pictureWidth", "pictureHeight", "wrapHeight"})
	public static void loadPicture(PictureView pictureView,
	                               Uri pictureUri,
	                               int pictureWidth,
	                               int pictureHeight,
	                               boolean wrapHeight) {
		if (pictureUri == null) {
			DraweeController controller = Fresco.newDraweeControllerBuilder()
					.setOldController(pictureView.getController())
					.build();
			pictureView.setController(controller);
			return;
		}
		
		int viewWidth = 0;
		int viewHeight = 0;
		ViewGroup.LayoutParams layoutParams = pictureView.getLayoutParams();
		
		if (pictureWidth > 0) {
			viewWidth = pictureWidth;
		} else if (pictureView.getMeasuredWidth() > 0) {
			viewWidth = pictureView.getMeasuredWidth();
		} else if (layoutParams.width > 0) {
			viewWidth = layoutParams.width;
		}
		if (pictureHeight > 0) {
			viewHeight = pictureHeight;
		} else if (pictureView.getMeasuredHeight() > 0) {
			viewHeight = pictureView.getMeasuredHeight();
		} else if (layoutParams.height > 0) {
			viewHeight = layoutParams.height;
		}
		
		if (viewWidth <= 0) {
			viewWidth = BaseApp.SCREEN_WIDTH / 2;
		}
		if (viewHeight <= 0) {
			if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
				viewHeight = BaseApp.SCREEN_HEIGHT / 2;
			} else if (pictureView.getAspectRatio() > 0) {
				viewHeight = (int) (viewWidth * pictureView.getAspectRatio());
			}
		}
		
		// pictureUri = Uri.parse(encodePictureUrl(pictureUri.toString(), viewWidth, viewHeight));
		
		if (wrapHeight) {
			if (pictureView.getAspectRatio() != pictureView.actualAspectRatio) {
				pictureView.setAspectRatio(pictureView.placeholderAspectRatio);
			}
		}
		
		ResizeOptions resizeOptions = new ResizeOptions(
				viewWidth <= 0 ? BaseApp.SCREEN_WIDTH / 2 : viewWidth,
				viewHeight <= 0 ? BaseApp.SCREEN_HEIGHT / 2 : viewHeight
		);
		
		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setControllerListener(wrapHeight ? pictureView.mControllerListener : null)
				.setOldController(pictureView.getController())
				.setImageRequest(
						ImageRequestBuilder.newBuilderWithSource(pictureUri)
								.setResizeOptions(resizeOptions)
								.build()
				)
				.build();
		pictureView.setController(controller);
	}
	
	/**
	 * 格式化图片地址
	 *
	 * @param pictureUrl    图片地址
	 * @param pictureWidth  图片宽度
	 * @param pictureHeight 图片高度
	 * @return 图片地址
	 */
	public static String encodePictureUrl(String pictureUrl, int pictureWidth, int pictureHeight) {
		if (pictureUrl.startsWith("http") && (pictureWidth > 0 || pictureHeight > 0)) {
			StringBuilder builder = new StringBuilder(pictureUrl);
			builder.append("?x-oss-process=image/resize,m_mfit");
			if (pictureWidth > 0) {
				builder.append(",w_").append(pictureWidth);
			}
			if (pictureHeight > 0) {
				builder.append(",h_").append(pictureHeight);
			}
			return builder.toString();
		}
		return pictureUrl;
	}
	
	private BaseControllerListener<ImageInfo> mControllerListener = new BaseControllerListener<ImageInfo>() {
		
		@Override
		public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
			updateViewSize(imageInfo);
		}
		
		@Override
		public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
			updateViewSize(imageInfo);
		}
	};
	
	void updateViewSize(ImageInfo imageInfo) {
		if (imageInfo != null) {
			this.actualAspectRatio = (float) imageInfo.getWidth() / imageInfo.getHeight();
			setAspectRatio(this.actualAspectRatio);
		}
	}
}
