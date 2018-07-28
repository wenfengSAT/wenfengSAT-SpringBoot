package com.xuwc.simpo.respository;

import com.xuwc.simpo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**  
 * 用户Repository
 */
@Repository
public interface UserRepository extends JpaSpecificationExecutor<User>,JpaRepository<User,Long> {

    /**
     *  查询用户信息
     * @param id
     * @return
     */
    @Query(nativeQuery = true,value = " SELECT * FROM  sys_user WHERE id = ?1")
    User getUserInfo(String id);
    //按照用户名查询
    List<User> findByUserName(String userName);
    //按照性别查询
    List<User> findBySex(String sex);
    //测试用户名和性别查询
    List<User> findByUserNameAndSex(String userName,String sex);
}
