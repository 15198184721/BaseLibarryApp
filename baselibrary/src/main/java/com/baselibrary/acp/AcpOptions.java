package com.baselibrary.acp;

/**
 * Created by hupei on 2016/4/26.
 */
public final class AcpOptions {
    private String mRationalMessage;
    private String mDeniedMessage;
    private String mDeniedCloseBtn;
    private String mDeniedSettingBtn;
    private String mRationalBtn;
    private String[] mPermissions;

    private AcpOptions(Builder builder) {
        mRationalMessage = builder.mRationalMessage;
        mDeniedMessage = builder.mDeniedMessage;
        mDeniedCloseBtn = builder.mDeniedCloseBtn;
        mDeniedSettingBtn = builder.mDeniedSettingBtn;
        mRationalBtn = builder.mRationalBtn;
        this.mPermissions = builder.mPermissions;
    }

    public String getRationalMessage() {
        return mRationalMessage;
    }

    public String getDeniedMessage() {
        return mDeniedMessage;
    }

    public String getDeniedCloseBtn() {
        return mDeniedCloseBtn;
    }

    public String getDeniedSettingBtn() {
        return mDeniedSettingBtn;
    }

    public String getRationalBtnText() {
        return mRationalBtn;
    }

    public String[] getPermissions() {
        return mPermissions;
    }

    public static class Builder {
//        private static final String DEF_RATIONAL_MESSAGE = "此功能需要您授权，否则将不能正常使用";
        //在内容为空的情况下第一个提示窗口不显示，(给用户的提示内容)
        private static final String DEF_RATIONAL_MESSAGE = "";
        private static final String DEF_DENIED_MESSAGE = "您拒绝权限申请，此功能将不能正常使用，您可以去设置页面重新授权";
        private static final String DEF_DENIED_CLOSE_BTN_TEXT = "关闭";
        private static final String DEF_DENIED_SETTINGS_BTN_TEXT = "设置权限";
        private static final String DEF_RATIONAL_BTN_TEXT = "我知道了";
        //在内容为空的情况下第一个提示窗口不显示，(给用户的提示内容)
        private String mRationalMessage = DEF_RATIONAL_MESSAGE;
        private String mDeniedMessage = DEF_DENIED_MESSAGE;
        private String mDeniedCloseBtn = DEF_DENIED_CLOSE_BTN_TEXT;
        private String mDeniedSettingBtn = DEF_DENIED_SETTINGS_BTN_TEXT;
        private String mRationalBtn = DEF_RATIONAL_BTN_TEXT;
        private String[] mPermissions;

        /**
         * 如果不设置或者设置为空字符串那么第一个提示框将不在现实<br>
         *   第一个提示窗口是告诉用户需要那些权限等说明信息
         * @param rationalMessage 说明信息内容，不设置或者空串表示不显示
         * @return
         */
        public Builder setRationalMessage(String rationalMessage) {
            mRationalMessage = rationalMessage;
            return this;
        }

        /**
         * 设置显示的文本(如果拒绝了权限提示窗口的内容)
         * @param deniedMessage 拒绝后的提示内容
         * @return
         */
        public Builder setDeniedMessage(String deniedMessage) {
            mDeniedMessage = deniedMessage;
            return this;
        }

        public Builder setDeniedCloseBtn(String deniedCloseBtnText) {
            this.mDeniedCloseBtn = deniedCloseBtnText;
            return this;
        }

        public Builder setDeniedSettingBtn(String deniedSettingText) {
            this.mDeniedSettingBtn = deniedSettingText;
            return this;
        }

        public Builder setRationalBtn(String rationalBtnText) {
            this.mRationalBtn = rationalBtnText;
            return this;
        }

        public Builder setPermissions(String... mPermissions) {
            this.mPermissions = mPermissions;
            return this;
        }

        public AcpOptions build() {
            if (this.mPermissions == null || this.mPermissions.length == 0) {
                throw new IllegalArgumentException("mPermissions no found...");
            }
            return new AcpOptions(this);
        }
    }
}
