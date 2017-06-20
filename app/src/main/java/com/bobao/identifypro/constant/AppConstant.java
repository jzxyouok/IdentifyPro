package com.bobao.identifypro.constant;


import com.bobao.identifypro.R;

/**
 * Created by star on 15/6/1.
 */
public class AppConstant {
    // 获取短信通知
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public static final String SP_KEY_PHONE_CHECKED = "phone_number_checked";

    public static final int DefaultTakePictureNum = 4;
    public static final int MaxTakePictureNum = 8;
    public static final String DefaultPageItemNum = "10";
    public static final String HAS_NOCHARGE_IDENTIFY = "has_no_charge_idntify";
    // 鉴定类别
    public static final int IdentifyTypeChina = 1;
    public static final int IdentifyTypeJade = 2;
    public static final int IdentifyTypePainting = 3;
    public static final int IdentifyTypeBronze = 4;
    public static final int IdentifyTypeMoney = 5;
    public static final int IdentifyTypeWooden = 6;
    public static final int IdentifyTypeSundry = 7;
    public static final int IdentifyTypeMoneyPaper = 51;
    public static final int IdentifyTypeMoneySilver = 52;
    public static final int IdentifyTypeMoneyBronze = 53;

    //6.0权限管理
    public static final int PERMISSION_REQ_CODE_READ_STORAGE = 100;
    public static final int PERMISSION_REQ_CODE_WRITE_STORAGE = 101;
    public static final int PERMISSION_REQ_CODE_READ_CAMERA = 102;


    public class OrderState {
        public static final String Wait = "0";
        public static final String Cancel = "1";
        public static final String Real = "2";
        public static final String Fake = "3";
        public static final String Doubt = "4";
    }

    public class IdentifyType {
        public static final int Normal = 0;
        public static final int Speed = 1;
        public static final int Video = 2;
        public static final int Order = 3;
        public static final int Expert = 4;
    }

    public static int getIdentifyType(int type) {
        switch (type) {
            case IdentifyType.Normal:
                return R.string.identify_type_normal;
            case IdentifyType.Speed:
                return R.string.identify_type_speed;
            case IdentifyType.Video:
                return R.string.identify_type_video;
            case IdentifyType.Order:
                return R.string.identify_type_order;
            case IdentifyType.Expert:
                return R.string.identify_type_expert;
            default:
                return R.string.identify_type_normal;
        }
    }

    public static final String SCORE_OBTAIN_TYPE_PAY = "支付";
    public static final String SCORE_OBTAIN_TYPE_REGISTER = "注册";
    public static final String SCORE_OBTAIN_TYPE_SHARE = "分享";
    public static final String SCORE_OBTAIN_TYPE_PROMOTION = "活动";
    public static final String SCORE_OBTAIN_TYPE_RECHARGE = "充值";
    public static final String SCORE_OBTAIN_TYPE_COMMENTS = "评论";
    public static final String SCORE_OBTAIN_TYPE_IDENTIFY = "鉴定";

    public static String[] SCORE_OBTAIN_TYPES = {
            SCORE_OBTAIN_TYPE_PAY,
            SCORE_OBTAIN_TYPE_REGISTER,
            SCORE_OBTAIN_TYPE_SHARE,
            SCORE_OBTAIN_TYPE_PROMOTION,
            SCORE_OBTAIN_TYPE_RECHARGE,
            SCORE_OBTAIN_TYPE_COMMENTS,
            SCORE_OBTAIN_TYPE_IDENTIFY
    };

    public static String[] IDENTIFY_KIND_TABLE = {
            "瓷器",
            "玉器",
            "书画",
            "铜器",
            "钱币",
            "杂项"
    };

    public static String[] IDENTIFY_KIND_TABLE_ID = {
            "1",
            "2",
            "3",
            "4",
            "5",
            "7"
    };

    public static int[] IDENTIFY_IMG_TABLE = {
            R.drawable.icon_normal,
            R.drawable.icon_speed,
            R.drawable.icon_order,
            R.drawable.icon_expert_panel,
            R.drawable.icon_fast,
    };
    public static String[] IDENTIFY_METHOD_TABLE = {
            "普通鉴定",
            "极速鉴定",
            "预约鉴定",
            "专家团鉴定",
            "快速鉴定",
    };

    public static String[] IDENTIFY_TIME_TABLE = {
            "3天",
            "1小时",
            "权威专家当面鉴定",
            "5天",
            "24小时",
    };

    public static String[] IDENTIFY_PRICES = {
            "￥5", "￥20", "￥800", "￥30", "￥10"
    };

    public static final int GOODS_TRUE = 2;
    public static final int GOODS_FALSE = 3;
    public static final int GOODS_IMPEACH = 4;

    public static String[] HOME_SERVICE_TYPE = {
            "2",
            "3"
    };

    public static String[] HOME_SERVICE_TYPE_NAME = {
            "服务项目",
            "定制服务"
    };

    public static String[] SERVICE_TYPE_NAME = {
            "上门鉴定",
            "视频鉴定",
            "当面鉴定",
            "鉴定会",
            "专家团",
            "亲临卖场",
            "拍卖现场",
            "收藏讲座"
    };

    public static int[] SERVICE_TYPE_BIG_IMG = {
            R.drawable.service_visit_icon,
            R.drawable.service_video_icon,
            R.drawable.service_face_to_face_icon,
            R.drawable.service_appraisal_icon,
            R.drawable.service_expert_panel_icon,
            R.drawable.service_local_icon,
            R.drawable.service_auction_icon,
            R.drawable.service_lecture_icon
    };

    public static int[] SERVICE_TYPE_FONT_IMG = {
            R.drawable.service_visit_font,
            R.drawable.service_video_font,
            R.drawable.service_face_to_face_font,
            R.drawable.service_appraisal_font,
            R.drawable.service_expert_panel_font,
            R.drawable.service_local_font,
            R.drawable.service_auction_font,
            R.drawable.service_lecture_font
    };

    public static int[] SERVICE_TYPE_ID = {
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
    };

    public static int[] SERVICE_PROCESS_IMG = {
            R.drawable.service_process_appointment_icon,
            R.drawable.service_process_payment_icon,
            R.drawable.service_process_affirm_icon,
            R.drawable.service_process_meet_icon,
    };

    public static int[] COLLECTION_TYPE_IMG = {
            R.drawable.identify_china_icon,
            R.drawable.identify_jade_icon,
            R.drawable.identify_paint_icon,
            R.drawable.identify_bronze_icon,
            R.drawable.identify_money_icon,
            R.drawable.identify_sundry_icon
    };

    public static int[] COLLECTION_MEET_TYPE_IMG = {
            R.drawable.identify_china_icon,
            R.drawable.identify_sundry_icon
    };

    public static int[] COLLECTION_TYPE_IMG_NAME = {
            R.drawable.identify_china_icon_name,
            R.drawable.identify_jade_icon_name,
            R.drawable.identify_paint_icon_name,
            R.drawable.identify_bronze_icon_name,
            R.drawable.identify_money_icon_name,
            R.drawable.identify_sundry_icon_name
    };

    public static int[] COLLECTION_TYPE_MEET_IMG_NAME = {
            R.drawable.identify_china_icon_name,
            R.drawable.identify_sundry_icon_name
    };
}
