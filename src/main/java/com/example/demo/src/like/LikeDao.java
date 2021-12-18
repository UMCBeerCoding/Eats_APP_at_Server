package com.example.demo.src.like;

import com.example.demo.src.like.model.CheckLike;
import com.example.demo.src.like.model.PostLikeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class LikeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public CheckLike checkLikeIdx(PostLikeReq postLikeReq) {
        String checkLikeIdxQuery = "select favIdx, status from Favorite WHERE userIdx = ? and storeIdx = ?";
        Object[] checkLikeIdxParams = new Object[]{postLikeReq.getUserIdx(), postLikeReq.getStoreIdx()};

        try{
            return this.jdbcTemplate.queryForObject( checkLikeIdxQuery,
                    (rs, row) -> new CheckLike(
                            rs.getInt("favIdx"),
                            rs.getString("status")
                    ),
                    checkLikeIdxParams);
        }
        catch (Exception e){
            return null;
        }
    }

    public CheckLike createLike(PostLikeReq postLikeReq) {
        String createLikeQuery = "insert into Favorite (userIdx, storeIdx) VALUES (?,?)";
        Object[] createLikeParams = new Object[]{postLikeReq.getUserIdx(), postLikeReq.getStoreIdx()};
        this.jdbcTemplate.update(createLikeQuery, createLikeParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        int favIdx = this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
        return new CheckLike(favIdx, "T");
    }

    public CheckLike createActiveLike(int favIdx) {
        String createActiveLikeQuery = "update Favorite set status = 'T' where favIdx = ?";
        int createActiveLikeParams = favIdx;
        this.jdbcTemplate.update(createActiveLikeQuery, createActiveLikeParams);

        return new CheckLike(favIdx, "T");
    }

    public CheckLike createInactiveLike(int favIdx) {
        String createActiveLikeQuery = "update Favorite set status = 'F' where favIdx = ?";
        int createActiveLikeParams = favIdx;
        this.jdbcTemplate.update(createActiveLikeQuery, createActiveLikeParams);

        return new CheckLike(favIdx, "F");
    }
}
