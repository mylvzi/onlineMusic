package org.example.onlinemusic.controller;

import org.apache.ibatis.binding.BindingException;
import org.example.onlinemusic.mapper.LoveMusicMapper;
import org.example.onlinemusic.mapper.MusicMapper;
import org.example.onlinemusic.model.Music;
import org.example.onlinemusic.model.User;
import org.example.onlinemusic.tools.Constant;
import org.example.onlinemusic.tools.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {
    @Value("${music.local.path}")
    private String SAVE_PATH/* = "D:/music1/"*/;

    @Autowired
    private MusicMapper musicMapper;

    @Resource
    private LoveMusicMapper loveMusicMapper;

    /**
     * 上传音乐
     * @param singer
     * @param file
     * @param request
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> insertMusic(@RequestParam String singer,
                                                    @RequestParam("filename") MultipartFile file,
                                                    HttpServletRequest request,
                                                    HttpServletResponse resp) throws IOException {
        System.out.println("==========");
        //1、检查是否登录了  检察服务端是否含有状态信息(通过session机制判断即可)  不登录无法上传文件
        HttpSession httpSession = request.getSession(false);
        if(httpSession == null || httpSession.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            System.out.println("没有登录！");
            return new ResponseBodyMessage<>(-1,"请登录后上传",false);
        }

        // ADD:判断上传的文件是否是MP3
        InputStream inputStream = file.getInputStream();
        byte[] header = new byte[3];
        int n = inputStream.read(header);
        // 打印每个字节的十六进制值和字符
        // System.out.print("前3字节内容：");
        for (byte b : header) {
            System.out.printf("0x%02X(%c) ", b, (b >= 32 && b <= 126) ? (char) b : '.');
        }
        if(header[0] != 'I' || header[1] != 'D' || header[2] != '3')
            return new ResponseBodyMessage<>(-1, "不能上传非MP3文件， 请重新上传", false);

        String fileNameAndType = file.getOriginalFilename();//xxx.mp3
        System.out.println("fileNameAndType: "+fileNameAndType);

        int index = fileNameAndType.lastIndexOf(".");
        String title = fileNameAndType.substring(0,index);
        //2、先查询数据库当中 是否有当前音乐【歌曲名+歌手 完全相同】
        int cnt = musicMapper.existsByTitleAndSinger(title,singer);
        if(cnt > 0) {
            return new ResponseBodyMessage<>(-1, "不能上传相同的曲目，请重新上传",false);
        }

        //2、上传到了服务器  文件使用户post的  从提交的参数中获取

        // 完整的文件路径
        String path = SAVE_PATH +"/"+fileNameAndType;

        File dest = new File(path);
        if(!dest.exists()) {
            dest.mkdir();
        }

        try {
            file.transferTo(dest);// 保存到本地磁盘  将用户提交的音乐保存到服务器
            System.out.println("服务器上传成功！");
            // return new ResponseBodyMessage<>(0,"服务器上传成功！",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseBodyMessage<>(-1,"服务器上传失败！",false);
        }
        // return new ResponseBodyMessage<>(-1,"上传失败！",false);

        //进行数据库的上传
        //1、准备数据   2、调用insert
        // title在上方
        // System.out.println(title);

        // 用户的信息保存到session之中  根据session标识符获取用户信息
        User user = (User)httpSession.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userid = user.getId();

        //1、播放音乐-》http请求   存进去的时候，没有加后缀.mp3
        String url = "/music/get?path="+title;

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sf.format(new Date());

        try {
            int ret = 0;
            ret = musicMapper.insert(title,singer,time,url,userid);// 成功插入的数据个数
            if(ret == 1) {
                // 音乐上传成功  跳转到音乐界面
                resp.sendRedirect("/list.html");
                return new ResponseBodyMessage<>(0,"数据库上传成功！",true);
            }else {
                return new ResponseBodyMessage<>(-1,"数据库上传失败！",false);
            }
        }catch (BindingException e) {
            dest.delete();// 数据库上传失败  对应的服务器上传的音乐也要被删除
            return new ResponseBodyMessage<>(-1,"数据库上传失败！",false);
        }
        //另外的一个问题：  如果重复上传一首歌曲，能否上传成功？？ 可以
    }


    /**
     * 播放音乐的时候：/music/get?path=xxx.mp3
     * @param path
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(String path) {
        File file = new File(SAVE_PATH+"/"+path);// 本地文件路径
        byte[] a = null;
        try {
            a = Files.readAllBytes(file.toPath());// 将MP3文件读取为字节流文件
            if(a == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(a);// 获取成功
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
        //return ResponseEntity.internalServerError().build();
        //return ResponseEntity.notFound().build();
    }


    /**
     * 删除单个音乐
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> deleteMusicById(@RequestParam String id) {
        //1、先检查这个音乐是不是存在的？
        int iid = Integer.parseInt(id);

        //2、如果存在要进行删除
        Music music = musicMapper.findMusicById(iid);
        if(music == null) {
            System.out.println("没有这个id的音乐");
            return new ResponseBodyMessage<>(-1,"没有你要删除的音乐",false);
        }else {
            //2.1 删除数据库
            int ret = musicMapper.deleteMusicById(iid);
            if(ret == 1) {
                //2.2 删除服务器上的数据
                int index = music.getUrl().lastIndexOf("=");
                String fileName = music.getUrl().substring(index+1);// 从url从获取文件名称

                File file = new File(SAVE_PATH+"/"+fileName+".mp3");// 拼接文件路径  在本地服务器的具体路径
                System.out.println("当前的路径："+file.getPath());

                if(file.delete()) {
                    //同步删除lovemusic表当中的这个音乐
                    loveMusicMapper.deleteLoveMusicByMusicId(iid);
                    return new ResponseBodyMessage<>(0,"服务器当中的音乐删除成功！",true);
                }else {
                    return new ResponseBodyMessage<>(-1,"服务器当中的音乐删除失败！",false);
                }
            }else {
                // 删除失败  返回值是0
                return new ResponseBodyMessage<>(-1,"数据库当中的音乐没有删除成功！",false);
            }
        }
    }

    /**
     * 批量进行删除
     * @param id 【1,3,5,7,9】
     * @return
     */
    @RequestMapping("/deleteSel")
    public ResponseBodyMessage<Boolean> deleteSelMusic(@RequestParam("id[]") List<Integer> id) {
        System.out.println("所有的ID ： "+ id);
        int sum = 0;

        // 遍历所有的id
        for (int i = 0; i < id.size(); i++) {
            // 2.1删除数据库上的音乐
            int musicId = id.get(i);
            Music music = musicMapper.findMusicById(musicId);
            if(music == null) {
                System.out.println("没有这个id的音乐");
                return new ResponseBodyMessage<>(-1, "没有你要删除的音乐", false);
            }
            int ret = musicMapper.deleteMusicById(musicId);
            if(ret == 1) {
                //2.2 删除服务器上的数据
                int index = music.getUrl().lastIndexOf("=");
                String fileName = music.getUrl().substring(index+1);//liu

                File file = new File(SAVE_PATH+"/"+fileName+".mp3");
                if(file.delete()) {
                    //同步检查lovemusic表当中 是否存在这个音乐
                    loveMusicMapper.deleteLoveMusicByMusicId(musicId);
                    sum += ret;
                    //return new ResponseBodyMessage<>(0,"服务器当中的音乐删除成功！",true);
                }else {
                    return new ResponseBodyMessage<>(-1,"服务器当中的音乐删除失败！",false);
                }
            }else {// 不等于1  数据库删除失败
                return new ResponseBodyMessage<>(-1,"数据库当中的音乐删除失败！",false);
            }
        }
        if(sum == id.size()) {
            System.out.println("整体删除成功！");
            return new ResponseBodyMessage<>(0,"音乐删除成功！",true);
        }else {
            System.out.println("整体删除失败！");
            return new ResponseBodyMessage<>(-1,"音乐删除失败！",false);
        }
    }

    /**
     * 查询音乐  1.模糊查询  2.默认返回所有音乐
     * @param musicName
     * @return
     */
    @RequestMapping("/findmusic")
    public ResponseBodyMessage<List<Music>> findMusic(@RequestParam(required = false) String musicName) {

        List<Music> musicList = null;
        if(musicName != null) {
            musicList = musicMapper.findMusicByName(musicName);
        }else {// 不传参数  默认返回所有的音乐
            musicList = musicMapper.findMusic();
        }
        return new ResponseBodyMessage<>(0,"查询到了所有的音乐",musicList);
    }
}
