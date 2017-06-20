package com.bobao.identifypro.constant;

/**
 * Created by star on 15/6/1.
 */
public class IntentConstant {
    // 拍照、提交订单
    public static final String IdentifyId = "identify_id";
    public static final String IDENTIFY_TYPE = "identify_type";
    public static final String IdentifyMethodInfo = "identify_method_info";
    public static final String IdentifyMethodPrices = "identify_method_prices";
    public static final String UsingPictureFilePaths = "using_picture_file_paths";
    public static final String UsingPictureIndex = "using_pickture_index";
    public static final String PictureFilePath = "picture_file_path";
    public static final String TotalPictureNum = "total_picture_num";
    public static final String CurrentPictureNum = "current_picture_num";
    public static final String NextPictureNum = "next_picture_num";
    public static final String RefreshPictureNum = "refresh_picture_num";

    public static final String KEY_MAIN_PAGER_FRAGMENT_ID = "key_main_page_fragment_id";

    public static final String USER_PAY_GOODS_ID = "goods_id";
    public static final String EXPERT_ID = "expert_id";
    public static final String ORGANIZATION_ID = "organization_id";
    public static final String ORGANIZATION_NAME = "organization_name";
    public static final String EXPERT_NAME = "expert_name";

    public static final String INFO_ID = "info_id";
    public static final String ORDER_ID = "order_id";

    public static final String IDENTIFY_PAGE_INDEX = "identify_page_index";
    public static final int IDENTIFY_PAGE_INDEX_NO_PAY = 0;
    public static final int IDENTIFY_PAGE_INDEX_NO_IDENTIFY = 1;
    public static final int IDENTIFY_PAGE_INDEX_IDENTIFIED = 2;

    public static final String USER_ACCOUNT_BALANCE = "user_account_banlance";
    public static final String USER_ID = "userid";
    public static final String USER_RECHARG_VALUE = "recharge_value";
    public static final String USER_SCORE = "user_score";

    public static final String APP_EXIT_KEY = "app_exit";
    public static final int APP_EXIT_CODE = 1;

    public static final String QUERY_GOODS_STATE = "ask_goods_state";
    public static final String QUERY_GOODS_PRICE = "ask_goods_price";

    public static final String QUERY_REPORT = "ask_report";
    public static final String QUERY_GOODS_PHOTO = "ask_photo";
    public static final String QUERY_GOODS_FROM = "ask_from";
    public static final String QUERY_GOODS_TO = "ask_to";
    public static final String QUERY_GOODS_ID = "gid";
    public static final String QUERY_TYPE = "query_type";

    public static final String QUERY_EXPERT_NAME = "expert_name";
    public static final String QUERY_EXPERT_HONOR = "expert_honor";

    public static final String CHARGED_STATE = "identify_state";

    public static final int RequestCodeCheckPhoneNumber = 10009;
    public static final int RequestCodeTakePicture = 10010;
    public static final int RequestCodeSelectPicture = 10011;
    public static final int RequestCodeIdentifyType = 10000;

    public static final String TARGET_ACTIVITY = "target_activity";
    public static final String TARGET_FRAGMENT = "target_fragment";
    public static final String TYPE = "type";
    //详情页banner进图片浏览页面
    public static final String ORDER_DETAIL_BANNER_IMG_URLS = "home_banner_img_urls";
    public static final String ORDER_DETAIL_BANNER_IMG_INDEX = "home_banner_img_index";
    public static final String ORDER_DETAIL_BANNER_IMG_RATIOS = "home_banner_img_ratios";

    //修改订单
    public static final String MODIFY_ORDER = "modify_order";
    public static final String MODIFY_CURRENT_INDEX = "modify_current_index";

    public static final String CHECK_PHONE_FLAG = "code_success";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String QUERY_GOOD_NAME = "query_good_name";

    public static final String IDENTIFY_TYPE_FLAG = "identify_type_flag";

    public static final String ORDER_VIDEO_URL = "order_video_url";
    public static final String QUERY_GOODS_HEAD = "order_query_head";

    public static final String IS_MY_ORDER_FLAGS = "is_my_order_flags";
    public static final String QUERY_EXPERT_ID = "query_expert_id";

    public static final String IntentAction = "intent_action";
    public static final String SubmitOrder = "submit_order";

    public static final String IS_FROM_NOTIFICATION = "is_from_notification";

    public static final String WEB_URL = "web_url";
    public static final String WEB_TITLE = "web_title";

    public static final String IDENTIFY_TYPE_REGISTRATION = "identify_type_registration";

    public static final int RECYCLERVIEW_SIZE_GRIDLAYOUT_THREE = 3;
    public static final int RECYCLERVIEW_SIZE_GRIDLAYOUT_FOUR = 4;

