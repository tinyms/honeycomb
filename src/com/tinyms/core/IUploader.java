package com.tinyms.core;

import java.util.Map;

/**
 * Created by tinyms on 13-12-25.
 */
public interface IUploader {
    public void doInFileUpload(UploadItem uploadItem, Map<String,Object> msg);
}
