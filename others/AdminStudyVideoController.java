package io.bloom.insurance.web.controller.admin;

import cn.zfgc.base.model.ReturnDataDO;
import io.bloom.buava.model.BTPage;
import io.bloom.insurance.web.mapper.GGCompanyMapper;
import io.bloom.insurance.web.model.AccountDO;
import io.bloom.insurance.web.model.GGCompanyDO;
import io.bloom.insurance.web.model.StudyVideoDO;
import io.bloom.insurance.web.model.StudyVideoLogDO;
import io.bloom.insurance.web.model.commond.StudyVideoCommand;
import io.bloom.insurance.web.service.drp.IStudyVideoLogService;
import io.bloom.insurance.web.service.drp.IStudyVideoService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-24
 * 文件描述:学习专区管理
 */
@Controller
@RequestMapping("/admin/study-manage")
public class AdminStudyVideoController {
    private static final Logger logger = LoggerFactory.getLogger(AdminStudyVideoController.class);

    @Autowired
    private IStudyVideoService studyVideoService;

    @Autowired
    private IStudyVideoLogService studyVideoLogService;

    @Autowired
    private GGCompanyMapper ggCompanyMapper;

    /**
     * 视频文件上传
     * @param videoFile  视频文件
     * @param imageFile  视频预览图片
     * @param title      手机上展示的视频标题
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/video-upload", method=RequestMethod.POST)
    private String upload(@RequestParam("file1") MultipartFile videoFile,
                          @RequestParam("file2") MultipartFile imageFile,
                          @RequestParam("title")  String title,
                          ModelMap modelMap) throws Exception {
        String videoFileFullName = videoFile.getOriginalFilename(); //视频文件带后缀的文件全名
        String videoFileName = videoFile.getName();      // 视频文件不带后缀的文件名
        String imageFileFullName = imageFile.getOriginalFilename(); //视频预览图片带后缀的文件全名
        String name = videoFileName;   //手机上展示的文件标题，默认为视频文件名称
        boolean isSuccess = true;
        StringBuilder errorMessage = new StringBuilder();

        if(title != null && !"".equals(title.trim())){
            name = title;
        }

        //视频文件格式校验
        if (!videoFileFullName.toLowerCase().endsWith(".flv")
                && !videoFileFullName.toLowerCase().endsWith(".mp4")
                && !videoFileFullName.toLowerCase().endsWith(".3gp")
                && !videoFileFullName.toLowerCase().endsWith(".mov")
                && !videoFileFullName.toLowerCase().endsWith(".avi")
                && !videoFileFullName.toLowerCase().endsWith(".wmv")
                ) {
            errorMessage.append("不是支持的文件类型！只支持视频上传！");
            isSuccess = false;
        }

        //预览图片格式校验
        if (!imageFileFullName.toLowerCase().endsWith(".jpg")
                && !imageFileFullName.toLowerCase().endsWith(".png")
                && !imageFileFullName.toLowerCase().endsWith(".bmp")
                && !imageFileFullName.toLowerCase().endsWith(".gif")) {
           errorMessage.append("不是支持的文件类型！只支持图片上传！");
           isSuccess = false;
        }

        if(isSuccess){
            //获取登录用户信息
            AccountDO accountDO =  (AccountDO) SecurityUtils.getSubject().getPrincipal();

            byte[] videoData = FileCopyUtils.copyToByteArray(videoFile.getInputStream());
            byte[] imageData = FileCopyUtils.copyToByteArray(imageFile.getInputStream());

            StudyVideoDO studyVideoDO = new StudyVideoDO();
            studyVideoDO.setVideoName(name);
            studyVideoDO.setVideoContent(videoData);
            studyVideoDO.setVideoFileType(getFileType(videoFileFullName));
            studyVideoDO.setImageContent(imageData);
            studyVideoDO.setImageFileType(getFileType(imageFileFullName));
            studyVideoDO.setCompanyCode(accountDO.getAreas());
            studyVideoDO.setCreateBy(accountDO.getUsername());
            studyVideoDO.setUpdateBy(accountDO.getUsername());

            //操作日志
            StudyVideoLogDO studyVideoLogDO = new StudyVideoLogDO();
            studyVideoLogDO.setVideoName(name);
            studyVideoLogDO.setVideoFileType(getFileType(videoFileFullName));
            studyVideoLogDO.setCompanyCode(accountDO.getAreas());
            studyVideoLogDO.setCreateBy(accountDO.getUsername());
            studyVideoLogDO.setOperator(accountDO.getUsername());
            studyVideoLogDO.setOperatorMsg("新增");

            studyVideoService.create(studyVideoDO);
            studyVideoLogService.create(studyVideoLogDO);

            return "redirect:/admin/study-manage/index";
        }else{
            modelMap.put("errorMessage",errorMessage);
            return "admin/study-manage/video-upload";
        }
    }


    /**
     * 跳转到视频文件编辑页面
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/video-edit/{id}", method=RequestMethod.GET)
    private String videoEdit(@PathVariable("id") String id,ModelMap modelMap) throws Exception {
        StudyVideoDO studyVideoDO =  studyVideoService.fetchByPk(Long.parseLong(id));
        modelMap.put("studyVideoDO", studyVideoDO);
        return "admin/study/video-edit";
    }

    /**
     * 视频文件编辑
     * @param imageFile  视频预览图片
     * @param title      手机上展示的视频标题
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/saveEdit", method=RequestMethod.POST)
    private String saveEdit(  @RequestParam("file2") MultipartFile imageFile,
                              @RequestParam("title")  String title,
                              @RequestParam("id") String id,
                               ModelMap modelMap) throws Exception {
        StudyVideoDO studyVideoDO =  studyVideoService.fetchByPk(Long.parseLong(id));
        String imageFileFullName = imageFile.getOriginalFilename(); //视频预览图片带后缀的文件全名
        boolean isSuccess = true;
        StringBuilder errorMessage = new StringBuilder();

        if(title != null && !"".equals(title.trim())){
            studyVideoDO.setVideoName(title);
        }

        //预览图片格式校验
        if (!imageFileFullName.toLowerCase().endsWith(".jpg")
                && !imageFileFullName.toLowerCase().endsWith(".png")
                && !imageFileFullName.toLowerCase().endsWith(".bmp")
                && !imageFileFullName.toLowerCase().endsWith(".gif")) {
            errorMessage.append("不是支持的文件类型！只支持图片上传！");
            isSuccess = false;
        }

        if(isSuccess){
            //获取登录用户信息
            AccountDO accountDO =  (AccountDO) SecurityUtils.getSubject().getPrincipal();

            byte[] imageData = FileCopyUtils.copyToByteArray(imageFile.getInputStream());

            studyVideoDO.setImageContent(imageData);
            studyVideoDO.setImageFileType(getFileType(imageFileFullName));
            studyVideoDO.setUpdateBy(accountDO.getUsername());

            //操作日志
            StudyVideoLogDO studyVideoLogDO = new StudyVideoLogDO();
            studyVideoLogDO.setVideoName(studyVideoDO.getVideoName());
            studyVideoLogDO.setVideoFileType(studyVideoDO.getVideoFileType());
            studyVideoLogDO.setCompanyCode(studyVideoDO.getCompanyCode());
            studyVideoLogDO.setCreateBy(studyVideoDO.getCreateBy());
            studyVideoLogDO.setOperator(accountDO.getUsername());
            studyVideoLogDO.setOperatorMsg("编辑");

            studyVideoService.updateByPk(studyVideoDO);
            studyVideoLogService.create(studyVideoLogDO);

            return "redirect:/admin/study-manage/index";
        }else{
            modelMap.put("errorMessage",errorMessage);
            return "admin/study-manage/video-edit/"+id;
        }
    }

    private  String getFileType(String fileFullName) {
        String fileType = null;

        if (fileFullName.toLowerCase().endsWith(".flv")){
            fileType = "video/x-flv";
        }else if(fileFullName.toLowerCase().endsWith(".mp4")){
            fileType = "video/mp4";
        }else if(fileFullName.toLowerCase().endsWith(".3gp")){
            fileType = "video/3gpp";
        }else if(fileFullName.toLowerCase().endsWith(".mov")){
            fileType = "video/quicktime";
        }else if(fileFullName.toLowerCase().endsWith(".avi")){
            fileType = "video/x-msvideo";
        }else if(fileFullName.toLowerCase().endsWith(".wmv")){
            fileType = "video/x-ms-wmv";
        }else if(fileFullName.toLowerCase().endsWith(".jpg")){
            fileType = "image/jpeg";
        }else if(fileFullName.toLowerCase().endsWith(".png")){
            fileType = "image/png";
        }else if(fileFullName.toLowerCase().endsWith(".bmp")){
            fileType = "image/bmp";
        }else if(fileFullName.toLowerCase().endsWith(".gif")){
            fileType = "image/gif";
        }

        return fileType;
    }

    /**
     * 跳转到学习专区管理页面
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        AccountDO accountDO = (AccountDO) SecurityUtils.getSubject().getPrincipal(); //获取登录人员信息
        List<GGCompanyDO> ggCompanyDOList = ggCompanyMapper.selectByComCode(accountDO.getAreas());
        modelMap.put("ggCompanyDOList",ggCompanyDOList);
        return "admin/study/study-manage";
    }

    @RequestMapping(value = "/study-list", method = RequestMethod.GET)
    @ResponseBody
    public BTPage<StudyVideoDO> studyList(StudyVideoCommand command){
        if(command.getCompanyCode() == null || "".equals(command.getCompanyCode().trim()) ){
            AccountDO accountDO = (AccountDO) SecurityUtils.getSubject().getPrincipal(); //获取登录人员信息
            command.setCompanyCode(accountDO.getAreas().length() > 4 ? accountDO.getAreas().substring(0, 4) : accountDO.getAreas());
        }else{
            String strArr[] = command.getCompanyCode().split("-");
            command.setCompanyCode(strArr[0]);
        }
        //亚太财产保险有限公司本部也作为总部，特殊处理
        if("0200".equals(command.getCompanyCode())){
            command.setCompanyCode(command.getCompanyCode().substring(0, 2));
        }
        return studyVideoService.page(command.apply());
    }

    /**
     * 学习专区视频上传
     * @return
     */
    @RequestMapping(value = "/video-upload", method = RequestMethod.GET)
    public String videoUploadPage() {
        return "admin/study/video-upload";
    }

    @RequestMapping(value = "/delVideo", method = RequestMethod.POST)
    @ResponseBody
    public String delVideo(@RequestParam("id") String id){
        AccountDO accountDO = (AccountDO) SecurityUtils.getSubject().getPrincipal(); //获取登录人员信息
        StudyVideoDO studyVideoDO =  studyVideoService.fetchByPk(Long.parseLong(id));
        int num = studyVideoService.removeByPk(Long.parseLong(id));
        if(num>0) {
            //操作日志
            StudyVideoLogDO studyVideoLogDO = new StudyVideoLogDO();
            studyVideoLogDO.setVideoName(studyVideoDO.getVideoName());
            studyVideoLogDO.setVideoFileType(studyVideoDO.getVideoFileType());
            studyVideoLogDO.setCompanyCode(studyVideoDO.getCompanyCode());
            studyVideoLogDO.setCreateBy(studyVideoDO.getCreateBy());
            studyVideoLogDO.setOperator(accountDO.getUsername());
            studyVideoLogDO.setOperatorMsg("删除");

            studyVideoLogService.create(studyVideoLogDO);

            return ReturnDataDO.returnSuccessJson("删除成功!");
        } else{
            return ReturnDataDO.returnErrorJson("删除失败!");
        }
    }

}
