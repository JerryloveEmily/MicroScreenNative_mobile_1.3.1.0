package com.boding.microscreen.app;

import com.boding.microscreen.model.ModelInfo;

import java.util.ArrayList;

public class AppConstants {
	
//	public static final String BASEURL = "http://jsj.fanzhi.com/index.php?r=";// 金山角
//	public static final String BASEURL = "http://i.fanzhi.com/index.php?r=";
//	public static final String BASEURL = "http://i.wichao.com/index.php?r=";
    // 正式服务器地址(包括金山角)
	public static final String BASEURL = "http://ys.fanzhi.com/index.php?r=";

	// 测试部门的测试服务器地址
//	public static final String BASEURL = "http://test.fanzhi.com/index.php?r=";

    // http://ys.fanzhi.com/index.php?r=app/update&version=1.0.3;
    public static final String UPDATE_APP_URL = BASEURL + "app/update&version=%s";
    // 视频地址
    public static final String VIDEO_URL = BASEURL + "screentv/interfaceSet/videoModules&code=%s";
    // http://i.wichao.com/index.php?r=screentv/interfaceSet/videoModules&code=15501865
    // 留言墙信息
    public static final String MESSAGE_URL = BASEURL + "screentv/interfaceSet/messageModules&code=%s";
    // http://i.wichao.com/index.php?r=screentv/interfaceSet/messageModules&code=15501865
    // 基础数据
    public static final String BASE_DATA_URL = BASEURL + "screentv/interfaceSet/baseData&code=%s";
    // http://i.wichao.com/index.php?r=screentv/interfaceSet/baseData&code=15501865
    // 留言列表
    public static final String MESSAGE_ITEM_URL = BASEURL + "screentv/interfaceSet/messageList&code=%s&time=%s";
    // http://i.wichao.com/index.php?r=screentv/interfaceSet/messageList&code=15501865
    // 活动列表
    public static final String ACTIVITIES_URL = BASEURL + "screentv/interfaceSet/activityList&code=%s&sort=%s";
    // http://i.wichao.com/index.php?r=screentv/interfaceSet/activityList&code=15501865
    // 图片列表
    public static final String PICTURE_URL = BASEURL + "screentv/interfaceSet/photoList&code=%s";
    //商品分类列表
    public static final String GOODS_TYPE_URL= BASEURL + "screentv/interfaceSet/goodsCategory&code=%s";

    // 商圈列表
    public static final String DISTRICT_URL = BASEURL + "screentv/interfaceSet/myDistrictList&code=%s";

    //商品列表
    public static final String GOODS_DATA_URL= BASEURL + "screentv/interfaceSet/goodsList&code=%s&id=%s";
    // 商圈数据列表
    public static final String BUSINESS_DATA = BASEURL + "screentv/interfaceSet/districtList&code=%s&id=%s";
    //商圈分类栏目
    public static final String BUSINESS_CATEORY = BASEURL + "screentv/interfaceSet/districtCategory&code=%s";

	/**
	 * 请求方式
	 */
	public static final int GET = 10000;
	public static final int POST = 10001;

    /**
     * 滚动时间1秒
     */
    public static final int SCROLL_DURATION = 1000;
	
	public static final String VALIDATE_CODE = " validateCode";
	public static final String MICRO_SCREEN_URL = " microScreenUrl";
	public static final String FIRST_SHOW_PARAM = " firstShowParam";
	
	/** 存放apk安装包的文件夹 */
	public static final String APK_NAME = "MicroScreen.apk";
	
	//****************** 应用信息提示 ********************************* /
	/** 应用更新检查出错提示信息 **/
	public static final String APP_CHECK_UPDATE_ERROR_INFO = "应用更新检查出错！";
	/** 获取首页显示参数出错提示信息*/
	public static final String GET_FRIST_SHOW_PARAM_ERROR_INFO = "获取首页显示参数出错！";
	/** 识别码不存在出错提示信息*/
	public static final String VALIDATE_CODE_ERROR_INFO = "识别码不存在，请检查！";
    /** 微屏幕活动结束*/
	public static final String VALIDATE_CODE_OVER_INFO = "该识别码活动已结束，请检查！";

    public static final String[] COLORS = {
        "#3AC88C","#69D5F2","#41BEC4","#F85E6A",
        "#C4E688","#FA9F72","#C76DC5","#9DC05A",
        "#C566C2","#8ECC0F","#4D50C5","#FA9F70"
    };
    // 更多模块列表
    public static ArrayList<ModelInfo> mMoreModels;
    // 促销应用模块列表
    public static ArrayList<ModelInfo> mPromotionModels;

    // 背景音乐被手动开启或暂停
    public static boolean isHandOpen = false;
    public static boolean isHandClose = false;

    /**
     * 点击的是哪个模块
     */
    public static String modelTag = "";

    public static String[] testImg = {
            "http://img2.imgtn.bdimg.com/it/u=1901465252,2329264458&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2725012004,2632149361&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2383617292,1835645853&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1506260297,392156409&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=4158735287,2144111564&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1877465525,2990703038&fm=21&gp=0.jpg"
    };

    public static String[] testImg2 = {
            "http://img3.imgtn.bdimg.com/it/u=3716985675,1104816796&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=30147007,1336990568&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2191634573,3706913716&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3753444090,285767058&fm=21&gp=0.jpg"
    };
}
