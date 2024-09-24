package com.springproject.springboot.security.project.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Service
public class UploadProfileService {

    @Autowired
    private AmazonS3 s3Amazon;

    @Value("${cloud.aws.region.static}")    
    private String region;

    @Value("${aws.s3.bucketName}")    
    private String bucketName;

    private String fileName;
 
    public String uploadImage(final MultipartFile multiplePartToFile, final String folderName) {
        try {
            final File file = convertMultiPartFileToFile(multiplePartToFile);

            if (!isBucketExist(bucketName)) {
                throw new RuntimeException("Bucket " + bucketName + " does not exist.");
            }

            fileName = uploadFileToS3Bucket(bucketName, folderName, file);
            System.out.println("Uploaded file name: " + fileName);
            file.delete();

        } catch (final AmazonServiceException e) {
            System.out.println("Amazon Service Error: " + e.getErrorMessage());
        } catch (final Exception e) {
            System.out.println("General Error: " + e.getMessage());
        }
        return fileName;
    }

private File convertMultiPartFileToFile(final MultipartFile multiplePartToFile) {
        File file = new File(multiplePartToFile.getOriginalFilename());
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multiplePartToFile.getBytes());
        } catch (final IOException e) {
            System.out.println("Error Message: " + e.getMessage());
        }
        return file;
    }

    private String uploadFileToS3Bucket(final String bucketName, final String folder, final File file) {
        fileName = folder + "/" + System.currentTimeMillis() + "_" + file.getName().replaceAll("\\s+", "_");
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        s3Amazon.putObject(putObjectRequest);
        return fileName;
    }

    private boolean isBucketExist(String bucketName) {
        return s3Amazon.doesBucketExistV2(bucketName);
    }
}