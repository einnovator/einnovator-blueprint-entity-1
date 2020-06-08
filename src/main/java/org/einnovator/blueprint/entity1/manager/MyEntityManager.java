/**
 * 
 */
package org.einnovator.blueprint.entity1.manager;

import org.einnovator.blueprint.entity1.model.MyEntity;
import org.einnovator.blueprint.entity1.modelx.MyEntityFilter;
import org.einnovator.jpa.manager.ManagerBase2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author support@einnovator.org
 *
 */
public interface MyEntityManager extends ManagerBase2<MyEntity, Long> {

	Page<MyEntity> findAll(MyEntityFilter filter, Pageable pageable);

	MyEntity findOneByName(String name);

	void populate(boolean force);

}
