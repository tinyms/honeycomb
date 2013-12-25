package com.tinyms.core;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by tinyms on 13-12-25.
 */
@WebServlet(name = "UploaderServlet", urlPatterns = "/uploader")
public class UploaderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        Map<String,Object> msg = new HashMap<String, Object>();
        if(ServletFileUpload.isMultipartContent(req)){
            Date today = Calendar.getInstance().getTime();
            String relativePath = "upload/"+ DateFormatUtils.format(today,"yyyyMM")+"/"+DateFormatUtils.format(today,"dd")+"/";
            String savePath = HttpContext.realpath+relativePath+"/";
            Utils.mkdirs(savePath);
            String indexJsp = savePath+"index.jsp";
            File jspFile = new File(indexJsp);
            if(!jspFile.exists()){
                FileUtils.writeStringToFile(jspFile,"Error.");
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
            // 1024*1024 == 1M
            factory.setSizeThreshold(1024*1024);
            factory.setRepository(new File(savePath));
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Set overall request size constraint
            upload.setSizeMax(5 * 1024 * 1024);//5M
            UploadItem uploadItem = new UploadItem();
            try {
                List<FileItem> files = upload.parseRequest(req);
                for(FileItem f : files){
                    if(f.isFormField()){
                        String name = f.getFieldName();
                        String val = f.getString();
                        uploadItem.getFormfields().put(name,val);
                    }else{
                        String name = f.getName();
                        String extName = FilenameUtils.getExtension(name);
                        String saveFileName = UUID.randomUUID().toString()+"."+extName;
                        String saveFileRelativePath = relativePath+saveFileName;
                        String realPath = savePath+saveFileName;
                        f.write(new File(realPath));
                        UploadFileItem uploadFileItem = new UploadFileItem();
                        uploadFileItem.setName(name);
                        uploadFileItem.setExt(extName);
                        uploadFileItem.setRelativePath(saveFileRelativePath);
                        uploadFileItem.setRealPath(realPath);
                        uploadFileItem.setMimeType(Files.probeContentType(Paths.get(realPath)));
                        uploadFileItem.setSize(Files.size(Paths.get(realPath)));
                        uploadItem.getFileItems().add(uploadFileItem);
                    }
                }
                msg.put("flag","OK");
                //deal upload data after. call plugin..
                List<IUploader> iUploaders = ClassLoaderUtil.getPlugin(IUploader.class);
                for(IUploader uploader : iUploaders){
                    uploader.doInFileUpload(uploadItem, msg);
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
                msg.put("flag","Failure");
            } catch (Exception e) {
                e.printStackTrace();
                msg.put("flag","Failure");
            }
        }else{
            msg.put("flag","Failure");
        }

        resp.getWriter().write(Utils.encode(msg));
    }
}
