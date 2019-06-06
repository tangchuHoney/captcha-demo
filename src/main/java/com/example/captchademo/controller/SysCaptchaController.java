package com.example.captchademo.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.IOException;

/**
 * 图片验证码（支持算术形式）
 */
@Controller
@Api(value = "SysCaptchaController", tags = "图形验证码控制器")
@RequestMapping("/captcha")
public class SysCaptchaController {
//    @Resource(name = "captchaProducer")
//    private Producer captchaProducer;
//
//    @Resource(name = "captchaProducerMath")
//    private Producer captchaProducerMath;

//    /**
//     * 验证码生成
//     */
//    @ApiOperation(value = "验证码生成")
//    @ApiImplicitParam(name = "type", value = "验证码类型  math为数字类型图片   char为string类型图片")
//    @GetMapping(value = "/captchaImage")
//    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("type") String type) {
//        ServletOutputStream out = null;
//        try {
//            HttpSession session = request.getSession();
//            response.setDateHeader("Expires", 0);
//            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//            response.setHeader("Pragma", "no-cache");
//            response.setContentType("image/jpeg");
//
//
//            String capStr = null;
//            String code = null;
//            BufferedImage bi = null;
//            if ("math".equals(type)) {
//                String capText = captchaProducerMath.createText();
//                capStr = capText.substring(0, capText.lastIndexOf("@"));
//                code = capText.substring(capText.lastIndexOf("@") + 1);
//                bi = captchaProducerMath.createImage(capStr);
//            } else if ("char".equals(type)) {
//                capStr = code = captchaProducer.createText();
//                bi = captchaProducer.createImage(capStr);
//            }
//            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, code);
//            out = response.getOutputStream();
//            ImageIO.write(bi, "jpg", out);
//            out.flush();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    @ApiOperation(value = "验证码校验")
//    @ApiImplicitParam(name = "code", value = "验证码")
//    @GetMapping(value = "/verifyCaptchaImage")
//    @ResponseBody
//    public Object verifyCaptchaImage(HttpServletRequest request, @RequestParam("code") String webCode) {
//        HttpSession session = request.getSession();
//        String code = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        if (null == code) {
//            return JSON.toJSONString("验证码不正确");
//        }
//        if (code.equals(webCode)) {
//            return JSON.toJSONString("验证码正确");
//        }
//        return JSON.toJSONString("未知错误");
//    }

    //=======================上面方法太复杂，繁琐 已废弃=========================
    @ApiOperation(value = "生成带有线段干扰的图形验证码")
    @ApiImplicitParam(name = "type",value = "line:线段干扰 cicle:圆圈干扰 shear:扭曲干扰")
    @GetMapping(value = "/crCaptchaImage")
    public ModelAndView crCaptchaImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("type") String type) {
        //生成图片
        LineCaptcha lineCaptcha = null;
        CircleCaptcha circleCaptcha = null;
        ShearCaptcha shearCaptcha = null;
        String code = null;
        if (type.equals("line")) {
            //生成线段干扰图形验证码
            lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        }
        if (type.equals("cicle")) {
            //生成圆圈干扰图形验证码
            //定义图形验证码的长、宽、验证码字符数、干扰元素个数
            circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        }
        if (type.equals("shear")) {
            //生成扭曲干扰图形验证码
            shearCaptcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        }
        //获取图片流
        BufferedImage image=null;
        if (lineCaptcha != null) {
            code = lineCaptcha.getCode();
            image = lineCaptcha.getImage();
        }
        if (circleCaptcha != null) {
            code = circleCaptcha.getCode();
            image = circleCaptcha.getImage();
        }
        if (shearCaptcha != null) {
            code = shearCaptcha.getCode();
            image = shearCaptcha.getImage();
        }

        ServletOutputStream out = null;
        try {
            HttpSession session = request.getSession();
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/png");
            //为了测试方便  放到了Attribute里面  实际code可以存在redis里面
            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, code);
            out = response.getOutputStream();
            ImageIO.write(image, "png", out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @ApiOperation(value = "验证动态图片的code")
    @ApiImplicitParam(name = "code", value = "验证码")
    @GetMapping(value = "/verCaptchaImageCode")
    @ResponseBody
    public Object verCaptchaImageCode(@RequestParam("code") String webCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (null == code) {
            return JSON.toJSONString("验证码不正确");
        }
        if (code.equals(webCode)) {
            return JSON.toJSONString("验证码正确");
        }
        return JSON.toJSONString("未知错误");
    }
}