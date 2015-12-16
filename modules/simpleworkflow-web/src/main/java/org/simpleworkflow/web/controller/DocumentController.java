package org.simpleworkflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.simpleworkflow.domain.Document;
import org.simpleworkflow.model.DocumentDTO;
import org.simpleworkflow.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
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

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public HttpEntity<Resources<DocumentDTO>> findAll() {
    List<Document> documentList = documentService.findAll();
    List<DocumentDTO> documentDTOList = new ArrayList<DocumentDTO>();
    for (Document document : documentList) {
      documentDTOList.add(mapper.map(document, DocumentDTO.class));
    }

    Resources<DocumentDTO> resources = new Resources<DocumentDTO>(documentDTOList);
    resources.add(this.entityLinks.linkToCollectionResource(DocumentDTO.class));
    return new ResponseEntity<Resources<DocumentDTO>>(resources, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public HttpEntity<Resource<DocumentDTO>> findOne(@PathVariable Long id) {
    Document document=documentService.findOne(id);
    DocumentDTO documentDTO = mapper.map(document, DocumentDTO.class);
    Resource<DocumentDTO> resource = new Resource<DocumentDTO>(documentDTO);
    resource.add(this.entityLinks.linkToSingleResource(DocumentDTO.class, id));
    return new ResponseEntity<Resource<DocumentDTO>>(resource, HttpStatus.OK);
  }

}
