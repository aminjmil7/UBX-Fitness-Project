package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Media;
import com.mycompany.myapp.repository.MediaRepository;
import com.mycompany.myapp.service.MediaService;
import com.mycompany.myapp.service.dto.MediaDTO;
import com.mycompany.myapp.service.mapper.MediaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Media}.
 */
@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    private final Logger log = LoggerFactory.getLogger(MediaServiceImpl.class);

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    public MediaServiceImpl(MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    @Override
    public MediaDTO save(MediaDTO mediaDTO) {
        log.debug("Request to save Media : {}", mediaDTO);
        Media media = mediaMapper.toEntity(mediaDTO);
        media = mediaRepository.save(media);
        return mediaMapper.toDto(media);
    }

    @Override
    public Optional<MediaDTO> partialUpdate(MediaDTO mediaDTO) {
        log.debug("Request to partially update Media : {}", mediaDTO);

        return mediaRepository
            .findById(mediaDTO.getId())
            .map(
                existingMedia -> {
                    mediaMapper.partialUpdate(existingMedia, mediaDTO);
                    return existingMedia;
                }
            )
            .map(mediaRepository::save)
            .map(mediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MediaDTO> findAll() {
        log.debug("Request to get all Media");
        return mediaRepository.findAll().stream().map(mediaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MediaDTO> findOne(Long id) {
        log.debug("Request to get Media : {}", id);
        return mediaRepository.findById(id).map(mediaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Media : {}", id);
        mediaRepository.deleteById(id);
    }
}
