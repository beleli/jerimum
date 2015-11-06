package br.com.jerimum.fw.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.jerimum.fw.dto.AbstractDto;
import br.com.jerimum.fw.entity.AbstractEntity;
import br.com.jerimum.fw.service.CrudService;

/**
 * 
 * @author Dali Freire Dias - dalifreire@gmail.com
 * @since 11/2015
 *
 * @param <DTO>
 * @param <ENTITY>
 */
public abstract class AbstractCrudService<DTO extends AbstractDto, ENTITY extends AbstractEntity<?>> extends AbstractEntityService<ENTITY> implements CrudService<DTO, ENTITY> {

	protected abstract JpaCrudRepository<ENTITY, Long> getRepository();

	@Override
	public DTO getDtoById(Long id) {
		ENTITY entity = getEntityById(id);
		if (entity == null) {
			Class<ENTITY> clazz = getEntityClass();
			throwEmptyResultDataAccessException(clazz.getSimpleName(), id);
		}
		return buildDtoFromEntity(entity);
	}

	@Override
	public DTO insertDto(DTO dto) {
		ENTITY entity = buildEntityFromDto(dto);
		getRepository().save(entity);
		return buildDtoFromEntity(entity);
	}

	@Override
	public DTO updateDto(DTO dto) {
		ENTITY entity = buildEntityFromDto(dto);
		entity = saveEntity(entity);
		return buildDtoFromEntity(entity);
	}

	@Override
	public void deleteDtoById(Long id) {
		deleteEntityById(id);
	}

	@Override
	public Set<DTO> getAllDtos() {
		return buildDtoFromEntity(getAllEntities());
	}

	/**
	 * takes a DTO object and converts it to an entity.
	 * 
	 * @param dto the DTO.
	 * @return a constructed Boundary.
	 */
	protected abstract ENTITY buildEntityFromDto(DTO dto);

	/**
	 * takes an entity and converts it to a DTO object.
	 * 
	 * @param entity the entity.
	 * @return a constructed DTO.
	 */
	protected abstract DTO buildDtoFromEntity(ENTITY entity);

	/**
	 * Converts an entities list into dto list.
	 * 
	 * @param entities The entity list to be converted.
	 * @return {@link List} - The converted entity list.
	 */
	protected List<DTO> buildDtoFromEntity(List<ENTITY> entities) {
		List<DTO> dtoList = new ArrayList<DTO>();
		for (ENTITY entity : entities) {
			dtoList.add(buildDtoFromEntity(entity));
		}
		return dtoList;
	}

	/**
	 * Converts an entities set into dto set.
	 * 
	 * @param entities The entity list to be converted.
	 * @return {@link List} - The converted entity list.
	 */
	protected Set<DTO> buildDtoFromEntity(Set<ENTITY> entities) {
		Set<DTO> dtoSet = new HashSet<DTO>();
		for (ENTITY entity : entities) {
			dtoSet.add(buildDtoFromEntity(entity));
		}
		return dtoSet;
	}
	
	@SuppressWarnings("unchecked")
	private Class<ENTITY> getEntityClass() {
		return (Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

}
