package io.bloom.insurance.web.controller.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zfgc.base.api.*;
import cn.zfgc.base.api.time.constant.DateTimeFormat;
import cn.zfgc.base.model.Prop;
import io.bloom.insurance.web.mapper.*;
import io.bloom.insurance.web.util.PersonalCenterUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.zfgc.easyokhttp.response.HttpResponse;
import io.bloom.buava.lang.KitMap;
import io.bloom.buava.model.BTPage;
import io.bloom.insurance.web.model.*;
import io.bloom.insurance.web.model.commond.*;
import io.bloom.insurance.web.service.common.IMsgService;
import io.bloom.insurance.web.service.common.component.msg.Msg;
import io.bloom.insurance.web.service.common.component.msg.WxTemplateMsg;
import io.bloom.insurance.web.service.common.exception.DrpUserNotFoundException;
import io.bloom.insurance.web.service.drp.*;
import io.bloom.insurance.web.support.interceptor.Weixin;
import io.bloom.insurance.web.support.yatai.ReadSystemPropertiesUtil;
import io.bloom.insurance.web.util.Drps;
import io.bloom.insurance.web.util.PersonalCenterUtil;
import io.bloom.plugin.mybatis.SqlCriteria;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * MobilePersonalCenterController
 *
 * @author longhuashen
 * @since 16/4/15
 */
@Controller
@RequestMapping("/mobile")
public class MobilePersonalCenterController extends MobileBaseController {

    private Logger logger = LoggerFactory.getLogger(MobilePersonalCenterController.class);

    @Autowired
    private IDrpRecordSplitService drpSplitRecordService;

    @Autowired
    private IDrpUserService drpUserService;

    @Autowired
    private IDrpApproveWithdrawService drpApproveService;

    @Autowired
    private IDrpApproveWithdrawService drpApproveWithdrawService;

    @Autowired
    private IDrpMsgService drpMsgService;

    @Autowired
    private IMsgService msgService;

    @Autowired
    private IDrpItemService drpItemService;

    @Autowired
    private IDrpApproveUserService drpApproveUserService;

    @Autowired
    private  IMsgInfoService msgInfoService;

    @Autowired
    private IMsgReadService iMsgReadService;

    @Autowired
    private GgUserMapper ggUserMapper;

    @Autowired
    private MsgInfoMapper msgInfoMapper;

    @Autowired
    private EpuserMapingMapper epuserMapingMapper;

    @Autowired
    private DrpUserMapper drpUserMapper;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    @Autowired
    private FenXiaoUserMapper fenXiaoUserMapper;

    @Autowired
    private DrpApproveUserMapper drpApproveUserMapper;

    @Autowired
    private IStudyVideoService studyVideoService;

    @Value("${domain.site}")
    private String domain;

