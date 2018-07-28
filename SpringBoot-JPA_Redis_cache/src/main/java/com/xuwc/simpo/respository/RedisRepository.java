package com.xuwc.simpo.respository;

import com.xuwc.simpo.entity.Redis;
import com.xuwc.simpo.entity.Thread;
import com.xuwc.simpo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**  
 * Redis Repository
 */
@Repository
public interface RedisRepository extends JpaSpecificationExecutor<Redis>,JpaRepository<Redis,Long> {

}
