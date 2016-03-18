package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.BaseDatas;
import com.boding.microscreen.model.MessageData;
import com.boding.microscreen.model.MessageInfo;
import com.boding.microscreen.model.MessageItem;
import com.boding.microscreen.net.Request4Message;
import com.boding.microscreen.net.Request4MessageInfo;
import com.boding.microscreen.service.AudioPlayService;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.JLog;
import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.util.Player;
import com.boding.microscreen.util.ShowToast;
import com.boding.microscreen.util.Util;
import com.boding.microscreen.widget.CustomDialog;
import com.boding.microscreen.widget.refreshview.DividerItemDecoration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 留言墙
 *
 * @author Administrator
 */
public class MessageListFragment extends BaseFragment {

    private static final String TAG = "MessageListFragment";

    private static final String PARAMS_KEY = "baseDatas";

    private HomeActivity mActivity;
    private ImageView mIVLogo;
    private ImageView mIVQrcode;
    private ImageButton mIbtnArrowUp, mIbtnArrowDown;
    private TextView mTvSponName, mTvTheme;
    private RecyclerView mRecyclerView;
    private MessageInfoAdapter mMsgAdapter;

    // 是否允许留言墙信息滚动
    private boolean isScrooll = false;
    // 是否开启留言墙信息审核
    private boolean isCheck = false;
    // 新发的留言列表数据
    private boolean isAddData = false;

    public MessageListFragment() {

    }

