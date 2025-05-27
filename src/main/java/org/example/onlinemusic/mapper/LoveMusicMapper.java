package org.example.onlinemusic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.onlinemusic.model.Music;

import java.util.List;

@Mapper
public interface LoveMusicMapper {
    /**
     * 查询个人的喜欢的音乐
     * @param userId
     * @param musicId
     * @return
     */
    Music findLoveMusicByMusicIdAndUserId(@Param("userId")int userId, @Param("musicId")int musicId);

    /**
     * 个人收藏音乐
     * @param userId
     * @param musicId
     * @return
     */
    boolean insertLoveMusic(@Param("userId") int userId, @Param("musicId") int musicId);


    /**
     * 查询某个用户收藏过的所有的音乐
     * @param userId
     * @return
     */
    List<Music> findLoveMusicByUserId(int userId);


    /**
     * 查询当前用户，指定为musicName的音乐，支持模糊查询
     * @param musicName
     * @param userId
     * @return
     */
    List<Music> findLoveMusicBykeyAndUID(@Param("musicName") String musicName,@Param("userId") int userId);

    /**
     * 移除某个用户喜欢的音乐
     * @param userId 用户的ID
     * @param musicId 音乐的ID
     * @return 受影响的行数
     */
    int deleteLoveMusic(@Param("userId")int userId,@Param("musicId") int musicId);

    /**
     * 根据音乐的ID 进行删除
     * @param musicId 音乐的ID
     * @return
     */
    int deleteLoveMusicByMusicId(@Param("musicId") int musicId);
}
