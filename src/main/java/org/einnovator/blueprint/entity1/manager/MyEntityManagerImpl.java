/**
 * 
 */
package org.einnovator.blueprint.entity1.manager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.blueprint.entity1.model.Status;
import org.einnovator.blueprint.entity1.model.MyEntity;
import org.einnovator.blueprint.entity1.modelx.MyEntityFilter;
import org.einnovator.blueprint.entity1.repository.MyEntityRepository;
import org.einnovator.common.config.AppConfiguration;
import org.einnovator.common.config.UIConfiguration;
import org.einnovator.jpa.manager.ManagerBaseImpl3;
import org.einnovator.jpa.repository.RepositoryBase2;
import org.einnovator.social.client.manager.ChannelManager;
import org.einnovator.social.client.model.Channel;
import org.einnovator.util.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class MyEntityManagerImpl extends ManagerBaseImpl3<MyEntity> implements MyEntityManager {

	private final Log logger = LogFactory.getLog(getClass());

	public static final String SUPERHEROS_RESOURCE_JSON = "data/entities.json";

	@Autowired
	private MyEntityRepository repository;

	@Autowired
	private ChannelManager channelManager;

	@Autowired
	private UIConfiguration ui;

	@Autowired
	private AppConfiguration app;

	@Override
	protected RepositoryBase2<MyEntity, Long> getRepository() {
		return repository;
	}
	
	@Override
	public MyEntity find(MyEntity entity) {
		MyEntity entity0 = super.find(entity);
		if (entity0!=null) {
			return entity0;
		}
		if (entity.getName()!=null) {
			entity0 = findOneByName(entity.getName());			
			if (entity0!=null) {
				return entity0;
			}
		}
		return entity0;
	}
	
	@Override
	public MyEntity findOneByName(String name) {
		Optional<MyEntity> entity = repository.findOneByName(name);
		return entity.isPresent() ? processAfterLoad(entity.get(), null) : null;
	}

	@Override
	public Page<MyEntity> findAll(MyEntityFilter filter, Pageable pageable) {
		populate();
		Page<MyEntity> page = null;
		if (filter!=null) {
			if (filter.getStatus()!=null || filter.getCheck()!=null) {
				String q = filter.getQ()!=null ?  "%" + filter.getQ().trim() + "%" : "%";
				Collection<Status> statuss = filter.getStatus()!=null ? Collections.singleton(filter.getStatus()) : Arrays.asList(Status.values());
				if (filter.getCheck()!=null) {
					page = repository.findAllByNameLikeAndStatusInAndCheck(q, statuss, filter.getCheck(), pageable);
				} else {
					page = repository.findAllByNameLikeAndStatusIn(q, statuss, pageable);
				}
			} else if (filter.getQ()!=null){
				String q = "%" + filter.getQ().trim() + "%";
				page = repository.findAllByNameLike(q, pageable);					
			}
		}
		if (page==null) {
			page = repository.findAll(pageable);
		}
		return processAfterLoad(page, null);
	}

	@Override
	public void processAfterPersistence(MyEntity entity) {
		super.processAfterPersistence(entity);
		Channel channel = entity.makeChannel(getBaseUri());
		channel = channelManager.createOrUpdateChannel(channel, null);
		if (channel!=null && entity.getChannelId()==null) {
			entity.setChannelId(channel.getUuid());
			repository.save(entity);			
		}
	}
	

	public String getBaseUri() {
		return ui.getLink(app.getId());
	}
	
	private boolean init;

	public void populate() {
		populate(true);
	}
	
	@Override
	public void populate(boolean force) {
		if (force || !init) {
			init = true;
			if (!force && repository.count()!=0) {
				return;
			}
			logger.info("populate: ");
			MyEntity[] entities = MappingUtils.readJson(new ClassPathResource(SUPERHEROS_RESOURCE_JSON), MyEntity[].class);
			createOrUpdate(Arrays.asList(entities));
		}
		
	}

	
}
