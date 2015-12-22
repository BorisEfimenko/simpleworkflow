package org.simpleworkflow.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.simpleworkflow.domain.Document;
import org.simpleworkflow.model.DocumentDTO;
import org.simpleworkflow.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/documents")
@ExposesResourceFor(DocumentDTO.class)
public class DocumentController {

  @Autowired
  private Mapper mapper;
  @Autowired
  private DocumentService documentService;
  @Autowired
  private EntityLinks entityLinks;

  @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
  public HttpEntity<Resources<Resource<DocumentDTO>>> findAll() {
    List<Document> documentList = documentService.findAll();
    List<Resource<DocumentDTO>> documentResourceList = new ArrayList<Resource<DocumentDTO>>();
    for (Document document : documentList) {
      //documentDTOList.add(mapper.map(document, DocumentDTO.class));
      DocumentDTO documentDTO=mapper.map(document, DocumentDTO.class);
      documentResourceList.add(getDocumentResource(documentDTO));
    }

    Resources<Resource<DocumentDTO>> resources = new Resources<Resource<DocumentDTO>>(documentResourceList);
    resources.add(linkTo(methodOn(this.getClass()).findAll()).withSelfRel());
    //resources.add(entityLinks.linkToCollectionResource(DocumentDTO.class).withRel("documents"));

    return new ResponseEntity<Resources<Resource<DocumentDTO>>>(resources, HttpStatus.OK);
  }


  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
  public HttpEntity<Resource<DocumentDTO>> findOne(@PathVariable Long id) {
    Document document=documentService.findOne(id);
    DocumentDTO documentDTO = mapper.map(document, DocumentDTO.class);
    Resource<DocumentDTO> resource =getDocumentResource(documentDTO);
    return new ResponseEntity<Resource<DocumentDTO>>(resource, HttpStatus.OK);
  }

  private Resource<DocumentDTO> getDocumentResource(DocumentDTO documentDTO) {
    Resource<DocumentDTO> resource = new Resource<DocumentDTO>(documentDTO);    
    //resource.add(linkTo(methodOn(DocumentController.class).findOne(documentDTO.getId())).withSelfRel());
    resource.add(this.entityLinks.linkToSingleResource(DocumentDTO.class, documentDTO.getId()));
    return resource;
  }
}
