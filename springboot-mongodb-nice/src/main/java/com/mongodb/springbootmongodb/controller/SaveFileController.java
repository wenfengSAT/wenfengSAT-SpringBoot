package com.mongodb.springbootmongodb.controller;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.springbootmongodb.util.UUIDStringUtil;

/** 
* @author 作者 zhujf: 
* @version 创建时间：2017年10月17日 下午6:24:33 
* 
*/
@RestController
@RequestMapping("savefile")
public class SaveFileController {
	
	@Autowired
    private  MongoTemplate mongoTemplate;
	/**
     * 存储文件 
     * @param collectionName 集合名 
     * @param file 文件 
     * @param fileid 文件id 
     * @param companyid 文件的公司id 
     * @param filename 文件名称
     */
    public  void SaveFile(String collectionName, File file, String fileid, String companyid, String filename) {
        try {
            DB db = mongoTemplate.getDb();
            // 存储fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSInputFile gfs = gridFS.createFile(file);
           // GridFSInputFile gfs = gridFS.createFile(byte[] date);
            gfs.put("aliases", companyid);
            gfs.put("filename", fileid);
           // gfs.put("contentType", filename.substring(filename.lastIndexOf(".")));
            gfs.put("contentType", filename);
            gfs.save();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("存储文件时发生错误！！！");
        }
    }
    
    
 // 取出文件
    public GridFSDBFile retrieveFileOne(String collectionName, String filename) {
        try {
            DB db = mongoTemplate.getDb();
            // 获取fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSDBFile dbfile = gridFS.findOne(filename);
            //GridFSDBFile dbfile = gridFS.find(objectId);
            Streams.copy(dbfile.getInputStream(), new FileOutputStream("D:/images/aa.jpg"), true);
            if (dbfile != null) {
                return dbfile;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    
    @RequestMapping("save")
    public void save(){
    	 String path = "D:/images/x4.jpg";
 		File file = new File(path);
 		
 		SaveFile("file",file, UUIDStringUtil.getUUIDString(), "1", "mv1");
    }
    
    @RequestMapping("read")
    public void read(){
 		retrieveFileOne("file","3c696bc20b994523a7c2b0be1fc9b369");
    }
   
}