    @RequestMapping(value = "/personal-center", method = {RequestMethod.GET, RequestMethod.POST})
    @Weixin
    public String personalCenter(HttpServletRequest request, ModelMap modelMap)
    {
        String openid = getOpenid(request);
      /*  String openid = request.getParameter("openid");*/
        if("sales".equals(request.getParameter("debug")))
        {   openid = "openid1";}
        logger.info("openid is : {}", openid);
        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);
        logger.info("DrpUserDO is : {}", drpUserDO);
        int unReadNo=0;
        GgUser  ggUser=new GgUser();
        String orgCode="";
        String msgUserNum="";
        String date= DateTimeUtils.getCurrentDateWithFormatStr(DateTimeFormat.Format1);
        String CarCheckNo="";
        if(StringUtils.isNotBlank(date))
        {
            CarCheckNo=ggUserMapper.findCarCheckNoByDate(date);
            logger.info("CarCheckNo is : {}", CarCheckNo);
        }
        if(drpUserDO == null)
        {
            String remoteDomain = ReadSystemPropertiesUtil.getPropertiesValue("synchUserDomain");
            HttpResponse res = HttpClient.get((new StringBuilder()).append(remoteDomain).append("/fenxiao/page/remoteAgentInfo?openID=").append(openid).toString()).execute();
            String infoString = res.asString();
            logger.info("infoString is : {}", infoString);
            if(!StringUtils.isBlank(infoString) && !StringUtils.equals(infoString, "null"))
            {
                JSONObject jsonObject = JSON.parseObject(infoString);
                String headimgurl = request.getParameter("headimgurl");
                String name = jsonObject.getString("NAME");
                String workID = jsonObject.getString("WORKID");
                logger.info("name is : {}", name);
                logger.info("workID is : {}", workID);
                logger.info("headimgurl is : {}", headimgurl);
                modelMap.put("name", name);
                modelMap.put("workID", workID);
                modelMap.put("headimgurl", headimgurl);
                if(StringUtils.isNotBlank(workID)) {
                    ggUser=ggUserMapper.findUserByUserCode3(workID);
                    msgUserNum=msgInfoMapper.selectCount(workID);
                    if(StringUtils.isNotBlank(ggUser.getCompanycode()))
                    {
                        orgCode=ggUser.getCompanycode();
                    }
                    List<MsgInfoDo> msgOrgInfoDos = msgInfoService.getAllOrgSaleMsg(workID);
                    List<MsgInfoDo> allMasg=msgInfoService.getAllSaleMsg(workID);
                    unReadNo = iMsgReadService.getUreadNo(workID, allMasg);
                    iMsgReadService.saveUnread(workID, msgOrgInfoDos);
                }else{
                    unReadNo=0;
                }
                logger.info("orgCode is : {}", orgCode);
                logger.info("msgUserNum is : {}", msgUserNum);
                modelMap.put("orgCode",orgCode);
                modelMap.put("unReadNo",unReadNo);
                modelMap.put("msgUserNum",msgUserNum);
                modelMap.put("CarCheckNo",CarCheckNo);
                return "mobile/personal-center-agent-new";
            } else
            {
                return "redirect:/mobile/apply/sales?openID="+openid;
            }
        }
        DrpMsgCommand msgCommand = new DrpMsgCommand();
     /*   SqlCriteria criteria = msgCommand.apply1();
        criteria.add2Criteria("openid", openid);
        criteria.add2Criteria("bizState", "unread");
        criteria.add2Criteria("msgType", "WITHDRAW");*/
        /*modelMap.put("unreadCount", Integer.valueOf(drpMsgService.page(criteria).getTotal()));*/
        if(Drps.DRP_USER_LEVEL_SALES!= drpUserDO.getUserLevel())
        {
            String url = (new StringBuilder()).append(ReadSystemPropertiesUtil.getPropertiesValue("verifyAgent")).append("?openID=").append(openid).toString();
            logger.info("reqesturl is : {}", url);
            HttpResponse responsse = HttpClient.get(url).execute();
            String res = responsse.asString();
            logger.info("verify lower res is : {}", res);
            Map map = PersonalCenterUtil.doPersonalAgentReturn(res);
            logger.info("return map is : {}", map);
            String code = (String)map.get("code");
            String returnUrl = (String)map.get("url");
            String workID = (String)map.get("workID");
            String name=(String)map.get("name");
            if(code == "0")
            {
                ggUser=ggUserMapper.findUserByUserCode3(workID);
                msgUserNum=msgInfoMapper.selectCount(workID);
                if(StringUtils.isNotBlank(ggUser.getCompanycode()))
                {
                    orgCode=ggUser.getCompanycode();
                }
                logger.info("orgCode is : {}",orgCode);
                logger.info("msgUserNum is : {}", msgUserNum);
                modelMap.put("workID", workID);
                modelMap.put("openID", openid);
                modelMap.put("name", name);
                modelMap.put("orgCode",orgCode);
                modelMap.put("msgUserNum",msgUserNum);
                modelMap.put("CarCheckNo",CarCheckNo);
                return returnUrl;
            }
            if(code != "0")
            {  return (new StringBuilder()).append("redirect:").append(returnUrl).toString();}
        }
     /*   modelMap.put("orderCount", Integer.valueOf(drpSplitRecordService.countOrderByOpenIdAndBizStatus(openid, drpUserDO.getId(), "ok")));*/
        if(Drps.DRP_USER_LEVEL_AGENT== drpUserDO.getUserLevel())
        {
            modelMap.put("drpUser", drpUserDO);
            return "mobile/personal-center-agent";
        }
        if(Drps.DRP_USER_LEVEL_AGENT_2 == drpUserDO.getUserLevel())
        {
            modelMap.put("drpUser", drpUserDO);
            return "mobile/personal-center-agent2";
        } else
        {
            if(StringUtils.isNotBlank(drpUserDO.getUpSalesNo()))
            {
                List msgInfoDos = msgInfoService.getAllOrgSaleMsg(drpUserDO.getUpSalesNo());
                List<MsgInfoDo> allMasg=msgInfoService.getAllSaleMsg(drpUserDO.getUpSalesNo());
                unReadNo = iMsgReadService.getUreadNo(drpUserDO.getUpSalesNo(), allMasg);
                iMsgReadService.saveUnread(drpUserDO.getUpSalesNo(), msgInfoDos);
                ggUser=ggUserMapper.findUserByUserCode3(drpUserDO.getUpSalesNo());
                msgUserNum=msgInfoMapper.selectCount(drpUserDO.getUpSalesNo());
            } else
            {
                unReadNo = 0;
            }
            logger.info("msgUserNum is : {}", msgUserNum);
            modelMap.put("orgCode",ggUser.getCompanycode());
            modelMap.put("unReadNo",unReadNo);
            modelMap.put("drpUser", drpUserDO);
            modelMap.put("msgUserNum",msgUserNum);
            modelMap.put("CarCheckNo",CarCheckNo);
            return "mobile/personal-center-sales";
        }
    }
    /**
     * 我的订单
     *
     * @return
     */
    @RequestMapping(value = "/personal-order-list", method = RequestMethod.GET)
    @Weixin
    public String listPersonalOrderJson(HttpServletRequest request, ModelMap modelMap) {
        String openid = getOpenid(request);
        if ("sales".equals(request.getParameter("debug"))) {
            openid = "oi6I6txS8a9OjeC1iJcK5ZBDxXMc";
        }

        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);
        if (drpUserDO == null) {
            throw new DrpUserNotFoundException();
        }
        modelMap.put("drpUser", drpUserDO);
        return "mobile/order-list";
    }

    @RequestMapping(value = "/replaceOpenid")
    public String replaceOpenid(HttpServletRequest request)
    {
        String openid = request.getParameter("openid");
        String newOpenid=request.getParameter("newOpenid");
        String nickname=request.getParameter("nickname");
        String headimgurl=request.getParameter("headimgurl");
        logger.info("replaceOpenid openid is"+openid);
        logger.info("replaceOpenid newOpenid is"+newOpenid);
        logger.info("replaceOpenid nickname is"+nickname);
        if(StringUtils.isNotBlank(nickname))
        {
            nickname=HttpUtil.urlEncode(nickname);
        }

        if(StringUtils.isNotBlank(openid)&& StringUtils.isNotBlank(newOpenid)
           &&!openid.equals("null") &&!newOpenid.equals("null"))
        {
            DrpUserDO drpUserDO=drpUserMapper.getByOpenId(openid);
            WxUserInfoDO wxUserInfoDO=wxUserInfoMapper.getByOpenId(openid);
            FenXiaoUserDo fenXiaoUserDo=fenXiaoUserMapper.getByOpenId(openid);
            WxUserInfoDO fxUserInfo=wxUserInfoMapper.getFxUserInfoByOpenId(openid);
            DrpApproveUserDO drpApproveUserDO=drpApproveUserMapper.getByOpenId(openid);
            String update=DateTimeUtils.getCurrentDateWithFormatStr(DateTimeFormat.Format2);
            if(ObjectUtils.isNotNull(drpUserDO))
            {
                drpUserDO.setOpenid(newOpenid);
                drpUserDO.setUsedopenid(openid);
                drpUserMapper.updateByPk(drpUserDO);
            }
            if(ObjectUtils.isNotNull(wxUserInfoDO))
            {
                String id =String.valueOf(wxUserInfoDO.getId());
                wxUserInfoMapper.updateOpenid(newOpenid,openid,id);
            }
            if(ObjectUtils.isNotNull(fenXiaoUserDo))
            {
                String id =String.valueOf(fenXiaoUserDo.getId());
                fenXiaoUserMapper.updateOpenid(newOpenid,update,openid,id);
            }
            if(ObjectUtils.isNotNull(fxUserInfo))
            {
                String id =String.valueOf(fxUserInfo.getId());
                wxUserInfoMapper.updateFXOpenid(newOpenid,update,openid,id);
            }
            if(ObjectUtils.isNotNull(drpApproveUserDO))
            {
                String id =String.valueOf(drpApproveUserDO.getId());
                drpApproveUserMapper.updateByPk(drpApproveUserDO);
            }
            return "redirect:/mobile/personal-center?openid="+newOpenid+"&nickname="+nickname+"&headimgurl="+headimgurl;
        }
        if(StringUtils.isNotBlank(openid))
        {
            DrpUserDO drpUserDO=drpUserMapper.getByOpenId(openid);
            //为空则获取旧的openid
            if(ObjectUtils.isNull(drpUserDO))
            {
                Prop p = PropKit.use("application.properties");
                Prop p1 = PropKit.use("system.properties");
                String redUrl=p.get("ytcxCompanyUrl")+"&callbackUrl="+p.get("synchUserDomain")+"&openid="+openid+"&nickname="+nickname+"&headimgurl="+headimgurl;
                logger.info("replaceOpenid redUrl is"+redUrl);
                return "redirect: "+redUrl;
            }else{
                return "redirect:/mobile/personal-center?openid="+openid+"&nickname="+nickname+"&headimgurl="+headimgurl;
            }
        }
        return "redirect:/";
    }
    /**
     * 我的订单数据
     *
     * @param drpRecordSplitCommand
     * @return
     */
    @RequestMapping(value = "/personal-order-list.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BTPage<DrpRecordSplitDO> listPersonalOrderJson(DrpRecordSplitCommand drpRecordSplitCommand) {
        // return drpSplitRecordService.page(drpRecordSplitCommand.apply());
        // 除了openid之外，还要根据shopid做判断，目前shopid==userid
        // 由于有退保存在，只查状态为ok的
        drpRecordSplitCommand.setSplitState(Drps.BIZ_STATE_OK);
        return drpSplitRecordService.page(drpRecordSplitCommand.apply2());
    }

    /**
     * 转到消息中心页面
     *
     * @return
     */
    @RequestMapping(value = "/personal-message", method = RequestMethod.GET)
    @Weixin
    public String message(HttpServletRequest request, ModelMap modelMap) {
        String openid = getOpenid(request);
        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);

        if (drpUserDO == null) {
            throw new DrpUserNotFoundException();
        }

        modelMap.put("drpUser", drpUserDO);
        return "mobile/message";
    }

    @RequestMapping(value = "/personal-message-list.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BTPage<DrpMsgDO> messageListJson(DrpMsgCommand msgCommand) {
        SqlCriteria criteria = msgCommand.apply();
        criteria.add2Criteria("bizState", "unread");
        return this.drpMsgService.page(criteria);
//        return drpMsgService.page(msgCommand.apply());
    }

    /**
     * 进入分销人员管理界面
     *
     * @return
     */
    @RequestMapping(value = "/personal-agent-user", method = {RequestMethod.GET, RequestMethod.POST})
    @Weixin
    public String drpAgentUser(HttpServletRequest request, ModelMap modelMap) {
        String openid = getOpenid(request);
        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);

        if (drpUserDO == null) {
            throw new DrpUserNotFoundException();
        }

        if (drpUserDO.getUserLevel() == Drps.DRP_USER_LEVEL_AGENT_2) {
            throw new DrpUserNotFoundException("该用户是2级代理！");
        }
        modelMap.put("drpUser", drpUserDO);
        return "mobile/personal-agent-user";
    }

    /**
     * 列出分销人员
     *
     * @return
     */
    @RequestMapping(value = "/agent-user-list.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BTPage<DrpUserDO> listDrpAgentUserJson(DrpUserCommand command) {
        return drpUserService.page(command.apply());
    }

    /**
     * 转到分销销售页面
     *
     * @return
     */
    @RequestMapping(value = "/distribution-order-list", method = RequestMethod.GET)
    @Weixin
    public String distributionOrderListJson(HttpServletRequest request, ModelMap modelMap) {
        String openid = getOpenid(request);
        if ("sales".equals(request.getParameter("debug"))) {
            openid = "oi6I6txS8a9OjeC1iJcK5ZBDxXMc";
        }

        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);

        if (drpUserDO == null) {
            throw new DrpUserNotFoundException();
        }

        List<DrpUserDO> drpUserDOList = drpUserService
                .listBy(new SqlCriteria().add2Criteria("upperUserId", drpUserDO.getId()));

        List<Long> list = new ArrayList<>();
        if (drpUserDOList != null && drpUserDOList.size() > 0) {
            for (DrpUserDO user : drpUserDOList) {
                list.add(user.getId());
            }
        } else {
            list.add(0l);
        }
        modelMap.put("splitUserIds", list);
        return "mobile/distribution-order-list";
    }

    /**
     * 分销订单数据
     *
     * @param drpRecordSplitCommand
     * @return
     */
    @RequestMapping(value = "/distribution-order-list.json", method = RequestMethod.POST)
    @ResponseBody
    public BTPage<DrpRecordSplitDO> distributionOrderListJson(DrpRecordSplitCommand drpRecordSplitCommand,
                                                              @RequestParam("splitUserIds[]") Integer[] splitUserIds) {
        SqlCriteria sqlCriteria = drpRecordSplitCommand.apply();
        sqlCriteria.add2Criteria("splitUserIds", splitUserIds);
        // 由于有退保存在，只查状态为ok的
        sqlCriteria.add2Criteria("splitState", Drps.BIZ_STATE_OK);
        return drpSplitRecordService.page(sqlCriteria);
    }

    @RequestMapping(value = "/change-commission-percent.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public KitMap changeCommissionPercent(@RequestParam("drpItemId") Long drpItemId,
                                          @RequestParam("percent") int percent) {
        DrpItemDO drpItemDO = drpItemService.fetchByPk(drpItemId);
        if (drpItemId == null) {
            throw new RuntimeException("要修改的分销商品不存在！");
        }

        if (percent < 0 || percent > 100) {
            return KitMap.fail("填写的分销比例有错！");
        }
        try {
            drpItemDO.setSplitRate(percent);
            return KitMap.data(drpItemService.updateByPk(drpItemDO));
        } catch (Exception e) {
            logger.error("修改提成比例成功出错:e:{}", e);
            return KitMap.fail("服务器出错:" + e.getMessage());
        }
    }

    /**
     * 删除代理人员
     *
     * @return
     */
    @RequestMapping(value = "/del-agent-user.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @Weixin
    public KitMap delAgentUser(@CookieValue("openid") String openid, @RequestParam("id") Long id) {
        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);

        if (drpUserDO == null) {
            throw new DrpUserNotFoundException("用户不存在！");
        }

        try {
            // 业务员删除下级代理
            if (drpUserDO.getUserLevel() == Drps.DRP_USER_LEVEL_SALES) {
                return KitMap.data(drpUserService.deleteAgent(id));
            }
            // 一级代理删除下级代理
            else {
                DrpUserDO lowerDrpUser = drpUserService.fetchByPk(id);
                if (lowerDrpUser == null) {
                    return KitMap.fail("该下级代理不存在！");
                }
                drpApproveUserService.deleteByOpenId(lowerDrpUser.getOpenid());// 删除申请记录

                int num = drpUserService.removeByPk(id);
                if (num > 0) {
                    // 发送通知(通知下级代理)
                    sendMessage(lowerDrpUser, "上级代理" + drpUserDO.getName() + "与你解除了分销关系！", domain);
                }
                return KitMap.data(num);
            }
        } catch (Exception e) {
            logger.error("删除代理出错:e:{}\n, openId:{}\n", e, openid);
            return KitMap.fail("服务器出错:" + e.getMessage());
        }
    }

    /**
     * 发送消息
     *
     * @param lowerDrpUser
     * @param nameValue
     * @param domain
     */
    private void sendMessage(DrpUserDO lowerDrpUser, String nameValue, String domain) {
        if (lowerDrpUser == null) {
            return;
        }

        Msg msg = new WxTemplateMsg("解除分销", nameValue, DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), "解除分销成功！",
                domain);
        msgService.send(lowerDrpUser, msg, "weixin");
    }

    /**
     * 转到提现页面
     *
     * @return
     */
    @RequestMapping(value = "/personal-withdraw", method = RequestMethod.GET)
    @Weixin
    public String withDraw(HttpServletRequest request, ModelMap modelMap) {
        String openId = getOpenid(request);
        // 手动插入openId调试
        if (!StringUtils.isEmpty(request.getParameter("debug"))) {
            System.out.println("debug start");
            openId = request.getParameter("openid");
            System.out.println("open id is : " + openId);
        }

        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openId);
        if (drpUserDO == null) {
            System.out.println("can not find drp user by id : " + openId);
            throw new DrpUserNotFoundException();
        }
        modelMap.put("drpUser", drpUserDO);
        return "mobile/withdraw";
    }

    /**
     * 提现历史记录
     *
     * @param command
     * @return
     */
    @RequestMapping(value = "/approve-withdraw-list.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BTPage<DrpApproveWithdrawDO> withDrawListJson(ApproveUserCommand command) {
        return drpApproveService.page(command.apply());
    }

    /**
     * 申请提现
     *
     * @return
     */
    @RequestMapping(value = "/apply-withdraw.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @Weixin
    public KitMap applyWithDraw(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "money", defaultValue = "0") Double money) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        final int cent = Drps.yuan2cent(money);
        String openId = getOpenid(request);
        // 手动插入openId调试
        if (!StringUtils.isEmpty(request.getParameter("debug"))) {
            System.out.println("debug start");
            openId = request.getParameter("openid");
            System.out.println("open id is : " + openId);
        }
        if (money < 1) {
            return KitMap.result(false, "提现金额填写有误！");
        }

        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openId);
        if (cent > drpUserDO.getBalanceAmount()) {
            return KitMap.result(false, "提现金额不能大于余额！");
        }

        drpApproveWithdrawService.applyAndFrozenBalance(drpUserDO, cent);

        DrpUserDO upper = drpUserService.getUpperByOpenid(drpUserDO.getOpenid());

        // 发送模板消息通知上级
        Msg msg = new WxTemplateMsg("积分兑换申请", "[" + drpUserDO.getName() + "]申请积分兑换",
                DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), "申请积分兑换", this.domain + "/mobile/personal-message");
        this.msgService.send(upper, msg, new String[]{"weixin"});
        return KitMap.result(true, "发送申请成功！");

