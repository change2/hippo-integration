package com.change.hippo.admin.repository;

import java.io.Serializable;

/**
 * 
* @ClassName: org.sdn.authenticate.biz.repository.IdGeneratorRepository
* @Description: 主键生成器
* @author tianfl 
* @date 2017年9月25日 上午10:03:27  
* @version V1.0 
* @param <ID>
 */
public interface IdGeneratorRepository<ID extends Serializable> {

	public ID generate();
}