    /**
     * 实例化一个留言墙Fragment的对象
     *
     * @param baseDatas 基础数据
     * @return 实例对象
     */
    public static MessageListFragment newInstance(BaseDatas baseDatas) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_KEY, baseDatas);
        MessageListFragment fragment = new MessageListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 向下的箭头获取焦点
     * @return
     */
    public int requestFocuseView(){
        // 把焦点给向下的箭头
        return mIbtnArrowDown.getId();
    }

    /**
     * 指定的消息列表View的item获取焦点
     * @return true 拦截按键
     */
    private boolean requestFocuseMsgListView() {
        if (null != mHandler && mMsgAdapter.getItemCount() != 0) {
            mHandler.removeCallbacks(mRunnable);
            mMsgAdapter.requestFocusItem();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof HomeActivity) {
            mActivity = (HomeActivity) activity;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayout llMsg = (LinearLayout) view.findViewById(R.id.id_ll_msg);
        // 主办方logo
        mIVLogo = (ImageView) view.findViewById(R.id.id_iv_logo);
        // 主办方二维码
        mIVQrcode = (ImageView) view.findViewById(R.id.id_iv_qrcode);
        // 主办方名称
        mTvSponName = (TextView) view.findViewById(R.id.id_tv_spon_name);
        // 主办方活动主题
        mTvTheme = (TextView) view.findViewById(R.id.id_tv_theme);
        // 上下翻页滚动箭头
        mIbtnArrowUp = (ImageButton) view.findViewById(R.id.ibtn_arrow_up);
        mIbtnArrowDown = (ImageButton) view.findViewById(R.id.ibtn_arrow_down);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_rv_msg);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        String messageUrl = String.format(AppConstants.MESSAGE_URL, mActivity.validateCode);
        loadMessageData(messageUrl);
        llMsg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                llMsg.getViewTreeObserver().removeOnPreDrawListener(this);
                mMsgAdapter = new MessageInfoAdapter(null);
                mRecyclerView.setAdapter(mMsgAdapter);
                mMsgAdapter.mTotalHeight = llMsg.getMeasuredHeight();
                // 定时每隔3秒请求一次留言墙数据
                mRecycleTimer.schedule(mRecycleTimerTask, 0, DELAY_TIME);
                return true;
            }
        });

        initEvent();
    }


    /**
     * 初始化事件
     */
    private void initEvent() {

        // 点击上箭头
        mIbtnArrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 向下滚动, 显示上面的内容
                scrollMsgList(true);
            }
        });

        // 点击下箭头
        mIbtnArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 向上滚动，显示下面的内容
                scrollMsgList(false);
            }
        });

        // 上箭头遥控器方向键事件
        mIbtnArrowUp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_UP:
                        break;
                    case KeyEvent.ACTION_DOWN:
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            requestFocuseMsgListView();
                        }
                        if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                            // 拦截遥控器Right键
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        // 下箭头遥控器方向键事件
        mIbtnArrowDown.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_UP:
                        break;
                    case KeyEvent.ACTION_DOWN:
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            requestFocuseMsgListView();
                        }
                        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                            // 拦截遥控器Right键
                            return true;
                        }
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                            // 拦截遥控器down键
                            return false;
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 手动滚动列表
     *
     * @param isScrollDown 是否向下滚动
     */
    private void scrollMsgList(boolean isScrollDown) {
        int itemCount = mMsgAdapter.getItemCount();
        LinearLayoutManager llManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (itemCount > mScrollNum) { // 消息列表条数大于一次滚动的条数，才能滚动
            if (isScrollDown) {
                int nextPosition = llManager.findLastVisibleItemPosition() - mScrollNum;
                if (nextPosition >= 0) {
                    mRecyclerView.smoothScrollToPosition(nextPosition);
                } else {
                    mRecyclerView.scrollToPosition(0);
                }
            } else {
                int nextPosition = llManager.findLastVisibleItemPosition() + mScrollNum;
                if (nextPosition <= itemCount - 1) {
                    mRecyclerView.smoothScrollToPosition(nextPosition);
                } else {
                    if (nextPosition < (itemCount - 1) + mScrollNum) {
                        mRecyclerView.smoothScrollToPosition(nextPosition - mScrollNum + 1);
                    } else {
                        mRecyclerView.scrollToPosition(0);
                    }
                }
            }
        }
    }


    // 定时请求获取留言列表信息
    private Timer mRecycleTimer = new Timer();
    private RecycleTimerTask mRecycleTimerTask = new RecycleTimerTask(this);

    private static class RecycleTimerTask extends TimerTask {
        private WeakReference<MessageListFragment> outClass = null;
        private boolean isOnce = true;

        public RecycleTimerTask(MessageListFragment fragment) {
            outClass = new WeakReference<>(fragment);
        }

        @Override
        public void run() {
            MessageListFragment fragment = outClass.get();
            if (null != fragment) {
                fragment.recycleRequest(isOnce);
                isOnce = false;
            }
        }
    }

    private void recycleRequest(boolean isOnce) {
        String url;
        if (isOnce) {
            url = String.format(AppConstants.MESSAGE_ITEM_URL, mActivity.validateCode, "");
        } else {
            if (null == mMsgAdapter.getMsgInfos()) {
                return;
            }
            if (isCheck) {
                // 发送根据审核时间排序后的消息的最新审核时间, 来请求数据
                if (0 == mMsgAdapter.getMsgInfos().size()) {
                    url = String.format(AppConstants.MESSAGE_ITEM_URL, mActivity.validateCode,
                            Util.timeStamp2Date(System.currentTimeMillis(), null));
                } else {
                    url = String.format(AppConstants.MESSAGE_ITEM_URL, mActivity.validateCode,
                            mMsgAdapter.getTimeSortMsgInfos().get(0).getOnwalltime());
                }
            } else {
                // 发送根据发送时间排序后的消息的最新发送时间, 来请求数据
                if (0 == mMsgAdapter.getMsgInfos().size()) {
                    url = String.format(AppConstants.MESSAGE_ITEM_URL, mActivity.validateCode,
                            Util.timeStamp2Date(System.currentTimeMillis(), null));
                } else {
                    url = String.format(AppConstants.MESSAGE_ITEM_URL, mActivity.validateCode,
                            mMsgAdapter.getTimeSortMsgInfos().get(0).getTime());
                }
            }
        }
        if (JLog.isAllowDebug) {
            JLog.e("ck:" + isCheck + ",url:" + url);
        }
        mMsgAdapter.loadData(isOnce, url);
    }

    /**
     * 加载留言墙界面信息
     *
     * @param url 请求地址
     */
    private void loadMessageData(String url) {
        executeRequest(new Request4Message(url, new Response.Listener<MessageData>() {
            @Override
            public void onResponse(MessageData response) {
                MyLog.e(TAG, response.toString());
                setMessageData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e(TAG, error.getMessage());
            }
        }));
    }

    /**
     * 设置留言墙界面信息
     *
     * @param messageData 留言墙界面信息
     */
    private void setMessageData(MessageData messageData) {
        if (JLog.isAllowDebug) {
            JLog.e("isScroll: " + messageData.getIsScroll());
        }
        isScrooll = messageData.getIsScroll() == 1;
        isCheck = messageData.getIsCheck() == 1;
        // 主办方icon
        Glide.with(MessageListFragment.this)
                .load(messageData.getSponIcon())
                .error(R.drawable.logo_right_top)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIVLogo);
        // 主办方二维码
        Glide.with(MessageListFragment.this)
                .load(messageData.getQrCode())
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIVQrcode);
        // 主办方名称
        mTvSponName.setText(messageData.getSponName());
        // 主办方活动主题
        mTvTheme.setText(messageData.getTheme());
    }

    private class MessageInfoAdapter extends RecyclerView.Adapter<MsgViewHolder> {

        private ArrayList<MessageItem> mTimeSortMsgInfos;

        public ArrayList<MessageItem> getTimeSortMsgInfos() {
            return mTimeSortMsgInfos;
        }

        public ArrayList<MessageItem> getMsgInfos() {
            return mMsgInfos;
        }

        private ArrayList<MessageItem> mMsgInfos;
        private int mTotalHeight;

        private int mItemHeight;
        private MsgViewHolder mViewHolder;

        public MessageInfoAdapter(ArrayList<MessageItem> msgInfos) {
            this.mMsgInfos = (msgInfos == null) ? new ArrayList<MessageItem>() :
                    new ArrayList<>(msgInfos);
            this.mTimeSortMsgInfos = new ArrayList<>();
        }

        @Override
        public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_fragment_message_list_item, parent, false);
            mViewHolder = new MsgViewHolder(view);
            return mViewHolder;
        }

        private void requestFocusItem() {
            mViewHolder.mLLItem.requestFocus();
        }

        @Override
        public void onBindViewHolder(final MsgViewHolder holder, final int position) {
            final MessageItem messageInfo = mMsgInfos.get(position);
            // 设置item视图最小的高度
            holder.mLLItem.setMinimumHeight(mItemHeight);
            // 消息列表获取焦点的时候停止滚动
            holder.mLLItem.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            break;
                        case KeyEvent.ACTION_DOWN:
                            //当按下的按钮是方向下（并且是列表的最后一个item）、方向左、方向右时让列表滚动
                            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                if (mMsgInfos.size() - 1 == position) {
                                    mHandler.post(mRunnable);
                                    mActivity.requestFocuseToTab(1);
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                                    keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                mHandler.post(mRunnable);
                                //把焦点换给当前选择的activity里的RadioGroup的radiobuton
                                mActivity.requestFocuseToTab(1);
                            }
                            break;
                    }
                    return false;
                }
            });
