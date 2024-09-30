package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long>,
    UploadFileCutomRepository {

    default UploadFile findByIdOrElseThorw(Long id) {
        return findById(id).orElseThrow(() -> new InvalidRequestException("존재하지 않는 파일입니다."));
    }
}
