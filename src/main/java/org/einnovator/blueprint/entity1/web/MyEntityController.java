package org.einnovator.blueprint.entity1.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.blueprint.entity1.manager.MyEntityManager;
import org.einnovator.blueprint.entity1.model.MyEntity;
import org.einnovator.blueprint.entity1.modelx.MyEntityFilter;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.PageOptions;
import org.einnovator.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/entity")
public class MyEntityController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MyEntityManager manager;

	@GetMapping
	public String list(@ModelAttribute("filter") MyEntityFilter filter, PageOptions options,  @RequestParam(required=false) Boolean async,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		Page<MyEntity> page = manager.findAll(filter, options.toPageRequest());
		model.addAttribute("entities", page);
		model.addAttribute("page", page);
		model.addAttribute("pageJson", PageUtil.toJson(page, false));

		logger.info("list: " + PageUtil.toString(page) + " " + filter + " " + options);
		return Boolean.TRUE.equals(async) ? "entity/entity-table" : "entity/list";

	}

	@GetMapping("/{id:.*}")
	public String show(@PathVariable("id") String id, String slug,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		
		MyEntity entity = manager.find(id);

		if (entity == null) {
			flashNotfound("show", request, redirectAttributes);
			return redirect("/entity");
		}
		if (!isAllowedView(principal, entity)) {
			flashForbidden("show", request, redirectAttributes);
			return redirect("/entity");
		}
		model.addAttribute("entity", entity);
		model.addAttribute("entityJson", MappingUtils.toJson(entity));


		logger.info("show: " + entity);
		return "entity/show";
	}

	@GetMapping("/create")
	public String createGET(@ModelAttribute("entity") MyEntity entity,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		
		logger.info("create: " + entity);
		addCommonToModel(principal, model);
		return "entity/edit";
	}

	@PostMapping
	public String createPOST(@ModelAttribute("entity") MyEntity entity, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		if (!isAllowedCreate(principal, entity)) {
			flashForbidden("createPOST", request, redirectAttributes);
			return redirect("/entity");
		}
		if (errors.hasErrors()) {
			addCommonToModel(principal, model);
			flashFailure("createPOST", request, redirectAttributes, errors);
			return "entity/edit";
		}
		MyEntity entity2 = manager.create(entity, true);
		if (entity2 == null) {
			logger.error("createPOST: " + entity);
			flashFailure("createPOST", request, redirectAttributes, entity);
			return "";
		}
		logger.info("createPOST: " + entity2);
		model.addAttribute("entity", entity2);
		return redirect("/entity/" + entity2.getUuid());
	}

	@GetMapping("/{id:.*}/edit")
	public String editGet(@PathVariable("id") String id,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {

		MyEntity entity = manager.find(id);
		if (entity == null) {
			flashNotfound("editGet", request, redirectAttributes);
			return redirect("/entity");
		}
		if (!isAllowedManage(principal, entity)) {
			flashForbidden("show", request, redirectAttributes);
			return redirect("/entity");
		}
		
		logger.info("editGet: " + entity);
		model.addAttribute("entity", entity);
		return "entity/edit";
	}

	@PutMapping("/{id_:.*}")
	public String editPut(@PathVariable("id_") String id_, @ModelAttribute("entity") MyEntity entity, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		MyEntity entity0 = manager.find(id_);
		if (entity0 == null) {
			flashNotfound("editPut", request, redirectAttributes);
			return redirect("/entity");
		}
		if (!isAllowedManage(principal, entity0)) {
			flashForbidden("editPut", request, redirectAttributes);
			return redirect("/entity");
		}
		entity.setId(entity0.getId());
		MyEntity entity2 = manager.update(entity, true, true);
		if (entity2 == null) {
			logger.error("editPut:  " + HttpStatus.BAD_REQUEST.getReasonPhrase());
			return redirect("/entity");
		}
		flashSuccess("editPut", request, redirectAttributes, entity);
		model.addAttribute("entity", entity2);
		return redirect("/entity/" + entity.getUuid());
	}

	@DeleteMapping("/{id:.*}")
	public String delete(@PathVariable String id, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		MyEntity entity = manager.find(id);
		if (entity == null) {
			flashNotfound("delete", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedManage(principal, entity)) {
			flashForbidden("delete", request, redirectAttributes);
			return redirect("/");
		}

		MyEntity entity2 = manager.delete(entity, false);
		if (entity2 == null) {
			flashFailure("delete", request, redirectAttributes, id);
			return redirect("/entity/" + entity.getUuid());
		}
		flashSuccess("delete", request, redirectAttributes, entity);
		return redirect("/entity");
	}


	protected void addCommonToModel(Principal principal, Model model) {
	}

}
