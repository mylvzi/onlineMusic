package org.example.onlinemusic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.onlinemusic.model.Music;

import java.util.List;

@Mapper
public interface MusicMapper {
    /**
     * 插入音乐
     */
    int insert(@Param("title") String title,
               @Param("singer") String singer,
               @Param("time") String time,
               @Param("url") String url,
               @Param("userid") int userid);

    /**
     * 根据歌曲名和歌手名查询是否存在
     * @param title 歌曲名
     * @param singer 歌手名
     * @return 存在数量（>0 表示存在）
     */
    int existsByTitleAndSinger(@Param("title") String title, @Param("singer") String singer);

    /**
     * 查询当前id的音乐是否存在
     * @param id
     * @return
     */
    Music findMusicById(int id);

    /**
     * 删除当前iD的音乐
     * @param musicId
     * @return 1表示删除成功  0表示删除失败
     */
    int deleteMusicById(int musicId);

    /**
     * 查询所有的音乐  不含参数返回所有音乐
     * @return
     */
    List<Music> findMusic();

    /**
     * 查询指定的音乐  根据名称查询某个音乐
     * @return
     */
    List<Music> findMusicByName(String musicName);
}
