package org.einnovator.blueprint.entity1.web;

import java.net.URI;
import java.security.Principal;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.einnovator.blueprint.entity1.manager.MyEntityManager;
import org.einnovator.blueprint.entity1.model.MyEntity;
import org.einnovator.blueprint.entity1.modelx.MyEntityFilter;
import org.einnovator.util.PageOptions;
import org.einnovator.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

@RestController
@RequestMapping("/api/entity")
public class MyEntityRestController extends ControllerBase {

	@Autowired
	private MyEntityManager manager;


	@GetMapping
	public ResponseEntity<Page<MyEntity>> listEntities(PageOptions options, MyEntityFilter filter, 
			Principal principal, HttpServletResponse response) {
		if (!isAllowed(principal, false)) {
			return forbidden("listEntities", response);
		}
		Page<MyEntity> page = manager.findAll(filter, options.toPageRequest());
		return ok(page, "listEntities", response,  PageUtil.toString(page), filter, options);
	}

	@PostMapping
	public ResponseEntity<Void> createSuperhero(@RequestBody MyEntity entity, BindingResult errors,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		if (!isAllowedCreate(principal, entity)) {
			return forbidden("createSuperhero", response);
		}

		if (errors.hasErrors()) {
			String err = errors.getAllErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(","));
			return badrequest("createSuperhero", response, err);
		}

		MyEntity entity2 = manager.create(entity, false);
		if (entity2 == null) {
			return badrequest("createSuperhero", response);
		}
		URI location = new UriTemplate(request.getRequestURI() + "/{id}").expand(entity2.getUuid());
		return created(location, "createSuperhero", response);
	}


	@GetMapping("/{id:.*}")
	public ResponseEntity<MyEntity> getSuperhero(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		MyEntity entity = manager.find(id);
		if (entity == null) {
			return notfound("getSuperhero", response);
		}
		if (!isAllowedView(principal, entity)) {
			return forbidden("getSuperhero", response);
		}
		return ok(entity, "getSuperhero", response);
	}

	@PutMapping("/{_id:.*}")
	public ResponseEntity<Void> updateSuperhero(@PathVariable("_id") String id, @RequestBody MyEntity entity,
		Principal principal, HttpServletResponse response) {		
		MyEntity entity0 = manager.find(id);
		if (entity0 == null) {
			return notfound("updateSuperhero", response);
		}
		entity.setUuid(id);
		if (!isAllowedManage(principal, entity)) {
			return forbidden("updateSuperhero", response);
		}
		MyEntity entity2 = manager.update(entity, true, false);
		if (entity2 == null) {
			return badrequest("updateSuperhero", response, id);
		}
		return nocontent("updateSuperhero", response);
	}


	@DeleteMapping("/{id:.*}")
	public ResponseEntity<Void> deleteSuperhero(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		MyEntity entity = manager.find(id);
		if (entity == null) {
			return notfound("deleteSuperhero", response);
		}
		if (!isAllowedManage(principal, entity)) {
			return forbidden("deleteSuperhero", response);
		}
		MyEntity entity2 = manager.delete(entity);
		if (entity2 == null) {
			return badrequest("deleteSuperhero", response, id);
		}
		return nocontent("deleteSuperhero", response);
	}

}
