package org.simpleworkflow.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.transaction.Transactional;

import org.simpleworkflow.domain.AbstractEntity;
import org.simpleworkflow.exception.NotFoundException;
import org.simpleworkflow.repository.support.JpaCrudRepository;
import org.simpleworkflow.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

@Transactional
public class CrudServiceImpl<T extends AbstractEntity, R extends JpaCrudRepository<T, Long>>
		implements CrudService<T, R> {
	
	protected R repository;
	@Override
	public void setRepository(R repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public T create(T resource) {
		Assert.notNull(resource, "Resource can't be null");
		return repository.save(resource);
	}

	@Override
	@Transactional
	public T update(T resource) {
		Assert.notNull(resource, "Resource can't be null");
		T retrievedResource = this.findById(resource.getId());
		if (retrievedResource == null) {
			throw new NotFoundException();
		}

		return repository.save(resource);
	}

	@Override
	@Transactional
	public T save(T resource) {
		Assert.notNull(resource, "Resource can't be null");
		return repository.save(resource);
	}

	@Override
	@Transactional
	public void delete(T resource) {
		Assert.notNull(resource, "Resource can't be null");
		repository.delete(resource);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		Assert.notNull(id, "Resource Long can't be null");
		T resource = repository.findOne(id);
		if (resource == null) {
            throw new NotFoundException();
        }
		repository.delete(resource);
	}

	@Override
	public T findById(Long id) {
		Assert.notNull(id, "Resource Long can't be null");
		T entity = repository.findOne(id);
		if (entity == null) {
            throw new NotFoundException();
        }

        return entity;
	}

	@Override
	public Iterable<T> findByIds(Set<Long> ids) {
		Assert.notNull(ids, "Resource ids can't be null");
		return repository.findAll(ids);
	}

	@Override
	public Iterable<T> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<T> findAll(Pageable pageRequest) {
		Assert.notNull(pageRequest, "page request can't be null");
		return repository.findAll(pageRequest);
	}

	@Override
	public Page<T> findPaginated(Integer pageNumber, Integer pageSize, String direction, String properties) {
		Integer iPage = (pageNumber == null) ? 1 : pageNumber;
		Integer iSize = (pageSize == null) ? 10 : pageSize;
		Assert.isTrue(pageNumber > 0, "Page index must be greater than 0");
		Assert.isTrue(
				direction.isEmpty() || direction.equalsIgnoreCase(Sort.Direction.ASC.toString())
						|| direction.equalsIgnoreCase(Sort.Direction.DESC.toString()),
				"Direction should be ASC or DESC");
		if (direction.isEmpty()) {
			return this.repository.findAll(new PageRequest(iPage - 1, iSize));
		} else {
			Assert.notNull(properties);
			return this.repository.findAll(new PageRequest(iPage - 1, iSize,
					new Sort(Sort.Direction.fromString(direction.toUpperCase(Locale.ENGLISH)), properties.split(","))));
		}
	}

	@Override
	public Long count() {
		return repository.count();
	}

  @Override
  public T findOne(T example) {
    Specification<T> spec=getSpecByExample(example);     
    return repository.findOne(spec);
  }

  @Override
  public List<T> findAll(T example) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<T> findAll(T example, Pageable pageable) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<T> findAll(T example, Sort sort) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long count(T example) {
    // TODO Auto-generated method stub
    return 0;
  }

  private Specification<T> getSpecByExample(T example) {
    // TODO Auto-generated method stub

    return null;
  }

}