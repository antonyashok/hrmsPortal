package com.tm.common.engagement.resource.assemblers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tm.common.engagement.domain.CntrEngmt;
import com.tm.common.engagement.service.dto.EngagementDTO;
import com.tm.common.engagement.service.dto.TaskDTO;
import com.tm.common.engagement.service.mapper.EngagementMapper;
import com.tm.common.engagement.web.rest.EngagementResource;

@Service
public class EngagementResourceAssembler extends
		ResourceAssemblerSupport<CntrEngmt, EngagementDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EngagementResourceAssembler(final EntityLinks entityLinks,
			final RelProvider relProvider) {
		super(EngagementResource.class, EngagementDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EngagementDTO toResource(CntrEngmt engagement) {
		EngagementDTO resource = instantiateResource(engagement);

		return resource;
	}

	@Override
	protected EngagementDTO instantiateResource(CntrEngmt engagement) {
		return EngagementMapper.INSTANCE.engagementToEngagementDTO(engagement);
	}

	public ResponseEntity<EngagementDTO> getTasks(
			List<EngagementDTO> engagementsList) {
		EngagementDTO engagementDTO = new EngagementDTO();
		Map<String, EngagementDTO> engagementTasks = new HashMap<>();
		engagementDTO.setEngagementId(engagementsList.get(0).getEngagementId());
		engagementDTO.setEngagementName(engagementsList.get(0)
				.getEngagementName());
		engagementDTO.setEngagementStartDate(engagementsList.get(0)
				.getEngagementStartDate());
		engagementDTO.setEngagementEndDate(engagementsList.get(0)
				.getEngagementEndDate());
		engagementDTO.setStartDay(engagementsList.get(0).getStartDay());
		engagementDTO.setEndDay(engagementsList.get(0).getEndDay());
		List<TaskDTO> listTaskDTO = new ArrayList<>();
		engagementsList.forEach(engagement -> {
			listTaskDTO.add(EngagementMapper.INSTANCE
					.engagementToTaskDTO(engagement));
		});
		engagementDTO.setTaskDTO(listTaskDTO);
		engagementTasks.put("engagement", engagementDTO);
		return new ResponseEntity<>(engagementDTO, HttpStatus.OK);
	}
}