//			MyLog.e(TAG, "mItemHeight: " + mItemHeight);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 点击留言墙item项
                    // 文字信息
                    if ("txt".equals(messageInfo.getContentType())) {
                        showImageDialog(messageInfo, false);
                    }
                    // 图片消息
                    if ("img".equals(messageInfo.getContentType())) {
                        showImageDialog(messageInfo, true);
                    }
                    // 音频消息
                    if ("audio".equals(messageInfo.getContentType())) {
                        final Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                        intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PAUSE);
                        mActivity.sendBroadcast(intent);
                        Player player = new Player();
                        player.playUrl(messageInfo.getContent());
                        player.setPlayerListener(new Player.PlayerListener() {
                            @Override
                            public void onPrepared(Player player, MediaPlayer mp) {
                                mp.start();
                            }

                            @Override
                            public void onCompletion(Player player, MediaPlayer mp) {
                                player.stop();
                                intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
                                mActivity.sendBroadcast(intent);
                            }

                            @Override
                            public void onBufferingUpdate(Player player, MediaPlayer mp, int percent) {

                            }
                        });
                        MyLog.e(TAG, "audioUrl=" + messageInfo.getContent());
                    }
                }
            });

            // 头像
            Glide.with(MessageListFragment.this)
                    .load(messageInfo.getIconUrl())
                    .error(R.drawable.ic_msg_list_item_header)
                    .override(3 * mItemHeight / 5, 3 * mItemHeight / 5)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.mSDVIcon);

            // 昵称
            holder.mName.setText(messageInfo.getName());
            holder.mName.setTextSize((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    20 * 3 / mScrollNum, getResources().getDisplayMetrics()));
            // 日期时间
            holder.mTime.setText(messageInfo.getTime());
            holder.mTime.setTextSize((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    25 * 3 / mScrollNum, getResources().getDisplayMetrics()));
            // 文字消息
            if ("txt".equals(messageInfo.getContentType())) {
                holder.mContentImage.setVisibility(View.GONE);
                holder.mContentAudio.setVisibility(View.GONE);
                holder.mContentText.setVisibility(View.VISIBLE);
                holder.mContentText.setText(messageInfo.getContent());
                holder.mContentText.setTextSize((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        35 * 3 / mScrollNum, getResources().getDisplayMetrics()));

                int tempMaxLines = 2;
                if (3 >= mScrollNum) {
                    tempMaxLines = 2;
                }
                if (4 <= mScrollNum) {
                    tempMaxLines = 1;
                }
                holder.mContentText.setMaxLines(tempMaxLines);
            }
            // 图片消息
            if ("img".equals(messageInfo.getContentType())) {
                holder.mContentText.setVisibility(View.GONE);
                holder.mContentAudio.setVisibility(View.GONE);
                holder.mContentImage.setVisibility(View.VISIBLE);
//				(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mItemHeight, getResources().getDisplayMetrics()),
//						(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mItemHeight, getResources().getDisplayMetrics())
                // 使用Glide框架加载图片
                Glide.with(MessageListFragment.this)
                        .load(messageInfo.getContent())
                        .override(150 * 3 / mScrollNum, 110 * 3 / mScrollNum)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.mContentImage);

            }
            // 音频消息
            if ("audio".equals(messageInfo.getContentType())) {
                holder.mContentText.setVisibility(View.GONE);
                holder.mContentImage.setVisibility(View.GONE);
                holder.mContentAudio.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mMsgInfos.size();
        }

        /**
         * 设置显示大图对话框
         */
        private void showImageDialog(MessageItem messageInfo, boolean isImage) {
            final CustomDialog dialog = new CustomDialog(mActivity, false);
            dialog.showDialog(R.layout.layout_dialog_qrcode, R.style.DialogWindowAnim);
            TextView tvName = (TextView) dialog.findViewById(R.id.id_tv_name);
            TextView tvContent = (TextView) dialog.findViewById(R.id.id_tv_msg_content);
            ImageView bigImage = (ImageView) dialog.findViewById(R.id.id_iv_qrcode);
            if (isImage) {
                Glide.with(MessageListFragment.this)
                        .load(messageInfo.getContent())
                        .error(R.drawable.qrcode)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(bigImage);
            } else {
                bigImage.setVisibility(View.GONE);
                // 昵称
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(messageInfo.getName().trim() + "  说：");
                // 文字内容
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(messageInfo.getContent().trim());
            }

            Button button = (Button) dialog.findViewById(R.id.id_btn_close);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        /**
         * 加载留言墙数据
         *
         * @param isOnce 是否第一次加载
         * @param url    请求地址
         */
        private void loadData(final boolean isOnce, String url) {
            executeRequest(new Request4MessageInfo(url, new Response.Listener<MessageInfo>() {
                @Override
                public void onResponse(MessageInfo response) {
                    ArrayList<MessageItem> messageItems = response.getMessageList();
                    if (JLog.isAllowDebug) {
                        for (MessageItem mi : messageItems) {
                            JLog.e("留言信息：" + mi.toString());
                        }
                    }
                    // 判断是否有新发的留言列表数据
                    isAddData = 0 != mMsgInfos.size() && !messageItems.isEmpty();
                    int tempNum = response.getMessageNums();
                    mScrollNum = tempNum;
                    mItemHeight = mTotalHeight / tempNum;
                    boolean isCheckTemp = response.getIsCheck() == 1;
                    isCheck = isCheckTemp;
                    if (isCheckTemp) {
                        // 消息审核开关开启，更加审核时间排序消息，但是屏幕上显示还是按照发送时间排序
                        if (0 == mMsgInfos.size()) {
                            // 按发送时间降序排序显示在屏幕上
                            sortDataByTime(messageItems, false);
                            mMsgInfos.addAll(messageItems);
                            sortDataByTime(mMsgInfos, false);
                            // 按审核时间降序排序用来请求新消息
                            sortDataByTime(messageItems, true);
                            mTimeSortMsgInfos.addAll(messageItems);
                        } else {
                            // 按发送时间降序排序显示在屏幕上
                            sortDataByTime(messageItems, false);
                            sortDataByTime(mMsgInfos, false);
                            mMsgInfos.addAll(0, messageItems);
                            // 按审核时间降序排序用来请求新消息
                            sortDataByTime(messageItems, true);
                            sortDataByTime(mTimeSortMsgInfos, true);
                            mTimeSortMsgInfos.addAll(0, messageItems);
                        }
                    } else {
                        // 消息审核开关关闭，根据发送时间排序消息，但是屏幕上显示还是按照发送时间排序
                        if (0 == mMsgInfos.size()) {
                            // 按发送时间降序排序显示在屏幕上
                            sortDataByTime(messageItems, false);
                            mMsgInfos.addAll(messageItems);
                            // 按发送时间降序排序用来请求新消息
                            sortDataByTime(mTimeSortMsgInfos, false);
                            mTimeSortMsgInfos.addAll(messageItems);
                        } else {
                            // 按发送时间降序排序显示在屏幕上
                            sortDataByTime(messageItems, false);
                            sortDataByTime(mMsgInfos, false);
                            mMsgInfos.addAll(0, messageItems);
                            // 按发送时间降序排序用来请求新消息
                            sortDataByTime(mTimeSortMsgInfos, false);
                            mTimeSortMsgInfos.addAll(0, messageItems);

                        }
                    }
                    if (!messageItems.isEmpty()) { // 消息列表不为空的时候才刷新列表
                        notifyDataSetChanged();
                    }
                    if (isOnce) {
                        mHandler.postDelayed(mRunnable, DELAY_TIME);
                    }
                    if (isAddData) {
//						nextPosition = 0;
                        mRecyclerView.scrollToPosition(0);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ShowToast.Short(error.getMessage());
                }
            }));
        }

        /**
         * 根据时间排序
         *
         * @param elements      需要排序的数据
         * @param isByAuditTime 是否通过审核时间来排序
         * @return 排序后的数据
         */
        private ArrayList<MessageItem> sortDataByTime(ArrayList<MessageItem> elements,
                                                      final boolean isByAuditTime) {
            if (null != elements && !elements.isEmpty()) {
                Collections.sort(elements, new Comparator<MessageItem>() {
                    @Override
                    public int compare(MessageItem lhs, MessageItem rhs) {
                        String time1, time2;
                        if (isByAuditTime) {
                            time1 = lhs.getOnwalltime();
                            time2 = rhs.getOnwalltime();
                        } else {
                            time1 = lhs.getTime();
                            time2 = rhs.getTime();
                        }
                        // 时间降序排列
                        return time2.compareTo(time1);
                    }
                });
            }
            return elements;
        }
    }

    private static class MsgViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLLItem;
        // 头像
        private CircleImageView mSDVIcon;
        // 昵称
        private TextView mName;
        // 日期时间
        private TextView mTime;
        // 文字消息
        private TextView mContentText;
        // 图片消息
        private ImageView mContentImage;
        // 音频消息
        private ImageView mContentAudio;

        public MsgViewHolder(View itemView) {
            super(itemView);
            mLLItem = (LinearLayout) itemView.findViewById(R.id.id_ll_msg_item);
            mSDVIcon = (CircleImageView) itemView.findViewById(R.id.id_sdv_icon);
            mName = (TextView) itemView.findViewById(R.id.id_tv_name);
            mTime = (TextView) itemView.findViewById(R.id.id_tv_time);
            mContentText = (TextView) itemView.findViewById(R.id.id_tv_contentText);
            mContentImage = (ImageView) itemView.findViewById(R.id.id_iv_image);
            mContentAudio = (ImageView) itemView.findViewById(R.id.id_iv_contentAudio);
        }
    }

    // 滚动的留言条数
    private int mScrollNum = 3;
    private static final int DELAY_TIME = 6000;
    private Handler mHandler = new Handler();
    private MyRunnable mRunnable = new MyRunnable(this);

    private static class MyRunnable implements Runnable {
        WeakReference<MessageListFragment> weakFragment;

        public MyRunnable(MessageListFragment fragment) {
            weakFragment = new WeakReference<>(fragment);
        }

        @Override
        public void run() {
            MessageListFragment outClass = weakFragment.get();
            if (outClass != null) {
                outClass.recycleListItem();
            }
        }
    }

    private void recycleListItem() {
        if (isScrooll) {
            LinearLayoutManager llManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int itemCount = mMsgAdapter.getItemCount();
            int nextPosition = llManager.findLastVisibleItemPosition() + mScrollNum;
            if (nextPosition <= itemCount - 1) {
                mRecyclerView.smoothScrollToPosition(nextPosition);
            } else {
                if (nextPosition < (itemCount - 1) + mScrollNum) {
                    mRecyclerView.smoothScrollToPosition(nextPosition - mScrollNum + 1);
                } else {
                    mRecyclerView.scrollToPosition(0);
                }
            }
        }
        mHandler.postDelayed(mRunnable, DELAY_TIME);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 低内存的时候清理Glide图片内存缓存:bitmapPool、memoryCache
        Glide.get(mActivity).clearMemory();
    }

    @Override
    public void onDestroyView() {
        MyLog.d(TAG, "onDestroyView...");
        if (mActivity != null) {
            mActivity = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
            mRunnable = null;
        }
        if (mMsgAdapter != null) {
            mMsgAdapter.onDetachedFromRecyclerView(mRecyclerView);
            mMsgAdapter = null;
        }
        if (mRecyclerView != null) {
            mRecyclerView.removeAllViews();
            mRecyclerView = null;
        }
        if (mRecycleTimer != null) {
            mRecycleTimer.cancel();
            mRecycleTimer = null;
        }
        if (mRecycleTimerTask != null) {
            mRecycleTimerTask = null;
        }
        Glide.clear(mIVLogo);
        Glide.clear(mIVQrcode);
        Glide.get(mActivity).clearMemory();
        System.gc();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销EventBus事件总线
//        EventBus.getDefault().unregister(this);
    }
}