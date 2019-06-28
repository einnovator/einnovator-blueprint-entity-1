package org.einnovator.blueprint.entity1.repository;

import java.util.Collection;
import java.util.Optional;

import org.einnovator.blueprint.entity1.model.Status;
import org.einnovator.blueprint.entity1.model.MyEntity;
import org.einnovator.jpa.repository.RepositoryBase2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyEntityRepository extends RepositoryBase2<MyEntity, Long> {

	Page<MyEntity> findAllByNameLike(String q, Pageable pageable);

	Page<MyEntity> findAllByNameLikeAndStatusIn(String q, Collection<Status> statuss, Pageable pageable);

	Page<MyEntity> findAllByNameLikeAndStatusInAndCheck(String q, Collection<Status> statuss, boolean villain, Pageable pageable);

	Optional<MyEntity> findOneByName(String name);

}
