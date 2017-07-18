package com.xsm.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Xiasem
 * Date: 17-1-18
 * Email: xiasem@163.com <br/>
 * Github: https://github.com/Xiasm
 *
 * <p>
 *      仿微博实现话题选择输入框
 * </p>
 */
public class TEditText extends android.support.v7.widget.AppCompatEditText{

    private int preTextLength = 0;
    // 默认,话题文本高亮颜色
    private static final int FOREGROUND_COLOR = Color.parseColor("#FF8C00");
    // 默认,话题背景高亮颜色
    private static final int BACKGROUND_COLOR = Color.parseColor("#FFDEAD");

    /**
     * 开发者可设置内容
     */

    // 话题文本高亮颜色
    private int mForegroundColor = FOREGROUND_COLOR;

    // 话题背景高亮颜色
    private int mBackgroundColor = BACKGROUND_COLOR;

    private List<TObject> mTObjectsList = new ArrayList<>();// object集合

    public TEditText(Context context) {
        this(context, null);
        // 初始化设置
        init();
    }

    public TEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public TEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TEditText);
        mBackgroundColor = typedArray.getColor(R.styleable.TEditText_object_background_color, BACKGROUND_COLOR);
        mForegroundColor = typedArray.getColor(R.styleable.TEditText_object_foreground_color, FOREGROUND_COLOR);
        typedArray.recycle();

        // 初始化设置
        init();
    }

    /**
     * 监听光标的位置,若光标处于话题内容中间则移动光标到话题结束位置
     */
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if (mTObjectsList == null || mTObjectsList.size() == 0) {
            return;
        }

        int startPosition = 0;
        int endPosition = 0;
        String objectText = "";

        for (int i = 0; i < mTObjectsList.size(); i++) {

            objectText = mTObjectsList.get(i).getObjectText();
            int length = getText().toString().length();

            while (true) {
                // 获取话题文本开始下标
                startPosition = getText().toString().indexOf(objectText, startPosition);
                endPosition = startPosition + objectText.length();
                if (startPosition == -1) {
                    break;
                }
                // 若光标处于话题内容中间则移动光标到话题结束位置
                if (selStart > startPosition && selStart <= endPosition) {
                    if ((endPosition + 1) > length) {
                        setSelection(endPosition);
                    } else {
                        setSelection(endPosition + 1);
                    }
                    break;
                }
                startPosition = endPosition;
            }


        }

    }

    /**
     * <b>Description:</b>
     * <p>
     *     初始化控件,设置一些监听,如文字变化,文字删除
     * </p>
     * Author: Xiasem, Date: 17-7-17, Email: xiasem@163.com <br/>
     * Version:  <br/>
     */
    private void init() {

//        /*
//         * 输入框内容变化监听<br/>
//         * 1.当文字内容产生变化的时候实时更新UI
//         */
//        this.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // 文字改变刷新UI
//                refreshEditTextUI(s.toString());
//            }
//        });

        /*
         * 监听软键盘删除按钮(本打算使用setOnKeyListener,但发现在华为手机上Del按钮的keyCode与其他手机不一样,故用此)
         * 1.光标在话题后面,将整个话题内容删除
         * 2.光标在普通文字后面,删除一个字符
         */
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editablede) {
                Editable editable = getText();
                int length = editablede.toString().length();

                //删除
                if (length < preTextLength) {
                    int selectionStart = getSelectionStart();
                    int selectionEnd = getSelectionEnd();


                    /*
                     * 如果光标起始和结束不在同一位置,删除文本
                     */
                    if (selectionStart != selectionEnd) {
                        // 查询文本是否属于话题对象,若是移除列表数据
                        String tagetText = getText().toString().substring(selectionStart, selectionEnd);
                        for (int i = 0; i < mTObjectsList.size(); i++) {
                            TObject object = mTObjectsList.get(i);
                            if (tagetText.equals(object.getObjectText())) {
                                mTObjectsList.remove(object);
                            }
                        }
                        return;
                    }

                    int lastPos = 0;
                    if (mTObjectsList != null && mTObjectsList.size() > 0) {
                        // 遍历判断光标的位置
                        for (int i = 0; i < mTObjectsList.size(); i++) {

                            String objectText = mTObjectsList.get(i).getObjectText();

                            lastPos = getText().toString().indexOf(objectText, lastPos);
                            if (lastPos != -1) {
                                if (selectionStart != 0 && selectionStart >= lastPos && selectionStart <= (lastPos + objectText.length())) {
                                    // 选中话题
                                    setSelection(lastPos, lastPos + objectText.length());
                                    // 设置背景色
                                    editable.setSpan(new BackgroundColorSpan(
                                                    mBackgroundColor), lastPos, lastPos
                                                    + objectText.length(),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    return;
                                }
                                lastPos += objectText.length();
                            }
                        }
                    }

                }
                preTextLength = length;
                refreshEditTextUI(editable.toString());
            }
        });

    }

    /**
     * <b>Description:</b>
     * <p>
     *     TEditText内容修改之后刷新UI
     * </p>
     * Author: Xiasem, Date: 17-7-17, Email: xiasem@163.com <br/>
     * Version: 1.0 <br/>
     * @param content 输入框内容
     */
    private void refreshEditTextUI(String content) {

        /*
         * 内容变化时操作:
         * 1.查找匹配所有话题内容
         * 2.设置话题内容特殊颜色
         */
        if (mTObjectsList.size() == 0)
            return;

        if (TextUtils.isEmpty(content)) {
            mTObjectsList.clear();
            return;
        }

        /*
         * 重新设置span
         */
        Editable editable = getText();
        int textLength = editable.length();

        int findPosition = 0;
        for (int i = 0; i < mTObjectsList.size(); i++) {
            final TObject object = mTObjectsList.get(i);
            // 文本
            String objectText = object.getObjectText();
            while (findPosition <= length()) {
                // 获取文本开始下标
                findPosition = content.indexOf(objectText, findPosition);
                if (findPosition != -1) {
                    // 设置话题内容前景色高亮
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(mForegroundColor);
                    editable.setSpan(colorSpan, findPosition, findPosition + objectText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    findPosition += objectText.length();
                } else {
                    break;
                }
            }

        }

    }

    /**
     * 插入/设置话题
     *
     * @param object 话题对象
     */
    public void setObject(TObject object) {

        if (object == null) {
            return;
        }

        String objectRule = object.getObjectRule();
        String objectText = object.getObjectText();
        if (TextUtils.isEmpty(objectText) || TextUtils.isEmpty(objectRule)) {
            return;
        }

        // 拼接字符# %s #,并保存
        objectText = objectRule + objectText + objectRule;
        object.setObjectText(objectText);

        /*
         * 1.添加话题内容到数据集合
         */
        mTObjectsList.add(object);

        /*
         * 2.将话题内容添加到EditText中展示
         */
        // 光标位置
        int selectionStart = getSelectionStart();
        // 原先内容
        Editable editable = getText();

        if (selectionStart >= 0) {
            // 在光标位置插入内容  话题后面插入空格,至关重要
            editable.insert(selectionStart, objectText + " ");
//            editable.insert(getSelectionStart(), " ");

            setSelection(getSelectionStart());// 移动光标到添加的内容后面
        }

    }

    /**
     * 获取object列表数据
     */
    public List<TObject> getObjects() {
        List<TObject> objectsList = new ArrayList<TObject>();
        // 由于保存时候文本内容添加了匹配字符#,此处去除,还原数据
        if (mTObjectsList != null && mTObjectsList.size() > 0) {
            for (int i = 0; i < mTObjectsList.size(); i++) {
                TObject object = mTObjectsList.get(i);
                String objectText = object.getObjectText();
                String objectRule = object.getObjectRule();
                object.setObjectText(objectText.replace(objectRule, ""));// 将匹配规则字符替换
                objectsList.add(object);
            }
        }
        return objectsList;
    }

}