    public static final String SERVICE_TYPE_NAME = "service_type_name";
    public static final String SERVICE_TYPE = "service_type";
    public static final String IDENTIFY_TYPE_NAME = "identify_type_name";
    public static final String APPRAISAL_COST = "appraisal_cost";

    public static final int RESERVATION_EXPERTS = 1;
    public static final int EXPERTS_LIST = 2;
    public static final int COLLECTION_NUM = 3;
    public static final int EXPERT_SERVICE_TYPE = 4;
    public static final int DETAIL_EXPERT_SERVICE_TYPE = 5;

    public static final String EXPERT_LIST_ENTRANCE = "expert_list_entrance";
    public static final int EXPERT_LIST = 0;
    public static final int EXPERT_CLASSIFICATION = 1;

    public static final String ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY = "activity_from_service_note_activity";//服务说明页进入，确定鉴定类型
    public static final String ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY_FULL = "activity_from_service_note_activity_full";//从专家详情入口进入，已确定鉴定类型与专家
    public static final String ACTIVITY_FROM_EXPERT_DETAIL_ACTIVITY = "activity_from_expert_detail_activity";//专家详情页，确定专家
    public static final String ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY = "activity_from_appointment_expert_activity";//我的预约详情页修改,确定服务类型，鉴定类型，只能修改数量，说明
    public static final String ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY = "activity_from_appointment_expert_detail_activity";//查看我的预约专家详情页

    public static final String SERVICE_TYPE_ID_LIST = "service_type_id_list";
    public static final String SERVICE_TYPE_NAME_LIST = "service_type_name_list";
    public static final String SERVICE_TYPE_PRICE_LIST = "service_type_price_list";
    public static final String SERVICE_TYPE_INFO_LIST = "service_type_info_list";

    public static final int EXPERTS_DETAIL_ORDINARY_SERVICE = 1;
    public static final int EXPERTS_DETAIL_SPECIAL_SERVICE = 2;

    public static final int AUTH_CODE_LENGTH_FOUR = 4;
    public static final int AUTH_CODE_LENGTH_SIX = 6;
    public static final int PHONE_LENGTH = 11;
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 20;


    public static final int REQUESTCODE_CAMERA = 1;
    public static final int REQUESTCODE_PICK = 2;
    public static final int REQUESTCODE_CUTTING = 3;

    public static final int NICKNAME_MIN_LENGTH = 1;
    public static final int NICKNAME_MAX_LENGTH = 12;

    public static final int AUTH_CODE_TIME_OUT = 60;

    public static final String ACTIVITY_FROM_EDIT_PHONE = "activity_from_edit_phone";
    public static final String ACTIVITY_FROM_EDIT_PASSWORD = "activity_from_edit_password";

    public static final int UMENG_UPDATE_UNLATEST = 0;
    public static final int UMENG_UPDATE_LATEST = 1;

    public static final String APPOINTMENT_IDENTIFY_ID = "appointment_identify_id";
    public static final String APPOINTMENT_IDENTIFY_TYPE = "appointment_identify_type";

    public static final String EXPERTS_RECOMMEND_ID = "0";
    public static final String OPTIONAL_EXPERTS_ID = "1";

    public static final String APPOINTMENT_IDENTIFY_IDENTIFY = "1";
    public static final String APPOINTMENT_IDENTIFY_DETAIL = "2";
    public static final String RESUME_DETAIL_NAME = "resume_detail_name";
    public static final String RESUME_DETAIL_TIME = "resume_detail_time";
    public static final String RESUME_DETAIL_FROM = "resume_detail_from";
    public static final String RESUME_DETAIL_PRICE = "resume_detail_price";

    public static final String SELECT_A_DATE_TIME_FORMAT = "yyyy-MM-dd";

    public static final String LEGALDOCUMENT_ISONECOME = "one_come";

    public static final String RESUME_RECORD_FROM = "enter";

    public static final String USER_INFO_PROPERTIES_FILE = "user_info_properties.properties";

    public static final String SP_KEY_USER_INTEGRATE_VERSION="sp_key_user_integrate_version";
    public static final String SP_KEY_USER_INTEGRATE_INTEGRAL="sp_key_user_integrate_integral";
    public static final String SP_KEY_USER_INTEGRATE_SLOGAN="sp_key_user_integrate_slogan";
    public static final String SP_KEY_USER_INTEGRATE_RULE="sp_key_user_integrate_rule";
    public static final String SP_KEY_USER_INTEGRATE_MODE="sp_key_user_integrate_mode";

    public static final String INTEGRATE_OBTAIN= "0";
    public static final String INTEGRATE_APPLY= "1";
    public static final String INTEGRATE_RETURN= "2";

    public static final String APP_THUMBNAIL_NAME="identifypro";

    public static final String ACTIVITY_TITLE = "activity_title";

    public static final String SAVE_EXPERT_DATA = "save_expert_data";
}