//        Msg msg = new WxTemplateMsg("申请提现", "[" + drpUserDO.getName() + "]申请提现",
//                DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), "申请提现", domain + "/mobile/personal-message");
//        msgService.send(upper, msg, "weixin");

//        return KitMap.result(true, "发送提现申请成功！");
    }

    @RequestMapping(value = "/personal-withdraw-detail", method = RequestMethod.GET)
    public String withDrawDetail(@RequestParam("id") Long id, ModelMap modelMap) {
        DrpApproveWithdrawDO drpApproveWithdrawDO = drpApproveWithdrawService.fetchByPk(id);
        modelMap.put("withDraw", drpApproveWithdrawDO);
        return "mobile/withdraw-detail";
    }

    @RequestMapping(value = "/personal-withdraw-cancel.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public KitMap cancelWithdraw(@RequestParam("id") Long id) {
        try {
            drpApproveWithdrawService.cancel(id);
            return KitMap.succ("撤销成功!");
        } catch (Exception e) {
            logger.error("提现撤销失败！", e);
            return KitMap.fail(e.getMessage());
        }
    }

    /**
     * 转到下级代理商品页面
     *
     * @return
     */
    @RequestMapping(value = "/personal-agent-items", method = RequestMethod.GET)
    @Weixin
    public String listAgentItems(@RequestParam("id") Long id, ModelMap modelMap) {
        DrpUserDO drpUserDO = drpUserService.fetchByPk(id);
        if (drpUserDO == null) {
            throw new DrpUserNotFoundException("下级代理不存在！");
        }
        modelMap.put("drpUser", drpUserDO);
        return "mobile/personal-agent-items";
    }

    /**
     * 根据下级代理列出其代理商品
     *
     * @return
     */
    @RequestMapping(value = "/list-agent-items.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BTPage<DrpItemDO> listAgentItems(DrpItemCommand command) {
        return drpItemService.page(command.apply());
    }

    /**
     * 退出分销
     *
     * @return
     */
    @RequestMapping(value = "/personal-disassociate.json", method = RequestMethod.GET)
    @ResponseBody
    @Weixin
    public KitMap disassociate(HttpServletRequest request) {
        String openid = getOpenid(request);
        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);
        if (drpUserDO == null) {
            return KitMap.result(false, "用户不存在！");
        }
        if (drpUserDO.getBalanceAmount() > 0) {// 提醒提现
            return KitMap.result(false, "还有余额，请先进行提现！");
        }
        int num;
        // 二级代理退出分销
        if (drpUserDO.getUserLevel() > Drps.DRP_USER_LEVEL_AGENT) {
            // 删除申请记录
            drpApproveUserService.deleteByOpenId(openid);
            num = drpUserService.removeByPk(drpUserDO.getId());
        }
        // 一级代理退出分销
        else {
            num = drpUserService.agentQuit(drpUserDO.getId());
        }

        if (num > 0) {
            // 发送通知(通知上级业务员)
            sendMessage(drpUserService.getUpperByOpenid(openid)// 发送给谁
                    , "[" + drpUserDO.getName() + "]解除了分销关系！"// 内容
                    , domain// url
            );
        }

        return KitMap.result(true, "解除分销关系成功！");
    }

    /**
     * 提现审批记录
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/personal-withdraw-list", method = RequestMethod.GET)
    @Weixin
    public String personalWithdrawList(HttpServletRequest request, ModelMap modelMap) {
        String openid = getOpenid(request);
        String msgId = request.getParameter("msgId");
        DrpMsgDO msgDO = null;
        if ((msgId != null) && (msgId.trim() != "")) {
            Long mID = Long.valueOf(request.getParameter("msgId"));
            msgDO = (DrpMsgDO) this.drpMsgService.fetchByPk(mID);
        }
        DrpUserDO drpUserDO = this.drpUserService.fetchByOpenid(openid);
        if (drpUserDO == null) {
            throw new DrpUserNotFoundException("用户不存在！");
        }
        if (msgDO == null) {
            throw new RuntimeException("消息不存在！");
        }
        modelMap.put("drpUser", drpUserDO);
        modelMap.put("msgDO", msgDO);
        return "mobile/personal-withdraw-list";

//
//        String openid = getOpenid(request);
//        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);
//        if (drpUserDO == null) {
//            throw new DrpUserNotFoundException("用户不存在！");
//        }
//        modelMap.put("drpUser", drpUserDO);
//        return "mobile/personal-withdraw-list";
    }

    @RequestMapping(value = "/personal-withdraw-list.json", method = RequestMethod.GET)
    @ResponseBody
    public BTPage<DrpApproveWithdrawDO> personalWithdrawList(ApproveUserCommand command) {

        SqlCriteria sqlCriteria = command.apply();
        sqlCriteria.add2Criteria("bizState", "apply");
        if ((command.getMsgUUID() != null) && (command.getMsgUUID().trim() != "")) {
            sqlCriteria.add2Criteria("msgId", command.getMsgUUID());
        }
        return this.drpApproveWithdrawService.page(sqlCriteria);
//        SqlCriteria sqlCriteria = command.apply();
//        sqlCriteria.add2Criteria("bizState", Drps.BIZ_STATE_APPLY);
//        return drpApproveWithdrawService.page(sqlCriteria);
    }

    @RequestMapping(value = "/personal-withdraw-pass.json", method = RequestMethod.POST)
    @ResponseBody
    @Weixin
    public KitMap personalWithDrawPass(@RequestParam("id") long id, @CookieValue("openid") String openid,
                                       HttpServletRequest request) {
        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);
        if (drpUserDO == null) {
            return KitMap.fail("审核用户不存在！");
        }

        try {
            drpApproveService.pass(id, drpUserDO.getName(), "审核通过", null);

            return KitMap.succ("操作成功");
        } catch (Exception e) {
            logger.error("审核提现通过操作失败:e:{}", e);
            return KitMap.fail("服务器出错:" + e.getMessage());
        }
    }

    @RequestMapping(value = "/personal-withdraw-deny.json", method = RequestMethod.POST)
    @ResponseBody
    @Weixin
    public KitMap personalWithDrawDeny(@RequestParam("id") long id, @CookieValue("openid") String openid,
                                       HttpServletRequest request) {
        DrpUserDO drpUserDO = drpUserService.fetchByOpenid(openid);
        if (drpUserDO == null) {
            return KitMap.fail("审核用户不存在！");
        }
        try {
            drpApproveService.deny(id, drpUserDO.getName(), "审核拒绝");

            return KitMap.succ("操作成功");
        } catch (Exception e) {
            logger.error("审核提现拒绝操作失败:e:{}", e);
            return KitMap.fail("服务器出错:" + e.getMessage());
        }
    }


    /**
     * 增加一个方法，用于查询商机的分销比例
     * add by nixiaobing
     */
    @RequestMapping(value = "/split-upper-splits.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<DrpItemDO> listUpperUserItems(String userId) {
        List<DrpItemDO> list = Collections.EMPTY_LIST;

        //根据openId获取用户
        DrpUserDO user = drpUserService.fetchByIdNo(userId);

        //如果不是sale
        if (user.getUserLevel() != Drps.DRP_USER_LEVEL_SALES) {
            long upperUserId = user.getUpperUserId();
            SqlCriteria criteria = new SqlCriteria();
            criteria.add2Criteria("userId", upperUserId);
            return drpItemService.listBy(criteria);

        }
        return list;
    }

    /**
     * 查询是否是签单吧用户
     * @param workID
     * @return
     */
    @RequestMapping(value = "/isFHFlag.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public boolean isFHFlag(String workID) {
        EpuserMapingDO epuserMapingDO = epuserMapingMapper.selectByUcodeStatus(workID);
        if(epuserMapingDO != null){
            return true;
        }
        return false;
    }


    /**
     * 学习专区视频列表
     * @return
     */
    @RequestMapping(value = "/study/videolist", method = RequestMethod.GET)
    @Weixin
    public String videoList() {
        return "mobile/study-video-list";
    }

    /**
     * 学习专区查看视频
     * @param id
     * @return
     */
    @RequestMapping(value = "/study/videopreview/{id}", method = RequestMethod.GET)
    @Weixin
    public ModelAndView videoPreview(@PathVariable("id") String id) {
        ModelAndView view = new ModelAndView("mobile/study-video-preview");
        return view;
    }

    /**
     * 学习专区查看预览图片
     * @param id
     * @return
     */
    @RequestMapping(value = "/study/imagepreview/{id}", method = RequestMethod.GET)
    @Weixin
    public ModelAndView imagePreview(@PathVariable("id") String id) {
        ModelAndView view = new ModelAndView("mobile/study-image-preview");
        view.addObject("id",id);
        return view;
    }

    /**
     * 生成视频文件，用于学习专区播放视频
     * @param id  视频ID
     * @param response
     */
    @RequestMapping(value="/study/video/{id}",method=RequestMethod.GET)
    public void getVideo(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

        StudyVideoDO studyVideoDO = studyVideoService.fetchByPk(Long.parseLong(id));
        byte[] data  = studyVideoDO.getVideoContent();

        String diskfilename = studyVideoDO.getVideoName() + getSuffixName(studyVideoDO.getVideoFileType());
        response.setContentType(studyVideoDO.getVideoFileType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"" );
        response.setContentLength(data.length);
        byte[] content = new byte[1024];
        BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
        OutputStream os = response.getOutputStream();
        while (is.read(content) != -1) {
            os.write(content);
        }
        is.close();
        os.close();
    }

    /**
     * 生成视频预览图片文件，用于学习专区视频列表
     * @param id  视频ID
     * @param response
     */
    @RequestMapping(value="/study/video/image/{id}",method=RequestMethod.GET)
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

        StudyVideoDO studyVideoDO = studyVideoService.fetchByPk(Long.parseLong(id));
        byte[] data  = studyVideoDO.getImageContent();

        String diskfilename = studyVideoDO.getVideoName() + getSuffixName(studyVideoDO.getImageFileType());
        response.setContentType(studyVideoDO.getImageFileType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"" );
        response.setContentLength(data.length);
        byte[] content = new byte[1024];
        BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
        OutputStream os = response.getOutputStream();
        while (is.read(content) != -1) {
            os.write(content);
        }
        is.close();
        os.close();
    }

    private  String getSuffixName(String fileType) {
        String suffixName = fileType;
        switch(fileType){
            case "video/x-flv":
                suffixName = ".flv";
                break;
            case "video/mp4":
                suffixName = ".mp4";
                break;
            case "video/3gpp":
                suffixName = ".3gp";
                break;
            case "video/quicktime":
                suffixName = ".mov";
                break;
            case "video/x-msvideo":
                suffixName = ".avi";
                break;
            case "video/x-ms-wmv":
                suffixName = ".wmv";
                break;
            case "image/jpeg":
                suffixName = ".jpg";
                break;
            case "image/png":
                suffixName = ".png";
                break;
            case "image/bmp":
                suffixName = ".bmp";
                break;
            case "image/gif":
                suffixName = ".gif";
                break;
        }

        return suffixName;
    }
}
