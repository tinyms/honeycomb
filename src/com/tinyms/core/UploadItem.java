package com.tinyms.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tinyms on 13-12-25.
 */
public class UploadItem {
    private Map<String, String> formfields = new HashMap<String, String>();
    private List<UploadFileItem> fileItems = new ArrayList<UploadFileItem>();

    public Map<String, String> getFormfields() {
        return formfields;
    }

    public List<UploadFileItem> getFileItems() {
        return fileItems;
    }
}
