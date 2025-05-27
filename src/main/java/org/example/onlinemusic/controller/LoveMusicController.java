package org.example.onlinemusic.controller;

import org.example.onlinemusic.mapper.LoveMusicMapper;
import org.example.onlinemusic.model.Music;
import org.example.onlinemusic.model.User;
import org.example.onlinemusic.tools.Constant;
import org.example.onlinemusic.tools.ResponseBodyMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/lovemusic")
public class LoveMusicController {
    @Resource
    private LoveMusicMapper loveMusicMapper;

    /**
     * 收藏音乐
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/likeMusic")
    public ResponseBodyMessage<Boolean> likeMusic(@RequestParam String id, HttpServletRequest request) {

        int musicId = Integer.parseInt(id);
        System.out.println("musicId："+musicId);
        //1、检查是否登录了
        HttpSession httpSession = request.getSession(false);
        if(httpSession == null || httpSession.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            System.out.println("没有登录！");
            return new ResponseBodyMessage<>(-1,"请登录后上传",false);
        }

        User user = (User)httpSession.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        System.out.println("userId："+userId);

        Music music = loveMusicMapper.findLoveMusicByMusicIdAndUserId(userId,musicId);
        if(music != null) {
            //之前收藏过，不能进行收藏了!
            return new ResponseBodyMessage<>(-1,"您之前收藏过这个音乐",false);
        }

        // 没有收藏过 需要进行收藏
        boolean effect = loveMusicMapper.insertLoveMusic(userId,musicId);
        if(effect) {
            return new ResponseBodyMessage<>(0,"收藏成功！",true);
        }else {
            return new ResponseBodyMessage<>(0,"收藏失败！",true);
        }
    }

    @RequestMapping("/findlovemusic")
    public ResponseBodyMessage<List<Music>> findLoveMusic(@RequestParam(required = false) String musicName,
                                                          HttpServletRequest  request) {
        //1、检查是否登录了
        HttpSession httpSession = request.getSession(false);
        if(httpSession == null || httpSession.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            System.out.println("没有登录！");
            return new ResponseBodyMessage<>(-1,"请登录后查找",null);
        }

        //  2.根据session获取用户的相关信息
        User user = (User)httpSession.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        System.out.println("userId："+userId);

        List<Music> musicList = null;
        if(musicName == null) {// 返回userid对应的所有收藏的音乐
            musicList = loveMusicMapper.findLoveMusicByUserId(userId);
        }else {// 模糊查询含有musicName关键词的收藏音乐列表
            musicList = loveMusicMapper.findLoveMusicBykeyAndUID(musicName,userId);
        }
        return new ResponseBodyMessage<>(0,"查询到了所有的歌曲信息",musicList);
    }


    /**
     * 删除收藏音乐
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/deletelovemusic")
    public ResponseBodyMessage<Boolean> deleteLoveMusic(@RequestParam String id,HttpServletRequest request) {
        int musicId = Integer.parseInt(id);

        // 从session从获取用户信息
        HttpSession httpSession = request.getSession(false);
        if(httpSession == null || httpSession.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            System.out.println("没有登录！");
            return new ResponseBodyMessage<>(-1,"请登录后移除",null);
        }

        User user = (User)httpSession.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();

        // 从数据库中删除对应的信息
        int ret = loveMusicMapper.deleteLoveMusic(userId,musicId);

        if(ret == 1) {
            return new ResponseBodyMessage<>(0,"取消收藏成功！",true);
        }else {
            return new ResponseBodyMessage<>(-1,"取消收藏失败！",false);
        }
    }
}
