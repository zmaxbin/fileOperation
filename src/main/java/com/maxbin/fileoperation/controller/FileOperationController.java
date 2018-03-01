package com.maxbin.fileoperation.controller;

import com.maxbin.fileoperation.WebSecurityConfig;
import com.maxbin.fileoperation.domain.FileEntity;
import com.maxbin.fileoperation.domain.UserEntity;
import com.maxbin.fileoperation.repository.FileRepository;
import com.maxbin.fileoperation.service.HdfsService;
import com.maxbin.fileoperation.service.UserService;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/front")
public class FileOperationController {

    private static String UPLOADED_FOLDER = "hdfs://172.16.56.100:9000/";


    @Autowired
    private UserService userService;

    @Autowired
    private HdfsService hdfsService;

    @Autowired
    private FileRepository fileRepository;

    //index页面
    @RequestMapping("/index")
    public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY) String account, Model model) {
        return "index";
    }

    //注册页面
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "register";
    }

    //注册协议
    @GetMapping("/protocol")
    public String protocol() {
        return "protocol";
    }

    //登陆页面
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "login";
    }

    //文件操作页面
    @RequestMapping("/fileOperation")
    public String fileOperation(@SessionAttribute(WebSecurityConfig.SESSION_KEY) String account, Model model) {
        return "fileOperation";
    }

    //注册方法
    @RequestMapping("/addregister")
    public String register(UserEntity userEntity){
        return userService.registerUser(userEntity);
    }

    //登录方法
    @RequestMapping("/loginVerify")
    public String login(UserEntity userEntity, Model model, HttpSession session){
        boolean verify = userService.verifyLogin(userEntity);
        if (verify) {
            session.setAttribute(WebSecurityConfig.SESSION_KEY, userEntity.getUsername());
            model.addAttribute("username", userEntity.getUsername());
            model.addAttribute("password", userEntity.getPassword());
            return "index";
        } else {
            return "redirect:/front/login";
        }
    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "redirect:/front/login";
    }

    @GetMapping("/uploadFileAction")
    public String getFiles(Model model) {
        List<FileEntity> fileLsit = fileRepository.findAll();
        if (fileLsit != null) {
            model.addAttribute("files",fileLsit);
        }
        return "fileOperation";
    }

    //hdfs文件上传
    @PostMapping("/uploadFileAction")
    public String signalUploadFile(@RequestParam("uploadFile") MultipartFile file, RedirectAttributes redirectAttributes, FileEntity fileEntity) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/front/uploadStatus";
        }
        try {
            FileSystem fs = hdfsService.init();
            Path dst = new Path(UPLOADED_FOLDER + file.getOriginalFilename());
            FSDataOutputStream os = fs.create(dst);
            org.apache.commons.io.IOUtils.copy(file.getInputStream(),os);

            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setFilesize(file.getSize());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fileEntity.setFileupdatetime(df.format(new Date()));
            fileRepository.save(fileEntity);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/front/uploadFileAction";
    }

    //hdfs文件下载
    @PostMapping("/downloadFileAction")
    public void downloadFileAction(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        try {
            FileSystem fs = hdfsService.init();
            FSDataInputStream fis = fs.open(new Path(UPLOADED_FOLDER + "costTime.txt"));
            response.setHeader("Content-Disposition", "attachment; filename="+"costTime.txt");
            IOUtils.copy(fis,response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

//    @PostMapping("/uploadFileAction")
//    public String signalUploadFile(@RequestParam("uploadFile") MultipartFile file, RedirectAttributes redirectAttributes) {
//        if (file.isEmpty()) {
//            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//            return "redirect:/front/uploadStatus";
//        }
//        try {
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//            Files.write(path, bytes);
//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "redirect:/front/uploadStatus";
//    }
//
//    //文件下载
//    @PostMapping("/downloadFileAction")
//    public void downloadFileAction(HttpServletRequest request, HttpServletResponse response) {
//        response.setCharacterEncoding(request.getCharacterEncoding());
//        response.setContentType("application/octet-stream");
//        FileInputStream fis = null;
//        try {
//            File file = new File("E:\\b.txt");
//            fis = new FileInputStream(file);
//            response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
//            IOUtils.copy(fis,response.getOutputStream());
//            response.flushBuffer();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
