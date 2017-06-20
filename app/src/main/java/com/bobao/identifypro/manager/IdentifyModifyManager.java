package com.bobao.identifypro.manager;

import android.text.TextUtils;

import com.bobao.identifypro.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cm on 2015/7/21.
 * 修改订单的缓存数据
 */
public class IdentifyModifyManager {
    private static IdentifyModifyManager sInstance = new IdentifyModifyManager();

    private IdentifyModifyManager() {
    }

    //缓存的订单信息数据
    private Map<String, ModifyIdnentifyData> mIdentifyModifyCacheData = new HashMap<>();

    private Map<String, HashMap<Integer, String>> mEditHistoryDatas = new HashMap<>();

    public static IdentifyModifyManager getInstance() {
        return sInstance;
    }

    public ModifyIdnentifyData getModifyIdnentifyData(String orderId) {
        return mIdentifyModifyCacheData.get(orderId);
    }

    /**
     * 获得编辑后的可视图片列表
     */
    public List<String> getInvalidDisplayPhotos(String orderId) {
        ArrayList<String> photos = new ArrayList<>();
        ModifyIdnentifyData modifyIdnentifyData = getModifyIdnentifyData(orderId);
        ModifyIdnentifyData.DataEntity modifyIdnentifyDataEntity = modifyIdnentifyData.getData();
        List<String> tempPhotos = modifyIdnentifyDataEntity.getPhoto();
        for (String photo : tempPhotos) {
            photos.add(photo);
        }
        return photos;
    }

    public void saveModifyIdnentifyData(String orderId, ModifyIdnentifyData data) {
        mIdentifyModifyCacheData.put(orderId, data);
    }

    public void clearCache() {
        mIdentifyModifyCacheData.clear();
        mEditHistoryDatas.clear();
    }

    public boolean checkIdentifyDataEmpty(String orderId) {
        if (mIdentifyModifyCacheData == null
                || mIdentifyModifyCacheData.isEmpty()
                || getModifyIdnentifyData(orderId) == null) {
            return true;
        }

        return false;
    }

    /**
     * 缓存当前正在编辑的信息
     *
     * @param orderId   订单ID
     * @param editIndex 编辑的索引
     * @param filePath  编辑索引对饮的文件路径
     */
    public void saveCurrentEditInfo(String orderId, int editIndex, String filePath) {
        //当前编辑信息保存到编辑记录
        saveEditedInfo(orderId, editIndex, filePath);
        refreshIdentifyData(orderId);
    }

    /**
     * 根据修改记录,刷新显示的数据
     */
    private void refreshIdentifyData(String orderId) {
        HashMap<Integer, String> editedDatas = mEditHistoryDatas.get(orderId);
        ModifyIdnentifyData modifyIdnentifyData = getModifyIdnentifyData(orderId);
        if (modifyIdnentifyData != null) {
            List<String> photos = modifyIdnentifyData.getData().getPhoto();
            if (photos != null) {
                for (Integer edittedPos : editedDatas.keySet()) {
                    String path = editedDatas.get(edittedPos);
                    if (edittedPos < photos.size()) {//修改操作
                        photos.set(edittedPos, path);
                    } else {
                        photos.add(path);
                    }
                }

            }
            //更新图片数据
            modifyIdnentifyData.getData().setPhoto(photos);
            saveModifyIdnentifyData(orderId, modifyIdnentifyData);
        }
    }

    /**
     * 缓存整个会话的修改信息，添加到修改记录
     *
     * @param orderId   订单ID
     * @param editIndex 编辑的位置
     * @param filePath  新添加的文件路径
     */
    public void saveEditedInfo(String orderId, int editIndex, String filePath) {
        HashMap<Integer, String> edittedDatas = mEditHistoryDatas.get(orderId);
        if (edittedDatas == null) {
            edittedDatas = new HashMap<>();
        }
        edittedDatas.put(editIndex, filePath);
        mEditHistoryDatas.put(orderId, edittedDatas);
    }

