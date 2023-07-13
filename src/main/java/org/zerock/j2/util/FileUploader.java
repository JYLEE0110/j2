package org.zerock.j2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

@Component
@Log4j2
public class FileUploader {

    // 파일 업로드 시 예외처리
    public static class UploadException extends RuntimeException {

        public UploadException(String msg) {
            super(msg);
        }

    }

    @Value("${org.zerock.upload.path}")
    private String path;

    public void removeFiles(List<String> fileNames){

        if(fileNames == null || fileNames.size() == 0) {

            return;
        }

        // 예외처리 때문에 람다식으로 X
        // 파일을 삭제 할 경우
        for(String fname :fileNames){

            // 원본파일
            File original = new File(path, fname);
            File thumb = new File(path, "s_"+fname);

            // 썸네일이 존재한다면 => 사진을 경우
            if(thumb.exists()){
                thumb.delete();
            }
            original.delete();
        }

    }

    public List<String> uploadFiles(List<MultipartFile> files, boolean makeThumbnail) {

        if (files == null || files.size() == 0) {

            throw new UploadException("No file");

        }

        List<String> uploadFileNames = new ArrayList<>();

        log.info("path : " + path);

        log.info(files);

        // loop 예외처리해서 람다식 X
        for (MultipartFile mFile : files) {

            String originalFileName = mFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();

            // save할 파일 uuid + originalFileName
            String saveFileName = uuid + "_" + originalFileName;

            // 지정한 path에 파일을 저장하려고 쓰임
            File saveFile = new File(path, saveFileName);

            // 자동 close해주려고 try catch
            try (
                    InputStream in = mFile.getInputStream();
                    OutputStream out = new FileOutputStream(saveFile);) {

                // 원본파일 복사 후 저장
                FileCopyUtils.copy(in, out);

                // 썸네일이 필요하다면
                if (makeThumbnail) {

                    File thumbOutFile = new File(path, "s_" + saveFileName);

                    Thumbnailator.createThumbnail(saveFile, thumbOutFile, 200, 200);
                }

                uploadFileNames.add(saveFileName);

            } catch (Exception e) {
                throw new UploadException("Upload Fail" + e.getMessage());
            }

        }

        return uploadFileNames;

    }

}
