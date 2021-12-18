package com.example.demo.src.store;


import com.example.demo.src.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */
public class StoreDao {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ******************************************************************************

    /**
     * DAO관련 함수코드의 전반부는 크게 String ~~~Query와 Object[] ~~~~Params, jdbcTemplate함수로 구성되어 있습니다.(보통은 동적 쿼리문이지만, 동적쿼리가 아닐 경우, Params부분은 없어도 됩니다.)
     * Query부분은 DB에 SQL요청을 할 쿼리문을 의미하는데, 대부분의 경우 동적 쿼리(실행할 때 값이 주입되어야 하는 쿼리) 형태입니다.
     * 그래서 Query의 동적 쿼리에 입력되어야 할 값들이 필요한데 그것이 Params부분입니다.
     * Params부분은 클라이언트의 요청에서 제공하는 정보(~~~~Req.java에 있는 정보)로 부터 getXXX를 통해 값을 가져옵니다. ex) getEmail -> email값을 가져옵니다.
     *      Notice! get과 get의 대상은 카멜케이스로 작성됩니다. ex) item -> getItem, password -> getPassword, email -> getEmail, userIdx -> getUserIdx
     * 그 다음 GET, POST, PATCH 메소드에 따라 jabcTemplate의 적절한 함수(queryForObject, query, update)를 실행시킵니다(DB요청이 일어납니다.).
     *      Notice!
     *      POST, PATCH의 경우 jdbcTemplate.update
     *      GET은 대상이 하나일 경우 jdbcTemplate.queryForObject, 대상이 복수일 경우, jdbcTemplate.query 함수를 사용합니다.
     * jdbcTeplate이 실행시킬 때 Query 부분과 Params 부분은 대응(값을 주입)시켜서 DB에 요청합니다.
     * <p>
     * 정리하자면 < 동적 쿼리문 설정(Query) -> 주입될 값 설정(Params) -> jdbcTemplate함수(Query, Params)를 통해 Query, Params를 대응시켜 DB에 요청 > 입니다.
     * <p>
     * <p>
     * DAO관련 함수코드의 후반부는 전반부 코드를 실행시킨 후 어떤 결과값을 반환(return)할 것인지를 결정합니다.
     * 어떠한 값을 반환할 것인지 정의한 후, return문에 전달하면 됩니다.
     * ex) return this.jdbcTemplate.query( ~~~~ ) -> ~~~~쿼리문을 통해 얻은 결과를 반환합니다.
     */

    /**
     * 참고 링크
     * https://jaehoney.tistory.com/34 -> JdbcTemplate 관련 함수에 대한 설명
     * https://velog.io/@seculoper235/RowMapper%EC%97%90-%EB%8C%80%ED%95%B4 -> RowMapper에 대한 설명
     */

    // 해당 userIdx를 갖는 유저조회
    public List<GetStoreCatRes> getStoreCat() {
        String getUserQuery = "select * from Category"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문

        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new GetStoreCatRes(
                        rs.getInt("catIdx"),
                        rs.getString("catName"),
                        rs.getString("catImage"))
        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 골라먹는 맛집 조회
    public List<GetGolaRes> getStoreGola() {
        String getUserQuery = "select Store.storeIdx, storeName, truncate((select avg(Review.rating) from Review where Review.storeIdx=Store.storeIdx), 1) as rating,\n" +
                "       (select count(reviewIdx) from Review where Store.storeIdx=Review.storeIdx) as reviewNum,\n" +
                "        case when (deliveryPrice=0)\n" +
                "            then '무료배달'\n" +
                "            else concat('배달비 ', format(deliveryPrice,0), '원') end as deliveryTip,\n" +
                "       deliveryTime\n" +
                "from Store;";

        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new GetGolaRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getFloat("rating"),
                        rs.getInt("reviewNum"),
                        "2.0km",
                        rs.getString("deliveryTip"),
                        rs.getString("deliveryTime"),
                        getStoreImages(rs.getInt("storeIdx")))
        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 가게의 사진 가져오기
    public List<String> getStoreImages(int storeIdx) {
        String getUserQuery = "select image\n" +
                "from StoreImg\n" +
                "where storeIdx=?;"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문

        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new String(
                        rs.getString("image")), storeIdx
        );
    }

}