    /**
     * 移除一条编辑记录
     * 注意的坑:移除一条记录后，该记录位置删除，在该位置之后的索引记录都需要前移，
     * 否则会出现下面的情况:
     * example：添加了2张图片(记录index:4,5),
     * 删除了index=4（记录index：5），list长度变为5，
     * 再添加图片（index=5，此时原来的index= 5的记录会被覆盖！！！）
     * 所以没删除一条记录，就需要把该位置之后的记录前移一位
     */
    private void removeEditedInfo(String orderId, int editIndex) {
        HashMap<Integer, String> edittedDatas = mEditHistoryDatas.get(orderId);
        if (edittedDatas == null) {
            edittedDatas = new HashMap<>();
        }
        edittedDatas.remove(editIndex);
        HashMap<Integer, String> tempEdittedDatas = new HashMap<>();
        //小于删除位置的记录保持不变，大于删除位置的记录前移一位
        for (Integer key : edittedDatas.keySet()) {
            String value = edittedDatas.get(key);
            if (key < editIndex) {
                tempEdittedDatas.put(key, value);
            } else {
                tempEdittedDatas.put(key - 1, value);
            }
        }
        //copy更新后的记录
        copyCollection(tempEdittedDatas, edittedDatas);
        mEditHistoryDatas.put(orderId, edittedDatas);
    }

    private void copyCollection(Map<Integer, String> srcMap, Map<Integer, String> destMap) {
        destMap.clear();
        destMap.putAll(srcMap);
    }

    /**
     * 删除某一条目,不管是否编辑过
     * Setp1：添加编辑记录 key-value: index-""
     * Step2：删除OriginalString(如果有的话)
     * Step3：移除该位置的编辑记录-->删除对应位置的filepath，
     */
    public void deleteEditItem(String orderId, int editIndex) {
        //Step1
        saveCurrentEditInfo(orderId, editIndex, "");
        ModifyIdnentifyData modifyIdnentifyData = getModifyIdnentifyData(orderId);
        List<String> originalPaths = modifyIdnentifyData.getData().getOriginal();
        if (mEditHistoryDatas != null && !mEditHistoryDatas.isEmpty()) {
            //Step2
            HashMap<Integer, String> editedData = mEditHistoryDatas.get(orderId);
            for (Integer index : editedData.keySet()) {
                if (index < originalPaths.size()) {
                    originalPaths.set(index, "");
                }
            }
        }
        removeEditedInfo(orderId, editIndex);//Step3
        refreshIdentifyData(orderId);
    }

    /**
     * 根据编辑信息,删除对应的path,上传
     */
    public String getOriginalPathsAfterEdit(String orderId, List<String> originalPaths) {
        if (mEditHistoryDatas == null || mEditHistoryDatas.isEmpty()) {
            return getStringFromList(originalPaths);
        }
        HashMap<Integer, String> edittedDatas = mEditHistoryDatas.get(orderId);
        for (Integer index : edittedDatas.keySet()) {
            if (index < originalPaths.size()) {
                originalPaths.set(index, "");
            }
        }
        return getStringFromList(originalPaths);
    }

    /**
     * 根据修改记录，获取需要上传的文件列表
     */
    public List<String> getCommitFilePaths(String orderId) {
        ArrayList<String> result = new ArrayList<>();
        if (mEditHistoryDatas == null || mEditHistoryDatas.isEmpty()) {
            return null;
        }
        HashMap<Integer, String> edittedDatas = mEditHistoryDatas.get(orderId);
        for (Integer index : edittedDatas.keySet()) {
            String s = edittedDatas.get(index);
            if (!TextUtils.isEmpty(s)) {
                result.add(s);
            }
        }
        return result;
    }

    public String getStringFromList(List<String> list) {
        String result = "";
        for (String s : list) {
            if (!TextUtils.isEmpty(s)) {
                result = StringUtils.getString(result, s, ";");
            }
        }
        if (TextUtils.isEmpty(result)) {
            return result;
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 拿到编辑过的索引列表，给服务器用于排序
     */
    public String getEdittedIndexs(String orderId) {
        String result = "";
        if (mEditHistoryDatas == null || mEditHistoryDatas.isEmpty()) {
            return result;
        }
        HashMap<Integer, String> edittedDatas = mEditHistoryDatas.get(orderId);
        if (edittedDatas == null) {
            return result;
        }
        for (Integer index : edittedDatas.keySet()) {
            result = StringUtils.getString(result, index, ",");
        }
        if (!TextUtils.isEmpty(result)) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public void saveIdentifyMethod(String orderId, String method) {
        ModifyIdnentifyData modifyIdnentifyData = getModifyIdnentifyData(orderId);
        if (modifyIdnentifyData != null) {
            modifyIdnentifyData.getData().setJb_type(method);
            saveModifyIdnentifyData(orderId, modifyIdnentifyData);
        }
    }

    public void saveRemark(String orderId, String remark) {
        ModifyIdnentifyData modifyIdnentifyData = getModifyIdnentifyData(orderId);
        modifyIdnentifyData.getData().setNote(remark);
        saveModifyIdnentifyData(orderId, modifyIdnentifyData);
    }

}
